package server.buffs.buffclasses.adventurer;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

public class ThiefBuff extends AbstractBuffClass {

    public ThiefBuff() {
        buffs = new int[]{
            //一轉
            4001003, // 隱身術
            4001005, // 速度激發
            //下忍
            4301003, // 自我速度激發

            //二轉
            //刺客
            4101003, // 極速暗殺
            //俠盜
//TODO            4200013, // 爆擊
            4201002, // 快速之刀
            4201009, // 輪迴
            4201011, // 楓幣護盾
            //中忍
            4311005, // 輪迴
            4311009, // 神速雙刀

            //三轉
            //暗殺者
            4111002, // 影分身
            4111009, // 無形鏢
            //神偷
            4211003, // 勇者掠奪術
            4211008, // 影分身
            //隱忍
//TODO            4330001, // 進階隱身術
//TODO            4330009, // 暗影迴避
            4331002, // 替身術
            4331006, // 隱‧鎖鏈地獄

            //四轉
            //夜使者
            4121000, // 楓葉祝福
            4121014, // 黑暗能量
            //暗影神偷
//TODO            4220015, // 致命爆擊
            4221000, // 楓葉祝福
            4221013, // 暗殺本能
            //影武者
            4341000, // 楓葉祝福
//TODO            4341002, // 絕殺刃
            4341007, // 荊棘特效

            //超技
            //夜使者
            4121053, // 傳說冒險
            4121054, // 出血毒素
            //暗影神偷
            4221053, // 傳說冒險
//TODO            4221054, // 翻轉硬幣
            //影武者
            4341052, // 修羅
            4341054, // 隱藏刀
            4341053, // 傳說冒險
        };
    }

    @Override
    public boolean containsJob(int job) {
        return MapleJob.is冒險家(job) && MapleJob.is盜賊(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 4001005: // 速度激發
            case 4301003: // 自我速度激發
                eff.statups.put(MapleBuffStat.SPEED, eff.info.get(MapleStatInfo.speed));
                eff.statups.put(MapleBuffStat.JUMP, eff.info.get(MapleStatInfo.jump));
                break;
            case 4001003: //Dark Sight
                eff.statups.put(MapleBuffStat.DARKSIGHT, eff.info.get(MapleStatInfo.x));
                break;
            case 4101003: //Claw Booster
            case 4201002: //Dagger Booster
            case 4311009: // 神速雙刀
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            case 4201011: //Meso Guard
                eff.statups.put(MapleBuffStat.MESOGUARD, eff.info.get(MapleStatInfo.x));
                break;
            case 4201009: // 輪迴
            case 4311005: // 輪迴
                eff.statups.put(MapleBuffStat.WATK, eff.info.get(MapleStatInfo.pad));
                break;
            case 4211003: //Pick Pocket
                eff.info.put(MapleStatInfo.time, 2100000000);
                eff.statups.put(MapleBuffStat.PICKPOCKET, eff.info.get(MapleStatInfo.x));
                break;
            case 4111002: //Shadow Partner
            case 4211008: //Shadow Partner
            case 4331002: // 替身術
                eff.statups.put(MapleBuffStat.SHADOWPARTNER, eff.info.get(MapleStatInfo.x));
                break;
            case 4111009: //Shadow Star
                eff.statups.put(MapleBuffStat.SPIRIT_CLAW, 0);
                break;
            case 4121014: //Dark Harmony
                eff.statups.put(MapleBuffStat.INDIE_PAD, eff.info.get(MapleStatInfo.indiePad));//test - works without
                break;
            case 4331006: // 隱‧鎖鏈地獄
                eff.statups.put(MapleBuffStat.INVINCIBILITY, 1);
                eff.statups.put(MapleBuffStat.STATUS_RESIST_TWO, 1);
                break;
            case 4341007: // 荊棘特效
                eff.statups.put(MapleBuffStat.STANCE, (int) eff.info.get(MapleStatInfo.prop));
                eff.statups.put(MapleBuffStat.ENHANCED_WATK, (int) eff.info.get(MapleStatInfo.epad));
                break;
            case 4221013: //Shadow Instinct
                break;
            case 4121000: //Maple Warrior 
            case 4221000: //Maple Warrior 
            case 4341000: // 楓葉祝福
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 4121054: //Bleed Dart
                break;
            case 4341052: // 修羅
                eff.statups.put(MapleBuffStat.ASURA_IS_ANGER, eff.info.get(MapleStatInfo.x));
                break;
            case 4341054: // 隱藏刀
                eff.statups.put(MapleBuffStat.BLADE_CLONE, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
            case 4121053: //Epic Adventure
            case 4221053: //Epic Adventure
            case 4341053: // 傳說冒險
                eff.statups.put(MapleBuffStat.INDIE_DAM_R, eff.info.get(MapleStatInfo.indieDamR));
                eff.statups.put(MapleBuffStat.INDIE_MAX_DAMAGE_OVER, eff.info.get(MapleStatInfo.indieMaxDamageOver));
                break;
            default:
                System.out.println("盜賊技能未處理,技能代碼: " + skill);
                break;
        }
    }
}
