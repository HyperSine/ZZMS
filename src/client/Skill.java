package client;

import constants.GameConstants;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import provider.MapleData;
import provider.MapleDataTool;
import server.MapleStatEffect;
import server.Randomizer;
import server.life.Element;
import tools.Pair;

public class Skill implements Comparator<Skill> {

    private String name = "";
    private final List<MapleStatEffect> effects = new ArrayList<>();
    private List<MapleStatEffect> pvpEffects = null;
    private List<Integer> animation = null;
    private final List<Pair<String, Integer>> requiredSkill = new ArrayList<>();
    private Element element = Element.NEUTRAL;
    private int id, animationTime = 0, masterLevel = 0, maxLevel = 0, delay = 0, trueMax = 0, eventTamingMob = 0, skillTamingMob = 0, skillType = 0; //4 is alert
    private boolean invisible = false, chargeskill = false, timeLimited = false, combatOrders = false, pvpDisabled = false, magic = false, casterMove = false, pushTarget = false, pullTarget = false;
    private int hyper = 0;
    private int reqLev = 0;
    private int fixLevel;

    public Skill(final int id) {
        super();
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Skill loadFromData(final int id, final MapleData data, final MapleData delayData) {
        Skill ret = new Skill(id);

        boolean isBuff;
        final int skillType = MapleDataTool.getInt("skillType", data, -1);
        final String elem = MapleDataTool.getString("elemAttr", data, null);
        if (elem != null) {
            ret.element = Element.getFromChar(elem.charAt(0));
        }
        ret.skillType = skillType;
        ret.invisible = MapleDataTool.getInt("invisible", data, 0) > 0;
        ret.timeLimited = MapleDataTool.getInt("timeLimited", data, 0) > 0;
        ret.combatOrders = MapleDataTool.getInt("combatOrders", data, 0) > 0;
        ret.hyper = MapleDataTool.getInt("hyper", data, 0);
        ret.reqLev = MapleDataTool.getInt("reqLev", data, 0);
        ret.fixLevel = MapleDataTool.getInt("fixLevel", data, 0);
        ret.masterLevel = MapleDataTool.getInt("masterLevel", data, 0);
        if ((id == 22111001 || id == 22140000 || id == 22141002)) {
            ret.masterLevel = 5; //hack
        }
        ret.eventTamingMob = MapleDataTool.getInt("eventTamingMob", data, 0);
        ret.skillTamingMob = MapleDataTool.getInt("skillTamingMob", data, 0);
        final MapleData inf = data.getChildByPath("info");
        if (inf != null) {
            ret.pvpDisabled = MapleDataTool.getInt("pvp", inf, 1) <= 0;
            ret.magic = MapleDataTool.getInt("magicDamage", inf, 0) > 0;
            ret.casterMove = MapleDataTool.getInt("casterMove", inf, 0) > 0;
            ret.pushTarget = MapleDataTool.getInt("pushTarget", inf, 0) > 0;
            ret.pullTarget = MapleDataTool.getInt("pullTarget", inf, 0) > 0;
        }
        final MapleData effect = data.getChildByPath("effect");
        if (skillType == 2) {
            isBuff = true;
        } else if (skillType == 3) { //final attack
            ret.animation = new ArrayList<>();
            ret.animation.add(0);
            isBuff = effect != null;
        } else {
            MapleData action_ = data.getChildByPath("action");
            final MapleData hit = data.getChildByPath("hit");
            final MapleData ball = data.getChildByPath("ball");

            boolean action = false;
            if (action_ == null) {
                if (data.getChildByPath("prepare/action") != null) {
                    action_ = data.getChildByPath("prepare/action");
                    action = true;
                }
            }
            isBuff = effect != null && hit == null && ball == null;
            if (action_ != null) {
                String d;
                if (action) { //prepare
                    d = MapleDataTool.getString(action_, null);
                } else {
                    d = MapleDataTool.getString("0", action_, null);
                }
                if (d != null) {
                    isBuff |= d.equals("alert2");
                    final MapleData dd = delayData.getChildByPath(d);
                    if (dd != null) {
                        for (MapleData del : dd) {
                            ret.delay += Math.abs(MapleDataTool.getInt("delay", del, 0));
                        }
                        if (ret.delay > 30) {
                            ret.delay = (int) Math.round(ret.delay * 11.0 / 16.0);
                            ret.delay -= (ret.delay % 30);
                        }
                    }
                    if (SkillFactory.getDelay(d) != null) { //this should return true always
                        ret.animation = new ArrayList<>();
                        ret.animation.add(SkillFactory.getDelay(d));
                        if (!action) {
                            for (MapleData ddc : action_) {
                                if (!MapleDataTool.getString(ddc, d).equals(d)) {
                                    String c = MapleDataTool.getString(ddc);
                                    if (SkillFactory.getDelay(c) != null) {
                                        ret.animation.add(SkillFactory.getDelay(c));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            switch (id) {
                case 1076:
                case 11076:
                case 2111002:
                case 2111003:
                case 2121001:
                case 2221001:
                case 2301002:
                case 2321001:
                case 4211001:
                case 12111005:
                case 22161003:
                    isBuff = false;
                    break;
                case 32121006:
                case 93:
                case 1004:
                case 1026:
                case 1101013: //鬥氣集中
                case 1111002:
                case 1111007:
                case 1211009:
                case 1220013:
                case 1311007:
                case 1320009:
                case 2120010:
                case 2121009:
                case 2220010:
                case 2221009:
                case 2311006:
                case 2320011:
                case 2321010:
                case 3120006:
                case 3121002:
                case 3220005:
                case 3221002:
                case 4111001:
                case 4111009:
                case 4211003:
                case 4221013:
                case 4321000:
                case 4331003:
                case 4341002:
                case 5001005:
                case 5110001:
                case 5111005:
                case 5111007:
                case 5120011:
                case 5120012:
                case 5121003:
                case 5121009:
                case 5121015:
                case 5211001:
                case 5211002:
                case 5211006:
                case 5211007:
                case 5211009:
                case 5220002:
                case 5220011:
                case 5220012:
                case 5311004:
                case 5311005:
                case 5320007:
                case 5321003:
                case 5321004:
                case 5721066: // 千斤墜
                case 5081023: // 追影連擊
                case 5701013: // 真氣流貫
                case 5711024: // 天地無我
                case 5721000: // 楓葉祝福
                case 5701005:
                case 5711001:
                case 5711011:
                case 5220014://dice2 cosair
                case 5720005:
                case 5721002:
                case 9001004:
                case 9101004:
                case 10000093:
                case 10001004:
                case 10001026:
                case 11101022: // 沉月
                case 11111022: // 旭日
                case 11121012: // 雙重力量（旭日）
                case 11121011: // 雙重力量（沉月）
                case 12000022: //元素:火焰
                case 12101023: //火之書
                case 12101024: //燃燒
                case 13111005:
                case 13001022: //元素： 風暴
                case 13101023: //快速之箭
                case 13101024: //妖精援助
                case 13111023: //阿爾法
                case 13121004: //風之祈禱
                case 13121005: //會心之眼
                case 13120008: //極限阿爾法
                case 13121053: //守護者榮耀
                case 13121054: //風暴使者
                case 14111007:
                case 14001021: //元素 : 闇黑
                case 14001022: //急速
                case 14001023: //黑暗面
                case 14001027: //暗影蝙蝠
                case 15001003:
                case 15100004:
                case 15101006:
                case 15111002:
                case 15111005:
                case 15111006:
                case 15111011:
                case 15001022: //元素： 雷電
                case 15101022: //致命快打
                case 15111022: //疾風
                case 15111023: //渦流
                case 15111024: //磁甲
                case 15121005: //最終極速
                case 15121004: // 引雷
                case 20000093:
                case 20001004:
                case 20001026:
                case 20010093:
                case 20011004:
                case 20011026:
                case 20020093:
                case 20021026:
                case 20031209:
                case 20031210:
                case 21000000:
                case 21101003:
                case 22121001:
                case 22131001:
                case 22131002:
                case 22141002:
                case 22151002:
                case 22151003:
                case 22161002:
                case 22161004:
                case 22171000:
                case 22171004:
                case 22181000:
                case 22181003:
                case 22181004:
                case 24101005:
                case 24111002:
                case 24121008:
                case 24121009:
                case 27001004: // 擴充魔力 - Mana Well
                case 27100003: // 黑暗祝福 - Black Blessing
                case 27101004: // 極速詠唱 - Booster
                case 27101202: // 黑暗之眼 - Pressure Void
                case 27111004: // 魔力護盾 - Shadow Shell
                case 27111005: // 光暗之盾 - Dusk Guard
                case 27111006: // 團隊精神 - Photic Meditation
                case 27110007: // 光暗轉換
                case 27121005: // 黑暗強化 - Dark Crescendo
                case 27121006: // 黑暗魔心 - Arcane Pitch
                case 30000093:
                case 30001026:
                case 30010093:
                case 30011026:
                case 31121005:
                case 32001003:
                case 32101003:
                case 32110000:
                case 32110007:
                case 32110008:
                case 32110009:
                case 32111005:
                case 32111006:
                case 32120000:
                case 32120001:
                case 32121003:
                case 32121017: // 黑色光環
                case 32121018: // 減益效果光環
                case 32111012: // 藍色繩索
                case 32101009: // 紅色光環
                case 32001016: // 黃色光環
                case 32100010: // 死神契約I
                case 32110017: // 死神契約II
                case 32120019: // 死神契約III
                case 32111016: // 黑暗閃電       
                case 33101006:
                case 33111003:
                case 35001001:
                case 35001002:
                case 35101005:
                case 35101007:
                case 35101009:
                case 35111001:
                case 35111002:
                case 35111004:
                case 35111005:
                case 35111009:
                case 35111010:
                case 35111011:
                case 35111013:
                case 35120000:
                case 35120014:
                case 35121003:
                case 35121005:
                case 35121006:
                case 35121009:
                case 35121010:
                case 35121013:
                case 36111006:
                case 40011186: // 劍豪初心者拔刀術
                case 41001001: // 拔刀術
                case 41110008: // 拔刀術‧心體技
                case 41101003: // 武神招來
                case 41110006: // 柳身
                case 41101005: // 秘劍‧隼
                case 41121002: // 一閃
                case 41121003: // 剛健
                case 41121005: // 曉月勇者
                case 41121014: // 疾風五月雨刃
                case 41121015: // 制敵之先
                case 41121054: // 劍神護佑
                case 41121053: // 紋櫻的祝福
                case 42100010:
                case 42101002:
                case 42101004:
                case 42111006:
                case 42121008:
                case 50001214:
                case 51101003:
                case 51111003:
                case 51111004:
                case 51121004:
                case 51121005:
                case 60001216:
                case 60001217:
                case 61101002:
                case 61111008:
                case 61120007:
                case 61120008:
                case 61120011:
                case 80001000:
                case 80001089:
                case 80001427:
                case 80001428:
                case 80001430:
                case 80001432:
                case 5111010:
                    isBuff = true;
            }
            if (GameConstants.isAngel(id)/* || GameConstants.isSummon(id)*/) {
                isBuff = false;
            }
        }
        ret.chargeskill = data.getChildByPath("keydown") != null;

        final MapleData level = data.getChildByPath("common");
        if (level != null) {
            ret.maxLevel = MapleDataTool.getInt("maxLevel", level, 1); //10 just a failsafe, shouldn't actually happens
            ret.trueMax = ret.maxLevel + (ret.combatOrders ? 2 : 0);
            for (int i = 1; i <= ret.trueMax; i++) {
                ret.effects.add(MapleStatEffect.loadSkillEffectFromData(level, id, isBuff, i, "x"));
            }

        } else {
            for (final MapleData leve : data.getChildByPath("level")) {
                ret.effects.add(MapleStatEffect.loadSkillEffectFromData(leve, id, isBuff, Byte.parseByte(leve.getName()), null));
            }
            ret.maxLevel = ret.effects.size();
            ret.trueMax = ret.effects.size();
        }
        final MapleData level2 = data.getChildByPath("PVPcommon");
        if (level2 != null) {
            ret.pvpEffects = new ArrayList<>();
            for (int i = 1; i <= ret.trueMax; i++) {
                ret.pvpEffects.add(MapleStatEffect.loadSkillEffectFromData(level2, id, isBuff, i, "x"));
            }
        }
        final MapleData reqDataRoot = data.getChildByPath("req");
        if (reqDataRoot != null) {
            for (final MapleData reqData : reqDataRoot.getChildren()) {
                ret.requiredSkill.add(new Pair<>(reqData.getName(), MapleDataTool.getInt(reqData, 1)));
            }
        }
        ret.animationTime = 0;
        if (effect != null) {
            for (final MapleData effectEntry : effect) {
                ret.animationTime += MapleDataTool.getIntConvert("delay", effectEntry, 0);
            }
        }
        return ret;
    }

    public MapleStatEffect getEffect(final int level) {
        if (effects.size() < level) {
            if (effects.size() > 0) { //incAllskill
                return effects.get(effects.size() - 1);
            }
            return null;
        } else if (level <= 0) {
            return effects.get(0);
        }
        return effects.get(level - 1);
    }

    public MapleStatEffect getPVPEffect(final int level) {
        if (pvpEffects == null) {
            return getEffect(level);
        }
        if (pvpEffects.size() < level) {
            if (pvpEffects.size() > 0) { //incAllskill
                return pvpEffects.get(pvpEffects.size() - 1);
            }
            return null;
        } else if (level <= 0) {
            return pvpEffects.get(0);
        }
        return pvpEffects.get(level - 1);
    }

    public int getSkillType() {
        return skillType;
    }

    public List<Integer> getAllAnimation() {
        return animation;
    }

    public int getAnimation() {
        if (animation == null) {
            return -1;
        }
        return (animation.get(Randomizer.nextInt(animation.size())));
    }

    public boolean isPVPDisabled() {
        return pvpDisabled;
    }

    public boolean isChargeSkill() {
        return chargeskill;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public boolean hasRequiredSkill() {
        return requiredSkill.size() > 0;
    }

    public List<Pair<String, Integer>> getRequiredSkills() {
        return requiredSkill;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getTrueMax() {
        return trueMax;
    }

    public boolean combatOrders() {
        return combatOrders;
    }

    public boolean canBeLearnedBy(int job) { //test
//        if (GameConstants.getBeginnerJob((short) (id / 10000)) == GameConstants.getBeginnerJob((short) job))
//            return true;
        int jid = job;
        int skillForJob = id / 10000;
        if (skillForJob == 2001) {
            return MapleJob.is龍魔導士(job); //special exception for beginner -.-
        } else if (skillForJob == 0) {
            return MapleJob.is冒險家(job); //special exception for beginner
        } else if (skillForJob == 1000) {
            return MapleJob.is皇家騎士團(job); //special exception for beginner
        } else if (skillForJob == 2000) {
            return MapleJob.is狂狼勇士(job); //special exception for beginner
        } else if (skillForJob == 3000) {
            return MapleJob.is末日反抗軍(job); //special exception for beginner
        } else if (skillForJob == 1) {
            return MapleJob.is重砲指揮官(job); //special exception for beginner
        } else if (skillForJob == 3001) {
            return MapleJob.is惡魔(job); //special exception for beginner
        } else if (skillForJob == 2002) {
            return MapleJob.is精靈遊俠(job); //special exception for beginner
        } else if (skillForJob == 508) {
            return MapleJob.is蒼龍俠客(job); //special exception for beginner
        } else if (skillForJob == 2003) {
            return MapleJob.is幻影俠盜(job); //special exception for beginner
        } else if (skillForJob == 5000) {
            return MapleJob.is米哈逸(job); //special exception for beginner
        } else if (skillForJob == 2004) {
            return MapleJob.is夜光(job); //special exception for beginner
        } else if (skillForJob == 6000) {
            return MapleJob.is凱撒(job); //special exception for beginner
        } else if (skillForJob == 6001) {
            return MapleJob.is天使破壞者(job); //special exception for beginner
        } else if (skillForJob == 3002) {
            return MapleJob.is傑諾(job); //special exception for beginner
        } else if (skillForJob == 10000) {
            return MapleJob.is神之子(job); //special exception for beginner
        } else if (jid / 100 != skillForJob / 100) { // wrong job
            return false;
        } else if (jid / 1000 != skillForJob / 1000) { // wrong job
            return false;
        } else if (MapleJob.is惡魔復仇者(skillForJob) && !MapleJob.is惡魔復仇者(job)) {
            return false;
        } else if (MapleJob.is傑諾(skillForJob) && !MapleJob.is傑諾(job)) {
            return false;
        } else if (MapleJob.is神之子(skillForJob) && !MapleJob.is神之子(job)) {
            return false;
        } else if (MapleJob.is幻獸師(skillForJob) && !MapleJob.is幻獸師(job)) {
            return false;
        } else if (MapleJob.is天使破壞者(skillForJob) && !MapleJob.is天使破壞者(job)) {
            return false;
        } else if (MapleJob.is凱撒(skillForJob) && !MapleJob.is凱撒(job)) {
            return false;
        } else if (MapleJob.is米哈逸(skillForJob) && !MapleJob.is米哈逸(job)) {
            return false;
        } else if (MapleJob.is夜光(skillForJob) && !MapleJob.is夜光(job)) {
            return false;
        } else if (MapleJob.is幻影俠盜(skillForJob) && !MapleJob.is幻影俠盜(job)) {
            return false;
        } else if (MapleJob.is蒼龍俠客(skillForJob) && !MapleJob.is蒼龍俠客(job)) {
            return false;
        } else if (MapleJob.is重砲指揮官(skillForJob) && !MapleJob.is重砲指揮官(job)) {
            return false;
        } else if (MapleJob.is惡魔殺手(skillForJob) && !MapleJob.is惡魔殺手(job)) {
            return false;
        } else if (MapleJob.is冒險家(skillForJob) && !MapleJob.is冒險家(job)) {
            return false;
        } else if (MapleJob.is皇家騎士團(skillForJob) && !MapleJob.is皇家騎士團(job)) {
            return false;
        } else if (MapleJob.is狂狼勇士(skillForJob) && !MapleJob.is狂狼勇士(job)) {
            return false;
        } else if (MapleJob.is龍魔導士(skillForJob) && !MapleJob.is龍魔導士(job)) {
            return false;
        } else if (MapleJob.is精靈遊俠(skillForJob) && !MapleJob.is精靈遊俠(job)) {
            return false;
        } else if (MapleJob.is末日反抗軍(skillForJob) && !MapleJob.is末日反抗軍(job)) {
            return false;
        } else if ((job / 10) % 10 == 0 && (skillForJob / 10) % 10 > (job / 10) % 10) { // wrong 2nd job
            return false;
        } else if ((skillForJob / 10) % 10 != 0 && (skillForJob / 10) % 10 != (job / 10) % 10) { //wrong 2nd job
            return false;
        } else if (skillForJob % 10 > job % 10) { // wrong 3rd/4th job
            return false;
        }
        return true;
    }

    public boolean isTimeLimited() {
        return timeLimited;
    }
/*
    public boolean isFourthJobSkill(int skillid) {
        switch (skillid / 10000) {
            case 112:
            case 122:
            case 132:
            case 212:
            case 222:
            case 232:
            case 312:
            case 322:
            case 412:
            case 422:
            case 512:
            case 522:
                return true;
        }
        return false;
    }

    public boolean isThirdJobSkill(int skillid) {
        switch (skillid / 10000) {
            case 111:
            case 121:
            case 131:
            case 211:
            case 221:
            case 231:
            case 311:
            case 321:
            case 411:
            case 421:
            case 511:
            case 521:
                return true;
        }
        return false;
    }

    public boolean isSecondJobSkill(int skillid) {
        switch (skillid / 10000) {
            case 110:
            case 120:
            case 130:
            case 210:
            case 220:
            case 230:
            case 310:
            case 320:
            case 410:
            case 420:
            case 510:
            case 520:
                return true;
        }
        return false;
    }*/

    public boolean isFourthJob() {
        if (isHyper()) {
            return true;
        }

        switch (id) { // I guess imma make an sql table to store these, so that we could max them all out.
            case 1120012:
            case 1320011:
            case 3110014:
            case 4320005:
            case 4340010:
            case 4340012:
            case 5120011:
            case 5120012:
            case 5220012:
            case 5220014:
            case 5321006:
            case 5720008:
            case 21120011:
            case 21120014:
            case 22171004:
            case 22181004:
            case 23120011:
            case 23120013:
            case 23121008:
            case 33120010:
            case 33121005:
                return false;
        }

        switch (this.id / 10000) {
            case 2312:
            case 2412:
            case 2217:
            case 2218:
            case 2512:
            case 2712:
            case 3122:
                return true;
            case 3612:
                return getMasterLevel() >= 10;
            case 6112:
            case 6512:
                return true;
            case 10100:
                return this.id == 101000101;
            case 10110:
                return (this.id == 101100101) || (this.id == 101100201);
            case 10111:
                return (this.id == 101110102) || (this.id == 101110200) || (this.id == 101110203);
            case 10112:
                return (this.id == 101120104) || (this.id == 101120204);
        }
        if ((getMaxLevel() <= 15 && !invisible && getMasterLevel() <= 0)) {
            return false;
        }
        //龍魔技能
        if (id / 10000 >= 2210 && id / 10000 < 3000) {
            return ((id / 10000) % 10) >= 7 || getMasterLevel() > 0;
        }
        //影武技能
        if (id / 10000 >= 430 && id / 10000 <= 434) {
            return ((id / 10000) % 10) == 4 || getMasterLevel() > 0;
        }
        if (this.id == 40020002) {
            return true;
        }
        //冒險家
        return ((id / 10000) % 10) == 2 && id < 90000000 && !isBeginnerSkill();
    }

    public Element getElement() {
        return element;
    }

    public int getAnimationTime() {
        return animationTime;
    }

    public int getMasterLevel() {
        return masterLevel;
    }

    public int getDelay() {
        return delay;
    }

    public int getTamingMob() {
        return eventTamingMob;
    }

    public int getSkillTamingMob() {
        return eventTamingMob;
    }

    public boolean isBeginnerSkill() {
        int jobId = id / 10000;
        return MapleJob.isBeginner(jobId);
    }

    public boolean isMagic() {
        return magic;
    }

    public boolean isHyper() {
        //return hyper > 0;
        return (hyper > 0) && (reqLev > 0);
    }

    public int getHyper() {
        return hyper;
    }

    public int getReqLevel() {
        return this.reqLev;
    }

    public boolean isMovement() {
        return casterMove;
    }

    public boolean isPush() {
        return pushTarget;
    }

    public boolean isPull() {
        return pullTarget;
    }

    public boolean isSpecialSkill() {
        int jobId = id / 10000;
        return jobId == 900 || jobId == 800 || jobId == 9000 || jobId == 9200 || jobId == 9201 || jobId == 9202 || jobId == 9203 || jobId == 9204;
    }

    @Override
    public int compare(Skill o1, Skill o2) {
        return (Integer.valueOf(o1.getId()).compareTo(o2.getId()));
    }

    public boolean isLinkSkills() {
        switch (this.id) {
            case 80000000:
            case 80000001:
            case 80000002:
            case 80000005:
            case 80000006:
            case 80000047:
            case 80000050:
            case 80001040:
            case 80001140:
            case 80001151:
            case 80001155:
                return true;
        }
        return false;
    }

    public boolean isTeachSkills() {
        switch (this.id) {
            case 110:
            case 1214:
            case 20021110:
            case 20030204:
            case 20040218:
            case 30010112:
            case 30010241:
            case 30020233:
            case 50001214:
            case 60000222:
            case 60011219:
                return true;
        }
        return false;
    }

    public int getFixLevel() {
        return fixLevel;
    }
}
