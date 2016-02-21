package constants;

import java.net.InetAddress;
import java.util.Calendar;
import server.ServerProperties;
import tools.Triple;

public class ServerConstants {

    public static byte Class_Bonus_EXP(final int job) {
        switch (job) {
            case 501:
            case 530:
            case 531:
            case 532:
            case 2300:
            case 2310:
            case 2311:
            case 2312:
            case 3100:
            case 3110:
            case 3111:
            case 3112:
            case 11212:
            case 800:
            case 900:
            case 910:
                return 10;
        }
        return 0;
    }

    public static boolean getEventTime() {
        int time = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        switch (Calendar.DAY_OF_WEEK) {
            case 1:
                return time >= 1 && time <= 5;
            case 2:
                return time >= 4 && time <= 9;
            case 3:
                return time >= 7 && time <= 12;
            case 4:
                return time >= 10 && time <= 15;
            case 5:
                return time >= 13 && time <= 18;
            case 6:
                return time >= 16 && time <= 21;
        }
        return time >= 19 && time <= 24;
    }

    // GMS stuff
    public static boolean TESPIA = false;
    public static short MAPLE_VERSION = (short) 183;
    public static String MAPLE_PATCH = "2";
    public static MapleType MAPLE_TYPE = MapleType.台港澳;

    // Server stuff
    public static boolean USE_LOCALHOST = false;
    public static boolean REDIRECTOR = true; // 退出遊戲返回選角界面開關
    public static int SHARK_VERSION = 0x8225;
    public static boolean AntiKS = false;
    public static int MIRACLE_RATE = 1;
    public static byte SHOP_DISCOUNT = 0;
    public static boolean IS_BETA_FOR_ADMINS = false;//是否Beta版,若是,創建的角色都是伺服器管理員
    public static boolean FEVER_TIME = false; // Forever Time!! 咒語的痕跡用的
    public static Triple<String, Integer, Boolean>[] backgrounds = new Triple[]{ //boolean for randomize
        new Triple<>("20140430/0", 1, false),
        new Triple<>("20140326/0", 0, false),
        new Triple<>("20140326/1", 0, false)
    };

    public static final byte[] getGateway_IP() {
        try {
            final InetAddress inetAddr = InetAddress.getByName(ServerConfig.IP);
            byte[] addr = inetAddr.getAddress();
            return addr;
        } catch (Exception e) {
            return new byte[]{(byte) 127, (byte) 0, (byte) 0, (byte) 1};
        }
    }

    public static enum MapleType {

        UNKNOWN(-1, "UTF-8"),
        한국(1, "EUC_KR"),
        //2 - KMS測試機
        日本(3, "Shift_JIS"),
        中国(4, "GB18030"),
        //5 - 測試機
        台港澳(6, "BIG5-HKSCS"),
        SEA(7, "UTF-8"),
        GLOBAL(8, "UTF-8"),
        BRAZIL(9, "UTF-8");
        byte type;
        final String ascii;

        private MapleType(int type, String ascii) {
            this.type = (byte) type;
            this.ascii = ascii;
        }

        public byte getType() {
            if (!ServerConstants.TESPIA) {
                return type;
            }
            if (this == 한국) {
                return 2;//KMS測試機
            } else {
                return 5;//測試機
            }
        }

        public String getAscii() {
            return ascii;
        }

        public void setType(int type) {
            this.type = (byte) type;
        }

        public static MapleType getByType(byte type) {
            for (MapleType l : MapleType.values()) {
                if (l.getType() == type) {
                    return l;
                }
            }
            return UNKNOWN;
        }
    }

    public static void loadSetting() {
        TESPIA = ServerProperties.getProperty("TESPIA", TESPIA);
        MAPLE_VERSION = ServerProperties.getProperty("MAPLE_VERSION", MAPLE_VERSION);
        MAPLE_PATCH = ServerProperties.getProperty("MAPLE_PATCH", MAPLE_PATCH);
        MAPLE_TYPE = MapleType.getByType(ServerProperties.getProperty("MAPLE_TYPE", MAPLE_TYPE.getType()));

        USE_LOCALHOST = ServerProperties.getProperty("USE_LOCALHOST", USE_LOCALHOST);
        REDIRECTOR = ServerProperties.getProperty("REDIRECTOR", REDIRECTOR);
        SHARK_VERSION = ServerProperties.getProperty("SHARK_VERSION", SHARK_VERSION);
        MIRACLE_RATE = ServerProperties.getProperty("MIRACLE_RATE", MIRACLE_RATE);
        SHOP_DISCOUNT = ServerProperties.getProperty("SHOP_DISCOUNT", SHOP_DISCOUNT);
        IS_BETA_FOR_ADMINS = ServerProperties.getProperty("IS_BETA_FOR_ADMINS", IS_BETA_FOR_ADMINS);
        FEVER_TIME = ServerProperties.getProperty("FEVER_TIME", FEVER_TIME);
    }

    static {
        loadSetting();
    }
}
