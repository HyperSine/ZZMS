package server.buffs.buffclasses.cygnus;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

public class MihileBuff extends AbstractBuffClass {

    public MihileBuff() {
        buffs = new int[]{
            51101003, // 快速之劍
            51101004, // 激勵
            51111003, // 閃耀激發
            51111004, // 靈魂抗性
            51121004, // 格檔
            51121005, // 楓葉祝福
            51121006, // 靈魂之怒
            51121053, // 明日女皇
            51121054, // 神聖護石
        };
    }
    
    @Override
    public boolean containsJob(int job) {
        return MapleJob.is米哈逸(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        if (!containsSkill(skill)) {
            return;
        }
       
        switch (skill) {
            case 51101003: // 快速之劍
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x) * 2);
                break;
            case 51101004: // 激勵
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                eff.statups.put(MapleBuffStat.POWERGUARD, eff.info.get(MapleStatInfo.x));
                break;
            case 51111003: // 閃耀激發
                eff.statups.put(MapleBuffStat.WK_CHARGE, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.DAMAGE_BUFF, eff.info.get(MapleStatInfo.z));
                break;
            case 51111004: // 靈魂抗性
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.z));
                eff.statups.put(MapleBuffStat.DEFENCE_BOOST_R, eff.info.get(MapleStatInfo.x));
                break;
            case 51121004: // 格檔
                eff.statups.put(MapleBuffStat.STANCE, (int) eff.info.get(MapleStatInfo.prop));
                break;
            case 51121005: // 楓葉祝福
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 51121006: // 靈魂之怒
                eff.statups.put(MapleBuffStat.DAMAGE_BUFF, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.CRITICAL_RATE_BUFF, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.CRITICAL_RATE_BUFF, eff.info.get(MapleStatInfo.z));
                break;
            case 51121053:
                break;
            case 51121054:
                break;
            default:
                //System.out.println("Unhandled Buff: " + skill);
                break;
        }
    }
}
