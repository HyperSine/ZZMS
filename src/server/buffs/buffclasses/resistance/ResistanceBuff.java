/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
 * @author Fate
 */
public class ResistanceBuff extends AbstractBuffClass {
    public ResistanceBuff() {
        buffs = new int[]{
            30001001, //潛入
        };
    }
    
    @Override
    public boolean containsJob(int job) {
        return MapleJob.is末日反抗軍(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 30001001: //潛入
                eff.statups.put(MapleBuffStat.INFILTRATE, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.SPEED, eff.info.get(MapleStatInfo.x));
                break;
            default:
                System.out.println("未知的 市民(3000) Buff: " + skill);
                break;
        }
    }
}
