package server.buffs.buffclasses.hero;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

public class PhantomBuff extends AbstractBuffClass {

    public PhantomBuff() {
        buffs = new int[]{
            20031211, //鬼鬼祟祟的移動
            20031205, //幻影斗蓬
            24101005, //極速手杖
            24111002, //幸運幻影
            24111003, //幸運卡牌守護
            24111005, //月光賜福
            24121003, //最終的夕陽
            24121004, //艾麗亞祝禱
            24121008, //楓葉祝福
            24121054 //終極審判
        };
    }

    @Override
    public boolean containsJob(int job) {
        return MapleJob.is幻影俠盜(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 20031211: //鬼鬼祟祟的移動
                eff.statups.put(MapleBuffStat.DARKSIGHT, eff.info.get(MapleStatInfo.x));
                break;
            case 20031205: //幻影斗蓬
                eff.statups.put(MapleBuffStat.PHANTOM_MOVE, eff.info.get(MapleStatInfo.x));
                break;
            case 24101005: //極速手杖
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x) * 2);
                break;
            case 24111002: //幸運幻影
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.FINAL_FEINT, eff.info.get(MapleStatInfo.x));
                break;
            case 24111003: //幸運卡牌守護
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.INDIE_MHP_R, eff.info.get(MapleStatInfo.indieMhpR));
                eff.statups.put(MapleBuffStat.INDIE_MMP_R, eff.info.get(MapleStatInfo.indieMmpR));
                break;
            case 24111005: //月光賜福
                eff.statups.clear();
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                eff.statups.put(MapleBuffStat.INDIE_ACC, eff.info.get(MapleStatInfo.indieAcc));
                break;
            case 24121003: //最終的夕陽
                eff.info.put(MapleStatInfo.damage, eff.info.get(MapleStatInfo.v));
                eff.info.put(MapleStatInfo.attackCount, eff.info.get(MapleStatInfo.w));
                eff.info.put(MapleStatInfo.mobCount, eff.info.get(MapleStatInfo.x));
                break;
            case 24121004: //艾麗亞祝禱
                eff.statups.put(MapleBuffStat.DAMAGE_RATE, eff.info.get(MapleStatInfo.damR));
                eff.statups.put(MapleBuffStat.IGNORE_DEF, eff.info.get(MapleStatInfo.damR));
                break;
            case 24121008://楓葉祝福
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 24121054:
                eff.statups.put(MapleBuffStat.FINAL_JUDGMENT_DRAW, eff.info.get(MapleStatInfo.x));
                break;
            default:
                System.out.println("未知的 幻影俠盜(2400) Buff: " + skill);
                break;
        }
    }
}
