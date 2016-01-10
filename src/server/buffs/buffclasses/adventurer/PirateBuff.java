/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.buffs.buffclasses.adventurer;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Maple
 */
public class PirateBuff extends AbstractBuffClass {

    public PirateBuff() {
        buffs = new int[]{
            //一轉
            5001005, //衝鋒Dash

            //二轉
            //打手
            5101006, //致命快打Knuckle Booster
            5101011, //全神貫注Dark Clarity
            //槍手
            5201003, //迅雷再起Gun Booster
            5201008, //無形彈藥Infinity Blast
//            5201012, //召喚船員Scurvy Summons
//            5201013, //召喚船員Scurvy Summons
//            5201014, //召喚船員Scurvy Summons
            //重砲兵
            5301002, //加農砲推進器Cannon Booster
            5301003, //猴子的魔法Monkey Magic

            //三轉
            //格鬥家
            5111007, //幸運骰子Roll Of The Dice
            5111010, //雲體風身
            //神槍手
//            5210015, //召喚船員
//            5210016, //召喚船員
//            5210017, //召喚船員
            5211007, //幸運骰子Roll Of The Dice
            5211009, //魔法彈丸Cross Cut Blast
            //重砲兵隊長
//            5311002, //猴子的衝擊波
            5311004, //幸運木桶Barrel Roulette
            5311005, //幸運骰子Luck of the Die

            //四轉
            //拳霸
            5121000, //楓葉祝福Maple Warrior
            5121009, //最終極速Speed Infusion
//            5120011, //反擊姿態
//            5120012, //雙倍幸運骰子
            5121015, //拳霸大師Crossbones
            //槍神
//            5220012, //反擊
//            5220014, //雙倍幸運骰子
            5221000, //楓葉祝福Maple Warrior
            5221004, //瞬‧迅雷
            5221018, //海盜風采Jolly Roger
//            5221021, //極速之指Quickdraw
            //重砲指揮官
//            5320007, //雙倍幸運骰子
//            5320008, //神聖猴子的咒語
            5321005, //楓葉祝福Maple Warrior
            5321010, //百烈精神Pirate's Spirit

            //超技
            //拳霸
//            5121052, //家族之力
            5121053, //傳說冒險Epic Adventure
            5121054, //暴能續發Stimulating Conversation
            //槍神
            5221053, //傳說冒險Epic Adventure
            5221054, //撫慰甘露Whaler's Potion
            //重砲指揮官
            5321053, //傳說冒險Epic Adventure
            5321054, //壓制砲擊Buckshot
        };
    }

    @Override
    public boolean containsJob(int job) {
        return MapleJob.is冒險家(job) && MapleJob.is海盜(job) && !MapleJob.is蒼龍俠客(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 5001005: //衝鋒Dash
                eff.statups.put(MapleBuffStat.DASH_SPEED, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.DASH_JUMP, eff.info.get(MapleStatInfo.y));
                break;
            case 5221004: //瞬‧迅雷
                eff.statups.put(MapleBuffStat.USING_SKILL_MOVE, eff.info.get(MapleStatInfo.x));
                break;
            case 5101011: //Dark Clarity
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                eff.statups.put(MapleBuffStat.INDIE_ACC, eff.info.get(MapleStatInfo.indieAcc));
                break;
            case 5101006: //Knuckle Booster
            case 5201003: //Gun Booster
            case 5301002: //Cannon Booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 5111007: //Roll Of The Dice
            case 5211007: //Roll Of The Dice
            case 5311005: //Luck of the Die
                eff.statups.put(MapleBuffStat.DICE_ROLL, 0);
                break;
            case 5111010: //雲體風身
                eff.statups.put(MapleBuffStat.WATER_SHIELD, eff.info.get(MapleStatInfo.x));
                break;
            case 5121015: //拳霸大師
                eff.statups.put(MapleBuffStat.DAMAGE_RATE, eff.info.get(MapleStatInfo.x));
                break;
            case 5121009: //Speed Infusion
                eff.statups.put(MapleBuffStat.SPEED_INFUSION, eff.info.get(MapleStatInfo.x));
                break;
            case 5121054: //Stimulating Conversation
                //TODO 拳霸BUFF
                break;
            case 5221018: //Jolly Roger
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.y));//or x?
                eff.statups.put(MapleBuffStat.DAMAGE_RATE, eff.info.get(MapleStatInfo.damR));
                eff.statups.put(MapleBuffStat.STANCE, eff.info.get(MapleStatInfo.z));
                eff.statups.put(MapleBuffStat.AVOID, eff.info.get(MapleStatInfo.eva));
                break;
            case 5201008: //Infinity Blast
                eff.statups.put(MapleBuffStat.SPIRIT_CLAW, eff.info.get(MapleStatInfo.x));
                break;
            case 5211009: //Cross Cut Blast
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                break;
            case 5221054: //Whaler's Potion
                //TODO 槍神BUFF
                break;
            case 5301003: //Monkey Magic
                eff.statups.put(MapleBuffStat.INDIE_MAX_HP, eff.info.get(MapleStatInfo.indieMhp));
                eff.statups.put(MapleBuffStat.INDIE_MAX_MP, eff.info.get(MapleStatInfo.indieMmp));
                eff.statups.put(MapleBuffStat.INDIE_ACC, eff.info.get(MapleStatInfo.indieAcc));
                eff.statups.put(MapleBuffStat.INDIE_EVA, eff.info.get(MapleStatInfo.indieEva));
                eff.statups.put(MapleBuffStat.INDIE_JUMP, eff.info.get(MapleStatInfo.indieJump));
                eff.statups.put(MapleBuffStat.INDIE_SPEED, eff.info.get(MapleStatInfo.indieSpeed));
                eff.statups.put(MapleBuffStat.INDIE_ALL_STATE, eff.info.get(MapleStatInfo.indieAllStat));
                break;
            case 5311004: //Barrel Roulette
                eff.statups.put(MapleBuffStat.BARREL_ROLL, 0);
                break;
            case 5321010: //Pirate's Spirit
                eff.statups.put(MapleBuffStat.STANCE, eff.info.get(MapleStatInfo.prop));
                break;
            case 5321054: //Buckshot
                //TODO 重砲BUFF
                break;
            case 5121000: //Maple Warrior
            case 5221000: //Maple Warrior
            case 5321005: //Maple Warrior
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 5121053: //Epic Adventure
            case 5221053: //Epic Adventure
            case 5321053: //Epic Adventure
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.INDIE_MAX_DAMAGE_OVER, eff.info.get(MapleStatInfo.indieMaxDamageOver));
                break;
            default:
                System.out.println("海盜技能未處理,技能代碼: " + skill);
                break;
        }
    }
}
