package constants;

import server.ServerProperties;

public class ServerConfig {

    public static String SQL_IP = "127.0.0.1";
    public static String SQL_PORT = "3306";
    public static String SQL_DATABASE = "ZZMS";
    public static String SQL_USER = "root";
    public static String SQL_PASSWORD = "root";

    public static boolean ADMIN_ONLY = false;
    public static boolean LOG_PACKETS = false;
    public static boolean AUTO_REGISTER = false;
    public static String SERVER_NAME = "ZZMS";
    public static int USER_LIMIT = 1500;
    public static String IP = "127.0.0.1";
    public static boolean LOG_SHARK = false;
    public static int CHANNEL_MAX_CHAR_VIEW = 20;
    public static String EVENTS = ""/* + "AutomatedEvent,"*/ + "PinkZakumEntrance,PVP,CygnusBattle,ScarTarBattle,BossBalrog_EASY,BossBalrog_NORMAL,HorntailBattle,Nibergen,PinkBeanBattle,ZakumBattle,NamelessMagicMonster,Dunas,Dunas2,2095_tokyo,ZakumPQ,LudiPQ,KerningPQ,ProtectTylus,WitchTower_EASY,WitchTower_Med,WitchTower_Hard,Vergamot,ChaosHorntail,ChaosZakum,CoreBlaze,BossQuestEASY,BossQuestMed,BossQuestHARD,BossQuestHELL,BossQuestCHAOS,Ravana_EASY,Ravana_HARD,Ravana_MED,GuildQuest,Aufhaven,Dragonica,Rex,MonsterPark,KentaPQ,ArkariumBattle,AswanOffSeason,HillaBattle,The Dragon Shout,VonLeonBattle,Ghost,OrbisPQ,Romeo,Juliet,Pirate,Amoria,Ellin,CWKPQ,DollHouse,Kenta,Prison,Azwan,HenesysPQ,jett2ndjob,ATT_Wall_War,ATT_Hook_Shot,SimpleZakum,DarkHillaBattle,FairyBossBattle,BloodyBoss,BloodyJBoss";
    public static int MAX_DAMAGE = 50000000;
    
    /*Anti-Sniff*/
    public static boolean USE_FIXED_IV = false;
    public static final byte[] Static_LocalIV = new byte[]{0x47, 0x71, 0x1A, 0x2C};
    public static final byte[] Static_RemoteIV = new byte[]{0x46, 0x70, 0x19, 0x2B};

    public static enum Events {

        EVENT1("PinkZakumEntrance"),
        EVENT2("PVP"),
        EVENT3("CygnusBattle"),
        EVENT4("ScarTarBattle"),
        EVENT5("BossBalrog_EASY"),
        EVENT6("BossBalrog_NORMAL"),
        EVENT7("HorntailBattle"),
        EVENT8("Nibergen"),
        EVENT9("PinkBeanBattle"),
        EVENT10("ZakumBattle"),
        EVENT11("NamelessMagicMonster"),
        EVENT12("Dunas"),
        EVENT13("Dunas2"),
        EVENT14("2095_tokyo"),
        EVENT15("ZakumPQ"),
        EVENT16("LudiPQ"),
        EVENT17("KerningPQ"),
        EVENT18("ProtectTylus"),
        EVENT19("WitchTower_EASY"),
        EVENT20("WitchTower_Med"),
        EVENT21("WitchTower_Hard"),
        EVENT22("Vergamot"),
        EVENT23("ChaosHorntail"),
        EVENT24("ChaosZakum"),       
        EVENT25("CoreBlaze"),
        EVENT26("BossQuestEASY"),
        EVENT27("BossQuestMed"),
        EVENT28("BossQuestHARD"),
        EVENT29("BossQuestHELL"),
        EVENT30("Ravana_EASY"),
        EVENT31("Ravana_HARD"),
        EVENT32("Ravana_MED"),
        EVENT33("GuildQuest"),
        EVENT34("Aufhaven"),
        EVENT35("Dragonica"),
        EVENT36("Rex"),
        EVENT37("MonsterPark"),
        EVENT38("KentaPQ"),
        EVENT39("ArkariumBattle"),
        EVENT40("AswanOffSeason"),
        EVENT41("HillaBattle"),
        EVENT42("The Dragon Shout"),
        EVENT43("VonLeonBattle"),
        EVENT44("Ghost"),
        EVENT45("OrbisPQ"),
        EVENT46("Romeo"),
        EVENT47("Juliet"),
        EVENT48("Pirate"),
        EVENT49("Amoria"),
        EVENT50("Ellin"),
        EVENT51("CWKPQ"),
        EVENT52("DollHouse"),
        EVENT53("Kenta"),
        EVENT54("Prison"),
        EVENT55("Azwan"),
        EVENT56("ATT_Wall_War"),
        EVENT57("ATT_Hook_Shot"),
        EVENT58("SimpleZakum"),
        EVENT59("DarkHillaBattle"),
        EVENT60("FairyBossBattle"),
        EVENT61("BloodyBoss"),
        EVENT62("BloodyJBoss");
        private final String name;

        Events(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static String[] getEvents() {
        String[] eventlist = new String[Events.values().length];
        int arrayLocation = 0;
        for (Events event : Events.values()) {
            eventlist[arrayLocation] += event.getName();
            arrayLocation++;
        }
        return eventlist;
    }

    public static String getEventList() {
        String eventlist = new String();
        for (Events event : Events.values()) {
            eventlist += event.getName();
            eventlist += ", ";
        }
        eventlist += "@";
        eventlist = eventlist.replaceAll(", @", "");
        return eventlist;
    }

    public static boolean isAutoRegister() {
        return AUTO_REGISTER;
    }

    public static void loadSetting() {
        SQL_IP = ServerProperties.getProperty("SQL_IP", SQL_IP);
        SQL_PORT = ServerProperties.getProperty("SQL_PORT", SQL_PORT);
        SQL_DATABASE = ServerProperties.getProperty("SQL_DATABASE", SQL_DATABASE);
        SQL_USER = ServerProperties.getProperty("SQL_USER", SQL_USER);
        SQL_PASSWORD = ServerProperties.getProperty("SQL_PASSWORD", SQL_PASSWORD);

        ADMIN_ONLY = ServerProperties.getProperty("ADMIN_ONLY", ADMIN_ONLY);
        LOG_PACKETS = ServerProperties.getProperty("LOG_PACKETS", LOG_PACKETS);
        AUTO_REGISTER = ServerProperties.getProperty("AUTO_REGISTER", AUTO_REGISTER);
        SERVER_NAME = ServerProperties.getProperty("SERVER_NAME", SERVER_NAME);
        USER_LIMIT = ServerProperties.getProperty("USER_LIMIT", USER_LIMIT);
        IP = ServerProperties.getProperty("IP", IP);
        LOG_SHARK = ServerProperties.getProperty("LOG_SHARK", LOG_SHARK);
        CHANNEL_MAX_CHAR_VIEW = ServerProperties.getProperty("CHANNEL_MAX_CHAR_VIEW", CHANNEL_MAX_CHAR_VIEW);

        USE_FIXED_IV = ServerProperties.getProperty("USE_FIXED_IV", USE_FIXED_IV);
    }

    static {
        loadSetting();
    }
}
