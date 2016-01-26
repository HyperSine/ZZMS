/*
 Made by Pungin
 */

        var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 4) {
            qm.sendOkSNew("真是可惜的事情。", 0x20, 1);
            qm.dispose();
            return;
        }
        status--;
    }

    if (status == 0) {
        qm.sendNextSNew("您好，我的名字叫「K」。#b#h0##k，可以聽聽我的話嗎？ ", 0x20, 1);
    } else if (status == 1) {
        qm.sendNextPrevSNew("可是我不認識妳阿. 妳怎麼會知道我的名字呢？", 0x38, 1);
    } else if (status == 2) {
        qm.sendNextPrevSNew("嗯. 那個我認為不是重點. 不管怎樣，現在我跟你在談話的事情為比較重要吧? 我能保證我絕對不是什麼奇怪的人. 反而是提出ㄧ些有趣事情的人喔。", 0x20, 1);
    } else if (status == 3) {
        qm.sendNextPrevSNew("現在雖然無法了解妳說的事情…好…妳先說說看吧…", 0x38, 1);
    } else if (status == 4) {
        qm.sendYesNoSNew("感謝你. 那先暫時移動到別的地方好了. 跟我一起走吧。", 0x20, 1);
    } else if (status == 5) {
        qm.forceCompleteQuest();
        qm.gainExp(1000);
        qm.saveReturnLocation("MULUNG_TC");
        if (qm.getMapId() != 814000000) {
            qm.warp(814000000);
        }
        qm.dispose();
    } else {
        qm.dispose();
    }
}
