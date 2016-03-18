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
        qm.sendYesNo("喂~ BOSS!\r\n你受傷了嗎?\r\n要快點治療啊, BOSS!\r\n\r\n雖然無法像阿樂那傢伙一樣治療,但我先給你 #b紅色藥水#k代替一下吧.\r\n\r\n吃這個的話,可以#b體力恢復#k, BOSS!\r\n好, 現在快點吃掉吧!");
    } else if (status === i++) {
        qm.sendNext("哦嗚~ BOSS! 你, 還不賴嘛!. 但好像有點受傷了. 等一下~ BOSS!");
    } else if (status === i++) {
        qm.sendNextPrev("如果把我給你的東西弄碎,就再來跟我說話!");
    } else if (status === i++) {
        qm.gainItem(2432253, 1);
        qm.forceStartQuest();
        qm.openUI(0);
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
        qm.sendNextS("GOOD JOB, BOSS! 現在知道使用道具恢復體力的方法了吧?", 1);
    } else if (status === i++) {
        qm.sendNextPrevS("給你'#b#t2000001##k'和'#b#t2000006##k'各50個,以後要好好使用哦BOSS!", 1);
    } else if (status === i++) {
        qm.sendNextPrevS("咦??可是BOSS!! 有種怪怪的味道耶??", 1);
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.gainItem(2000001, 50);
        qm.gainItem(2000006, 50);
        qm.dispose();
        qm.warp(866111000, 0);
    } else {
        qm.dispose();
    }
}