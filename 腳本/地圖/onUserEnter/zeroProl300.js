/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (ms.getOneInfo(40008, "act1") === "clear") {
        ms.dispose();
    } else if (ms.getQuestStatus(40002) !== 0) {
        ms.resetMap(ms.getMapId());
        action2(mode, type, selection);
    } else {
        action1(mode, type, selection);
    }
}

function action1(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.resetMap(ms.getMapId());
        ms.getTopMsg("連續按[Ctrl] 攻擊鍵消滅敵人.");
        ms.spawnMob(9300744, -448, -6);
        ms.spawnMob(9300744, 234, -6);
        ms.spawnMob(9300744, 525, -6);
        ms.spawnMob(9300744, -646, -388);
        ms.spawnMob(9300744, 310, -379);
        ms.spawnMob(9300744, 848, -379);
        ms.spawnMob(9300744, 1164, -298);
        ms.spawnMob(9300744, 1791, -368);
        ms.spawnMob(9300744, 1542, -368);
        ms.spawnMob(9300744, -656, -388);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
/* global ms */
var status = -1;

function action2(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.getTopMsg("請按照地圖上的方向鍵移動.");
        ms.dispose();
    } else {
        ms.dispose();
    }
}