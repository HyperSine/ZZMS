/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.buffs.buffclasses.adventurer;

import client.MapleBuffStat;
import client.MapleJob;
import client.MonsterStatus;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Itzik
 */
public class MagicianBuff extends AbstractBuffClass {

    public MagicianBuff() {
        buffs = new int[]{
            //一轉
            2001002, //魔心防禦Magic Guard

            //二轉
            //火毒
//TODO 抓包            2100009, //元素吸收
            2101001, //精神強化Meditation
            2101008, //極速詠唱Magic Booster
            2101010, //燎原之火
            //冰雷
//TODO 抓包            2200011, //結冰特效
            2201009, //寒冰迅移
            2201001, //精神強化Meditation
            2201010, //極速詠唱Magic Booster
            //僧侶
//特殊處理技能            2300009, //祝福福音Blessed Ensemble - passive but buff?
            2301003, //神聖之光Invicible
            2301004, //天使祝福Bless
            2301008, //極速詠唱Magic Booster

            //三轉
            //火毒
            2111008, //自然力重置Elemental Decrease
            2111007, //瞬間移動精通Teleport Mastery
            2111011, //元素適應(火、毒)Elemental Adaptation (Fire, Poison)
            //冰雷
            2211008, //自然力重置Elemental Decrease
            2211007, //瞬間移動精通Teleport Mastery
            2211012, //元素適應(雷、冰)Elemental Adaptation (Ice, Lightning)
            //祭司
            2311002, //時空門Mystic Door
            2311003, //神聖祈禱Holy Symbol
            2311007, //瞬間移動精通Teleport Mastery
            2311009, //聖十字魔法盾Holy Magic Shield
            2311012, //聖靈守護Divine Protection

            //四轉
            //火毒
            2120010, //神秘狙擊
//TODO 抓包            2120014, //元素強化
            2121000, //楓葉祝福Maple Warrior
            2121004, //魔力無限Infinity
            //冰雷
            2220010, //神秘狙擊
//TODO 抓包            2220015, //冰凍效果
            2221000, //楓葉祝福Maple Warrior
            2221004, //魔力無限Infinity
            //主教
            2320011, //神秘狙擊
//特殊處理技能            2320013, //祝福旋律
            2321000, //楓葉祝福Maple Warrior
            2221001, //核爆術
            2321004, //魔力無限Infinity
            2321005, //進階祝福Advanced Blessing

            //超技
            //火毒
            2121053, //傳說冒險Epic Adventure
            2121054, //火靈結界Inferno Aura
            //冰雷
            2221053, //傳說冒險Epic Adventure
            2221054, //冰雪結界Absolute Zero Aura
            //主教
            2321052, //天堂之門
            2321053, //傳說冒險Epic Adventure
            2321054, //復仇天使Avenging Angel
        };
    }

    @Override
    public boolean containsJob(int job) {
        return MapleJob.is冒險家(job) && MapleJob.is法師(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 2001002: //魔心防禦Magic Guard
                eff.statups.put(MapleBuffStat.MAGIC_GUARD, eff.info.get(MapleStatInfo.x));
                break;
            case 2101001: //精神強化Meditation
            case 2201001: //精神強化Meditation
                eff.statups.put(MapleBuffStat.INDIE_MAD, eff.info.get(MapleStatInfo.indieMad));
                break;
            case 2101010: //燎原之火
                eff.statups.put(MapleBuffStat.IGNITE, 1);
                break;
            case 2101008: //極速詠唱Magic Booster
            case 2201010: //極速詠唱Magic Booster
            case 2301008: //極速詠唱Magic Booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 2201009: //寒冰迅移
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.CHILLING_STEP, 1);
                break;
            case 2301004: //天使祝福Bless   
                eff.statups.put(MapleBuffStat.BLESS, (int) eff.getLevel());
                break;
            case 2301003: //神聖之光Invicible
                eff.statups.put(MapleBuffStat.INVICIBLE, eff.info.get(MapleStatInfo.x));
                break;
            case 2111011: //元素適應(火、毒)Elemental Adaptation (Fire, Poison)
            case 2211012: //元素適應(雷、冰)Elemental Adaptation (Ice, Lightning)
            case 2311012: //聖靈守護Divine Protection
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.ABNORMAL_BUFF_RESISTANCES, 1);
                break;
            case 2111008: //自然力重置Elemental Decrease
            case 2211008: //自然力重置Elemental Decrease
                eff.statups.put(MapleBuffStat.ELEMENT_RESET, eff.info.get(MapleStatInfo.x));
                break;
            case 2111007: //瞬間移動精通Teleport Mastery
            case 2211007: //瞬間移動精通Teleport Mastery
            case 2311007: //瞬間移動精通Teleport Mastery
                eff.info.put(MapleStatInfo.mpCon, eff.info.get(MapleStatInfo.y));
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.TELEPORT_MASTERY, eff.info.get(MapleStatInfo.x));
                eff.monsterStatus.put(MonsterStatus.STUN, 1);
                break;
            case 2311002: //時空門Mystic Door
                eff.statups.put(MapleBuffStat.MYSTIC_DOOR, eff.info.get(MapleStatInfo.x));
                break;
            case 2311003: //神聖祈禱Holy Symbol
                eff.statups.put(MapleBuffStat.HOLY_SYMBOL, eff.info.get(MapleStatInfo.x));
                break;
            case 2311009: //聖十字魔法盾Holy Magic Shield
                eff.statups.put(MapleBuffStat.HOLY_MAGIC_SHELL, eff.info.get(MapleStatInfo.x));
                break;
            case 2121004: //魔力無限Infinity
            case 2221004: //魔力無限Infinity
            case 2321004: //魔力無限Infinity
                eff.statups.put(MapleBuffStat.STANCE, eff.info.get(MapleStatInfo.prop));
                eff.statups.put(MapleBuffStat.INFINITY, eff.info.get(MapleStatInfo.damage) + 1);
                break;
            case 2121000: //楓葉祝福Maple Warrior
            case 2221000: //楓葉祝福Maple Warrior
            case 2321000: //楓葉祝福Maple Warrior
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 2221001: //核爆術
                eff.statups.put(MapleBuffStat.BIG_BANG, 1);
                break;
            case 2321005: //進階祝福Advanced Blessing
                eff.statups.put(MapleBuffStat.HOLY_SHIELD, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.INDIE_MAX_HP, eff.info.get(MapleStatInfo.indieMhp));
                eff.statups.put(MapleBuffStat.INDIE_MAX_MP, eff.info.get(MapleStatInfo.indieMmp));
                break;
            case 2320011://神秘狙擊
            case 2220010://神秘狙擊
            case 2120010://神秘狙擊
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.MANY_USES, eff.info.get(MapleStatInfo.x));
                break;
            case 2121053: //傳說冒險Epic Adventure
            case 2221053: //傳說冒險Epic Adventure
            case 2321053: //傳說冒險Epic Adventure
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.INDIE_MAX_DAMAGE_OVER, eff.info.get(MapleStatInfo.indieMaxDamageOver));
                break;
            case 2121054: //火靈結界Inferno Aura
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.INFERNO_AURA, 1);
                break;
            case 2221054: //冰雪結界Absolute Zero Aura
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.ABSOLUTE_ZERO_AURA, 1);
                eff.statups.put(MapleBuffStat.INDIE_TER_R, eff.info.get(MapleStatInfo.v));
                eff.statups.put(MapleBuffStat.INDIE_ASR_R, eff.info.get(MapleStatInfo.v));
                break;
            case 2321052: //天堂之門
                eff.statups.put(MapleBuffStat.HEAVEN_IS_DOOR, 1);
                break;
            case 2321054: //復仇天使Avenging Angel
                eff.statups.clear();
                eff.statups.put(MapleBuffStat.IGNORE_DEF, eff.info.get(MapleStatInfo.ignoreMobpdpR));
                eff.statups.put(MapleBuffStat.AVENGING_ANGEL, (int) eff.getLevel());
                eff.statups.put(MapleBuffStat.INDIE_MAD, eff.info.get(MapleStatInfo.indieMad));
                eff.statups.put(MapleBuffStat.INDIE_BOOSTER, eff.info.get(MapleStatInfo.indieBooster) - 2);
                eff.statups.put(MapleBuffStat.INDIE_MAX_DAMAGE_OVER, eff.info.get(MapleStatInfo.indieMaxDamageOver));
                break;
            default:
                System.out.println("法師技能未處理,技能代碼: " + skill);
                break;
        }
    }
}
