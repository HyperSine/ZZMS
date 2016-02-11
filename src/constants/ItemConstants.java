package constants;

import client.MapleCharacter;
import client.MapleClient;
import client.inventory.Equip;
import client.inventory.MapleInventoryType;
import client.inventory.MapleWeaponType;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.StructItemOption;

public class ItemConstants {

    //裝備欄已裝備道具位置
    public static final short HAT = -1; //帽子
    public static final short EARRING = -4; //耳環
    public static final short TOP = -5; //上衣
    public static final short BOTTOM = -6; //褲裙
    public static final short SHOE = -7; //鞋子
    public static final short GLOVE = -8; //手套
    public static final short CAPE = -9; //披風
    public static final short SHIELD = -10; //副手
    public static final short WEAPON = -11; //武器
    public static final short RING1 = -12; //戒指1
    public static final short RING2 = -13; //戒指2
    public static final short FACE = -13; //臉飾?
    public static final short RING3 = -14; //戒指3
    public static final short RING4 = -15; //戒指4
    public static final short PENDANT = -17; //墜飾
    public static final short MOUNT = -18; //騎寵
    public static final short SADDLE = -19; //鞍
    public static final short MEDAL = -49; //勳章
    public static final short BELT = -50; //腰帶
    public static final short PET_EQUIP = -123; //寵物裝備
    public static final short AUTO_HP = -124; //PET_HP
    public static final short AUTO_MP = -125; //PET_MP
    public static final short CS_RING1 = -112; //現金戒指
    public static final short CS_RING2 = -113;
    public static final short CS_RING3 = -114;
    public static final short CS_RING4 = -115;
    public static final short CODEX = 55;

    public static class 傷害字型 {

        private static Map<Integer, Integer> damageSkin = new TreeMap<>((v1, v2) -> v1.compareTo(v2));

        public static Map<Integer, Integer> getDamageSkin() {
            if (damageSkin.isEmpty()) {
                //Effect.wz/BasicEff.img/damageSkin
                damageSkin.put(2431965, 0); //基本傷害肌膚
                damageSkin.put(2431966, 1); //數位顯示肌膚
                damageSkin.put(2432084, 1); //數位顯示字型
                damageSkin.put(2431967, 2); //克里提亞斯傷害肌膚
                damageSkin.put(2432131, 3); //組隊任務傷害肌膚
                damageSkin.put(2432153, 4); //衝擊震撼字型
                damageSkin.put(2432638, 4); //具有衝擊性的傷害字型
                damageSkin.put(2432659, 4); //具有衝擊性的傷害字型
                damageSkin.put(2432154, 5); //甜蜜餅乾字型
                damageSkin.put(2432637, 5); //傳統韓果傷害字型
                damageSkin.put(2432658, 5); //傳統韓果傷害字型
                damageSkin.put(2432207, 6); //鐵壁城牆字型
                damageSkin.put(2432354, 7); //聖誕快樂傷害字型
                damageSkin.put(2432355, 8); //雪花飄落字型
                damageSkin.put(2432972, 8); //雪花傷害肌膚
                damageSkin.put(2432465, 9); //愛麗西亞的傷害字型
                damageSkin.put(2432479, 10); //桃樂絲的傷害字型
                damageSkin.put(2432526, 11); //鍵盤戰士字型
                damageSkin.put(2432639, 11); //鍵盤戰士傷害字型
                damageSkin.put(2432660, 11); //鍵盤戰士傷害字型
                damageSkin.put(2432532, 12); //多彩春風字型
                damageSkin.put(2432592, 13); //單身部隊傷害字型
                damageSkin.put(2432640, 14); //雷咪納杉司傷害字型
                damageSkin.put(2432661, 14); //雷咪納杉司傷害字型
                damageSkin.put(2432710, 15); //菇菇寶貝傷害字型
                damageSkin.put(2432836, 16); //王冠傷害字型
                damageSkin.put(2432973, 17); //灰白傷害肌膚
                damageSkin.put(2433063, 18); //明星星球傷害肌膚
                damageSkin.put(2433456, 19); //韓文的傷害字型(效果是中文)
                damageSkin.put(2433178, 20); //2014萬聖節傷害肌膚
//            damageSkin.put(2433456, 21); //韓文的傷害字型
                damageSkin.put(2433631, 22); //NENE雞的傷害字型
                damageSkin.put(2433655, 22); //NENE雞的傷害字型
                damageSkin.put(2433981, 28); //皮卡啾傷害字型
                damageSkin.put(2432591, 1000); //櫻花浪漫字型傷害肌膚
                damageSkin.put(2432803, 1004); //濃姬傷害字型(30日)
                damageSkin.put(2432804, 1004); //濃姬傷害字型(無限期)
                damageSkin.put(2432846, 1005); //傑特字型交換卷
                damageSkin.put(2433049, 1009); //初音未來傷害字型
                damageSkin.put(2433038, 1010); //皇家神獸學院字型
                damageSkin.put(2433165, 1011); //俠客字型交換卷
                damageSkin.put(2433197, 1012); //菲歐娜字型交換卷
                damageSkin.put(2433195, 1013); //橘子節字型交換卷
                damageSkin.put(2433182, 1014); //萬聖節南瓜字型交換卷
                damageSkin.put(2433183, 1015); //萬聖節幽靈字型交換卷
                damageSkin.put(2433184, 1016); //萬聖節掃把字型交換卷
                //1018 - 跟1014一樣
                damageSkin.put(2433775, 1032); //殺人鯨傷害字型
                damageSkin.put(2433776, 1033); //史烏傷害字型
                damageSkin.put(2434004, 1041); //小筱傷害字型
            }
            Map<Integer, Integer> value = new TreeMap<>((v1, v2) -> v1.compareTo(v2));
            value.putAll(damageSkin);
            return value;
        }

        public static int getDamageSkinNumberByItem(int itemid) {
            Map<Integer, Integer> skin = getDamageSkin();
            if (skin.containsKey(itemid)) {
                return skin.get(itemid);
            }
            return -1;
        }

        public static Integer[] getDamageSkinsTradeBlock() {
            Map<Integer, Integer> skin = getDamageSkin();
            ArrayList<Integer> skins = new ArrayList<>();
            for (int s : skin.keySet()) {
                if (MapleItemInformationProvider.getInstance().isOnlyTradeBlock(s)) {
                    skins.add(s);
                }
            }
            Integer list[] = new Integer[skins.size()];
            return skins.toArray(list);
        }

        public static boolean isDamageSkin(int itemid) {
            Map<Integer, Integer> skin = getDamageSkin();
            if (skin.containsKey(itemid)) {
                return true;
            }
            return false;
        }
    }

    public static class 卷軸 {

        public static boolean canScroll(final int itemId) {
            return (itemId / 100000 != 19 && itemId / 100000 != 16) || 類型.機器人心臟(itemId); //no mech/taming/dragon/心臟
        }

        public static boolean canHammer(final int itemId) {
            switch (itemId) {
                case 1122000:
                case 1122076: //ht, chaos ht
                    return false;
            }
            return canScroll(itemId);
        }

        public static int getChaosNumber(int itemId) {
            switch (itemId) {
                case 2049116://驚訝的混沌卷軸 60%
                case 2049132://驚訝的混沌卷軸 30%
                case 2049133://驚訝的混沌卷軸 50%
                case 2049134://驚訝的混沌卷軸 70%
                case 2049135://驚訝樂觀的混沌卷軸 20%
                case 2049136://驚訝樂觀的混沌卷軸 20%
                case 2049137://驚訝樂觀的混沌卷軸 40%
                case 2049140://珠寶戒指的驚訝的混沌卷軸 40%
                case 2049142://驚訝的混沌卷軸 40%
                case 2049145://珠寶工藝驚訝的混沌卷軸 40%
                case 2049152://驚訝的混沌卷軸 60%
                case 2049153://驚訝樂觀的混沌卷軸
                case 2049156://驚訝的混沌卷軸 20%
                case 2049159://驚訝的混沌卷軸 50%
                case 2049165://驚訝的混沌卷軸 50%
                    return 10;
            }
            return 5;
        }

        public static int getBonusPotentialScrollSucc(int scrollId) {
            switch (scrollId) {
                case 2048306://成功100,3行
                case 2048307://成功100
                case 2048315://成功100
                case 2048316://成功100
                    return 100;
                case 2048313://心之項鍊專用,成功80
                    return 80;
                case 2048305://成功70,失敗損壞100
                    return 70;
                case 2048309://成功60,無損
                case 2048310://成功60,失敗損壞100
                case 2048314://成功60
                    return 60;
                case 2048308://成功50,失敗損壞50
                case 2048311://成功50,失敗損壞50
                    return 50;
                case 2048312://成功1
                    return 1;
                default:
                    return 0;
            }
        }

        public static int getBonusPotentialScrollCurse(int scrollId) {
            switch (scrollId) {
                case 2048305://成功70,失敗損壞100
                case 2048310://成功60,失敗損壞100
                    return 100;
                case 2048308://成功50,失敗損壞50
                case 2048311://成功50,失敗損壞50
                    return 50;
                default:
                    return 0;
            }
        }

        public static int getSuccessTablet(final int scrollId, final int level) {
            if (scrollId % 1000 / 100 == 2) { //2047_2_00 = armor, 2047_3_00 = accessory
                switch (level) {
                    case 0:
                        return 70;
                    case 1:
                        return 55;
                    case 2:
                        return 43;
                    case 3:
                        return 33;
                    case 4:
                        return 26;
                    case 5:
                        return 20;
                    case 6:
                        return 16;
                    case 7:
                        return 12;
                    case 8:
                        return 10;
                    default:
                        return 7;
                }
            } else if (scrollId % 1000 / 100 == 3) {
                switch (level) {
                    case 0:
                        return 70;
                    case 1:
                        return 35;
                    case 2:
                        return 18;
                    case 3:
                        return 12;
                    default:
                        return 7;
                }
            } else {
                switch (level) {
                    case 0:
                        return 70;
                    case 1:
                        return 50; //-20
                    case 2:
                        return 36; //-14
                    case 3:
                        return 26; //-10
                    case 4:
                        return 19; //-7
                    case 5:
                        return 14; //-5
                    case 6:
                        return 10; //-4
                    default:
                        return 7;  //-3
                }
            }
        }

        public static int getCurseTablet(final int scrollId, final int level) {
            if (scrollId % 1000 / 100 == 2) { //2047_2_00 = armor, 2047_3_00 = accessory
                switch (level) {
                    case 0:
                        return 10;
                    case 1:
                        return 12;
                    case 2:
                        return 16;
                    case 3:
                        return 20;
                    case 4:
                        return 26;
                    case 5:
                        return 33;
                    case 6:
                        return 43;
                    case 7:
                        return 55;
                    case 8:
                        return 70;
                    default:
                        return 100;
                }
            } else if (scrollId % 1000 / 100 == 3) {
                switch (level) {
                    case 0:
                        return 12;
                    case 1:
                        return 18;
                    case 2:
                        return 35;
                    case 3:
                        return 70;
                    default:
                        return 100;
                }
            } else {
                switch (level) {
                    case 0:
                        return 10;
                    case 1:
                        return 14; //+4
                    case 2:
                        return 19; //+5
                    case 3:
                        return 26; //+7
                    case 4:
                        return 36; //+10
                    case 5:
                        return 50; //+14
                    case 6:
                        return 70; //+20
                    default:
                        return 100;  //+30
                }
            }
        }

        public static int getEnhanceTimes(int itemId) {
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            int level = ii.getReqLevel(itemId);
            boolean isSuperiorEquip = ii.isSuperiorEquip(itemId);
            int enhanceTimes = 0;
            if (level >= 0 && level < 95) {
                enhanceTimes = isSuperiorEquip ? 3 : 5;
            } else if (level >= 95 && level < 108) {
                enhanceTimes = isSuperiorEquip ? 5 : 8;
            } else if (level >= 108 && level < 118) {
                enhanceTimes = isSuperiorEquip ? 8 : 10;
            } else if (level >= 118 && level < 128) {
                enhanceTimes = isSuperiorEquip ? 10 : 15;
            } else if (level >= 128 && level < 138) {
                enhanceTimes = isSuperiorEquip ? 12 : 20;
            } else if (level >= 138) {
                enhanceTimes = isSuperiorEquip ? 15 : 25;
            }
            return enhanceTimes;
        }
    }

    public static class 套裝 {

        public static ArrayList<Integer> get6YearSet() {
            int[] set = {1462116, 1342039, 1402109, 1472139, 1332147, 1322105, 1442135, 1452128, 1312071, 1382123, 1492100, 1372099, 1432098, 1422072, 1302172, 1482101, 1412070};
            ArrayList<Integer> list = new ArrayList();
            for (int i : set) {
                list.add(i);
            }
            return list;
        }

        public static ArrayList<Integer> get7YearSet() {
            int[] set = {1003243, 1052358, 1072522, 1082315, 1102295, 1132093, 1152061, 1332145, 1402107, 1442133, 1462114, 1472137, 1532070, 1522066, 1452126, 1312069, 1382121, 1492098, 1372097, 1362058, 1432096, 1422070, 1302170, 1482099, 1412068};
            ArrayList<Integer> list = new ArrayList();
            for (int i : set) {
                list.add(i);
            }
            return list;
        }

        public static ArrayList<Integer> get8YearSet() {
            int[] set = {1462159, 1462156, 1402145, 1402151, 1052461, 1052457, 1532073, 1532074, 1472177, 1472179, 1332186, 1332193, 1322154, 1322162, 1442173, 1442182, 1522068, 1522071, 1452165, 1312114, 1312116, 1382160, 1132154, 1132151, 1072666, 1072660, 1212069, 1212068, 1492152, 1492138, 1372139, 1372131, 1222063, 1222064, 1082433, 1082430, 1362060, 1362067, 1432138, 1432135, 1152088, 1152089, 1003529, 1003552, 1422107, 1422105, 1232070, 1232063, 1302227, 1302212, 1113036, 1113035, 1112743, 1112742, 1482140, 1482138, 1242048, 1242075, 1412102, 1412014, 1102394, 1102441};
            ArrayList<Integer> list = new ArrayList();
            for (int i : set) {
                list.add(i);
            }
            return list;
        }

        public static ArrayList<Integer> get10YearSet() {
            int[] set = {
                1004172,//帽子
                1012471,//臉飾
                1052758,//套服
                1102691,//披風
                1122280,//墜飾
                1212095,//魔法克魯
                1222089,//靈魂射手
                1232089,//魔劍
                1242095,//能量劍
                1302304,//單手劍
                1312179,//單手斧
                1322230,//單手棍
                1332254,//短劍
                1342094,//雙刀
                1362115,//手杖
                1372201,//短杖
                1382239,//长杖
                1402229,//雙手劍
                1412158,//雙手斧
                1422165,//雙手棍
                1432194,//槍
                1442248,//矛
                1452232,//弓
                1462219,//弩
                1472241,//拳套
                1482196,//指虎
                1492205,//火槍
                1522118,//雙弩槍
                1532124,//加農炮
            };
            ArrayList<Integer> list = new ArrayList();
            for (int i : set) {
                list.add(i);
            }
            return list;
        }
    }

    public static class 方塊 {

        public static boolean canUseCube(Equip eq, int cubeId) {
            switch (cubeId) {
                case 2711007://10週年武器專用方塊
                    if (套裝.get10YearSet().contains(eq.getItemId()) && 類型.武器(eq.getItemId())) {
                        return true;
                    }
                    return false;
                case 5062100://楓葉奇幻方塊(罕見)
                    if (套裝.get7YearSet().contains(eq.getItemId()) && eq.getState(false) < 20) {
                        return true;
                    }
                    return false;
                case 5062102://[6週年]奇幻方塊
                    if (套裝.get6YearSet().contains(eq.getItemId()) && 類型.武器(eq.getItemId())) {
                        return true;
                    }
                    return false;
                case 5062103://梦幻的神奇魔方
                    if (套裝.get8YearSet().contains(eq.getItemId())) {
                        return true;
                    }
                    return false;
                case 2711000://可疑的方塊
                case 2711001://奇怪的方塊
                    if (eq.getState(false) < 18) {
                        return true;
                    }
                    return false;
                case 2710000://奇怪的方塊(罕見)
                case 2711005://工匠的方塊
                case 5062000://奇幻方塊
                case 5062004://星星方塊
                    if (eq.getState(false) < 20) {
                        return true;
                    }
                    return false;
                default:
                    return true;
            }
        }

        public enum CubeType {

            特殊(0x1),
            稀有(0x2),
            罕見(0x4),
            傳說(0x8),
            等級下降(0x10),
            調整潛能條數(0x20),
            洗後無法交易(0x40),
            對等(0x80),
            去掉無用潛能(0x100),
            前兩條相同(0x200),
            附加潛能(0x400),
            點商光環(0x800),
            ;
            private final int value;

            private CubeType(int value) {
                this.value = value;
            }

            public final int getValue() {
                return value;
            }

            public final boolean check(int flag) {
                return (flag & value) == value;
            }
        }

        public static int getCubeType(int itemId) {
            int type = CubeType.特殊.getValue() | CubeType.稀有.getValue() | CubeType.罕見.getValue() | CubeType.傳說.getValue();
            switch (itemId) {
                case 2711000://可疑的方塊(稀有)
                case 2711001://奇怪的方塊(傳說,說明上是傳說,實際只能洗到稀有)
                    type -= CubeType.罕見.getValue();
                case 2710000://奇怪的方塊(罕見)
                    type -= CubeType.傳說.getValue();
                    type |= CubeType.等級下降.getValue();
                    break;
                case 2710001://情谊魔方(洗后装备不可交换)
                    type -= CubeType.傳說.getValue();
                case 3994895://楓方塊
                    type |= CubeType.洗後無法交易.getValue();
                    break;
                case 2711005://工匠的方塊
                case 2711007://10週年武器專用方塊
                case 5062000://奇幻方塊
                    type -= CubeType.傳說.getValue();
                    break;
                case 5062001://超級奇幻方塊
                    type -= CubeType.傳說.getValue();
                    type |= CubeType.調整潛能條數.getValue();
                    break;
                case 5062004://星星方塊
                    type -= CubeType.傳說.getValue();
                    type |= CubeType.去掉無用潛能.getValue();
                    break;
                case 5062013://太陽方塊
                    type |= CubeType.去掉無用潛能.getValue();
                case 5062005://驚奇方塊
                case 5062006://白金奇幻方塊
                case 5062021://新對等方塊
                    type |= CubeType.對等.getValue();
                    break;
                case 5062008://鏡射方塊
                case 5062019://閃耀鏡射方塊
                    type |= CubeType.前兩條相同.getValue();
                    break;
                case 5062500://大師附加奇幻方塊
                case 5062501://[MS特價] 大師附加奇幻方塊
                    type |= CubeType.附加潛能.getValue();
                    break;
                case 2711006://名匠的方塊
                case 5062002://傳說方塊
                case 5062009://紅色方塊
                case 5062010://黑色方塊
                case 5062017://閃耀方塊
                case 5062020://閃炫方塊
                case 5062090://記憶方塊
                case 5062100://楓葉奇幻方塊
                case 5062102://[6週年]奇幻方塊
                case 5062103://奇異奇幻方塊
                default:
                    break;
            }
            if (MapleItemInformationProvider.getInstance().isCash(itemId)) {
                type |= CubeType.點商光環.getValue();
            }
            return type;
        }

        public static boolean potentialIDFits(final int potentialID, final int newstate, final int i) {
            //first line is always the best
            //but, sometimes it is possible to get second/third line as well
            //may seem like big chance, but it's not as it grabs random potential ID anyway
            if (newstate == 20) {
                return (i == 1 || Randomizer.nextInt(20) == 0 ? potentialID >= 40000 : potentialID >= 30000 && potentialID < 60004); // xml say so
            } else if (newstate == 19) {
                return (i == 1 || Randomizer.nextInt(20) == 0 ? potentialID >= 30000 && potentialID < 40000 : potentialID >= 20000 && potentialID < 30000);
            } else if (newstate == 18) {
                return (i == 1 || Randomizer.nextInt(20) == 0 ? potentialID >= 20000 && potentialID < 30000 : potentialID >= 10000 && potentialID < 20000);
            } else if (newstate == 17) {
                return (i == 1 || Randomizer.nextInt(20) == 0 ? potentialID >= 10000 && potentialID < 20000 : potentialID < 10000);
            } else {
                return false;
            }
        }

        public static boolean optionTypeFits(final int optionType, final int itemId) {
            switch (optionType) {
                case 10: // 武器、盾牌、副手和能源
                    return 類型.武器(itemId) || 類型.副手(itemId) || 類型.能源(itemId);
                case 11: // 除了武器的全部裝備
                    return !類型.武器(itemId);
                case 20: // 除了配飾和武器的全部裝備
                    return !類型.飾品(itemId) && !類型.武器(itemId);
                case 40: // 配飾
                    return 類型.飾品(itemId);
                case 51: // 帽子
                    return 類型.帽子(itemId);
                case 52: // 披風
                    return 類型.披風(itemId);
                case 53: // 上衣、褲子與套服
                    return 類型.上衣(itemId) || 類型.套服(itemId) || 類型.褲裙(itemId);
                case 54: // 手套
                    return 類型.手套(itemId);
                case 55: // 鞋子
                    return 類型.鞋子(itemId);
                default:
                    return true;
            }
        }

        public static boolean isAllowedPotentialStat(Equip eqp, int opID, boolean bonus, boolean cash) { //For now
            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            boolean superPot = ZZMSConfig.superiorPotential && ii.isSuperiorEquip(eqp.getItemId()) && Randomizer.nextInt(100) < 15;
            //判斷潛能是主潛還是附潛
            int type = opID / 1000 % 10;
            if ((bonus && ((!superPot && type != 2) || (superPot && type >= 1))) || (!bonus && type == 2)) {
                return false;
            }
            //點商光環清除罕見以上潛能的非常的垃圾純數字潛能
            if ((opID % 1000 <= 14 || opID % 1000 == 81) && opID < 60000 && cash) {
                return false;
            }

            int state = opID % 1000;
            return superPot && !bonus ? (state != 4 && state != 9 && state != 24 && (state < 13 || state > 18)) : opID < 60000;
        }

        public static int getCubeFragment(int itemId) {
            switch (itemId) {
                case 5062000://奇幻方塊
                    return 2430112;
                case 5062002://傳說方塊
                    return 2430481;
                case 5062004://星星方塊
                    return 2432114;
                case 5062005://驚奇方塊
                    return 2430759;
                case 5062006://白金奇幻方塊
                    return 2431427;
                case 5062009://紅色方塊
                    return 2431893;
                case 5062010://黑色方塊
                    return 2431894;
                case 5062013://太陽方塊
                    return 2432115;
                case 5062090://記憶方塊
                    return 2431445;
                case 5062100://枫叶魔方
                    return 2430112;
                case 5062102://[7周年]神奇魔方
                    return 2430112;
                case 5062103://奇異奇幻方塊
                    return 2430112;
                case 5062500://大師附加奇幻方塊
                    return 2430915;
                default:
                    return 0;
            }
        }

        public static boolean canLockCube(int itemId) {
            switch (itemId) {
                case 5062000://奇幻方塊
                case 5062004://星星方塊
                case 5062006://白金奇幻方塊
                case 5062013://太陽方塊
                    return true;
                default:
                    return false;
            }
        }

        public static long getMapleCubeCost(int times, int potentialState) {
            potentialState -= 1;
            if (potentialState < 0) {
                return 100;
            }
            long cost = 0;
            long[] mapleCubeCostPlus = {100, 10000, 500000, 20000000};
            long[] mapleCubeCostInitial = {100, 100000, 1000000, 10000000};
            long[] mapleCubeCostMax = {15000, 47400000, 5113000000L, 9999999999L};
            if (times >= 50) {
                cost = mapleCubeCostMax[potentialState];
            } else {
                for (int i = 1; i <= times; i++) {
                    long plus = 1;
                    for (int j = 0; j < i / (potentialState == 0 ? 10 : 5); j++) {
                        if (potentialState == 0) {
                            plus += 1;
                        } else if (potentialState == 1) {
                            plus *= 2;
                        } else if (potentialState == 2) {
                            plus *= 2 + (j == 0 ? 2 : 0);
                        } else if (potentialState == 3) {
                            plus *= 2 + (j == 3 ? 1 : 0);
                        }
                    }
                    cost += mapleCubeCostPlus[potentialState] * plus;
                }
            }
            cost += mapleCubeCostInitial[potentialState];
            cost = cost > mapleCubeCostMax[potentialState] ? mapleCubeCostMax[potentialState] : cost;
            return cost;
        }

        public static boolean isUselessPotential(StructItemOption pot) {
            boolean useless = false;
            for (String s : pot.getItemOption()) {
                if (pot.get(s) > 0) {
                    switch (s) {
                        case "incSTRr":
                        case "incDEXr":
                        case "incINTr":
                        case "incLUKr":
                        case "incMHPr":
                        case "incPADr":
                        case "incMADr":
                        case "incCriticaldamageMin":
                        case "incCriticaldamageMax":
                        case "incDAMr":
                        case "incTerR":
                        case "incAsrR":
                        case "ignoreTargetDEF":
                        case "incMaxDamage":
                        case "reduceCooltime":
                        case "boss":
                        case "incMesoProp":
                        case "incRewardProp":
                        case "level":
                        case "attackType":
                            break;
                        default:
                            useless = true;
                    }
                }
            }
            return useless;
        }
    }

    public static class 類型 {

        //<editor-fold defaultstate="collapsed" desc="道具一級分類">
        public static boolean 帽子(int itemid) {
            return itemid / 10000 == 100;
        }

        public static boolean 臉飾(int itemid) {
            return itemid / 10000 == 101;
        }

        public static boolean 眼飾(int itemid) {
            return itemid / 10000 == 102;
        }

        public static boolean 耳環(int itemid) {
            return itemid / 10000 == 103;
        }

        public static boolean 上衣(int itemid) {
            return itemid / 10000 == 104;
        }

        public static boolean 套服(int itemId) {
            return itemId / 10000 == 105;
        }

        public static boolean 褲裙(int itemid) {
            return itemid / 10000 == 106;
        }

        public static boolean 鞋子(int itemid) {
            return itemid / 10000 == 107;
        }

        public static boolean 手套(int itemid) {
            return itemid / 10000 == 108;
        }

        public static boolean 盾牌(int itemid) {
            return itemid / 10000 == 109;
        }

        public static boolean 披風(int itemid) {
            return itemid / 10000 == 110;
        }

        public static boolean 戒指(int itemid) {
            return itemid / 10000 == 111;
        }

        public static boolean 墜飾(int itemid) {
            return itemid / 10000 == 112;
        }

        public static boolean 腰帶(int itemid) {
            return itemid / 10000 == 113;
        }

        public static boolean 勳章(int itemid) {
            return itemid / 10000 == 114;
        }

        public static boolean 肩飾(int itemid) {
            return itemid / 10000 == 115;
        }

        public static boolean 口袋道具(int itemid) {
            return itemid / 10000 == 116;
        }

        public static boolean 胸章(int itemId) {
            return itemId / 10000 == 118;
        }

        public static boolean 能源(final int itemid) {
            return itemid / 10000 == 119;
        }

        public static boolean 圖騰(final int itemid) {
            return itemid / 10000 == 120;
        }

        public static boolean 閃亮克魯(final int itemid) {
            return itemid / 10000 == 121;
        }

        public static boolean 靈魂射手(final int itemid) {
            return itemid / 10000 == 122;
        }

        public static boolean 魔劍(final int itemid) {
            return itemid / 10000 == 123;
        }

        public static boolean 能量劍(final int itemid) {
            return itemid / 10000 == 124;
        }

        public static boolean 幻獸棍棒(final int itemid) {
            return itemid / 10000 == 125;
        }

        public static boolean ESP限幅器(final int itemid) {
            return itemid / 10000 == 126;
        }

        public static boolean 單手劍(final int itemid) {
            return itemid / 10000 == 130;
        }

        public static boolean 單手斧(final int itemid) {
            return itemid / 10000 == 131;
        }

        public static boolean 單手棍(final int itemid) {
            return itemid / 10000 == 132;
        }

        public static boolean 短劍(final int itemid) {
            return itemid / 10000 == 133;
        }

        public static boolean 雙刀(final int itemid) {
            return itemid / 10000 == 134;
        }

        public static boolean 特殊副手(final int itemid) {
            return itemid / 10000 == 135;
        }

        public static boolean 手杖(final int itemid) {
            return itemid / 10000 == 136;
        }

        public static boolean 短杖(final int itemid) {
            return itemid / 10000 == 137;
        }

        public static boolean 長杖(final int itemid) {
            return itemid / 10000 == 138;
        }

        public static boolean 雙手劍(final int itemid) {
            return itemid / 10000 == 140;
        }

        public static boolean 雙手斧(final int itemid) {
            return itemid / 10000 == 141;
        }

        public static boolean 雙手棍(final int itemid) {
            return itemid / 10000 == 142;
        }

        public static boolean 槍(final int itemid) {
            return itemid / 10000 == 143;
        }

        public static boolean 矛(final int itemid) {
            return itemid / 10000 == 144;
        }

        public static boolean 弓(final int itemid) {
            return itemid / 10000 == 145;
        }

        public static boolean 弩(final int itemid) {
            return itemid / 10000 == 146;
        }

        public static boolean 拳套(final int itemid) {
            return itemid / 10000 == 147;
        }

        public static boolean 指虎(final int itemid) {
            return itemid / 10000 == 148;
        }

        public static boolean 火槍(final int itemid) {
            return itemid / 10000 == 149;
        }

        public static boolean 雙弩槍(final int itemid) {
            return itemid / 10000 == 152;
        }

        public static boolean 加農炮(final int itemid) {
            return itemid / 10000 == 153;
        }

        public static boolean 太刀(final int itemid) {
            return itemid / 10000 == 154;
        }

        public static boolean 扇子(final int itemid) {
            return itemid / 10000 == 155;
        }

        public static boolean 琉(final int itemid) {
            return itemid / 10000 == 156;
        }

        public static boolean 璃(final int itemid) {
            return itemid / 10000 == 157;
        }

        public static boolean 戰神引擎(int itemid) {
            return itemid / 10000 == 161;
        }

        public static boolean 戰神手臂(int itemid) {
            return itemid / 10000 == 162;
        }

        public static boolean 戰神腿部(int itemid) {
            return itemid / 10000 == 163;
        }

        public static boolean 戰神身軀(int itemid) {
            return itemid / 10000 == 164;
        }

        public static boolean 戰神電晶體(int itemid) {
            return itemid / 10000 == 165;
        }

        public static boolean 機器人(int itemid) {
            return itemid / 10000 == 166;
        }

        public static boolean 機器人心臟(int itemId) {
            return itemId / 10000 == 167;
        }

        public static boolean 寵物裝備(int itemid) {
            return itemid / 10000 >= 180 && itemid / 10000 <= 183;
        }

        public static boolean 騎寵(int itemid) {
            return itemid / 10000 == 190 || itemid / 10000 == 193;
        }

        public static boolean 馬鞍(int itemid) {
            return itemid / 10000 == 191;
        }

        public static boolean 龍面具(int itemid) {
            return itemid / 10000 == 194;
        }

        public static boolean 龍墜飾(int itemid) {
            return itemid / 10000 == 195;
        }

        public static boolean 龍之翼(int itemid) {
            return itemid / 10000 == 196;
        }

        public static boolean 龍尾巴(int itemid) {
            return itemid / 10000 == 197;
        }

        public static boolean 飛鏢(int itemid) {
            return itemid / 10000 == 207;
        }

        public static boolean 子彈(int itemid) {
            return itemid / 10000 == 233;
        }

        public static boolean 寵物(int id) {
            return id / 10000 == 500;
        }

        //</editor-fold>

        public static boolean 防具(int itemid) {
            return 帽子(itemid) || 上衣(itemid) || 套服(itemid) || 褲裙(itemid) || 鞋子(itemid) || 手套(itemid) || 披風(itemid);
        }

        public static boolean 飾品(int itemid) {
            return 臉飾(itemid) || 眼飾(itemid) || 耳環(itemid) || 戒指(itemid) || 墜飾(itemid) || 腰帶(itemid) || 勳章(itemid) || 肩飾(itemid) || 口袋道具(itemid) || 胸章(itemid) || 能源(itemid) || 圖騰(itemid);
        }

        public static boolean 副手(int itemid) {
            return 盾牌(itemid) || 雙刀(itemid) || 特殊副手(itemid);
        }

        public static boolean 武器(int itemid) {
            return 閃亮克魯(itemid)
                    || 靈魂射手(itemid)
                    || 魔劍(itemid)
                    || 能量劍(itemid)
                    || 幻獸棍棒(itemid)
                    || 單手劍(itemid)
                    || 單手斧(itemid)
                    || 單手棍(itemid)
                    || 短劍(itemid)
                    || 手杖(itemid)
                    || 短杖(itemid)
                    || 長杖(itemid)
                    || 雙手劍(itemid)
                    || 雙手斧(itemid)
                    || 雙手棍(itemid)
                    || 槍(itemid)
                    || 矛(itemid)
                    || 弓(itemid)
                    || 弩(itemid)
                    || 拳套(itemid)
                    || 指虎(itemid)
                    || 火槍(itemid)
                    || 雙弩槍(itemid)
                    || 加農炮(itemid)
                    || 太刀(itemid)
                    || 扇子(itemid)
                    || 琉(itemid)
                    || 璃(itemid);
        }

        public static boolean 機械(final int itemid) {
            return 戰神引擎(itemid) || 戰神手臂(itemid) || 戰神腿部(itemid) || 戰神身軀(itemid) || 戰神電晶體(itemid);
        }

        public static boolean 龍裝備(final int itemid) {
            return 龍面具(itemid) || 龍墜飾(itemid) || 龍之翼(itemid) || 龍尾巴(itemid);
        }

        public static boolean 可充值道具(int itemid) {
            return (飛鏢(itemid)) || (子彈(itemid));
        }

        public static boolean 單手武器(int itemid) {
            return 武器(itemid) && !雙手武器(itemid);
        }

        public static boolean 雙手武器(final int itemid) {
            return 雙手劍(itemid)
                    || 雙手斧(itemid)
                    || 雙手棍(itemid)
                    || 槍(itemid)
                    || 矛(itemid)
                    || 弓(itemid)
                    || 弩(itemid)
                    || 拳套(itemid)
                    || 指虎(itemid)
                    || 火槍(itemid)
                    || 雙弩槍(itemid)
                    || 加農炮(itemid)
                    || 太刀(itemid)
                    || 扇子(itemid)
                    || 琉(itemid)
                    || 璃(itemid);
        }

        public static boolean 物理武器(int itemid) {
            return 武器(itemid) && !魔法武器(itemid);
        }

        public static boolean 魔法武器(int itemid) {
            return 短杖(itemid) || 長杖(itemid) || 扇子(itemid) || 幻獸棍棒(itemid) || ESP限幅器(itemid);
        }

        public static boolean 騎寵道具(int itemid) {
            return 騎寵(itemid) || 馬鞍(itemid);
        }

        public static boolean 裝備(int itemid) {
            return (itemid / 10000 >= 100) && (itemid / 10000 < 200);
        }

        public static boolean 消耗(int itemid) {
            return (itemid / 10000 >= 200) && (itemid / 10000 < 300);
        }

        public static boolean 裝飾(int itemid) {
            return (itemid / 10000 >= 300) && (itemid / 10000 < 400);
        }

        public static boolean 其他(int itemid) {
            return (itemid / 10000 >= 400) && (itemid / 10000 < 500);
        }

        public static boolean 特殊(int itemid) {
            return itemid / 1000 >= 500;
        }

        public static boolean 友誼戒指(int itemid) {
            switch (itemid) {
                case 1112800:
                case 1112801:
                case 1112802:
                case 1112810:
                case 1112811:
                case 1112816:
                case 1112817:
                    return true;
                case 1112803:
                case 1112804:
                case 1112805:
                case 1112806:
                case 1112807:
                case 1112808:
                case 1112809:
                case 1112812:
                case 1112813:
                case 1112814:
                case 1112815:
            }
            return false;
        }

        public static boolean 戀人戒指(int itemid) {
            switch (itemid) {
                case 1112001:
                case 1112002:
                case 1112003:
                case 1112005:
                case 1112006:
                case 1112007:
                case 1112012:
                case 1112015:
                case 1048000:
                case 1048001:
                case 1048002:
                    return true;
                case 1112004:
                case 1112008:
                case 1112009:
                case 1112010:
                case 1112011:
                case 1112013:
                case 1112014:
            }
            return false;
        }

        public static boolean 結婚戒指(int itemid) {
            switch (itemid) {
                case 1112300:
                case 1112301:
                case 1112302:
                case 1112303:
                case 1112304:
                case 1112305:
                case 1112306:
                case 1112307:
                case 1112308:
                case 1112309:
                case 1112310:
                case 1112311:
                case 1112315:
                case 1112316:
                case 1112317:
                case 1112318:
                case 1112319:
                case 1112320:
                case 1112803:
                case 1112806:
                case 1112807:
                case 1112808:
                case 1112809:
                    return true;
            }
            return false;
        }

        public static boolean 特效戒指(int itemid) {
            return 友誼戒指(itemid) || 戀人戒指(itemid) || 結婚戒指(itemid);
        }

        public static boolean 管理員裝備(final int itemid) {
            switch (itemid) {
                case 1002140://維澤特帽
                case 1003142://維澤特帽
                case 1003274://維澤特帽
                case 1042003://維澤特西裝
                case 1042223://維澤特西裝
                case 1062007://維澤特西褲
                case 1062140://維澤特西褲
                case 1322013://維澤特手提包
                case 1322106://維澤特手提包
                case 1002959:
                    return true;
            }
            return false;
        }




        public static boolean 城鎮傳送卷軸(final int itemid) {
            return itemid >= 2030000 && itemid < 2040000;
        }

        public static boolean 普通升級卷軸(int itemid) {
            return itemid >= 2040000 && itemid <= 2048100 && !阿斯旺卷軸(itemid);
        }

        public static boolean 阿斯旺卷軸(int itemid) {
            //return MapleItemInformationProvider.getInstance().getEquipStats(scroll.getItemId()).containsKey("tuc");
            //should add this ^ too.
            return itemid >= 2046060 && itemid <= 2046069 || itemid >= 2046141 && itemid <= 2046145 || itemid >= 2046519 && itemid <= 2046530 || itemid >= 2046701 && itemid <= 2046712;
        }

        public static boolean 提升卷(int itemid) { // 龍騎士獲得的強化牌板
            return itemid >=  2047000 && itemid < 2047310;
        }

        public static boolean 附加潛能卷軸(int itemid) {
            return itemid / 100 == 20483 && !(itemid >= 2048200 && itemid <= 2048304);
        }

        public static boolean 白醫卷軸(int itemid) {
            return itemid / 100 == 20490;
        }

        public static boolean 混沌卷軸(int itemid) {
            if (itemid >= 2049105 && itemid <= 2049110) {
                return false;
            }
            return itemid / 100 == 20491 || itemid == 2040126;
        }

        public static boolean 樂觀混沌卷軸(int itemid) {
            if (!混沌卷軸(itemid)) {
                return false;
            }
            switch (itemid) {
                case 2049122://樂觀的混卷軸50%
                case 2049129://樂觀的混卷軸 50%
                case 2049130://樂觀的混卷軸 30%
                case 2049131://樂觀的混卷軸 20%
                case 2049135://驚訝樂觀的混卷軸 20%
                case 2049136://驚訝樂觀的混卷軸 20%
                case 2049137://驚訝樂觀的混卷軸 40%
                case 2049141://珠寶戒指的樂觀的混卷軸 30%
                case 2049155://珠寶工藝樂觀的混卷軸 30%
                case 2049153://驚訝樂觀的混卷軸
                    return true;
            }
            return false;
        }

        public static boolean 飾品卷軸(int itemid) {
            return itemid / 100 == 20492;
        }

        public static boolean 裝備強化卷軸(int itemid) {
            return itemid / 100 == 20493;
        }

        public static boolean 鐵鎚(int itemid) {
            return itemid / 10000 == 247;
        }

        public static boolean 潛能卷軸(int itemid) {
            return itemid / 100 == 20494 || itemid / 100 == 20497 || itemid == 5534000;
        }

        public static boolean 回真卷軸(int itemid) {
            switch (itemid) {
                case 5064200://完美回真卡
                case 5064201://星光回真卷軸
                    return true;
                default:
                    return itemid / 100 == 20496;
            }
        }

        public static boolean 幸運日卷軸(int itemid) {
            switch (itemid) {
                case 5063100://幸運保護券(防爆+幸運)
                case 5068000://寵物專用幸運日卷軸
                    return true;
                default:
                    return itemid / 1000 == 2530;
            }
        }

        public static boolean 保護卷軸(int itemid) {
            switch (itemid) {
                case 5063100://幸運保護券(防爆+幸運)
                case 5064000://裝備保護卷軸(無法用於尊貴或12星以上)
                case 5064002://星光裝備保護卷軸(105以下的裝備且無法用於尊貴或12星以上)
                case 5064003://尊貴裝備保護卷軸(無法用於非尊貴以及尊貴7星以上)
                case 5064004://[MS特價] 裝備保護卷軸(無法用於尊貴或12星以上)
                    return true;
                default:
                    return itemid / 1000 == 2531;
            }
        }

        public static boolean 安全卷軸(int itemid) {
            switch (itemid) {
                case 5064100://安全盾牌卷軸
                case 5064101://星光安全盾牌卷軸(105以下的裝備)
                case 5068100://寵物安全盾牌卷軸
                    return true;
                default:
                    return itemid / 1000 == 2532;
            }
        }

        public static boolean 卷軸保護卡(int itemid) {
            switch (itemid) {
                case 5064300://卷軸保護卡
                case 5064301://星光卷軸保護卡(105以下的裝備)
                case 5068200://寵物卷軸保護卡
                    return true;
            }
            return false;
        }

        public static boolean 靈魂卷軸_附魔器(int itemid) {
            return itemid / 1000 == 2590;
        }

        public static boolean 靈魂寶珠(int itemid) {
            return itemid / 1000 == 2591;
        }

        public static boolean TMS特殊卷軸(int itemid) {
            return itemid / 10000 == 261;
        }

        public static boolean 特殊卷軸(final int itemid) {
            return 幸運日卷軸(itemid) || 保護卷軸(itemid) || 安全卷軸(itemid) || 卷軸保護卡(itemid);
        }

        public static boolean 特殊潛能道具(final int itemid) {
            if (itemid / 100 == 10121 && itemid % 100 >= 64 && itemid % 100 <= 74 && itemid % 100 != 65 && itemid % 100 != 66) {//恰吉
                return true;
            } else if (itemid / 10 == 112212 && (itemid % 10 >= 2 && itemid % 10 <= 6)) {//真. 楓葉之心
                return true;
            } else if (itemid >= 1122224 && itemid <= 1122245) {//心之項鍊
                return true;
            } else if (itemid / 10 == 101244) {//卡爾頓的鬍子
                return true;
            }
            return false;
        }

        public static boolean 無法潛能道具(final int itemid) {
            return false;
        }
    }

    public static MapleWeaponType 武器類型(final int itemid) {
        if (類型.閃亮克魯(itemid)) {
            return MapleWeaponType.閃亮克魯;
        }
        if (類型.靈魂射手(itemid)) {
            return MapleWeaponType.靈魂射手;
        }
        if (類型.魔劍(itemid)) {
            return MapleWeaponType.魔劍;
        }
        if (類型.能量劍(itemid)) {
            return MapleWeaponType.能量劍;
        }
        if (類型.幻獸棍棒(itemid)) {
            return MapleWeaponType.幻獸棍棒;
        }
        if (類型.單手劍(itemid)) {
            return MapleWeaponType.單手劍;
        }
        if (類型.單手斧(itemid)) {
            return MapleWeaponType.單手斧;
        }
        if (類型.單手棍(itemid)) {
            return MapleWeaponType.單手棍;
        }
        if (類型.短劍(itemid)) {
            return MapleWeaponType.短劍;
        }
        if (類型.雙刀(itemid)) {
            return MapleWeaponType.雙刀;
        }
        if (類型.手杖(itemid)) {
            return MapleWeaponType.手杖;
        }
        if (類型.短杖(itemid)) {
            return MapleWeaponType.短杖;
        }
        if (類型.長杖(itemid)) {
            return MapleWeaponType.長杖;
        }
        if (類型.雙手劍(itemid)) {
            return MapleWeaponType.雙手劍;
        }
        if (類型.雙手斧(itemid)) {
            return MapleWeaponType.雙手斧;
        }
        if (類型.雙手棍(itemid)) {
            return MapleWeaponType.雙手棍;
        }
        if (類型.槍(itemid)) {
            return MapleWeaponType.槍;
        }
        if (類型.矛(itemid)) {
            return MapleWeaponType.矛;
        }
        if (類型.弓(itemid)) {
            return MapleWeaponType.弓;
        }
        if (類型.弩(itemid)) {
            return MapleWeaponType.弩;
        }
        if (類型.拳套(itemid)) {
            return MapleWeaponType.拳套;
        }
        if (類型.指虎(itemid)) {
            return MapleWeaponType.指虎; 
       }
        if (類型.火槍(itemid)) {
            return MapleWeaponType.火槍;
        }
        if (類型.雙弩槍(itemid)) {
            return MapleWeaponType.雙弩槍;
        }
        if (類型.加農炮(itemid)) {
            return MapleWeaponType.加農炮;
        }
        if (類型.太刀(itemid)) {
            return MapleWeaponType.太刀;
        }
        if (類型.扇子(itemid)) {
            return MapleWeaponType.扇子;
        }
        if (類型.琉(itemid)) {
            return MapleWeaponType.琉;
        }
        if (類型.璃(itemid)) {
            return MapleWeaponType.璃;
        }
        return MapleWeaponType.未知;
    }

    public static byte gachaponRareItem(final int itemid) {
        switch (itemid) {
            case 2340000: // White Scroll
            case 2049100: // Chaos Scroll
            case 3010014: // Moon Star Chair
            case 3010043: // Halloween Brromstick
            case 3010073: // Giant Pink bean Cushion
            case 3010072: // Miwok Chief's Chair
            case 3010068: // Lotus Leaf Chair
            case 3010085: // Olivia's Chair
            case 3010118: // Musical Note Chair
            case 3010124: // Dunas Jet Char
            case 3010125: // Nibelung Battleship
            case 3010131: //chewing panda chair
            case 3010137: // Dragon lord Chair
            case 3010156: // Visitor Representative
            case 3010615: // Nao Resting
            case 3010592: //Black Bean Chair
            case 3010602: // Heart Cloud Chair
            case 3010670: // absolute Ring chair
            case 3010728: // ilove Maplestory
            case 1342033: // VIP Katara
            case 1372078: // VIP wand
            case 1382099: // Staff
            case 1402090: // Two handed Sword
            case 1412062: // Two Handed Axe
            case 1422063: // Two handed Blunt Weapon
            case 1432081: // Spear
            case 1442111: // Polearm
            case 1452106: // Bow
            case 1462091: // Crossbow
            case 1472117: // Claw
            case 1482079: // Knuckle
            case 1492079: // Gun
            case 1302147: // one sword
            case 1312062: // One handed Axe
            case 1322090: // One Handed Blunt Weapon
            case 1332120: // Dagger(LUK)
            case 1332125: // Dagger (STR)< end of VIP
            case 1102041: // Pink Adventure Cape
            case 1022082: // Spectrum Goog
            case 1072238: // Violet snow shoes
            case 5062002: // Super Miracle
            case 5062003: // Miracle
            case 5062005: // Miracle
            case 2040834: // Scroll for gloves for att 50%^
            case 1102042: // Purple adventure cape
                return 2;
            //1 = wedding msg o.o
        }
        return 0;
    }

    public static boolean isOverPoweredEquip(final MapleClient c, final int itemId, short slot) {
        Equip source = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
        return source.getAcc() > 1000
                || source.getAvoid() > 1000
                || source.getStr() > 500
                || source.getDex() > 500
                || source.getInt() > 500
                || source.getLuk() > 500
                || source.getEnhance() > 25
                || source.getHands() > 100
                || source.getHp() > 5000
                || source.getMp() > 5000
                || source.getJump() > 100
                || source.getSpeed() > 100
                || source.getMatk() > 700
                || source.getMdef() > 1500
                || source.getUpgradeSlots() > 32
                || source.getViciousHammer() > 1
                || source.getWatk() > 700
                || source.getWdef() > 1500 /*|| source.getLevel() > 32*/;
    }

    public static boolean isForGM(int itemid) {
        return (itemid >= 2049335 && itemid <= 2049349) || //強化捲軸
                itemid == 2430011 || //特務召喚
                itemid == 2430012 || //移除特務
                itemid == 2430124 || //GM測試
                itemid == 2002085;//GM的無敵飲料
    }

    public static boolean isMadeByGM(final MapleClient c, final int itemId, short slot) {
        Equip source = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
        MapleCharacter gm = c.getChannelServer().getPlayerStorage().getCharacterByName(source.getOwner());
        if (source.getOwner() == null || source.getOwner().isEmpty() || gm == null) {
            return false;
        }
        return gm.isStaff();
    }
    
    public static int getMaxDamageLimitBreak(int itemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        return ServerConfig.MAX_DAMAGE - (ii.getLimitBreak(itemId) > 0 ? ii.getLimitBreak(itemId) : 999999);
    }

    public static int getEffectItemID(int itemId) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Map<String, Integer> stats = ii.getEquipStats(itemId);
        if (stats.containsKey("effectItemID")) {
            return stats.get("effectItemID");
        }
        return 0;
    }
}
