/* global qm */
var status = -1;

function start(mode, type, selection) {
    qm.forceStartQuest();
    qm.dispose();
}

function end(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.getNPCTalk(["為什麼要花這麼久時間？ 趁我不注意又偷懶是不是？叫你拿來的物品拿來了嗎？\r\n#b\r\n#L0# 是…在這裡…還有我在閣樓撿到這封信。我還沒看…可是好像是雷神之錘寄來的…#l"], [4, 1106002, 0, 5, 0, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["什麼！！ 拿給我！為什麼隨便碰別人的東西？"], [4, 1106002, 0, 0, 0, 0, 0, 1, 0]);
    } else if (status === i++) {
        qm.getDirectionStatus(true);
        qm.lockUI(1, 1);
        qm.disableOthers(true);
        qm.getDirectionFacialExpression(4, 2000);
        qm.getDirectionEffect(1, "", [2000]);
        qm.gainItem(2001503, 10);
    } else if (status === i++) {
        qm.getNPCTalk(["呼…今天又被罵了…"], [4, 1106002, 0, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["咦？這是什麼？"], [4, 1106002, 0, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getDirectionEffect(2, "Effect/Direction7.img/effect/tuto/soul/0", [4000, 0, -100, 1, 0, -100]);
        qm.getDirectionEffect(1, "", [5000]);
    } else if (status === i++) {
        qm.getNPCTalk(["嗚哇！ 剛才那是什麼？微弱的…紅色的光…？"], [4, 1106002, 0, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.dispose();
        qm.warp(913070002, 0);
    } else {
        qm.dispose();
    }
}
