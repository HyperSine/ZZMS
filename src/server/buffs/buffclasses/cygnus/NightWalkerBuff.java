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
public class NightWalkerBuff extends AbstractBuffClass {
    public NightWalkerBuff() {
        buffs = new int[]{            
            14001021, // 元素 : 闇黑
            14001022, // 急速
            14001023, // 黑暗面
            14001027, // 暗影蝙蝠
        };
    }
    
    @Override
    public boolean containsJob(int job) {
        return MapleJob.is暗夜行者(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {  
            case 14001021: //元素 : 闇黑
                eff.statups.put(MapleBuffStat.DARK_ELEMENTAL, 1);
                break;
            case 14001022: //急速
                eff.statups.put(MapleBuffStat.JUMP, eff.info.get(MapleStatInfo.jump));
                eff.statups.put(MapleBuffStat.SPEED, eff.info.get(MapleStatInfo.speed));
                break;
            case 14001023: //黑暗面
                eff.statups.put(MapleBuffStat.DARKSIGHT, eff.info.get(MapleStatInfo.x));
                break;
            case 14001027: //暗影蝙蝠
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.SHADOW_BAT, 1);
                break;
            default:
                //System.out.println("暗夜行者 skill not coded: " + skill);
                break;
        }
    }    
}
