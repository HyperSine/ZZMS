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
        qm.sendNext("那個該不會是你弄的?\r\n#b#p9390305##k會很為難的啊.");
    } else if (status === i++) {
        if (mode !== 1) {
            qm.dispose();
            return;
        }
        qm.sendNextPrev("快去和 #b#p9390305##k道歉.");
    } else if (status === i++) {
        qm.sendYesNo("反省自已的錯誤及道歉也是英雄的條件.");
    } else if (status === i++) {
        qm.sendNextS("英雄的條件!\r\n是!我馬上就去道歉.", 3);
    } else if (status === i++) {
        qm.forceStartQuest();
        qm.openUI(6);
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
        qm.sendNextS("希嘉大嬸, 對不起..", 2);
    } else if (status === i++) {
        qm.sendNextPrev("這是你開的玩笑嗎?");
    } else if (status === i++) {
        qm.sendNextPrevS("是的,對不起. 只是想讓大家都開心一點而已...", 2);
    } else if (status === i++) {
        qm.sendNextPrev("哎喲,每天只知道胡鬧... 好吧,這次就原諒你,下次不可以這樣了?");
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.dispose();
    } else {
        qm.dispose();
    }
}