package client;

import client.MapleTrait.MapleTraitType;
import client.anticheat.CheatTracker;
import client.anticheat.ReportType;
import client.inventory.*;
import client.inventory.MapleImp.ImpFlag;
import constants.*;
import database.DatabaseConnection;
import database.DatabaseException;
import handling.channel.ChannelServer;
import handling.login.LoginInformationProvider.JobType;
import handling.login.LoginServer;
import handling.world.*;
import handling.world.family.MapleFamily;
import handling.world.family.MapleFamilyBuff;
import handling.world.family.MapleFamilyCharacter;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildCharacter;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.sql.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import org.apache.mina.core.future.WriteFuture;
import scripting.EventInstanceManager;
import scripting.EventManager;
import scripting.MapScriptMethods.MapScriptType;
import scripting.NPCScriptManager;
import server.*;
import server.MapleStatEffect.CancelEffectAction;
import server.Timer.BuffTimer;
import server.Timer.MapTimer;
import server.Timer.WorldTimer;
import server.commands.PlayerGMRank;
import server.life.*;
import server.maps.*;
import server.movement.LifeMovementFragment;
import server.quest.MapleQuest;
import server.shops.MapleShop;
import server.shops.MapleShopFactory;
import server.shops.MapleShopItem;
import server.stores.IMaplePlayerShop;
import tools.*;
import tools.packet.*;
import tools.packet.CField.EffectPacket;
import tools.packet.CField.NPCPacket;
import tools.packet.CField.SummonPacket;
import tools.packet.CWvsContext.BuddylistPacket;
import tools.packet.CWvsContext.BuffPacket;
import tools.packet.CWvsContext.InfoPacket;
import tools.packet.CWvsContext.InventoryPacket;
import tools.packet.CWvsContext.Reward;
import tools.packet.JobPacket.AvengerPacket;
import tools.packet.JobPacket.LuminousPacket;
import tools.packet.JobPacket.PhantomPacket;
import tools.packet.JobPacket.XenonPacket;
import tools.packet.provider.SpecialEffectType;

public class MapleCharacter extends AnimatedMapleMapObject implements Serializable, MapleCharacterLook {

    private static final long serialVersionUID = 845748950829L;
    private String name, chalktext, BlessOfFairy_Origin, BlessOfEmpress_Origin, teleportname;
    private long lastCombo, lastfametime, keydown_skill, nextConsume, pqStartTime, lastDragonBloodTime,
            lastBerserkTime, lastRecoveryTime, lastSummonTime, mapChangeTime, lastFishingTime, lastFairyTime,
            lastHPTime, lastMPTime, lastFamiliarEffectTime, lastExceedTime, lastDOTTime, exp, meso;
    private byte gmLevel, gender, initialSpawnPoint, skinColor, guildrank = 5, allianceRank = 5,
            world, fairyExp, numClones, subcategory, cardStack, runningStack, runningBless = 0;
    private short level, job, mulung_energy, combo, force, availableCP, fatigue, totalCP, hpApUsed, scrolledPosition,
            kaiserCombo, xenonSurplus, exceed, exceedAttack = 0, soulcount = 0;
    private int accountid, id, hair, face, secondHair, secondFace, faceMarking, ears, tail, elf, mapid, fame, pvpExp, pvpPoints, totalWins, totalLosses,
            guildid = 0, fallcounter, maplepoints, acash, nxcredit, chair, itemEffect, points, vpoints, dpoints, epoints,
            rank = 1, rankMove = 0, jobRank = 1, jobRankMove = 0, marriageId, marriageItemId, dotHP,
            currentrep, totalrep, coconutteam, followid, battleshipHP, gachexp, challenge, guildContribution = 0,
            remainingAp, honourExp, honorLevel, runningLight, runningLightSlot, runningDark, runningDarkSlot, luminousState, touchedrune, weaponPoint;
    private byte lastWorld = -3;
    private Point old;
    private MonsterFamiliar summonedFamiliar;
    private int[] wishlist = new int[12], rocks, savedLocations, regrocks, hyperrocks, remainingSp = new int[10], remainingHSp = new int[3];
    private transient AtomicInteger inst, insd;
    private transient List<LifeMovementFragment> lastres;
    private List<Integer> lastmonthfameids, lastmonthbattleids, extendedSlots;
    private List<MapleDoor> doors;
    private List<MechDoor> mechDoors;
    private List<MaplePet> pets;
    private List<Item> rebuy;
    private MapleShop azwanShopList;
    private MapleImp[] imps;
    private List<Pair<Integer, Boolean>> stolenSkills = new ArrayList<>();
    private transient WeakReference<MapleCharacter>[] clones;
    private transient Set<MapleMonster> controlled;
    private Map<Integer, String> entered = new LinkedHashMap<>();
    private transient Set<MapleMapObject> visibleMapObjects;
    private transient ReentrantReadWriteLock visibleMapObjectsLock;
    private transient ReentrantReadWriteLock summonsLock;
    private transient ReentrantReadWriteLock controlledLock;
    private transient MapleAndroid android;
    private final Map<MapleQuest, MapleQuestStatus> quests;
    private Map<Integer, String> questinfo;
    private final Map<Skill, SkillEntry> skills;
    private transient Map<MapleBuffStat, MapleBuffStatValueHolder> effects;
    private transient List<SpecialBuffInfo> stack_effects;
    private final Map<String, String> CustomValues = new HashMap<>();
    private transient List<MapleSummon> summons;
    private transient Map<Integer, MapleCoolDownValueHolder> coolDowns;
    private transient Map<MapleDisease, MapleDiseaseValueHolder> diseases;
    private Map<ReportType, Integer> reports;
    private CashShop cs;
    private transient Deque<MapleCarnivalChallenge> pendingCarnivalRequests;
    private transient MapleCarnivalParty carnivalParty;
    private BuddyList buddylist;
    private MonsterBook monsterbook;
    private transient CheatTracker anticheat;
    private MapleClient client;
    private transient MapleParty party;
    private PlayerStats stats;
    private final MapleCharacterCards characterCard;
    private transient MapleMap map;
    private transient MapleShop shop;
    private transient MapleDragon dragon;
    private transient MapleHaku haku;
    private transient MapleExtractor extractor;
    private transient RockPaperScissors rps;
    private Map<Integer, MonsterFamiliar> familiars;
    private MapleStorage storage;
    private transient MapleTrade trade;
    private MapleMount mount;
    private int sp;
    private MapleMessenger messenger;
    private byte[] petStore;
    private transient IMaplePlayerShop playerShop;
    private boolean invincible, canTalk, clone, followinitiator, followon, smega, hasSummon;
    private MapleGuildCharacter mgc;
    private MapleFamilyCharacter mfc;
    private transient EventInstanceManager eventInstance;
    private final List<MapleCharacter> chars = new LinkedList<>(); //this is messy
    private final ReentrantReadWriteLock mutex = new ReentrantReadWriteLock();
    private final Lock rL = mutex.readLock(), wL = mutex.writeLock();
    private transient EventManager eventInstanceAzwan;
    private MapleInventory[] inventory;
    private SkillMacro[] skillMacros = new SkillMacro[5];
    private final EnumMap<MapleTraitType, MapleTrait> traits;
    private MapleKeyLayout keylayout;
    private transient ScheduledFuture<?> mapTimeLimitTask;
    private transient Event_PyramidSubway pyramidSubway = null;
    private transient List<Integer> pendingExpiration = null;
    private transient Map<Skill, SkillEntry> pendingSkills = null;
    private transient Map<Integer, Integer> linkMobs;
    private List<InnerSkillValueHolder> innerSkills;
    public boolean keyvalue_changed = false, innerskill_changed = true;
    private boolean changed_wishlist, changed_trocklocations, changed_regrocklocations, changed_hyperrocklocations, changed_skillmacros,
            changed_savedlocations, changed_questinfo, changed_skills, changed_reports, changed_extendedSlots, update_skillswipe;
    /*
     * Start of Custom Feature
     */
    private int reborns, apstorage;
    /*
     * End of Custom Feature
     */

    private int str;
    private int luk;
    private int int_;
    private int dex;
    private short chattype = 0;
    private int[] friendshippoints = new int[5];
    private int friendshiptoadd;
    private int wheelItem = 0;
    public transient static ScheduledFuture<?> XenonSupplyTask = null;
    private MapleCoreAura coreAura;
    private List<MaplePotionPot> potionPots;
    private int deathCount = 0;
    private MapleMarriage marriage;
    private boolean hiddenChatCanSee = false;
    private long lastMonsterCombo;
    private int monsterCombo;
    public List<Integer> killMonsterExps = new ArrayList();
    private long lastCheckProcess;
    private final List<MapleProcess> Process = new LinkedList();
    private boolean skipOnceChat = true;
    private Pair<MapScriptType, String> mapScript = new Pair<>(MapScriptType.UNK, "");
    public int 卡圖 = 0;

    private MapleCharacter(final boolean ChannelServer) {
        setStance(0);
        setPosition(new Point(0, 0));

        inventory = new MapleInventory[MapleInventoryType.values().length];
        for (MapleInventoryType type : MapleInventoryType.values()) {
            inventory[type.ordinal()] = new MapleInventory(type);
        }
        quests = new LinkedHashMap<>(); // Stupid erev quest.
        skills = new LinkedHashMap<>(); //Stupid UAs.
        stats = new PlayerStats();
        innerSkills = new LinkedList<>();
        azwanShopList = null;
        characterCard = new MapleCharacterCards();
        for (int i = 0; i < remainingSp.length; i++) {
            remainingSp[i] = 0;
        }
        for (int i = 0; i < remainingHSp.length; i++) {
            remainingHSp[i] = 0;
        }
        traits = new EnumMap<>(MapleTraitType.class);
        for (MapleTraitType t : MapleTraitType.values()) {
            traits.put(t, new MapleTrait(t));
        }
        if (ChannelServer) {
            changed_reports = false;
            changed_skills = false;
            changed_wishlist = false;
            changed_trocklocations = false;
            changed_regrocklocations = false;
            changed_hyperrocklocations = false;
            changed_skillmacros = false;
            changed_savedlocations = false;
            changed_extendedSlots = false;
            changed_questinfo = false;
            update_skillswipe = false;
            scrolledPosition = 0;
            lastCombo = 0;
            mulung_energy = 0;
            combo = 0;
            force = 0;
            keydown_skill = 0;
            nextConsume = 0;
            pqStartTime = 0;
            fairyExp = 0;
            cardStack = 0;
            runningStack = 1;
            mapChangeTime = 0;
            lastRecoveryTime = 0;
            lastDragonBloodTime = 0;
            lastBerserkTime = 0;
            lastFishingTime = 0;
            lastFairyTime = 0;
            lastHPTime = 0;
            lastMPTime = 0;
            lastFamiliarEffectTime = 0;
            old = new Point(0, 0);
            coconutteam = 0;
            followid = 0;
            battleshipHP = 0;
            marriageItemId = 0;
            marriage = null;
            fallcounter = 0;
            challenge = 0;
            dotHP = 0;
            lastSummonTime = 0;
            hasSummon = false;
            invincible = false;
            canTalk = true;
            clone = false;
            followinitiator = false;
            followon = false;
            rebuy = new ArrayList<>();
            linkMobs = new HashMap<>();
            reports = new EnumMap<>(ReportType.class);
            teleportname = "";
            smega = true;
            petStore = new byte[3];
            for (int i = 0; i < petStore.length; i++) {
                petStore[i] = (byte) -1;
            }
            wishlist = new int[30];
            rocks = new int[10];
            regrocks = new int[5];
            hyperrocks = new int[13];
            imps = new MapleImp[3];
            clones = new WeakReference[5]; //for now
            for (int i = 0; i < clones.length; i++) {
                clones[i] = new WeakReference<>(null);
            }
            familiars = new LinkedHashMap<>();
            extendedSlots = new ArrayList<>();
            effects = new ConcurrentEnumMap<>(MapleBuffStat.class);
            stack_effects = new ArrayList<>();
            coolDowns = new LinkedHashMap<>();
            diseases = new ConcurrentEnumMap<>(MapleDisease.class);
            inst = new AtomicInteger(0);// 1 = NPC/ Quest, 2 = Donald, 3 = Hired Merch store, 4 = Storage
            insd = new AtomicInteger(-1);
            keylayout = new MapleKeyLayout();
            doors = new ArrayList<>();
            mechDoors = new ArrayList<>();
            controlled = new LinkedHashSet<>();
            controlledLock = new ReentrantReadWriteLock();
            summons = new LinkedList<>();
            summonsLock = new ReentrantReadWriteLock();
            visibleMapObjects = new LinkedHashSet<>();
            visibleMapObjectsLock = new ReentrantReadWriteLock();
            pendingCarnivalRequests = new LinkedList<>();

            savedLocations = new int[SavedLocationType.values().length];
            for (int i = 0; i < SavedLocationType.values().length; i++) {
                savedLocations[i] = -1;
            }
            questinfo = new LinkedHashMap<>();
            pets = new ArrayList<>();
            friendshippoints = new int[5];
            coreAura = new MapleCoreAura(id, 24 * 60);
            potionPots = new ArrayList<>();
        }
    }

    public static MapleCharacter getDefault(final MapleClient client, final JobType type) {
        MapleCharacter ret = new MapleCharacter(false);
        ret.client = client;
        ret.map = null;
        ret.exp = 0;
        ret.gmLevel = (byte) (ServerConstants.IS_BETA_FOR_ADMINS ? PlayerGMRank.ADMIN.getLevel() : client.getGmLevel());
        if (ServerConstants.IS_BETA_FOR_ADMINS) {
            client.setGmLevel(ret.gmLevel);
        }
        ret.job = (short) type.id;
        ret.meso = 100000;
        ret.level = 1;
        ret.remainingAp = 0;
        ret.fame = 0;
        ret.accountid = client.getAccID();
        ret.buddylist = new BuddyList((byte) 50);

        ret.stats.str = 12;
        ret.stats.dex = 5;
        ret.stats.int_ = 4;
        ret.stats.luk = 4;
        ret.stats.maxhp = 50;
        ret.stats.hp = 50;
        ret.stats.maxmp = 5;
        ret.stats.mp = 5;
        ret.gachexp = 0;
        ret.friendshippoints = new int[]{0, 0, 0, 0, 0};
        ret.friendshiptoadd = 0;

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
            ps.setInt(1, ret.accountid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ret.client.setAccountName(rs.getString("name"));
                    ret.nxcredit = rs.getInt("nxCredit");
                    ret.acash = rs.getInt("ACash");
                    ret.maplepoints = rs.getInt("mPoints");
                    ret.points = rs.getInt("points");
                    ret.vpoints = rs.getInt("vpoints");
                    ret.epoints = rs.getInt("epoints");
                    ret.dpoints = rs.getInt("dpoints");
                }
            }
            ps.close();
        } catch (SQLException e) {
            System.err.println("Error getting character default" + e);
        }
        return ret;
    }

    public static MapleCharacter ReconstructChr(final CharacterTransfer ct, final MapleClient client, final boolean isChannel) {
        final MapleCharacter ret = new MapleCharacter(true); // Always true, it's change channel
        ret.client = client;
        if (!isChannel) {
            ret.client.setChannel(ct.channel);
        }
        ret.id = ct.characterid;
        ret.name = ct.name;
        ret.level = ct.level;
        ret.fame = ct.fame;

        ret.CRand = new PlayerRandomStream();

        ret.stats.str = ct.str;
        ret.stats.dex = ct.dex;
        ret.stats.int_ = ct.int_;
        ret.stats.luk = ct.luk;
        ret.stats.maxhp = ct.maxhp;
        ret.stats.maxmp = ct.maxmp;
        ret.stats.hp = ct.hp;
        ret.stats.mp = ct.mp;

        ret.characterCard.setCards(ct.cardsInfo);

        ret.chalktext = ct.chalkboard;
        ret.gmLevel = ct.gmLevel;
        ret.exp = ret.level >= ret.maxLevel ? 0 : ct.exp;
        ret.hpApUsed = ct.hpApUsed;
        ret.remainingSp = ct.remainingSp;
        ret.remainingHSp = ct.remainingHSp;
        ret.remainingAp = ct.remainingAp;
        ret.meso = ct.meso;
        ret.stolenSkills = ct.stolenSkills;
        ret.skinColor = ct.skinColor;
        ret.gender = ct.gender;
        ret.job = ct.job;
        ret.hair = ct.hair;
        ret.face = ct.face;
        ret.faceMarking = ct.faceMarking;
        ret.ears = ct.ears;
        ret.tail = ct.tail;
        ret.elf = ct.elf;
        ret.accountid = ct.accountid;
        ret.totalWins = ct.totalWins;
        ret.totalLosses = ct.totalLosses;
        ret.weaponPoint = ct.weaponPoint;
        client.setAccID(ct.accountid);
        ret.mapid = ct.mapid;
        ret.initialSpawnPoint = ct.initialSpawnPoint;
        ret.world = ct.world;
        ret.guildid = ct.guildid;
        ret.guildrank = ct.guildrank;
        ret.guildContribution = ct.guildContribution;
        ret.allianceRank = ct.alliancerank;
        ret.points = ct.points;
        ret.vpoints = ct.vpoints;
        ret.epoints = ct.epoints;
        ret.dpoints = ct.dpoints;
        ret.fairyExp = ct.fairyExp;
        ret.cardStack = ct.cardStack;
        ret.marriageId = ct.marriageId;
        ret.marriage = ct.marriage;
        ret.currentrep = ct.currentrep;
        ret.totalrep = ct.totalrep;
        ret.gachexp = ct.gachexp;
        ret.honourExp = ct.honourexp;
        ret.honorLevel = ct.honourlevel;
        ret.innerSkills = (LinkedList<InnerSkillValueHolder>) ct.innerSkills;
        ret.azwanShopList = (MapleShop) ct.azwanShopList;
        ret.pvpExp = ct.pvpExp;
        ret.pvpPoints = ct.pvpPoints;
        ret.soulcount = ct.soulcount;
        /*
         * Start of Custom Feature
         */
        ret.reborns = ct.reborns;
        ret.apstorage = ct.apstorage;
        /*
         * End of Custom Feature
         */
        ret.makeMFC(ct.familyid, ct.seniorid, ct.junior1, ct.junior2);
        if (ret.guildid > 0) {
            ret.mgc = new MapleGuildCharacter(ret);
        }
        ret.fatigue = ct.fatigue;
        ret.buddylist = new BuddyList(ct.buddysize);
        ret.subcategory = ct.subcategory;

        if (isChannel) {
            final MapleMapFactory mapFactory = ChannelServer.getInstance(client.getChannel()).getMapFactory();
            ret.map = mapFactory.getMap(ret.mapid);
            if (ret.map == null) { //char is on a map that doesn't exist warp it to spinel forest
                ret.map = mapFactory.getMap(950000100);
            } else {
                if (ret.map.getForcedReturnId() != 999999999 && ret.map.getForcedReturnMap() != null) {
                    ret.map = ret.map.getForcedReturnMap();
                    if (ret.map.getForcedReturnId() == 4000000) {
                        ret.initialSpawnPoint = 0;
                    }
                }
            }
            MaplePortal portal = ret.map.getPortal(ret.initialSpawnPoint);
            if (portal == null) {
                portal = ret.map.getPortal(0); // char is on a spawnpoint that doesn't exist - select the first spawnpoint instead
                ret.initialSpawnPoint = 0;
            }
            ret.setPosition(portal.getPosition());

            final int messengerid = ct.messengerid;
            if (messengerid > 0) {
                ret.messenger = World.Messenger.getMessenger(messengerid);
            }
        } else {

            ret.messenger = null;
        }
        int partyid = ct.partyid;
        if (partyid >= 0) {
            MapleParty party = World.Party.getParty(partyid);
            if (party != null && party.getMemberById(ret.id) != null) {
                ret.party = party;
            }
        }

        MapleQuestStatus queststatus_from;
        for (final Map.Entry<Integer, Object> qs : ct.Quest.entrySet()) {
            queststatus_from = (MapleQuestStatus) qs.getValue();
            queststatus_from.setQuest(qs.getKey());
            ret.quests.put(queststatus_from.getQuest(), queststatus_from);
        }
        for (final Map.Entry<Integer, SkillEntry> qs : ct.Skills.entrySet()) {
            ret.skills.put(SkillFactory.getSkill(qs.getKey()), qs.getValue());
        }
        for (Entry<MapleTraitType, Integer> t : ct.traits.entrySet()) {
            ret.traits.get(t.getKey()).setExp(t.getValue());
        }
        for (final Map.Entry<Byte, Integer> qs : ct.reports.entrySet()) {
            ret.reports.put(ReportType.getById(qs.getKey()), qs.getValue());
        }
        ret.monsterbook = new MonsterBook(ct.mbook, ret);
        ret.inventory = (MapleInventory[]) ct.inventorys;
        ret.BlessOfFairy_Origin = ct.BlessOfFairy;
        ret.BlessOfEmpress_Origin = ct.BlessOfEmpress;
        ret.skillMacros = (SkillMacro[]) ct.skillmacro;
        ret.petStore = ct.petStore;
        ret.keylayout = new MapleKeyLayout(ct.keymap);
        ret.questinfo = ct.InfoQuest;
        ret.familiars = ct.familiars;
        ret.savedLocations = ct.savedlocation;
        ret.wishlist = ct.wishlist;
        ret.rocks = ct.rocks;
        ret.regrocks = ct.regrocks;
        ret.hyperrocks = ct.hyperrocks;
        ret.buddylist.loadFromTransfer(ct.buddies);
        ret.keydown_skill = 0; // Keydown skill can't be brought over
        ret.lastfametime = ct.lastfametime;
        ret.lastmonthfameids = ct.famedcharacters;
        ret.lastmonthbattleids = ct.battledaccs;
        ret.extendedSlots = ct.extendedSlots;
        ret.storage = (MapleStorage) ct.storage;
        ret.cs = (CashShop) ct.cs;
        client.setAccountName(ct.accountname);
        ret.nxcredit = ct.nxCredit;
        ret.acash = ct.ACash;
        ret.maplepoints = ct.MaplePoints;
        ret.numClones = ct.clonez;
        ret.imps = ct.imps;
        ret.anticheat = (CheatTracker) ct.anticheat;
        ret.anticheat.start(ret);
        ret.rebuy = ct.rebuy;
        ret.mount = new MapleMount(ret, ct.mount_itemid, PlayerStats.getSkillByJob(1004, ret.job), ct.mount_Fatigue, ct.mount_level, ct.mount_exp);
        ret.expirationTask(false, false);
        ret.stats.recalcLocalStats(true, ret);
        client.setTempIP(ct.tempIP);

        return ret;
    }

    public static MapleCharacter loadCharFromDB(int charid, MapleClient client, boolean channelserver) {
        return loadCharFromDB(charid, client, channelserver, null);
    }

    public static MapleCharacter loadCharFromDB(int charid, MapleClient client, boolean channelserver, final Map<Integer, CardData> cads) {
        final MapleCharacter ret = new MapleCharacter(channelserver);
        ret.client = client;
        ret.id = charid;

        Connection con = DatabaseConnection.getConnection();
        PreparedStatement ps = null;
        PreparedStatement pse;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
            ps.setInt(1, charid);
            rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
                throw new RuntimeException("加載角色失敗(未找到角色)");
            }
            ret.name = rs.getString("name");
            ret.level = rs.getShort("level");
            ret.fame = rs.getInt("fame");

            ret.stats.str = rs.getShort("str");
            ret.stats.dex = rs.getShort("dex");
            ret.stats.int_ = rs.getShort("int");
            ret.stats.luk = rs.getShort("luk");
            ret.stats.maxhp = rs.getInt("maxhp");
            ret.stats.maxmp = rs.getInt("maxmp");
            ret.stats.hp = rs.getInt("hp");
            ret.stats.mp = rs.getInt("mp");
            ret.job = rs.getShort("job");
            ret.gmLevel = rs.getByte("gm");
            ret.exp = ret.level >= ret.maxLevel ? 0 : rs.getLong("exp");
            ret.hpApUsed = rs.getShort("hpApUsed");
            String[] sp = rs.getString("sp").split(",");
            for (int i = 0; i < ret.remainingSp.length; i++) {
                ret.remainingSp[i] = Integer.parseInt(sp[i]);
            }
            String[] hsp = rs.getString("hsp").split(",");
            for (int i = 0; i < ret.remainingHSp.length; i++) {
                ret.remainingHSp[i] = Integer.parseInt(hsp[i]);
            }
            ret.remainingAp = rs.getShort("ap");
            ret.meso = rs.getLong("meso");
            ret.skinColor = rs.getByte("skincolor");
            ret.gender = rs.getByte("gender");

            ret.hair = rs.getInt("hair");
            ret.face = rs.getInt("face");
            ret.faceMarking = rs.getInt("faceMarking");
            ret.ears = rs.getInt("ears");
            ret.tail = rs.getInt("tail");
            ret.elf = rs.getInt("elf");
            ret.accountid = rs.getInt("accountid");
            client.setAccID(ret.accountid);
            ret.weaponPoint = rs.getInt("weaponPoint");
            ret.mapid = rs.getInt("map");
            ret.initialSpawnPoint = rs.getByte("spawnpoint");
            ret.world = rs.getByte("world");
            ret.guildid = rs.getInt("guildid");
            ret.guildrank = rs.getByte("guildrank");
            ret.allianceRank = rs.getByte("allianceRank");
            ret.guildContribution = rs.getInt("guildContribution");
            ret.totalWins = rs.getInt("totalWins");
            ret.totalLosses = rs.getInt("totalLosses");
            ret.currentrep = rs.getInt("currentrep");
            ret.totalrep = rs.getInt("totalrep");
            ret.makeMFC(rs.getInt("familyid"), rs.getInt("seniorid"), rs.getInt("junior1"), rs.getInt("junior2"));
            if (ret.guildid > 0) {
                ret.mgc = new MapleGuildCharacter(ret);
            }
            ret.gachexp = rs.getInt("gachexp");
            ret.buddylist = new BuddyList(rs.getByte("buddyCapacity"));
            ret.honourExp = rs.getInt("honourExp");
            ret.honorLevel = rs.getInt("honourLevel");
            ret.subcategory = rs.getByte("subcategory");
            ret.mount = new MapleMount(ret, 0, PlayerStats.getSkillByJob(1004, ret.job), (byte) 0, (byte) 1, 0);
            ret.rank = rs.getInt("rank");
            ret.rankMove = rs.getInt("rankMove");
            ret.jobRank = rs.getInt("jobRank");
            ret.jobRankMove = rs.getInt("jobRankMove");
            ret.marriageId = rs.getInt("marriageId");
            ret.fatigue = rs.getShort("fatigue");
            ret.pvpExp = rs.getInt("pvpExp");
            ret.pvpPoints = rs.getInt("pvpPoints");
            ret.friendshiptoadd = rs.getInt("friendshiptoadd");
            ret.chattype = rs.getShort("chatcolour");
            /*
             * Start of Custom Features
             */
            ret.reborns = rs.getInt("reborns");
            ret.apstorage = rs.getInt("apstorage");
            /*
             * End of Custom Features
             */
            for (MapleTrait t : ret.traits.values()) {
                t.setExp(rs.getInt(t.getType().name()));
            }
            if (channelserver) {
                ret.CRand = new PlayerRandomStream();
                ret.anticheat = new CheatTracker(ret);
                MapleMapFactory mapFactory = ChannelServer.getInstance(client.getChannel()).getMapFactory();
                ret.map = mapFactory.getMap(ret.mapid);
                if (ret.map == null) { //char is on a map that doesn't exist warp it to spinel forest
                    ret.map = mapFactory.getMap(950000100);
                }
                MaplePortal portal = ret.map.getPortal(ret.initialSpawnPoint);
                if (portal == null) {
                    portal = ret.map.getPortal(0); // char is on a spawnpoint that doesn't exist - select the first spawnpoint instead
                    ret.initialSpawnPoint = 0;
                }
                ret.setPosition(portal.getPosition());

                int partyid = rs.getInt("party");
                if (partyid >= 0) {
                    MapleParty party = World.Party.getParty(partyid);
                    if (party != null && party.getMemberById(ret.id) != null) {
                        ret.party = party;
                    }
                }
                String[] pets = rs.getString("pets").split(",");
                for (int i = 0; i < ret.petStore.length; i++) {
                    ret.petStore[i] = Byte.parseByte(pets[i]);
                }
                String[] friendshippoints = rs.getString("friendshippoints").split(",");
                for (int i = 0; i < 5; i++) {
                    ret.friendshippoints[i] = Integer.parseInt(friendshippoints[i]);
                }
                rs.close();
                ps.close();

                ps = con.prepareStatement("SELECT * FROM reports WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (ReportType.getById(rs.getByte("type")) != null) {
                        ret.reports.put(ReportType.getById(rs.getByte("type")), rs.getInt("count"));
                    }
                }

            }
            rs.close();
            ps.close();

            if (ret.marriageId > 0) {
                ps = con.prepareStatement("SELECT * FROM characters WHERE id = ?");
                ps.setInt(1, ret.marriageId);
                rs = ps.executeQuery();
                int partnerId = rs.getInt("id");
                ret.marriage = new MapleMarriage(partnerId, ret.marriageItemId);
                ret.marriage.setHusbandId(ret.gender == 0 ? ret.id : partnerId);
                ret.marriage.setWifeId(ret.gender == 1 ? ret.id : partnerId);
                String partnerName = rs.getString("name");
                ret.marriage.setHusbandName(ret.gender == 0 ? ret.name : partnerName);
                ret.marriage.setWifeName(ret.gender == 1 ? ret.name : partnerName);
                /*if (rs.next()) {
                 ret.marriage = new MapleMarriage(rs.getInt("id"), rs.getInt("ring"));
                 ret.marriage.setHusbandId(rs.getInt("husbandId"));
                 ret.marriage.setWifeId(rs.getInt("husbandId"));
                 ret.marriage.setHusbandName(rs.getString("husbandName"));
                 ret.marriage.setWifeName(rs.getString("husbandName"));
                 } else {
                 ret.marriage = null;
                 }*/
                rs.close();
                ps.close();
            }

            if (cads != null) { // so that we load only once.
                ret.characterCard.setCards(cads);
            } else { // load
                ret.characterCard.loadCards(client, channelserver);
            }

            ps = con.prepareStatement("SELECT * FROM queststatus WHERE characterid = ?");
            ps.setInt(1, charid);
            rs = ps.executeQuery();
            pse = con.prepareStatement("SELECT * FROM queststatusmobs WHERE queststatusid = ?");

            while (rs.next()) {
                final int id = rs.getInt("quest");
                final MapleQuest q = MapleQuest.getInstance(id);
                final byte stat = rs.getByte("status");
                if ((stat == 1 || stat == 2) && channelserver && (q == null || q.isBlocked())) { //bigbang
                    continue;
                }
                if (stat == 1 && channelserver && !q.canStart(ret, null)) { //bigbang
                    continue;
                }
                final MapleQuestStatus status = new MapleQuestStatus(q, stat);
                final long cTime = rs.getLong("time");
                if (cTime > -1) {
                    status.setCompletionTime(cTime * 1000);
                }
                status.setForfeited(rs.getInt("forfeited"));
                status.setCustomData(rs.getString("customData"));
                ret.quests.put(q, status);
                pse.setInt(1, rs.getInt("queststatusid"));
                try (ResultSet rsMobs = pse.executeQuery()) {
                    while (rsMobs.next()) {
                        status.setMobKills(rsMobs.getInt("mob"), rsMobs.getInt("count"));
                    }
                }
            }
            rs.close();
            ps.close();
            pse.close();
            if (channelserver) {
                ret.monsterbook = MonsterBook.loadCards(ret.accountid, ret);
                ps = con.prepareStatement("SELECT * FROM inventoryslot where characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                if (!rs.next()) {
                    rs.close();
                    ps.close();
                    throw new RuntimeException("No Inventory slot column found in SQL. [inventoryslot]");
                } else {
                    ret.getInventory(MapleInventoryType.EQUIP).setSlotLimit(rs.getByte("equip"));
                    ret.getInventory(MapleInventoryType.USE).setSlotLimit(rs.getByte("use"));
                    ret.getInventory(MapleInventoryType.SETUP).setSlotLimit(rs.getByte("setup"));
                    ret.getInventory(MapleInventoryType.ETC).setSlotLimit(rs.getByte("etc"));
                    ret.getInventory(MapleInventoryType.CASH).setSlotLimit(rs.getByte("cash"));
                }
                ps.close();
                rs.close();
                for (Pair<Item, MapleInventoryType> mit : ItemLoader.INVENTORY.loadItems(false, charid).values()) {
                    ret.getInventory(mit.getRight()).addFromDB(mit.getLeft());
                    if (mit.getLeft().getPet() != null) {
                        ret.pets.add(mit.getLeft().getPet());
                    }
                }

                /*ps = con.prepareStatement("SELECT * FROM potionpots WHERE cid = ?");
                 ps.setInt(1, ret.id);
                 rs = ps.executeQuery();
                 ret.potionPots = new ArrayList();
                 while (rs.next()) {
                 MaplePotionPot pot = MaplePotionPot.loadFromResult(rs);
                 if (pot != null) {
                 ret.potionPots.add(pot);
                 }
                 }
                 rs.close();
                 ps.close();*/
                ps = con.prepareStatement("SELECT * FROM accounts WHERE id = ?");
                ps.setInt(1, ret.accountid);
                rs = ps.executeQuery();
                if (rs.next()) {
                    ret.getClient().setAccountName(rs.getString("name"));
                    ret.nxcredit = rs.getInt("nxCredit");
                    ret.acash = rs.getInt("ACash");
                    ret.maplepoints = rs.getInt("mPoints");
                    ret.points = rs.getInt("points");
                    ret.vpoints = rs.getInt("vpoints");
                    ret.epoints = rs.getInt("epoints");
                    ret.dpoints = rs.getInt("dpoints");

                    if (rs.getTimestamp("lastlogon") != null) {
                        final Calendar cal = Calendar.getInstance();
                        cal.setTimeInMillis(rs.getTimestamp("lastlogon").getTime());
                    }
                    if (rs.getInt("banned") > 0) {
                        rs.close();
                        ps.close();
                        ret.getClient().getSession().close(true);
                        throw new RuntimeException("Loading a banned character");
                    }
                    rs.close();
                    ps.close();

                    ps = con.prepareStatement("UPDATE accounts SET lastlogon = CURRENT_TIMESTAMP() WHERE id = ?");
                    ps.setInt(1, ret.accountid);
                    ps.executeUpdate();
                } else {
                    rs.close();
                }
                ps.close();

                ps = con.prepareStatement("SELECT * FROM questinfo WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();

                while (rs.next()) {
                    ret.questinfo.put(rs.getInt("quest"), rs.getString("customData"));
                }
                rs.close();
                ps.close();

                ps = con.prepareStatement("SELECT skillid, skilllevel, masterlevel, expiration FROM skills WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                Skill skil;
                while (rs.next()) {
                    final int skid = rs.getInt("skillid");
                    skil = SkillFactory.getSkill(skid);
                    int skl = rs.getInt("skilllevel");
                    byte msl = rs.getByte("masterlevel");
                    if (skil != null && GameConstants.isApplicableSkill(skid)) {
                        if (skl > skil.getMaxLevel() && (skid < 92000000 || skid > 99999999)) {
                            if (!skil.isBeginnerSkill() && skil.canBeLearnedBy(ret.job) && !skil.isSpecialSkill()) {
                                ret.remainingSp[GameConstants.getSkillBookBySkill(skid)] += (skl - skil.getMaxLevel());
                            }
                            skl = (byte) skil.getMaxLevel();
                        }
                        if (msl > skil.getMaxLevel()) {
                            msl = (byte) skil.getMaxLevel();
                        }
                        ret.skills.put(skil, new SkillEntry(skl, msl, rs.getLong("expiration")));
                    } else if (skil == null) { //doesnt. exist. e.g. bb
                        if (!MapleJob.isBeginner(skid / 10000) && skid / 10000 != 900 && skid / 10000 != 800 && skid / 10000 != 9000) {
                            ret.remainingSp[GameConstants.getSkillBookBySkill(skid)] += skl;
                        }
                    }
                }
                rs.close();
                ps.close();

                ret.expirationTask(false, true); //do it now

                ps = con.prepareStatement("SELECT * FROM coreauras WHERE cid = ?");
                ps.setInt(1, ret.id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    ret.coreAura = new MapleCoreAura(ret.id, rs.getInt("expire"));
                    ret.coreAura.setStr(rs.getInt("str"));
                    ret.coreAura.setDex(rs.getInt("dex"));
                    ret.coreAura.setInt(rs.getInt("int"));
                    ret.coreAura.setLuk(rs.getInt("luk"));
                    ret.coreAura.setAtt(rs.getInt("att"));
                    ret.coreAura.setMagic(rs.getInt("magic"));
                    ret.coreAura.setTotal(rs.getInt("total"));
                } else {
                    ret.coreAura = new MapleCoreAura(ret.id, 24 * 60);
                }
                rs.close();
                ps.close();

                // Bless of Fairy handling
                ps = con.prepareStatement("SELECT * FROM characters WHERE accountid = ? ORDER BY level DESC");
                ps.setInt(1, ret.accountid);
                rs = ps.executeQuery();
                int maxlevel_ = 0, maxlevel_2 = 0;
                while (rs.next()) {
                    if (rs.getInt("id") != charid) { // Not this character
                        if (MapleJob.is皇家騎士團(rs.getShort("job"))) {
                            int maxlevel = (rs.getShort("level") / 5);

                            if (maxlevel > 24) {
                                maxlevel = 24;
                            }
                            if (maxlevel > maxlevel_2 || maxlevel_2 == 0) {
                                maxlevel_2 = maxlevel;
                                ret.BlessOfEmpress_Origin = rs.getString("name");
                            }
                        }
                        int maxlevel = (rs.getShort("level") / 10);

                        if (maxlevel > 20) {
                            maxlevel = 20;
                        }
                        if (maxlevel > maxlevel_ || maxlevel_ == 0) {
                            maxlevel_ = maxlevel;
                            ret.BlessOfFairy_Origin = rs.getString("name");
                        }

                    }
                }
                /*
                 * if (!compensate_previousSP) { for (Entry<Skill, SkillEntry>
                 * skill : ret.skills.entrySet()) { if
                 * (!skill.getKey().isBeginnerSkill() &&
                 * !skill.getKey().isSpecialSkill()) {
                 * ret.remainingSp[GameConstants.getSkillBookForSkill(skill.getKey().getId())]
                 * += skill.getValue().skillevel; skill.getValue().skillevel =
                 * 0; } } ret.setQuestAdd(MapleQuest.getInstance(170000), (byte)
                 * 0, null); //set it so never again }
                 */
                if (ret.BlessOfFairy_Origin == null) {
                    ret.BlessOfFairy_Origin = ret.name;
                }
                ret.skills.put(SkillFactory.getSkill(GameConstants.getBOF_ForJob(ret.job)), new SkillEntry(maxlevel_, (byte) 0, -1));
                if (SkillFactory.getSkill(GameConstants.getEmpress_ForJob(ret.job)) != null) {
                    if (ret.BlessOfEmpress_Origin == null) {
                        ret.BlessOfEmpress_Origin = ret.BlessOfFairy_Origin;
                    }
                    ret.skills.put(SkillFactory.getSkill(GameConstants.getEmpress_ForJob(ret.job)), new SkillEntry(maxlevel_2, (byte) 0, -1));
                }
                ps.close();
                rs.close();
                // END

                ps = con.prepareStatement("SELECT skill_id, skill_level, max_level, rank, locked FROM inner_ability_skills WHERE player_id = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                while (rs.next()) {
                    ret.innerSkills.add(new InnerSkillValueHolder(rs.getInt("skill_id"), rs.getByte("skill_level"), rs.getByte("max_level"), rs.getByte("rank"), rs.getBoolean("locked")));
                }
                ps = con.prepareStatement("SELECT * FROM skillmacros WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                int position;
                while (rs.next()) {
                    position = rs.getInt("position");
                    SkillMacro macro = new SkillMacro(rs.getInt("skill1"), rs.getInt("skill2"), rs.getInt("skill3"), rs.getString("name"), rs.getInt("shout"), position);
                    ret.skillMacros[position] = macro;
                }
                rs.close();
                ps.close();
                /*
                 * ps = con.prepareStatement("SELECT victimid, skillid,
                 * skilllevel, slot, category FROM stolen_skills WHERE chrid =
                 * ?"); ps.setInt(1, charid); rs = ps.executeQuery(); int slot;
                 * while (rs.next()) { slot = rs.getInt("slot"); SkillSwipe ss =
                 * new SkillSwipe(rs.getInt("victimid"), rs.getInt("skillid"),
                 * rs.getInt("skilllevel"), rs.getInt("category"), slot);
                 * ret.skillSwipe[slot] = ss; } rs.close(); ps.close();
                 */
                ps = con.prepareStatement("SELECT * FROM familiars WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getLong("expiry") <= System.currentTimeMillis()) {
                        continue;
                    }
                    ret.familiars.put(rs.getInt("familiar"), new MonsterFamiliar(charid, rs.getInt("id"), rs.getInt("familiar"), rs.getLong("expiry"), rs.getString("name"), rs.getInt("fatigue"), rs.getByte("vitality")));
                }
                rs.close();
                ps.close();

                ps = con.prepareStatement("SELECT `key`,`type`,`action` FROM keymap WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();

                final Map<Integer, Pair<Byte, Integer>> keyb = ret.keylayout.Layout();
                while (rs.next()) {
                    keyb.put(rs.getInt("key"), new Pair<>(rs.getByte("type"), rs.getInt("action")));
                }
                rs.close();
                ps.close();
                ret.keylayout.unchanged();

                ps = con.prepareStatement("SELECT `locationtype`,`map` FROM savedlocations WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                while (rs.next()) {
                    ret.savedLocations[rs.getInt("locationtype")] = rs.getInt("map");
                }
                rs.close();
                ps.close();

                ps = con.prepareStatement("SELECT `characterid_to`,`when` FROM famelog WHERE characterid = ? AND DATEDIFF(NOW(),`when`) < 30");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                ret.lastfametime = 0;
                ret.lastmonthfameids = new ArrayList<>(31);
                while (rs.next()) {
                    ret.lastfametime = Math.max(ret.lastfametime, rs.getTimestamp("when").getTime());
                    ret.lastmonthfameids.add(rs.getInt("characterid_to"));
                }
                rs.close();
                ps.close();

                ps = con.prepareStatement("SELECT `accid_to`,`when` FROM battlelog WHERE accid = ? AND DATEDIFF(NOW(),`when`) < 30");
                ps.setInt(1, ret.accountid);
                rs = ps.executeQuery();
                ret.lastmonthbattleids = new ArrayList<>();
                while (rs.next()) {
                    ret.lastmonthbattleids.add(rs.getInt("accid_to"));
                }
                rs.close();
                ps.close();

                ps = con.prepareStatement("SELECT `itemId` FROM extendedSlots WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                while (rs.next()) {
                    ret.extendedSlots.add(rs.getInt("itemId"));
                }
                rs.close();
                ps.close();

                ret.buddylist.loadFromDb(charid);
                ret.storage = MapleStorage.loadStorage(ret.accountid);
                ret.cs = new CashShop(ret.accountid, charid, ret.getJob());

                ps = con.prepareStatement("SELECT sn FROM wishlist WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                int i = 0;
                while (rs.next()) {
                    ret.wishlist[i] = rs.getInt("sn");
                    i++;
                }
                while (i < 30) {
                    ret.wishlist[i] = 0;
                    i++;
                }
                rs.close();
                ps.close();

                ps = con.prepareStatement("SELECT mapid FROM trocklocations WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                int r = 0;
                while (rs.next()) {
                    ret.rocks[r] = rs.getInt("mapid");
                    r++;
                }
                while (r < 10) {
                    ret.rocks[r] = 999999999;
                    r++;
                }
                rs.close();
                ps.close();

                ps = con.prepareStatement("SELECT mapid FROM regrocklocations WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                r = 0;
                while (rs.next()) {
                    ret.regrocks[r] = rs.getInt("mapid");
                    r++;
                }
                while (r < 5) {
                    ret.regrocks[r] = 999999999;
                    r++;
                }
                rs.close();
                ps.close();

                ps = con.prepareStatement("SELECT mapid FROM hyperrocklocations WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                r = 0;
                while (rs.next()) {
                    ret.hyperrocks[r] = rs.getInt("mapid");
                    r++;
                }
                while (r < 13) {
                    ret.hyperrocks[r] = 999999999;
                    r++;
                }
                rs.close();
                ps.close();

                ps = con.prepareStatement("SELECT * from stolen WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                while (rs.next()) {
                    ret.stolenSkills.add(new Pair<>(rs.getInt("skillid"), rs.getInt("chosen") > 0));
                }
                rs.close();
                ps.close();
                ps = con.prepareStatement("SELECT * FROM imps WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                r = 0;
                while (rs.next()) {
                    ret.imps[r] = new MapleImp(rs.getInt("itemid"));
                    ret.imps[r].setLevel(rs.getByte("level"));
                    ret.imps[r].setState(rs.getByte("state"));
                    ret.imps[r].setCloseness(rs.getShort("closeness"));
                    ret.imps[r].setFullness(rs.getShort("fullness"));
                    r++;
                }
                rs.close();
                ps.close();

                ps = con.prepareStatement("SELECT * FROM mountdata WHERE characterid = ?");
                ps.setInt(1, charid);
                rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new RuntimeException("在數據庫沒有找到角色坐騎訊息...");
                }
                final Item mount = ret.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -18);
                ret.mount = new MapleMount(ret, mount != null ? mount.getItemId() : 0, 80001000, rs.getByte("Fatigue"), rs.getByte("Level"), rs.getInt("Exp"));
                ps.close();
                rs.close();

                ret.stats.recalcLocalStats(true, ret);
            } else { // Not channel server
                for (Pair<Item, MapleInventoryType> mit : ItemLoader.INVENTORY.loadItems(true, charid).values()) {
                    ret.getInventory(mit.getRight()).addFromDB(mit.getLeft());
                }
                ret.stats.recalcPVPRank(ret);
            }
        } catch (SQLException ess) {
            ess.printStackTrace();
            System.out.println("加載角色出錯..");
            FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, ess);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignore) {
            }
        }
        return ret;
    }

    public static int getQuestKillCount(MapleCharacter chr, final int mobid) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement pse;
            try (PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT queststatusid FROM queststatus WHERE characterid = ?")) {
                ResultSet rse;
                try (ResultSet rs = ps.executeQuery()) {
                    pse = (PreparedStatement) con.prepareStatement("SELECT count FROM queststatusmobs WHERE queststatusid = ?");
                    rse = pse.executeQuery();
                    while (rs.next()) {
                        return rse.getInt("count");
                    }
                }
                rse.close();
            }
            pse.close();
        } catch (SQLException e) {
        }
        return -1;
    }

    public static void saveNewCharToDB(final MapleCharacter chr, final JobType type, short db) {
        saveNewCharToDB(chr, type, db, 0);
    }

    public static void saveNewCharToDB(final MapleCharacter chr, final JobType type, short db, int keymapType) {
        Connection con = DatabaseConnection.getConnection();

        PreparedStatement ps = null;
        PreparedStatement pse = null;
        ResultSet rs = null;
        try {
            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            con.setAutoCommit(false);

            ps = con.prepareStatement("INSERT INTO characters (level, str, dex, luk, `int`, hp, mp, maxhp, maxmp, sp, hsp, ap, skincolor, gender, job, hair, face, faceMarking, ears, tail, weaponPoint, map, meso, party, buddyCapacity, pets, subcategory, elf,friendshippoints, chatcolour, gm, accountid, name, world, position)"     //subcategory后删除了elf。
                    + "                                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", DatabaseConnection.RETURN_GENERATED_KEYS);
            int index = 0;
            ps.setInt(++index, chr.level); // Level
            final PlayerStats stat = chr.stats;
            ps.setInt(++index, stat.getStr()); // Str
            ps.setInt(++index, stat.getDex()); // Dex
            ps.setInt(++index, stat.getInt()); // Int
            ps.setInt(++index, stat.getLuk()); // Luk
            ps.setInt(++index, stat.getHp()); // HP
            ps.setInt(++index, stat.getMp());
            ps.setInt(++index, stat.getMaxHp()); // MP
            ps.setInt(++index, stat.getMaxMp());
            final StringBuilder sps = new StringBuilder();
            for (int i = 0; i < chr.remainingSp.length; i++) {
                sps.append(chr.remainingSp[i]);
                sps.append(",");
            }
            final String sp = sps.toString();
            ps.setString(++index, sp.substring(0, sp.length() - 1));
            final StringBuilder hsps = new StringBuilder();
            for (int i = 0; i < chr.remainingHSp.length; i++) {
                hsps.append(chr.remainingHSp[i]);
                hsps.append(",");
            }
            final String hsp = hsps.toString();
            ps.setString(++index, hsp.substring(0, hsp.length() - 1));
            if (chr.remainingAp > (999 + 16) - (chr.str + chr.dex + chr.int_ + chr.luk)) {
                chr.remainingAp = (999 + 16) - (chr.str + chr.dex + chr.int_ + chr.luk);
            }
            ps.setShort(++index, (short) chr.remainingAp); // Remaining AP
            ps.setByte(++index, chr.skinColor);
            ps.setByte(++index, chr.gender);
            ps.setInt(++index, chr.job);
            ps.setInt(++index, chr.hair);
            ps.setInt(++index, chr.face);
            ps.setInt(++index, chr.faceMarking);
            ps.setInt(++index, chr.ears);
            ps.setInt(++index, chr.tail);
            if (db < 0 || db > 10) {
                db = 0;
            }
            ps.setLong(++index, chr.weaponPoint); // WeaponPoint
            ps.setInt(++index, db == 2 ? 3000600 : type.map);
            ps.setLong(++index, chr.meso); // Meso
            ps.setInt(++index, -1); // Party
            ps.setByte(++index, chr.buddylist.getCapacity()); // Buddylist
            ps.setString(++index, "-1,-1,-1");
            ps.setInt(++index, db); //for now
            ps.setInt(++index, chr.elf);
            ps.setString(++index, chr.friendshippoints[0] + "," + chr.friendshippoints[1] + "," + chr.friendshippoints[2] + "," + chr.friendshippoints[3] + "," + chr.friendshippoints[4]);
            ps.setShort(++index, (short) 0);
            ps.setByte(++index, (byte) chr.gmLevel);
            ps.setInt(++index, chr.getAccountID());
            ps.setString(++index, chr.name);
            ps.setByte(++index, chr.world);
            ps.setInt(++index, chr.getClient().getCharacterPos().size() + 1);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                chr.id = rs.getInt(1);
            } else {
                ps.close();
                rs.close();
                throw new DatabaseException("Inserting char failed.");
            }
            ps.close();
            rs.close();
            ps = con.prepareStatement("INSERT INTO queststatus (`queststatusid`, `characterid`, `quest`, `status`, `time`, `forfeited`, `customData`) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)", DatabaseConnection.RETURN_GENERATED_KEYS);
            pse = con.prepareStatement("INSERT INTO queststatusmobs VALUES (DEFAULT, ?, ?, ?)");
            ps.setInt(1, chr.id);
            for (final MapleQuestStatus q : chr.quests.values()) {
                ps.setInt(2, q.getQuest().getId());
                ps.setInt(3, q.getStatus());
                ps.setInt(4, (int) (q.getCompletionTime() / 1000));
                ps.setInt(5, q.getForfeited());
                ps.setString(6, q.getCustomData());
                ps.execute();
                rs = ps.getGeneratedKeys();
                if (q.hasMobKills()) {
                    rs.next();
                    for (int mob : q.getMobKills().keySet()) {
                        pse.setInt(1, rs.getInt(1));
                        pse.setInt(2, mob);
                        pse.setInt(3, q.getMobKills(mob));
                        pse.execute();
                    }
                }
                rs.close();
            }
            ps.close();
            pse.close();

            ps = con.prepareStatement("INSERT INTO skills (characterid, skillid, skilllevel, masterlevel, expiration) VALUES (?, ?, ?, ?, ?)");
            ps.setInt(1, chr.id);

            for (final Entry<Skill, SkillEntry> skill : chr.skills.entrySet()) {
                if (GameConstants.isApplicableSkill(skill.getKey().getId())) { //do not save additional skills
                    ps.setInt(2, skill.getKey().getId());
                    ps.setInt(3, skill.getValue().skillLevel);
                    ps.setByte(4, skill.getValue().masterlevel);
                    ps.setLong(5, skill.getValue().expiration);
                    ps.execute();
                }
            }
            ps.close();

            ps = con.prepareStatement("INSERT INTO coreauras (cid, str, dex, `int`, luk, att, magic, total, expire) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, chr.id);
            if (MapleJob.is蒼龍俠客(chr.job)) {
                ps.setInt(2, 3);
                ps.setInt(3, 3);
                ps.setInt(4, 3);
                ps.setInt(5, 3);
                ps.setInt(6, 3);
                ps.setInt(7, 3);
                ps.setInt(8, 24 * 60);
            }

            ps = con.prepareStatement("INSERT INTO inventoryslot (characterid, `equip`, `use`, `setup`, `etc`, `cash`) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setInt(1, chr.id);
            ps.setByte(2, (byte) ZZMSConfig.defaultInventorySlot); // 初始裝備欄Eq
            ps.setByte(3, (byte) ZZMSConfig.defaultInventorySlot); // 初始消耗欄Use
            ps.setByte(4, (byte) ZZMSConfig.defaultInventorySlot); // 初始裝飾欄Setup
            ps.setByte(5, (byte) ZZMSConfig.defaultInventorySlot); // 初始其他欄ETC
            ps.setByte(6, (byte) 96); // 初始特殊欄Cash
            ps.execute();
            ps.close();

            ps = con.prepareStatement("INSERT INTO mountdata (characterid, `Level`, `Exp`, `Fatigue`) VALUES (?, ?, ?, ?)");
            ps.setInt(1, chr.id);
            ps.setByte(2, (byte) 1);
            ps.setInt(3, 0);
            ps.setByte(4, (byte) 0);
            ps.execute();
            ps.close();
            int[] array1;
            int[] array2;
            int[] array3;
            if (keymapType == 0) {
                // 基本模式設定
                array1 = new int[]{2, 3, 4, 5, 6, 7, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 29, 31, 34, 35, 37, 38, 39, 40, 41, 43, 44, 45, 46, 47, 48, 50, 52, 56, 57, 59, 60, 61, 63, 64, 65, 66};
                array2 = new int[]{4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 4, 4, 4, 4, 4, 5, 5, 6, 6, 6, 6, 6, 6, 6};
                array3 = new int[]{10, 12, 13, 18, 23, 25, 8, 5, 0, 4, 27, 30, 39, 1, 41, 19, 14, 15, 52, 2, 17, 11, 3, 20, 26, 16, 22, 9, 50, 51, 6, 31, 29, 7, 40, 53, 54, 100, 101, 102, 103, 104, 105, 106};
            } else {
                // 進階模式設定
                array1 = new int[]{20, 21, 22, 23, 25, 26, 27, 29, 34, 35, 36, 37, 38, 39, 40, 41, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 56, 57, 59, 60, 61, 63, 64, 65, 66, 71, 73, 79, 81, 82, 83};
                array2 = new int[]{4, 4, 4, 4, 4, 4, 4, 5, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 4, 4, 4, 4, 4, 4, 4, 5, 5, 6, 6, 6, 6, 6, 6, 6, 4, 4, 4, 4, 4, 4};
                array3 = new int[]{27, 30, 0, 1, 19, 14, 15, 52, 17, 11, 8, 3, 20, 26, 16, 22, 9, 50, 51, 2, 31, 29, 5, 7, 40, 4, 53, 54, 100, 101, 102, 103, 104, 105, 106, 12, 13, 23, 25, 10, 18};
            }
            ps = con.prepareStatement("INSERT INTO keymap (characterid, `key`, `type`, `action`) VALUES (?, ?, ?, ?)");
            ps.setInt(1, chr.id);
            for (int i = 0; i < array1.length; i++) {
                ps.setInt(2, array1[i]);
                ps.setInt(3, array2[i]);
                ps.setInt(4, array3[i]);
                ps.execute();
            }
            ps.close();

            List<Pair<Item, MapleInventoryType>> listing = new ArrayList<>();
            for (final MapleInventory iv : chr.inventory) {
                for (final Item item : iv.list()) {
                    listing.add(new Pair<>(item, iv.getType()));
                }
            }
            ItemLoader.INVENTORY.saveItems(listing, con, chr.id);

            con.commit();
        } catch (SQLException | DatabaseException e) {
            FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
            System.err.println("[newcharsave] Error saving character data: " + e);
            e.printStackTrace();
            try {
                con.rollback();
            } catch (SQLException ex) {
                FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, ex);
                System.err.println("[newcharsave] Error Rolling Back");
            }
        } finally {
            try {
                if (pse != null) {
                    pse.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                con.setAutoCommit(true);
                con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            } catch (SQLException e) {
                FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
                System.err.println("[charsave] Error going back to autocommit mode");
            }
        }
        chr.getClient().addCharacterPos(chr.getId());
    }

    public void saveToDB(boolean dc, boolean fromcs) {
        if (isClone()) {
            return;
        }
        Connection con = DatabaseConnection.getConnection();

        PreparedStatement ps = null;
        PreparedStatement pse = null;
        ResultSet rs = null;

        try {
            con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            con.setAutoCommit(false);

            ps = con.prepareStatement("UPDATE characters SET level = ?, fame = ?, str = ?, dex = ?, luk = ?, `int` = ?, exp = ?, hp = ?, mp = ?, maxhp = ?, maxmp = ?, sp = ?, hsp = ?, ap = ?, gm = ?, skincolor = ?, gender = ?, job = ?, hair = ?, face = ?, faceMarking = ?, ears = ?, tail = ?, weaponPoint = ?, map = ?, meso = ?, hpApUsed = ?, spawnpoint = ?, party = ?, buddyCapacity = ?, pets = ?, subcategory = ?, currentrep = ?, totalrep = ?, gachexp = ?, fatigue = ?, charm = ?, charisma = ?, craft = ?, insight = ?, sense = ?, will = ?, totalwins = ?, totallosses = ?, pvpExp = ?, pvpPoints = ?, reborns = ?, apstorage = ?, elf = ?,honourExp = ?, honourLevel = ?, friendshippoints = ?, friendshiptoadd = ?, chatcolour = ?, name = ? WHERE id = ?", DatabaseConnection.RETURN_GENERATED_KEYS);  //apstorage = ?后删除了elf = ?
            int index = 0;
            ps.setInt(++index, level);
            ps.setInt(++index, fame);
            ps.setInt(++index, stats.getStr());
            ps.setInt(++index, stats.getDex());
            ps.setInt(++index, stats.getLuk());
            ps.setInt(++index, stats.getInt());
            ps.setLong(++index, level >= maxLevel ? 0 : exp);
            ps.setInt(++index, stats.getHp() < 1 ? 50 : stats.getHp());
            ps.setInt(++index, stats.getMp());
            ps.setInt(++index, stats.getMaxHp());
            ps.setInt(++index, stats.getMaxMp());
            final StringBuilder sps = new StringBuilder();
            for (int i = 0; i < remainingSp.length; i++) {
                sps.append(remainingSp[i]);
                sps.append(",");
            }
            final String skillpoints = sps.toString();
            ps.setString(++index, skillpoints.substring(0, skillpoints.length() - 1));
            final StringBuilder hsps = new StringBuilder();
            for (int i = 0; i < remainingHSp.length; i++) {
                hsps.append(remainingHSp[i]);
                hsps.append(",");
            }
            final String hskillpoints = hsps.toString();
            ps.setString(++index, hskillpoints.substring(0, hskillpoints.length() - 1));
            ps.setInt(++index, remainingAp);
            ps.setByte(++index, gmLevel);
            ps.setByte(++index, skinColor);
            ps.setByte(++index, gender);
            ps.setInt(++index, job);
            ps.setInt(++index, hair);
            ps.setInt(++index, face);
            ps.setInt(++index, faceMarking);
            ps.setInt(++index, ears);
            ps.setInt(++index, tail);
            ps.setInt(++index, weaponPoint);
            if (!fromcs && map != null) {
                if (map.getForcedReturnId() != 999999999 && map.getForcedReturnMap() != null) {
                    ps.setInt(++index, map.getForcedReturnId());
                } else {
                    ps.setInt(++index, stats.getHp() < 1 ? map.getReturnMapId() : map.getId());
                }
            } else {
                ps.setInt(++index, mapid);
            }
            ps.setLong(++index, meso);
            ps.setShort(++index, hpApUsed);
            if (map == null) {
                ps.setByte(++index, (byte) 0);
            } else {
                final MaplePortal closest = map.findClosestSpawnpoint(getTruePosition());
                ps.setByte(++index, (byte) (closest != null ? closest.getId() : 0));
            }
            ps.setInt(++index, party == null ? -1 : party.getId());
            ps.setShort(++index, buddylist.getCapacity());
            final StringBuilder petz = new StringBuilder();
            int petLength = 0;
            for (final MaplePet pet : pets) {
                if (pet.getSummoned()) {
                    pet.saveToDb();
                    petz.append(pet.getInventoryPosition());
                    petz.append(",");
                    petLength++;
                }
            }
            while (petLength < 3) {
                petz.append("-1,");
                petLength++;
            }
            final String petstring = petz.toString();
            ps.setString(++index, petstring.substring(0, petstring.length() - 1));
            ps.setByte(++index, subcategory);
            //ps.setInt(++index, marriageId);
            ps.setInt(++index, currentrep);
            ps.setInt(++index, totalrep);
            ps.setInt(++index, gachexp);
            ps.setShort(++index, fatigue);
            ps.setInt(++index, traits.get(MapleTraitType.charm).getTotalExp());
            ps.setInt(++index, traits.get(MapleTraitType.charisma).getTotalExp());
            ps.setInt(++index, traits.get(MapleTraitType.craft).getTotalExp());
            ps.setInt(++index, traits.get(MapleTraitType.insight).getTotalExp());
            ps.setInt(++index, traits.get(MapleTraitType.sense).getTotalExp());
            ps.setInt(++index, traits.get(MapleTraitType.will).getTotalExp());
            ps.setInt(++index, totalWins);
            ps.setInt(++index, totalLosses);
            ps.setInt(++index, pvpExp);
            ps.setInt(++index, pvpPoints);
            /*
             * Start of Custom Features
             */
            ps.setInt(++index, reborns);
            ps.setInt(++index, apstorage);
            /*
             * End of Custom Features
             */
            ps.setInt(++index, elf);
            ps.setInt(++index, honourExp);
            ps.setInt(++index, honorLevel);
            ps.setString(++index, friendshippoints[0] + "," + friendshippoints[1] + "," + friendshippoints[2] + "," + friendshippoints[3] + "," + friendshippoints[4]);
            ps.setInt(++index, friendshiptoadd);
            ps.setShort(++index, chattype);
            ps.setString(++index, name);
            ps.setInt(++index, id);
            if (ps.executeUpdate() < 1) {
                ps.close();
                throw new DatabaseException("Character not in database (" + id + ")");
            }
            ps.close();
            deleteWhereCharacterId(con, "DELETE FROM stolen WHERE characterid = ?");
            for (Pair<Integer, Boolean> st : stolenSkills) {
                ps = con.prepareStatement("INSERT INTO stolen (characterid, skillid, chosen) VALUES (?, ?, ?)");
                ps.setInt(1, id);
                ps.setInt(2, st.left);
                ps.setInt(3, st.right ? 1 : 0);
                ps.execute();
                ps.close();
            }

            if (changed_skillmacros) {
                deleteWhereCharacterId(con, "DELETE FROM skillmacros WHERE characterid = ?");
                for (int i = 0; i < 5; i++) {
                    final SkillMacro macro = skillMacros[i];
                    if (macro != null) {
                        ps = con.prepareStatement("INSERT INTO skillmacros (characterid, skill1, skill2, skill3, name, shout, position) VALUES (?, ?, ?, ?, ?, ?, ?)");
                        ps.setInt(1, id);
                        ps.setInt(2, macro.getSkill1());
                        ps.setInt(3, macro.getSkill2());
                        ps.setInt(4, macro.getSkill3());
                        ps.setString(5, macro.getName());
                        ps.setInt(6, macro.getShout());
                        ps.setInt(7, i);
                        ps.execute();
                        ps.close();
                    }
                }
            }
            deleteWhereCharacterId(con, "DELETE FROM inventoryslot WHERE characterid = ?");
            ps = con.prepareStatement("INSERT INTO inventoryslot (characterid, `equip`, `use`, `setup`, `etc`, `cash`) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setInt(1, id);
            ps.setByte(2, getInventory(MapleInventoryType.EQUIP).getSlotLimit());
            ps.setByte(3, getInventory(MapleInventoryType.USE).getSlotLimit());
            ps.setByte(4, getInventory(MapleInventoryType.SETUP).getSlotLimit());
            ps.setByte(5, getInventory(MapleInventoryType.ETC).getSlotLimit());
            ps.setByte(6, getInventory(MapleInventoryType.CASH).getSlotLimit());
            ps.execute();
            ps.close();

            saveInventory(con);

            if (changed_questinfo) {
                deleteWhereCharacterId(con, "DELETE FROM questinfo WHERE characterid = ?");
                ps = con.prepareStatement("INSERT INTO questinfo (`characterid`, `quest`, `customData`) VALUES (?, ?, ?)");
                ps.setInt(1, id);
                for (final Entry<Integer, String> q : questinfo.entrySet()) {
                    ps.setInt(2, q.getKey());
                    ps.setString(3, q.getValue());
                    ps.execute();
                }
                ps.close();
            }

            deleteWhereCharacterId(con, "DELETE FROM queststatus WHERE characterid = ?");
            ps = con.prepareStatement("INSERT INTO queststatus (`queststatusid`, `characterid`, `quest`, `status`, `time`, `forfeited`, `customData`) VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)", DatabaseConnection.RETURN_GENERATED_KEYS);
            pse = con.prepareStatement("INSERT INTO queststatusmobs VALUES (DEFAULT, ?, ?, ?)");
            ps.setInt(1, id);
            for (final MapleQuestStatus q : quests.values()) {
                ps.setInt(2, q.getQuest().getId());
                ps.setInt(3, q.getStatus());
                ps.setInt(4, (int) (q.getCompletionTime() / 1000));
                ps.setInt(5, q.getForfeited());
                ps.setString(6, q.getCustomData());
                ps.execute();
                rs = ps.getGeneratedKeys();
                if (q.hasMobKills()) {
                    rs.next();
                    for (int mob : q.getMobKills().keySet()) {
                        pse.setInt(1, rs.getInt(1));
                        pse.setInt(2, mob);
                        pse.setInt(3, q.getMobKills(mob));
                        pse.execute();
                    }
                }
                rs.close();
            }
            ps.close();
            pse.close();

            if (changed_skills) {
                deleteWhereCharacterId(con, "DELETE FROM skills WHERE characterid = ?");
                ps = con.prepareStatement("INSERT INTO skills (characterid, skillid, skilllevel, masterlevel, expiration) VALUES (?, ?, ?, ?, ?)");
                ps.setInt(1, id);

                for (final Entry<Skill, SkillEntry> skill : skills.entrySet()) {
                    if (GameConstants.isApplicableSkill(skill.getKey().getId())) { //do not save additional skills
                        ps.setInt(2, skill.getKey().getId());
                        ps.setInt(3, skill.getValue().skillLevel);
                        ps.setByte(4, skill.getValue().masterlevel);
                        ps.setLong(5, skill.getValue().expiration);
                        ps.execute();
                    }
                }
                ps.close();
            }

            deleteWhereCharacterId(con, "DELETE FROM coreauras WHERE cid = ?");
            ps = con.prepareStatement("INSERT INTO coreauras (cid, str, dex, `int`, luk, att, magic, total, expire) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setInt(1, id);
            ps.setInt(2, getCoreAura().getStr());
            ps.setInt(3, getCoreAura().getDex());
            ps.setInt(4, getCoreAura().getInt());
            ps.setInt(5, getCoreAura().getLuk());
            ps.setInt(6, getCoreAura().getAtt());
            ps.setInt(7, getCoreAura().getMagic());
            ps.setInt(8, getCoreAura().getTotal());
            ps.setInt(9, getCoreAura().getExpire());
            ps.execute();
            ps.close();

            if (innerskill_changed) {
                if (innerSkills != null) {
                    deleteWhereCharacterId(con, "DELETE FROM inner_ability_skills WHERE player_id = ?");
                    ps = con.prepareStatement("INSERT INTO inner_ability_skills (player_id, skill_id, skill_level, max_level, rank, locked) VALUES (?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, id);

                    for (int i = 0; i < innerSkills.size(); ++i) {
                        ps.setInt(2, innerSkills.get(i).getSkillId());
                        ps.setInt(3, innerSkills.get(i).getSkillLevel());
                        ps.setInt(4, innerSkills.get(i).getMaxLevel());
                        ps.setInt(5, innerSkills.get(i).getRank());
                        ps.setBoolean(6, innerSkills.get(i).isLocked());
                        ps.executeUpdate();
                    }
                    ps.close();
                }
            }

            List<MapleCoolDownValueHolder> cd = getCooldowns();
            if (dc && cd.size() > 0) {
                ps = con.prepareStatement("INSERT INTO skills_cooldowns (charid, SkillID, StartTime, length) VALUES (?, ?, ?, ?)");
                ps.setInt(1, getId());
                for (final MapleCoolDownValueHolder cooling : cd) {
                    ps.setInt(2, cooling.skillId);
                    ps.setLong(3, cooling.startTime);
                    ps.setLong(4, cooling.length);
                    ps.execute();
                }
                ps.close();
            }

            if (changed_savedlocations) {
                deleteWhereCharacterId(con, "DELETE FROM savedlocations WHERE characterid = ?");
                ps = con.prepareStatement("INSERT INTO savedlocations (characterid, `locationtype`, `map`) VALUES (?, ?, ?)");
                ps.setInt(1, id);
                for (final SavedLocationType savedLocationType : SavedLocationType.values()) {
                    if (savedLocations[savedLocationType.getValue()] != -1) {
                        ps.setInt(2, savedLocationType.getValue());
                        ps.setInt(3, savedLocations[savedLocationType.getValue()]);
                        ps.execute();
                    }
                }
                ps.close();
            }

            if (changed_reports) {
                deleteWhereCharacterId(con, "DELETE FROM reports WHERE characterid = ?");
                ps = con.prepareStatement("INSERT INTO reports VALUES(DEFAULT, ?, ?, ?)");
                for (Entry<ReportType, Integer> achid : reports.entrySet()) {
                    ps.setInt(1, id);
                    ps.setByte(2, achid.getKey().i);
                    ps.setInt(3, achid.getValue());
                    ps.execute();
                }
                ps.close();
            }

            if (buddylist.changed()) {
                deleteWhereCharacterId(con, "DELETE FROM buddies WHERE characterid = ?");
                ps = con.prepareStatement("INSERT INTO buddies (characterid, `buddyid`, `pending`) VALUES (?, ?, ?)");
                ps.setInt(1, id);
                for (BuddylistEntry entry : buddylist.getBuddies()) {
                    ps.setInt(2, entry.getCharacterId());
                    ps.setInt(3, entry.isVisible() ? 0 : 1);
                    ps.execute();
                }
                ps.close();
                buddylist.setChanged(false);
            }

            ps = con.prepareStatement("UPDATE accounts SET `nxCredit` = ?, `ACash` = ?, `mPoints` = ?, `points` = ?, `vpoints` = ?, `dpoints` = ?, `epoints` = ?, `lastWorld` = ? WHERE id = ?");
            ps.setInt(1, nxcredit);
            ps.setInt(2, acash);
            ps.setInt(3, maplepoints);
            ps.setInt(4, points);
            ps.setInt(5, vpoints);
            ps.setInt(6, dpoints);
            ps.setInt(7, epoints);
            //ps.setInt(8, client.getAccID());
            ps.setByte(8, lastWorld);
            ps.setInt(9, client.getAccID());
            ps.executeUpdate();
            ps.close();

            if (storage != null) {
                storage.saveToDB();
            }
            if (cs != null) {
                cs.save();
            }
            if (PlayerNPC.Auto_Update) {
                PlayerNPC.updateByCharId(this);
            }
            keylayout.saveKeys(id);
            mount.saveMount(id);
            monsterbook.saveCards(accountid);

            deleteWhereCharacterId(con, "DELETE FROM familiars WHERE characterid = ?");
            ps = con.prepareStatement("INSERT INTO familiars (characterid, expiry, name, fatigue, vitality, familiar) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setInt(1, id);
            for (MonsterFamiliar f : familiars.values()) {
                ps.setLong(2, f.getExpiry());
                ps.setString(3, f.getName());
                ps.setInt(4, f.getFatigue());
                ps.setByte(5, f.getVitality());
                ps.setInt(6, f.getFamiliar());
                ps.executeUpdate();
            }
            ps.close();

            deleteWhereCharacterId(con, "DELETE FROM imps WHERE characterid = ?");
            ps = con.prepareStatement("INSERT INTO imps (characterid, itemid, closeness, fullness, state, level) VALUES (?, ?, ?, ?, ?, ?)");
            ps.setInt(1, id);
            for (MapleImp imp : imps) {
                if (imp != null) {
                    ps.setInt(2, imp.getItemId());
                    ps.setShort(3, imp.getCloseness());
                    ps.setShort(4, imp.getFullness());
                    ps.setByte(5, imp.getState());
                    ps.setByte(6, imp.getLevel());
                    ps.executeUpdate();
                }
            }
            ps.close();
            if (changed_wishlist) {
                deleteWhereCharacterId(con, "DELETE FROM wishlist WHERE characterid = ?");
                for (int i = 0; i < getWishlistSize(); i++) {
                    ps = con.prepareStatement("INSERT INTO wishlist(characterid, sn) VALUES(?, ?) ");
                    ps.setInt(1, getId());
                    ps.setInt(2, wishlist[i]);
                    ps.execute();
                    ps.close();
                }
            }
            if (changed_trocklocations) {
                deleteWhereCharacterId(con, "DELETE FROM trocklocations WHERE characterid = ?");
                for (int i = 0; i < rocks.length; i++) {
                    if (rocks[i] != 999999999) {
                        ps = con.prepareStatement("INSERT INTO trocklocations(characterid, mapid) VALUES(?, ?) ");
                        ps.setInt(1, getId());
                        ps.setInt(2, rocks[i]);
                        ps.execute();
                        ps.close();
                    }
                }
            }

            if (changed_regrocklocations) {
                deleteWhereCharacterId(con, "DELETE FROM regrocklocations WHERE characterid = ?");
                for (int i = 0; i < regrocks.length; i++) {
                    if (regrocks[i] != 999999999) {
                        ps = con.prepareStatement("INSERT INTO regrocklocations(characterid, mapid) VALUES(?, ?) ");
                        ps.setInt(1, getId());
                        ps.setInt(2, regrocks[i]);
                        ps.execute();
                        ps.close();
                    }
                }
            }
            if (changed_hyperrocklocations) {
                deleteWhereCharacterId(con, "DELETE FROM hyperrocklocations WHERE characterid = ?");
                for (int i = 0; i < hyperrocks.length; i++) {
                    if (hyperrocks[i] != 999999999) {
                        ps = con.prepareStatement("INSERT INTO hyperrocklocations(characterid, mapid) VALUES(?, ?) ");
                        ps.setInt(1, getId());
                        ps.setInt(2, hyperrocks[i]);
                        ps.execute();
                        ps.close();
                    }
                }
            }
            if (changed_extendedSlots) {
                deleteWhereCharacterId(con, "DELETE FROM extendedSlots WHERE characterid = ?");
                for (int i : extendedSlots) {
                    if (getInventory(MapleInventoryType.ETC).findById(i) != null) { //just in case
                        ps = con.prepareStatement("INSERT INTO extendedSlots(characterid, itemId) VALUES(?, ?) ");
                        ps.setInt(1, getId());
                        ps.setInt(2, i);
                        ps.execute();
                        ps.close();
                    }
                }
            }
            changed_wishlist = false;
            changed_trocklocations = false;
            changed_regrocklocations = false;
            changed_hyperrocklocations = false;
            changed_skillmacros = false;
            changed_savedlocations = false;
            changed_questinfo = false;
            changed_extendedSlots = false;
            changed_skills = false;
            changed_reports = false;
            con.commit();
        } catch (SQLException | DatabaseException e) {
            FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
            System.err.println(MapleClient.getLogMessage(this, "[儲存角色] 儲存角色出錯:") + e);
            try {
                con.rollback();
            } catch (SQLException ex) {
                FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, ex);
                System.err.println(MapleClient.getLogMessage(this, "[charsave] Error Rolling Back") + e);
            }
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (pse != null) {
                    pse.close();
                }
                if (rs != null) {
                    rs.close();
                }
                con.setAutoCommit(true);
                con.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            } catch (SQLException e) {
                FileoutputUtil.outputFileError(FileoutputUtil.PacketEx_Log, e);
                System.err.println(MapleClient.getLogMessage(this, "[charsave] Error going back to autocommit mode") + e);
            }
        }
    }

    private void deleteWhereCharacterId(Connection con, String sql) throws SQLException {
        deleteWhereCharacterId(con, sql, id);
    }

    public static void deleteWhereCharacterId(Connection con, String sql, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static void deleteWhereCharacterId_NoLock(Connection con, String sql, int id) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
        }
    }

    public void saveInventory(final Connection con) throws SQLException {
        List<Pair<Item, MapleInventoryType>> listing = new ArrayList<>();
        for (final MapleInventory iv : inventory) {
            for (final Item item : iv.list()) {
                listing.add(new Pair<>(item, iv.getType()));
            }
        }
        if (con != null) {
            ItemLoader.INVENTORY.saveItems(listing, con, id);
        } else {
            ItemLoader.INVENTORY.saveItems(listing, id);
        }
    }

    public final PlayerStats getStat() {
        return stats;
    }

    public final void QuestInfoPacket(final tools.data.MaplePacketLittleEndianWriter mplew) {
        if (MapleJob.is幻獸師(getJob())) {
            int beast = MapleJob.is幻獸師(getJob()) ? 1 : 0;
            String ears = Integer.toString(getEars());
            String tail = Integer.toString(getTail());
            questinfo.put(59300, "bTail=" + beast + ";bEar=" + beast + ";TailID=" + tail + ";EarID=" + ears);
        }
        mplew.writeShort(questinfo.size()); // // Party Quest data (quest needs to be added in the quests list)
        for (final Entry<Integer, String> q : questinfo.entrySet()) {
            mplew.writeShort(q.getKey());
            mplew.writeMapleAsciiString(q.getValue() == null ? "" : q.getValue());
        }
    }

    public final void updateInfoQuest(final int questid, final String data) {
        questinfo.put(questid, data);
        changed_questinfo = true;
        client.getSession().write(InfoPacket.updateInfoQuest(questid, data));
    }

    public final String getInfoQuest(final int questid) {
        if (questinfo.containsKey(questid)) {
            return questinfo.get(questid);
        }
        return "";
    }

    public final void clearInfoQuest(final int questid) {
        if (questinfo.containsKey(questid)) {
            updateInfoQuest(questid, "");
            questinfo.remove(questid);
        }
    }

    public final int getNumQuest() {
        int i = 0;
        for (final MapleQuestStatus q : quests.values()) {
            if (q.getStatus() == 2 && !(q.isCustom())) {
                i++;
            }
        }
        return i;
    }

    public final byte getQuestStatus(final int quest) {
        final MapleQuest qq = MapleQuest.getInstance(quest);
        if (getQuestNoAdd(qq) == null) {
            return 0;
        }
        return getQuestNoAdd(qq).getStatus();
    }

    public final MapleQuestStatus getQuest(final MapleQuest quest) {
        if (!quests.containsKey(quest)) {
            return new MapleQuestStatus(quest, (byte) 0);
        }
        return quests.get(quest);
    }

    public final void setQuestAdd(final MapleQuest quest, final byte status, final String customData) {
        if (!quests.containsKey(quest)) {
            final MapleQuestStatus stat = new MapleQuestStatus(quest, status);
            stat.setCustomData(customData);
            quests.put(quest, stat);
        }
    }

    public final MapleQuestStatus getQuestNAdd(final MapleQuest quest) {
        if (!quests.containsKey(quest)) {
            final MapleQuestStatus status = new MapleQuestStatus(quest, (byte) 0);
            quests.put(quest, status);
            return status;
        }
        return quests.get(quest);
    }

    public final MapleQuestStatus getQuestNoAdd(final MapleQuest quest) {
        return quests.get(quest);
    }

    public final MapleQuestStatus getQuestRemove(final MapleQuest quest) {
        return quests.remove(quest);
    }

    public final void updateQuest(final MapleQuestStatus quest) {
        updateQuest(quest, false);
    }

    public final void updateQuest(final MapleQuestStatus quest, final boolean update) {
        quests.put(quest.getQuest(), quest);
        if (!(quest.isCustom())) {
            client.getSession().write(InfoPacket.updateQuest(quest));
            if (quest.getStatus() == 1 && !update) {
                client.getSession().write(CField.updateQuestInfo(this, quest.getQuest().getId(), quest.getNpc(), (byte) 11));//was10
            }
        }
    }

    public final Map<Integer, String> getInfoQuest_Map() {
        return questinfo;
    }

    public final Map<MapleQuest, MapleQuestStatus> getQuest_Map() {
        return quests;
    }
    
    public MapleBuffStatValueHolder getmbsvh(MapleBuffStat effect) {
        for(SpecialBuffInfo x : stack_effects) {
            if(x.m_buff == effect) {
                return x.mbsvh;
            }
        }
        return null;
    }
    
    public Integer getStackBuffedValue(MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = getmbsvh(effect);
        return mbsvh == null ? null : mbsvh.value;
    }
    
    public Integer getBuffedValue(MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        return mbsvh == null ? null : mbsvh.value;
    }

    public final Integer getBuffedSkill_X(final MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return null;
        }
        return mbsvh.effect.getX();
    }

    public final Integer getBuffedSkill_Y(final MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return null;
        }
        return mbsvh.effect.getY();
    }

    public boolean isBuffFrom(MapleBuffStat stat, Skill skill) {
        final MapleBuffStatValueHolder mbsvh = effects.get(stat);
        if (mbsvh == null || mbsvh.effect == null) {
            return false;
        }
        return mbsvh.effect.isSkill() && mbsvh.effect.getSourceId() == skill.getId();
    }

    public int getBuffSource(MapleBuffStat stat) {
        final MapleBuffStatValueHolder mbsvh = effects.get(stat);
        return mbsvh == null ? -1 : mbsvh.effect.getSourceId();
    }

    public int getTrueBuffSource(MapleBuffStat stat) {
        final MapleBuffStatValueHolder mbsvh = effects.get(stat);
        return mbsvh == null ? -1 : (mbsvh.effect.isSkill() ? mbsvh.effect.getSourceId() : -mbsvh.effect.getSourceId());
    }

    public int getItemQuantity(int itemid, boolean checkEquipped) {
        int possesed = inventory[GameConstants.getInventoryType(itemid).ordinal()].countById(itemid);
        if (checkEquipped) {
            possesed += inventory[MapleInventoryType.EQUIPPED.ordinal()].countById(itemid);
        }
        return possesed;
    }

    public void setBuffedValue(MapleBuffStat effect, int value) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return;
        }
        mbsvh.value = value;
    }

    public void setSchedule(MapleBuffStat effect, ScheduledFuture<?> sched) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        if (mbsvh == null) {
            return;
        }
        mbsvh.schedule.cancel(false);
        mbsvh.schedule = sched;
    }

    public Long getBuffedStarttime(MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        return mbsvh == null ? null : mbsvh.startTime;
    }

    public MapleStatEffect getStatForBuff(MapleBuffStat effect) {
        final MapleBuffStatValueHolder mbsvh = effects.get(effect);
        return mbsvh == null ? null : mbsvh.effect;
    }

    public void doDragonBlood() {
        final MapleStatEffect bloodEffect = getStatForBuff(MapleBuffStat.DRAGONBLOOD);
        if (bloodEffect == null) {
            lastDragonBloodTime = 0;
            return;
        }
        prepareDragonBlood();
        if (stats.getHp() - bloodEffect.getX() <= 1) {
            cancelBuffStats(MapleBuffStat.DRAGONBLOOD);

        } else {
            addHP(-bloodEffect.getX());
            client.getSession().write(EffectPacket.showBuffEffect(bloodEffect.getSourceId(), SpecialEffectType.ITEM_OPERATION, getLevel(), bloodEffect.getLevel()));
            map.broadcastMessage(MapleCharacter.this, EffectPacket.showBuffeffect(getId(), bloodEffect.getSourceId(), SpecialEffectType.ITEM_OPERATION, getLevel(), bloodEffect.getLevel()), false);
        }
    }

    public final boolean canBlood(long now) {
        return lastDragonBloodTime > 0 && lastDragonBloodTime + 4000 < now;
    }

    private void prepareDragonBlood() {
        lastDragonBloodTime = System.currentTimeMillis();
    }

    public void doRecovery() {
        MapleStatEffect bloodEffect = getStatForBuff(MapleBuffStat.RECOVERY);
        if (bloodEffect == null) {
            bloodEffect = getStatForBuff(MapleBuffStat.MECH_CHANGE);
            if (bloodEffect == null) {
                lastRecoveryTime = 0;
            } else if (bloodEffect.getSourceId() == 35121005) {
                prepareRecovery();
                if (stats.getMp() < bloodEffect.getU()) {
                    cancelEffectFromBuffStat(MapleBuffStat.MONSTER_RIDING);
                    cancelEffectFromBuffStat(MapleBuffStat.MECH_CHANGE);
                } else {
                    addMP(-bloodEffect.getU());
                }
            }
        } else {
            prepareRecovery();
            if (stats.getHp() >= stats.getCurrentMaxHp()) {
                cancelEffectFromBuffStat(MapleBuffStat.RECOVERY);
            } else {
                healHP(bloodEffect.getX());
            }
        }
    }

    public final boolean canRecover(long now) {
        return lastRecoveryTime > 0 && lastRecoveryTime + 5000 < now;
    }

    private void prepareRecovery() {
        lastRecoveryTime = System.currentTimeMillis();
    }

    public void startMapTimeLimitTask(int time, final MapleMap to) {
        if (time <= 0) { //jail
            time = 1;
        }
        client.getSession().write(CField.getClock(time));
        final MapleMap ourMap = getMap();
        time *= 1000;
        mapTimeLimitTask = MapTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                if (ourMap.getId() == GameConstants.JAIL) {
                    getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_TIME)).setCustomData(String.valueOf(System.currentTimeMillis()));
                    getQuestNAdd(MapleQuest.getInstance(GameConstants.JAIL_QUEST)).setCustomData("0"); //release them!
                }
                changeMap(to, to.getPortal(0));
            }
        }, time, time);
    }

    public boolean canDOT(long now) {
        return lastDOTTime > 0 && lastDOTTime + 8000 < now;
    }

    public boolean hasDOT() {
        return dotHP > 0;
    }

    public void doDOT() {
        addHP(-(dotHP * 4));
        dotHP = 0;
        lastDOTTime = 0;
    }

    public void setDOT(int d, int source, int sourceLevel) {
        this.dotHP = d;
        addHP(-(dotHP * 4));
        map.broadcastMessage(CField.getPVPMist(id, source, sourceLevel, d));
        lastDOTTime = System.currentTimeMillis();
    }

    public void startFishingTask() {
        cancelFishingTask();
        lastFishingTime = System.currentTimeMillis();
    }

    public boolean canFish(long now) {
        return lastFishingTime > 0 && lastFishingTime + GameConstants.getFishingTime(false, isGM()) < now;
    }

    public void doFish(long now) {
        lastFishingTime = now;
        if (client == null || client.getPlayer() == null || !client.isReceiving() || (!haveItem(2270008, 1, false, true)) || !GameConstants.isFishingMap(getMapId()) || chair <= 0) {
            cancelFishingTask();
            return;
        }
        MapleInventoryManipulator.removeById(client, MapleInventoryType.USE, 2270008, 1, false, false);
        boolean passed = false;
        while (!passed) {
            int randval = RandomRewards.getFishingReward();
            switch (randval) {
                case 0: // Meso
                    final int money = Randomizer.rand(10, 50000);
                    gainMeso(money, true);
                    passed = true;
                    break;
                case 1: // EXP
                    final long experi = Randomizer.nextInt((int) Math.min((Math.abs(getNeededExp() / 200) + 1), 500000));
                    gainExp(experi, true, false, true);
                    passed = true;
                    break;
                default:
                    if (MapleItemInformationProvider.getInstance().itemExists(randval)) {
                        MapleInventoryManipulator.addById(client, randval, (short) 1, "Fishing" + " on " + FileoutputUtil.CurrentReadable_Date());
                        passed = true;
                    }
                    break;
            }
        }
    }

    public void cancelMapTimeLimitTask() {
        if (mapTimeLimitTask != null) {
            mapTimeLimitTask.cancel(false);
            mapTimeLimitTask = null;
        }
    }

    public long getNeededExp() {
        return GameConstants.getExpNeededForLevel(level);
    }

    public void cancelFishingTask() {
        lastFishingTime = 0;
    }

    public void registerEffect(MapleStatEffect effect, long starttime, ScheduledFuture<?> schedule, int from) {
        registerEffect(effect, starttime, schedule, effect.getStatups(), false, effect.getDuration(), from);
    }

    public void registerEffect(MapleStatEffect effect, long starttime, ScheduledFuture<?> schedule, Map<MapleBuffStat, Integer> statups, boolean silent, final int localDuration, final int cid) {
        if (effect.isHide()) {
            map.broadcastNONGMMessage(this, CField.removePlayerFromMap(getId()), false);
        } else if (effect.isDragonBlood()) {
            prepareDragonBlood();
        } else if (effect.isRecovery()) {
            prepareRecovery();
        } else if (effect.isBerserk()) {
            checkBerserk();
        } else if (effect.isMonsterRiding_()) {
            getMount().startSchedule();
        }
        int clonez = 0;
        for (Entry<MapleBuffStat, Integer> statup : statups.entrySet()) {
            if (statup.getKey() == MapleBuffStat.ILLUSION) {
                clonez = statup.getValue();
            }
            int value = statup.getValue();
            if (statup.getKey() == MapleBuffStat.MONSTER_RIDING) {
                if (effect.getSourceId() == 5221006 && battleshipHP <= 0) {
                    battleshipHP = maxBattleshipHP(effect.getSourceId()); //copy this as well
                }
                removeFamiliar();
            }
            
            if (statup.getKey().canStack() && !GameConstants.isSpecialStackBuff(statup.getKey())) {
                stack_effects.add(new SpecialBuffInfo(effect.getSourceId(), statup.getKey(), new MapleBuffStatValueHolder(effect, starttime, schedule, value, localDuration, cid)));
            } else {
                effects.put(statup.getKey(), new MapleBuffStatValueHolder(effect, starttime, schedule, value, localDuration, cid));
            }
        }
        if (clonez > 0) {
            int cloneSize = Math.max(getNumClones(), getCloneSize());
            if (clonez > cloneSize) { //how many clones to summon
                for (int i = 0; i < clonez - cloneSize; i++) { //1-1=0
                    cloneLook();
                }
            }
        }
        if (!silent) {
            stats.recalcLocalStats(this);
        }
        //System.out.println("Effect registered. Effect: " + effect.getSourceId());
        if (isShowInfo()) {
            showMessage(25, new StringBuilder().append("註冊一般BUFF效果 - 當前BUFF總數： ").append(this.effects.size()).append(" 技能： ").append(effect.getSourceId()).toString());
            showMessage(25, new StringBuilder().append("註冊堆疊BUFF效果 - 當前BUFF總數： ").append(this.stack_effects.size()).append(" 技能： ").append(effect.getSourceId()).toString());
        }
    }

    public List<SpecialBuffInfo> getStackBuffInfo(final MapleBuffStat buff) {
        final List<SpecialBuffInfo> sbi = new ArrayList<>();
        for (SpecialBuffInfo ss : stack_effects) {
            if (ss.getBuffStat() == buff) {
                sbi.add(ss);
            }
        }
        return sbi;
    }

    public List<SpecialBuffInfo> getSpecialBuffInfo() {
        return stack_effects;
    }

    public List<MapleBuffStat> getBuffStats(final MapleStatEffect effect, final long startTime) {
        final List<MapleBuffStat> bstats = new ArrayList<>();
        final Map<MapleBuffStat, MapleBuffStatValueHolder> allBuffs = new EnumMap<>(effects);
        for (Entry<MapleBuffStat, MapleBuffStatValueHolder> stateffect : allBuffs.entrySet()) {
            final MapleBuffStatValueHolder mbsvh = stateffect.getValue();
            if (mbsvh.effect.sameSource(effect) && (startTime == -1 || startTime == mbsvh.startTime || stateffect.getKey().canStack())) {
                bstats.add(stateffect.getKey());
            }
        }
        return bstats;
    }

    private boolean deregisterBuffStats(List<MapleBuffStat> stats) {
        boolean clonez = false;
        List<MapleBuffStatValueHolder> effectsToCancel = new ArrayList<>(stats.size());
        for (MapleBuffStat stat : stats) {
            final MapleBuffStatValueHolder mbsvh = effects.remove(stat);
            if (mbsvh != null) {
                boolean addMbsvh = true;
                for (MapleBuffStatValueHolder contained : effectsToCancel) {
                    if (mbsvh.startTime == contained.startTime && contained.effect == mbsvh.effect) {
                        addMbsvh = false;
                    }
                }
                if (addMbsvh) {
                    effectsToCancel.add(mbsvh);
                }
                if (stat == MapleBuffStat.SUMMON || stat == MapleBuffStat.PUPPET || stat == MapleBuffStat.REAPER || stat == MapleBuffStat.BEHOLDER || stat == MapleBuffStat.DAMAGE_BUFF || stat == MapleBuffStat.RAINING_MINES || stat == MapleBuffStat.INDIE_PAD) {
                    final int summonId = mbsvh.effect.getSourceId();
                    final List<MapleSummon> toRemove = new ArrayList<>();
                    visibleMapObjectsLock.writeLock().lock(); //We need to lock this later on anyway so do it now to prevent deadlocks.
                    summonsLock.writeLock().lock();
                    try {
                        for (MapleSummon summon : summons) {
                            if (summon.getSkill() == summonId || (stat == MapleBuffStat.RAINING_MINES && summonId == 33101008) || (summonId == 35121009 && summon.getSkill() == 35121011) || ((summonId == 86 || summonId == 88 || summonId == 91 || summonId == 180 || summonId == 96) && summon.getSkill() == summonId + 999) || ((summonId == 1085 || summonId == 1087 || summonId == 1090 || summonId == 1179 || summonId == 1154) && summon.getSkill() == summonId - 999)) { //removes bots n tots
                                map.broadcastMessage(SummonPacket.removeSummon(summon, true));
                                map.removeMapObject(summon);
                                visibleMapObjects.remove(summon);
                                toRemove.add(summon);
                            }
                        }
                        for (MapleSummon s : toRemove) {
                            summons.remove(s);
                        }
                    } finally {
                        summonsLock.writeLock().unlock();
                        visibleMapObjectsLock.writeLock().unlock(); //lolwut
                    }
                    if (summonId == 3111005 || summonId == 3211005) {
                        cancelEffectFromBuffStat(MapleBuffStat.SPIRIT_LINK);
                    }
                } else if (stat == MapleBuffStat.DRAGONBLOOD) {
                    lastDragonBloodTime = 0;
                } else if (stat == MapleBuffStat.RECOVERY || mbsvh.effect.getSourceId() == 35121005) {
                    lastRecoveryTime = 0;
                } else if (stat == MapleBuffStat.HOMING_BEACON || stat == MapleBuffStat.MANY_USES) {
                    linkMobs.clear();
                } else if (stat == MapleBuffStat.ILLUSION) {
                    disposeClones();
                    clonez = true;
                }
            }
        }
        for (MapleBuffStatValueHolder cancelEffectCancelTasks : effectsToCancel) {
            if (getBuffStats(cancelEffectCancelTasks.effect, cancelEffectCancelTasks.startTime).isEmpty()) {
                if (cancelEffectCancelTasks.schedule != null) {
                    cancelEffectCancelTasks.schedule.cancel(false);
                }
            }
        }
        return clonez;
    }

    /**
     * @param effect
     * @param overwrite when overwrite is set no data is sent and all the
     * Buffstats in the StatEffect are deregistered
     * @param startTime
     */
    public void cancelEffect(final MapleStatEffect effect, final boolean overwrite, final long startTime) {
        if (effect == null) {
            return;
        }
        cancelEffect(effect, overwrite, startTime, effect.getStatups());
    }

    public void cancelEffect(final MapleStatEffect effect, final boolean overwrite, final long startTime, Map<MapleBuffStat, Integer> statups) {
        if (effect == null) {
            return;
        }
        List<MapleBuffStat> buffstats;
        if (!overwrite) {
            buffstats = getBuffStats(effect, startTime);
        } else {
            buffstats = new ArrayList<>(statups.keySet());
        }
        if (buffstats.size() <= 0 && stack_effects.isEmpty()) {
            return;
        }
        if (effect.isInfinity() && getBuffedValue(MapleBuffStat.INFINITY) != null) { //before
            int duration = Math.max(effect.getDuration(), effect.alchemistModifyVal(this, effect.getDuration(), false));
            final long start = getBuffedStarttime(MapleBuffStat.INFINITY);
            duration += (int) ((start - System.currentTimeMillis()));
            if (duration > 0) {
                final int neworbcount = getBuffedValue(MapleBuffStat.INFINITY) + effect.getDamage();
                final Map<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.INFINITY, neworbcount);
                setBuffedValue(MapleBuffStat.INFINITY, neworbcount);
                client.getSession().write(BuffPacket.giveBuff(effect.getSourceId(), duration, stat, effect, this));
                addHP((int) (effect.getHpR() * this.stats.getCurrentMaxHp()));
                addMP((int) (effect.getMpR() * this.stats.getCurrentMaxMp(this.getJob())));
                setSchedule(MapleBuffStat.INFINITY, BuffTimer.getInstance().schedule(new CancelEffectAction(this, effect, start, stat), effect.alchemistModifyVal(this, 4000, false)));
                return;
            }
        }
        final boolean clonez = deregisterBuffStats(buffstats);

        //清除堆疊Buff
        for (Iterator<SpecialBuffInfo> it = stack_effects.iterator(); it.hasNext();) {
            SpecialBuffInfo ss = it.next();
            if (ss.getBuffStatValueHolder().effect.sameSource(effect)) {
                ss.getBuffStatValueHolder().schedule.cancel(false);
                buffstats.add(ss.getBuffStat());
                it.remove();
            }
            if (stack_effects.isEmpty()) {
                break;
            }
        }
        if (effect.isMagicDoor()) {
            // remove for all on maps
            if (!getDoors().isEmpty()) {
                removeDoor();
                silentPartyUpdate();
            }
        } else if (effect.isMechDoor()) {
            if (!getMechDoors().isEmpty()) {
                removeMechDoor();
            }
        } else if (effect.isMonsterRiding_()) {
            getMount().cancelSchedule();
        } else if (effect.isMonsterRiding()) {
            cancelEffectFromBuffStat(MapleBuffStat.MECH_CHANGE);
        } else if (effect.isAranCombo()) {
            combo = 0;
        }
        // check if we are still logged in o.o
        cancelPlayerBuffs(buffstats, overwrite);
        if (!overwrite) {
            if (effect.isHide() && client.getChannelServer().getPlayerStorage().getCharacterById(this.getId()) != null) { //Wow this is so fking hacky...
                map.broadcastMessage(this, CField.spawnPlayerMapobject(this), false);
                map.broadcastMessage(this, CField.getEffectSwitch(getId(), getEffectSwitch()), true);

                for (final MaplePet pet : pets) {
                    if (pet.getSummoned()) {
                        map.broadcastMessage(this, PetPacket.showPet(this, pet, false, false), false);
                    }
                }
                for (final WeakReference<MapleCharacter> chr : clones) {
                    if (chr.get() != null) {
                        map.broadcastMessage(chr.get(), CField.spawnPlayerMapobject(chr.get()), false);
                        map.broadcastMessage(this, CField.getEffectSwitch(getId(), getEffectSwitch()), true);
                    }
                }
            }
        }
        if (effect.getSourceId() == 35121013 && !overwrite) { //when siege 2 deactivates, missile re-activates
            SkillFactory.getSkill(35121005).getEffect(getTotalSkillLevel(35121005)).applyTo(this);
        }
        if (!clonez) {
            for (WeakReference<MapleCharacter> chr : clones) {
                if (chr.get() != null) {
                    chr.get().cancelEffect(effect, overwrite, startTime);
                }
            }
        }
        if (isShowInfo()) {
            dropMessage(5, new StringBuilder().append("取消一般BUFF效果 - 當前BUFF總數：").append(this.effects.size()).append(" 技能：").append(effect.getSourceId()).toString());
            dropMessage(5, new StringBuilder().append("取消堆疊BUFF效果 - 當前BUFF總數：").append(this.stack_effects.size()).append(" 技能：").append(effect.getSourceId()).toString());
        }
        //System.out.println("Effect deregistered. Effect: " + effect.getSourceId());
    }

    public void cancelBuffStats(MapleBuffStat... stat) {
        List<MapleBuffStat> buffStatList = Arrays.asList(stat);
        deregisterBuffStats(buffStatList);
        cancelPlayerBuffs(buffStatList, false);
    }

    public void cancelEffectFromBuffStat(MapleBuffStat stat) {
        if (effects.get(stat) != null) {
            cancelEffect(effects.get(stat).effect, false, -1);
        }
    }

    public void cancelEffectFromBuffStat(MapleBuffStat stat, int from) {
        if (effects.get(stat) != null && effects.get(stat).cid == from) {
            cancelEffect(effects.get(stat).effect, false, -1);
        }
    }

    private void cancelPlayerBuffs(List<MapleBuffStat> buffstats, boolean overwrite) {
        boolean write = client != null && client.getChannelServer() != null && client.getChannelServer().getPlayerStorage().getCharacterById(getId()) != null;
        if (buffstats.contains(MapleBuffStat.HOMING_BEACON)) {
            client.getSession().write(BuffPacket.cancelHoming());
        } else {
            if (overwrite) {
                List<MapleBuffStat> z = new ArrayList<>();
                for (MapleBuffStat s : buffstats) {
                    if (s.canStack()) {
                        z.add(s);
                    }
                }
                if (z.size() > 0) {
                    buffstats = z;
                } else {
                    return; //don't write anything
                }
            } else if (write) {
                stats.recalcLocalStats(this);
            }
            if (isShowInfo()) {
                dropMessage(5, new StringBuilder().append("取消BUFF效果 - 發送封包 - 是否註冊BUFF時：").append(overwrite).toString());
            }
            client.getSession().write(BuffPacket.cancelBuff(buffstats, this));
            map.broadcastMessage(this, BuffPacket.cancelForeignBuff(getId(), buffstats), false);
        }
    }

    public void dispel() {
        if (!isHidden()) {
            final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());
            for (MapleBuffStatValueHolder mbsvh : allBuffs) {
                if (mbsvh.effect.isSkill() && mbsvh.schedule != null && !mbsvh.effect.isMorph() && !mbsvh.effect.isGmBuff() && !mbsvh.effect.isMonsterRiding() && !mbsvh.effect.isMechChange() && !mbsvh.effect.isEnergyCharge() && !mbsvh.effect.isAranCombo()) {
                    cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                }
            }
        }
    }

    public void dispelSkill(int skillid) {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isSkill() && mbsvh.effect.getSourceId() == skillid) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                break;
            }
        }
    }

    public void dispelSummons() {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.getSummonMovementType() != null) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
            }
        }
    }

    public void dispelBuff(int skillid) {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.getSourceId() == skillid) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                break;
            }
        }
    }

    public void cancelAllBuffs_() {
        effects.clear();
        stack_effects.clear();
    }

    public void cancelAllBuffs() {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            cancelEffect(mbsvh.effect, false, mbsvh.startTime);
        }
    }

    public void cancelMorphs() {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            switch (mbsvh.effect.getSourceId()) {
                case 5111005:
                case 5121003:
                case 15111002:
                case 13111005:
                    return; // Since we can't have more than 1, save up on loops
                default:
                    if (mbsvh.effect.isMorph()) {
                        if (mbsvh.effect.isMorph()) {
                            disposeClones();
                            cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                        }
                    }
            }
        }
    }

    public int getMorphState() {
        LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());
        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isMorph()) {
                return mbsvh.effect.getSourceId();
            }
        }
        return -1;
    }

    public void silentGiveBuffs(List<PlayerBuffValueHolder> buffs) {
        if (buffs == null) {
            return;
        }
        for (PlayerBuffValueHolder mbsvh : buffs) {
            mbsvh.effect.silentApplyBuff(this, mbsvh.startTime, mbsvh.localDuration, mbsvh.statup, mbsvh.cid);
        }
    }

    public void silentGiveStackBuffs(List<SpecialBuffInfo> buffs) {
        if (buffs == null) {
            return;
        }
        for (SpecialBuffInfo mbsvh : buffs) {
            mbsvh.getBuffStatValueHolder().effect.silentApplyBuff(this, mbsvh.getBuffStatValueHolder().startTime, mbsvh.getBuffStatValueHolder().localDuration, mbsvh.getBuffStatValueHolder().effect.getStatups(), mbsvh.getBuffStatValueHolder().cid);
        }
    }

    public List<PlayerBuffValueHolder> getAllBuffs() {
        final List<PlayerBuffValueHolder> ret = new ArrayList<>();
        final Map<Pair<Integer, Byte>, Integer> alreadyDone = new HashMap<>();
        final LinkedList<Entry<MapleBuffStat, MapleBuffStatValueHolder>> allBuffs = new LinkedList<>(effects.entrySet());
        for (Entry<MapleBuffStat, MapleBuffStatValueHolder> mbsvh : allBuffs) {
            final Pair<Integer, Byte> key = new Pair<>(mbsvh.getValue().effect.getSourceId(), mbsvh.getValue().effect.getLevel());
            if (alreadyDone.containsKey(key)) {
                ret.get(alreadyDone.get(key)).statup.put(mbsvh.getKey(), mbsvh.getValue().value);
            } else {
                alreadyDone.put(key, ret.size());
                final EnumMap<MapleBuffStat, Integer> list = new EnumMap<>(MapleBuffStat.class);
                list.put(mbsvh.getKey(), mbsvh.getValue().value);
                ret.add(new PlayerBuffValueHolder(mbsvh.getValue().startTime, mbsvh.getValue().effect, list, mbsvh.getValue().localDuration, mbsvh.getValue().cid));
            }
        }
        return ret;
    }

    public void cancelMagicDoor() {
        final LinkedList<MapleBuffStatValueHolder> allBuffs = new LinkedList<>(effects.values());

        for (MapleBuffStatValueHolder mbsvh : allBuffs) {
            if (mbsvh.effect.isMagicDoor()) {
                cancelEffect(mbsvh.effect, false, mbsvh.startTime);
                break;
            }
        }
    }

    public int getSkillLevel(int skillid) {
        return getSkillLevel(SkillFactory.getSkill(skillid));
    }

    public int getTotalSkillLevel(int skillid) {
        return getTotalSkillLevel(SkillFactory.getSkill(skillid));
    }

    public final void handleEnergyCharge(int skillid, int targets) {
        Skill echskill = SkillFactory.getSkill(skillid);
        int skilllevel = getTotalSkillLevel(echskill);
        if (skilllevel > 0) {
            MapleStatEffect echeff = echskill.getEffect(skilllevel);
            if (targets > 0) {
                if (skillid != 15001022) {
                    if (getBuffedValue(MapleBuffStat.ENERGY_CHARGE) == null) {
                        echeff.applyEnergyBuff(this, true, targets);
                    } else {
                        Integer energyLevel = getBuffedValue(MapleBuffStat.ENERGY_CHARGE);

                        if (energyLevel < 10000) {
                            energyLevel = energyLevel + echeff.getX() * targets;

                            this.client.getSession().write(CField.EffectPacket.showBuffEffect(skillid, SpecialEffectType.REMOTE_SKILL, getLevel(), skilllevel));
                            this.map.broadcastMessage(this, CField.EffectPacket.showBuffeffect(this.id, skillid, SpecialEffectType.REMOTE_SKILL, getLevel(), skilllevel), false);

                            if (energyLevel >= 10000) {
                                energyLevel = 10000;
                            }
                            final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                            stat.put(MapleBuffStat.ENERGY_CHARGE, energyLevel);
//                        this.client.getSession().write(CWvsContext.BuffPacket.giveEnergyChargeTest(energyLevel, echeff.getDuration() / 1000));
                            this.client.getSession().write(CWvsContext.BuffPacket.giveBuff(0, 0, stat, echeff, this));
                            setBuffedValue(MapleBuffStat.ENERGY_CHARGE, energyLevel);
                        } else if (energyLevel == 10000) {
                            echeff.applyEnergyBuff(this, false, targets);
                            setBuffedValue(MapleBuffStat.ENERGY_CHARGE, 10001);
                        }
                    }
                } else {
                    if (getBuffedValue(MapleBuffStat.STORM_ELEMENTAL) == null) {
                        return;
                    } else {
                        int MaxBuff = 1;
                        if (getTotalSkillLevel(15000023) > 0) {
                            MaxBuff += 1;
                        }
                        if (getTotalSkillLevel(15100025) > 0) {
                            MaxBuff += 1;
                        }
                        if (getTotalSkillLevel(15110026) > 0) {
                            MaxBuff += 1;
                        }
                        if (getTotalSkillLevel(15120008) > 0) {
                            MaxBuff += 1;
                        }
                        if (this.kaiserCombo < MaxBuff) {
                            this.kaiserCombo += 1;
                        }
                        final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                        stat.put(MapleBuffStat.IGNORE_DEF, kaiserCombo * 5);
                        long starttime = System.currentTimeMillis();
                        registerEffect(echeff, starttime, null, getId());
                        this.client.getSession().write(CWvsContext.BuffPacket.giveBuff(15001022, 30000, stat, echeff, this));
                    }
                }
            }
        }
    }

    public final void handleBattleshipHP(int damage) {
        if (damage < 0) {
            final MapleStatEffect effect = getStatForBuff(MapleBuffStat.MONSTER_RIDING);
            if (effect != null && effect.getSourceId() == 5221006) {
                battleshipHP += damage;
                client.getSession().write(CField.skillCooldown(5221999, battleshipHP / 10));
                if (battleshipHP <= 0) {
                    battleshipHP = 0;
                    client.getSession().write(CField.skillCooldown(5221006, effect.getCooldown(this)));
                    addCooldown(5221006, System.currentTimeMillis(), effect.getCooldown(this) * 1000);
                    cancelEffectFromBuffStat(MapleBuffStat.MONSTER_RIDING);
                }
            }
        }
    }

    public final void handleOrbgain() {
        int orbcount = getBuffedValue(MapleBuffStat.COMBO);
        Skill combo = SkillFactory.getSkill(1101013);
        Skill advcombo = SkillFactory.getSkill(1120003);

        int advComboSkillLevel = getTotalSkillLevel(advcombo);
        MapleStatEffect ceffect;
        if (advComboSkillLevel > 0) {
            ceffect = advcombo.getEffect(advComboSkillLevel);
        } else if (getSkillLevel(combo) > 0) {
            ceffect = combo.getEffect(getTotalSkillLevel(combo));
        } else {
            return;
        }

        if (orbcount < ceffect.getX() + 1) {
            int neworbcount = orbcount + 1;
            if ((advComboSkillLevel > 0) && (ceffect.makeChanceResult())
                    && (neworbcount < ceffect.getX() + 1)) {
                neworbcount++;
            }

            EnumMap stat = new EnumMap(MapleBuffStat.class);
            stat.put(MapleBuffStat.COMBO, neworbcount);
            setBuffedValue(MapleBuffStat.COMBO, neworbcount);
            int duration = ceffect.getDuration();
            duration += (int) (getBuffedStarttime(MapleBuffStat.COMBO) - System.currentTimeMillis());

            this.client.getSession().write(CWvsContext.BuffPacket.giveBuff(combo.getId(), duration, stat, ceffect, this));
            this.map.broadcastMessage(this, CWvsContext.BuffPacket.giveForeignBuff(getId(), stat, ceffect), false);
        }
    }

    public void handleOrbconsume(int howmany) {
        Skill normalcombo = SkillFactory.getSkill(1101013);
        if (getSkillLevel(normalcombo) <= 0) {
            return;
        }
        MapleStatEffect ceffect = getStatForBuff(MapleBuffStat.COMBO);
        if (ceffect == null) {
            return;
        }
        EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
        stat.put(MapleBuffStat.COMBO, Math.max(1, getBuffedValue(MapleBuffStat.COMBO) - howmany));
        setBuffedValue(MapleBuffStat.COMBO, Math.max(1, getBuffedValue(MapleBuffStat.COMBO) - howmany));
        int duration = ceffect.getDuration();
        duration += (int) ((getBuffedStarttime(MapleBuffStat.COMBO) - System.currentTimeMillis()));

        client.getSession().write(BuffPacket.giveBuff(normalcombo.getId(), duration, stat, ceffect, this));
        map.broadcastMessage(this, BuffPacket.giveForeignBuff(getId(), stat, ceffect), false);
    }

    public void handleTriflingWhim(int mobid, boolean adv, boolean storm) {
        int rdz = server.Randomizer.nextInt(100);
        int skillid = 0;
        int count = 0;

        if (getTotalSkillLevel(13100022) > 0) {
            skillid = adv ? 13100027 : 13100022; // 風妖精之箭I
            count = 3;
            //13100027 - 風妖精之箭I
        }
        if (getTotalSkillLevel(13110022) > 0) {
            skillid = adv ? 13110027 : 13110022; // 風妖精之箭Ⅱ
            count = 4;
            //13110027 - 風妖精之箭Ⅱ
        }
        if (getTotalSkillLevel(13120003) > 0) {
            skillid = adv ? 13120010 : 13120003; // 風妖精之箭Ⅲ
            count = 5;
            //13120010 - 風妖精之箭Ⅲ
        }
        if (skillid != 0) {
            List<Integer> mobsid = new ArrayList<>();
            mobsid.add(mobid);

            List<Pair<Integer, Integer>> forceinfo = new ArrayList<>();
            forceinfo.add(new Pair<>(count, adv ? 2 : 1));
            if (storm) {
                client.getSession().write(CField.gainForce(false, this, mobsid, 8, 13121054, forceinfo));
            } else {
                client.getSession().write(CField.gainForce(false, this, mobsid, 7, skillid, forceinfo));
            }
        }
    }

    public void handleShadowBat(int mobid, int skill) {
        if (this.kaiserCombo > 3) {
                List<Integer> mobsid = new ArrayList<>();
                mobsid.add(mobid);

                List<Pair<Integer, Integer>> forceinfo = new ArrayList<>();
                forceinfo.add(new Pair<>(3, 1));
                client.getSession().write(CField.gainForce(false, this, mobsid, 15, 14000028, forceinfo)); // 到處飛的
//                client.getSession().write(CField.gainForce(true, this, mobsid, 16, 14000029, forceinfo)); // 在身邊的
        }
        if (getBuffedValue(MapleBuffStat.SHADOW_BAT) != null && skill != 0) {
            if (this.kaiserCombo >= 7) {
//                System.out.println("暗影蝙蝠::[" + kaiserCombo + "]");
                return;
            }
            if (this.kaiserCombo % 3 == 0 && this.kaiserCombo != 0) {
//                System.out.println("暗影蝙蝠::[" + kaiserCombo + "]");
                this.kaiserCombo += 1;
                final MapleSummon tosummon = new MapleSummon(this, 14000027, 1, new Point(this.getTruePosition()), SummonMovementType.CIRCLE_FOLLOW);
                getMap().spawnSummon(tosummon);
                addSummon(tosummon);
            } else {
                this.kaiserCombo += 1;
//                System.out.println("暗影蝙蝠::[" + kaiserCombo + "]");
            }
        }
    }

    public void silentEnforceMaxHpMp() {
        stats.setMp(stats.getMp(), this);
        stats.setHp(stats.getHp(), true, this);
    }

    public void enforceMaxHpMp() {
        Map<MapleStat, Long> statups = new EnumMap<>(MapleStat.class);
        if (stats.getMp() > stats.getCurrentMaxMp(this.getJob())) {
            stats.setMp(stats.getMp(), this);
            statups.put(MapleStat.MP, Long.valueOf(stats.getMp()));
        }
        if (stats.getHp() > stats.getCurrentMaxHp()) {
            stats.setHp(stats.getHp(), this);
            statups.put(MapleStat.HP, Long.valueOf(stats.getHp()));
        }
        if (statups.size() > 0) {
            client.getSession().write(CWvsContext.updatePlayerStats(statups, this));
        }
    }

    public MapleMap getMap() {
        return map;
    }

    public MonsterBook getMonsterBook() {
        return monsterbook;
    }

    public void setMap(MapleMap newmap) {
        this.map = newmap;
    }

    public void setMap(int PmapId) {
        this.mapid = PmapId;
    }

    public int getMapId() {
        if (map != null) {
            return map.getId();
        }
        return mapid;
    }

    public byte getInitialSpawnpoint() {
        return initialSpawnPoint;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public final String getBlessOfFairyOrigin() {
        return this.BlessOfFairy_Origin;
    }

    public final String getBlessOfEmpressOrigin() {
        return this.BlessOfEmpress_Origin;
    }
//the sp isnt fixed oh hold on

    public final short getLevel() {
        return level;
    }

    public final int getFame() {
        return fame;
    }

    public final int getFallCounter() {
        return fallcounter;
    }

    public final MapleClient getClient() {
        return client;
    }

    public final void setClient(final MapleClient client) {
        this.client = client;
    }

    public long getExp() {
        return exp;
    }

    public int getRemainingAp() {
        return remainingAp;
    }

    public int getRemainingSp() {
        return remainingSp[GameConstants.getSkillBookByJob(job)]; //default
    }

    public int getRemainingSp(final int skillbook) {
        return remainingSp[skillbook];
    }

    public int[] getRemainingSps() {
        return remainingSp;
    }

    public int getRemainingSpSize() {
        int ret = 0;
        for (int i = 0; i < remainingSp.length; i++) {
            if (remainingSp[i] > 0) {
                ret++;
            }
        }
        return ret;
    }

    public int getRemainingHSp(final int mode) {
        return remainingHSp[mode];
    }

    public int[] getRemainingHSps() {
        return remainingHSp;
    }

    public int getRemainingHSpSize() {
        int ret = 0;
        for (int i = 0; i < remainingHSp.length; i++) {
            if (remainingHSp[i] > 0) {
                ret++;
            }
        }
        return ret;
    }

    public short getHpApUsed() {
        return hpApUsed;
    }

    public boolean isHidden() {
        return getBuffSource(MapleBuffStat.DARKSIGHT) / 1000000 == 9;
    }

    public void setHpApUsed(short hpApUsed) {
        this.hpApUsed = hpApUsed;
    }

    @Override
    public byte getSkinColor() {
        return skinColor;
    }

    @Override
    public byte getSecondSkinColor() {
        return skinColor;
    }

    public void setSkinColor(byte skinColor) {
        this.skinColor = skinColor;
    }

    @Override
    public short getJob() {
        return job;
    }

    public String getJobName(short id) {
        return MapleJob.getName(MapleJob.getById(id));
    }

    @Override
    public byte getGender() {
        return gender;
    }

    @Override
    public byte getSecondGender() {
        if (MapleJob.is神之子(getJob())) {
            return 1;
        }
        return gender;
    }

    @Override
    public int getHair() {
        return hair;
    }

    @Override
    public int getSecondHair() {
        if (MapleJob.is天使破壞者(getJob())) {
            return 37141; //default id here
        } else if (MapleJob.is神之子(getJob())) {
            return 37623;
        }
        return hair;
    }

    @Override
    public int getFace() {
        return face;
    }

    @Override
    public int getSecondFace() {
        if (MapleJob.is天使破壞者(getJob())) {
            return 21173; //default id here
        } else if (MapleJob.is神之子(getJob())) {
            return 21290;
        }
        return face;
    }

    @Override
    public int getFaceMarking() {
        return faceMarking;
    }

    public void setFaceMarking(int mark) {
        this.faceMarking = mark;
    }

    @Override
    public int getEars() {
        return ears;
    }

    public void setEars(int ears) {
        this.ears = ears;
        equipChanged();
    }

    @Override
    public int getTail() {
        return tail;
    }

    public void setTail(int tail) {
        this.tail = tail;
        equipChanged();
    }

    @Override
    public int getElf() {
        return elf;
    }

    public void setElf(int elf) {
        this.elf = elf;
        equipChanged();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    public void setHair(int hair) {
        this.hair = hair;
    }

    public void setFace(int face) {
        this.face = face;
        equipChanged();
    }

    public void setSecondHair(int hair) {
        this.secondHair = hair;
    }

    public void setSecondFace(int face) {
        this.secondFace = face;
    }

    public void setFame(int fame) {
        this.fame = fame;
    }

    public void setFallCounter(int fallcounter) {
        this.fallcounter = fallcounter;
    }

    public Point getOldPosition() {
        return old;
    }

    public void setOldPosition(Point x) {
        this.old = x;
    }

    public void setRemainingAp(int remainingAp) {
        this.remainingAp = remainingAp;
    }

    public void setRemainingSp(int remainingSp) {
        this.remainingSp[GameConstants.getSkillBookByJob(job)] = remainingSp; //default
    }

    public void setRemainingSp(int remainingSp, final int skillbook) {
        this.remainingSp[skillbook] = remainingSp;
    }

    public void setRemainingHSp(int mode, int amount) {
        this.remainingHSp[mode] = amount;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public void setInvincible(boolean invinc) {
        invincible = invinc;
        if (invincible) {
            SkillFactory.getSkill(1010).getEffect(1).applyTo(this);
        } else {
            dispelBuff(1010);
        }
    }

    public boolean isInvincible() {
        return invincible;
    }

    public CheatTracker getCheatTracker() {
        return anticheat;
    }

    public BuddyList getBuddylist() {
        return buddylist;
    }

    public void addFame(int famechange) {
        this.fame += famechange;
        getTrait(MapleTraitType.charm).addLocalExp(famechange);
    }

    public void updateFame() {
        updateSingleStat(MapleStat.FAME, this.fame);
    }

    public void changeMapBanish(final int mapid, final String portal, final String msg) {
        dropMessage(5, msg);
        final MapleMap tomap = client.getChannelServer().getMapFactory().getMap(mapid);
        changeMap(tomap, tomap.getPortal(portal));
    }

    public void changeMap(final MapleMap to, final Point pos) {
        changeMapInternal(to, pos, CField.getWarpToMap(to, 0x80, this), null);
    }

    public void changeMap(final MapleMap to) {
        changeMapInternal(to, to.getPortal(0).getPosition(), CField.getWarpToMap(to, 0, this), to.getPortal(0));
    }

    public void changeMap(final MapleMap to, final MaplePortal pto) {
        changeMapInternal(to, pto.getPosition(), CField.getWarpToMap(to, pto.getId(), this), null);
    }

    public void changeMapPortal(final MapleMap to, final MaplePortal pto) {
        changeMapInternal(to, pto.getPosition(), CField.getWarpToMap(to, pto.getId(), this), pto);
    }

    private void changeMapInternal(final MapleMap to, final Point pos, byte[] warpPacket, final MaplePortal pto) {
        if (to == null) {
            return;
        }
        //if (getAntiMacro().inProgress()) {
        //    dropMessage(5, "You cannot use it in the middle of the Lie Detector Test.");
        //    return;
        //}
        final int nowmapid = map.getId();
        if (eventInstance != null) {
            eventInstance.changedMap(this, to.getId());
        }
        final boolean pyramid = pyramidSubway != null;
        if (map.getId() == nowmapid) {
            client.getSession().write(warpPacket);
            final boolean shouldChange = !isClone() && client.getChannelServer().getPlayerStorage().getCharacterById(getId()) != null;
            final boolean shouldState = map.getId() == to.getId();
            if (shouldChange && shouldState) {
                to.setCheckStates(false);
            }
            map.removePlayer(this);
            if (ServerConstants.AntiKS) {
                try {
                    MapleQuestStatus stat = getQuestNoAdd(MapleQuest.getInstance(732648172));
                    boolean antiks;
                    if (stat == null || stat.getCustomData() == null) {
                        antiks = false;
                        stat.setCustomData(antiks + ";" + 0);
                    } else {
                        String[] statss = stat.getCustomData().split(";");
                        try {
                            antiks = Boolean.parseBoolean(statss[0]);
                        } catch (Exception ex) {
                            antiks = false;
                        }
                        List<MapleMonster> monsters = map.getAllMonster();
                        for (MapleMapObject mmo : monsters) {
                            MapleMonster m = (MapleMonster) mmo;
                            if (m.getBelongsTo() == getId()) {
                                stat.setCustomData(antiks + ";" + (Integer.parseInt(statss[1]) - 1));
                                m.expireAntiKS();
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
            if (shouldChange) {
                map = to;
                setStance(0);
                setPosition(new Point(pos.x, pos.y -50));
                to.addPlayer(this);
                stats.relocHeal(this);
                if (shouldState) {
                    to.setCheckStates(true);
                }
            }
        }
        if (pyramid && pyramidSubway != null) { //checks if they had pyramid before AND after changing
            pyramidSubway.onChangeMap(this, to.getId());
        }
        updateDeathCount();

        if (deathCount < 1) {
//            dropMessage(0, "I must be very talented if I died so many times!");
//            World.Broadcast.broadcastMessage(CField.getGameMessage(name + " has somehow died 99 times! He is very talented!", (short) 13));
//            deathCount = 99;
//            client.getSession().write(EffectPacket.updateDeathCount(99)); //for fun
        }
    }

    public void cancelChallenge() {
        if (challenge != 0 && client.getChannelServer() != null) {
            final MapleCharacter chr = client.getChannelServer().getPlayerStorage().getCharacterById(challenge);
            if (chr != null) {
                chr.dropMessage(6, getName() + " has denied your request.");
                chr.setChallenge(0);
            }
            dropMessage(6, "Denied the challenge.");
            challenge = 0;
        }
    }

    public void leaveMap(MapleMap map) {
        controlledLock.writeLock().lock();
        visibleMapObjectsLock.writeLock().lock();
        try {
            for (MapleMonster mons : controlled) {
                if (mons != null) {
                    mons.setController(null);
                    mons.setControllerHasAggro(false);
                    map.updateMonsterController(mons);
                }
            }
            controlled.clear();
            visibleMapObjects.clear();
        } finally {
            controlledLock.writeLock().unlock();
            visibleMapObjectsLock.writeLock().unlock();
        }
        if (chair != 0) {
            chair = 0;
        }
        clearLinkMid();
        cancelFishingTask();
        cancelChallenge();
        if (!getMechDoors().isEmpty()) {
            removeMechDoor();
        }
        cancelMapTimeLimitTask();
        if (getTrade() != null) {
            MapleTrade.cancelTrade(getTrade(), client, this);
        }
        //antiMacro.reset(); // reset lie detector
    }

    public void changeJob(short newJob) {
        try {
            cancelEffectFromBuffStat(MapleBuffStat.SHADOWPARTNER);
            if (MapleJob.isBeginner(job)) {
                resetStats(4, 4, 4, 4);
            }
            this.job = newJob;
            updateSingleStat(MapleStat.JOB, newJob);

            if (!MapleJob.isBeginner(newJob) && newJob != MapleJob.隱忍.getId() && !MapleJob.is神之子(newJob) && !MapleJob.is幻獸師(newJob)) {
                addJobSkills();
                if (GameConstants.isSeparatedSp(newJob)) {
                    if (MapleJob.is幻影俠盜(job)) {
                        client.getSession().write(PhantomPacket.updateCardStack(0));
                        resetRunningStack();
                    }
                    int changeSp = MapleJob.is龍魔導士(newJob) && MapleJob.getNumber(newJob) < 6 && MapleJob.getNumber(newJob) != 4 ? 3 : 5;
                    if (MapleJob.is末日反抗軍(job) && MapleJob.getNumber(newJob) != 1) {
                        changeSp = 3;
                    }
                    remainingSp[GameConstants.getSkillBookByJob(newJob)] += changeSp;
                    client.getSession().write(InfoPacket.getSPMsg((byte) changeSp, (short) newJob));
                } else {
                    remainingSp[GameConstants.getSkillBookByJob(newJob)]++;
                    if (MapleJob.getNumber(newJob) >= 4) {
                        remainingSp[GameConstants.getSkillBookByJob(newJob)] += 2;
                    }
                }
                if (MapleJob.getNumber(newJob) >= 3 && level >= 60) { //3rd job or higher. lucky for evans who get 80, 100, 120, 160 ap...
                    remainingAp += 5;
                    updateSingleStat(MapleStat.AVAILABLEAP, remainingAp);
                }
                if (MapleJob.getNumber(newJob) == 1 && level >= 10) { //first job
                    if (MapleJob.is龍魔導士(newJob)) {
                        MapleQuest.getInstance(22100).forceStart(this, 0, null);
                        MapleQuest.getInstance(22100).forceComplete(this, 0);
                        client.getSession().write(NPCPacket.getEvanTutorial("UI/tutorial/evan/14/0"));
                        dropMessage(5, "The baby Dragon hatched and appears to have something to tell you. Click the baby Dragon to start a conversation.");
                    } else {
                        remainingSp[GameConstants.getSkillBookByJob(newJob)] += 3 * (level - 10);
                    }
                }
                //自定義補滿技能點
                if (newJob == MapleJob.下忍.getId() || newJob == MapleJob.中忍.getId() || newJob == MapleJob.上忍.getId()) {
                    remainingSp[GameConstants.getSkillBookByJob(newJob)] += 5;
                    if (newJob == MapleJob.中忍.getId()) {
                        remainingSp[GameConstants.getSkillBookByJob(newJob)] += 10;
                    }
                }
                updateSingleStat(MapleStat.AVAILABLESP, 0); // we don't care the value here
            }

            int maxhp = stats.getMaxHp(), maxmp = stats.getMaxMp();

            switch (job) {
                case 100: // Warrior
                case 1100: // Soul Master
                case 2100: // Aran
                case 3100:
                case 3200:
                case 4100:
                case 5100:
                case 6100:
                    maxhp += Randomizer.rand(200, 250);
                    break;
                case 3110:
                    maxhp += Randomizer.rand(300, 350);
                    break;
                case 200: // Magician
                case 2200: //evan
                case 2210: //evan
                case 2700:
                case 4200:
                    maxmp += Randomizer.rand(100, 150);
                    break;
                case 300: // Bowman
                case 400: // Thief
                case 500: // Pirate
                case 2300:
                case 3300:
                case 3500:
                case 3600:
                case 6500:
                    maxhp += Randomizer.rand(100, 150);
                    maxmp += Randomizer.rand(25, 50);
                    break;
                case 110: // Fighter
                case 120: // Page
                case 130: // Spearman
                case 1110: // Soul Master
                case 2110: // Aran
                case 3210:
                case 4110:
                case 5110:
                case 6110:
                    maxhp += Randomizer.rand(300, 350);
                    break;
                case 210: // FP
                case 220: // IL
                case 230: // Cleric
                    maxmp += Randomizer.rand(400, 450);
                    break;
                case 310: // Bowman
                case 320: // Crossbowman
                case 410: // Assasin
                case 420: // Bandit
                case 430: // Semi Dualer
                case 510:
                case 520:
                case 530:
                case 2310:
                case 1310: // Wind Breaker
                case 1410: // Night Walker
                case 3310:
                case 3510:
                    maxhp += Randomizer.rand(200, 250);
                    maxhp += Randomizer.rand(150, 200);
                    break;
                case 800: // Manager
                case 900: // GM
                case 910: // SuperGM
                    break; //No additions for now
            }
            if (maxhp >= 500000) {
                maxhp = 500000;
            }
            if (maxmp >= 500000) {
                maxmp = 500000;
            }
            if (MapleJob.is惡魔殺手(job)) {
                maxmp = GameConstants.getMPByJob(job);
            } else if (MapleJob.is神之子(job)) {
                maxmp = 100;
            }
            stats.setInfo(maxhp, maxmp, maxhp, maxmp);
            Map<MapleStat, Long> statup = new EnumMap<>(MapleStat.class);
            statup.put(MapleStat.MAXHP, Long.valueOf(maxhp));
            statup.put(MapleStat.MAXMP, Long.valueOf(maxmp));
            statup.put(MapleStat.HP, Long.valueOf(maxhp));
            statup.put(MapleStat.MP, Long.valueOf(maxmp));
            characterCard.recalcLocalStats(this);
            stats.recalcLocalStats(this);
            client.getSession().write(CWvsContext.updatePlayerStats(statup, this));
            map.broadcastMessage(this, EffectPacket.showJobChangeEffect(this), false);
            this.map.broadcastMessage(this, CField.updateCharLook(this, false), false);
            silentPartyUpdate();
            guildUpdate();
            familyUpdate();
            if (dragon != null) {
                map.broadcastMessage(CField.removeDragon(this.id));
                dragon = null;
            }
            if (haku != null) {
                haku = null;
            }
            baseSkills();
            if (newJob >= 2200 && newJob <= 2218) { //make new
                if (getBuffedValue(MapleBuffStat.MONSTER_RIDING) != null) {
                    cancelBuffStats(MapleBuffStat.MONSTER_RIDING);
                }
                makeDragon();
            }
            if (MapleJob.is陰陽師(newJob)) {
                if (getBuffedValue(MapleBuffStat.MONSTER_RIDING) != null) {
                    cancelBuffStats(new MapleBuffStat[]{MapleBuffStat.MONSTER_RIDING});
                }
                makeHaku();
            }
            checkForceShield();
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, e); //all jobs throw errors :(
        }
    }

    public void handleDemonJob(int selection) {
        if (selection == 0) {
            changeJob((short) 3101);
        } else if (selection == 1) {
            changeJob((short) 3100);
        }
    }

    public void addJobSkills() {
        if (MapleJob.is幻影俠盜(job)) {
            changeSkillLevel(SkillFactory.getSkill(job == 2412 ? 20031210 : 20031209), (byte) 1, (byte) 1);
            changeSkillLevel(SkillFactory.getSkill(job == 2412 ? 20031209 : 20031210), (byte) -1, (byte) 0);
        } else if (MapleJob.is傑諾(job)) {
            int[] _skills = new int[]{job >= 3610 ? 30021235 : 0,
                job != 3002 ? 30021236 : 0, job != 3002 ? 30021237 : 0, 36000004,
                36100007, 36110007, 36120010};
            for (int skill : _skills) {
                Skill _skill = SkillFactory.getSkill(skill);
                if (_skill != null && _skill.canBeLearnedBy(job)) {
                    changeSkillLevel(_skill, (byte) 1, (byte) 1);
                }
            }
        }
    }

    public void addLevelSkills() {
        if (job == 3612 && level >= 200) { // 傑諾(4轉)
            Skill skill = SkillFactory.getSkill(36120016);
            if (getSkillLevel(skill) < 1) {
                if (skill != null && skill.canBeLearnedBy(job)) {
                    changeSkillLevel(skill, (byte) 1, (byte) 1);
                }
            }
        }
    }

    public void baseSkills() {
        Map<Skill, SkillEntry> list = new HashMap<>();
        if (MapleJob.getNumber(job) >= 3) { //third job.
            List<Integer> base_skills = SkillFactory.getSkillsByJob(job);
            if (base_skills != null) {
                for (int i : base_skills) {
                    final Skill skil = SkillFactory.getSkill(i);
                    if (skil != null && !skil.isInvisible() && skil.isFourthJob() && getSkillLevel(skil) <= 0 && getMasterLevel(skil) <= 0 && skil.getMasterLevel() > 0) {
                        list.put(skil, new SkillEntry((byte) 0, (byte) skil.getMasterLevel(), SkillFactory.getDefaultSExpiry(skil))); //usually 10 master
                    } else if (skil != null && skil.getName() != null && (skil.getName().contains("楓葉祝福") || skil.getName().contains("超新星勇士")) && getSkillLevel(skil) <= 0 && getMasterLevel(skil) <= 0) {
                        list.put(skil, new SkillEntry((byte) 0, (byte) skil.getMasterLevel(), SkillFactory.getDefaultSExpiry(skil))); //hackish
                    }
                }

            }
        }
        Skill skil;

        int start = (job / 100) * 1000000;
        int end = job * 10000 + 3000;
//        System.out.println(start + " | " + end);
        for (int i = start; i < end; i++) {
            skil = SkillFactory.getSkill(i);
            if (skil != null) {
                if (getSkillLevel(skil) <= 0 && !skil.isInvisible()) {
                    boolean fourth = skil.isFourthJob();
                    int master = skil.getMasterLevel();
                    list.put(skil, new SkillEntry((byte) 0, (byte) (fourth ? master > 0 ? master : skil.getMaxLevel() : skil.getMaxLevel()), -1));
                }
            }
        }

        if (ZZMSConfig.maxSkillLevel) {
            Map<Skill, SkillEntry> skills = getSkills();
            for (Skill skill : skills.keySet()) {
                if (skill.isFourthJob() && skills.get(skill).masterlevel < skill.getMaxLevel()) {
                    changeSingleSkillLevel(SkillFactory.getSkill(skill.getId()), skills.get(skill).skillLevel, (byte) skill.getMaxLevel());
                }
            }
        }

        //處理傳授技能
        int linkId = 0;
        int maxLinkLevel = 3;
        if (MapleJob.is聖魂劍士(job)) {
            linkId = 10000255;//西格諾斯祝福（劍士）
            maxLinkLevel = 2;
        } else if (MapleJob.is烈焰巫師(job)) {
            linkId = 10000256;//西格諾斯祝福（法師）
            maxLinkLevel = 2;
        } else if (MapleJob.is破風使者(job)) {
            linkId = 10000257;//西格諾斯祝福（弓箭手）
            maxLinkLevel = 2;
        } else if (MapleJob.is暗夜行者(job)) {
            linkId = 10000258;//西格諾斯祝福（盜賊）
            maxLinkLevel = 2;
        } else if (MapleJob.is閃雷悍將(job)) {
            linkId = 10000259;//西格諾斯祝福（海盜）
            maxLinkLevel = 2;
        } else if (MapleJob.is精靈遊俠(job)) {
            linkId = 20021110;//精靈的祝福
        } else if (MapleJob.is惡魔殺手(job)) {
            linkId = 30010112;//惡魔之怒
        } else if (MapleJob.is惡魔復仇者(job)) {
            linkId = 30010241;//狂暴鬥氣
        } else if (MapleJob.is幻影俠盜(job)) {
            linkId = 20030204;//致命本能
        } else if (MapleJob.is米哈逸(job)) {
            linkId = 50001214;//光之守護
        } else if (MapleJob.is夜光(job)) {
            linkId = 20040218;//滲透
        } else if (MapleJob.is凱撒(job)) {
            linkId = 60000222;//鋼鐵意志
        } else if (MapleJob.is天使破壞者(job)) {
            linkId = 60011219;//靈魂契約
        } else if (MapleJob.is重砲指揮官(job)) {
            linkId = 110;//百烈祝福。
        } else if (MapleJob.is傑諾(job)) {
            linkId = 30020233;//合成邏輯
            maxLinkLevel = 2;
        } else if (MapleJob.is隱月(job)) {
            linkId = 20050286;//死裡逃生
            maxLinkLevel = 2;
        } else if (MapleJob.is蒼龍俠客(job)) {
            linkId = 1214;//寶盒的護佑
        } else if (MapleJob.is劍豪(job)) {
            linkId = 40010001;//疾風傳授
            maxLinkLevel = 1;
        } else if (MapleJob.is陰陽師(job)) {
            linkId = 40020002;//紫扇傳授
            maxLinkLevel = 2;
        } else if (MapleJob.is神之子(job)) {
            linkId = 100000271;//時之祝福
            maxLinkLevel = 5;
        } else if (MapleJob.is幻獸師(job)) {
            linkId = 110000800;//精靈集中
        }
        if (linkId > 0) {
            int nowLinkLevel = 0;
            if (!MapleJob.is神之子(job)) {
                nowLinkLevel = level < 70 ? 0 : level < 120 ? 1 : level < 200 ? 2 : 3;
            }
            if (SkillFactory.getSkill(linkId) == null) {
                System.out.println("client.MapleCharacter.baseSkills::get(" + linkId + ")");
            } else {
                if (!getSkills().containsKey(SkillFactory.getSkill(linkId)) || getSkills().get(SkillFactory.getSkill(linkId)).skillLevel < nowLinkLevel) {
                    changeSingleSkillLevel(SkillFactory.getSkill(linkId), Math.min(maxLinkLevel, nowLinkLevel), (byte) (nowLinkLevel > 0 ? nowLinkLevel : -1));
                }
            }
        }

        if (!list.isEmpty()) {
            changeSkillsLevel(list);
        }
    }

    public void makeDragon() {
        dragon = new MapleDragon(this);
        map.broadcastMessage(CField.spawnDragon(dragon));
    }

    public MapleDragon getDragon() {
        return dragon;
    }

    public void makeHaku() {
        haku = new MapleHaku(this);
        map.broadcastMessage(CField.spawnHaku(haku));
    }

    public MapleHaku getHaku() {
        return haku;
    }

    public void gainAp(short ap) {
        this.remainingAp += ap;
        updateSingleStat(MapleStat.AVAILABLEAP, this.remainingAp);
    }

    public void gainSP(int sp) {
        this.remainingSp[GameConstants.getSkillBookByJob(job)] += sp; //default
        updateSingleStat(MapleStat.AVAILABLESP, 0); // we don't care the value here
        client.getSession().write(InfoPacket.getSPMsg((byte) sp, (short) job));
    }

    public void gainSP(int sp, final int skillbook) {
        this.remainingSp[skillbook] += sp;
        updateSingleStat(MapleStat.AVAILABLESP, 0);
        client.getSession().write(InfoPacket.getSPMsg((byte) sp, (short) 0));
    }

    public void gainHSP(int mode, int hsp) {
        this.remainingHSp[mode] += hsp;
        client.getSession().write(CWvsContext.updateHyperSp(mode, hsp));
    }

    public void setHSP(int mode, int hsp) {
        this.remainingHSp[mode] = hsp;
        client.getSession().write(CWvsContext.updateHyperSp(mode, hsp));
    }

    public void updateHSP() {
        for (int i = 0; i < 3; i++) {
            client.getSession().write(CWvsContext.updateHyperSp(i, remainingHSp[i]));
        }
    }

    public void resetSP(int sp) {
        for (int i = 0; i < remainingSp.length; i++) {
            this.remainingSp[i] = sp;
        }
        updateSingleStat(MapleStat.AVAILABLESP, 0);
    }

    public void resetAPSP() {
        resetSP(0);
        gainAp((short) -this.remainingAp);
    }

    public void resetSp(int jobid) {
        int skillpoint = 0;
        for (Skill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId()) && skil.canBeLearnedBy(getJob()) && skil.getId() >= jobid * 1000000 && skil.getId() < (jobid + 1) * 1000000 && !skil.isInvisible()) {
                skillpoint += getSkillLevel(skil);
            }
        }
        gainSP(skillpoint, GameConstants.getSkillBookByJob(jobid));
        final Map<Skill, SkillEntry> skillmap = new HashMap<>(getSkills());
        final Map<Skill, SkillEntry> newList = new HashMap<>();
        for (Entry<Skill, SkillEntry> skill : skillmap.entrySet()) {
            newList.put(skill.getKey(), new SkillEntry((byte) 0, (byte) 0, -1));
        }
        changeSkillsLevel(newList);
        newList.clear();
        skillmap.clear();
    }

    public List<Integer> getProfessions() {
        List<Integer> prof = new ArrayList<>();
        for (int i = 9200; i <= 9204; i++) {
            if (getProfessionLevel(id * 10000) > 0) {
                prof.add(i);
            }
        }
        return prof;
    }

    public byte getProfessionLevel(int id) {
        int ret = getSkillLevel(id);
        if (ret <= 0) {
            return 0;
        }
        return (byte) ((ret >>> 24) & 0xFF); //the last byte
    }

    public short getProfessionExp(int id) {
        int ret = getSkillLevel(id);
        if (ret <= 0) {
            return 0;
        }
        return (short) (ret & 0xFFFF); //the first two byte
    }

    public boolean addProfessionExp(int id, int expGain) {
        int ret = getProfessionLevel(id);
        if (ret <= 0 || ret >= 10) {
            return false;
        }
        int newExp = getProfessionExp(id) + expGain;
        if (newExp >= GameConstants.getProfessionEXP(ret)) {
            //gain level
            changeProfessionLevelExp(id, ret + 1, newExp - GameConstants.getProfessionEXP(ret));
            int traitGain = (int) Math.pow(2, ret + 1);
            switch (id) {
                case 92000000:
                    traits.get(MapleTraitType.sense).addExp(traitGain, this);
                    break;
                case 92010000:
                    traits.get(MapleTraitType.will).addExp(traitGain, this);
                    break;
                case 92020000:
                case 92030000:
                case 92040000:
                    traits.get(MapleTraitType.craft).addExp(traitGain, this);
                    break;
            }
            return true;
        } else {
            changeProfessionLevelExp(id, ret, newExp);
            return false;
        }
    }

    public void changeProfessionLevelExp(int id, int level, int exp) {
        changeSingleSkillLevel(SkillFactory.getSkill(id), ((level & 0xFF) << 24) + (exp & 0xFFFF), (byte) 10);
    }

    public void changeSingleSkillLevel(final Skill skill, int newLevel, byte newMasterlevel) { //1 month
        if (skill == null) {
            return;
        }
        changeSingleSkillLevel(skill, newLevel, newMasterlevel, SkillFactory.getDefaultSExpiry(skill));
    }

    public void changeSingleSkillLevel(final Skill skill, int newLevel, byte newMasterlevel, long expiration) {
        final Map<Skill, SkillEntry> list = new HashMap<>();
        boolean hasRecovery = false, recalculate = false;
        if (changeSkillData(skill, newLevel, newMasterlevel, expiration)) { // no loop, only 1
            list.put(skill, new SkillEntry(newLevel, newMasterlevel, expiration));
            if (GameConstants.isRecoveryIncSkill(skill.getId())) {
                hasRecovery = true;
            }
            if (skill.getId() < 80000000) {
                recalculate = true;
            }
        }
        if (list.isEmpty()) { // nothing is changed
            return;
        }
        client.getSession().write(CWvsContext.updateSkills(list, false));
        reUpdateStat(hasRecovery, recalculate);
    }

    public void changeSingleSkillLevel(final Skill skill, int newLevel, byte newMasterlevel, long expiration, boolean hyper) {
        final Map<Skill, SkillEntry> list = new HashMap<>();
        boolean hasRecovery = false, recalculate = false;
        if (changeSkillData(skill, newLevel, newMasterlevel, expiration)) { // no loop, only 1
            list.put(skill, new SkillEntry(newLevel, newMasterlevel, expiration));
            if (GameConstants.isRecoveryIncSkill(skill.getId())) {
                hasRecovery = true;
            }
            if (skill.getId() < 80000000) {
                recalculate = true;
            }
        }
        if (list.isEmpty()) { // nothing is changed
            return;
        }
        client.getSession().write(CWvsContext.updateSkills(list, hyper));
        reUpdateStat(hasRecovery, recalculate);
    }

    public void changeSkillsLevel(final Map<Skill, SkillEntry> ss) {
        changeSkillsLevel(ss, false);
    }

    public void changeSkillsLevel(final Map<Skill, SkillEntry> ss, boolean hyper) {
        if (ss.isEmpty()) {
            return;
        }
        final Map<Skill, SkillEntry> list = new HashMap<>();
        boolean hasRecovery = false, recalculate = false;
        for (final Entry<Skill, SkillEntry> data : ss.entrySet()) {
            if (changeSkillData(data.getKey(), data.getValue().skillLevel, data.getValue().masterlevel, data.getValue().expiration)) {
                list.put(data.getKey(), data.getValue());
                if (GameConstants.isRecoveryIncSkill(data.getKey().getId())) {
                    hasRecovery = true;
                }
                if (data.getKey().getId() < 80000000) {
                    recalculate = true;
                }
            }
        }
        if (list.isEmpty()) { // nothing is changed
            return;
        }
        client.getSession().write(CWvsContext.updateSkills(list, hyper));
        reUpdateStat(hasRecovery, recalculate);
    }

    public boolean changeSkillData(final Skill skill, int newLevel, byte newMasterlevel, long expiration) {
        if (skill == null || (!GameConstants.isApplicableSkill(skill.getId()) && !GameConstants.isApplicableSkill_(skill.getId()))) {
            return false;
        }
        if (newLevel < 0 && newMasterlevel == 0) {
            if (skills.containsKey(skill)) {
                skills.remove(skill);
            } else {
                return false; //nothing happen
            }
        } else {
            skills.put(skill, new SkillEntry(newLevel, newMasterlevel, expiration));
        }
        return true;
    }

    public void changeSkillLevel(Skill skill, byte newLevel, byte newMasterlevel) {
        changeSkillLevel_Skip(skill, newLevel, newMasterlevel);
    }

    public void changeSkillLevel_Skip(Skill skil, int skilLevel, byte masterLevel) {
        final Map<Skill, SkillEntry> enry = new HashMap<>(1);
        enry.put(skil, new SkillEntry(skilLevel, masterLevel, -1L));
        changeSkillLevel_Skip(enry, true);
    }

    public void changeSkillLevel_Skip(final Map<Skill, SkillEntry> skill, final boolean write) { // only used for temporary skills (not saved into db)
        if (skill.isEmpty()) {
            return;
        }
        final Map<Skill, SkillEntry> newL = new HashMap<>();
        for (final Entry<Skill, SkillEntry> z : skill.entrySet()) {
            if (z.getKey() == null) {
                continue;
            }
            newL.put(z.getKey(), z.getValue());
            if (z.getValue().skillLevel < 0 && z.getValue().masterlevel == 0) {
                if (skills.containsKey(z.getKey())) {
                    skills.remove(z.getKey());
                } else {
                    continue;
                }
            } else {
                skills.put(z.getKey(), z.getValue());
            }
        }
        if (write && !newL.isEmpty()) {
            client.getSession().write(CWvsContext.updateSkills(newL, false));
        }
    }

    public void reUpdateStat(boolean hasRecovery, boolean recalculate) {
        changed_skills = true;
        if (hasRecovery) {
            stats.relocHeal(this);
        }
        if (recalculate) {
            stats.recalcLocalStats(this);
        }
    }

    public void playerDead() {
        if (deathCount > 0) {
            deathCount--;
            updateDeathCount();
        }
        //final MapleStatEffect statss = getStatForBuff(MapleBuffStat.SOUL_STONE);
        //if (statss != null) {
        //    dropMessage(5, "You have been revived by Soul Stone.");
        //    getStats().setHp(((getStats().getMaxHp() / 100) * statss.getX()), this);
        //    setStance(0);
        //    changeMap(getMap(), getMap().getPortal(0));
        //    return;
        //}
        if (getEventInstance() != null) {
            getEventInstance().playerKilled(this);
        }
        cancelEffectFromBuffStat(MapleBuffStat.SHADOWPARTNER);
        cancelEffectFromBuffStat(MapleBuffStat.MORPH);
        cancelEffectFromBuffStat(MapleBuffStat.SOARING);
        cancelEffectFromBuffStat(MapleBuffStat.MONSTER_RIDING);
        cancelEffectFromBuffStat(MapleBuffStat.MECH_CHANGE);
        cancelEffectFromBuffStat(MapleBuffStat.RECOVERY);
        cancelEffectFromBuffStat(MapleBuffStat.INDIE_MHP_R);
        cancelEffectFromBuffStat(MapleBuffStat.INDIE_MMP_R);
        cancelEffectFromBuffStat(MapleBuffStat.INDIE_MAX_HP);
        cancelEffectFromBuffStat(MapleBuffStat.INDIE_MAX_MP);
        cancelEffectFromBuffStat(MapleBuffStat.ENHANCED_MAXHP);
        cancelEffectFromBuffStat(MapleBuffStat.ENHANCED_MAXMP);
        cancelEffectFromBuffStat(MapleBuffStat.MAXHP);
        cancelEffectFromBuffStat(MapleBuffStat.MAXMP);
        dispelSummons();
        checkFollow();
        dotHP = 0;
        lastDOTTime = 0;
        if (GameConstants.isAzwanMap(getMapId())) {
            client.getSession().write(CWvsContext.showAzwanKilled());
        }
        if (!MapleJob.isBeginner(job) && !inPVP() && !GameConstants.isAzwanMap(getMapId())) {
            int amulet = getItemQuantity(5130000, false);
            if (amulet > 0) {
                MapleInventoryManipulator.removeById(client, MapleInventoryType.CASH, 5130000, 1, true, false);
                amulet--;
                if (amulet > 0xFF) {
                    amulet = 0xFF;
                }
                client.getSession().write(EffectPacket.useAmulet(1, (byte) amulet, (byte) 0));
            } else {
                float diepercentage;
                long expforlevel = getNeededExp();
                if (map.isTown() || FieldLimitType.RegularExpLoss.check(map.getFieldLimit())) {
                    diepercentage = 0.01f;
                } else {
                    diepercentage = (float) (0.1f - ((traits.get(MapleTraitType.charisma).getLevel() / 20) / 100f) - (stats.expLossReduceR / 100f));
                }
                long v10 = (exp - (long) ((double) expforlevel * diepercentage));
                if (v10 < 0) {
                    v10 = 0;
                }
                this.exp = v10;
            }
            this.updateSingleStat(MapleStat.EXP, this.exp);
        }
        if (!stats.checkEquipDurabilitys(this, -100)) { //i guess this is how it works ?
            dropMessage(5, "An item has run out of durability but has no inventory room to go to.");
        } //lol
        if (pyramidSubway != null) {
            stats.setHp((short) 50, this);
            setXenonSurplus((short) 0);
            pyramidSubway.fail(this);
        }
    }

    public void updatePartyMemberHP() {
        if (party != null && client.getChannelServer() != null) {
            final int channel = client.getChannel();
            for (MaplePartyCharacter partychar : party.getMembers()) {
                if (partychar != null && partychar.getMapid() == getMapId() && partychar.getChannel() == channel) {
                    final MapleCharacter other = client.getChannelServer().getPlayerStorage().getCharacterByName(partychar.getName());
                    if (other != null) {
                        other.getClient().getSession().write(CField.updatePartyMemberHP(getId(), stats.getHp(), stats.getCurrentMaxHp()));
                    }
                }
            }
        }
    }

    public void receivePartyMemberHP() {
        if (party == null) {
            return;
        }
        int channel = client.getChannel();
        for (MaplePartyCharacter partychar : party.getMembers()) {
            if (partychar != null && partychar.getMapid() == getMapId() && partychar.getChannel() == channel) {
                MapleCharacter other = client.getChannelServer().getPlayerStorage().getCharacterByName(partychar.getName());
                if (other != null) {
                    client.getSession().write(CField.updatePartyMemberHP(other.getId(), other.getStat().getHp(), other.getStat().getCurrentMaxHp()));
                }
            }
        }
    }

    public void healHP(int delta) {
        addHP(delta);
        client.getSession().write(EffectPacket.showHealed(delta));
        getMap().broadcastMessage(this, EffectPacket.showHealed(this, delta), false);
    }

    public void healMP(int delta) {
        addMP(delta);
        client.getSession().write(EffectPacket.showHealed(delta));
        getMap().broadcastMessage(this, EffectPacket.showHealed(this, delta), false);
    }

    /**
     * Convenience function which adds the supplied parameter to the current hp
     * then directly does a updateSingleStat.
     *
     * @see MapleCharacter#setHp(int)
     * @param delta
     */
    public void addHP(int delta) {
        if (stats.setHp(stats.getHp() + delta, this)) {
            updateSingleStat(MapleStat.HP, stats.getHp());
        }
    }

    /**
     * Convenience function which adds the supplied parameter to the current mp
     * then directly does a updateSingleStat.
     *
     * @see MapleCharacter#setMp(int)
     * @param delta
     */
    public void addMP(int delta) {
        addMP(delta, false);
    }

    public void addMP(int delta, boolean ignore) {
        if ((delta < 0 && MapleJob.is惡魔殺手(getJob())) || !MapleJob.is惡魔殺手(getJob()) || ignore) {
            if (stats.setMp(stats.getMp() + delta, this)) {
                updateSingleStat(MapleStat.MP, stats.getMp());
            }
        }
    }

    public void addMPHP(int hpDiff, int mpDiff) {
        Map<MapleStat, Long> statups = new EnumMap<>(MapleStat.class);

        if (stats.setHp(stats.getHp() + hpDiff, this)) {
            statups.put(MapleStat.HP, Long.valueOf(stats.getHp()));
        }
        if ((mpDiff < 0 && MapleJob.is惡魔殺手(getJob())) || !MapleJob.is惡魔殺手(getJob())) {
            if (stats.setMp(stats.getMp() + mpDiff, this)) {
                statups.put(MapleStat.MP, Long.valueOf(stats.getMp()));
            }
        }
        if (statups.size() > 0) {
            client.getSession().write(CWvsContext.updatePlayerStats(statups, this));
        }
    }

    public void updateSingleStat(MapleStat stat, long newval) {
        updateSingleStat(stat, newval, false);
    }

    /**
     * Updates a single stat of this MapleCharacter for the client. This method
     * only creates and sends an update packet, it does not update the stat
     * stored in this MapleCharacter instance.
     *
     * @param stat
     * @param newval
     * @param itemReaction
     */
    public void updateSingleStat(MapleStat stat, long newval, boolean itemReaction) {
        Map<MapleStat, Long> statup = new EnumMap<>(MapleStat.class);
        statup.put(stat, newval);
        client.getSession().write(CWvsContext.updatePlayerStats(statup, itemReaction, this));
    }

    public void gainExp(final long total, final boolean show, final boolean inChat, final boolean white) {
        try {
            long prevexp = getExp();
            long needed = getNeededExp();
            if (total > 0) {
                stats.checkEquipLevels(this, total); //gms like
            }
            if (level >= maxLevel) {
                setExp(0);
                //if (exp + total > needed) {
                //    setExp(needed);
                //} else {
                //    exp += total;
                //}
            } else {
                boolean leveled = false;
                long tot = exp + total;
                if (tot >= needed) {
                    exp += total;
                    while (exp >= needed) {
                        levelUp();
                        needed = getNeededExp();
                    }
                    leveled = true;
                    if (level >= maxLevel && !isIntern()) {
                        setExp(0);
                    } else {
                        needed = getNeededExp();
                        if (exp >= needed) {
                            setExp(needed - 1);
                        }
                    }
                } else {
                    exp += total;
                }
                if (total > 0) {
                    familyRep(prevexp, needed, leveled);
                }
            }
            if (total != 0) {
                if (exp < 0) { // After adding, and negative
                    if (total > 0) {
                        setExp(needed);
                    } else if (total < 0) {
                        setExp(0);
                    }
                }
                updateSingleStat(MapleStat.EXP, getExp());
                if (show) { // still show the expgain even if it's not there
                    client.getSession().write(InfoPacket.GainEXP_Others(total, inChat, white));
                }
            }
        } catch (Exception e) {
            FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, e); //all jobs throw errors :(
        }
    }

    public void setGmLevel(byte level) {
        this.gmLevel = level;
    }

    public void dispose(byte level) {
        this.gmLevel = level;
    }

    public boolean isShowInfo() {
        return isAdmin() /*&& ServerConfig.logPackets*/;
    }

    public boolean isShowErr() {
        return isSuperGM() /*&& ServerConfig.logPackets*/;
    }

    public void familyRep(long prevexp, long needed, boolean leveled) {
        if (mfc != null) {
            long onepercent = needed / 100;
            if (onepercent <= 0) {
                return;
            }
            int percentrep = (int) (getExp() / onepercent - prevexp / onepercent);
            if (leveled) {
                percentrep = 100 - percentrep + (level / 2);
            }
            if (percentrep > 0) {
                int sensen = World.Family.setRep(mfc.getFamilyId(), mfc.getSeniorId(), percentrep * 10, level, name);
                if (sensen > 0) {
                    World.Family.setRep(mfc.getFamilyId(), sensen, percentrep * 5, level, name); //and we stop here
                }
            }
        }
    }

    public void gainExpMonster(int gain, final boolean show, final boolean white, byte pty, final byte Class_Bonus_EXP, final byte Premium_Bonus_EXP_PERCENT, MapleMonster mob) {
        if (!isAlive()) {
            return;
        }
        //獲得怪物的魅力性向
        getTrait(MapleTraitType.charisma).addExp(mob.getStats().getCharismaEXP(), this);

        //詛咒減少50%經驗
        if (hasDisease(MapleDisease.CURSE)) {
            gain /= 2;
        }

        //出生劇情地圖無伺服器經驗值倍數加成
        if (!GameConstants.isTutorialMap(getMapId())) {
            int ch_exp = ChannelServer.getInstance(map.getChannel()).getExpRate(getWorld());
            gain *= GameConstants.getExpRate(getJob(), ch_exp);
        }

        //組隊經驗值 處理
        int Party_Bonus_EXP = 0;
        long prevexp = getExp();
        if (pty > 1) {
            Party_Bonus_EXP = gain;
            Party_Bonus_EXP *= (5 * pty) / 100.0;
            if (map != null && mob.getStats().isPartyBonus() && map.getPartyBonusRate() > 0 && mob.getStats().getPartyBonusRate() > 0) {
                Party_Bonus_EXP *= 1 + (mob.getStats().getPartyBonusRate() * Math.min(4, pty) / 100.0);
            }
            Party_Bonus_EXP *= 1 + (Premium_Bonus_EXP_PERCENT / 100.0);
        }

        //結婚紅利經驗值 處理
        int Wedding_Bonus_EXP = 0;
        if (marriageId > 0) {
            MapleCharacter marrChr = map.getCharacterById(marriageId);
            if (marrChr != null) {
                Wedding_Bonus_EXP = (int) (gain / 100.0D * 10.0D);
            }
        }

        //道具裝備紅利經驗值 處理
        int Equipment_Bonus_EXP = (int) ((gain / 100.0) * getStat().equipmentBonusExp);
        if (getStat().equippedFairy > 0 && getFairyExp() > 0) {
            Equipment_Bonus_EXP += (int) ((gain / 100.0) * getFairyExp());
        }

        //高級服務贈送經驗值 處理
        int Internet_Cafe_Bonus_EXP = 0;
        if (haveItem(5420008)) {
            Internet_Cafe_Bonus_EXP = (int) (gain / 100.0D * 25.0D);
        }

        //神使 神之子，精靈的祝福 額外經驗值 處理
        int Skill_Bonus_EXP = 0;
        if ((getSkillLevel(20021110) > 0 && (getJob() == 2002 || (getJob() >= 2300 && getJob() <= 2312))) || getSkillLevel(80001040) > 0) {
            Skill_Bonus_EXP += (int) (gain / 100.0D * 10.0D);
        }

        //獲得追加經驗值 處理
        int Additional_Bonus_EXP = gain;
        //挑釁
        final MonsterStatusEffect ms = mob.getBuff(MonsterStatus.SHOWDOWN);
        if (ms != null) {
            Additional_Bonus_EXP *= 1 + (ms.getX() / 100.0);
        }
        //祈禱
        final Integer holySymbol = getBuffedValue(MapleBuffStat.HOLY_SYMBOL);
        if (holySymbol != null) {
            Additional_Bonus_EXP *= 1 + (holySymbol.doubleValue() / 100.0);
        }
        //經驗值倍率模式
        Additional_Bonus_EXP *= getEXPMod();
        //經驗BUFF
        Additional_Bonus_EXP *= getStat().expBuff / 100.0;

        Additional_Bonus_EXP -= gain;

        //monsterCombo處理
        long MonsterCombo_EXP = 0;
        if (killMonsterExps.isEmpty()) {
            if ((monsterCombo > 0) && (System.currentTimeMillis() - lastMonsterCombo > 7000)) {
                monsterCombo = 0;
            }
            monsterCombo += monsterCombo == 999 ? 0 : 1;
            lastMonsterCombo = System.currentTimeMillis();
        }
        if (monsterCombo > 1) {//monsterCombo經驗
            MonsterCombo_EXP += (long) Math.ceil((double) ((double) gain * Math.min((double) monsterCombo, 60.0D) * 0.5D / 100.0D));
        }

        //總經驗
        long total = gain + Party_Bonus_EXP + Wedding_Bonus_EXP + Equipment_Bonus_EXP + Internet_Cafe_Bonus_EXP + Skill_Bonus_EXP + Additional_Bonus_EXP + Class_Bonus_EXP + MonsterCombo_EXP;

        //處理給角色加經驗并升級
        if (gain > 0 && total < gain) { //just in case
            total = Integer.MAX_VALUE;
        }
        if (total > 0) {
            stats.checkEquipLevels(this, total); //gms like
        }
        long needed = getNeededExp();
        if (level >= maxLevel) {
            setExp(0);
        } else {
            boolean leveled = false;
            if (exp + total >= needed || exp >= needed) {
                exp += total;
                while (exp > needed) {
                    levelUp();
                    needed = getNeededExp();
                }
                leveled = true;
                if (level >= maxLevel && !isIntern()) {
                    setExp(0);
                } else {
                    needed = getNeededExp();
                    if (exp >= needed) {
                        setExp(needed);
                    }
                }
            } else {
                exp += total;
            }
            if (total > 0) {
                familyRep(prevexp, needed, leveled);
            }
        }

        //處理經驗值數據包
        if (gain != 0) {
            if (exp < 0) { // After adding, and negative
                if (gain > 0) {
                    setExp(getNeededExp());
                } else if (gain < 0) {
                    setExp(0);
                }
            }
            updateSingleStat(MapleStat.EXP, getExp());
            if (show) { // still show the expgain even if it's not there
                final ArrayList<Pair<ExpMasks, Object[]>> expmasks = new ArrayList<>();
                // 活動獎勵經驗值
                expmasks.add(new Pair<>(ExpMasks.EVENT_BONUS, new Object[]{0}));
                // 怪物額外經驗值
                expmasks.add(new Pair<>(ExpMasks.MONSTER_PERCENT, new Object[]{(byte) 0}));
                // 活動組隊經驗值
                expmasks.add(new Pair<>(ExpMasks.EVENT_PARTY_BONUS, new Object[]{(byte) Class_Bonus_EXP}));
                // 組隊經驗值
                expmasks.add(new Pair<>(ExpMasks.PARTY_BONUS, new Object[]{Party_Bonus_EXP}));
                // 結婚紅利經驗值
                expmasks.add(new Pair<>(ExpMasks.WEDDING_BONUS, new Object[]{Wedding_Bonus_EXP}));
                // 道具裝備紅利經驗值
                expmasks.add(new Pair<>(ExpMasks.EQUIP_BONUS, new Object[]{Equipment_Bonus_EXP}));
                // 高級服務贈送經驗值
                expmasks.add(new Pair<>(ExpMasks.INTERNET_CAFE_BONUS, new Object[]{Internet_Cafe_Bonus_EXP}));
                // 彩虹週獎勵經驗值
                expmasks.add(new Pair<>(ExpMasks.RAINBOW_WEEK_BONUS, new Object[]{0}));
                // 爆發獎勵經驗值
                expmasks.add(new Pair<>(ExpMasks.BOOM_UP_BONUS, new Object[]{0}));
                // 秘藥額外經驗值
                expmasks.add(new Pair<>(ExpMasks.ELIXIR_BONUS, new Object[]{0}));
                // 神使 神之子，精靈的祝福 額外經驗值
                expmasks.add(new Pair<>(ExpMasks.SKILL_BONUS, new Object[]{Skill_Bonus_EXP}));
                // 加持獎勵經驗值
                expmasks.add(new Pair<>(ExpMasks.BUFF_BONUS, new Object[]{0}));
                // 休息獎勵經驗值
                expmasks.add(new Pair<>(ExpMasks.REST_BONUS, new Object[]{0}));
                // 道具獎勵經驗值
                expmasks.add(new Pair<>(ExpMasks.ITEM_BONUS, new Object[]{0}));
                // 阿斯旺勝利者獎勵經驗值
                expmasks.add(new Pair<>(ExpMasks.ASWAN_WINNER_BONUS, new Object[]{0}));
                // 依道具%增加經驗值
                expmasks.add(new Pair<>(ExpMasks.ITEM_PERCENT_BONUS, new Object[]{0}));
                // 超值包獎勵經驗值
                expmasks.add(new Pair<>(ExpMasks.VALUE_PACK_BONUS, new Object[]{0}));
                // 依道具的組隊任務%增加經驗值
                expmasks.add(new Pair<>(ExpMasks.ITEM_PARTYQUEST_PERCENT_BONUS, new Object[]{0}));
                // 獲得追加經驗值
                expmasks.add(new Pair<>(ExpMasks.ADDITIONAL_BONUS, new Object[]{Additional_Bonus_EXP}));
                // 家族經驗值獎勵
                expmasks.add(new Pair<>(ExpMasks.BLOOD_ALLIANCE_BONUS, new Object[]{0}));
                // 冷凍勇士經驗值獎勵
                expmasks.add(new Pair<>(ExpMasks.FROZEN_WARRIOR_BONUS, new Object[]{0}));
                // 燃燒場地獎勵經驗 x%
                expmasks.add(new Pair<>(ExpMasks.BURNING_FIELD_BONUS, new Object[]{0, 0}));

                client.getSession().write(InfoPacket.GainEXP_Monster(gain, false, white, expmasks));
                if (killMonsterExps.isEmpty() && monsterCombo > 1) {
                    client.getSession().write(InfoPacket.GainEXP_MonsterCombo(MonsterCombo_EXP, monsterCombo, mob.getObjectId()));
                }
            }
        }
        killMonsterExps.add(gain);
    }

    public void forceReAddItem_NoUpdate(Item item, MapleInventoryType type) {
        getInventory(type).removeSlot(item.getPosition());
        getInventory(type).addFromDB(item);
    }

    public void forceReAddItem(Item item, MapleInventoryType type) { //used for stuff like durability, item exp/level, probably owner?
        forceReAddItem_NoUpdate(item, type);
        type = MapleInventoryType.EQUIP;
        if (type != MapleInventoryType.UNDEFINED) {
            client.getSession().write(InventoryPacket.addInventorySlot(type, item, false));
//            client.getSession().write(InventoryPacket.updateSpecialItemUse(item, type == MapleInventoryType.EQUIPPED ? (byte) 1 : type.getType(), this));
        }
    }

    public void forceReAddItem_Flag(Item item, MapleInventoryType type) { //used for flags
        forceReAddItem_NoUpdate(item, type);
        if (type != MapleInventoryType.UNDEFINED) {
            client.getSession().write(InventoryPacket.updateSpecialItemUse_(item, type == MapleInventoryType.EQUIPPED ? (byte) 1 : type.getType(), this));
        }
    }

    public void forceReAddItem_Book(Item item, MapleInventoryType type) { //used for mbook
        forceReAddItem_NoUpdate(item, type);
        if (type != MapleInventoryType.UNDEFINED) {
            client.getSession().write(CWvsContext.upgradeBook(item, this));
        }
    }

    public void silentPartyUpdate() {
        if (party != null) {
            World.Party.updateParty(party.getId(), PartyOperation.SILENT_UPDATE, new MaplePartyCharacter(this));
        }
    }

    public boolean isIntern() {
        return gmLevel >= PlayerGMRank.INTERN.getLevel();
    }

    public boolean isGM() {
        return gmLevel >= PlayerGMRank.GM.getLevel();
    }

    public boolean isSuperGM() {
        return gmLevel >= PlayerGMRank.SUPERGM.getLevel();
    }

    public boolean isAdmin() {
        return gmLevel >= PlayerGMRank.ADMIN.getLevel();
    }

    public int getGMLevel() {
        return gmLevel;
    }

    public boolean hasGmLevel(int level) {
        return gmLevel >= level;
    }

    public boolean isDonator() {
        if (getInventory(MapleInventoryType.EQUIP).findById(1142229) != null) {
//            updateDonorMedal(getInventory(MapleInventoryType.EQUIP).findById(1142229));
            return true;
        }
        if (getInventory(MapleInventoryType.EQUIPPED).findById(1142229) != null) {
//            updateDonorMedal(getInventory(MapleInventoryType.EQUIPPED).findById(1142229));
            return true;
        }
        return false;
    }

    public final MapleInventory getInventory(MapleInventoryType type) {
        return inventory[type.ordinal()];
    }

    public final MapleInventory[] getInventorys() {
        return inventory;
    }

    public final void expirationTask(boolean pending, boolean firstLoad) {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (pending) {
            if (pendingExpiration != null) {
                for (Integer z : pendingExpiration) {
                    client.getSession().write(InfoPacket.itemExpired(z));
                    if (!firstLoad) {
                        final Pair<Integer, String> replace = ii.replaceItemInfo(z.intValue());
                        if (replace != null && replace.left > 0 && replace.right.length() > 0) {
                            dropMessage(5, replace.right);
                        }
                    }
                }
            }
            pendingExpiration = null;
            if (pendingSkills != null) {
                client.getSession().write(CWvsContext.updateSkills(pendingSkills, false));
                for (Skill z : pendingSkills.keySet()) {
                    client.getSession().write(CWvsContext.broadcastMsg(5, "[" + SkillFactory.getSkillName(z.getId()) + "] skill has expired and will not be available for use."));
                }
            } //not real msg
            pendingSkills = null;
            return;
        }
        final MapleQuestStatus stat = getQuestNoAdd(MapleQuest.getInstance(GameConstants.墜飾欄));
        long expiration;
        final List<Integer> ret = new ArrayList<>();
        final long currenttime = System.currentTimeMillis();
        final List<Triple<MapleInventoryType, Item, Boolean>> toberemove = new ArrayList<>(); // This is here to prevent deadlock.
        final List<Item> tobeunlock = new ArrayList<>(); // This is here to prevent deadlock.

        for (final MapleInventoryType inv : MapleInventoryType.values()) {
            for (final Item item : getInventory(inv)) {
                expiration = item.getExpiration();

                if ((expiration != -1 && !ItemConstants.類型.寵物(item.getItemId()) && currenttime > expiration) || (firstLoad && ii.isLogoutExpire(item.getItemId()))) {
                    if (ItemFlag.LOCK.check(item.getFlag())) {
                        tobeunlock.add(item);
                    } else if (currenttime > expiration) {
                        toberemove.add(new Triple<>(inv, item, false));
                    }
                } else if (item.getItemId() == 5000054 && item.getPet() != null && item.getPet().getSecondsLeft() <= 0) {
                    toberemove.add(new Triple<>(inv, item, false));
                } else if (item.getPosition() == -59) {
                    if (stat == null || stat.getCustomData() == null || Long.parseLong(stat.getCustomData()) < currenttime) {
                        toberemove.add(new Triple<>(inv, item, true));
                    }
                }
            }
        }
        Item item;
        for (final Triple<MapleInventoryType, Item, Boolean> itemz : toberemove) {
            item = itemz.getMid();
            getInventory(itemz.getLeft()).removeItem(item.getPosition(), item.getQuantity(), false);
            if (itemz.getRight() && getInventory(GameConstants.getInventoryType(item.getItemId())).getNextFreeSlot() > -1) {
                item.setPosition(getInventory(GameConstants.getInventoryType(item.getItemId())).getNextFreeSlot());
                getInventory(GameConstants.getInventoryType(item.getItemId())).addFromDB(item);
            } else {
                ret.add(item.getItemId());
            }
            if (!firstLoad) {
                final Pair<Integer, String> replace = ii.replaceItemInfo(item.getItemId());
                if (replace != null && replace.left > 0) {
                    Item theNewItem;
                    if (GameConstants.getInventoryType(replace.left) == MapleInventoryType.EQUIP) {
                        theNewItem = ii.getEquipById(replace.left);
                        theNewItem.setPosition(item.getPosition());
                    } else {
                        theNewItem = new Item(replace.left, item.getPosition(), (short) 1, (byte) 0);
                    }
                    getInventory(itemz.getLeft()).addFromDB(theNewItem);
                }
            }
        }
        for (final Item itemz : tobeunlock) {
            itemz.setExpiration(-1);
            itemz.setFlag((byte) (itemz.getFlag() - ItemFlag.LOCK.getValue()));
        }
        this.pendingExpiration = ret;

        final Map<Skill, SkillEntry> skilz = new HashMap<>();
        final List<Skill> toberem = new ArrayList<>();
        for (Entry<Skill, SkillEntry> skil : skills.entrySet()) {
            if (skil.getValue().expiration != -1 && currenttime > skil.getValue().expiration) {
                toberem.add(skil.getKey());
            }
        }
        for (Skill skil : toberem) {
            skilz.put(skil, new SkillEntry(0, (byte) 0, -1));
            this.skills.remove(skil);
            changed_skills = true;
        }
        this.pendingSkills = skilz;
        if (stat != null && stat.getCustomData() != null && !"0".equals(stat.getCustomData()) && Long.parseLong(stat.getCustomData()) < currenttime) { //expired bro
            quests.remove(MapleQuest.getInstance(7830));
            quests.remove(MapleQuest.getInstance(GameConstants.墜飾欄));
        }
    }

    public void refreshBattleshipHP() {
        if (getJob() == 592) {
            client.getSession().write(CWvsContext.giveKilling(currentBattleshipHP()));
        }
    }

    public MapleShop getShop() {
        return shop;
    }

    public void setShop(MapleShop shop) {
        this.shop = shop;
    }

    public long getMeso() {
        return meso;
    }

    public final int[] getSavedLocations() {
        return savedLocations;
    }

    public int getSavedLocation(SavedLocationType type) {
        return savedLocations[type.getValue()];
    }

    public void saveLocation(SavedLocationType type) {
        savedLocations[type.getValue()] = getMapId();
        changed_savedlocations = true;
    }

    public void saveLocation(SavedLocationType type, int mapz) {
        savedLocations[type.getValue()] = mapz;
        changed_savedlocations = true;
    }

    public void clearSavedLocation(SavedLocationType type) {
        savedLocations[type.getValue()] = -1;
        changed_savedlocations = true;
    }

    public void gainMeso(long gain, boolean show) {
        gainMeso(gain, show, false);
    }

    public void gainMeso(long gain, boolean show, boolean inChat) {
        if (meso + gain < 0L) {
            gain = -meso;
            meso = 0;
        } else if (meso + gain > 99999999999L) {
            gain = 99999999999L - meso;
            meso = 99999999999L;
        } else {
            meso += gain;
        }

        updateSingleStat(MapleStat.MESO, meso, false);
        client.getSession().write(CWvsContext.enableActions());
        if (show) {
            client.getSession().write(InfoPacket.showMesoGain(gain, inChat));
        }
    }

    public void controlMonster(MapleMonster monster, boolean aggro) {
        if (clone || monster == null) {
            return;
        }
        monster.setController(this);
        controlledLock.writeLock().lock();
        try {
            controlled.add(monster);
        } finally {
            controlledLock.writeLock().unlock();
        }
        client.getSession().write(MobPacket.controlMonster(monster, false, aggro, GameConstants.isAzwanMap(getMapId())));

        monster.sendStatus(client);
    }

    public void stopControllingMonster(MapleMonster monster) {
        if (clone || monster == null) {
            return;
        }
        controlledLock.writeLock().lock();
        try {
            if (controlled.contains(monster)) {
                controlled.remove(monster);
            }
        } finally {
            controlledLock.writeLock().unlock();
        }
    }

    public void checkMonsterAggro(MapleMonster monster) {
        if (clone || monster == null) {
            return;
        }
        if (monster.getController() == this) {
            monster.setControllerHasAggro(true);
        } else {
            monster.switchController(this, true);
        }
    }

    public int getControlledSize() {
        return controlled.size();
    }

    public int getAccountID() {
        return accountid;
    }

    public void mobKilled(final int id, final int skillID) {
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus() != 1 || !q.hasMobKills()) {
                continue;
            }
            if (q.mobKilled(id, skillID)) {
                client.getSession().write(InfoPacket.updateQuestMobKills(q));
                if (q.getQuest().canComplete(this, null)) {
                    client.getSession().write(CWvsContext.showQuestCompletion(q.getQuest().getId()));
                }
            }
        }
    }

    public final List<MapleQuestStatus> getStartedQuests() {
        List<MapleQuestStatus> ret = new LinkedList<>();
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus() == 1 && !q.isCustom() && !q.getQuest().isBlocked()) {
                ret.add(q);
            }
        }
        return ret;
    }

    public final List<MapleQuestStatus> getCompletedQuests() {
        List<MapleQuestStatus> ret = new LinkedList<>();
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus() == 2 && !q.isCustom() && !q.getQuest().isBlocked()) {
                ret.add(q);
            }
        }
        return ret;
    }

    public final List<Pair<Integer, Long>> getCompletedMedals() {
        List<Pair<Integer, Long>> ret = new ArrayList<>();
        for (MapleQuestStatus q : quests.values()) {
            if (q.getStatus() == 2 && !q.isCustom() && !q.getQuest().isBlocked() && q.getQuest().getMedalItem() > 0 && GameConstants.getInventoryType(q.getQuest().getMedalItem()) == MapleInventoryType.EQUIP) {
                ret.add(new Pair<>(q.getQuest().getId(), q.getCompletionTime()));
            }
        }
        return ret;
    }

    public Map<Skill, SkillEntry> getSkills() {
        return Collections.unmodifiableMap(skills);
    }

    public int getTotalSkillLevel(final Skill skill) {
        if (skill == null) {
            return 0;
        }
        int itemLevel = GameConstants.getItemSkillLevel(this, skill.getId());
        if (itemLevel > 0) {
            return itemLevel;
        }
        int skillLevel;
        if (getJob() >= skill.getId() / 10000 && getJob() < skill.getId() / 10000 + 3) {
            skillLevel = skill.getFixLevel();
        } else {
            skillLevel = 0;
        }

        final SkillEntry ret = skills.get(skill);
        if (ret == null || ret.skillLevel <= 0) {
            return skillLevel;
        } else {
            skillLevel += ret.skillLevel;
        }
        return Math.min(skill.getTrueMax(), skillLevel + (skill.isBeginnerSkill() ? 0 : (stats.combatOrders + (skill.getMaxLevel() > 10 ? stats.incAllskill : 0) + stats.getSkillIncrement(skill.getId()))));
    }

    public int getAllSkillLevels() {
        int rett = 0;
        for (Entry<Skill, SkillEntry> ret : skills.entrySet()) {
            if (!ret.getKey().isBeginnerSkill() && !ret.getKey().isSpecialSkill() && ret.getValue().skillLevel > 0) {
                rett += ret.getValue().skillLevel;
            }
        }
        return rett;
    }

    public long getSkillExpiry(final Skill skill) {
        if (skill == null) {
            return 0;
        }
        final SkillEntry ret = skills.get(skill);
        if (ret == null || ret.skillLevel <= 0) {
            return 0;
        }
        return ret.expiration;
    }

    public int getSkillLevel(final Skill skill) {
        if (skill == null) {
            return 0;
        }
        int itemLevel = GameConstants.getItemSkillLevel(this, skill.getId());
        if (itemLevel > 0) {
            return itemLevel;
        }
        int skillLevel;
        if (getJob() >= skill.getId() / 10000 && getJob() < skill.getId() / 10000 + 3) {
            skillLevel = skill.getFixLevel();
        } else {
            skillLevel = 0;
        }
        SkillEntry ret = (SkillEntry) this.skills.get(skill);
        if (ret == null || ret.skillLevel <= 0) {
            return skillLevel;
        } else {
            skillLevel += ret.skillLevel;
        }
        return skillLevel;
    }

    public byte getMasterLevel(final int skill) {
        return getMasterLevel(SkillFactory.getSkill(skill));
    }

    public byte getMasterLevel(final Skill skill) {
        final SkillEntry ret = skills.get(skill);
        if (ret == null) {
            return 0;
        }
        return ret.masterlevel;
    }
    public final int maxLevel = 250;

    public void levelUp() {
        if (getLevel() >= maxLevel) {
            return;
        }
        remainingAp += 5;
        int maxhp = stats.getMaxHp();
        int maxmp = stats.getMaxMp();

        if (job == 3001 || job == 10000) {// 惡魔殺手,神之子 的新手
            maxhp += Randomizer.rand(52, 56);
        } else if (MapleJob.isBeginner(job)) { // 新手 (無惡魔,神之子)
            maxhp += Randomizer.rand(12, 16);
            maxmp += Randomizer.rand(10, 12);
        } else if (MapleJob.is劍士(job) && (MapleJob.is冒險家(job) || MapleJob.is皇家騎士團(job))) { // 劍士、聖魂劍士
            maxhp += Randomizer.rand(48, 52);
            maxmp += Randomizer.rand(4, 6);
        } else if (MapleJob.is狂狼勇士(job)) { // 狂狼勇士
            maxhp += Randomizer.rand(50, 52);
            maxmp += Randomizer.rand(4, 6);
        } else if (MapleJob.is惡魔殺手(job)) { // 惡魔殺手
            maxhp += Randomizer.rand(48, 52);
        } else if (MapleJob.is惡魔復仇者(job)) { // 惡魔復仇者
            maxhp += Randomizer.rand(70, 105);
        } else if (MapleJob.is米哈逸(job)) { // 米哈逸
            maxhp += Randomizer.rand(48, 52);
            maxmp += Randomizer.rand(4, 6);
        } else if (MapleJob.is凱撒(job)) { // 凱薩
            maxhp += Randomizer.rand(70, 105);
            maxmp += Randomizer.rand(10, 20);
        } else if (MapleJob.is神之子(job)) { // 神之子
            maxhp += Randomizer.rand(70, 105);
        } else if (MapleJob.is法師(job) && (MapleJob.is冒險家(job) || MapleJob.is皇家騎士團(job))) { // 法師、烈焰巫師
            maxhp += Randomizer.rand(10, 14);
            maxmp += Randomizer.rand(48, 52);
        } else if (MapleJob.is龍魔導士(job)) { // 龍魔導士
            maxhp += Randomizer.rand(12, 16);
            maxmp += Randomizer.rand(50, 52);
        } else if (MapleJob.is夜光(job)) { // 夜光
            maxhp += Randomizer.rand(25, 40);
            maxmp += Randomizer.rand(60, 100);
        } else if (MapleJob.is煉獄巫師(job)) { // 煉獄巫師
            maxhp += Randomizer.rand(20, 24);
            maxmp += Randomizer.rand(42, 44);
        } else if (MapleJob.is弓箭手(job) || (MapleJob.is盜賊(job) && (MapleJob.is冒險家(job) || MapleJob.is皇家騎士團(job)))) {
            maxhp += Randomizer.rand(20, 24);
            maxmp += Randomizer.rand(14, 16);
        } else if (MapleJob.is幻影俠盜(job)) { // 幻影
            maxhp += Randomizer.rand(56, 67);
            maxmp += Randomizer.rand(74, 100);
        } else if (MapleJob.is拳霸(job)) { // 拳霸
            maxhp += Randomizer.rand(37, 41);
            maxmp += Randomizer.rand(18, 22);
        } else if (MapleJob.is海盜(job) && MapleJob.is冒險家(job)) { // 除了拳霸的冒險家海盜
            maxhp += Randomizer.rand(20, 24);
            maxmp += Randomizer.rand(18, 22);
        } else if (MapleJob.is閃雷悍將(job) || MapleJob.is機甲戰神(job)) { // 閃雷悍將 機甲戰神
            maxhp += Randomizer.rand(56, 67);
            maxmp += Randomizer.rand(34, 47);
        } else if (MapleJob.is隱月(job)) { // 隱月
            maxhp += Randomizer.rand(66, 77);
            maxmp += Randomizer.rand(44, 57);
        } else if (MapleJob.is傑諾(job)) { // 傑諾
            maxhp += Randomizer.rand(100, 130);
            maxmp += Randomizer.rand(10, 15);
        } else if (MapleJob.is天使破壞者(job)) { // 天使破壞者
            maxhp += Randomizer.rand(56, 67);
        } else {
            System.err.println("職業 " + MapleJob.getById(job).name() + " 未處理升級HPMP增加");
        }
        maxmp += stats.getTotalInt() / 10;

        exp -= getNeededExp();
        if (exp < 0) {
            exp = 0;
        }
        level += 1;
        addLevelSkills();
        maxhp = Math.min(500000, Math.abs(maxhp));
        maxmp = Math.min(500000, Math.abs(maxmp));
        if (MapleJob.is惡魔殺手(job)) {
            maxmp = GameConstants.getMPByJob(job);
        } else if (MapleJob.is神之子(job)) {
            maxmp = 100;
        }
        final Map<MapleStat, Long> statup = new EnumMap<>(MapleStat.class);

        statup.put(MapleStat.MAXHP, (long) maxhp);
        statup.put(MapleStat.MAXMP, (long) GameConstants.fixDemonForce(this)); // 修正惡魔殺手DF不正確的問題
        statup.put(MapleStat.HP, (long) maxhp);
        statup.put(MapleStat.MP, (long) maxmp);
        statup.put(MapleStat.EXP, exp);
        statup.put(MapleStat.LEVEL, (long) level);
        updateSingleStat(MapleStat.AVAILABLESP, 0);

        if (level <= 10) {
            stats.str += remainingAp;
            remainingAp = 0;
            statup.put(MapleStat.STR, (long) stats.getStr());
        }
        if (MapleJob.is神之子(job)) {
            if (level >= 100) {
                remainingSp[0] += 3; //alpha gets 3sp
                remainingSp[1] += 3; //beta gets 3sp
            }
        } else if (level >= 11) {
            int point = 3;
            if (!MapleJob.is龍魔導士(job)) {
                if (level > 100 && level <= 140) {
                    point += level <= 110 ? 0 : level / 10 % 10 > 0 ? level / 10 % 10 : level / 10 % 10 - 1;
                    point *= level % 10 == 3 || level % 10 == 6 || level % 10 == 9 || level % 10 == 0 ? 2 : 1;
                }
                if (level > 140) {
                    point = 0;
                }
            }
            remainingSp[GameConstants.getSkillBook(job, level)] += point;
        }

        statup.put(MapleStat.AVAILABLEAP, (long) remainingAp);
        statup.put(MapleStat.AVAILABLESP, (long) remainingSp[GameConstants.getSkillBook(job, level)]);
        obtainHyperSP();
        stats.setInfo(maxhp, maxmp, maxhp, maxmp);
        client.getSession().write(CWvsContext.updatePlayerStats(statup, this));
        map.broadcastMessage(this, EffectPacket.showLevelupEffect(this), false);
        characterCard.recalcLocalStats(this);
        stats.recalcLocalStats(this);
        silentPartyUpdate();
        guildUpdate();
        familyUpdate();
        autoJob();
        if (MapleJob.is神之子(job)) {
            checkZeroWeapon();
            checkZeroTranscendent();
        }
        if (level == 250 && !isIntern()) {
            setExp(0);
            StringBuilder sb = new StringBuilder();
            sb.append("[恭喜] ");
            addMedalString(client.getPlayer(), sb);
            sb.append(getName()).append("達到了等級").append(level).append("級！請大家一起恭喜他！");
            //addMedalString(client.getPlayer(), sb);
            //sb.append(getName()).append(" on such an amazing achievement!");
            World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(6, sb.toString()));
        }
        //if (map.getForceMove() > 0 && map.getForceMove() <= getLevel()) {
        //    changeMap(map.getReturnMap(), map.getReturnMap().getPortal(0));
        //    dropMessage(-1, "You have been expelled from the map.");
        //}
        checkCustomReward(level);
        baseSkills();
    }

    public void obtainHyperSP() {
        if (MapleJob.is神之子(job)) {
            return; //no hypers for zero
        }
        switch (level) {
            case 140:
            case 160:
            case 190:
                gainHSP(0, 1);
                gainHSP(1, 1);
                break;
            case 150:
            case 170:
                gainHSP(0, 1);
                gainHSP(2, 1);
                break;
            case 180:
                gainHSP(1, 1);
                break;
            case 200:
                gainHSP(0, 1);
                gainHSP(1, 1);
                gainHSP(2, 1);
                break;
        }
    }

    //自動轉職
    public void autoJob() {
        if (MapleJob.is英雄(job)) {
            switch (getLevel()) {
                case 60:
                    changeJob((short) MapleJob.十字軍.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.英雄.getId());
                    break;
            }
        } else if (MapleJob.is聖騎士(job)) {
            switch (getLevel()) {
                case 60:
                    changeJob((short) MapleJob.騎士.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.聖騎士.getId());
                    break;
            }
        } else if (MapleJob.is黑騎士(job)) {
            switch (getLevel()) {
                case 60:
                    changeJob((short) MapleJob.嗜血狂騎.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.黑騎士.getId());
                    break;
            }
        } else if (MapleJob.is大魔導士_火毒(job)) {
            switch (getLevel()) {
                case 60:
                    changeJob((short) MapleJob.魔導士_火毒.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.大魔導士_火毒.getId());
                    break;
            }
        } else if (MapleJob.is大魔導士_冰雷(job)) {
            switch (getLevel()) {
                case 60:
                    changeJob((short) MapleJob.魔導士_冰雷.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.大魔導士_冰雷.getId());
                    break;
            }
        } else if (MapleJob.is主教(job)) {
            switch (getLevel()) {
                case 60:
                    changeJob((short) MapleJob.祭司.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.主教.getId());
                    break;
            }
        } else if (MapleJob.is箭神(job)) {
            switch (getLevel()) {
                case 60:
                    changeJob((short) MapleJob.遊俠.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.箭神.getId());
                    break;
            }
        } else if (MapleJob.is神射手(job)) {
            switch (getLevel()) {
                case 60:
                    changeJob((short) MapleJob.狙擊手.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.神射手.getId());
                    break;
            }
        } else if (MapleJob.is夜使者(job)) {
            switch (getLevel()) {
                case 60:
                    changeJob((short) MapleJob.暗殺者.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.夜使者.getId());
                    break;
            }
        } else if (MapleJob.is暗影神偷(job)) {
            switch (getLevel()) {
                case 60:
                    changeJob((short) MapleJob.神偷.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.暗影神偷.getId());
                    break;
            }
        } else if (MapleJob.is影武者(job) || (this.subcategory == 1 && job == 400)) {
            switch (getLevel()) {
                case 20:
                    changeJob((short) MapleJob.下忍.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.中忍.getId());
                    break;
                case 45:
                    changeJob((short) MapleJob.上忍.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.隱忍.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.影武者.getId());
                    break;
            }
        } else if (MapleJob.is拳霸(job)) {
            switch (getLevel()) {
                case 60:
                    changeJob((short) MapleJob.格鬥家.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.拳霸.getId());
                    break;
            }
        } else if (MapleJob.is槍神(job)) {
            switch (getLevel()) {
                case 60:
                    changeJob((short) MapleJob.神槍手.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.槍神.getId());
                    break;
            }
        } else if (MapleJob.is重砲指揮官(job)) {
            switch (getLevel()) {
                case 30:
                    changeJob((short) MapleJob.重砲兵.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.重砲兵隊長.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.重砲指揮官.getId());
                    break;
            }
        } else if (MapleJob.is蒼龍俠客(job)) {
            switch (getLevel()) {
                case 30:
                    changeJob((short) MapleJob.蒼龍俠客2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.蒼龍俠客3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.蒼龍俠客4轉.getId());
                    break;
            }
        } else if (MapleJob.is聖魂劍士(job)) {
            switch (getLevel()) {
                case 30:
                    changeJob((short) MapleJob.聖魂劍士2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.聖魂劍士3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.聖魂劍士4轉.getId());
                    break;
            }
        } else if (MapleJob.is烈焰巫師(job)) {
            switch (getLevel()) {
                case 30:
                    changeJob((short) MapleJob.烈焰巫師2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.烈焰巫師3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.烈焰巫師4轉.getId());
                    break;
            }
        } else if (MapleJob.is破風使者(job)) {
            switch (getLevel()) {
                case 30:
                    changeJob((short) MapleJob.破風使者2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.破風使者3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.破風使者4轉.getId());
                    break;
            }
        } else if (MapleJob.is暗夜行者(job)) {
            switch (getLevel()) {
                case 30:
                    changeJob((short) MapleJob.暗夜行者2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.暗夜行者3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.暗夜行者4轉.getId());
                    break;
            }
        } else if (MapleJob.is閃雷悍將(job)) {
            switch (getLevel()) {
                case 30:
                    changeJob((short) MapleJob.閃雷悍將2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.閃雷悍將3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.閃雷悍將4轉.getId());
                    break;
            }
        } else if (MapleJob.is狂狼勇士(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.狂狼勇士1轉.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.狂狼勇士2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.狂狼勇士3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.狂狼勇士4轉.getId());
                    break;
            }
        } else if (MapleJob.is龍魔導士(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.龍魔導士1轉.getId());
                    break;
                case 20:
                    changeJob((short) (job + 10));
                    break;
                case 30:
                case 40:
                case 50:
                case 60:
                case 80:
                case 100:
                case 120:
                case 160:
                    changeJob((short) (job + 1));
                    break;
            }
        } else if (MapleJob.is精靈遊俠(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.精靈遊俠1轉.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.精靈遊俠2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.精靈遊俠3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.精靈遊俠4轉.getId());
                    break;
            }
        } else if (MapleJob.is幻影俠盜(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.幻影俠盜1轉.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.幻影俠盜2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.幻影俠盜3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.幻影俠盜4轉.getId());
                    break;
            }
        } else if (MapleJob.is夜光(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.夜光1轉.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.夜光2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.夜光3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.夜光4轉.getId());
                    break;
            }
        } else if (MapleJob.is隱月(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.隱月1轉.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.隱月2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.隱月3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.隱月4轉.getId());
                    break;
            }
        } else if (MapleJob.is惡魔殺手(job)) {
            switch (getLevel()) {
                case 30:
                    changeJob((short) MapleJob.惡魔殺手2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.惡魔殺手3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.惡魔殺手4轉.getId());
                    break;
            }
        } else if (MapleJob.is惡魔復仇者(job)) {
            switch (getLevel()) {
                case 30:
                    changeJob((short) MapleJob.惡魔復仇者2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.惡魔復仇者3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.惡魔復仇者4轉.getId());
                    break;
            }
        } else if (MapleJob.is煉獄巫師(job)) {
            switch (getLevel()) {
                case 30:
                    changeJob((short) MapleJob.煉獄巫師2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.煉獄巫師3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.煉獄巫師4轉.getId());
                    break;
            }
        } else if (MapleJob.is狂豹獵人(job)) {
            switch (getLevel()) {
                case 30:
                    changeJob((short) MapleJob.狂豹獵人2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.狂豹獵人3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.狂豹獵人4轉.getId());
                    break;
            }
        } else if (MapleJob.is機甲戰神(job)) {
            switch (getLevel()) {
                case 30:
                    changeJob((short) MapleJob.機甲戰神2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.機甲戰神3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.機甲戰神4轉.getId());
                    break;
            }
        } else if (MapleJob.is傑諾(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.傑諾1轉.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.傑諾2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.傑諾3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.傑諾4轉.getId());
                    break;
            }
        } else if (MapleJob.is劍豪(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.劍豪1轉.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.劍豪2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.劍豪3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.劍豪4轉.getId());
                    break;
            }
        } else if (MapleJob.is陰陽師(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.陰陽師1轉.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.陰陽師2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.陰陽師3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.陰陽師4轉.getId());
                    break;
            }
        } else if (MapleJob.is米哈逸(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.米哈逸1轉.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.米哈逸2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.米哈逸3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.米哈逸4轉.getId());
                    break;
            }
        } else if (MapleJob.is凱撒(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.凱撒1轉.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.凱撒2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.凱撒3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.凱撒4轉.getId());
                    break;
            }
        } else if (MapleJob.is天使破壞者(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.天使破壞者1轉.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.天使破壞者2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.天使破壞者3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.天使破壞者4轉.getId());
                    break;
            }
        } else if (MapleJob.is幻獸師(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.幻獸師1轉.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.幻獸師2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.幻獸師3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.幻獸師4轉.getId());
                    break;
            }
        } else if (MapleJob.is皮卡啾(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.皮卡啾1轉.getId());
                    break;
            }
        } else if (MapleJob.is凱內西斯(job)) {
            switch (getLevel()) {
                case 10:
                    changeJob((short) MapleJob.凱內西斯1轉.getId());
                    break;
                case 30:
                    changeJob((short) MapleJob.凱內西斯2轉.getId());
                    break;
                case 60:
                    changeJob((short) MapleJob.凱內西斯3轉.getId());
                    break;
                case 100:
                    changeJob((short) MapleJob.凱內西斯4轉.getId());
                    break;
            }
        }
    }

    public void sendPolice(int greason, String reason, int duration) {
        client.getSession().write(CWvsContext.GMPoliceMessage(String.format("You have been blocked by #bMapleGM for the %s reason.#k", "HACK")));
        ban(reason, true);
        WorldTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                client.disconnect(false, false); //FAGGOTS
            }
        }, duration);
    }

    public void sendPolice(String text) {
        client.getSession().write(CWvsContext.GMPoliceMessage(text));
        ban(text, true);
        WorldTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                client.disconnect(false, false); //FAGGOTS
            }
        }, 6000);
    }

    public void changeKeybinding(int key, byte type, int action) {
        if (type != 0) {
            keylayout.Layout().put(key, new Pair<>(type, action));
        } else {
            keylayout.Layout().remove(key);
        }
    }

    public void changeKeybinding(String key_name, byte type, int action) {
        int key;
        switch (key_name.toUpperCase()) {
            case "F1":
            case "F2":
            case "F3":
            case "F4":
            case "F5":
            case "F6":
            case "F7":
            case "F8":
            case "F9":
            case "F10":
            case "F11":
            case "F12":
                key = 58 + Integer.parseInt(key_name.replace("F", ""));
                break;
            case "1":
            case "!":
            case "2":
            case "@":
            case "3":
            case "#":
            case "4":
            case "$":
            case "5":
            case "%":
            case "6":
            case "^":
            case "7":
            case "&":
            case "8":
            case "*":
            case "9":
            case "(":
                key = 1 + Integer.parseInt(key_name);
                break;
            case "0":
            case ")":
                key = 11;
                break;
            case "-":
            case "_":
                key = 12;
                break;
            case "=":
            case "+":
                key = 13;
                break;
            default:
                key = -1;
                break;
        }
        if (key != -1) {
            if (type != 0) {
                keylayout.Layout().put(key, new Pair<>(type, action));
            } else {
                keylayout.Layout().remove(key);
            }
        }
    }

    public void sendMacros() {
        for (int i = 0; i < 5; i++) {
            if (skillMacros[i] != null) {
                client.getSession().write(CField.getMacros(skillMacros));
                break;
            }
        }
    }

    public void updateMacros(int position, SkillMacro updateMacro) {
        skillMacros[position] = updateMacro;
        changed_skillmacros = true;
    }

    public final SkillMacro[] getMacros() {
        return skillMacros;
    }

    public void tempban(String reason, Calendar duration, int greason, boolean IPMac) {
        if (IPMac) {
            client.banMacs();
        }
        client.getSession().write(CWvsContext.GMPoliceMessage(true));
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            if (IPMac) {
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, client.getSession().getRemoteAddress().toString().split(":")[0]);
                ps.execute();
                ps.close();
            }

            client.getSession().close(true);

            ps = con.prepareStatement("UPDATE accounts SET tempban = ?, banreason = ?, greason = ? WHERE id = ?");
            Timestamp TS = new Timestamp(duration.getTimeInMillis());
            ps.setTimestamp(1, TS);
            ps.setString(2, reason);
            ps.setInt(3, greason);
            ps.setInt(4, accountid);
            ps.execute();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("Error while tempbanning" + ex);
        }

    }

    public int getMaxHp() {
        return getStat().getMaxHp();
    }

    public int getMaxMp() {
        return getStat().getMaxMp();
    }

    public void setHp(int amount) {
        getStat().setHp(amount, this);
    }

    public void setMp(int amount) {
        getStat().setMp(amount, this);
    }

    public void logProcess(String path) {
        if (path == null) {
            return;
        }
        StringBuilder ret = new StringBuilder();
        for (MapleProcess mp : Process) {
            ret.append("\r\n---------------------------------------------------------------------\r\n");
            ret.append("進程路徑：").append(mp.getPath()).append("\r\nMD5值：").append(mp.getMD5()).append("\r\n");
        }
        ret.append("\r\n");
        FileoutputUtil.log(path, ret.toString());
    }

    public final boolean ban(String reason, boolean IPMac, boolean autoban, boolean hellban) {
        if (lastmonthfameids == null) {
            throw new RuntimeException("Trying to ban a non-loaded character (testhack)");
        }
        client.getSession().write(CWvsContext.GMPoliceMessage(true));
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET banned = ?, banreason = ? WHERE id = ?");
            ps.setInt(1, autoban ? 2 : 1);
            ps.setString(2, reason);
            ps.setInt(3, accountid);
            ps.execute();
            ps.close();

            if (IPMac) {
                client.banMacs();
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, client.getSessionIPAddress());
                ps.execute();
                ps.close();

                if (hellban) {
                    try (PreparedStatement psa = con.prepareStatement("SELECT * FROM accounts WHERE id = ?")) {
                        psa.setInt(1, accountid);
                        try (ResultSet rsa = psa.executeQuery()) {
                            if (rsa.next()) {
                                try (PreparedStatement pss = con.prepareStatement("UPDATE accounts SET banned = ?, banreason = ? WHERE email = ? OR SessionIP = ?")) {
                                    pss.setInt(1, autoban ? 2 : 1);
                                    pss.setString(2, reason);
                                    pss.setString(3, rsa.getString("email"));
                                    pss.setString(4, client.getSessionIPAddress());
                                    pss.execute();
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error while banning" + ex);
            return false;
        }
        client.getSession().close(true);
        logProcess(FileoutputUtil.BanLog_Process_Dir + getName() + "_" + getId() + ".txt");
        return true;
    }

    public static boolean ban(String id, String reason, boolean accountId, int gmlevel, boolean hellban) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps;
            if (id.matches("/[0-9]{1,3}\\..*")) {
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, id);
                ps.execute();
                ps.close();
                return true;
            }
            if (accountId) {
                ps = con.prepareStatement("SELECT id FROM accounts WHERE name = ?");
            } else {
                ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
            }
            boolean ret = false;
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int z = rs.getInt(1);
                    try (PreparedStatement psb = con.prepareStatement("UPDATE accounts SET banned = 1, banreason = ? WHERE id = ? AND gm < ?")) {
                        psb.setString(1, reason);
                        psb.setInt(2, z);
                        psb.setInt(3, gmlevel);
                        psb.execute();
                    }

                    if (gmlevel > 100) { //admin ban
                        try (PreparedStatement psa = con.prepareStatement("SELECT * FROM accounts WHERE id = ?")) {
                            psa.setInt(1, z);
                            try (ResultSet rsa = psa.executeQuery()) {
                                if (rsa.next()) {
                                    String sessionIP = rsa.getString("sessionIP");
                                    if (sessionIP != null && sessionIP.matches("/[0-9]{1,3}\\..*")) {
                                        try (PreparedStatement psz = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)")) {
                                            psz.setString(1, sessionIP);
                                            psz.execute();
                                        }
                                    }
                                    if (rsa.getString("macs") != null) {
                                        String[] macData = rsa.getString("macs").split(", ");
                                        if (macData.length > 0) {
                                            MapleClient.banMacs(macData);
                                        }
                                    }
                                    if (hellban) {
                                        try (PreparedStatement pss = con.prepareStatement("UPDATE accounts SET banned = 1, banreason = ? WHERE email = ?" + (sessionIP == null ? "" : " OR SessionIP = ?"))) {
                                            pss.setString(1, reason);
                                            pss.setString(2, rsa.getString("email"));
                                            if (sessionIP != null) {
                                                pss.setString(3, sessionIP);
                                            }
                                            pss.execute();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    ret = true;
                }
            }
            ps.close();
            return ret;
        } catch (SQLException ex) {
            System.err.println("Error while banning" + ex);
        }
        return false;
    }

    /**
     * Oid of players is always = the cid
     *
     * @return
     */
    @Override
    public int getObjectId() {
        return getId();
    }

    /**
     * Throws unsupported operation exception, oid of players is read only
     *
     * @param id
     */
    @Override
    public void setObjectId(int id) {
        throw new UnsupportedOperationException();
    }

    public MapleStorage getStorage() {
        return storage;
    }

    public void addVisibleMapObject(MapleMapObject mo) {
        if (clone) {
            return;
        }
        visibleMapObjectsLock.writeLock().lock();
        try {
            visibleMapObjects.add(mo);
        } finally {
            visibleMapObjectsLock.writeLock().unlock();
        }
    }

    public void removeVisibleMapObject(MapleMapObject mo) {
        if (clone) {
            return;
        }
        visibleMapObjectsLock.writeLock().lock();
        try {
            visibleMapObjects.remove(mo);
        } finally {
            visibleMapObjectsLock.writeLock().unlock();
        }
    }

    public boolean isMapObjectVisible(MapleMapObject mo) {
        visibleMapObjectsLock.readLock().lock();
        try {
            return !clone && visibleMapObjects.contains(mo);
        } finally {
            visibleMapObjectsLock.readLock().unlock();
        }
    }

    public Collection<MapleMapObject> getAndWriteLockVisibleMapObjects() {
        visibleMapObjectsLock.writeLock().lock();
        return visibleMapObjects;
    }

    public void unlockWriteVisibleMapObjects() {
        visibleMapObjectsLock.writeLock().unlock();
    }

    public boolean isAlive() {
        return stats.getHp() > 0;
    }

    @Override
    public void sendDestroyData(MapleClient client) {
        client.getSession().write(CField.removePlayerFromMap(this.getObjectId()));
        for (final WeakReference<MapleCharacter> chr : clones) {
            if (chr.get() != null) {
                chr.get().sendDestroyData(client);
            }
        }
        //don't need this, client takes care of it
        /*
         * if (dragon != null) {
         * client.getSession().write(CField.removeDragon(this.getId())); } if
         * (android != null) {
         * client.getSession().write(CField.deactivateAndroid(this.getId())); }
         * if (summonedFamiliar != null) {
         * client.getSession().write(CField.removeFamiliar(this.getId())); }
         */
        XenonSupplyTask.cancel(true);
        XenonSupplyTask = null;
    }

    @Override
    public void sendSpawnData(MapleClient client) {
        if (client.getPlayer().allowedToTarget(this)) {
            //if (client.getPlayer() != this)
            client.getSession().write(CField.spawnPlayerMapobject(this));
            client.getSession().write(CField.getEffectSwitch(getId(), getEffectSwitch()));
            if (getParty() != null && !isClone()) {
                updatePartyMemberHP();
                receivePartyMemberHP();
            }

            for (final MaplePet pet : pets) {
                if (pet.getSummoned()) {
                    client.getSession().write(PetPacket.updatePet(pet, getInventory(MapleInventoryType.CASH).getItem((short) (byte) pet.getInventoryPosition()), false));
                    client.getSession().write(PetPacket.showPet(this, pet, false, false));
                    client.getSession().write(PetPacket.showPetUpdate(this, pet.getUniqueId(), (byte) (pet.getSummonedValue() - 1)));
//                    client.getSession().write(PetPacket.petStatUpdate(this));
                }
            }
            for (final WeakReference<MapleCharacter> chr : clones) {
                if (chr.get() != null) {
                    chr.get().sendSpawnData(client);
                }
            }
            if (dragon != null) {
                client.getSession().write(CField.spawnDragon(dragon));
            }
            if (haku != null) {
                client.getSession().write(CField.spawnHaku(haku));
            }
            if (android != null) {
                client.getSession().write(CField.spawnAndroid(this, android));
            }
            if (summonedFamiliar != null) {
                client.getSession().write(CField.spawnFamiliar(summonedFamiliar, true, true));
            }
            if (summons != null && summons.size() > 0) {
                summonsLock.readLock().lock();
                try {
                    for (final MapleSummon summon : summons) {
                        client.getSession().write(SummonPacket.spawnSummon(summon, false));
                    }
                } finally {
                    summonsLock.readLock().unlock();
                }
            }
            if (followid > 0 && followon) {
                client.getSession().write(CField.followEffect(followinitiator ? followid : id, followinitiator ? id : followid, null));
            }
        }
    }

    public final void equipChanged() {
        if (map == null) {
            return;
        }
        map.broadcastMessage(this, CField.updateCharLook(this, false), false);
        stats.recalcLocalStats(this);
        if (getMessenger() != null) {
            World.Messenger.updateMessenger(getMessenger().getId(), getName(), client.getChannel());
        }
    }

    public final MaplePet getPet(final int index) {
        byte count = 0;
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                if (count == index) {
                    return pet;
                }
                count++;
            }
        }
        return null;
    }

    public void removePetCS(MaplePet pet) {
        pets.remove(pet);
    }

    public void addPet(final MaplePet pet) {
        if (pets.contains(pet)) {
            pets.remove(pet);
        }
        pets.add(pet);
        // So that the pet will be at the last
        // Pet index logic :(
    }

    public void removePet(MaplePet pet, boolean shiftLeft) {
        pet.setSummoned(0);
        /*
         * int slot = -1; for (int i = 0; i < 3; i++) { if (pets[i] != null) {
         * if (pets[i].getUniqueId() == pet.getUniqueId()) { pets[i] = null;
         * slot = i; break; } } } if (shiftLeft) { if (slot > -1) { for (int i =
         * slot; i < 3; i++) { if (i != 2) { pets[i] = pets[i + 1]; } else {
         * pets[i] = null; } } } }
         */
    }

    public final byte getPetIndex(final MaplePet petz) {
        byte count = 0;
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                if (pet.getUniqueId() == petz.getUniqueId()) {
                    return count;
                }
                count++;
            }
        }
        return -1;
    }

    public final byte getPetIndex(final int petId) {
        byte count = 0;
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                if (pet.getUniqueId() == petId) {
                    return count;
                }
                count++;
            }
        }
        return -1;
    }

    public final List<MaplePet> getSummonedPets() {
        List<MaplePet> ret = new ArrayList<>();
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                ret.add(pet);
            }
        }
        return ret;
    }

    public final byte getPetById(final int petId) {
        byte count = 0;
        for (final MaplePet pet : pets) {
            if (pet.getSummoned()) {
                if (pet.getPetItemId() == petId) {
                    return count;
                }
                count++;
            }
        }
        return -1;
    }

    public final List<MaplePet> getPets() {
        return pets;
    }

    public final void unequipAllPets() {
        for (final MaplePet pet : pets) {
            if (pet != null) {
                unequipPet(pet, true, false);
            }
        }
    }

    public void unequipPet(MaplePet pet, boolean shiftLeft, boolean hunger) {
        if (pet.getSummoned()) {
            pet.saveToDb();

            client.getSession().write(PetPacket.updatePet(pet, getInventory(MapleInventoryType.CASH).getItem((byte) pet.getInventoryPosition()), true));
            if (map != null) {
                map.broadcastMessage(this, PetPacket.showPet(this, pet, true, hunger), true);
            }
            removePet(pet, shiftLeft);
            //List<Pair<MapleStat, Integer>> stats = new ArrayList<Pair<MapleStat, Integer>>();
            //stats.put(MapleStat.PET, Integer.valueOf(0)));
            //showpetupdate isn't done here...
//            client.getSession().write(PetPacket.petStatUpdate(this));
            client.getSession().write(CWvsContext.enableActions());
        }
    }

    /*
     * public void shiftPetsRight() { if (pets[2] == null) { pets[2] = pets[1];
     * pets[1] = pets[0]; pets[0] = null; } }
     */
    public final long getLastFameTime() {
        return lastfametime;
    }

    public final List<Integer> getFamedCharacters() {
        return lastmonthfameids;
    }

    public final List<Integer> getBattledCharacters() {
        return lastmonthbattleids;
    }

    public FameStatus canGiveFame(MapleCharacter from) {
        if (lastfametime >= System.currentTimeMillis() - 60 * 60 * 24 * 1000) {
            return FameStatus.NOT_TODAY;
        } else if (from == null || lastmonthfameids == null || lastmonthfameids.contains(from.getId())) {
            return FameStatus.NOT_THIS_MONTH;
        }
        return FameStatus.OK;
    }

    public void hasGivenFame(MapleCharacter to) {
        lastfametime = System.currentTimeMillis();
        lastmonthfameids.add(to.getId());
        Connection con = DatabaseConnection.getConnection();
        try {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO famelog (characterid, characterid_to) VALUES (?, ?)")) {
                ps.setInt(1, getId());
                ps.setInt(2, to.getId());
                ps.execute();
            }
        } catch (SQLException e) {
            System.err.println("ERROR writing famelog for char " + getName() + " to " + to.getName() + e);
        }
    }

    public boolean canBattle(MapleCharacter to) {
        return to != null && lastmonthbattleids != null && !lastmonthbattleids.contains(to.getAccountID());
    }

    public void hasBattled(MapleCharacter to) {
        lastmonthbattleids.add(to.getAccountID());
        Connection con = DatabaseConnection.getConnection();
        try {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO battlelog (accid, accid_to) VALUES (?, ?)")) {
                ps.setInt(1, getAccountID());
                ps.setInt(2, to.getAccountID());
                ps.execute();
            }
        } catch (SQLException e) {
            System.err.println("ERROR writing battlelog for char " + getName() + " to " + to.getName() + e);
        }
    }

    public final MapleKeyLayout getKeyLayout() {
        return this.keylayout;
    }

    public MapleParty getParty() {
        if (party == null) {
            return null;
        } else if (party.isDisbanded()) {
            party = null;
        }
        return party;
    }

    public byte getWorld() {
        return world;
    }

    public void setWorld(byte world) {
        this.world = world;
    }

    public void setParty(MapleParty party) {
        this.party = party;
    }

    public MapleTrade getTrade() {
        return trade;
    }

    public void setTrade(MapleTrade trade) {
        this.trade = trade;
    }

    public EventInstanceManager getEventInstance() {
        return eventInstance;
    }

    public void setEventInstance(EventInstanceManager eventInstance) {
        this.eventInstance = eventInstance;
    }

    public void setEventInstanceAzwan(EventManager eventInstance) {
        this.eventInstanceAzwan = eventInstance;
    }

    public void addDoor(MapleDoor door) {
        doors.add(door);
    }

    public void clearDoors() {
        doors.clear();
    }

    public List<MapleDoor> getDoors() {
        return new ArrayList<>(doors);
    }

    public void addMechDoor(MechDoor door) {
        mechDoors.add(door);
    }

    public void clearMechDoors() {
        mechDoors.clear();
    }

    public List<MechDoor> getMechDoors() {
        return new ArrayList<>(mechDoors);
    }

    public void setSmega() {
        if (smega) {
            smega = false;
            dropMessage(5, "You have set megaphone to disabled mode");
        } else {
            smega = true;
            dropMessage(5, "You have set megaphone to enabled mode");
        }
    }

    public boolean getSmega() {
        return smega;
    }

    public List<MapleSummon> getSummonsReadLock() {
        summonsLock.readLock().lock();
        return summons;
    }

    public int getSummonsSize() {
        return summons.size();
    }

    public void unlockSummonsReadLock() {
        summonsLock.readLock().unlock();
    }

    public void addSummon(MapleSummon s) {
        summonsLock.writeLock().lock();
        try {
            summons.add(s);
        } finally {
            summonsLock.writeLock().unlock();
        }
    }

    public void removeSummon(MapleSummon s) {
        summonsLock.writeLock().lock();
        try {
            summons.remove(s);
        } finally {
            summonsLock.writeLock().unlock();
        }
    }

    public int getChair() {
        return chair;
    }

    public int getItemEffect() {
        return itemEffect;
    }

    public int getTitleEffect() {
        String info = getInfoQuest(GameConstants.稱號);
        return info == null || info.isEmpty() ? 0 : Integer.parseInt(info);
    }

    public int getDamageSkin() {
        String info = getInfoQuest(GameConstants.傷害字型);
        return info == null || info.isEmpty() ? 0 : Integer.parseInt(info);
    }

    public void setChair(int chair) {
        this.chair = chair;
        stats.relocHeal(this);
    }

    public void setItemEffect(int itemEffect) {
        this.itemEffect = itemEffect;
    }

    public void setTitleEffect(int titleEffect) {
        MapleQuestStatus queststatus;
        if (titleEffect == 0) {
            queststatus = getQuestRemove(MapleQuest.getInstance(GameConstants.稱號));
        } else {
            queststatus = getQuestNAdd(MapleQuest.getInstance(GameConstants.稱號));
            queststatus.setCustomData(String.valueOf(titleEffect));
        }
        if (queststatus != null) {
            updateQuest(queststatus);
        }
    }

    public void setDamageSkin(int damageSkin) {
        MapleQuestStatus queststatus;
        if (damageSkin == 0) {
            queststatus = getQuestRemove(MapleQuest.getInstance(GameConstants.傷害字型));
        } else {
            queststatus = getQuestNAdd(MapleQuest.getInstance(GameConstants.傷害字型));
            queststatus.setCustomData(String.valueOf(damageSkin));
        }
        if (queststatus != null) {
            updateQuest(queststatus);
        }
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.PLAYER;
    }

    public int getFamilyId() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getFamilyId();
    }

    public int getSeniorId() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getSeniorId();
    }

    public int getJunior1() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getJunior1();
    }

    public int getJunior2() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getJunior2();
    }

    public int getCurrentRep() {
        return currentrep;
    }

    public int getTotalRep() {
        return totalrep;
    }

    public void setCurrentRep(int _rank) {
        currentrep = _rank;
        if (mfc != null) {
            mfc.setCurrentRep(_rank);
        }
    }

    public void setTotalRep(int _rank) {
        totalrep = _rank;
        if (mfc != null) {
            mfc.setTotalRep(_rank);
        }
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public void increaseTotalWins() {
        totalWins++;
    }

    public void increaseTotalLosses() {
        totalLosses++;
    }

    public int getGuildId() {
        return guildid;
    }

    public byte getGuildRank() {
        return guildrank;
    }

    public int getGuildContribution() {
        return guildContribution;
    }

    public void setGuildId(int _id) {
        guildid = _id;
        if (guildid > 0) {
            if (mgc == null) {
                mgc = new MapleGuildCharacter(this);
            } else {
                mgc.setGuildId(guildid);
            }
        } else {
            mgc = null;
            guildContribution = 0;
        }
    }

    public void setGuildRank(byte _rank) {
        guildrank = _rank;
        if (mgc != null) {
            mgc.setGuildRank(_rank);
        }
    }

    public void setGuildContribution(int _c) {
        this.guildContribution = _c;
        if (mgc != null) {
            mgc.setGuildContribution(_c);
        }
    }

    public MapleGuildCharacter getMGC() {
        return mgc;
    }

    public void setAllianceRank(byte rank) {
        allianceRank = rank;
        if (mgc != null) {
            mgc.setAllianceRank(rank);
        }
    }

    public byte getAllianceRank() {
        return allianceRank;
    }

    public MapleGuild getGuild() {
        if (getGuildId() <= 0) {
            return null;
        }
        return World.Guild.getGuild(getGuildId());
    }

    public void setJob(int j) {
        this.job = (short) j;
    }

    public void guildUpdate() {
        if (guildid <= 0) {
            return;
        }
        mgc.setLevel((short) level);
        mgc.setJobId(job);
        World.Guild.memberLevelJobUpdate(mgc);
    }

    public void saveGuildStatus() {
        MapleGuild.setOfflineGuildStatus(guildid, guildrank, guildContribution, allianceRank, id);
    }

    public void familyUpdate() {
        if (mfc == null) {
            return;
        }
        World.Family.memberFamilyUpdate(mfc, this);
    }

    public void saveFamilyStatus() {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("UPDATE characters SET familyid = ?, seniorid = ?, junior1 = ?, junior2 = ? WHERE id = ?")) {
                if (mfc == null) {
                    ps.setInt(1, 0);
                    ps.setInt(2, 0);
                    ps.setInt(3, 0);
                    ps.setInt(4, 0);
                } else {
                    ps.setInt(1, mfc.getFamilyId());
                    ps.setInt(2, mfc.getSeniorId());
                    ps.setInt(3, mfc.getJunior1());
                    ps.setInt(4, mfc.getJunior2());
                }
                ps.setInt(5, id);
                ps.executeUpdate();
            }
        } catch (SQLException se) {
            System.err.println("SQLException: " + se.getLocalizedMessage());
        }
        //MapleFamily.setOfflineFamilyStatus(familyid, seniorid, junior1, junior2, currentrep, totalrep, id);
    }

    public void modifyCSPoints(int type, int quantity) {
        modifyCSPoints(type, quantity, false);
    }

    public void modifyCSPoints(int type, int quantity, boolean show) {
        switch (type) {
            case 1:
                if (nxcredit + quantity < 0) {
                    if (show) {
                        dropMessage(-1, "樂豆點已達到上限.");
                    }
                    return;
                }
                ///if (quantity > 0) {
                //    quantity = (quantity / 2); //stuff is cheaper lol
                //}
                nxcredit += quantity;
                break;
            case 4:
                if (acash + quantity < 0) {
                    if (show) {
                        dropMessage(-1, "acash已達到上限.");
                    }
                    return;
                }
                //if (quantity > 0) {
                //    quantity = (quantity / 2); //stuff is cheaper lol
                //}
                acash += quantity;
                break;
            case 2:
                if (maplepoints + quantity < 0) {
                    if (show) {
                        dropMessage(-1, "楓點已達到上限.");
                    }
                    return;
                }
                maplepoints += quantity;
                client.getSession().write(CWvsContext.updateMaplePoint(maplepoints));
                break;
            default:
                break;
        }
        if (show && quantity != 0) {
            dropMessage(-1, (quantity > 0 ? "獲得 " : "失去 ") + Math.abs(quantity) + (type == 1 ? " 樂豆點" : type == 2 ? " 楓點" : "未知點數") + "。");
            client.getSession().write(EffectPacket.showDodgeChanceEffect());
        }
    }

    public int getCSPoints(int type) {
        switch (type) {
            case 1:
                return nxcredit;
            case 2:
                return maplepoints;
            case 4:
                return acash;
            default:
                return 0;
        }
    }

    public final boolean hasEquipped(int itemid) {
        return inventory[MapleInventoryType.EQUIPPED.ordinal()].countById(itemid) >= 1;
    }

    public final boolean haveItem(int itemid, int quantity, boolean checkEquipped, boolean greaterOrEquals) {
        final MapleInventoryType type = GameConstants.getInventoryType(itemid);
        int possesed = inventory[type.ordinal()].countById(itemid);
        if (checkEquipped && type == MapleInventoryType.EQUIP) {
            possesed += inventory[MapleInventoryType.EQUIPPED.ordinal()].countById(itemid);
        }
        if (greaterOrEquals) {
            return possesed >= quantity;
        } else {
            return possesed == quantity;
        }
    }

    public final boolean haveItem(int itemid, int quantity) {
        return haveItem(itemid, quantity, true, true);
    }

    public final boolean haveItem(int itemid) {
        return haveItem(itemid, 1, true, true);
    }

    public static boolean tempban(String reason, Calendar duration, int greason, int accountid) {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("UPDATE accounts SET tempban = ?, banreason = ?, greason = ? WHERE id = ?")) {
                Timestamp TS = new Timestamp(duration.getTimeInMillis());
                ps.setTimestamp(1, TS);
                ps.setString(2, reason);
                ps.setInt(3, greason);
                ps.setInt(4, accountid);
                ps.executeUpdate();
            }
            return true;
        } catch (SQLException ex) {
            //log.error("Error while tempbanning", ex);
        }
        return false;
    }

    public void setGM(byte level) {
        this.gmLevel = level;
    }

    public List<Item> getRebuy() {
        return this.rebuy;
    }

    public void setRebuy(List<Item> rebuy) {
        this.rebuy = rebuy;
    }

    public static enum FameStatus {

        OK,
        NOT_TODAY,
        NOT_THIS_MONTH
    }

    public byte getBuddyCapacity() {
        return buddylist.getCapacity();
    }

    public void setBuddyCapacity(byte capacity) {
        buddylist.setCapacity(capacity);
        client.getSession().write(BuddylistPacket.updateBuddyCapacity(capacity));
    }

    public MapleMessenger getMessenger() {
        return messenger;
    }

    public void setMessenger(MapleMessenger messenger) {
        this.messenger = messenger;
    }

    public void addCooldown(int skillId, long startTime, long length) {
        if (isAdmin()) {
            showMessage(10, "伺服器管理員消除技能冷卻時間(原時間:" + length / 1000 + "秒)");
            client.getSession().write(CField.skillCooldown(skillId, 0));
        } else {
            coolDowns.put(skillId, new MapleCoolDownValueHolder(skillId, startTime, length));
        }
    }

    public void removeCooldown(int skillId) {
        if (coolDowns.containsKey(skillId)) {
            coolDowns.remove(skillId);
        }
    }

    public boolean skillisCooling(int skillId) {
        return coolDowns.containsKey(skillId);
    }

    public void giveCoolDowns(final int skillid, long starttime, long length) {
        addCooldown(skillid, starttime, length);
    }

    public void giveCoolDowns(final List<MapleCoolDownValueHolder> cooldowns) {
        int time;
        if (cooldowns != null) {
            for (MapleCoolDownValueHolder cooldown : cooldowns) {
                coolDowns.put(cooldown.skillId, cooldown);
            }
        } else {
            try {
                Connection con = DatabaseConnection.getConnection();
                ResultSet rs;
                try (PreparedStatement ps = con.prepareStatement("SELECT SkillID,StartTime,length FROM skills_cooldowns WHERE charid = ?")) {
                    ps.setInt(1, getId());
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (rs.getLong("length") + rs.getLong("StartTime") - System.currentTimeMillis() <= 0) {
                            continue;
                        }
                        giveCoolDowns(rs.getInt("SkillID"), rs.getLong("StartTime"), rs.getLong("length"));
                    }
                }
                rs.close();
                deleteWhereCharacterId(con, "DELETE FROM skills_cooldowns WHERE charid = ?");

            } catch (SQLException e) {
                System.err.println("Error while retriving cooldown from SQL storage");
            }
        }
    }

    public int getCooldownSize() {
        return coolDowns.size();
    }

    public int getDiseaseSize() {
        return diseases.size();
    }

    public List<MapleCoolDownValueHolder> getCooldowns() {
        List<MapleCoolDownValueHolder> ret = new ArrayList<>();
        for (MapleCoolDownValueHolder mc : coolDowns.values()) {
            if (mc != null) {
                ret.add(mc);
            }
        }
        return ret;
    }

    public final List<MapleDiseaseValueHolder> getAllDiseases() {
        return new ArrayList<>(diseases.values());
    }

    public final boolean hasDisease(final MapleDisease dis) {
        return diseases.containsKey(dis);
    }

    public void giveDebuff(MapleDisease disease, MobSkill skill) {
        giveDebuff(disease, skill.getX(), skill.getDuration(), skill.getSkillId(), skill.getSkillLevel());
    }

    public void giveDebuff(MapleDisease disease, int x, long duration, int skillid, int level) {
        if ((this.map != null) && (!hasDisease(disease))) {
            if ((disease != MapleDisease.SEDUCE) && (disease != MapleDisease.STUN) && (disease != MapleDisease.FLAG)
                    && (getBuffedValue(MapleBuffStat.HOLY_SHIELD) != null)) {
                return;
            }

            int mC = getBuffSource(MapleBuffStat.MECH_CHANGE);
            if ((mC > 0) && (mC != 35121005)) {
                return;
            }
            if ((this.stats.ASR > 0) && (Randomizer.nextInt(100) < this.stats.ASR)) {
                return;
            }

            this.diseases.put(disease, new MapleDiseaseValueHolder(disease, System.currentTimeMillis(), duration - this.stats.decreaseDebuff));
            this.client.getSession().write(CWvsContext.BuffPacket.giveDebuff(disease, x, skillid, level, (int) duration));
            this.map.broadcastMessage(this, CWvsContext.BuffPacket.giveForeignDebuff(this.id, disease, skillid, level, x), false);

            if ((x > 0) && (disease == MapleDisease.POISON)) {
                addHP((int) (-(x * ((duration - this.stats.decreaseDebuff) / 1000L))));
            }
        }
    }

    public final void giveSilentDebuff(final List<MapleDiseaseValueHolder> ld) {
        if (ld != null) {
            for (final MapleDiseaseValueHolder disease : ld) {
                diseases.put(disease.disease, disease);
            }
        }
    }

    public void dispelDebuff(MapleDisease debuff) {
        if (hasDisease(debuff)) {
            client.getSession().write(BuffPacket.cancelDebuff(debuff));
            map.broadcastMessage(this, BuffPacket.cancelForeignDebuff(id, debuff), false);

            diseases.remove(debuff);
        }
    }

    public void dispelDebuffs() {
        List<MapleDisease> diseasess = new ArrayList<>(diseases.keySet());
        for (MapleDisease d : diseasess) {
            dispelDebuff(d);
        }
    }

    public void cancelAllDebuffs() {
        diseases.clear();
    }

    public void setLevel(final short level) {
        this.level = level;
    }

    public void sendNote(String to, String msg) {
        sendNote(to, msg, 0);
    }

    public void sendNote(String to, String msg, int fame) {
        MapleCharacterUtil.sendNote(to, getName(), msg, fame);
    }

    public void sendMapleGMNote(String to, String msg, int fame) {
        MapleCharacterUtil.sendNote(to, "楓之谷GM", msg, fame);
    }

    public void showNote() {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM notes WHERE `to`=?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
                ps.setString(1, getName());
                try (ResultSet rs = ps.executeQuery()) {
                    rs.last();
                    int count = rs.getRow();
                    rs.first();
                    client.getSession().write(CSPacket.showNotes(rs, count));
                }
            }
        } catch (SQLException e) {
            System.err.println("Unable to show note" + e);
        }
    }

    public void deleteNote(int id, int fame) {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT gift FROM notes WHERE `id`=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if (rs.getInt("gift") == fame && fame > 0) { //not exploited! hurray
                    addFame(fame);
                    updateSingleStat(MapleStat.FAME, getFame());
                    client.getSession().write(InfoPacket.getShowFameGain(fame));
                }
            }
            rs.close();
            ps.close();
            ps = con.prepareStatement("DELETE FROM notes WHERE `id`=?");
            ps.setInt(1, id);
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Unable to delete note" + e);
        }
    }

    public int getMulungEnergy() {
        return mulung_energy;
    }

    public void mulung_EnergyModify(boolean inc) {
        if (inc) {
            if (mulung_energy + 100 > 10000) {
                mulung_energy = 10000;
            } else {
                mulung_energy += 100;
            }
        } else {
            mulung_energy = 0;
        }
        client.getSession().write(CWvsContext.MulungEnergy(mulung_energy));
    }

    public void writeMulungEnergy() {
        client.getSession().write(CWvsContext.MulungEnergy(mulung_energy));
    }

    public void writeEnergy(String type, String inc) {
        client.getSession().write(CWvsContext.sendPyramidEnergy(type, inc));
    }

    public void writeStatus(String type, String inc) {
        client.getSession().write(CWvsContext.sendGhostStatus(type, inc));
    }

    public void writePoint(String type, String inc) {
        client.getSession().write(CWvsContext.sendGhostPoint(type, inc));
    }

    public final short getCombo() {
        return combo;
    }

    public void setCombo(final short combo) {
        this.combo = combo;
    }

    public final long getLastCombo() {
        return lastCombo;
    }

    public void setLastCombo(final long combo) {
        this.lastCombo = combo;
    }

    public final long getKeyDownSkill_Time() {
        return keydown_skill;
    }

    public void setKeyDownSkill_Time(final long keydown_skill) {
        this.keydown_skill = keydown_skill;
    }

    public void checkBerserk() { //berserk is special in that it doesn't use worldtimer :)
        if (job != 132 || lastBerserkTime < 0 || lastBerserkTime + 10000 > System.currentTimeMillis()) {
            return;
        }
        final Skill BerserkX = SkillFactory.getSkill(1320006);
        final int skilllevel = getTotalSkillLevel(BerserkX);
        if (skilllevel >= 1 && map != null) {
            lastBerserkTime = System.currentTimeMillis();
            final MapleStatEffect ampStat = BerserkX.getEffect(skilllevel);
            stats.Berserk = stats.getHp() * 100 / stats.getCurrentMaxHp() >= ampStat.getX();
            client.getSession().write(EffectPacket.showBuffEffect(1320006, SpecialEffectType.LOCAL_SKILL, getLevel(), skilllevel, (byte) (stats.Berserk ? 1 : 0)));
            map.broadcastMessage(this, EffectPacket.showBuffeffect(getId(), 1320006, SpecialEffectType.LOCAL_SKILL, getLevel(), skilllevel, (byte) (stats.Berserk ? 1 : 0)), false);
        } else {
            lastBerserkTime = -1; // somebody thre? O_O
        }
    }

    public void setChalkboard(String text) {
        this.chalktext = text;
        if (map != null) {
            map.broadcastMessage(CSPacket.useChalkboard(getId(), text));
        }
    }

    public String getChalkboard() {
        return chalktext;
    }

    public MapleMount getMount() {
        return mount;
    }

    public int[] getWishlist() {
        return wishlist;
    }

    public void clearWishlist() {
        for (int i = 0; i < 12; i++) {
            wishlist[i] = 0;
        }
        changed_wishlist = true;
    }

    public int getWishlistSize() {
        int ret = 0;
        for (int i = 0; i < 12; i++) {
            if (wishlist[i] > 0) {
                ret++;
            }
        }
        return ret;
    }

    public void setWishlist(int[] wl) {
        this.wishlist = wl;
        changed_wishlist = true;
    }

    public int[] getRocks() {
        return rocks;
    }

    public int getRockSize() {
        int ret = 0;
        for (int i = 0; i < 10; i++) {
            if (rocks[i] != 999999999) {
                ret++;
            }
        }
        return ret;
    }

    public void deleteFromRocks(int map) {
        for (int i = 0; i < 10; i++) {
            if (rocks[i] == map) {
                rocks[i] = 999999999;
                changed_trocklocations = true;
                break;
            }
        }
    }

    public void addRockMap() {
        if (getRockSize() >= 10) {
            return;
        }
        rocks[getRockSize()] = getMapId();
        changed_trocklocations = true;
    }

    public boolean isRockMap(int id) {
        for (int i = 0; i < 10; i++) {
            if (rocks[i] == id) {
                return true;
            }
        }
        return false;
    }

    public int[] getRegRocks() {
        return regrocks;
    }

    public int getRegRockSize() {
        int ret = 0;
        for (int i = 0; i < 5; i++) {
            if (regrocks[i] != 999999999) {
                ret++;
            }
        }
        return ret;
    }

    public void deleteFromRegRocks(int map) {
        for (int i = 0; i < 5; i++) {
            if (regrocks[i] == map) {
                regrocks[i] = 999999999;
                changed_regrocklocations = true;
                break;
            }
        }
    }

    public void addRegRockMap() {
        if (getRegRockSize() >= 5) {
            return;
        }
        regrocks[getRegRockSize()] = getMapId();
        changed_regrocklocations = true;
    }

    public boolean isRegRockMap(int id) {
        for (int i = 0; i < 5; i++) {
            if (regrocks[i] == id) {
                return true;
            }
        }
        return false;
    }

    public int[] getHyperRocks() {
        return hyperrocks;
    }

    public int getHyperRockSize() {
        int ret = 0;
        for (int i = 0; i < 13; i++) {
            if (hyperrocks[i] != 999999999) {
                ret++;
            }
        }
        return ret;
    }

    public void deleteFromHyperRocks(int map) {
        for (int i = 0; i < 13; i++) {
            if (hyperrocks[i] == map) {
                hyperrocks[i] = 999999999;
                changed_hyperrocklocations = true;
                break;
            }
        }
    }

    public void addHyperRockMap() {
        if (getRegRockSize() >= 13) {
            return;
        }
        hyperrocks[getHyperRockSize()] = getMapId();
        changed_hyperrocklocations = true;
    }

    public boolean isHyperRockMap(int id) {
        for (int i = 0; i < 13; i++) {
            if (hyperrocks[i] == id) {
                return true;
            }
        }
        return false;
    }

    public List<LifeMovementFragment> getLastRes() {
        return lastres;
    }

    public void setLastRes(List<LifeMovementFragment> lastres) {
        this.lastres = lastres;
    }

    public void dropMessage(int type, String message) {
        switch (type) {
            case -1:
                client.getSession().write(CWvsContext.getTopMsg(message));
                break;
            case -2:
                client.getSession().write(PlayerShopPacket.shopChat(message, 0)); //0 or what
                break;
            case -3:
                client.getSession().write(CField.getChatText(getId(), message, isSuperGM(), 0)); //1 = hide
                break;
            case -4:
                client.getSession().write(CField.getChatText(getId(), message, isSuperGM(), 1)); //1 = hide
                break;
            case -5:
                client.getSession().write(CField.getGameMessage(message, (short) 6)); //灰色
                break;
            case -6:
                client.getSession().write(CField.getGameMessage(message, (short) 10)); //白色背景
                break;
            case -7:
                client.getSession().write(CWvsContext.getMidMsg(message, false, 0));
                break;
            case -8:
                client.getSession().write(CWvsContext.getMidMsg(message, true, 0));
                break;
            case -9:
                client.getSession().write(CWvsContext.InfoPacket.showInfo(message));
                break;
            case -10:
                client.getSession().write(CField.getFollowMessage(message));
                break;
            case -11:
                client.getSession().write(CWvsContext.yellowChat(message));
                break;
            default:
                client.getSession().write(CWvsContext.broadcastMsg(type, message));
        }
    }

    //0 - 白(普聊)
    //1 - 綠(悄悄話)
    //2 - 粉紅(隊伍聊天)
    //3 - 橙(好友聊天)
    //4 - 淺紫(公會聊天)
    //5 - 淺綠(聯盟聊天)
    //6 - 灰(訊息)
    //7 - 亮黃
    //8 - 淡黃
    //9 - 藍
    //10 - 黑字白底
    //11 - 紅
    //12 - 藍字藍底
    //13 - 紅字粉紅底(喇叭)
    //14 - 紅字粉紅底(喇叭_有:時無字)
    //15 - 黑字黃底(喇叭)
    //16 - 紫
    //17 - 黑字綠底(有:時:前面的字變成[W:-1])
    //18 - 灰(喇叭)
    //19 - 黃
    //20 - 青
    //21 - 黑字黃底
    //22 - 藍("[]"裡面字會變黃色而"[]"不顯示)
    //23 - 淡黃
    //24 - 藍("[]"裡面字會變黃色"[]"顯示)
    //25 - 玫瑰紅(喇叭)
    //26 - 淺紫("[]"裡面字會變黃色"[]"顯示)
    //27 - 淺黃
    //28 - 橘黃
    //29 - 無
    //30 - 草綠
    //31 - 粉紅白底(喇叭)
    //32 - 黑字紅底(喇叭)
    //33 - 黑字綠底(喇叭)
    //34 - 黃字紅底(喇叭)
    //35 - 黑字粉紅底(喇叭)
    //36 - 黑字淺黃底(喇叭)
    //37 - 黃字紅底
    //38 - 白字半透大紅底(喇叭)
    //39 - 遊戲崩潰
    //40 - 遊戲崩潰
    //41 - 黑字綠底(喇叭背景)
    //42 - 黑字紅底(喇叭背景)
    //43 - 黑字淺黃底(喇叭背景)
    //44 - 深藍
    public void showMessage(int type, String msg) {
        client.getSession().write(CField.getGameMessage(msg, (short) type));
    }

    public void showInfo(String caption, boolean pink, String msg) {
        short type = (short) (pink ? 11 : 6);
        if (caption != null && !caption.isEmpty()) {
            msg = "[" + caption + "] " + msg;
        }
        showMessage(type, msg);
        dropMessage(-1, msg);
    }

    public IMaplePlayerShop getPlayerShop() {
        return playerShop;
    }

    public void setPlayerShop(IMaplePlayerShop playerShop) {
        this.playerShop = playerShop;
    }

    public int getConversation() {
        return inst.get();
    }

    public void setConversation(int inst) {
        this.inst.set(inst);
    }

    public int getDirection() {
        return insd.get();
    }

    public void setDirection(int inst) {
        this.insd.set(inst);
    }

    public MapleCarnivalParty getCarnivalParty() {
        return carnivalParty;
    }

    public void setCarnivalParty(MapleCarnivalParty party) {
        carnivalParty = party;
    }

    public void addCP(int ammount) {
        totalCP += ammount;
        availableCP += ammount;
    }

    public void useCP(int ammount) {
        availableCP -= ammount;
    }

    public int getAvailableCP() {
        return availableCP;
    }

    public int getTotalCP() {
        return totalCP;
    }

    public void resetCP() {
        totalCP = 0;
        availableCP = 0;
    }

    public void addCarnivalRequest(MapleCarnivalChallenge request) {
        pendingCarnivalRequests.add(request);
    }

    public final MapleCarnivalChallenge getNextCarnivalRequest() {
        return pendingCarnivalRequests.pollLast();
    }

    public void clearCarnivalRequests() {
        pendingCarnivalRequests = new LinkedList<>();
    }

    public void startMonsterCarnival(final int enemyavailable, final int enemytotal) {
        client.getSession().write(MonsterCarnivalPacket.startMonsterCarnival(this, enemyavailable, enemytotal));
    }

    public void CPUpdate(final boolean party, final int available, final int total, final int team) {
        client.getSession().write(MonsterCarnivalPacket.CPUpdate(party, available, total, team));
    }

    public void playerDiedCPQ(final String name, final int lostCP, final int team) {
        client.getSession().write(MonsterCarnivalPacket.playerDiedMessage(name, lostCP, team));
    }

    public boolean getCanTalk() {
        return this.canTalk;
    }

    public void canTalk(boolean talk) {
        this.canTalk = talk;
    }

    public double getEXPMod() {
        return stats.expMod;
    }

    public double getDropMod() {
        return stats.dropMod;
    }

    public int getCashMod() {
        return stats.cashMod;
    }

    public void setPoints(int p) {
        this.points = p;
    }

    public int getPoints() {
        return points;
    }

    public void setVPoints(int p) {
        this.vpoints = p;
    }

    public int getVPoints() {
        return vpoints;
    }

    public void setEPoints(int p) {
        this.epoints = p;
    }

    public int getEPoints() {
        return epoints;
    }

    public void setDPoints(int p) {
        this.dpoints = p;
    }

    public int getDPoints() {
        return dpoints;
    }

    public int getGMlevel() {
        return gmLevel;
    }

    public CashShop getCashInventory() {
        return cs;
    }

    public void removeItem(int id, int quantity) {
        MapleInventoryManipulator.removeById(client, GameConstants.getInventoryType(id), id, quantity, true, false);
        client.getSession().write(InfoPacket.getShowItemGain(id, (short) -quantity, true));
    }

    public void removeAll(int id) {
        removeAll(id, true);
    }

    public void removeAll(int id, boolean show) {
        MapleInventoryType type = GameConstants.getInventoryType(id);
        int possessed = getInventory(type).countById(id);

        if (possessed > 0) {
            MapleInventoryManipulator.removeById(getClient(), type, id, possessed, true, false);
            if (show) {
                getClient().getSession().write(InfoPacket.getShowItemGain(id, (short) -possessed, true));
            }
        }
        /*
         * if (type == MapleInventoryType.EQUIP) { //check equipped type =
         * MapleInventoryType.EQUIPPED; possessed =
         * getInventory(type).countById(id);
         *
         * if (possessed > 0) {
         * MapleInventoryManipulator.removeById(getClient(), type, id,
         * possessed, true, false);
         * getClient().getSession().write(CField.getShowItemGain(id,
         * (short)-possessed, true)); } }
         */
    }

    public Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> getRings(boolean equip) {
        MapleInventory iv = getInventory(MapleInventoryType.EQUIPPED);
        List<Item> equipped = iv.newList();
        Collections.sort(equipped);
        List<MapleRing> crings = new ArrayList<>(), frings = new ArrayList<>(), mrings = new ArrayList<>();
        MapleRing ring;
        for (Item ite : equipped) {
            Equip item = (Equip) ite;
            if (item.getRing() != null) {
                ring = item.getRing();
                ring.setEquipped(true);
                if (ItemConstants.類型.特效戒指(item.getItemId())) {
                    if (equip) {
                        if (ItemConstants.類型.戀人戒指(item.getItemId())) {
                            crings.add(ring);
                        } else if (ItemConstants.類型.友誼戒指(item.getItemId())) {
                            frings.add(ring);
                        } else if (ItemConstants.類型.結婚戒指(item.getItemId())) {
                            mrings.add(ring);
                        }
                    } else {
                        if (crings.isEmpty() && ItemConstants.類型.戀人戒指(item.getItemId())) {
                            crings.add(ring);
                        } else if (frings.isEmpty() && ItemConstants.類型.友誼戒指(item.getItemId())) {
                            frings.add(ring);
                        } else if (mrings.isEmpty() && ItemConstants.類型.結婚戒指(item.getItemId())) {
                            mrings.add(ring);
                        } //for 3rd person the actual slot doesnt matter, so we'll use this to have both shirt/ring same?
                        //however there seems to be something else behind this, will have to sniff someone with shirt and ring, or more conveniently 3-4 of those
                    }
                }
            }
        }
        if (equip) {
            iv = getInventory(MapleInventoryType.EQUIP);
            for (Item ite : iv.list()) {
                Equip item = (Equip) ite;
                if (item.getRing() != null && ItemConstants.類型.戀人戒指(item.getItemId())) {
                    ring = item.getRing();
                    ring.setEquipped(false);
                    if (ItemConstants.類型.友誼戒指(item.getItemId())) {
                        frings.add(ring);
                    } else if (ItemConstants.類型.戀人戒指(item.getItemId())) {
                        crings.add(ring);
                    } else if (ItemConstants.類型.結婚戒指(item.getItemId())) {
                        mrings.add(ring);
                    }
                }
            }
        }
        Collections.sort(frings, new MapleRing.RingComparator());
        Collections.sort(crings, new MapleRing.RingComparator());
        Collections.sort(mrings, new MapleRing.RingComparator());
        return new Triple<>(crings, frings, mrings);
    }

    public int getFH() {
        MapleFoothold fh = getMap().getFootholds().findBelow(getTruePosition(), true);
        if (fh != null) {
            return fh.getId();
        }
        return 0;
    }

    public void startFairySchedule(boolean exp) {
        startFairySchedule(exp, false);
    }

    public void startFairySchedule(boolean exp, boolean equipped) {
        cancelFairySchedule(exp || stats.equippedFairy == 0);
        if (fairyExp <= 0) {
            fairyExp = (byte) stats.equippedFairy;
        }
        if (equipped && fairyExp < stats.equippedFairy * 3 && stats.equippedFairy > 0) {
            dropMessage(5, "The Fairy Pendant's experience points will increase to " + (fairyExp + stats.equippedFairy) + "% after one hour.");
        }
        lastFairyTime = System.currentTimeMillis();
    }

    public final boolean canFairy(long now) {
        return lastFairyTime > 0 && lastFairyTime + (60 * 60 * 1000) < now;
    }

    public final boolean canHP(long now) {
        if (lastHPTime + 5000 < now) {
            lastHPTime = now;
            return true;
        }
        return false;
    }

    public final boolean canMP(long now) {
        if (lastMPTime + 5000 < now) {
            lastMPTime = now;
            return true;
        }
        return false;
    }

    public final boolean canHPRecover(long now) {
        if (stats.hpRecoverTime > 0 && lastHPTime + stats.hpRecoverTime < now) {
            lastHPTime = now;
            return true;
        }
        return false;
    }

    public final boolean canMPRecover(long now) {
        if (stats.mpRecoverTime > 0 && lastMPTime + stats.mpRecoverTime < now) {
            lastMPTime = now;
            return true;
        }
        return false;
    }

    public void cancelFairySchedule(boolean exp) {
        lastFairyTime = 0;
        if (exp) {
            this.fairyExp = 0;
        }
    }

    public void doFairy() {
        if (fairyExp < stats.equippedFairy * 3 && stats.equippedFairy > 0) {
            fairyExp += stats.equippedFairy;
            dropMessage(5, "The Fairy Pendant's EXP was boosted to " + fairyExp + "%.");
        }
        if (getGuildId() > 0) {
            World.Guild.gainGP(getGuildId(), 20, id);
            client.getSession().write(InfoPacket.getGPContribution(20));
        }
        traits.get(MapleTraitType.will).addExp(5, this); //willpower every hour
        startFairySchedule(false, true);
    }

    public byte getFairyExp() {
        return fairyExp;
    }

    public int getTeam() {
        return coconutteam;
    }

    public void setTeam(int v) {
        this.coconutteam = v;
    }

    public void spawnPet(byte slot) {
        spawnPet(slot, false, true);
    }

    public void spawnPet(byte slot, boolean lead) {
        spawnPet(slot, lead, true);
    }

    public void spawnPet(byte slot, boolean lead, boolean broadcast) {
        final Item item = getInventory(MapleInventoryType.CASH).getItem(slot);
        if (item == null || item.getItemId() >= 5010000 || item.getItemId() < 5000000) {
            return;
        }
        switch (item.getItemId()) {
            case 5000047:
            case 5000028: {
                final MaplePet pet = MaplePet.createPet(item.getItemId() + 1);
                if (pet != null) {
                    MapleInventoryManipulator.addById(client, item.getItemId() + 1, (short) 1, item.getOwner(), pet, 45, MapleInventoryManipulator.DAY, "從" + item.toString() + "寵物進化, 時間:" + FileoutputUtil.CurrentReadable_Date());
                    MapleInventoryManipulator.removeFromSlot(client, MapleInventoryType.CASH, slot, (short) 1, false);
                }
                break;
            }
            default: {
                final MaplePet pet = item.getPet();
                if (pet != null && (item.getItemId() != 5000054 || pet.getSecondsLeft() > 0) && (item.getExpiration() == -1 || item.getExpiration() > System.currentTimeMillis())) {
                    if (pet.getSummoned()) { // Already summoned, let's keep it
                        unequipPet(pet, true, false);
                    } else {
                        int leadid = 8;
                        if (MapleJob.is皇家騎士團(getJob())) {
                            leadid = 10000018;
                        } else if (MapleJob.is狂狼勇士(getJob())) {
                            leadid = 20000024;
                        } else if (MapleJob.is龍魔導士(getJob())) {
                            leadid = 20011024;
                        } else if (MapleJob.is精靈遊俠(getJob())) {
                            leadid = 20021024;
                        } else if (MapleJob.is幻影俠盜(getJob())) {
                            leadid = 20031024;
                        } else if (MapleJob.is惡魔殺手(getJob())) {
                            leadid = 30011024;
                        } else if (MapleJob.is末日反抗軍(getJob())) {
                            leadid = 30001024;
                        } else if (MapleJob.is米哈逸(getJob())) {
                            leadid = 50001018;
                            //} else if (GameConstants.isCannon(getJob())) {
                            //    leadid = 10008;
                        }
                        if (getSkillLevel(SkillFactory.getSkill(leadid)) == 0 && getPet(0) != null) {
                            unequipPet(getPet(0), false, false);
                        } else if (lead && getSkillLevel(SkillFactory.getSkill(leadid)) > 0) { // Follow the Lead
                            //			    shiftPetsRight();
                        }
                        final Point pos = getPosition();
                        pet.setPos(pos);
                        try {
                            pet.setFh(getMap().getFootholds().findBelow(pos, true).getId());
                        } catch (NullPointerException e) {
                            pet.setFh(0); //lol, it can be fixed by movement
                        }
                        pet.setStance(0);
                        pet.setSummoned(1); //let summoned be true..
                        addPet(pet);
                        pet.setSummoned(getPetIndex(pet) + 1); //then get the index
                        if (broadcast && getMap() != null) {
                            client.getSession().write(PetPacket.updatePet(pet, getInventory(MapleInventoryType.CASH).getItem((short) (byte) pet.getInventoryPosition()), false));
                            getMap().broadcastMessage(this, PetPacket.showPet(this, pet, false, false), true);
                            client.getSession().write(PetPacket.showPetUpdate(this, pet.getUniqueId(), (byte) (pet.getSummonedValue() - 1)));
//                            client.getSession().write(PetPacket.petStatUpdate(this));
                        }
                    }
                }
                break;
            }
        }
        client.getSession().write(CWvsContext.enableActions());
    }

    public void clearLinkMid() {
        linkMobs.clear();
        cancelEffectFromBuffStat(MapleBuffStat.HOMING_BEACON);
        cancelEffectFromBuffStat(MapleBuffStat.MANY_USES);
    }

    public int getFirstLinkMid() {
        for (Integer lm : linkMobs.keySet()) {
            return lm;
        }
        return 0;
    }

    public Map<Integer, Integer> getAllLinkMid() {
        return linkMobs;
    }

    public void setLinkMid(int lm, int x) {
        linkMobs.put(lm, x);
    }

    public int getDamageIncrease(int lm) {
        if (linkMobs.containsKey(lm)) {
            return linkMobs.get(lm);
        }
        return 0;
    }

    public boolean isClone() {
        return clone;
    }

    public void setClone(boolean c) {
        this.clone = c;
    }

    public WeakReference<MapleCharacter>[] getClones() {
        return clones;
    }

    public MapleCharacter cloneLooks() {
        MapleClient cloneclient = new MapleClient(null, null, new MockIOSession());

        final int minus = (getId() + Randomizer.nextInt(Integer.MAX_VALUE - getId())); // really randomize it, dont want it to fail

        MapleCharacter ret = new MapleCharacter(true);
        ret.id = minus;
        ret.client = cloneclient;
        ret.exp = 0;
        ret.meso = 0;
        ret.remainingAp = 0;
        ret.fame = 0;
        ret.accountid = client.getAccID();
        ret.anticheat = anticheat;
        ret.name = name;
        ret.level = level;
        ret.fame = fame;
        ret.job = job;
        ret.hair = hair;
        ret.face = face;
        ret.faceMarking = faceMarking;
        ret.ears = ears;
        ret.tail = tail;
        ret.elf = elf;
        ret.skinColor = skinColor;
        ret.monsterbook = monsterbook;
        ret.mount = mount;
        ret.CRand = new PlayerRandomStream();
        ret.gmLevel = gmLevel;
        ret.gender = gender;
        ret.mapid = map.getId();
        ret.map = map;
        ret.setStance(getStance());
        ret.chair = chair;
        ret.itemEffect = itemEffect;
        ret.guildid = guildid;
        ret.currentrep = currentrep;
        ret.totalrep = totalrep;
        ret.stats = stats;
        ret.effects.putAll(effects);
        ret.stack_effects.addAll(stack_effects);
        ret.dispelSummons();
        ret.guildrank = guildrank;
        ret.guildContribution = guildContribution;
        ret.allianceRank = allianceRank;
        ret.setPosition(getTruePosition());
        for (Item equip : getInventory(MapleInventoryType.EQUIPPED).newList()) {
            ret.getInventory(MapleInventoryType.EQUIPPED).addFromDB(equip.copy());
        }
        ret.skillMacros = skillMacros;
        ret.keylayout = keylayout;
        ret.questinfo = questinfo;
        ret.savedLocations = savedLocations;
        ret.wishlist = wishlist;
        ret.buddylist = buddylist;
        ret.keydown_skill = 0;
        ret.lastmonthfameids = lastmonthfameids;
        ret.lastfametime = lastfametime;
        ret.storage = storage;
        ret.cs = this.cs;
        ret.client.setAccountName(client.getAccountName());
        ret.nxcredit = nxcredit;
        ret.acash = acash;
        ret.maplepoints = maplepoints;
        ret.clone = true;
        ret.client.setChannel(this.client.getChannel());
        while (map.getCharacterById(ret.id) != null || client.getChannelServer().getPlayerStorage().getCharacterById(ret.id) != null) {
            ret.id++;
        }
        ret.client.setPlayer(ret);
        return ret;
    }

    public final void cloneLook() {
        if (clone || inPVP()) {
            return;
        }
        for (int i = 0; i < clones.length; i++) {
            if (clones[i].get() == null) {
                final MapleCharacter newp = cloneLooks();
                map.addPlayer(newp);
                map.broadcastMessage(CField.updateCharLook(newp, false));
                map.movePlayer(newp, getTruePosition());
                clones[i] = new WeakReference<>(newp);
                return;
            }
        }
    }

    public final void disposeClones() {
        numClones = 0;
        for (int i = 0; i < clones.length; i++) {
            if (clones[i].get() != null) {
                map.removePlayer(clones[i].get());
                if (clones[i].get().getClient() != null) {
                    clones[i].get().getClient().setPlayer(null);
                    clones[i].get().client = null;
                }
                clones[i] = new WeakReference<>(null);
                numClones++;
            }
        }
    }

    public final int getCloneSize() {
        int z = 0;
        for (int i = 0; i < clones.length; i++) {
            if (clones[i].get() != null) {
                z++;
            }
        }
        return z;
    }

    public void spawnClones() {
        if (!isGM()) { //removed tetris piece likely, expired or whatever
            numClones = (byte) (stats.hasClone ? 1 : 0);
        }
        for (int i = 0; i < numClones; i++) {
            cloneLook();
        }
        numClones = 0;
    }

    public byte getNumClones() {
        return numClones;
    }

    public void setDragon(MapleDragon d) {
        this.dragon = d;
    }

    public void setHaku(MapleHaku h) {
        this.haku = h;
    }

    public MapleExtractor getExtractor() {
        return extractor;
    }

    public void setExtractor(MapleExtractor me) {
        removeExtractor();
        this.extractor = me;
    }

    public void removeExtractor() {
        if (extractor != null) {
            map.broadcastMessage(CField.removeExtractor(this.id));
            map.removeMapObject(extractor);
            extractor = null;
        }
    }

    public final void spawnSavedPets() {
        for (int i = 0; i < petStore.length; i++) {
            if (petStore[i] > -1) {
                spawnPet(petStore[i], false, false);
            }
        }
//        client.getSession().write(PetPacket.petStatUpdate(this));
        petStore = new byte[]{-1, -1, -1};
    }

    public final byte[] getPetStores() {
        return petStore;
    }

    public void resetStats(final int str, final int dex, final int int_, final int luk) {
        Map<MapleStat, Long> stat = new EnumMap<>(MapleStat.class);
        int total = stats.getStr() + stats.getDex() + stats.getLuk() + stats.getInt() + getRemainingAp();

        total -= str;
        stats.str = (int) str;

        total -= dex;
        stats.dex = (int) dex;

        total -= int_;
        stats.int_ = (int) int_;

        total -= luk;
        stats.luk = (int) luk;

        setRemainingAp((int) total);
        stats.recalcLocalStats(this);
        stat.put(MapleStat.STR, (long) str);
        stat.put(MapleStat.DEX, (long) dex);
        stat.put(MapleStat.INT, (long) int_);
        stat.put(MapleStat.LUK, (long) luk);
        stat.put(MapleStat.AVAILABLEAP, (long) total);
        client.getSession().write(CWvsContext.updatePlayerStats(stat, false, this));
        client.getSession().write(EffectPacket.TutInstructionalBalloon("Effect/OnUserEff.img/RecordClear_BT/clear"));
    }

    public Event_PyramidSubway getPyramidSubway() {
        return pyramidSubway;
    }

    public void setPyramidSubway(Event_PyramidSubway ps) {
        this.pyramidSubway = ps;
    }

    public byte getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(int z) {
        this.subcategory = (byte) z;
    }

    public int itemQuantity(final int itemid) {
        return getInventory(GameConstants.getInventoryType(itemid)).countById(itemid);
    }

    public void setRPS(RockPaperScissors rps) {
        this.rps = rps;
    }

    public RockPaperScissors getRPS() {
        return rps;
    }

    public long getNextConsume() {
        return nextConsume;
    }

    public void setNextConsume(long nc) {
        this.nextConsume = nc;
    }

    public int getRank() {
        return rank;
    }

    public int getRankMove() {
        return rankMove;
    }

    public int getJobRank() {
        return jobRank;
    }

    public int getJobRankMove() {
        return jobRankMove;
    }

    public void getAllModes() {
        if (MapleJob.is幻獸師(job)) {
            final Map<Skill, SkillEntry> ss = new HashMap<>();
            ss.put(SkillFactory.getSkill(110001501), new SkillEntry((byte) 1, (byte) 1, -1));
            ss.put(SkillFactory.getSkill(110001502), new SkillEntry((byte) 1, (byte) 1, -1));
            ss.put(SkillFactory.getSkill(110001503), new SkillEntry((byte) 1, (byte) 1, -1));
            ss.put(SkillFactory.getSkill(110001504), new SkillEntry((byte) 1, (byte) 1, -1));
            changeSkillsLevel(ss);
            NPCScriptManager.getInstance().dispose(client);
            System.out.println("All modes are added");
        } else {
            dropMessage(6, "You are not beast tamer!");
        }
    }

    public void changeChannel(final int channel) {
        final ChannelServer toch = ChannelServer.getInstance(channel);

        if (channel == client.getChannel() || toch == null || toch.isShutdown()) {
            client.getSession().write(CField.serverBlocked(1));
            return;
        }
        changeRemoval();

        final ChannelServer ch = ChannelServer.getInstance(client.getChannel());
        if (getMessenger() != null) {
            World.Messenger.silentLeaveMessenger(getMessenger().getId(), new MapleMessengerCharacter(this));
        }
        PlayerBuffStorage.addBuffsToStorage(getId(), getAllBuffs());
        PlayerBuffStorage.addStackBuffsToStorage(getId(), stack_effects);
        PlayerBuffStorage.addCooldownsToStorage(getId(), getCooldowns());
        PlayerBuffStorage.addDiseaseToStorage(getId(), getAllDiseases());
        World.ChannelChange_Data(new CharacterTransfer(this), getId(), channel);
        ch.removePlayer(this);
        client.updateLoginState(MapleClient.CHANGE_CHANNEL, client.getSessionIPAddress());
        final String s = client.getSessionIPAddress();
        LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
        client.getSession().write(CField.getChannelChange(client, Integer.parseInt(toch.getIP().split(":")[1])));
        saveToDB(false, false);
        getMap().removePlayer(this);

        client.setPlayer(null);
        client.setReceiving(false);
    }

    public void forceChangeChannel(final int channel) {
        final ChannelServer toch = ChannelServer.getInstance(channel);

        changeRemoval();

        final ChannelServer ch = ChannelServer.getInstance(client.getChannel());
        if (getMessenger() != null) {
            World.Messenger.silentLeaveMessenger(getMessenger().getId(), new MapleMessengerCharacter(this));
        }
        PlayerBuffStorage.addBuffsToStorage(getId(), getAllBuffs());
        PlayerBuffStorage.addStackBuffsToStorage(getId(), stack_effects);
        PlayerBuffStorage.addCooldownsToStorage(getId(), getCooldowns());
        PlayerBuffStorage.addDiseaseToStorage(getId(), getAllDiseases());
        World.ChannelChange_Data(new CharacterTransfer(this), getId(), channel);
        ch.removePlayer(this);
        client.updateLoginState(MapleClient.CHANGE_CHANNEL, client.getSessionIPAddress());
        final String s = client.getSessionIPAddress();
        LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
        client.getSession().write(CField.getChannelChange(client, Integer.parseInt(toch.getIP().split(":")[1])));
        saveToDB(false, false);
        getMap().removePlayer(this);

        client.setPlayer(null);
        client.setReceiving(false);
    }

    public void expandInventory(byte type, int amount) {
        final MapleInventory inv = getInventory(MapleInventoryType.getByType(type));
        inv.addSlot((byte) amount);
        client.getSession().write(InventoryPacket.getSlotUpdate(type, (byte) inv.getSlotLimit()));
    }

    public boolean allowedToTarget(MapleCharacter other) {
        return other != null && (!other.isHidden() || getGMLevel() >= other.getGMLevel());
    }

    public int getFollowId() {
        return followid;
    }

    public void setFollowId(int fi) {
        this.followid = fi;
        if (fi == 0) {
            this.followinitiator = false;
            this.followon = false;
        }
    }

    public void setFollowInitiator(boolean fi) {
        this.followinitiator = fi;
    }

    public void setFollowOn(boolean fi) {
        this.followon = fi;
    }

    public boolean isFollowOn() {
        return followon;
    }

    public boolean isFollowInitiator() {
        return followinitiator;
    }

    public void checkFollow() {
        if (followid <= 0) {
            return;
        }
        if (followon) {
            map.broadcastMessage(CField.followEffect(id, 0, null));
            map.broadcastMessage(CField.followEffect(followid, 0, null));
        }
        MapleCharacter tt = map.getCharacterById(followid);
        client.getSession().write(CField.getFollowMessage("跟隨解除。"));
        if (tt != null) {
            tt.setFollowId(0);
            tt.getClient().getSession().write(CField.getFollowMessage("跟隨解除。"));
        }
        setFollowId(0);
    }

    public int getMarriageId() {
        return marriageId;
    }

    public void setMarriageId(final int mi) {
        this.marriageId = mi;
    }

    public int getMarriageItemId() {
        return marriageItemId;
    }

    public void setMarriageItemId(final int mi) {
        this.marriageItemId = mi;
    }

    public MapleMarriage getMarriage() {
        return marriage;
    }

    public void setMarriage(MapleMarriage marriage) {
        this.marriage = marriage;
    }

    public boolean isStaff() {
        return this.gmLevel >= 1;
    }

    public boolean startPartyQuest(final int questid) {
        boolean ret = false;
        MapleQuest q = MapleQuest.getInstance(questid);
        if (q == null || !q.isPartyQuest()) {
            return false;
        }
        if (!quests.containsKey(q) || !questinfo.containsKey(questid)) {
            final MapleQuestStatus status = getQuestNAdd(q);
            status.setStatus((byte) 1);
            updateQuest(status);
            switch (questid) {
                case 1300:
                case 1301:
                case 1302: //carnival, ariants.
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have=0;rank=F;try=0;cmp=0;CR=0;VR=0;gvup=0;vic=0;lose=0;draw=0");
                    break;
                case 1303: //ghost pq
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have=0;have1=0;rank=F;try=0;cmp=0;CR=0;VR=0;vic=0;lose=0");
                    break;
                case 1204: //herb town pq
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have0=0;have1=0;have2=0;have3=0;rank=F;try=0;cmp=0;CR=0;VR=0");
                    break;
                case 1206: //ellin pq
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have0=0;have1=0;rank=F;try=0;cmp=0;CR=0;VR=0");
                    break;
                default:
                    updateInfoQuest(questid, "min=0;sec=0;date=0000-00-00;have=0;rank=F;try=0;cmp=0;CR=0;VR=0");
                    break;
            }
            ret = true;
        } //started the quest.
        return ret;
    }

    public String getOneInfo(final int questid, final String key) {
        if (!questinfo.containsKey(questid) || key == null || MapleQuest.getInstance(questid) == null/* || !MapleQuest.getInstance(questid).isPartyQuest()*/) {
            return null;
        }
        final String[] split = questinfo.get(questid).split(";");
        for (String x : split) {
            final String[] split2 = x.split("="); //should be only 2
            if (split2.length == 2 && split2[0].equals(key)) {
                return split2[1];
            }
        }
        return null;
    }

    public void updateOneInfo(final int questid, final String key, final String value) {
        if (key == null || value == null || MapleQuest.getInstance(questid) == null/* || !MapleQuest.getInstance(questid).isPartyQuest()*/) {
            return;
        }
        if (!questinfo.containsKey(questid)) {
            questinfo.put(questid, key + "=" + value);
            updateInfoQuest(questid, key + "=" + value);
            return;
        }
        final String[] split = questinfo.get(questid).split(";");
        boolean changed = false;
        boolean match = false;
        final StringBuilder newQuest = new StringBuilder();
        for (String x : split) {
            final String[] split2 = x.split("="); //should be only 2
            if (split2.length != 2) {
                continue;
            }
            if (split2[0].equals(key)) {
                newQuest.append(key).append("=").append(value);
                match = true;
            } else {
                newQuest.append(x);
            }
            newQuest.append(";");
            changed = true;
        }
        if (!match) {
            newQuest.append(key).append("=").append(value).append(";");
        }

        updateInfoQuest(questid, changed ? newQuest.toString().substring(0, newQuest.toString().length() - 1) : newQuest.toString());
    }

    public void recalcPartyQuestRank(final int questid) {
        if (MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        if (!startPartyQuest(questid)) {
            final String oldRank = getOneInfo(questid, "rank");
            if (oldRank == null || oldRank.equals("S")) {
                return;
            }
            String newRank;
            switch (oldRank) {
                case "A":
                    newRank = "S";
                    break;
                case "B":
                    newRank = "A";
                    break;
                case "C":
                    newRank = "B";
                    break;
                case "D":
                    newRank = "C";
                    break;
                case "F":
                    newRank = "D";
                    break;
                default:
                    return;
            }
            final List<Pair<String, Pair<String, Integer>>> questInfo = MapleQuest.getInstance(questid).getInfoByRank(newRank);
            if (questInfo == null) {
                return;
            }
            for (Pair<String, Pair<String, Integer>> q : questInfo) {
                boolean found = false;
                final String val = getOneInfo(questid, q.right.left);
                if (val == null) {
                    return;
                }
                int vall;
                try {
                    vall = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    return;
                }
                switch (q.left) {
                    case "less":
                        found = vall < q.right.right;
                        break;
                    case "more":
                        found = vall > q.right.right;
                        break;
                    case "equal":
                        found = vall == q.right.right;
                        break;
                }
                if (!found) {
                    return;
                }
            }
            //perfectly safe
            updateOneInfo(questid, "rank", newRank);
        }
    }

    public void tryPartyQuest(final int questid) {
        if (MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        try {
            startPartyQuest(questid);
            pqStartTime = System.currentTimeMillis();
            updateOneInfo(questid, "try", String.valueOf(Integer.parseInt(getOneInfo(questid, "try")) + 1));
        } catch (NumberFormatException e) {
            System.err.println("tryPartyQuest error");
        }
    }

    public void endPartyQuest(final int questid) {
        if (MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        try {
            startPartyQuest(questid);
            if (pqStartTime > 0) {
                final long changeTime = System.currentTimeMillis() - pqStartTime;
                final int mins = (int) (changeTime / 1000 / 60), secs = (int) (changeTime / 1000 % 60);
                final int mins2 = Integer.parseInt(getOneInfo(questid, "min"));
                if (mins2 <= 0 || mins < mins2) {
                    updateOneInfo(questid, "min", String.valueOf(mins));
                    updateOneInfo(questid, "sec", String.valueOf(secs));
                    updateOneInfo(questid, "date", FileoutputUtil.CurrentReadable_Date());
                }
                final int newCmp = Integer.parseInt(getOneInfo(questid, "cmp")) + 1;
                updateOneInfo(questid, "cmp", String.valueOf(newCmp));
                updateOneInfo(questid, "CR", String.valueOf((int) Math.ceil((newCmp * 100.0) / Integer.parseInt(getOneInfo(questid, "try")))));
                recalcPartyQuestRank(questid);
                pqStartTime = 0;
            }
        } catch (Exception e) {
            System.err.println("endPartyQuest error");
        }

    }

    public void havePartyQuest(final int itemId) {
        int questid, index = -1;
        switch (itemId) {
            case 1002798:
                questid = 1200; //henesys
                break;
            case 1072369:
                questid = 1201; //kerning
                break;
            case 1022073:
                questid = 1202; //ludi
                break;
            case 1082232:
                questid = 1203; //orbis
                break;
            case 1002571:
            case 1002572:
            case 1002573:
            case 1002574:
                questid = 1204; //herbtown
                index = itemId - 1002571;
                break;
            case 1102226:
                questid = 1303; //ghost
                break;
            case 1102227:
                questid = 1303; //ghost
                index = 0;
                break;
            case 1122010:
                questid = 1205; //magatia
                break;
            case 1032061:
            case 1032060:
                questid = 1206; //ellin
                index = itemId - 1032060;
                break;
            case 3010018:
                questid = 1300; //ariant
                break;
            case 1122007:
                questid = 1301; //carnival
                break;
            case 1122058:
                questid = 1302; //carnival2
                break;
            default:
                return;
        }
        if (MapleQuest.getInstance(questid) == null || !MapleQuest.getInstance(questid).isPartyQuest()) {
            return;
        }
        startPartyQuest(questid);
        updateOneInfo(questid, "have" + (index == -1 ? "" : index), "1");
    }

    public boolean hasSummon() {
        return hasSummon;
    }

    public void setHasSummon(boolean summ) {
        this.hasSummon = summ;
    }

    public void removeDoor() {
        final MapleDoor door = getDoors().iterator().next();
        for (final MapleCharacter chr : door.getTarget().getCharactersThreadsafe()) {
            door.sendDestroyData(chr.getClient());
        }
        for (final MapleCharacter chr : door.getTown().getCharactersThreadsafe()) {
            door.sendDestroyData(chr.getClient());
        }
        for (final MapleDoor destroyDoor : getDoors()) {
            door.getTarget().removeMapObject(destroyDoor);
            door.getTown().removeMapObject(destroyDoor);
        }
        clearDoors();
    }

    public void removeMechDoor() {
        for (final MechDoor destroyDoor : getMechDoors()) {
            for (final MapleCharacter chr : getMap().getCharactersThreadsafe()) {
                destroyDoor.sendDestroyData(chr.getClient());
            }
            getMap().removeMapObject(destroyDoor);
        }
        clearMechDoors();
    }

    public void changeRemoval() {
        changeRemoval(false);
    }

    public void changeRemoval(boolean dc) {
        if (getCheatTracker() != null && dc) {
            getCheatTracker().dispose();
        }
        removeFamiliar();
        dispelSummons();
        if (!dc) {
            cancelEffectFromBuffStat(MapleBuffStat.SOARING);
            cancelEffectFromBuffStat(MapleBuffStat.MONSTER_RIDING);
            cancelEffectFromBuffStat(MapleBuffStat.MECH_CHANGE);
            cancelEffectFromBuffStat(MapleBuffStat.RECOVERY);
        }
        if (getPyramidSubway() != null) {
            getPyramidSubway().dispose(this);
        }
        if (playerShop != null && !dc) {
            playerShop.removeVisitor(this);
            if (playerShop.isOwner(this)) {
                playerShop.setOpen(true);
            }
        }
        if (!getDoors().isEmpty()) {
            removeDoor();
        }
        if (!getMechDoors().isEmpty()) {
            removeMechDoor();
        }
        disposeClones();
        NPCScriptManager.getInstance().dispose(client);
        cancelFairySchedule(false);
    }

    public void updateTick(int newTick) {
        anticheat.updateTick(newTick);
    }

    public boolean canUseFamilyBuff(MapleFamilyBuff buff) {
        final MapleQuestStatus stat = getQuestNoAdd(MapleQuest.getInstance(buff.questID));
        if (stat == null) {
            return true;
        }
        if (stat.getCustomData() == null) {
            stat.setCustomData("0");
        }
        return Long.parseLong(stat.getCustomData()) + (24 * 3600000) < System.currentTimeMillis();
    }

    public void useFamilyBuff(MapleFamilyBuff buff) {
        final MapleQuestStatus stat = getQuestNAdd(MapleQuest.getInstance(buff.questID));
        stat.setCustomData(String.valueOf(System.currentTimeMillis()));
    }

    public List<Integer> usedBuffs() {
        //assume count = 1
        List<Integer> used = new ArrayList<>();
        MapleFamilyBuff[] z = MapleFamilyBuff.values();
        for (int i = 0; i < z.length; i++) {
            if (!canUseFamilyBuff(z[i])) {
                used.add(i);
            }
        }
        return used;
    }

    public String getTeleportName() {
        return teleportname;
    }

    public void setTeleportName(final String tname) {
        teleportname = tname;
    }

    public int getNoJuniors() {
        if (mfc == null) {
            return 0;
        }
        return mfc.getNoJuniors();
    }

    public MapleFamilyCharacter getMFC() {
        return mfc;
    }

    public void makeMFC(final int familyid, final int seniorid, final int junior1, final int junior2) {
        if (familyid > 0) {
            MapleFamily f = World.Family.getFamily(familyid);
            if (f == null) {
                mfc = null;
            } else {
                mfc = f.getMFC(id);
                if (mfc == null) {
                    mfc = f.addFamilyMemberInfo(this, seniorid, junior1, junior2);
                }
                if (mfc.getSeniorId() != seniorid) {
                    mfc.setSeniorId(seniorid);
                }
                if (mfc.getJunior1() != junior1) {
                    mfc.setJunior1(junior1);
                }
                if (mfc.getJunior2() != junior2) {
                    mfc.setJunior2(junior2);
                }
            }
        } else {
            mfc = null;
        }
    }

    public void setFamily(final int newf, final int news, final int newj1, final int newj2) {
        if (mfc == null || newf != mfc.getFamilyId() || news != mfc.getSeniorId() || newj1 != mfc.getJunior1() || newj2 != mfc.getJunior2()) {
            makeMFC(newf, news, newj1, newj2);
        }
    }

    public int maxBattleshipHP(int skillid) {
        return (getTotalSkillLevel(skillid) * 5000) + ((getLevel() - 120) * 3000);
    }

    public int currentBattleshipHP() {
        return battleshipHP;
    }

    public void setBattleshipHP(int v) {
        this.battleshipHP = v;
    }

    public void decreaseBattleshipHP() {
        this.battleshipHP--;
    }

    public int getGachExp() {
        return gachexp;
    }

    public void setGachExp(int ge) {
        this.gachexp = ge;
    }

    public boolean isInBlockedMap() {
        if (!isAlive() || getPyramidSubway() != null || getMap().getSquadByMap() != null || getEventInstance() != null || getMap().getEMByMap() != null) {
            return true;
        }
        if ((getMapId() >= 680000210 && getMapId() <= 680000502) || (getMapId() / 10000 == 92502 && getMapId() >= 925020100) || (getMapId() / 10000 == 92503) || getMapId() == GameConstants.JAIL) {
            return true;
        }
        for (int i : GameConstants.blockedMaps) {
            if (getMapId() == i) {
                return true;
            }
        }
        if (getMapId() >= 689010000 && getMapId() < 689014000) { //Pink Zakum
            return true;
        }
        return false;
    }

    public boolean isInTownMap() {
        if (hasBlockedInventory() || !getMap().isTown() || FieldLimitType.VipRock.check(getMap().getFieldLimit()) || getEventInstance() != null) {
            return false;
        }
        for (int i : GameConstants.blockedMaps) {
            if (getMapId() == i) {
                return false;
            }
        }
        return true;
    }

    public boolean hasBlockedInventory() {
        boolean hasBlockedInventory = !isAlive() || getTrade() != null || getConversation() > 0 || getDirection() >= 0 || getPlayerShop() != null || map == null;
        if (hasBlockedInventory) {
            System.err.println("hasBlockedInventory:!isAlive() - " + !isAlive() + " getTrade() != null - " + (getTrade() != null) + " getConversation() > 0 - " + (getConversation() > 0) + " getDirection() >= 0 - " + (getDirection() >= 0) + " getPlayerShop() != null - " + (getPlayerShop() != null) + " map == null - " + (map == null));
        }
        return hasBlockedInventory;
    }

    public void startPartySearch(final List<Integer> jobs, final int maxLevel, final int minLevel, final int membersNeeded) {
        for (MapleCharacter chr : map.getCharacters()) {
            if (chr.getId() != id && chr.getParty() == null && chr.getLevel() >= minLevel && chr.getLevel() <= maxLevel && (jobs.isEmpty() || jobs.contains((int) chr.getJob())) && (isGM() || !chr.isGM())) {
                if (party != null && party.getMembers().size() < 6 && party.getMembers().size() < membersNeeded) {
                    chr.setParty(party);
                    World.Party.updateParty(party.getId(), PartyOperation.JOIN, new MaplePartyCharacter(chr));
                    chr.receivePartyMemberHP();
                    chr.updatePartyMemberHP();
                } else {
                    break;
                }
            }
        }
    }

    public int getChallenge() {
        return challenge;
    }

    public void setChallenge(int c) {
        this.challenge = c;
    }

    public short getFatigue() {
        return fatigue;
    }

    public void setFatigue(int j) {
        this.fatigue = (short) Math.max(0, j);
        updateSingleStat(MapleStat.FATIGUE, this.fatigue);
    }

    public void fakeRelog() {
        client.getSession().write(CField.getCharInfo(this));
        final MapleMap mapp = getMap();
        mapp.setCheckStates(false);
        mapp.removePlayer(this);
        mapp.addPlayer(this);
        mapp.setCheckStates(true);

        client.getSession().write(CWvsContext.getFamiliarInfo(this));
    }

    public boolean canSummon() {
        return canSummon(5000);
    }

    public boolean canSummon(int g) {
        if (lastSummonTime + g < System.currentTimeMillis()) {
            lastSummonTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public int getIntNoRecord(int questID) {
        final MapleQuestStatus stat = getQuestNoAdd(MapleQuest.getInstance(questID));
        if (stat == null || stat.getCustomData() == null) {
            return 0;
        }
        return Integer.parseInt(stat.getCustomData());
    }

    public long getLongNoRecord(int questID) {
        final MapleQuestStatus stat = getQuestNoAdd(MapleQuest.getInstance(questID));
        if (stat == null || stat.getCustomData() == null) {
            return 0;
        }
        return Long.parseLong(stat.getCustomData());
    }

    public int getIntRecord(int questID) {
        final MapleQuestStatus stat = getQuestNAdd(MapleQuest.getInstance(questID));
        if (stat.getCustomData() == null) {
            stat.setCustomData("0");
        }
        return Integer.parseInt(stat.getCustomData());
    }

    public long getLongRecord(int questID) {
        final MapleQuestStatus stat = getQuestNAdd(MapleQuest.getInstance(questID));
        if (stat.getCustomData() == null) {
            stat.setCustomData("0");
        }
        return Long.parseLong(stat.getCustomData());
    }

    public void updatePetAuto() {
        if (getIntNoRecord(GameConstants.HP_ITEM) > 0) {
            client.getSession().write(CField.petAutoHP(getIntRecord(GameConstants.HP_ITEM)));
        }
        if (getIntNoRecord(GameConstants.MP_ITEM) > 0) {
            client.getSession().write(CField.petAutoMP(getIntRecord(GameConstants.MP_ITEM)));
        }
        if (getIntNoRecord(GameConstants.BUFF_ITEM) > 0) {
            client.getSession().write(CField.petAutoBuff(getIntRecord(GameConstants.BUFF_ITEM)));
        }
    }

    public void sendEnglishQuiz(String msg) {
        client.getSession().write(NPCPacket.getEnglishQuiz(9010000, (byte) 0, 9010000, msg, "00 00"));
    }

    public void setChangeTime() {
        mapChangeTime = System.currentTimeMillis();
    }

    public long getChangeTime() {
        return mapChangeTime;
    }

    public Map<ReportType, Integer> getReports() {
        return reports;
    }

    public void addReport(ReportType type) {
        Integer value = reports.get(type);
        reports.put(type, value == null ? 1 : (value + 1));
        changed_reports = true;
    }

    public void clearReports(ReportType type) {
        reports.remove(type);
        changed_reports = true;
    }

    public void clearReports() {
        reports.clear();
        changed_reports = true;
    }

    public final int getReportPoints() {
        int ret = 0;
        for (Integer entry : reports.values()) {
            ret += entry;
        }
        return ret;
    }

    public final String getReportSummary() {
        final StringBuilder ret = new StringBuilder();
        final List<Pair<ReportType, Integer>> offenseList = new ArrayList<>();
        for (final Entry<ReportType, Integer> entry : reports.entrySet()) {
            offenseList.add(new Pair<>(entry.getKey(), entry.getValue()));
        }
        Collections.sort(offenseList, new Comparator<Pair<ReportType, Integer>>() {
            @Override
            public final int compare(final Pair<ReportType, Integer> o1, final Pair<ReportType, Integer> o2) {
                final int thisVal = o1.getRight();
                final int anotherVal = o2.getRight();
                return (thisVal < anotherVal ? 1 : (thisVal == anotherVal ? 0 : -1));
            }
        });
        for (int x = 0; x < offenseList.size(); x++) {
            ret.append(StringUtil.makeEnumHumanReadable(offenseList.get(x).left.name()));
            ret.append(": ");
            ret.append(offenseList.get(x).right);
            ret.append(" ");
        }
        return ret.toString();
    }

    public short getScrolledPosition() {
        return scrolledPosition;
    }

    public void setScrolledPosition(short s) {
        this.scrolledPosition = s;
    }

    public MapleTrait getTrait(MapleTraitType t) {
        return traits.get(t);
    }

    public void forceCompleteQuest(int id) {
        MapleQuest.getInstance(id).forceComplete(this, 9270035); //troll
    }

    public List<Integer> getExtendedSlots() {
        return extendedSlots;
    }

    public int getExtendedSlot(int index) {
        if (extendedSlots.size() <= index || index < 0) {
            return -1;
        }
        return extendedSlots.get(index);
    }

    public void changedExtended() {
        changed_extendedSlots = true;
    }

    public MapleAndroid getAndroid() {
        return android;
    }

    public void removeAndroid() {
        if (map != null) {
            map.broadcastMessage(CField.deactivateAndroid(this.id));
        }
        android = null;
    }

    public void setAndroid(MapleAndroid a) {
        this.android = a;
        if ((this.map != null) && (a != null)) {
            this.map.broadcastMessage(CField.spawnAndroid(this, a));
            this.map.broadcastMessage(CField.showAndroidEmotion(getId(), (byte) Randomizer.nextInt(5), (byte) Randomizer.nextInt(1)));
        }
    }

    public void updateAndroid(short position) {
        if (this.map != null) {
            if (position == 0) {
                this.map.broadcastMessage(CField.updateAndroidLook(this, android));
            } else {
                this.map.broadcastMessage(CField.updateAndroidEquip(this, position));
            }
        }
    }

    public Map<Integer, MonsterFamiliar> getFamiliars() {
        return familiars;
    }

    public MonsterFamiliar getSummonedFamiliar() {
        return summonedFamiliar;
    }

    public void removeFamiliar() {
        if (summonedFamiliar != null && map != null) {
            removeVisibleFamiliar();
        }
        summonedFamiliar = null;
    }

    public void removeVisibleFamiliar() {
        getMap().removeMapObject(summonedFamiliar);
        removeVisibleMapObject(summonedFamiliar);
        getMap().broadcastMessage(CField.removeFamiliar(this.getId()));
        anticheat.resetFamiliarAttack();
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        cancelEffect(ii.getItemEffect(ii.getFamiliar(summonedFamiliar.getFamiliar()).passive), false, System.currentTimeMillis());
    }

    public void spawnFamiliar(MonsterFamiliar mf, boolean respawn) {
        summonedFamiliar = mf;

        mf.setStance(0);
        mf.setPosition(getPosition());
        mf.setFh(getFH());
        addVisibleMapObject(mf);
        getMap().spawnFamiliar(mf, respawn);

        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        final MapleStatEffect eff = ii.getItemEffect(ii.getFamiliar(summonedFamiliar.getFamiliar()).passive);
        if (eff != null && eff.getInterval() <= 0 && eff.makeChanceResult()) { //i think this is actually done through a recv, which is ATTACK_FAMILIAR +1
            eff.applyTo(this);
        }
        lastFamiliarEffectTime = System.currentTimeMillis();
    }

    public final boolean canFamiliarEffect(long now, MapleStatEffect eff) {
        return lastFamiliarEffectTime > 0 && lastFamiliarEffectTime + eff.getInterval() < now;
    }

    public void doFamiliarSchedule(long now) {
        if (familiars == null) {
            return;
        }
        for (MonsterFamiliar mf : familiars.values()) {
            if (summonedFamiliar != null && summonedFamiliar.getId() == mf.getId()) {
                mf.addFatigue(this, 5);
                final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                final MapleStatEffect eff = ii.getItemEffect(ii.getFamiliar(summonedFamiliar.getFamiliar()).passive);
                if (eff != null && eff.getInterval() > 0 && canFamiliarEffect(now, eff) && eff.makeChanceResult()) {
                    eff.applyTo(this);
                }
            } else if (mf.getFatigue() > 0) {
                mf.setFatigue(Math.max(0, mf.getFatigue() - 5));
            }
        }
    }

    public MapleImp[] getImps() {
        return imps;
    }

    public void sendImp() {
        for (int i = 0; i < imps.length; i++) {
            if (imps[i] != null) {
                client.getSession().write(CWvsContext.updateImp(imps[i], ImpFlag.SUMMONED.getValue(), i, true));
            }
        }
    }

    public int getBattlePoints() {
        return pvpPoints;
    }

    public int getTotalBattleExp() {
        return pvpExp;
    }

    public void setBattlePoints(int p) {
        if (p != pvpPoints) {
            client.getSession().write(InfoPacket.getBPMsg(p - pvpPoints));
            updateSingleStat(MapleStat.BATTLE_POINTS, p);
        }
        this.pvpPoints = p;
    }

    public void setTotalBattleExp(int p) {
        final int previous = pvpExp;
        this.pvpExp = p;
        if (p != previous) {
            stats.recalcPVPRank(this);

            updateSingleStat(MapleStat.BATTLE_EXP, stats.pvpExp);
            updateSingleStat(MapleStat.BATTLE_RANK, stats.pvpRank);
        }
    }

    public void changeTeam(int newTeam) {
        this.coconutteam = newTeam;

        if (inPVP()) {
            client.getSession().write(CField.getPVPTransform(newTeam + 1));
            map.broadcastMessage(CField.changeTeam(id, newTeam + 1));
        } else {
            client.getSession().write(CField.showEquipEffect(newTeam));
        }
    }

    public void disease(int type, int level) {
        if (MapleDisease.getBySkill(type) == null) {
            return;
        }
        chair = 0;
        client.getSession().write(CField.cancelChair(-1, getId()));
        map.broadcastMessage(this, CField.showChair(id, 0, ""), false);
        giveDebuff(MapleDisease.getBySkill(type), MobSkillFactory.getMobSkill(type, level));
    }

    public boolean inPVP() {
        return eventInstance != null && eventInstance.getName().startsWith("PVP");
    }

    public boolean inAzwan() {
        return mapid >= 262020000 && mapid < 262023000;
    }

    public void clearAllCooldowns() {
        for (MapleCoolDownValueHolder m : getCooldowns()) {
            final int skil = m.skillId;
            removeCooldown(skil);
            client.getSession().write(CField.skillCooldown(skil, 0));
        }
    }

    public Pair<Double, Boolean> modifyDamageTaken(double damage, MapleMapObject attacke) {
        Pair<Double, Boolean> ret = new Pair<>(damage, false);
        if (damage <= 0) {
            return ret;
        }
        if (stats.ignoreDAMr > 0 && Randomizer.nextInt(100) < stats.ignoreDAMr_rate) {
            damage -= Math.floor((stats.ignoreDAMr * damage) / 100.0f);
        }
        if (stats.ignoreDAM > 0 && Randomizer.nextInt(100) < stats.ignoreDAM_rate) {
            damage -= stats.ignoreDAM;
        }
        final Integer div = getBuffedValue(MapleBuffStat.DIVINE_SHIELD);
        final Integer div2 = getBuffedValue(MapleBuffStat.HOLY_MAGIC_SHELL);
        if (div2 != null) {
            if (div2 <= 0) {
                cancelEffectFromBuffStat(MapleBuffStat.HOLY_MAGIC_SHELL);
            } else {
                setBuffedValue(MapleBuffStat.HOLY_MAGIC_SHELL, div2 - 1);
                damage = 0;
            }
        } else if (div != null) {
            if (div <= 0) {
                cancelEffectFromBuffStat(MapleBuffStat.DIVINE_SHIELD);
            } else {
                setBuffedValue(MapleBuffStat.DIVINE_SHIELD, div - 1);
                damage = 0;
            }
        }
        MapleStatEffect barrier = getStatForBuff(MapleBuffStat.COMBO_BARRIER);
        if (barrier != null) {
            damage = ((barrier.getX() / 1000.0) * damage);
        }
        barrier = getStatForBuff(MapleBuffStat.MAGIC_SHIELD);
        if (barrier != null) {
            damage = ((barrier.getX() / 1000.0) * damage);
        }
        barrier = getStatForBuff(MapleBuffStat.WATER_SHIELD);
        if (barrier != null) {
            damage = ((barrier.getX() / 1000.0) * damage);
        }
        List<Integer> attack = attacke instanceof MapleMonster || attacke == null ? null : (new ArrayList<Integer>());
        if (damage > 0) {
            if (getJob() == 122 && !skillisCooling(1220013)) {
                final Skill divine = SkillFactory.getSkill(1220013);
                if (getTotalSkillLevel(divine) > 0) {
                    final MapleStatEffect divineShield = divine.getEffect(getTotalSkillLevel(divine));
                    if (divineShield.makeChanceResult()) {
                        divineShield.applyTo(this);
                        client.getSession().write(CField.skillCooldown(1220013, divineShield.getCooldown(this)));
                        addCooldown(1220013, System.currentTimeMillis(), divineShield.getCooldown(this) * 1000);
                    }
                }
            } else if (getBuffedValue(MapleBuffStat.SATELLITESAFE_PROC) != null && getBuffedValue(MapleBuffStat.SATELLITESAFE_ABSORB) != null && getBuffedValue(MapleBuffStat.PUPPET) != null) {
                double buff = getBuffedValue(MapleBuffStat.SATELLITESAFE_PROC).doubleValue();
                double buffz = getBuffedValue(MapleBuffStat.SATELLITESAFE_ABSORB).doubleValue();
                if ((int) ((buff / 100.0) * getStat().getMaxHp()) <= damage) {
                    damage -= ((buffz / 100.0) * damage);
                    cancelEffectFromBuffStat(MapleBuffStat.PUPPET);
                }
            } else if (getJob() == 433 || getJob() == 434) {
                final Skill divine = SkillFactory.getSkill(4330001);
                if (getTotalSkillLevel(divine) > 0 && getBuffedValue(MapleBuffStat.DARKSIGHT) == null && !skillisCooling(divine.getId())) {
                    final MapleStatEffect divineShield = divine.getEffect(getTotalSkillLevel(divine));
                    if (Randomizer.nextInt(100) < divineShield.getX()) {
                        divineShield.applyTo(this);
                    }
                }
            } else if ((getJob() == 512 || getJob() == 522) && getBuffedValue(MapleBuffStat.DAMAGE_RATE) == null) {
                final Skill divine = SkillFactory.getSkill(getJob() == 512 ? 5120011 : 5220012);
                if (getTotalSkillLevel(divine) > 0 && !skillisCooling(divine.getId())) {
                    final MapleStatEffect divineShield = divine.getEffect(getTotalSkillLevel(divine));
                    if (divineShield.makeChanceResult()) {
                        divineShield.applyTo(this);
                        client.getSession().write(CField.skillCooldown(divine.getId(), divineShield.getCooldown(this)));
                        addCooldown(divine.getId(), System.currentTimeMillis(), divineShield.getCooldown(this) * 1000);
                    }
                }
            } else if (getJob() == 312 && attacke != null) {
                final Skill divine = SkillFactory.getSkill(3120010);
                if (getTotalSkillLevel(divine) > 0) {
                    final MapleStatEffect divineShield = divine.getEffect(getTotalSkillLevel(divine));
                    if (divineShield.makeChanceResult()) {
                        if (attacke instanceof MapleMonster) {
                            final Rectangle bounds = divineShield.calculateBoundingBox(getTruePosition(), isFacingLeft());
                            final List<MapleMapObject> affected = getMap().getMapObjectsInRect(bounds, Arrays.asList(attacke.getType()));
                            int i = 0;

                            for (final MapleMapObject mo : affected) {
                                MapleMonster mons = (MapleMonster) mo;
                                if (mons.getStats().isFriendly() || mons.isFake()) {
                                    continue;
                                }
                                mons.applyStatus(this, new MonsterStatusEffect(MonsterStatus.STUN, 1, divineShield.getSourceId(), null, false), false, divineShield.getDuration(), true, divineShield);
                                final int theDmg = (int) (divineShield.getDamage() * getStat().getCurrentMaxBaseDamage() / 100.0);
                                mons.damage(this, theDmg, true);
                                getMap().broadcastMessage(MobPacket.damageMonster(mons.getObjectId(), theDmg));
                                i++;
                                if (i >= divineShield.getMobCount()) {
                                    break;
                                }
                            }
                        } else {
                            MapleCharacter chr = (MapleCharacter) attacke;
                            chr.addHP(-divineShield.getDamage());
                            attack.add((int) divineShield.getDamage());
                        }
                    }
                }
            } else if ((getJob() == 531 || getJob() == 532) && attacke != null) {
                final Skill divine = SkillFactory.getSkill(5310009); //slea.readInt() = 5310009, then slea.readInt() = damage. (175000)
                if (getTotalSkillLevel(divine) > 0) {
                    final MapleStatEffect divineShield = divine.getEffect(getTotalSkillLevel(divine));
                    if (divineShield.makeChanceResult()) {
                        if (attacke instanceof MapleMonster) {
                            final MapleMonster attacker = (MapleMonster) attacke;
                            final int theDmg = (int) (divineShield.getDamage() * getStat().getCurrentMaxBaseDamage() / 100.0);
                            attacker.damage(this, theDmg, true);
                            getMap().broadcastMessage(MobPacket.damageMonster(attacker.getObjectId(), theDmg));
                        } else {
                            final MapleCharacter attacker = (MapleCharacter) attacke;
                            attacker.addHP(-divineShield.getDamage());
                            attack.add((int) divineShield.getDamage());
                        }
                    }
                }
            } else if (getJob() == 132 && attacke != null) {
                final Skill divine = SkillFactory.getSkill(1320011);
                if (getTotalSkillLevel(divine) > 0 && !skillisCooling(divine.getId()) && getBuffSource(MapleBuffStat.BEHOLDER) == 1321007) {
                    final MapleStatEffect divineShield = divine.getEffect(getTotalSkillLevel(divine));
                    if (divineShield.makeChanceResult()) {
                        client.getSession().write(CField.skillCooldown(divine.getId(), divineShield.getCooldown(this)));
                        addCooldown(divine.getId(), System.currentTimeMillis(), divineShield.getCooldown(this) * 1000);
                        if (attacke instanceof MapleMonster) {
                            final MapleMonster attacker = (MapleMonster) attacke;
                            final int theDmg = (int) (divineShield.getDamage() * getStat().getCurrentMaxBaseDamage() / 100.0);
                            attacker.damage(this, theDmg, true);
                            getMap().broadcastMessage(MobPacket.damageMonster(attacker.getObjectId(), theDmg));
                        } else {
                            final MapleCharacter attacker = (MapleCharacter) attacke;
                            attacker.addHP(-divineShield.getDamage());
                            attack.add((int) divineShield.getDamage());
                        }
                    }
                }
            }
            if (attacke != null) {
                final int damr = (Randomizer.nextInt(100) < getStat().DAMreflect_rate ? getStat().DAMreflect : 0) + (getBuffedValue(MapleBuffStat.POWERGUARD) != null ? getBuffedValue(MapleBuffStat.POWERGUARD) : 0);
                final int bouncedam_ = damr + (getBuffedValue(MapleBuffStat.PERFECT_ARMOR) != null ? getBuffedValue(MapleBuffStat.PERFECT_ARMOR) : 0);
                if (bouncedam_ > 0) {
                    long bouncedamage = (long) (damage * bouncedam_ / 100);
                    long bouncer = (long) (damage * damr / 100);
                    damage -= bouncer;
                    if (attacke instanceof MapleMonster) {
                        final MapleMonster attacker = (MapleMonster) attacke;
                        bouncedamage = Math.min(bouncedamage, attacker.getMobMaxHp() / 10);
                        attacker.damage(this, (int) bouncedamage, true);
                        getMap().broadcastMessage(this, MobPacket.damageMonster(attacker.getObjectId(), bouncedamage), getTruePosition());
                        if (getBuffSource(MapleBuffStat.PERFECT_ARMOR) == 31101003) {
                            MapleStatEffect eff = this.getStatForBuff(MapleBuffStat.PERFECT_ARMOR);
                            if (eff.makeChanceResult()) {
                                attacker.applyStatus(this, new MonsterStatusEffect(MonsterStatus.STUN, 1, eff.getSourceId(), null, false), false, eff.getSubTime(), true, eff);
                            }
                        }
                    } else {
                        final MapleCharacter attacker = (MapleCharacter) attacke;
                        bouncedamage = Math.min(bouncedamage, attacker.getStat().getCurrentMaxHp() / 10);
                        attacker.addHP(-((int) bouncedamage));
                        attack.add((int) bouncedamage);
                        if (getBuffSource(MapleBuffStat.PERFECT_ARMOR) == 31101003) {
                            MapleStatEffect eff = this.getStatForBuff(MapleBuffStat.PERFECT_ARMOR);
                            if (eff.makeChanceResult()) {
                                attacker.disease(MapleDisease.STUN.getDisease(), 1);
                            }
                        }
                    }
                    ret.right = true;
                }
                if ((getJob() == 411 || getJob() == 412 || getJob() == 421 || getJob() == 422) && getBuffedValue(MapleBuffStat.SUMMON) != null && attacke != null) {
                    final List<MapleSummon> ss = getSummonsReadLock();
                    try {
                        for (MapleSummon sum : ss) {
                            if (sum.getTruePosition().distanceSq(getTruePosition()) < 400000.0 && (sum.getSkill() == 4111007 || sum.getSkill() == 4211007 || sum.getSkill() == 14111010)) {
                                final List<Pair<Integer, Integer>> allDamage = new ArrayList<>();
                                if (attacke instanceof MapleMonster) {
                                    final MapleMonster attacker = (MapleMonster) attacke;
                                    final int theDmg = (int) (SkillFactory.getSkill(sum.getSkill()).getEffect(sum.getSkillLevel()).getX() * damage / 100.0);
                                    allDamage.add(new Pair<>(attacker.getObjectId(), theDmg));
                                    getMap().broadcastMessage(SummonPacket.summonAttack(sum.getOwnerId(), sum.getObjectId(), (byte) 0x84, allDamage, getLevel(), true));
                                    attacker.damage(this, theDmg, true);
                                    checkMonsterAggro(attacker);
                                    if (!attacker.isAlive()) {
                                        getClient().getSession().write(MobPacket.killMonster(attacker.getObjectId(), 1, false));
                                    }
                                } else {
                                    final MapleCharacter chr = (MapleCharacter) attacke;
                                    final int dmg = SkillFactory.getSkill(sum.getSkill()).getEffect(sum.getSkillLevel()).getX();
                                    chr.addHP(-dmg);
                                    attack.add(dmg);
                                }
                            }
                        }
                    } finally {
                        unlockSummonsReadLock();
                    }
                }
            }
        }
        if (attack != null && attack.size() > 0 && attacke != null) {
            getMap().broadcastMessage(CField.pvpCool(attacke.getObjectId(), attack));
        }
        ret.left = damage;
        return ret;
    }

    public void onAttack(long maxhp, int maxmp, int skillid, int oid, int totDamage, int critCount) {
        if (stats.hpRecoverProp > 0) {
            if (Randomizer.nextInt(100) <= stats.hpRecoverProp) {//i think its out of 100, anyway
                if (stats.hpRecover > 0) {
                    healHP(stats.hpRecover);
                }
                if (stats.hpRecoverPercent > 0) {
                    addHP(((int) Math.min(maxhp, Math.min(((int) ((double) totDamage * (double) stats.hpRecoverPercent / 100.0)), stats.getMaxHp() / 2))));
                }
            }
        }
        if (stats.mpRecoverProp > 0 && !MapleJob.is惡魔殺手(getJob())) {
            if (Randomizer.nextInt(100) <= stats.mpRecoverProp) {//i think its out of 100, anyway
                healMP(stats.mpRecover);
            }
        }
        if (getBuffedValue(MapleBuffStat.COMBO_DRAIN) != null) {
            addHP(((int) Math.min(maxhp, Math.min(((int) ((double) totDamage * (double) getStatForBuff(MapleBuffStat.COMBO_DRAIN).getX() / 100.0)), stats.getMaxHp() / 2))));
        }
        if (getBuffSource(MapleBuffStat.COMBO_DRAIN) == 23101003) {
            addMP(((int) Math.min(maxmp, Math.min(((int) ((double) totDamage * (double) getStatForBuff(MapleBuffStat.COMBO_DRAIN).getX() / 100.0)), stats.getMaxMp() / 2))));
        }
        if (getBuffedValue(MapleBuffStat.REAPER) != null && getBuffedValue(MapleBuffStat.SUMMON) == null && getSummonsSize() < 4 && canSummon()) {
            final MapleStatEffect eff = getStatForBuff(MapleBuffStat.REAPER);
            if (eff.makeChanceResult()) {
                eff.applyTo(this, this, false, null, eff.getDuration());
            }
        }
        if (getJob() == 212 || getJob() == 222 || getJob() == 232) {
            int[] venomskills = {2120010, 2220010, 2320011};
            for (int i : venomskills) {
                final Skill skill = SkillFactory.getSkill(i);
                if (getTotalSkillLevel(skill) > 0) {
                    final MapleStatEffect venomEffect = skill.getEffect(getTotalSkillLevel(skill));
                    if (venomEffect.makeChanceResult() && getAllLinkMid().size() < venomEffect.getY()) {
                        setLinkMid(oid, venomEffect.getX());
                        venomEffect.applyTo(this);
                    }
                    break;
                }
            }
        }
        int[] venomskills = {4110011, 4120005, 4210010, 4220005, 4320005, 4340001, 14110004};
        for (int i : venomskills) {
            if (i == 4110011) {
                if (getTotalSkillLevel(4120011) > 0) {
                    i = 4120011;
                }
            } else if (i == 4210010) {
                if (getTotalSkillLevel(4220011) > 0) {
                    i = 4220011;
                }
            }
            final Skill skill = SkillFactory.getSkill(i);
            if (getTotalSkillLevel(skill) > 0) {
                final MapleStatEffect venomEffect = skill.getEffect(getTotalSkillLevel(skill));
                final MapleMonster monster = map.getMonsterByOid(oid);
                if (venomEffect.makeChanceResult() && monster != null) {
                    monster.applyStatus(this, new MonsterStatusEffect(MonsterStatus.POISON, 1, i, null, false), true, venomEffect.getDuration(), true, venomEffect);
                }
                break;
            }
        }
        if (getJob() == 2410 || getJob() == 2411 || getJob() == 2412) {
            final Skill skil = SkillFactory.getSkill(getJob() == 2412 ? 24120002 : 24100003);
            if (getTotalSkillLevel(skil) > 0 && critCount > 0 && skillid != 24120002 && skillid != 24100003) {
                final MapleStatEffect eff = skil.getEffect(getTotalSkillLevel(skil));
                if (eff.makeChanceResult()) {
                    setBattleshipHP(Math.min(getJob() == 2412 ? 40 : 20, currentBattleshipHP() + 1));
                    attackCarte(eff, oid, 1);
                }
            }
        }
        // effects
        if (skillid > 0) {
            final Skill skil = SkillFactory.getSkill(skillid);
            final MapleStatEffect effect = skil.getEffect(getTotalSkillLevel(skil));
            switch (skillid) {
                case 15111001:
                case 3111008:
                case 1078:
                case 31111003:
                case 11078:
                case 14101006:
                case 33111006: //swipe
                case 4101005: //drain
                case 5111004: { // Energy Drain
                    addHP(((int) Math.min(maxhp, Math.min(((int) ((double) totDamage * (double) effect.getX() / 100.0)), stats.getMaxHp() / 2))));
                    break;
                }
                case 5211006:
                case 22151002: //killer wing
                case 5220011: {//homing
                    setLinkMid(oid, effect.getX());
                    break;
                }
                case 33101007: { //jaguar
                    clearLinkMid();
                    break;
                }
            }
        }
    }

    public void attackCarte(final MapleStatEffect eff, final int oid, final int x) {
        if (x > 0) {
            lastBerserkTime += x; //lol unused variable.
            map.broadcastMessage(PhantomPacket.getCarteAnimation(this, oid, job, (int) lastBerserkTime, x));
            client.getSession().write(PhantomPacket.updateCardStack(currentBattleshipHP()));
        }
    }

    public void handleForceGain(int oid, int skillid) {
        handleForceGain(oid, skillid, 0);
    }

    public void handleForceGain(int oid, int skillid, int extraForce) {
        if (!GameConstants.isForceIncrease(skillid) && extraForce <= 0) {
            return;
        }
        int forceGain = 1;
        if (getLevel() >= 30 && getLevel() < 60) {
            forceGain = 2;
        } else if (getLevel() >= 60 && getLevel() < 100) {
            forceGain = 3;
        } else if (getLevel() >= 100) {
            forceGain = 4;
        }
        force++; // counter
        if (MapleJob.is惡魔殺手(getJob())) {
            addMP(extraForce > 0 ? extraForce : forceGain, true);
        }
        getClient().getSession().write(CField.gainForce(this, oid, force, forceGain));
        if (MapleJob.is惡魔殺手(getJob())) {
            if (stats.mpRecoverProp > 0 && extraForce <= 0) {
                if (Randomizer.nextInt(100) <= stats.mpRecoverProp) {//i think its out of 100, anyway
                    force++; // counter
                    addMP(stats.mpRecover, true);
                    getClient().getSession().write(CField.gainForce(this, oid, force, stats.mpRecover));
                }
            }
        }
    }

    public void afterAttack(int mobCount, int attackCount, int skillid) {
        switch (getJob()) {
            case 511:
            case 512: {
                handleEnergyCharge(5110001, mobCount * attackCount);
                break;
            }
            case 1510:
            case 1511:
            case 1512: {
                if (this.getBuffSource(MapleBuffStat.STORM_ELEMENTAL) == 15001022) {
                    handleEnergyCharge(15001022, 1);
                }
                break;
            }
            case 592:
                if (skillid == 4221001) {
                    setBattleshipHP(0);
                } else {
                    setBattleshipHP(Math.min(5, currentBattleshipHP() + 1)); //max 5
                }
                refreshBattleshipHP();
                break;
            case 110:
            case 111:
            case 112:
            case 1111:
            case 1112:
            case 2411:
            case 2412:
                if (skillid != 1111008 & getBuffedValue(MapleBuffStat.COMBO) != null) { // shout should not give orbs
                    handleOrbgain();
                }
                break;
        }
        if (getBuffedValue(MapleBuffStat.OWL_SPIRIT) != null) {
            if (currentBattleshipHP() > 0) {
                decreaseBattleshipHP();
            }
            if (currentBattleshipHP() <= 0) {
                cancelEffectFromBuffStat(MapleBuffStat.OWL_SPIRIT);
            }
        }
        if (!isIntern()) {
            cancelEffectFromBuffStat(MapleBuffStat.WIND_WALK);
            cancelEffectFromBuffStat(MapleBuffStat.INFILTRATE);
            final MapleStatEffect ds = getStatForBuff(MapleBuffStat.DARKSIGHT);
            if (ds != null) {
                if (ds.getSourceId() != 4330001 || !ds.makeChanceResult()) {
                    cancelEffectFromBuffStat(MapleBuffStat.DARKSIGHT);
                }
            }
        }
    }

    public void applyIceGage(int x) {
        updateSingleStat(MapleStat.ICE_GAGE, x);
    }

    public Rectangle getBounds() {
        return new Rectangle(getTruePosition().x - 25, getTruePosition().y - 75, 50, 75);
    }

    @Override
    public Map<Byte, Integer> getEquips(boolean fusionAnvil) {
        final Map<Byte, Integer> eq = new HashMap<>();
        for (final Item item : inventory[MapleInventoryType.EQUIPPED.ordinal()].newList()) {
            int itemId = item.getItemId();
            if (item instanceof Equip && fusionAnvil) {
                if (((Equip) item).getFusionAnvil() != 0) {
                    itemId = ((Equip) item).getFusionAnvil();
                }
            }
            eq.put((byte) item.getPosition(), itemId);
        }
        return eq;
    }

    @Override
    public Map<Byte, Integer> getSecondEquips(boolean fusionAnvil) {
        final Map<Byte, Integer> eq = new HashMap<>();
        for (final Item item : inventory[MapleInventoryType.EQUIPPED.ordinal()].newList()) {
            int itemId = item.getItemId();
            if (item instanceof Equip) {
                if (fusionAnvil) {
                    if (((Equip) item).getFusionAnvil() != 0) {
                        itemId = ((Equip) item).getFusionAnvil();
                    }
                }
                if (MapleJob.is天使破壞者(getJob()) && ItemConstants.類型.套服(itemId)) {
                    itemId = 1051291; //ab def overall
                }
            }
            if (MapleJob.is天使破壞者(getJob())) {
                if (!ItemConstants.類型.套服(itemId) && !ItemConstants.類型.副手(itemId)
                        && !ItemConstants.類型.武器(itemId) && !ItemConstants.類型.勳章(itemId)) {
                    continue;
                }
            }
            eq.put((byte) item.getPosition(), itemId);
        }
        return eq;
    }

    @Override
    public Map<Byte, Integer> getTotems() {
        final Map<Byte, Integer> eq = new HashMap<>();
        for (final Item item : inventory[MapleInventoryType.EQUIPPED.ordinal()].newList()) {
            byte pos = (byte) ((item.getPosition() + 5000) * -1);
            if (pos < 0 || pos > 2) { //3 totem slots
                continue;
            }
            if (item.getItemId() < 1200000 || item.getItemId() >= 1210000) {
                continue;
            }
            eq.put(pos, item.getItemId());
        }
        return eq;
    }
    private transient PlayerRandomStream CRand;

    public final PlayerRandomStream CRand() {
        return CRand;
    }

    public void handleCardStack(int mobid) {
        Skill noir = SkillFactory.getSkill(24120002);
        Skill blanc = SkillFactory.getSkill(24100003);
        MapleStatEffect ceffect;
        int advSkillLevel = getTotalSkillLevel(noir);
        boolean isAdv = false;
        if (advSkillLevel > 0) {
            ceffect = noir.getEffect(advSkillLevel);
            isAdv = true;
        } else if (getSkillLevel(blanc) > 0) {
            ceffect = blanc.getEffect(getTotalSkillLevel(blanc));
        } else {
            return;
        }
        if (getJob() == 2412 && getCardStack() == 40) {
            return;
        }
        if (getJob() == 2400 && getCardStack() == 20 || getJob() == 2410 && getCardStack() == 20 || getJob() == 2411 && getCardStack() == 20) {
            return;
        }
        if (ceffect.makeChanceResult()) {
            if (this.cardStack < (getJob() == 2412 ? 40 : 20)) {
                this.cardStack = (byte) (this.cardStack + 1);
            }
            this.runningStack += 1;
            this.client.getSession().write(PhantomPacket.gainCardStack(this, this.runningStack, isAdv ? 2 : 1, ceffect.getSourceId(), mobid, 1));
            this.client.getSession().write(PhantomPacket.updateCardStack(this.cardStack));
        }
    }
    /*
     public void resetRunningStack() {
     this.runningStack = 0;
     }

     public int getRunningStack() {
     return this.runningStack;
     }

     public void addRunningStack(int s) {
     this.runningStack += s;
     }

     public void setCardStack(byte amount) {
     this.cardStack = amount;
     }

     public byte getCardStack() {
     return cardStack;
     } // running id plox
     */

    public final MapleCharacterCards getCharacterCard() {
        return characterCard;
    }

    /*
     * Start of Custom Feature
     */
    public int getReborns() {
        return reborns;
    }

    public int getAPS() {
        return apstorage;
    }

    public void gainAPS(int aps) {
        apstorage += aps;
    }

    public final boolean canHold(final int itemid) {
        return getInventory(GameConstants.getInventoryType(itemid)).getNextFreeSlot() > -1;
    }

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
        stats.recalcLocalStats(this);
    }

    public int getInt() {
        return int_;
    }

    public void setInt(int int_) {
        this.int_ = int_;
        stats.recalcLocalStats(this);
    }

    public int getLuk() {
        return luk;
    }

    public int getDex() {
        return dex;
    }

    public void setLuk(int luk) {
        this.luk = luk;
        stats.recalcLocalStats(this);
    }

    public void setDex(int dex) {
        this.dex = dex;
        stats.recalcLocalStats(this);
    }

    public void gainVPoints(int amount) {
        this.vpoints += amount;
    }

    public void gainEPoints(int amount) {
        this.epoints += amount;
    }

    public void gainDPoints(int amount) {
        this.dpoints += amount;
    }

    public static String makeMapleReadable(String in) {
        String i = in.replace('I', 'i');
        i = i.replace('l', 'L');
        i = i.replace("rn", "Rn");
        i = i.replace("vv", "Vv");
        i = i.replace("VV", "Vv");
        return i;
    }

    public void ban(String reason, boolean autoban, boolean mac) {
        try {
            if (mac) {
                client.banMacs();
            }
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET banned = 1, banreason = ? WHERE id = ?");
            ps.setString(1, reason);
            ps.setInt(2, accountid);
            ps.executeUpdate();
            ps.close();
            final String ip = client.getSession().getRemoteAddress().toString().split(":")[0];
            ps = con.prepareStatement("SELECT ip FROM ipbans WHERE ip = ?");
            ps.setString(1, ip);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, ip);
                ps.executeUpdate();
                ps.close();
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
        }
        client.getSession().close(true);
    }

    public void ban(String reason, boolean permBan) {
        if (lastmonthfameids == null) {
            throw new RuntimeException("Trying to ban a non-loaded character (testhack)");
        }
        try {
            getClient().banMacs();
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET banned = ?, banreason = ? WHERE id = ?");
            ps.setInt(1, 1);
            ps.setString(2, reason);
            ps.setInt(3, accountid);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
            String[] ipSplit = client.getSession().getRemoteAddress().toString().split(":");
            ps.setString(1, ipSplit[0]);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
        }
        client.getSession().close(true);
    }

    public static boolean ban(String id, String reason, boolean accountId) {
        PreparedStatement ps = null;
        try {
            Connection con = DatabaseConnection.getConnection();
            if (id.matches("/[0-9]{1,3}\\..*")) {
                ps = con.prepareStatement("INSERT INTO ipbans VALUES (DEFAULT, ?)");
                ps.setString(1, id);
                ps.executeUpdate();
                ps.close();
                return true;
            }
            if (accountId) {
                ps = con.prepareStatement("SELECT id FROM accounts WHERE name = ?");
            } else {
                ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
            }
            boolean ret = false;
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement psb = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET banned = 1, banreason = ? WHERE id = ?")) {
                        psb.setString(1, reason);
                        psb.setInt(2, rs.getInt(1));
                        psb.executeUpdate();
                    }
                    ret = true;
                }
            }
            ps.close();
            return ret;
        } catch (SQLException ex) {
        } finally {
            try {
                if (ps != null && !ps.isClosed()) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
        }
        return false;
    }

    public static boolean unban(String name) {
        try {
            int accountid = -1;
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT accountid FROM characters WHERE name = ?");
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                accountid = rs.getInt("accountid");
            }
            ps.close();
            rs.close();
            if (accountid == -1) {
                return false;
            }
            ps = con.prepareStatement("UPDATE accounts SET banned = -1 WHERE id = ?");
            ps.setInt(1, accountid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            //  log.error("Error while unbanning", ex);
            return false;
        }
        return true;
    }

    public void changeMap(int map, int portal) {
        MapleMap warpMap = client.getChannelServer().getMapFactory().getMap(map);
        changeMap(warpMap, warpMap.getPortal(portal));
    }

    public void unchooseStolenSkill(int skillID) { //base skill
        if (skillisCooling(20031208) || stolenSkills == null) {
            dropMessage(-6, "[Loadout] The skill is under cooldown. Please wait.");
            return;
        }
        final int stolenjob = MapleJob.getNumber(skillID / 10000);
        boolean changed = false;
        for (Pair<Integer, Boolean> sk : stolenSkills) {
            if (sk.right && MapleJob.getNumber(sk.left / 10000) == stolenjob) {
                cancelStolenSkill(sk.left);
                sk.right = false;
                changed = true;
            }
        }
        if (changed) {
            final Skill skil = SkillFactory.getSkill(skillID);
            changeSkillLevel_Skip(skil, getSkillLevel(skil), (byte) 0);
            client.getSession().write(PhantomPacket.replaceStolenSkill(GameConstants.getStealSkill(stolenjob), 0));
        }
    }

    public void cancelStolenSkill(int skillID) {
        final Skill skk = SkillFactory.getSkill(skillID);
        final MapleStatEffect eff = skk.getEffect(getTotalSkillLevel(skk));

        if (eff.isMonsterBuff() || (eff.getStatups().isEmpty() && !eff.getMonsterStati().isEmpty())) {
            for (MapleMonster mons : map.getAllMonstersThreadsafe()) {
                for (MonsterStatus b : eff.getMonsterStati().keySet()) {
                    if (mons.isBuffed(b) && mons.getBuff(b).getFromID() == this.id) {
                        mons.cancelStatus(b);
                    }
                }
            }
        } else if (eff.getDuration() > 0 && !eff.getStatups().isEmpty()) {
            for (MapleCharacter chr : map.getCharactersThreadsafe()) {
                chr.cancelEffect(eff, false, -1, eff.getStatups());

            }
        }
    }

    public void chooseStolenSkill(int skillID) {
        if (skillisCooling(20031208) || stolenSkills == null) {
            dropMessage(-6, "[Loadout] The skill is under cooldown. Please wait.");
            return;
        }
        final Pair<Integer, Boolean> dummy = new Pair<>(skillID, false);
        if (stolenSkills.contains(dummy)) {
            unchooseStolenSkill(skillID);
            stolenSkills.get(stolenSkills.indexOf(dummy)).right = true;

            client.getSession().write(PhantomPacket.replaceStolenSkill(GameConstants.getStealSkill(MapleJob.getNumber(skillID / 10000)), skillID));
            //if (ServerConstants.CUSTOM_SKILL) {
           //     client.getSession().write(MaplePacketCreator.skillCooldown(20031208, 5));
            //    addCooldown(20031208, System.currentTimeMillis(), 5000);
            //}
        }
    }

    public void addStolenSkill(int skillID, int skillLevel) {
        if (skillisCooling(20031208) || stolenSkills == null) {
            dropMessage(-6, "[Loadout] The skill is under cooldown. Please wait.");
            return;
        }
        final Pair<Integer, Boolean> dummy = new Pair<>(skillID, true);
        final Skill skil = SkillFactory.getSkill(skillID);
        if (!stolenSkills.contains(dummy) && GameConstants.canSteal(skil)) {
            dummy.right = false;
            skillLevel = Math.min(skil.getMaxLevel(), skillLevel);
            final int jobid = MapleJob.getNumber(skillID / 10000);
            if (!stolenSkills.contains(dummy) && getSkillLevel(GameConstants.getStealSkill(jobid)) > 0) {
                int count = 0;
                skillLevel = Math.min(getSkillLevel(GameConstants.getStealSkill(jobid)), skillLevel);
                for (Pair<Integer, Boolean> sk : stolenSkills) {
                    if (MapleJob.getNumber(sk.left / 10000) == jobid) {
                        count++;
                    }
                }
                if (count < GameConstants.getNumSteal(jobid)) {
                    stolenSkills.add(dummy);
                    changed_skills = true;
                    changeSkillLevel_Skip(skil, skillLevel, (byte) skillLevel);
                    client.getSession().write(PhantomPacket.addStolenSkill(jobid, count, skillID, skillLevel));
                    //client.getSession().write(MaplePacketCreator.updateStolenSkills(this, jobid));
                }
            }
        }
    }

    public void removeStolenSkill(int skillID) {
        if (skillisCooling(20031208) || stolenSkills == null) {
            dropMessage(-6, "[Loadout] The skill is under cooldown. Please wait.");
            return;
        }
        final int jobid = MapleJob.getNumber(skillID / 10000);
        final Pair<Integer, Boolean> dummy = new Pair<>(skillID, false);
        int count = -1, cc = 0;
        for (int i = 0; i < stolenSkills.size(); i++) {
            if (stolenSkills.get(i).left == skillID) {
                if (stolenSkills.get(i).right) {
                    unchooseStolenSkill(skillID);
                }
                count = cc;
                break;
            } else if (MapleJob.getNumber(stolenSkills.get(i).left / 10000) == jobid) {
                cc++;
            }
        }
        if (count >= 0) {
            cancelStolenSkill(skillID);
            stolenSkills.remove(dummy);
            dummy.right = true;
            stolenSkills.remove(dummy);
            changed_skills = true;
            changeSkillLevel_Skip(SkillFactory.getSkill(skillID), 0, (byte) 0);
            //hacky process begins here
            client.getSession().write(PhantomPacket.replaceStolenSkill(GameConstants.getStealSkill(jobid), 0));
            for (int i = 0; i < GameConstants.getNumSteal(jobid); i++) {
                client.getSession().write(PhantomPacket.removeStolenSkill(jobid, i));
            }
            count = 0;
            for (Pair<Integer, Boolean> sk : stolenSkills) {
                if (MapleJob.getNumber(sk.left / 10000) == jobid) {
                    client.getSession().write(PhantomPacket.addStolenSkill(jobid, count, sk.left, getSkillLevel(sk.left)));
                    if (sk.right) {
                        client.getSession().write(PhantomPacket.replaceStolenSkill(GameConstants.getStealSkill(jobid), sk.left));
                    }
                    count++;
                }
            }
            client.getSession().write(PhantomPacket.removeStolenSkill(jobid, count));
            //client.getSession().write(MaplePacketCreator.updateStolenSkills(this, jobid));
        }
    }

    public List<Pair<Integer, Boolean>> getStolenSkills() {
        return stolenSkills;
    }

    public void setHonourExp(int exp) {
        this.honourExp = exp;
    }

    public int getHonourExp() {
        return honourExp;
    }

    public void setHonorLevel(int level) {
        this.honorLevel = level;
    }

    public int getHonorLevel() {
        if (honorLevel == 0) {
            honorLevel++;
        }
        return honorLevel;
    }

    public List<InnerSkillValueHolder> getInnerSkills() {
        return innerSkills;
    }

    public void addHonorExp(int amount, boolean show) {
        if (getHonorLevel() < 1) {
            setHonorLevel(1);
        }
        if (getHonourExp() + amount >= getHonorLevel() * 500) {
            honourLevelUp();
            int leftamount = (getHonourExp() + amount) - ((getHonorLevel() - 1) * 500);
            leftamount = Math.min(leftamount, ((getHonorLevel()) * 500) - 1);
            setHonourExp(leftamount);
            return;
        }
        setHonourExp(getHonourExp() + amount);
        client.getSession().write(CWvsContext.updateAzwanFame(getHonorLevel(), getHonourExp(), true));
        client.getSession().write(CWvsContext.updateSpecialStat("honorLeveling", 0, getHonorLevel(), getHonourNextExp()));
        if (show) {
            dropMessage(5, "You obtained " + amount + " Honor EXP.");
        }
    }

    public int getHonourNextExp() {
        if (getHonorLevel() == 0) {
            return 0;
        }
        return (getHonorLevel() + 1) * 500;
    }

    public void honourLevelUp() {
        setHonorLevel(getHonorLevel() + 1);
        client.getSession().write(CWvsContext.updateAzwanFame(getHonorLevel(), getHonourExp(), true));
        client.getSession().write(CWvsContext.updateSpecialStat("honorLeveling", 0, getHonorLevel(), getHonourNextExp()));
        InnerSkillValueHolder inner = null;
        byte ability = 0;
        switch (getHonorLevel()) {
            case 2:
                ability = 1;
                break;
            case 30:
                ability = 2;
                break;
            case 70:
                ability = 3;
                break;
        }
        if (getHonorLevel() == 2 || getHonorLevel() == 30 || getHonorLevel() == 70 && inner != null && ability >= 1 && ability <= 3) {
            inner = new InnerSkillValueHolder(70000015, (byte) 1, (byte) 1, (byte) 0, false);
            innerSkills.add(inner);
            changeSkillLevel(SkillFactory.getSkill(inner.getSkillId()), inner.getSkillLevel(), inner.getSkillLevel());
            client.getSession().write(CField.getCharInfo(this));
            client.getSession().write(CField.updateInnerPotential(ability, inner.getSkillId(), inner.getSkillLevel(), inner.getRank()));
        }
    }

    public void gainHonor(int honor, boolean show) {
        addHonorExp(honor, false);
        if (show) {
            client.getSession().write(InfoPacket.showInfo(honor + " Honor EXP obtained."));
        }
    }

    public void azwanReward(final int map, final int portal) {
        client.getSession().write(CField.UIPacket.openUIOption(0x45, 0));
        client.getSession().write(CWvsContext.enableActions());
        MapTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                changeMap(map, portal);
            }
        }, 5 * 1000);
    }

    public void setTouchedRune(int type) {
        touchedrune = type;
    }

    public int getTouchedRune() {
        return touchedrune;
    }

    public long getRuneTimeStamp() {
        return Long.parseLong(getKeyValue("LastTouchedRune"));
    }

    public void setRuneTimeStamp(long time) {
        setKeyValue("LastTouchedRune", String.valueOf(time));
    }

    public void setKeyValue(String key, String values) {
        if (CustomValues.containsKey(key)) {
            CustomValues.remove(key);
        }
        CustomValues.put(key, values);
        keyvalue_changed = true;
    }

    public String getKeyValue(String key) {
        if (CustomValues.containsKey(key)) {
            return CustomValues.get(key);
        }
        return null;
    }

    public void setChatType(short chattype) {
        this.chattype = chattype;
    }

    public short getChatType() {
        return chattype;
    }

    public void gainItem(int code, int amount) {
        MapleInventoryManipulator.addById(client, code, (short) amount, null);
    }

    public void gainItem(int code, int amount, String gmLog) {
        MapleInventoryManipulator.addById(client, code, (short) amount, gmLog);
    }

    public final int[] getFriendShipPoints() {
        return friendshippoints;
    }

    public final void setFriendShipPoints(int joejoe, int hermoninny, int littledragon, int ika, int Wooden) {
        this.friendshippoints[0] = joejoe;
        this.friendshippoints[1] = hermoninny;
        this.friendshippoints[2] = littledragon;
        this.friendshippoints[3] = ika;
        this.friendshippoints[4] = Wooden;
    }

    public final int getFriendShipToAdd() {
        return friendshiptoadd;
    }

    public final void setFriendShipToAdd(int points) {
        this.friendshiptoadd = points;
    }

    public final void addFriendShipToAdd(int points) {
        this.friendshiptoadd += points;
    }

    public void makeNewAzwanShop() {
        /*
         * Azwan Etc Scrolls 60% - 30 conqueror coins
         * Azwan Weapon Scrolls 60% - 50 conqueror coins
         * 9 Conqueror's Coin - 1 emperor coin
         * Circulator - rank * 1 emperor coins
         * Azwan Scrolls 50% - 3 emperor coins
         * Azwan Scrolls 40% - 5 emperor coins
         * Azwan Scrolls 30% - 7 emperor coins
         * Azwan Scrolls 20% - 10 emperor coins
         * Emperor Armor Recipes - 70 emperor coins
         * Emperor Accessory & Weapon Recipes - 75 emperor coins
         * Emperor Weapon - 300 emperor coins
         * Emperor Armor - 250 emperor coins
         * Duke Accessory - 50 emperor coins
         * Duke Accessory - 50 emperor coins
         * 10 Sunrise Dew - 3 conqueror coins
         * 10 Simset Dew - 4 conqueror coins
         * 10 Reindeer Milk - 2 conqueror coins
         * 100% Scrolls - 3 conqueror coins
         */
        azwanShopList = new MapleShop(100000000 + getId(), 2182002);
        int itemid = GameConstants.getAzwanRecipes()[(int) Math.floor(Math.random() * GameConstants.getAzwanRecipes().length)];
        azwanShopList.addItem(new MapleShopItem((short) 1, (short) 1, itemid, 0, (short) 0, 4310038, 75, (byte) 0, 0, 0, 0, false));
        itemid = GameConstants.getAzwanScrolls()[(int) Math.floor(Math.random() * GameConstants.getAzwanScrolls().length)];
        azwanShopList.addItem(new MapleShopItem((short) 1, (short) 1, itemid, 0, (short) 0, 4310036, 15, (byte) 0, 0, 0, 0, false));
        itemid = (Integer) GameConstants.getUseItems()[(int) Math.floor(Math.random() * GameConstants.getUseItems().length)].getLeft();
        int price = (Integer) GameConstants.getUseItems()[(int) Math.floor(Math.random() * GameConstants.getUseItems().length)].getRight();
        azwanShopList.addItem(new MapleShopItem((short) 1, (short) 1, itemid, price, (short) 0, 0, 0, (byte) 0, 0, 0, 0, false));
        itemid = GameConstants.getCirculators()[(int) Math.floor(Math.random() * GameConstants.getCirculators().length)];
        price = InnerAbillity.getInstance().getCirculatorRank(itemid);
        if (price > 10) {
            price = 10;
        }
        azwanShopList.addItem(new MapleShopItem((short) 1, (short) 1, itemid, 0, (short) 0, 4310038, price, (byte) 0, 0, 0, 0, false));
        client.getSession().write(CField.getWhisper("Jean Pierre", client.getChannel(), "Psst! I got some new items in stock! Come take a look! Oh, but if your Honor Level increased, why not wait until you get a Circulator?"));
    }

    public MapleShop getAzwanShop() {
        return azwanShopList;
    }

    public void openAzwanShop() {
        if (azwanShopList == null) {
            MapleShopFactory.getInstance().getShop(2182002).sendShop(client);
        } else {
            getAzwanShop().sendShop(client);
        }
    }

    public void sendWelcome() {
        getClient().getSession().write(CField.startMapEffect("Welcome to " + getClient().getChannelServer().getServerName() + "!", 5122000, true));
        dropMessage(1, "Welcome to " + getClient().getChannelServer().getServerName() + ", " + getName() + " ! \r\nUse @npc to collect your Item Of Appreciation once you're level 10! \r\nUse @help for commands. \r\nGood luck and have fun!");
        dropMessage(5, "Your EXP Rate will be set to 1x until you finish the tutorial.");
        dropMessage(5, "Use @npc to collect your Item Of Appreciation once you're level 10! Use @help for commands. Good luck and have fun!");
        dropMessage(1, "Use @help for additional commands.");
    }

    private static void addMedalString(final MapleCharacter c, final StringBuilder sb) {
        final Item medal = c.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -46);
        if (medal != null) { // Medal
            sb.append("<");
            if (medal.getItemId() == 1142257 && MapleJob.is冒險家(c.getJob())) {
                MapleQuestStatus stat = c.getQuestNoAdd(MapleQuest.getInstance(GameConstants.ULT_EXPLORER));
                if (stat != null && stat.getCustomData() != null) {
                    sb.append(stat.getCustomData());
                    sb.append("的繼承者");
                } else {
                    sb.append(MapleItemInformationProvider.getInstance().getName(medal.getItemId()));
                }
            } else {
                sb.append(MapleItemInformationProvider.getInstance().getName(medal.getItemId()));
            }
            sb.append("> ");
        }
    }

    public void updateReward() {
        List<MapleReward> rewards = new LinkedList();
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM rewards WHERE `cid`=?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    //rewards.last();
                    //int size = rewards.getRow();
                    //rewards.first();
                    //client.getSession().write(Reward.updateReward(rewards.getInt("id"), (byte) 9, rewards, size, 9));
                    while (rs.next()) {
                        rewards.add(new MapleReward(
                                rs.getInt("id"),
                                rs.getLong("start"),
                                rs.getLong("end"),
                                rs.getInt("type"),
                                rs.getInt("itemId"),
                                rs.getInt("mp"),
                                rs.getInt("meso"),
                                rs.getInt("exp"),
                                rs.getString("desc")));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Unable to update rewards: " + e);
        }
        client.getSession().write(Reward.updateReward(0, (byte) 0x09, rewards, 0x09));
    }

    public MapleReward getReward(int id) {
        MapleReward reward = null;
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM rewards WHERE `id` = ?")) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        reward = new MapleReward(rs.getInt("id"), rs.getLong("start"), rs.getLong("end"), rs.getInt("type"), rs.getInt("itemId"), rs.getInt("mp"), rs.getInt("meso"), rs.getInt("exp"), rs.getString("desc"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Unable to obtain reward information: " + e);
        }
        return reward;
    }

    public void addReward(int type, int item, int mp, int meso, int exp, String desc) {
        addReward(0L, 0L, type, item, mp, meso, exp, desc);
    }

    public void addReward(long start, long end, int type, int item, int mp, int meso, int exp, String desc) {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO rewards (`cid`, `start`, `end`, `type`, `itemId`, `mp`, `meso`, `exp`, `desc`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                ps.setInt(1, id);
                ps.setLong(2, start);
                ps.setLong(3, end);
                ps.setInt(4, type);
                ps.setInt(5, item);
                ps.setInt(6, mp);
                ps.setInt(7, meso);
                ps.setInt(8, exp);
                ps.setString(9, desc);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Unable to obtain reward: " + e);
        }
    }

    public void deleteReward(int id) {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM rewards WHERE `id` = ?")) {
                ps.setInt(1, id);
                ps.execute();
            }
        } catch (SQLException e) {
            System.err.println("Unable to delete reward: " + e);
        }
        updateReward();
    }

    public void checkCustomReward(int level) {
        List<Integer> rewards = new LinkedList<>();
        /* 類型(type)：
         * [3] 楓葉點數
         * [4] 楓幣
         * [5] 經驗值
         */
        int mp = 0;
        switch (level) {
            case 10:
                rewards.add(2450000); // 獵人的幸運
                rewards.add(2022918); // 掉落機率 1.5倍優惠券
                break;
            case 20:
                rewards.add(1032099); // 葡萄耳環
                rewards.add(2022918); // 掉落機率 1.5倍優惠券
                break;
            case 30:
                addReward(4, 0, 0, 1000000, 0, "恭喜您達到30級！");
                rewards.add(1112659); // 冒險家的克勞特斯戒指
                rewards.add(2450000); // 獵人的幸運
                break;
            case 50:
                rewards.add(2022918); // 掉落機率 1.5倍優惠券
                break;
            case 70:
                rewards.add(1003016); // 野狼的頭巾
                rewards.add(2450000); // 獵人的幸運
                rewards.add(2022918); // 掉落機率 1.5倍優惠券
                break;
            case 100:
                addReward(4, 0, 0, 50000000, 0, "恭喜您達到100級！");//just for beta
                rewards.add(1132043); // 聯合鎖扣
                rewards.add(2450000); // 獵人的幸運
                rewards.add(2022918); // 掉落機率 1.5倍優惠券
                break;
            case 120:
                rewards.add(1182007); // 彩虹胸章
                rewards.add(2450000); // 獵人的幸運
                break;
            case 150:
                rewards.add(1142349); // 傳說主角的勳章
                break;
            case 170:
                rewards.add(1142295); // 簽到終結者的勳章
                break;
            case 200:
                mp = 30;
                break;
            case 250:
                mp = 300;
                break;
        }
        for (int reward : rewards) {
            addReward(1, reward, 0, 0, 0, "恭喜您達到" + level + "級！");
        }
        if (mp != 0) {
            addReward(3, 0, mp, 0, 0, "恭喜您達到" + level + "級！");
        }
        updateReward();
    }

    public void newCharRewards() {
        List<Integer> rewards = new LinkedList<>();
        rewards.add(2022680);
        rewards.add(2450031);
        rewards.add(1142358);
        for (int reward : rewards) {
            addReward(1, reward, 0, 0, 0, "Here is a special reward for beginners to help you start your journey!");
        }
        updateReward();
    }

    public boolean canVote() {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM ipvotes WHERE `accid` = ?")) {
                ps.setInt(1, getAccountID());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    if ((rs.getLong("lastvote") - (System.currentTimeMillis() / 1000)) > 6 * 60 * 60) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("Unable to obtain vote information: " + e);
        }
        return false;
    }

    public void setVote() {
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM ipvotes WHERE `accid` = ?")) {
                ps.setInt(1, getAccountID());
                ps.execute();
            }
        } catch (SQLException e) {
            System.err.println("Unable to delete vote entry: " + e);
        }
        try {
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO ipvotes (`ip`, `accid`, `lastvote`) VALUES (?, ?, ?)")) {
                ps.setString(1, getClient().getSession().getRemoteAddress().toString().split(":")[0].replaceAll("/", ""));
                ps.setInt(2, getAccountID());
                ps.setLong(3, System.currentTimeMillis() / 1000);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Unable to update votes: " + e);
        }
    }

    public void removeBGLayers() {
        for (byte i = 0; i < 127; i++) {
            client.getSession().write(CField.removeBGLayer(true, 0, i, 0));
            //duration 0 = forever map 0 = current map
        }
    }

    public void showBGLayers() {
        for (byte i = 0; i < 127; i++) {
            client.getSession().write(CField.removeBGLayer(false, 0, i, 0));
            //duration 0 = forever map 0 = current map
        }
    }
    public static final int CUSTOM_BG = 61000000;

    public int getCustomBGState() {
        return getIntNoRecord(CUSTOM_BG);
    }

    public void toggleCustomBGState() {
        getQuestNAdd(MapleQuest.getInstance(CUSTOM_BG)).setCustomData(String.valueOf(getCustomBGState() == 1 ? 0 : 1));
        for (byte i = 0; i < 127; i++) {
            WriteFuture write = client.getSession().write(CField.removeBGLayer((getCustomBGState() == 1), 0, i, 0));
            //duration 0 = forever map 0 = current map
        }
    }

    public static void addLinkSkill(int cid, int skill) {
        Connection con = DatabaseConnection.getConnection();
        try {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO skills (characterid, skillid, skilllevel, masterlevel, expiration) VALUES (?, ?, ?, ?, ?)")) {
                ps.setInt(1, cid);
                if (GameConstants.isApplicableSkill(skill)) { //do not save additional skills
                    ps.setInt(2, skill);
                    ps.setInt(3, 1);
                    ps.setByte(4, (byte) 1);
                    ps.setLong(5, -1);
                    ps.execute();
                }
            }
        } catch (SQLException ex) {
            System.err.println("Failed adding link skill: " + ex);
        }
    }

    public int getWheelItem() {
        return wheelItem;
    }

    public void setWheelItem(int wheelItem) {
        this.wheelItem = wheelItem;
    }

    public static void removePartTime(int cid) {
        Connection con = DatabaseConnection.getConnection();
        try {
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM parttime where cid = ?")) {
                ps.setInt(1, cid);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            System.err.println("Failed to remove part time job: " + ex);
        }
    }

    public static void addPartTime(PartTimeJob partTime) {
        if (partTime.getCharacterId() < 1) {
            return;
        }
        addPartTime(partTime.getCharacterId(), partTime.getJob(), partTime.getTime(), partTime.getReward());
    }

    public static void addPartTime(int cid, byte job, long time, int reward) {
        Connection con = DatabaseConnection.getConnection();
        try {
            try (PreparedStatement ps = con.prepareStatement("INSERT INTO parttime (cid, job, time, reward) VALUES (?, ?, ?, ?)")) {
                ps.setInt(1, cid);
                ps.setByte(2, job);
                ps.setLong(3, time);
                ps.setInt(4, reward);
                ps.execute();
            }
        } catch (SQLException ex) {
            System.err.println("Failed to add part time job: " + ex);
        }
    }

    public static PartTimeJob getPartTime(int cid) {
        PartTimeJob partTime = new PartTimeJob(cid);
        Connection con = DatabaseConnection.getConnection();
        try {
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM parttime WHERE cid = ?")) {
                ps.setInt(1, cid);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        partTime.setJob(rs.getByte("job"));
                        partTime.setTime(rs.getLong("time"));
                        partTime.setReward(rs.getInt("reward"));
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("Failed to retrieve part time job: " + ex);
        }
        return partTime;
    }

    public void handleKaiserCombo() {
        if (kaiserCombo < 1000) {
            kaiserCombo += 4;
        }
        SkillFactory.getSkill(61111008).getEffect(1).applyKaiser_Combo(this, kaiserCombo);
    }

    public void resetKaiserCombo() {
        kaiserCombo = 0;
        SkillFactory.getSkill(61111008).getEffect(1).applyKaiser_Combo(this, kaiserCombo);
    }

    public void setLuminousMode(final int skillid) {
        if (skillid >= 27100000 && skillid < 27120400 && (luminousState != 20040219 && luminousState != 20040220)) {
            System.out.println("Value:" + ((getLightGauge() - 277) <= 0) + ":" + (runningLight == 1) + ":::" + ((getDarkGauge() + 416) >= 9999) + ":" + (runningDark == 1));
            int chage_skill = GameConstants.getLuminousSkillMode(skillid);
            if ((getLightGauge() <= 0 && runningLight == 1) || (getDarkGauge() >= 9999) && runningDark == 1) {
                System.out.println("執行平衡狀態");
                changeLuminousMode(runningLight == 1 ? 20040220 : 20040219);
                client.getSession().write(CWvsContext.enableActions());
            } else if (skillid >= 27100000 && skillid < 27120400 && getLuminousState() < 20040000) {
                setLuminousState(chage_skill);
                MapleStatEffect effect = SkillFactory.getSkill(chage_skill).getEffect(getTotalSkillLevel(chage_skill));
                EnumMap stat = new EnumMap(MapleBuffStat.class);
                stat.put(MapleBuffStat.LUMINOUS_GAUGE, 1);
                client.getSession().write(CWvsContext.BuffPacket.giveBuff(chage_skill, 0, stat, effect, this));
                effect.applyTo(this);
            }
        }
    }

    public void changeLuminousMode(final int skillid) {
        final boolean equilibrium = skillid == 20040220 || skillid == 20040219;
        final boolean eclipse = skillid == 20040217;
        final boolean sunfire = skillid == 20040216;
        final MapleCharacter chr = this;
        int changeSkill = 0;
        if (equilibrium || (!eclipse && !sunfire)) {
            return; //impossible
        }
        dispelBuff(skillid);
        if (runningLight == 1) {
            changeSkill = 20040220;
        } else {
            changeSkill = 20040219;
        }
        luminousState = changeSkill;
        MapleStatEffect effect = SkillFactory.getSkill(changeSkill).getEffect(getTotalSkillLevel(changeSkill));
        EnumMap stat = new EnumMap(MapleBuffStat.class);
        stat.put(MapleBuffStat.LUMINOUS_GAUGE, 2);
        client.getSession().write(CWvsContext.BuffPacket.giveBuff(changeSkill, 0, stat, effect, this));
        effect.applyTo(this);
        equipChanged();
        WorldTimer.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                dispelBuff(luminousState);
                if (runningLight == 1) {
                    luminousState = 20040216;
                } else {
                    luminousState = 20040217;
                }
                MapleStatEffect effect = SkillFactory.getSkill(luminousState).getEffect(getTotalSkillLevel(luminousState));
                EnumMap stat = new EnumMap(MapleBuffStat.class);
                stat.put(MapleBuffStat.LUMINOUS_GAUGE, 1);
                client.getSession().write(CWvsContext.BuffPacket.giveBuff(luminousState, 0, stat, effect, chr));
                effect.applyTo(chr);
            }
        }, 13000);
    }

    public void handleLuminous(int skillid) {
        if (luminousState == 20040219 || luminousState == 20040220) { // 平衡
            return;
        }
        System.out.println("執行handleLuminous:" + skillid);
        // gauge = 0 Dark, gauge = 9999 Light
        // 預設值 gauge = 5000, type = 3
        // 黑預設值 gauge = 1, type = 1
        // 每次使用增加 76, 過千就 416
        // 第一次滿值 gauge = 9999, type = 2
        // 每次使用減少 227
        int[] lightSkills = new int[]{27001100, 27101100, 27101101, 27111100, 27111101, 27121100};
        int[] darkSkills = new int[]{27001201, 27101202, 27111202, 27121201, 27121202, 27120211};
        boolean found = false;
        int runningSolt = 0;
        int type = 1;
        // 初始化DarkSolt 跟 LightSolt的值
        if (runningLightSlot == 0 && runningDarkSlot == 0) {
            runningLightSlot = 9999;
            runningDarkSlot = 1;
        }
        // 判斷執行哪個狀態
        for (int light : lightSkills) {
            if (skillid == light && runningDark == 0) {
                runningLight = 1;
            }
        }
        for (int dark : darkSkills) {
            if (skillid == dark && runningLight == 0) {
                runningDark = 1;
            }
        }
        System.out.println("執行handleLuminous:黑:" + runningDark + " 光:" + runningLight);
        if (runningLight == 1) {
            for (int light : lightSkills) {
                if (skillid == light) {
                    runningLightSlot -= 277;
                    if (runningLightSlot <= 0) {
                        setLuminousMode(skillid);
                        runningLightSlot = 9999;
                        runningLight = 0;
                        runningDark = 1;
                        runningSolt = 1;
                    } else {
                        runningSolt = runningLightSlot;
                        setLuminousMode(skillid);
                    }
                    type = 1;
                    found = true;
                }
            }
        }
        if (runningDark == 1) {
            for (int dark : darkSkills) {
                if (skillid == dark) {
                    if (runningDarkSlot >= 1000) {
                        runningDarkSlot += 416;
                    } else {
                        runningDarkSlot += 76;
                    }
                    if (runningDarkSlot > 10000) {
                        setLuminousMode(skillid);
                        runningDarkSlot = 1;
                        runningLight = 1;
                        runningDark = 0;
                        runningSolt = 9999;
                    } else {
                        runningSolt = runningDarkSlot;
                        setLuminousMode(skillid);
                    }
                    type = 2;
                    found = true;
                }
            }
        }
        if (!found) {
            return;
        }
        System.out.println(runningLightSlot + "::" + runningDarkSlot + "::" + runningSolt);
        client.getSession().write(LuminousPacket.updateLuminousGauge(runningSolt, type));
    }

    public void resetRunningStack() {
        this.runningStack = 0;
        this.runningDark = 0;
        this.runningDarkSlot = 0;
        this.runningLight = 0;
        this.runningLightSlot = 0;
    }

    public int getRunningStack() {
        return this.runningStack;
    }

    public void addRunningStack(int s) {
        runningStack += s;
    }

    public int getLightGauge() {
        return runningLightSlot;
    }

    public int getDarkGauge() {
        return runningDarkSlot;
    }

    public int getLuminousState() {
        return luminousState;
    }

    public void setLuminousState(int luminousState) {
        this.luminousState = luminousState;
    }

    public void setCardStack(byte amount) {
        this.cardStack = amount;
    }

    public byte getCardStack() {
        return this.cardStack;
    }

    public void applyBlackBlessingBuff(int combos) {
        if ((combos == -1) && (this.runningBless == 0)) {
            //combos = 0;
            return;
        }
        Skill skill = SkillFactory.getSkill(27100003);
        int lvl = getTotalSkillLevel(27100003);
        if (lvl > 0) {
            runningBless = ((byte) (runningBless + combos));
            if (runningBless > 3) {
                runningBless = 3;
            }
            if (runningBless == 0) {
                if (getBuffedValue(MapleBuffStat.BLACK_BLESSING) != null) {
                    cancelBuffStats(new MapleBuffStat[]{MapleBuffStat.BLACK_BLESSING});
                }
            } else {
                skill.getEffect(lvl).applyBlackBlessingBuff(this, runningBless);
            }
        }
    }

    public short getXenonSurplus() {
        return xenonSurplus;
    }

    public void setXenonSurplus(short amount) {
        int maxSupply = level >= 100 ? 20 : level >= 60 ? 15 : level >= 30 ? 10 : 5;
        if (xenonSurplus + amount > maxSupply) {
            this.xenonSurplus = (short) maxSupply;
            updateXenonSurplus(xenonSurplus);
            return;
        }
        this.xenonSurplus = amount;
        updateXenonSurplus(xenonSurplus);
    }

    public void gainXenonSurplus(short amount) {
        int maxSupply = level >= 100 ? 20 : level >= 60 ? 15 : level >= 30 ? 10 : 5;
        if (xenonSurplus + amount > maxSupply) {
            this.xenonSurplus = (short) maxSupply;
            updateXenonSurplus(xenonSurplus);
            return;
        }
        this.xenonSurplus += amount;
        updateXenonSurplus(xenonSurplus);
    }

    public void updateXenonSurplus(short amount) {
        int maxSupply = level >= 100 ? 20 : level >= 60 ? 15 : level >= 30 ? 10 : 5;
        if (amount > maxSupply) {
            amount = (short) maxSupply;
        }
        client.getSession().write(XenonPacket.giveXenonSupply(amount));
    }

    public final void startXenonSupply() {
        BuffTimer tMan = BuffTimer.getInstance();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                int maxSupply = level >= 100 ? 20 : level >= 60 ? 15 : level >= 30 ? 10 : 5;
                if (maxSupply > getXenonSurplus()) {
                    gainXenonSurplus((short) 1);
                }
            }
        };
        if (client.isLoggedIn()) {
            XenonSupplyTask = tMan.register(r, 4000);
        }
    }

    public short getExceed() {
        return exceed;
    }

    public void setExceed(short amount) {
        this.exceed = amount;
    }

    public void gainExceed(short amount) {
        this.exceed += amount;
        updateExceed(exceed);
    }

    public void updateExceed(short amount) {
        client.getSession().write(AvengerPacket.giveExceed(amount));
    }

    public void handleExceedAttack(int skill) {
        long now = System.currentTimeMillis();
        if (lastExceedTime + 15000 < now) {
            exceedAttack = 0;
            lastExceedTime = now;
        }
        client.getSession().write(AvengerPacket.giveExceedAttack(skill, ++exceedAttack));
    }

    public MapleCoreAura getCoreAura() {
        return coreAura;
    }

    public void changeWarriorStance(final int skillid) {
        if (skillid == 11101022) { // 沉月
//            dispelBuff(11111022); 
            List<MapleBuffStat> statups = new LinkedList();
            statups.add(MapleBuffStat.INDIE_CR_R);
            statups.add(MapleBuffStat.ATTACK_COUNT);
            statups.add(MapleBuffStat.SOLUNA_EFFECT);
//            client.getSession().write(BuffPacket.cancelBuff(statups));*/ 
//            client.getSession().write(JobPacket.DawnWarriorPacket.giveMoonfallStance(getSkillLevel(skillid))); 
            SkillFactory.getSkill(skillid).getEffect(1).applyTo(this);
        } else if (skillid == 11111022) { // 旭日
//            dispelBuff(11101022); 
            System.out.println("Start of buff");
            List<MapleBuffStat> statups = new LinkedList();
            statups.add(MapleBuffStat.INDIE_BOOSTER);
            System.out.println("ATT Speed");
            statups.add(MapleBuffStat.INDIE_DAM_R);
            System.out.println("DMG Perc");
            statups.add(MapleBuffStat.SOLUNA_EFFECT);
            System.out.println("WAR Stance");
//            client.getSession().write(BuffPacket.cancelBuff(statups));*/ 
//            client.getSession().write(JobPacket.DawnWarriorPacket.giveSunriseStance(getSkillLevel(skillid))); 
            SkillFactory.getSkill(skillid).getEffect(1).applyTo(this);
        } else if (skillid == 11121005) { // 雙重力量
            //equinox 
        } else if (skillid == 11121011) { // 雙重力量（沉月）
            dispelBuff(11101022);
//            client.getSession().write(DawnWarriorPacket.giveEquinox_Moon(getSkillLevel(skillid), Integer.MAX_VALUE)); 
            SkillFactory.getSkill(skillid).getEffect(1).applyTo(this);
        } else if (skillid == 11121012) { // 雙重力量（旭日）
            dispelBuff(11101022);
//            client.getSession().write(DawnWarriorPacket.giveEquinox_Sun(getSkillLevel(skillid), Integer.MAX_VALUE)); 
            SkillFactory.getSkill(skillid).getEffect(1).applyTo(this);
        }
    }

    public List<MaplePotionPot> getPotionPots() {
        return potionPots;
    }

    public int getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }

    public void updateDeathCount() {
        if (deathCount < 0) {
            deathCount = 0;
        }
        if (deathCount > 99) {
            deathCount = 99;
        }
//        client.getSession().write(EffectPacket.updateDeathCount(deathCount));
    }

    public void giveMiracleBlessing() {
        for (MapleCharacter chr : getMap().getCharactersThreadsafe()) {
            MapleItemInformationProvider.getInstance().getItemEffect(2023055).applyTo(chr);
        }
        getMap().broadcastMessage(CField.startMapEffect(name + " has received Double Miracle Time's Mysterious Blessing. Congratulations!", 2023055, true));
        World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(0x19, 0, name + " has received [Double Miracle Time's Miraculous Blessing]. Congratulations!", false));
    }

    public void checkForceShield() {
        final MapleItemInformationProvider li = MapleItemInformationProvider.getInstance();
        Equip equip;
        boolean potential = false;
        switch (job) {
            case 508://蒼龍俠客1轉
                equip = (Equip) li.getEquipById(1352820);
                break;
            case 572://蒼龍俠客4轉
                potential = true;
            case 570://蒼龍俠客2轉
            case 571://蒼龍俠客3轉
                equip = (Equip) li.getEquipById(1352821 + job % 10);
                break;
            case 3001://惡魔
                equip = (Equip) li.getEquipById(1099001);
                break;
            case 3100://惡魔殺手1轉
                equip = (Equip) li.getEquipById(1099000);
                break;
            case 3112://惡魔殺手4轉
                potential = true;
            case 3110://惡魔殺手2轉
            case 3111://惡魔殺手3轉
                equip = (Equip) li.getEquipById(1099001 + job % 10 + ((job % 100) / 10));
                break;
            case 3101://惡魔復仇者1轉
                equip = (Equip) li.getEquipById(1099006);
                break;
            case 3122://惡魔復仇者4轉
                potential = true;
            case 3120://惡魔復仇者2轉
            case 3121://惡魔復仇者3轉
                equip = (Equip) li.getEquipById(1099005 + job % 10 + ((job % 100) / 10));
                break;
            case 5112://米哈逸4轉
                potential = true;
            case 5100://米哈逸1轉
            case 5110://米哈逸2轉
            case 5111://米哈逸3轉
                equip = (Equip) li.getEquipById(1098000 + job % 10 + ((job % 100) / 10));
                break;
            case 6001://天使破壞者
                equip = (Equip) li.getEquipById(1352600);
                break;
            case 6512://天使破壞者4轉
                potential = true;
            case 6500://天使破壞者1轉
            case 6510://天使破壞者2轉
            case 6511://天使破壞者3轉
                equip = (Equip) li.getEquipById(1352601 + job % 10 + ((job % 100) / 10));
                break;
            case 6000://凱撒
                equip = (Equip) li.getEquipById(1352500);
                break;
            case 6112://凱撒4轉
                potential = true;
            case 6100://凱撒1轉
            case 6110://凱撒2轉
            case 6111://凱撒3轉
                equip = (Equip) li.getEquipById(1352500 + job % 10 + ((job % 100) / 10));
                break;
            case 3002://傑諾
                equip = (Equip) li.getEquipById(1353000);
                break;
            case 3612://傑諾4轉
                potential = true;
            case 3600://傑諾1轉
            case 3610://傑諾2轉
            case 3611://傑諾3轉
                equip = (Equip) li.getEquipById(1353001 + job % 10 + ((job % 100) / 10));
                break;
            default:
                equip = null;
        }
        if (equip != null) {
            if (potential && equip.getState(false) == 0) {
                equip.resetPotential(false);
            }
            equip.setPosition((short) -10);
            equip.setQuantity((short) 1);
            equip.setGMLog("轉職更變道具獲得, 時間 " + FileoutputUtil.CurrentReadable_Time());
            forceReAddItem_NoUpdate(equip, MapleInventoryType.EQUIPPED);
            client.getSession().write(InventoryPacket.updateEquippedItem(this, equip, (short) -10));
            equipChanged();
        }
    }

    public void checkZeroWeapon() {
        if (level < 100) {
            return;
        }
        int lazuli = getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId();
        int lapis = getInventory(MapleInventoryType.EQUIPPED).getItem((short) -10).getItemId();
        if (lazuli == getZeroWeapon(false) && lapis == getZeroWeapon(true)) {
            return;
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        for (int i = 0; i < 2; i++) {
            int itemId = i == 0 ? getZeroWeapon(false) : getZeroWeapon(true);
            Equip equip = (Equip) ii.getEquipById(itemId);
            equip.setPosition((short) (i == 0 ? -11 : -10));
            equip.setQuantity((short) 1);
            equip.setGMLog("神之子升級武器, 時間:" + FileoutputUtil.CurrentReadable_Time());
            forceReAddItem_NoUpdate(equip, MapleInventoryType.EQUIPPED);
            client.getSession().write(InventoryPacket.updateEquippedItem(this, equip, (short) (i == 0 ? -11 : -10)));
        }
        equipChanged();
    }

    public int getZeroWeapon(boolean lapis) {
        if (level < 100) {
            return lapis ? 1562000 : 1572000;
        }
        int weapon = lapis ? 1562001 : 1572001;
        if (level >= 100 && level < 160) {
            weapon += (level % 100) / 10;
        } else if (level >= 160 && level < 170) {
            weapon += 5;
        } else if (level >= 170) {
            weapon += 6;
        }
        return weapon;
    }

    public void zeroChange(boolean beta) {
        getMap().broadcastMessage(this, CField.updateCharLook(this, beta), getPosition());
    }

    public void checkZeroTranscendent() {
        int skill = -1;
        switch (level / 10) {
            case 10:
                skill = 100000267;
                break;
            case 11:
                skill = 100001261;
                break;
            case 12:
                skill = 100001274;
                break;
            case 14:
                skill = 100001272;
                break;
            case 17:
                skill = 100001283;
                break;
            case 20:
                skill = 100001005;
                break;
        }
        if (skill == -1) {
            return;
        }
        Skill skil = SkillFactory.getSkill(skill);
        if (skil != null) {
            changeSkillLevel_Skip(skil, 1, (byte) 1);
        }
    }

    public boolean hasEntered(String script) {
        for (int mapId : entered.keySet()) {
            if (entered.get(mapId).equals(script)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasEntered(String script, int mapId) {
        if (entered.containsKey(mapId)) {
            if (entered.get(mapId).equals(script)) {
                return true;
            }
        }
        return false;
    }

    public void enteredScript(String script, int mapid) {
        if (!entered.containsKey(mapid)) {
            entered.put(mapid, script);
        }
    }

    public void femaleMihile() {
        setGender((byte) 1);
        setFace(21158); //Elegant Lady face sounds nice
        setHair(34773); //Aria hair fits there most
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        Equip equip;
        int[][] items = new int[][]{{1051355, -5}, {1072833, -6}};
        for (int[] item : items) {
            equip = (Equip) ii.getEquipById(item[0]);
            equip.setPosition((short) item[1]);
            equip.setQuantity((short) 1);
            equip.setGMLog("米哈逸變性,時間:" + FileoutputUtil.CurrentReadable_Time());
            forceReAddItem_NoUpdate(equip, MapleInventoryType.EQUIPPED);
            client.getSession().write(InventoryPacket.updateEquippedItem(this, equip, (short) item[1]));
        }
        updateSingleStat(MapleStat.FACE, this.face);
        updateSingleStat(MapleStat.HAIR, this.hair);
        equipChanged();
    }

    public void updateSkills() {
        final Map<Skill, SkillEntry> sa = new HashMap<>();
        sa.put(SkillFactory.getSkill(110001506), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(110001514), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(110001510), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(110001500), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(110001502), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(112100000), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(110001501), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(110001501), new SkillEntry((byte) 1, (byte) 1, -1));
        sa.put(SkillFactory.getSkill(112000000), new SkillEntry((byte) 1, (byte) 1, -1));
        changeKeybinding(42, (byte) 1, 110001510);
        client.getSession().write(CField.getKeymap(getKeyLayout(), this));
        client.getPlayer().changeSkillsLevel(sa);
    }

    public boolean isHiddenChatCanSee() {
        return hiddenChatCanSee;
    }

    public void setHiddenChatCanSee(boolean can) {
        hiddenChatCanSee = can;
    }

    public static MapleCharacter getOnlineCharacterById(int cid) {
        MapleCharacter chr = null;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            chr = cs.getPlayerStorage().getCharacterById(cid);
            if (chr != null) {
                break;
            }
        }
        return chr;
    }

    public static MapleCharacter getCharacterById(int cid) {
        MapleCharacter chr = getOnlineCharacterById(cid);
        return chr == null ? MapleCharacter.loadCharFromDB(cid, new MapleClient(null, null, new tools.MockIOSession()), true) : chr;
    }

    public void monsterMultiKill() {
        if (killMonsterExps.size() > 2) {
            long multiKillExp = 0;
            for (int exps : killMonsterExps) {
                multiKillExp += (long) Math.ceil((double) ((double) exps * (Math.min((double) monsterCombo, 60.0D) + 1.0D) * 0.5D / 100.0D));
            }
            gainExp((int) multiKillExp, false, false, false);
            client.getSession().write(InfoPacket.GainEXP_MultiKill(multiKillExp, Math.min(killMonsterExps.size(), 10)));
        }
        killMonsterExps.clear();
    }

    public int getFreeChronosphere() {
        return 7 - getDaysPQLog("免費強化任意門", 7);
    }

    public int getChronosphere() {
        String num = getQuestNAdd(MapleQuest.getInstance(99997)).getCustomData();
        if (num == null) {
            num = "0";
        }
        return Integer.parseInt(num);
    }

    public void setChronosphere(int mount) {
        getQuestNAdd(MapleQuest.getInstance(99997)).setCustomData(String.valueOf(mount));
    }

    public int getPQLog(String pqName) {
        return getPQLog(pqName, 0);
    }

    public int getPQLog(String pqName, int type) {
        return getPQLog(pqName, type, 1);
    }

    public int getDaysPQLog(String pqName, int days) {
        return getDaysPQLog(pqName, 0, days);
    }

    public int getDaysPQLog(String pqName, int type, int days) {
        return getPQLog(pqName, type, days);
    }

    public int getPQLog(String pqName, int type, int days) {
        try {
            int count = 0;

            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM pqlog WHERE characterid = ? AND pqname = ?");
            ps.setInt(1, this.id);
            ps.setString(2, pqName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("count");
                Timestamp bossTime = rs.getTimestamp("time");
                rs.close();
                ps.close();
                if (type == 0) {
                    Calendar calNow = Calendar.getInstance();
                    Calendar sqlcal = Calendar.getInstance();
                    if (bossTime != null) {
                        sqlcal.setTimeInMillis(bossTime.getTime());
                        sqlcal.add(Calendar.DAY_OF_YEAR, +days);
                    }
                    int day;
                    if (calNow.get(Calendar.YEAR) - sqlcal.get(Calendar.YEAR) > 1) {
                        day = 0;
                    } else if (calNow.get(Calendar.YEAR) - sqlcal.get(Calendar.YEAR) >= 0) {
                        if (calNow.get(Calendar.YEAR) - sqlcal.get(Calendar.YEAR) > 0) {
                            sqlcal.add(Calendar.YEAR, 1);
                        }
                        day = calNow.get(Calendar.DAY_OF_YEAR) - sqlcal.get(Calendar.DAY_OF_YEAR);
                    } else {
                        day = -1;
                    }
                    if (day >= 0) {
                        count = 0;
                        ps = con.prepareStatement("UPDATE pqlog SET count = 0, time = CURRENT_TIMESTAMP() WHERE characterid = ? AND pqname = ?");
                        ps.setInt(1, this.id);
                        ps.setString(2, pqName);
                        ps.executeUpdate();
                        ps.close();
                    }
                }
            } else {
                try (PreparedStatement psu = con.prepareStatement("INSERT INTO pqlog (characterid, pqname, count, type) VALUES (?, ?, ?, ?)")) {
                    psu.setInt(1, this.id);
                    psu.setString(2, pqName);
                    psu.setInt(3, 0);
                    psu.setInt(4, type);
                    psu.executeUpdate();
                    ps.close();
                }
            }
            rs.close();
            ps.close();
            return count;
        } catch (SQLException Ex) {
            System.err.println("Error while get pqlog: " + Ex);
        }
        return -1;
    }

    public void setPQLog(String pqName) {
        setPQLog(pqName, 0);
    }

    public void setPQLog(String pqName, int type) {
        setPQLog(pqName, type, 1);
    }

    public void setPQLog(String pqName, int type, int count) {
        int pqCount = getPQLog(pqName, type);
        try {
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE pqlog SET count = ?, type = ?, time = CURRENT_TIMESTAMP() WHERE characterid = ? AND pqname = ?")) {
                ps.setInt(1, pqCount + count);
                ps.setInt(2, type);
                ps.setInt(3, this.id);
                ps.setString(4, pqName);
                ps.executeUpdate();
                ps.close();
            }
        } catch (SQLException Ex) {
            System.err.println("Error while set pqlog: " + Ex);
        }
    }

    public void resetPQLog(String pqName) {
        resetPQLog(pqName, 0);
    }

    public void resetPQLog(String pqName, int type) {
        try {
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE pqlog SET count = ?, type = ?, time = CURRENT_TIMESTAMP() WHERE characterid = ? AND pqname = ?")) {
                ps.setInt(1, 0);
                ps.setInt(2, type);
                ps.setInt(3, this.id);
                ps.setString(4, pqName);
                ps.executeUpdate();
                ps.close();
            }
        } catch (SQLException Ex) {
            System.err.println("Error while reset pqlog: " + Ex);
        }
    }

    public void iNeedSystemProcess() {
        setLastCheckProcess(System.currentTimeMillis());
        this.getClient().getSession().write(CWvsContext.SystemProcess());
    }

    public long getLastCheckProcess() {
        return lastCheckProcess;
    }

    public void setLastCheckProcess(long lastCheckProcess) {
        this.lastCheckProcess = lastCheckProcess;
    }

    public List<MapleProcess> getProcess() {
        return Process;
    }

    public boolean useCube(int itemId, Equip eq) {
        return useCube(itemId, eq, 0);
    }

    public boolean useCube(int itemId, Equip eq, int toLock) {
        if (getInventory(MapleInventoryType.USE).getNumFreeSlot() >= 1) {
            int stateRate = 4;//chr.getClient().getChannelServer().getStateRate();
            int cubeTpye = ItemConstants.方塊.getCubeType(itemId);
            boolean isBonus = ItemConstants.方塊.CubeType.附加潛能.check(cubeTpye);
            if (!ItemConstants.方塊.canUseCube(eq, itemId)) {
                dropMessage(5, "在這道具無法使用。");
                return false;
            }
            if (eq.getState(isBonus) >= 17 && eq.getState(isBonus) <= 20) {
                if (isShowInfo() && eq.getState(isBonus) < 20) {
                    dropMessage(-6, "伺服器管理員潛能等級提升成功率100%");
                }
                eq.renewPotential(isAdmin() ? 99 : stateRate, cubeTpye, toLock);
                forceReAddItem_NoUpdate(eq, MapleInventoryType.EQUIP);

                if (ItemConstants.方塊.getCubeFragment(itemId) > 0) {
                    MapleInventoryManipulator.addById(getClient(), ItemConstants.方塊.getCubeFragment(itemId), (short) 1, new StringBuilder().append("使用方塊獲得").append(FileoutputUtil.CurrentReadable_Date()).toString());
                }
                return true;
            } else {
                dropMessage(5, "請確認您要重置的道具具有潛能屬性。");
            }
        }
        return false;
    }

    public boolean changeFace(int color) {
        int f = 0;
        if (face % 1000 < 100) {
            f = face + color;
        } else if ((face % 1000 >= 100) && (face % 1000 < 200)) {
            f = face - 100 + color;
        } else if ((face % 1000 >= 200) && (face % 1000 < 300)) {
            f = face - 200 + color;
        } else if ((face % 1000 >= 300) && (face % 1000 < 400)) {
            f = face - 300 + color;
        } else if ((face % 1000 >= 400) && (face % 1000 < 500)) {
            f = face - 400 + color;
        } else if ((face % 1000 >= 500) && (face % 1000 < 600)) {
            f = face - 500 + color;
        } else if ((face % 1000 >= 600) && (face % 1000 < 700)) {
            f = face - 600 + color;
        } else if ((face % 1000 >= 700) && (face % 1000 < 800)) {
            f = face - 700 + color;
        }
        if (!MapleItemInformationProvider.getInstance().faceExists(f)) {
            return false;
        }
        face = f;
        updateSingleStat(MapleStat.FACE, face);
        equipChanged();
        return true;
    }

    public boolean isEquippedSoulWeapon() {
        Equip weapon = (Equip) getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
        if (weapon == null) {
            return false;
        }
        return weapon.getSoulEnchanter() != 0;
    }

    public boolean isSoulWeapon(Equip equip) {
        if (equip == null) {
            return false;
        }
        return equip.getSoulEnchanter() != 0;
    }

    public void equipSoulWeapon(Equip equip) {
        changeSkillLevel(new Skill(getEquippedSoulSkill()), (byte) -1, (byte) 0);
        changeSkillLevel(new Skill(equip.getSoulSkill()), (byte) 1, (byte) 1);
        setSoulCount((short) 0);
        // getClient().getSession().write(MainPacketCreator.getInventoryFull());
        getClient().getSession().write(CWvsContext.BuffPacket.giveSoulGauge(getSoulCount(), equip.getSoulSkill()));
    }

    public void unequipSoulWeapon(Equip equip) {
        changeSkillLevel(new Skill(equip.getSoulSkill()), (byte) -1, (byte) 0);
        setSoulCount((short) 0);
        //getClient().getSession().write(MainPacketCreator.getInventoryFull());
        getClient().getSession().write(CWvsContext.BuffPacket.cancelSoulGauge());
        getMap().broadcastMessage(CWvsContext.BuffPacket.cancelForeignSoulEffect(getId()));
        //message("장착해제");
    }

    public void checkSoulState(boolean useskill) {
        int skillid = getEquippedSoulSkill();
        MapleStatEffect skill = SkillFactory.getSkill(skillid).getEffect(getSkillLevel(skillid));
        long cooldown = getCooldownLimit(skillid);
        if (useskill) {
            if (getSoulCount() >= skill.getSoulMPCon()) {
                setSoulCount((short) (getSoulCount() - skill.getSoulMPCon()));
                //if (cooldown > 0) {
                getClient().getSession().write(CWvsContext.BuffPacket.giveSoulEffect(0));
                getMap().broadcastMessage(CWvsContext.BuffPacket.cancelForeignSoulEffect(getId()));
                getClient().getSession().write(CWvsContext.InventoryPacket.getInventoryFull());
                //}
            }

        } else {
            if (getSoulCount() >= skill.getSoulMPCon()) {
                if (cooldown <= 0) {
                    getClient().getSession().write(CWvsContext.BuffPacket.giveSoulEffect(skillid));
                    getMap().broadcastMessage(this, CWvsContext.BuffPacket.giveForeignSoulEffect(getId(), skillid), false);
                }
            }
        }
    }

    public short getSoulCount() {
        return soulcount;
    }

    public void setSoulCount(short soulcount) {
        this.soulcount = soulcount > 1000 ? 1000 : soulcount;
    }

    public void addSoulCount() {
        if (soulcount < 1000) {
            this.soulcount++;
        }
    }

    public short addgetSoulCount() {
        addSoulCount();
        return getSoulCount();
    }

    public int getEquippedSoulSkill() {
        Equip weapon = (Equip) getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11);
        return weapon.getSoulSkill();//weapon.getSoulSkill();
    }

    public int getSoulSkillMpCon() {
        int skillid = getEquippedSoulSkill();
        MapleStatEffect skill = SkillFactory.getSkill(skillid).getEffect(getSkillLevel(skillid));
        return skill.getSoulMPCon();
    }
    
    public int getWeaponPoint() {
        return weaponPoint;
    }

    public void gainWeaponPoint(int wp) {
        this.weaponPoint += wp;
    }

    public void addWeaponPoint(int wp) {
        gainWeaponPoint(wp);
        getClient().getSession().write(CField.ZeroPacket.gainWeaponPoint(wp));
        getClient().getSession().write(CField.ZeroPacket.updateWeaponPoint(getWeaponPoint()));
    }

    public long getCooldownLimit(int skillid) {
        for (MapleCoolDownValueHolder mcdvh : getAllCooldowns()) {
            if (mcdvh.skillId == skillid) {
                return System.currentTimeMillis() - mcdvh.startTime;
            }
        }
        return 0;
    }

    public List<MapleCoolDownValueHolder> getAllCooldowns() {
        List<MapleCoolDownValueHolder> ret = new ArrayList<>();
        for (MapleCoolDownValueHolder mcdvh : coolDowns.values()) {
            ret.add(new MapleCoolDownValueHolder(mcdvh.skillId, mcdvh.startTime, mcdvh.length));
        }
        return ret;
    }

    public boolean hasMapScript() {
        return mapScript.getLeft() != MapScriptType.UNK;
    }

    public Pair<MapScriptType, String> getMapScript() {
        return mapScript;
    }

    public void updateMapScript(MapScriptType type, String script) {
        mapScript.left = type;
        mapScript.right = script;
    }

    public boolean cheakSkipOnceChat() {
        skipOnceChat = !skipOnceChat;
        return skipOnceChat;
    }
    
    public Collection<MapleMonster> getControlledMonsters() {
        return Collections.unmodifiableCollection(controlled);
    }
    
    public List<Integer> getEffectSwitch() {
        List<Integer> posList = new ArrayList();
        try {
            PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM effectswitch WHERE `characterid` = ?");
            ps.setInt(1, getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                posList.add(rs.getInt("pos"));
            }
            ps.close();
            rs.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ItemLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return posList;
    }
    
    public void updateEffectSwitch(int pos) {
        PreparedStatement ps = null;
        Connection con = DatabaseConnection.getConnection();
        try {
            ps = con.prepareStatement("SELECT * FROM effectswitch WHERE `characterid` = ? AND `pos` = ?");
            ps.setInt(1, getId());
            ps.setInt(2, pos);
            ResultSet rs = ps.executeQuery();
            if (rs.first()) {
                ps.close();
                rs.close();
                ps = con.prepareStatement("DELETE FROM effectswitch WHERE pos = ?");
                ps.setInt(1, pos);
                ps.executeUpdate();
            } else {
                ps.close();
                rs.close();
                ps = con.prepareStatement("INSERT INTO effectswitch (characterid, pos) VALUES (?, ?)");
                ps.setInt(1, getId());
                ps.setInt(2, pos);
                ps.execute();
            }
            ps.close();
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(ItemLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
