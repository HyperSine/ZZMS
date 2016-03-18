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
        qm.sendNext("就像剛才揮動樹枝一樣擊退野狼喵.");
    } else if (status === i++) {
        qm.sendNextPrev("喵嗚!!! 想清楚哦.\r\n還有永遠要注意體力.\r\n畫面下方出現的HP就是體力. HP不足時請和我對話喵!那我就會像現在一樣幫你恢復體力喵!\r\n#i03800626#");
    } else if (status === i++) {
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
        qm.sendNext("喵嗚! 太感動了!真沒想到你能完成這件事.");
    } else if (status === i++) {
        qm.sendNextPrevS("喂!!! 但是什麼都不管看到我就和野狼打架嗎?", 2);
    } else if (status === i++) {
        qm.sendNextPrev("喵嗚! 再次向您介紹, 我叫做阿樂. 是楓之谷動物英雄團的...");
    } else if (status === i++) {
        qm.sendNextPrevS("不要轉移話題, 你這傲慢的貓咪!!!", 2);
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.gainExp(10);
        qm.dispose();
    } else {
        qm.dispose();
    }
}