package server.commands;

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import client.MapleDisease;
import client.MapleStat;
import client.SkillFactory;
import client.anticheat.ReportType;
import client.inventory.Item;
import client.inventory.ItemFlag;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import constants.GameConstants;
import constants.ItemConstants;
import tools.SearchGenerator;
import handling.channel.ChannelServer;
import handling.world.CheaterData;
import handling.world.World;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import scripting.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.MapleSquad.MapleSquadType;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterInformationProvider;
import server.life.MobSkillFactory;
import server.life.MonsterGlobalDropEntry;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.quest.MapleQuest;
import server.shops.MapleShopFactory;
import tools.FileoutputUtil;
import tools.Pair;
import tools.StringUtil;
import tools.packet.CField;
import tools.packet.CField.NPCPacket;
import tools.packet.CWvsContext;

/**
 *
 * @author Emilyx3
 */
public class InternCommand {

    public static PlayerGMRank getPlayerLevelRequired() {
        return PlayerGMRank.INTERN;
    }

    public static class 隱藏 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().isHidden()) {
                c.getPlayer().dispelBuff(9001004);
                c.getPlayer().dropMessage(-5, "隱藏已關閉。");
                MapleItemInformationProvider.getInstance().getItemEffect(2100069).applyTo(c.getPlayer());
                c.getSession().write(CWvsContext.InfoPacket.getStatusMsg(2100069));
            } else {
                SkillFactory.getSkill(9001004).getEffect(1).applyTo(c.getPlayer());
                c.getPlayer().dropMessage(-5, "隱藏已開啟。");
            }
            return 0;
        }
    }

    public static class 隱藏聊天可見 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getPlayer().isHiddenChatCanSee()) {
                c.getPlayer().setHiddenChatCanSee(false);
                c.getPlayer().dropMessage(6, "當前隱藏狀態時聊天訊息玩家可見性：不可見");
            } else {
                c.getPlayer().setHiddenChatCanSee(true);
                c.getPlayer().dropMessage(6, "當前隱藏狀態時聊天訊息玩家可見性：可見");
            }
            return 0;
        }
    }

    public static class 治愈 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getStat().heal(c.getPlayer());
            c.getPlayer().dispelDebuffs();
            return 0;
        }
    }

    public static class 治愈全圖 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            for (MapleCharacter mch : player.getMap().getCharacters()) {
                if (mch != null) {
                    mch.getStat().heal(mch);
                    mch.dispelDebuffs();
                }
            }
            return 1;
        }
    }

    public static class 臨時封號 extends CommandExecute {

        protected boolean ipBan = false;
        private final String[] types = {"外掛", "BOT", "AD", "HARASS", "CURSE", "SCAM", "MISCONDUCT", "SELL", "ICASH", "TEMP", "GM", "IPROGRAM", "MEGAPHONE"};

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 4) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱> <理由> <時間(小時)>");
                StringBuilder s = new StringBuilder("臨時封號理由: ");
                for (int i = 0; i < types.length; i++) {
                    s.append(i + 1).append(" - ").append(types[i]).append(", ");
                }
                c.getPlayer().dropMessage(6, s.toString());
                return 0;
            }
            final MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            final int reason = Integer.parseInt(splitted[2]);
            final int numHour = Integer.parseInt(splitted[3]);

            final Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR, numHour);
            final DateFormat df = DateFormat.getInstance();

            if (victim == null || reason < 0 || reason >= types.length) {
                c.getPlayer().dropMessage(6, "無法找到玩家或者理由是無效的, 輸入" + splitted[0] + "查看可用理由");
                return 0;
            }
            victim.tempban("因 " + types[reason] + " 被 " + c.getPlayer().getName() + " 臨時封號", cal, reason, ipBan);
            c.getPlayer().dropMessage(6, "玩家 " + splitted[1] + " 被臨時封號到 " + df.format(cal.getTime()));
            return 1;
        }
    }

    public static class 封號 extends CommandExecute {

        protected boolean hellban = false, ipBan = false;

        private String getCommand() {
            if (hellban) {
                return "匿名封號";
            } else if (ipBan) {
                return "封IP";
            } else {
                return "封號";
            }
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                c.getPlayer().dropMessage(5, splitted[0] + " <玩家名稱> <理由>");
//                c.getPlayer().dropMessage(5, "If you want to consider this ban as an autoban, set the reason \"AutoBan\"");
                return 0;
            }
            StringBuilder sb = new StringBuilder();
            if (hellban) {
                sb.append(splitted[1]).append("被封號: ").append(StringUtil.joinStringFrom(splitted, 2));
            } else {
                sb.append(c.getPlayer().getName()).append(" 對 ").append(splitted[1]).append(" 進行封號處理: ").append(StringUtil.joinStringFrom(splitted, 2));
            }
            MapleCharacter target = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (target != null) {
                if ((c.getPlayer().getGMLevel() > target.getGMLevel() || c.getPlayer().isAdmin()) && !target.getClient().isGM() && !target.isAdmin()) {
                    //sb.append(" (IP: ").append(target.getClient().getSessionIPAddress()).append(")");
                    if (target.ban(sb.toString(), hellban || ipBan, false, hellban)) {
                        c.getPlayer().dropMessage(6, "[" + getCommand() + "] " + splitted[1] + " 已經被封號");
                        return 1;
                    } else {
                        c.getPlayer().dropMessage(6, "[" + getCommand() + "] 封號失敗");
                        return 0;
                    }
                } else {
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] 無法封GM...");
                    return 1;
                }
            } else {
                if (MapleCharacter.ban(splitted[1], sb.toString(), false, c.getPlayer().isAdmin() ? 250 : c.getPlayer().getGMLevel(), hellban)) {
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] " + splitted[1] + " 已經被離線封號");
                    return 1;
                } else {
                    c.getPlayer().dropMessage(6, "[" + getCommand() + "] " + splitted[1] + "封號失敗");
                    return 0;
                }
            }
        }
    }

    public static class 下線 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱> ([玩家名稱] [玩家名稱]...)");
                return 0;
            }
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[splitted.length - 1]);
            if (victim != null && c.getPlayer().getGMLevel() >= victim.getGMLevel()) {
                victim.getClient().getSession().close(true);
                victim.getClient().disconnect(true, false);
                return 1;
            } else {
                c.getPlayer().dropMessage(6, "受害者不存在或不在線上。");
                return 0;
            }
        }
    }

    public static class 殺 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱> ([玩家名稱] [玩家名稱]...)");
                return 0;
            }
            MapleCharacter victim = null;
            for (int i = 1; i < splitted.length; i++) {
                try {
                    victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[i]);
                } catch (Exception e) {
                    c.getPlayer().dropMessage(6, "沒找到受害者 " + splitted[i]);
                }
                if (player.allowedToTarget(victim) && player.getGMLevel() >= victim.getGMLevel()) {
                    victim.getStat().setHp((short) 0, victim);
                    victim.getStat().setMp((short) 0, victim);
                    victim.updateSingleStat(MapleStat.HP, victim.getStat().getHp());
                    victim.updateSingleStat(MapleStat.MP, victim.getStat().getMp());
                }
            }
            return 1;
        }
    }

    public static class 我在哪裡 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().dropMessage(5, "你所在的地圖為 " + c.getPlayer().getMap().toString());
            return 1;
        }
    }

    public static class 線上 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            String online = "";
            for (ChannelServer cs : ChannelServer.getAllInstances()) {
                online += cs.getPlayerStorage().getOnlinePlayers(true);
            }
            c.getPlayer().dropMessage(6, online);
            return 1;
        }
    }

    public static class 角色訊息 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱>");
                return 0;
            }
            final StringBuilder builder = new StringBuilder();
            final MapleCharacter other = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (other == null) {
                builder.append("輸入的角色不存在...");
                c.getPlayer().dropMessage(6, builder.toString());
                return 0;
            }
            if (other.getClient().getLastPing() <= 0) {
                other.getClient().sendPing();
            }
            builder.append(MapleClient.getLogMessage(other, ""));
            builder.append(" 坐標 ").append(other.getPosition().x);
            builder.append(" /").append(other.getPosition().y);

            builder.append(" || HP : ");
            builder.append(other.getStat().getHp());
            builder.append(" /");
            builder.append(other.getStat().getCurrentMaxHp());

            builder.append(" || MP : ");
            builder.append(other.getStat().getMp());
            builder.append(" /");
            builder.append(other.getStat().getCurrentMaxMp(other.getJob()));

            builder.append(" || BattleshipHP : ");
            builder.append(other.currentBattleshipHP());

            builder.append(" || 物理攻擊力 : ");
            builder.append(other.getStat().getTotalWatk());
            builder.append(" || 魔法攻擊力 : ");
            builder.append(other.getStat().getTotalMagic());
            builder.append(" || 最大傷害 : ");
            builder.append(other.getStat().getCurrentMaxBaseDamage());
            builder.append(" || 傷害% : ");
            builder.append(other.getStat().dam_r);
            builder.append(" || BOSS攻擊力% : ");
            builder.append(other.getStat().bossdam_r);
            builder.append(" || 暴擊幾率 : ");
            builder.append(other.getStat().passive_sharpeye_rate());
            builder.append(" || 暴擊傷害 : ");
            builder.append(other.getStat().passive_sharpeye_percent());

            builder.append(" || STR : ");
            builder.append(other.getStat().getStr()).append(" + (").append(other.getStat().getTotalStr() - other.getStat().getStr()).append(")");
            builder.append(" || DEX : ");
            builder.append(other.getStat().getDex()).append(" + (").append(other.getStat().getTotalDex() - other.getStat().getDex()).append(")");
            builder.append(" || INT : ");
            builder.append(other.getStat().getInt()).append(" + (").append(other.getStat().getTotalInt() - other.getStat().getInt()).append(")");
            builder.append(" || LUK : ");
            builder.append(other.getStat().getLuk()).append(" + (").append(other.getStat().getTotalLuk() - other.getStat().getLuk()).append(")");

            builder.append(" || 經驗 : ");
            builder.append(other.getExp());
            builder.append(" || 楓幣 : ");
            builder.append(other.getMeso());

            builder.append(" || Vote Points : ");
            builder.append(other.getVPoints());
            builder.append(" || Event Points : ");
            builder.append(other.getPoints());
            builder.append(" || NX Prepaid : ");
            builder.append(other.getCSPoints(1));

            builder.append(" || 是否組隊 : ");
            builder.append(other.getParty() == null ? -1 : other.getParty().getId());

            builder.append(" || 是否交易: ");
            builder.append(other.getTrade() != null);
            builder.append(" || Latency: ");
            builder.append(other.getClient().getLatency());
            builder.append(" || PING: ");
            builder.append(other.getClient().getLastPing());
            builder.append(" || PONG: ");
            builder.append(other.getClient().getLastPong());
            builder.append(" || 連接地地址: ");
            other.getClient().DebugMessage(builder);

            c.getPlayer().dropMessage(6, builder.toString());
            return 1;
        }
    }

    public static class Cheaters extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            List<CheaterData> cheaters = World.getCheaters();
            for (int x = cheaters.size() - 1; x >= 0; x--) {
                CheaterData cheater = cheaters.get(x);
                c.getPlayer().dropMessage(6, cheater.getInfo());
            }
            return 1;
        }
    }

    public static class 去往 extends CommandExecute {

        private static final HashMap<String, Integer> gotomaps = new HashMap<>();

        static {
            gotomaps.put("專業技術村", 910001000);
            gotomaps.put("納希沙漠", 260000100);
            gotomaps.put("楓之島", 1010000);
            gotomaps.put("結婚村莊", 680000000);
            gotomaps.put("另一個水世界", 860000000);
            gotomaps.put("水之都", 230000000);
            gotomaps.put("駁船碼頭城", 541000000);
//            gotomaps.put("cwk", 610030000);
            gotomaps.put("埃德爾斯坦", 310000000);
            gotomaps.put("艾靈森林", 300000000);
            gotomaps.put("魔法森林", 101000000);
            gotomaps.put("愛里涅湖水", 101071300);
            gotomaps.put("精靈之林", 101050000);
            gotomaps.put("冰原雪域", 211000000);
            gotomaps.put("耶雷弗", 130000000);
//            gotomaps.put("florina", 120000300);
            gotomaps.put("自由市場", 910000000);
            gotomaps.put("未來之門", 271000000);
            gotomaps.put("工作場所", 180000000);
            gotomaps.put("幸福村", 209000000);
            gotomaps.put("維多利亞港", 104000000);
            gotomaps.put("弓箭手村", 100000000);
            gotomaps.put("藥靈幻境", 251000000);
            gotomaps.put("鄉村鎮", 551000000);
            gotomaps.put("墮落城市", 103000000);
//            gotomaps.put("korean", 222000000);
            gotomaps.put("神木村", 240000000);
            gotomaps.put("玩具城", 220000000);
            gotomaps.put("馬來西亞", 550000000);
            gotomaps.put("桃花仙境", 250000000);
            gotomaps.put("鯨魚號", 120000000);
            gotomaps.put("新葉城", 600000000);
//            gotomaps.put("omega", 221000000);
            gotomaps.put("天空之城", 200000000);
            gotomaps.put("萬神殿", 400000000);
            gotomaps.put("皮卡啾", 270050100);
//            gotomaps.put("phantom", 610010000);
            gotomaps.put("勇士之村", 102000000);
            gotomaps.put("瑞恩村", 140000000);
            gotomaps.put("昭和村", 801000000);
            gotomaps.put("新加坡", 540000000);
            gotomaps.put("六條岔道", 104020000);
            gotomaps.put("奇幻村", 105000000);
            gotomaps.put("楓之港", 2000000);
            gotomaps.put("綠樹村", 866000000);
            gotomaps.put("三扇門", 270000000);
            gotomaps.put("黃昏的勇士之村", 273000000);
            gotomaps.put("克林森烏德城", 301000000);
            gotomaps.put("海怒斯", 230040420);
            gotomaps.put("闇黑龍王", 240060200);
            gotomaps.put("混沌闇黑龍王", 240060201);
            gotomaps.put("格瑞芬多", 240020101);
            gotomaps.put("噴火龍", 240020401);
            gotomaps.put("殘暴炎魔", 280030100);
            gotomaps.put("混沌殘暴炎魔", 280030000);
            gotomaps.put("拉圖斯", 220080001);
            gotomaps.put("選邊站", 109020001);
            gotomaps.put("向上攀升", 109030101);
            gotomaps.put("障礙競走", 109040000);
            gotomaps.put("滾雪球", 109060000);
            gotomaps.put("江戶村", 800000000);
        }

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <地圖名>");
            } else {
                if (gotomaps.containsKey(splitted[1])) {
                    MapleMap target = c.getChannelServer().getMapFactory().getMap(gotomaps.get(splitted[1]));
                    if (target == null) {
                        c.getPlayer().dropMessage(6, "地圖不存在");
                        return 0;
                    }
                    MaplePortal targetPortal = target.getPortal(0);
                    c.getPlayer().changeMap(target, targetPortal);
                } else {
                    if (splitted[1].equals("列表")) {
                        c.getPlayer().dropMessage(6, "地圖列表: ");
                        StringBuilder sb = new StringBuilder();
                        for (String s : gotomaps.keySet()) {
                            sb.append(s).append(", ");
                        }
                        c.getPlayer().dropMessage(6, sb.substring(0, sb.length() - 2));
                    } else {
                        c.getPlayer().dropMessage(6, "指令錯誤, 使用方法: " + splitted[0] + " <地圖名>(你可以使用 " + splitted[0] + " 列表 來獲取可用地圖列表)");
                    }
                }
            }
            return 1;
        }
    }

    public static class 時鐘 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " (時間:默認60秒)");
            }
            c.getPlayer().getMap().broadcastMessage(CField.getClock(CommandProcessorUtil.getOptionalIntArg(splitted, 1, 60)));
            return 1;
        }
    }

    public static class 來這裡 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <玩家名稱>");
                return 0;
            }
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null) {
                if (c.getPlayer().inPVP() || (!c.getPlayer().isGM() && (victim.isInBlockedMap() || victim.isGM()))) {
                    c.getPlayer().dropMessage(5, "請稍後再試");
                    return 0;
                }
                victim.changeMap(c.getPlayer().getMap(), c.getPlayer().getMap().findClosestPortal(c.getPlayer().getTruePosition()));
            } else {
                int ch = World.Find.findChannel(splitted[1]);
                if (ch < 0) {
                    c.getPlayer().dropMessage(5, "未找到");
                    return 0;
                }
                victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
                if (victim == null || victim.inPVP() || (!c.getPlayer().isGM() && (victim.isInBlockedMap() || victim.isGM()))) {
                    c.getPlayer().dropMessage(5, "請稍後再試");
                    return 0;
                }
                c.getPlayer().dropMessage(5, "受害者正在更變頻道");
                victim.dropMessage(5, "正在更變頻道");
                if (victim.getMapId() != c.getPlayer().getMapId()) {
                    final MapleMap mapp = victim.getClient().getChannelServer().getMapFactory().getMap(c.getPlayer().getMapId());
                    victim.changeMap(mapp, mapp.findClosestPortal(c.getPlayer().getTruePosition()));
                }
                victim.changeChannel(c.getChannel());
            }
            return 1;
        }
    }

    public static class 傳送 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0]);
                c.getPlayer().dropMessage(6, "用法一:(要傳送的玩家名稱) <地圖ID> (傳送點ID:默認無)");
                c.getPlayer().dropMessage(6, "用法二:<要傳送到的玩家名稱>");
                return 0;
            }
            MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim != null && c.getPlayer().getGMLevel() >= victim.getGMLevel() && !victim.inPVP() && !c.getPlayer().inPVP()) {
                if (splitted.length == 2) {
                    c.getPlayer().changeMap(victim.getMap(), victim.getMap().findClosestSpawnpoint(victim.getTruePosition()));
                } else {
                    MapleMap target = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(Integer.parseInt(splitted[2]));
                    if (target == null) {
                        c.getPlayer().dropMessage(6, "地圖不存在");
                        return 0;
                    }
                    MaplePortal targetPortal = null;
                    if (splitted.length > 3) {
                        try {
                            targetPortal = target.getPortal(Integer.parseInt(splitted[3]));
                        } catch (IndexOutOfBoundsException e) {
                            // noop, assume the gm didn't know how many portals there are
                            c.getPlayer().dropMessage(5, "傳送點的選擇無效");
                        } catch (NumberFormatException a) {
                            // noop, assume that the gm is drunk
                        }
                    }
                    if (targetPortal == null) {
                        targetPortal = target.getPortal(0);
                    }
                    victim.changeMap(target, targetPortal);
                }
            } else {
                try {
                    int ch = World.Find.findChannel(splitted[1]);
                    if (ch < 0) {
                        MapleMap target = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[1]));
                        if (target == null) {
                            c.getPlayer().dropMessage(6, "地圖不存在");
                            return 0;
                        }
                        MaplePortal targetPortal = null;
                        if (splitted.length > 2) {
                            try {
                                targetPortal = target.getPortal(Integer.parseInt(splitted[2]));
                            } catch (IndexOutOfBoundsException e) {
                                // noop, assume the gm didn't know how many portals there are
                                c.getPlayer().dropMessage(5, "傳送點的選擇無效");
                            } catch (NumberFormatException a) {
                                // noop, assume that the gm is drunk
                            }
                        }
                        if (targetPortal == null) {
                            targetPortal = target.getPortal(0);
                        }
                        c.getPlayer().changeMap(target, targetPortal);
                    } else {
                        victim = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(splitted[1]);
                        c.getPlayer().dropMessage(6, "正在更變頻道, 請稍候");
                        if (victim.getMapId() != c.getPlayer().getMapId()) {
                            final MapleMap mapp = c.getChannelServer().getMapFactory().getMap(victim.getMapId());
                            c.getPlayer().changeMap(mapp, mapp.findClosestPortal(victim.getTruePosition()));
                        }
                        c.getPlayer().changeChannel(ch);
                    }
                } catch (NumberFormatException e) {
                    c.getPlayer().dropMessage(6, "出現錯誤: " + e.getMessage());
                    return 0;
                }
            }
            return 1;
        }
    }

    public static class 說 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length > 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                if (!c.getPlayer().isGM()) {
                    sb.append("實習 ");
                }
                sb.append(c.getPlayer().getName());
                sb.append("] ");
                sb.append(StringUtil.joinStringFrom(splitted, 1));
                World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(c.getPlayer().isGM() ? 6 : 5, sb.toString()));
            } else {
                c.getPlayer().dropMessage(6, splitted[0] + " <內容>");
                return 0;
            }
            return 1;
        }
    }

    public static class 搜尋 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length == 2) {
                c.getPlayer().dropMessage(6, "請提供搜尋訊息");
            } else {
                boolean err = false;
                if (splitted.length == 1) {
                    err = true;
                } else {
                    String typeName = splitted[1];
                    String search = StringUtil.joinStringFrom(splitted, 2);
                    SearchGenerator.SearchType type = SearchGenerator.SearchType.valueOf(typeName);
                    if (type != SearchGenerator.SearchType.未知) {
                        if (!SearchGenerator.foundData(type.getValue(), search)) {
                            c.getPlayer().dropMessage(6, "搜尋不到此" + type.name());
                            return 0;
                        }
                        switch (type) {
                            case 髮型:
                            case 臉型:
                                Set<Integer> keySet = SearchGenerator.getSearchData(type, search).keySet();
                                int[] styles = new int[keySet.size()];
                                int i = 0;
                                for (int key : keySet) {
                                    styles[i] = key;
                                    i++;
                                }
                                c.getSession().write(NPCPacket.getNPCTalkStyle(9010000, "", styles, false));
                                break;
                            default:
                                String str = SearchGenerator.searchData(type, search);
                                c.getSession().write(NPCPacket.getNPCTalk(9010000, (byte) 5, str, "", (byte) 0));
                                break;
                        }
                        return 1;
                    }
                    err = true;
                }
                if (err) {
                    StringBuilder sb = new StringBuilder("");
                    for (SearchGenerator.SearchType searchType : SearchGenerator.SearchType.values()) {
                        if (searchType != SearchGenerator.SearchType.未知) {
                            sb.append(searchType.name()).append("/");
                        }
                    }
                    c.getPlayer().dropMessage(6, splitted[0] + ": <類型> <搜尋訊息>");
                    c.getPlayer().dropMessage(6, "類型:" + sb.toString());
                }
            }
            return 0;
        }
    }

    public static class WhosFirst extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            //probably bad way to do it
            final long currentTime = System.currentTimeMillis();
            List<Pair<String, Long>> players = new ArrayList<>();
            for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
                if (!chr.isIntern()) {
                    players.add(new Pair<>(MapleCharacterUtil.makeMapleReadable(chr.getName()) + (currentTime - chr.getCheatTracker().getLastAttack() > 600000 ? " (AFK)" : ""), chr.getChangeTime()));
                }
            }
            Collections.sort(players, new WhoComparator());
            StringBuilder sb = new StringBuilder("List of people in this map in order, counting AFK (10 minutes):  ");
            for (Pair<String, Long> z : players) {
                sb.append(z.left).append(", ");
            }
            c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
            return 0;
        }

        public static class WhoComparator implements Comparator<Pair<String, Long>>, Serializable {

            @Override
            public int compare(Pair<String, Long> o1, Pair<String, Long> o2) {
                if (o1.right > o2.right) {
                    return 1;
                } else if (Objects.equals(o1.right, o2.right)) {
                    return 0;
                } else {
                    return -1;
                }
            }
        }
    }

    public static class WhosLast extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                StringBuilder sb = new StringBuilder("whoslast [type] where type can be:  ");
                for (MapleSquadType t : MapleSquadType.values()) {
                    sb.append(t.name()).append(", ");
                }
                c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
                return 0;
            }
            final MapleSquadType t = MapleSquadType.valueOf(splitted[1].toLowerCase());
            if (t == null) {
                StringBuilder sb = new StringBuilder("whoslast [type] where type can be:  ");
                for (MapleSquadType z : MapleSquadType.values()) {
                    sb.append(z.name()).append(", ");
                }
                c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
                return 0;
            }
            if (t.queuedPlayers.get(c.getChannel()) == null) {
                c.getPlayer().dropMessage(6, "The queue has not been initialized in this channel yet.");
                return 0;
            }
            c.getPlayer().dropMessage(6, "Queued players: " + t.queuedPlayers.get(c.getChannel()).size());
            StringBuilder sb = new StringBuilder("List of participants:  ");
            for (Pair<String, String> z : t.queuedPlayers.get(c.getChannel())) {
                sb.append(z.left).append('(').append(z.right).append(')').append(", ");
            }
            c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
            return 0;
        }
    }

    public static class WhosNext extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                StringBuilder sb = new StringBuilder("whosnext [type] where type can be:  ");
                for (MapleSquadType t : MapleSquadType.values()) {
                    sb.append(t.name()).append(", ");
                }
                c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
                return 0;
            }
            final MapleSquadType t = MapleSquadType.valueOf(splitted[1].toLowerCase());
            if (t == null) {
                StringBuilder sb = new StringBuilder("whosnext [type] where type can be:  ");
                for (MapleSquadType z : MapleSquadType.values()) {
                    sb.append(z.name()).append(", ");
                }
                c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
                return 0;
            }
            if (t.queue.get(c.getChannel()) == null) {
                c.getPlayer().dropMessage(6, "The queue has not been initialized in this channel yet.");
                return 0;
            }
            c.getPlayer().dropMessage(6, "Queued players: " + t.queue.get(c.getChannel()).size());
            StringBuilder sb = new StringBuilder("List of participants:  ");
            final long now = System.currentTimeMillis();
            for (Pair<String, Long> z : t.queue.get(c.getChannel())) {
                sb.append(z.left).append('(').append(StringUtil.getReadableMillis(z.right, now)).append(" ago),");
            }
            c.getPlayer().dropMessage(6, sb.toString().substring(0, sb.length() - 2));
            return 0;
        }
    }

    public static class 清怪 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " (範圍:默認全圖) (地圖D:默認當前地圖)");
            }
            MapleMap map = c.getPlayer().getMap();
            double range = Double.POSITIVE_INFINITY;

            if (splitted.length > 1) {
                int irange = Integer.parseInt(splitted[1]);
                if (splitted.length <= 2) {
                    range = irange * irange;
                } else {
                    map = c.getChannelServer().getMapFactory().getMap(Integer.parseInt(splitted[2]));
                }
            }
            if (map == null) {
                c.getPlayer().dropMessage(6, "地圖不存在");
                return 0;
            }
            MapleMonster mob;
            for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
                mob = (MapleMonster) monstermo;
                if (!mob.getStats().isBoss() || mob.getStats().isPartyBonus() || c.getPlayer().isGM()) {
                    map.killMonster(mob, c.getPlayer(), false, false, (byte) 1);
                }
            }
            return 1;
        }
    }

    public static class 全屏撿物 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            final List<MapleMapObject> items = c.getPlayer().getMap().getMapObjectsInRange(c.getPlayer().getPosition(), GameConstants.maxViewRangeSq(), Arrays.asList(MapleMapObjectType.ITEM));
            MapleMapItem mapitem;
            for (MapleMapObject item : items) {
                mapitem = (MapleMapItem) item;
                if (mapitem.getMeso() > 0) {
                    c.getPlayer().gainMeso(mapitem.getMeso(), true);
                } else if (mapitem.getItem() == null || !MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), true)) {
                    continue;
                }
                mapitem.setPickedUp(true);
                c.getPlayer().getMap().broadcastMessage(CField.removeItemFromMap(mapitem.getObjectId(), 2, c.getPlayer().getId()), mapitem.getPosition());
                c.getPlayer().getMap().removeMapObject(item);

            }
            return 1;
        }
    }

    public static class 清除BUFF extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().cancelAllBuffs();
            return 1;
        }
    }

    public static class 換頻 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().changeChannel(Integer.parseInt(splitted[1]));
            return 1;
        }
    }

    public static class Reports extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            List<CheaterData> cheaters = World.getReports();
            for (int x = cheaters.size() - 1; x >= 0; x--) {
                CheaterData cheater = cheaters.get(x);
                c.getPlayer().dropMessage(6, cheater.getInfo());
            }
            return 1;
        }
    }

    public static class ClearReport extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 3) {
                StringBuilder ret = new StringBuilder("report [ign] [all/");
                for (ReportType type : ReportType.values()) {
                    ret.append(type.theId).append('/');
                }
                ret.setLength(ret.length() - 1);
                c.getPlayer().dropMessage(6, ret.append(']').toString());
                return 0;
            }
            final MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(splitted[1]);
            if (victim == null) {
                c.getPlayer().dropMessage(5, "Does not exist");
                return 0;
            }
            final ReportType type = ReportType.getByString(splitted[2]);
            if (type != null) {
                victim.clearReports(type);
            } else {
                victim.clearReports();
            }
            c.getPlayer().dropMessage(5, "Done.");
            return 1;
        }
    }

    public static class 假重載 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().fakeRelog();
            return 1;
        }
    }

    public static class 飛 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            SkillFactory.getSkill(1146).getEffect(1).applyTo(c.getPlayer());
            SkillFactory.getSkill(1142).getEffect(1).applyTo(c.getPlayer());
            c.getPlayer().dispelBuff(1146);
            return 1;
        }
    }

    public static class 打開NPC extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <NPCID> (特殊:默認空)");
                return 0;
            }
            NPCScriptManager.getInstance().start(c, Integer.parseInt(splitted[1]), splitted.length > 2 ? StringUtil.joinStringFrom(splitted, 2) : splitted[1]);
            return 1;
        }
    }

    public static class 打開商店 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <商店ID>");
                return 0;
            }
            MapleShopFactory.getInstance().getShop(Integer.parseInt(splitted[1])).sendShop(c);
            return 1;
        }
    }

    public static class 清理掉寶 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.getPlayer().getMap().removeDrops();
            return 1;
        }
    }

    public static class 商店 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleShopFactory shop = MapleShopFactory.getInstance();
            int shopId = Integer.parseInt(splitted[1]);
            if (shop.getShop(shopId) != null) {
                shop.getShop(shopId).sendShop(c);
            }
            return 1;
        }
    }

    public static class 殺附近 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleMap map = c.getPlayer().getMap();
            List<MapleMapObject> players = map.getMapObjectsInRange(c.getPlayer().getPosition(), 25000, Arrays.asList(MapleMapObjectType.PLAYER));
            for (MapleMapObject closeplayers : players) {
                MapleCharacter playernear = (MapleCharacter) closeplayers;
                if (playernear.isAlive() && playernear != c.getPlayer() && playernear.getGMLevel() < c.getPlayer().getGMLevel()) {
                    playernear.getStat().setHp((short) 0, playernear);
                    playernear.getStat().setMp((short) 0, playernear);
                    playernear.updateSingleStat(MapleStat.HP, playernear.getStat().getHp());
                    playernear.updateSingleStat(MapleStat.MP, playernear.getStat().getMp());
                    playernear.dropMessage(5, "你太靠近管理員了");
                }
            }
            return 1;
        }
    }

    public static class ManualEvent extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (c.getChannelServer().manualEvent(c.getPlayer())) {
                for (MapleCharacter chrs : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                    //chrs.dropMessage(0, "MapleGM is hosting an event! Use the @joinevent command to join the event!");
                    //chrs.dropMessage(0, "Event Map: " + c.getPlayer().getMap().getMapName());
                    //World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(25, 0, "MapleGM is hosting an event! Use the @joinevent command to join the event!"));
                    //World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(26, 0, "Event Map: " + c.getPlayer().getMap().getMapName()));
                    chrs.getClient().getSession().write(CWvsContext.broadcastMsg(GameConstants.isEventMap(chrs.getMapId()) ? 0 : 25, c.getChannel(), "Event : MapleGM is hosting an event! Use the @joinevent command to join the event!"));
                    chrs.getClient().getSession().write(CWvsContext.broadcastMsg(GameConstants.isEventMap(chrs.getMapId()) ? 0 : 26, c.getChannel(), "Event : Event Channel: " + c.getChannel() + " Event Map: " + c.getPlayer().getMap().getMapName()));
                }
            } else {
                for (MapleCharacter chrs : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                    //World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(22, 0, "Enteries to the GM event are closed. The event has began!"));
                    chrs.getClient().getSession().write(CWvsContext.broadcastMsg(GameConstants.isEventMap(chrs.getMapId()) ? 0 : 22, c.getChannel(), "Event : Enteries to the GM event are closed. The event has began!"));
                }
            }
            return 1;
        }
    }

    public static class ActiveBomberman extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (player.getMapId() != 109010100) {
                player.dropMessage(5, "This command is only usable in map 109010100.");
            } else {
                c.getChannelServer().toggleBomberman(c.getPlayer());
                for (MapleCharacter chr : player.getMap().getCharacters()) {
                    if (!chr.isIntern()) {
                        chr.cancelAllBuffs();
                        chr.giveDebuff(MapleDisease.SEAL, MobSkillFactory.getMobSkill(120, 1));
                        //MapleInventoryManipulator.removeById(chr.getClient(), MapleInventoryType.USE, 2100067, chr.getItemQuantity(2100067, false), true, true);
                        //chr.gainItem(2100067, 30);
                        //MapleInventoryManipulator.removeById(chr.getClient(), MapleInventoryType.ETC, 4031868, chr.getItemQuantity(4031868, false), true, true);
                        //chr.gainItem(4031868, (short) 5);
                        //chr.dropMessage(0, "You have been granted 5 jewels(lifes) and 30 bombs.");
                        //chr.dropMessage(0, "Pick up as many bombs and jewels as you can!");
                        //chr.dropMessage(0, "Check inventory for Bomb under use");
                    }
                }
                for (MapleCharacter chrs : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                    chrs.getClient().getSession().write(CWvsContext.broadcastMsg(GameConstants.isEventMap(chrs.getMapId()) ? 0 : 22, c.getChannel(), "Event : Bomberman event has started!"));
                }
                player.getMap().broadcastMessage(CField.getClock(60));
            }
            return 1;
        }
    }

    public static class DeactiveBomberman extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (player.getMapId() != 109010100) {
                player.dropMessage(5, "This command is only usable in map 109010100.");
            } else {
                c.getChannelServer().toggleBomberman(c.getPlayer());
                int count = 0;
                String winner = "";
                for (MapleCharacter chr : player.getMap().getCharacters()) {
                    if (!chr.isGM()) {
                        if (count == 0) {
                            winner = chr.getName();
                            count++;
                        } else {
                            winner += " , " + chr.getName();
                        }
                    }
                }
                for (MapleCharacter chrs : c.getChannelServer().getPlayerStorage().getAllCharacters()) {
                    chrs.getClient().getSession().write(CWvsContext.broadcastMsg(GameConstants.isEventMap(chrs.getMapId()) ? 0 : 22, c.getChannel(), "Event : Bomberman event has ended! The winners are: " + winner));
                }
            }
            return 1;
        }
    }

    public static class 清理道具 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleCharacter player = c.getPlayer();
            if (splitted.length < 2 || player.hasBlockedInventory()) {
                c.getPlayer().dropMessage(5, splitted[0] + " <道具欄:裝備 / 消耗 / 其他 / 裝飾 / 特殊 / 全部>");
                return 0;
            } else {
                MapleInventoryType type;
                if (splitted[1].equalsIgnoreCase("裝備")) {
                    type = MapleInventoryType.EQUIP;
                } else if (splitted[1].equalsIgnoreCase("消耗")) {
                    type = MapleInventoryType.USE;
                } else if (splitted[1].equalsIgnoreCase("其他")) {
                    type = MapleInventoryType.ETC;
                } else if (splitted[1].equalsIgnoreCase("裝飾")) {
                    type = MapleInventoryType.SETUP;
                } else if (splitted[1].equalsIgnoreCase("特殊")) {
                    type = MapleInventoryType.CASH;
                } else if (splitted[1].equalsIgnoreCase("全部")) {
                    type = null;
                } else {
                    c.getPlayer().dropMessage(5, "找不到道具欄 < 裝備 / 消耗 / 其他 / 裝飾 / 特殊 / 全部>");
                    return 0;
                }
                if (type == null) { //All, a bit hacky, but it's okay 
                    MapleInventoryType[] invs = {MapleInventoryType.EQUIP, MapleInventoryType.USE, MapleInventoryType.SETUP, MapleInventoryType.ETC, MapleInventoryType.CASH};
                    for (MapleInventoryType t : invs) {
                        type = t;
                        MapleInventory inv = c.getPlayer().getInventory(type);
                        byte start = -1;
                        for (byte i = 0; i < inv.getSlotLimit() + 1; i++) {
                            if (inv.getItem(i) != null) {
                                start = i;
                                break;
                            }
                        }
                        if (start == -1) {
                            c.getPlayer().dropMessage(5, "此道具欄沒有道具");
                            return 0;
                        }
                        int end = 0;
                        for (byte i = start; i < inv.getSlotLimit() + 1; i++) {
                            if (inv.getItem(i) != null) {
                                MapleInventoryManipulator.removeFromSlot(c, type, i, inv.getItem(i).getQuantity(), true);
                            } else {
                                end = i;
                                break;//Break at first empty space. 
                            }
                        }
                        c.getPlayer().dropMessage(5, "已清除第" + start + "格到第" + end + "格的道具");
                    }
                } else {
                    MapleInventory inv = c.getPlayer().getInventory(type);
                    byte start = -1;
                    for (byte i = 0; i < inv.getSlotLimit() + 1; i++) {
                        if (inv.getItem(i) != null) {
                            start = i;
                            break;
                        }
                    }
                    if (start == -1) {
                        c.getPlayer().dropMessage(5, "此道具欄沒有道具");
                        return 0;
                    }
                    byte end = 0;
                    for (byte i = start; i < inv.getSlotLimit() + 1; i++) {
                        if (inv.getItem(i) != null) {
                            MapleInventoryManipulator.removeFromSlot(c, type, i, inv.getItem(i).getQuantity(), true);
                        } else {
                            end = i;
                            break;//Break at first empty space. 
                        }
                    }
                    c.getPlayer().dropMessage(5, "已清除第" + start + "格到第" + end + "格的道具");
                }
                return 1;
            }
        }
    }

    public static class Bob extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            MapleMonster mob = MapleLifeFactory.getMonster(9400551);
            for (int i = 0; i < 10; i++) {
                c.getPlayer().getMap().spawnMonsterOnGroundBelow(mob, c.getPlayer().getPosition());
            }
            return 1;
        }
    }

    public static class 殺全圖 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            for (MapleCharacter map : c.getPlayer().getMap().getCharactersThreadsafe()) {
                if (map != null && !map.isIntern()) {
                    map.getStat().setHp((short) 0, map);
                    map.getStat().setMp((short) 0, map);
                    map.updateSingleStat(MapleStat.HP, map.getStat().getHp());
                    map.updateSingleStat(MapleStat.MP, map.getStat().getMp());
                }
            }
            return 1;
        }
    }

    public static class 說話顏色 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <顏色值>");
                return 0;
            }
            try {
                c.getPlayer().setChatType((short) Short.parseShort(splitted[1]));
                c.getPlayer().dropMessage(6, "說話顏色更變完成。");
            } catch (Exception e) {
                c.getPlayer().dropMessage(5, "出現未知錯誤。");
            }
            return 1;
        }
    }

    public static class 檢索指令 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <關鍵字詞>");
                return 0;
            }
            c.getPlayer().dropMessage(6, "檢索指令(關鍵字詞:" + splitted[1] + ")結果如下:");
            HashMap<Integer, ArrayList<String>> commandList = CommandProcessor.getCommandList();
            for (int i = 0; i <= c.getPlayer().getGMLevel(); i++) {
                if (commandList.containsKey(i)) {
                    final PlayerGMRank pGMRank = PlayerGMRank.getByLevel(i);
                    final StringBuilder sb = new StringBuilder("");
                    final StringBuilder 指令前綴 = new StringBuilder("");
                    char[] gmRank = pGMRank.getCommandPrefix();
                    for (int j = 0; j < gmRank.length; j++) {
                        指令前綴.append('"').append(gmRank[j]).append('"');
                        if (j != gmRank.length - 1 && gmRank.length != 1) {
                            指令前綴.append("或");
                        }
                    }
                    for (String s : commandList.get(i)) {
                        if (s.contains(splitted[1].toLowerCase())) {
                            if ((gmRank.length > 1 && s.substring(0, 1).equals(String.valueOf(gmRank[0]))) || gmRank.length == 1) {
                                sb.append(s.substring(1));
                                sb.append("，");
                            }
                        }
                    }
                    if (!sb.toString().equals("")) {
                        c.getPlayer().dropMessage(6, "-----------------------------------------------------------------------------------------");
                        if (pGMRank == PlayerGMRank.NORMAL) {
                            c.getPlayer().dropMessage(6, "玩家指令(前綴:" + 指令前綴 + ")：");
                        } else if (pGMRank == PlayerGMRank.INTERN) {
                            c.getPlayer().dropMessage(6, "實習管理員指令(前綴:" + 指令前綴 + ")：");
                        } else if (pGMRank == PlayerGMRank.GM) {
                            c.getPlayer().dropMessage(6, "遊戲管理員指令(前綴:" + 指令前綴 + ")：");
                        } else if (pGMRank == PlayerGMRank.SUPERGM) {
                            c.getPlayer().dropMessage(6, "高級管理員指令(前綴:" + 指令前綴 + ")：");
                        } else if (pGMRank == PlayerGMRank.ADMIN) {
                            c.getPlayer().dropMessage(6, "伺服器管理指令(前綴:" + 指令前綴 + ")：");
                        }
                        c.getPlayer().dropMessage(6, sb.toString());
                    }
                }
            }
            return 1;
        }
    }

    public static class 全域掉寶 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            c.removeClickedNPC();
            final List<MonsterGlobalDropEntry> drops = MapleMonsterInformationProvider.getInstance().getGlobalDrop();
            StringBuilder name = new StringBuilder();
            if (drops != null && drops.size() > 0) {
                int num = 0;
                int itemId;
                int ch;
                MonsterGlobalDropEntry de;
                for (int i = 0; i < drops.size(); i++) {
                    de = drops.get(i);
                    if (de.chance > 0 && (de.questid <= 0 || (de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0))) {
                        itemId = de.itemId;
                        if (num == 0) {
                            name.append("全域掉寶數據如下\r\n");
                            name.append("--------------------------------------\r\n");
                        }
                        String namez = "#z" + itemId + "#";
                        if (itemId == 0) { //meso
                            itemId = 4031041; //display sack of cash
                            namez = (de.Minimum * c.getChannelServer().getMesoRate(c.getPlayer().getWorld())) + "到" + (de.Maximum * c.getChannelServer().getMesoRate(c.getPlayer().getWorld())) + "楓幣";
                        }
                        ch = de.chance * c.getChannelServer().getDropRate(c.getPlayer().getWorld());
                        name.append(num + 1).append(") #i").append(itemId).append(":#").append(namez).append("(").append(itemId).append(")").append(de.questid > 0 && MapleQuest.getInstance(de.questid).getName().length() > 0 ? ("[" + MapleQuest.getInstance(de.questid).toString() + "]") : "").append("\r\n掉寶幾率：").append(Integer.valueOf(ch >= 999999 ? 1000000 : ch).doubleValue() / 10000.0).append("%").append(" 來源：").append(de.addFrom).append("\r\n\r\n");
                        num++;
                    }
                }
            }
            if (name.length() > 0) {
                c.getSession().write(NPCPacket.getNPCTalk(9010000, (byte) 0, name.toString(), "00 00", (byte) 0, 9010000));
            } else {
                c.getPlayer().dropMessage(1, "全域掉寶無數據。");
            }
            return 1;
        }
    }

    public static class 製作道具 extends CommandExecute {

        @Override
        public int execute(MapleClient c, String[] splitted) {
            if (splitted.length < 2) {
                c.getPlayer().dropMessage(6, splitted[0] + " <道具ID>");
                return 0;
            }
            final int itemId = Integer.parseInt(splitted[1]);
            final short quantity = (short) CommandProcessorUtil.getOptionalIntArg(splitted, 2, 1);

            if (!c.getPlayer().isAdmin()) {
                for (int i : GameConstants.itemBlock) {
                    if (itemId == i) {
                        c.getPlayer().dropMessage(5, "當前管理員等級沒有製作此道具的權限");
                        return 0;
                    }
                }
            }
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            if (!ii.itemExists(itemId)) {
                c.getPlayer().dropMessage(5, "此道具不存在");
            } else {
                Item item;
                short flag = (short) ItemFlag.LOCK.getValue();

                if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    item = ii.getEquipById(itemId);
                } else {
                    item = new Item(itemId, (byte) 0, quantity, (byte) 0);
                }

                if (ItemConstants.類型.寵物(itemId)) {
                    MaplePet pet = MaplePet.createPet(itemId);
                    if (pet != null) {
                        item.setPet(pet);
                    }
                }
                if (!c.getPlayer().isGM()) {
                    item.setFlag(flag);
                }
                if (!c.getPlayer().isAdmin()) {
                    item.setOwner(c.getPlayer().getName());
                }
                item.setGMLog(c.getPlayer().getName() + " 使用 " + splitted[0] + " 指令制作, 時間:" + FileoutputUtil.CurrentReadable_Time());
                MapleInventoryManipulator.addbyItem(c, item);
            }
            return 1;
        }
    }
}
