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
        ms.resetMap(ms.getMapId());
        ms.getDirectionStatus(true);
        ms.lockUI(true);
        ms.getDirectionEffect(3, "", [0]);
        ms.disableOthers(true);
        ms.spawnNPCRequestController(9390442, 77, 0);
        ms.spawnNPCRequestController(9390432, 184, 0);
        ms.spawnNPCRequestController(9390433, 246, 0);
        ms.spawnNPCRequestController(9390434, 327, 0);
        ms.spawnNPCRequestController(9390435, 393, 0);
        ms.spawnNPCRequestController(9390304, -209, 0);
        ms.spawnNPCRequestController(9390305, -359, 0);
        ms.spawnNPCRequestController(9390306, -389, 0);
        ms.spawnNPCRequestController(9390307, -169, 0);
        ms.spawnNPCRequestController(9390308, -259, 0);
        ms.spawnNPCRequestController(9390309, -409, 0);
        ms.spawnNPCRequestController(9390310, -459, 0);
        ms.wait(800);
        ms.showDarkEffect(false);
    } else if (status === i++) {
        ms.wait(800);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.wait(100);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(5, "", [0, 1000, 0, 0]);
    } else if (status === i++) {
        ms.wait(1700);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390442, "attack1", 0, true, 300);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390432, "attack1", 0, true, 300);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390433, "attack1", 0, true, 300);
        ms.playSound("ShamanBTTuto/mobattack0");
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390442, "attack1", 0, true, 300);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390434, "attack1", 0, true, 300);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390435, "attack1", 0, true, 300);
        ms.playSound("ShamanBTTuto/mobattack0");
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 1000]);
    } else if (status === i++) {
        ms.wait(1000);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/10", [2000, 0, -100, 1, 0, 0]);
        ms.getDirectionFacialExpression(5, 1500);
        ms.wait(2100);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/BalloonMsg1/9", [2000, 0, -100, 1, 0, 0]);
        ms.wait(1000);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9390434, 1, 100, 100);
        ms.wait(500);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9390435, 1, 120, 100);
        ms.wait(1000);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390442, "attack1", 0, true, 100);
        ms.playSound("ShamanBTTuto/mobattack0");
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390434, "attack1", 0, true, 100);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390432, "attack1", 0, true, 100);
        ms.playSound("ShamanBTTuto/mobattack0");
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390435, "attack1", 0, true, 100);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390433, "attack1", 0, true, 100);
        ms.playSound("ShamanBTTuto/mobattack0");
    } else if (status === i++) {
        ms.getDirectionFacialExpression(5, 5000);
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/BalloonMsg1/10", [1000, 0, -120, 1, 0, 0]);
        ms.wait(1000);
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [436, 540]);
        ms.wait(1000);
    } else if (status === i++) {
        ms.wait(1000);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/7", [3620, -160, 0, 1, 0, 0]);
        ms.wait(300);
    } else if (status === i++) {
        ms.playSound("ShamanBTTuto/RaiUse0");
        ms.trembleEffect(0, 300);
        ms.setNPCSpecialAction(9390435, "hit1", 0, true, 350);
    } else if (status === i++) {
        ms.playSound("ShamanBTTuto/RaiUse1");
        ms.setNPCSpecialAction(9390434, "hit1", 0, true, 100);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390435, "hit1", 0, true, 50);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390434, "hit1", 0, true, 600);
        ms.playSound("ShamanBTTuto/RaiUse2");
        ms.trembleEffect(0, 600);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390435, "die1", 0, true, 100);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390434, "die1", 0, true, 100);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390433, "die1", 0, true, 100);
        ms.setNPCSpecialAction(9390432, "die1", 0, true);
        ms.setNPCSpecialAction(9390442, "die1", 0, true);
        ms.removeNPCRequestController(9390435);
    } else if (status === i++) {
        ms.playSound("ShamanBTTuto/RaiUse0");
        ms.removeNPCRequestController(9390434);
        ms.wait(100);
    } else if (status === i++) {
        ms.removeNPCRequestController(9390433);
        ms.wait(100);
    } else if (status === i++) {
        ms.removeNPCRequestController(9390432);
        ms.wait(100);
    } else if (status === i++) {
        ms.removeNPCRequestController(9390442);
        ms.wait(740);
    } else if (status === i++) {
        ms.showBlackBGEffect(0, 3000, 1000, -1);
        ms.wait(1500);
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/9", [1, 1, 1, 0, 0]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390383, 540, 0, 1);
        ms.wait(1300);
    } else if (status === i++) {
        ms.changeMusic("BgmBT.img/rock'nBattleTiger");
        ms.wait(2000);
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/9", [1, 0]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390383, "special0", 0, true, 1000);
    } else if (status === i++) {
        ms.sendOthersTalk("喂~ 可愛的小鬼, 是 #h0#嗎?", 9390383, [false, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("啊!! 可怕的猛獸!!", 9390383, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("喂~ 什麼可怕,我的心受傷了", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("咦,這樣看來你會說話? \r\n或許貓咪 #b阿樂#k說的 #b同伴#k是你嗎?", 9390383, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("喂~ 小鬼.\r\n很有眼光嘛.\r\n我叫做#b萊伊#k, 勇猛的 #b雪豹#k.", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("哇哦~! 見到你很開心.\r\n其它同伴呢? #b阿樂#k呢?", 9390383, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("喂~ 小鬼. 那個以後再說~\r\n 動作快一點比較好. \r\n 首先快點和我締結#b守護條約#k.", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("守護條約?", 9390383, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("這樣~ 那樣~ #b阿樂#k 這小子,什麼都沒說嗎? \r\n 是指你可以使用我們#b動物英雄們的力量#k的合約.", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("#b動物英雄們的力量#k嗎?我嗎?", 9390383, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("喂~ 小鬼,沒時間了啦. \r\n快點和我擊掌3次!!", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.wait(1000);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390383, "special0", 0, true, 1000);
        ms.getDirectionEffect(0, "", [990, 1010]);
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/8", [2000, -50, 0, 1, 0, 0]);
    } else if (status === i++) {
        ms.playSound("ShamanBTTuto/Contract");
        ms.sendOthersTalk("拍, 拍, 拍!!", 9390383, [false, true], 3);
    } else if (status === i++) {
        ms.wait(1000);
    } else if (status === i++) {
        ms.sendOthersTalk("喂?這樣就結束了?", 9390383, [false, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("喂~ #bBOSS#k! \r\n 合約太複雜通常都是不好的合約吧!", 9390383, [true, true], 1);
    } else if (status === i++) {
        if (ms.isQuestActive(59008)) {
            ms.forceCompleteQuest(59008);
            ms.gainExp(16);
        }
        ms.wait(1000);
    } else if (status === i++) {
        ms.removeNPCRequestController(9390383);
        ms.removeNPCRequestController(9390304);
        ms.removeNPCRequestController(9390305);
        ms.removeNPCRequestController(9390306);
        ms.removeNPCRequestController(9390307);
        ms.removeNPCRequestController(9390308);
        ms.removeNPCRequestController(9390309);
        ms.removeNPCRequestController(9390310);
        ms.lockUI(false);
        ms.disableOthers(false);
        ms.resetMap(ms.getMapId());
        ms.dispose();
        ms.warp(866110000, 0);
    } else {
        ms.dispose();
    }
}