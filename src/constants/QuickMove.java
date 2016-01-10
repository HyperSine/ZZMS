
package constants;

public enum QuickMove {

    弓箭手村(100000000, QuickMoveNPC.大陸移動碼頭.getValue() | QuickMoveNPC.計程車.getValue()),
    魔法森林(101000000, QuickMoveNPC.大陸移動碼頭.getValue() | QuickMoveNPC.計程車.getValue()),
    勇士之村(102000000,QuickMoveNPC.大陸移動碼頭.getValue() | QuickMoveNPC.計程車.getValue()),
    墮落城市(103000000, QuickMoveNPC.大陸移動碼頭.getValue() | QuickMoveNPC.計程車.getValue()),
    維多利亞港(104000000, QuickMoveNPC.大陸移動碼頭.getValue() | QuickMoveNPC.計程車.getValue()),
    奇幻村(105000000, QuickMoveNPC.大陸移動碼頭.getValue() | QuickMoveNPC.計程車.getValue()),
    鯨魚號(120000100, QuickMoveNPC.大陸移動碼頭.getValue() | QuickMoveNPC.計程車.getValue()),
    耶雷弗(130000000, QuickMoveNPC.大陸移動碼頭.getValue()),
    瑞恩(140000000, QuickMoveNPC.大陸移動碼頭.getValue()),
    天空之城(200000000, QuickMoveNPC.大陸移動碼頭.getValue()),
    玩具城(220000000, QuickMoveNPC.大陸移動碼頭.getValue()),
    地球防禦總部(221000000, QuickMoveNPC.大陸移動碼頭.getValue()),
    童話村(222000000, QuickMoveNPC.大陸移動碼頭.getValue()),
    水世界(230000000, 0),
    神木村(240000000, QuickMoveNPC.大陸移動碼頭.getValue()),
    桃花仙境(250000000, QuickMoveNPC.大陸移動碼頭.getValue()),
    靈藥幻境(251000000, 0),
    納希沙漠(260000000, QuickMoveNPC.大陸移動碼頭.getValue()),
    瑪迦提亞城(261000000, 0),
    埃德爾斯坦(310000000, QuickMoveNPC.大陸移動碼頭.getValue()),
    萬神殿(400000000, 0);
    private final int map;
    private final long npc;
    private final long generalNpc = 
            QuickMoveNPC.怪物公園.getValue()
            | QuickMoveNPC.次元之鏡.getValue()
            | QuickMoveNPC.自由市場.getValue()
            | QuickMoveNPC.梅斯特鎮.getValue()
            | QuickMoveNPC.打工.getValue()
            | QuickMoveNPC.皇家美髮.getValue()
            | QuickMoveNPC.皇家整形.getValue()
            | QuickMoveNPC.里貝卡.getValue()
            | QuickMoveNPC.楓之谷拍賣場.getValue()
            | QuickMoveNPC.初音未來.getValue();
    public final static long GLOBAL_NPC = 
            QuickMoveNPC.戰國露西亞.getValue()
            | QuickMoveNPC.次元傳送門.getValue();

    private QuickMove(int map, long npc) {
        this.map = map;
        this.npc = npc | generalNpc;
    }

    public int getMap() {
        return map;
    }

    public long getNPCFlag() {
        return npc;
    }

    public enum QuickMoveNPC {//UI.wz\UIWindow2.img\EasyMove\

        PVP(0, false, 9070004, 30, "Move to the Battle Mode zone #cBattle Square#, where you can fight against other users.\n#cLv. 30 or above can participate in Battle Square."),
        怪物公園(1, true, 9071003, 20, "和隊員們齊心合力攻略強悍怪物的區域.\n移動到#c<怪物公園>#.\n#c一般怪物公園:  100級以上可參加\n 怪物競技場: 70級 ~ 200級"),
        次元之鏡(2, true, 9010022, 10, "使用可傳送到組隊任務等各種副本地圖的#c<次元之鏡>#。"),
        自由市場(3, true, 9000087, 0, "傳送到可與其他玩家進行道具交易的#c<自由市場>#。"),
        梅斯特鎮(4, true, 9000088, 30, "傳送到進行生產技術的#c<梅斯特鎮>#。\n#c35級以上就可進入。"),
        大陸移動碼頭(5, true, 9000086, 0, "傳送到最近的#c<大陸移動碼頭>#。"), //Boats, Airplanes
        計程車(6, true, 9000089, 0, "使用#c<計程車>#可將角色移動到附近主要地區。"), //Taxi, Camel
        戴彼得(7, false, 9010040, 10, ""),
        被派來的藍多普(8, false, 0, 10, ""),
        被派來的露西亞(9, false, 0, 10, ""),
        打工(10, true, 9010041, 30, "獲得打工的酬勞。"),
        末日風暴防具商店(11, false, 9010047, 30, ""),
        末日風暴武器商店(12, false, 9010048, 30, ""),
        皇家美髮(13, true, 9000123, 0, "可以讓比克·艾德華為你修剪一頭帥氣的髮型。"),
        皇家整形(14, true, 9201252, 0, "可以讓Dr·塑膠洛伊為你進行整型手術。"),
        冬季限量防具商店(15, false, 9000152, 30, ""),
        冬季限量武器商店(16, false, 9000153, 30, ""),
        里貝卡(17, true, 9000018, 30, "可使用高級硬幣向里貝卡兌換各種道具裝備。"),
        QM_UNKNOW1(18, false, 0, 30, ""),//一个好像财主的兔子
        彌莎(19, false, 0, 30, ""),
        楓之谷拍賣場(20, false, 0, 30, "可以透過愛格里曲來訪問楓之谷拍賣場."),//178已刪
        戰國露西亞(21, 100, true, 9130033, 30, "測試服專用的便利商店"),//戰國商店
        戰國藍多普(22, 101, false, 9130032, 30, ""),//戰國商店
        初音未來(23, 102, true, 812000000, 30, "移動至初音未來合作特設地圖#c<初音未來的演唱會會場>#。"),
        次元傳送門(24, 2, true, 3000012, 10, "使用可傳送到任意城市的#c<次元傳送門>#。");
        private final long value;
        private final int type, id, level;
        private final String desc;
        private final boolean show;

        private QuickMoveNPC(int type, boolean show, int id, int level, String desc) {
            this.value = 1 << type;
            this.type = type;
            this.show = show;
            this.id = id;
            this.level = level;
            this.desc = desc;
        }

        private QuickMoveNPC(int value, int type, boolean show, int id, int level, String desc) {
            this.value = 1 << value;
            this.type = type;
            this.show = show;
            this.id = id;
            this.level = level;
            this.desc = desc;
        }

        public final long getValue() {
            return value;
        }

        public final boolean check(int flag) {
            return (flag & value) != 0;
        }

        public int getType() {
            return type;
        }

        public boolean show() {
            return show;
        }

        public int getId() {
            return id;
        }

        public int getLevel() {
            return level;
        }

        public String getDescription() {
            return desc;
        }
    }
}
