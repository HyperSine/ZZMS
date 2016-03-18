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
        qm.getNPCTalk(["UI/tutorial/zero/0/0"], [4, 2410006, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["...這個...好像有寫什麼.是這女人像拿的卷軸嗎?...也許這女人並不是一開始就是石像吧."], [4, 2410006, 0, 0, 45, 0, 0, 1, 0, 2410008]);
        qm.playVoiceEffect("Voice.img/Alpha/55");
    } else if (status === i++) {
        qm.getNPCTalk(["時間太晚了.即使是笨笨的 #p2410003# 也會覺得奇怪.還是回去吧."], [4, 2410006, 0, 0, 45, 0, 1, 1, 0, 2410008]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/56");
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
        qm.getNPCTalk(["...楓之谷與黑魔法師?還有女神優伊娜...時間的超越者..."], [3, 0, 0, 0, 45, 0, 0, 1, 0, 2410008]);
        qm.playVoiceEffect("Voice.img/Alpha/65");
    } else if (status === i++) {
        qm.getNPCTalk(["#face7#噓!聲音太大了.要小心一點.能相信的人一個都沒有...打起精神."], [3, 0, 0, 0, 45, 0, 1, 1, 0, 2410008]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/66");
        qm.getNPCTalk(["#b(這卷軸中稱為蜘蛛王的人,是把我關在這裡的罪魁禍首...)"], [3, 0, 0, 0, 45, 0, 1, 1, 0, 2410008]);
        qm.playVoiceEffect("Voice.img/Alpha/67");
    } else if (status === i++) {
        qm.getNPCTalk(["#b(我擁有的力量,就是時間之力的話...這裡說的神之子難道就是...我嗎?不然...就是關在影子神殿的那女人?)"], [3, 0, 0, 0, 45, 0, 1, 1, 0, 2410008]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/68");
        qm.getNPCTalk(["#face11# #b(...可惡.還是沒有頭緒.結論還是得喚醒神殿裡的那女人,才能聽到正確的說明吧...)"], [3, 0, 0, 0, 45, 0, 1, 1, 0, 2410008]);
        qm.playVoiceEffect("Voice.img/Alpha/69");
    } else if (status === i++) {
        qm.getNPCTalk(["#face11# #b(可是不知道要如何才能喚醒他呀.)"], [3, 0, 0, 0, 45, 0, 1, 1, 0, 2410008]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/70");
        qm.updateInfoQuest(40003, "");
        qm.updateInfoQuest(40010, "");
        qm.forceCompleteQuest();
        qm.dispose();
        qm.warp(321000900, 0);
    } else {
        qm.dispose();
    }
}
