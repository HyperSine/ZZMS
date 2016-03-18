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
        qm.forceStartQuest();
        qm.sendNext("喂~ BOSS! BOSS現在也快點上來吧.");
    } else if (status === i++) {
        qm.sendPrev("樹藤前面左邊的箭頭按下去就可以上來了, BOSS!");
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

    var i = -1;
    if (status === i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.sendNext("做的好, BOSS! 現在安全了.");
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.dispose();
        qm.warp(866130000, 0);
    } else {
        qm.dispose();
    }
}