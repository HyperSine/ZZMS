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
        qm.sendNext("但是村莊入口的#b#p9390306##k在哭,那也是你開的玩笑嗎?");
    } else if (status === i++) {
        if (mode !== 1) {
            qm.dispose();
            return;
        }
        qm.sendNextPrevS("齁!..", 2);
    } else if (status === i++) {
        qm.sendYesNo("別鬧了!要讓朋友知道你很珍惜他. 快去和 #b#p9390306##k道歉.");
    } else if (status === i++) {
        qm.sendNextS("是!珍惜朋友也是英雄的條件!", 3);
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

    var i = -1;
    if (status === i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.sendNextS("布蘭登對不起...", 2);
    } else if (status === i++) {
        qm.sendNextPrev("切,我以為是你弄的.");
    } else if (status === i++) {
        qm.sendNextPrevS("嘿嘿,對不起.我以後不開玩笑了.\r\n這樣才能成為英雄!", 2);
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.dispose();
    } else {
        qm.dispose();
    }
}