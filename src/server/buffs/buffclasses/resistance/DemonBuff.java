/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package server.buffs.buffclasses.resistance;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Sunny
 */
public class DemonBuff extends AbstractBuffClass {
    
    public DemonBuff() {
        buffs = new int[]{
                // 惡魔殺手
                31001001, // booster
                31101003, // Vengeance
                31111004, // Black-Hearted Strength
                31121007, // Boundless Rage
                31121004, // MW
                31121002, // Leech Aura  
                // 惡魔復仇者
                31011001, // 超載解放
                31201002, // 急速惡魔
                31201003, // 深淵之怒
                31211003, // 邪惡強化
                31211004, // 急速療癒
                31221004, // 地獄之力
                31221008, // 楓葉祝福                
                // 共通              
                31121054, // 高貴血統
        };
    }
    
    @Override
    public boolean containsJob(int job) {
        return MapleJob.is惡魔(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
           case 31001001:
           case 31201002: // 急速惡魔
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x) * 2);
                break;
           case 31011001: // 超載解放
                eff.statups.put(MapleBuffStat.INDIE_MHP_R, eff.info.get(MapleStatInfo.indieMhpR));
                break;
           case 31101003: // Vengeance
                eff.statups.put(MapleBuffStat.POWERGUARD, eff.info.get(MapleStatInfo.y));
                break;
           case 31201003: // 深淵之怒 
               eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                break;
           case 31211003: // 邪惡強化
                eff.statups.put(MapleBuffStat.WATER_SHIELD, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.z));
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.x));
                break;
           case 31211004: // 急速療癒
                eff.statups.put(MapleBuffStat.DIABOLIC_RECOVERY, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.INDIE_MHP_R, eff.info.get(MapleStatInfo.indieMhpR));
                break;
           case 31221004: // 地獄之力
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.INDIE_BOOSTER, eff.info.get(MapleStatInfo.indieBooster));
                break;
           case 31111004: // Black-Hearted Strength
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.z));
                eff.statups.put(MapleBuffStat.DEFENCE_BOOST_R, eff.info.get(MapleStatInfo.x));
                break;
           case 31121007: // Boundless Rage
                eff.statups.put(MapleBuffStat.BOUNDLESS_RAGE, 1);
                break;
           case 31121004: // MW
           case 31221008: // 楓葉祝福
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
           case 31121002: //Leech Aura
                eff.statups.put(MapleBuffStat.ABSORB_DAMAGE_HP, eff.info.get(MapleStatInfo.x));
                break;
           case 31121054:
                eff.statups.put(MapleBuffStat.SHADOWPARTNER, eff.info.get(MapleStatInfo.x));
                break;
            default:
                System.out.println("未知的 惡魔(3100) Buff: " + skill);
                break;
        }
    }
}
