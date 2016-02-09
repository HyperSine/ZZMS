//副本開關 開啟、true 關閉、false
var open = true;
//副本腳本名
var name = ["ChowBossBattle"];
//等級限制
var minLevel = [70];
var maxLevel = [255];
//次數限制
var maxenter = 100;
//記錄次數名稱
var PQName = "ChowBoss";

var status = -1;

function start() {
    cm.sendOk("請等待管理員修復。");
    cm.dispose();
    return;
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }

    if (status == 0) {
        cm.sendSimple("#e<Boss: 希拉>#n\r\n請選擇想要的模式.\r\n\r\n#L0# 普通模式 ( 等級 " + minLevel[0] + " 以上 )#l\r\n#L1# 困難模式 ( 等級 " + minLevel[1] + " 以上 )#l\r\n");
    } else if (status == 1) {
        if (selection != 0 && selection != 1) {
            cm.sendOk("出現未知錯誤。");
            cm.dispose();
            return;
        }
        if (!cm.isAllPartyMembersAllowedLevel(minLevel[selection], maxLevel[selection])) {
            cm.sendNext("組隊成員等級 " + minLevel[selection] + " 以上 " + maxLevel[selection] + " 以下才可以入場。");
        } else if (!cm.isAllPartyMembersAllowedPQ(PQName, maxenter)) {
            cm.sendNext("你的隊員\"" + cm.getNotAllowedPQMember(PQName, maxenter).getName() + "\"次數已經達到上限了。");
        } else {
            var em = cm.getEventManager(name[selection]);
            if (em == null || !open) {
                cm.sendOk("要挑戰的希拉副本還未開放。");
            } else {
                var prop = em.getProperty("state");
                if (prop == null || prop.equals("0")) {
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