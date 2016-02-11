package client;

import handling.Buffstat;
import java.io.Serializable;

public enum MapleBuffStat implements Serializable, Buffstat {

    //==========================Mask[0] - 1 - IDA[0xE]
 
    // 0    
    TEST_BUFF0(0, true),
    //提升最大爆擊                      [完成-182]
    INDIE_CR(1, true),
    // 2
    TEST_BUFF2(2, true),
    //物理攻擊力增加                    [完成-182]
    INDIE_PAD(3, true),//indiePad
    //魔法攻擊力增加                    [完成-182]
    INDIE_MAD(4, true),//indieMad
    //物理防御力增加                    [完成-182]
    INDIE_PDD(5, true),//indiePdd
    //魔法防御力增加                    [完成-182]
    INDIE_MDD(6, true),//indieMdd
    //最大體力                          [完成-182]
    INDIE_MAX_HP(7, true), //indieMaxHp, indieMhp
    //最大體力百分比                    [完成-182]
    INDIE_MHP_R(8, true), //indieMhpR
    //最大魔法                          [完成-182]
    INDIE_MAX_MP(9, true),//indieMaxMp
    //最大魔法百分比                    [完成-182]
    INDIE_MMP_R(10, true), //indieMmpR
    //命中值增加                        [完成-182]
    INDIE_ACC(11, true),//indieAcc
    //提升迴避值                        [完成-182]
    INDIE_EVA(12, true),
    //疾速之輪行蹤
    INDIE_JUMP(13, true),
    //疾速之輪行蹤                      [完成-182]
    INDIE_SPEED(14, true),
    //所有能力提升
    INDIE_ALL_STATE(15, true), //indieAllStat 
    // 16
    TEST_BUFF16(16, true),
    //疾速之輪行蹤 & 重生的輪行蹤        [完成-182]
    INDIE_EXP(17, true), //indieExp
    //攻擊速度提升                      [完成-182]
    INDIE_BOOSTER(18, true), //indieBooster
    // 19
    TEST_BUFF19(19, true),
    // 20
    TEST_BUFF20(20, true),
    // 21
    TEST_BUFF21(21, true),
    // 22
    TEST_BUFF22(22, true),
    // 23 
    STACKING_ATK(23, true),
    // 24
    TEST_BUFF24(24, true),
    //提高STR                          [完成-182] 
    INDIE_STR(25, true),
    //提高DEX                          [完成-182] 
    INDIE_DEX(26, true),
    //提高INT                          [完成-182] 
    INDIE_INT(27, true),
    //提高LUK                          [完成-182]  
    INDIE_LUK(28, true),    
    //提高技能攻擊力                    [完成-182]
    INDIE_DAM_R(29, true),//indieDamR
    // 30
    TEST_BUFF30(30, true),
    // 31
    TEST_BUFF31(31, true),
    
    //==========================Mask[1] - 2 - IDA[0xD]
    
    //傷害最大值
    INDIE_MAX_DAMAGE_OVER(32, true),
    //物理傷害減少百分比(重生的輪行蹤)   [完成-182]
    INDIE_ASR_R(33, true), //indieAsrR
    //魔法傷害減少百分比(重生的輪行蹤)   [完成-182]
    INDIE_TER_R(34, true), //indieTerR
    //爆擊率提升                        [完成-182]
    INDIE_CR_R(35, true),
    //超衝擊防禦_防禦力                 [完成-182]
    INDIE_PDD_R(36, true), 
    //提升最大爆擊                      [完成-182]
    INDIE_MAX_CR(37, true),    
    //提升BOSS攻擊力                    [完成-182]
    INDIE_BOSS(38, true),
    //提升所有屬性                      [完成-182]
    INDIE_ALL_STATE_R(39, true),
    //提升檔格                          [完成-182]
    INDIE_STANCE_R(40, true),
    //提升無視防禦                      [完成-182]
    INDIE_IGNORE_MOB_PDP_R(41, true), //indieIgnoreMobpdpR    
    // 42
    TEST_BUFF42(42, true),
    // 43
    TEST_BUFF43(43, true),
    //提升攻擊％                        [完成-182]
    INDIE_PAD_R(44, true),
    //提升最大爆擊％                    [完成-182]
    INDIE_MAX_CR_R(45, true),
    //提升所有迴避值％                  [完成-182]
    INDIE_EVA_R(46, true),
    //提升魔法防禦力％                  [完成-182]
    INDIE_MDD_R(47, true),
    // 48
    TEST_BUFF48(48, true),
    // 49
    TEST_BUFF49(49, true),
    //物理攻擊力                        [完成-182]
    WATK(50),
    //物理防御力                        [完成-182]
    WDEF(51),
    //魔法攻擊力                        [完成-182]
    MATK(52),
    //魔法防御力                        [完成-182]
    MDEF(53),
    //命中率                            [完成-182]
    ACC(54),
    //迴避率                            [完成-182]
    AVOID(55),
    //手技                              [完成-182]
    HANDS(56),
    //移動速度                          [完成-182]
    SPEED(57),
    //跳躍力                            [完成-182]
    JUMP(58),
    //魔心防禦                          [完成-182]
    MAGIC_GUARD(59),
    //隱藏術                            [完成-182]
    DARKSIGHT(60),
    //攻擊加速                          [完成-182]
    BOOSTER(61),
    //傷害反擊                          [完成-182]
    POWERGUARD(62),
    //神聖之火_最大MP                   [完成-182]
    MAXMP(63),
    
    //==========================Mask[2] - 3 - IDA[0xC]
    
    //神聖之火_最大HP                   [完成-182]
    MAXHP(64),    
    //神聖之光                          [完成-182]
    INVICIBLE(65),
    //無形之箭                          [完成-182]
    SOULARROW(66),
    //昏迷                              [完成-182]
    STUN(67),
    //中毒
    POISON(68),
    //封印
    SEAL(69),
    //黑暗
    DARKNESS(70),
    //鬥氣集中
    COMBO(71),
    //召喚獸
    SUMMON(71),
    //屬性攻擊
    WK_CHARGE(72),
    //龍之力量
    DRAGONBLOOD(72),
    //神聖祈禱(176-OK)
    HOLY_SYMBOL(73),
    //(CMS_聚財術)
    MESOUP(74),
    //影分身                       [完成-182]
    SHADOWPARTNER(75),
    //勇者掠奪術
    PICKPOCKET(76),
    //替身術
    PUPPET(76),
    //楓幣護盾
    MESOGUARD(77),
    //
    HP_LOSS_GUARD(78),
    //虛弱                          [完成-182]
    WEAKEN(79),
    //詛咒
    CURSE(80),
    //緩慢                          [完成-182]
    SLOW(81),
    //變身                          [完成-182]
    MORPH(82),
    //恢復(176-OK)
    RECOVERY(83),
    //楓葉祝福                      [完成-182]
    MAPLE_WARRIOR(84),
    //格擋(穩如泰山)                [完成-182]
    STANCE(85),
    //銳利之眼                      [完成-182]
    SHARP_EYES(86),
    //魔法反擊
    MANA_REFLECTION(87),
    //誘惑                          [完成-182]
    SEDUCE(88),
    //暗器傷人
    SPIRIT_CLAW(89),
    //時空門
    MYSTIC_DOOR(89),
    //魔力無限
    INFINITY(90),
    //進階祝福                      [完成-182]
    HOLY_SHIELD(91),
    //敏捷提升                      [完成-182]
    HAMSTRING(92),
    //命中率增加
    BLIND(93),
    //集中精力
    CONCENTRATE(94),
    //不死化
    ZOMBIFY(95),
    //英雄的回響                    [完成-182]
    ECHO_OF_HERO(96),
    //==========================Mask[3] - 4 - IDA[0xB]
    //楓幣獲得率
    MESO_RATE(97),
    //鬼魂變身                      [完成-182]
    GHOST_MORPH(98),
    //
    ARIANT_COSS_IMU(98), // The white ball around you
    //混亂
    REVERSE_DIRECTION(99),
    //掉寶幾率
    DROP_RATE(100),
    //經驗倍率
    EXPRATE(101),
    //樂豆倍率
    ACASH_RATE(102),
    //
    ILLUSION(103),
    //解多人Buff用的                    [IDA找的-182]
    PLAYERS_BUFF8(104),
    //解多人Buff用的                    [IDA找的-182]
    PLAYERS_BUFF9(105),
    //狂暴戰魂
    BERSERK_FURY(106),
    //金剛霸體                      [完成-182]
    DIVINE_BODY(107),
    //(CMS_闪光击)
    SPARK(108),
    //(CMS_终极弓剑)
    FINALATTACK(109),
    //隱藏刀(176-OK)
    BLADE_CLONE(110),
    //自然力重置                    [完成-182]
    ELEMENT_RESET(111),
    //(CMS_风影漫步)
    WIND_WALK(112),
    //組合無限
    UNLIMITED_COMBO(113),
    //矛之鬥氣
    ARAN_COMBO(114),
    //嗜血連擊                      [完成-182]
    COMBO_DRAIN(115),
    //宙斯之盾
    COMBO_BARRIER(115),
    //強化連擊(CMS_戰神抗壓)
    BODY_PRESSURE(116),
    //釘錘(CMS_戰神威勢)
    SMART_KNOCKBACK(117),
    //(CMS_天使狀態)
    PYRAMID_PQ(118),
    //解多人Buff用的                    [IDA找的-182]
    PLAYERS_BUFF10(119),
    //無法使用藥水
    POTION_CURSE(120),
    //影子
    SHADOW(122),
    //致盲
    BLINDNESS(123),
    //緩慢術
    SLOWNESS(124),
    //守護之力(CMS_魔法屏障)             [IDA找的-182]
    MAGIC_SHIELD(125),
    //魔法抵抗．改
    MAGIC_RESISTANCE(126),
    //靈魂之石
    SOUL_STONE(127),
    
    //==========================Mask[4] - 5 - IDA[0xA]
    
    //飛天騎乘                      [完成-182]
    SOARING(128),    
    //冰凍                          [完成-182]
    FREEZE(129),
    //雷鳴之劍
    LIGHTNING_CHARGE(130),
    //鬥氣爆發                      [完成-182]
    ENRAGE(131),
    //障礙                          [完成-182]
    BACKSTEP(132),
    //無敵(隱‧鎖鏈地獄、自由精神等)[完成-182]
    INVINCIBILITY(133),
    //絕殺刃(176-OK)
    FINAL_CUT(134),
    //咆哮                          [完成-182]
    DAMAGE_BUFF(135),
    //狂獸附體
    ATTACK_BUFF(136), 
    //地雷                          [完成-182]
    RAINING_MINES(137),
    //增強_MaxHP
    ENHANCED_MAXHP(138),
    //增強_MaxMP
    ENHANCED_MAXMP(139),
    //增強_物理攻擊力                   [完成-182]
    ENHANCED_WATK(140),
    //增強_魔法攻擊力                   [完成-182]
    ENHANCED_MATK(141),
    //增強_物理防禦力                   [完成-182]
    ENHANCED_WDEF(142),
    //增強_魔法防禦力                   [完成-182]
    ENHANCED_MDEF(143),
    //全備型盔甲                        [完成-182]
    PERFECT_ARMOR(144),
    //終極賽特拉特_PROC                 [IDA找的-179]
    SATELLITESAFE_PROC(145),
    //終極賽特拉特_吸收
    SATELLITESAFE_ABSORB(146),                   
    //颶風
    TORNADO(147),
    //咆哮_會心一擊機率增加               [完成-182]
    CRITICAL_RATE_BUFF(148),
    //咆哮_MaxMP 增加                    [完成-182]
    MP_BUFF(149),
    //咆哮_傷害減少                      [完成-182]
    DAMAGE_TAKEN_BUFF(150),
    //咆哮_迴避機率                      [完成-182]
    DODGE_CHANGE_BUFF(151),
    //
    CONVERSION(152),
    //甦醒(176-OK)
    REAPER(153),
    //潛入                               [完成-182]
    INFILTRATE(155),
    //合金盔甲                           [完成-182]                    
    MECH_CHANGE(156),    
    
    //==========================Mask[5] - 6 - IDA[0x9]
    
    //暴走形态
    FELINE_BERSERK(157),
    //幸运骰子                          [完成-182]
    DICE_ROLL(158),
    //祝福护甲
    DIVINE_SHIELD(159),
    //攻擊增加百分比                    [完成-182]
    DAMAGE_RATE(160),
    //瞬間移動精通
    TELEPORT_MASTERY(161),
    //戰鬥命令
    COMBAT_ORDERS(162),
    //追隨者                             [完成-182]
    BEHOLDER(163),
    //功能不知道                        [IDA找的-182]
    IDA_UNK_BUFF11(163),
    //裝備潛能無效化
    DISABLE_POTENTIAL(164),    
    //
    GIANT_POTION(165),
    //玛瑙的保佑
    ONYX_SHROUD(166),
    //玛瑙的意志
    ONYX_WILL(167),
    //龍捲風
    TORNADO_CURSE(168),
    //天使祝福
    BLESS(169),    
    //解多人Buff用的                    [IDA找的-182]
    PLAYERS_BUFF4(170),
    //解多人Buff用的                    [IDA找的-182]
    PLAYERS_BUFF6(171),
    //解多人Buff用的                    [IDA找的-182]
    PLAYERS_BUFF7(172),
    //压制术
    THREATEN_PVP(172),
    //解多人Buff用的                    [IDA找的-182]
    PLAYERS_BUFF5(173),
    //冰骑士
    ICE_KNIGHT(173),    
    //解多人Buff用的                    [IDA找的-182]
    PLAYERS_BUFF15(174),    
    //力量(176-OK)
    STR(174),
    //智力(176-OK)
    INT(175),
    //敏捷(176-OK)
    DEX(176),
    //運氣(176-OK)
    LUK(177),
    //未知(未確定)
    ATTACK(178),    
    //未知(未確定)
    STACK_ALLSTATS(179),
    //未知(未確定)
    PVP_DAMAGE(180),
    //IDA移動Buff                       [IDA找的-182]
    IDA_MOVE_BUFF7(181),
    
    // 182
    // 183
    // 184
    
    //解多人Buff用的                    [IDA找的-182]
    PLAYERS_BUFF2(185),
    //解多人Buff用的                    [IDA找的-182]
    PLAYERS_BUFF1(186),
    //解多人Buff用的                    [IDA找的-182]
    PLAYERS_BUFF12(187),
    //未知(未確定)
    PVP_ATTACK(187),
    
    //188
    
    //隱藏潛能(未確定)
    HIDDEN_POTENTIAL(189),
    //未知(未確定)
    ELEMENT_WEAKEN(190),
    //解多人Buff用的                    [IDA找的-182]
    PLAYERS_BUFF3(190),
    //未知(未確定)(90002000)
    SNATCH(191), 
    
    //凍結                              [完成-182]
    FROZEN(191),
    
    // 192
    // 193   
    // 194

    //==========================Mask[6] - 7 - IDA[0x8]
    //未知(未確定)
    PVP_FLAG(195),    
    //無限力量[猜測]
    BOUNDLESS_RAGE(196),
    //聖十字魔法盾
    HOLY_MAGIC_SHELL(196),
    //核爆術
    BIG_BANG(197),
    //神秘狙擊
    MANY_USES(198),
    //大魔法師(已改成被動,無BUFF)
    BUFF_MASTERY(198),
    //異常抗性                      [完成-182]
    ABNORMAL_STATUS_R(199),
    //屬性抗性                      [完成-182]
    ELEMENTAL_STATUS_R(200),
    //水之盾                        [完成-182]
    WATER_SHIELD(201),
    //變形                          [完成-182]
    DARK_METAMORPHOSIS(202),
    //随机橡木桶
    BARREL_ROLL(203),
    //精神连接(IDA-Check-OK) [跟靈魂灌注是同個東西]
    SPIRIT_LINK(204),
    //靈魂灌注_攻擊增加             [完成-182]
    DAMAGE_R(204),
    //神圣拯救者的祝福
    VIRTUE_EFFECT(205),    
    //光明綠化                     [完成-182]           
    COSMIC_GREEN(206),    
    //靈魂灌注_爆擊率增加           [完成-182]
    CRITICAL_RATE(207),
    
    //未知(未確定)
    NO_SLIP(214),
    //未知(未確定)
    FAMILIAR_SHADOW(215),
    //吸血鬼之觸                    [完成-182]
    ABSORB_DAMAGE_HP(215),
    //提高防禦力[猜測]
    DEFENCE_BOOST_R(216),

    // 217
    // 218
    // 219
    // 220
    // 221
    // 222
    // 223
    // 224
    // 225

    //==========================Mask[7] - 8 - IDA[0x7]
    
    //IDA特殊Buff                  [IDA找的-182]
    IDA_SPECIAL_BUFF_1(226),
    
    // 227
    // 228
    
    //角設預設Buff                  [完成-182]
    CHAR_BUFF(229),
    //角設預設Buff                  [完成-182]
    MOUNT_MORPH(230),
    
    // 231
    
    //使用技能中移動                [完成-182]
    USING_SKILL_MOVE(232),
    //無視防禦力                    [完成-182]
    IGNORE_DEF(233),
    //幸運幻影                      [完成-182]
    FINAL_FEINT(234),
    //幻影斗蓬                      [完成-182]
    PHANTOM_MOVE(235),
    //最小爆擊傷害                  [完成-182]
    MIN_CRITICAL_DAMAGE(236),
    //爆擊機率增加                  [完成-182]
    CRITICAL_RATE2(237),
    //審判                          [完成-182]
    JUDGMENT_DRAW(238),
    //增加_物理攻擊                 [完成-182]
    DAMAGE_UP(239),
    
    // 240
    
    //IDA移動Buff                  [IDA找的-182]
    IDA_MOVE_BUFF2(241),
    //IDA移動Buff                  [IDA找的-182]
    IDA_MOVE_BUFF3(242),
    //IDA移動Buff                  [IDA找的-182]
    IDA_MOVE_BUFF4(243),
    //解多人Buff用的                [IDA找的-182]
    PLAYERS_BUFF14(244),    
    //黑暗之眼                     [完成-182]
    PRESSURE_VOID(245),    
    //光蝕 & 暗蝕                  [完成-182]
    LUMINOUS_GAUGE(246),
    //黑暗強化 & 全面防禦          [完成-182]
    DARK_CRESCENDO(247),
    //黑暗祝福                     [完成-182]
    BLACK_BLESSING(248),
    //抵禦致命異常狀態              [完成-182]
    //(如 元素適應(火、毒), 元素適應(雷、冰), 聖靈守護)
    ABNORMAL_BUFF_RESISTANCES(249),
    //血之限界                     [完成-182]
    LUNAR_TIDE(250),
    
    // 251
    
    //凱撒變型值                   [完成-182]
    KAISER_COMBO(252),
    
    
    //==========================Mask[8] - 9 - IDA[0x6]
    
    //堅韌護甲                     [完成-182]
    GRAND_ARMOR(253),
    //凱撒模式切換                 [完成-182]
    KAISER_MODE_CHANGE(254),
    
    // 255
    
    //IDA移動Buff                  [IDA找的-182]
    IDA_MOVE_BUFF5(256),
    //意志之劍                     [完成-182]
    TEMPEST_BLADES(257),
    //會心一擊傷害                  [完成-182]
    CRIT_DAMAGE(258),
    
    // 259
    
    //靈魂傳動                      [完成-182]
    DAMAGE_ABSORBED(260),
    
    // 261
    
    //功能不知道                    [IDA找的-182]
    IDA_UNK_BUFF3(262),
    
    // 263
    // 264

    //功能不知道                    [IDA找的-182]
    IDA_UNK_BUFF5(265),

    //IDA特殊Buff                   [IDA找的-182]
    IDA_SPECIAL_BUFF_3(263),
    //繼承人(176-OK)
    SOUL_BUSTER(269),
    //未知(未確定)
    PRETTY_EXALTATION(269),
    //未知(未確定)
    KAISER_MAJESTY3(270),
    //未知(未確定)
    KAISER_MAJESTY4(271), 
    //IDA移動Buff                   [IDA找的-182]
    IDA_MOVE_BUFF6(266),
    //靈魂深造
    RECHARGE(272),
    //0x10000
    //未知(未確定)
    
    //隱‧鎖鏈地獄
    STATUS_RESIST_TWO(274),
    
    //未知(未確定)
    PARTY_STANCE(275),
    
    //終極審判[猜測]
    FINAL_JUDGMENT_DRAW(274),
    
    //冰雪結界                      [完成-182]
    ABSOLUTE_ZERO_AURA(274),
    //火靈結界[猜測]
    INFERNO_AURA(275),
    
    //復仇天使
    AVENGING_ANGEL(280, true),
    //天堂之門
    HEAVEN_IS_DOOR(281),
    //戰鬥準備
    BOWMASTERHYPER(282),
    
    
    //修羅
    ASURA_IS_ANGER(286),
    //暴能續發
    STIMULATING_CONVERSATION(287),
    //IDA特殊Buff                    [IDA找的-182]
    IDA_SPECIAL_BUFF_2(288),
    
    //==========================Mask[9] - 10 - IDA[0x5]
    
    //功能不知道                     [IDA找的-182]
    IDA_UNK_BUFF10(289),
    //BOSS傷害
    BOSS_DAMAGE(290),
    //功能不知道                     [IDA找的-182]
    IDA_UNK_BUFF8(290),
    //全域代碼                       [完成-182]
    OOPARTS_CODE(291),
    
    //292
    
    //超越_攻擊                      [完成-182]
    EXCEED_ATTACK(293),
    //急速療癒                       [完成-182]
    DIABOLIC_RECOVERY(294),
    
    // 295
    // 296
    
    //超越                          [完成-182]
    EXCEED(297),
    //沉月-攻擊數量                  [完成-182]
    ATTACK_COUNT(298),
    
    // 299
    // 300

    //傑諾能量                      [完成-182]
    SUPPLY_SURPLUS(301),
    //IDA特殊Buff                   [IDA找的-182]
    IDA_SPECIAL_BUFF_5(302),
    
    //303
    
    //傑諾飛行                      [完成-182]
    XENON_FLY(304),
    //阿瑪蘭斯發電機                [完成-182]
    AMARANTH_GENERATOR(305),
    //解多人Buff用的                [IDA找的-182]
    PLAYERS_BUFF13(306),
    //元素： 風暴                   [完成-182]
    STORM_ELEMENTAL(307),
    //開天闢地[猜測]
    PRIMAL_BOLT(308),
    
    // 309
    // 310
    
    //風暴使者                     [完成-182]
    STORM_BRINGER(311),
    //光之劍-命中提升               [完成-182]
    ACCURACY_PERCENT(312),
    //迴避提升                     [完成-182]
    AVOID_PERCENT(313),
    //阿爾法                       [完成-182]
    ALBATROSS(314),
    //
    SPEED_LEVEL(315),
    //雙重力量 : 沉月/旭日          [完成-182]
    SOLUNA_EFFECT(316),    
    //光之劍                        [完成-182]
    ADD_AVOIDABILITY(317),
    //元素： 靈魂                   [完成-182]
    SOUL_ELEMENT(318),
    //雙重力量 : 沉月/旭日           [完成-182]
    EQUINOX_STANCE(319),
    
    // 320
    // 321
    
    //==========================Mask[10] - 11 - IDA[0x4]
    
    //靈魂球BUFF                    [完成-182]
    SOUL_WEAPON(322),
    //靈魂BUFF                      [完成-182]
    SOUL_WEAPON_HEAD(323),
    //復原
    HP_RECOVER(324),
    //功能不知道                    [完成-182]
    IDA_UNK_BUFF4(325),
    
    // 326
    
    //十字深鎖鏈                    [完成-182]
    CROSS_SURGE(327),
    //超衝擊防禦_防禦概率
    PARASHOCK_GUARD(328),
    //功能不知道                   [IDA找的-182]
    IDA_UNK_BUFF12(329),
    //寒冰迅移[猜測]               [IDA找的-182]                 
    CHILLING_STEP(330),
    
    // 331
    
    //祝福福音
    PASSIVE_BLESS(332),
    
    // 333
    // 334
    
    //功能不知道                    [IDA找的-182]
    IDA_UNK_BUFF13(335),
    //IDA特殊Buff                   [IDA找的-182]
    QUIVER_KARTRIGE(336),
    //IDA特殊Buff                   [IDA找的-182]
    IDA_SPECIAL_BUFF_6(337),
    
    // 338
    
    //IDA移動Buff                   [IDA找的-182]
    IDA_MOVE_BUFF8(339),
    //IDA特殊Buff                   [IDA找的-182]
    IDA_SPECIAL_BUFF_7(340),
    //功能不知道                    [IDA找的-182]
    IDA_UNK_BUFF14(341),
    //時之威能                      [完成-182]
    DIVINE_FORCE_AURA(342),
    //聖靈神速                      [完成-182]
    DIVINE_SPEED_AURA(343),
    
    // 344
    // 345
    // 346
    // 347
    // 348
    // 349
    // 大砲(95001002)               [完成-182]
    CANNON(350),
    
    // 351
    // 352
    // 353

    //==========================Mask[11] - 12 - IDA[0x3]
    
    //無視怪物傷害(重生的輪行蹤)        [完成-182]
    IGNORE_MOB_DAM_R(354), //ignoreMobDamR
    //靈魂結界                         [完成-182]
    SPIRIT_WARD(355),
    //死裡逃生                         [完成-182]
    CLOSE_CALL(356),    
    //IDA特殊Buff                     [IDA找的-182]
    IDA_SPECIAL_BUFF_9(357),
    
    // 358
    // 359
    // 360
    // 361
    // 362
    
    //IDA移動Buff                     [IDA找的-182]
    IDA_MOVE_BUFF9(363),
    //元素 : 闇黑                     [完成-182]
    DARK_ELEMENTAL(364),
    
    // 365
    
    //燃燒                            [完成-182]
    CONTROLLED_BURN(366),
    
    // 367
    // 368
    // 369
    // 370
    // 371
    火焰屏障(370),
    暗影僕從(371),
    // 372
    // 373
    
    //功能不知道                       [IDA找的-182]
    IDA_UNK_BUFF16(374),
    //危機繩索
    AURA_BOOST(375),
    //拔刀術                           [完成-182]
    HAYATO(376),
    //拔刀術-新技體                    [完成-182]
    BATTOUJUTSU_SOUL(377),
    //制敵之先                         [完成-182]
    COUNTERATTACK(378),
    //柳身                             [完成-182]
    WILLOW_DODGE(379),        
    //紫扇仰波                         [完成-182]
    SHIKIGAMI(380),
    //武神招來                         [完成-182]
    MILITARY_MIGHT(381),
    //武神招來                         [完成-182]
    MILITARY_MIGHT1(382),
    //武神招來                         [完成-182]
    MILITARY_MIGHT2(383),
    //拔刀術                           [完成-182]
    BATTOUJUTSU_STANCE(384),
    
    // 385
    
    //==========================Mask[12] - 13 - IDA[0x2]

    // 386
    
    //迅速                             [完成-182]
    JINSOKU(387),
    //一閃                             [完成-182]
    HITOKIRI_STRIKE(388), 
    //花炎結界                         [完成-182]
    FOX_FIRE(389),
    //影朋‧花狐                        [完成-182]
    HAKU_REBORN(390),
    //花狐的祝福
    HAKU_BLESS(391),
    
    // 392
    
    //解多人Buff用的                    [IDA找的-182]
    PLAYERS_BUFF11(393),
    
    // 394
    // 395
    // 396
    // 397
    
    //精靈召喚模式                      [完成-182]
    ANIMAL_SELECT(398),
    
    // 399
    // 400
    // 401
    // 402
    
    //IDA特殊Buff                       [IDA找的-182]
    IDA_SPECIAL_BUFF_4(402),
    
    // 404
    
    //依古尼斯咆哮-迴避提升               [完成-182]
    PROP(404),
    
    // 406
    
    //召喚美洲豹                         [完成-182]
    SUMMON_JAGUAR(406),
    //自由精神                           [完成-182]
    SPIRIT_OF_FREEDOM(407),    
    //功能不知道                         [IDA找的-182]
    IDA_UNK_BUFF1(408),
    
    // 410
    
    //功能不知道                        [IDA找的-182]
    IDA_UNK_BUFF2(410),    
    //光環效果                          [完成-182]
    NEW_AURA(411),
    //黑暗閃電                          [完成-182]
    DARK_SHOCK(412),
    //戰鬥精通                          [完成-182]
    BATTLE_MASTER(413),
    //死神契約                          [完成-182]
    GRIM_CONTRACT(414), 
    
    // 416
    // 417
    
    //==========================Mask[13] - 14 - IDA[0x0]
    
    // 418
    // 419
    
    //神盾系統                          [完成-182]
    AEGIS_SYSTEM(419),    
    //索魂精通                          [完成-182]   
    SOUL_SEEKER(420),
    //小狐仙                            [完成-182]
    FOX_SPIRITS(421),
    //暗影蝙蝠                          [完成-182]
    SHADOW_BAT(422),
    
    // 424
    
    //燎原之火                          [完成-182]
    IGNITE(424),
    
    //能量獲得                          [完成-182]
    ENERGY_CHARGE(425, true),
    //預設Buff-3                        [完成-182]
    DEFAULTBUFF3(427, true),
    //預設Buff-4                        [完成-182]
    //皮卡啾攻擊                        [完成-182]
    PINK_BEAN_ATTACK(430),
    //皮卡啾未知                        [完成-182]
    PINK_BEAN_UNK(431),
    
    DEFAULTBUFF4(432, true),
    
    PINK_BEAN_YOYO(433),
    //預設Buff-5                        [完成-182]
    DEFAULTBUFF5(442, true),
    //衝鋒_速度                         [完成-182]
    DASH_SPEED(443, true),    
    //衝鋒_跳躍                         [完成-182]
    DASH_JUMP(444, true),
    //怪物騎乘                          [完成-182]
    MONSTER_RIDING(445, true),//+16
    //最終極速                          [完成-182]
    SPEED_INFUSION(447, true),//+16
    //指定攻擊(無盡追擊)                 [完成-182]
    HOMING_BEACON(448, true),
    //預設Buff-1                        [完成-182]
    DEFAULTBUFF1(449, true),
    //預設Buff-2                        [完成-182]
    DEFAULTBUFF2(450, true),
    
    
    //-----------------[已停用的Buff]
    //黑色繩索                      
    DARK_AURA_OLD(888),
    //藍色繩索                      
    BLUE_AURA_OLD(888),
    //黃色繩索                      
    YELLOW_AURA_OLD(888),
    //貓頭鷹召喚
    OWL_SPIRIT(888),
    //超級體
    BODY_BOOST(888),
    
    ;
    private static final long serialVersionUID = 0L;
    private final int buffstat;
    private final int first;
    private boolean stacked = false;

    private MapleBuffStat(int buffstat) {
        this.buffstat = 1 << (31 - (buffstat % 32));
        this.first = (int) Math.floor(buffstat / 32);
    }

    private MapleBuffStat(int buffstat, boolean stacked) {
        this.buffstat = 1 << (31 - (buffstat % 32));
        this.first = (int) Math.floor(buffstat / 32);
        this.stacked = stacked;
    }

    @Override
    public int getPosition() {
        return first;
    }

    @Override
    public int getValue() {
        return buffstat;
    }

    public final boolean canStack() {
        return stacked;
    }
}
