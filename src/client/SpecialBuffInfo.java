package client;

import java.io.Serializable;

/**
 *
 * @author 黯淡
 */
public class SpecialBuffInfo  implements Serializable {
    
    private int buffid = 0;
    MapleBuffStat m_buff = null;
    MapleBuffStatValueHolder mbsvh = null;
    
    public SpecialBuffInfo(final int skillid, final MapleBuffStat buff, final  MapleBuffStatValueHolder mb) {
        this.buffid = skillid;
        this.m_buff = buff;
        this.mbsvh = mb;
    }
    
    public int getSkillId() {
        return buffid;
    }
    
    public MapleBuffStat getBuffStat() {
        return m_buff;
    }
    
    public MapleBuffStatValueHolder getBuffStatValueHolder() {
        return mbsvh;
    }
}
