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
        ms.spawnNPCRequestController(9390383, -150, 0, 1);
        ms.spawnNPCRequestController(9390382, 200, 0, 1);
        ms.sendOthersTalk("好樣的! BOSS, 波波!", 9390383, [false, true], 1);
        ms.showDarkEffect(false);
    } else if (status === i++) {
        ms.sendOthersTalk("嘿嘿, 波波做的好嗎?", 9390382, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("呵呵呵,嗯! 做的好,可愛的熊熊!", 9390382, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("呵... BOSS這身軀也...", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("這麼可愛,竟然力氣這麼大!!", 9390383, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("嘿嘿.嘿嘿!", 9390382, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("哦... BOSS... 這... 這又是???", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 1000, 1496, 0]);
    } else if (status === i++) {
        ms.wait(2495);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390436, 1496, 0);
        ms.updateNPCSpecialAction(9390436, -1, 950, 100);
        ms.wait(800);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390437, 1496, 0);
        ms.updateNPCSpecialAction(9390437, -1, 600, 100);
        ms.wait(1000);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390438, 1496, 0);
        ms.updateNPCSpecialAction(9390438, -1, 900, 100);
        ms.wait(1200);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390439, 1496, 0);
        ms.updateNPCSpecialAction(9390439, -1, 700, 100);
        ms.wait(800);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390440, 1496, 0);
        ms.updateNPCSpecialAction(9390440, -1, 700, 100);
        ms.wait(1000);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 1000]);
    } else if (status === i++) {
        ms.wait(800);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/22", [1500, 0, -150, 1, 0, 0]);
        ms.wait(300);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/22", [1500, 200, -150, 1, 0, 0]);
        ms.wait(300);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/23", [1500, -80, -150, 1, 0, 0]);
        ms.wait(300);
    } else if (status === i++) {
        ms.sendOthersTalk("嗚哇啊!真的是超煩的這些不死心的傢伙們!!!\r\nBOSS! 這樣沒完沒了的. \r\n我們躲一下比較好.", 9390383, [false, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("有吧, 有吧. \r\n 波波是想爬著樹藤到樹上面去,好像比較好.", 9390382, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("好啊, 波波. 真是不錯的點子. \r\n BOSS, 快叫人們躲避吧.", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("知道了,交給我!", 9390383, [true, true], 3);
    } else if (status === i++) {
        ms.wait(800);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.wait(1300);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.sendOthersTalk("各位!!! 羅莎那奶奶! 布蘭登! 希嘉大嬸! \r\n 快往那樹上面逃吧!!!", 9390383, [false, true], 3);
    } else if (status === i++) {
        ms.wait(1000);
    } else if (status === i++) {
        ms.removeNPCRequestController(9390383);
        ms.removeNPCRequestController(9390382);
        ms.removeNPCRequestController(9390436);
        ms.removeNPCRequestController(9390437);
        ms.removeNPCRequestController(9390438);
        ms.removeNPCRequestController(9390439);
        ms.removeNPCRequestController(9390440);
        ms.lockUI(false);
        ms.disableOthers(false);
        ms.resetMap(ms.getMapId());
        ms.dispose();
        ms.warp(866129000, 0);
    } else {
        ms.dispose();
    }
}