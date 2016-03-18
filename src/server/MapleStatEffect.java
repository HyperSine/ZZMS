package server;

import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleCoolDownValueHolder;
import client.MapleDisease;
import client.MapleJob;
import client.MapleStat;
import client.MapleTrait.MapleTraitType;
import client.MonsterStatus;
import client.MonsterStatusEffect;
import client.PlayerStats;
import client.Skill;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.ItemConstants;
import handling.channel.ChannelServer;
import handling.world.MaplePartyCharacter;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;
import provider.MapleData;
import provider.MapleDataTool;
import provider.wz.MapleDataType;
import server.MapleCarnivalFactory.MCSkill;
import server.Timer.BuffTimer;
import server.buffs.BuffClassFetcher;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleDoor;
import server.maps.MapleExtractor;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.MapleMist;
import server.maps.MapleSummon;
import server.maps.MechDoor;
import server.maps.SummonMovementType;
import tools.CaltechEval;
import tools.FileoutputUtil;
import tools.Pair;
import tools.Triple;
import tools.packet.CField;
import tools.packet.CField.EffectPacket;
import tools.packet.CWvsContext;
import tools.packet.CWvsContext.BuffPacket;
import tools.packet.JobPacket;
import tools.packet.JobPacket.PhantomPacket;
import tools.packet.SkillPacket;
import tools.packet.provider.SpecialEffectType;

public class MapleStatEffect implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;
    public Map<MapleStatInfo, Integer> info;
    private Map<MapleTraitType, Integer> traits;
    private boolean overTime, skill, partyBuff = true;
    public EnumMap<MapleBuffStat, Integer> statups;
    private ArrayList<Pair<Integer, Integer>> availableMap;
    public EnumMap<MonsterStatus, Integer> monsterStatus;
    private Point lt, rb;
    private byte level;
//    private List<Pair<Integer, Integer>> randomMorph;
    private List<MapleDisease> cureDebuffs;
    private List<Integer> petsCanConsume, familiars, randomPickup;
    private List<Triple<Integer, Integer, Integer>> rewardItem;
    private byte expR, familiarTarget, recipeUseCount, recipeValidDay, reqSkillLevel, slotCount, effectedOnAlly, effectedOnEnemy, type, preventslip, immortal, bs;
    private short ignoreMob, mesoR, thaw, fatigueChange, lifeId, imhp, immp, inflation, useLevel, indiePdd, indieMdd, incPVPdamage, mobSkill, mobSkillLevel;
    public double hpR, mpR;
    private int sourceid, recipe, moveTo, moneyCon, morphId = 0, expinc, exp, consumeOnPickup, charColor, interval, rewardMeso, totalprob, cosmetic;
    private int weapon = 0;
    private int expBuff, itemup, mesoup, cashup, berserk, illusion, booster, berserk2, cp, nuffSkill, combo;

    public static MapleStatEffect loadSkillEffectFromData(final MapleData source, final int skillid, final boolean overtime, final int level, final String variables) {
        return loadFromData(source, skillid, true, overtime, level, variables);
    }

    public static MapleStatEffect loadItemEffectFromData(final MapleData source, final int itemid) {
        return loadFromData(source, itemid, false, false, 1, null);
    }

    private static void addBuffStatPairToListIfNotZero(final EnumMap<MapleBuffStat, Integer> list, final MapleBuffStat buffstat, final Integer val) {
        if (val != 0) {
            list.put(buffstat, val);
        }
    }

    private static int parseEval(String path, MapleData source, int def, String variables, int level) {
        if (variables == null) {
            return MapleDataTool.getIntConvert(path, source, def);
        } else {
            final MapleData dd = source.getChildByPath(path);
            if (dd == null) {
                return def;
            }
            if (dd.getType() != MapleDataType.STRING) {
                return MapleDataTool.getIntConvert(path, source, def);
            }
            String dddd = MapleDataTool.getString(dd).replace(variables, String.valueOf(level));
            if (dddd.substring(0, 1).equals("-")) { //-30+3*x
                if (dddd.substring(1, 2).equals("u") || dddd.substring(1, 2).equals("d")) { //-u(x/2)
                    dddd = "n(" + dddd.substring(1, dddd.length()) + ")"; //n(u(x/2))
                } else {
                    dddd = "n" + dddd.substring(1, dddd.length()); //n30+3*x
                }
            } else if (dddd.substring(0, 1).equals("=")) { //lol nexon and their mistakes
                dddd = dddd.substring(1, dddd.length());
            } else if (dddd.contains("y")) { // AngelicBuster Exception
                dddd = "0";
            }
            return (int) (new CaltechEval(dddd).evaluate());
        }
    }

    private static MapleStatEffect loadFromData(final MapleData source, final int sourceid, final boolean skill, final boolean overTime, final int level, final String variables) {
        final MapleStatEffect ret = new MapleStatEffect();
        ret.sourceid = sourceid;
        ret.skill = skill;
        ret.level = (byte) level;
        if (source == null) {
            return ret;
        }
        ret.info = new EnumMap<>(MapleStatInfo.class);
        for (final MapleStatInfo i : MapleStatInfo.values()) {
            if (i.isSpecial()) {
                ret.info.put(i, parseEval(i.name().substring(0, i.name().length() - 1), source, i.getDefault(), variables, level));
            } else {
                ret.info.put(i, parseEval(i.name(), source, i.getDefault(), variables, level));
            }
        }
        ret.hpR = parseEval("hpR", source, 0, variables, level) / 100.0;
        ret.mpR = parseEval("mpR", source, 0, variables, level) / 100.0;
        ret.ignoreMob = (short) parseEval("ignoreMobpdpR", source, 0, variables, level);
        ret.thaw = (short) parseEval("thaw", source, 0, variables, level);
        ret.interval = parseEval("interval", source, 0, variables, level);
        ret.expinc = parseEval("expinc", source, 0, variables, level);
        ret.exp = parseEval("exp", source, 0, variables, level);
        ret.morphId = parseEval("morph", source, 0, variables, level);
        ret.cp = parseEval("cp", source, 0, variables, level);
        ret.cosmetic = parseEval("cosmetic", source, 0, variables, level);
        ret.slotCount = (byte) parseEval("slotCount", source, 0, variables, level);
        ret.preventslip = (byte) parseEval("preventslip", source, 0, variables, level);
        ret.useLevel = (short) parseEval("useLevel", source, 0, variables, level);
        ret.nuffSkill = parseEval("nuffSkill", source, 0, variables, level);
        ret.familiarTarget = (byte) (parseEval("familiarPassiveSkillTarget", source, 0, variables, level) + 1);
        ret.immortal = (byte) parseEval("immortal", source, 0, variables, level);
        ret.type = (byte) parseEval("type", source, 0, variables, level);
        ret.bs = (byte) parseEval("bs", source, 0, variables, level);
        ret.indiePdd = (short) parseEval("indiePdd", source, 0, variables, level);
        ret.indieMdd = (short) parseEval("indieMdd", source, 0, variables, level);
        ret.expBuff = parseEval("expBuff", source, 0, variables, level);
        ret.cashup = parseEval("cashBuff", source, 0, variables, level);
        ret.itemup = parseEval("itemupbyitem", source, 0, variables, level);
        ret.mesoup = parseEval("mesoupbyitem", source, 0, variables, level);
        ret.berserk = parseEval("berserk", source, 0, variables, level);
        ret.berserk2 = parseEval("berserk2", source, 0, variables, level);
        ret.booster = parseEval("booster", source, 0, variables, level);
        ret.lifeId = (short) parseEval("lifeId", source, 0, variables, level);
        ret.inflation = (short) parseEval("inflation", source, 0, variables, level);
        ret.imhp = (short) parseEval("imhp", source, 0, variables, level);
        ret.immp = (short) parseEval("immp", source, 0, variables, level);
        ret.illusion = parseEval("illusion", source, 0, variables, level);
        ret.consumeOnPickup = parseEval("consumeOnPickup", source, 0, variables, level);
        if (ret.consumeOnPickup == 1) {
            if (parseEval("party", source, 0, variables, level) > 0) {
                ret.consumeOnPickup = 2;
            }
        }
        ret.recipe = parseEval("recipe", source, 0, variables, level);
        ret.recipeUseCount = (byte) parseEval("recipeUseCount", source, 0, variables, level);
        ret.recipeValidDay = (byte) parseEval("recipeValidDay", source, 0, variables, level);
        ret.reqSkillLevel = (byte) parseEval("reqSkillLevel", source, 0, variables, level);
        ret.effectedOnAlly = (byte) parseEval("effectedOnAlly", source, 0, variables, level);
        ret.effectedOnEnemy = (byte) parseEval("effectedOnEnemy", source, 0, variables, level);
        ret.incPVPdamage = (short) parseEval("incPVPDamage", source, 0, variables, level);
        ret.moneyCon = parseEval("moneyCon", source, 0, variables, level);
        ret.moveTo = parseEval("moveTo", source, -1, variables, level);

        ret.charColor = 0;
        String cColor = MapleDataTool.getString("charColor", source, null);
        if (cColor != null) {
            ret.charColor |= Integer.parseInt("0x" + cColor.substring(0, 2));
            ret.charColor |= Integer.parseInt("0x" + cColor.substring(2, 4) + "00");
            ret.charColor |= Integer.parseInt("0x" + cColor.substring(4, 6) + "0000");
            ret.charColor |= Integer.parseInt("0x" + cColor.substring(6, 8) + "000000");
        }
        ret.traits = new EnumMap<>(MapleTraitType.class);
        for (MapleTraitType t : MapleTraitType.values()) {
            int expz = parseEval(t.name() + "EXP", source, 0, variables, level);
            if (expz != 0) {
                ret.traits.put(t, expz);
            }
        }
        List<MapleDisease> cure = new ArrayList<>(5);
        if (parseEval("poison", source, 0, variables, level) > 0) {
            cure.add(MapleDisease.POISON);
        }
        if (parseEval("seal", source, 0, variables, level) > 0) {
            cure.add(MapleDisease.SEAL);
        }
        if (parseEval("darkness", source, 0, variables, level) > 0) {
            cure.add(MapleDisease.DARKNESS);
        }
        if (parseEval("weakness", source, 0, variables, level) > 0) {
            cure.add(MapleDisease.WEAKEN);
        }
        if (parseEval("curse", source, 0, variables, level) > 0) {
            cure.add(MapleDisease.CURSE);
        }
        ret.cureDebuffs = cure;
        ret.petsCanConsume = new ArrayList<>();
        for (int i = 0; true; i++) {
            final int dd = parseEval(String.valueOf(i), source, 0, variables, level);
            if (dd > 0) {
                ret.petsCanConsume.add(dd);
            } else {
                break;
            }
        }
        final MapleData mdd = source.getChildByPath("0");
        if (mdd != null && mdd.getChildren().size() > 0) {
            ret.mobSkill = (short) parseEval("mobSkill", mdd, 0, variables, level);
            ret.mobSkillLevel = (short) parseEval("level", mdd, 0, variables, level);
        } else {
            ret.mobSkill = 0;
            ret.mobSkillLevel = 0;
        }
        final MapleData pd = source.getChildByPath("randomPickup");
        if (pd != null) {
            ret.randomPickup = new ArrayList<>();
            for (MapleData p : pd) {
                ret.randomPickup.add(MapleDataTool.getInt(p));
            }
        }
        final MapleData ltd = source.getChildByPath("lt");
        if (ltd != null) {
            ret.lt = (Point) ltd.getData();
            ret.rb = (Point) source.getChildByPath("rb").getData();
        }
        final MapleData ltc = source.getChildByPath("con");
        if (ltc != null) {
            ret.availableMap = new ArrayList<>();
            for (MapleData ltb : ltc) {
                ret.availableMap.add(new Pair<>(MapleDataTool.getInt("sMap", ltb, 0), MapleDataTool.getInt("eMap", ltb, 999999999)));
            }
        }
        final MapleData ltb = source.getChildByPath("familiar");
        if (ltb != null) {
            ret.fatigueChange = (short) (parseEval("incFatigue", ltb, 0, variables, level) - parseEval("decFatigue", ltb, 0, variables, level));
            ret.familiarTarget = (byte) parseEval("target", ltb, 0, variables, level);
            final MapleData lta = ltb.getChildByPath("targetList");
            if (lta != null) {
                ret.familiars = new ArrayList<Integer>();
                for (MapleData ltz : lta) {
                    ret.familiars.add(MapleDataTool.getInt(ltz, 0));
                }
            }
        } else {
            ret.fatigueChange = 0;
        }
        int totalprob = 0;
        final MapleData lta = source.getChildByPath("reward");
        if (lta != null) {
            ret.rewardMeso = parseEval("meso", lta, 0, variables, level);
            final MapleData ltz = lta.getChildByPath("case");
            if (ltz != null) {
                ret.rewardItem = new ArrayList<Triple<Integer, Integer, Integer>>();
                for (MapleData lty : ltz) {
                    ret.rewardItem.add(new Triple<Integer, Integer, Integer>(MapleDataTool.getInt("id", lty, 0), MapleDataTool.getInt("count", lty, 0), MapleDataTool.getInt("prop", lty, 0))); // todo: period (in minutes)
                    totalprob += MapleDataTool.getInt("prob", lty, 0);
                }
            }
        } else {
            ret.rewardMeso = 0;
        }
        ret.totalprob = totalprob;
        // start of server calculated stuffs
        if (ret.skill) {
            final int priceUnit = ret.info.get(MapleStatInfo.priceUnit); // Guild skills
            if (priceUnit > 0) {
                final int price = ret.info.get(MapleStatInfo.price);
                final int extendPrice = ret.info.get(MapleStatInfo.extendPrice);
                ret.info.put(MapleStatInfo.price, price * priceUnit);
                ret.info.put(MapleStatInfo.extendPrice, extendPrice * priceUnit);
            }
            switch (sourceid) {
                case 1100002:
                case 1200002:
                case 1300002:
                case 3100001:
                case 3200001:
                case 11101002:
                case 51100002:
                case 13101002:
                case 2111007:
                case 2211007:
                case 2311007:
                case 32111010:
                case 22161005:
                case 12111007:
                case 33100009:
                case 22150004:
                case 22181004: //All Final Attack
                case 1120013:
                case 3120008:
                case 23100006:
                case 23120012:
                    ret.info.put(MapleStatInfo.mobCount, 6);
                    break;
                case 35121005:
                case 35111004:
                case 35121013:
                    ret.info.put(MapleStatInfo.attackCount, 6);
                    ret.info.put(MapleStatInfo.bulletCount, 6);
                    break;
                case 24100003:
                case 24120002:
                    ret.info.put(MapleStatInfo.attackCount, 15);
                    break;
            }
            if (GameConstants.isNoDelaySkill(sourceid)) {
                ret.info.put(MapleStatInfo.mobCount, 6);
            }
        }
        if (!ret.skill && ret.info.get(MapleStatInfo.time) > -1) {
            ret.overTime = true;
        } else {
            ret.info.put(MapleStatInfo.time, (ret.info.get(MapleStatInfo.time) * 1000)); // items have their times stored in ms, of course
            ret.info.put(MapleStatInfo.subTime, (ret.info.get(MapleStatInfo.subTime) * 1000));
            ret.overTime = overTime || ret.isMorph() || ret.isPirateMorph() || ret.isFinalAttack() || ret.isAngel() || ret.getSummonMovementType() != null || ret.isSkillWithBuff();
        }
        ret.monsterStatus = new EnumMap<>(MonsterStatus.class);
        ret.statups = new EnumMap<>(MapleBuffStat.class);
        if (ret.overTime && ret.getSummonMovementType() == null && !ret.isEnergyCharge()) {
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.WATK, ret.info.get(MapleStatInfo.pad));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.WDEF, ret.info.get(MapleStatInfo.pdd));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.MATK, ret.info.get(MapleStatInfo.mad));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.MDEF, ret.info.get(MapleStatInfo.mdd));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.ACC, ret.info.get(MapleStatInfo.acc));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.AVOID, ret.info.get(MapleStatInfo.eva));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.SPEED, sourceid == 32120001 || sourceid == 32120014 || sourceid == 32101003 ? ret.info.get(MapleStatInfo.x) : ret.info.get(MapleStatInfo.speed));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.JUMP, ret.info.get(MapleStatInfo.jump));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.MAXHP, ret.info.get(MapleStatInfo.mhpR));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.MAXMP, ret.info.get(MapleStatInfo.mmpR));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.BOOSTER, ret.booster);
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.HP_LOSS_GUARD, Integer.valueOf(ret.thaw));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.EXPRATE, ret.expBuff); // EXP
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.ACASH_RATE, ret.cashup); // custom
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.DROP_RATE, GameConstants.getModifier(ret.sourceid, ret.itemup)); // defaults to 2x
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.MESO_RATE, GameConstants.getModifier(ret.sourceid, ret.mesoup)); // defaults to 2x
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.BERSERK_FURY, ret.berserk2);
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.ILLUSION, ret.illusion);
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.PYRAMID_PQ, ret.berserk);
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.ENHANCED_MAXHP, ret.info.get(MapleStatInfo.emhp));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.ENHANCED_MAXMP, ret.info.get(MapleStatInfo.emmp));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.ENHANCED_WATK, ret.info.get(MapleStatInfo.epad));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.ENHANCED_MATK, ret.info.get(MapleStatInfo.emad));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.ENHANCED_WDEF, ret.info.get(MapleStatInfo.epdd));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.ENHANCED_MDEF, ret.info.get(MapleStatInfo.emdd));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.GIANT_POTION, Integer.valueOf(ret.inflation));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.STR, ret.info.get(MapleStatInfo.str));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.DEX, ret.info.get(MapleStatInfo.dex));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INT, ret.info.get(MapleStatInfo.int_));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.LUK, ret.info.get(MapleStatInfo.luk));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INDIE_PAD, ret.info.get(MapleStatInfo.indiePad));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INDIE_MAD, ret.info.get(MapleStatInfo.indieMad));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INDIE_MAX_HP, Integer.valueOf(ret.imhp));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INDIE_MAX_MP, Integer.valueOf(ret.immp)); //same one? lol
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INDIE_MHP_R, ret.info.get(MapleStatInfo.indieMhpR));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INDIE_MMP_R, ret.info.get(MapleStatInfo.indieMmpR));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INDIE_MAX_HP, ret.info.get(MapleStatInfo.indieMhp));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INDIE_MAX_MP, ret.info.get(MapleStatInfo.indieMmp));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.PVP_DAMAGE, Integer.valueOf(ret.incPVPdamage));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INDIE_JUMP, ret.info.get(MapleStatInfo.indieJump));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INDIE_SPEED, ret.info.get(MapleStatInfo.indieSpeed));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INDIE_ACC, ret.info.get(MapleStatInfo.indieAcc));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.ANGEL_AVOID, ret.info.get(MapleStatInfo.indieEva));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INDIE_ALL_STATE, ret.info.get(MapleStatInfo.indieAllStat));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INDIE_BOOSTER, ret.info.get(MapleStatInfo.indieBooster));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.PVP_ATTACK, ret.info.get(MapleStatInfo.PVPdamage));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.INVINCIBILITY, Integer.valueOf(ret.immortal));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.NO_SLIP, Integer.valueOf(ret.preventslip));
//            addBuffStatPairToListIfNotZero(ret.statups, MapleBuffStat.FAMILIAR_SHADOW, ret.charColor > 0 ? 1 : 0);
            if (sourceid == 5221006 || ret.isPirateMorph()) { //HACK: add stance :D and also this buffstat has to be the first one..
                ret.statups.put(MapleBuffStat.STANCE, 100); //100% :D:D:D
            }
        }
        if (ret.skill) { // hack because we can't get from the datafile...
            boolean found = false;
            if (!found) {
                boolean handle = BuffClassFetcher.getHandleMethod(ret, sourceid);
                if (!handle) {
                    switch (sourceid) {
                        case 12001001:
                        case 22111001:
                            ret.statups.put(MapleBuffStat.MAGIC_GUARD, ret.info.get(MapleStatInfo.x));
                            break;
                        case 13101006: // Wind Walk
                            ret.statups.put(MapleBuffStat.WIND_WALK, ret.info.get(MapleStatInfo.x));
                            break;
                        case 4330001:
                            ret.statups.put(MapleBuffStat.DARKSIGHT, (int) ret.level);
                            break;
                        case 4001003: // Dark Sight
                        case 14001003: // cygnus ds
                        case 20031211:
                            ret.statups.put(MapleBuffStat.DARKSIGHT, ret.info.get(MapleStatInfo.x)); // d
                            break;
                        case 4211003: // pickpocket
                            ret.info.put(MapleStatInfo.time, 2100000000);
                            ret.statups.put(MapleBuffStat.PICKPOCKET, ret.info.get(MapleStatInfo.x));
                            break;
                        case 4211005: // mesoguard
                        case 4201011:
                            ret.statups.put(MapleBuffStat.MESOGUARD, ret.info.get(MapleStatInfo.x));
                            break;
                        case 4111001: // mesoup
                            ret.statups.put(MapleBuffStat.MESOUP, ret.info.get(MapleStatInfo.x));
                            break;
                        case 14111000: // cygnus
                            ret.statups.put(MapleBuffStat.SHADOWPARTNER, ret.info.get(MapleStatInfo.x)); // d
                            break;
                        case 4211008:
                            ret.statups.put(MapleBuffStat.SHADOWPARTNER, (int) ret.level);
                            break;
                        case 11101002: // All Final attack
                        case 51100002:
                        case 13101002:
                            ret.statups.put(MapleBuffStat.FINALATTACK, ret.info.get(MapleStatInfo.x));
                            break;
                        case 22161004:
                            ret.statups.put(MapleBuffStat.ONYX_SHROUD, ret.info.get(MapleStatInfo.x));
                            break;
                        case 3101004: // soul arrow
                        case 3201004:
                        case 13101003:
                        case 13101024://new cyngus
                            ret.statups.put(MapleBuffStat.SOULARROW, ret.info.get(MapleStatInfo.x));
                            break;
                        case 2321010:
                        case 2221009:
                        case 2121009:
                            ret.info.put(MapleStatInfo.time, 2100000000);
                            ret.statups.put(MapleBuffStat.BUFF_MASTERY, ret.info.get(MapleStatInfo.x));
                            break;
                        case 1211006: // wk charges
                        case 1211004:
                        case 1211008:
                        case 1221004:
                        case 11111007:
                        // case 51111003: // Mihile's Radiant Charge
                        case 21101006:
                        case 21111005:
                        case 15101006:
                            ret.statups.put(MapleBuffStat.WK_CHARGE, ret.info.get(MapleStatInfo.x));
                            ret.statups.put(MapleBuffStat.DAMAGE_BUFF, ret.info.get(MapleStatInfo.z));
                            break;
//                    case 51111004:
//                        ret.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, ret.info.get(MapleStatInfo.y));
//                        ret.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, ret.info.get(MapleStatInfo.z));
//                        ret.statups.put(MapleBuffStat.DEFENCE_BOOST_R, ret.info.get(MapleStatInfo.x));
//                        break;
//                    case 51121006:
//                        ret.statups.put(MapleBuffStat.DAMAGE_BUFF, ret.info.get(MapleStatInfo.x));
//                        ret.statups.put(MapleBuffStat.CRITICAL_RATE_BUFF, ret.info.get(MapleStatInfo.criticaldamageMin.x));
//                        ret.statups.put(MapleBuffStat.CRITICAL_RATE_BUFF, ret.info.get(MapleStatInfo.criticaldamageMax.x));
//                        break;
                        case 12101005:
                        case 22121001: // Elemental Reset
                            ret.statups.put(MapleBuffStat.ELEMENT_RESET, ret.info.get(MapleStatInfo.x));
                            break;
                        case 3111000:
                        case 3121008:
                        case 13111001:
                            ret.statups.put(MapleBuffStat.CONCENTRATE, ret.info.get(MapleStatInfo.x));
                            break;
                        case 5110001: // Energy Charge
                        case 15100004:
                            ret.statups.put(MapleBuffStat.ENERGY_CHARGE, 0);
                            break;
                        case 1101004:
                        case 1201004:
                        case 1301004:
                        case 3101002:
                        case 3201002:
                        case 4101003:
                        case 4201002:
                        case 2111005: // spell booster, do these work the same?
                        case 2211005:
                        case 5101006:
                        case 5201003:
                        case 11101001:
                        case 12101004:
                        case 13101001:
                        case 14101002:
                        case 15101002:
                        case 22141002: // Magic Booster
                        case 2311006: // Magic Booster
                        case 33001003:
                        case 5301002:
                        case 24101005:
                        case 5701005:
                        // case 51101003: //Mihile's sword booster
                        case 27101004: //Luminous booster
                            ret.statups.put(MapleBuffStat.BOOSTER, ret.info.get(MapleStatInfo.x) * 2);
                            break;
                        case 21001003: // Aran - Pole Arm Booster
                            ret.statups.put(MapleBuffStat.BOOSTER, -ret.info.get(MapleStatInfo.y));
                            break;
                        case 35111013:
                        case 5111007:
                        case 5211007:
                        case 5811007:
                        case 5911007:
                        case 5311005:
                        case 5320007:
                        case 5120012:
                        case 5220014:
                        case 5711011:
                        case 5720005:
                        case 15111011:
                            ret.statups.put(MapleBuffStat.DICE_ROLL, 0);
                            break;
                        case 5120011:
                        case 5220012:
                            ret.info.put(MapleStatInfo.cooltime, ret.info.get(MapleStatInfo.x));
                            ret.statups.put(MapleBuffStat.DAMAGE_RATE, (int) ret.info.get(MapleStatInfo.damR)); //i think
                            break;
                        case 4321000: //tornado spin uses same buffstats
                            ret.info.put(MapleStatInfo.time, 1000);
                            ret.statups.put(MapleBuffStat.DASH_SPEED, 100 + ret.info.get(MapleStatInfo.x));
                            ret.statups.put(MapleBuffStat.DASH_JUMP, ret.info.get(MapleStatInfo.y)); //always 0 but its there
                            break;
                        case 5001005: // Dash
                        case 15001003:
                            ret.statups.put(MapleBuffStat.DASH_SPEED, ret.info.get(MapleStatInfo.x));
                            ret.statups.put(MapleBuffStat.DASH_JUMP, ret.info.get(MapleStatInfo.y));
                            break;
                        case 1101007: // pguard
                        case 1201007:
                            ret.statups.put(MapleBuffStat.POWERGUARD, ret.info.get(MapleStatInfo.x));
                            break;
                        case 32111004: //conversion
                            ret.statups.put(MapleBuffStat.CONVERSION, ret.info.get(MapleStatInfo.x));
                            break;
                        case 1301007: // hyper body
                        case 1111002: // combo
                        case 11111001: // combo
                        case 1101013:
                            ret.info.put(MapleStatInfo.time, 2100000000);
                            ret.statups.put(MapleBuffStat.COMBO, 1);
                            break;
                        case 21120007: //combo barrier
                            ret.statups.put(MapleBuffStat.COMBO_BARRIER, ret.info.get(MapleStatInfo.x));
                            break;
                        case 5211006: // Homing Beacon
                        case 5220011: // Bullseye
                        case 22151002: //killer wings
                            ret.info.put(MapleStatInfo.time, 2100000000);
                            ret.statups.put(MapleBuffStat.HOMING_BEACON, ret.info.get(MapleStatInfo.x));
                            break;
                        case 1311015: // 十字深鎖鏈
                            ret.statups.put(MapleBuffStat.CROSS_SURGE, ret.info.get(MapleStatInfo.x));
                            break;
                        case 21111009: //combo recharge
                        case 1311006: //dragon roar
                        case 1311005: //NOT A BUFF - Sacrifice
                            ret.hpR = -ret.info.get(MapleStatInfo.x) / 100.0;
                            break;
                        case 1211010: //NOT A BUFF - HP Recover
                            ret.hpR = ret.info.get(MapleStatInfo.x) / 100.0;
                            break;
                        case 4341002:
                            ret.info.put(MapleStatInfo.time, 60 * 1000);
                            ret.hpR = -ret.info.get(MapleStatInfo.x) / 100.0;
                            ret.statups.put(MapleBuffStat.FINAL_CUT, ret.info.get(MapleStatInfo.y));
                            break;
                        case 2111007:
                        case 2211007:
                        case 2311007:
                        case 32111010:
                        case 22161005:
                        case 12111007:
                            ret.info.put(MapleStatInfo.mpCon, ret.info.get(MapleStatInfo.y));
                            ret.info.put(MapleStatInfo.time, 2100000000);
                            ret.statups.put(MapleBuffStat.TELEPORT_MASTERY, ret.info.get(MapleStatInfo.x));
                            ret.monsterStatus.put(MonsterStatus.STUN, 1);
                            break;

                        case 4331003:
                            ret.info.put(MapleStatInfo.time, 2100000000);
                            ret.statups.put(MapleBuffStat.OWL_SPIRIT, ret.info.get(MapleStatInfo.y));
                            break;
                        case 1311008: // dragon blood
                            ret.statups.put(MapleBuffStat.DRAGONBLOOD, ret.info.get(MapleStatInfo.x));
                            break;
                        case 15111006: //spark
                            ret.statups.put(MapleBuffStat.SPARK, ret.info.get(MapleStatInfo.x));
                            break;
                        case 3121002: // sharp eyes bow master
                        case 3221002: // sharp eyes marksmen
                            ret.statups.put(MapleBuffStat.SHARP_EYES, (ret.info.get(MapleStatInfo.x) << 8) + ret.info.get(MapleStatInfo.criticaldamageMax));
                            break;
                        case 22151003: //magic resistance
                            ret.statups.put(MapleBuffStat.MAGIC_RESISTANCE, ret.info.get(MapleStatInfo.x));
                            break;
                        case 2000007:
                        case 12000006:
                        case 22000002:
                        case 32000012:
                            ret.statups.put(MapleBuffStat.ELEMENT_WEAKEN, ret.info.get(MapleStatInfo.x));
                            break;
                        case 21101003: // Body Pressure
                            ret.statups.put(MapleBuffStat.BODY_PRESSURE, ret.info.get(MapleStatInfo.x));
                            break;
                        case 21000000: // Aran Combo
                            ret.statups.put(MapleBuffStat.ARAN_COMBO, 100);
                            break;
                        case 21100005: // Combo Drain
                            ret.statups.put(MapleBuffStat.COMBO_DRAIN, ret.info.get(MapleStatInfo.x));
                            break;
                        case 42101020:
                            ret.statups.put(MapleBuffStat.RECOVERY, ret.info.get(MapleStatInfo.hp));
                            break;
                        case 21111001: // Smart Knockback
                            ret.statups.put(MapleBuffStat.SMART_KNOCKBACK, ret.info.get(MapleStatInfo.x));
                            break;

                        case 23121004:
                            ret.statups.put(MapleBuffStat.DAMAGE_RATE, (int) ret.info.get(MapleStatInfo.damR));
                            ret.statups.put(MapleBuffStat.ENHANCED_MAXHP, (int) ret.info.get(MapleStatInfo.emhp));
                            break;
                        case 1211009:
                        case 1111007:
                        case 1311007: //magic crash
                        case 51111005: //Mihile's magic crash
                            ret.monsterStatus.put(MonsterStatus.MAGIC_CRASH, 1);
                            break;
                        case 1220013:
                            ret.statups.put(MapleBuffStat.DIVINE_SHIELD, ret.info.get(MapleStatInfo.x) + 1);
                            break;
                        case 1211011:
                            ret.statups.put(MapleBuffStat.COMBAT_ORDERS, ret.info.get(MapleStatInfo.x));
                            break;
                        case 22131001: //magic shield
                            ret.statups.put(MapleBuffStat.MAGIC_SHIELD, ret.info.get(MapleStatInfo.x));
                            break;
                        case 22181003: //soul stone
                            ret.statups.put(MapleBuffStat.SOUL_STONE, 1);
                            break;
                        case 24111002: //Final Feint
                            ret.info.put(MapleStatInfo.time, 2100000000);
                            ret.statups.put(MapleBuffStat.SOUL_STONE, 1);
                            break;
                        case 32121003: //twister
                            ret.statups.put(MapleBuffStat.TORNADO, ret.info.get(MapleStatInfo.x));
                            break;
                        case 2311009: //holy magic
                            ret.statups.put(MapleBuffStat.HOLY_MAGIC_SHELL, ret.info.get(MapleStatInfo.x));
                            ret.info.put(MapleStatInfo.cooltime, ret.info.get(MapleStatInfo.y));
                            ret.hpR = ret.info.get(MapleStatInfo.z) / 100.0;
                            break;
                        case 22131002:
                        case 22141003: // Slow
                            ret.statups.put(MapleBuffStat.SLOW, ret.info.get(MapleStatInfo.x));
                            break;
                        case 4001002: // disorder
                        case 14001002: // cygnus disorder
                            ret.monsterStatus.put(MonsterStatus.WATK, ret.info.get(MapleStatInfo.x));
                            ret.monsterStatus.put(MonsterStatus.WDEF, ret.info.get(MapleStatInfo.y));
                            break;
                        case 5221009: // Mind Control
                            ret.monsterStatus.put(MonsterStatus.HYPNOTIZE, 1);
                            break;
                        case 4341003: // Monster Bomb
                            ret.monsterStatus.put(MonsterStatus.MONSTER_BOMB, (int) ret.info.get(MapleStatInfo.damage));
                            break;
                        case 1201006: // threaten
                            ret.monsterStatus.put(MonsterStatus.WATK, ret.info.get(MapleStatInfo.x));
                            ret.monsterStatus.put(MonsterStatus.WDEF, ret.info.get(MapleStatInfo.x));
                            ret.monsterStatus.put(MonsterStatus.DARKNESS, ret.info.get(MapleStatInfo.z));
                            break;
                        case 22141001:
                        case 1211002: // charged blow
                        case 1111008: // shout
                        case 4211002: // assaulter
                        case 3101005: // arrow bomb
                        case 1111005: // coma: sword
                        case 4221007: // boomerang step
                        case 5101002: // Backspin Blow
                        case 5101003: // Double Uppercut
                        case 5121004: // Demolition
                        case 5121005: // Snatch
                        case 5121007: // Barrage
                        case 5201004: // pirate blank shot
                        case 4121008: // Ninja Storm
                        case 22151001:
                        case 4201004: //steal, new
                        case 33101001:
                        case 33101002:
                        case 32101001:
                        case 32111011:
                        case 32121004:
                        case 33111002:
                        case 33121002:
                        case 35101003:
                        case 35111015:
                        case 5111002: //energy blast
                        case 15101005:
                        case 4331005:
                        case 1121001: //magnet
                        case 1221001:
                        case 1321001:
                        case 9001020:
                        case 31111001:
                        case 31101002:
                        case 9101020:
                        case 2211003:
                        case 2311004:
                        case 3120010:
                        case 22181001:
                        case 21110006:
                        case 22131000:
                        case 5301001:
                        case 5311001:
                        case 5311002:
                        case 2221006:
                        case 5310008:
                            ret.monsterStatus.put(MonsterStatus.STUN, 1);
                            break;
                        case 90001004:
                        case 4321002:
                        case 1111003:
                        case 11111002:
                            ret.monsterStatus.put(MonsterStatus.DARKNESS, ret.info.get(MapleStatInfo.x));
                            break;
                        case 4221003:
                        case 4121003:
                        case 33121005:
                            ret.monsterStatus.put(MonsterStatus.SHOWDOWN, ret.info.get(MapleStatInfo.x));
                            ret.monsterStatus.put(MonsterStatus.MDEF, ret.info.get(MapleStatInfo.x)); // removed for taunt
                            ret.monsterStatus.put(MonsterStatus.WDEF, ret.info.get(MapleStatInfo.x)); // removed for taunt
                            break;
                        case 31121003:
                            ret.monsterStatus.put(MonsterStatus.SHOWDOWN, ret.info.get(MapleStatInfo.w));
                            ret.monsterStatus.put(MonsterStatus.MDEF, ret.info.get(MapleStatInfo.x));
                            ret.monsterStatus.put(MonsterStatus.WDEF, ret.info.get(MapleStatInfo.x));
                            ret.monsterStatus.put(MonsterStatus.MATK, ret.info.get(MapleStatInfo.x));
                            ret.monsterStatus.put(MonsterStatus.WATK, ret.info.get(MapleStatInfo.x));
                            ret.monsterStatus.put(MonsterStatus.ACC, ret.info.get(MapleStatInfo.x));
                            break;
                        case 23121002: //not sure if negative
                            ret.monsterStatus.put(MonsterStatus.WDEF, -ret.info.get(MapleStatInfo.x));
                            break;
                        case 2201004: // cold beam
                        case 2221003:
                        case 2211002: // ice strike
                        case 3211003: // blizzard
                        case 2211006: // il elemental compo
                        case 2221007: // Blizzard
                        case 5211005: // Ice Splitter
                        case 2121006: // Paralyze
                        case 21120006: // Tempest
                        case 22121000:
                        case 90001006:
                        case 2221001:
                            ret.monsterStatus.put(MonsterStatus.FREEZE, 1);
                            ret.info.put(MapleStatInfo.time, ret.info.get(MapleStatInfo.time) * 2); // freezing skills are a little strange
                            break;
                        case 2101003: // fp slow
                        case 2201003: // il slow
                        case 12101001:
                        case 90001002:
                            ret.monsterStatus.put(MonsterStatus.SPEED, ret.info.get(MapleStatInfo.x));
                            break;
                        case 5011002:
                            ret.monsterStatus.put(MonsterStatus.SPEED, ret.info.get(MapleStatInfo.z));
                            break;
                        case 23111002:
                        case 22161002: //phantom imprint
                            ret.monsterStatus.put(MonsterStatus.IMPRINT, ret.info.get(MapleStatInfo.x));
                            break;
                        case 90001003:
                            ret.monsterStatus.put(MonsterStatus.POISON, 1);
                            break;
                        case 4121004: // Ninja ambush
                        case 4221004:
                            ret.monsterStatus.put(MonsterStatus.NINJA_AMBUSH, (int) ret.info.get(MapleStatInfo.damage));
                            break;
                        case 2311005:
                            ret.monsterStatus.put(MonsterStatus.DOOM, 1);
                            break;
                        case 4341006:
                        case 3120012:
                        case 3220012:
                        case 3111002: // puppet ranger
                        case 3211002: // puppet sniper
                        case 13111004: // puppet cygnus
                        case 13111024: // Emerald Flower
                        case 5211001: // Pirate octopus summon
                        case 5220002: // wrath of the octopi
                        case 33111003:
                        case 5321003:
                        case 5211014:
                            ret.statups.put(MapleBuffStat.PUPPET, 1);
                            break;
                        case 3120006:
                        case 3220005:
                            ret.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, (int) ret.info.get(MapleStatInfo.terR));
                            ret.statups.put(MapleBuffStat.SPIRIT_LINK, 1);
                            break;
                        case 5220019:
                            ret.info.put(MapleStatInfo.time, 120000);
                            break;
                        case 5211011:
                        case 5211015:
                        case 5211016:
                        case 5711001: // turret
                        case 2121005: // elquines
                        case 3201007:
                        case 3101007:
                        case 3211005: // golden eagle
                        case 3111005: // golden hawk
                        case 33111005:
                        case 3121006: // phoenix
                        case 23111008:
                        case 23111009:
                        case 23111010:
                            ret.statups.put(MapleBuffStat.SUMMON, 1);
                            ret.monsterStatus.put(MonsterStatus.STUN, 1);
                            break;
                        case 3221005: // frostprey
                        case 2221005: // ifrit
                            ret.statups.put(MapleBuffStat.SUMMON, 1);
                            ret.monsterStatus.put(MonsterStatus.FREEZE, 1);
                            break;
                        case 1321007: // Beholder
                        case 1301013: // Evil Eye
//                        case 1311013: // Evil Eye of Domination
                            ret.statups.put(MapleBuffStat.BEHOLDER, (int) ret.level);
                            break;
                        case 2321003: // bahamut
                        case 5211002: // Pirate bird summon
                        case 11001004:
                        case 12001004:
                        case 12111004: // Itrit
                        case 13001004:
                        case 14001005:
                        case 15001004:
                        case 33101008: //summon - its raining mines
                        case 4111007: //dark flare
                        case 4211007: //dark flare
                        case 14111010: //dark flare
                        case 5321004:
                            ret.statups.put(MapleBuffStat.SUMMON, 1);
                            break;
                        case 65101002:
                            ret.statups.put(MapleBuffStat.DAMAGE_ABSORBED, ret.info.get(MapleStatInfo.x));
                            break;
                        case 65111004: // Iron Blossom
                            ret.statups.put(MapleBuffStat.STANCE, ret.info.get(MapleStatInfo.prop));
                            break;
                        case 31121005:
                            ret.statups.put(MapleBuffStat.DAMAGE_RATE, (int) ret.info.get(MapleStatInfo.damR));
                            ret.statups.put(MapleBuffStat.DARK_METAMORPHOSIS, 6); // mob count
                            break;
                        case 2311003: // hs
                        case 80001034: //virtue
                        case 80001035: //virtue
                        case 80001036: //virtue
                            ret.statups.put(MapleBuffStat.VIRTUE_EFFECT, 1);
                            break;
                        case 2211004: // il seal
                        case 2111004: // fp seal
                        case 12111002: // cygnus seal
                        case 90001005:
                            ret.monsterStatus.put(MonsterStatus.SEAL, 1);
                            break;
                        case 24121003:
                            ret.info.put(MapleStatInfo.damage, ret.info.get(MapleStatInfo.v));
                            ret.info.put(MapleStatInfo.attackCount, ret.info.get(MapleStatInfo.w));
                            ret.info.put(MapleStatInfo.mobCount, ret.info.get(MapleStatInfo.x));
                            break;
                        case 4111003: // shadow web
                        case 14111001:
                            ret.monsterStatus.put(MonsterStatus.SHADOW_WEB, 1);
                            break;
                        case 4111009: // Shadow Stars
                        case 5201008:
                        case 14111007:
                            ret.statups.put(MapleBuffStat.SPIRIT_CLAW, 0);
                            break;
                        case 2121004:
                        case 2221004:
                        case 2321004: // Infinity
                            ret.hpR = ret.info.get(MapleStatInfo.y) / 100.0;
                            ret.mpR = ret.info.get(MapleStatInfo.y) / 100.0;
                            ret.statups.put(MapleBuffStat.INFINITY, ret.info.get(MapleStatInfo.x));
                            ret.statups.put(MapleBuffStat.STANCE, (int) ret.info.get(MapleStatInfo.prop));
                            break;
                        case 22181004:
                            ret.statups.put(MapleBuffStat.ONYX_WILL, (int) ret.info.get(MapleStatInfo.damage)); //is this the right order
                            ret.statups.put(MapleBuffStat.STANCE, (int) ret.info.get(MapleStatInfo.prop));
                            break;
                        case 1121002:
                        case 1221002:
                        case 1321002: // Stance
                        // case 51121004: //Mihile's Stance
                        case 50001214:
                        case 80001140:
                        case 21121003: // Aran - Freezing Posture
                        case 5321010:
                            ret.statups.put(MapleBuffStat.STANCE, (int) ret.info.get(MapleStatInfo.prop));
                            break;
                        case 2121002: // mana reflection
                        case 2221002:
                        case 2321002:
                            ret.statups.put(MapleBuffStat.MANA_REFLECTION, 1);
                            break;
                        case 2321005: // 進階祝福Advanced Blessing
                            ret.statups.put(MapleBuffStat.HOLY_SHIELD, ret.info.get(MapleStatInfo.x));
                            ret.statups.put(MapleBuffStat.INDIE_MAX_HP, ret.info.get(MapleStatInfo.indieMhp));
                            ret.statups.put(MapleBuffStat.INDIE_MAX_MP, ret.info.get(MapleStatInfo.indieMmp));
                            break;
                        case 3121007: // Hamstring
                            ret.statups.put(MapleBuffStat.HAMSTRING, ret.info.get(MapleStatInfo.x));
                            ret.monsterStatus.put(MonsterStatus.SPEED, ret.info.get(MapleStatInfo.x));
                            break;
                        case 3221006: // Blind
                        case 33111004:
                            ret.statups.put(MapleBuffStat.BLIND, ret.info.get(MapleStatInfo.x));
                            ret.monsterStatus.put(MonsterStatus.ACC, ret.info.get(MapleStatInfo.x));
                            break;

                        case 9101003: //customs for infinite dmg :D
                            ret.statups.put(MapleBuffStat.INDIE_PAD, Integer.MAX_VALUE);
                            ret.statups.put(MapleBuffStat.INDIE_MAD, Integer.MAX_VALUE);
                            ret.statups.put(MapleBuffStat.INDIE_MAX_DAMAGE_OVER, 500000);
                        case 2301004:
                        case 9001003:
                            ret.statups.put(MapleBuffStat.BLESS, (int) ret.level);
                            break;
                        case 32120000:
                            ret.info.put(MapleStatInfo.dot, ret.info.get(MapleStatInfo.damage));
                            ret.info.put(MapleStatInfo.dotTime, 3);
                        case 32120001:
                            ret.monsterStatus.put(MonsterStatus.SPEED, ret.info.get(MapleStatInfo.speed));
                        case 33101004: //it's raining mines
                            ret.statups.put(MapleBuffStat.RAINING_MINES, ret.info.get(MapleStatInfo.x)); //x?
                            break;
                        case 80001040:
                        case 20021110:
                        case 20031203:
                            ret.moveTo = ret.info.get(MapleStatInfo.x);
                            break;
                        case 5311004:
                            ret.statups.put(MapleBuffStat.BARREL_ROLL, 0);
                            break;
                        case 5121015:
                            ret.statups.put(MapleBuffStat.DAMAGE_BUFF, ret.info.get(MapleStatInfo.x));
                            break;
                        case 80001089: // Soaring
                            ret.info.put(MapleStatInfo.time, 2100000000);
                            ret.statups.put(MapleBuffStat.SOARING, 1);
                            break;
                        case 20031205:
                            ret.statups.put(MapleBuffStat.PHANTOM_MOVE, ret.info.get(MapleStatInfo.x));
                            break;
                        case 5211009:
                            ret.info.put(MapleStatInfo.time, 2100000000);
                            break;
                        case 10001075: // Cygnus Echo
                        case 50001075: // Mihile's Empress's Prayer
                            ret.statups.put(MapleBuffStat.ECHO_OF_HERO, ret.info.get(MapleStatInfo.x));
                            break;
                        case 51111007:
                        // case 51121008:
                        //     ret.monsterStatus.put(MonsterStatus.STUN, 1);
                        //     break;
                        // case 51121007:
                        //     ret.statups.put(MapleBuffStat.BLIND, ret.info.get(MapleStatInfo.x));
                        //     ret.monsterStatus.put(MonsterStatus.ACC, ret.info.get(MapleStatInfo.x));
                        //     break;
                        case 80001427://疾速之輪
                            ret.statups.put(MapleBuffStat.INDIE_JUMP, ret.info.get(MapleStatInfo.indieJump));
                            ret.statups.put(MapleBuffStat.INDIE_SPEED, ret.info.get(MapleStatInfo.indieSpeed));
                            ret.statups.put(MapleBuffStat.INDIE_EXP, ret.info.get(MapleStatInfo.indieExp));
                            ret.statups.put(MapleBuffStat.INDIE_BOOSTER, ret.info.get(MapleStatInfo.indieBooster));
                            break;
                        case 80001428://再生之輪
                            ret.statups.put(MapleBuffStat.INDIE_EXP, ret.info.get(MapleStatInfo.indieExp));
                            ret.statups.put(MapleBuffStat.INDIE_ASR_R, ret.info.get(MapleStatInfo.indieAsrR));
                            ret.statups.put(MapleBuffStat.INDIE_TER_R, ret.info.get(MapleStatInfo.indieTerR));
                            ret.statups.put(MapleBuffStat.IGNORE_MOB_DAM_R, ret.info.get(MapleStatInfo.ignoreMobDamR));
                            break;
                        case 80001430://崩壞之輪
                        case 80001432://破滅之輪
                            ret.statups.put(MapleBuffStat.INDIE_EXP, ret.info.get(MapleStatInfo.indieExp));
                            ret.statups.put(MapleBuffStat.INDIE_BOOSTER, ret.info.get(MapleStatInfo.indieBooster));
                            ret.statups.put(MapleBuffStat.INDIE_DAM_R, ret.info.get(MapleStatInfo.indieDamR));
                            break;
                        default:
                            break;
                    }
                }
            }
            if (MapleJob.isBeginner(sourceid / 10000)) {
                switch (sourceid % 10000) {
                    case 1087:
                        ret.info.put(MapleStatInfo.time, 2100000000);
                        ret.statups.put(MapleBuffStat.INDIE_PAD, 10);
                        ret.statups.put(MapleBuffStat.INDIE_MAD, 10);
                        ret.statups.put(MapleBuffStat.SPEED, 1);
                        break;
                    case 1085:
                    case 1090:
                        ret.info.put(MapleStatInfo.time, 2100000000);
                        ret.statups.put(MapleBuffStat.INDIE_PAD, 5);
                        ret.statups.put(MapleBuffStat.INDIE_MAD, 5);
                        ret.statups.put(MapleBuffStat.SPEED, 1);
                        break;
                    case 1179:
                        ret.info.put(MapleStatInfo.time, 2100000000);
                        ret.statups.put(MapleBuffStat.INDIE_PAD, 12);
                        ret.statups.put(MapleBuffStat.INDIE_MAD, 12);
                        ret.statups.put(MapleBuffStat.SPEED, 1);
                        break;
//            if (MapleJob.isBeginner(sourceid / 10000)) {
//                switch (sourceid % 10000) {
//                    //angelic blessing: HACK, we're actually supposed to use the passives for atk/matk buff
//                    case 1087:
//                        ret.info.put(MapleStatInfo.time, 2100000000);
//                        ret.statups.put(MapleBuffStat.INDIE_PAD, 10);
//                        ret.statups.put(MapleBuffStat.INDIE_MAD, 10);
//                        break;
//                    case 1085:
//                    case 1090:
//                        ret.info.put(MapleStatInfo.time, 2100000000);
//                        ret.statups.put(MapleBuffStat.INDIE_PAD, 5);
//                        ret.statups.put(MapleBuffStat.INDIE_MAD, 5);
//                        break;
//                    case 1179:
//                    case 1154:
//                        ret.info.put(MapleStatInfo.time, 2100000000);
//                        ret.statups.put(MapleBuffStat.INDIE_PAD, 12);
//                        ret.statups.put(MapleBuffStat.INDIE_MAD, 12);
//                        break;
                    case 1105:
                        ret.statups.put(MapleBuffStat.FROZEN, 1);
                        ret.info.put(MapleStatInfo.time, 2100000000);
                        break;
                    case 93:
                        ret.statups.put(MapleBuffStat.HIDDEN_POTENTIAL, 1);
                        break;
                    case 8001:
                        ret.statups.put(MapleBuffStat.SOULARROW, ret.info.get(MapleStatInfo.x));
                        break;
                    case 1005: // Echo of Hero
                        ret.statups.put(MapleBuffStat.ECHO_OF_HERO, ret.info.get(MapleStatInfo.x));
                        break;
                    case 1011: // Berserk fury
                        ret.statups.put(MapleBuffStat.BERSERK_FURY, ret.info.get(MapleStatInfo.x));
                        break;
                    case 1010:
                        ret.statups.put(MapleBuffStat.DIVINE_BODY, 1);
                        break;
                    case 1001:
                        if (sourceid / 10000 == 3001 || sourceid / 10000 == 3000) { //resistance is diff
                            ret.statups.put(MapleBuffStat.INFILTRATE, ret.info.get(MapleStatInfo.x));
                        } else {
                            ret.statups.put(MapleBuffStat.RECOVERY, ret.info.get(MapleStatInfo.x));
                        }
                        break;
                    case 1002:
                        ret.statups.put(MapleBuffStat.SPEED, ret.info.get(MapleStatInfo.speed));
                        break;
                    case 8003:
                        ret.statups.put(MapleBuffStat.MAXHP, ret.info.get(MapleStatInfo.x));
                        ret.statups.put(MapleBuffStat.MAXMP, ret.info.get(MapleStatInfo.x));
                        break;
                    case 8004:
                        ret.statups.put(MapleBuffStat.COMBAT_ORDERS, ret.info.get(MapleStatInfo.x));
                        break;
                    case 8005:
                        ret.statups.put(MapleBuffStat.HOLY_SHIELD, 1);
                        break;
                    case 8006:
                        ret.statups.put(MapleBuffStat.SPEED_INFUSION, ret.info.get(MapleStatInfo.x));
                        break;
                    case 103:
                        ret.monsterStatus.put(MonsterStatus.STUN, 1);
                        break;
                    case 99:
                    case 104:
                        ret.monsterStatus.put(MonsterStatus.FREEZE, 1);
                        ret.info.put(MapleStatInfo.time, ret.info.get(MapleStatInfo.time) * 2); // freezing skills are a little strange
                        break;
                    case 8002:
                        ret.statups.put(MapleBuffStat.SHARP_EYES, (ret.info.get(MapleStatInfo.x) << 8) + ret.info.get(MapleStatInfo.y) + ret.info.get(MapleStatInfo.criticaldamageMax));
                        break;
                    case 1026: // Soaring
                    case 1142: // Soaring
                        ret.info.put(MapleStatInfo.time, 2100000000);
                        ret.statups.put(MapleBuffStat.SOARING, 1);
                        break;
                }
            }
        } else {
            switch (sourceid) {
                case 2022746: //angel bless
                case 2022747: //d.angel bless
                case 2022823:
                    ret.statups.clear(); //no atk/matk
                    ret.statups.put(MapleBuffStat.PYRAMID_PQ, 1); //ITEM_EFFECT buff
                    break;
            }
        }
        if (ret.isPoison()) {
            ret.monsterStatus.put(MonsterStatus.POISON, 1);
        }
        if (ret.isMorph() || ret.isPirateMorph()) {
            ret.statups.put(MapleBuffStat.MORPH, ret.getMorph());
        }

        return ret;
    }

    /**
     * @param applyto
     * @param obj
     */
    public final void applyPassive(final MapleCharacter applyto, final MapleMapObject obj) {
        if (makeChanceResult() && !MapleJob.is惡魔殺手(applyto.getJob())) { // demon can't heal mp
            switch (sourceid) { // MP eater
                case 2100000:
                case 2200000:
                case 2300000:
                    if (obj == null || obj.getType() != MapleMapObjectType.MONSTER) {
                        return;
                    }
                    final MapleMonster mob = (MapleMonster) obj; // x is absorb percentage
                    if (!mob.getStats().isBoss()) {
                        final int absorbMp = Math.min((int) (mob.getMobMaxMp() * (getX() / 100.0)), mob.getMp());
                        if (absorbMp > 0) {
                            mob.setMp(mob.getMp() - absorbMp);
                            applyto.getStat().setMp(applyto.getStat().getMp() + absorbMp, applyto);
                            applyto.getClient().getSession().write(EffectPacket.showBuffEffect(true, applyto, sourceid, SpecialEffectType.LOCAL_SKILL, applyto.getLevel(), level));
                            applyto.getMap().broadcastMessage(applyto, EffectPacket.showBuffEffect(false, applyto, sourceid, SpecialEffectType.LOCAL_SKILL, applyto.getLevel(), level), false);
                        }
                    }
                    break;
            }
        }
    }

    public final boolean applyTo(MapleCharacter chr) {
        return applyTo(chr, chr, true, null, info.get(MapleStatInfo.time));
    }

    public final boolean applyTo(MapleCharacter chr, Point pos) {
        return applyTo(chr, chr, true, pos, info.get(MapleStatInfo.time));
    }

    public final boolean applyTo(final MapleCharacter applyfrom, final MapleCharacter applyto, final boolean primary, final Point pos, int newDuration) {
        if (isHeal() && (applyfrom.getMapId() == 749040100 || applyto.getMapId() == 749040100)) {
            applyfrom.getClient().getSession().write(CWvsContext.enableActions());
            return false; //z
        } else if ((isSoaring_Mount() && applyfrom.getBuffedValue(MapleBuffStat.MONSTER_RIDING) == null) || (isSoaring_Normal() && !applyfrom.getMap().canSoar())) {
            applyfrom.getClient().getSession().write(CWvsContext.enableActions());
            return false;
        } else if (sourceid == 4341006 && applyfrom.getBuffedValue(MapleBuffStat.SHADOWPARTNER) == null) {
            applyfrom.getClient().getSession().write(CWvsContext.enableActions());
            return false;
        } else if (sourceid == 33101008 && (applyfrom.getBuffedValue(MapleBuffStat.RAINING_MINES) == null || applyfrom.getBuffedValue(MapleBuffStat.SUMMON) != null || !applyfrom.canSummon())) {
            applyfrom.getClient().getSession().write(CWvsContext.enableActions());
            return false;
        } else if (isShadow() && applyfrom.getJob() / 100 % 10 != 4) { //pirate/shadow = dc
            applyfrom.getClient().getSession().write(CWvsContext.enableActions());
            return false;
        } else if (sourceid == 33101004 && applyfrom.getMap().isTown()) {
            applyfrom.dropMessage(5, "You may not use this skill in towns.");
            applyfrom.getClient().getSession().write(CWvsContext.enableActions());
            return false;
        }
        int hpchange = calcHPChange(applyfrom, primary);
        int mpchange = calcMPChange(applyfrom, primary);
        int powerchange = calcPowerChange(applyfrom);

        final PlayerStats stat = applyto.getStat();
        if (primary) {
            if (info.get(MapleStatInfo.itemConNo) != 0 && !applyto.isClone() && !applyto.inPVP()) {
                if (!applyto.haveItem(info.get(MapleStatInfo.itemCon), info.get(MapleStatInfo.itemConNo), false, true)) {
                    applyto.getClient().getSession().write(CWvsContext.enableActions());
                    return false;
                }
                MapleInventoryManipulator.removeById(applyto.getClient(), GameConstants.getInventoryType(info.get(MapleStatInfo.itemCon)), info.get(MapleStatInfo.itemCon), info.get(MapleStatInfo.itemConNo), false, true);
            }
        } else if (!primary && isResurrection()) {
            hpchange = stat.getMaxHp();
            applyto.setStance(0);
        }
        if (isDispel() && makeChanceResult()) {
            applyto.dispelDebuffs();
        } else if (isHeroWill()) {
            applyto.dispelDebuffs();
        } else if (cureDebuffs.size() > 0) {
            for (final MapleDisease debuff : cureDebuffs) {
                applyfrom.dispelDebuff(debuff);
            }
        } else if (isMPRecovery()) {
            final int toDecreaseHP = ((stat.getMaxHp() / 100) * 10);
            if (stat.getHp() > toDecreaseHP) {
                hpchange += -toDecreaseHP; // -10% of max HP
                mpchange += ((toDecreaseHP / 100) * getY());
            } else {
                hpchange = stat.getHp() == 1 ? 0 : stat.getHp() - 1;
            }
        }
        final Map<MapleStat, Long> hpmpupdate = new EnumMap<>(MapleStat.class);
        if (hpchange != 0) {
            if (hpchange < 0 && (-hpchange) > stat.getHp() && !applyto.hasDisease(MapleDisease.ZOMBIFY)) {
                applyto.getClient().getSession().write(CWvsContext.enableActions());
                return false;
            }
            stat.setHp(stat.getHp() + hpchange, applyto);
        }
        if (mpchange != 0) {
            if (mpchange < 0 && (-mpchange) > stat.getMp()) {
                applyto.getClient().getSession().write(CWvsContext.enableActions());
                return false;
            }
            //short converting needs math.min cuz of overflow
            if ((mpchange < 0 && MapleJob.is惡魔殺手(applyto.getJob())) || !MapleJob.is惡魔殺手(applyto.getJob())) { // heal
                stat.setMp(stat.getMp() + mpchange, applyto);
            }
            hpmpupdate.put(MapleStat.MP, Long.valueOf(stat.getMp()));
        }
        hpmpupdate.put(MapleStat.HP, Long.valueOf(stat.getHp()));

        applyto.getClient().getSession().write(CWvsContext.updatePlayerStats(hpmpupdate, true, applyto));
        if (powerchange != 0) {
            if (applyto.getXenonSurplus() - powerchange < 0) {
                return false;
            }
            applyto.gainXenonSurplus((short) -powerchange);
        }
        if (expinc != 0) {
            applyto.gainExp(expinc, true, true, false);
            applyto.getClient().getSession().write(EffectPacket.showDodgeChanceEffect());
        } else if (sourceid / 10000 == 238) {
            final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            final int mobid = ii.getCardMobId(sourceid);
            if (mobid > 0) {
                final boolean done = applyto.getMonsterBook().monsterCaught(applyto.getClient(), mobid, MapleLifeFactory.getMonsterStats(mobid).getName());
                applyto.getClient().getSession().write(CWvsContext.getCard(done ? sourceid : 0, 1));
            }
        } else if (isReturnScroll()) {
            applyReturnScroll(applyto);
        } else if (useLevel > 0 && !skill) {
            applyto.setExtractor(new MapleExtractor(applyto, sourceid, useLevel * 50, 1440)); //no clue about time left
            applyto.getMap().spawnExtractor(applyto.getExtractor());
        } else if (isMistEruption()) {
            int i = info.get(MapleStatInfo.y);
            for (MapleMist m : applyto.getMap().getAllMistsThreadsafe()) {
                if (m.getOwnerId() == applyto.getId() && m.getSourceSkill().getId() == 2111003) {
                    if (m.getSchedule() != null) {
                        m.getSchedule().cancel(false);
                        m.setSchedule(null);
                    }
                    if (m.getPoisonSchedule() != null) {
                        m.getPoisonSchedule().cancel(false);
                        m.setPoisonSchedule(null);
                    }
                    applyto.getMap().broadcastMessage(CField.removeMist(m.getObjectId(), true));
                    applyto.getMap().removeMapObject(m);

                    i--;
                    if (i <= 0) {
                        break;
                    }
                }
            }
        } else if (cosmetic > 0) {
            if (cosmetic >= 30000) {
                applyto.setHair(cosmetic);
                applyto.updateSingleStat(MapleStat.HAIR, cosmetic);
            } else if (cosmetic >= 20000) {
                applyto.setFace(cosmetic);
                applyto.updateSingleStat(MapleStat.FACE, cosmetic);
            } else if (cosmetic < 100) {
                applyto.setSkinColor((byte) cosmetic);
                applyto.updateSingleStat(MapleStat.SKIN, cosmetic);
            }
            applyto.equipChanged();
        } else if (bs > 0) {
            if (!applyto.inPVP()) {
                return false;
            }
            final int x = Integer.parseInt(applyto.getEventInstance().getProperty(String.valueOf(applyto.getId())));
            applyto.getEventInstance().setProperty(String.valueOf(applyto.getId()), String.valueOf(x + bs));
            applyto.getClient().getSession().write(CField.getPVPScore(x + bs, false));
        } else if (info.get(MapleStatInfo.iceGageCon) > 0) {
            if (!applyto.inPVP()) {
                return false;
            }
            final int x = Integer.parseInt(applyto.getEventInstance().getProperty("icegage"));
            if (x < info.get(MapleStatInfo.iceGageCon)) {
                return false;
            }
            applyto.getEventInstance().setProperty("icegage", String.valueOf(x - info.get(MapleStatInfo.iceGageCon)));
            applyto.getClient().getSession().write(CField.getPVPIceGage(x - info.get(MapleStatInfo.iceGageCon)));
            applyto.applyIceGage(x - info.get(MapleStatInfo.iceGageCon));
        } else if (recipe > 0) {
            if (applyto.getSkillLevel(recipe) > 0 || applyto.getProfessionLevel((recipe / 10000) * 10000) < reqSkillLevel) {
                return false;
            }
            applyto.changeSingleSkillLevel(SkillFactory.getCraft(recipe), Integer.MAX_VALUE, recipeUseCount, recipeValidDay > 0 ? (System.currentTimeMillis() + recipeValidDay * 24L * 60 * 60 * 1000) : -1L);
        } else if (isComboRecharge()) {
            applyto.setCombo((short) Math.min(30000, applyto.getCombo() + info.get(MapleStatInfo.y)));
            applyto.setLastCombo(System.currentTimeMillis());
            applyto.getClient().getSession().write(CField.rechargeCombo(applyto.getCombo()));
            SkillFactory.getSkill(21000000).getEffect(10).applyComboBuff(applyto, applyto.getCombo());
        } else if (isDragonBlink()) {
            final MaplePortal portal = applyto.getMap().getPortal(Randomizer.nextInt(applyto.getMap().getPortals().size()));
            if (portal != null) {
                applyto.getClient().getSession().write(CField.dragonBlink(portal.getId()));
                applyto.getMap().movePlayer(applyto, portal.getPosition());
                applyto.checkFollow();
            }
        } else if (isSpiritClaw() && !applyto.isClone()) {
            MapleInventory use = applyto.getInventory(MapleInventoryType.USE);
            boolean itemz = false;
            for (int i = 0; i < use.getSlotLimit(); i++) { // impose order...
                Item item = use.getItem((byte) i);
                if (item != null) {
                    if (ItemConstants.類型.可充值道具(item.getItemId()) && item.getQuantity() >= 100) {
                        MapleInventoryManipulator.removeFromSlot(applyto.getClient(), MapleInventoryType.USE, (short) i, (short) 100, false, true);
                        itemz = true;
                        break;
                    }
                }
            }
            if (!itemz) {
                return false;
            }
        } else if (isSpiritBlast() && !applyto.isClone()) {
            MapleInventory use = applyto.getInventory(MapleInventoryType.USE);
            boolean itemz = false;
            for (int i = 0; i < use.getSlotLimit(); i++) { // impose order...
                Item item = use.getItem((byte) i);
                if (item != null) {
                    if (ItemConstants.類型.子彈(item.getItemId()) && item.getQuantity() >= 100) {
                        MapleInventoryManipulator.removeFromSlot(applyto.getClient(), MapleInventoryType.USE, (short) i, (short) 100, false, true);
                        itemz = true;
                        break;
                    }
                }
            }
            if (!itemz) {
                return false;
            }
        } else if (cp != 0 && applyto.getCarnivalParty() != null) {
            applyto.getCarnivalParty().addCP(applyto, cp);
            applyto.CPUpdate(false, applyto.getAvailableCP(), applyto.getTotalCP(), 0);
            for (MapleCharacter chr : applyto.getMap().getCharactersThreadsafe()) {
                chr.CPUpdate(true, applyto.getCarnivalParty().getAvailableCP(), applyto.getCarnivalParty().getTotalCP(), applyto.getCarnivalParty().getTeam());
            }
        } else if (nuffSkill != 0 && applyto.getParty() != null) {
            final MCSkill skil = MapleCarnivalFactory.getInstance().getSkill(nuffSkill);
            if (skil != null) {
                final MapleDisease dis = skil.getDisease();
                for (MapleCharacter chr : applyto.getMap().getCharactersThreadsafe()) {
                    if (applyto.getParty() == null || chr.getParty() == null || (chr.getParty().getId() != applyto.getParty().getId())) {
                        if (skil.targetsAll || Randomizer.nextBoolean()) {
                            if (dis == null) {
                                chr.dispel();
                            } else if (skil.getSkill() == null) {
                                chr.giveDebuff(dis, 1, 30000, dis.getDisease(), 1);
                            } else {
                                chr.giveDebuff(dis, skil.getSkill());
                            }
                            if (!skil.targetsAll) {
                                break;
                            }
                        }
                    }
                }
            }
        } else if ((effectedOnEnemy > 0 || effectedOnAlly > 0) && primary && applyto.inPVP()) {
            final int eventType = Integer.parseInt(applyto.getEventInstance().getProperty("type"));
            if (eventType > 0 || effectedOnEnemy > 0) {
                for (MapleCharacter chr : applyto.getMap().getCharactersThreadsafe()) {
                    if (chr.getId() != applyto.getId() && (effectedOnAlly > 0 ? (chr.getTeam() == applyto.getTeam()) : (chr.getTeam() != applyto.getTeam() || eventType == 0))) {
                        applyTo(applyto, chr, false, pos, newDuration);
                    }
                }
            }
        } else if (mobSkill > 0 && mobSkillLevel > 0 && primary && applyto.inPVP()) {
            if (effectedOnEnemy > 0) {
                final int eventType = Integer.parseInt(applyto.getEventInstance().getProperty("type"));
                for (MapleCharacter chr : applyto.getMap().getCharactersThreadsafe()) {
                    if (chr.getId() != applyto.getId() && (chr.getTeam() != applyto.getTeam() || eventType == 0)) {
                        chr.disease(mobSkill, mobSkillLevel);
                    }
                }
            } else {
                if (sourceid == 2910000 || sourceid == 2910001) { //red flag
                    applyto.getClient().getSession().write(EffectPacket.showBuffEffect(true, applyto, sourceid, SpecialEffectType.MULUNG_DOJO_UP, applyto.getLevel(), level));
                    applyto.getMap().broadcastMessage(applyto, EffectPacket.showBuffEffect(false, applyto, sourceid, SpecialEffectType.MULUNG_DOJO_UP, applyto.getLevel(), level), false);

                    applyto.getClient().getSession().write(EffectPacket.showCraftingEffect("UI/UIWindow2.img/CTF/Effect", (byte) applyto.getDirection(), 0, 0));
                    applyto.getMap().broadcastMessage(applyto, EffectPacket.showCraftingEffect(applyto, "UI/UIWindow2.img/CTF/Effect", (byte) applyto.getDirection(), 0, 0), false);
                    if (applyto.getTeam() == (sourceid - 2910000)) { //restore duh flag
                        if (sourceid == 2910000) {
                            applyto.getEventInstance().broadcastPlayerMsg(-7, "The Red Team's flag has been restored.");
                        } else {
                            applyto.getEventInstance().broadcastPlayerMsg(-7, "The Blue Team's flag has been restored.");
                        }
                        applyto.getMap().spawnAutoDrop(sourceid, applyto.getMap().getGuardians().get(sourceid - 2910000).left);
                    } else {
                        applyto.disease(mobSkill, mobSkillLevel);
                        if (sourceid == 2910000) {
                            applyto.getEventInstance().setProperty("redflag", String.valueOf(applyto.getId()));
                            applyto.getEventInstance().broadcastPlayerMsg(-7, "The Red Team's flag has been captured!");
                            applyto.getClient().getSession().write(EffectPacket.showCraftingEffect("UI/UIWindow2.img/CTF/Tail/Red", (byte) applyto.getDirection(), 600000, 0));
                            applyto.getMap().broadcastMessage(applyto, EffectPacket.showCraftingEffect(applyto, "UI/UIWindow2.img/CTF/Tail/Red", (byte) applyto.getDirection(), 600000, 0), false);
                        } else {
                            applyto.getEventInstance().setProperty("blueflag", String.valueOf(applyto.getId()));
                            applyto.getEventInstance().broadcastPlayerMsg(-7, "The Blue Team's flag has been captured!");
                            applyto.getClient().getSession().write(EffectPacket.showCraftingEffect("UI/UIWindow2.img/CTF/Tail/Blue", (byte) applyto.getDirection(), 600000, 0));
                            applyto.getMap().broadcastMessage(applyto, EffectPacket.showCraftingEffect(applyto, "UI/UIWindow2.img/CTF/Tail/Blue", (byte) applyto.getDirection(), 600000, 0), false);
                        }
                    }
                } else {
                    applyto.disease(mobSkill, mobSkillLevel);
                }
            }
        } else if (randomPickup != null && randomPickup.size() > 0) {
            MapleItemInformationProvider.getInstance().getItemEffect(randomPickup.get(Randomizer.nextInt(randomPickup.size()))).applyTo(applyto);
        } else if (sourceid == 20031203 || sourceid == 20021110 || sourceid == 80001040) {
            applyto.changeMap(sourceid == 20031203 ? 150000000 : sourceid == 20021110 || sourceid == 80001040 ? 101050000 : 100000000, 0);
        }
        for (Entry<MapleTraitType, Integer> t : traits.entrySet()) {
            applyto.getTrait(t.getKey()).addExp(t.getValue(), applyto);
        }
        final SummonMovementType summonMovementType = getSummonMovementType();
        if (summonMovementType != null && (sourceid != 32111006 || (applyfrom.getBuffedValue(MapleBuffStat.REAPER) != null && !primary)) && !applyto.isClone()) {
            int summId = sourceid;
            if (sourceid == 3111002) {
                final Skill elite = SkillFactory.getSkill(3120012);
                if (applyfrom.getTotalSkillLevel(elite) > 0) {
                    return elite.getEffect(applyfrom.getTotalSkillLevel(elite)).applyTo(applyfrom, applyto, primary, pos, newDuration);
                }
            } else if (sourceid == 3211002) {
                final Skill elite = SkillFactory.getSkill(3220012);
                if (applyfrom.getTotalSkillLevel(elite) > 0) {
                    return elite.getEffect(applyfrom.getTotalSkillLevel(elite)).applyTo(applyfrom, applyto, primary, pos, newDuration);
                }
            }
            final MapleSummon tosummon = new MapleSummon(applyfrom, summId, getLevel(), new Point(pos == null ? applyfrom.getTruePosition() : pos), summonMovementType);
            if (!tosummon.isPuppet()) {
                applyfrom.getCheatTracker().resetSummonAttack();
            }
            applyfrom.cancelEffect(this, true, -1, statups);
            applyfrom.getMap().spawnSummon(tosummon);
            applyfrom.addSummon(tosummon);
            tosummon.addHP(info.get(MapleStatInfo.x).shortValue());
            if (isBeholder()) {
                tosummon.addHP((short) 1);
            } else if (sourceid == 4341006) {
                applyfrom.cancelEffectFromBuffStat(MapleBuffStat.SHADOWPARTNER);
            } else if (sourceid == 32111006) {
                return true; //no buff
            } else if (sourceid == 35111002) {
                List<Integer> count = new ArrayList<>();
                final List<MapleSummon> ss = applyfrom.getSummonsReadLock();
                try {
                    for (MapleSummon s : ss) {
                        if (s.getSkill() == sourceid) {
                            count.add(s.getObjectId());
                        }
                    }
                } finally {
                    applyfrom.unlockSummonsReadLock();
                }
                if (count.size() != 3) {
                    return true; //no buff until 3
                }
                applyfrom.getClient().getSession().write(CField.skillCooldown(sourceid, getCooldown(applyfrom)));
                applyfrom.addCooldown(sourceid, System.currentTimeMillis(), getCooldown(applyfrom) * 1000);
                applyfrom.getMap().broadcastMessage(CField.teslaTriangle(applyfrom.getId(), count.get(0), count.get(1), count.get(2)));
            } else if (sourceid == 35121003) {
                applyfrom.getClient().getSession().write(CWvsContext.enableActions()); //doubt we need this at all
            }
        } else if (isMechDoor()) {
            int newId = 0;
            boolean applyBuff = false;
            if (applyto.getMechDoors().size() >= 2) {
                final MechDoor remove = applyto.getMechDoors().remove(0);
                newId = remove.getId();
                applyto.getMap().broadcastMessage(CField.removeMechDoor(remove, true));
                applyto.getMap().removeMapObject(remove);
            } else {
                for (MechDoor d : applyto.getMechDoors()) {
                    if (d.getId() == newId) {
                        applyBuff = true;
                        newId = 1;
                        break;
                    }
                }
            }
            final MechDoor door = new MechDoor(applyto, new Point(pos == null ? applyto.getTruePosition() : pos), newId);
            applyto.getMap().spawnMechDoor(door);
            applyto.addMechDoor(door);
            applyto.getClient().getSession().write(CWvsContext.mechPortal(door.getTruePosition()));
            if (!applyBuff) {
                return true; //do not apply buff until 2 doors spawned
            }
        }
        if (primary && availableMap != null) {
            for (Pair<Integer, Integer> e : availableMap) {
                if (applyto.getMapId() < e.left || applyto.getMapId() > e.right) {
                    applyto.getClient().getSession().write(CWvsContext.enableActions());
                    return true;
                }
            }
        }

        // 處理技能強化技能用的 例如:拔刀術(主動)→拔刀術‧心體技(被動)
        boolean isSpecialSkill = false;
        switch (sourceid) {
            case 41001001: // 拔刀術
            case 13111023: // 阿爾法
            case 32100010: // 死神契約I
                isSpecialSkill = true;
                break;
        }
        if ((overTime && !isEnergyCharge()) || isSpecialSkill) {
            if (applyfrom.isShowInfo()) {
                applyfrom.showMessage(10, "開始 => applyBuffEffect ID: " + this.sourceid + " 持續時間: " + newDuration);
            }
            applyBuffEffect(applyfrom, applyto, primary, newDuration);
        }
        if (skill) {
            removeMonsterBuff(applyfrom);
        }
        if (primary) {
            if ((overTime || isHeal()) && !isEnergyCharge()) {
                applyBuff(applyfrom, newDuration);
            }
            if (isMonsterBuff()) {
                applyMonsterBuff(applyfrom);
            }
        }
        if (isMagicDoor()) { // 時空門Magic Door
            MapleDoor door = new MapleDoor(applyto, new Point(pos == null ? applyto.getTruePosition() : pos), sourceid); // Current Map door
            if (door.getTownPortal() != null) {

                applyto.getMap().spawnDoor(door);
                applyto.addDoor(door);

                MapleDoor townDoor = new MapleDoor(door); // Town door
                applyto.addDoor(townDoor);
                door.getTown().spawnDoor(townDoor);

                if (applyto.getParty() != null) { // update town doors
                    applyto.silentPartyUpdate();
                }
            } else {
                applyto.dropMessage(5, "村莊裡已經沒有可以創造時空門的位置。");
            }
        } else if (isMist()) {
            final Rectangle bounds = calculateBoundingBox(pos != null ? pos : applyfrom.getPosition(), applyfrom.isFacingLeft());
            final MapleMist mist = new MapleMist(bounds, applyfrom, this);
            applyfrom.getMap().spawnMist(mist, getDuration(), false);

        } else if (isTimeLeap()) { // Time Leap
            for (MapleCoolDownValueHolder i : applyto.getCooldowns()) {
                if (i.skillId != 5121010) {
                    applyto.removeCooldown(i.skillId);
                    applyto.getClient().getSession().write(CField.skillCooldown(i.skillId, 0));
                }
            }
        } else {
            for (WeakReference<MapleCharacter> chrz : applyto.getClones()) {
                if (chrz.get() != null) {
                    applyTo(chrz.get(), chrz.get(), primary, pos, newDuration);
                }
            }
        }
        if (fatigueChange != 0 && applyto.getSummonedFamiliar() != null && (familiars == null || familiars.contains(applyto.getSummonedFamiliar().getFamiliar()))) {
            applyto.getSummonedFamiliar().addFatigue(applyto, fatigueChange);
        }
        if (rewardMeso != 0) {
            applyto.gainMeso(rewardMeso, false);
        }
        if (rewardItem != null && totalprob > 0) {
            for (Triple<Integer, Integer, Integer> reward : rewardItem) {
                if (MapleInventoryManipulator.checkSpace(applyto.getClient(), reward.left, reward.mid, "") && reward.right > 0 && Randomizer.nextInt(totalprob) < reward.right) { // Total prob
                    if (GameConstants.getInventoryType(reward.left) == MapleInventoryType.EQUIP) {
                        final Item item = MapleItemInformationProvider.getInstance().getEquipById(reward.left);
                        item.setGMLog("Reward item (effect): " + sourceid + " 時間:" + FileoutputUtil.CurrentReadable_Date());
                        MapleInventoryManipulator.addbyItem(applyto.getClient(), item);
                    } else {
                        MapleInventoryManipulator.addById(applyto.getClient(), reward.left, reward.mid.shortValue(), "Reward item (effect): " + sourceid + " 時間:" + FileoutputUtil.CurrentReadable_Date());
                    }
                }
            }
        }
        if (familiarTarget == 2 && applyfrom.getParty() != null && primary) { //to party
            for (MaplePartyCharacter mpc : applyfrom.getParty().getMembers()) {
                if (mpc.getId() != applyfrom.getId() && mpc.getChannel() == applyfrom.getClient().getChannel() && mpc.getMapid() == applyfrom.getMapId() && mpc.isOnline()) {
                    MapleCharacter mc = applyfrom.getMap().getCharacterById(mpc.getId());
                    if (mc != null) {
                        applyTo(applyfrom, mc, false, null, newDuration);
                    }
                }
            }
        } else if (familiarTarget == 3 && primary) {
            for (MapleCharacter mc : applyfrom.getMap().getCharactersThreadsafe()) {
                if (mc.getId() != applyfrom.getId()) {
                    applyTo(applyfrom, mc, false, null, newDuration);
                }
            }
        }
        if (GameConstants.isTownSkill(sourceid)) {
            applyto.changeMap(info.get(MapleStatInfo.x), 0);
        }
        return true;
    }

    public void handle綠水靈病毒(final MapleCharacter applyto, final int time) {
        final java.util.Timer timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (MapleSummon x : applyto.getAllLinksummon()) {
                    applyto.getMap().removeSummon(x.getObjectId());
                }
                applyto.getAllLinksummon().clear();
                timer.cancel();
            }
        };
        timer.schedule(task, time);
    }
    
    /**
     * 處理召喚獸效果
     *
     * @param applyto 角色
     * @param primary
     * @param pos 坐標
     * @param newDuration 時間
     * @param monid 怪物的ID
     * @return boolean
     */
    public boolean applySummonEffect(MapleCharacter applyto, boolean primary, Point pos, int newDuration, int monid) {
        final SummonMovementType summonMovementType = getSummonMovementType();
        if (summonMovementType == null || (this.sourceid == 32111006 && applyto.getBuffedValue(MapleBuffStat.REAPER) == null) || applyto.isClone()) {
            return false;
        }
        byte[] buff = null;
        int summId = sourceid;
        int localDuration = newDuration;
        Map<MapleBuffStat, Integer> localstatups = statups;
        summId = GameConstants.getMountSkill(summId, applyto);
        if (applyto.isShowInfo()) {
            applyto.showMessage(10, "開始召喚召喚獸 - 召喚獸技能: " + summId + " 持續時間: " + newDuration);
        }
        if (this.sourceid == 32111006) {
            localstatups.put(MapleBuffStat.REAPER, 1);
        }
        if (this.sourceid != 35111002) {
            applyto.cancelEffect(this, true, -1L, localstatups);
        }
        if (is集合船员()) {
            int skilllevel = applyto.getTotalSkillLevel(5220019);
            if (skilllevel > 0) {
                SkillFactory.getSkill(5220019).getEffect(skilllevel).applyBuffEffect(applyto, applyto, primary, newDuration);
            }
        }
        final MapleSummon tosummon = new MapleSummon(applyto, summId, getLevel(), new Point(pos == null ? applyto.getTruePosition() : pos), summonMovementType);
        if (!tosummon.isPuppet()) {
            applyto.getCheatTracker().resetSummonAttack();
        }
        applyto.cancelEffect(this, true, -1, statups);
        applyto.getMap().spawnSummon(tosummon);
        applyto.addSummon(tosummon);
        if ((info.get(MapleStatInfo.hcSummonHp)) > 0) {
            tosummon.addHP(info.get(MapleStatInfo.hcSummonHp).shortValue());
        } else if (this.sourceid == 3221014) {
            tosummon.addHP(info.get(MapleStatInfo.x).shortValue());
        }

        Map<MapleBuffStat, Integer> stat = new HashMap();
        if (sourceid == 13120007) {
            applyto.dispelSkill(13111024);
        } else if (sourceid == 4341006) {
            applyto.cancelEffectFromBuffStat(MapleBuffStat.SHADOWPARTNER);
        } else if (this.sourceid == 14111024) {
            stat.put(MapleBuffStat.暗影僕從, 1);   /////////
            buff = BuffPacket.giveBuff(this.sourceid, localDuration, stat, this, applyto);
        } else if (this.sourceid == 1301013) {
            stat.put(MapleBuffStat.BEHOLDER, Integer.valueOf(level));
            buff = BuffPacket.giveBuff(this.sourceid, localDuration, stat, this, applyto);
        } else if (this.sourceid == 12000022 || this.sourceid == 12100026 || this.sourceid == 12110024 || this.sourceid == 12120007) {
            stat.put(MapleBuffStat.INDIE_MAD, getX());
            buff = BuffPacket.giveBuff(this.sourceid, localDuration, stat, this, applyto);
        } else if (this.sourceid == 12120013 || this.sourceid == 12120014) {
            stat.put(MapleBuffStat.IGNORE_DEF, getY());
            buff = BuffPacket.giveBuff(this.sourceid, localDuration, stat, this, applyto);
        } else if (this.sourceid == 22171052) {
            stat.put(MapleBuffStat.INDIE_ASR_R, getASRRate());
            buff = BuffPacket.giveBuff(this.sourceid, localDuration, stat, this, applyto);
        } else if (this.sourceid == 35121010) {
            stat.put(MapleBuffStat.DAMAGE_BUFF, getX());
            buff = BuffPacket.giveBuff(this.sourceid, localDuration, stat, this, applyto);
        } else if (this.sourceid >= 33001007 && this.sourceid <= 33001015) {
            localDuration = 2100000000;
            stat.put(MapleBuffStat.SUMMON_JAGUAR, (info.get(MapleStatInfo.asrR) << 8) + info.get(MapleStatInfo.criticaldamageMin));
            buff = BuffPacket.giveBuff(this.sourceid, localDuration, stat, this, applyto);
        }

        long startTime = System.currentTimeMillis();
        if (localDuration > 0) {
            CancelEffectAction cancelAction = new CancelEffectAction(applyto, this, startTime, localstatups);
            ScheduledFuture schedule = Timer.BuffTimer.getInstance().schedule(cancelAction, localDuration);
            applyto.registerEffect(this, startTime, schedule, localstatups, false, localDuration, applyto.getId());
        }
        if (this.sourceid == 2111010) {
            applyto.addLinksummon(tosummon);
        }
        int cooldown = getCooldown(applyto);
        if (cooldown > 0) {
            if (sourceid == 35111002) {
                List<Integer> count = new ArrayList<>();
                final List<MapleSummon> summons = applyto.getSummonsReadLock();
                try {
                    for (MapleSummon summon : summons) {
                        if (summon.getSkill() == sourceid) {
                            count.add(summon.getObjectId());
                        }
                    }
                } finally {
                    applyto.unlockSummonsReadLock();
                }
                if (count.size() == 3) {
                    applyto.addCooldown(sourceid, startTime, cooldown * 1000);
                    applyto.getMap().broadcastMessage(SkillPacket.teslaTriangle(applyto.getId(), count.get(0), count.get(1), count.get(2)));
                }
            } else {
                applyto.addCooldown(sourceid, startTime, cooldown * 1000);
            }
        }
        if (buff != null) {
            applyto.getClient().getSession().write(buff);
        }
        return true;
    }

    public boolean isMultiSummon() {//TODO 添加可以召唤多个召唤兽
        switch (sourceid) {
            case 35111002: // 磁場
            case 2111010: // 綠水靈病毒
                return true;
            default:
                return false;
        }
    }
    
    public final boolean applyReturnScroll(final MapleCharacter applyto) {
        if (moveTo != -1) {
            if (applyto.getMap().getReturnMapId() != applyto.getMapId() || sourceid == 2031010 || sourceid == 2030021) {
                MapleMap target;
                if (moveTo == 999999999) {
                    target = applyto.getMap().getReturnMap();
                } else {
                    target = ChannelServer.getInstance(applyto.getClient().getChannel()).getMapFactory().getMap(moveTo);
                    if (target.getId() / 10000000 != 60 && applyto.getMapId() / 10000000 != 61) {
                        if (target.getId() / 10000000 != 21 && applyto.getMapId() / 10000000 != 20) {
                            if (target.getId() / 10000000 != applyto.getMapId() / 10000000) {
                                return false;
                            }
                        }
                    }
                }
                applyto.changeMap(target, target.getPortal(0));
                return true;
            }
        }
        return false;
    }

    private boolean isSoulStone() {
        return skill && sourceid == 22181003 || sourceid == 24111002;
    }

    private void applyBuff(final MapleCharacter applyfrom, int newDuration) {
        if (isSoulStone() && sourceid != 24111002) {
            if (applyfrom.getParty() != null) {
                int membrs = 0;
                for (MapleCharacter chr : applyfrom.getMap().getCharactersThreadsafe()) {
                    if (!chr.isClone() && chr.getParty() != null && chr.getParty().getId() == applyfrom.getParty().getId() && chr.isAlive()) {
                        membrs++;
                    }
                }
                List<MapleCharacter> awarded = new ArrayList<>();
                while (awarded.size() < Math.min(membrs, info.get(MapleStatInfo.y))) {
                    for (MapleCharacter chr : applyfrom.getMap().getCharactersThreadsafe()) {
                        if (chr != null && !chr.isClone() && chr.isAlive() && chr.getParty() != null && chr.getParty().getId() == applyfrom.getParty().getId() && !awarded.contains(chr) && Randomizer.nextInt(info.get(MapleStatInfo.y)) == 0) {
                            awarded.add(chr);
                        }
                    }
                }
                for (MapleCharacter chr : awarded) {
                    applyTo(applyfrom, chr, false, null, newDuration);
                    chr.getClient().getSession().write(EffectPacket.showBuffEffect(true, chr, sourceid, SpecialEffectType.REMOTE_SKILL, applyfrom.getLevel(), level));
                    chr.getMap().broadcastMessage(chr, EffectPacket.showBuffEffect(false, chr, sourceid, SpecialEffectType.REMOTE_SKILL, applyfrom.getLevel(), level), false);
                }
            }
        } else if (isPartyBuff() && (applyfrom.getParty() != null || isGmBuff() || applyfrom.inPVP())) {
            final Rectangle bounds = calculateBoundingBox(applyfrom.getTruePosition(), applyfrom.isFacingLeft());
            final List<MapleMapObject> affecteds = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.PLAYER));

            for (final MapleMapObject affectedmo : affecteds) {
                final MapleCharacter affected = (MapleCharacter) affectedmo;

                if (affected.getId() != applyfrom.getId() && (isGmBuff() || (applyfrom.inPVP() && affected.getTeam() == applyfrom.getTeam() && Integer.parseInt(applyfrom.getEventInstance().getProperty("type")) != 0) || (applyfrom.getParty() != null && affected.getParty() != null && applyfrom.getParty().getId() == affected.getParty().getId()))) {
                    if ((isResurrection() && !affected.isAlive()) || (!isResurrection() && affected.isAlive())) {
                        applyTo(applyfrom, affected, false, null, newDuration);
                        affected.getClient().getSession().write(EffectPacket.showBuffEffect(true, affected, sourceid, SpecialEffectType.REMOTE_SKILL, applyfrom.getLevel(), level));
                        affected.getMap().broadcastMessage(affected, EffectPacket.showBuffEffect(false, affected, sourceid, SpecialEffectType.REMOTE_SKILL, applyfrom.getLevel(), level), false);
                    }
                    if (isTimeLeap()) {
                        for (MapleCoolDownValueHolder i : affected.getCooldowns()) {
                            if (i.skillId != 5121010) {
                                affected.removeCooldown(i.skillId);
                                affected.getClient().getSession().write(CField.skillCooldown(i.skillId, 0));
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeMonsterBuff(final MapleCharacter applyfrom) {
        List<MonsterStatus> cancel = new ArrayList<>();
        switch (sourceid) {
            case 1111007:
            case 51111005: //Mihile's magic crash
            case 1211009:
            case 1311007:
                cancel.add(MonsterStatus.WEAPON_DEFENSE_UP);
                cancel.add(MonsterStatus.MAGIC_DEFENSE_UP);
                cancel.add(MonsterStatus.WEAPON_ATTACK_UP);
                cancel.add(MonsterStatus.MAGIC_ATTACK_UP);
                break;
            default:
                return;
        }
        final Rectangle bounds = calculateBoundingBox(applyfrom.getTruePosition(), applyfrom.isFacingLeft());
        final List<MapleMapObject> affected = applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(MapleMapObjectType.MONSTER));
        int i = 0;

        for (final MapleMapObject mo : affected) {
            if (makeChanceResult()) {
                for (MonsterStatus stat : cancel) {
                    ((MapleMonster) mo).cancelStatus(stat);
                }
            }
            i++;
            if (i >= info.get(MapleStatInfo.mobCount)) {
                break;
            }
        }
    }

    public final void applyMonsterBuff(final MapleCharacter applyfrom) {
        final Rectangle bounds = calculateBoundingBox(applyfrom.getTruePosition(), applyfrom.isFacingLeft());
        final boolean pvp = applyfrom.inPVP();
        final MapleMapObjectType objType = pvp ? MapleMapObjectType.PLAYER : MapleMapObjectType.MONSTER;
        final List<MapleMapObject> affected = sourceid == 35111005 ? applyfrom.getMap().getMapObjectsInRange(applyfrom.getTruePosition(), Double.POSITIVE_INFINITY, Arrays.asList(objType)) : applyfrom.getMap().getMapObjectsInRect(bounds, Arrays.asList(objType));
        int i = 0;

        for (final MapleMapObject mo : affected) {
            if (makeChanceResult()) {
                for (Map.Entry<MonsterStatus, Integer> stat : getMonsterStati().entrySet()) {
                    if (pvp) {
                        MapleCharacter chr = (MapleCharacter) mo;
                        MapleDisease d = MonsterStatus.getLinkedDisease(stat.getKey());
                        if (d != null) {
                            chr.giveDebuff(d, stat.getValue(), getDuration(), d.getDisease(), 1);
                        }
                    } else {
                        MapleMonster mons = (MapleMonster) mo;
                        if (sourceid == 35111005 && mons.getStats().isBoss()) {
                            break;
                        }
                        mons.applyStatus(applyfrom, new MonsterStatusEffect(stat.getKey(), stat.getValue(), sourceid, null, false), isPoison(), isSubTime(sourceid) ? getSubTime() : getDuration(), true, this);
                    }
                }
                if (pvp && skill) {
                    MapleCharacter chr = (MapleCharacter) mo;
                    handleExtraPVP(applyfrom, chr);
                }
            }
            i++;
            if (i >= info.get(MapleStatInfo.mobCount) && sourceid != 35111005) {
                break;
            }
        }
    }

    public final boolean isSubTime(final int source) {
        switch (source) {
            case 1201006: // threaten
            case 23111008: // spirits
            case 23111009:
            case 23111010:
            case 31101003:
            case 31121003:
            case 31121005:
                return true;//u there?
        }
        return false;
    }

    public final void handleExtraPVP(MapleCharacter applyfrom, MapleCharacter chr) {
        if (sourceid == 2311005 || sourceid == 5121005 || sourceid == 1201006 || (MapleJob.isBeginner(sourceid / 10000) && sourceid % 10000 == 104)) { //doom, threaten, snatch
            final long starttime = System.currentTimeMillis();

            final int localsourceid = sourceid == 5121005 ? 90002000 : sourceid;
            final Map<MapleBuffStat, Integer> localstatups = new EnumMap<>(MapleBuffStat.class);
            if (sourceid == 2311005) {
                localstatups.put(MapleBuffStat.MORPH, 7);
            } else if (sourceid == 1201006) {
                localstatups.put(MapleBuffStat.THREATEN_PVP, (int) level);
            } else if (sourceid == 5121005) {
                localstatups.put(MapleBuffStat.SNATCH, 1);
            } else {
                localstatups.put(MapleBuffStat.MORPH, info.get(MapleStatInfo.x));
            }
            chr.registerEffect(this, starttime, BuffTimer.getInstance().schedule(new CancelEffectAction(chr, this, starttime, localstatups), isSubTime(sourceid) ? getSubTime() : getDuration()), localstatups, false, getDuration(), applyfrom.getId());
            chr.getClient().getSession().write(BuffPacket.giveBuff(localsourceid, getDuration(), localstatups, this, chr));
        }
    }

    public final Rectangle calculateBoundingBox(final Point posFrom, final boolean facingLeft) {
        return calculateBoundingBox(posFrom, facingLeft, lt, rb, info.get(MapleStatInfo.range));
    }

    public final Rectangle calculateBoundingBox(final Point posFrom, final boolean facingLeft, int addedRange) {
        return calculateBoundingBox(posFrom, facingLeft, lt, rb, info.get(MapleStatInfo.range) + addedRange);
    }

    public static Rectangle calculateBoundingBox(final Point posFrom, final boolean facingLeft, final Point lt, final Point rb, final int range) {
        if (lt == null || rb == null) {
            return new Rectangle((facingLeft ? (-200 - range) : 0) + posFrom.x, (-100 - range) + posFrom.y, 200 + range, 100 + range);
        }
        Point mylt;
        Point myrb;
        if (facingLeft) {
            mylt = new Point(lt.x + posFrom.x - range, lt.y + posFrom.y);
            myrb = new Point(rb.x + posFrom.x, rb.y + posFrom.y);
        } else {
            myrb = new Point(lt.x * -1 + posFrom.x + range, rb.y + posFrom.y);
            mylt = new Point(rb.x * -1 + posFrom.x, lt.y + posFrom.y);
        }
        return new Rectangle(mylt.x, mylt.y, myrb.x - mylt.x, myrb.y - mylt.y);
    }

    public final double getMaxDistanceSq() { //lt = infront of you, rb = behind you; not gonna distanceSq the two points since this is in relative to player position which is (0,0) and not both directions, just one
        final int maxX = Math.max(Math.abs(lt == null ? 0 : lt.x), Math.abs(rb == null ? 0 : rb.x));
        final int maxY = Math.max(Math.abs(lt == null ? 0 : lt.y), Math.abs(rb == null ? 0 : rb.y));
        return (maxX * maxX) + (maxY * maxY);
    }

    public final void setDuration(int d) {
        this.info.put(MapleStatInfo.time, d);
    }

    public final void silentApplyBuff(final MapleCharacter chr, final long starttime, final int localDuration, final Map<MapleBuffStat, Integer> statup, final int cid) {
        chr.registerEffect(this, starttime, BuffTimer.getInstance().schedule(new CancelEffectAction(chr, this, starttime, statup),
                ((starttime + localDuration) - System.currentTimeMillis())), statup, true, localDuration, cid);

        final SummonMovementType summonMovementType = getSummonMovementType();
        if (summonMovementType != null) {
            final MapleSummon tosummon = new MapleSummon(chr, this, chr.getTruePosition(), summonMovementType);
            if (!tosummon.isPuppet()) {
                chr.getCheatTracker().resetSummonAttack();
                chr.getMap().spawnSummon(tosummon);
                chr.addSummon(tosummon);
                tosummon.addHP(info.get(MapleStatInfo.x).shortValue());
                if (isBeholder()) {
                    tosummon.addHP((short) 1);
                }
            }
        }
    }

    public final void applyKaiser_Combo(MapleCharacter applyto, short combo) {
        EnumMap stat = new EnumMap(MapleBuffStat.class);
        this.combo = combo;
        stat.put(MapleBuffStat.KAISER_COMBO, (int) combo);
        applyto.getClient().getSession().write(CWvsContext.BuffPacket.giveBuff(0, 99999, stat, this, applyto));
    }

    public final void applyXenon_Combo(MapleCharacter applyto, int combo) {
        this.combo = combo;
        EnumMap stat = new EnumMap(MapleBuffStat.class);
        stat.put(MapleBuffStat.SUPPLY_SURPLUS, combo);
        applyto.getClient().getSession().write(CWvsContext.BuffPacket.giveBuff(0, 99999, stat, this, applyto));
    }

    public final void applyComboBuff(MapleCharacter applyto, short combo) {
        this.combo = combo;
        EnumMap stat = new EnumMap(MapleBuffStat.class);
        stat.put(MapleBuffStat.ARAN_COMBO, (int) combo);

        long starttime = System.currentTimeMillis();

        applyto.registerEffect(this, starttime, null, applyto.getId());
        applyto.getClient().getSession().write(CWvsContext.BuffPacket.giveBuff(this.sourceid, 99999, stat, this, applyto));
    }

    public final void applyBlackBlessingBuff(MapleCharacter applyto, int combo) {
        EnumMap stat = new EnumMap(MapleBuffStat.class);
        stat.put(MapleBuffStat.BLACK_BLESSING, combo);
        applyto.getClient().getSession().write(CWvsContext.BuffPacket.giveBuff(this.sourceid, 0, stat, this, applyto));
        applyto.getClient().getSession().write(CField.EffectPacket.showBlackBlessingEffect(applyto, 27100003));
    }

    public final void applyLunarTideBuff(MapleCharacter applyto) {
        EnumMap stat = new EnumMap(MapleBuffStat.class);
        double hpx = applyto.getStat().getMaxHp() / applyto.getStat().getHp();
        double mpx = applyto.getStat().getMaxMp() / applyto.getStat().getMp();
        stat.put(MapleBuffStat.LUNAR_TIDE, hpx >= mpx ? 2 : 1);
        applyto.getClient().getSession().write(CWvsContext.BuffPacket.giveBuff(this.sourceid, 99999999, stat, this, applyto));
    }

    public final void applyEnergyBuff(final MapleCharacter applyto, final boolean infinity, int targets) {
        final long starttime = System.currentTimeMillis();
        if (infinity) {
            applyto.registerEffect(this, starttime, null, applyto.getId());
            applyto.getClient().getSession().write(BuffPacket.giveEnergyChargeTest(0, info.get(MapleStatInfo.time) / 1000, targets));
        } else {
            final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
            stat.put(MapleBuffStat.ENERGY_CHARGE, 10000);
            applyto.cancelEffect(this, true, -1, stat);
            final CancelEffectAction cancelAction = new CancelEffectAction(applyto, this, starttime, stat);
            final ScheduledFuture<?> schedule = BuffTimer.getInstance().schedule(cancelAction, ((starttime + info.get(MapleStatInfo.time)) - System.currentTimeMillis()));
            applyto.registerEffect(this, starttime, schedule, stat, false, info.get(MapleStatInfo.time), applyto.getId());
            applyto.getMap().broadcastMessage(applyto, BuffPacket.giveEnergyChargeTest(applyto.getId(), 10000, info.get(MapleStatInfo.time) / 1000), false);

        }
    }

    private void applyBuffEffect(final MapleCharacter applyfrom, final MapleCharacter applyto, final boolean primary, final int newDuration) {
        int localDuration = newDuration;
        if (primary) {
            localDuration = Math.max(newDuration, alchemistModifyVal(applyfrom, localDuration, false));
        }
        Map<MapleBuffStat, Integer> localstatups = statups, maskedStatups = null;
        boolean normal = true, showEffect = primary;
        int maskedDuration = 0;
        switch (sourceid) {
            case 61101002: //意志之劍
            case 61120007: //進階意志之劍
                if (applyfrom.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11) == null) {
                    normal = false;
                } else {
                    this.statups.put(MapleBuffStat.TEMPEST_BLADES, applyfrom.getSkillLevel(61101002));
                    this.weapon = applyfrom.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -11).getItemId();
                    maskedStatups = new EnumMap(localstatups);
                    maskedStatups.clear();
                    maskedStatups.put(MapleBuffStat.TEMPEST_BLADES, applyfrom.getSkillLevel(61101002));
                }
                break;
            case 42101002: //影朋·花狐
                if (applyto.getHaku() != null) {
                    applyto.getHaku().sendStats();
                    applyto.getMap().broadcastMessage(applyto, CField.spawnHaku_change0(applyto.getId()), true);
                    applyto.getMap().broadcastMessage(applyto, CField.spawnHaku_change1(applyto.getHaku()), true);
                    applyto.getMap().broadcastMessage(applyto, CField.spawnHaku_bianshen(applyto.getId(), applyto.getHaku().getObjectId(), applyto.getHaku().getStats()), true);
                }
                break;
            case 60001216:
            case 60001217:
                if (applyfrom.getStatForBuff(MapleBuffStat.KAISER_MODE_CHANGE) == null) {
                    break;
                }
                applyfrom.cancelEffectFromBuffStat(MapleBuffStat.KAISER_MODE_CHANGE);
                break;
            case 27110007:
                localstatups = new EnumMap(MapleBuffStat.class);
                double hpx = applyfrom.getStat().getMaxHp() / applyfrom.getStat().getHp();
                double mpx = applyfrom.getStat().getMaxMp() / applyfrom.getStat().getMp();
                localstatups.put(MapleBuffStat.LUNAR_TIDE, hpx >= mpx ? 2 : 1);
                break;            
            case 4221013: {
                localstatups = new EnumMap<>(MapleBuffStat.class);
                localstatups.put(MapleBuffStat.INDIE_PAD, info.get(MapleStatInfo.x) + (info.get(MapleStatInfo.kp) * applyfrom.currentBattleshipHP()));
                applyfrom.setBattleshipHP(0);
                applyfrom.refreshBattleshipHP();
                break;
            }
            case 5311004: {
                final int zz = Randomizer.nextInt(4) + 1;
                applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showDiceEffect(applyto, sourceid, zz, -1, level), false);
                applyto.getClient().getSession().write(CField.EffectPacket.showDiceEffect(sourceid, zz, -1, level));
                localstatups = new EnumMap<>(MapleBuffStat.class);
                localstatups.put(MapleBuffStat.BARREL_ROLL, zz);
                break;
            }
            case 5211011:
            case 5211015:
            case 5211016: {
                if (applyfrom.getTotalSkillLevel(5220019) > 0) {
                    SkillFactory.getSkill(5220019).getEffect(applyfrom.getTotalSkillLevel(5220019)).applyBuffEffect(applyfrom, applyto, primary, newDuration);
                }
                break;
            }
            case 42101001:
                SkillFactory.getSkill(42100010).getEffect(applyfrom.getTotalSkillLevel(42101001)).applyBuffEffect(applyfrom, applyto, primary, newDuration);
                normal = false;
                break;
            case 35111013:
            case 15111011:
            case 5111007:
            case 5811007:
            case 5911007:
            case 5311005:
            case 5711011:
            case 5211007: {//dice
                final int zz = Randomizer.nextInt(6) + 1;
                applyto.getMap().broadcastMessage(applyto, EffectPacket.showDiceEffect(applyto, sourceid, zz, -1, level), false);
                applyto.getClient().getSession().write(EffectPacket.showDiceEffect(sourceid, zz, -1, level));
                if (zz <= 1) {
                    return;
                }
                localstatups = new EnumMap<>(MapleBuffStat.class);
                localstatups.put(MapleBuffStat.DICE_ROLL, zz);
                applyto.getClient().getSession().write(BuffPacket.giveDice(zz, sourceid, localDuration, localstatups));
                normal = false;
                showEffect = false;
                break;
            }
            case 5720005:
            case 5120012:
            case 5220014:
            case 5320007: {//dice
                final int zz = Randomizer.nextInt(6) + 1;
                final int zz2 = makeChanceResult() ? (Randomizer.nextInt(6) + 1) : 0;
                applyto.getMap().broadcastMessage(applyto, EffectPacket.showDiceEffect(applyto, sourceid, zz, zz2 > 0 ? -1 : 0, level), false);
                applyto.getClient().getSession().write(EffectPacket.showDiceEffect(sourceid, zz, zz2 > 0 ? -1 : 0, level));
                if (zz <= 1 && zz2 <= 1) {
                    return;
                }
                final int buffid = zz == zz2 ? (zz * 100) : (zz <= 1 ? zz2 : (zz2 <= 1 ? zz : (zz * 10 + zz2)));
                if (buffid >= 100) { //just because of animation lol
                    applyto.dropMessage(-6, "[Double Lucky Dice] You have rolled a Double Down! (" + (buffid / 100) + ")");
                } else if (buffid >= 10) {
                    applyto.dropMessage(-6, "[Double Lucky Dice] You have rolled two dice. (" + (buffid / 10) + " and " + (buffid % 10) + ")");
                }
                localstatups = new EnumMap<>(MapleBuffStat.class);
                localstatups.put(MapleBuffStat.DICE_ROLL, buffid);
                applyto.getClient().getSession().write(BuffPacket.giveDice(zz, sourceid, localDuration, localstatups));
                normal = false;
                showEffect = false;
                break;
            }
            case 20031209: // 卡牌審判
            case 20031210: // 審判
                // 0 = 沒有, 1 = 爆擊, 2 = 掉寶, 3 = 抗性, 4 = 防禦, 5 = 攻擊回復HP, 6 <= 無
                int zz = Randomizer.nextInt(this.sourceid == 20031209 ? 2 : 5) + 1;
                int skillid = 24100003;
                if (applyto.getSkillLevel(24120002) > 0) {
                    skillid = 24120002;
                }
                applyto.setCardStack((byte) 0);
                applyto.resetRunningStack();
                applyto.addRunningStack(skillid == 24100003 ? 5 : 10);
                applyto.getMap().broadcastMessage(applyto, PhantomPacket.gainCardStack(applyto, applyto.getRunningStack(), skillid == 24120002 ? 2 : 1, skillid, 0, skillid == 24100003 ? 5 : 10), true);
                applyto.getMap().broadcastMessage(applyto, CField.EffectPacket.showDiceEffect(applyto, this.sourceid, zz, -1, this.level), false);
                applyto.getClient().getSession().write(CField.EffectPacket.showDiceEffect(this.sourceid, zz, -1, this.level));
                localstatups = new EnumMap(MapleBuffStat.class);
                localstatups.put(MapleBuffStat.JUDGMENT_DRAW, zz);
                if (zz == 5) {
                    localstatups.put(MapleBuffStat.ABSORB_DAMAGE_HP, this.info.get(MapleStatInfo.z));
                }
                applyto.getClient().getSession().write(CWvsContext.BuffPacket.giveBuff(this.sourceid, localDuration, localstatups, this, applyto));
                normal = false;
                showEffect = false;
                break;
            case 33101006: {//jaguar oshi
                applyto.clearLinkMid();
                MapleBuffStat theBuff = null;
                int theStat = info.get(MapleStatInfo.y);
                switch (Randomizer.nextInt(6)) {
                    case 0:
                        theBuff = MapleBuffStat.CRITICAL_RATE_BUFF;
                        break;
                    case 1:
                        theBuff = MapleBuffStat.MP_BUFF;
                        break;
                    case 2:
                        theBuff = MapleBuffStat.DAMAGE_TAKEN_BUFF;
                        theStat = info.get(MapleStatInfo.x);
                        break;
                    case 3:
                        theBuff = MapleBuffStat.DODGE_CHANGE_BUFF;
                        theStat = info.get(MapleStatInfo.x);
                        break;
                    case 4:
                        theBuff = MapleBuffStat.DAMAGE_BUFF;
                        break;
                    case 5:
                        theBuff = MapleBuffStat.ATTACK_BUFF;
                        break;
                }
                localstatups = new EnumMap<>(MapleBuffStat.class);
                localstatups.put(theBuff, theStat);
                applyto.getClient().getSession().write(BuffPacket.giveBuff(sourceid, localDuration, localstatups, this, applyto));
                normal = false;
                break;
            }
            case 8006:
            case 10008006:
            case 20008006:
            case 20018006:
            case 20028006:
            case 30008006:
            case 30018006:
            case 5121009: // Speed Infusion
            case 15111005:
            case 5001005: // Dash
            case 4321000: //tornado spin
            case 15001003: {
                applyto.getClient().getSession().write(BuffPacket.givePirate(statups, localDuration / 1000, sourceid));
                if (!applyto.isHidden()) {
                    applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignPirate(statups, localDuration / 1000, applyto.getId(), sourceid), false);
                }
                normal = false;
                break;
            }
            case 24121004:
                applyto.getClient().getSession().write(PhantomPacket.giveAriaBuff(level, sourceid, localDuration));
                //System.out.println("Applying aria buff");
                break;
            case 5211006: // Homing Beacon
            case 22151002: //killer wings
            case 5220011: {// Bullseye
                if (applyto.getFirstLinkMid() > 0) {
                    applyto.getClient().getSession().write(BuffPacket.cancelHoming());
                    applyto.getClient().getSession().write(BuffPacket.giveHoming(sourceid, applyto.getFirstLinkMid(), 1));
                } else {
                    return;
                }
                normal = false;
                break;
            }
            case 112000000:
            case 112100000:
            case 112110003: {
                if (applyto.isHidden()) {
                    break;
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.HOMING_BEACON, info.get(MapleStatInfo.x));
                break;
            }
            case 2120010:
            case 2220010:
            case 2320011: //arcane aim
                if (applyto.getFirstLinkMid() > 0) {
                    applyto.getClient().getSession().write(BuffPacket.giveArcane(sourceid, applyto.getAllLinkMid()));
                } else {
                    return;
                }
                normal = false;
                break;
            case 30011001:
            case 30001001: { // Wind Walk
                if (applyto.isHidden()) {
                    break;
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.INFILTRATE, 0);
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            case 13101006: { // Wind Walk
                if (applyto.isHidden()) {
                    break;
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.DARKSIGHT, 1); // HACK..
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            case 4001003: {
                if (applyfrom.getTotalSkillLevel(4330001) > 0 && ((applyfrom.getJob() >= 430 && applyfrom.getJob() <= 434) || (applyfrom.getJob() == 400 && applyfrom.getSubcategory() == 1))) {
                    SkillFactory.getSkill(4330001).getEffect(applyfrom.getTotalSkillLevel(4330001)).applyBuffEffect(applyfrom, applyto, primary, newDuration);
                    return;
                } //fallthrough intended
            }
            case 4330001:
            case 20031211:
            case 14001003: { // Dark Sight
                if (applyto.isHidden()) {
                    return; //don't even apply the buff
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.DARKSIGHT, 0);
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            case 23111005: {
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.WATER_SHIELD, info.get(MapleStatInfo.x));
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            case 23101003: {
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.DAMAGE_R, info.get(MapleStatInfo.damage));
                stat.put(MapleBuffStat.CRITICAL_RATE, info.get(MapleStatInfo.x));
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            //case 22131001: {//magic shield
            //final List<Pair<MapleBuffStat, Integer>> stat = Collections.singletonList(new Pair<MapleBuffStat, Integer>(MapleBuffStat.MAGIC_SHIELD, x));
            //applyto.getMap().broadcastMessage(applyto, CField.giveForeignBuff(applyto.getId(), stat, this), false);
            //break;
            //}
            case 32121003: { //twister
                if (applyto.isHidden()) {
                    break;
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.TORNADO, info.get(MapleStatInfo.x));
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            /*
             ID: 110001501 NAME: Bear Mode
             ID: 110001502 NAME: Snow Leopard Mode
             ID: 110001503 NAME: Hawk Mode
             ID: 110001504 NAME: Cat Mode
             */
            case 110001501:
            case 110001502:
            case 110001503:
            case 110001504: {
                //applyto.cancelEffectFromBuffStat(MapleBuffStat.ANIMAL_SELECT);
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                //stat.clear();
                //stat.clear();
                //applyto.getClient().getSession().write(BuffPacket.giveBuff(sourceid, 2147483647, stat, this));
                // stat.put(MapleBuffStat.ANIMAL_SELECT, info.get(MapleStatInfo.x));
                System.out.println("You chose " + sourceid);
                break;
            }
            case 41001001: // 拔刀術
                if (applyfrom.getTotalSkillLevel(41110008) > 0) {
                    SkillFactory.getSkill(41110008).getEffect(applyfrom.getTotalSkillLevel(41110008)).applyTo(applyfrom);
                    return;
                }
                break;
            case 32100010: // 死神契約I
                if (applyfrom.getTotalSkillLevel(32110017) > 0) {
                    if (applyfrom.getTotalSkillLevel(32110017) > 0) {
                        SkillFactory.getSkill(32120019).getEffect(applyfrom.getTotalSkillLevel(32120019)).applyTo(applyfrom);
                        return;
                    } else {
                        SkillFactory.getSkill(32110017).getEffect(applyfrom.getTotalSkillLevel(32110017)).applyTo(applyfrom);
                        return;
                    }
                }
                break;
            case 13111023: // 阿爾法
                if (applyfrom.getTotalSkillLevel(13120008) > 0) {
                    SkillFactory.getSkill(13120008).getEffect(applyfrom.getTotalSkillLevel(13120008)).applyTo(applyfrom);
                    return;
                }
                break;
            case 27101202: // 黑暗之眼
                showEffect = false;
                break;
            case 11101022: // 沉月                
            case 11111022: // 旭日
                if (applyto.getBuffedValue(MapleBuffStat.SOLUNA_EFFECT) != null) {
                    applyto.cancelEffectFromBuffStat(MapleBuffStat.SOLUNA_EFFECT);
                }
                break;
            case 11121005: // 雙重力量
                if (applyto.getBuffedValue(MapleBuffStat.SOLUNA_EFFECT) != null) {
                    localstatups = new EnumMap<>(MapleBuffStat.class);
                    localstatups.put(MapleBuffStat.SOLUNA_EFFECT, applyto.getBuffedValue(MapleBuffStat.SOLUNA_EFFECT));
                    normal = true;
                }
                break;
            case 11121011: // 雙重力量（沉月）
            case 11121012: // 雙重力量（旭日）
                break;
            case 1211008: { //lightning
                if (applyto.getBuffedValue(MapleBuffStat.WK_CHARGE) != null && applyto.getBuffSource(MapleBuffStat.WK_CHARGE) != sourceid) {
                    localstatups = new EnumMap<>(MapleBuffStat.class);
                    localstatups.put(MapleBuffStat.LIGHTNING_CHARGE, 1);
                } else if (!applyto.isHidden()) {
                    final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                    stat.put(MapleBuffStat.WK_CHARGE, 1);
                    applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                }
                applyto.getClient().getSession().write(BuffPacket.giveBuff(sourceid, localDuration, localstatups, this, applyto));
                normal = false;
                break;
            }
            case 35111004: {//siege
                if (applyto.getBuffedValue(MapleBuffStat.MECH_CHANGE) != null && applyto.getBuffSource(MapleBuffStat.MECH_CHANGE) == 35121005) {
                    SkillFactory.getSkill(35121013).getEffect(level).applyBuffEffect(applyfrom, applyto, primary, newDuration);
                    return;
                }
                if (applyto.isHidden()) {
                    break;
                }
                if (applyto.getBuffedValue(MapleBuffStat.MECH_CHANGE) != null) {
                    applyto.cancelEffectFromBuffStat(MapleBuffStat.MECH_CHANGE);
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.MECH_CHANGE, 1);
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            case 35001001: //flame
            case 35101009:
            case 35121013:
            case 35121005: { //missile
                if (applyto.isHidden()) {
                    break;
                }
                if (applyto.getBuffedValue(MapleBuffStat.MECH_CHANGE) != null) {
                    applyto.cancelEffectFromBuffStat(MapleBuffStat.MECH_CHANGE);
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.MECH_CHANGE, 1);
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            case 1220013: {
                if (applyto.isHidden()) {
                    break;
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.DIVINE_SHIELD, 1);
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            case 1111002:
            case 1101013:
            case 11111001: { // Combo
                if (applyto.isHidden()) {
                    break;
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.COMBO, 0);
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            case 3101004:
            case 3201004:
            case 13101024://new cyngus
            case 13101003: { // Soul Arrow
                if (applyto.isHidden()) {
                    break;
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.SOULARROW, 0);
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            case 2321005: //holy shield
                applyto.cancelEffectFromBuffStat(MapleBuffStat.BLESS);
                break;
            case 4211008:
            case 4331002:
            case 4111002:
            case 14111000: { // Shadow Partner
                if (applyto.isHidden()) {
                    break;
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.SHADOWPARTNER, info.get(MapleStatInfo.x));
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            case 15111006: { // Spark
                localstatups = new EnumMap<>(MapleBuffStat.class);
                localstatups.put(MapleBuffStat.SPARK, info.get(MapleStatInfo.x));
                applyto.getClient().getSession().write(BuffPacket.giveBuff(sourceid, localDuration, localstatups, this, applyto));
                normal = false;
                break;
            }
            case 4341002: { // Final Cut
                localstatups = new EnumMap<>(MapleBuffStat.class);
                localstatups.put(MapleBuffStat.FINAL_CUT, info.get(MapleStatInfo.y));
                applyto.getClient().getSession().write(BuffPacket.giveBuff(sourceid, localDuration, localstatups, this, applyto));
                normal = false;
                break;
            }
            case 3211005: {// golden eagle
                if (applyfrom.getTotalSkillLevel(3220005) > 0) {
                    SkillFactory.getSkill(3220005).getEffect(applyfrom.getTotalSkillLevel(3220005)).applyBuffEffect(applyfrom, applyto, primary, newDuration);
                }
                break;
            }
            case 3111005: {// golden hawk
                if (applyfrom.getTotalSkillLevel(3120006) > 0) {
                    SkillFactory.getSkill(3120006).getEffect(applyfrom.getTotalSkillLevel(3120006)).applyBuffEffect(applyfrom, applyto, primary, newDuration);
                }
                break;
            }
            case 1211006: // wk charges
            case 1211004:
            case 1221004:
            case 11111007:
            case 51111003: // Mihile's Radiant Charge
            case 21101006:
            case 21111005:
            case 15101006: { // Soul Arrow
                if (applyto.isHidden()) {
                    break;
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.WK_CHARGE, 1);
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            case 3120006:
            case 3220005: { // Spirit Link
                if (applyto.isHidden()) {
                    break;
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.SPIRIT_LINK, 0);
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            case 31121005: { // Dark Metamorphosis
                if (applyto.isHidden()) {
                    break;
                }
                final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                stat.put(MapleBuffStat.DARK_METAMORPHOSIS, 6); // mob count
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                break;
            }
            case 2121004:
            case 2221004:
            case 2321004: { //Infinity
                maskedDuration = alchemistModifyVal(applyfrom, 4000, false);
                break;
            }
            case 4331003: { // Owl Spirit
                localstatups = new EnumMap<>(MapleBuffStat.class);
                localstatups.put(MapleBuffStat.OWL_SPIRIT, info.get(MapleStatInfo.y));
                applyto.getClient().getSession().write(BuffPacket.giveBuff(sourceid, localDuration, localstatups, this, applyto));
                applyto.setBattleshipHP(info.get(MapleStatInfo.x)); //a variable that wouldnt' be used by a db
                normal = false;
                break;
            }
            case 1121010: // Enrage
                applyto.handleOrbconsume(10);
                break;
            case 2022746: //angel bless
            case 2022747: //d.angel bless
            case 2022823:
                if (applyto.isHidden()) {
                    break;
                }
                applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), maskedStatups == null ? localstatups : maskedStatups, this), false);
                break;
            case 31011001: {
                applyto.setExceed((short) 0);// set exceed to 0, it is very important, in server the value of exceed must be set 0 once skill 31011001 is used.
                applyto.getClient().getSession().write(JobPacket.AvengerPacket.cancelExceed());  // here and the below are used to tell clients that the exceed should be set 0.
                applyto.addHP((int) ((applyto.getStat().getCurrentMaxHp() * (level / 100.0D)) * (getX() / 100.0D)));
                break;
            }
            case 35001002:
                if (applyfrom.getTotalSkillLevel(35120000) > 0) {
                    SkillFactory.getSkill(35120000).getEffect(applyfrom.getTotalSkillLevel(35120000)).applyBuffEffect(applyfrom, applyto, primary, newDuration);
                    return;
                }

            //fallthrough intended
            default:
                if (isPirateMorph()) {
                    final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                    stat.put(MapleBuffStat.MORPH, getMorph(applyto));
                    applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                    applyto.getClient().getSession().write(BuffPacket.giveBuff(sourceid, localDuration, stat, this, applyto));
                    maskedStatups = new EnumMap<>(localstatups);
                    maskedStatups.remove(MapleBuffStat.MORPH);
                    normal = false;
                } else if (isMorph()) {
                    if (applyto.isHidden()) {
                        break;
                    }
                    if (isIceKnight()) {
                        //odd
                        final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                        stat.put(MapleBuffStat.ICE_KNIGHT, 2);
                        applyto.getClient().getSession().write(BuffPacket.giveBuff(0, localDuration, stat, this, applyto));
                    }
                    final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                    stat.put(MapleBuffStat.MORPH, getMorph(applyto));
                    applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                } else if (isInflation()) {
                    if (applyto.isHidden()) {
                        break;
                    }
                    final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                    stat.put(MapleBuffStat.GIANT_POTION, (int) inflation);
                    applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                } else if (charColor > 0) {
                    if (applyto.isHidden()) {
                        break;
                    }
                    final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                    stat.put(MapleBuffStat.FAMILIAR_SHADOW, 1);
                    applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                } else if (isMonsterRiding()) {
                    final int mountid = parseMountInfo(applyto, sourceid);
                    final int mountid2 = parseMountInfo_Pure(applyto, sourceid);
                    if (mountid != 0 && mountid2 != 0) {
                        final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(statups);
                        stat.remove(MapleBuffStat.MONSTER_RIDING); //先移除原本的
                        stat.put(MapleBuffStat.MONSTER_RIDING, mountid); //加入新的
                        final EnumMap<MapleBuffStat, Integer> stat2 = new EnumMap<>(statups);
                        stat2.remove(MapleBuffStat.MONSTER_RIDING); //先移除原本的
                        stat2.put(MapleBuffStat.MONSTER_RIDING, mountid2);
                        applyto.cancelEffectFromBuffStat(MapleBuffStat.POWERGUARD);
                        applyto.cancelEffectFromBuffStat(MapleBuffStat.MANA_REFLECTION);
                        //applyto.getClient().getSession().write(BuffPacket.giveMount(mountid2, sourceid, stat));
                        applyto.getClient().getSession().write(BuffPacket.giveBuff(sourceid, 0, stat2, this, applyto));
                        applyto.getMap().broadcastMessage(applyto, BuffPacket.showMonsterRiding(applyto.getId(), stat, mountid, sourceid), false);
                    } else {
                        return;
                    }
                    normal = false;
                } else if (isSoaring()) {
                    if (applyto.isHidden()) {
                        break;
                    }
                    final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                    stat.put(MapleBuffStat.SOARING, 1);
                    applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                } else if (berserk > 0) {
                    if (applyto.isHidden()) {
                        break;
                    }
                    final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                    stat.put(MapleBuffStat.PYRAMID_PQ, 0);
                    applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                } else if (isBerserkFury() || berserk2 > 0) {
                    if (applyto.isHidden()) {
                        break;
                    }
                    final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                    stat.put(MapleBuffStat.BERSERK_FURY, 1);
                    applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                } else if (isDivineBody()) {
                    if (applyto.isInvincible() && sourceid == 1010) {
                        if (applyto.isShowInfo()) {
                            applyto.dropMessage(-6, "定制技能 - GM無敵[原技能: 金剛霸體](在角色無敵狀態才會生效)");
                        }
                        localDuration = 2100000000;
                        localstatups = new EnumMap<>(MapleBuffStat.class);
                        localstatups.put(MapleBuffStat.INVINCIBILITY, 1);
                        localstatups.put(MapleBuffStat.DIVINE_BODY, 1);
                    }
                    if (applyto.isHidden()) {
                        break;
                    }
                    final EnumMap<MapleBuffStat, Integer> stat = new EnumMap<>(MapleBuffStat.class);
                    stat.put(MapleBuffStat.DIVINE_BODY, 1);
                    applyto.getMap().broadcastMessage(applyto, BuffPacket.giveForeignBuff(applyto.getId(), stat, this), false);
                }
                break;
        }
        if (showEffect && !applyto.isHidden()) {
            applyto.getMap().broadcastMessage(applyto, EffectPacket.showBuffEffect(false, applyto, sourceid, SpecialEffectType.LOCAL_SKILL, applyto.getLevel(), level), false);
        }
        if (isMechPassive()) {
            applyto.getClient().getSession().write(EffectPacket.showBuffEffect(true, applyto, sourceid - 1000, SpecialEffectType.LOCAL_SKILL, applyto.getLevel(), level, (byte) 1));
        }
        if (!isMonsterRiding() && !isMechDoor() && getSummonMovementType() == null) {
            applyto.cancelEffect(this, true, -1, localstatups);
        }

        final long starttime = System.currentTimeMillis();
        final CancelEffectAction cancelAction = new CancelEffectAction(applyto, this, starttime, localstatups);
        final ScheduledFuture<?> schedule = BuffTimer.getInstance().schedule(cancelAction, maskedDuration > 0 ? maskedDuration : localDuration);
        applyto.registerEffect(this, starttime, schedule, localstatups, false, localDuration, applyfrom.getId());

        // Broadcast effect to self
        if (normal && localstatups.size() > 0) {
            applyto.getClient().getSession().write(BuffPacket.giveBuff((skill ? sourceid : -sourceid), localDuration, maskedStatups == null ? localstatups : maskedStatups, this, applyto));
        }
    }

    public static int parseMountInfo(final MapleCharacter player, final int skillid) {
        switch (skillid) {
            case 80001000:
            case 1004: // Monster riding
            case 11004: // Monster riding
            case 10001004:
            case 20001004:
            case 20011004:
            case 20021004:
                if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -118) != null && player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -119) != null) {
                    return player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -118).getItemId();
                }
                return parseMountInfo_Pure(player, skillid);
            default:
                return GameConstants.getMountItem(skillid, player);
        }
    }

    public static int parseMountInfo_Pure(final MapleCharacter player, final int skillid) {
        switch (skillid) {
            case 80001000:
            case 1004: // Monster riding
            case 11004: // Monster riding
            case 10001004:
            case 20001004:
            case 20011004:
            case 20021004:
                if (player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -18) != null && player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -19) != null) {
                    return player.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -18).getItemId();
                }
                return 0;
            default:
                return GameConstants.getMountItem(skillid, player);
        }
    }

    private int calcHPChange(final MapleCharacter applyfrom, final boolean primary) {
        int hpchange = 0;
        if (this.sourceid == 9001000) {
            hpchange = 500000;
        }
        if (info.get(MapleStatInfo.hp) != 0) {
            if (!skill) {
                if (primary) {
                    hpchange += alchemistModifyVal(applyfrom, info.get(MapleStatInfo.hp), true);
                } else {
                    hpchange += info.get(MapleStatInfo.hp);
                }
                if (applyfrom.hasDisease(MapleDisease.ZOMBIFY)) {
                    hpchange /= 2;
                }
            } else { // assumption: this is heal
                hpchange += makeHealHP(info.get(MapleStatInfo.hp) / 100.0, applyfrom.getStat().getTotalMagic(), 3, 5);
                if (applyfrom.hasDisease(MapleDisease.ZOMBIFY)) {
                    hpchange = -hpchange;
                }
            }
        }
        if (hpR != 0) {
            hpchange += (int) (applyfrom.getStat().getCurrentMaxHp() * hpR) / (applyfrom.hasDisease(MapleDisease.ZOMBIFY) ? 2 : 1);
        }
        // actually receivers probably never get any hp when it's not heal but whatever
        if (primary) {
            if (info.get(MapleStatInfo.hpCon) != 0) {
                hpchange -= info.get(MapleStatInfo.hpCon);
            }
        }
        switch (this.sourceid) {
            case 4211001: // Chakra
                final PlayerStats stat = applyfrom.getStat();
                int v42 = getY() + 100;
                int v38 = Randomizer.rand(1, 100) + 100;
                hpchange = (int) ((v38 * stat.getLuk() * 0.033 + stat.getDex()) * v42 * 0.002);
                hpchange += makeHealHP(getY() / 100.0, applyfrom.getStat().getTotalLuk(), 2.3, 3.5);
                break;
        }
        return hpchange;
    }

    private static int makeHealHP(double rate, double stat, double lowerfactor, double upperfactor) {
        return (int) ((Math.random() * ((int) (stat * upperfactor * rate) - (int) (stat * lowerfactor * rate) + 1)) + (int) (stat * lowerfactor * rate));
    }

    private int calcMPChange(final MapleCharacter applyfrom, final boolean primary) {
        int mpchange = 0;
        if (info.get(MapleStatInfo.mp) != 0) {
            if (primary) {
                mpchange += alchemistModifyVal(applyfrom, info.get(MapleStatInfo.mp), false); // recovery up doesn't apply for mp
            } else {
                mpchange += info.get(MapleStatInfo.mp);
            }
        }
        if (mpR != 0) {
            mpchange += (int) (applyfrom.getStat().getCurrentMaxMp(applyfrom.getJob()) * mpR);
        }
        if (MapleJob.is惡魔殺手(applyfrom.getJob())) {
            mpchange = 0;
        }
        if (primary) {
            if (info.get(MapleStatInfo.mpCon) != 0 && !MapleJob.is惡魔殺手(applyfrom.getJob())) {
                boolean free = false;
                if (applyfrom.getJob() == 411 || applyfrom.getJob() == 412) {
                    final Skill expert = SkillFactory.getSkill(4110012);
                    if (applyfrom.getTotalSkillLevel(expert) > 0) {
                        final MapleStatEffect eff = expert.getEffect(applyfrom.getTotalSkillLevel(expert));
                        if (eff.makeChanceResult()) {
                            free = true;
                        }
                    }
                }
                if (applyfrom.getBuffedValue(MapleBuffStat.INFINITY) != null) {
                    mpchange = 0;
                } else if (!free) {
                    mpchange -= (info.get(MapleStatInfo.mpCon) - (info.get(MapleStatInfo.mpCon) * applyfrom.getStat().mpconReduce / 100)) * (applyfrom.getStat().mpconPercent / 100.0);
                }
            } else if (info.get(MapleStatInfo.forceCon) != 0) {
                if (applyfrom.getBuffedValue(MapleBuffStat.BOUNDLESS_RAGE) != null) {
                    mpchange = 0;
                } else {
                    mpchange -= info.get(MapleStatInfo.forceCon);
                }
            }
        }

        return mpchange;
    }

    public final int alchemistModifyVal(final MapleCharacter chr, final int val, final boolean withX) {
        if (!skill) { // RecoveryUP only used for hp items and skills
            return (val * (100 + (withX ? chr.getStat().RecoveryUP : chr.getStat().BuffUP)) / 100);
        }
        return (val * (100 + (withX ? chr.getStat().RecoveryUP : (chr.getStat().BuffUP_Skill + (getSummonMovementType() == null ? 0 : chr.getStat().BuffUP_Summon)))) / 100);
    }

    public final int calcPowerChange(final MapleCharacter applyfrom) {
        int powerchange = 0;
        if (info.get(MapleStatInfo.powerCon) != 0 && MapleJob.is傑諾(applyfrom.getJob())) {
            //if (applyfrom.getBuffedValue(MapleBuffStat.AMARANTH_GENERATOR) != null) {
            //    powerchange = 0;
            //} else {
            powerchange = info.get(MapleStatInfo.powerCon);
            //}
        }
        return powerchange;
    }

    public final void setSourceId(final int newid) {
        sourceid = newid;
    }

    public final boolean isGmBuff() {
        switch (sourceid) {
            case 10001075: // Empress Prayer
            case 9001000: // 終極祝福
            case 9001001: // 終極輕功
            case 9001002: // 終極祈禱
            case 9001003: // GM的祝福
            case 9001005: // 復活
            case 9001008: // hyper body

            case 9101000:
            case 9101001:
            case 9101002:
            case 9101003:
            case 9101005:
            case 9101008:
                return true;
            default:
                return MapleJob.isBeginner(sourceid / 10000) && sourceid % 10000 == 1005;
        }
    }

    public final boolean isInflation() {
        return inflation > 0;
    }

    public final int getInflation() {
        return inflation;
    }

    public final boolean isEnergyCharge() {
        return skill && (sourceid == 5110001 || sourceid == 15100004);
    }

    public boolean isMonsterBuff() {
        switch (sourceid) {
            case 1211013: // Threaten
            case 1201006: // threaten
            case 2101003: // fp slow
            case 2201003: // il slow
            case 5011002:
            case 12101001: // cygnus slow
            case 2211004: // il seal
            case 2111004: // fp seal
            case 12111002: // cygnus seal
            case 2311005: // doom
            case 4111003: // shadow web
            case 14111001: // cygnus web
            case 4121004: // Ninja ambush
            case 4221004: // Ninja ambush
            case 22151001:
            case 22121000:
            case 22161002:
            case 4321002:
            case 4341003:
            case 90001002:
            case 90001003:
            case 90001004:
            case 90001005:
            case 90001006:
            case 1111007:
            case 51111005: //Mihile's magic crash
            case 1211009:
            case 1311007:
            case 35111005:
            case 32120000:
            case 32120001:
                return skill;
        }
        return false;
    }

    public final void setPartyBuff(boolean pb) {
        this.partyBuff = pb;
    }

    private boolean isPartyBuff() {
        if (lt == null || rb == null || !partyBuff) {
            return isSoulStone() && sourceid != 24111002;
        }
        switch (sourceid) {
            case 1211003:
            case 1211004:
            case 1211005:
            case 1211006:
            case 1211007:
            case 1211008:
            case 1221003:
            case 1221004:
            case 11111007:
            case 51111003: // Mihile's Radiant Charge
            case 12101005:
            case 4311001:
            case 4331003:
            case 4341002:
            case 35121005:
                return false;
        }
        return !GameConstants.isNoDelaySkill(sourceid);
    }

    public final boolean isArcane() {
        return skill && (sourceid == 2320011 || sourceid == 2220010 || sourceid == 2120010);
    }

    public final boolean isHeal() {
        return skill && (sourceid == 2301002 || sourceid == 9101000 || sourceid == 9001000);
    }

    public final boolean isResurrection() {
        return skill && (sourceid == 9001005 || sourceid == 9101005 || sourceid == 2321006);
    }

    public final boolean isTimeLeap() {
        return skill && sourceid == 5121010;
    }

    public final int getHp() {
        return info.get(MapleStatInfo.hp);
    }

    public final int getMp() {
        return info.get(MapleStatInfo.mp);
    }

    public final int getDOTStack() {
        return info.get(MapleStatInfo.dotSuperpos);
    }

    public final double getHpR() {
        return hpR;
    }

    public final double getMpR() {
        return mpR;
    }

    public final int getMastery() {
        return info.get(MapleStatInfo.mastery);
    }

    public final int getWatk() {
        return info.get(MapleStatInfo.pad);
    }

    public final int getMatk() {
        return info.get(MapleStatInfo.mad);
    }

    public final int getWdef() {
        return info.get(MapleStatInfo.pdd);
    }

    public final int getMdef() {
        return info.get(MapleStatInfo.mdd);
    }

    public final int getAcc() {
        return info.get(MapleStatInfo.acc);
    }

    public final int getAccR() {
        return info.get(MapleStatInfo.ar);
    }

    public final int getAvoid() {
        return info.get(MapleStatInfo.eva);
    }

    public final int getSpeed() {
        return info.get(MapleStatInfo.speed);
    }

    public final int getJump() {
        return info.get(MapleStatInfo.jump);
    }

    public final int getSpeedMax() {
        return info.get(MapleStatInfo.speedMax);
    }

    public final int getPassiveSpeed() {
        return info.get(MapleStatInfo.psdSpeed);
    }

    public final int getPassiveJump() {
        return info.get(MapleStatInfo.psdJump);
    }

    public final int getDuration() {
        return info.get(MapleStatInfo.time);
    }

    public final int getSubTime() {
        return info.get(MapleStatInfo.subTime);
    }

    public final boolean isOverTime() {
        return overTime;
    }

    public final Map<MapleBuffStat, Integer> getStatups() {
        return statups;
    }

    public final boolean sameSource(final MapleStatEffect effect) {
        boolean sameSrc = this.sourceid == effect.sourceid;
        switch (this.sourceid) { // All these are passive skills, will have to cast the normal ones.
            case 35120000: // Extreme Mech
                sameSrc = effect.sourceid == 35001002;
                break;
            case 35121013: // Mech: Siege Mode
                sameSrc = effect.sourceid == 35111004;
                break;
        }
        return effect != null && sameSrc && this.skill == effect.skill;
    }

    public final int getCr() {
        return info.get(MapleStatInfo.cr);
    }

    public final int getT() {
        return info.get(MapleStatInfo.t);
    }

    public final int getU() {
        return info.get(MapleStatInfo.u);
    }

    public final int getV() {
        return info.get(MapleStatInfo.v);
    }

    public final int getW() {
        return info.get(MapleStatInfo.w);
    }

    public final int getX() {
        return info.get(MapleStatInfo.x);
    }

    public final int getY() {
        return info.get(MapleStatInfo.y);
    }

    public final int getZ() {
        return info.get(MapleStatInfo.z);
    }

    public final int getDamage() {
        return info.get(MapleStatInfo.damage);
    }

    public final int getPVPDamage() {
        return info.get(MapleStatInfo.PVPdamage);
    }

    public final int getAttackCount() {
        return info.get(MapleStatInfo.attackCount);
    }

    public final int getBulletCount() {
        return info.get(MapleStatInfo.bulletCount);
    }

    public final int getBulletConsume() {
        return info.get(MapleStatInfo.bulletConsume);
    }

    public final int getOnActive() {
        return info.get(MapleStatInfo.onActive);
    }

    public final int getMobCount() {
        return info.get(MapleStatInfo.mobCount);
    }

    public final int getMoneyCon() {
        return moneyCon;
    }

    public final int getCooltimeReduceR() {
        return info.get(MapleStatInfo.coolTimeR);
    }

    public final int getMesoAcquisition() {
        return info.get(MapleStatInfo.mesoR);
    }

    public final int getCooldown(final MapleCharacter chra) {
        if (chra.getStat().coolTimeR > 0) {
            return Math.max(0, ((info.get(MapleStatInfo.cooltime) * (100 - (chra.getStat().coolTimeR / 100))) - chra.getStat().reduceCooltime));
        }
        return Math.max(0, (info.get(MapleStatInfo.cooltime) - chra.getStat().reduceCooltime));
    }

    public final Map<MonsterStatus, Integer> getMonsterStati() {
        return monsterStatus;
    }

    public final int getBerserk() {
        return berserk;
    }

    public final boolean isHide() {
        return skill && (sourceid == 9001004 || sourceid == 9101004);
    }

    public final boolean isDragonBlood() {
        return skill && sourceid == 1311008;
    }

    public final boolean isRecovery() {
        return skill && (sourceid == 1001 || sourceid == 10001001 || sourceid == 20001001 || sourceid == 20011001 || sourceid == 20021001 || sourceid == 11001 || sourceid == 35121005);
    }

    public final boolean isBerserk() {
        return skill && sourceid == 1320006;
    }

    public final boolean isBeholder() {
        return skill && sourceid == 1321007 || sourceid == 1301013 /*|| sourceid == 1311013*/;
    }

    public final boolean isMPRecovery() {
        return skill && sourceid == 5101005;
    }

    public final boolean isInfinity() {
        return skill && (sourceid == 2121004 || sourceid == 2221004 || sourceid == 2321004);
    }

    public final boolean isMonsterRiding_() {
        return skill && (sourceid == 1004 || sourceid == 10001004 || sourceid == 20001004 || sourceid == 20011004 || sourceid == 11004 || sourceid == 20021004 || sourceid == 80001000);
    }

    public final boolean isMonsterRiding() {
        return skill && (isMonsterRiding_() || GameConstants.getMountItem(sourceid, null) != 0);
    }

    public final boolean isMagicDoor() {
        return skill && (sourceid == 2311002 || sourceid % 10000 == 8001);
    }

    public final boolean isMesoGuard() {
        return skill && sourceid == 4211005;
    }

    public final boolean isMechDoor() {
        return skill && sourceid == 35101005;
    }

    public final boolean isComboRecharge() {
        return skill && sourceid == 21111009;
    }

    public final boolean isDragonBlink() {
        return skill && sourceid == 22141004;
    }

    public final boolean isCharge() {
        switch (sourceid) {
            case 1211003:
            case 1211008:
            case 11111007:
            case 51111003: // Mihile's Radiant Charge
            case 12101005:
            case 15101006:
            case 21111005:
                return skill;
        }
        return false;
    }

    public final boolean isPoison() {
        return info.get(MapleStatInfo.dot) > 0 && info.get(MapleStatInfo.dotTime) > 0;
    }

    public boolean isMist() {
        return skill && (sourceid == 2111003 || sourceid == 4221006 || sourceid == 12111005 || sourceid == 14111006 || sourceid == 22161003 || sourceid == 32121006 || sourceid == 1076 || sourceid == 11076 || sourceid == 2311011 || sourceid == 4121015 || sourceid == 42111004 || sourceid == 42121005); // poison mist, smokescreen and flame gear, recovery aura
    }

    private boolean isSpiritClaw() {
        return skill && sourceid == 4111009 || sourceid == 14111007 || sourceid == 5201008;
    }

    private boolean isSpiritBlast() {
        return skill && sourceid == 5201008;
    }

    private boolean isDispel() {
        return skill && (sourceid == 2311001 || sourceid == 9001000 || sourceid == 9101000);
    }

    private boolean isHeroWill() {
        switch (sourceid) {
            case 1121011:
            case 1221012:
            case 1321010:
            case 2121008:
            case 2221008:
            case 2321009:
            case 3121009:
            case 3221008:
            case 4121009:
            case 4221008:
            case 5121008:
            case 5221010:
            case 21121008:
            case 22171004:
            case 4341008:
            case 32121008:
            case 33121008:
            case 35121008:
            case 5321008:
            case 23121008:
            case 24121009:
            case 5721002:
                return skill;
        }
        return false;
    }

    public final boolean isAranCombo() {
        return sourceid == 21000000;
    }

    public final boolean isCombo() {
        switch (sourceid) {
            case 1111002:
            case 11111001: // Combo
            case 1101013:
                return skill;
        }
        return false;
    }

    public final boolean isPirateMorph() {
        switch (sourceid) {
            case 13111005:
            case 15111002:
            case 5111005:
            case 5121003:
            case 1203:
                return skill;
        }
        return false;
    }

    public final boolean isMorph() {
        return morphId > 0;
    }

    public final int getMorph() {
        switch (sourceid) {
            case 15111002:
            case 5111005:
                return 1000;
            case 5121003:
            case 1203:
                return 1001;
            case 5101007:
                return 1002;
            case 13111005:
                return 1003;
        }
        return morphId;
    }

    public final boolean isDivineBody() {
        return skill && MapleJob.isBeginner(sourceid / 10000) && sourceid % 10000 == 1010;
    }

    public final boolean isDivineShield() {
        switch (sourceid) {
            case 1220013:
                return skill;
        }
        return false;
    }

    public final boolean isBerserkFury() {
        return skill && MapleJob.isBeginner(sourceid / 10000) && sourceid % 10000 == 1011;
    }

    public final int getMorph(final MapleCharacter chr) {
        final int morph = getMorph();
        switch (morph) {
            case 1000:
            case 1001:
            case 1003:
                return morph + (chr.getGender() == 1 ? 100 : 0);
        }
        return morph;
    }

    public final byte getLevel() {
        return level;
    }

    public final SummonMovementType getSummonMovementType() {
        if (!skill) {
            return null;
        }
        switch (sourceid) {
            case 3211002: // puppet sniper
            case 3111002: // puppet ranger
            case 33111003:
            case 13111024: // Emerald Flower
            case 13111004: // puppet cygnus
            case 5211001: // octopus - pirate
            case 5220002: // advanced octopus - pirate
            case 4341006:
            case 35111002:
            case 35111005:
            case 35111011:
            case 35121009:
            case 35121010:
            case 35121011:
            case 4111007: //dark flare
            case 4211007: //dark flare
            case 14111010: //dark flare
            case 33101008:
            case 35121003:
            case 3120012:
            case 3220012:
            case 5321003:
            case 5321004:
            case 5320011:
            case 5211014:
            case 5711001: // turret
            case 42100010:
            case 61111002: //Stone Dragon
                return SummonMovementType.STATIONARY;
            case 3211005: // golden eagle
            case 3111005: // golden hawk
            case 3101007:
            case 3201007:
            case 33111005:
            case 3221005: // frostprey
            case 3121006: // phoenix
            case 23111008:
            case 23111009:
            case 23111010:
                return SummonMovementType.CIRCLE_FOLLOW;
            case 5211002: // bird - pirate
                return SummonMovementType.CIRCLE_STATIONARY;
            case 32111006: //reaper
            case 5211011:
            case 5211015:
            case 5211016:
                return SummonMovementType.WALK_STATIONARY;
            case 1321007: // beholder
            case 1301013: // Evil Eye
//            case 1311013: // Evil Eye of Domination
            case 2121005: // elquines
            case 2221005: // ifrit
            case 2321003: // bahamut
            case 12111004: // Ifrit
            case 11001004: // soul
            case 12001004: // flame
            case 13001004: // storm
            case 14001005: // darkness
            case 15001004: // lightning
            case 35111001:
            case 35111010:
            case 35111009:
            case 42111003: // Kishin Shoukan
            case 42101021: // Foxfire
            case 42121021: // Foxfire
                return SummonMovementType.FOLLOW;
        }
        if (isAngel()) {
            return SummonMovementType.FOLLOW;
        }
        return null;
    }

    public final boolean isAngel() {
        return GameConstants.isAngel(sourceid);
    }

    public boolean isSkillWithBuff() {
        switch (this.sourceid) {
            case 2201009://寒冰迅移
            case 2321052://天堂之門
            case 2101010://燎原之火
                return this.skill;
        }
        return false;
    }

    public final boolean isSkill() {
        return skill;
    }

    public final int getSourceId() {
        return sourceid;
    }

    public final boolean isIceKnight() {
        return skill && MapleJob.isBeginner(sourceid / 10000) && sourceid % 10000 == 1105;
    }

    public final boolean isSoaring() {
        return isSoaring_Normal() || isSoaring_Mount();
    }

    public final boolean isSoaring_Normal() {
        return skill && MapleJob.isBeginner(sourceid / 10000) && sourceid % 10000 == 1026;
    }

    public final boolean isSoaring_Mount() {
        return skill && ((MapleJob.isBeginner(sourceid / 10000) && sourceid % 10000 == 1142) || sourceid == 80001089);
    }

    public final boolean isFinalAttack() {
        switch (sourceid) {
            case 13101002:
            case 11101002:
            case 51100002:
                return skill;
        }
        return false;
    }

    public final boolean isMistEruption() {
        switch (sourceid) {
            case 2121003:
                return skill;
        }
        return false;
    }

    public final boolean isShadow() {
        switch (sourceid) {
            case 4111002: // shadowpartner
            case 14111000: // cygnus
            case 4211008:
            case 4331002:// Mirror Image
                return skill;
        }
        return false;
    }

    public final boolean isMechPassive() {
        switch (sourceid) {
            //case 35121005:
            case 35121013:
                return true;
        }
        return false;
    }

    /**
     *
     * @return true if the effect should happen based on it's probablity, false
     * otherwise
     */
    public final boolean makeChanceResult() {
        return info.get(MapleStatInfo.prop) >= 100 || Randomizer.nextInt(100) < info.get(MapleStatInfo.prop);
    }

    public final int getProb() {
        return info.get(MapleStatInfo.prop);
    }

    public final short getIgnoreMob() {
        return ignoreMob;
    }

    public final int getEnhancedHP() {
        return info.get(MapleStatInfo.emhp);
    }

    public final int getEnhancedMP() {
        return info.get(MapleStatInfo.emmp);
    }

    public final int getEnhancedWatk() {
        return info.get(MapleStatInfo.epad);
    }

    public final int getEnhancedWdef() {
        return info.get(MapleStatInfo.pdd);
    }

    public final int getEnhancedMatk() {
        return info.get(MapleStatInfo.emad);
    }

    public final int getEnhancedMdef() {
        return info.get(MapleStatInfo.emdd);
    }

    public final int getDOT() {
        return info.get(MapleStatInfo.dot);
    }

    public final int getDOTTime() {
        return info.get(MapleStatInfo.dotTime);
    }

    public final int getCriticalMax() {
        return info.get(MapleStatInfo.criticaldamageMax);
    }

    public final int getCriticalMin() {
        return info.get(MapleStatInfo.criticaldamageMin);
    }

    public final int getASRRate() {
        return info.get(MapleStatInfo.asrR);
    }

    public final int getTERRate() {
        return info.get(MapleStatInfo.terR);
    }

    public final int getDAMRate() {
        return info.get(MapleStatInfo.damR);
    }

    public final int getHpToDamage() {
        return info.get(MapleStatInfo.mhp2damX);
    }

    public final int getMpToDamage() {
        return info.get(MapleStatInfo.mmp2damX);
    }

    public int getLevelToMaxHp() {
        return info.get(MapleStatInfo.lv2mhp);
    }

    public int getLevelToMaxMp() {
        return info.get(MapleStatInfo.lv2mmp);
    }

    public final int getLevelToDamage() {
        return info.get(MapleStatInfo.lv2damX);
    }

    public final int getLevelToWatk() {
        return info.get(MapleStatInfo.lv2pdX);
    }

    public final int getLevelToMatk() {
        return info.get(MapleStatInfo.lv2mdX);
    }

    public final int getEXPLossRate() {
        return info.get(MapleStatInfo.expLossReduceR);
    }

    public final int getBuffTimeRate() {
        return info.get(MapleStatInfo.bufftimeR);
    }

    public final int getSuddenDeathR() {
        return info.get(MapleStatInfo.suddenDeathR);
    }

    public final int getPercentAcc() {
        return info.get(MapleStatInfo.accR);
    }

    public final int getPercentAvoid() {
        return info.get(MapleStatInfo.evaR);
    }

    public final int getSummonTimeInc() {
        return info.get(MapleStatInfo.summonTimeR);
    }

    public final int getMPConsumeEff() {
        return info.get(MapleStatInfo.mpConEff);
    }

    public final short getMesoRate() {
        return mesoR;
    }

    public final int getEXP() {
        return exp;
    }

    public final int getAttackX() {
        return info.get(MapleStatInfo.padX);
    }

    public final int getMagicX() {
        return info.get(MapleStatInfo.madX);
    }

    public final int getPercentHP() {
        return info.get(MapleStatInfo.mhpR);
    }

    public final int getPercentMP() {
        return info.get(MapleStatInfo.mmpR);
    }

    public final int getConsume() {
        return consumeOnPickup;
    }

    public final int getSelfDestruction() {
        return info.get(MapleStatInfo.selfDestruction);
    }

    public final int getCharColor() {
        return charColor;
    }

    public final List<Integer> getPetsCanConsume() {
        return petsCanConsume;
    }

    public final boolean isReturnScroll() {
        return skill && (sourceid == 80001040 || sourceid == 20021110 || sourceid == 20031203);
    }

    public final boolean isMechChange() {
        switch (sourceid) {
            case 35111004: //siege
            case 35001001: //flame
            case 35101009:
            case 35121013:
            case 35121005:
                return skill;
        }
        return false;
    }

    public final boolean isAnimalMode() {
        return skill && (sourceid == 110001501 || sourceid == 110001502 || sourceid == 110001503 || sourceid == 110001504);
    }

    public final int getRange() {
        return info.get(MapleStatInfo.range);
    }

    public final int getER() {
        return info.get(MapleStatInfo.er);
    }

    public final int getPrice() {
        return info.get(MapleStatInfo.price);
    }

    public final int getExtendPrice() {
        return info.get(MapleStatInfo.extendPrice);
    }

    public final int getPeriod() {
        return info.get(MapleStatInfo.period);
    }

    public final int getReqGuildLevel() {
        return info.get(MapleStatInfo.reqGuildLevel);
    }

    public final byte getEXPRate() {
        return expR;
    }

    public final short getLifeID() {
        return lifeId;
    }

    public final short getUseLevel() {
        return useLevel;
    }

    public final byte getSlotCount() {
        return slotCount;
    }

    public final int getStr() {
        return info.get(MapleStatInfo.str);
    }

    public final int getStrX() {
        return info.get(MapleStatInfo.strX);
    }

    public final int getDex() {
        return info.get(MapleStatInfo.dex);
    }

    public final int getDexX() {
        return info.get(MapleStatInfo.dexX);
    }

    public final int getInt() {
        return info.get(MapleStatInfo.int_);
    }

    public final int getIntX() {
        return info.get(MapleStatInfo.intX);
    }

    public final int getLuk() {
        return info.get(MapleStatInfo.luk);
    }

    public final int getLukX() {
        return info.get(MapleStatInfo.lukX);
    }

    public final int getMaxHpX() {
        return info.get(MapleStatInfo.mhpX);
    }

    public final int getMaxMpX() {
        return info.get(MapleStatInfo.mmpX);
    }

    public final int getMaxDemonFury() {
        return info.get(MapleStatInfo.MDF);
    }

    public final int getAccX() {
        return info.get(MapleStatInfo.accX);
    }

    public final int getMPConReduce() {
        return info.get(MapleStatInfo.mpConReduce);
    }

    public final int getIndieMHp() {
        return info.get(MapleStatInfo.indieMhp);
    }

    public final int getIndieMMp() {
        return info.get(MapleStatInfo.indieMmp);
    }

    public final int getIndieAllStat() {
        return info.get(MapleStatInfo.indieAllStat);
    }

    public final byte getType() {
        return type;
    }

    public int getBossDamage() {
        return info.get(MapleStatInfo.bdR);
    }

    public int getInterval() {
        return interval;
    }

    public ArrayList<Pair<Integer, Integer>> getAvailableMaps() {
        return availableMap;
    }

    public int getWDEFX() {
        return info.get(MapleStatInfo.pddX);
    }

    public int getMDEFX() {
        return info.get(MapleStatInfo.mddX);
    }

    public int getWDEFRate() {
        return info.get(MapleStatInfo.pddR);
    }

    public int getMDEFRate() {
        return info.get(MapleStatInfo.mddR);
    }

    public final int getSoulMPCon() {
        return info.get(MapleStatInfo.soulmpCon);
    }

    public int getWeapon() {
        return weapon;
    }

    public final int getCombo() {
        return this.combo;
    }

    public static class CancelEffectAction implements Runnable {

        private final MapleStatEffect effect;
        private final WeakReference<MapleCharacter> target;
        private final long startTime;
        private final Map<MapleBuffStat, Integer> statup;

        public CancelEffectAction(final MapleCharacter target, final MapleStatEffect effect, final long startTime, final Map<MapleBuffStat, Integer> statup) {
            this.effect = effect;
            this.target = new WeakReference<>(target);
            this.startTime = startTime;
            this.statup = statup;
        }

        @Override
        public void run() {
            final MapleCharacter realTarget = target.get();
            if (realTarget != null && !realTarget.isClone()) {
                realTarget.cancelEffect(effect, false, startTime, statup);
            }
        }
    }

    public final boolean isUnstealable() {
        for (MapleBuffStat b : statups.keySet()) {
            if (b == MapleBuffStat.MAPLE_WARRIOR) {
                return true;
            }
        }
        return sourceid == 4221013;
    }
    
    public boolean is集合船员() {
        switch (this.sourceid) {
            case 5210015:
            case 5210016:
            case 5210017:
            case 5210018:
                return this.skill;
        }
        return false;
    }
    
    public void applyQuiverKartrige(final MapleCharacter applyfrom) {
        int mode = applyfrom.getBuffedValue(MapleBuffStat.QUIVER_KARTRIGE);
        mode = (mode == 0 ? 1 : mode == 1 ? 2 : mode == 2 ? 3 : 1);
        int skillid = 3101009;
        boolean add = false;
        Skill skills = SkillFactory.getSkill(skillid);
        int skilllevel = applyfrom.getSkillLevel(skillid);
        MapleStatEffect infoEffect = skills.getEffect(skilllevel);

        infoEffect.getStatups().put(MapleBuffStat.QUIVER_KARTRIGE, mode);

        infoEffect.applyBuffEffect(applyfrom, 2100000000);
    }
    
    public void applyBuffEffect(MapleCharacter chr, int newDuration) {
        applyBuffEffect(chr, chr, false, newDuration);
    }
}
