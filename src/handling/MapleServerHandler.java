package handling;

import client.ClientRedirector;
import handling.farm.handler.FarmHandler;
import client.MapleClient;
import client.inventory.Item;
import client.inventory.MaplePet;
import client.inventory.PetDataFactory;
import constants.ServerConfig;
import constants.ServerConstants;
import handling.cashshop.CashShopServer;
import handling.cashshop.handler.*;
import handling.channel.ChannelServer;
import handling.channel.handler.*;
import handling.farm.FarmServer;
import handling.farm.handler.FarmOperation;
import handling.login.LoginServer;
import handling.login.handler.*;
import handling.mina.MaplePacketDecoder;
import handling.world.World;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import server.CashItemFactory;
import server.CashItemInfo;
import server.Randomizer;
import server.commands.CommandProcessor;
import server.maps.MapleMap;
import server.shark.SharkPacket;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.MapleAESOFB;
import tools.Pair;
import tools.data.ByteArrayByteStream;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.LoginPacket;
import tools.packet.CSPacket;

public class MapleServerHandler extends IoHandlerAdapter {

    private final int channel;
    public final static int FARM_SERVER = -20;
    public final static int CASH_SHOP_SERVER = -10;
    public final static int LOGIN_SERVER = 0;
    private final List<String> BlockedIP = new ArrayList<>();
    private final Map<String, Pair<Long, Byte>> tracker = new ConcurrentHashMap<>();

    public MapleServerHandler(int channel) {
        this.channel = channel;
    }

    @Override
    public void exceptionCaught(final IoSession session, final Throwable cause) throws Exception {
        if (cause.getMessage() != null) {
            System.err.println("[連結異常] " + cause.getMessage());
            cause.getLocalizedMessage();
            FileoutputUtil.printError("連結異常.txt", cause.getMessage());
        }
        if (!(cause instanceof IOException)) {
            MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);
            if ((client != null) && (client.getPlayer() != null)) {
                client.getPlayer().saveToDB(false, channel == MapleServerHandler.CASH_SHOP_SERVER);
                FileoutputUtil.printError("連結異常.txt", cause, "連結異常 by: 角色:" + client.getPlayer().getName() + " 職業:" + client.getPlayer().getJob() + " 地圖:" + client.getPlayer().getMap());
            }
        }
//        MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);
//        log.error(MapleClient.getLogMessage(client, cause.getMessage()), cause);
//        cause.printStackTrace();
    }

    @Override
    public void sessionOpened(final IoSession session) throws Exception {
        // Start of IP checking
        final String address = session.getRemoteAddress().toString().split(":")[0];
        System.out.println("[連結線程] " + address + " 已連結");

        if (BlockedIP.contains(address)) {
            session.close(true);
            return;
        }
        final Pair<Long, Byte> track = tracker.get(address);

        byte count;
        if (track == null) {
            count = 1;
        } else {
            count = track.right;

            final long difference = System.currentTimeMillis() - track.left;
            if (difference < 2000) { // Less than 2 sec
                count++;
            } else if (difference > 20000) { // Over 20 sec
                count = 1;
            }
            if (count >= 10) {
                BlockedIP.add(address);
                System.out.println("[登入服務] IP:" + address + " 連結次數超過限制斷開連結");
                tracker.remove(address); // Cleanup
                session.close(true);
                return;
            }
        }
        tracker.put(address, new Pair<>(System.currentTimeMillis(), count));
        // End of IP checking.
        String IP = address.substring(address.indexOf('/') + 1, address.length());

        if (channel == MapleServerHandler.FARM_SERVER) {
            if (FarmServer.isShutdown()) {
                session.close(true);
                return;
            }
        } else if (channel == MapleServerHandler.CASH_SHOP_SERVER) {
            if (CashShopServer.isShutdown()) {
                session.close(true);
                return;
            }
        } else if (channel == MapleServerHandler.LOGIN_SERVER) {
            if (LoginServer.isShutdown()) {
                session.close(true);
                return;
            }
        } else if (channel > MapleServerHandler.LOGIN_SERVER) {
            if (ChannelServer.getInstance(channel).isShutdown()) {
                session.close(true);
                return;
            }
            if (!LoginServer.containsIPAuth(IP)) {
                session.close(true);
                return;
            }
        } else {
            System.out.println("[連結錯誤] 未知類型: " + channel);
            session.close(true);
            return;
        }

        LoginServer.removeIPAuth(IP);
        // IV used to decrypt packets from client.
        final byte ivRecv[] = new byte[]{(byte) Randomizer.nextInt(255), (byte) Randomizer.nextInt(255), (byte) Randomizer.nextInt(255), (byte) Randomizer.nextInt(255)};
        // IV used to encrypt packets for client.
        final byte ivSend[] = new byte[]{(byte) Randomizer.nextInt(255), (byte) Randomizer.nextInt(255), (byte) Randomizer.nextInt(255), (byte) Randomizer.nextInt(255)};
        MapleAESOFB sendCypher = new MapleAESOFB(ServerConfig.USE_FIXED_IV ? ServerConfig.Static_LocalIV : ivSend, (short) (0xFFFF - ServerConstants.MAPLE_VERSION));
        MapleAESOFB recvCypher = new MapleAESOFB(ServerConfig.USE_FIXED_IV ? ServerConfig.Static_RemoteIV : ivRecv, ServerConstants.MAPLE_VERSION);

        final MapleClient client = new MapleClient(sendCypher, recvCypher, session);
        client.setChannel(channel);

        MaplePacketDecoder.DecoderState decoderState = new MaplePacketDecoder.DecoderState();
        session.setAttribute(MaplePacketDecoder.DECODER_STATE_KEY, decoderState);

        byte[] handShakePacket = LoginPacket.getHello(ivSend, ivRecv);
        session.write(handShakePacket);

        byte[] hp = new byte[handShakePacket.length + 2];
        hp[0] = (byte) 0xFF;
        hp[1] = (byte) 0xFF;
        for (int i = 2; i < handShakePacket.length + 2; i++) {
            hp[i] = handShakePacket[i - 2];
        }
        final SharkPacket sp = new SharkPacket(hp, false);
        client.sl.log(sp);

        System.out.println("握手包發送到 " + address);
        Random r = new Random();
        client.setSessionId(r.nextLong()); // Generates a random session id.  
        session.setAttribute(MapleClient.CLIENT_KEY, client);
        World.Client.addClient(client);

        if (LoginServer.isAdminOnly()) {
            StringBuilder sb = new StringBuilder();
            sb.append("IoSession opened ").append(address);
            System.out.println(sb.toString());
        }
    }

    @Override
    public void sessionClosed(final IoSession session) throws Exception {
        MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);

        if (client != null) {
            try {
                client.disconnect(true, channel == MapleServerHandler.CASH_SHOP_SERVER);
            } finally {
                World.Client.removeClient(client);
                session.close(true);
                session.removeAttribute(MapleClient.CLIENT_KEY);
                session.removeAttribute(MaplePacketDecoder.DECODER_STATE_KEY);
                client = null;
            }
        }
        super.sessionClosed(session);
    }

    @Override
    public void sessionIdle(final IoSession session, final IdleStatus status) throws Exception {
        MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);

        if (client != null) {
            client.sendPing();
        }
        super.sessionIdle(session, status);
    }

    @Override
    public void messageReceived(final IoSession session, final Object message) {
        if (message == null || session == null) {
            return;
        }
        final LittleEndianAccessor slea = new LittleEndianAccessor(new ByteArrayByteStream((byte[]) message));
        if (slea.available() < 2) {
            return;
        }
        final MapleClient c = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);
        if (c == null || !c.isReceiving()) {
            return;
        }
        final short header_num = slea.readShort();
        for (final RecvPacketOpcode recv : RecvPacketOpcode.values()) {
            if (recv.getValue() == header_num) {
                if (recv.NeedsChecking() && !c.isLoggedIn()) {
                    return;
                }
                try {
                    if (c.getPlayer() != null && c.isMonitored()) {
                        try (FileWriter fw = new FileWriter(new File(FileoutputUtil.Monitor_Dir + c.getPlayer().getName() + "_log.txt"), true)) {
                            fw.write(String.valueOf(recv) + " (" + Integer.toHexString(header_num) + ") Handled: \r\n" + slea.toString() + "\r\n");
                            fw.flush();
                        }
                    }
                    handlePacket(recv, slea, c);
                } catch (NegativeArraySizeException | ArrayIndexOutOfBoundsException e) {
                    if (ServerConstants.USE_LOCALHOST) {
                        FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
                        FileoutputUtil.log(FileoutputUtil.PacketEx_Log, "Packet: " + header_num + "\r\n" + slea.toString(true));
                    }
                } catch (Exception e) {
                    FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
                    FileoutputUtil.log(FileoutputUtil.PacketEx_Log, "Packet: " + header_num + "\r\n" + slea.toString(true));
                }

                return;
            }
        }
        if (ServerConfig.LOG_PACKETS) {
            final byte[] packet = slea.read((int) slea.available());
            final StringBuilder sb = new StringBuilder("發現未知用戶端數據包 - (包頭:0x" + Integer.toHexString(header_num) + ")");
            System.err.println(sb.toString());
            sb.append(":\r\n").append(HexTool.toString(packet)).append("\r\n").append(HexTool.toStringFromAscii(packet));
            FileoutputUtil.log(FileoutputUtil.UnknownPacket_Log, sb.toString());
        }
    }

    public static void handlePacket(final RecvPacketOpcode header, final LittleEndianAccessor slea, final MapleClient c) throws Exception {
        switch (header) {
            case LOGIN_REDIRECTOR:
                if (!ServerConstants.REDIRECTOR) {
                    System.out.println("Redirector login packet recieved, but server is not set to redirector. Please change it in ServerConstants!");
                } else {
                    slea.skip(2);
                    String code = slea.readMapleAsciiString();
                    Map<String, ClientRedirector> redirectors = World.Redirector.getRedirectors();
                    if (!redirectors.containsKey(code) || redirectors.get(code).isLogined()) {
                        if (redirectors.get(code).isLogined()) {
                            redirectors.remove(code);
                        }
                        c.getSession().close(true);
                    } else {
                        ClientRedirector redirector = redirectors.get(code);
                        redirector.setLogined(true);
                        c.loginData(redirector.getAccount());
                        c.getSession().write(LoginPacket.getSecondAuthSuccess(c));
                    }
                }
                break;
            case CRASH_INFO:
                System.out.println("Crash" + slea.toString());
                break;
            case CLIENT_HELLO:
                byte mapleType = slea.readByte();
                short mapleVersion = slea.readShort();
                String maplePatch = String.valueOf(slea.readShort());
                if ((mapleType != (ServerConstants.MAPLE_TYPE.getType())) || (mapleVersion != ServerConstants.MAPLE_VERSION) || (!maplePatch.equals(ServerConstants.MAPLE_PATCH))) {
                    c.getSession().close(true);
                    System.out.println(c.getSessionIPAddress() + " 用戶端不正確,斷開連接");
                } else {
                    System.out.println(c.getSessionIPAddress() + " 連接到遊戲!");
                }
                break;
            case CLIENT_START:
                c.getSession().write(LoginPacket.showMapleStory());
                break;
            case PONG:
                c.pongReceived();
                break;
            case STRANGE_DATA:
                break;
            case LOGIN_PASSWORD:
                CharLoginHandler.login(slea, c);
                break;
            case WRONG_PASSWORD:
                if (c.getSessionIPAddress().contains("8.31.99.141") || c.getSessionIPAddress().contains("8.31.99.143") || c.getSessionIPAddress().contains("127.0.0.1")) {
                    c.loginData("admin");
                    c.getSession().write(LoginPacket.getAuthSuccessRequest(c));
                }
                break;
            case SET_GENDER:
                CharLoginHandler.SetGender(slea, c);
                break;
            case AUTH_REQUEST:
                CharLoginHandler.handleAuthRequest(slea, c);
                break;
            case CLIENT_FAILED:
                break;
            case VIEW_SERVERLIST:
                if (slea.readByte() == 0) {
                    CharLoginHandler.ServerListRequest(c);
                }
                break;
            case REDISPLAY_SERVERLIST:
            case SERVERLIST_REQUEST:
                CharLoginHandler.ServerListRequest(c);
                break;
            case CHARLIST_REQUEST:
                CharLoginHandler.CharlistRequest(slea, c);
                break;
            case CHANGE_CHAR_POSITION:
                CharLoginHandler.changeCharPosition(slea, c);
                break;
            case SERVERSTATUS_REQUEST:
                CharLoginHandler.ServerStatusRequest(c);
                break;
            case CHECK_CHAR_NAME:
                CharLoginHandler.CheckCharName(slea.readMapleAsciiString(), c);
                break;
            case CREATE_CHAR:
            case CREATE_SPECIAL_CHAR:
                CharLoginHandler.CreateChar(slea, c);
                break;
            case CREATE_CHAR_2PW:
                CharLoginHandler.CreateChar2Pw(slea, c);
                break;
            case CREATE_ULTIMATE:
                CharLoginHandler.CreateUltimate(slea, c);
                break;
            case DELETE_CHAR:
                CharLoginHandler.DeleteChar(slea, c);
                break;
            case CHAR_SELECT_NO_PIC:
                CharLoginHandler.Character_WithoutSecondPassword(slea, c, false, false);
                break;
            case VIEW_REGISTER_PIC:
                CharLoginHandler.Character_WithoutSecondPassword(slea, c, true, true);
                break;
            case PART_TIME_JOB:
                CharLoginHandler.PartJob(slea, c);
                break;
            case CHAR_SELECT:
            case CREACTE_CHAR_SELECT:
                CharLoginHandler.Character_WithoutSecondPassword(slea, c, true, false);
                break;
            case VIEW_SELECT_PIC:
                CharLoginHandler.Character_WithSecondPassword(slea, c, true);
                break;
            case AUTH_SECOND_PASSWORD:
                CharLoginHandler.Character_WithSecondPassword(slea, c, false);
                break;
            case CHARACTER_CARD:
                CharLoginHandler.updateCCards(slea, c);
                break;
            case CLIENT_FEEDBACK:
                FileoutputUtil.log(FileoutputUtil.Client_Feedback, "\r\n" + slea.readMapleAsciiString());
                break;
            case CLIENT_ERROR:
                System.err.println("收到用戶端的報錯: \r\n(詳細請看\"日誌/用戶端_報錯.txt\")\r\n(" + slea.toString());
                if (slea.available() < 8) {
                    System.out.println(slea.toString());
                    return;
                }
                short type = slea.readShort();
                String type_str = "Unknown?!";
                if (type == 0x01) {
                    type_str = "SendBackupPacket";
                } else if (type == 0x02) {
                    type_str = "Crash Report";
                } else if (type == 0x03) {
                    type_str = "Exception";
                }
                int errortype = slea.readInt(); // example error 38
//                if (errortype == 0) { // i don't wanna log error code 0 stuffs, (usually some bounceback to login)
//                    return;
//                }
                short data_length = slea.readShort();
                slea.skip(4); // ?B3 86 01 00 00 00 FF 00 00 00 00 00 9E 05 C8 FF 02 00 CD 05 C9 FF 7D 00 00 00 3F 00 00 00 00 00 02 77 01 00 25 06 C9 FF 7D 00 00 00 40 00 00 00 00 00 02 C1 02
                short opcodeheader = slea.readShort();
                byte[] opcode = slea.read((int) slea.available());
                int packetLen = (int) slea.available() + 2;
                String AccountName = "null";
                String charName = "null";
                String charLevel = "null";
                String charJob = "null";
                String Map = "null";
                try {
                    AccountName = c.getAccountName();
                } catch (Throwable e) {
                }
                try {
                    charName = c.getPlayer().getName();
                } catch (Throwable e) {
                }
                try {
                    charLevel = String.valueOf(c.getPlayer().getLevel());
                } catch (Throwable e) {
                }
                try {
                    charJob = String.valueOf(c.getPlayer().getJob());
                } catch (Throwable e) {
                }
                try {
                    Map = c.getPlayer().getMap().toString();
                } catch (Throwable e) {
                }
                System.err.println("[用戶端 報錯] 錯誤代碼 " + errortype + " 類型 " + type_str + "\r\n\t[數據] 長度 " + data_length + " [" + SendPacketOpcode.nameOf(opcodeheader) + " | " + opcodeheader + "]\r\n" + HexTool.toString(slea.read((int) slea.available())));
                String tab = "";
                for (int i = 4; i > SendPacketOpcode.nameOf(opcodeheader).length() / 8; i--) {
                    tab += "\t";
                }
                String t = packetLen >= 10 ? packetLen >= 100 ? packetLen >= 1000 ? "" : " " : "  " : "   ";
                FileoutputUtil.log(FileoutputUtil.Client_Error, "\r\n"
                        + "賬號:" + AccountName + "\r\n"
                        + "角色:" + charName + "(等級:" + charLevel + ")" + "\r\n"
                        + "職業:" + charJob + "\r\n"
                        + "地圖:" + Map + "\r\n"
                        + "錯誤類型: " + type_str + "(" + errortype + ")\r\n"
                        + "\r\n"
                        + "[發送]\t" + SendPacketOpcode.nameOf(opcodeheader) + tab + " \t包頭:" + HexTool.getOpcodeToString(opcodeheader) + t + "[" + (data_length - 4) + "字元]\r\n"
                        + "\r\n"
                        + (opcode.length < 1 ? "" : (HexTool.toString(opcode) + "\r\n"))
                        + (opcode.length < 1 ? "" : (HexTool.toStringFromAscii(opcode) + "\r\n"))
                        + "\r\n");
                break;
            case ENABLE_SPECIAL_CREATION:
                c.getSession().write(LoginPacket.enableSpecialCreation(c.getAccID(), true));
                break;
            case RSA_KEY:
                break;
            case GET_SERVER:
                c.getSession().write(LoginPacket.getLoginBackground());
                break;
            // END OF LOGIN SERVER
            case CHANGE_CHANNEL:
            case CHANGE_ROOM_CHANNEL:
                InterServerHandler.ChangeChannel(slea, c, c.getPlayer(), header == RecvPacketOpcode.CHANGE_ROOM_CHANNEL);
                break;
            case PLAYER_LOGGEDIN:
                slea.readInt();
                final int playerid = slea.readInt();
                InterServerHandler.Loggedin(playerid, c);
                break;
            case ENTER_PVP:
            case ENTER_PVP_PARTY:
                PlayersHandler.EnterPVP(slea, c);
                break;
            case PVP_RESPAWN:
                PlayersHandler.RespawnPVP(slea, c);
                break;
            case LEAVE_PVP:
                PlayersHandler.LeavePVP(slea, c);
                break;
            case ENTER_AZWAN:
                PlayersHandler.EnterAzwan(slea, c);
                break;
            case ENTER_AZWAN_EVENT:
                PlayersHandler.EnterAzwanEvent(slea, c);
                break;
            case LEAVE_AZWAN:
                PlayersHandler.LeaveAzwan(slea, c);
                c.getSession().write(CField.showEffect("hillah/fail"));
                c.getSession().write(CField.UIPacket.openUIOption(0x45, 0));
                break;
            case PVP_ATTACK:
                PlayersHandler.AttackPVP(slea, c);
                break;
            case PVP_SUMMON:
                SummonHandler.SummonPVP(slea, c);
                break;
            case ENTER_FARM:
                InterServerHandler.EnterFarm(c, c.getPlayer());
                break;
            case FARM_COMPLETE_QUEST:
                FarmHandler.completeQuest(slea, c);
                break;
            case FARM_NAME:
                FarmHandler.createFarm(slea, c);
                break;
            case PLACE_FARM_OBJECT:
                FarmHandler.placeBuilding(slea, c);
                break;
            case FARM_SHOP_BUY:
                FarmHandler.buy(slea, c);
                break;
            case HARVEST_FARM_BUILDING:
                FarmHandler.harvest(slea, c);
                break;
            case USE_FARM_ITEM:
                FarmHandler.useItem(slea, c);
                break;
            case RENAME_MONSTER:
                FarmHandler.renameMonster(slea, c);
                break;
            case NURTURE_MONSTER:
                FarmHandler.nurtureMonster(slea, c);
                break;
            case FARM_QUEST_CHECK:
                FarmHandler.checkQuestStatus(slea, c);
                break;
            case FARM_FIRST_ENTRY:
                FarmHandler.firstEntryReward(slea, c);
                break;
            case EXIT_FARM:
                FarmOperation.LeaveFarm(slea, c, c.getPlayer());
                break;
            case ENTER_CASH_SHOP:
                InterServerHandler.EnterCS(c, c.getPlayer());
                break;
            case ENTER_MTS:
                InterServerHandler.EnterMTS(c, c.getPlayer());
                break;
            case MOVE_PLAYER:
                PlayerHandler.MovePlayer(slea, c, c.getPlayer());
                break;
            case CHAR_INFO_REQUEST:
                c.getPlayer().updateTick(slea.readInt());
                PlayerHandler.CharInfoRequest(slea.readInt(), c, c.getPlayer());
                break;
            case SUMMON_ATTACK:
            case CLOSE_RANGE_ATTACK:
            case RANGED_ATTACK:
            case MAGIC_ATTACK:
            case PASSIVE_ATTACK:
                PlayerHandler.attack(slea, c, header);
                break;
            case SPECIAL_SKILL:
                PlayerHandler.SpecialSkill(slea, c, c.getPlayer());
                break;
            case GET_BOOK_INFO:
                PlayersHandler.MonsterBookInfoRequest(slea, c, c.getPlayer());
                break;
            case MONSTER_BOOK_DROPS:
                PlayersHandler.MonsterBookDropsRequest(slea, c, c.getPlayer());
                break;
            case CHANGE_CODEX_SET:
                PlayersHandler.ChangeSet(slea, c, c.getPlayer());
                break;
            case PROFESSION_INFO:
                ItemMakerHandler.ProfessionInfo(slea, c);
                break;
            case CRAFT_DONE:
                ItemMakerHandler.CraftComplete(slea, c, c.getPlayer());
                break;
            case CRAFT_MAKE:
                ItemMakerHandler.CraftMake(slea, c, c.getPlayer());
                break;
            case CRAFT_EFFECT:
                ItemMakerHandler.CraftEffect(slea, c, c.getPlayer());
                break;
            case START_HARVEST:
                ItemMakerHandler.StartHarvest(slea, c, c.getPlayer());
                break;
            case STOP_HARVEST:
                ItemMakerHandler.StopHarvest(slea, c, c.getPlayer());
                break;
            case MAKE_EXTRACTOR:
                ItemMakerHandler.MakeExtractor(slea, c, c.getPlayer());
                break;
            case USE_BAG:
                ItemMakerHandler.UseBag(slea, c, c.getPlayer());
                break;
            case USE_FAMILIAR:
                MobHandler.UseFamiliar(slea, c, c.getPlayer());
                break;
            case SPAWN_FAMILIAR:
                MobHandler.SpawnFamiliar(slea, c, c.getPlayer());
                break;
            case RENAME_FAMILIAR:
                MobHandler.RenameFamiliar(slea, c, c.getPlayer());
                break;
            case MOVE_FAMILIAR:
                MobHandler.MoveFamiliar(slea, c, c.getPlayer());
                break;
            case ATTACK_FAMILIAR:
                MobHandler.AttackFamiliar(slea, c, c.getPlayer());
                break;
            case TOUCH_FAMILIAR:
                MobHandler.TouchFamiliar(slea, c, c.getPlayer());
                break;
            case REVEAL_FAMILIAR:
                break;
            case USE_RECIPE:
                ItemMakerHandler.UseRecipe(slea, c, c.getPlayer());
                break;
            case MOVE_HAKU:
                PlayerHandler.MoveHaku(slea, c, c.getPlayer());
                break;
            case HAKU_ACTION:
                PlayerHandler.ChangeHaku(slea, c, c.getPlayer());
                break;
            case MOVE_ANDROID:
                PlayerHandler.MoveAndroid(slea, c, c.getPlayer());
                break;
            case FACE_EXPRESSION:
                PlayerHandler.ChangeEmotion(slea.readInt(), c.getPlayer());
                break;
            case FACE_ANDROID:
                PlayerHandler.ChangeAndroidEmotion(slea.readInt(), c.getPlayer());
                break;
            case TAKE_DAMAGE:
                PlayerHandler.TakeDamage(slea, c, c.getPlayer());
                break;
            case HEAL_OVER_TIME:
                PlayerHandler.Heal(slea, c.getPlayer());
                break;
            case CANCEL_BUFF:
                PlayerHandler.CancelBuffHandler(slea.readInt(), c.getPlayer());
                break;
            case MECH_CANCEL:
                PlayerHandler.CancelMech(slea, c.getPlayer());
                break;
            case CANCEL_ITEM_EFFECT:
                PlayerHandler.CancelItemEffect(slea.readInt(), c.getPlayer());
                break;
            case USE_TITLE:
                PlayerHandler.UseTitle(slea.readInt(), c, c.getPlayer());
                break;
            case ANGELIC_CHANGE:
                PlayerHandler.AngelicChange(slea, c, c.getPlayer());
                break;
            case DRESSUP_TIME:
                PlayerHandler.DressUpTime(slea, c);
                break;
            case USE_CHAIR:
                PlayerHandler.UseChair(slea, c, c.getPlayer());
                break;
            case CANCEL_CHAIR:
                PlayerHandler.CancelChair(slea.readShort(), c, c.getPlayer());
                break;
            case WHEEL_OF_FORTUNE:
                break; //whatever
            case USE_ITEMEFFECT:
                PlayerHandler.UseItemEffect(slea.readInt(), c, c.getPlayer());
                break;
            case SKILL_EFFECT:
                PlayerHandler.SkillEffect(slea, c.getPlayer());
                break;
            case QUICK_SLOT:
                PlayerHandler.QuickSlot(slea, c.getPlayer());
                break;
            case KAISER_QUICK_KEY:
                PlayerHandler.KaiserQuickKey(slea, c.getPlayer());
                break;
            case MESO_DROP:
                c.getPlayer().updateTick(slea.readInt());
                PlayerHandler.DropMeso(slea.readInt(), c.getPlayer());
                break;
            case CHANGE_KEYMAP:
                PlayerHandler.ChangeKeymap(slea, c.getPlayer());
                break;
            case PET_BUFF:
                PlayerHandler.ChangePetBuff(slea, c.getPlayer());
                break;
            case UPDATE_ENV:
                // We handle this in MapleMap
                break;
            case CHANGE_MAP:
                if (c.getPlayer().getMap() == null) {
                    CashShopOperation.LeaveCS(slea, c, c.getPlayer());
                } else {
                    PlayerHandler.ChangeMap(slea, c, c.getPlayer());
                }
                break;
            case CHANGE_MAP_SPECIAL:
                slea.skip(1);
                PlayerHandler.ChangeMapSpecial(slea.readMapleAsciiString(), c, c.getPlayer());
                break;
            case USE_INNER_PORTAL:
                slea.skip(1);
                PlayerHandler.InnerPortal(slea, c, c.getPlayer());
                break;
            case TROCK_ADD_MAP:
                PlayerHandler.TrockAddMap(slea, c, c.getPlayer());
                break;
            case LIE_DETECTOR:
            case LIE_DETECTOR_SKILL:
                //PlayersHandler.LieDetector(slea, c, c.getPlayer(), header == RecvPacketOpcode.LIE_DETECTOR);
                break;
            case LIE_DETECTOR_RESPONSE:
                //PlayersHandler.LieDetectorResponse(slea, c);
                break;
            case ARAN_COMBO:
                PlayerHandler.AranCombo(c, c.getPlayer(), 1);
                break;
            case SKILL_MACRO:
                PlayerHandler.ChangeSkillMacro(slea, c.getPlayer());
                break;
            case GIVE_FAME:
                PlayersHandler.GiveFame(slea, c, c.getPlayer());
                break;
            case TRANSFORM_PLAYER:
                PlayersHandler.TransformPlayer(slea, c, c.getPlayer());
                break;
            case NOTE_ACTION:
                PlayersHandler.Note(slea, c.getPlayer());
                break;
            case USE_DOOR:
                PlayersHandler.UseDoor(slea, c.getPlayer());
                break;
            case USE_MECH_DOOR:
                PlayersHandler.UseMechDoor(slea, c.getPlayer());
                break;
            case DAMAGE_REACTOR:
                PlayersHandler.HitReactor(slea, c);
                break;
            case CLICK_REACTOR:
            case TOUCH_REACTOR:
                PlayersHandler.TouchReactor(slea, c);
                break;
            case TOUCH_RUNE:
                PlayersHandler.TouchRune(slea, c.getPlayer());
                break;
            case USE_RUNE:
                PlayersHandler.UseRune(slea, c.getPlayer());
                break;
            case CLOSE_CHALKBOARD:
                c.getPlayer().setChalkboard(null);
                break;
            case ITEM_SORT:
                InventoryHandler.ItemSort(slea, c);
                break;
            case ITEM_GATHER:
                InventoryHandler.ItemGather(slea, c);
                break;
            case ITEM_MOVE:
                InventoryHandler.ItemMove(slea, c);
                break;
            case MOVE_BAG:
                InventoryHandler.MoveBag(slea, c);
                break;
            case SWITCH_BAG:
                InventoryHandler.SwitchBag(slea, c);
                break;
            case ITEM_MAKER:
                ItemMakerHandler.ItemMaker(slea, c);
                break;
            case ITEM_PICKUP:
                InventoryHandler.Pickup_Player(slea, c, c.getPlayer());
                break;
            case USE_CASH_ITEM:
                InventoryHandler.UseCashItem(slea, c);
                break;
            case USE_ITEM:
                InventoryHandler.UseItem(slea, c, c.getPlayer());
                break;
            case USE_COSMETIC:
                InventoryHandler.UseCosmetic(slea, c, c.getPlayer());
                break;
            case USE_SOUL_ENCHANTER:
                InventoryHandler.UseSoulEnchanter(slea, c, c.getPlayer());
                break;
            case USE_SOUL_SCROLL:
                InventoryHandler.UseSoulScroll(slea, c, c.getPlayer());
                break;
            case EQUIPMENT_ENCHANT:
                EquipmentEnchant.handlePacket(slea, c);
                break;
            case USE_MAGNIFY_GLASS:
                InventoryHandler.UseMagnify(slea, c);
                break;
            case USE_SCRIPTED_NPC_ITEM:
                InventoryHandler.UseScriptedNPCItem(slea, c, c.getPlayer());
                break;
            case USE_RETURN_SCROLL:
                InventoryHandler.UseReturnScroll(slea, c, c.getPlayer());
                break;
            case USE_NEBULITE:
                InventoryHandler.UseNebulite(slea, c);
                break;
            case USE_ALIEN_SOCKET:
                InventoryHandler.UseAlienSocket(slea, c);
                break;
            case USE_ALIEN_SOCKET_RESPONSE:
                slea.skip(4); // all 0
                c.getSession().write(CSPacket.useAlienSocket(false));
                break;
            case GOLDEN_HAMMER:
                InventoryHandler.UseGoldenHammer(slea, c);
                break;
            case VICIOUS_HAMMER:
                slea.skip(4);
                c.getSession().write(CSPacket.GoldenHammer((byte) 2, slea.readInt()));
                break;
            case PLATINUM_HAMMER:
                InventoryHandler.UsePlatinumHammer(slea, c);
                break;
            case USE_NEBULITE_FUSION:
                InventoryHandler.UseNebuliteFusion(slea, c);
                break;
            case USE_UPGRADE_SCROLL:
                c.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseUpgradeScroll(slea.readShort(), slea.readShort(), slea.readShort(), c, c.getPlayer(), slea.readByte() > 0);
                break;
            case USE_FLAG_SCROLL:
            case USE_EQUIP_SCROLL:
            case USE_POTENTIAL_SCROLL:
            case USE_BONUS_POTENTIAL_SCROLL:
                c.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseUpgradeScroll(slea.readShort(), slea.readShort(), (short) 0, c, c.getPlayer(), false);//slea.readByte() > 0);
                break;
            case USE_ABYSS_SCROLL:
                InventoryHandler.UseAbyssScroll(slea, c);
                break;
            case USE_CARVED_SEAL:
                InventoryHandler.UseCarvedSeal(slea, c);
                break;
            case USE_CRAFTED_CUBE:
                InventoryHandler.UseCube(slea, c);
                break;
            case USE_FLASH_CUBE:
                InventoryHandler.UseFlashCube(slea, c);
                break;
            case USE_SUMMON_BAG:
                InventoryHandler.UseSummonBag(slea, c, c.getPlayer());
                break;
            case USE_TREASURE_CHEST:
                InventoryHandler.UseTreasureChest(slea, c, c.getPlayer());
                break;
            case USE_SKILL_BOOK:
                c.getPlayer().updateTick(slea.readInt());
                InventoryHandler.UseSkillBook((byte) slea.readShort(), slea.readInt(), c, c.getPlayer());
                break;
            case USE_EXP_POTION:
                InventoryHandler.UseExpPotion(slea, c, c.getPlayer());
                break;
            case USE_ADDITIONAL_ITEM:
                InventoryHandler.UseAdditionalItem(slea, c);
                break;
            case USE_MOUNT_FOOD:
                InventoryHandler.UseMountFood(slea, c, c.getPlayer());
                break;
            case REWARD_ITEM:
                InventoryHandler.UseRewardItem(slea, c, c.getPlayer());
                break;
            case SOLOMON_EXP:
                InventoryHandler.UseExpItem(slea, c, c.getPlayer());
                break;
            case HYPNOTIZE_DMG:
                MobHandler.HypnotizeDmg(slea, c.getPlayer());
                break;
            case MOB_NODE:
                MobHandler.MobNode(slea, c.getPlayer());
                break;
            case DISPLAY_NODE:
                MobHandler.DisplayNode(slea, c.getPlayer());
                break;
            case MOVE_LIFE:
                MobHandler.MoveMonster(slea, c, c.getPlayer());
                break;
            case AUTO_AGGRO:
                MobHandler.AutoAggro(slea.readInt(), c.getPlayer());
                break;
            case FRIENDLY_DAMAGE:
                MobHandler.FriendlyDamage(slea, c.getPlayer());
                break;
            case REISSUE_MEDAL:
                PlayerHandler.ReIssueMedal(slea, c, c.getPlayer());
                break;
            case MONSTER_BOMB:
                MobHandler.MonsterBomb(slea.readInt(), c.getPlayer());
                break;
            case MOB_BOMB:
                MobHandler.MobBomb(slea, c.getPlayer());
                break;
            case NPC_SHOP:
                NPCHandler.NPCShop(slea, c, c.getPlayer());
                break;
            case NPC_TALK:
                NPCHandler.NPCTalk(slea, c, c.getPlayer());
                break;
            case NPC_TALK_MORE:
                NPCHandler.NPCMoreTalk(slea, c);
                break;
            case DIRECTION_COMPLETE:
                NPCHandler.DirectionComplete(slea, c);
                break;
            case NPC_ACTION:
                NPCHandler.NPCAnimation(slea, c);
                break;
            case QUEST_ACTION:
                NPCHandler.QuestAction(slea, c, c.getPlayer());
                break;
            case TOT_GUIDE:
                break;
            case STORAGE:
                NPCHandler.Storage(slea, c, c.getPlayer());
                break;
            case GENERAL_CHAT:
                if (c.getPlayer() != null && c.getPlayer().getMap() != null) {
                    c.getPlayer().updateTick(slea.readInt());
                    ChatHandler.GeneralChat(slea.readMapleAsciiString(), slea.readByte(), c, c.getPlayer());
                }
                break;
            case PARTYCHAT:
                ChatHandler.Others(slea, c, c.getPlayer());
                break;
            case COMMAND:
                ChatHandler.Command(slea, c);
                break;
            case MESSENGER:
                ChatHandler.Messenger(slea, c);
                break;
            case AUTO_ASSIGN_AP:
                StatsHandling.AutoAssignAP(slea, c, c.getPlayer());
                break;
            case DISTRIBUTE_AP:
                StatsHandling.DistributeAP(slea, c, c.getPlayer());
                break;
            case DISTRIBUTE_SP:
                StatsHandling.DistributeSP(slea, c, c.getPlayer());
                break;
            case PLAYER_INTERACTION:
                PlayerInteractionHandler.PlayerInteraction(slea, c, c.getPlayer());
                break;
            case ADMIN_CHAT:
                ChatHandler.AdminChat(slea, c, c.getPlayer());
                break;
            case ADMIN_COMMAND:
                PlayerHandler.AdminCommand(slea, c, c.getPlayer());
                break;
            case ADMIN_LOG:
                CommandProcessor.logCommandToDB(c.getPlayer(), slea.readMapleAsciiString(), "adminlog");
                break;
            case GUILD_OPERATION:
                GuildHandler.Guild(slea, c);
                break;
            case DENY_GUILD_REQUEST:
                slea.skip(1);
                GuildHandler.DenyGuildRequest(slea.readMapleAsciiString(), c);
                break;
            case JOIN_GUILD_REQUEST:
                GuildHandler.JoinGuildRequest(slea.readInt(), c);
                break;
            case JOIN_GUILD_CANCEL:
                GuildHandler.JoinGuildCancel(c);
                break;
            case ALLOW_GUILD_JOIN:
                GuildHandler.AddGuildMember(slea, c);
                break;
            case DENY_GUILD_JOIN:
                GuildHandler.DenyGuildJoin(slea, c);
                break;
            case ALLIANCE_OPERATION:
                AllianceHandler.HandleAlliance(slea, c, false);
                break;
            case DENY_ALLIANCE_REQUEST:
                AllianceHandler.HandleAlliance(slea, c, true);
                break;
            case QUICK_MOVE:
                NPCHandler.OpenQuickMoveNpc(slea, c);
                break;
            case QUICK_MOVE_SPECIAL:
                NPCHandler.OpenQuickMoveSpecial(slea, c);
                break;
            case ZERO_QUICK_MOVE:
                NPCHandler.OpenZeroQuickMoveSpecial(slea, c);
                break;
            case BBS_OPERATION:
                BBSHandler.BBSOperation(slea, c);
                break;
            case PARTY_OPERATION:
                PartyHandler.PartyOperation(slea, c);
                break;
            case DENY_PARTY_REQUEST:
                PartyHandler.DenyPartyRequest(slea, c);
                break;
            case ALLOW_PARTY_INVITE:
                PartyHandler.AllowPartyInvite(slea, c);
                break;
            case BUDDYLIST_MODIFY:
                BuddyListHandler.BuddyOperation(slea, c);
                break;
            case CYGNUS_SUMMON:
                UserInterfaceHandler.CygnusSummon_NPCRequest(c);
                break;
            case SHIP_OBJECT:
                UserInterfaceHandler.ShipObjectRequest(slea.readInt(), c);
                break;
            case BUY_CS_ITEM:
                CashShopOperation.BuyCashItem(slea, c, c.getPlayer());
                break;
            case CS_GIFT:
                CashShopOperation.sendCSgift(slea, c);
                break;
            case COUPON_CODE:
                slea.skip(2);
                String code = slea.readMapleAsciiString();
                CashShopOperation.CouponCode(code, c);
//                CashShopOperation.doCSPackets(c);
                break;
            case CASH_CATEGORY:
                CashShopOperation.SwitchCategory(slea, c);
                break;
            case TWIN_DRAGON_EGG:
                System.out.println("TWIN_DRAGON_EGG: " + slea.toString());
                final CashItemInfo item = CashItemFactory.getInstance().getItem(10003055);
                Item itemz = c.getPlayer().getCashInventory().toItem(item);
                //Aristocat c.getSession().write(CSPacket.sendTwinDragonEgg(true, true, 38, itemz, 1));
                break;
            case XMAS_SURPRISE:
                System.out.println("XMAS_SURPRISE: " + slea.toString());
                break;
            case CS_UPDATE:
                CashShopOperation.CSUpdate(c);
                break;
            case USE_POT:
                ItemMakerHandler.UsePot(slea, c);
                break;
            case CLEAR_POT:
                ItemMakerHandler.ClearPot(slea, c);
                break;
            case FEED_POT:
                ItemMakerHandler.FeedPot(slea, c);
                break;
            case CURE_POT:
                ItemMakerHandler.CurePot(slea, c);
                break;
            case REWARD_POT:
                ItemMakerHandler.RewardPot(slea, c);
                break;
            case DAMAGE_SUMMON:
                slea.skip(4);
                SummonHandler.DamageSummon(slea, c.getPlayer());
                break;
            case MOVE_SUMMON:
                SummonHandler.MoveSummon(slea, c.getPlayer());
                break;
            case MOVE_DRAGON:
                SummonHandler.MoveDragon(slea, c.getPlayer());
                break;
            case SUB_SUMMON:
                SummonHandler.SubSummon(slea, c.getPlayer());
                break;
            case REMOVE_SUMMON:
                SummonHandler.RemoveSummon(slea, c);
                break;
            case SPAWN_PET:
                PetHandler.SpawnPet(slea, c, c.getPlayer());
                break;
            case MOVE_PET:
                PetHandler.MovePet(slea, c.getPlayer());
                break;
            case PET_CHAT:
                //System.out.println("Pet chat: " + slea.toString());
                if (slea.available() < 8) {
                    break;
                }
                final int petid = c.getPlayer().getPetIndex(slea.readInt());
                c.getPlayer().updateTick(slea.readInt());
                PetHandler.PetChat(petid, slea.readShort(), slea.readMapleAsciiString(), c.getPlayer());
                break;
            case PET_COMMAND:
                MaplePet pet;
                pet = c.getPlayer().getPet(c.getPlayer().getPetIndex(slea.readInt()));
                slea.readByte(); //always 0?
                if (pet == null) {
                    return;
                }
                PetHandler.PetCommand(pet, PetDataFactory.getPetCommand(pet.getPetItemId(), slea.readByte()), c, c.getPlayer());
                break;
            case PET_FOOD:
                PetHandler.PetFood(slea, c, c.getPlayer());
                break;
            case PET_LOOT:
                //System.out.println("PET_LOOT ACCESSED");
                InventoryHandler.Pickup_Pet(slea, c, c.getPlayer());
                break;
            case PET_AUTO_POT:
                PetHandler.Pet_AutoPotion(slea, c, c.getPlayer());
                break;
            case MONSTER_CARNIVAL:
                MonsterCarnivalHandler.MonsterCarnival(slea, c);
                break;
            case PACKAGE_OPERATION:
                PackageHandler.handleAction(slea, c);
                break;
            case USE_HIRED_MERCHANT:
                HiredMerchantHandler.UseHiredMerchant(c, true);
                break;
            case MERCH_ITEM_STORE:
                HiredMerchantHandler.MerchantItemStore(slea, c);
                break;
            case CANCEL_DEBUFF:
                // Ignore for now
                break;
            case MAPLETV:
                break;
            case LEFT_KNOCK_BACK:
                PlayerHandler.leftKnockBack(slea, c);
                break;
            case SNOWBALL:
                PlayerHandler.snowBall(slea, c);
                break;
            case COCONUT:
                PlayersHandler.hitCoconut(slea, c);
                break;
            case REPAIR:
                NPCHandler.repair(slea, c);
                break;
            case REPAIR_ALL:
                NPCHandler.repairAll(c);
                break;
            case BUY_SILENT_CRUSADE:
                PlayersHandler.buySilentCrusade(slea, c);
                break;
            //case GAME_POLL:
            //    UserInterfaceHandler.InGame_Poll(slea, c);
            //    break;
            case OWL:
                InventoryHandler.Owl(slea, c);
                break;
            case OWL_WARP:
                InventoryHandler.OwlWarp(slea, c);
                break;
            case USE_OWL_MINERVA:
                InventoryHandler.OwlMinerva(slea, c);
                break;
            case RPS_GAME:
                NPCHandler.RPSGame(slea, c);
                break;
            case UPDATE_QUEST:
                NPCHandler.UpdateQuest(slea, c);
                break;
            case USE_ITEM_QUEST:
                NPCHandler.UseItemQuest(slea, c);
                break;
            case FOLLOW_REQUEST:
                PlayersHandler.FollowRequest(slea, c);
                break;
            case AUTO_FOLLOW_REPLY:
            case FOLLOW_REPLY:
                PlayersHandler.FollowReply(slea, c);
                break;
            case RING_ACTION:
                PlayersHandler.RingAction(slea, c);
                break;
            case REQUEST_FAMILY:
                FamilyHandler.RequestFamily(slea, c);
                break;
            case OPEN_FAMILY:
                FamilyHandler.OpenFamily(slea, c);
                break;
            case FAMILY_OPERATION:
                FamilyHandler.FamilyOperation(slea, c);
                break;
            case DELETE_JUNIOR:
                FamilyHandler.DeleteJunior(slea, c);
                break;
            case DELETE_SENIOR:
                FamilyHandler.DeleteSenior(slea, c);
                break;
            case USE_FAMILY:
                FamilyHandler.UseFamily(slea, c);
                break;
            case FAMILY_PRECEPT:
                FamilyHandler.FamilyPrecept(slea, c);
                break;
            case FAMILY_SUMMON:
                FamilyHandler.FamilySummon(slea, c);
                break;
            case ACCEPT_FAMILY:
                FamilyHandler.AcceptFamily(slea, c);
                break;
            case SOLOMON:
                PlayersHandler.Solomon(slea, c);
                break;
            case GACH_EXP:
                PlayersHandler.GachExp(slea, c);
                break;
            case MEMBER_SEARCH:
                PartyHandler.MemberSearch(slea, c);
                break;
            case PARTY_SEARCH:
                PartyHandler.PartySearch(slea, c);
                break;
            case EXPEDITION_LISTING:
                PartyHandler.PartyListing(slea, c);
                break;
            case EXPEDITION_OPERATION:
                PartyHandler.Expedition(slea, c);
                break;
            case USE_TELE_ROCK:
                InventoryHandler.TeleRock(slea, c);
                break;
            case AZWAN_REVIVE:
                PlayersHandler.reviveAzwan(slea, c);
                break;
            case PLAYER_UPDATE:
                break;
            case INNER_CIRCULATOR:
                InventoryHandler.useInnerCirculator(slea, c);
                break;
            case PAM_SONG:
                InventoryHandler.PamSong(slea, c);
                break;
            case REPORT:
                PlayersHandler.Report(slea, c);
                break;
            //working
            case CANCEL_OUT_SWIPE:
                slea.readInt();
                break;
            //working
            case VIEW_SKILLS:
                PlayersHandler.viewSkills(slea, c);
                break;
            //working
            case SKILL_SWIPE:
                PlayersHandler.StealSkill(slea, c);
                break;
            case CHOOSE_SKILL:
                PlayersHandler.ChooseSkill(slea, c);
                break;
            case RELEASE_TEMPEST_BLADES:
                PlayerHandler.releaseTempestBlades(slea, c.getPlayer());
                break;
            case MAGIC_WHEEL:
                System.out.println("[MAGIC_WHEEL] [" + slea.toString() + "]");
                PlayersHandler.magicWheel(slea, c);
                break;
            case REWARD:
                PlayersHandler.onReward(slea, c);
                break;
            case BLACK_FRIDAY:
                PlayersHandler.blackFriday(slea, c);
            case UPDATE_RED_LEAF:
                PlayersHandler.updateRedLeafHigh(slea, c);
                break;
            case SPECIAL_STAT:
                PlayersHandler.updateSpecialStat(slea, c);
                break;
            case UPDATE_HYPER:
                StatsHandling.DistributeHyper(slea, c, c.getPlayer());
                break;
            case RESET_HYPER:
                StatsHandling.ResetHyper(slea, c, c.getPlayer());
                break;
            case DF_COMBO:
                PlayerHandler.absorbingDF(slea, c);
                break;
            case EXIT_GAME:
                c.getSession().write(CField.exitGame());
                break;
            case BACK_TO_CHARLIST:
                InterServerHandler.BackToCharList(slea, c);
                break;
            case MESSENGER_RANKING:
                PlayerHandler.MessengerRanking(slea, c, c.getPlayer());
                break;
            case OS_INFORMATION:
                System.out.println(c.getSessionIPAddress());
                break;
            case BUFF_RESPONSE:
                break;
            case BUTTON_PRESSED:
                break;
            case CASSANDRAS_COLLECTION:
                PlayersHandler.CassandrasCollection(slea, c);
                break;
            case LUCKY_LUCKY_MONSTORY:
                PlayersHandler.LuckyLuckyMonstory(slea, c);
                break;
            case CHRONOSPHERE:
                PlayersHandler.UseChronosphere(slea, c, c.getPlayer());
                break;
            case GUIDE_TRANSFER:
                PlayerHandler.GuildTransfer(slea, c, c.getPlayer());//遊戲嚮導
                break;
            case ALLOW_PET_LOOT:
                PetHandler.AllowPetLoot(slea, c, c.getPlayer());
                break;
            case ARROW_BLASTER_ACTION:
                PlayerHandler.ArrowBlasterAction(slea, c, c.getPlayer());
                break;
            case SYSTEM_PROCESS_LIST:
                SystemProcess.SystemProcess(slea, c, c.getPlayer());
                break;
            case ZERO_SCROLL_START:
                PlayerHandler.ZeroHandler.ZeroScrollStart(slea, c.getPlayer(), c);
                break;
            case ZERO_WEAPON_UI:
                PlayerHandler.ZeroHandler.openWeaponUI(slea, c);
                break;
            case ZERO_NPC_TALK:
                PlayerHandler.ZeroHandler.talkZeroNpc(slea, c);
                break;
            case ZERO_WEAPON_SCROLL:
                PlayerHandler.ZeroHandler.useZeroScroll(slea, c);
                break;
            case ZERO_WEAPON_UPGRADE:
                PlayerHandler.ZeroHandler.openZeroUpgrade(slea, c);
                break;
	    case ZERO_WEAPON_UPGRADE_START:
                break;
            case ZERO_WEAPON_ABILITY:
                break;
            case LOAD_PLAYER_SCCUCESS:
                PlayerHandler.LoadPlayerSuccess(c, c.getPlayer());
                break;
            case ATTACK_ON_TITAN_SELECT:
                if (c.getPlayer().getMapId() != 814000000) {
                    break;
                }
                int select = slea.readInt();
                if (select == 0) {
                    select = 814000600;
                } else if (select == 1) {
                    select = 814010000;
                } else {
                    select = 814030000;
                }
                final MapleMap mapz = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(select);
                c.getPlayer().changeMap(mapz, mapz.getPortal(0));
                break;
            case EFFECT_SWITCH:
                PlayerHandler.EffectSwitch(slea, c);
                break;
            default:
                System.err.println("[發現未處理數據包] Recv [" + header.toString() + "]");
                break;
        }
    }
}
