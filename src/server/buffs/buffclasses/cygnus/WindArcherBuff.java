package server.buffs.buffclasses.cygnus;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Maple
 */
public class WindArcherBuff extends AbstractBuffClass {

    public WindArcherBuff() {
        buffs = new int[]{
            13001022, //元素： 風暴
            13101024, //妖精援助
            13101023, //快速之箭
            13111023, //阿爾法
            13111024, //翡翠花園
            13121004, //風之祈禱
            13121005, //會心之眼
            13120008, //極限阿爾法
            13121053, //守護者榮耀
            13121054, //風暴使者
        };
    }

    @Override
    public boolean containsJob(int job) {
        return MapleJob.is皇家騎士團(job) && (job / 100) % 10 == 3;
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 13001022:// 元素： 風暴
                eff.statups.put(MapleBuffStat.STORM_ELEMENTAL, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                break;
            case 13101024:// 妖精援助
                eff.statups.put(MapleBuffStat.SOULARROW, 1);
                eff.statups.put(MapleBuffStat.CRITICAL_RATE, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                break;
            case 13101023:// 快速之箭
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 13111023://阿爾法
                eff.statups.put(MapleBuffStat.ALBATROSS, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.INDIE_MAX_HP, eff.info.get(MapleStatInfo.indieMhp));
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                eff.statups.put(MapleBuffStat.INDIE_BOOSTER, eff.info.get(MapleStatInfo.indieBooster));//true?
                eff.statups.put(MapleBuffStat.INDIE_CR_R, eff.info.get(MapleStatInfo.indieCr));
                break;
            case 13120008://極限阿爾法
                eff.statups.put(MapleBuffStat.ALBATROSS, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.INDIE_MAX_HP, eff.info.get(MapleStatInfo.indieMhp));
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                eff.statups.put(MapleBuffStat.INDIE_BOOSTER, eff.info.get(MapleStatInfo.indieBooster));//true?
                eff.statups.put(MapleBuffStat.INDIE_CR_R, eff.info.get(MapleStatInfo.indieCr));
                break;
            case 13111024:// 翡翠花園
                //spawn
                break;
            case 13121004:// 風之祈禱
                eff.statups.put(MapleBuffStat.ACCURACY_PERCENT, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.HAMSTRING, eff.info.get(MapleStatInfo.y));
                eff.statups.put(MapleBuffStat.AVOID_PERCENT, eff.info.get(MapleStatInfo.prop));
                eff.statups.put(MapleBuffStat.INDIE_MHP_R, eff.info.get(MapleStatInfo.indieMhpR));
                break;
            case 13121005:// 會心之眼
                eff.statups.put(MapleBuffStat.SHARP_EYES, (eff.info.get(MapleStatInfo.x) << 8) + eff.info.get(MapleStatInfo.criticaldamageMax));
                break;
            case 13121053:// 守護者榮耀
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.INDIE_MAX_DAMAGE_OVER, eff.info.get(MapleStatInfo.indieMaxDamageOver));
                break;
            case 13121054:// 風暴使者
                eff.statups.put(MapleBuffStat.STORM_BRINGER, eff.info.get(MapleStatInfo.x));
                break;
            default:
                System.out.println("Unhandled Buff: " + skill);
                break;
        }
    }
}
