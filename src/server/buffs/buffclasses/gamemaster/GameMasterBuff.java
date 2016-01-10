package server.buffs.buffclasses.gamemaster;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

public class GameMasterBuff extends AbstractBuffClass {

    public GameMasterBuff() {
        buffs = new int[]{
            9001001, // 終極輕功
            9001002, // 終極祈禱
            9001003, // GM的祝福
            9001004, // 終極隱藏
            9001008, // hyper body
        };
    }

    @Override
    public boolean containsJob(int job) {
        return MapleJob.is管理員(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 9001001: // 終極輕功
                eff.statups.put(MapleBuffStat.SPEED, eff.info.get(MapleStatInfo.speed));
                eff.statups.put(MapleBuffStat.JUMP, eff.info.get(MapleStatInfo.jump));
                break;
            case 9001002: // 終極祈禱
                eff.statups.put(MapleBuffStat.HOLY_SYMBOL, eff.info.get(MapleStatInfo.x));
                break;
            case 9001003: // GM的祝福
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indieMad));
                eff.statups.put(MapleBuffStat.INDIE_MHP_R, eff.info.get(MapleStatInfo.indieMhpR));
                eff.statups.put(MapleBuffStat.INDIE_MMP_R, eff.info.get(MapleStatInfo.indieMmpR));
                eff.statups.put(MapleBuffStat.WDEF, eff.info.get(MapleStatInfo.pdd));
                eff.statups.put(MapleBuffStat.MDEF, eff.info.get(MapleStatInfo.mdd));
                eff.statups.put(MapleBuffStat.AVOID, eff.info.get(MapleStatInfo.eva));
                eff.statups.put(MapleBuffStat.SPEED, eff.info.get(MapleStatInfo.speed));
                break;
            case 9001004: // 終極隱藏
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.DARKSIGHT, eff.info.get(MapleStatInfo.x));
                break;
            case 9001008: // hyper body
                eff.statups.put(MapleBuffStat.MAXHP, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.MAXMP, eff.info.get(MapleStatInfo.y));
                break;
            default:
                //System.out.println("未知的 管理員(900) Buff: " + skill);
                break;
        }
    }
}
