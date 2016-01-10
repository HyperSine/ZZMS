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
public class WarriorBuff extends AbstractBuffClass {

    public WarriorBuff() {
        buffs = new int[]{
            //一轉
            1001003, //自身強化Iron Body

            //二轉
            //狂戰士
            1101004, //極速武器Weapon Booster
            1101006, //激勵Rage 
            1101013, //鬥氣集中Combo Order
            //見習騎士
            1201004, //極速武器Weapon Booster
            1201011, //烈焰之劍Flame Charge
            1201012, //寒冰之劍Blizzard Charge
            //槍騎兵
            1301004, //極速武器Weapon Booster
            1301006, //禦魔陣Iron Will
            1301007, //神聖之火Hyper Body 
            1301013, //追隨者Evil Eye

            //三轉
            //十字軍
            1111003, //黑暗之劍
            //騎士
            1211008, //雷鳴之劍Lightning Charge
            1211010, //復原HP Recovery:
            1211011, //戰鬥命令Combat Orders
            1211013, //降魔咒Threaten
            1211014, //超衝擊防禦Parashock Guard
            //嗜血狂騎
//TODO特殊BUFF獨立處理            1311013, //追隨者支配Evil Eye of Domination
            1311015, //十字深鎖鏈Cross Surge
            1310016, //黑暗守護

            //四轉
            //英雄
            1121000, //楓葉祝福Maple Warrior
            1121010, //鬥氣爆發Enrage
            //聖騎士
            1221000, //楓葉祝福Maple Warrior
            1221015, //自然之力Void Elemental
            1221016, //守護者精神Guardian
            1221004, //聖靈之劍Holy Charge
            //黑騎士
            1321000, //楓葉祝福Maple Warrior
            1321015, //暗之獻祭
            1320019, //轉生_狀態

            //超技
            //英雄
            1121053, //傳說冒險Epic Adventure
            1121054, //劍士意念Cry Valhalla
            //聖騎士
            1221053, //傳說冒險Epic Adventure
            1221054, //神域護佑Sacrosanctity
            //黑騎士
            1321053, //傳說冒險Epic Adventure
//TODO            1321054, //黑暗飢渴Dark Thrist
        };
    }

    @Override
    public boolean containsJob(int job) {
        return MapleJob.is冒險家(job) && MapleJob.is劍士(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 1001003://自身強化Iron Body
                eff.statups.clear();
                eff.statups.put(MapleBuffStat.INDIE_PDD, eff.info.get(MapleStatInfo.indiePdd));
                break;
            case 1101004: //極速武器Weapon Booster
            case 1201004: //極速武器Weapon Booster
            case 1301004: //極速武器Weapon Booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 1101006: //激勵Rage 
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                eff.statups.put(MapleBuffStat.POWERGUARD, eff.info.get(MapleStatInfo.x));
                break;
            case 1101013: //鬥氣集中Combo Order
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.COMBO, 1);
                break;
            case 1111003: //黑暗之劍
                eff.monsterStatus.put(MonsterStatus.DARKNESS, eff.info.get(MapleStatInfo.x));
                break;
            case 1121010: //鬥氣爆發Enrage
                eff.statups.put(MapleBuffStat.ENRAGE, (eff.info.get(MapleStatInfo.x)) * 100 + (eff.info.get(MapleStatInfo.mobCount)));
                eff.statups.put(MapleBuffStat.CRITICAL_RATE2, eff.info.get(MapleStatInfo.z));
                eff.statups.put(MapleBuffStat.MIN_CRITICAL_DAMAGE, eff.info.get(MapleStatInfo.y));
                break;
            case 1121054: //劍士意念Cry Valhalla
                eff.statups.clear();
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                break;
            case 1201011://烈焰之劍Flame Charge
                eff.monsterStatus.put(MonsterStatus.BURNED, eff.info.get(MapleStatInfo.dot));
                break;
            case 1201012://寒冰之劍Blizzard Charge
                eff.monsterStatus.put(MonsterStatus.SPEED, eff.info.get(MapleStatInfo.x));
                break;
            case 1211008: //雷鳴之劍Lightning Charge
                eff.monsterStatus.put(MonsterStatus.STUN, 1);
                break;
            case 1221004: //聖靈之劍Holy Charge
                eff.monsterStatus.put(MonsterStatus.SEAL, eff.info.get(MapleStatInfo.x));
                break;
            case 1211010: //復原HP Recovery:
                eff.statups.put(MapleBuffStat.HP_RECOVER, 1);
                break;
            case 1211013: //降魔咒Threaten
                eff.monsterStatus.put(MonsterStatus.WATK, eff.info.get(MapleStatInfo.x));
                eff.monsterStatus.put(MonsterStatus.WDEF, eff.info.get(MapleStatInfo.x));
                eff.monsterStatus.put(MonsterStatus.MATK, eff.info.get(MapleStatInfo.x));
                eff.monsterStatus.put(MonsterStatus.MDEF, eff.info.get(MapleStatInfo.x));
                eff.monsterStatus.put(MonsterStatus.AVOID, eff.info.get(MapleStatInfo.z));
                break;
            case 1211014: //超衝擊防禦Parashock Guard
                eff.statups.put(MapleBuffStat.PARASHOCK_GUARD, eff.info.get(MapleStatInfo.x) / 2);
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                eff.statups.put(MapleBuffStat.INDIE_PDD_R, eff.info.get(MapleStatInfo.z));
                break;
            case 1211011: //戰鬥命令Combat Orders
                eff.statups.put(MapleBuffStat.COMBAT_ORDERS, eff.info.get(MapleStatInfo.x));
                break;
            case 1221015: //自然之力Void Elemental
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.ELEMENT_RESET, eff.info.get(MapleStatInfo.x));
                break;
            case 1221016: //守護者精神Guardian
            case 1221054: //神域護佑Sacrosanctity
                eff.statups.put(MapleBuffStat.INVINCIBILITY, 1);
                break;
            case 1301006: //禦魔陣Iron Will
                eff.statups.put(MapleBuffStat.MDEF, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.MDEF, eff.info.get(MapleStatInfo.y));
                break;
            case 1301007: //神聖之火Hyper Body
                eff.statups.put(MapleBuffStat.MAXHP, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.MAXMP, eff.info.get(MapleStatInfo.y));
                break;
            case 1301013: //追隨者Evil Eye
                eff.statups.put(MapleBuffStat.BEHOLDER, eff.info.get(MapleStatInfo.x));
                break;
            case 1311015: //十字深鎖鏈Cross Surge
                eff.statups.put(MapleBuffStat.CROSS_SURGE, eff.info.get(MapleStatInfo.x));
                break;
            case 1310016: //黑暗守護
                eff.statups.put(MapleBuffStat.CRITICAL_RATE2, eff.info.get(MapleStatInfo.indieCr));
                break;
            case 1320019: //重生
                eff.statups.put(MapleBuffStat.IDA_UNK_BUFF12, 1); //TODO 未知
                eff.statups.put(MapleBuffStat.INVINCIBILITY, 1);
                break;
            case 1321015: //暗之獻祭
                eff.hpR = ((eff.info.get(MapleStatInfo.y)) / 100.0D);
                eff.statups.put(MapleBuffStat.INDIE_IGNORE_MOB_PDP_R, eff.info.get(MapleStatInfo.ignoreMobpdpR));
                eff.statups.put(MapleBuffStat.INDIE_BOSS, eff.info.get(MapleStatInfo.indieBDR));
                break;
            case 1121000: //楓葉祝福Maple Warrior
            case 1221000: //楓葉祝福Maple Warrior
            case 1321000: //楓葉祝福Maple Warrior
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 1121053: //傳說冒險Epic Adventure
            case 1221053: //傳說冒險Epic Adventure
            case 1321053: //傳說冒險Epic Adventure
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.INDIE_MAX_DAMAGE_OVER, eff.info.get(MapleStatInfo.indieMaxDamageOver));
                break;
            default:
                System.out.println("劍士技能未處理,技能代碼: " + skill);
                break;
        }
    }
}
