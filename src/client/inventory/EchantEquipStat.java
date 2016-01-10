/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.inventory;

/**
 *
 * @author Pungin
 */
public enum EchantEquipStat {

    WATK(0x01),
    MATK(0x02),
    STR(0x04),
    DEX(0x08),
    INT(0x10),
    LUK(0x20),
    WDEF(0x40),
    MDEF(0x80),
    MHP(0x100),
    MMP(0x200),
    ACC(0x400),
    AVOID(0x800),
    JUMP(0x1000),
    SPEED(0x2000);
    private final int value;

    private EchantEquipStat(int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public final boolean check(int flag) {
        return (flag & value) != 0;
    }
}
