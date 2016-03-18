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
        ms.spawnNPCRequestController(9390383, -180, 0, 1);
        ms.wait(1000);
        ms.showDarkEffect(false);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/21", [1500, 0, -150, 1, 0, 0]);
        ms.getDirectionFacialExpression(5, 5000);
        ms.wait(300);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/23", [1500, -180, -150, 1, 0, 0]);
        ms.wait(1800);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 1000, 1496, 0]);
    } else if (status === i++) {
        ms.wait(2495);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390436, 1496, 0);
        ms.updateNPCSpecialAction(9390436, -1, 700, 100);
        ms.wait(800);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390437, 1496, 0);
        ms.updateNPCSpecialAction(9390437, -1, 153855, 100);
        ms.wait(1000);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390438, 1496, 0);
        ms.updateNPCSpecialAction(9390438, -1, 900, 100);
        ms.wait(1200);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390439, 1496, 0);
        ms.updateNPCSpecialAction(9390439, -1, 1000, 100);
        ms.wait(800);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390440, 1496, 0);
        ms.updateNPCSpecialAction(9390440, -1, 1100, 100);
        ms.wait(1200);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9390437, -1, 500, 100);
        ms.wait(1300);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390436, "attack1", 0, true, 300);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390437, "attack1", 0, true, 1000);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg1/11", [1500, 400, -150, 1, 0, 0]);
        ms.wait(1800);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390438, "attack1", 0, true, 800);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390436, "attack1", 0, true, 450);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 1000]);
    } else if (status === i++) {
        ms.wait(800);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390436, "attack1", 0, true, 300);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390436, "attack1", 0, true, 300);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390440, "attack1", 0, true, 300);
    } else if (status === i++) {
        ms.sendOthersTalk("喂~ #bBOSS#k! 還真是不死心的傢伙", 9390383, [false, true], 1);
    } else if (status === i++) {
        ms.wait(500);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390438, "attack1", 0, true, 500);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390437, "attack1", 0, true, 400);
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [436, 540]);
        ms.setNPCSpecialAction(9390439, "attack1", 0, true, 200);
    } else if (status === i++) {
        ms.sendOthersTalk("消滅掉吧!啊!!", 9390383, [false, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("這戰鬥方式我喜歡, #bBOSS#k!\r\n但這些傢伙是比剛才那些更強大的怪物. \r\n 不要忘了使用我教你的 #b藥水#k. \r\n 好,那就馬上發動 #b雪豹的召喚森林靈魂#k!", 9390383, [true, true], 3);
    } else if (status === i++) {
        if (ms.isQuestActive(59011)) {
            ms.forceCompleteQuest(59011);
            ms.gainExp(372);
        }
        ms.removeNPCRequestController(9390383);
        ms.removeNPCRequestController(9390436);
        ms.removeNPCRequestController(9390437);
        ms.removeNPCRequestController(9390438);
        ms.removeNPCRequestController(9390439);
        ms.removeNPCRequestController(9390440);
        ms.lockUI(false);
        ms.disableOthers(false);
        ms.resetMap(ms.getMapId());
        ms.dispose();
        ms.warp(866112000, 0);
    } else {
        ms.dispose();
    }
}