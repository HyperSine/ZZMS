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
        qm.getNPCTalk(["#p2410008#!是這裡!到剛剛為止確認的結果,影子怪物就聚集在這附近.呃呃...任何時候看到影子怪物都覺得不吉利?看它慢吞吞的樣子..."], [4, 2410003, 0, 0, 32, 0, 0, 1, 0]);
        qm.playVoiceEffect("Voice.img/Milro/0");
    } else if (status === i++) {
        qm.getNPCTalk(["呵,這次不是去清除,只是支援後方所以很輕鬆.如果有怪物來到這邊只要逮他就可以了.托你的福我也很輕鬆喔,#p2410008#!"], [4, 2410003, 0, 0, 32, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Milro/1");
    } else if (status === i++) {
        qm.getNPCTalk(["啊,#r怪物#k好像是在 #b#m321000300##k區域.數量還是#b9隻#k,並沒有增加.呃,天氣悶悶的,好想趕快回去休息!快點清除他們!"], [4, 2410003, 0, 15, 32, 0]);
        qm.playVoiceEffect("Voice.img/Milro/2");
    } else if (status === i++) {
        qm.forceStartQuest();
        qm.removeNPCRequestController(11585230);
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
        qm.getNPCTalk(["雖然請你盡快消滅...但是真的已經都解決了?的確沒有人比你更配得上影子騎士.那我們就撤退吧!"], [4, 2410003, 0, 0, 33, 0, 0, 1, 0]);
        qm.playVoiceEffect("Voice.img/Milro/3");
    } else if (status === i++) {
        qm.getNPCTalk(["...咦?幹嘛要等等?為什麼?還有沒消滅的嗎..."], [4, 2410003, 0, 0, 33, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Milro/4");
    } else if (status === i++) {
        qm.updateInfoQuest(40001, "");
        qm.forceStartQuest(40001, "009");
        qm.forceCompleteQuest(40001);
        qm.dispose();
    } else {
        qm.dispose();
    }
}
