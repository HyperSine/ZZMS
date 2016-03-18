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
        ms.spawnNPCRequestController(9390381, 89, 32);
        ms.wait(1000);
    } else if (status === i++) {
        ms.sendOthersTalk("喵嗚!!!謝謝喵.\r\n我是阿樂喵嗚.", 9390381, [false, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("楓之谷動物英雄團的一員喵.", 9390381, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("動物英雄團?", 9390381, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("喵嗚!!雖然還沒什麼名氣,但馬上楓之谷的所有人都會稱讚我們了喵.", 9390381, [true, true], 1);
    } else if (status === i++) {
        ms.spawnMob(9390931, 600, 246);
        ms.wait(300);
    } else if (status === i++) {
        ms.spawnMob(9390931, 722, 246);
        ms.wait(300);
    } else if (status === i++) {
        ms.spawnMob(9390931, 825, 246);
        ms.wait(300);
    } else if (status === i++) {
        ms.spawnMob(9390931, 918, 246);
        ms.wait(300);
    } else if (status === i++) {
        ms.spawnMob(9390931, 1069, 246);
        ms.wait(300);
    } else if (status === i++) {
        ms.spawnMob(9390931, 1174, 246);
        ms.wait(300);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 1000, 900, 217]);
    } else if (status === i++) {
        ms.wait(1588);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg1/7", [1000, 600, 0, 1, 0, 0]);
        ms.wait(1500);
    } else if (status === i++) {
        ms.sendOthersTalk("啊!是野狼!", 9390381, [false, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("哦啊啊,是野狼啊喵啊~!!!救命喵啊啊!", 9390381, [true, true], 1);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 1000, -165, -4]);
    } else if (status === i++) {
        ms.wait(1588);
    } else if (status === i++) {
        ms.sendOthersTalk("但.. 但要怎麼做..", 9390381, [false, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("就像剛剛破壞樹木和捕獸器一樣,打斷樹枝就可以了喵.", 9390381, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("知道了,擊退危險的怪物也是身為英雄的條件!", 9390381, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("滾開, 野狼!!!", 9390381, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("就想成只是一般的野狼喵嗚.", 9390381, [true, true], 1);
    } else if (status === i++) {
        if (ms.isQuestActive(59004)) {
            ms.forceCompleteQuest(59004);
            ms.gainExp(10);
        }
        ms.removeNPCRequestController(9390381);
        ms.lockUI(false);
        ms.disableOthers(false);
        ms.resetMap(ms.getMapId());
        ms.dispose();
        ms.warp(866107000, 0);
    } else {
        ms.dispose();
    }
}