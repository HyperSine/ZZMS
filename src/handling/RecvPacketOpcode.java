package handling;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import tools.EncodingDetect;

public enum RecvPacketOpcode implements WritableIntValueHolder {
    
    // 未知[完成-182] [01 00 00 00 00 00 00 00 00]
    STRANGE_DATA(false, (short) 0x66),
    // 客戶端驗證[完成-182]
    CLIENT_HELLO(false, (short) 0x67),
    
    // 0x68
    
    // 密碼驗證[完成-182]
    LOGIN_PASSWORD(false, (short) 0x69),
    // 角色選單[完成-182]
    CHARLIST_REQUEST(false, (short) 0x6A),
    
    // 0x6B
    // 0x6C
    
    // 玩家登入[完成-182]
    PLAYER_LOGGEDIN(false, (short) 0x6D),
    // 選擇角色[完成-182]
    CHAR_SELECT(true, (short) 0x6E),
    
    // 0x6F
    // 0x70
    
    // 伺服器選單回覆[完成-182]
    SERVERLIST_REQUEST(false, (short) 0x71),    
    // 自動登入轉向[完成-182]
    LOGIN_REDIRECTOR(false, (short) 0x72),
    // 檢查角色名稱[完成-182]
    CHECK_CHAR_NAME(true, (short) 0x73), 
    
    // 0x74
    // 0x75
    // 0x76
    // 0x77
    // 0x78
    // 0x79
    // 0x7A
    // 0x7B
    
    // 建立角色[完成-182]
    CREATE_CHAR(false, (short) 0x7C),
    // 50等角色卡角色建立[完成-182]
    CREATE_LV50_CHAR(true, (short) 0x7D),
    // 建立終極冒險家[完成]
    CREATE_ULTIMATE(false, (short) 0x7E),
    // 刪除角色[完成-182]
    DELETE_CHAR(true, (short) 0x7F),
    
    // 0x80
    // 0x81
    // 0x82
    // 0x83
    // 0x84
    
    // 客戶端錯誤信息回覆[完成-182]
    CLIENT_FEEDBACK(false, (short) 0x85),

    // 0x86
    // 0x87
    // 0x88
    // 0x89
    // 0x8A
    // 0x8B
    // 0x8C
    // 0x8D
    // 0x8E
    
    // 打工系统[完成-182]
    PART_TIME_JOB(true, (short) 0x8F),
    // 角色卡[完成-182]
    CHARACTER_CARD(true, (short) 0x90),
    // 未知[推測-182]
    ENABLE_LV50_CHAR(true, (short) 0x91),
    
    // 0x92
    // 0x93
    
    // Pong[完成-182]
    PONG(false, (short) 0x94),
    
    // 0x95
    
    // 客戶端錯誤[完成-182] 【[ Name: %s, Job: %d, Field: %d, World: %d, Channel: %d ]\r\n】
    CLIENT_ERROR(false, (short) 0x96),
    
    // 0x97
    // 0x98
    // 0x99
    // 0x9A
    // 0x9B
    // 0x9C
    
    // 選擇性別[完成-182]
    SET_GENDER(false, (short) 0x9D),
    
    // 0x9E 【回到登入介面】[String(帳號)]
    
    // 伺服器狀態[推測-182]
    SERVERSTATUS_REQUEST(false, (short) 0x9F),
    // 背景驗證[完成-182]
    GET_SERVER(false, (short) 0xA0),
    
    // 0xA1
    // 0xA2
    // 0xA3
    // 0xA4
    
    // 客戶端開始(顯示視窗)[完成-182]
    CLIENT_START(false, (short) 0xA5), 
    
    ////////////////////////////////////////////////
    
    // 變更地圖[完成-182]
    CHANGE_MAP(true, (short) 0xA8),
    // 變更頻道[完成-182]
    CHANGE_CHANNEL(true, (short) 0xA9),
    
    // 0xAA
    // 0xAB
    // 0xAC
    
    // 購物商城[完成-182]
    ENTER_CASH_SHOP(true, (short) 0xAD),
    // PvP開始[完成]
    ENTER_PVP(true, (short) 0xAE),
    
    // 0xAF
    
    // 阿斯旺開始[完成]
    ENTER_AZWAN(true, (short) 0xB0),
    // 阿斯旺活動
    ENTER_AZWAN_EVENT(true, (short) 0xB1),
    // 離開阿斯旺
    LEAVE_AZWAN(true, (short) 0xB2),
    // PvP隊伍
    ENTER_PVP_PARTY(true, (short) 0xB3),
    // 離開PvP[完成]
    LEAVE_PVP(true, (short) 0xB4),
    
    // 0xB5
    // 0xB6
    
    // 玩家移動[完成-182]
    MOVE_PLAYER(true, (short) 0xB7),    
    // 取消椅子[完成-182]
    CANCEL_CHAIR(true, (short) 0xB8),
    // 使用椅子[完成-182]
    USE_CHAIR(true, (short) 0xB9),
    
    // 0xBA
    
    // 近距離攻擊[完成-182]
    CLOSE_RANGE_ATTACK(true, (short) 0xBB),
    // 遠距離攻擊[完成-182]
    RANGED_ATTACK(true, (short) 0xBC),
    // 魔法攻擊[完成-182]
    MAGIC_ATTACK(true, (short) 0xBD),
    // 被動攻擊(抗壓...)[推測]
    PASSIVE_ATTACK(true, (short) 0xBE),
    
    // 0xBF
    // 0xC0
    
    // 角色受傷[完成-179]
    TAKE_DAMAGE(true, (short) 0xC1),
    // PvP攻擊[推測]
    PVP_ATTACK(true, (short) 0xC2),
    // 普通聊天[完成-182]
    GENERAL_CHAT(true, (short) 0xC3),
    // 關閉黑板[完成-179]
    CLOSE_CHALKBOARD(true, (short) 0xC4),
    // 臉部情緒[完成-179]
    FACE_EXPRESSION(true, (short) 0xC5),
    // 機器人臉部情緒[推測]
    FACE_ANDROID(true, (short) 0xC6),
    // 使用物品效果[完成-179]
    USE_ITEMEFFECT(true, (short) 0xC7),
    // 使用原地復活[推測]
    WHEEL_OF_FORTUNE(true, (short) 0xC8),
    // 使用稱號效果[完成]
    USE_TITLE(true, (short) 0xC9),
    
    // 0xCA
    
    // 變更天使破壞者外觀
    ANGELIC_CHANGE(true, (short) 0xCB),
    
    // 0xCC
    // 0xCD
    // 0xCE
    // 0xCF
    // 0xD0
    // 0xD1
    // 0xD2
    
    // Npc交談[完成-182]
    NPC_TALK(true, (short) 0xD3),
    
    // 0xD4
    
    // Npc詳細交談[完成-182]
    NPC_TALK_MORE(true, (short) 0xD5),
    // Npc商店[完成-182]
    NPC_SHOP(true, (short) 0xD6),
    // 倉庫[完成]
    STORAGE(true, (short) 0xD7),
    // 精靈商人[完成]
    USE_HIRED_MERCHANT(true, (short) 0xD8),
    // 精靈商人物品[完成]
    MERCH_ITEM_STORE(true, (short) 0xD9),
    // 宅配操作[完成]
    PACKAGE_OPERATION(true, (short) 0xDA),
    // 取消開放通道
    MECH_CANCEL(true, (short) 0xDB),
    
    // 0xDC
    // 0xDD
    // 0xDE
    // 0xDF
    // 0xE0
    
    // 智慧貓頭鷹(5230000)[完成]
    OWL(true, (short) 0xE1),
    // 智慧貓頭鷹購買
    OWL_WARP(true, (short) 0xE2),
    
    // 0xE3
    
    // 管理員商店[完成-179]
    ADMIN_SHOP(false, (short) 0xE4),
    // 向上整理[完成-179]
    ITEM_SORT(true, (short) 0xE5),
    // 種類排序[完成-179]
    ITEM_GATHER(true, (short) 0xE6),
    // 物品移動[完成-179]
    ITEM_MOVE(true, (short) 0xE7),
    
    // 0xE8 【輸入觀戰板內容】
    
    // 移動道具至背包欄位[完成]
    MOVE_BAG(true, (short) 0xE9),
    // 背包道具至道具欄位
    SWITCH_BAG(true, (short) 0xEA),
    
    // 0xEB
    
    // 使用物品[完成-179]
    USE_ITEM(true, (short) 0xEC),
    // 取消物品效果[完成]
    CANCEL_ITEM_EFFECT(true, (short) 0xED),
    
    // 0xEE
    
    // 使用召喚包(2100017)[完成-179]
    USE_SUMMON_BAG(true, (short) 0xEF),
    // 寵物飼料[完成]
    PET_FOOD(true, (short) 0xF0),
    // 提神飲料[推測]
    USE_MOUNT_FOOD(true, (short) 0xF1),
    // 使用腳本物品[完成-179]
    USE_SCRIPTED_NPC_ITEM(true, (short) 0xF2),
    // 使用製作書
    USE_RECIPE(true, (short) 0xF3),
    
    // 0xF4
    // 0xF5
    // 0xF6
    
    // 使用商城道具[完成]
    USE_CASH_ITEM(true, (short) 0xF7),
    // 使用捕捉道具
    USE_CATCH_ITEM(true, (short) 0xF8),
    // 是否允許寵物拾取道具[完成]
    ALLOW_PET_LOOT(true, (short) 0xF9),
    // 是否允許寵物自動餵食[完成]
    ALLOW_PET_AOTO_EAT(true, (short) 0xFA),
    // 使用附加潛能印章
    USE_ADDITIONAL_ITEM(true, (short) 0xFB),
    
    // 0xFC
    // 0xFD
    
    // 使用技能書[完成]
    USE_SKILL_BOOK(true, (short) 0xFE),
    
    // 0xFF
    // 0x100
    // 0x101
    
    // 經驗瓶(2230000)[完成]
    USE_EXP_POTION(true, (short) 0x102),
    
    // 0x103
    // 0x104
    // 0x105
    // 0x106
    // 0x107
    
    // 智慧貓頭鷹開始搜索
    USE_OWL_MINERVA(true, (short) 0x108),
    // 使用瞬移之石
    USE_TELE_ROCK(true, (short) 0x109),
    // 使用回家卷軸[完成-179]
    USE_RETURN_SCROLL(true, (short) 0x10A),    
    // 移動至梅斯特鎮[完成-179]
    MOVE_ARDENTMILL(true, (short) 0x10B),    
    // 使用卷軸[完成]
    USE_UPGRADE_SCROLL(true, (short) 0x10C),
    // 使用卷軸保護卡(5064300)[完成]
    USE_FLAG_SCROLL(true, (short) 0x10D),
    // 使用裝備強化卷軸[完成]
    USE_EQUIP_SCROLL(true, (short) 0x10E),
    
    // 0x10F
    // 0x110
    // 0x111
    
    // 使用潛能賦予卷軸[完成]
    USE_POTENTIAL_SCROLL(true, (short) 0x112),
    // 使用附加潛在能力賦予卷軸[完成]
    USE_BONUS_POTENTIAL_SCROLL(true, (short) 0x113),
    // 使用烙的印章(2049500)[完成]
    USE_CARVED_SEAL(true, (short) 0x114),
    // 使用奇怪的方塊(2710000)[完成]
    USE_CRAFTED_CUBE(true, (short) 0x115),
    
    // 0x116
    
    // 靈魂卷軸[2590002][完成]
    USE_SOUL_ENCHANTER(true, (short) 0x117),
    // 靈魂寶珠[2591001][完成]
    USE_SOUL_SCROLL(true, (short) 0x118),
    // 咒語的痕跡[4001832][完成]
    EQUIPMENT_ENCHANT(true, (short) 0x119),
    
    // 0x11A
    
    // 使用背包[4330009][完成]
    USE_BAG(true, (short) 0x11B),
    // 使用放大鏡[2460001][完成]
    USE_MAGNIFY_GLASS(true, (short) 0x11C),
    
    // 0x11D
    // 0x11E
    // 0x11F
    // 0x120
    
    // 使用能力點數[完成-179]
    DISTRIBUTE_AP(true, (short) 0x121),
    // 自動分配能力點數[完成-179]
    AUTO_ASSIGN_AP(true, (short) 0x122),
    
    // 0x123
    
    // 自動恢復HP/MP[完成-179]
    HEAL_OVER_TIME(true, (short) 0x124),
    
    // 0x125 [Int][Long][Short][Short]
    // 0x126
    
    // 使用技能點數[完成-179]
    DISTRIBUTE_SP(true, (short) 0x127),
    // 角色使用技能[完成-179]
    SPECIAL_SKILL(true, (short) 0x128),
    // 取消輔助效果[完成-179]
    CANCEL_BUFF(true, (short) 0x129),
    // 技能效果[完成-179]
    SKILL_EFFECT(true, (short) 0x12A),
    // 楓幣掉落[完成-179]
    MESO_DROP(true, (short) 0x12B),
    // 添加名聲[推測]
    GIVE_FAME(true, (short) 0x12C),
    
    // 0x12D
    
    // 角色信息[完成-179]
    CHAR_INFO_REQUEST(true, (short) 0x12E),
    // 召喚寵物[完成]
    SPAWN_PET(true, (short) 0x12F),
    
    // 0x130
    
    // 取消異常效果
    CANCEL_DEBUFF(true, (short) 0x131),
    // 腳本地圖[完成-182]
    CHANGE_MAP_SPECIAL(true, (short) 0x132),
    // 使用時空門
    USE_INNER_PORTAL(true, (short) 0x133),
    
    // 0x134
    
    // 使用瞬移之石[完成]
    TROCK_ADD_MAP(true, (short) 0x135),
    // 使用測謊機
    LIE_DETECTOR(true, (short) 0x136),
    // 測謊機技能
    LIE_DETECTOR_SKILL(true, (short) 0x137),
    // 確認測謊機驗證碼 
    LIE_DETECTOR_RESPONSE(true, (short) 0x138),
    // 重新整理測謊機驗證碼
    LIE_DETECTOR_REFRESH(true, (short) 0x139),
    // 舉報玩家
    REPORT(true, (short) 0x13A),
    // 任務操作[完成-182]
    QUEST_ACTION(true, (short) 0x13B),
    // 重新領取勳章
    REISSUE_MEDAL(true, (short) 0x13C),
    // 輔助效果回應
    BUFF_RESPONSE(true, (short) 0x13D),
    
    // 0x13E
    // 0x13F
    // 0x140
    // 0x141
    // 0x142
    
    // 技能組合[完成-179]
    SKILL_MACRO(true, (short) 0x143),
    
    // 0x144
    
    // 獎勵道具[完成]
    REWARD_ITEM(true, (short) 0x145),
    
    // 0x146
    // 0x147
    // 0x148
    
    // 鍛造技能
    ITEM_MAKER(true, (short) 0x149),
    // 全部修理(勇士之村(辛德))
    REPAIR_ALL(true, (short) 0x14A),
    // 裝備修理
    REPAIR(true, (short) 0x14B),
    
    // 0x14C
    // 0x14D
    
    // 請求跟隨()
    FOLLOW_REQUEST(true, (short) 0x14E),
    
    // 0x14F
    
    // 組隊任務獎勵[完成]
    PQ_REWARD(true, (short) 0x150),
    // 請求跟隨回覆
    FOLLOW_REPLY(true, (short) 0x151),
    // 自動跟隨回覆()
    AUTO_FOLLOW_REPLY(true, (short) 0x152),
    // 能力值信息[完成-182]
    PROFESSION_INFO(true, (short) 0x153),
    // 使用培養皿[完成]
    USE_POT(true, (short) 0x154),
    // 清理培養皿[完成]
    CLEAR_POT(true, (short) 0x155),
    // 餵食培養皿
    FEED_POT(true, (short) 0x156),
    // 治癒培養皿
    CURE_POT(true, (short) 0x157),
    // 培養皿獎勵
    REWARD_POT(true, (short) 0x158),
    // 阿斯旺復活
    AZWAN_REVIVE(true, (short) 0x159),
    
    // 0x15A
    
    // 使用髮型卷[2540000][完成]
    USE_COSMETIC(true, (short) 0x15B),
    
    // DF連擊[完成-181] [意志之劍取消]
    DF_COMBO(true, (short) 0x10B),
    
    // 神之子狀態轉換
    ZERO_STAT_CHANGE(true, (short) 0x7FFE),
    // 神之子
    ZERO_CLOTHES(true, (short) 0x7FFE),
    // 使用能力傳播者
    INNER_CIRCULATOR(true, (short) 0x7FFE),
    // PvP重生
    PVP_RESPAWN(true, (short) 0x7FFE),
    
    // 0x11C
    
    // 0x120
    
    // 管理員聊天[完成]
    ADMIN_CHAT(true, (short) 0x171),
    // 隊伍聊天
    PARTYCHAT(true, (short) 0x172),
    // 悄悄話[完成]
    COMMAND(true, (short) 0x173),
    // 聊天招待[完成]
    MESSENGER(true, (short) 0x174),
    // 玩家互動[完成]
    PLAYER_INTERACTION(true, (short) 0x175),
    // 隊伍操作[完成]
    PARTY_OPERATION(true, (short) 0x176),
    // 接受/拒絕組隊邀請[完成]
    DENY_PARTY_REQUEST(true, (short) 0x177),
    // 允許組隊邀請
    ALLOW_PARTY_INVITE(true, (short) 0x178),
    // 建立遠征隊
    EXPEDITION_OPERATION(true, (short) 0x179),
    // 遠征隊搜尋
    EXPEDITION_LISTING(true, (short) 0x17A),
    // 公會操作[完成]
    GUILD_OPERATION(true, (short) 0x17B),
    // 拒絕公會邀請
    DENY_GUILD_REQUEST(true, (short) 0x17C),
    // 申請加入公會
    JOIN_GUILD_REQUEST(true, (short) 0x17D),
    // 取消加入公會
    JOIN_GUILD_CANCEL(true, (short) 0x17E),
    // 允許加入公會邀請
    ALLOW_GUILD_JOIN(true, (short) 0x17F),
    // 拒絕加入公會邀請
    DENY_GUILD_JOIN(true, (short) 0x180),
    
    // 0x181
    // 0x182
    
    // 管理員指令[完成]
    ADMIN_COMMAND(true, (short) 0x183),
    // 管理員指令
    ADMIN_COMMAND2(true, (short) 0x184),
    // 管理員日誌[完成]
    ADMIN_LOG(true, (short) 0x185),
    // 好友操作[完成]
    BUDDYLIST_MODIFY(true, (short) 0x186), 
    
    // 0x137
    // 0x138
    // 0x139
    
    // 訊息操作[完成]
    NOTE_ACTION(true, (short) 0x7FFE),
    
    // 0x13B
    
    // 使用時空門[完成]
    USE_DOOR(true, (short) 0x7FFE),
    // 使用開放通道[完成]
    USE_MECH_DOOR(true, (short) 0x7FFE),
    
    // 0x13E
    
    // 變更鍵盤設置[完成-182]
    CHANGE_KEYMAP(true, (short) 0x190),
    // 猜拳遊戲[完成]
    RPS_GAME(true, (short) 0x191),
    
    // 0x192
    // 0x193
    // 0x194
    
    // 戒指操作[完成]
    RING_ACTION(true, (short) 0x195),
    // 結婚操作[完成]
    WEDDING_ACTION(true, (short) 0x196),
    // 公會聯盟操作
    ALLIANCE_OPERATION(true, (short) 0x197),
    // 拒絕公會聯盟邀請
    DENY_ALLIANCE_REQUEST(true, (short) 0x198),
    // 移動至家族成員身邊
    CYGNUS_SUMMON(true, (short) 0x199),
    // 狂郎勇士連擊
    ARAN_COMBO(true, (short) 0x19A),
    // 怪物CRC Key改變回傳[完成]
    MONSTER_CRC_KEY(true, (short) 0x19B),
    // 製作道具完成
    CRAFT_DONE(true, (short) 0x19C),
    // 製作道具效果
    CRAFT_EFFECT(true, (short) 0x19D),
    // 製作道具開始
    CRAFT_MAKE(true, (short) 0x19E),
    // 變更房間[完成-179]
    CHANGE_ROOM_CHANNEL(true, (short) 0x1B3),
    // 選擇技能
    CHOOSE_SKILL(true, (short) 0x1B5),
    // 技能竊取
    SKILL_SWIPE(true, (short) 0x1B6),
    // 檢視技能
    VIEW_SKILLS(true, (short) 0x1B7),
    // 撤銷偷竊技能
    CANCEL_OUT_SWIPE(true, (short) 0x1B8),
    
    // 釋放意志之劍[完成-181]
    RELEASE_TEMPEST_BLADES(true, (short) 0x1BF),
    
    // 更新超級技能(Done)
    UPDATE_HYPER(true, (short) 0x1C6),
    // 重置超級技能(Done)
    RESET_HYPER(true, (short) 0x1C7),
    
    // 返回選角界面[完成-182]
    BACK_TO_CHARLIST(true, (short) 0x1D8),
    
    // 創建角色跟刪除角色輸入的驗證碼[完成-182]
    SECURITY_CODE(true, (short) 0x1E7),
    
    // 0x1E8
    // 0x1E9
    // 0x1EA
    // 0x1EB
    // 0x1EC
    // 0x1ED
    // 0x1EE
    // 0x1EF
    // 0x1F0
    // 0x1F1
    // 0x1F2
    // 0x1F3
    
    // 幸運怪物(完成)
    LUCKY_LUCKY_MONSTORY(true, (short) 0x1F4),
    
    // 0x1F5
    // 0x1F6
    // 0x1F7
    // 0x1F8
    // 0x1F9
    
    // 快速移動(非打開NPC)
    QUICK_MOVE_SPECIAL(true, (short) 0x1FA),
    // 神之子鏡子世界地圖傳送[完成-180]
    ZERO_QUICK_MOVE(true, (short) 0x1FB),
    
    // 0x1FC
    // 0x1FD
    // 0x1FE
    // 0x1FF
    // 0x200
    // 0x201
    // 0x202
    // 0x203
    // 0x204
    
    // 活動卡片[完成]
    EVENT_CARD(true, (short) 0x205), //0x19C+6(or 5 ?) 178 未知
    // 凱撒快速鍵(176-Done)
    KAISER_QUICK_KEY(true, (short) 0x206),
    
    // 0x207
    // 0x208
    // 0x209
    
    // 賓果[完成-179]
    BINGO(true, (short) 0x20A),
    
    // 0x20B
    // 0x20C
    // 0x20D
    // 0x20E
    // 0x20F
    // 0x210
    // 0x211
    // 0x212
    // 0x213
    // 0x214
    // 0x215
    // 0x216
    
    // 燃燒計畫[完成-182]
    COMBUSTION_PROJECT(true, (short) 0x217),    
    // 變更角色順序[完成-182]
    CHANGE_CHAR_POSITION(true, (short) 0x218),
    // 創角進入遊戲[完成-182]
    CREACTE_CHAR_SELECT(true, (short) 0x219),
    
    // 0x21A
    
    // 寵物移動[完成]
    MOVE_PET(true, (short) 0x21B),
    // 寵物說話[完成]
    PET_CHAT(true, (short) 0x21C),
    // 寵物指令[完成]
    PET_COMMAND(true, (short) 0x21D),
    // 寵物拾取[完成]
    PET_LOOT(true, (short) 0x21E),
    // 宠物自动吃药
    PET_AUTO_POT(true, (short) 0x21F),
    // 寵物過濾
    PET_IGNORE(true, (short) 0x220),
    // 花狐移動[177-完成]
    MOVE_HAKU(true, (short) 0x225),
    // 花狐動作(包括變身)[177-完成]
    HAKU_ACTION(true, (short) 0x226),

    //
    //
    //

    // 影朋花狐使用輔助技能
    HAKU_USE_BUFF(true, (short) 0x229),
    
    //
    //

    //召唤兽移动[177-完成]
    MOVE_SUMMON(true, (short) 0x22C),
    //召唤兽攻击(176.Done)
    SUMMON_ATTACK(true, (short) 0x22D),
    //召唤兽伤害(176.Done)
    DAMAGE_SUMMON(true, (short) 0x22E),
    //召唤兽技能(176.Done)
    SUB_SUMMON(true, (short) 0x22F),
    //移除召唤兽(176.Done)
    REMOVE_SUMMON(true, (short) 0x230),

    //
    //
    //
    //
    //

    //神龍移動[177-完成]
    MOVE_DRAGON(true, (short) 0x236),
    // 使用物品任務
    USE_ITEM_QUEST(true, (short) 0x239),
    // 機器人移動[完成]
    MOVE_ANDROID(true, (short) 0x23A), 
    //安卓情感回傳(176.Done)
    ANDROID_EMOTION_RESULT(true, (short) 0x23B),
    //更新任務
    UPDATE_QUEST(true, (short) 0x23C),
    QUEST_ITEM(true, (short) 0x23D),
    //快速欄按鍵(176.Done)
    QUICK_SLOT(true, (short) 0x241),
    //按下按鈕
    BUTTON_PRESSED(true, (short) 0x242),
    
    //

    // 操控角色完成反饋[完成]
    DIRECTION_COMPLETE(true, (short) 0x244),
    
    //
    //
    
    //進程列表[完成-182]
    SYSTEM_PROCESS_LIST(true, (short) 0x247),
    
    //神之子-開始強化[完成-180]
    ZERO_SCROLL_START(true, (short) 0x249),
    //神之子-武器潛在能力[完成-180]
    ZERO_WEAPON_ABILITY(true, (short) 0x24A),
    //神之子-武器介面[完成-180]
    ZERO_WEAPON_UI(true, (short) 0x24B),
    //神之子-與精靈對話[完成-180]
    ZERO_NPC_TALK(true, (short) 0x24C),
    //神之子-使用卷軸[完成-180]
    ZERO_WEAPON_SCROLL(true, (short) 0x24D),
    //神之子-武器成長[完成-180]
    ZERO_WEAPON_UPGRADE(true, (short) 0x24E),
    //神之子-武器成長[完成-180]
    ZERO_WEAPON_UPGRADE_START(true, (short) 0x24F),

    //加載角色成功
    LOAD_PLAYER_SCCUCESS(true, (short) 0x253),
    // 箭座控制[完成]
    ARROW_BLASTER_ACTION(true, (short) 0x256),
    
    // 遊戲嚮導[完成-179]
    GUIDE_TRANSFER(true, (short) 0x26C),
    
    // 0x216
    // 0x217
    // 0x218
    // 0x219
    
    // 新星世界[完成-179]
    SHINING_STAR_WORLD(true, (short) 0x272),
    // Boss清單[完成-179]
    BOSS_LIST(true, (short) 0x273),
    
    // 0x276 【新星世界試穿衣服】
    // 0x277 【新星世界復原衣服】

    // 公會佈告欄操作
    BBS_OPERATION(true, (short) 0x295),
    // 離開遊戲[完成] 
    EXIT_GAME(true, (short) 0x29A),
    // 潘姆音樂[完成]
    PAM_SONG(true, (short) 0x29C),
    // 聖誕團隊藥水[2212000][完成]
    TRANSFORM_PLAYER(true, (short) 0x2B5),
    // 0x2B6 [Long]
    // 進擊的巨人視窗選項反饋
    ATTACK_ON_TITAN_SELECT(true, (short) 0x2B7),
    // 拍賣系統[完成-179]
    ENTER_MTS(true, (short) 0x2B9),
    // 使用兵法書(2370000)[完成]
    SOLOMON(true, (short) 0x2BA),
    // 獲得兵法書經驗值[完成]
    GACH_EXP(true, (short) 0x2BB),
    // 使用強化任意門[完成-179]
    CHRONOSPHERE(true, (short) 0x2C9),
    // 0x272
    // 0x273
    // 0x274
    // 使用閃耀方塊(5062017)[完成]
    USE_FLASH_CUBE(true, (short) 0x2CD),
    // 0x276
    // 0x277
    // 0x278
    // 0x279
    // 0x27A
    // 0x27B
    // 0x27C
    // 0x27D
    // 怪物移動[完成-182]
    MOVE_LIFE(true, (short) 0x2EA),
    // 怪物復仇
    AUTO_AGGRO(true, (short) 0x2EB),
    // 怪物自爆
    MONSTER_BOMB(true, (short) 0x2EC),

    // Npc動作(包括說話)[完成-182]
    NPC_ACTION(true, (short) 0x303),
    
    // 304
    // 305
    // 306
    // 307
    // 308
    // 309
    
    // 拾取物品[完成]
    ITEM_PICKUP(true, (short) 0x30A),
    
    // 30B
    // 30C
    
    // 攻擊箱子[完成-182]
    DAMAGE_REACTOR(true, (short) 0x30D),
    // 雙擊箱子
    TOUCH_REACTOR(true, (short) 0x30E),
    // 召喚分解機[完成]
    MAKE_EXTRACTOR(true, (short) 0x312),
    // 玩家數據更新?
    UPDATE_ENV(true, (short) 0x313),
    // 滾雪球
    SNOWBALL(true, (short) 0x316),
    // 向左擊飛[完成]
    LEFT_KNOCK_BACK(true, (short) 0x317),
    //组队成员搜索
    MEMBER_SEARCH(true, (short) 0x32B),
    //队伍搜索
    PARTY_SEARCH(true, (short) 0x32C),
    // 開始採集[完成-179]
    START_HARVEST(true, (short) 0x332),
    // 停止採集[完成-179]
    STOP_HARVEST(true, (short) 0x333),
    // 快速移動(開啟Npc)[完成-179]
    QUICK_MOVE(true, (short) 0x336),
    //採集符文輪[完成-179]
    TOUCH_RUNE(true, (short) 0x337),
    //取得符文[完成-179]
    USE_RUNE(true, (short) 0x338),

    // 0x2CB 【楓葉戰士選擇模式】

    // 玩家更新
    PLAYER_UPDATE(true, (short) 0x3B3),
    // 購物商城更新[完成]
    CS_UPDATE(true, (short) 0x3B4),
    // 購買點數道具[完成-182]
    BUY_CS_ITEM(true, (short) 0x3B5),
    // 使用兌換券[完成]
    COUPON_CODE(true, (short) 0x3B6),
    
    // 0x3B7
    
    // 購物商城送禮[完成]
    CS_GIFT(true, (short) 0x3B8),
    
    // 0x3B9
    
    // 儲存造型設計[完成]
    CASH_CATEGORY(true, (short) 0x3BA),
    
    // 0x34F
    // 0x350
    // 0x351
    // 0x352
    // 0x353
    // 0x354
    // 0x355
    // 0x356
    
    // 創建角色二次密碼認證[完成-182]
    CREATE_CHAR_2PW(true, (short) 0x3C3),
    // 使用黃金鐵鎚[完成-179]
    GOLDEN_HAMMER(true, (short) 0x3D2),
    // 黃金鐵鎚使用完成
    VICIOUS_HAMMER(true, (short) 0x3D3),
    
    // 使用白金鎚子[完成-181]
    PLATINUM_HAMMER(true, (short) 0x3D5),
    
    // 獲得獎勵[完成-181]
    REWARD(true, (short) 0x3AE),
    
    // 裝備特效開關[完成-182]
    EFFECT_SWITCH(true, (short) 0x420),
    
    // 使用世界樹的祝福(2048500)
    USE_ABYSS_SCROLL(true, (short) 0x7FFE),
    MONSTER_BOOK_DROPS(true, (short) 0x7FFE),
    
    // General
    RSA_KEY(false, (short) 0x7FFE),
    MAPLETV((short) 0x7FFE),
    CRASH_INFO(false, (short) 0x7FFE),
    // Login
    GUEST_LOGIN(true, (short) 0x7FFE),
    TOS(true, (short) 0x7FFE),
    VIEW_SERVERLIST(false, (short) 0x7FFE),
    REDISPLAY_SERVERLIST(true, (short) 0x7FFE),
    CHAR_SELECT_NO_PIC(false, (short) 0x7FFE),
    AUTH_REQUEST(false, (short) 0x7FFE),
    VIEW_REGISTER_PIC(true, (short) 0x7FFE),
    VIEW_SELECT_PIC(true, (short) 0x7FFE),
    CLIENT_FAILED(false, (short) 0x7FFE),
    ENABLE_SPECIAL_CREATION(true, (short) 0x7FFE),
    CREATE_SPECIAL_CHAR(true, (short) 0x7FFE),
    AUTH_SECOND_PASSWORD(true, (short) 0x7FFE),
    WRONG_PASSWORD(false, (short) 0x7FFE),
    ENTER_FARM(true, (short) 0x7FFE),
    CHANGE_CODEX_SET(true, (short) 0x7FFE),
    CODEX_UNK(true, (short) 0x7FFE),

    USE_NEBULITE(true, (short) 0x7FFE),
    USE_ALIEN_SOCKET(true, (short) 0x7FFE),
    USE_ALIEN_SOCKET_RESPONSE(true, (short) 0x7FFE),
    USE_NEBULITE_FUSION(true, (short) 0x7FFE),

    TOT_GUIDE(true, (short) 0x7FFE),

    GET_BOOK_INFO(true, (short) 0x7FFE),
    USE_FAMILIAR(true, (short) 0x7FFE),
    SPAWN_FAMILIAR(true, (short) 0x7FFE),
    RENAME_FAMILIAR(true, (short) 0x7FFE),
    PET_BUFF(true, (short) 0x7FFE),
    USE_TREASURE_CHEST(true, (short) 0x7FFE),
    REQUEST_FAMILY(true, (short) 0x7FFE),
    OPEN_FAMILY(true, (short) 0x7FFE),
    FAMILY_OPERATION(true, (short) 0x7FFE),
    DELETE_JUNIOR(true, (short) 0x7FFE),
    DELETE_SENIOR(true, (short) 0x7FFE),
    ACCEPT_FAMILY(true, (short) 0x7FFE),
    USE_FAMILY(true, (short) 0x7FFE),
    FAMILY_PRECEPT(true, (short) 0x7FFE),
    FAMILY_SUMMON(true, (short) 0x7FFE),
    SOLOMON_EXP(true, (short) 0x7FFE),
    NEW_YEAR_CARD(true, (short) 0x7FFE),
    XMAS_SURPRISE(true, (short) 0x7FFE),
    TWIN_DRAGON_EGG(true, (short) 0x7FFE),
    YOUR_INFORMATION(true, (short) 0x7FFE),
    FIND_FRIEND(true, (short) 0x7FFE),
    PINKBEAN_CHOCO_OPEN(true, (short) 0x7FFE),
    PINKBEAN_CHOCO_SUMMON(true, (short) 0x7FFE),
    BUY_SILENT_CRUSADE(true, (short) 0x7FFE),
    CASSANDRAS_COLLECTION(true, (short) 0x7FFE),
    BUDDY_ADD(true, (short) 0x7FFE),
    //HAKU_1D8(true, (short) 0x1D8),
    //HAKU_1D9(true, (short) 0x1D9),
    PVP_SUMMON(true, (short) 0x7FFE),

    MOVE_FAMILIAR(true, (short) 0x7FFE),
    TOUCH_FAMILIAR(true, (short) 0x7FFE),
    ATTACK_FAMILIAR(true, (short) 0x7FFE),
    REVEAL_FAMILIAR(true, (short) 0x7FFE),

    FRIENDLY_DAMAGE(true, (short) 0x7FFE),
    HYPNOTIZE_DMG(true, (short) 0x7FFE),

    MOB_BOMB(true, (short) 0x7FFE),
    MOB_NODE(true, (short) 0x7FFE),
    DISPLAY_NODE(true, (short) 0x7FFE),
    MONSTER_CARNIVAL(true, (short) 0x7FFE),

    CLICK_REACTOR(true, (short) 0x7FFE),
    CANDY_RANKING(true, (short) 0x7FFE),
    COCONUT(true, (short) 0x7FFE),
    SHIP_OBJECT(true, (short) 0x7FFE),
    PLACE_FARM_OBJECT(false, (short) 0x7FFE),
    FARM_SHOP_BUY(false, (short) 0x7FFE),
    FARM_COMPLETE_QUEST(false, (short) 0x7FFE),
    FARM_NAME(false, (short) 0x7FFE),
    HARVEST_FARM_BUILDING(false, (short) 0x7FFE),
    USE_FARM_ITEM(false, (short) 0x7FFE),
    RENAME_MONSTER(false, (short) 0x7FFE),
    NURTURE_MONSTER(false, (short) 0x7FFE),
    EXIT_FARM(false, (short) 0x7FFE),
    FARM_QUEST_CHECK(false, (short) 0x7FFE),
    FARM_FIRST_ENTRY(false, (short) 0x7FFE),
    PYRAMID_BUY_ITEM(true, (short) 0x7FFE),
    CLASS_COMPETITION(true, (short) 0x7FFE),
    MAGIC_WHEEL(true, (short) 0x7FFE),
    BLACK_FRIDAY(true, (short) 0x7FFE),
    RECEIVE_GIFT_EFFECT(true, (short) 0x7FFE),
    UPDATE_RED_LEAF(true, (short) 0x7FFE),
    //Not Placed:
    SPECIAL_STAT(false, (short) 0x7FFE),

    DRESSUP_TIME(true, (short) 0x7FFE),
    OS_INFORMATION(true, (short) 0x7FFE),
    LUCKY_LOGOUT(true, (short) 0x7FFE),
    MESSENGER_RANKING(true, (short) 0x7FFE),
    UNKNOWN((short) 0x7FFE);
    private short code;

    @Override
    public void setValue(short code) {
        this.code = code;
    }

    @Override
    public final short getValue() {
        return code;
    }
    private final boolean CheckState;

    private RecvPacketOpcode(short code) {
        this.CheckState = true;
        this.code = code;
    }

    private RecvPacketOpcode(final boolean CheckState, short code) {
        this.CheckState = CheckState;
        this.code = code;
    }

    public final boolean NeedsChecking() {
        return CheckState;
    }

    public static String nameOf(short value) {
        for (RecvPacketOpcode header : RecvPacketOpcode.values()) {
            if (header.getValue() == value) {
                return header.name();
            }
        }
        return "UNKNOWN";
    }

    public static boolean isSpamHeader(RecvPacketOpcode header) {
        switch (header.name()) {
            case "PONG":
            case "NPC_ACTION":
//            case "ENTER"":
//            case "CRASH_INFO":
//            case "AUTH_REQUEST":
//            case "SPECIAL_MOVE":
            case "MOVE_LIFE":
            case "MOVE_PLAYER":
            case "MOVE_ANDROID":
//            case "MOVE_DRAGON":
//            case "MOVE_SUMMON":
//            case "MOVE_FAMILIAR":
//            case "MOVE_PET":
//            case "CLOSE_RANGE_ATTACK":
//            case "QUEST_ACTION":
            case "HEAL_OVER_TIME":
//            case "CHANGE_KEYMAP":
//            case "USE_INNER_PORTAL":
//            case "MOVE_HAKU":
//            case "FRIENDLY_DAMAGE":
//            case "CLOSE_RANGE_ATTACK":
//            case "RANGED_ATTACK":
//            case "ARAN_COMBO":
//            case "SPECIAL_STAT":
//            case "UPDATE_HYPER":
//            case "RESET_HYPER":
//            case "ANGELIC_CHANGE":
//            case "DRESSUP_TIME":
            case "BUTTON_PRESSED":
            case "STRANGE_DATA":
            case "SYSTEM_PROCESS_LIST":
                return true;
            default:
                return false;
        }
    }

    public static void loadValues() {
        Properties props = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("recvops.properties")) {
            props.load(new BufferedReader(new InputStreamReader(fileInputStream, EncodingDetect.getJavaEncode("recvops.properties"))));
        } catch (IOException ex) {
            InputStream in = RecvPacketOpcode.class.getClassLoader().getResourceAsStream("recvops.properties");
            if (in == null) {
                System.out.println("未加載 recvops.properties 檔案, 使用內置RecvPacketOpcode Enum");
                return;
            }
            try {
                props.load(in);
            } catch (IOException e) {
                throw new RuntimeException("加載 recvops.properties 檔案出現錯誤", e);
            }
        }
        ExternalCodeTableGetter.populateValues(props, values());
    }

    static {
        loadValues();
    }
}
