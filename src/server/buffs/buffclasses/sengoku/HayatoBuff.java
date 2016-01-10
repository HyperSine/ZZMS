
package server.buffs.buffclasses.sengoku;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;


/**
 *
 * @author Charmander
 */
public class HayatoBuff extends AbstractBuffClass {

    public HayatoBuff() {
        buffs = new int[]{
            40011186, // 劍豪初心者拔刀術
            41121014, // 疾風五月雨刃
            41110006, // 柳身
            41110009, // 心頭滅卻            
            41101003, // 武神招來
            41101005, // 秘劍‧隼
            41121002, // 一閃
            41121003, // 剛健
            41121005, // 曉月勇者 
            41001001, // 拔刀術
            41121015, // 制敵之先
            41110008, // 拔刀術‧心體技
            41121054, // 無雙十刃之型
            41121053, // 公主的加護
            
        };
    }
    
    @Override
    public boolean containsJob(int job) {
        return MapleJob.is劍豪(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 40011186: // 劍豪初心者拔刀術
                eff.info.put(MapleStatInfo.time, 600000);
                eff.statups.put(MapleBuffStat.SPEED, 10);
                break;
            case 41110006: // 柳身
                eff.statups.put(MapleBuffStat.WILLOW_DODGE, eff.info.get(MapleStatInfo.prop));
                break;
            case 41110009: // 心頭滅卻
                eff.statups.put(MapleBuffStat.RECOVERY, eff.info.get(MapleStatInfo.damage));
                break;
            case 41121014: //疾風五月雨刃
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                break;
            case 41001001: // 拔刀術
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.CRITICAL_RATE, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.HAYATO, eff.info.get(MapleStatInfo.prop));
                eff.statups.put(MapleBuffStat.BATTOUJUTSU_STANCE, -1);                
                break;
            case 41110008: // 拔刀術‧心體技
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.CRITICAL_RATE, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.HAYATO, eff.info.get(MapleStatInfo.prop));
                eff.statups.put(MapleBuffStat.BATTOUJUTSU_STANCE, -1);     
                eff.statups.put(MapleBuffStat.BATTOUJUTSU_SOUL, 0);  
                break;
            case 41101003: // 武神招來
                eff.statups.put(MapleBuffStat.JUMP, eff.info.get(MapleStatInfo.u));
                eff.statups.put(MapleBuffStat.SPEED, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.MILITARY_MIGHT, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.MILITARY_MIGHT1, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.MILITARY_MIGHT2, eff.info.get(MapleStatInfo.x));
                break;
            case 41101005: // 秘劍‧隼
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 41121002: // 一閃
                eff.statups.put(MapleBuffStat.HITOKIRI_STRIKE, eff.info.get(MapleStatInfo.prop));
                break;
            case 41121003: // 剛健
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.y));
                break;
            case 41121005: // 曉月勇者
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 41121015: // 制敵之先
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.COUNTERATTACK, 0);
                break;
            case 41121054: // 無雙十刃之型
                //TODO - 剑豪BUFF無雙十刃之型（41121054）works without
                break;
            case 41121053: // 公主的加護
                eff.statups.put(MapleBuffStat.INDIE_MAX_DAMAGE_OVER, eff.info.get(MapleStatInfo.indieMaxDamageOver));
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                break;
            default:
                System.out.println("Hayato skill not coded: " + skill);
                break;
        }
    }
}
