/* global qm */

var status = -1;

function start(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (status === i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.sendNext("OK, BOSS! 再用一次這樣貌大展身手吧!");
    } else if (status === i++) {
        qm.sendNextPrev("準備好就走了哦!");
    } else if (status === i++) {
	qm.spawnMob(9390928, 461, 28);
	qm.spawnMob(9390928, 561, 28);
	qm.spawnMob(9390928, 661, 28);
	qm.spawnMob(9390928, 761, 28);
	qm.spawnMob(9390928, 861, 28);
        qm.forceStartQuest();
        qm.dispose();
    } else {
        qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    if (qm.haveItem(2432253)) {
        qm.dispose();
        return;
    }

    var i = -1;
    if (status === i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.sendNext("哦嗚~ BOSS! 你,很有天份也!");
    } else if (status === i++) {
        qm.dispose();
        qm.warp(866126000, 0);
    } else {
        qm.dispose();
    }
}