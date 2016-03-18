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
        ms.spawnNPCRequestController(9390383, -300, 0, 1);
        ms.sendOthersTalk("做的好, BOSS!\r\n真佩服現在這個身體!", 9390383, [false, true], 1);
        ms.showDarkEffect(false);
    } else if (status === i++) {
        ms.sendOthersTalk("真的? 我做的好嗎?\r\n我真的像英雄一樣擊退怪物了嗎?", 9390383, [true, true], 3);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/21", [1500, -270, -150, 1, 0, 0]);
        ms.wait(1800);
    } else if (status === i++) {
        ms.sendOthersTalk("等等!\r\n有種非常危險的感覺.", 9390383, [false, true], 1);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 1000, 1496, 0]);
    } else if (status === i++) {
        ms.wait(2495);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390443, 1496, 0);
        ms.updateNPCSpecialAction(9390443, -1, 1100, 100);
        ms.wait(800);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390436, 1496, 0);
        ms.updateNPCSpecialAction(9390436, -1, 1000, 100);
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
        ms.wait(4000);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9390437, -1, 500, 100);
        ms.wait(1300);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 1000]);
    } else if (status === i++) {
        ms.wait(10000);
    } else if (status === i++) {
        ms.sendOthersTalk("那些不死心的傢伙真的很煩人! \r\n 喂~ BOSS! 那些傢伙, 我隨便收拾掉就可以了吧?", 9390383, [false, true], 1);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390383, "special0", 0, true, 1500);
    } else if (status === i++) {
        ms.removeNPCRequestController(9390383);
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/11", [2150, -300, 0, 1, 0, 0]);
        ms.wait(900);
    } else if (status === i++) {
        ms.trembleEffect(0, 350);
        ms.playSound("ShamanBTTuto/RaiUse0");
        ms.setNPCSpecialAction(9390443, "attack1", 0, true, 450);
    } else if (status === i++) {
        ms.playSound("ShamanBTTuto/mobattack0");
        ms.trembleEffect(0, 0);
        ms.wait(900);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390383, 20, 0, 1);
        ms.setNPCSpecialAction(9390383, "freeze0", 0, true, 750);
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/BalloonMsg1/0", [2150, -300, 0, 1, 0, 0]);
    } else if (status === i++) {
        ms.sendOthersTalk("哼!我的爪子不適合對付穿堅硬盔甲的傢伙!", 9390383, [false, true], 1);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390383, "move", 0, true, 2000);
        ms.updateNPCSpecialAction(9390383, -1, 150, 100);
    } else if (status === i++) {
        ms.sendOthersTalk("該死, 那傢伙怎麼那麼慢啊?", 9390383, [false, true], 1);
    } else if (status === i++) {
        ms.wait(500);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/21", [1500, -150, -150, 1, 0, 0]);
        ms.wait(1000);
    } else if (status === i++) {
        ms.trembleEffect(0, 0);
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/BalloonMsg1/12", [1000, 0, -200, 1, 0, 0]);
        ms.wait(500);
    } else if (status === i++) {
        ms.trembleEffect(0, 0);
        ms.wait(300);
    } else if (status === i++) {
        ms.trembleEffect(0, 0);
        ms.playSound("ShamanBTTuto/PoPo1");
        ms.wait(200);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/15", [1700, 200, 0, 1, 0, 0]);
        ms.wait(900);
    } else if (status === i++) {
        ms.playSound("ShamanBTTuto/PoPo2");
        ms.wait(300);
    } else if (status === i++) {
        ms.trembleEffect(0, 0);
        ms.setNPCSpecialAction(9390443, "die1", 0, true, 150);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390382, 200, 0);
        ms.wait(100);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9390382, -1, 30, 100);
        ms.setNPCSpecialAction(9390383, "move", 0, true, 1550);
        ms.updateNPCSpecialAction(9390383, 1, 50, 100);
    } else if (status === i++) {
        ms.removeNPCRequestController(9390443);
        ms.changeMusic("BgmBT.img/RemembranceBear");
        ms.wait(500);
    } else if (status === i++) {
        ms.sendOthersTalk("有啊... 波波... 頭暈了... 呃啊啊...", 9390382, [false, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("哇哦! 好可愛的熊熊!", 9390382, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("喂~ 像熊一樣慢吞吞的傢伙!!! \r\n為什麼這麼慢才出現啊?", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("嘿!波波本來就是... 熊對吧?嘿嘿嘿!", 9390382, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("趕快清醒啊,波波! \r\n我需要你那無知的力量啊!", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("恩? 這可愛的熊熊是楓之谷動物英雄團的嗎?", 9390383, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("有吧, 有吧, 請叫我波波. 嘿嘿.", 9390382, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("啊啊, 好可愛!!!", 9390382, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("嘿嘿, 嘿嘿!", 9390382, [true, true], 1);
    } else if (status === i++) {
        ms.wait(1000);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9390436, -1, 100, 100);
        ms.updateNPCSpecialAction(9390382, 1, 30, 100);
        ms.wait(1000);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390436, "attack1", 0, true, 400);
        ms.playSound("ShamanBTTuto/PoPo2");
    } else if (status === i++) {
        ms.showBlackBGEffect(0, 1500, 500, -1);
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/17", [2800, 0, -250, 1, 0, 0]);
        ms.trembleEffect(0, 0);
        ms.wait(1000);
    } else if (status === i++) {
        ms.trembleEffect(0, 0);
        ms.removeNPCRequestController(9390436);
        ms.wait(2500);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/BalloonMsg1/0", [1000, 0, -150, 1, 0, 0]);
        ms.wait(1100);
    } else if (status === i++) {
        ms.sendOthersTalk("嚇... 嚇我一跳...\r\n有點可怕...", 9390382, [false, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("嘿嘿, 嘿嘿", 9390382, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("(什... 什麼可怕!!!)", 9390382, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("喂~ 波波! 不是搞這個的時候啦. \r\n 你也快點和BOSS簽訂守護條約,然後去收拾那些硬梆梆的傢伙啦!", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("嗯!知道了. 好, 和波波簽訂守護條約.嘿嘿嘿", 9390382, [true, true], 1);
    } else if (status === i++) {
        ms.wait(500);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9390382, -1, 80, 100);
        ms.wait(2000);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390382, "special3", 0, true, 500);
        ms.getDirectionEffect(0, "", [978, 1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/10", [2000, 60, 0, 1, 0, 0]);
        ms.wait(1000);
    } else if (status === i++) {
        ms.playSound("ShamanBTTuto/Contract");
        ms.sendOthersTalk("拍, 拍, 拍!!", 9390382, [false, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("好, BOSS! 現在就用波波的力量去收拾那個傢伙吧!", 9390383, [true, true], 1);
    } else if (status === i++) {
        if (ms.isQuestActive(59013)) {
            ms.forceCompleteQuest(59013);
            ms.gainExp(560);
        }
        ms.removeNPCRequestController(9390383);
        ms.removeNPCRequestController(9390437);
        ms.removeNPCRequestController(9390438);
        ms.removeNPCRequestController(9390439);
        ms.removeNPCRequestController(9390440);
        ms.removeNPCRequestController(9390382);
        ms.lockUI(false);
        ms.disableOthers(false);
        ms.resetMap(ms.getMapId());
        ms.dispose();
        ms.warp(866127000, 0);
    } else {
        ms.dispose();
    }
}