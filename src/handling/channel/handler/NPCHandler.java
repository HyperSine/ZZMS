package handling.channel.handler;

import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.MapleClient;
import client.MapleCharacter;
import constants.GameConstants;
import client.MapleQuestStatus;
import client.RockPaperScissors;
import client.inventory.ItemFlag;
import constants.ItemConstants;
import constants.QuickMove;
import handling.SendPacketOpcode;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import server.shops.MapleShop;
import server.MapleInventoryManipulator;
import server.MapleStorage;
import server.life.MapleNPC;
import server.quest.MapleQuest;
import scripting.NPCScriptManager;
import scripting.NPCConversationManager;
import scripting.ScriptType;
import server.MapleItemInformationProvider;
import server.life.MapleLifeFactory;
import scripting.MapScriptMethods;
import tools.packet.CField;
import tools.Pair;
import tools.data.LittleEndianAccessor;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.CWvsContext;

public class NPCHandler {

    public static void NPCAnimation(LittleEndianAccessor slea, MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.NPC_ACTION.getValue());
        int length = (int) slea.available();
        if (length == 10) {
            mplew.writeInt(slea.readInt());
            mplew.writeShort(slea.readShort());
            mplew.writeInt(slea.readInt());
        } else if (length > 6) {
            mplew.write(slea.read(length - 9));
        } else {
            return;
        }
        c.getSession().write(mplew.getPacket());
    }

    public static final void NPCShop(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        byte bmode = slea.readByte();
        if (chr == null) {
            return;
        }

        switch (bmode) {
            case 0: {//購買
                MapleShop shop = chr.getShop();
                if (shop == null) {
                    return;
                }
                short slot = slea.readShort();
                int itemId = slea.readInt();
                short quantity = slea.readShort();
                // int unitprice = slea.readInt();
                shop.buy(c, slot, itemId, quantity);
                break;
            }
            case 1: {//出售
                MapleShop shop = chr.getShop();
                if (shop == null) {
                    return;
                }
                byte slot = (byte) slea.readShort();
                int itemId = slea.readInt();
                short quantity = slea.readShort();
                shop.sell(c, GameConstants.getInventoryType(itemId), slot, quantity);
                break;
            }
            case 2: {//充值
                MapleShop shop = chr.getShop();
                if (shop == null) {
                    return;
                }
                byte slot = (byte) slea.readShort();
                shop.recharge(c, slot);
                break;
            }
            default:
                chr.setConversation(0);
        }
    }

    public static final void NPCTalk(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null || chr.getMap() == null) {
            return;
        }
        final MapleNPC npc = chr.getMap().getNPCByOid(slea.readInt());
        if (npc == null) {
            if (c.getPlayer().isShowErr()) {
                c.getPlayer().showInfo("NPC對話", true, "當前地圖不存在此NPC");
            }
            return;
        }
        if (chr.hasBlockedInventory()) {
            if (c.getPlayer().isShowErr()) {
                c.getPlayer().showInfo("NPC對話", true, "無法進行對話,hasBlockedInventory-" + chr.hasBlockedInventory());
            }
            c.getPlayer().dropMessage(-1, "你當前已經和1個NPC對話了. 如果不是請輸入 @解卡 指令進行解卡。");
            return;
        }
        if (npc.hasShop()) {
            chr.setConversation(1);
            npc.sendShop(c);
        } else {
            NPCScriptManager.getInstance().start(c, npc.getId(), null);
        }
    }

    public static final void QuestAction(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final byte action = slea.readByte();
        int quest = slea.readUShort();
        if (quest == 20734) {
            c.getSession().write(CWvsContext.ultimateExplorer());
            return;
        }
        if (chr == null) {
            return;
        }

        boolean 冰原雪域的長老任務;
        switch (quest) {
            case 1430:
            case 1434:
            case 1438:
            case 1441:
            case 1444:
                冰原雪域的長老任務 = true;
                break;
            default:
                冰原雪域的長老任務 = false;
        }
        if (冰原雪域的長老任務 && c.getPlayer().getQuestStatus(quest) != 1 && c.getPlayer().getMapId() != 211000001) {//冒險家3轉傳送
            final server.maps.MapleMap mapz = handling.channel.ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(211000001);
            c.getPlayer().changeMap(mapz, mapz.getPortal(0));
        }

        final MapleQuest q = MapleQuest.getInstance(quest);
        switch (action) {
            case 0: { // Restore lost item
                //chr.updateTick(slea.readInt());
                slea.readInt();
                final int itemid = slea.readInt();
                q.RestoreLostItem(chr, itemid);
                break;
            }
            case 1: { // Start Quest
                final int npc = slea.readInt();
                if (npc == 0 && quest > 0) {
                    q.forceStart(chr, npc, null);
                } else if (!q.hasStartScript()) {
                    q.start(chr, npc);
                }
                break;
            }
            case 2: { // Complete Quest
                final int npc = slea.readInt();
                //chr.updateTick(slea.readInt());
                slea.readInt();
                if (q.hasEndScript()) {
                    return;
                }
                if (slea.available() >= 4) {
                    q.complete(chr, npc, slea.readInt());
                } else {
                    q.complete(chr, npc);
                }
                // c.getSession().write(CField.completeQuest(c.getPlayer(), quest));
                //c.getSession().write(CField.updateQuestInfo(c.getPlayer(), quest, npc, (byte)14));
                // 6 = start quest
                // 7 = unknown error
                // 8 = equip is full
                // 9 = not enough mesos
                // 11 = due to the equipment currently being worn wtf o.o
                // 12 = you may not posess more than one of this item
                break;
            }
            case 3: { // Forfeit Quest
                if (GameConstants.canForfeit(q.getId())) {
                    q.forfeit(chr);
                } else {
                    chr.dropMessage(1, "You may not forfeit this quest.");
                }
                break;
            }
            case 4: { // Scripted Start Quest
                final int npc = slea.readInt();
                if (chr.hasBlockedInventory()) {
                    if (c.getPlayer().isShowErr()) {
                        c.getPlayer().showInfo("NPC對話", true, "無法進行對話,hasBlockedInventory-" + chr.hasBlockedInventory());
                    }
                    c.getPlayer().dropMessage(-1, "你當前已經和1個NPC對話了. 如果不是請輸入 @解卡 指令進行解卡。");
                    return;
                }
                //c.getPlayer().updateTick(slea.readInt());
                NPCScriptManager.getInstance().startQuest(c, npc, quest);
                break;
            }
            case 5: { // Scripted End Quest
                final int npc = slea.readInt();
                if (chr.hasBlockedInventory()) {
                    if (c.getPlayer().isShowErr()) {
                        c.getPlayer().showInfo("NPC對話", true, "無法進行對話,hasBlockedInventory-" + chr.hasBlockedInventory());
                    }
                    c.getPlayer().dropMessage(-1, "你當前已經和1個NPC對話了. 如果不是請輸入 @解卡 指令進行解卡。");
                    return;
                }
                //c.getPlayer().updateTick(slea.readInt());
                NPCScriptManager.getInstance().endQuest(c, npc, quest, false);
//                c.getSession().write(EffectPacket.showQuetCompleteEffect());
//                chr.getMap().broadcastMessage(chr, EffectPacket.showQuetCompleteEffect(chr.getId()), false);
                break;
            }
        }
    }

    @SuppressWarnings("empty-statement")
    public static final void Storage(LittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        byte mode = slea.readByte();
        if (chr == null) {
            return;
        }
        MapleStorage storage = chr.getStorage();

        switch (mode) {
            case 3: {//二次密碼驗證(未完成)
                /*
                 String secondpw = slea.readMapleAsciiString();
                 if (c.CheckSecondPassword(secondpw)) {
                 c.getPlayer().setConversation(4);
                 c.getPlayer().getStorage().sendStorage(c, 0);
                 }*/
            }
            case 4: {//取出
                byte type = slea.readByte();
                byte slot = storage.getSlot(MapleInventoryType.getByType(type), slea.readByte());
                Item item = storage.takeOut(slot);

                if (item != null) {
                    MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                    short flag = item.getFlag();
                    if (ii.isDropRestricted(item.getItemId())) {
                        if (ItemFlag.KARMA_EQ.check(flag)) {
                            item.setFlag((short) (flag - ItemFlag.KARMA_EQ.getValue()));
                        } else if (ItemFlag.KARMA_USE.check(flag)) {
                            item.setFlag((short) (flag - ItemFlag.KARMA_USE.getValue()));
                        } else if (ItemFlag.KARMA_ACC.check(flag)) {
                            item.setFlag((short) (flag - ItemFlag.KARMA_ACC.getValue()));
                        } else if (ItemFlag.KARMA_ACC_USE.check(flag)) {
                            item.setFlag((short) (flag - ItemFlag.KARMA_ACC_USE.getValue()));
                        } else {
                            c.getSession().write(CWvsContext.enableActions());
                            return;
                        }
                    }
                    if (ItemFlag.UNTRADABLE.check(flag)) {
                        if (ItemFlag.KARMA_EQ.check(flag)) {
                            item.setFlag((short) (flag - ItemFlag.KARMA_EQ.getValue()));
                        } else if (ItemFlag.KARMA_USE.check(flag)) {
                            item.setFlag((short) (flag - ItemFlag.KARMA_USE.getValue()));
                        } else {
                            c.getSession().write(CWvsContext.enableActions());
                            return;
                        }
                    }
                    if (ii.isAccountShared(item.getItemId())) {
                        if (ItemFlag.KARMA_ACC.check(flag)) {
                            item.setFlag((short) (flag - ItemFlag.KARMA_ACC.getValue()));
                        } else if (ItemFlag.KARMA_ACC_USE.check(flag)) {
                            item.setFlag((short) (flag - ItemFlag.KARMA_ACC_USE.getValue()));
                        } else {
                            c.getSession().write(CWvsContext.enableActions());
                            return;
                        }
                    }
                    if (!MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), item.getOwner())) {
                        storage.store(item);
                        chr.dropMessage(1, "道具欄空間不足。");
                    } else {
                        MapleInventoryManipulator.addFromDrop(c, item, false);
                        storage.sendTakenOut(c, GameConstants.getInventoryType(item.getItemId()));
                    }
                } else {
                    c.getSession().write(CWvsContext.enableActions());
                }
                break;
            }
            case 5: {//放入倉庫
                byte slot = (byte) slea.readShort();
                int itemId = slea.readInt();
                MapleInventoryType type = GameConstants.getInventoryType(itemId);
                short quantity = slea.readShort();
                MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                if (quantity < 1) {
                    return;
                }
                if (storage.isFull()) {
                    c.getSession().write(CField.NPCPacket.getStorageFull());
                    return;
                }
                if (chr.getInventory(type).getItem((short) slot) == null) {
                    c.getSession().write(CWvsContext.enableActions());
                    return;
                }

                if (chr.getMeso() < 100L) {
                    chr.dropMessage(1, "You don't have enough mesos to store the item");
                } else {
                    Item item = chr.getInventory(type).getItem((short) slot).copy();

                    if (ItemConstants.類型.寵物(item.getItemId())) {
                        c.getSession().write(CWvsContext.enableActions());
                        return;
                    }
                    short flag = item.getFlag();
                    if ((ii.isPickupRestricted(item.getItemId())) && (storage.findById(item.getItemId()) != null)) {
                        c.getSession().write(CWvsContext.enableActions());
                        return;
                    }
                    if ((item.getItemId() == itemId) && ((item.getQuantity() >= quantity) || ItemConstants.類型.可充值道具(itemId))) {
                        if (ii.isDropRestricted(item.getItemId()) && !ItemFlag.KARMA_EQ.check(flag) && !ItemFlag.KARMA_USE.check(flag) && !ItemFlag.KARMA_ACC.check(flag) && !ItemFlag.KARMA_ACC_USE.check(flag)) {
                            c.getSession().write(CWvsContext.enableActions());
                            return;
                        }
                        if (ItemFlag.UNTRADABLE.check(flag) && !ItemFlag.KARMA_EQ.check(flag) && !ItemFlag.KARMA_USE.check(flag)) {
                            c.getSession().write(CWvsContext.enableActions());
                            return;
                        }
                        if (ii.isAccountShared(item.getItemId()) && !ItemFlag.KARMA_ACC.check(flag) && !ItemFlag.KARMA_ACC_USE.check(flag)) {
                            c.getSession().write(CWvsContext.enableActions());
                            return;
                        }
                        if (ItemConstants.類型.可充值道具(itemId)) {
                            quantity = item.getQuantity();
                        }
                        chr.gainMeso(-100L, false, false);
                        MapleInventoryManipulator.removeFromSlot(c, type, (short) slot, quantity, false);
                        item.setQuantity(quantity);
                        storage.store(item);
                    } else {
                        return;
                    }
                }
                storage.sendStored(c, GameConstants.getInventoryType(itemId));
                break;
            }
            case 6:
                storage.arrange();
                storage.update(c);
                break;
            case 7: {//楓幣存取操作
                long meso = slea.readLong();
                long storageMesos = storage.getMeso();
                long playerMesos = chr.getMeso();

                if ((meso > 0L && storageMesos >= meso) || (meso < 0L && playerMesos >= -meso)) {
                    if (meso < 0L && storageMesos - meso > 9999999999999L) {//保管
                        meso = -(9999999999999L - storageMesos);
                        if (-meso > playerMesos) {
                            return;
                        }
                    } else if (meso > 0L && playerMesos + meso > 99999999999L) {//取出
                        meso = 99999999999L - playerMesos;
                        if (meso > storageMesos) {
                            return;
                        }
                    }
                    storage.setMeso(storageMesos - meso);
                    chr.gainMeso(meso, false, false);
                } else {
                    return;
                }
                storage.sendMeso(c);
                break;
            }
            case 8:
                storage.close();
                chr.setConversation(0);
                break;
            default:
                System.out.println("未處理的倉庫操作碼 : " + mode);
        }
    }

    public static void NPCMoreTalk(final LittleEndianAccessor slea, final MapleClient c) {
        final byte lastMsg = slea.readByte(); // 00 (last msg type I think)

        if (lastMsg == 0x12 && c.getPlayer().hasMapScript()) {
            byte action = slea.readByte();
            if (action != 1) {
                return;
            }
            byte lastbyte = slea.readByte(); // 00 = end chat, 01 == follow
            if (lastbyte == 0) {
                c.getSession().write(CWvsContext.enableActions());
            } else {
                switch (c.getPlayer().getMapScript().getLeft()) {
                    case directionInfo:
                        MapScriptMethods.startDirectionInfo(c.getPlayer(), true);
                        break;
                    case onFirstUserEnter:
                        MapScriptMethods.startScript_FirstUser(c, c.getPlayer().getMapScript().getRight());
                        break;
                    case onUserEnter:
                        MapScriptMethods.startScript_User(c, c.getPlayer().getMapScript().getRight());
                        break;
                }
//                MapScriptMethods.startDirectionInfo(c.getPlayer(), lastMsg == 0x13);
//                c.getSession().write(CWvsContext.enableActions());
            }
            return;
        }

        if (lastMsg == 9 && slea.available() >= 4) {
            slea.readShort();
        }
        final byte action = slea.readByte(); // 00 = end chat, 01 == follow

        final NPCConversationManager cm = NPCScriptManager.getInstance().getCM(c);
        /*if (cm != null && lastMsg == 0x17) {
         c.getPlayer().handleDemonJob(slea.readInt());
         return;
         }*/
        if (cm == null || c.getPlayer().getConversation() == 0 || cm.getLastMsg() != lastMsg) {
            return;
        }
        cm.setLastMsg((byte) -1);
        if (lastMsg == 1) {
            NPCScriptManager.getInstance().action(c, action, lastMsg, -1);
        } else if (lastMsg == 3) {
            if (action != 0) {
                cm.setGetText(slea.readMapleAsciiString());
                if (cm.getType() == ScriptType.QUEST_START) {
                    NPCScriptManager.getInstance().startQuest(c, action, lastMsg, -1);
                } else if (cm.getType() == ScriptType.QUEST_END) {
                    NPCScriptManager.getInstance().endQuest(c, action, lastMsg, -1);
                } else {
                    NPCScriptManager.getInstance().action(c, action, lastMsg, -1);
                }
            } else {
                cm.dispose();
            }
        } else {
            if (lastMsg == 0x13 && action == 0) {
                c.getSession().write(CWvsContext.getTopMsg("影片播放失敗。"));
            }
            int selection = -1;
            if (slea.available() >= 4) {
                selection = slea.readInt();
            } else if (slea.available() > 0) {
                selection = slea.readByte();
            }
            if (lastMsg == 4 && selection == -1) {
                cm.dispose();
                return;//h4x
            }
            if (selection >= -1 && action != -1) {
                if (cm.getType() == ScriptType.QUEST_START) {
                    NPCScriptManager.getInstance().startQuest(c, action, lastMsg, selection);
                } else if (cm.getType() == ScriptType.QUEST_END) {
                    NPCScriptManager.getInstance().endQuest(c, action, lastMsg, selection);
                } else {
                    NPCScriptManager.getInstance().action(c, action, lastMsg, selection);
                }
            } else {
                cm.dispose();
            }
        }
    }

    public static void DirectionComplete(final LittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer().hasMapScript()) {
            switch (c.getPlayer().getMapScript().getLeft()) {
                case directionInfo:
                    MapScriptMethods.startDirectionInfo(c.getPlayer(), true);
                    break;
                case onFirstUserEnter:
                    MapScriptMethods.startScript_FirstUser(c, c.getPlayer().getMapScript().getRight());
                    break;
                case onUserEnter:
                    MapScriptMethods.startScript_User(c, c.getPlayer().getMapScript().getRight());
                    break;
            }
            return;
        }

        final NPCConversationManager cm = NPCScriptManager.getInstance().getCM(c);

        if (cm == null || c.getPlayer().getConversation() == 0) {
            return;
        }
        cm.setLastMsg((byte) -1);
        if (cm.getType() == ScriptType.QUEST_START) {
            NPCScriptManager.getInstance().startQuest(c, (byte) 1, (byte) 0, 0);
        } else if (cm.getType() == ScriptType.QUEST_END) {
            NPCScriptManager.getInstance().endQuest(c, (byte) 1, (byte) 0, 0);
        } else {
            NPCScriptManager.getInstance().action(c, (byte) 1, (byte) 0, 0);
        }
    }

    public static final void repairAll(final MapleClient c) {
        Equip eq;
        double rPercentage;
        int price = 0;
        Map<String, Integer> eqStats;
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final Map<Equip, Integer> eqs = new HashMap<>();
        final MapleInventoryType[] types = {MapleInventoryType.EQUIP, MapleInventoryType.EQUIPPED};
        for (MapleInventoryType type : types) {
            for (Item item : c.getPlayer().getInventory(type).newList()) {
                if (item instanceof Equip) { //redundant
                    eq = (Equip) item;
                    if (eq.getDurability() >= 0) {
                        eqStats = ii.getEquipStats(eq.getItemId());
                        if (eqStats.containsKey("durability") && eqStats.get("durability") > 0 && eq.getDurability() < eqStats.get("durability")) {
                            rPercentage = (100.0 - Math.ceil((eq.getDurability() * 1000.0) / (eqStats.get("durability") * 10.0)));
                            eqs.put(eq, eqStats.get("durability"));
                            price += (int) Math.ceil(rPercentage * ii.getPrice(eq.getItemId()) / (ii.getReqLevel(eq.getItemId()) < 70 ? 100.0 : 1.0));
                        }
                    }
                }
            }
        }
        if (eqs.size() <= 0 || c.getPlayer().getMeso() < price) {
            return;
        }
        c.getPlayer().gainMeso(-price, true);
        Equip ez;
        for (Entry<Equip, Integer> eqqz : eqs.entrySet()) {
            ez = eqqz.getKey();
            ez.setDurability(eqqz.getValue());
            c.getPlayer().forceReAddItem(ez.copy(), ez.getPosition() < 0 ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP);
        }
    }

    public static final void repair(final LittleEndianAccessor slea, final MapleClient c) {
        if (slea.available() < 4) { //leafre for now
            return;
        }
        final int position = slea.readInt(); //who knows why this is a int
        final MapleInventoryType type = position < 0 ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP;
        final Item item = c.getPlayer().getInventory(type).getItem((byte) position);
        if (item == null) {
            return;
        }
        final Equip eq = (Equip) item;
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final Map<String, Integer> eqStats = ii.getEquipStats(item.getItemId());
        if (eq.getDurability() < 0 || !eqStats.containsKey("durability") || eqStats.get("durability") <= 0 || eq.getDurability() >= eqStats.get("durability")) {
            return;
        }
        final double rPercentage = (100.0 - Math.ceil((eq.getDurability() * 1000.0) / (eqStats.get("durability") * 10.0)));
        //drpq level 105 weapons - ~420k per %; 2k per durability point
        //explorer level 30 weapons - ~10 mesos per %
        final int price = (int) Math.ceil(rPercentage * ii.getPrice(eq.getItemId()) / (ii.getReqLevel(eq.getItemId()) < 70 ? 100.0 : 1.0)); // / 100 for level 30?
        if (c.getPlayer().getMeso() < price) {
            return;
        }
        c.getPlayer().gainMeso(-price, false);
        eq.setDurability(eqStats.get("durability"));
        c.getPlayer().forceReAddItem(eq.copy(), type);
    }

    public static final void UpdateQuest(final LittleEndianAccessor slea, final MapleClient c) {
        final MapleQuest quest = MapleQuest.getInstance(slea.readShort());
        if (quest != null) {
            c.getPlayer().updateQuest(c.getPlayer().getQuest(quest), true);
        }
    }

    public static final void UseItemQuest(final LittleEndianAccessor slea, final MapleClient c) {
        final short slot = slea.readShort();
        final int itemId = slea.readInt();
        final Item item = c.getPlayer().getInventory(MapleInventoryType.ETC).getItem(slot);
        final int qid = slea.readInt();
        final MapleQuest quest = MapleQuest.getInstance(qid);
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Pair<Integer, List<Integer>> questItemInfo = null;
        boolean found = false;
        for (Item i : c.getPlayer().getInventory(MapleInventoryType.ETC)) {
            if (i.getItemId() / 10000 == 422) {
                questItemInfo = ii.questItemInfo(i.getItemId());
                if (questItemInfo != null && questItemInfo.getLeft() == qid && questItemInfo.getRight() != null && questItemInfo.getRight().contains(itemId)) {
                    found = true;
                    break; //i believe it's any order
                }
            }
        }
        if (quest != null && found && item != null && item.getQuantity() > 0 && item.getItemId() == itemId) {
            final int newData = slea.readInt();
            final MapleQuestStatus stats = c.getPlayer().getQuestNoAdd(quest);
            if (stats != null && stats.getStatus() == 1) {
                stats.setCustomData(String.valueOf(newData));
                c.getPlayer().updateQuest(stats, true);
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.ETC, slot, (short) 1, false);
            }
        }
    }

    public static final void RPSGame(LittleEndianAccessor slea, MapleClient c) {
        if ((slea.available() == 0L) || (c.getPlayer() == null) || (c.getPlayer().getMap() == null) || (!c.getPlayer().getMap().containsNPC(9000019))) {
            if ((c.getPlayer() != null) && (c.getPlayer().getRPS() != null)) {
                c.getPlayer().getRPS().dispose(c);
            }
            return;
        }
        byte mode = slea.readByte();
        switch (mode) {
            case 0:
            case 5:
                if (c.getPlayer().getRPS() != null) {
                    c.getPlayer().getRPS().reward(c);
                }
                if (c.getPlayer().getMeso() >= 1000L) {
                    c.getPlayer().setRPS(new RockPaperScissors(c, mode));
                } else {
                    c.getSession().write(CField.getRPSMode((byte) 8, -1, -1, -1));
                }
                break;
            case 1:
                if ((c.getPlayer().getRPS() == null) || (!c.getPlayer().getRPS().answer(c, slea.readByte()))) {
                    c.getSession().write(CField.getRPSMode((byte) 13, -1, -1, -1));
                }
                break;
            case 2:
                if ((c.getPlayer().getRPS() == null) || (!c.getPlayer().getRPS().timeOut(c))) {
                    c.getSession().write(CField.getRPSMode((byte) 13, -1, -1, -1));
                }
                break;
            case 3:
                if ((c.getPlayer().getRPS() == null) || (!c.getPlayer().getRPS().nextRound(c))) {
                    c.getSession().write(CField.getRPSMode((byte) 13, -1, -1, -1));
                }
                break;
            case 4:
                if (c.getPlayer().getRPS() != null) {
                    c.getPlayer().getRPS().dispose(c);
                } else {
                    c.getSession().write(CField.getRPSMode((byte) 13, -1, -1, -1));
                }
                break;
        }
    }

    public static void OpenQuickMoveNpc(final LittleEndianAccessor slea, final MapleClient c) {
        final int npcid = slea.readInt();
        if (c.getPlayer().hasBlockedInventory() || c.getPlayer().isInBlockedMap() || c.getPlayer().getLevel() < 10) {
            c.getPlayer().dropMessage(-1, "你當前已經和1個NPC對話了. 如果不是請輸入 @解卡 指令進行解卡。");
            return;
        }
        int npcId = -1;
        for (QuickMove qm : QuickMove.values()) {
            if (qm.getMap() == c.getPlayer().getMapId()) {
                List<QuickMove.QuickMoveNPC> qmn = new LinkedList();
                long npcs = qm.getNPCFlag();
                for (QuickMove.QuickMoveNPC npc : QuickMove.QuickMoveNPC.values()) {
                    if ((npcs & npc.getValue()) != 0 && npc.getId() == npcid) {
                        npcId = npcid;
                        break;
                    }
                }
            }
        }

        if (npcId == -1 && QuickMove.GLOBAL_NPC != 0 && !GameConstants.isBossMap(c.getPlayer().getMapId()) && !GameConstants.isTutorialMap(c.getPlayer().getMapId())) {
            for (QuickMove.QuickMoveNPC npc : QuickMove.QuickMoveNPC.values()) {
                if ((QuickMove.GLOBAL_NPC & npc.getValue()) != 0 && npc.getId() == npcid) {
                    npcId = npcid;
                    break;
                }
            }
        }

        if (npcId == -1) {
            System.err.println("未找到QuickMove的NPC:" + npcId);
            return;
        }
        final MapleNPC npc = MapleLifeFactory.getNPC(npcId);
        if (npc == null) {
            System.err.println("未找到QuickMove的NPC:" + npcId);
            return;
        }
        if (npc.hasShop()) {
            c.getPlayer().setConversation(1);
            npc.sendShop(c);
        } else {
            NPCScriptManager.getInstance().start(c, npcId, null);
        }
    }

    public static void OpenQuickMoveSpecial(final LittleEndianAccessor slea, MapleClient c) {
        final int selection = slea.readInt();
        if (c.getPlayer().hasBlockedInventory() || c.getPlayer().isInBlockedMap() || c.getPlayer().getLevel() < 10) {
            return;
        }
        QuickMove.QuickMoveNPC quickMove = null;
        for (QuickMove qm : QuickMove.values()) {
            if (qm.getMap() == c.getPlayer().getMapId()) {
                long npcs = qm.getNPCFlag();
                int i = 0;
                for (QuickMove.QuickMoveNPC npc : QuickMove.QuickMoveNPC.values()) {
                    if (!npc.show() || (npcs & npc.getValue()) == 0) {
                        continue;
                    }
                    if (selection == i++) {
                        quickMove = npc;
                        break;
                    }
                }
                break;
            }
        }

        if (quickMove == null && QuickMove.GLOBAL_NPC != 0 && !GameConstants.isBossMap(c.getPlayer().getMapId()) && !GameConstants.isTutorialMap(c.getPlayer().getMapId())) {
            int i = 0;
            for (QuickMove.QuickMoveNPC npc : QuickMove.QuickMoveNPC.values()) {
                if (!npc.show() || (QuickMove.GLOBAL_NPC & npc.getValue()) == 0) {
                    continue;
                }
                if (selection == i++) {
                    quickMove = npc;
                    break;
                }
            }
        }

        if (quickMove == null) {
            System.err.println("未找到QuickMove動作, 選項:" + selection);
            return;
        }
        int npcId = 0;
        String special = null;
        switch (quickMove) {
            default:
                System.err.println("未處理QuickMove動作, 類型:" + quickMove.getType());
        }
        if (npcId > 0) {
            final MapleNPC npc = MapleLifeFactory.getNPC(npcId);
            if (npc.hasShop()) {
                c.getPlayer().setConversation(1);
                npc.sendShop(c);
            } else {
                NPCScriptManager.getInstance().start(c, npcId, null);
            }
        }
    }
    
    public static void OpenZeroQuickMoveSpecial(final LittleEndianAccessor slea, MapleClient c) {
        final int type = slea.readShort();
        final int selection = slea.readShort();
        if (c.getPlayer().hasBlockedInventory() || c.getPlayer().isInBlockedMap() || c.getPlayer().getLevel() < 10) {
            return;
        }
        if (type == 2) {
            switch(selection) {
                case 17491:
                    c.getPlayer().changeMap(321190100, 0);
                    break;
                case 17996:
                    c.getPlayer().changeMap(321100000, 0);
                    break;
            }
        }
    }
}
