package handling.channel.handler;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleJob;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.ServerConstants;
import handling.MapleServerHandler;
import handling.cashshop.CashShopServer;
import handling.cashshop.handler.CashShopOperation;
import handling.channel.ChannelServer;
import handling.farm.FarmServer;
import handling.login.LoginServer;
import handling.world.*;
import handling.world.exped.MapleExpedition;
import handling.world.guild.*;
import java.util.ArrayList;
import java.util.List;
import server.*;
import server.maps.FieldLimitType;
import server.maps.MapleMap;
import server.quest.MapleQuest;
import tools.FileoutputUtil;
import tools.Triple;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CSPacket;
import tools.packet.CWvsContext;
import tools.packet.CWvsContext.BuddylistPacket;
import tools.packet.CWvsContext.GuildPacket;
import tools.packet.CWvsContext.InventoryPacket;
import tools.packet.FarmPacket;
import tools.packet.JobPacket.AvengerPacket;

public class InterServerHandler {

    public static void EnterMTS(MapleClient c, MapleCharacter chr) {
        if (chr.卡圖 == chr.getMapId() && chr.getMapId() / 1000000 != 4) {
            chr.changeMap(100000000, 0);
        }
        chr.卡圖 = 0;
        c.getSession().write(CWvsContext.enableActions());
    }

    public static void BackToCharList(final LittleEndianAccessor slea, MapleClient c) {
        final String account = slea.readMapleAsciiString();
        if (!ServerConstants.REDIRECTOR || !c.getAccountName().equals(account)) {
            c.getSession().close(true);
        } else {
            if (c.getChannel() <= MapleServerHandler.LOGIN_SERVER) {
                return;
            }
            c.disconnect(true, false);
            c.getSession().write(CField.autoLogin(World.Redirector.addRedirector(c)));
        }
    }

    public static void EnterCS(final MapleClient c, final MapleCharacter chr) {
        if (chr.hasBlockedInventory() || chr.getMap() == null || chr.getEventInstance() != null || c.getChannelServer() == null) {
            c.getSession().write(CField.serverBlocked(2));
            CharacterTransfer farmtransfer = FarmServer.getPlayerStorage().getPendingCharacter(chr.getId());
            if (farmtransfer != null) {
                c.getSession().write(FarmPacket.farmMessage("You cannot move into Cash Shop while visiting your farm, yet."));
            }
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (World.getPendingCharacterSize(chr.getWorld()) >= 10) {
            chr.dropMessage(1, "The server is busy at the moment. Please try again in a minute or less.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        ChannelServer ch = ChannelServer.getInstance(c.getChannel());
        chr.changeRemoval();
        if (chr.getMessenger() != null) {
            MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(chr);
            World.Messenger.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
        }
        PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
        PlayerBuffStorage.addStackBuffsToStorage(chr.getId(), chr.getSpecialBuffInfo());
        PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
        PlayerBuffStorage.addDiseaseToStorage(chr.getId(), chr.getAllDiseases());
        World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), -10);
        ch.removePlayer(chr);
        c.updateLoginState(3, c.getSessionIPAddress());
        chr.saveToDB(false, false);
        chr.getMap().removePlayer(chr);
        c.getSession().write(CField.getChannelChange(c, Integer.parseInt(CashShopServer.getIP().split(":")[1])));
        c.setPlayer(null);
        c.setReceiving(false);
    }

    public static void EnterFarm(final MapleClient c, final MapleCharacter chr) {
        if (chr.hasBlockedInventory() || chr.getMap() == null || chr.getEventInstance() != null || c.getChannelServer() == null) {
            c.getSession().write(CField.serverBlocked(2));
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (World.getPendingCharacterSize(chr.getWorld()) >= 10) {
            chr.dropMessage(1, "The server is busy at the moment. Please try again in a minute or less.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        ChannelServer ch = ChannelServer.getInstance(c.getChannel());
        chr.changeRemoval();
        if (chr.getMessenger() != null) {
            MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(chr);
            World.Messenger.leaveMessenger(chr.getMessenger().getId(), messengerplayer);
        }
        PlayerBuffStorage.addBuffsToStorage(chr.getId(), chr.getAllBuffs());
        PlayerBuffStorage.addStackBuffsToStorage(chr.getId(), chr.getSpecialBuffInfo());
        PlayerBuffStorage.addCooldownsToStorage(chr.getId(), chr.getCooldowns());
        PlayerBuffStorage.addDiseaseToStorage(chr.getId(), chr.getAllDiseases());
        World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), -30);
        ch.removePlayer(chr);
        c.updateLoginState(3, c.getSessionIPAddress());
        chr.saveToDB(false, false);
        chr.getMap().removePlayer(chr);
        c.getSession().write(CField.getChannelChange(c, Integer.parseInt(FarmServer.getIP().split(":")[1])));
        c.setPlayer(null);
        c.setReceiving(false);
    }

    public static void Loggedin(final int playerid, final MapleClient c) {
//        try {
        MapleCharacter player;
        CharacterTransfer transfer = CashShopServer.getPlayerStorage().getPendingCharacter(playerid);
        if (transfer != null) {
//            c.getSession().write(CWvsContext.BuffPacket.cancelBuff());
            System.out.println("進入商城");
            CashShopOperation.EnterCS(transfer, c);
            return;
        }
//        CharacterTransfer farmtransfer = FarmServer.getPlayerStorage().getPendingCharacter(playerid);//國服沒有農場
//        if (farmtransfer != null) {
//            System.out.println("Enter Farm");
//            FarmOperation.EnterFarm(farmtransfer, c);
//            return;
//        }
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            transfer = cserv.getPlayerStorage().getPendingCharacter(playerid);
            if (transfer != null) {
                System.out.println("Set channel to " + cserv.getChannel());
                c.setChannel(cserv.getChannel());
                break;
            }
        }
        if (transfer == null) { // Player isn't in storage, probably isn't CC
            Triple<String, String, Integer> ip = LoginServer.getLoginAuth(playerid);
            String s = c.getSessionIPAddress();
            if (ip == null || !s.substring(s.indexOf('/') + 1, s.length()).equals(ip.left)) {
                if (ip != null) {
                    LoginServer.putLoginAuth(playerid, ip.left, ip.mid, ip.right);
                }
                System.out.println("Session close");
                c.getSession().close(true);
                return;
            }
            c.setTempIP(ip.mid);
            c.setChannel(ip.right);
            player = MapleCharacter.loadCharFromDB(playerid, c, true);
            System.out.println("從數據庫中獲取角色");
        } else {
            player = MapleCharacter.ReconstructChr(transfer, c, true);
            System.out.println("重建角色");
        }
        final ChannelServer channelServer = c.getChannelServer();
        c.setPlayer(player);
        System.out.println("設置用戶端角色: " + player);
        c.setAccID(player.getAccountID());
        System.out.println("設置用戶端賬號ID: " + player.getAccountID());

        if (!c.CheckIPAddress()) { // Remote hack
            c.getSession().close(true);
            System.out.println("Remote Hack");
            return;
        }
        final int state = c.getLoginState();
        System.out.println("狀態 = " + c.getLoginState());
        boolean allowLogin = false;
        if (state == MapleClient.LOGIN_SERVER_TRANSITION || state == MapleClient.CHANGE_CHANNEL || state == MapleClient.LOGIN_NOTLOGGEDIN) {
            allowLogin = !World.isCharacterListConnected(c.loadCharacterNames(c.getWorld()));
        }
        if (!allowLogin) {
            System.err.println("允許登入 = false");
            c.getSession().close(true);
            System.err.println("斷線處理!");
            c.setPlayer(null);
            System.err.println("清除用戶端的角色記錄");
            return;
        }
        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        System.out.println("正在將角色添加到頻道");
        channelServer.addPlayer(player);

        player.giveCoolDowns(PlayerBuffStorage.getCooldownsFromStorage(player.getId()));
        player.silentGiveBuffs(PlayerBuffStorage.getBuffsFromStorage(player.getId()));
        player.silentGiveStackBuffs(PlayerBuffStorage.getStackBuffsFromStorage(player.getId()));
        player.giveSilentDebuff(PlayerBuffStorage.getDiseaseFromStorage(player.getId()));

        c.getSession().write(CWvsContext.updateCrowns(new int[]{-1, -1, -1, -1, -1}));
        // =====活動清單=====
//        List<String> eventMessage = new ArrayList();
//        String notice = "楓之谷4月~5月活動";
//        eventMessage.add("楓之谷秋天活動!");
//        eventMessage.add("[獅子王城] 全新的獅子王城");
//        eventMessage.add("[公會相關] 挑戰旗幟爭霸戰");
//        eventMessage.add("[吸血鬼城] 陰森的吸血鬼城大門已開啟");
//        eventMessage.add("[Happy Day] OPEN! 每天參加點名,領取每個月的11個道具");
//        c.getSession().write(CWvsContext.InfoPacket.getEventList(notice, eventMessage));
        // ===================
        c.getSession().write(CField.getCharInfo(player));
//        PlayersHandler.calcHyperSkillPointCount(c);
        c.getSession().write(CSPacket.enableCSUse());
        c.getSession().write(CWvsContext.updateMount(player, false));
//        c.getSession().write(CWvsContext.updateSkills(c.getPlayer().getSkills(), false));//skill to 0 "fix"
        c.getSession().write(InventoryPacket.updateInventorySlot());
        c.getSession().write(CWvsContext.temporaryStats_Reset());

        if (player.isIntern()) {//GM登入自動隱身並無敵處理
            SkillFactory.getSkill(9001004).getEffect(1).applyTo(player);
            player.setInvincible(true);
        }
        if (player.getQuestNoAdd(MapleQuest.getInstance(GameConstants.墜飾欄)) != null
                && player.getQuestNoAdd(MapleQuest.getInstance(GameConstants.墜飾欄)).getCustomData() != null
                && "0".equals(player.getQuestNoAdd(MapleQuest.getInstance(GameConstants.墜飾欄)).getCustomData())) {//更新永久墜飾欄
            c.getSession().write(CField.updatePendantSlot(0));
        }

        player.getMap().addPlayer(player);
        try {
            // Start of buddylist
            final int buddyIds[] = player.getBuddylist().getBuddyIds();
            World.Buddy.loggedOn(player.getName(), player.getId(), c.getChannel(), buddyIds);
            if (player.getParty() != null) {
                final MapleParty party = player.getParty();
                World.Party.updateParty(party.getId(), PartyOperation.LOG_ONOFF, new MaplePartyCharacter(player));

                if (party != null && party.getExpeditionId() > 0) {
                    final MapleExpedition me = World.Party.getExped(party.getExpeditionId());
                    if (me != null) {
                        c.getSession().write(CWvsContext.ExpeditionPacket.expeditionStatus(me, false, true));
                    }
                }
            }
            final CharacterIdChannelPair[] onlineBuddies = World.Find.multiBuddyFind(player.getId(), buddyIds);
            for (CharacterIdChannelPair onlineBuddy : onlineBuddies) {
                player.getBuddylist().get(onlineBuddy.getCharacterId()).setChannel(onlineBuddy.getChannel());
            }
            c.getSession().write(BuddylistPacket.updateBuddylist(player.getBuddylist().getBuddies()));

            // Start of Messenger
            final MapleMessenger messenger = player.getMessenger();
            if (messenger != null) {
                World.Messenger.silentJoinMessenger(messenger.getId(), new MapleMessengerCharacter(c.getPlayer()));
                World.Messenger.updateMessenger(messenger.getId(), c.getPlayer().getName(), c.getChannel());
            }

            // Start of Guild and alliance
            if (player.getGuildId() > 0) {
                World.Guild.setGuildMemberOnline(player.getMGC(), true, c.getChannel());
                c.getSession().write(GuildPacket.showGuildInfo(player));
                final MapleGuild gs = World.Guild.getGuild(player.getGuildId());
                if (gs != null) {
                    final List<byte[]> packetList = World.Alliance.getAllianceInfo(gs.getAllianceId(), true);
                    if (packetList != null) {
                        for (byte[] pack : packetList) {
                            if (pack != null) {
                                c.getSession().write(pack);
                            }
                        }
                    }
                } else { //guild not found, change guild id
                    player.setGuildId(0);
                    player.setGuildRank((byte) 5);
                    player.setAllianceRank((byte) 5);
                    player.saveGuildStatus();
                }
            }
            if (player.getFamilyId() > 0) {
                World.Family.setFamilyMemberOnline(player.getMFC(), true, c.getChannel());
            }
            //c.getSession().write(FamilyPacket.getFamilyData());
            //c.getSession().write(FamilyPacket.getFamilyInfo(player));
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.Login_Error, e);
        }
        player.getClient().getSession().write(CWvsContext.broadcastMsg(channelServer.getServerMessage(player.getWorld())));
        player.sendMacros();
        player.showNote();
        player.sendImp();
        player.updatePartyMemberHP();
        player.startFairySchedule(false);
        player.baseSkills(); //fix people who've lost skills.
        if (MapleJob.is神之子(player.getJob())) {
            c.getSession().write(CWvsContext.updateSkills(player.getSkills(), false));
        }
        int job = c.getPlayer().getJob();
        c.getSession().write(CField.getKeymap(player.getKeyLayout(), player));//fix keylayout?
        player.updatePetAuto();
        player.expirationTask(true, transfer == null);
        c.getSession().write(CWvsContext.updateMaplePoint(player.getCSPoints(2)));
        if (player.getJob() == 132) { // DARKKNIGHT
            player.checkBerserk();
        }
        if (MapleJob.is傑諾(player.getJob())) {
            player.startXenonSupply();
        }
        if (MapleJob.is惡魔復仇者(player.getJob())) {
            c.getSession().write(AvengerPacket.giveAvengerHpBuff(player.getStat().getHp()));
        }
        player.spawnClones();
        player.spawnSavedPets();
        if (player.getStat().equippedSummon > 0) {
            SkillFactory.getSkill(player.getStat().equippedSummon + (MapleJob.getBeginner(player.getJob()) * 1000)).getEffect(1).applyTo(player);
        }
        MapleInventory equipped = player.getInventory(MapleInventoryType.EQUIPPED);
        List<Short> slots = new ArrayList<>();
        for (Item item : equipped.newList()) {
            slots.add(item.getPosition());
        }

        if (c.getPlayer().isEquippedSoulWeapon() && transfer == null) {
            c.getPlayer().setSoulCount((short) 0);
            c.getSession().write(CWvsContext.BuffPacket.giveSoulGauge(c.getPlayer().getSoulCount(), c.getPlayer().getEquippedSoulSkill()));
        }

        //清理斷線未處理的方塊任務
        c.getPlayer().clearInfoQuest(GameConstants.台方塊);

        player.updateReward();
        player.getClient().getSession().write(CWvsContext.broadcastMsg(channelServer.getServerMessage(player.getWorld())));
//        Thread.sleep(1000);
//        c.getSession().write(CWvsContext.getTopMsg("Earned Forever Single title!"));
//        Thread.sleep(3100);
//        if (c.getPlayer().getLevel() < 11) { 
//            NPCScriptManager.getInstance().start(c, 9010000, "LoginTot");
//        }
//        } catch (InterruptedException e) {
//      }
    }

    public static final void ChangeChannel(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr, final boolean room) {
        if (chr == null || chr.hasBlockedInventory() || chr.getEventInstance() != null || chr.getMap() == null || chr.isInBlockedMap() || FieldLimitType.ChannelSwitch.check(chr.getMap().getFieldLimit())) {
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (World.getPendingCharacterSize(chr.getWorld()) >= 10) {
            chr.dropMessage(1, "The server is busy at the moment. Please try again in less than a minute.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        final int chc = slea.readByte() + 1;
        int mapid = 0;
        if (room) {
            mapid = slea.readInt();
        }
        chr.updateTick(slea.readInt());
        if (!World.isChannelAvailable(chc, chr.getWorld())) {
            chr.dropMessage(1, "Request denied due to an unknown error.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (room && (mapid < 910000001 || mapid > 910000022)) {
            chr.dropMessage(1, "Request denied due to an unknown error.");
            c.getSession().write(CWvsContext.enableActions());
            return;
        }
        if (room) {
            if (chr.getMapId() == mapid) {
                if (c.getChannel() == chc) {
                    chr.dropMessage(1, "You are already in " + chr.getMap().getMapName());
                    c.getSession().write(CWvsContext.enableActions());
                } else { // diff channel
                    chr.changeChannel(chc);
                }
            } else { // diff map
                if (c.getChannel() != chc) {
                    chr.changeChannel(chc);
                }
                final MapleMap warpz = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(mapid);
                if (warpz != null) {
                    chr.changeMap(warpz, warpz.getPortal("out00"));
                } else {
                    chr.dropMessage(1, "Request denied due to an unknown error.");
                    c.getSession().write(CWvsContext.enableActions());
                }
            }
        } else {
            chr.changeChannel(chc);
        }
    }
}
