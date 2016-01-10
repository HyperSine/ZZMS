package server.buffs.buffclasses.hero;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Maple
 */
public class LuminousBuff extends AbstractBuffClass {
    
    public LuminousBuff() {
        buffs = new int[]{
            27001004, // 擴充魔力 - Mana Well    
            27101202, // 黑暗之眼 - Pressure Void
            27100003, // 黑暗祝福 - Black Blessing
            27101004, // 極速詠唱 - Booster
            27111004, // 魔力護盾 - Shadow Shell
            27111005, // 光暗之盾 - Dusk Guard
            27111006, // 團隊精神 - Photic Meditation
            27121005, // 黑暗強化 - Dark Crescendo
            27121006, // 黑暗魔心 - Arcane Pitch
        };
    }
    
    @Override
    public boolean containsJob(int job) {
        return MapleJob.is夜光(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 27001004: // 擴充魔力 - Mana Well
                eff.statups.put(MapleBuffStat.INDIE_MMP_R, eff.info.get(MapleStatInfo.indieMmpR));
                break;
            case 27101202: // 黑暗之眼 - Pressure Void
                eff.info.put(MapleStatInfo.time, 0);
                eff.statups.put(MapleBuffStat.PRESSURE_VOID, eff.info.get(MapleStatInfo.x));
                break;
            case 27100003: // 黑暗祝福 - Black Blessing
                eff.statups.put(MapleBuffStat.BLACK_BLESSING, 1);//球的個數?應該
                break;            
            case 27101004: // 極速詠唱 - Booster
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 27111004: // 魔力護盾 - Shadow Shell
                eff.statups.put(MapleBuffStat.ABNORMAL_BUFF_RESISTANCES, eff.info.get(MapleStatInfo.asrR));
                eff.statups.put(MapleBuffStat.ABNORMAL_BUFF_RESISTANCES, eff.info.get(MapleStatInfo.terR));
                break;
            case 27111005: // 光暗之盾 - Dusk Guard
                eff.statups.put(MapleBuffStat.INDIE_PDD, eff.info.get(MapleStatInfo.pdd));
                eff.statups.put(MapleBuffStat.INDIE_MDD, eff.info.get(MapleStatInfo.mdd));
                break;
            case 27111006: // 團隊精神 - Photic Meditation
                eff.statups.put(MapleBuffStat.ENHANCED_MATK, eff.info.get(MapleStatInfo.emad));
                break;
            case 27121005: // 黑暗強化 - Dark Crescendo
                eff.statups.put(MapleBuffStat.DARK_CRESCENDO, eff.info.get(MapleStatInfo.x));
                break;
            case 27121006: // 黑暗魔心 - Arcane Pitch
                eff.statups.put(MapleBuffStat.IGNORE_DEF, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.FINALATTACK, eff.info.get(MapleStatInfo.y));
                break;
            default:
                System.out.println("夜光未註冊 - Buff: " + skill);
                break;
        }
    }
}
