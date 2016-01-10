package server.buffs.buffclasses.cygnus;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

public class BlazeWizardBuff extends AbstractBuffClass {
    public BlazeWizardBuff() {
        buffs = new int[]{
            12000022, // 元素:火焰
            12101023, // 火之書
            12101024, // 燃燒
        };
    }

    @Override
    public boolean containsJob(int job) {
        return MapleJob.is烈焰巫師(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 12000022: //元素:火焰
                eff.statups.put(MapleBuffStat.INDIE_MAD, eff.info.get(MapleStatInfo.x));
                break;
            case 12101023: //火之書
                eff.statups.put(MapleBuffStat.INDIE_MAD, eff.info.get(MapleStatInfo.indieMad));
                eff.statups.put(MapleBuffStat.INDIE_BOOSTER, eff.info.get(MapleStatInfo.indieBooster));
                break;
            case 12101024: //燃燒
                eff.statups.put(MapleBuffStat.CONTROLLED_BURN, 1);
                break;
            default:
                //System.out.println("Unhandled Buff: " + skill);
                break;
        }
    }
}
