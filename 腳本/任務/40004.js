/* global qm */
var status = -1;

function start(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.getNPCTalk(["#b(已經回不去了...)"], [3, 0, 0, 0, 45, 0, 0, 1, 0, 2410008]);
        qm.playVoiceEffect("Voice.img/Alpha/90");
    } else if (status === i++) {
        qm.getNPCTalk(["#b(如果害怕陷阱就想逃避的話...我就不會努力挖掘真相了.)"], [3, 0, 0, 0, 45, 0, 1, 1, 0, 2410008]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/91");
        qm.getNPCTalk(["#face3# #b(毫無意義的活著,倒不如死了算了.)"], [3, 0, 0, 0, 45, 0, 1, 1, 0, 2410008]);
        qm.playVoiceEffect("Voice.img/Alpha/92");
    } else if (status === i++) {
        qm.getNPCTalk(["#face3# #b(不論用什麼方法,我都得讓她醒來!然後向騙我的人報仇!這就是我要做的事!)"], [3, 0, 0, 0, 45, 0, 1, 1, 0, 2410008]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/93");
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
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.getNPCTalk(["哈啊...哈啊...全都擊倒了嗎...果真是來到回不去的地步了..."], [3, 0, 0, 0, 45, 0, 0, 1, 0, 2410008]);
        qm.playVoiceEffect("Voice.img/Alpha/98");
    } else if (status === i++) {
        qm.getNPCTalk(["#face11#現在不論用什麼方法都得讓她醒來!"], [3, 0, 0, 0, 45, 0, 1, 1, 0, 2410008]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/99");
        qm.forceStartQuest(40004, "001001003");
        qm.forceCompleteQuest();
        qm.forceCompleteQuest(40900);
        qm.dispose();
    } else {
        qm.dispose();
    }
}
