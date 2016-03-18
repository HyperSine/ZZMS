/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package extensions.temporary;

/**
 *
 * @author Itzik
 */
public enum NPCTalk {

    NEXT_PREV(0),
    UNK_1(1),
    YES_NO(2),
    TEXT(3),
    NUMBER(4),
    SELECTION(5),
    UNK_6(6),
    UNK_7(7),
    UNK_8(8),
    AVATAR(9),
    ANDROID(10),
    UNK_11(11),
    UNK_12(12),
    UNK_13(13),
    UNK_14(14),
    ACCEPT_DECLINE(16),
    UNK_17(17),
    SLIDE_MENU(0x12),
    DIRECTION_SCRIPT_ACTION(0x12),
    DIRECTION_PLAYMOVE(0x13),
    UNK_22(22),
    JOB_SELECTION(0x18),    
    UNK_24(24),
    UNK_25(25),
    UNK_26(26),
    UNK_27(27),
    UNK_28(28),
    UNK_29(29),
    UNK_30(30),
    UNK_31(31),
    UNK_32(32),
    TELL_STORY(0x23);
    
    private final byte type;

    private NPCTalk(int type) {
        this.type = (byte) type;
    }

    public byte getType() {
        return type;
    }
}
