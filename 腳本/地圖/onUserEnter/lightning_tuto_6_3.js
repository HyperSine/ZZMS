/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.getDirectionEffect(3, "", [0]);
        ms.spawnNPCRequestController(2159357, 300, -80, 0, 5443960);
        ms.getNPCTalk(["#b（怎麼樣了呢?）#k"], [3, 0, 2159357, 0, 1, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["#b（把五個封印都啟動了。）#k"], [3, 0, 2159357, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#b（剩餘的事情就是從黑魔法師引出時間的力量而已，我相信如果是擁有光的力量的你就沒有問題。）#k"], [3, 0, 2159357, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 300, 0, -500]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [456]);
    } else if (status === i++) {
        ms.showEnvironment(13, "lightning/screenMsg/3", [0]);
        ms.getDirectionEffect(1, "", [4000]);
        ms.playVoiceEffect("Voice.img/DarkMage/1");
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [-500]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/BalloonMsg0/8", [0, 0, -100, 0, 0]);
        ms.getDirectionEffect(1, "", [2300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/BalloonMsg0/9", [0, 0, -100, 0, 0]);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 300, 0, -500]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2400]);
        ms.playVoiceEffect("Voice.img/DarkMage/4");
    } else if (status === i++) {
        ms.showEnvironment(13, "demonSlayer/whiteOut", [0]);
        ms.getDirectionEffect(1, "", [5000]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(5443960);
        ms.dispose();
        ms.warp(927020100, 0);
    } else {
        ms.dispose();
    }
}
