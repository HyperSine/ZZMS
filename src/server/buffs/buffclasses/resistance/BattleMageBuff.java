/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.buffs.buffclasses.resistance;

import client.MapleBuffStat;
import client.MapleJob;
import client.MonsterStatus;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Dismal
 */
public class BattleMageBuff extends AbstractBuffClass {

    public BattleMageBuff() {
        buffs = new int[]{
            32101004, // 紅色吸血術
            32101005, // 長杖極速
            32111014, // 防禦姿態
            32111006, // 甦醒            
            32121010, // 煉獄鬥氣
            32121005, // 危機繩索
            32121007, // 楓葉祝福
            32111010, // 瞬間移動精通
            32121053, // 自由意志
            32121054, // 聯盟繩索
            32121056, // 戰鬥精通

            //------------178
            32121017, // 黑色光環
            32121018, // 減益效果光環
            32111012, // 藍色繩索
            32101009, // 紅色光環
            32001016, // 黃色光環
            32100010, // 死神契約I
            32110017, // 死神契約II
            32120019, // 死神契約III
            32111016, // 黑暗閃電            
        };
    }
    
    @Override
    public boolean containsJob(int job) {
        return MapleJob.is煉獄巫師(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 32111006: //甦醒
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.REAPER, (int) eff.info.get(MapleStatInfo.x));
                break;
            case 32101004: //紅色吸血術
                eff.statups.put(MapleBuffStat.COMBO_DRAIN, eff.info.get(MapleStatInfo.x));
                break;
            case 32101005: //長杖極速
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 32111014: //防禦姿態
                eff.statups.put(MapleBuffStat.STANCE, eff.info.get(MapleStatInfo.prop));
                break;
            case 32121005: //危機繩索
                eff.statups.put(MapleBuffStat.AURA_BOOST, (int) eff.getLevel());
                break;
            case 32121010: //煉獄鬥氣
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.ENRAGE, (int) (eff.getLevel() * 2000) + 1);
                eff.statups.put(MapleBuffStat.CRITICAL_RATE2, eff.info.get(MapleStatInfo.z));
                eff.statups.put(MapleBuffStat.MIN_CRITICAL_DAMAGE, eff.info.get(MapleStatInfo.y));
                break;
            case 32111010: //瞬間移動精通
                eff.info.put(MapleStatInfo.mpCon, eff.info.get(MapleStatInfo.y));
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.TELEPORT_MASTERY, eff.info.get(MapleStatInfo.x));
                eff.monsterStatus.put(MonsterStatus.STUN, 1);
                break;
            case 32121007: //楓葉祝福
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 32121053: //自由意志                
                break;
            case 32121054: //聯盟繩索                
                break;
            case 32121056: //戰鬥精通
                eff.statups.put(MapleBuffStat.BATTLE_MASTER, 2);
                break;
            case 32111012: // 藍色繩索
                eff.statups.put(MapleBuffStat.NEW_AURA, (int) eff.getLevel());
                eff.statups.put(MapleBuffStat.INDIE_ASR_R, eff.info.get(MapleStatInfo.indieAsrR));
                eff.info.put(MapleStatInfo.time, 2100000000);
                break;
            case 32121017: // 黑色光環
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.NEW_AURA, (int) eff.getLevel());
                eff.info.put(MapleStatInfo.time, 2100000000);
                break;
            case 32121018: // 減益效果光環
                eff.statups.put(MapleBuffStat.NEW_AURA, (int) eff.getLevel());
                eff.info.put(MapleStatInfo.time, 2100000000);
                break;
            case 32101009: // 紅色光環
                eff.statups.put(MapleBuffStat.NEW_AURA, (int) eff.getLevel());
                eff.statups.put(MapleBuffStat.COMBO_DRAIN, eff.info.get(MapleStatInfo.x));
                eff.info.put(MapleStatInfo.time, 2100000000);
                break;
            case 32001016: // 黃色光環
                eff.statups.put(MapleBuffStat.NEW_AURA, (int) eff.getLevel());
                eff.statups.put(MapleBuffStat.INDIE_BOOSTER, eff.info.get(MapleStatInfo.indieSpeed));
                eff.statups.put(MapleBuffStat.INDIE_SPEED, eff.info.get(MapleStatInfo.indieBooster));
                eff.info.put(MapleStatInfo.time, 2100000000);
                break;
            case 32100010: // 死神契約I
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.GRIM_CONTRACT, eff.info.get(MapleStatInfo.x));
                break;
            case 32110017: // 死神契約II
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.GRIM_CONTRACT, eff.info.get(MapleStatInfo.x));
                break;
            case 32120019: // 死神契約III
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.GRIM_CONTRACT, eff.info.get(MapleStatInfo.x));
                break;
            case 32111016: // 黑暗閃電                               
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.DARK_SHOCK, 1); 
                break;
            default:
                System.out.println("未知的 煉獄巫師(3200) Buff: " + skill);
                break;
        }
    }
}
