package tools.packet.provider;

/**
 *
 * @author 黯淡
 */
public enum SpecialEffectType {

    // 等級提升[182-完成]
    LEVEL_UP(0x0),
    // 近端技能特效[182-完成]
    LOCAL_SKILL(0x1),
    // 遠端技能特效[182-完成]
    REMOTE_SKILL(0x2),
    // 神之子特效[182-完成]
    ZERO(0x3),
    // 機甲戰神-輔助機器特效[182-完成]
    MECHANIC(0x4),
    SHOW_DICE(0x5),
    UNK_6(0x6),
    // 物品獲得/丟棄文字特效[182-完成]
    ITEM_OPERATION(0x7),
    // 寵物等級提升[182-完成]
    PET_LEVEL_UP(0x8),
    // 技能飛行體特效[182-完成]
    SKILL_FLYING_OBJECT(0x9),
    // 抵抗異常狀態[182-完成]
    RESIST(0xA),
    // 使用護身符[182-完成]
    USE_AMULET(0xB),
    UNK_C(0xC),
    MULUNG_DOJO_UP(0xD),
    // 職業變更[182-完成]
    CHANGE_JOB(0xE),
    // 任務完成[182-完成]
    QUET_COMPLETE(0xF),
    // 回復特效(Byte)[182-完成]
    HEALED_BYTE(0x10),
    UNK_11(0x11),
    UNK_12(0x12),
    UNK_13(0x13),
    REWARD_ITEM(0x14),
    ITEM_LEVEL_UP(0x15),
    ITEM_MAKER_SUCCESS(0x16),
    DODGE_CHANCE(0x17),
    UNK_18(0x18),
    // 顯示WZ的效果[182-完成]
    WZ(0x19),
    // 聊天窗顯示"消耗1個原地復活術 ，於角色所在原地進行復活！（尚餘Byte個）"[182-完成]
    RESURRECTION_INFO(0x1A),
    UNK_1B(0x1B),
    // 顯示WZ的效果2[182-完成]
    WZ2(0x1C),
    UNK_1D(0x1D),
    UNK_1E(0x1E),
    SOUND(0x1F),
    UNK_20(0x20),
    UNK_21(0x21),
    // 商城道具效果[182-完成]
    CASH_ITEM(0x22),
    UNK_23(0x23),
    // 回復特效(Int)[182-完成]
    HEALED_INT(0x24),
    CHAMPION(0x25),
    UNK_26(0x26),
    UNK_27(0x27),
    UNK_28(0x28),
    UNK_29(0x29),
    UNK_2A(0x2A),
    UNK_2B(0x2B),
    UNK_2C(0x2D),
    // 採集/挖礦[182-完成]
    CRAFTING(0x2D),
    UNK_2E(0x2E),
    UNK_2F(0x2F),
    UNK_30(0x30),
    UNK_31(0x31),
    UNK_32(0x32),
    BLACK_BACKGROUND(0x33),
    UNSEAL_BOX(0x34),
    UNK_35(0x35),
    // 影武者出生劇情背景黑暗特效[182-完成]
    DARK(0x36),
    UNK_37(0x37),
    // 天使技能充能效果[182-完成]
    ANGELIC_RECHARGE(0x38),
    UNK_39(0x39),
    UNK_3A(0x3A),
    UNK_3B(0x3B),
    UNK_3C(0x3C),
    UNK_3D(0x3D),
    WZ3(0x3E),
    UNK_3F(0x3F),
    UNK_40(0x40),
    UNK_41(0x41),
    UNK_42(0x42),
    //NPC說話特效
    NPC_BUBBLE(0x43),
    UNK_44(0x44),
    // 暗夜行者技能特效[182-完成]
    FIRE(0x45),
    UNK_46(0x46),
    UNK_47(0x47),
    UNK_48(0x48),
    UNK_49(0x49),
    // 獲得道具頂部提示
    ITEM_TOP_MSG(0x4A),
    UNK_4B(0x4B),
    ;

    private final int value;

    private SpecialEffectType(int value) {
        this.value = value;
    }
    
    public int getValue() {
        return value;
    }
}
