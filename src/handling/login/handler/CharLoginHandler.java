package handling.login.handler;

import client.ClientRedirector;
import client.LoginCrypto;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleJob;
import client.PartTimeJob;
import client.Skill;
import client.SkillEntry;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import constants.JobConstants;
import constants.ServerConfig;
import constants.ServerConstants;
import constants.WorldConstants;
import database.DatabaseConnection;
import handling.channel.ChannelServer;
import handling.login.LoginInformationProvider;
import handling.login.LoginInformationProvider.JobType;
import handling.login.LoginServer;
import handling.login.LoginWorker;
import handling.world.World;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import server.MapleItemInformationProvider;
import server.quest.MapleQuest;
import tools.FileoutputUtil;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.LoginPacket;
import tools.packet.PacketHelper;

public class CharLoginHandler {

    private static boolean loginFailCount(final MapleClient c) {
        c.loginAttempt++;
        return c.loginAttempt > 4;
    }

    public static void handleAuthRequest(final LittleEndianAccessor slea, final MapleClient c) {
        //System.out.println("Sending response to client.");
        int request = slea.readInt();
        int response;

        response = ((request >> 5) << 5) + (((((request & 0x1F) >> 3) ^ 2) << 3) + (7 - (request & 7)));
        response |= ((request >> 7) << 7);
        response -= 1; //-1 again on v143

        c.getSession().write(LoginPacket.sendAuthResponse(response));
    }

    public static final void login(final LittleEndianAccessor slea, final MapleClient c) {
        slea.skip(22);
        String login = slea.readMapleAsciiString()/*.replace("NP12:auth06:5:0:","")*/;
        String pwd = slea.readMapleAsciiString();

        final boolean ipBan = c.hasBannedIP();
        final boolean macBan = c.hasBannedMac();

        int loginok = c.login(login, pwd, ipBan || macBan);
        final Calendar tempbannedTill = c.getTempBanCalendar();
        String errorInfo = null;

        if (loginok == 0 && (ipBan || macBan) && !c.isGM()) {
            //被封鎖IP或MAC的非GM角色成功登入處理
            loginok = 3;
            if (macBan) {
                // this is only an ipban o.O" - maybe we should refactor this a bit so it's more readable
                MapleCharacter.ban(c.getSession().getRemoteAddress().toString().split(":")[0], "Enforcing account ban, account " + login, false, 4, false);
            }
        } else if (loginok == 0 && (c.getGender() == 10 || c.getSecondPassword() == null)) {
            //選擇性别並設置第二組密碼
//            c.updateLoginState(MapleClient.CHOOSE_GENDER, c.getSessionIPAddress());
            c.getSession().write(LoginPacket.genderNeeded());
            return;
        } else if (loginok == 5) {
            //账号不存在
            if (ServerConfig.isAutoRegister()) {
                if (AutoRegister.createAccount(login, pwd, c.getSession().getRemoteAddress().toString())) {
                    errorInfo = "註冊賬號成功。\r\n請重新輸入賬號密碼進入遊戲。";
                } else {
                    errorInfo = "註冊賬號失敗。";
                }
            } else {
                errorInfo = "賬號註冊失敗，未開啟自動註冊功能，請到網站註冊賬號。";
            }
            loginok = 1;
        }

        if (loginok != 0) {
            if (!loginFailCount(c)) {
                c.clearInformation();
                c.getSession().write(LoginPacket.getLoginFailed(loginok));
                if (errorInfo != null) {
                    c.getSession().write(CWvsContext.broadcastMsg(1, errorInfo));
                }
            } else {
                c.getSession().close(true);
            }
        } else if (tempbannedTill.getTimeInMillis() != 0) {
            if (!loginFailCount(c)) {
                c.clearInformation();
                c.getSession().write(LoginPacket.getTempBan(PacketHelper.getTime(tempbannedTill.getTimeInMillis()), c.getBanReason()));
            } else {
                c.getSession().close(true);
            }
        } else {
            c.loginAttempt = 0;
            LoginWorker.registerClient(c);
        }
    }

    public static void ServerListRequest(final MapleClient c) {
        List<Triple<String, Integer, Boolean>> backgrounds = new LinkedList<>(); //boolean for randomize
        backgrounds.addAll(Arrays.asList(ServerConstants.backgrounds));
        c.getSession().write(ServerConstants.MAPLE_TYPE == ServerConstants.MapleType.GLOBAL ? CField.spawnFlags(null) : LoginPacket.changeBackground(backgrounds));
        for (WorldConstants.Option servers : WorldConstants.values()) {
            if (servers.show()) {
                c.getSession().write(LoginPacket.getServerList(servers));
                if (ServerConstants.MAPLE_TYPE == ServerConstants.MapleType.GLOBAL) {
                    c.getSession().write(LoginPacket.getWorldSelected(c));
                }
            }
        }
        c.getSession().write(LoginPacket.getEndOfServerList());
        boolean hasCharacters = false;
        for (int world = 0; world < WorldConstants.values().length; world++) {
            final List<MapleCharacter> chars = c.loadCharacters(world);
            if (chars != null) {
                hasCharacters = true;
                break;
            }
        }
        if (!hasCharacters) {
            c.getSession().write(LoginPacket.enableRecommended(WorldConstants.recommended));
        }
        if (WorldConstants.recommended >= 0) {
            c.getSession().write(LoginPacket.sendRecommended(WorldConstants.recommended, WorldConstants.recommendedmsg));
        }
    }

    public static void ServerStatusRequest(final MapleClient c) {
        // 0 = Select world normally
        // 1 = "Since there are many users, you may encounter some..."
        // 2 = "The concurrent users in this world have reached the max"
        final int numPlayer = LoginServer.getUsersOn();
        final int userLimit = LoginServer.getUserLimit();
        if (numPlayer >= userLimit) {
            c.getSession().write(LoginPacket.getServerStatus(2));
        } else if (numPlayer * 2 >= userLimit) {
            c.getSession().write(LoginPacket.getServerStatus(1));
        } else {
            c.getSession().write(LoginPacket.getServerStatus(0));
        }
    }

    public static void CharlistRequest(final LittleEndianAccessor slea, final MapleClient c) {
        if (!c.isLoggedIn()) {
            c.getSession().close(true);
            return;
        }
        final int mode = slea.readByte(); //2?
        final int server;
        final int channel;
        if (mode == 0) {
            server = slea.readByte();
            channel = slea.readByte() + 1;
        } else {
            slea.skip(1);
            String code = slea.readMapleAsciiString();
            Map<String, ClientRedirector> redirectors = World.Redirector.getRedirectors();
            ClientRedirector redirector;
            if (!redirectors.containsKey(code) || !redirectors.get(code).isLogined()) {
                if (!redirectors.get(code).isLogined()) {
                    redirectors.remove(code);
                }
                c.getSession().close(true);
                return;
            } else {
                redirector = redirectors.remove(code);
            }
            server = redirector.getWorld();
            channel = redirector.getChannel();
        }
        if (!World.isChannelAvailable(channel, server) || !WorldConstants.isExists(server)) {
            c.getSession().write(LoginPacket.getLoginFailed(10)); //cannot process so many
            return;
        }

        if (!WorldConstants.getById(server).isAvailable() && !(c.isGM() && server == WorldConstants.gmserver)) {
            c.getSession().write(CWvsContext.broadcastMsg(1, "這個伺服器暫時無法連接. \r\n請嘗試連接其他伺服器."));
            c.getSession().write(LoginPacket.getLoginFailed(1)); //Shows no message, but it is used to unstuck
            return;
        }

        System.out.println("用戶端地址: " + c.getSession().getRemoteAddress().toString().split(":")[0] + " 連接到伺服器: " + server + " 頻道: " + channel + "");
        final List<MapleCharacter> chars = c.loadCharacters(server);
        if (chars != null && ChannelServer.getInstance(channel) != null) {
            c.setWorld(server);
            c.setChannel(channel);
            if (ServerConstants.MAPLE_TYPE == ServerConstants.MapleType.GLOBAL || mode == 1) {
                c.getSession().write(LoginPacket.getSecondAuthSuccess(c));
                c.getSession().write(LoginPacket.getChannelSelected());
            }
            c.getSession().write(LoginPacket.getCharList(c.getSecondPassword(), c.getCharacterPos(), chars, c.getCharacterSlots()));
        } else {
            c.getSession().close(true);
        }
    }

    public static void changeCharPosition(final LittleEndianAccessor slea, final MapleClient c) {
        slea.readInt();
        slea.readByte();
        int count = slea.readInt();

        if (count != c.getCharacterPos().size()) {
            System.out.println("角色位置更變出錯: 更變個數與實際不符");
        }

        final ArrayList<Integer> newCharPos = new ArrayList();

        for (int i = 0; i < count; i++) {
            int pos = slea.readInt();
            if (c.getCharacterPos().contains(pos)) {
                newCharPos.add(pos);
            } else {
                System.out.println("角色位置更變出錯: 非本賬號的角色ID");
                return;
            }
        }
        c.updateCharacterPos(newCharPos);
    }

    public static void updateCCards(final LittleEndianAccessor slea, final MapleClient c) {
        if (slea.available() != 36 || !c.isLoggedIn()) {
            c.getSession().close(true);
            return;
        }
        final Map<Integer, Integer> cids = new LinkedHashMap<>();
        for (int i = 1; i <= 9; i++) {
            final int charId = slea.readInt();
            if ((!c.login_Auth(charId) && charId != 0) || ChannelServer.getInstance(c.getChannel()) == null || !WorldConstants.isExists(c.getWorld())) {
                c.getSession().close(true);
                return;
            }
            cids.put(i, charId);
        }
        c.updateCharacterCards(cids);
    }

    public static void CheckCharName(final String name, final MapleClient c) {
        LoginInformationProvider li = LoginInformationProvider.getInstance();
        boolean nameUsed = true;
        if (MapleCharacterUtil.canCreateChar(name, c.isGM())) {
            nameUsed = false;
        }
        if (li.isForbiddenName(name) && !c.isGM()) {
            nameUsed = false;
        }
        c.getSession().write(LoginPacket.charNameResponse(name, nameUsed));
    }

    public static void CreateChar2Pw(final LittleEndianAccessor slea, final MapleClient c) {
        final String Secondpw_Client = slea.readMapleAsciiString();

        if (!c.isLoggedIn() || loginFailCount(c) || c.getSecondPassword() == null) {
            c.getSession().close(true);
            return;
        }
        byte state = 0;

        if (!c.CheckSecondPassword(Secondpw_Client)) { // Wrong Password
            state = 20;
        }

        c.getSession().write(LoginPacket.createCharResponse(state));
        // 驗證碼
//        c.getSession().write(LoginPacket.createCharCheckCode(CheckCodeImageCreator.createCheckCode().getRight(), (byte) 0, (byte) 1, (byte) 1, (byte) 0));
    }

    public static void CreateChar(final LittleEndianAccessor slea, final MapleClient c) {
        String name;
        byte gender, skin, unk;
        short subcategory;
        int face, hair, hairColor = -1, hat = -1, top, bottom = -1, glove = -1, shoes, weapon, cape = -1, faceMark = -1, ears = -1, tail = -1, shield = -1;
        JobType job;
        name = slea.readMapleAsciiString();
        if (!MapleCharacterUtil.canCreateChar(name, false)) {
            System.out.println("非法創建角色名: " + name);
            return;
        }
        int keymapType = slea.readInt(); //按鍵模式: 0-基本模式; 1-進階模式
        slea.readInt(); //
        int job_type = slea.readInt();
        job = JobType.getByType(job_type);
        if (job == null) {
            System.out.println("發現新職業類型: " + job_type);
            return;
        }
        for (JobConstants.LoginJob j : JobConstants.LoginJob.values()) {
            if (j.getJobType() == job_type) {
                if (!j.enableCreate()) {
                    System.out.println("未開放的職業被嘗試創建");
                    return;
                }
            }
        }
        subcategory = slea.readShort();
        gender = slea.readByte();
        skin = slea.readByte();
        unk = slea.readByte(); //6/7/8/9
        face = slea.readInt();
        hair = slea.readInt();
        if (job.faceMark) {
            faceMark = slea.readInt();
        }
        if (job.ears) {
            ears = slea.readInt();
        }
        if (job.tail) {
            tail = slea.readInt();
        }
        if (job.hat) {
            hat = slea.readInt();
        }
        top = slea.readInt();
        if (job.bottom) {
            bottom = slea.readInt();
        }
        if (job.cape) {
            cape = slea.readInt();
        }
        shoes = slea.readInt();
        if (job.glove) {
            glove = slea.readInt();
        }
        weapon = slea.readInt();
        if (slea.available() >= 4) {
            shield = slea.readInt();
        }
//        int index = 0;
//        boolean noSkin = job == JobType.惡魔 || job == JobType.精靈遊俠 || job == JobType.蒼龍俠客;
//        int[] items = new int[]{face, hair, hairColor, noSkin ? -1 : skin, faceMark, hat, top, bottom, cape, shoes, weapon, shield};
//        for (int i : items) {
//            if (i > -1) {
//                if (!LoginInformationProvider.getInstance().isEligibleItem(gender, index, job.type, i)) {
//                    System.out.println(gender + " | " + index + " | " + job.type + " | " + i);
//                    return;
//                }
//                index++;
//            }
//        }
        //讀取創建角色默認配置
        MapleCharacter newchar = MapleCharacter.getDefault(c, job);
        newchar.setWorld((byte) c.getWorld());
        newchar.setFace(face);
        newchar.setSecondFace(face);
        if (hairColor < 0) {
            hairColor = 0;
        }
        if (job != JobType.米哈逸) {
            hair += hairColor;
        }
        newchar.setHair(hair);
        newchar.setSecondHair(hair);
        if (job == JobType.天使破壞者) {
            newchar.setSecondFace(21173);
            newchar.setSecondHair(37141);
        } else if (job == JobType.神之子) {
            newchar.setSecondFace(21290);
            newchar.setSecondHair(37623);
        }
        newchar.setGender(gender);
        newchar.setName(name);
        newchar.setSkinColor(skin);
        if (faceMark < 0) {
            faceMark = 0;
        }
        newchar.setFaceMarking(faceMark);
        int[] wrongEars = {1004062, 1004063, 1004064};
        int[] correctEars = {5010116, 5010117, 5010118};
        int[] wrongTails = {1102661, 1102662, 1102663};
        int[] correctTails = {5010119, 5010120, 5010121};
        for (int i = 0; i < wrongEars.length; i++) {
            if (ears == wrongEars[i]) {
                ears = correctEars[i];
            }
        }
        for (int i = 0; i < wrongTails.length; i++) {
            if (tail == wrongTails[i]) {
                tail = correctTails[i];
            }
        }
        if (ears < 0) {
            ears = 0;
        }
        newchar.setEars(ears);
        if (tail < 0) {
            tail = 0;
        }
        newchar.setTail(tail);
        final MapleItemInformationProvider li = MapleItemInformationProvider.getInstance();
        final MapleInventory equip = newchar.getInventory(MapleInventoryType.EQUIPPED);
        Item item;
        //-1 Hat | -2 Face | -3 Eye acc | -4 Ear acc | -5 Topwear 
        //-6 Bottom | -7 Shoes | -8 glove | -9 Cape | -10 Shield | -11 Weapon
        int[][] equips = new int[][]{{hat, -1}, {top, -5}, {bottom, -6}, {cape, -9}, {shoes, -7}, {glove, -8}, {weapon, -11}, {shield, -10}};
        for (int[] i : equips) {
            if (i[0] > 0) {
                item = li.getEquipById(i[0]);
                item.setPosition((byte) i[1]);
                item.setGMLog("創建角色獲得, 時間 " + FileoutputUtil.CurrentReadable_Time());
                equip.addFromDB(item);
            }
        }
        // Additional skills for all first job classes. Some skills are not added by default,
        // so adding the skill ID here between the {}, will give the skills you entered to the desired job.
        int[][] skills = new int[][]{
            {30000074/*自由精神*/},//末日反抗軍Resistance
            {1281/*回歸楓之谷*/},//冒險家Explorer
            {10000252/*元素位移*/, 10001244/*元素狂刃*/, 10001254/*元素閃現*/},//皇家騎士團Cygnus
            {/*20000194找回的記憶*/},//狂狼勇士Aran
            {20010022/*龍飛行*/},//龍魔導士Evan
            {}, //精靈遊俠Mercedes
            {},//惡魔Demon
            {},//幻影俠盜Phantom
            {},//影武者Dualblade
            {},//米哈逸Mihile
            {20040216/*光蝕*/, 20040217/*暗蝕*/, 20040219/*平衡*/, 20040220/*平衡*/, 20040221/*光明力量*/, 20041222/*星光順移*/},//夜光Luminous
            {},//凱撒Kaiser
            {60011216/*繼承人*/, 60011218/*魔法起重機*/, 60011220/*白日夢*/, 60011221/*配飾*/, 60011222/*魔法變身*/},//天使破壞者AngelicBuster
            {},//重炮指揮官Cannoneer
            {30020232/*蓄能系統*/, 30020234/*全能增幅 I*/, 30020240/*多樣化裝扮*/, 30021238/*刀舞*/},//傑諾Xenon
            {100000279/*時之意志*/, 100000282/*雙重打擊*/, 100001262/*神殿回歸*/, 100001263/*時之威能*/, 100001264/*聖靈神速*/, 100001265/*爆裂跳躍*/, 100001266/*爆裂衝刺*/, 100001268/*時之庇護*/},//神之子Zero
            {20051284/*閃現*/, 20050285/*精靈降臨1式*/},//隱月Eunwol
            {},//皮卡啾PinkBean
            {228/*草上飛*/},//蒼龍俠客Jett
            {40010000/*天賦的才能*/, 40010067/*攻守兼備*/, 40011023/*心刀*/},//劍豪Hayato
            {},//陰陽師Kanna
            {110001251/*管理連結技能*/}//幻獸師BeastTamer
        };
        if (skills[job.type].length > 0) {
            final Map<Skill, SkillEntry> ss = new HashMap<>();
            Skill s;
            for (int i : skills[job.type]) {
                s = SkillFactory.getSkill(i);
                int maxLevel = s.getMaxLevel();
                if (maxLevel < 1) {
                    maxLevel = s.getMasterLevel();
                }
                ss.put(s, new SkillEntry((byte) 1, (byte) maxLevel, -1));
            }
            if (job == JobType.神之子) {
                ss.put(SkillFactory.getSkill(101000103), new SkillEntry((byte) 8, (byte) 10, -1));
                ss.put(SkillFactory.getSkill(101000203), new SkillEntry((byte) 8, (byte) 10, -1));
            }
            if (job == JobType.幻獸師) {
                ss.put(SkillFactory.getSkill(110001511), new SkillEntry((byte) 0, (byte) 30, -1));
                ss.put(SkillFactory.getSkill(110001512), new SkillEntry((byte) 0, (byte) 5, -1));
                ss.put(SkillFactory.getSkill(110000513), new SkillEntry((byte) 0, (byte) 30, -1));
                ss.put(SkillFactory.getSkill(110000515), new SkillEntry((byte) 0, (byte) 10, -1));
            }
            newchar.changeSkillLevel_Skip(ss, false);
        }
        int[][] guidebooks = new int[][]{{4161001, 0}, {4161047, 1}, {4161048, 2000}, {4161052, 2001}, {4161054, 3}, {4161079, 2002}};
        int guidebook = 0;
        for (int[] i : guidebooks) {
            if (newchar.getJob() == i[1]) {
                guidebook = i[0];
            } else if (newchar.getJob() / 1000 == i[1]) {
                guidebook = i[0];
            }
        }
        if (guidebook > 0) {
            newchar.getInventory(MapleInventoryType.ETC).addItem(new Item(guidebook, (byte) 0, (short) 1, (byte) 0));
        }

        if (job == JobType.幻影俠盜) {
            newchar.getStat().maxhp = 850;
            newchar.getStat().hp = 140;
            newchar.getStat().maxmp = 805;
            newchar.getStat().mp = 38;
        }

        if (job == JobType.天使破壞者) {
            newchar.setQuestAdd(MapleQuest.getInstance(25835), (byte) 2, ""); //任務：愛斯卡達的真面目
            newchar.setQuestAdd(MapleQuest.getInstance(25829), (byte) 2, ""); //任務：這個技能是什麼？
        }

        if (job == JobType.神之子) {
            newchar.setLevel((short) 100);
            newchar.getStat().str = 518;
            newchar.getStat().maxhp = 6910;
            newchar.getStat().hp = 6910;
            newchar.getStat().maxmp = 100;
            newchar.getStat().mp = 100;
            newchar.setRemainingSp(3, 0); //alpha
            newchar.setRemainingSp(3, 1); //beta
            newchar.setQuestAdd(MapleQuest.getInstance(40900), (byte) 2, "");
            newchar.setQuestAdd(MapleQuest.getInstance(40901), (byte) 2, "");
            newchar.setQuestAdd(MapleQuest.getInstance(40902), (byte) 2, "");
            newchar.setQuestAdd(MapleQuest.getInstance(40903), (byte) 2, "");
            newchar.setQuestAdd(MapleQuest.getInstance(40904), (byte) 2, "");
            newchar.setQuestAdd(MapleQuest.getInstance(40905), (byte) 2, "");
        }

        if (job == JobType.幻獸師) {
            newchar.setLevel((short) 10);
            newchar.getStat().maxhp = 567;
            newchar.getStat().hp = 551;
            newchar.getStat().maxmp = 270;
            newchar.getStat().mp = 263;
            newchar.setRemainingAp(45);
            newchar.setRemainingSp(3, 0);
        }

        if (ServerConfig.LOG_PACKETS) {
            FileoutputUtil.log(FileoutputUtil.Create_Character,
                    "\r\n\r\n名字: " + name
                    + "\r\n職業: " + job_type
                    + "\r\n性別: " + gender
                    + "\r\n皮膚: " + skin
                    + "\r\n頭髮顏色: " + unk
                    + "\r\n臉型: " + face
                    + "\r\n髮型: " + hair
                    + "\r\n臉飾: " + faceMark
                    + "\r\n耳朵: " + ears
                    + "\r\n尾巴: " + tail
                    + "\r\n帽子: " + hat
                    + "\r\n上衣: " + top
                    + "\r\n褲子: " + bottom
                    + "\r\n披風:" + cape
                    + "\r\n鞋子: " + shoes
                    + "\r\n手套: " + glove
                    + "\r\n武器: " + weapon
                    + "\r\n盾牌: " + shield
                    + "\r\n\r\n"
            );
        }

        // 修正進階按鍵不完全
        if (keymapType == 1) {
            int[] keyValue = new int[]{0x10, 0x11, 0x12, 0x13, 0x1E, 0x1F, 0x20, 0x21, 0x02, 0x03,
                0x04, 0x05, 0x1D, 0x38, 0x2C, 0x2D, 0x06, 0x07, 0x08, 0x09,
                0x2E, 0x16, 0x17, 0x24, 0x0A, 0x0B, 0x25, 0x31};
            StringBuilder ret = new StringBuilder();
            for (int i = 0; i < 28; i++) {
                ret.append(keyValue[i]).append(",");
            }
            ret.deleteCharAt(ret.length() - 1);
            newchar.getQuestNAdd(MapleQuest.getInstance(123000)).setCustomData(ret.toString());
        }

        if (MapleCharacterUtil.canCreateChar(name, c.isGM()) && (!LoginInformationProvider.getInstance().isForbiddenName(name) || c.isGM()) && (c.isGM() || c.canMakeCharacter(c.getWorld()))) {
            MapleCharacter.saveNewCharToDB(newchar, job, subcategory, keymapType);
            c.getSession().write(LoginPacket.addNewCharEntry(newchar, true));
            c.createdChar(newchar.getId());
            //newchar.newCharRewards();
        } else {
            c.getSession().write(LoginPacket.addNewCharEntry(newchar, false));
        }
    }

    public static void CreateUltimate(final LittleEndianAccessor slea, final MapleClient c) {
        if (!c.getPlayer().isGM() && (!c.isLoggedIn() || c.getPlayer() == null || c.getPlayer().getLevel() < 120 || c.getPlayer().getMapId() != 130000000 || c.getPlayer().getQuestStatus(20734) != 0 || c.getPlayer().getQuestStatus(20616) != 2 || !MapleJob.is皇家騎士團(c.getPlayer().getJob()) || !c.canMakeCharacter(c.getPlayer().getWorld()))) {
            c.getSession().write(CField.createUltimate(2));
            //Character slots are full. Please purchase another slot from the Cash Shop.
            return;
        }
        //System.out.println(slea.toString());
        final String name = slea.readMapleAsciiString();
        final int job = slea.readInt(); //job ID

        final int face = slea.readInt();
        final int hair = slea.readInt();

        //No idea what are these used for:
        final int hat = slea.readInt();
        final int top = slea.readInt();
        final int glove = slea.readInt();
        final int shoes = slea.readInt();
        final int weapon = slea.readInt();

        final byte gender = c.getPlayer().getGender();

        //JobType errorCheck = JobType.Adventurer;
        //if (!LoginInformationProvider.getInstance().isEligibleItem(gender, 0, errorCheck.type, face)) {
        //    c.getSession().write(CWvsContext.enableActions());
        //    return;
        //}
        JobType jobType = JobType.終極冒險家;

        MapleCharacter newchar = MapleCharacter.getDefault(c, jobType);
        newchar.setJob(job);
        newchar.setWorld(c.getPlayer().getWorld());
        newchar.setFace(face);
        newchar.setHair(hair);
        newchar.setGender(gender);
        newchar.setName(name);
        newchar.setSkinColor((byte) 3); //troll
        newchar.setLevel((short) 50);
        newchar.getStat().str = (short) 4;
        newchar.getStat().dex = (short) 4;
        newchar.getStat().int_ = (short) 4;
        newchar.getStat().luk = (short) 4;
        newchar.setRemainingAp((short) 254); //49*5 + 25 - 16
        newchar.setRemainingSp(job / 100 == 2 ? 128 : 122); //2 from job advancements. 120 from leveling. (mages get +6)
        newchar.getStat().maxhp += 150; //Beginner 10 levels
        newchar.getStat().maxmp += 125;
        switch (job) {
            case 110:
            case 120:
            case 130:
                newchar.getStat().maxhp += 600; //Job Advancement
                newchar.getStat().maxhp += 2000; //Levelup 40 times
                newchar.getStat().maxmp += 200;
                break;
            case 210:
            case 220:
            case 230:
                newchar.getStat().maxmp += 600;
                newchar.getStat().maxhp += 500; //Levelup 40 times
                newchar.getStat().maxmp += 2000;
                break;
            case 310:
            case 320:
            case 410:
            case 420:
            case 520:
                newchar.getStat().maxhp += 500;
                newchar.getStat().maxmp += 250;
                newchar.getStat().maxhp += 900; //Levelup 40 times
                newchar.getStat().maxmp += 600;
                break;
            case 510:
                newchar.getStat().maxhp += 500;
                newchar.getStat().maxmp += 250;
                newchar.getStat().maxhp += 450; //Levelup 20 times
                newchar.getStat().maxmp += 300;
                newchar.getStat().maxhp += 800; //Levelup 20 times
                newchar.getStat().maxmp += 400;
                break;
            default:
                return;
        }

        final Map<Skill, SkillEntry> ss = new HashMap<>();
        ss.put(SkillFactory.getSkill(1074 + (job / 100)), new SkillEntry((byte) 5, (byte) 5, -1));
        ss.put(SkillFactory.getSkill(80), new SkillEntry((byte) 1, (byte) 1, -1));
        newchar.changeSkillLevel_Skip(ss, false);
        final MapleItemInformationProvider li = MapleItemInformationProvider.getInstance();

        int[] items = new int[]{1142257, hat, top, shoes, glove, weapon, hat + 1, top + 1, shoes + 1, glove + 1, weapon + 1}; //brilliant = fine+1
        for (byte i = 0; i < items.length; i++) {
            Item item = li.getEquipById(items[i]);
            item.setPosition((byte) (i + 1));
            newchar.getInventory(MapleInventoryType.EQUIP).addFromDB(item);
        }

        newchar.getInventory(MapleInventoryType.USE).addItem(new Item(2000004, (byte) 0, (short) 200, (byte) 0));
        if (MapleCharacterUtil.canCreateChar(name, c.isGM()) && (!LoginInformationProvider.getInstance().isForbiddenName(name) || c.isGM())) {
            MapleCharacter.saveNewCharToDB(newchar, jobType, (short) 0);
            MapleQuest.getInstance(20734).forceComplete(c.getPlayer(), 1101000);
            c.getSession().write(CField.createUltimate(0));
        } else if (!LoginInformationProvider.getInstance().isForbiddenName(name) || c.isGM()) {
            c.getSession().write(CField.createUltimate(3)); //"You cannot use this name."
        } else {
            c.getSession().write(CField.createUltimate(1));
        }
    }

    public static void DeleteChar(final LittleEndianAccessor slea, final MapleClient c) {
        final String Secondpw_Client = slea.readMapleAsciiString();
        final int Character_ID = slea.readInt();

        if (!c.login_Auth(Character_ID) || !c.isLoggedIn() || loginFailCount(c) || c.getSecondPassword() == null) {
            c.getSession().close(true);
            return; // Attempting to delete other character
        }
        byte state = 0;

        if (!c.CheckSecondPassword(Secondpw_Client)) { // Wrong Password
            state = 20;
        }

        if (state == 0) {
            state = (byte) c.deleteCharacter(Character_ID);
        }
        c.getSession().write(LoginPacket.deleteCharResponse(Character_ID, state));
    }

    public static void Character_WithoutSecondPassword(final LittleEndianAccessor slea, final MapleClient c, final boolean haspic, final boolean view) {

        final int charId = slea.readInt();
        if (view) {
            c.setChannel(1);
            c.setWorld(slea.readInt());
        }
        final String currentpw = c.getSecondPassword();
        if (!c.isLoggedIn() || loginFailCount(c) || /*(currentpw != null && (!currentpw.equals("") || haspic)) || */ !c.login_Auth(charId) || ChannelServer.getInstance(c.getChannel()) == null || !WorldConstants.isExists(c.getWorld())) {
            c.getSession().close(true);
            return;
        }
        //c.updateMacs(slea.readMapleAsciiString());
        //slea.readMapleAsciiString();
        /*if (slea.available() != 0) {
         final String setpassword = slea.readMapleAsciiString();

         if (setpassword.length() >= 6 && setpassword.length() <= 16) {
         c.setSecondPassword(setpassword);
         c.updateSecondPassword();
         } else {
         c.getSession().write(LoginPacket.secondPwError((byte) 0x14));
         return;
         }
         } else if (haspic) {
         return;
         }*/
        if (c.getIdleTask() != null) {
            c.getIdleTask().cancel(true);
        }
        final String s = c.getSessionIPAddress();
        LoginServer.putLoginAuth(charId, s.substring(s.indexOf('/') + 1, s.length()), c.getTempIP(), c.getChannel());
        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, s);
        c.getSession().write(CField.getServerIP(c, Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1]), charId));
    }

    public static void Character_WithSecondPassword(final LittleEndianAccessor slea, final MapleClient c, final boolean view) {
        final String password = slea.readMapleAsciiString();
        final int charId = slea.readInt();
        if (view) {
            c.setChannel(1);
            c.setWorld(slea.readInt());
        }
        if (!c.isLoggedIn() || loginFailCount(c) || c.getSecondPassword() == null || !c.login_Auth(charId) || ChannelServer.getInstance(c.getChannel()) == null || !WorldConstants.isExists(c.getWorld())) {
            c.getSession().close(true);
            return;
        }
        c.updateMacs(slea.readMapleAsciiString());

        if (c.CheckSecondPassword(password) && password.length() >= 6 && password.length() <= 16 || c.isGM()) {

            if (c.getIdleTask() != null) {
                c.getIdleTask().cancel(true);
            }

            final String s = c.getSessionIPAddress();
            LoginServer.putLoginAuth(charId, s.substring(s.indexOf('/') + 1, s.length()), c.getTempIP(), c.getChannel());
            c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, s);
            c.getSession().write(CField.getServerIP(c, Integer.parseInt(ChannelServer.getInstance(c.getChannel()).getIP().split(":")[1]), charId));
        } else {
            c.getSession().write(LoginPacket.secondPwError((byte) 0x14));
        }
    }

    public static void partTimeJob(final LittleEndianAccessor slea, final MapleClient c) {
        System.out.println("[Part Time Job] data: " + slea);
        byte mode = slea.readByte(); //1 = start 2 = end
        int cid = slea.readInt(); //character id
        byte job = slea.readByte(); //part time job
        if (mode == 0) {
            LoginPacket.partTimeJob(cid, (byte) 0, System.currentTimeMillis());
        } else if (mode == 1) {
            LoginPacket.partTimeJob(cid, job, System.currentTimeMillis());
        }
    }

    public static void PartJob(LittleEndianAccessor slea, MapleClient c) {
        if (c.getPlayer() != null || !c.isLoggedIn()) {
            c.getSession().close(true);
            return;
        }
        final byte mode = slea.readByte();
        final int cid = slea.readInt();
        if (mode == 1) { // 開始打工
            final PartTimeJob partTime = MapleCharacter.getPartTime(cid);
            final byte job = slea.readByte();
            if (/*chr.getLevel() < 30 || */job < 0 || job > 5 || partTime.getReward() > 0
                    || (partTime.getJob() > 0 && partTime.getJob() <= 5)) {
                c.getSession().close(true);
                return;
            }
            partTime.setTime(System.currentTimeMillis());
            partTime.setJob(job);
            c.getSession().write(LoginPacket.updatePartTimeJob(partTime));
            MapleCharacter.removePartTime(cid);
            MapleCharacter.addPartTime(partTime);
        } else if (mode == 2) { // 結束打工
            final PartTimeJob partTime = MapleCharacter.getPartTime(cid);
            if (/*chr.getLevel() < 30 || */partTime.getReward() > 0
                    || partTime.getJob() < 0 || partTime.getJob() > 5) {
                c.getSession().close(true);
                return;
            }
            final long distance = (System.currentTimeMillis() - partTime.getTime()) / (60 * 60 * 1000L);
            if (distance > 1) {
                partTime.setReward((int) (((partTime.getJob() + 1) * 1000L) + distance));
            } else {
                partTime.setJob((byte) 0);
                partTime.setReward(0);
            }
            partTime.setTime(System.currentTimeMillis());
            MapleCharacter.removePartTime(cid);
            MapleCharacter.addPartTime(partTime);
            c.getSession().write(LoginPacket.updatePartTimeJob(partTime));
        }
    }

    public static void SetGender(LittleEndianAccessor slea, MapleClient c) {
        String name = slea.readMapleAsciiString();
        String secondPassword = slea.readMapleAsciiString();
        byte gender = slea.readByte();
        if (!c.getAccountName().equals(name) || c.getSecondPassword() != null || gender < 0 || gender > 1) {
            c.getSession().write(LoginPacket.genderChanged(false));
            return;
        }
        c.clearInformation();
        if (secondPassword.length() >= 5) {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            try {
                ps = con.prepareStatement("UPDATE accounts SET gender = ?, 2ndpassword = ? WHERE name = ?");
                ps.setInt(1, gender);
                ps.setString(2, LoginCrypto.hexSha1(secondPassword));
                ps.setString(3, name);
                ps.execute();
                ps.close();
            } catch (SQLException ex) {
            }
            c.getSession().write(LoginPacket.genderChanged(true));
        } else {
            c.getSession().write(LoginPacket.genderChanged(false));
        }
    }
}
