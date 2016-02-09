//副本開關 開啟、true 關閉、false
var open = true;
//副本腳本名
var name = ["FairyBossBattle"];
//等級限制
var minLevel = 100;
var maxLevel = 255;
//次數限制
var maxenter = 10;
//記錄次數名稱
var PQName = "艾畢奈亞";

var status = -1;

var out = false;

function start() {
    if (cm.getPlayer().getMapId() == 300030310) {
        cm.sendYesNo("你現在就想出去嗎？");
        out = true;
        return;
    }
    if (cm.getParty() === null) {
        cm.sendOk("要組1人或以上的隊伍,才能入場。");
        cm.dispose();
        return;
    } else if (!cm.isLeader()) {
        cm.sendOk("只有隊長才可以申請入場。");
        cm.dispose();
        return;
    }
    cm.sendSimple("#e<Boss: 艾畢奈亞>#n\r\n前方是艾畢奈亞的藏身地，準備好迎接BOSS了嗎？\r\n#b\r\n#L0# 申請<Boss: 艾畢奈亞> 的入場.#l");
}

function action(mode, type, selection) {
    if (out) {
        if (mode === 1) {
            cm.warp(300030300, 0);
        }
        cm.dispose();
        return;
    }
    if (mode === 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }

    if (status === 0) {
        cm.sendSimple("#e<Boss: 艾畢奈亞>#n\r\n請選擇想要的模式.\r\n\r\n#L0# 普通模式 ( 等級 " + minLevel + " 以上 )#l\r\n");
    } else if (status === 1) {
        if (selection !== 0) {
            cm.sendOk("出現未知錯誤。");
            cm.dispose();
            return;
        }
        if (!cm.isAllPartyMembersAllowedLevel(minLevel, maxLevel)) {
            cm.sendNext("組隊成員等級 " + minLevel + " 以上 " + maxLevel + " 以下才可以入場。");
        } else if (!cm.isAllPartyMembersAllowedPQ(PQName, maxenter)) {
            cm.sendNext("你的隊員\"" + cm.getNotAllowedPQMember(PQName, maxenter).getName() + "\"次數已經達到上限了。");
        } else {
            var em = cm.getEventManager(name[selection]);
            if (em === null || !open) {
                cm.sendOk("副本暫未開放。");
            } else {
                var prop = em.getProperty("state");
                if (prop === null || prop.equals("0")) {
                    em.startInstance(cm.getParty(), cm.getMap(), 255);
                    cm.gainMembersPQ(PQName, 1);
                } else {
                    cm.sendSimple("已經有隊伍在進行了,請換其他頻道嘗試。");
                }
            }
        }
        cm.dispose();
    } else {
        cm.dispose();
    }
}