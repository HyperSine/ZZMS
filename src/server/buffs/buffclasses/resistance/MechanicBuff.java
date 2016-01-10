package server.buffs.buffclasses.resistance;

import client.MapleBuffStat;
import client.MapleJob;
import client.MonsterStatus;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

public class MechanicBuff extends AbstractBuffClass {
	public MechanicBuff() {
		buffs = new int[]{
                    35101006, //機甲戰神極速
                    35001002, //合金盔甲: 原型
                    35120000, //合金盔甲終極
                    35111004, //合金盔甲: 重機槍
                    35121005, //合金盔甲: 導彈罐
                    35121013, //合金盔甲: 重機槍
                    35101007, //全備型盔甲 (Perfect Armor)
                    35001001, //火焰發射
                    35101009, //強化的火焰發射
                    35111002, //磁場
                    35111005, //加速器 : EX-7
                    35121003, //戰鬥機器 : 巨人錘
                    35111011, //治療機器人 : H-LX
                    35121009, //機器人工廠 : RM1
                    35121011,
                    35121010, //擴音器 : AF-11
                    35111001, //賽特拉特
                    35111010, //衛星
                    35111009, //賽特拉特
                    35121006 //終極賽特拉特
		};
	}
	
	@Override
    public boolean containsJob(int job) {
        return MapleJob.is機甲戰神(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 35101006: //機甲戰神極速
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 35101005: //開放通道 : GX-9
                eff.statups.put(MapleBuffStat.SOULARROW, eff.info.get(MapleStatInfo.x));
                break;
            case 35120000: //合金盔甲終極
            case 35001002: //合金盔甲: 原型
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.MONSTER_RIDING, 0);
                eff.statups.put(MapleBuffStat.ENHANCED_MAXHP, eff.info.get(MapleStatInfo.emhp));
                eff.statups.put(MapleBuffStat.ENHANCED_MAXMP, eff.info.get(MapleStatInfo.emmp));
                eff.statups.put(MapleBuffStat.ENHANCED_WATK, eff.info.get(MapleStatInfo.epad));
                eff.statups.put(MapleBuffStat.ENHANCED_WDEF, eff.info.get(MapleStatInfo.epdd));
                eff.statups.put(MapleBuffStat.ENHANCED_MDEF, eff.info.get(MapleStatInfo.emdd));                
                eff.statups.put(MapleBuffStat.INDIE_SPEED, eff.info.get(MapleStatInfo.indieSpeed));
                break;
            case 35101007: //全備型盔甲 (Perfect Armor)
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.PERFECT_ARMOR, eff.info.get(MapleStatInfo.x));
                break;
            case 35111002: //磁場
                eff.statups.put(MapleBuffStat.SUMMON, 1);
                eff.monsterStatus.put(MonsterStatus.STUN, 1);
                break;
            case 35111005: //加速器 : EX-7
                eff.statups.put(MapleBuffStat.SUMMON, 1);
                eff.monsterStatus.put(MonsterStatus.SPEED, eff.info.get(MapleStatInfo.x));
                eff.monsterStatus.put(MonsterStatus.WDEF, eff.info.get(MapleStatInfo.y));
                break;
            case 35121003: //戰鬥機器 : 巨人錘
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.SUMMON, 1);
                break;
            case 35111011: //治療機器人 : H-LX
            case 35121009: //機器人工廠 : RM1
            case 35121011:
                eff.statups.put(MapleBuffStat.SUMMON, 1);
                break;
            case 35121010: //擴音器 : AF-11
                eff.info.put(MapleStatInfo.time, 60000);
                eff.statups.put(MapleBuffStat.DAMAGE_BUFF, eff.info.get(MapleStatInfo.x));
                break;
            case 35111001: //賽特拉特
            case 35111010: //衛星
            case 35111009: //賽特拉特
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.PUPPET, 1);
                break;
            case 35121006: //終極賽特拉特
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.SATELLITESAFE_PROC, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.SATELLITESAFE_ABSORB, eff.info.get(MapleStatInfo.y));
                break;
            case 35001001: //火焰發射
            case 35101009: //強化的火焰發射
                eff.info.put(MapleStatInfo.time, 12000);
                eff.statups.put(MapleBuffStat.MECH_CHANGE, (int) eff.getLevel()); //ya wtf
                break;
            case 35121013: //合金盔甲: 重機槍
            case 35111004: //合金盔甲: 重機槍
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.MECH_CHANGE, (int) eff.getLevel()); //ya wtf
                break;
            case 35121005: //合金盔甲: 導彈罐
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.MECH_CHANGE, (int) eff.getLevel()); //ya wtf
                break;
            case 35121007:
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            default:
                System.out.println("未知的機甲戰神 Buff: " + skill);
                break;
        }
    }
}
