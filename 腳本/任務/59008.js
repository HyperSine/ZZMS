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
        qm.sendNext("#b#h0##k是住在這旁邊的大樹木村莊喵嗚?");
    } else if (status === i++) {
        if (mode !== 1) {
            qm.dispose();
            return;
        }
        qm.sendNextPrevS("嗯, 住在大樹木村莊的羅莎那奶奶家.", 2);
    } else if (status === i++) {
        qm.sendYesNo("知道了.那你就先回去等著.召喚同伴時再去找你喵嗚.");
    } else if (status === i++) {
        qm.sendNextS("知道了, 會準備好吃的海鮮,快點來哦. 阿喵.", 2);
    } else if (status === i++) {
        qm.sendNextPrev("我不是阿喵,我叫阿樂啦. 還有我一直都是吃素的貓咪.海鮮給波波那傢伙吧.");
    } else if (status === i++) {
        qm.sendNextPrevS("波波?", 2);
    } else if (status === i++) {
        qm.sendNextPrev("喵嗚!!!波波也是我們楓之谷動物英雄團的成員.超強的哦!!!");
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
        qm.sendNext("做的好,有感覺變強一點了喵嗚.");
    } else if (status === i++) {
        qm.sendNextPrev("就像現在這樣一直打怪,就可以升級變的更強了哦喵!!!");
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.gainExp(34);
        qm.dispose();
    } else {
        qm.dispose();
    }
}