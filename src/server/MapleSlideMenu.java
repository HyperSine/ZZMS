/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import client.MapleCharacter;
import constants.ServerConstants;

/**
 *
 * @author Itzik
 */
public class MapleSlideMenu { // UI.wz\UIWindow2.img\SlideMenu\

    public static class SlideMenu0 {

        public static final int version = ServerConstants.MAPLE_VERSION; //Change as you wish
        public static final int above = 0xFF;

        public static enum 異次元的門 { //Updated to V182 without quests

            PQ0(0, "阿里安特競技場", 682020000, 3, 20, 30, 0, 0, false),
            PQ1(1, "武陵道場", 925020000, 4, 90, above, 0, 0, true),
            PQ2(2, "怪物擂臺賽", 980000000, 0, 30, 50, 0, 0, false),
            PQ3(3, "怪物擂臺賽2", 980030000, 0, 50, 70, 0, 0, false),
            PQ4(4, "霧之海", 923020000, 0, 120, 159, 0, 0, false),
            PQ5(5, "奈特的金字塔", 926010000, 0, 60, 109, 0, 0, true),
            PQ6(6, "廢棄的地鐵月台", 910320000, 0, 25, 30, 0, 0, false),
            PQ7(7, "幸福村", 209000000, 0, 13, above, 0, 0, false),
            PQ8(8, "黃金寺廟2", 252000000, 0, 110, above, 0, 0, false),
            PQ9(9, "月妙的年糕", 0, 0, 50, above, 0, 0, false),
            PQ10(10, "第一次同行", 910340700, 0, 50, above, 0, 0, false),
            PQ11(11, "時空的裂縫", 221023300, 0, 50, above, 0, 0, false),
            PQ12(12, "毒霧森林", 300030100, 0, 70, above, 0, 0, false),
            PQ13(13, "女神的痕跡", 200080101, 0, 70, above, 0, 0, false),
            PQ14(14, "金勾海賊王", 251010404, 0, 70, above, 0, 0, false),
            PQ15(15, "羅密歐和朱麗葉", 261000021, 0, 70, above, 0, 0, false),
            PQ16(16, "侏儒帝王的復活", 211000002, 0, 120, above, 0, 0, false),
            PQ17(17, "龍騎士", 240080000, 0, 120, above, 0, 0, false),
            PQ18(18, "活動地圖", 0, 0, 10, above, 0, 0, false),
            PQ19(19, "萬聖節的樹", 682000000, 0, 120, above, 0, 0, false),
            PQ20(20, "活動地圖2", 0, 0, 10, above, 0, 0, false),
            PQ21(21, "陷入危險的坎特", 923040000, 0, 120, above, 0, 0, false),
            PQ22(22, "逃脫", 921160000, 0, 120, above, 0, 0, false),
            PQ23(23, "冰凍騎士的詛咒", 932000000, 0, 20, above, 0, 0, false),
            PQ24(24, "活動地圖3", 0, 0, 10, above, 0, 0, false),
            PQ25(25, "楓之谷聯盟會議場", 913050010, 0, 10, above, 0, 0, true),
            PQ26(26, "萬聖節的樹2", 682000700, 0, 10, above, 0, 0, false),
            PQ27(27, "阿斯旺解放之戰", 262010000, 0, 40, above, 0, 0, true),
            PQ28(28, "黃金寺廟", 252000000, 0, 105, above, 0, 0, true),
            PQ29(29, "休菲凱曼的畫廊", 0, 0, 50, 120, 0, 0, false),
            PQ30(30, "大亂鬥", 0, 0, 70, above, 0, 0, false),
            PQ31(31, "櫻花城", 231000000, 0, 10, above, 0, 0, false),
            PQ32(32, "進化系統研究所", 957000000, 0, 100, above, 1802, 1, true),
            PQ33(33, "次元入侵", 940020000, 0, 140, above, 0, 0, true),
            PQ34(34, "休菲凱曼的招待所(初級)", 910002000, 0, 50, above, 0, 0, false),
            PQ35(35, "休菲凱曼的招待所(中級)", 910002010, 0, 70, above, 0, 0, false),
            PQ36(36, "休菲凱曼的招待所(高級)", 910002020, 2, 10, above, 0, 0, false),
            PQ37(37, "湯寶寶", 0, 0, 70, above, 0, 0, false),
            PQ38(38, "克林森烏德城", 301000000, 0, 130, above, 0, 0, true),
            PQ39(39, "次元圖書館", 302000000, 0, 100, 125, 0, 0, true),
            PQ40(40, "進入聯合組隊任務", 910002000, 0, 0, above, 0, 0, true),
            PQ41(41, "跨伺服器聯合組隊任務", 708001000, 0, 70, above, 0, 0, true),
            PQ42(42, "露塔必思", 105200000, 0, 125, above, 0, 0, true),//910700200任务
            PQ43(43, "綠野仙蹤", 0, 0, 100, above, 0, 0, true),
            PQ44(44, "Friends Story", 100000004, 0, 100, above, 0, 0, true),
            PQ45(45, "怪物公園", 100000004, 0, 100, above, 0, 0, true),
            PQ200(200, "外星訪問者", 861000000, 0, 10, above, 0, 0, true),
            PQ201(201, "另一個水世界", 860000000, 6, 170, above, 0, 0, true),
            PQ512(512, "維拉仙特", 512000000, 0, 0, above, 0, 0, true),
            PQ801(801, "世界旅行", 950000000, 0, 0, above, 0, 0, true),
            PQ802(802, "中式婚禮", 700000000, 0, 0, above, 0, 0, false),
            PQ803(803, "楓葉丘陵", 807000000, 0, 0, above, 0, 0, true),
            PQ804(804, "尖耳狐狸村", 410000000, 0, 0, above, 0, 0, true),
            PQ805(805, "Jett&俠客", 743050000, 3, 0, above, 0, 0, true),
            PQ806(806, "幸福山", 0, 3, 0, above, 0, 0, true),
            PQ807(807, "阿里山", 749080900, 0, 0, above, 0, 0, true),
            PQ808(808, "虛幻之森", 749081200, 0, 0, above, 0, 0, true),
            PQ908(908, "比叡山本堂", 811000999, 0, 0, above, 0, 0, true),
            PQ992(992, "初音未來", 812000000, 0, 0, above, 0, 0, true),
            PQ993(993, "進擊的巨人", 814000000, 0, 0, above, 0, 0, true),
            DEFAULT(Integer.MAX_VALUE, "Default Map", 999999999, 0, 0, 0, 0, 0, false);
            private int id, map, portal, minLevel, maxLevel, requieredQuest, requieredQuestState;
            private String name, portal_string;
            private boolean show;

            異次元的門(int id, String name, int map, int portal, int minLevel, int maxLevel, int requieredQuest, int requieredQuestState, boolean show) {
                this.id = id;
                this.name = name;
                this.map = map;
                this.portal = portal;
                this.minLevel = minLevel;
                this.maxLevel = maxLevel;
                this.requieredQuest = requieredQuest;
                this.requieredQuestState = requieredQuestState;
                this.show = show;
            }

            異次元的門(int id, String name, int map, String portal_string, int minLevel, int maxLevel, int requieredQuest, int requieredQuestState, boolean show) {
                this.id = id;
                this.name = name;
                this.map = map;
                this.portal_string = portal_string;
                this.minLevel = minLevel;
                this.maxLevel = maxLevel;
                this.requieredQuest = requieredQuest;
                this.requieredQuestState = requieredQuestState;
                this.show = show;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public int getMap() {
                return map;
            }

            public int getPortal() {
                return portal;
            }

            public String getPortalString() {
                return portal_string;
            }

            public int getMinLevel() {
                return minLevel;
            }

            public int getMaxLevel() {
                return maxLevel;
            }

            public int getRequieredQuest() {
                return requieredQuest;
            }

            public int getRequieredQuestState() {
                return requieredQuestState;
            }

            public boolean show() {
                return show;
            }

            public static 異次元的門 getById(int id) {
                for (異次元的門 mirror : 異次元的門.values()) {
                    if (mirror.getId() == id) {
                        return mirror;
                    }
                }
                return 異次元的門.DEFAULT;
            }
        }

        public static String getSelectionInfo(MapleCharacter chr, int npcid) {
            String mapselect = "";
            for (異次元的門 mirror : 異次元的門.values()) {
                if (chr.getLevel() >= mirror.getMinLevel() && chr.getLevel() <= mirror.getMaxLevel() && mirror.show()) {
                    if ((chr.getQuestStatus(mirror.getRequieredQuest()) >= mirror.getRequieredQuestState()) || mirror.getRequieredQuest() == 0) {
                        if (mirror != 異次元的門.DEFAULT) {
                            mapselect += "#" + mirror.getId() + "#" + mirror.getName();
                        }
                    }
                }
            }
            if (mapselect.isEmpty() || mapselect.equals("")) {
                mapselect = "#-1# There are no locations you can move to.";
            }
            if (npcid == 9201231) {
                mapselect = "#87# Crack in the Dimensional Mirror";
            }
            return mapselect;
        }

        public static int[] getDataIntegers(int id) {
            System.out.println(異次元的門.getById(id).getMap());
            異次元的門 mirror = 異次元的門.getById(id);
            int[] dataIntegers = {mirror.getMap(), mirror.getPortal()};
            return dataIntegers;
        }
    }

    public static class SlideMenu1 {

        public static enum 時間通道 {
            //TODO: finish this(add quest ids and map ids)

            YEAR_2021(1, "Year 2021, Average Town", 0, 0, 0, 0),
            YEAR_2099(2, "Year 2099, Midnight Harbor", 0, 0, 0, 0),
            YEAR_2215(3, "Year 2215, Bombed City Center", 0, 0, 0, 0),
            YEAR_2216(4, "Year 2216, Ruined City", 0, 0, 0, 0),
            YEAR_2230(5, "Year 2230, Dangerous Tower", 0, 0, 0, 0),
            YEAR_2503(6, "Year 2503, Air Battleship Hermes", 0, 0, 0, 0);
            private int id, map, portal, requieredQuest, requieredQuestState;
            private String name;

            時間通道(int id, String name, int map, int portal, int requieredQuest, int requieredQuestState) {
                this.id = id;
                this.name = name;
                this.map = map;
                this.requieredQuest = requieredQuest;
                this.requieredQuestState = requieredQuestState;
                this.portal = portal;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public int getMap() {
                return map;
            }

            public int getPortal() {
                return portal;
            }

            public int getRequieredQuest() {
                return requieredQuest;
            }

            public int getRequieredQuestState() {
                return requieredQuestState;
            }

            public static 時間通道 getById(int id) {
                for (時間通道 gate : 時間通道.values()) {
                    if (gate.getId() == id) {
                        return gate;
                    }
                }
                return null; //default
            }
        }

        public static String getSelectionInfo(MapleCharacter chr, int npc) {
            String mapselect = "";
            for (時間通道 gate : 時間通道.values()) {
                if ((chr.getQuestStatus(gate.getRequieredQuest()) == gate.getRequieredQuestState()) || gate.getRequieredQuest() == 0) {
                    mapselect += "#" + gate.getId() + "#" + gate.getName();
                }
            }
            if (mapselect == null || mapselect.isEmpty()) {
                mapselect = "#-1# There are no locations you can move to.";
            }
            return mapselect;
        }

        public static int[] getDataIntegers(int id) {
            時間通道 timeGate = 時間通道.getById(id);
            int[] dataIntegers = {timeGate != null ? timeGate.getMap() : 時間通道.YEAR_2099.getMap(), 0}; //Year 2099 as default
            return dataIntegers;
        }
    }

    public static class SlideMenu2 {

        public static enum 優質網咖時空石 {

            TOWN_0(0, "弓箭手村", 100000000, 0),
            TOWN_1(1, "魔法森林", 101000000, 0),
            TOWN_2(2, "勇士之村", 102000000, 0),
            TOWN_3(3, "墮落城市", 103000000, 0),
            TOWN_4(4, "維多利亞港", 104000000, 0),
            TOWN_5(5, "奇幻村", 105000000, 0),
            TOWN_6(6, "鯨魚號", 120000100, 0),
            TOWN_7(7, "耶雷弗", 130000000, 0),
            TOWN_8(8, "瑞恩", 140000000, 0),
            TOWN_9(9, "天空之城", 200000000, 0),
            TOWN_10(10, "冰原雪域", 211000000, 0),
            TOWN_11(11, "玩具城", 220000000, 0),
            //            TOWN_12(12, "地球防禦總部", 221000000, 0),
            //            TOWN_13(13, "童话村", 222000000, 0),
            TOWN_14(14, "水世界", 230000000, 0),
            TOWN_15(15, "神木村", 240000000, 0),
            TOWN_16(16, "桃花仙境", 250000000, 0),
            TOWN_17(17, "靈藥幻境", 251000000, 0),
            TOWN_18(18, "納希沙漠", 260000000, 0),
            TOWN_19(19, "瑪迦提亞城", 261000000, 0),
            TOWN_20(20, "六條岔道", 104020000, 2),
            TOWN_21(21, "櫻花處", 101050000, 0),
            TOWN_22(22, "萬神殿", 400000000, 0),
            //            TOWN_23(23, "未知", 0, 0),
            TOWN_24(24, "天堂", 31007000, 0);
            private final int id, map, portal;
            private final String name;

            優質網咖時空石(int id, String name, int map, int portal) {
                this.id = id;
                this.name = name;
                this.map = map;
                this.portal = portal;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public int getMap() {
                return map;
            }

            public int getPortal() {
                return portal;
            }

            public static 優質網咖時空石 getById(int id) {
                for (優質網咖時空石 town : 優質網咖時空石.values()) {
                    if (town.getId() == id) {
                        return town;
                    }
                }
                return null;
            }
        }

        public static String getSelectionInfo(MapleCharacter chr, int npc) {
            String mapselect = "";
            for (優質網咖時空石 gate : 優質網咖時空石.values()) {
                mapselect += "#" + gate.getId() + "#" + gate.getName();
            }
            if ("".equals(mapselect)) {
                mapselect = "#-1# There are no locations you can move to.";
            }
            return mapselect;
        }

        public static int[] getDataIntegers(int id) {
            優質網咖時空石 toenTeleport = 優質網咖時空石.getById(id);
            int[] dataIntegers = {toenTeleport != null ? toenTeleport.getMap() : -1, toenTeleport != null ? toenTeleport.getPortal() : -1};
            return dataIntegers;
        }
    }

    public static class SlideMenu3 {

        public static enum 時空石 {

            TOWN_0(0, "弓箭手村", 100000000, 0),
            TOWN_1(1, "魔法森林", 101000000, 0),
            TOWN_2(2, "勇士之村", 102000000, 0),
            TOWN_3(3, "墮落城市", 103000000, 0),
            TOWN_4(4, "維多利亞港", 104000000, 0),
            TOWN_5(5, "奇幻村", 105000000, 0),
            TOWN_6(6, "鯨魚號", 120000100, 0),
            TOWN_7(7, "耶雷弗", 130000000, 0),
            TOWN_8(8, "瑞恩", 140000000, 0),
            TOWN_9(9, "天空之城", 200000000, 0),
            TOWN_10(10, "冰原雪域", 211000000, 0),
            TOWN_11(11, "玩具城", 220000000, 0),
            //            TOWN_12(12, "地球防禦總部", 221000000, 0),
            //            TOWN_13(13, "童话村", 222000000, 0),
            TOWN_14(14, "水世界", 230000000, 0),
            TOWN_15(15, "神木村", 240000000, 0),
            TOWN_16(16, "桃花仙境", 250000000, 0),
            TOWN_17(17, "靈藥幻境", 251000000, 0),
            TOWN_18(18, "納希沙漠", 260000000, 0),
            TOWN_19(19, "瑪迦提亞城", 261000000, 0),
            TOWN_20(20, "埃德爾斯坦", 310000000, 0),
            TOWN_21(21, "櫻花處", 101050000, 0),
            TOWN_22(22, "萬神殿", 400000000, 0),
            //            TOWN_23(23, "未知", 0, 0),
            TOWN_24(24, "天堂", 31007000, 0);
            private final int id, map, portal;
            private final String name;

            時空石(int id, String name, int map, int portal) {
                this.id = id;
                this.name = name;
                this.map = map;
                this.portal = portal;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public int getMap() {
                return map;
            }

            public int getPortal() {
                return portal;
            }

            public static 時空石 getById(int id) {
                for (時空石 town : 時空石.values()) {
                    if (town.getId() == id) {
                        return town;
                    }
                }
                return null;
            }
        }

        public static String getSelectionInfo(MapleCharacter chr, int npc) {
            String mapselect = "";
            for (時空石 gate : 時空石.values()) {
                mapselect += "#" + gate.getId() + "#" + gate.getName();
            }
            if ("".equals(mapselect)) {
                mapselect = "#-1# There are no locations you can move to.";
            }
            return mapselect;
        }

        public static int[] getDataIntegers(int selection) {
            時空石 toenTeleport = 時空石.getById(selection);
            int[] dataIntegers = {toenTeleport != null ? toenTeleport.getMap() : -1, toenTeleport != null ? toenTeleport.getPortal() : -1};
            return dataIntegers;
        }
    }

    public static class SlideMenu4 {

        public static enum 武陵道場 {
            /*
             #0# Recover 50% HP
             #1# Recover 100% HP
             #2# MaxHP + 10000 (Duration: 10 min)
             #3# Weapon/Magic ATT + 30 (Duration: 10 min)
             #4# Weapon/Magic ATT + 60 (Duration: 10 min)
             #5# Weapon/Magic DEF + 2500 (Duration: 10 min)
             #6# Weapon/Magic DEF + 4000 (Duration: 10 min)
             #7# Accuracy/Avoidability + 2000 (Duration: 10 min)
             #8# Speed/Jump MAX (Duration: 10 min)
             #9# Attack Speed + 1 (Duration: 10 min)
             */

            BUFF0(0, "Recover 50% HP", 2022855),
            BUFF1(1, "Recover 100% HP", 2022856),
            BUFF2(2, "MaxHP + 10000 (Duration: 10 min)", 2022857),
            BUFF3(3, "Weapon/Magic ATT + 30 (Duration: 10 min)", 2022858),
            BUFF4(4, "Weapon/Magic ATT + 60 (Duration: 10 min)", 2022859),
            BUFF5(5, "Weapon/Magic DEF + 2500 (Duration: 10 min)", 2022860),
            BUFF6(6, "Weapon/Magic DEF + 4000 (Duration: 10 min)", 2022861),
            BUFF7(7, "Accuracy/Avoidability + 2000 (Duration: 10 min)", 2022862),
            BUFF8(8, "Speed/Jump MAX (Duration: 10 min)", 2022863),
            BUFF9(9, "Attack Speed + 1 (Duration: 10 min)", 2022864);
            private final int id, buff;
            private final String name;

            武陵道場(int id, String name, int buff) {
                this.id = id;
                this.name = name;
                this.buff = buff;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public int getBuff() {
                return buff;
            }

            public static 武陵道場 getById(int id) {
                for (武陵道場 buff : 武陵道場.values()) {
                    if (buff.getId() == id) {
                        return buff;
                    }
                }
                return null;
            }
        }

        public static String getSelectionInfo(MapleCharacter chr, int npc) {
            String buffselect = "";
            for (武陵道場 buffdata : 武陵道場.values()) {
                buffselect += "#" + buffdata.getId() + "# " + buffdata.getName();
            }
            return buffselect;
        }

        public static int[] getDataIntegers(int id) {
            武陵道場 dataInteger = 武陵道場.getById(id);
            int[] dataIntegers = {dataInteger != null ? dataInteger.getBuff() : 武陵道場.BUFF0.getBuff(), 0};
            return dataIntegers;
        }
    }

    public static class SlideMenu5 {

        public static enum 萬神殿次元傳點 {

            TOWN_0(0, "六條岔道", 104020000, 2),
            TOWN_1(1, "弓箭手村", 100000000, 0),
            TOWN_2(2, "魔法森林", 101000000, 0),
            TOWN_3(3, "勇士之村", 102000000, 0),
            TOWN_4(4, "墮落城市", 103000000, 0),
            TOWN_5(5, "維多利亞港", 104000000, 0),
            TOWN_6(6, "奇幻村", 105000000, 0),
            TOWN_7(7, "鯨魚號", 120000100, 0),
            TOWN_8(8, "耶雷弗", 130000000, 0),
            TOWN_9(9, "瑞恩", 140000000, 0),
            TOWN_10(10, "天空之城", 200000000, 0),
            TOWN_11(11, "冰原雪域", 211000000, 0),
            TOWN_12(12, "玩具城", 220000000, 0),
            //            TOWN_13(13, "地球防禦總部", 221000000, 0),
            //            TOWN_14(14, "童话村", 222000000, 0),
            TOWN_15(15, "水世界", 230000000, 0),
            TOWN_16(16, "神木村", 240000000, 0),
            TOWN_17(17, "桃花仙境", 250000000, 0),
            TOWN_18(18, "靈藥幻境", 251000000, 0),
            TOWN_19(19, "納希沙漠", 260000000, 0),
            TOWN_20(20, "瑪迦提亞城", 261000000, 0),
            TOWN_21(21, "埃德爾斯坦", 310000000, 0),
            TOWN_22(22, "櫻花處", 101050000, 0),
            //            TOWN_23(23, "未知", 0, 0),
            TOWN_24(24, "天堂", 31007000, 0),
            TOWN_25(25, "萬神殿", 400000000, 0);
            private final int id, map, portal;
            private final String name;

            萬神殿次元傳點(int id, String name, int map, int portal) {
                this.id = id;
                this.name = name;
                this.map = map;
                this.portal = portal;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public int getMap() {
                return map;
            }

            public int getPortal() {
                return portal;
            }

            public static 萬神殿次元傳點 getById(int id) {
                for (萬神殿次元傳點 town : 萬神殿次元傳點.values()) {
                    if (town.getId() == id) {
                        return town;
                    }
                }
                return null;
            }
        }

        public static String getSelectionInfo(MapleCharacter chr, int npc) {
            String mapselect = "";
            for (萬神殿次元傳點 gate : 萬神殿次元傳點.values()) {
                mapselect += "#" + gate.getId() + "#" + gate.getName();
            }
            if (mapselect == null || "".equals(mapselect)) {
                mapselect = "#-1# There are no locations you can move to.";
            }
            return mapselect;
        }

        public static int[] getDataIntegers(int selection) {
            萬神殿次元傳點 toenTeleport = 萬神殿次元傳點.getById(selection);
            int[] dataIntegers = {toenTeleport != null ? toenTeleport.getMap() : -1, toenTeleport != null ? toenTeleport.getPortal() : -1};
            return dataIntegers;
        }
    }

    public static class SlideMenu6 {

        public static enum belitase_lomometsa的航路 {

            TOWN_0(0, "弓箭手村", 100000000, 0),
            TOWN_1(1, "魔法森林", 101000000, 0),
            TOWN_2(2, "勇士之村", 102000000, 0),
            TOWN_3(3, "墮落城市", 103000000, 0),
            TOWN_4(4, "維多利亞港", 104000000, 0),
            TOWN_5(5, "奇幻村", 105000000, 0),
            TOWN_6(6, "鯨魚號", 120000100, 0),
            TOWN_7(7, "耶雷弗", 130000000, 0),
            TOWN_8(8, "瑞恩", 140000000, 0),
            TOWN_9(9, "天空之城", 200000000, 0),
            TOWN_10(10, "冰原雪域", 211000000, 0),
            TOWN_11(11, "玩具城", 220000000, 0),
            //            TOWN_12(12, "地球防禦總部", 221000000, 0),
            //            TOWN_13(13, "童话村", 222000000, 0),
            TOWN_14(14, "水世界", 230000000, 17),
            TOWN_15(15, "神木村", 240000000, 0),
            TOWN_16(16, "桃花仙境", 250000000, 0),
            TOWN_17(17, "靈藥幻境", 251000000, 0),
            TOWN_18(18, "納希沙漠", 260000000, 0),
            TOWN_19(19, "瑪迦提亞城", 261000000, 0),
            TOWN_20(20, "埃德爾斯坦", 310000000, 0),
            TOWN_21(21, "櫻花處", 101050000, 0),
            TOWN_22(22, "萬神殿", 400000000, 0),
//            TOWN_23(23, "未知", 0, 0),
            TOWN_24(24, "天堂", 31007000, 0);
            private final int id, map, portal;
            private final String name;

            belitase_lomometsa的航路(int id, String name, int map, int portal) {
                this.id = id;
                this.name = name;
                this.map = map;
                this.portal = portal;
            }

            public int getId() {
                return id;
            }

            public String getName() {
                return name;
            }

            public int getMap() {
                return map;
            }

            public int getPortal() {
                return portal;
            }

            public static belitase_lomometsa的航路 getById(int id) {
                for (belitase_lomometsa的航路 town : belitase_lomometsa的航路.values()) {
                    if (town.getId() == id) {
                        return town;
                    }
                }
                return null;
            }
        }

        public static String getSelectionInfo(MapleCharacter chr, int npc) {
            String mapselect = "";
            for (belitase_lomometsa的航路 gate : belitase_lomometsa的航路.values()) {
                mapselect += "#" + gate.getId() + "#" + gate.getName();
            }
            if ("".equals(mapselect)) {
                mapselect = "#-1# There are no locations you can move to.";
            }
            return mapselect;
        }

        public static int[] getDataIntegers(int selection) {
            belitase_lomometsa的航路 toenTeleport = belitase_lomometsa的航路.getById(selection);
            int[] dataIntegers = {toenTeleport != null ? toenTeleport.getMap() : -1, toenTeleport != null ? toenTeleport.getPortal() : -1};
            return dataIntegers;
        }
    }

    public static Class<?> getSlideMenu(int id) {
        final Class<?>[] slideMenus = new Class<?>[]{SlideMenu0.class,
            SlideMenu1.class, SlideMenu2.class, SlideMenu3.class,
            SlideMenu4.class, SlideMenu5.class};
        return slideMenus[id];
    }
}
