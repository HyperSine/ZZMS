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
        ms.getDirectionEffect(5, "", [0, 1000, 1000, -480]);
        ms.showDarkEffect(false);
    } else if (status === i++) {
        ms.wait(2506);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 1000]);
    } else if (status === i++) {
        ms.wait(500);
    } else if (status === i++) {
        ms.sendOthersTalk("好,現在為了要讓你飛到那邊借給你我的翅膀.", 9390384, [false, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("#s110001510#\r\n#b[Shift]#k鍵即可儲存,使用 #b'召喚森林靈魂'#k , 按下#b'方向鍵右鍵'#k BOSS! 就可以切換成雀鷹模式了! 超炫的!", 9390384, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("按下(↑ + jump)鍵就可以飛了.", 9390384, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("按方向鍵即可控制想移動的方向.\r\n一口氣飛到瞭望台去吧. 不好好做的話會掉下去的哦.", 9390384, [true, true], 1);
    } else if (status === i++) {
        ms.teachSkill(110001503, 1, 1);
        ms.teachSkill(112111000, 1, 30);
        ms.lockUI(false);
        ms.disableOthers(false);
        ms.showWZEffectNew("Effect/Direction14.img/effect/ShamanBT/ChapterA/28");
        ms.dispose();
    } else {
        ms.dispose();
    }
}