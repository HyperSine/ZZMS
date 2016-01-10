/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.commands;

import client.*;
import client.inventory.*;
import constants.GameConstants;
import handling.channel.ChannelServer;
import handling.world.World;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import scripting.EventInstanceManager;
import scripting.EventManager;
import scripting.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.commands.InternCommand.封號;
import server.commands.InternCommand.臨時封號;
import server.events.MapleEvent;
import server.events.MapleEventType;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterInformationProvider;
import server.life.MapleNPC;
import server.life.OverrideMonsterStats;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleReactor;
import server.quest.MapleQuest;
import server.shops.MapleShopFactory;
import tools.FileoutputUtil;
import tools.StringUtil;
import tools.packet.CField;
import tools.packet.CWvsContext;
import tools.packet.CWvsContext.InventoryPacket;

/**
 *
 * @author Emilyx3
 */
public class GMCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.GM;
    }

    public static class 玩家飛 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            SkillFactory.getSkill(1146).getEffect(1).applyTo(chr);
            SkillFactory.getSkill(1142).getEffect(1).applyTo(chr);
            chr.dispelBuff(1146);
            return 1;
        }
    }

    public static class 全圖飛 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (MapleCharacter mch : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                SkillFactory.getSkill(1146).getEffect(1).applyTo(mch);
                SkillFactory.getSkill(1142).getEffect(1).applyTo(mch);
                mch.dispelBuff(1146);
            }
            return 1;
        }
    }

    public static class 給予寵物 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 7) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱> <寵物道具ID> <寵物名稱> <寵物等級> <寵物親密度> <寵物飢餓感>");
                return 0;
            }
            MapleCharacter petowner = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            int id = Integer.parseInt(splitted[2]);
            String name = splitted[3];
            int level = Integer.parseInt(splitted[4]);
            int closeness = Integer.parseInt(splitted[5]);
            int fullness = Integer.parseInt(splitted[6]);
            long period = 20000;
            short flags = 0;
            if (id >= 5001000 || id < 5000000) {
                c.getPlayer().dropMessage(0, "寵物道具ID錯誤");
                return 0;
            }
            if (level > 30) {
                level = 30;
            }
            if (closeness > 30000) {
                closeness = 30000;
            }
            if (fullness > 100) {
                fullness = 100;
            }
            if (level < 1) {
                level = 1;
            }
            if (closeness < 0) {
                closeness = 0;
            }
            if (fullness < 0) {
                fullness = 0;
            }
            try {
                MapleInventoryManipulator.addById(petowner.getClient(), id, (short) 1, "", MaplePet.createPet(id, name, level, closeness, fullness, MapleInventoryIdentifier.getInstance(), id == 5000054 ? (int) period : 0, flags, 0), 45, MapleInventoryManipulator.DAY, c.getPlayer().getName() + "使用" + splitted[0] + "指令製作");
            } catch (NullPointerException ex) {
            }
            return 1;
        }
    }

    public static class 獲得技能 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <技能ID> (技能等級:默認1) (技能最高等級:默認1)");
                return 0;
            }
            Skill skill = SkillFactory.getSkill(Integer.parseInt(splitted[1]));
            byte level = (byte) CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);
            byte masterlevel = (byte) CommandProcessorUtil.getOptionalIntArg(splitted, 3, 1);

            if (level > skill.getMaxLevel()) {
                level = (byte) skill.getMaxLevel();
            }
            if (masterlevel > skill.getMaxLevel()) {
                masterlevel = (byte) skill.getMaxLevel();
            }
            c.getPlayer().changeSingleSkillLevel(skill, level, masterlevel);
            return 1;
        }
    }

    public static class 增加人氣 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱> <數量>");
                return 0;
            }
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            int fame;
            try {
                fame = Integer.parseInt(splitted[2]);
            } catch (NumberFormatException nfe) {
                c.getPlayer().dropMessage(6, "數量無效...");
                return 0;
            }
            if (victim != null && player.allowedToTarget(victim)) {
                victim.addFame(fame);
                victim.updateSingleStat(MapleStat.FAME, victim.getFame());
            }
            return 1;
        }
    }

    public static class 技能點 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().setRemainingSp(CommandProcessorUtil.getOptionalIntArg(splitted, 1, 1));
            c.getPlayer().updateSingleStat(MapleStat.AVAILABLESP, 0);
            return 1;
        }
    }

    public static class 轉數技能點 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, splitted[0] + " <轉職次數> <技能點數>");
                return 0;
            }
            c.getPlayer().setRemainingSp(CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1), Integer.parseInt(splitted[1]) - 1);
            c.getPlayer().updateSingleStat(MapleStat.AVAILABLESP, 0);
            return 1;
        }
    }

    public static class 職業 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <職業ID>");
                return 0;
            }
            int jobid = Integer.parseInt(splitted[1]);
            if (!MapleJob.isExist(jobid)) {
                c.getPlayer().dropMessage(5, "職業ID無效");
                return 0;
            }
            c.getPlayer().changeJob((short) jobid);
            c.getPlayer().setSubcategory(c.getPlayer().getSubcategory());
            return 1;
        }
    }

    public static class 玩家職業 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱> <職業ID>");
                return 0;
            }
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (!MapleJob.isExist(Integer.parseInt(splitted[2]))) {
                c.getPlayer().dropMessage(5, "職業ID無效");
                return 0;
            }
            victim.changeJob((short) Integer.parseInt(splitted[2]));
            c.getPlayer().setSubcategory(c.getPlayer().getSubcategory());
            return 1;
        }
    }

    public static class 商店 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <商店ID>");
                return 0;
            }
            MapleShopFactory shop = MapleShopFactory.getInstance();
            int shopId = Integer.parseInt(splitted[1]);
            if (shop.getShop(shopId) != null) {
                shop.getShop(shopId).sendShop(c);
            }
            return 1;
        }
    }

    public static class 升級 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().getLevel() < 250) {
                c.getPlayer().gainExp(c.getPlayer().getNeededExp() - c.getPlayer().getExp(), true, false, true);
            }
            return 1;
        }
    }

    public static class 升級到 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <等級>");
                return 0;
            }
            //for (int i = 0; i < Integer.parseInt(splitted[1]) - c.getPlayer().getLevel(); i++) {
            while (c.getPlayer().getLevel() < Integer.parseInt(splitted[1])) {
                if (c.getPlayer().getLevel() < 250) {
                    c.getPlayer().gainExp(c.getPlayer().getNeededExp() - c.getPlayer().getExp(), true, false, true);
                }
            }
            //}
            return 1;
        }
    }

    public static class 升級玩家到 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱> <等级>");
                return 0;
            }
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            //for (int i = 0; i < Integer.parseInt(splitted[2]) - victim.getLevel(); i++) {
            while (victim.getLevel() < Integer.parseInt(splitted[2])) {
                if (victim.getLevel() < 250) {
                    victim.gainExp(c.getPlayer().getNeededExp() - c.getPlayer().getExp(), true, false, true);
                }
            }
            //}
            return 1;
        }
    }

    public static class 製作潛能道具 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 8) {
                c.getPlayer().dropMessage(6, splitted[0] + " <道具ID> <第一潛能> <第二潛能> <第三潛能> <第一附加潛能> <第二附加潛能> <第三附加潛能>");
                return 0;
            }
            final int itemId = Integer.parseInt(splitted[1]);
            if (!c.getPlayer().isAdmin()) {
                for (int i : GameConstants.itemBlock) {
                    if (itemId == i) {
                        c.getPlayer().dropMessage(5, "當前管理員等級沒有製作此道具的權限");
                        return 0;
                    }
                }
            }
            short flag = (short) ItemFlag.LOCK.getValue();
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            if (itemId >= 2000000) {
                c.getPlayer().dropMessage(5, "道具ID必須為裝備");
            } else if (!ii.itemExists(itemId)) {
                c.getPlayer().dropMessage(5, "道具不存在");
            } else {
                Equip equip;
                equip = ii.randomizeStats((Equip) ii.getEquipById(itemId));
                equip.setPotential(Integer.parseInt(splitted[2]), 1, false);
                equip.setPotential(Integer.parseInt(splitted[3]), 2, false);
                equip.setPotential(Integer.parseInt(splitted[4]), 3, false);
                equip.updateState(false);
                equip.setPotential(Integer.parseInt(splitted[5]), 1, true);
                equip.setPotential(Integer.parseInt(splitted[6]), 2, true);
                equip.setPotential(Integer.parseInt(splitted[7]), 3, true);
                equip.updateState(true);
                equip.setGMLog(c.getPlayer().getName() + " 使用 " + splitted[0] + " 指令製作, 時間:" + FileoutputUtil.CurrentReadable_Time());
                if (!c.getPlayer().isGM()) {
                    equip.setFlag(flag);
                }
                if (!c.getPlayer().isAdmin()) {
                    equip.setOwner(c.getPlayer().getName());
                }
                MapleInventoryManipulator.addbyItem(c, equip);
            }
            return 1;
        }
    }

    public static class 製作屬性道具 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (!c.getPlayer().isAdmin()) {
                return 0;
            }
            if (splitted.length < 4) {
                c.getPlayer().dropMessage(6, splitted[0] + " <道具ID> <屬性值> <潛能>");
                return 0;
            }
            final int itemId = Integer.parseInt(splitted[1]);
            short flag = (short) ItemFlag.LOCK.getValue();
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            if (itemId >= 2000000) {
                c.getPlayer().dropMessage(5, "道具ID必須為裝備");
            } else if (!ii.itemExists(itemId)) {
                c.getPlayer().dropMessage(5, "道具不存在");
            } else {
                Equip equip;
                equip = ii.randomizeStats((Equip) ii.getEquipById(itemId));
                equip.setStr(Short.parseShort(splitted[2]));
                equip.setDex(Short.parseShort(splitted[2]));
                equip.setInt(Short.parseShort(splitted[2]));
                equip.setLuk(Short.parseShort(splitted[2]));
                equip.setWatk(Short.parseShort(splitted[2]));
                equip.setMatk(Short.parseShort(splitted[2]));
                equip.setPotential(Integer.parseInt(splitted[3]), 1, false);
                equip.setPotential(Integer.parseInt(splitted[3]), 2, false);
                equip.setPotential(Integer.parseInt(splitted[3]), 3, false);
                equip.updateState(false);
                equip.setGMLog(c.getPlayer().getName() + " 使用 " + splitted[0] + " 指令製作, 時間:" + FileoutputUtil.CurrentReadable_Time());
                if (!c.getPlayer().isGM()) {
                    equip.setFlag(flag);
                }
                if (!c.getPlayer().isAdmin()) {
                    equip.setOwner(c.getPlayer().getName());
                }
                MapleInventoryManipulator.addbyItem(c, equip);
            }
            return 1;
        }
    }

    public static class 等級 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <等級>");
                return 0;
            }
            c.getPlayer().setLevel(Short.parseShort(splitted[1]));
            c.getPlayer().updateSingleStat(MapleStat.LEVEL, Integer.parseInt(splitted[1]));
            c.getPlayer().setExp(0);
            return 1;
        }
    }

    public static class 玩家等級 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            victim.setLevel(Short.parseShort(splitted[2]));
            victim.updateSingleStat(MapleStat.LEVEL, Integer.parseInt(splitted[2]));
            victim.setExp(0);
            return 1;
        }
    }

    public static class 開始自動事件 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            final EventManager em = c.getChannelServer().getEventSM().getEventManager("AutomatedEvent");
            if (em != null) {
                em.setWorldEvent();
                em.scheduleRandomEvent();
                System.out.println("正在進行隨機自動事件");
            } else {
                System.out.println("找不到AutomatedEvent腳本");
            }
            return 1;
        }
    }

    public static class 設置事件 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleEvent.onStartEvent(c.getPlayer());
            return 1;
        }
    }

    public static class 自動事件 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleEvent.onStartEvent(c.getPlayer());
            return 1;
        }
    }

    public static class 開始事件 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getChannelServer().getEvent() == c.getPlayer().getMapId()) {
                MapleEvent.setEvent(c.getChannelServer(), false);
                c.getPlayer().dropMessage(5, "Started the event and closed off");
                return 1;
            } else {
                c.getPlayer().dropMessage(5, "此指令必須在事件地圖才能使用");
                return 0;
            }
        }
    }

    public static class 事件 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <腳本名>");
                return 0;
            }
            final MapleEventType type = MapleEventType.getByString(splitted[1]);
            if (type == null) {
                final StringBuilder sb = new StringBuilder("Wrong syntax: ");
                for (MapleEventType t : MapleEventType.values()) {
                    sb.append(t.name()).append(",");
                }
                c.getPlayer().dropMessage(5, sb.toString().substring(0, sb.toString().length() - 1));
                return 0;
            }
            final String msg = MapleEvent.scheduleEvent(type, c.getChannelServer());
            if (msg.length() > 0) {
                c.getPlayer().dropMessage(5, msg);
                return 0;
            }
            return 1;
        }
    }

    public static class 清除玩家道具 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱> <道具ID>");
                return 0;
            }
            MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (chr == null) {
                c.getPlayer().dropMessage(6, "玩家不存在");
                return 0;
            }
            chr.removeAll(Integer.parseInt(splitted[2]), false);
            c.getPlayer().dropMessage(6, "玩家 " + splitted[1] + " 所有的道具(ID:" + splitted[2] + ")已被清除");
            return 1;

        }
    }

    public static class 封印道具 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱> <道具ID>");
                return 0;
            }
            MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (chr == null) {
                c.getPlayer().dropMessage(6, "找不到角色");
                return 0;
            }
            int itemid = Integer.parseInt(splitted[2]);
            MapleInventoryType type = GameConstants.getInventoryType(itemid);
            for (Item item : chr.getInventory(type).listById(itemid)) {
                item.setFlag((byte) (item.getFlag() | ItemFlag.LOCK.getValue()));
                chr.getClient().getSession().write(InventoryPacket.updateSpecialItemUse(item, type.getType(), item.getPosition(), true, chr));
            }
            if (type == MapleInventoryType.EQUIP) {
                type = MapleInventoryType.EQUIPPED;
                for (Item item : chr.getInventory(type).listById(itemid)) {
                    item.setFlag((byte) (item.getFlag() | ItemFlag.LOCK.getValue()));
                    //chr.getClient().getSession().write(CField.updateSpecialItemUse(item, type.getType()));
                }
            }
            c.getPlayer().dropMessage(6, "已將ID為 " + splitted[2] + " 的所有道具鎖定, 被執行角色 " + splitted[1]);
            return 1;
        }
    }

    public static class Smega extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            World.Broadcast.broadcastSmega(CWvsContext.broadcastMsg(3, c.getPlayer() == null ? c.getChannel() : c.getPlayer().getClient().getChannel(), c.getPlayer() == null ? c.getPlayer().getName() : c.getPlayer().getName() + " : " + StringUtil.joinStringFrom(splitted, 1), true));
            /*if (splitted.length < 2) {
             c.getPlayer().dropMessage(0, "!smega <itemid> <message>");
             return 0;
             }
             final List<String> lines = new LinkedList<>();
             for (int i = 0; i < 4; i++) {
             final String text = StringUtil.joinStringFrom(splitted, 2);
             if (text.length() > 55) {
             continue;
             }
             lines.add(text);
             }
             final boolean ear = true;
             World.Broadcast.broadcastSmega(CWvsContext.getAvatarMega(c.getPlayer(), c.getChannel(), Integer.parseInt(splitted[1]), lines, ear)); */
            return 1;
        }
    }

    public static class SpeakMega extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim == null) {
                c.getPlayer().dropMessage(0, "The person isn't login, or doesn't exists.");
                return 0;
            }
            World.Broadcast.broadcastSmega(CWvsContext.broadcastMsg(3, victim.getClient().getChannel(), victim.getName() + " : " + StringUtil.joinStringFrom(splitted, 2), true));
            /* 
             if (splitted.length < 2) {
             c.getPlayer().dropMessage(0, "!smega <itemid> <victim> <message>");
             return 0;
             }
             final List<String> lines = new LinkedList<>();
             for (int i = 0; i < 4; i++) {
             final String text = StringUtil.joinStringFrom(splitted, 3);
             if (text.length() > 55) {
             continue;
             }
             lines.add(text);
             }
             final boolean ear = true;
             World.Broadcast.broadcastSmega(CWvsContext.getAvatarMega(victim, victim.getClient().getChannel(), Integer.parseInt(splitted[1]), lines, ear));
             */
            return 1;
        }
    }

    public static class 線上說話 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (MapleCharacter mch : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                if (mch == null) {
                    return 0;
                } else {
                    mch.getMap().broadcastMessage(CField.getChatText(mch.getId(), StringUtil.joinStringFrom(splitted, 1), mch.isGM(), 0));
                }
            }
            return 1;
        }
    }

    public static class 玩家說話 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱> <內容>");
                return 0;
            }
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim == null) {
                c.getPlayer().dropMessage(5, "找不到玩家:" + splitted[1]);
                return 0;
            } else {
                victim.getMap().broadcastMessage(CField.getChatText(victim.getId(), StringUtil.joinStringFrom(splitted, 2), victim.isGM(), 0));
            }
            return 1;
        }
    }

    public static class 全圖異常狀態 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int type = -1;
            for (MapleDisease disease : MapleDisease.values()) {
                if (splitted.length < 2) {
                    break;
                }
                if (disease.name().equalsIgnoreCase(splitted[1])) {
                    type = disease.getValue();
                    break;
                }
            }
            if (type < 0) {
                StringBuilder sb = new StringBuilder("");
                for (MapleDisease disease : MapleDisease.values()) {
                    sb.append(disease.name()).append("/");
                }
                c.getPlayer().dropMessage(6, splitted[0] + " <類型> (等級:默認1) where 類型 = " + sb.toString());
                return 0;
            }
            for (MapleCharacter mch : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                if (mch.getMapId() == c.getPlayer().getMapId()) {
                    mch.disease(type, CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1));
                }
            }
            return 1;
        }
    }

    public static class 玩家異常狀態 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int type = -1;
            for (MapleDisease disease : MapleDisease.values()) {
                if (splitted.length < 3) {
                    break;
                }
                if (disease.name().equalsIgnoreCase(splitted[1])) {
                    type = disease.getValue();
                    break;
                }
            }
            if (type < 0) {
                StringBuilder sb = new StringBuilder("");
                for (MapleDisease disease : MapleDisease.values()) {
                    sb.append(disease.name()).append("/");
                }
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱> <類型> (等級:默認1) where 類型 = " + sb.toString());
                return 0;
            }
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[2]);
            if (victim == null) {
                c.getPlayer().dropMessage(5, "未找到玩家");
                return 0;
            }
            victim.disease(type, CommandProcessorUtil.getOptionalIntArg(splitted, 3, 1));
            return 1;
        }
    }

    public static class 克隆我 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().cloneLook();
            return 1;
        }
    }

    public static class 清除克隆 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(6, c.getPlayer().getCloneSize() + " 個克隆被清除了");
            c.getPlayer().disposeClones();
            return 1;
        }
    }

    public static class 全圖玩家 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            StringBuilder builder = new StringBuilder("當前地圖的玩家: 總共").append(c.getPlayer().getMap().getCharactersThreadsafe().size()).append("個, ");
            for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
                if (builder.length() > 150) { // wild guess :o
                    builder.setLength(builder.length() - 2);
                    c.getPlayer().dropMessage(6, builder.toString());
                    builder = new StringBuilder();
                }
                builder.append(MapleCharacterUtil.makeMapleReadable(chr.getName()));
                builder.append(", ");
            }
            builder.setLength(builder.length() - 2);
            c.getPlayer().dropMessage(6, builder.toString());
            return 1;
        }
    }

    public static class 設置事件實例屬性 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            EventManager em = c.getChannelServer().getEventSM().getEventManager(splitted[1]);
            if (em == null || em.getInstances().size() <= 0) {
                c.getPlayer().dropMessage(5, "事件實例不存在");
            } else {
                em.setProperty(splitted[2], splitted[3]);
                for (EventInstanceManager eim : em.getInstances()) {
                    eim.setProperty(splitted[2], splitted[3]);
                }
            }
            return 1;
        }
    }

    public static class 事件實例屬性 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            EventManager em = c.getChannelServer().getEventSM().getEventManager(splitted[1]);
            if (em == null || em.getInstances().size() <= 0) {
                c.getPlayer().dropMessage(5, "none");
            } else {
                for (EventInstanceManager eim : em.getInstances()) {
                    c.getPlayer().dropMessage(5, "Event " + eim.getName() + ", eventManager: " + em.getName() + " iprops: " + eim.getProperty(splitted[2]) + ", eprops: " + em.getProperty(splitted[2]));
                }
            }
            return 0;
        }
    }

    public static class 離開事件實例 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().getEventInstance() == null) {
                c.getPlayer().dropMessage(5, "你沒有在事件實例中");
            } else {
                c.getPlayer().getEventInstance().unregisterPlayer(c.getPlayer());
            }
            return 1;
        }
    }

    public static class 開始事件實例 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().getEventInstance() != null) {
                c.getPlayer().dropMessage(5, "你已在一個事件實例中");
            } else if (splitted.length > 2) {
                EventManager em = c.getChannelServer().getEventSM().getEventManager(splitted[1]);
                if (em == null || em.getInstance(splitted[2]) == null) {
                    c.getPlayer().dropMessage(5, "不存在");
                } else {
                    em.getInstance(splitted[2]).registerPlayer(c.getPlayer());
                }
            } else {
                c.getPlayer().dropMessage(5, splitted[0] + " [eventmanager] [eventinstance]");
            }
            return 1;

        }
    }

    public static class 殺OID怪物 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <怪物OID>");
                return 0;
            }
            MapleMap map = c.getPlayer().getMap();
            int targetId = Integer.parseInt(splitted[1]);
            MapleMonster monster = map.getMonsterByOid(targetId);
            if (monster != null) {
                map.killMonster(monster, c.getPlayer(), false, false, (byte) 1);
            }
            return 1;
        }
    }

    public static class 重置怪物 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().killAllMonsters(false);
            return 1;
        }
    }

    public static class 重置NPC extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().resetNPCs();
            return 1;
        }
    }

    public static class 聊天公告 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (MapleCharacter all : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                all.dropMessage(-6, StringUtil.joinStringFrom(splitted, 1));
            }
            return 1;
        }
    }

    public static class 公告事項 extends CommandExecute {

        protected static int getNoticeType(String typestring) {
            switch (typestring) {
                case "1":
                    return -1;
                case "2":
                    return -2;
                case "3":
                    return -3;
                case "4":
                    return -4;
                case "5":
                    return -5;
                case "6":
                    return -6;
                case "7":
                    return -7;
                case "8":
                    return -8;
                case "n":
                    return 0;
                case "p":
                    return 1;
                case "l":
                    return 2;
                case "nv":
                    return 5;
                case "v":
                    return 5;
                case "b":
                    return 6;
            }
            return -1;
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " (對象:默認w) (類型:默認0) <公告內容>");
                c.getPlayer().dropMessage(6, splitted[0] + "對象:地圖所有人 - m/頻道所有人 - c/伺服器所有人 - w");
                c.getPlayer().dropMessage(6, splitted[0] + "類型:1/2/3/4/5/6/7/8/n/彈窗 - p/頻道廣播 - l/紅字[公告事項] - nv/紅字 - v/无[公告事項] - b");
                return 0;
            }
            int joinmod = 1;
            int range = -1;
            switch (splitted[1]) {
                case "m":
                    range = 0;
                    break;
                case "c":
                    range = 1;
                    break;
                case "w":
                    range = 2;
                    break;
            }
            int tfrom = 2;
            if (range == -1) {
                range = 2;
                tfrom = 1;
            }
            int type = getNoticeType(splitted[tfrom]);
            if (type == -1) {
                type = 0;
                joinmod = 0;
            }
            StringBuilder sb = new StringBuilder();
            if (splitted[tfrom].equals("nv")) {
                sb.append("[公告事項]");
            } else {
                sb.append("");
            }
            joinmod += tfrom;
            sb.append(StringUtil.joinStringFrom(splitted, joinmod));

            byte[] packet = CWvsContext.broadcastMsg(type, sb.toString());
            if (range == 0) {
                c.getPlayer().getMap().broadcastMessage(packet);
            } else if (range == 1) {
                ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
            } else if (range == 2) {
                World.Broadcast.broadcastMessage(packet);
            }
            return 1;
        }
    }

    public static class Yellow extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int range = -1;
            switch (splitted[1]) {
                case "m":
                    range = 0;
                    break;
                case "c":
                    range = 1;
                    break;
                case "w":
                    range = 2;
                    break;
            }
            if (range == -1) {
                range = 2;
            }
            byte[] packet = CWvsContext.yellowChat((splitted[0].equals("!y") ? ("[" + c.getPlayer().getName() + "] ") : "") + StringUtil.joinStringFrom(splitted, 2));
            if (range == 0) {
                c.getPlayer().getMap().broadcastMessage(packet);
            } else if (range == 1) {
                ChannelServer.getInstance(c.getChannel()).broadcastPacket(packet);
            } else if (range == 2) {
                World.Broadcast.broadcastMessage(packet);
            }
            return 1;
        }
    }

    public static class Y extends Yellow {
    }

    public static class 我的IP extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "IP: " + c.getSession().getRemoteAddress().toString().split(":")[0]);
            return 1;
        }
    }

    public static class 臨時封IP extends 臨時封號 {

        public 臨時封IP() {
            ipBan = true;
        }
    }

    public static class 封IP extends 封號 {

        public 封IP() {
            ipBan = true;
        }
    }

    public static class 掉寶開關 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().toggleDrops();
            return 1;
        }
    }

    public static class 監禁 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱> <時間(分鐘,0為永久)>");
                return 0;
            }
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            final int minutes = Math.max(0, Integer.parseInt(splitted[2]));
            if (victim != null && c.getPlayer().getGMLevel() >= victim.getGMLevel()) {
                MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(GameConstants.JAIL);
                victim.getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_QUEST)).setCustomData(String.valueOf(minutes * 60));
                victim.changeMap(target, target.getPortal(0));
            } else {
                c.getPlayer().dropMessage(6, "請到此玩家所在的頻道");
                return 0;
            }
            return 1;
        }
    }

    public static class 查看NPC extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (MapleMapObject reactor1l : c.getPlayer().getMap().getAllNPCsThreadsafe()) {
                MapleNPC reactor2l = (MapleNPC) reactor1l;
                c.getPlayer().dropMessage(5, "NPC: oID: " + reactor2l.getObjectId() + " npcID: " + reactor2l.getId() + " 坐標: " + reactor2l.getPosition().toString() + " 名稱: " + reactor2l.getName());
            }
            return 0;
        }
    }

    public static class 查看反應堆 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (MapleMapObject reactor1l : c.getPlayer().getMap().getAllReactorsThreadsafe()) {
                MapleReactor reactor2l = (MapleReactor) reactor1l;
                c.getPlayer().dropMessage(5, "反應堆: oID: " + reactor2l.getObjectId() + " 反應堆ID: " + reactor2l.getReactorId() + " 坐標: " + reactor2l.getPosition().toString() + " 狀態: " + reactor2l.getState() + " 名稱: " + reactor2l.getName());
            }
            return 0;
        }
    }

    public static class 查看傳送點 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (MaplePortal portal : c.getPlayer().getMap().getPortals()) {
                c.getPlayer().dropMessage(5, "傳送點: ID: " + portal.getId() + " 腳本: " + portal.getScriptName() + " 名稱: " + portal.getName() + " 坐標: " + portal.getPosition().x + "," + portal.getPosition().y + " 目標地圖: " + portal.getTargetMapId() + " / " + portal.getTarget());
            }
            return 0;
        }
    }

    public static class 我的位置 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            Point pos = c.getPlayer().getPosition();
            c.getPlayer().dropMessage(6, "X: " + pos.x + " | Y: " + pos.y + " | RX0: " + (pos.x + 50) + " | RX1: " + (pos.x - 50) + " | FH: " + c.getPlayer().getFH());
            return 1;
        }
    }

    public static class 字母 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(6, splitted[0] + " <顏色 (綠/紅)> <單詞>");
                return 0;
            }
            int start;
            int nstart;
            if (splitted[1].equalsIgnoreCase("綠")) {
                start = 3991026;
                nstart = 3990019;
            } else if (splitted[1].equalsIgnoreCase("紅")) {
                start = 3991000;
                nstart = 3990009;
            } else {
                c.getPlayer().dropMessage(6, "未知顏色");
                return 0;
            }
            String splitString = StringUtil.joinStringFrom(splitted, 2);
            List<Integer> chars = new ArrayList();
            splitString = splitString.toUpperCase();

            for (int i = 0; i < splitString.length(); ++i) {
                char chr = splitString.charAt(i);
                if (chr == ' ') {
                    chars.add(-1);
                } else if ((chr >= 'A') && (chr <= 'Z')) {
                    chars.add(Integer.valueOf(chr));
                } else if ((chr >= '0') && (chr <= '9')) {
                    chars.add(chr + 200);
                }
            }
            int w = 32;
            int dStart = c.getPlayer().getPosition().x - (splitString.length() / 2 * 32);
            for (Integer i : chars) {
                if (i == -1) {
                    dStart += 32;
                } else {
                    int val;
                    Item item;
                    if (i < 200) {
                        val = start + i - 65;
                        item = new Item(val, (byte) 0, (short) 1);
                        c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), item, new Point(dStart, c.getPlayer().getPosition().y), false, false);
                        dStart += 32;
                    } else if ((i >= 200) && (i <= 300)) {
                        val = nstart + i - 48 - 200;
                        item = new Item(val, (byte) 0, (short) 1);
                        c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), item, new Point(dStart, c.getPlayer().getPosition().y), false, false);
                        dStart += 32;
                    }
                }
            }
            return 1;
        }
    }

    public static class 召喚 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <怪物ID> (數量:默認1)");
                return 0;
            }
            final int mid = Integer.parseInt(splitted[1]);
            final int num = Math.min(CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1), 500);
            Integer level = CommandProcessorUtil.getNamedIntArg(splitted, 1, "lvl");
            Long hp = CommandProcessorUtil.getNamedLongArg(splitted, 1, "hp");
            Integer exp = CommandProcessorUtil.getNamedIntArg(splitted, 1, "exp");
            Double php = CommandProcessorUtil.getNamedDoubleArg(splitted, 1, "php");
            Double pexp = CommandProcessorUtil.getNamedDoubleArg(splitted, 1, "pexp");

            MapleMonster onemob;
            try {
                onemob = MapleLifeFactory.getMonster(mid);
            } catch (RuntimeException e) {
                c.getPlayer().dropMessage(5, "錯誤: " + e.getMessage());
                return 0;
            }
            if (onemob == null) {
                c.getPlayer().dropMessage(5, "怪物不存在");
                return 0;
            }
            int newhp;
            int newexp;
            if (hp != null) {
                newhp = hp.intValue();
            } else if (php != null) {
                newhp = (int) (onemob.getMobMaxHp() * (php / 100));
            } else {
                newhp = (int) onemob.getMobMaxHp();
            }
            if (exp != null) {
                newexp = exp;
            } else if (pexp != null) {
                newexp = (int) (onemob.getMobExp() * (pexp / 100));
            } else {
                newexp = onemob.getMobExp();
            }
            if (newhp < 1) {
                newhp = 1;
            }

            final OverrideMonsterStats overrideStats = new OverrideMonsterStats(newhp, onemob.getMobMaxMp(), newexp, false);
            for (int i = 0; i < num; i++) {
                MapleMonster mob = MapleLifeFactory.getMonster(mid);
                mob.setHp(newhp);
                if (level != null) {
                    mob.changeLevel(level, false);
                } else {
                    mob.setOverrideStats(overrideStats);
                }
                c.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, c.getPlayer().getPosition());
            }
            return 1;
        }
    }

    public static class 召喚怪物 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " (數量:默認1) <怪物名稱>");
                return 0;
            }
            final String mname;
            final int num;
            if (splitted.length == 2) {
                mname = StringUtil.joinStringFrom(splitted, 1);
                num = 1;
            } else {
                mname = StringUtil.joinStringFrom(splitted, 2);
                num = Integer.parseInt(splitted[1]);
            }
            int mid = 0;
            for (Entry<Integer, String> mob : MapleMonsterInformationProvider.getInstance().getAllMonsters().entrySet()) {
                if (mob.getValue().toLowerCase().equals(mname.toLowerCase())) {
                    mid = mob.getKey();
                    break;
                }
            }

            MapleMonster onemob;
            try {
                onemob = MapleLifeFactory.getMonster(mid);
            } catch (RuntimeException e) {
                c.getPlayer().dropMessage(5, "錯誤: " + e.getMessage());
                return 0;
            }
            if (onemob == null) {
                c.getPlayer().dropMessage(5, "怪物不存在");
                return 0;
            }
            for (int i = 0; i < num; i++) {
                MapleMonster mob = MapleLifeFactory.getMonster(mid);
                c.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, c.getPlayer().getPosition());
            }
            return 1;
        }
    }

    public static class 禁言 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱>");
                return 0;
            }
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            victim.canTalk(false);
            return 1;
        }
    }

    public static class 取消禁言 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱>");
                return 0;
            }
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            victim.canTalk(true);
            return 1;
        }
    }

    public static class 全圖禁言 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
                chr.canTalk(false);
            }
            return 1;
        }
    }

    public static class 取消全圖禁言 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String splitted[]) {
            for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
                chr.canTalk(true);
            }
            return 1;
        }
    }

    public static class 無敵 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (player.isInvincible()) {
                player.setInvincible(false);
                player.dropMessage(6, "無敵模式關閉");
            } else {
                player.setInvincible(true);
                player.dropMessage(6, "無敵模式開啟");
            }
            return 1;
        }
    }

    public static class 線上玩家 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            int total = 0;
            c.getPlayer().dropMessage(6, "---------------------------------------------------------------------------------------");
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                int curConnected = cserv.getConnectedClients();
                c.getPlayer().dropMessage(6, new StringBuilder().append("頻道: ").append(cserv.getChannel()).append(" 線上人數: ").append(curConnected).toString());
                total += curConnected;
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr != null && c.getPlayer().getGMLevel() >= chr.getGMLevel()) {
                        StringBuilder ret = new StringBuilder();
                        ret.append(StringUtil.getRightPaddedStr(chr.getName(), ' ', 13));
                        ret.append(" ID: ");
                        ret.append(chr.getId());
                        ret.append(" 等級: ");
                        ret.append(StringUtil.getRightPaddedStr(String.valueOf(chr.getLevel()), ' ', 3));
                        if (chr.getMap() != null) {
                            ret.append(" 地圖: ");
                            ret.append(chr.getMap().toString());
                        }
                        c.getPlayer().dropMessage(6, ret.toString());
                    }
                }
            }
            c.getPlayer().dropMessage(6, new StringBuilder().append("當前伺服器總計線上人數: ").append(total).toString());
            c.getPlayer().dropMessage(6, "---------------------------------------------------------------------------------------");
            return 1;
        }
    }

    public static class 搜外掛 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <搜尋訊息：進程名/MD5>");
                return 0;
            }
            int i = 0;
            String name;
            String[] names;
            for (ChannelServer ch : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters()) {
                    for (MapleProcess mp : chr.getProcess()) {
                        if (mp.getMD5().equalsIgnoreCase(splitted[1]) || mp.getName().toLowerCase().contains(splitted[1].toLowerCase())) {
                            c.getPlayer().dropMessage(5, chr.getName() + " 存在該進程【\"" + mp.getPath() + "\" MD5值：\"" + mp.getMD5() + "\"】");
                            i++;
                        }
                    }
                }
            }
            c.getPlayer().dropMessage(5, "搜尋完畢，本次共搜尋到" + i + "個。");
            return 1;
        }
    }

    public static class 獲得楓點 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <點數>");
                return 0;
            }
            c.getPlayer().modifyCSPoints(2, Integer.parseInt(splitted[1]), true);
            return 1;
        }
    }

    public static class 楓幣 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <金額>");
                return 0;
            }
            c.getPlayer().gainMeso(Long.parseLong(splitted[1]), true);
            return 1;
        }
    }

    public static class 高級檢索 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.removeClickedNPC();
            NPCScriptManager.getInstance().start(c, 0, "AdvancedSearch");
            return 1;
        }
    }
}
