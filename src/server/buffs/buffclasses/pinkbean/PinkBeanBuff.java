/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.buffs.buffclasses.pinkbean;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Pungin
 */
public class PinkBeanBuff extends AbstractBuffClass {

    public PinkBeanBuff() {
        buffs = new int[]{
            131001006, // 放鬆
            131001009, // 大家加油！
            131001015, // 迷你啾出動
        };
    }

    @Override
    public boolean containsJob(int job) {
        return MapleJob.is皮卡啾(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 131001006: // 放鬆
                break;
            case 131001009: // 大家加油！
                break;
            case 131001015: // 迷你啾出動
                break;
            default:
                System.out.println("未知的 皮卡啾(13100) Buff: " + skill);
                break;
        }
    }
}
