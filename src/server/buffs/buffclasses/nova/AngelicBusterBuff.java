/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.buffs.buffclasses.nova;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Maple
 */
public class AngelicBusterBuff extends AbstractBuffClass {

    public AngelicBusterBuff() {
        buffs = new int[]{
            60011219, // Terms and Conditions
            65001002, //抒情十字
            65101002, //Power Transfer
            65111003, //Dragon Whistle
            65111004, //Iron Blossom
            65121004, //Star Gazer
            65121009, //Nova Warrior
            65121053, //Final Contract
            65121054, //Pretty Exaltation
        };
    }
    
    @Override
    public boolean containsJob(int job) {
        return MapleJob.is天使破壞者(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 60011219: // Terms and Conditions
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                break;
            case 65001002: // 抒情十字
                eff.statups.put(MapleBuffStat.INDIE_MAX_HP, (eff.info.get(MapleStatInfo.x) * 150) + 400);
                eff.statups.put(MapleBuffStat.INDIE_ACC, eff.info.get(MapleStatInfo.indieAcc));
                break;
            case 65101002: // Power Transfer
                eff.statups.put(MapleBuffStat.DAMAGE_ABSORBED, eff.info.get(MapleStatInfo.x));
                break;
            case 65111003: // Dragon Whistle
                break;
            case 65111004: // Iron Blossom
                eff.statups.put(MapleBuffStat.STANCE, eff.info.get(MapleStatInfo.prop));
                break;
            case 65121004: //Star Gazer
                eff.statups.put(MapleBuffStat.CRIT_DAMAGE, eff.info.get(MapleStatInfo.x));
                break;
            case 65121009: //Nova Warrior
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 65121053: // Final Contract - TODO 天使破壞者
//                eff.statups.put(MapleBuffStat.CRITICAL_RATE, eff.info.get(MapleStatInfo.x));
//                eff.statups.put(MapleBuffStat.STANCE, eff.info.get(MapleStatInfo.y));
//                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.asrR));
//                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.terR));
                    break;
            case 65121054: // Pretty Exaltation - TODO 天使破壞者
                //eff.statups.put(MapleBuffStat.RECHARGE, eff.info.get(MapleStatInfo.x));
                break;
            default:
                //System.out.println("Unhandled Buff: " + skill);
                break;
        }
    }
}
