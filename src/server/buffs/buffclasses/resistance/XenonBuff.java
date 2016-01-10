/*
 * To change this template, choose Tools | Templates
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
 * @author Maple
 */
public class XenonBuff extends AbstractBuffClass {

    public XenonBuff() {
        buffs = new int[]{
            30021237, // 自由飛行(Done)
            36001002, // 傾斜功率(Done)
            36101001, // 離子推進器
            36101002, // 線性透視(Done)
            36101003, // 效率管道
            36101004, // 能量加速器(Done)
            36111006, // 虛擬投影(Done)
            36111008, // 額外供應(Done)
            36111003, // 全面防禦
            36121003, // 全域代碼
            36121004, // 攻擊矩陣(Done)
            36121008, // 楓葉祝福(Done)
            36121054, // 發電機
            36121007, // 時空膠囊
        };
    }
    
    @Override
    public boolean containsJob(int job) {
        return MapleJob.is傑諾(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 30021237: // 自由飛行(Done)
                eff.statups.put(MapleBuffStat.XENON_FLY, 1);
                break;
            case 36001002: // 傾斜功率
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));
                break;
            case 36101001: // 離子推進器
                eff.statups.put(MapleBuffStat.USING_SKILL_MOVE, eff.info.get(MapleStatInfo.x));
                break;
            case 36101002: // 線性透視
                //eff.info.put(MapleStatInfo.powerCon, Integer.valueOf(6));
                eff.statups.put(MapleBuffStat.CRITICAL_RATE, eff.info.get(MapleStatInfo.x));
                break;
            case 36101003: // 效率管道
                eff.statups.put(MapleBuffStat.INDIE_MMP_R, eff.info.get(MapleStatInfo.indieMmpR));
                eff.statups.put(MapleBuffStat.INDIE_MHP_R, eff.info.get(MapleStatInfo.indieMhpR));
                break;
            case 36101004: // 能量加速器
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 36111006: // 虛擬投影
                eff.statups.put(MapleBuffStat.SHADOWPARTNER, eff.info.get(MapleStatInfo.x));
                break;
            case 36111008: // 額外供應
                eff.statups.put(MapleBuffStat.SUPPLY_SURPLUS, eff.info.get(MapleStatInfo.x));
                break;
            case 36111003: // 全面防禦
                eff.statups.put(MapleBuffStat.DARK_CRESCENDO, eff.info.get(MapleStatInfo.prop));
                eff.statups.put(MapleBuffStat.WATER_SHIELD, eff.info.get(MapleStatInfo.z));
                break;
            case 36121003: // 全域代碼
                eff.statups.put(MapleBuffStat.BOSS_DAMAGE, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                break; 
            case 36121004: // 攻擊矩陣
                eff.statups.put(MapleBuffStat.STANCE, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.IGNORE_DEF, eff.info.get(MapleStatInfo.y));
                break;
            case 36121008: // 楓葉祝福
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 36121054://發電機
                eff.info.put(MapleStatInfo.time, 10000);//AddBugFixTest
                break;
            case 36121007://時空膠囊
                eff.info.put(MapleStatInfo.time, 10000);//AddBugFixTest
                break;
            default:
                // System.out.println("Unhandled Xenon Buff: " + skill);
                break;
        }
    }
}
