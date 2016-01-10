/*
* 一、從封包轉成MonsterStatus的值：(範例都是以新版為主)
*     X = 從封包知道他是第幾個MASK(舊的是倒著算，新的是正著算，因為新的把反向封包去掉了)
*     範例：(舊的0x800，MASK=2；新的MASK=0)
*          MASK[0] [00 08 00 00]
*          MASK[1] [00 04 00 00]
*          MASK[2] [00 02 00 00]
*     Y = 從封包找到你要的值例如：0x800[00 08 00 00]，用小算盤先到16進制輸入800，然後按Rsh再按1，再按等於，然後一直按到變一為止，按等於的次數就是我們要的值
*     (其實也可以轉成十進制去看他是2的幾次方，次方數就是Y)或 Y = (Math.log(0x800) / Math.log(2))
*     MonsterStatus的值 = (31 - Y) + (X * 32)
*
* 二、從MonsterStatus的值轉成封包：(範例都是以新版為主)
*     用小算盤：拿37來當範例，輸入37 Mod 32，就會顯示 5，然後 31 - 5 顯示 26 ，再按1 Lsh 26就是 67108864(0x4000000)，再來37 / 32，就會顯示 1
*     轉成封包就是：
*     MASK[0] [00 00 00 00]
*     MASK[1] [00 00 00 04]
*     MASK[2] [00 00 00 00]
*     
*     也可以用公式： 1 << (31 - (X % 32))
*     
*※有寫完成的就代表已經確認過的
 */
package client;

import handling.Buffstat;
import java.io.Serializable;

/**
 *
 * @author o黯淡o
 */

public enum MonsterStatus implements Serializable, Buffstat {

    //  =========== MASK[0]
    //物攻
    WATK(0),
    //物防
    WDEF(1),
    //魔攻
    MATK(2),
    //魔防
    MDEF(3),
    //命中
    ACC(4),
    //迴避
    AVOID(5),
    //速度[完成]
    SPEED(6),
    //暈眩
    STUN(7),
    //結冰、麻痺
    FREEZE(8),
    //中毒[完成]
    POISON(9),
    //封印、沉默
    SEAL(10),
    //黑暗
    DARKNESS(11),    
    //物攻提升
    WEAPON_ATTACK_UP(12),
    //魔攻提升
    MAGIC_ATTACK_UP(13),
    //物防提升
    WEAPON_DEFENSE_UP(14),    
    //魔防提升
    MAGIC_DEFENSE_UP(15),
    //死亡
    DOOM(16),
    //影網
    SHADOW_WEB(17),
    //攻擊免疫
    WEAPON_IMMUNITY(18),
    //魔法免疫
    MAGIC_IMMUNITY(19),
    //挑釁
    SHOWDOWN(20),    
    //免疫傷害
    DAMAGE_IMMUNITY(21),
    //忍者伏擊
    NINJA_AMBUSH(22),
    //
    DAMAGED_ELEM_ATTR(23),
    //武器涂毒
    VENOMOUS_WEAPON(24),
    //致盲
    BLIND(25),
    //技能封印
    SEAL_SKILL(26),
    //燒毀
    BURNED(27),
    //心靈控制
    HYPNOTIZE(28),
    //反射物攻
    WEAPON_DAMAGE_REFLECT(29),
    //反射魔攻
    MAGIC_DAMAGE_REFLECT(30),
    //關閉
    DISABLE(31),
    
    // =========== MASK[1]
    //痛苦提升
    RISE_TOSS(32),
    //抵銷
    NEUTRALISE(33, true),
    //弱點
    IMPRINT(34, true),
    //怪物炸彈
    MONSTER_BOMB(35),
    //魔法無效
    MAGIC_CRASH(36),
    //恢復攻擊
    HEAL_DAMAGE(37),
    MBS38(38),
    MBS39(39),
    MBS40(40),
    MBS41(41),
    MBS42(42),
    MBS43(43),
    MBS44(44),
    //另一個咬擊[178-完成]
    ANOTHER_BITE(45),
    MBS46(46),
    
    //三角進攻
    TRIANGULATION(47),
    //減益爆炸
    STING_EXPLOSION(48),
    
    MBS49(49),
    MBS50(50),
    MBS51(51),
    MBS52(52),
    MBS53(53),
    MBS54(54),
    MBS55(55),
    MBS56(56),
    MBS57(57),
    MBS58(58, true),
    MBS59(59),
    MBS60(60, true),
    MBS61(61),
    
    //持續扣血 - 破滅之輪[178-完成]
    BLEED(62, true),    
    MBS63(63, true),
    
    // =========== MASK[2]
    
    MBS64(64, true),
    MBS65(65, true),
    MBS66(66, true),    
    MBS67(67, true),
    MBS68(68, true),
    //召喚怪物
    SUMMON(69, true),
    MBS70(70, true),
    MBS71(71, true),
    MBS72(72, true),
    MBS73(73, true),
    MBS74(74, true), 
    ;
    static final long serialVersionUID = 0L;
    private final int i;
    private final int position;
    private final boolean end;
    private final int bitNumber;

    private MonsterStatus(int i) {
        this.i = 1 << (31 - (i % 32)); // 如果要變舊的，就把減31去掉，詳細請參考頂端說明
        this.position = (int) Math.floor(i / 32);
        this.end = false;
        this.bitNumber = i;
    }

    private MonsterStatus(int i, boolean end) {
        this.i = 1 << (31 - (i % 32)); // 如果要變舊的，就把減31去掉，詳細請參考頂端說明
        this.position = (int) Math.floor(i / 32);
        this.end = end;
        this.bitNumber = i;
    }

    @Override
    public int getPosition() {
        return position;
    }

    public boolean isEmpty() {
        return end;
    }
    
    public int getBitNumber() {
        return bitNumber;
    }

    @Override
    public int getValue() {
        return i;
    }

    public static final MapleDisease getLinkedDisease(final MonsterStatus skill) {
        switch (skill) {
            case STUN:
            case SHADOW_WEB:
                return MapleDisease.STUN;
            case POISON:
            case VENOMOUS_WEAPON:
                return MapleDisease.POISON;
            case SEAL:
            case MAGIC_CRASH:
                return MapleDisease.SEAL;
            case FREEZE:
                return MapleDisease.FREEZE;
            case DARKNESS:
                return MapleDisease.DARKNESS;
            case SPEED:
                return MapleDisease.SLOW;
        }
        return null;
    }
}
