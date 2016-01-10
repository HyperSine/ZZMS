package scripting;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleJob;
import client.MapleQuestStatus;
import client.MapleStat;
import client.Skill;
import client.SkillEntry;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.ItemConstants;
import constants.ServerConfig;
import tools.SearchGenerator;
import database.DatabaseConnection;
import handling.SendPacketOpcode;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import handling.channel.handler.HiredMerchantHandler;
import handling.channel.handler.InventoryHandler;
import handling.channel.handler.PlayersHandler;
import handling.login.LoginInformationProvider;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import handling.world.exped.ExpeditionType;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildAlliance;
import java.awt.Point;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.script.Invocable;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataFileEntry;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import server.MapleCarnivalChallenge;
import server.MapleCarnivalParty;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleSlideMenu;
import server.MapleSquad;
import server.MapleStatEffect;
import server.Randomizer;
import server.SpeedRunner;
import server.StructItemOption;
import server.Timer.CloneTimer;
import server.life.*;
import server.maps.Event_DojoAgent;
import server.maps.Event_PyramidSubway;
import server.maps.MapleMap;
import server.quest.MapleQuest;
import server.shops.MapleShopFactory;
import tools.FileoutputUtil;
import tools.Pair;
import tools.StringUtil;
import tools.Triple;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.CField;
import tools.packet.CField.NPCPacket;
import tools.packet.CField.UIPacket;
import tools.packet.CWvsContext;
import tools.packet.CWvsContext.GuildPacket;
import tools.packet.CWvsContext.InfoPacket;

public class NPCConversationManager extends AbstractPlayerInteraction {

    private String getText;
    private final ScriptType type; // -1 = NPC, 0 = start quest, 1 = end quest
    private byte lastMsg = -1;
    public boolean pendingDisposal = false;
    private final Invocable iv;

    public NPCConversationManager(MapleClient c, int npc, int questid, String npcscript, ScriptType type, Invocable iv) {
        super(c, npc, questid, npcscript);
        this.type = type;
        this.iv = iv;
    }

    public Invocable getIv() {
        return iv;
    }

    public int getNpc() {
        return id;
    }

    public int getQuest() {
        return id2;
    }

    public String getScript() {
        return script;
    }

    public ScriptType getType() {
        return type;
    }

    public void safeDispose() {
        pendingDisposal = true;
    }

    public void dispose() {
        NPCScriptManager.getInstance().dispose(c);
    }

    public void sendSlideMenu(final int type, final String sel) {
        int lasticon = 0;
        c.getSession().write(NPCPacket.getSlideMenu(id, type, lasticon, sel));
        lastMsg = 0x11;//was12
    }

    public String getDimensionalMirror(MapleCharacter character) {
        return MapleSlideMenu.SlideMenu0.getSelectionInfo(character, id);
    }

    public String getSlideMenuSelection(int type) {
        switch (type) {
            case 0:
                return MapleSlideMenu.SlideMenu0.getSelectionInfo(getPlayer(), id);
            case 1:
                return MapleSlideMenu.SlideMenu1.getSelectionInfo(getPlayer(), id);
            case 2:
                return MapleSlideMenu.SlideMenu2.getSelectionInfo(getPlayer(), id);
            case 3:
                return MapleSlideMenu.SlideMenu3.getSelectionInfo(getPlayer(), id);
            case 4:
                return MapleSlideMenu.SlideMenu4.getSelectionInfo(getPlayer(), id);
            case 5:
                return MapleSlideMenu.SlideMenu5.getSelectionInfo(getPlayer(), id);
            case 6:
                return MapleSlideMenu.SlideMenu6.getSelectionInfo(getPlayer(), id);
            default:
                return MapleSlideMenu.SlideMenu0.getSelectionInfo(getPlayer(), id);
        }
    }

    public int[] getSlideMenuDataIntegers(int type, int selection) {
        switch (type) {
            case 0:
                return MapleSlideMenu.SlideMenu0.getDataIntegers(selection);
            case 1:
                return MapleSlideMenu.SlideMenu1.getDataIntegers(selection);
            case 2:
                return MapleSlideMenu.SlideMenu2.getDataIntegers(selection);
            case 3:
                return MapleSlideMenu.SlideMenu3.getDataIntegers(selection);
            case 4:
                return MapleSlideMenu.SlideMenu4.getDataIntegers(selection);
            case 5:
                return MapleSlideMenu.SlideMenu5.getDataIntegers(selection);
            case 6:
                return MapleSlideMenu.SlideMenu6.getDataIntegers(selection);
            default:
                return MapleSlideMenu.SlideMenu0.getDataIntegers(selection);
        }
    }

    public void sendNext(String text) {
        sendNext(text, id);
    }

    public void sendNext(String text, int id) {
        if (text.contains("#L")) { //sendNext will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "00 01", (byte) 0));
        lastMsg = 0;
    }

    public void sendPlayerToNpc(String text) {
        sendNextS(text, (byte) 3, id);
    }

    public void sendNextNoESC(String text) {
        sendNextS(text, (byte) 1, id);
    }

    public void sendNextNoESC(String text, int id) {
        sendNextS(text, (byte) 1, id);
    }

    public void sendNextS(String text, byte type) {
        sendNextS(text, type, id);
    }

    public void sendNextS(String text, byte type, int idd) {
        sendNextS(text, type, idd, id);
    }

    public void sendNextS(String text, byte type, int idd, int npcid) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(npcid, (byte) 0, text, "00 01", type, idd));
        lastMsg = 0;
    }

    public void sendNextSNew(String text, byte type, byte type2) {
        sendNextSNew(text, type, type2, id);
    }

    public void sendNextSNew(String text, byte type, byte type2, int idd) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleSNew(text, type, type2);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "00 01", type, type2, idd));
        lastMsg = 0;
    }

    public void sendPrev(String text) {
        sendPrev(text, id);
    }

    public void sendPrev(String text, int id) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "01 00", (byte) 0));
        lastMsg = 0;
    }

    public void sendZeroTalk(String talk) {
        c.getSession().write(NPCPacket.getZeroNPCTalk(id, (byte) 0, talk, "01 00", (byte) 3, 0));
    }

    public void sendSangokuTalk(String talk, int idd, boolean next, boolean prev) {
        sendSangokuTalk(talk, (byte) 5, idd, next, prev);
    }

    public void sendSangokuTalk(boolean unknown, String talk, int idd, boolean next, boolean prev) {
        sendSangokuTalk(unknown, talk, (byte) 5, idd, next, prev);
    }

    public void sendSangokuTalk(String talk, int idd, boolean next, boolean prev, boolean pic) {
        sendSangokuTalk(talk, (byte) 5, idd, next, prev, pic);
    }

    public void sendSangokuTalk(boolean unknown, String talk, int idd, boolean next, boolean prev, boolean pic) {
        sendSangokuTalk(unknown, talk, (byte) 5, idd, next, prev, pic);
    }

    public void sendSangokuTalk(String talk, int type, int idd, boolean next, boolean prev) {
        sendSangokuTalk(false, talk, (byte) 0x19, (byte) type, idd, next, prev, true);
    }

    public void sendSangokuTalk(boolean unknown, String talk, int type, int idd, boolean next, boolean prev) {
        sendSangokuTalk(unknown, talk, (byte) 0x19, (byte) type, idd, next, prev, true);
    }

    public void sendSangokuTalk(String talk, int type, int idd, boolean next, boolean prev, boolean pic) {
        sendSangokuTalk(false, talk, (byte) 0x19, (byte) type, idd, next, prev, pic);
    }

    public void sendSangokuTalk(boolean unknown, String talk, int type, int idd, boolean next, boolean prev, boolean pic) {
        sendSangokuTalk(unknown, talk, (byte) 0x19, (byte) type, idd, next, prev, pic);
    }

    public void sendSangokuTalk(boolean unknown, String talk, int msgType, int type, int idd, boolean next, boolean prev, boolean pic) {
        c.getSession().write(NPCPacket.getSengokuNPCTalk(unknown, id, (byte) msgType, (byte) type, idd, talk, next, prev, pic));
        lastMsg = (byte) msgType;
    }

    public void sendArisanNPCTalk(String talk) { // 阿里山
        sendArisanNPCTalk(false, (byte) 0x0F, (byte) 0x38, (byte) 0x01, talk);
    }

    public void sendArisanNPCTalk(boolean read, byte msgType, byte type, byte result, String talk) {
        c.getSession().write(NPCPacket.getArisanNPCTalk(0, false, (byte) msgType, (byte) type, (byte) result, talk));
        lastMsg = (byte) msgType;
    }

    public void sendDreamWorldNPCTalk(String talk) { // 虛幻之森
        sendDreamWorldNPCTalk(false, (byte) 0x02, (byte) 0x05, (byte) 0x00, talk);
    }

    public void sendDreamWorldNPCTalk(boolean read, byte msgType, byte type, byte result, String talk) {
        c.getSession().write(NPCPacket.getDreamWorldNPCTalk(0, false, (byte) msgType, (byte) type, (byte) result, id, talk));
        lastMsg = (byte) msgType;
    }

    public final void DisableUI(final boolean enabled) {
        c.getSession().write(UIPacket.IntroDisableUI(enabled));
    }

    public void sendPrevS(String text, byte type) {
        sendPrevS(text, type, id);
    }

    public void sendPrevS(String text, byte type, int idd) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "01 00", type, idd));
        lastMsg = 0;
    }

    public void sendPrevSNew(String text, byte type, byte type2) {
        sendPrevSNew(text, type, type2, id);
    }

    public void sendPrevSNew(String text, byte type, byte type2, int idd) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleSNew(text, type, type2);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "01 00", type, type2, idd));
        lastMsg = 0;
    }

    public void sendNextPrev(String text) {
        sendNextPrev(text, id);
    }

    public void sendNextPrev(String text, int id) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "01 01", (byte) 0));
        lastMsg = 0;
    }

    public void PlayerToNpc(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    public void sendNextPrevS(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    public void sendNextPrevS(String text, byte type) {
        sendNextPrevS(text, type, id);
    }

    public void sendNextPrevS(String text, byte type, int idd) {
        sendNextPrevS(text, type, idd, id);
    }

    public void sendNextPrevS(String text, byte type, int idd, int npcid) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(npcid, (byte) 0, text, "01 01", type, idd));
        lastMsg = 0;
    }

    public void sendNextPrevSNew(String text, byte type, byte type2) {
        sendNextPrevSNew(text, type, type2, id);
    }

    public void sendNextPrevSNew(String text, byte type, byte type2, int idd) {
        sendNextPrevSNew(text, type, type2, idd, id);
    }

    public void sendNextPrevSNew(String text, byte type, byte type2, int idd, int npcid) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleSNew(text, type, type2);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(npcid, (byte) 0, text, "01 01", type, type2, idd));
        lastMsg = 0;
    }

    public void sendOk(String text) {
        sendOk(text, id);
    }

    public void sendOk(String text, int id) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "00 00", (byte) 0));
        lastMsg = 0;
    }

    public void sendOkS(String text, byte type) {
        sendOkS(text, type, id);
    }

    public void sendOkS(String text, byte type, int idd) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "00 00", type, idd));
        lastMsg = 0;
    }

    public void sendOkSNew(String text, byte type, byte type2) {
        sendOkSNew(text, type, type2, id);
    }

    public void sendOkSNew(String text, byte type, byte type2, int idd) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleSNew(text, type, type2);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 0, text, "00 00", type, type2, idd));
        lastMsg = 0;
    }

//    public void sendSelfTalk(String text, byte type) {
//        c.getSession().write(NPCPacket.getSelfTalkText(text));
//    }
    public void sendSelfTalk(String text) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type.getValue());
            return;
        }
        c.getSession().write(NPCPacket.getSelfTalkText(text));
        lastMsg = 0;
    }

    public void sendYesNo(String text) {
        sendYesNo(text, id);
    }

    public void sendYesNo(String text, int id) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 2, text, "", (byte) 0));
        lastMsg = 2;
    }

    public void sendYesNoS(String text, byte type) {
        sendYesNoS(text, type, id);
    }

    public void sendYesNoS(String text, byte type, int idd) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 2, text, "", type, idd));
        lastMsg = 2;
    }

    public void sendYesNoSNew(String text, byte type, byte type2) {
        sendYesNoSNew(text, type, type2, id);
    }

    public void sendYesNoSNew(String text, byte type, byte type2, int idd) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleSNew(text, type, type2);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 2, text, "", type, type2, idd));
        lastMsg = 2;
    }

    public void sendAcceptDecline(String text) {
        askAcceptDecline(text);
    }

    public void sendAcceptDeclineNoESC(String text) {
        askAcceptDeclineNoESC(text);
    }

    public void askAcceptDecline(String text) {
        askAcceptDecline(text, id);
    }

    public void askAcceptDecline(String text, int id) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        lastMsg = (byte) (0xF);
        c.getSession().write(NPCPacket.getNPCTalk(id, lastMsg, text, "", (byte) 0));
    }

    public void askAcceptDeclineNoESC(String text) {
        askAcceptDeclineNoESC(text, id);
    }

    public void askAcceptDeclineNoESC(String text, int id) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        lastMsg = (byte) 15;
        c.getSession().write(NPCPacket.getNPCTalk(id, lastMsg, text, "", (byte) 1));
    }

    public void askAngelicBusterAvatar() {
        c.getSession().write(NPCPacket.getAngelicBusterAvatarSelect(id));
        lastMsg = 0x17;
    }

    public void askAvatar(String text, int... args) {
        c.getSession().write(NPCPacket.getNPCTalkStyle(id, text, args, false));
        lastMsg = 9;
    }

    public void sendSimple(String text) {
        sendSimple(text, id);
    }

    public void sendSimple(String text, int id) {
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNext(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 5, text, "", (byte) 0));
        lastMsg = 5;
    }

    public void sendSimpleS(String text, byte type) {
        sendSimpleS(text, type, id);
    }

    public void sendSimpleS(String text, byte type, int idd) {
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNextS(text, type);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 5, text, "", type, idd));
        lastMsg = 5;
    }

    public void sendSimpleSNew(String text, byte type, byte type2) {
        sendSimpleSNew(text, type, type2, id);
    }

    public void sendSimpleSNew(String text, byte type, byte type2, int idd) {
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNextSNew(text, type, type2);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalk(id, (byte) 5, text, "", type, type2, idd));
        lastMsg = 5;
    }

    public void sendStyle(String text, int styles[]) {
        c.getSession().write(NPCPacket.getNPCTalkStyle(id, text, styles, false));
        lastMsg = 9;
    }

    public void sendSecondStyle(String text, int styles[]) {
        c.getSession().write(NPCPacket.getNPCTalkStyle(id, text, styles, true));
        lastMsg = 9;
    }

    public void sendGetNumber(String text, int def, int min, int max) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalkNum(id, text, def, min, max));
        lastMsg = 4;
    }

    public void sendGetText(String text) {
        sendGetText(text, id);
    }

    public void sendGetText(String text, int id) {
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(NPCPacket.getNPCTalkText(id, text));
        lastMsg = 3;
    }

    public void setGetText(String text) {
        this.getText = text;
    }

    public String getText() {
        return getText;
    }

    public void setHair(int hair) {
        if (hairExists(hair)) {
            getPlayer().setHair(hair);
            getPlayer().updateSingleStat(MapleStat.HAIR, hair);
            getPlayer().equipChanged();
        }
    }

    public void setFace(int face) {
        if (faceExists(face)) {
            getPlayer().setFace(face);
            getPlayer().updateSingleStat(MapleStat.FACE, face);
            getPlayer().equipChanged();
        }
    }

    public void setSkin(int color) {
        getPlayer().setSkinColor((byte) color);
        getPlayer().updateSingleStat(MapleStat.SKIN, color);
        getPlayer().equipChanged();
    }

    public static boolean hairExists(int hair) {
        return MapleItemInformationProvider.getInstance().hairExists(hair);
    }

    public static boolean faceExists(int face) {
        return MapleItemInformationProvider.getInstance().faceExists(face);
    }

    public int[] getCanHair(int[] hairs) {
        List<Integer> canHair = new ArrayList();
        List<Integer> cantHair = new ArrayList();
        for (int hair : hairs) {
            if (hairExists(hair)) {
                canHair.add(hair);
            } else {
                cantHair.add(hair);
            }
        }
        if (cantHair.size() > 0 && c.getPlayer().isAdmin()) {
            StringBuilder sb = new StringBuilder("正在讀取的髮型里有");
            sb.append(cantHair.size()).append("個髮型用戶端不支援顯示，已經被清除：");
            for (int i = 0; i < cantHair.size(); i++) {
                sb.append(cantHair.get(i));
                if (i < cantHair.size() - 1) {
                    sb.append(",");
                }
            }
            playerMessage(sb.toString());
        }
        int[] getHair = new int[canHair.size()];
        for (int i = 0; i < canHair.size(); i++) {
            getHair[i] = canHair.get(i);
        }
        return getHair;
    }

    public int[] getCanFace(int[] faces) {
        List<Integer> canFace = new ArrayList();
        List<Integer> cantFace = new ArrayList();
        for (int face : faces) {
            if (faceExists(face)) {
                canFace.add(face);
            } else {
                cantFace.add(face);
            }
        }
        if (cantFace.size() > 0 && c.getPlayer().isAdmin()) {
            StringBuilder sb = new StringBuilder("正在讀取的臉型里有");
            sb.append(cantFace.size()).append("個臉型用戶端不支援顯示，已經被清除：");
            for (int i = 0; i < cantFace.size(); i++) {
                sb.append(cantFace.get(i));
                if (i < cantFace.size() - 1) {
                    sb.append(",");
                }
            }
            playerMessage(sb.toString());
        }
        int[] getFace = new int[canFace.size()];
        for (int i = 0; i < canFace.size(); i++) {
            getFace[i] = canFace.get(i);
        }
        return getFace;
    }

    public static boolean itemExists(int itemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        for (Pair<Integer, String> item : ii.getAllItems2()) {
            if (item.getLeft() == itemId) {
                return true;
            }
        }
        return false;
    }

    public int setRandomAvatar(int ticket, int... args_all) {
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);

        int args = args_all[Randomizer.nextInt(args_all.length)];
        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            c.getPlayer().setFace(args);
            c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            c.getPlayer().setHair(args);
            c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    public int setAvatar(int ticket, int args) {
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);

        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            c.getPlayer().setFace(args);
            c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            c.getPlayer().setHair(args);
            c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    public void sendStorage() {
        c.getPlayer().setConversation(4);
        c.getPlayer().getStorage().sendStorage(c, id);
    }

    public void openShop(int id) {
        MapleShopFactory.getInstance().getShop(id).sendShop(c);
    }

    public void openShopNPC(int id) {
        MapleShopFactory.getInstance().getShop(id).sendShop(c, this.id);
    }

    public int gainGachaponItem(int id, int quantity) {
        return gainGachaponItem(id, quantity, c.getPlayer().getMap().getStreetName());
    }

    public int gainGachaponItem(int id, int quantity, final String msg) {
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final Item item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            final byte rareness = ItemConstants.gachaponRareItem(item.getItemId());
            if (rareness > 0) {
                World.Broadcast.broadcastMessage(CWvsContext.getGachaponMega(c.getPlayer().getName(), " : got a(n)", item, rareness, msg));
            }
            c.getSession().write(InfoPacket.getShowItemGain(item.getItemId(), (short) quantity, true));
            return item.getItemId();
        } catch (Exception e) {
        }
        return -1;
    }

    public int useNebuliteGachapon() {
        try {
            if (c.getPlayer().getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < 1
                    || c.getPlayer().getInventory(MapleInventoryType.USE).getNumFreeSlot() < 1
                    || c.getPlayer().getInventory(MapleInventoryType.SETUP).getNumFreeSlot() < 1
                    || c.getPlayer().getInventory(MapleInventoryType.ETC).getNumFreeSlot() < 1
                    || c.getPlayer().getInventory(MapleInventoryType.CASH).getNumFreeSlot() < 1) {
                return -1;
            }
            int grade; // Default D
            final int chance = Randomizer.nextInt(100); // cannot gacha S, only from alien cube.
            if (chance < 1) { // Grade A
                grade = 3;
            } else if (chance < 3) { // Grade B
                grade = 2;
            } else if (chance < 40) { // Grade C
                grade = 1;
            } else { // grade == 0
                grade = Randomizer.nextInt(100) < 25 ? 5 : 0; // 25% again to get premium ticket piece				
            }
            int newId = 0;
            if (grade == 5) {
                newId = 4420000;
            } else {
                final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                final List<StructItemOption> pots = new LinkedList<>(ii.getAllSocketInfo(grade).values());
                while (newId == 0) {
                    StructItemOption pot = pots.get(Randomizer.nextInt(pots.size()));
                    if (pot != null) {
                        newId = pot.opID;
                    }
                }
            }
            final Item item = MapleInventoryManipulator.addbyId_Gachapon(c, newId, (short) 1);
            if (item == null) {
                return -1;
            }
            if (grade >= 3 && grade != 5) {
                World.Broadcast.broadcastMessage(CWvsContext.getGachaponMega(c.getPlayer().getName(), " : got a(n)", item, (byte) 0, "Maple World"));
            }
            c.getSession().write(InfoPacket.getShowItemGain(newId, (short) 1, true));
            gainItem(2430748, (short) 1);
            gainItemSilent(5220094, (short) -1);
            return item.getItemId();
        } catch (Exception e) {
            System.out.println("[Error] Failed to use Nebulite Gachapon. " + e);
        }
        return -1;
    }

    public void changeJob(short job) {
        c.getPlayer().changeJob(job);
    }

    public void startQuest(int idd) {
        MapleQuest.getInstance(idd).start(getPlayer(), id);
    }

    public void completeQuest(int idd) {
        MapleQuest.getInstance(idd).complete(getPlayer(), id);
    }

    public void forfeitQuest(int idd) {
        MapleQuest.getInstance(idd).forfeit(getPlayer());
    }

    public void forceStartQuest() {
        MapleQuest.getInstance(id2).forceStart(getPlayer(), getNpc(), null);
    }

    @Override
    public void forceStartQuest(int idd) {
        MapleQuest.getInstance(idd).forceStart(getPlayer(), getNpc(), null);
    }

    public void forceStartQuest(String customData) {
        MapleQuest.getInstance(id2).forceStart(getPlayer(), getNpc(), customData);
    }

    public void forceCompleteQuest() {
        MapleQuest.getInstance(id2).forceComplete(getPlayer(), getNpc());
    }

    @Override
    public void forceCompleteQuest(final int idd) {
        MapleQuest.getInstance(idd).forceComplete(getPlayer(), getNpc());
    }

    public String getQuestCustomData() {
        return c.getPlayer().getQuestNAdd(MapleQuest.getInstance(id2)).getCustomData();
    }

    public String getQuestCustomData(int quest) {
        return c.getPlayer().getQuestNAdd(MapleQuest.getInstance(quest)).getCustomData();
    }

    public void setQuestCustomData(String customData) {
        getPlayer().getQuestNAdd(MapleQuest.getInstance(id2)).setCustomData(customData);
    }

    public long getMeso() {
        return getPlayer().getMeso();
    }

    public void gainAp(final int amount) {
        c.getPlayer().gainAp((short) amount);
    }

    public void expandInventory(byte type, int amt) {
        c.getPlayer().expandInventory(type, amt);
    }

    public void unequipEverything() {
        MapleInventory equipped = getPlayer().getInventory(MapleInventoryType.EQUIPPED);
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<Short> ids = new LinkedList<>();
        for (Item item : equipped.newList()) {
            ids.add(item.getPosition());
        }
        for (short itemid : ids) {
            MapleInventoryManipulator.unequip(getC(), itemid, equip.getNextFreeSlot());
        }
    }

    public void equip(int itemId, byte slot) {
        equip(itemId, slot, true);
    }

    public void equip(int itemId, byte slot, boolean add) {
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        Equip eqp = null;
        for (Item item : equip.newList()) {
            if (item.getItemId() == itemId) {
                eqp = (Equip) item;
            }
        }

        if (eqp == null) {
            if (add) {
                final MapleItemInformationProvider li = MapleItemInformationProvider.getInstance();
                Item item = li.getEquipById(itemId);
                item.setPosition(equip.getNextFreeSlot());
                item.setGMLog("從腳本 " + this.id + "(" + id2 + ")[" + script + "] 中獲得, 時間 " + FileoutputUtil.CurrentReadable_Time());
                eqp = (Equip) item;
                MapleInventoryManipulator.addbyItem(c, eqp);
            } else {
                return;
            }
        }

        MapleInventoryManipulator.equip(getC(), eqp.getPosition(), slot);
    }

    public void unequip(int itemId) {
        unequip(itemId, false);
    }

    public void unequip(int itemId, boolean remove) {
        MapleInventory equipped = getPlayer().getInventory(MapleInventoryType.EQUIPPED);
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        Equip eqp = null;
        for (Item item : equipped.newList()) {
            if (item.getItemId() == itemId) {
                eqp = (Equip) item;
            }
        }

        if (eqp == null) {
            return;
        }

        MapleInventoryManipulator.unequip(getC(), eqp.getPosition(), equip.getNextFreeSlot());
        if (remove) {
            MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.EQUIP, eqp.getPosition(), (short) 1, remove);
        }
    }

    public final void clearSkills() {
        final Map<Skill, SkillEntry> skills = new HashMap<>(getPlayer().getSkills());
        final Map<Skill, SkillEntry> newList = new HashMap<>();
        for (Entry<Skill, SkillEntry> skill : skills.entrySet()) {
            newList.put(skill.getKey(), new SkillEntry((byte) 0, (byte) 0, -1));
        }
        getPlayer().changeSkillsLevel(newList);
        newList.clear();
        skills.clear();
    }

    public boolean hasSkill(int skillid) {
        Skill theSkill = SkillFactory.getSkill(skillid);
        if (theSkill != null) {
            return c.getPlayer().getSkillLevel(theSkill) > 0;
        }
        return false;
    }

    public void showEffect(boolean broadcast, String effect) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(CField.showEffect(effect));
        } else {
            c.getSession().write(CField.showEffect(effect));
        }
    }

    public void playSound(boolean broadcast, String sound) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(CField.playSound(sound));
        } else {
            c.getSession().write(CField.playSound(sound));
        }
    }

    public void getDirectionInfo(byte type, int value) {
        getDirectionInfo(type, value, 0);
    }

    public void getDirectionInfo(byte type, int value, int value2) {
        getDirectionEffect(type, "", value, value2, 0, 0, 0, 0, 0, 0);
    }

    public void getDirectionInfo(String data, int value, int x, int y, int a, int b) {
        getDirectionEffect(2, data, value, x, y, a, b, 0, 0, 0);
    }

    public void getDirectionInfoNew(byte type, int value) {
        getDirectionInfoNew(type, value, 0, 0);
    }

    public void getDirectionInfoNew(byte type, int value, int a, int b) {
        getDirectionEffect(5, "", type, value, a, b, 0, 0, 0, 0);
    }

    public void getDirectionEffect(int mod, String data, int value, int value2, int value3, int a1, int a2, int a3, int a4, int npc) {
        c.getSession().write(UIPacket.getDirectionEffect(mod, data, value, value2, value3, a1, a2, a3, a4, npc));
        lastMsg = 0x12;
    }

    public void playMovie(String data) {
        playMovie(data, true);
    }

    @Override
    public void playMovie(String data, boolean show) {
        super.playMovie(data, show);
        lastMsg = 0x13;
    }

    public void updateBuddyCapacity(int capacity) {
        c.getPlayer().setBuddyCapacity((byte) capacity);
    }

    public int getBuddyCapacity() {
        return c.getPlayer().getBuddyCapacity();
    }

    public int partyMembersInMap() {
        int inMap = 0;
        if (getPlayer().getParty() == null) {
            return inMap;
        }
        for (MapleCharacter char2 : getPlayer().getMap().getCharactersThreadsafe()) {
            if (char2.getParty() != null && char2.getParty().getId() == getPlayer().getParty().getId()) {
                inMap++;
            }
        }
        return inMap;
    }

    public List<MapleCharacter> getPartyMembers() {
        if (getPlayer().getParty() == null) {
            return null;
        }
        List<MapleCharacter> chars = new LinkedList<>(); // creates an empty array full of shit..
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            for (ChannelServer channel : ChannelServer.getAllInstances()) {
                MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                if (ch != null) { // double check <3
                    chars.add(ch);
                }
            }
        }
        return chars;
    }

    public void warpPartyWithExp(int mapId, int exp) {
        if (getPlayer().getParty() == null) {
            warp(mapId, 0);
            gainExp(exp);
            return;
        }
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
            }
        }
    }

    public void warpPartyWithExpMeso(int mapId, int exp, int meso) {
        if (getPlayer().getParty() == null) {
            warp(mapId, 0);
            gainExp(exp);
            gainMeso(meso);
            return;
        }
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
                curChar.gainMeso(meso, true);
            }
        }
    }

    public MapleSquad getSquad(String type) {
        return c.getChannelServer().getMapleSquad(type);
    }

    public int getSquadAvailability(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        }
        return squad.getStatus();
    }

    public boolean registerExpedition(String type, int minutes, String startText) {
        if (c.getChannelServer().getMapleSquad(type) == null) {
            final MapleSquad squad = new MapleSquad(c.getChannel(), type, c.getPlayer(), minutes * 60 * 1000, startText);
            final boolean ret = c.getChannelServer().addMapleSquad(squad, type);
            if (ret) {
                final MapleMap map = c.getPlayer().getMap();
                map.broadcastMessage(CField.getClock(minutes * 60));
                map.broadcastMessage(CWvsContext.broadcastMsg(-6, startText));
            } else {
                squad.clear();
            }
            return ret;
        }
        return false;
    }

    public boolean registerSquad(String type, int minutes, String startText) {
        if (c.getChannelServer().getMapleSquad(type) == null) {
            final MapleSquad squad = new MapleSquad(c.getChannel(), type, c.getPlayer(), minutes * 60 * 1000, startText);
            final boolean ret = c.getChannelServer().addMapleSquad(squad, type);
            if (ret) {
                final MapleMap map = c.getPlayer().getMap();
                map.broadcastMessage(CField.getClock(minutes * 60));
                map.broadcastMessage(CWvsContext.broadcastMsg(6, c.getPlayer().getName() + startText));
            } else {
                squad.clear();
            }
            return ret;
        }
        return false;
    }

    public boolean getSquadList(String type, byte type_) {
        try {
            final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
            if (squad == null) {
                return false;
            }
            if (type_ == 0 || type_ == 3) { // Normal viewing
                sendNext(squad.getSquadMemberString(type_));
            } else if (type_ == 1) { // Squad Leader banning, Check out banned participant
                sendSimple(squad.getSquadMemberString(type_));
            } else if (type_ == 2) {
                if (squad.getBannedMemberSize() > 0) {
                    sendSimple(squad.getSquadMemberString(type_));
                } else {
                    sendNext(squad.getSquadMemberString(type_));
                }
            }
            return true;
        } catch (NullPointerException ex) {
            FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
            return false;
        }
    }
//public void teachSkill(int id, int skillevel, byte masterlevel, long expiration) {
    //  getPlayer().changeSkillLevelAll(SkillFactory.getSkill(id), skillevel, masterlevel, expiration);
    //}

    public byte isSquadLeader(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else {
            if (squad.getLeader() != null && squad.getLeader().getId() == c.getPlayer().getId()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public boolean reAdd(String eim, String squad) {
        EventInstanceManager eimz = getDisconnected(eim);
        MapleSquad squadz = getSquad(squad);
        if (eimz != null && squadz != null) {
            squadz.reAddMember(getPlayer());
            eimz.registerPlayer(getPlayer());
            return true;
        }
        return false;
    }

    public void banMember(String type, int pos) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.banMember(pos);
        }
    }

    public void acceptMember(String type, int pos) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.acceptMember(pos);
        }
    }

    public int addMember(String type, boolean join) {
        try {
            final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
            if (squad != null) {
                return squad.addMember(c.getPlayer(), join);
            }
            return -1;
        } catch (NullPointerException ex) {
            FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
            return -1;
        }
    }

    public byte isSquadMember(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else {
            if (squad.getMembers().contains(c.getPlayer())) {
                return 1;
            } else if (squad.isBanned(c.getPlayer())) {
                return 2;
            } else {
                return 0;
            }
        }
    }

    public void resetReactors() {
        getPlayer().getMap().resetReactors();
    }

    public void genericGuildMessage(int code) {
        c.getSession().write(GuildPacket.genericGuildMessage((byte) code));
    }

    public void disbandGuild() {
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0 || c.getPlayer().getGuildRank() != 1) {
            return;
        }
        World.Guild.disbandGuild(gid);
    }

    public void increaseGuildCapacity(boolean trueMax) {
        if (c.getPlayer().getMeso() < 500000 && !trueMax) {
            c.getSession().write(CWvsContext.broadcastMsg(1, "You do not have enough mesos."));
            return;
        }
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0) {
            return;
        }
        if (World.Guild.increaseGuildCapacity(gid, trueMax)) {
            if (!trueMax) {
                c.getPlayer().gainMeso(-500000, true, true);
            } else {
                gainGP(-25000);
            }
            sendNext("Your guild capacity has been raised...");
        } else if (!trueMax) {
            sendNext("Please check if your guild capacity is full. (Limit: 100)");
        } else {
            sendNext("Please check if your guild capacity is full, if you have the GP needed or if subtracting GP would decrease a guild level. (Limit: 200)");
        }
    }

    public void displayGuildRanks() {
        c.getSession().write(GuildPacket.showGuildRanks(id, MapleGuildRanking.getInstance().getRank()));
    }

    public boolean removePlayerFromInstance() {
        if (c.getPlayer().getEventInstance() != null) {
            c.getPlayer().getEventInstance().removePlayer(c.getPlayer());
            return true;
        }
        return false;
    }

    public boolean isPlayerInstance() {
        return c.getPlayer().getEventInstance() != null;
    }

    public void makeTaintedEquip(byte slot) {
        Equip sel = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot);
        sel.setStr((short) 69);
        sel.setDex((short) 69);
        sel.setInt((short) 69);
        sel.setLuk((short) 69);
        sel.setHp((short) 69);
        sel.setMp((short) 69);
        sel.setWatk((short) 69);
        sel.setMatk((short) 69);
        sel.setWdef((short) 69);
        sel.setMdef((short) 69);
        sel.setAcc((short) 69);
        sel.setAvoid((short) 69);
        sel.setHands((short) 69);
        sel.setSpeed((short) 69);
        sel.setJump((short) 69);
        sel.setUpgradeSlots((byte) 69);
        sel.setViciousHammer((byte) 69);
        sel.setEnhance((byte) 69);
        c.getPlayer().equipChanged();
        c.getPlayer().fakeRelog();
    }

    public void changeStat(byte slot, int type, int amount) {
        Equip sel = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot);
        switch (type) {
            case 0:
                sel.setStr((short) amount);
                break;
            case 1:
                sel.setDex((short) amount);
                break;
            case 2:
                sel.setInt((short) amount);
                break;
            case 3:
                sel.setLuk((short) amount);
                break;
            case 4:
                sel.setHp((short) amount);
                break;
            case 5:
                sel.setMp((short) amount);
                break;
            case 6:
                sel.setWatk((short) amount);
                break;
            case 7:
                sel.setMatk((short) amount);
                break;
            case 8:
                sel.setWdef((short) amount);
                break;
            case 9:
                sel.setMdef((short) amount);
                break;
            case 10:
                sel.setAcc((short) amount);
                break;
            case 11:
                sel.setAvoid((short) amount);
                break;
            case 12:
                sel.setHands((short) amount);
                break;
            case 13:
                sel.setSpeed((short) amount);
                break;
            case 14:
                sel.setJump((short) amount);
                break;
            case 15:
                sel.setUpgradeSlots((byte) amount);
                break;
            case 16:
                sel.setViciousHammer((byte) amount);
                break;
            case 17:
                sel.setLevel((byte) amount);
                break;
            case 18:
                sel.setEnhance((byte) amount);
                break;
            case 19:
                sel.setPotential(amount, 1, false);
                sel.updateState(false);
                break;
            case 20:
                sel.setPotential(amount, 2, false);
                sel.updateState(false);
                break;
            case 21:
                sel.setPotential(amount, 3, false);
                sel.updateState(false);
                break;
            case 22:
                sel.setPotential(amount, 1, true);
                sel.updateState(true);
                break;
            case 23:
                sel.setPotential(amount, 2, true);
                sel.updateState(true);
                break;
            case 24:
                sel.setOwner(getText());
                break;
            default:
                break;
        }
        c.getPlayer().equipChanged();
        c.getPlayer().fakeRelog();
    }

    public void openPackageDeliverer() {
        c.getPlayer().setConversation(2);
        c.getSession().write(CField.sendPackageMSG((byte) 9, null));
    }

    public void openMerchantItemStore() {
        c.getPlayer().setConversation(3);
        HiredMerchantHandler.displayMerch(c);
        c.getSession().write(CWvsContext.enableActions());
    }

    public void sendPVPWindow() {
        c.getSession().write(UIPacket.openUI(0x32));
        c.getSession().write(CField.sendPVPMaps());
    }

    public void sendAzwanWindow() {
        c.getSession().write(UIPacket.openUI(0x46));
    }

    public void sendOpenJobChangeUI() {
        c.getSession().write(UIPacket.openUI(0xA4)); // job selections change depending on ur job
    }

    public void sendTimeGateWindow() {
        c.getSession().write(UIPacket.openUI(0xA8));
    }

    public void sendRepairWindow() {
        c.getSession().write(UIPacket.openUIOption(0x21, id));
    }

    public void sendJewelCraftWindow() {
        c.getSession().write(UIPacket.openUIOption(0x68, id));
    }

    public void sendRedLeaf(boolean viewonly, boolean autocheck) {
        if (autocheck) {
            viewonly = c.getPlayer().getFriendShipToAdd() == 0;
        }
        c.getSession().write(UIPacket.sendRedLeaf(viewonly ? 0 : c.getPlayer().getFriendShipToAdd(), viewonly));
    }

    public void sendProfessionWindow() {
        c.getSession().write(UIPacket.openUI(42));
    }

    public void sendQuestWindow() {
        c.getSession().write(UIPacket.openUI(6));
    }

    public void sendBeastTamerGiftWindow() {
        c.getSession().write(UIPacket.openUI(194));
    }

    public void sendCassandrasCollectionWindow() {
        c.getSession().write(UIPacket.openUI(127));
    }

    public void sendLuckyLuckyMonstoryWindow() {
        c.getSession().write(UIPacket.openUI(120));
    }

    public void OpenUI(int ui) {
        c.getPlayer().getMap().broadcastMessage(UIPacket.openUI(ui));
    }

    public void getMulungRanking() {
        c.getSession().write(CWvsContext.getMulungRanking());
    }

    public final int getDojoPoints() {
        return dojo_getPts();
    }

    public final int getDojoRecord() {
        return c.getPlayer().getIntNoRecord(GameConstants.DOJO_RECORD);
    }

    public void setDojoRecord(final boolean reset, final boolean take, int amount) {
        if (reset) {
            c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.DOJO_RECORD)).setCustomData("0");
            c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.DOJO)).setCustomData("0");
        } else if (take) {
            c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.DOJO_RECORD)).setCustomData(String.valueOf(c.getPlayer().getIntRecord(GameConstants.DOJO_RECORD) - amount));
            c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.DOJO)).setCustomData(String.valueOf(c.getPlayer().getIntRecord(GameConstants.DOJO_RECORD) - amount));
        } else {
            c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.DOJO_RECORD)).setCustomData(String.valueOf(c.getPlayer().getIntRecord(GameConstants.DOJO_RECORD) + 1));
        }
    }

    public boolean start_DojoAgent(final boolean dojo, final boolean party, final int mapid) {
        if (dojo) {
            return Event_DojoAgent.warpStartDojo(c.getPlayer(), party, getMap(mapid));
        }
        return Event_DojoAgent.warpStartAgent(c.getPlayer(), party);
    }

    public boolean start_PyramidSubway(final int pyramid) {
        if (pyramid >= 0) {
            return Event_PyramidSubway.warpStartPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpStartSubway(c.getPlayer());
    }

    public boolean bonus_PyramidSubway(final int pyramid) {
        if (pyramid >= 0) {
            return Event_PyramidSubway.warpBonusPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpBonusSubway(c.getPlayer());
    }

    public final short getKegs() {
        return c.getChannelServer().getFireWorks().getKegsPercentage();
    }

    public void giveKegs(final int kegs) {
        c.getChannelServer().getFireWorks().giveKegs(c.getPlayer(), kegs);
    }

    public final short getSunshines() {
        return c.getChannelServer().getFireWorks().getSunsPercentage();
    }

    public void addSunshines(final int kegs) {
        c.getChannelServer().getFireWorks().giveSuns(c.getPlayer(), kegs);
    }

    public final short getDecorations() {
        return c.getChannelServer().getFireWorks().getDecsPercentage();
    }

    public void addDecorations(final int kegs) {
        try {
            c.getChannelServer().getFireWorks().giveDecs(c.getPlayer(), kegs);
        } catch (Exception e) {
        }
    }

    public final MapleCarnivalParty getCarnivalParty() {
        return c.getPlayer().getCarnivalParty();
    }

    public final MapleCarnivalChallenge getNextCarnivalRequest() {
        return c.getPlayer().getNextCarnivalRequest();
    }

    public final MapleCarnivalChallenge getCarnivalChallenge(MapleCharacter chr) {
        return new MapleCarnivalChallenge(chr);
    }

    public void maxStats() {
        Map<MapleStat, Integer> statup = new EnumMap<>(MapleStat.class);
        c.getPlayer().getStat().str = (short) 999;
        c.getPlayer().getStat().dex = (short) 999;
        c.getPlayer().getStat().int_ = (short) 999;
        c.getPlayer().getStat().luk = (short) 999;

        int overrDemon = MapleJob.is惡魔殺手(c.getPlayer().getJob()) ? GameConstants.getMPByJob(c.getPlayer().getJob()) : 500000;
        c.getPlayer().getStat().maxhp = 500000;
        c.getPlayer().getStat().maxmp = overrDemon;
        c.getPlayer().getStat().setHp(500000, c.getPlayer());
        c.getPlayer().getStat().setMp(overrDemon, c.getPlayer());

        statup.put(MapleStat.STR, 999);
        statup.put(MapleStat.DEX, 999);
        statup.put(MapleStat.LUK, 999);
        statup.put(MapleStat.INT, 999);
        statup.put(MapleStat.HP, 500000);
        statup.put(MapleStat.MAXHP, 500000);
        statup.put(MapleStat.MP, overrDemon);
        statup.put(MapleStat.MAXMP, overrDemon);
        c.getPlayer().getStat().recalcLocalStats(c.getPlayer());
        //c.getSession().write(CWvsContext.updatePlayerStats(statup, c.getPlayer().getJob()));
    }

    public int setAndroid(int args) {
        if (args < 30000) {
            c.getPlayer().getAndroid().setFace(args);
            c.getPlayer().getAndroid().saveToDb();
        } else {
            c.getPlayer().getAndroid().setHair(args);
            c.getPlayer().getAndroid().saveToDb();
        }
        CField.updateAndroidLook(c.getPlayer(), c.getPlayer().getAndroid());
        c.getPlayer().setAndroid(c.getPlayer().getAndroid()); //Respawn it
        c.getPlayer().equipChanged();
        return 1;
    }

    public int getAndroidStat(final String type) {
        switch (type) {
            case "HAIR":
                return c.getPlayer().getAndroid().getHair();
            case "FACE":
                return c.getPlayer().getAndroid().getFace();
            case "GENDER":
                int itemid = c.getPlayer().getAndroid().getItemId();
                if (itemid == 1662000 || itemid == 1662002) {
                    return 0;
                } else {
                    return 1;
                }
        }
        return -1;
    }

    public void reloadChar() {
        getPlayer().getClient().getSession().write(CField.getCharInfo(getPlayer()));
        getPlayer().getMap().removePlayer(getPlayer());
        getPlayer().getMap().addPlayer(getPlayer());
    }

    public void askAndroid(String text, int... args) {
        c.getSession().write(CField.NPCPacket.getAndroidTalkStyle(id, text, args));
        lastMsg = 10;
    }

    @Override
    public MapleCharacter getChar() {
        return getPlayer();
    }

    public static int editEquipById(MapleCharacter chr, int max, int itemid, String stat, int newval) {
        return editEquipById(chr, max, itemid, stat, (short) newval);
    }

    public static int editEquipById(MapleCharacter chr, int max, int itemid, String stat, short newval) {
        // Is it an equip?
        if (!MapleItemInformationProvider.getInstance().isEquip(itemid)) {
            return -1;
        }

        // Get List
        List<Item> equips = chr.getInventory(MapleInventoryType.EQUIP).listById(itemid);
        List<Item> equipped = chr.getInventory(MapleInventoryType.EQUIPPED).listById(itemid);

        // Do you have any?
        if (equips.isEmpty() && equipped.isEmpty()) {
            return 0;
        }

        int edited = 0;

        // edit items
        for (Item itm : equips) {
            Equip e = (Equip) itm;
            if (edited >= max) {
                break;
            }
            edited++;
            switch (stat) {
                case "str":
                    e.setStr(newval);
                    break;
                case "dex":
                    e.setDex(newval);
                    break;
                case "int":
                    e.setInt(newval);
                    break;
                case "luk":
                    e.setLuk(newval);
                    break;
                case "watk":
                    e.setWatk(newval);
                    break;
                case "matk":
                    e.setMatk(newval);
                    break;
                default:
                    return -2;
            }
        }
        for (Item itm : equipped) {
            Equip e = (Equip) itm;
            if (edited >= max) {
                break;
            }
            edited++;
            switch (stat) {
                case "str":
                    e.setStr(newval);
                    break;
                case "dex":
                    e.setDex(newval);
                    break;
                case "int":
                    e.setInt(newval);
                    break;
                case "luk":
                    e.setLuk(newval);
                    break;
                case "watk":
                    e.setWatk(newval);
                    break;
                case "matk":
                    e.setMatk(newval);
                    break;
                default:
                    return -2;
            }
        }

        // Return items edited
        return (edited);
    }

    public int getReborns() { // tjat
        return getPlayer().getReborns();
    }

    public Triple<String, Map<Integer, String>, Long> getSpeedRun(String typ) {
        final ExpeditionType expedtype = ExpeditionType.valueOf(typ);
        if (SpeedRunner.getSpeedRunData(expedtype) != null) {
            return SpeedRunner.getSpeedRunData(expedtype);
        }
        return new Triple<String, Map<Integer, String>, Long>("", new HashMap<Integer, String>(), 0L);
    }

    public boolean getSR(Triple<String, Map<Integer, String>, Long> ma, int sel) {
        if (ma.mid.get(sel) == null || ma.mid.get(sel).length() <= 0) {
            dispose();
            return false;
        }
        sendOk(ma.mid.get(sel));
        return true;
    }

    public Equip getEquip(int itemid) {
        return (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemid);
    }

    public void setExpiration(Object statsSel, long expire) {
        if (statsSel instanceof Equip) {
            ((Equip) statsSel).setExpiration(System.currentTimeMillis() + (expire * 24 * 60 * 60 * 1000));
        }
    }

    public void setLock(Object statsSel) {
        if (statsSel instanceof Equip) {
            Equip eq = (Equip) statsSel;
            if (eq.getExpiration() == -1) {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
            } else {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADABLE.getValue()));
            }
        }
    }

    public boolean addFromDrop(Object statsSel) {
        if (statsSel instanceof Item) {
            final Item it = (Item) statsSel;
            return MapleInventoryManipulator.checkSpace(getClient(), it.getItemId(), it.getQuantity(), it.getOwner()) && MapleInventoryManipulator.addFromDrop(getClient(), it, false);
        }
        return false;
    }

    public int getVPoints() {
        return getPlayer().getVPoints();
    }

    public void setVPoints(int vpoints) {
        getPlayer().setVPoints(getPlayer().getVPoints() + vpoints);
    }

    public int getDPoints() {
        return getPlayer().getVPoints();
    }

    public void setDPoints(int dpoints) {
        getPlayer().setDPoints(getPlayer().getDPoints() + dpoints);
    }

    public int getEPoints() {
        return getPlayer().getEPoints();
    }

    public void setEPoints(int epoints) {
        getPlayer().setEPoints(getPlayer().getEPoints() + epoints);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type) {
        return replaceItem(slot, invType, statsSel, offset, type, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type, boolean takeSlot) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        Item item = getPlayer().getInventory(inv).getItem((byte) slot);
        if (item == null || statsSel instanceof Item) {
            item = (Item) statsSel;
        }
        if (offset > 0) {
            if (inv != MapleInventoryType.EQUIP) {
                return false;
            }
            Equip eq = (Equip) item;
            if (takeSlot) {
                if (eq.getUpgradeSlots() < 1) {
                    return false;
                } else {
                    eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() - 1));
                }
                if (eq.getExpiration() == -1) {
                    eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
                } else {
                    eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADABLE.getValue()));
                }
            }
            if (type.equalsIgnoreCase("Slots")) {
                eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() + offset));
                eq.setViciousHammer((byte) (eq.getViciousHammer() + offset));
            } else if (type.equalsIgnoreCase("Level")) {
                eq.setLevel((byte) (eq.getLevel() + offset));
            } else if (type.equalsIgnoreCase("Hammer")) {
                eq.setViciousHammer((byte) (eq.getViciousHammer() + offset));
            } else if (type.equalsIgnoreCase("STR")) {
                eq.setStr((short) (eq.getStr() + offset));
            } else if (type.equalsIgnoreCase("DEX")) {
                eq.setDex((short) (eq.getDex() + offset));
            } else if (type.equalsIgnoreCase("INT")) {
                eq.setInt((short) (eq.getInt() + offset));
            } else if (type.equalsIgnoreCase("LUK")) {
                eq.setLuk((short) (eq.getLuk() + offset));
            } else if (type.equalsIgnoreCase("HP")) {
                eq.setHp((short) (eq.getHp() + offset));
            } else if (type.equalsIgnoreCase("MP")) {
                eq.setMp((short) (eq.getMp() + offset));
            } else if (type.equalsIgnoreCase("WATK")) {
                eq.setWatk((short) (eq.getWatk() + offset));
            } else if (type.equalsIgnoreCase("MATK")) {
                eq.setMatk((short) (eq.getMatk() + offset));
            } else if (type.equalsIgnoreCase("WDEF")) {
                eq.setWdef((short) (eq.getWdef() + offset));
            } else if (type.equalsIgnoreCase("MDEF")) {
                eq.setMdef((short) (eq.getMdef() + offset));
            } else if (type.equalsIgnoreCase("ACC")) {
                eq.setAcc((short) (eq.getAcc() + offset));
            } else if (type.equalsIgnoreCase("Avoid")) {
                eq.setAvoid((short) (eq.getAvoid() + offset));
            } else if (type.equalsIgnoreCase("Hands")) {
                eq.setHands((short) (eq.getHands() + offset));
            } else if (type.equalsIgnoreCase("Speed")) {
                eq.setSpeed((short) (eq.getSpeed() + offset));
            } else if (type.equalsIgnoreCase("Jump")) {
                eq.setJump((short) (eq.getJump() + offset));
            } else if (type.equalsIgnoreCase("ItemEXP")) {
                eq.setItemEXP(eq.getItemEXP() + offset);
            } else if (type.equalsIgnoreCase("Expiration")) {
                eq.setExpiration(eq.getExpiration() + offset);
            } else if (type.equalsIgnoreCase("Flag")) {
                eq.setFlag((byte) (eq.getFlag() + offset));
            }
            item = eq.copy();
        }
        MapleInventoryManipulator.removeFromSlot(getClient(), inv, (short) slot, item.getQuantity(), false);
        return MapleInventoryManipulator.addFromDrop(getClient(), item, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int upgradeSlots) {
        return replaceItem(slot, invType, statsSel, upgradeSlots, "Slots");
    }

    public boolean isCash(final int itemId) {
        return MapleItemInformationProvider.getInstance().isCash(itemId);
    }

    public int getTotalStat(final int itemId) {
        return MapleItemInformationProvider.getInstance().getTotalStat((Equip) MapleItemInformationProvider.getInstance().getEquipById(itemId));
    }

    public int getReqLevel(final int itemId) {
        return MapleItemInformationProvider.getInstance().getReqLevel(itemId);
    }

    public void directionEffect(String data, int value, int x, int y, int npc) {
        c.getSession().write(UIPacket.getDirectionEffect(data, value, x, y, npc));
    }

    public void introEnableUI(boolean enable) {
        introEnableUI(enable ? 1 : 0);
    }

    public void introEnableUI(boolean enable, boolean enable2) {
        introEnableUI(enable ? 1 : 0, enable2 ? 1 : 0);
    }

    public MapleStatEffect getEffect(int buff) {
        return MapleItemInformationProvider.getInstance().getItemEffect(buff);
    }

    public void buffGuild(final int buff, final int duration, final String msg) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (ii.getItemEffect(buff) != null && getPlayer().getGuildId() > 0) {
            final MapleStatEffect mse = ii.getItemEffect(buff);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr.getGuildId() == getPlayer().getGuildId()) {
                        mse.applyTo(chr, chr, true, null, duration);
                        chr.dropMessage(5, "Your guild has gotten a " + msg + " buff.");
                    }
                }
            }
        }
    }

    public boolean createAlliance(String alliancename) {
        MapleParty pt = c.getPlayer().getParty();
        MapleCharacter otherChar = c.getChannelServer().getPlayerStorage().getCharacterById(pt.getMemberByIndex(1).getId());
        if (otherChar == null || otherChar.getId() == c.getPlayer().getId()) {
            return false;
        }
        try {
            return World.Alliance.createAlliance(alliancename, c.getPlayer().getId(), otherChar.getId(), c.getPlayer().getGuildId(), otherChar.getGuildId());
        } catch (Exception re) {
            return false;
        }
    }

    public boolean addCapacityToAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.changeAllianceCapacity(gs.getAllianceId())) {
                    gainMeso(-MapleGuildAlliance.CHANGE_CAPACITY_COST);
                    return true;
                }
            }
        } catch (Exception re) {
        }
        return false;
    }

    public boolean disbandAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.disbandAlliance(gs.getAllianceId())) {
                    return true;
                }
            }
        } catch (Exception re) {
        }
        return false;
    }

    public byte getLastMsg() {
        return lastMsg;
    }

    public final void setLastMsg(final byte last) {
        this.lastMsg = last;
    }

    public final void maxAllSkills() {
        HashMap<Skill, SkillEntry> sa = new HashMap<>();
        for (Skill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId()) && skil.getId() < 90000000) { //no db/additionals/resistance skills
                sa.put(skil, new SkillEntry((byte) skil.getMaxLevel(), (byte) skil.getMaxLevel(), SkillFactory.getDefaultSExpiry(skil)));
            }
        }
        getPlayer().changeSkillsLevel(sa);
    }

    public final void maxSkillsByJob() {
        HashMap<Skill, SkillEntry> sa = new HashMap<>();
        for (Skill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId()) && skil.canBeLearnedBy(getPlayer().getJob()) && !skil.isInvisible()) { //no db/additionals/resistance skills
                sa.put(skil, new SkillEntry((byte) skil.getMaxLevel(), (byte) skil.getMaxLevel(), SkillFactory.getDefaultSExpiry(skil)));
            }
        }
        getPlayer().changeSkillsLevel(sa);
    }

    public final void removeSkillsByJob() {
        HashMap<Skill, SkillEntry> sa = new HashMap<>();
        for (Skill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId()) && skil.canBeLearnedBy(getPlayer().getJob())) { //no db/additionals/resistance skills
                sa.put(skil, new SkillEntry((byte) -1, (byte) skil.getMaxLevel(), SkillFactory.getDefaultSExpiry(skil)));
            }
        }
        getPlayer().changeSkillsLevel(sa);
    }

    public final void maxSkillsByJobId(int jobid) {
        HashMap<Skill, SkillEntry> sa = new HashMap<>();
        for (Skill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId()) && skil.canBeLearnedBy(getPlayer().getJob()) && skil.getId() >= jobid * 1000000 && skil.getId() < (jobid + 1) * 1000000 && !skil.isInvisible()) {
                sa.put(skil, new SkillEntry((byte) skil.getMaxLevel(), (byte) skil.getMaxLevel(), SkillFactory.getDefaultSExpiry(skil)));
            }
        }
        getPlayer().changeSkillsLevel(sa);
    }

    public final void resetStats(int str, int dex, int z, int luk) {
        c.getPlayer().resetStats(str, dex, z, luk);
    }

    public void killAllMonsters(int mapid) {
        MapleMap map = c.getChannelServer().getMapFactory().getMap(mapid);
        map.killAllMonsters(false); // No drop.
    }

    public final void killAllMobs() {
        c.getPlayer().getMap().killAllMonsters(true);
    }

    public final void levelUp() {
        c.getPlayer().levelUp();
    }

    public void cleardrops() {
        MapleMonsterInformationProvider.getInstance().clearDrops();
    }

    public final boolean dropItem(int slot, int invType, int quantity) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        return MapleInventoryManipulator.drop(c, inv, (short) slot, (short) quantity, true);
    }

    public final List<Integer> getAllPotentialInfo() {
        List<Integer> list = new ArrayList<>(MapleItemInformationProvider.getInstance().getAllPotentialInfo().keySet());
        Collections.sort(list);
        return list;
    }

    public final List<Integer> getAllPotentialInfoSearch(String content) {
        List<Integer> list = new ArrayList<>();
        for (Entry<Integer, List<StructItemOption>> i : MapleItemInformationProvider.getInstance().getAllPotentialInfo().entrySet()) {
            for (StructItemOption ii : i.getValue()) {
                if (ii.toString().contains(content)) {
                    list.add(i.getKey());
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    public void MakeGMItem(byte slot, MapleCharacter player) {
        MapleInventory equip = player.getInventory(MapleInventoryType.EQUIP);
        Equip eu = (Equip) equip.getItem(slot);
        int item = equip.getItem(slot).getItemId();
        short hand = eu.getHands();
        byte level = eu.getLevel();
        Equip nItem = new Equip(item, slot, (byte) 0);
        nItem.setStr((short) 32767); // STR
        nItem.setDex((short) 32767); // DEX
        nItem.setInt((short) 32767); // INT
        nItem.setLuk((short) 32767); //LUK
        nItem.setUpgradeSlots((byte) 0);
        nItem.setHands(hand);
        nItem.setLevel(level);
        player.getInventory(MapleInventoryType.EQUIP).removeItem(slot);
        player.getInventory(MapleInventoryType.EQUIP).addFromDB(nItem);
    }

    public final String getPotentialInfo(final int id) {
        final List<StructItemOption> potInfo = MapleItemInformationProvider.getInstance().getPotentialInfo(id);
        final StringBuilder builder = new StringBuilder("#b#ePOTENTIAL INFO FOR ID: ");
        builder.append(id);
        builder.append("#n#k\r\n\r\n");
        int minLevel = 1, maxLevel = 10;
        for (StructItemOption item : potInfo) {
            builder.append("#eLevels ");
            builder.append(minLevel);
            builder.append("~");
            builder.append(maxLevel);
            builder.append(": #n");
            builder.append(item.get(potInfo.toString()));
            minLevel += 10;
            maxLevel += 10;
            builder.append("\r\n");
        }
        return builder.toString();
    }

    public final void sendRPS() {
        c.getSession().write(CField.getRPSMode((byte) 8, -1, -1, -1));
    }

    public final void setQuestRecord(Object ch, final int questid, final String data) {
        ((MapleCharacter) ch).getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(data);
    }

    public final void doWeddingEffect(final Object ch) {
        final MapleCharacter chr = (MapleCharacter) ch;
        final MapleCharacter player = getPlayer();
        getMap().broadcastMessage(CWvsContext.yellowChat(player.getName() + ", do you take " + chr.getName() + " as your wife and promise to stay beside her through all downtimes, crashes, and lags?"));
        CloneTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (chr == null || player == null) {
                    warpMap(680000500, 0);
                } else {
                    chr.getMap().broadcastMessage(CWvsContext.yellowChat(chr.getName() + ", do you take " + player.getName() + " as your husband and promise to stay beside him through all downtimes, crashes, and lags?"));
                }
            }
        }, 10000);
        CloneTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (chr == null || player == null) {
                    if (player != null) {
                        setQuestRecord(player, 160001, "3");
                        setQuestRecord(player, 160002, "0");
                    } else if (chr != null) {
                        setQuestRecord(chr, 160001, "3");
                        setQuestRecord(chr, 160002, "0");
                    }
                    warpMap(680000500, 0);
                } else {
                    setQuestRecord(player, 160001, "2");
                    setQuestRecord(chr, 160001, "2");
                    sendNPCText(player.getName() + " and " + chr.getName() + ", I wish you two all the best on your " + chr.getClient().getChannelServer().getServerName() + " journey together!", 9201002);
                    chr.getMap().startExtendedMapEffect("You may now kiss the bride, " + player.getName() + "!", 5120006);
                    if (chr.getGuildId() > 0) {
                        World.Guild.guildPacket(chr.getGuildId(), CWvsContext.sendMarriage(false, chr.getName()));
                    }
                    if (chr.getFamilyId() > 0) {
                        World.Family.familyPacket(chr.getFamilyId(), CWvsContext.sendMarriage(true, chr.getName()), chr.getId());
                    }
                    if (player.getGuildId() > 0) {
                        World.Guild.guildPacket(player.getGuildId(), CWvsContext.sendMarriage(false, player.getName()));
                    }
                    if (player.getFamilyId() > 0) {
                        World.Family.familyPacket(player.getFamilyId(), CWvsContext.sendMarriage(true, chr.getName()), player.getId());
                    }
                }
            }
        }, 20000); //10 sec 10 sec

    }

    public void putKey(int key, int type, int action) {
        getPlayer().changeKeybinding(key, (byte) type, action);
        getClient().getSession().write(CField.getKeymap(getPlayer().getKeyLayout(), getPlayer()));
    }

    public void doRing(final String name, final int itemid) {
        PlayersHandler.DoRing(getClient(), name, itemid);
    }

    public int getNaturalStats(final int itemid, final String it) {
        Map<String, Integer> eqStats = MapleItemInformationProvider.getInstance().getEquipStats(itemid);
        if (eqStats != null && eqStats.containsKey(it)) {
            return eqStats.get(it);
        }
        return 0;
    }

    public boolean isEligibleName(String t) {
        return MapleCharacterUtil.canCreateChar(t, getPlayer().isGM()) && (!LoginInformationProvider.getInstance().isForbiddenName(t) || getPlayer().isGM());
    }

    public String checkDrop(int mobId) {
        final List<MonsterDropEntry> ranks = MapleMonsterInformationProvider.getInstance().retrieveDrop(mobId);
        final List<MonsterGlobalDropEntry> drops = MapleMonsterInformationProvider.getInstance().getGlobalDrop();
        if ((ranks != null && ranks.size() > 0) || (drops != null && drops.size() > 0)) {
            int num = 0;
            int itemId;
            int ch;
            MonsterDropEntry de;
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < ranks.size(); i++) {
                de = ranks.get(i);
                if (de.chance > 0 && (de.questid <= 0 || (de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0))) {
                    itemId = de.itemId;
                    if (num == 0) {
                        name.append("怪物#e#o").append(mobId).append("##n的掉寶數據如下\r\n");
                        name.append("--------------------------------------\r\n");
                    }
                    String namez = "#z" + itemId + "#";
                    if (itemId == 0) { //meso
                        itemId = 4031041; //display sack of cash
                        namez = (de.Minimum * getClient().getChannelServer().getMesoRate(getPlayer().getWorld())) + "到" + (de.Maximum * getClient().getChannelServer().getMesoRate(getPlayer().getWorld())) + "楓幣";
                    }
                    ch = de.chance * getClient().getChannelServer().getDropRate(getPlayer().getWorld());
                    name.append(num + 1).append(") #i").append(itemId).append(":#").append(namez);
                    if (c.getPlayer().isIntern()) {
                        name.append("(").append(itemId).append(")").append(de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0 ? ("[" + MapleQuest.getInstance(de.questid).toString() + "]") : "").append("\r\n掉寶幾率：").append(Integer.valueOf(ch >= 999999 ? 1000000 : ch).doubleValue() / 10000.0).append("%").append(" 來源：").append(de.addFrom).append("\r\n");
                    }
                    name.append("\r\n");
                    num++;
                }
            }

            //加載全域掉寶
            if (drops != null && drops.size() > 0) {
                MonsterGlobalDropEntry dge;
                for (int i = 0; i < drops.size(); i++) {
                    dge = drops.get(i);
                    if (dge.chance > 0 && (dge.questid <= 0 || (dge.questid > 0 && MapleQuest.getInstance(dge.questid).getName().length() > 0))) {
                        itemId = dge.itemId;
                        String namez = "#z" + itemId + "#";
                        if (itemId == 0) { //meso
                            itemId = 4031041; //display sack of cash
                            namez = (dge.Minimum * c.getChannelServer().getMesoRate(c.getPlayer().getWorld())) + "到" + (dge.Maximum * c.getChannelServer().getMesoRate(c.getPlayer().getWorld())) + "楓幣";
                        }
                        ch = dge.chance * c.getChannelServer().getDropRate(c.getPlayer().getWorld());
                        name.append(num + 1).append(") #i").append(itemId).append(":#").append(namez);
                        if (c.getPlayer().isIntern()) {
                            name.append("(").append(itemId).append(")").append(dge.questid > 0 && MapleQuest.getInstance(dge.questid).getName().length() > 0 ? ("[" + MapleQuest.getInstance(dge.questid).toString() + "]") : "").append("\r\n掉寶幾率：").append(Integer.valueOf(ch >= 999999 ? 1000000 : ch).doubleValue() / 10000.0).append("%").append(" 來源：").append(dge.addFrom).append("(全域)\r\n");
                        }
                        name.append("\r\n");
                        num++;
                    }
                }
            }
            if (name.length() > 0) {
                return name.toString();
            }

        }
        return "此怪物無掉寶數據。";
    }

    public String getLeftPadded(final String in, final char padchar, final int length) {
        return StringUtil.getLeftPaddedStr(in, padchar, length);
    }

    public void handleDivorce() {
        if (getPlayer().getMarriageId() <= 0) {
            sendNext("Please make sure you have a marriage.");
            return;
        }
        final int chz = World.Find.findChannel(getPlayer().getMarriageId());
        if (chz == -1) {
            //sql queries
            try {
                Connection con = DatabaseConnection.getConnection();
                PreparedStatement ps = con.prepareStatement("UPDATE queststatus SET customData = ? WHERE characterid = ? AND (quest = ? OR quest = ?)");
                ps.setString(1, "0");
                ps.setInt(2, getPlayer().getMarriageId());
                ps.setInt(3, 160001);
                ps.setInt(4, 160002);
                ps.executeUpdate();
                ps.close();

                ps = con.prepareStatement("UPDATE characters SET marriageid = ? WHERE id = ?");
                ps.setInt(1, 0);
                ps.setInt(2, getPlayer().getMarriageId());
                ps.executeUpdate();
                ps.close();
            } catch (SQLException e) {
                outputFileError(e);
                return;
            }
            setQuestRecord(getPlayer(), 160001, "0");
            setQuestRecord(getPlayer(), 160002, "0");
            getPlayer().setMarriageId(0);
            sendNext("You have been successfully divorced...");
            return;
        } else if (chz < -1) {
            sendNext("Please make sure your partner is logged on.");
            return;
        }
        MapleCharacter cPlayer = ChannelServer.getInstance(chz).getPlayerStorage().getCharacterById(getPlayer().getMarriageId());
        if (cPlayer != null) {
            cPlayer.dropMessage(1, "Your partner has divorced you.");
            cPlayer.setMarriageId(0);
            setQuestRecord(cPlayer, 160001, "0");
            setQuestRecord(getPlayer(), 160001, "0");
            setQuestRecord(cPlayer, 160002, "0");
            setQuestRecord(getPlayer(), 160002, "0");
            getPlayer().setMarriageId(0);
            sendNext("You have been successfully divorced...");
        } else {
            sendNext("An error occurred...");
        }
    }

    public String getReadableMillis(long startMillis, long endMillis) {
        return StringUtil.getReadableMillis(startMillis, endMillis);
    }

    public void sendUltimateExplorer() {
        getClient().getSession().write(CWvsContext.ultimateExplorer());
    }

    public void sendPendant(boolean b) {
        c.getSession().write(CWvsContext.pendantSlot(b));
    }

    public Triple<Integer, Integer, Integer> getCompensation() {
        Triple<Integer, Integer, Integer> ret = null;
        try {
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM compensationlog_confirmed WHERE chrname LIKE ?")) {
                ps.setString(1, getPlayer().getName());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        ret = new Triple<>(rs.getInt("value"), rs.getInt("taken"), rs.getInt("donor"));
                    }
                }
            }
            return ret;
        } catch (SQLException e) {
            FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, e);
            return ret;
        }
    }

    public boolean deleteCompensation(int taken) {
        try {
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE compensationlog_confirmed SET taken = ? WHERE chrname LIKE ?")) {
                ps.setInt(1, taken);
                ps.setString(2, getPlayer().getName());
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, e);
            return false;
        }
    }

    /*
     * Start of Custom Features
     */
    public void gainAPS(int gain) {
        getPlayer().gainAPS(gain);
    }
    /*
     * End of Custom Features
     */

    public void changeJobById(short job) {
        c.getPlayer().changeJob(job);
    }

    public int getJobId() {
        return getPlayer().getJob();
    }

    public int getLevel() {
        return getPlayer().getLevel();
    }

    public int getEquipId(byte slot) {
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        Equip eu = (Equip) equip.getItem(slot);
        return equip.getItem(slot).getItemId();
    }

    public int getUseId(byte slot) {
        MapleInventory use = getPlayer().getInventory(MapleInventoryType.USE);
        return use.getItem(slot).getItemId();
    }

    public int getSetupId(byte slot) {
        MapleInventory setup = getPlayer().getInventory(MapleInventoryType.SETUP);
        return setup.getItem(slot).getItemId();
    }

    public int getCashId(byte slot) {
        MapleInventory cash = getPlayer().getInventory(MapleInventoryType.CASH);
        return cash.getItem(slot).getItemId();
    }

    public int getETCId(byte slot) {
        MapleInventory etc = getPlayer().getInventory(MapleInventoryType.ETC);
        return etc.getItem(slot).getItemId();
    }

    public String EquipList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<String> stra = new LinkedList<>();
        for (Item item : equip.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String UseList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory use = c.getPlayer().getInventory(MapleInventoryType.USE);
        List<String> stra = new LinkedList<>();
        for (Item item : use.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String CashList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory cash = c.getPlayer().getInventory(MapleInventoryType.CASH);
        List<String> stra = new LinkedList<>();
        for (Item item : cash.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String ETCList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory etc = c.getPlayer().getInventory(MapleInventoryType.ETC);
        List<String> stra = new LinkedList<>();
        for (Item item : etc.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String SetupList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory setup = c.getPlayer().getInventory(MapleInventoryType.SETUP);
        List<String> stra = new LinkedList<>();
        for (Item item : setup.list()) {
            stra.add("#L" + item.getPosition() + "##v" + item.getItemId() + "##l");
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String PotentialedEquipList(MapleClient c) {
        StringBuilder str = new StringBuilder();
        MapleInventory equip = c.getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<String> stra = new LinkedList<>();
        for (Item item : equip.list()) {
            Equip eq = (Equip) item;
            if (eq.getPotential(1, true) != 0) {
                stra.add("\r\n#L" + item.getPosition() + "##v" + item.getItemId() + "# - " + (eq.getPotential(2, true) != 0 ? 2 : 1) + " additional potential lines #l");
            }
        }
        for (String strb : stra) {
            str.append(strb);
        }
        return str.toString();
    }

    public String EquipPotentialList(short slot) {
        Equip equip = (Equip) getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
        StringBuilder sb = new StringBuilder();
        int[] potentials;
        potentials = new int[]{equip.getPotential(1, false), equip.getPotential(2, false), equip.getPotential(3, false)};
        for (int i : potentials) {
            StructItemOption op = MapleItemInformationProvider.getInstance().getPotentialInfo(equip.getPotential(1, false)).get(MapleItemInformationProvider.getInstance().getReqLevel(equip.getItemId()) / 10);
            sb.append("\r\nPotential ").append(i).append(" - ").append(op.toString());
        }
        return sb.toString();
    }

    public void showFredrick() {
        HiredMerchantHandler.showFredrick(c);
    }

    public String searchId(int type, String search) {
        return MapleInventoryManipulator.searchId(type, search);
    }

    public int parseInt(String s) {
        return Integer.parseInt(s);
    }

    public byte parseByte(String s) {
        return Byte.parseByte(s);
    }

    public short parseShort(String s) {
        return Short.parseShort(s);
    }

    public long parseLong(String s) {
        return Long.parseLong(s);
    }

    public void getEventEnvelope(int questid, int time) {
        CWvsContext.getEventEnvelope(questid, time);
    }

    public void write(Object o) {
        c.getSession().write(o);
    }

    public void openUIOption(int type) {
        CField.UIPacket.openUIOption(type, id);
    }

    public void sendAttackOnTitanScore(int type) {
        c.getSession().write(CField.UIPacket.openUIOption(type, 1));
    }

    public void showHilla() {
        try {
            c.getSession().write(CField.MapEff("phantom/hillah"));
            MapleNPC hilla = new MapleNPC(1402400, "Hilla");
            hilla.setPosition(new Point(-131, -2));
            hilla.setCy(-7);
            hilla.setF(1);
            hilla.setFh(12);
            hilla.setRx0(-181);
            hilla.setRx1(-81);
            MapleNPC guard1 = new MapleNPC(1402401, "Hilla's Guard");
            guard1.setPosition(new Point(-209, -2));
            guard1.setCy(-7);
            guard1.setF(1);
            guard1.setFh(12);
            guard1.setRx0(-259);
            guard1.setRx1(-159);
            MapleNPC guard2 = new MapleNPC(1402401, "Hilla's Guard");
            guard2.setPosition(new Point(-282, -2));
            guard2.setCy(-7);
            guard2.setF(1);
            guard2.setFh(12);
            guard2.setRx0(-332);
            guard2.setRx1(-232);
            MapleNPC guard3 = new MapleNPC(1402401, "Hilla's Guard");
            guard3.setPosition(new Point(-59, -2));
            guard3.setCy(-7);
            guard3.setF(1);
            guard3.setFh(12);
            guard3.setRx0(-109);
            guard3.setRx1(-9);
            c.getSession().write(NPCPacket.spawnNPC(hilla, true));
            c.getSession().write(NPCPacket.spawnNPC(guard1, true));
            c.getSession().write(NPCPacket.spawnNPC(guard2, true));
            c.getSession().write(NPCPacket.spawnNPC(guard3, true));
            Thread.sleep(6000);
        } catch (InterruptedException e) {
        }
        NPCScriptManager.getInstance().start(c.getPlayer().getClient(), 1104201, "PTtutor500_2");
    }

    public void showSkaia() {
        try {
            c.getSession().write(CField.MapEff("phantom/skaia"));
            Thread.sleep(8000);
        } catch (InterruptedException e) {
        }
        NPCScriptManager.getInstance().start(c.getPlayer().getClient(), 1104201, "PTtutor500_3");
    }

    public void showPhantomWait() {
        try {
            c.getSession().write(CField.MapEff("phantom/phantom"));
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        NPCScriptManager.getInstance().start(c.getPlayer().getClient(), 1104201, "PTtutor500_4");
    }

    public void movePhantom() {
        try {
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 3, 2));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 2000));
            Thread.sleep(2000);
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 3, 0));
        } catch (InterruptedException e) {
        }
        NPCScriptManager.getInstance().start(c.getPlayer().getClient(), 1104201, "PTtutor500_1");
    }

    public void showPhantomMovie() {
        warp(150000000);
        try {
            c.getSession().write(UIPacket.playMovie("phantom.avi", true));
            Thread.sleep(4 * 60 * 1000); //4 minutes
        } catch (InterruptedException e) {
        }
        MapleQuest.getInstance(25000).forceComplete(c.getPlayer(), 1402000);
        c.getSession().write(CField.UIPacket.getDirectionStatus(false));
        c.getSession().write(CField.UIPacket.IntroEnableUI(0));
    }

    public void mihileNeinheartDisappear() {
        try {
            c.getSession().write(UIPacket.getDirectionInfo("Effect/Direction7.img/effect/tuto/step0/4", 2000, 0, -100, 1, 0));
            c.getSession().write(UIPacket.getDirectionFacialExpression(6, 2000));
            c.getPlayer().getClient().getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 2000));
            Thread.sleep(2000);
            NPCScriptManager.getInstance().start(c, 1106000, "tuto002");
        } catch (InterruptedException e) {
        }
    }

    public void mihileMove913070001() {
        try {
            c.getPlayer().getClient().getSession().write(CField.UIPacket.getDirectionInfo((byte) 3, 2));
            c.getPlayer().getClient().getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 800));
            Thread.sleep(800);
        } catch (InterruptedException e) {
        }
        c.getSession().write(CField.UIPacket.IntroEnableUI(0));
        while (c.getPlayer().getLevel() < 2) {
            c.getPlayer().levelUp();
        }
        c.getPlayer().setExp(0);
        warp(913070001, 0);
        c.getSession().write(CWvsContext.enableActions());
    }

    public void mihileSoul() {
        try {
            c.getSession().write(UIPacket.getDirectionInfo("Effect/Direction7.img/effect/tuto/soul/0", 4000, 0, -100, 1, 0));
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }
        NPCScriptManager.getInstance().start(c, 1106000, "tuto003");
    }

    public void mihileMove913070050() {
        try {
            c.getPlayer().getClient().getSession().write(CField.UIPacket.getDirectionInfo((byte) 3, 2));
            c.getPlayer().getClient().getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 6000));
            Thread.sleep(5000);
            c.getPlayer().getClient().getSession().write(CField.UIPacket.getDirectionInfo((byte) 3, 0));
            NPCScriptManager.getInstance().start(c, 1106001, "tuto005");
        } catch (InterruptedException e) {
        }
    }

    public void mihileAssailantSummon() {
        for (int i = 0; i < 10; i++) {
            c.getPlayer().getMap().spawnMonster_sSack(MapleLifeFactory.getMonster(9001050), new Point(240, 65), 0);
        }
        c.getSession().write(CWvsContext.enableActions());
    }

    public List<Triple<Short, String, Integer>> rankList(short[] ranks, String[] names, int[] values) {
        List<Triple<Short, String, Integer>> list = new LinkedList();
        if (ranks.length != names.length || names.length != values.length || values.length != ranks.length) {
            return null;
        }
        for (int i = 0; i < ranks.length; i++) {
            list.add(new Triple<>(ranks[i], names[i], values[i]));
        }
        return list;
    }

    public void displayRank(int npcid, List<Triple<Short, String, Integer>> list) {
        if (ServerConfig.LOG_PACKETS) {
            System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
        mplew.write(0x50);
        mplew.writeInt(npcid);
        mplew.writeInt(list.size());
        for (Triple<Short, String, Integer> info : list) {
            mplew.writeShort(info.getLeft()); //Rank
            mplew.writeMapleAsciiString(info.getMid()); //Name
            mplew.writeInt(info.getRight()); //Value
            mplew.writeZeroBytes(16);
        }
        c.getSession().write(mplew.getPacket());
    }

    public void dragonShoutReward(int reward) {
        int itemid;
        switch (reward) {
            case 0:
                itemid = 1102207;
                break;
            case 1:
                itemid = 1122080;
                break;
            case 2:
                itemid = 2041213;
                break;
            case 3:
                itemid = 2022704;
                break;
            default:
                itemid = 2022704;
                break;
        }
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final MapleInventoryType invtype = GameConstants.getInventoryType(itemid);
        if (!MapleInventoryManipulator.checkSpace(c, itemid, 1, "")) {
            return;
        }
        if (invtype.equals(MapleInventoryType.EQUIP) && !ItemConstants.類型.可充值道具(itemid)) {
            final Equip item = (Equip) (ii.getEquipById(itemid));
            switch (reward) {
                case 0: //9% ATT, 9% MAGIC, 30% Boss Damage
                    item.setPotential(40051, 1, false); //9% Att
                    item.setPotential(40052, 2, false); //9% Magic
                    item.setPotential(40601, 3, false); //30% Boss Damage
                    item.updateState(false);
                    break;
                case 1: //30% All Stat
                    item.setPotential(40086, 1, false); //9% All Stat
                    item.setPotential(40086, 2, false); //9% All Stat
                    item.setPotential(40086, 3, false); //9% All Stat
                    item.setSocket1(ii.getSocketInfo(3063280).opID); //3% All Stat
                    item.updateState(false);
                    break;
            }
            item.setOwner("Hyperious");
            item.setGMLog("從腳本 " + this.id + "(" + id2 + ")[" + script + "] (The Dragon's Shout PQ)中獲得 時間: " + FileoutputUtil.CurrentReadable_Time());
            final String name = ii.getName(itemid);
            if (itemid / 10000 == 114 && name != null && name.length() > 0) { //medal
                final String msg = "<" + name + ">獲得稱號。";
                c.getPlayer().dropMessage(-1, msg);
                c.getPlayer().dropMessage(5, msg);
            }
            MapleInventoryManipulator.addbyItem(c, item.copy());
        } else {
            MapleInventoryManipulator.addById(c, itemid, (short) 1, "Hyperious", null, 0, MapleInventoryManipulator.DAY, "從腳本 " + this.id + "(" + id2 + ")[" + script + "] 中獲得 時間: " + FileoutputUtil.CurrentReadable_Time());
        }
        c.getSession().write(InfoPacket.getShowItemGain(itemid, (short) 1, true));
    }

    public String searchData(int type, String search) {
        return SearchGenerator.searchData(type, search);
    }

    public int[] getSearchData(int type, String search) {
        Map<Integer, String> data = SearchGenerator.getSearchData(type, search);
        if (data.isEmpty()) {
            return null;
        }
        int[] searches = new int[data.size()];
        int i = 0;
        for (int key : data.keySet()) {
            searches[i] = key;
            i++;
        }
        return searches;
    }

    public boolean foundData(int type, String search) {
        return SearchGenerator.foundData(type, search);
    }

    public boolean partyHaveItem(int itemid, short quantity) {
        if (getPlayer().getParty() == null) {
            return false;
        }
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            for (ChannelServer channel : ChannelServer.getAllInstances()) {
                MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                if (ch != null) {
                    if (!ch.haveItem(itemid, quantity)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public final boolean scrollItem(final short scroll, final short item) {
        return InventoryHandler.UseUpgradeScroll(scroll, item, (short) 0, getClient(), getPlayer(), 0, false);
    }

    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /*
     public final int WEAPON_RENTAL = 57463816;

     public int weaponRentalState() {
     if (c.getPlayer().getIntNoRecord(WEAPON_RENTAL) == 0) {
     return 0;
     }
     return (System.currentTimeMillis() / (60 * 1000) - c.getPlayer().getIntNoRecord(WEAPON_RENTAL)) >= 15 ? 1 : 2;
     }

     public void setWeaponRentalUnavailable() {
     c.getPlayer().getQuestNAdd(MapleQuest.getInstance(WEAPON_RENTAL)).setCustomData("" + System.currentTimeMillis() / (60 * 1000));
     }
     */
    public MapleQuest getQuestById(int questId) {
        return MapleQuest.getInstance(questId);
    }

    public int getEquipLevelById(int itemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        return ii.getEquipStats(itemId).get("reqLevel");
    }

    public void sendGMBoard(String url) {
        c.getSession().write(CWvsContext.gmBoard(c.getNextClientIncrenement(), url));
    }

    public void addPendantSlot(int days) {
        c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.墜飾欄)).setCustomData(String.valueOf(System.currentTimeMillis() + ((long) days * 24 * 60 * 60 * 1000)));
        c.getSession().write(CField.updatePendantSlot(System.currentTimeMillis() + ((long) days * 24 * 60 * 60 * 1000)));
    }

    public void setPendantSlot(long time) {
        c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.墜飾欄)).setCustomData(String.valueOf(time));
        c.getSession().write(CField.updatePendantSlot(time));
    }

    public long getCustomMeso() {
        return c.getPlayer().getLongNoRecord(GameConstants.CUSTOM_BANK);
    }

    public void setCustomMeso(long meso) {
        c.getPlayer().getQuestNAdd(MapleQuest.getInstance(GameConstants.CUSTOM_BANK)).setCustomData(meso + "");
    }

    public void enter_931060110() {
        try {
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/menuUI", 6000, 285, 186, 1, 0));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 900));
            Thread.sleep(900);
            c.getSession().write(CWvsContext.getTopMsg("First, click MENU at the bottom of the screen."));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseMoveToMenu", 1740, -114, -14, 1, 3));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1680));
            Thread.sleep(1680);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseClick", 1440, 246, 196, 1, 3));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1440));
            Thread.sleep(1440);
            c.getSession().write(CWvsContext.getTopMsg("Now, select Go to Farm."));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 600));
            Thread.sleep(600);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/menuOpen", 50000, 285, 186, 1, 2));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 600));
            Thread.sleep(600);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseMoveToMyfarm", 750, 246, 196, 1, 2));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 720));
            Thread.sleep(720);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/menuMouseOver", 50000, 285, 186, 1, 2));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseClick", 50000, 246, 166, 1, 3));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1440));
            Thread.sleep(1440);
        } catch (InterruptedException ex) {
        }
    }

    public void enter_931060120() {
        try {
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/character", 120000, -200, 0, 1, 1));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1200));
            Thread.sleep(1200);
            c.getSession().write(CWvsContext.getTopMsg("Hover over any other character..."));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseMoveToChar", 1680, -400, -210, 1, 3));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1650));
            Thread.sleep(1650);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseUp", 600, -190, -30, 1, 3));
            c.getSession().write(CWvsContext.getTopMsg("Then right-click."));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 540));
            Thread.sleep(540);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseClick", 1200, -190, -30, 1, 3));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1200));
            Thread.sleep(1200);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/characterMenu", 50000, -200, 0, 1, 2));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 900));
            Thread.sleep(900);
            c.getSession().write(CWvsContext.getTopMsg("When the Character Menu appears, click Go to Farm."));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseMoveToOtherfarm", 1440, -190, -30, 1, 5));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1380));
            Thread.sleep(1380);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/menuMouseOver", 50000, -200, 0, 1, 4));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/CharacterEff.img/farmEnterTuto/mouseClick", 60000, -130, 150, 1, 6));
            c.getSession().write(CField.UIPacket.getDirectionInfo((byte) 1, 1200));
            Thread.sleep(1200);
        } catch (InterruptedException ex) {
        }
    }

    public void showJettWanted() {
        try {
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 1000));
            Thread.sleep(1000);
            c.getSession().write(CField.environmentChange("newPirate/pendant_w", 13));
            c.getSession().write(UIPacket.getDirectionFacialExpression(5, 3000));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/DirectionNewPirate.img/newPirate/balloonMsg1/1", 2000, 0, -80, 0, 0));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 3000));
            Thread.sleep(3000);
            c.getSession().write(CField.UIPacket.getDirectionInfo(3, 1));
        } catch (InterruptedException ex) {
        }
        NPCScriptManager.getInstance().dispose(c);
        c.removeClickedNPC();
        NPCScriptManager.getInstance().start(c, 9270083, "np_tuto_0_2");
    }

    public void np_tuto_0_2() {
        try {
            c.getSession().write(CField.UIPacket.getDirectionInfo(3, 2));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 10));
            Thread.sleep(10);
            c.getSession().write(UIPacket.getDirectionFacialExpression(5, 3000));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/DirectionNewPirate.img/newPirate/balloonMsg1/1", 2000, 0, -80, 0, 0));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 1000));
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
        }
        NPCScriptManager.getInstance().dispose(c);
        c.removeClickedNPC();
        NPCScriptManager.getInstance().start(c, 9270083, "np_tuto_0_3");
    }

    public void spawnJettGuards() {
        try {
            c.getSession().write(CField.UIPacket.getDirectionInfo(3, 2));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 300));
            Thread.sleep(300);
            c.getSession().write(CField.UIPacket.getDirectionInfo(3, 0));
            c.getSession().write(UIPacket.getDirectionInfo("Effect/DirectionNewPirate.img/newPirate/balloonMsg1/3", 2000, 0, -80, 0, 0));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 500));
            Thread.sleep(500);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/DirectionNewPirate.img/newPirate/attack_tuto", 2000, 0, -80, 0, 0));
        } catch (InterruptedException ex) {
        }
        c.getSession().write(CField.UIPacket.IntroEnableUI(0));
        c.getSession().write(CWvsContext.getTopMsg("Eliminate all Guards."));
        forceStartQuest(53245);
        spawnMob(9420564, 3, 600, -120);
    }

    public static String getMobImg(int mob) {
        MapleMonster monster = MapleLifeFactory.getMonster(mob);
        if (monster.getStats().getLink() != 0) {
            mob = monster.getStats().getLink();
        }
        String mobStr = String.valueOf(mob);
        while (mobStr.length() < 7) {
            String newStr = "0" + mobStr;
            mobStr = newStr;
        }
        return "#fMob/" + mobStr + ".img/stand/0#";
    }

    public void showKannaMovie() {
        try {
            c.getSession().write(UIPacket.playMovie("JPKanna.avi", true));
            Thread.sleep(1 * 60 * 1000);
        } catch (InterruptedException e) {
        }
        c.getSession().write(CField.UIPacket.getDirectionStatus(false));
        c.getSession().write(CField.UIPacket.IntroEnableUI(0));
    }

    public void showMapleLeafScene() {
        try {
            c.getSession().write(UIPacket.getDirectionStatus(true));
            c.getSession().write(CField.UIPacket.IntroEnableUI(1));
            c.getSession().write(CField.environmentChange("adventureStory/mapleLeaf/0", 13));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 1800));
            Thread.sleep(1800);
        } catch (InterruptedException ex) {
        }
        c.getSession().write(CField.UIPacket.IntroEnableUI(0));
        NPCScriptManager.getInstance().dispose(c);
        c.removeClickedNPC();
        NPCScriptManager.getInstance().start(c, 10306, "ExplorerTut08");
    }

    public void showBeastTamerTutScene() {
        c.getSession().write(CField.UIPacket.IntroEnableUI(0));
        NPCScriptManager.getInstance().dispose(c);
        c.removeClickedNPC();
        NPCScriptManager.getInstance().start(c, 9390305, "BeastTamerTut01");
    }

    public void showBeastTamerTutScene1() {
        try {
            c.getSession().write(UIPacket.getDirectionStatus(true));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 1000));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 1000));
            Thread.sleep(2000);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/Direction14.img/effect/ShamanBT/balloonMsg/10", 2000, 0, -120, 1, 0));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 800));
            Thread.sleep(800);
            c.getSession().write(CField.UIPacket.getDirectionInfoNew((byte) 0, 1000, 700, 0));
            c.getSession().write(CField.UIPacket.getDirectionInfo(1, 1200));
            Thread.sleep(1200);
            c.getSession().write(UIPacket.getDirectionInfo("Effect/Direction14.img/effect/ShamanBT/BalloonMsg1/7", 2000, 571, -120, 1, 0));
            c.getSession().write(CField.environmentChange("ShamanBTTuto/sound0", 5));
            c.getSession().write(UIPacket.getDirectionInfo(1, 3000));
            Thread.sleep(3000);
            c.getSession().write(UIPacket.getDirectionInfo(1, 1000));
            c.getSession().write(UIPacket.getDirectionInfo(1, 500));
            Thread.sleep(1500);
            c.getSession().write(UIPacket.getDirectionFacialExpression(4, 5000));
            Thread.sleep(3000);
        } catch (InterruptedException ex) {
        }
        NPCScriptManager.getInstance().dispose(c);
        c.removeClickedNPC();
        NPCScriptManager.getInstance().start(c, 9390305, "BeastTamerTut02");
    }

    public String getSkillMenu(int skillMaster, int job) {
        String menu = "";
        int bookMin = 3;
        int bookMax = 3;
        if (MapleJob.is龍魔導士(job)) {
            bookMin = 9;
            bookMax = 9;
        } else if (MapleJob.is神之子(job)) {
            bookMin = 0;
            bookMax = 1;
        } else if (MapleJob.is影武者(job)) {
            if (skillMaster < 20) {
                bookMin = 2;
                bookMax = 4;
            } else {
                bookMin = 5;
                bookMax = 5;
            }
        } else if (!GameConstants.isSeparatedSp(job)) {
            bookMin = 0;
            bookMax = 0;
        }
        for (Map.Entry<Skill, SkillEntry> ret : c.getPlayer().getSkills().entrySet()) {
            int skillBook = GameConstants.getSkillBookBySkill(ret.getKey().getId());
            int skillJob = ret.getKey().getId() / 100000;
            if (skillBook < bookMin || skillBook > bookMax || skillJob != job / 10) {
                continue;
            }
            if ((ret.getKey().getMaxLevel() > 10 || skillMaster < 20) && ret.getValue().masterlevel < ret.getKey().getMaxLevel()) {
                if (skillMaster > 20) {
                    if (ret.getValue().masterlevel >= 30 || ret.getValue().masterlevel < 20 || ret.getKey().getMaxLevel() <= 20) {
                        continue;
                    }
                } else if (skillMaster > 10) {
                    if (ret.getValue().masterlevel >= 20) {
                        continue;
                    }
                }
                menu += "\r\n#L" + ret.getKey().getId() + "# #s" + ret.getKey().getId() + "# #fn字體##fs14##e#q" + ret.getKey().getId() + "##n#fs##fn##l";
            }
        }
        return menu;
    }

    public boolean canUseSkillBook(int skillId, int masterLevel) {
        if (masterLevel > 0) {
            int job = skillId / 10000;
            if (masterLevel == 10 && (MapleJob.中忍.getId() > job || MapleJob.隱忍.getId() < job)) {
                return false;
            }
            final Skill CurrSkillData = SkillFactory.getSkill(skillId);
            int skillLevel = c.getPlayer().getSkillLevel(CurrSkillData);
            if (skillLevel >= CurrSkillData.getMaxLevel()) {
                return false;
            }
            if ((skillLevel >= 5 && masterLevel <= 20) || (skillLevel >= 15 && masterLevel <= 30 && masterLevel > 20)) {
                return true;
            }
        }
        return false;
    }

    public void useSkillBook(int skillId, int masterLevel) {
        final Skill CurrSkillData = SkillFactory.getSkill(skillId);
        if (masterLevel == 10) {
            masterLevel = CurrSkillData.getMaxLevel();
        }
        masterLevel = masterLevel > CurrSkillData.getMaxLevel() ? CurrSkillData.getMaxLevel() : masterLevel;
        c.getPlayer().changeSingleSkillLevel(CurrSkillData, c.getPlayer().getSkillLevel(CurrSkillData), (byte) masterLevel);
        c.getPlayer().getMap().broadcastMessage(CWvsContext.useSkillBook(c.getPlayer(), 0, 0, true, true));
        c.getSession().write(CWvsContext.enableActions());
    }

    public void getDemonSelection() {
        c.getSession().write(NPCPacket.getDemonSelection());
        lastMsg = 0x17;
    }

    public void checkMedalQuest() {
        MapleQuest.MedalQuest m = null;
        for (MapleQuest.MedalQuest mq : MapleQuest.MedalQuest.values()) {
            for (int i : mq.maps) {
                if (c.getPlayer().getMapId() == i) {
                    m = mq;
                    break;
                }
            }
        }
        if (m != null && c.getPlayer().getLevel() >= m.level && c.getPlayer().getQuestStatus(m.questid) != 2) {
            if (c.getPlayer().getQuestStatus(m.lquestid) != 1) {
                MapleQuest.getInstance(m.lquestid).forceStart(c.getPlayer(), 0, "0");
            }
            if (c.getPlayer().getQuestStatus(m.questid) != 1) {
                MapleQuest.getInstance(m.questid).forceStart(c.getPlayer(), 0, null);
                final StringBuilder sb = new StringBuilder("enter=");
                for (int i = 0; i < m.maps.length; i++) {
                    sb.append("0");
                }
                c.getPlayer().updateInfoQuest(m.questid - 2005, sb.toString());
                MapleQuest.getInstance(m.questid - 1995).forceStart(c.getPlayer(), 0, "0");
            }
            String quest = c.getPlayer().getInfoQuest(m.questid - 2005);
            if (quest.length() != m.maps.length + 6) { //enter= is 6
                final StringBuilder sb = new StringBuilder("enter=");
                for (int i = 0; i < m.maps.length; i++) {
                    sb.append("0");
                }
                quest = sb.toString();
                c.getPlayer().updateInfoQuest(m.questid - 2005, quest);
            }
            final MapleQuestStatus stat = c.getPlayer().getQuestNAdd(MapleQuest.getInstance(m.questid - 1995));
            if (stat.getCustomData() == null) { //just a check.
                stat.setCustomData("0");
            }
            int number = Integer.parseInt(stat.getCustomData());
            final StringBuilder sb = new StringBuilder("enter=");
            boolean changedd = false;
            for (int i = 0; i < m.maps.length; i++) {
                boolean changed = false;
                if (c.getPlayer().getMapId() == m.maps[i]) {
                    if (quest.substring(i + 6, i + 7).equals("0")) {
                        sb.append("1");
                        changed = true;
                        changedd = true;
                    }
                }
                if (!changed) {
                    sb.append(quest.substring(i + 6, i + 7));
                }
            }
            if (changedd) {
                number++;
                c.getPlayer().updateInfoQuest(m.questid - 2005, sb.toString());
                MapleQuest.getInstance(m.questid - 1995).forceStart(c.getPlayer(), 0, String.valueOf(number));
                c.getPlayer().dropMessage(-1, "探險" + number + "/" + m.maps.length + "個地區");
                c.getPlayer().dropMessage(-1, "稱號- " + String.valueOf(m) + "挑戰中");
                c.getSession().write(CWvsContext.showQuestMsg("稱號- " + String.valueOf(m) + "挑戰中" + number + "/" + m.maps.length + "完成"));
            }
        }
    }
}
