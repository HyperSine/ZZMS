package server.buffs.buffclasses.sengoku;

import client.MapleBuffStat;
import client.MapleJob;
import server.MapleStatEffect;
import server.MapleStatInfo;
import server.buffs.AbstractBuffClass;

/**
 *
 * @author Charmander
 */
public class KannaBuff extends AbstractBuffClass  {

    public KannaBuff() {
        buffs = new int[]{
            42101023, // 幽玄氣息
            42101021, // 花炎結界
            42121021, // 花炎結界
            42101001, // 式神炎舞
            42101022, // 花狐的祝福
            
            42101002, // 影朋‧花狐
            42101003, // 扇‧孔雀
            42111004, // Blossom Barrier - mist
            42121005, // Bellflower Barrier - mist
            42121006, // Maple Warrior
            42121054, // Blackhearted Curse
            42121053, // Princess's Vow
            
            42101004, // 紫扇仰波‧焰
            42111006, // Frozen Shikigami Haunting
            42121008, // Mighty Shikigami Haunting
            42111003, // Kishin Shoukan
            
        };
    }
    
    @Override
    public boolean containsJob(int job) {
        return MapleJob.is陰陽師(job);
    }

    @Override
    public void handleBuff(MapleStatEffect eff, int skill) {
        switch (skill) {
            case 42101023: // 幽玄氣息
                eff.statups.put(MapleBuffStat.IGNORE_DEF, eff.info.get(MapleStatInfo.x));
                eff.statups.put(MapleBuffStat.STANCE, eff.info.get(MapleStatInfo.prop));
                break;
            case 42101021: // 花炎結界
                eff.statups.put(MapleBuffStat.FOX_FIRE, eff.info.get(MapleStatInfo.x));
             case 42121021:// Foxfire
             case 42101001:// Shikigami Charm
             case 42100010:
                 eff.statups.put(MapleBuffStat.SUMMON, 1);
                 break;
             case 42101022: // 花狐的祝福
                 eff.statups.put(MapleBuffStat.HAKU_BLESS, eff.info.get(MapleStatInfo.x));
                 eff.statups.put(MapleBuffStat.INDIE_PDD, eff.info.get(MapleStatInfo.indiePdd));
                 eff.statups.put(MapleBuffStat.INDIE_MDD, eff.info.get(MapleStatInfo.indieMdd));
                 break;
            case 42101002: // 影朋‧花狐
                eff.statups.put(MapleBuffStat.HAKU_REBORN, 1);
                break;
            case 42101003: // 扇‧孔雀
                eff.statups.put(MapleBuffStat.BOOSTER, eff.info.get(MapleStatInfo.x));
                break;
            
            case 42121006: // Maple Warrior
                eff.statups.put(MapleBuffStat.MAPLE_WARRIOR, eff.info.get(MapleStatInfo.x));
                break;
            case 42121054: // Blackhearted Curse
                //TODO 阴阳师BUFF42121054
                break;
            case 42121053: // Princess's Vow
                //TODO 阴阳师BUFF42121053
                break;
            case 42101004: // 紫扇仰波‧焰
            case 42111006: // Frozen Shikigami Haunting
            case 42121008: // Mighty Shikigami Haunting
                    eff.statups.put(MapleBuffStat.SHIKIGAMI, -eff.info.get(MapleStatInfo.x));
                    break;
            case 42111003: // Kishin Shoukan
                eff.info.put(MapleStatInfo.time, 60000);
                eff.statups.put(MapleBuffStat.SUMMON, 1);
                break;
            default:
                //System.out.println("Kanna skill not coded: " + skill);
                break;
        }
    }
}
