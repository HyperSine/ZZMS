/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package server.buffs.buffclasses.cygnus;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Fate
 */
public class ThunderBreakerBuff extends AbstractBuffClass {
    public ThunderBreakerBuff() {
        buffs = new int[]{            
            15001022, // 元素： 雷電
            15101022, // 致命快打
            15111023, // 渦流
            15111024, // 磁甲
            15121005, // 最終極速
            15121004, // 引雷
            15111022, // 疾風
        };
    }
    
    @Override
    public boolean containsJob(int job) {
        return MapleJob.is閃雷悍將(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {   
            case 15001022: //元素： 雷電
                eff.statups.put(MapleBuffStat.STORM_ELEMENTAL, 1);
                break;
            case 15100004: //蓄能激發
                eff.statups.put(MapleBuffStat.ENERGY_CHARGE, 0);
                break;
            case 15101022: //致命快打
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 15111022: //疾風
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                break;
            case 15111023: //渦流
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.accR));
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.terR));
                break;
            case 15111024: //磁甲
                eff.statups.put(MapleBuffStat.WATER_SHIELD, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                break;
            case 15121005: //最終極速
                eff.statups.put(MapleBuffStat.SPEED_INFUSION, eff.info.get(MapleStatInfo.x));
                break;
            case 15121004: // 引雷
                eff.statups.put(MapleBuffStat.SHADOWPARTNER, eff.info.get(MapleStatInfo.x));
                break;
            default:
                //System.out.println("Hayato skill not coded: " + skill);
                break;
        }
    }
}
