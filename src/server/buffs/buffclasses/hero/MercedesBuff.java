package server.buffs.buffclasses.hero;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

public class MercedesBuff extends AbstractBuffClass {
	public MercedesBuff() {
		buffs = new int[]{
                    23101002, //雙弩槍推進器
                    23101003, //靈魂灌注
                    23111004, //依古尼斯咆哮
                    23111005, //水之盾
                    23121054, //精靈祝福
		};
	}
	
	@Override
    public boolean containsJob(int job) {
        return MapleJob.is精靈遊俠(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 23101002: //雙弩槍推進器
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x) * 2);
                break;
            case 23101003: //靈魂灌注
                eff.statups.put(MapleBuffStat.DAMAGE_R, eff.info.get(MapleStatInfo.damage));
                eff.statups.put(MapleBuffStat.CRITICAL_RATE, eff.info.get(MapleStatInfo.x));
                break;
            case 23111004: //依古尼斯咆哮
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                eff.statups.put(MapleBuffStat.PROP, eff.info.get(MapleStatInfo.prop));
                break;
            case 23111005: //水之盾
                eff.statups.put(MapleBuffStat.ABNORMAL_STATUS_R, eff.info.get(MapleStatInfo.terR));
                eff.statups.put(MapleBuffStat.ELEMENTAL_STATUS_R, eff.info.get(MapleStatInfo.terR));
                eff.statups.put(MapleBuffStat.WATER_SHIELD, eff.info.get(MapleStatInfo.x));
                break;
            case 23121054: //精靈祝福
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                break;
            default:
                System.out.println("未知的 精靈遊俠(2300) Buff: " + skill);
                break;
        }
    }
}
