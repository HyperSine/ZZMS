/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package extensions.temporary;

/**
 *
 * @author o黯淡o
 */
public enum DirectionType {
    UNK(0),
    EXEC_TIME(1),
    EFFECT(2),
    ACTION(3),
    UNK4(4),
    UNK5(5),
    UNK6(6),
    UNK7(7),
    UNK8(8),
    HIDE_PLAYER(9),
    UNK10(10),    
    TELL_STORY(11),
    UNK12(12),
    UNK13(13),
    UNK14(14),
    UNK15(15),
    UNK16(16),
    UNK17(17),
    ;
    
    private final int value;

    private DirectionType(int value) {
        this.value = value;
    }
    
    public static DirectionType getType(int type) {
        for (DirectionType dt : values())
        {
            if (dt.getValue() == type) {
                return dt;
            }
        }
        return null;
    }
    
    public int getValue() {
        return value;
    }
}
