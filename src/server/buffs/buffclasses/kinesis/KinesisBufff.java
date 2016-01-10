/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.buffs.buffclasses.kinesis;

import client.MapleJob;
import server.MapleStatEffect;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Pungin
 */
public class KinesisBufff extends AbstractBuffClass {
    
    @Override
    public boolean containsJob(int job) {
        return MapleJob.is凱內西斯(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            default:
                System.out.println("未知的 凱內西斯(14200) Buff: " + skill);
                break;
        }
    }
}
