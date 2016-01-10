package client.inventory;

import constants.EventConstants;
import constants.GameConstants;
import constants.ItemConstants;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import server.MapleItemInformationProvider;
import server.Randomizer;

public class Equip extends Item implements Serializable {

    public static enum ScrollResult {

        SUCCESS,
        FAIL,
        CURSE
    }
    public static final long ARMOR_RATIO = 350000L;
    public static final long WEAPON_RATIO = 700000L;
    //charm: -1 = has not been initialized yet, 0 = already been worn, >0 = has teh charm exp
    private byte state = 0, bonusState = 0, oldState = 0, upgradeSlots = 0, level = 0, vicioushammer = 0, platinumhammer = 0, enhance = 0, enhanctBuff = 0, reqLevel = 0, yggdrasilWisdom = 0, bossDamage = 0, ignorePDR = 0, totalDamage = 0, allStat = 0, karmaCount = -1, fire = -1, starforce;
    private short str = 0, dex = 0, _int = 0, luk = 0, hp = 0, mp = 0, watk = 0, matk = 0, wdef = 0, mdef = 0, acc = 0, avoid = 0, hands = 0, speed = 0, jump = 0, charmExp = 0, pvpDamage = 0, soulname, soulenchanter, soulpotential;
    private int durability = -1, incSkill = -1, potential1 = 0, potential2 = 0, potential3 = 0, bonuspotential1 = 0, bonuspotential2 = 0, bonuspotential3 = 0, fusionAnvil = 0, socket1 = 0, socket2 = 0, socket3 = 0, soulskill;
    private long itemEXP = 0;
    private boolean finalStrike = false;
    private boolean trace = false;
    private int failCount = 0;
    private MapleRing ring = null;
    private MapleAndroid android = null;
    private List<EquipStat> stats = new LinkedList();
    private List<EquipSpecialStat> specialStats = new LinkedList();
    private Map<EquipStat, Long> statsTest = new LinkedHashMap<>();

    public Equip(int id, short position, byte flag) {
        super(id, position, (short) 1, flag);
    }

    public Equip(int id, short position, int uniqueid, short flag) {
        super(id, position, (short) 1, flag, uniqueid);
    }

    @Override
    public Item copy() {
        Equip ret = new Equip(getItemId(), getPosition(), getUniqueId(), getFlag());
        ret.str = str;
        ret.dex = dex;
        ret._int = _int;
        ret.luk = luk;
        ret.hp = hp;
        ret.mp = mp;
        ret.matk = matk;
        ret.mdef = mdef;
        ret.watk = watk;
        ret.wdef = wdef;
        ret.acc = acc;
        ret.avoid = avoid;
        ret.hands = hands;
        ret.speed = speed;
        ret.jump = jump;
        ret.enhance = enhance;
        ret.upgradeSlots = upgradeSlots;
        ret.level = level;
        ret.itemEXP = itemEXP;
        ret.durability = durability;
        ret.vicioushammer = vicioushammer;
        ret.platinumhammer = platinumhammer;
        ret.state = state;
        ret.potential1 = potential1;
        ret.potential2 = potential2;
        ret.potential3 = potential3;
        ret.bonusState = bonusState;
        ret.bonuspotential1 = bonuspotential1;
        ret.bonuspotential2 = bonuspotential2;
        ret.bonuspotential3 = bonuspotential3;
        ret.fusionAnvil = fusionAnvil;
        ret.socket1 = socket1;
        ret.socket2 = socket2;
        ret.socket3 = socket3;
        ret.charmExp = charmExp;
        ret.pvpDamage = pvpDamage;
        ret.incSkill = incSkill;
        ret.enhanctBuff = enhanctBuff;
        ret.reqLevel = reqLevel;
        ret.yggdrasilWisdom = yggdrasilWisdom;
        ret.finalStrike = finalStrike;
        ret.bossDamage = bossDamage;
        ret.ignorePDR = ignorePDR;
        ret.totalDamage = totalDamage;
        ret.allStat = allStat;
        ret.karmaCount = karmaCount;
        ret.fire = fire;
        ret.setGiftFrom(getGiftFrom());
        ret.setOwner(getOwner());
        ret.setQuantity(getQuantity());
        ret.setExpiration(getExpiration());
        ret.stats = stats;
        ret.specialStats = specialStats;
        ret.statsTest = statsTest;
        ret.soulname = soulname;
        ret.soulenchanter = soulenchanter;
        ret.soulpotential = soulpotential;
        ret.soulskill = soulskill;
        ret.starforce = starforce;
        return ret;
    }

    @Override
    public byte getType() {
        return 1;
    }

    public byte getUpgradeSlots() {
        return upgradeSlots;
    }

    public short getStr() {
        return str;
    }

    public short getDex() {
        return dex;
    }

    public short getInt() {
        return _int;
    }

    public short getLuk() {
        return luk;
    }

    public short getHp() {
        return hp;
    }

    public short getMp() {
        return mp;
    }

    public short getWatk() {
        return watk;
    }

    public short getMatk() {
        return matk;
    }

    public short getWdef() {
        return wdef;
    }

    public short getMdef() {
        return mdef;
    }

    public short getAcc() {
        return acc;
    }

    public short getAvoid() {
        return avoid;
    }

    public short getHands() {
        return hands;
    }

    public short getSpeed() {
        return speed;
    }

    public short getJump() {
        return jump;
    }

    public void setStr(short str) {
        if (str < 0) {
            str = 0;
        }
        this.str = str;
    }

    public void setDex(short dex) {
        if (dex < 0) {
            dex = 0;
        }
        this.dex = dex;
    }

    public void setInt(short _int) {
        if (_int < 0) {
            _int = 0;
        }
        this._int = _int;
    }

    public void setLuk(short luk) {
        if (luk < 0) {
            luk = 0;
        }
        this.luk = luk;
    }

    public void setHp(short hp) {
        if (hp < 0) {
            hp = 0;
        }
        this.hp = hp;
    }

    public void setMp(short mp) {
        if (mp < 0) {
            mp = 0;
        }
        this.mp = mp;
    }

    public void setWatk(short watk) {
        if (watk < 0) {
            watk = 0;
        }
        this.watk = watk;
    }

    public void setMatk(short matk) {
        if (matk < 0) {
            matk = 0;
        }
        this.matk = matk;
    }

    public void setWdef(short wdef) {
        if (wdef < 0) {
            wdef = 0;
        }
        this.wdef = wdef;
    }

    public void setMdef(short mdef) {
        if (mdef < 0) {
            mdef = 0;
        }
        this.mdef = mdef;
    }

    public void setAcc(short acc) {
        if (acc < 0) {
            acc = 0;
        }
        this.acc = acc;
    }

    public void setAvoid(short avoid) {
        if (avoid < 0) {
            avoid = 0;
        }
        this.avoid = avoid;
    }

    public void setHands(short hands) {
        if (hands < 0) {
            hands = 0;
        }
        this.hands = hands;
    }

    public void setSpeed(short speed) {
        if (speed < 0) {
            speed = 0;
        }
        this.speed = speed;
    }

    public void setJump(short jump) {
        if (jump < 0) {
            jump = 0;
        }
        this.jump = jump;
    }

    public void setUpgradeSlots(byte upgradeSlots) {
        this.upgradeSlots = upgradeSlots;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public byte getViciousHammer() {
        return vicioushammer;
    }

    public void setViciousHammer(byte ham) {
        vicioushammer = ham;
    }
    
    public byte getPlatinumHammer() {
        return platinumhammer;
    }

    public void setPlatinumHammer(byte ham) {
        platinumhammer = ham;
    }
    
    public byte getHammer() {
        return (byte) (vicioushammer + platinumhammer);
    }

    public long getItemEXP() {
        return itemEXP;
    }

    public void setItemEXP(long itemEXP) {
        if (itemEXP < 0) {
            itemEXP = 0;
        }
        this.itemEXP = itemEXP;
    }

    public long getEquipExp() {
        if (itemEXP <= 0) {
            return 0;
        }
        //aproximate value
        if (ItemConstants.類型.武器(getItemId())) {
            return itemEXP / WEAPON_RATIO;
        } else {
            return itemEXP / ARMOR_RATIO;
        }
    }

    public long getEquipExpForLevel() {
        if (getEquipExp() <= 0) {
            return 0;
        }
        long expz = getEquipExp();
        for (int i = getBaseLevel(); i <= GameConstants.getMaxLevel(getItemId()); i++) {
            if (expz >= GameConstants.getExpForLevel(i, getItemId())) {
                expz -= GameConstants.getExpForLevel(i, getItemId());
            } else { //for 0, dont continue;
                break;
            }
        }
        return expz;
    }

    public long getExpPercentage() {
        if (getEquipLevel() < getBaseLevel() || getEquipLevel() > GameConstants.getMaxLevel(getItemId()) || GameConstants.getExpForLevel(getEquipLevel(), getItemId()) <= 0) {
            return 0;
        }
        return getEquipExpForLevel() * 100 / GameConstants.getExpForLevel(getEquipLevel(), getItemId());
    }

    public int getEquipLevel() {
        int fixLevel = 0;
        Map<String, Integer> equipStats = MapleItemInformationProvider.getInstance().getEquipStats(getItemId());
        if (equipStats.containsKey("fixLevel")) {
            fixLevel = equipStats.get("fixLevel");
        }

        if (GameConstants.getMaxLevel(getItemId()) <= 0) {
            return fixLevel;
        }

        int levelz = getBaseLevel() + fixLevel;
        if (getEquipExp() <= 0) {
            return levelz;
        }
        long expz = getEquipExp();
        for (int i = levelz; (GameConstants.getStatFromWeapon(getItemId()) == null ? (i <= GameConstants.getMaxLevel(getItemId())) : (i < GameConstants.getMaxLevel(getItemId()))); i++) {
            if (expz >= GameConstants.getExpForLevel(i, getItemId())) {
                levelz++;
                expz -= GameConstants.getExpForLevel(i, getItemId());
            } else { //for 0, dont continue;
                break;
            }
        }
        return levelz;
    }

    public int getBaseLevel() {
        return (GameConstants.getStatFromWeapon(getItemId()) == null ? 1 : 0);
    }

    @Override
    public void setQuantity(short quantity) {
        if (quantity < 0 || quantity > 1) {
            throw new RuntimeException("Setting the quantity to " + quantity + " on an equip (itemid: " + getItemId() + ")");
        }
        super.setQuantity(quantity);
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(final int dur) {
        durability = dur;
    }

    public byte getEnhanctBuff() {
        return enhanctBuff;
    }

    public void setEnhanctBuff(byte enhanctBuff) {
        this.enhanctBuff = enhanctBuff;
    }

    public byte getReqLevel() {
        return reqLevel;
    }

    public void setReqLevel(byte reqLevel) {
        this.reqLevel = reqLevel;
    }

    public byte getYggdrasilWisdom() {
        return yggdrasilWisdom;
    }

    public void setYggdrasilWisdom(byte yggdrasilWisdom) {
        this.yggdrasilWisdom = yggdrasilWisdom;
    }

    public boolean getFinalStrike() {
        return finalStrike;
    }

    public void setFinalStrike(boolean finalStrike) {
        this.finalStrike = finalStrike;
    }

    public byte getBossDamage() {
        return bossDamage;
    }

    public void setBossDamage(byte bossDamage) {
        this.bossDamage = bossDamage;
    }

    public byte getIgnorePDR() {
        return ignorePDR;
    }

    public void setIgnorePDR(byte ignorePDR) {
        this.ignorePDR = ignorePDR;
    }

    public byte getTotalDamage() {
        return totalDamage;
    }

    public void setTotalDamage(byte totalDamage) {
        this.totalDamage = totalDamage;
    }

    public byte getAllStat() {
        return allStat;
    }

    public void setAllStat(byte allStat) {
        this.allStat = allStat;
    }

    public void setFailCount(int value) {
        failCount = value;
    }

    public int getFailCount() {
        return failCount;
    }

    public boolean isTrace() {
        return trace;
    }

    public void setTrace(boolean value) {
        trace = value;
    }

    public byte getKarmaCount() {
        return karmaCount;
    }

    public void setKarmaCount(byte karmaCount) {
        this.karmaCount = karmaCount;
    }

    public byte getEnhance() {
        return enhance;
    }

    public void setEnhance(final byte en) {
        enhance = en;
    }

    public int getPotential(int num, boolean isBonus) {
        switch (num) {
            case 1:
                if (isBonus) {
                    return bonuspotential1;
                } else {
                    return potential1;
                }
            case 2:
                if (isBonus) {
                    return bonuspotential2;
                } else {
                    return potential2;
                }
            case 3:
                if (isBonus) {
                    return bonuspotential3;
                } else {
                    return potential3;
                }
        }
        return 0;
    }

    public void setPotential(int en, int num, boolean isBonus) {
        switch (num) {
            case 1:
                if (isBonus) {
                    bonuspotential1 = en;
                } else {
                    potential1 = en;
                }
                break;
            case 2:
                if (isBonus) {
                    bonuspotential2 = en;
                } else {
                    potential2 = en;
                }
                break;
            case 3:
                if (isBonus) {
                    bonuspotential3 = en;
                } else {
                    potential3 = en;
                }
                break;
        }
    }

    public int getFusionAnvil() {
        return fusionAnvil;
    }

    public void setFusionAnvil(final int en) {
        fusionAnvil = en;
    }

    public byte getOldState() {
        return oldState;
    }

    public void setOldState(final byte en) {
        oldState = en;
    }

    public byte getState(boolean bonus) {
        if (bonus) {
            return bonusState;
        } else {
            return state;
        }
    }

    public void setState(final byte en, boolean bonus) {
        if (bonus) {
            bonusState = en;
        } else {
            state = en;
        }
    }

    public void updateState(boolean bonus) {
        int ret = 0;
        int v1;
        int v2;
        int v3;
        if (!bonus) {
            v1 = potential1;
            v2 = potential2;
            v3 = potential3;
        } else {
            v1 = bonuspotential1;
            v2 = bonuspotential2;
            v3 = bonuspotential3;
        }
        if (v1 >= 40000 || v2 >= 40000 || v3 >= 40000) {
            ret = 20;//傳說
        } else if (v1 >= 30000 || v2 >= 30000 || v3 >= 30000) {
            ret = 19;//罕見
        } else if (v1 >= 20000 || v2 >= 20000 || v3 >= 20000) {
            ret = 18;//稀有
        } else if (v1 >= 1 || v2 >= 1 || v3 >= 1) {
            ret = 17;//特殊
        } else if (v1 == -20 || v2 == -20 || v3 == -20 || v1 == -4 || v2 == -4 || v3 == -4) {
            ret = 4;//未鑒定傳說
        } else if (v1 == -19 || v2 == -19 || v3 == -19 || v1 == -3 || v2 == -3 || v3 == -3) {
            ret = 3;//未鑒定罕見
        } else if (v1 == -18 || v2 == -18 || v3 == -18 || v1 == -2 || v2 == -2 || v3 == -2) {
            ret = 2;//未鑒定稀有
        } else if (v1 == -17 || v2 == -17 || v3 == -17 || v1 == -1 || v2 == -1 || v3 == -1) {
            ret = 1;//未鑒定特殊
        } else if (v1 < 0 || v2 < 0 || v3 < 0) {
            return;
        }

        setState((byte) ret, bonus);
    }

    public short getSoulName() {
        return soulname;
    }

    public void setSoulName(final short soulname) {
        this.soulname = soulname;
    }

    public short getSoulEnchanter() {
        return soulenchanter;
    }

    public void setSoulEnchanter(final short soulenchanter) {
        this.soulenchanter = soulenchanter;
    }

    public short getSoulPotential() {
        return soulpotential;
    }

    public void setSoulPotential(final short soulpotential) {
        this.soulpotential = soulpotential;
    }

    public int getSoulSkill() {
        return soulskill;
    }

    public void setSoulSkill(final int skillid) {
        this.soulskill = skillid;
    }

    public byte getFire() {
        return fire;
    }

    public void setFire(byte fire) {
        this.fire = fire;
    }

    public byte getStarForce() {
        return starforce;
    }

    public void setStarForce(byte starforce) {
        this.starforce = starforce;
    }

    public void resetPotential_Fuse(boolean half, int potentialState, boolean bonus) { //maker skill - equip first receive
        //no legendary, 0.16% chance unique, 4% chance epic, else rare
        potentialState = -potentialState;
        if (Randomizer.nextInt(100) < 4) {
            potentialState -= Randomizer.nextInt(100) < 4 ? 2 : 1;
        }
        setPotential(potentialState, 1, bonus);
        setPotential((Randomizer.nextInt(half ? 5 : 10) == 0 ? potentialState : 0), 2, bonus); //1/10 chance of 3 line
        setPotential(0, 3, bonus); //just set it theoretically
        updateState(bonus);
    }

    public void resetPotential(boolean bonus) {
        resetPotential(0, bonus);
    }

    public void resetPotential(int state, boolean bonus) {
        resetPotential(state, false, bonus);
    }

    public void resetPotential(boolean fullLine, boolean bonus) {
        resetPotential(0, fullLine, bonus);
    }

    public void resetPotential(int state, boolean fullLine, boolean bonus) {
        final int rank;
        switch (state) {
            case 1:
                rank = -17;
                break;
            case 2:
                rank = -18;
                break;
            case 3:
                rank = -19;
                break;
            case 4:
                rank = -20;
                break;
            default:
                rank = Randomizer.nextInt(100) < 4 ? (Randomizer.nextInt(100) < 4 ? -19 : -18) : -17;
        }
        fullLine = getState(bonus) != 0 && getPotential(3, bonus) != 0 ? true : fullLine;
        setPotential(rank, 1, bonus);
        setPotential(Randomizer.nextInt(10) <= 1 || fullLine ? rank : 0, 2, bonus); //1/10 chance of 3 line
        setPotential(0, 3, bonus); //just set it theoretically
        updateState(bonus);
    }

    public void renewPotential(int rate, int type, int toLock) {
        int miracleRate = 1;
        if (EventConstants.DoubleMiracleTime) {
            miracleRate *= 2;
        }

        boolean bonus = ItemConstants.方塊.CubeType.附加潛能.check(type);

        boolean threeLine = getPotential(3, bonus) > 0;

        int rank = Randomizer.nextInt(100) < rate * miracleRate ? 1 : 0;
        if (ItemConstants.方塊.CubeType.等級下降.check(type)) {
            if (rank == 0) {
                rank = Randomizer.nextInt(100) < (rate + 20) * miracleRate ? -1 : 0;
            }
        }

        if (ItemConstants.方塊.CubeType.前兩條相同.check(type)) {
            type -= Randomizer.nextInt(10) <= 5 ? ItemConstants.方塊.CubeType.前兩條相同.getValue() : 0;
        }

        if (getState(bonus) + rank < 17 || getState(bonus) + rank > (!ItemConstants.方塊.CubeType.傳說.check(type) ? !ItemConstants.方塊.CubeType.罕見.check(type) ? !ItemConstants.方塊.CubeType.稀有.check(type) ? 17 : 18 : 19 : 20)) {
            rank = 0;
        }

        setState((byte) (getState(bonus) + rank - 16), bonus);

        if (toLock != 0 && toLock <= 3) {
            setPotential(-(toLock * 100000 + getPotential(toLock, bonus)), 1, bonus);
        } else {
            setPotential(-getState(bonus), 1, bonus);
        }

        if (ItemConstants.方塊.CubeType.調整潛能條數.check(type)) {
            setPotential(Randomizer.nextInt(10) <= 2 ? -getState(bonus) : 0, 2, bonus);
        } else if (threeLine) {
            setPotential(-getState(bonus), 2, bonus);
        } else {
            setPotential(0, 2, bonus);
        }

        setPotential(-type, 3, bonus);

        if (ItemConstants.方塊.CubeType.洗後無法交易.check(type)) {
            setFlag((short) (getFlag() | ItemFlag.UNTRADABLE.getValue()));
        }

        updateState(bonus);
    }

    public int getIncSkill() {
        return incSkill;
    }

    public void setIncSkill(int inc) {
        incSkill = inc;
    }

    public short getCharmEXP() {
        return charmExp;
    }

    public short getPVPDamage() {
        return pvpDamage;
    }

    public void setCharmEXP(short s) {
        charmExp = s;
    }

    public void setPVPDamage(short p) {
        pvpDamage = p;
    }

    public MapleRing getRing() {
        if (!ItemConstants.類型.特效戒指(getItemId()) || getUniqueId() <= 0) {
            return null;
        }
        if (ring == null) {
            ring = MapleRing.loadFromDb(getUniqueId(), getPosition() < 0);
        }
        return ring;
    }

    public void setRing(MapleRing ring) {
        this.ring = ring;
    }

    public MapleAndroid getAndroid() {
        if (getItemId() / 10000 != 166 || getUniqueId() <= 0) {
            return null;
        }
        if (android == null) {
            android = MapleAndroid.loadFromDb(getItemId(), getUniqueId());
        }
        return android;
    }

    public void setAndroid(MapleAndroid ad) {
        android = ad;
    }

    public short getSocketState() {
        int flag = 0;
        if (socket1 > 0 || socket2 > 0 || socket3 > 0) { // Got empty sockets show msg 
            flag |= SocketFlag.DEFAULT.getValue();
        }
        if (socket1 > 0) {
            flag |= SocketFlag.SOCKET_BOX_1.getValue();
        }
        if (socket1 > 1) {
            flag |= SocketFlag.USED_SOCKET_1.getValue();
        }
        if (socket2 > 0) {
            flag |= SocketFlag.SOCKET_BOX_2.getValue();
        }
        if (socket2 > 1) {
            flag |= SocketFlag.USED_SOCKET_2.getValue();
        }
        if (socket3 > 0) {
            flag |= SocketFlag.SOCKET_BOX_3.getValue();
        }
        if (socket3 > 1) {
            flag |= SocketFlag.USED_SOCKET_3.getValue();
        }
        return (short) flag;
    }

    public int getSocket1() {
        return socket1;
    }

    public void setSocket1(int socket1) {
        this.socket1 = socket1;
    }

    public int getSocket2() {
        return socket2;
    }

    public void setSocket2(int socket2) {
        this.socket2 = socket2;
    }

    public int getSocket3() {
        return socket3;
    }

    public void setSocket3(int socket3) {
        this.socket3 = socket3;
    }

    public List<EquipStat> getStats() {
        return stats;
    }

    public List<EquipSpecialStat> getSpecialStats() {
        return specialStats;
    }

    public static Equip calculateEquipStats(Equip eq) {
        eq.getStats().clear();
        eq.getSpecialStats().clear();
        if (eq.getUpgradeSlots() > 0) {
            eq.getStats().add(EquipStat.SLOTS);
        }
        if (eq.getLevel() > 0) {
            eq.getStats().add(EquipStat.LEVEL);
        }
        if (eq.getStr() > 0) {
            eq.getStats().add(EquipStat.STR);
        }
        if (eq.getDex() > 0) {
            eq.getStats().add(EquipStat.DEX);
        }
        if (eq.getInt() > 0) {
            eq.getStats().add(EquipStat.INT);
        }
        if (eq.getLuk() > 0) {
            eq.getStats().add(EquipStat.LUK);
        }
        if (eq.getHp() > 0) {
            eq.getStats().add(EquipStat.MHP);
        }
        if (eq.getMp() > 0) {
            eq.getStats().add(EquipStat.MMP);
        }
        if (eq.getWatk() > 0) {
            eq.getStats().add(EquipStat.WATK);
        }
        if (eq.getMatk() > 0) {
            eq.getStats().add(EquipStat.MATK);
        }
        if (eq.getWdef() > 0) {
            eq.getStats().add(EquipStat.WDEF);
        }
        if (eq.getMdef() > 0) {
            eq.getStats().add(EquipStat.MDEF);
        }
        if (eq.getAcc() > 0) {
            eq.getStats().add(EquipStat.ACC);
        }
        if (eq.getAvoid() > 0) {
            eq.getStats().add(EquipStat.AVOID);
        }
        if (eq.getHands() > 0) {
            eq.getStats().add(EquipStat.HANDS);
        }
        if (eq.getSpeed() > 0) {
            eq.getStats().add(EquipStat.SPEED);
        }
        if (eq.getJump() > 0) {
            eq.getStats().add(EquipStat.JUMP);
        }
        if (eq.getFlag() > 0) {
            eq.getStats().add(EquipStat.FLAG);
        }
        if (eq.getIncSkill() > 0) {
            eq.getStats().add(EquipStat.INC_SKILL);
        }
        if (eq.getEquipLevel() > 0) {
            eq.getStats().add(EquipStat.ITEM_LEVEL);
        }
        if (eq.getItemEXP() > 0) {
            eq.getStats().add(EquipStat.ITEM_EXP);
        }
        if (eq.getDurability() > -1) {
            eq.getStats().add(EquipStat.DURABILITY);
        }
        if (eq.getViciousHammer() > 0 || eq.getPlatinumHammer() > 0) {
            eq.getStats().add(EquipStat.VICIOUS_HAMMER);
        }
        if (eq.getPVPDamage() > 0) {
            eq.getStats().add(EquipStat.PVP_DAMAGE);
        }
        if (eq.getEnhanctBuff() > 0) {
            eq.getStats().add(EquipStat.ENHANCT_BUFF);
        }
        if (eq.getReqLevel() > 0) {
            eq.getStats().add(EquipStat.REQUIRED_LEVEL);
        }
        if (eq.getYggdrasilWisdom() > 0) {
            eq.getStats().add(EquipStat.YGGDRASIL_WISDOM);
        }
        if (eq.getFinalStrike()) {
            eq.getStats().add(EquipStat.FINAL_STRIKE);
        }
        if (eq.getBossDamage() > 0) {
            eq.getStats().add(EquipStat.BOSS_DAMAGE);
        }
        if (eq.getIgnorePDR() > 0) {
            eq.getStats().add(EquipStat.IGNORE_PDR);
        }
        //SPECIAL STATS:
        if (eq.getTotalDamage() > 0) {
            eq.getSpecialStats().add(EquipSpecialStat.TOTAL_DAMAGE);
        }
        if (eq.getAllStat() > 0) {
            eq.getSpecialStats().add(EquipSpecialStat.ALL_STAT);
        }
        eq.getSpecialStats().add(EquipSpecialStat.KARMA_COUNT); //no count = -1
        if (eq.getFire() > 0) {
            eq.getSpecialStats().add(EquipSpecialStat.FIRE);
        }
        if (eq.getStarForce() > 0) {
            eq.getSpecialStats().add(EquipSpecialStat.STAR_FORCE);
        }
        return (Equip) eq.copy();
    }

    public Item reset(Equip newEquip) {
        //Equip ret = new Equip(getItemId(), getPosition(), getUniqueId(), getFlag());
        this.str = newEquip.str;
        this.dex = newEquip.dex;
        this._int = newEquip._int;
        this.luk = newEquip.luk;
        this.hp = newEquip.hp;
        this.mp = newEquip.mp;
        this.matk = newEquip.matk;
        this.mdef = newEquip.mdef;
        this.watk = newEquip.watk;
        this.wdef = newEquip.wdef;
        this.acc = newEquip.acc;
        this.avoid = newEquip.avoid;
        this.hands = newEquip.hands;
        this.speed = newEquip.speed;
        this.jump = newEquip.jump;
        this.upgradeSlots = newEquip.upgradeSlots;
        this.level = newEquip.level;
        this.itemEXP = newEquip.itemEXP;
        this.durability = newEquip.durability;
        this.vicioushammer = newEquip.vicioushammer;
        this.platinumhammer = newEquip.platinumhammer;
        this.enhance = newEquip.enhance;
        this.charmExp = newEquip.charmExp;
        this.pvpDamage = newEquip.pvpDamage;
        this.incSkill = newEquip.incSkill;

        this.enhanctBuff = newEquip.enhanctBuff;
        this.reqLevel = newEquip.reqLevel;
        this.yggdrasilWisdom = newEquip.yggdrasilWisdom;
        this.finalStrike = newEquip.finalStrike;
        this.bossDamage = newEquip.bossDamage;
        this.ignorePDR = newEquip.ignorePDR;
        this.totalDamage = newEquip.totalDamage;
        this.allStat = newEquip.allStat;
        this.karmaCount = newEquip.karmaCount;
        this.soulname = newEquip.soulname;
        this.soulenchanter = newEquip.soulenchanter;
        this.soulpotential = newEquip.soulpotential;
        this.soulskill = newEquip.soulskill;
        this.starforce = newEquip.starforce;
        this.fire = newEquip.fire;
        this.setGiftFrom(getGiftFrom());
        return this;
    }
}
