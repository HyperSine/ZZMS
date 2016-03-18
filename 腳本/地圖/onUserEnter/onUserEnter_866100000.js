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
        ms.lockUI(true);
        ms.getDirectionEffect(3, "", [0]);
        ms.disableOthers(true);
        ms.sendOthersTalk("我是#b#h0##k!\r\n還有這裡是登貝伊森林深處的大樹木村莊!", 9390305, [false, true], 3);
    } else if (status === i++) {
        ms.wait(500);
    } else if (status === i++) {
        ms.getDirectionFacialExpression(1, 2000);
        ms.wait(500);
    } else if (status === i++) {
        ms.sendOthersTalk("我的耳朵和尾巴很神奇吧?", 9390305, [false, true], 3);
    } else if (status === i++) {
        ms.wait(500);
    } else if (status === i++) {
        ms.getDirectionFacialExpression(0, 5000);
        ms.wait(500);
    } else if (status === i++) {
        ms.sendOthersTalk("也許這是我成為未來英雄的證明,不是嗎?", 9390305, [false, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("羅莎那奶奶每天睡前都會和我說.", 9390305, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("打敗可怕的魔法師#b黑魔法師#k那#b 5位英雄#k的故事!!! \r\n我早晚也會成為那樣的英雄的!", 9390305, [true, true], 3);
    } else if (status === i++) {
        ms.wait(1000);
    } else if (status === i++) {
        ms.wait(1000);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/10", [2000, 0, -120, 1, 0, 0]);
        ms.wait(800);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 1000, 700, 0]);
    } else if (status === i++) {
        ms.wait(1200);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/BalloonMsg1/7", [2000, 571, -120, 1, 0, 0]);
        ms.playSound("ShamanBTTuto/sound0");
        ms.wait(3000);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 1000]);
    } else if (status === i++) {
        ms.wait(500);
    } else if (status === i++) {
        ms.getDirectionFacialExpression(4, 5000);
        ms.sendOthersTalk("啊!大事不好了!!", 9390305, [false, true], 3);
    } else if (status === i++) {
        ms.wait(500);
    } else if (status === i++) {
        ms.lockUI(false);
        ms.disableOthers(false);
        ms.dispose();
        ms.warp(866101000, 0);
    } else {
        ms.dispose();
    }
}