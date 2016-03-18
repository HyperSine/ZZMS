/* global ms */

var status = -1;
var next = false;

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
        if (!next) {
            ms.resetMap(ms.getMapId());
            ms.getDirectionStatus(true);
            ms.lockUI(true);
            ms.getDirectionEffect(3, "", [0]);
            ms.disableOthers(true);
            ms.teachSkill(110001503, -1, 0);
            ms.teachSkill(112111000, -1, 0);
            ms.showDarkEffect(false);
            ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/22", [1, 1, 0, 1020, -363]);
            ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/0", [1, 1, 0, 1442, 20]);
            next = !next;
        }
        ms.sendOthersTalk("話說回來,像你這種程度要使用我的力量還太勉強.先封印起來吧.", 9390384, [false, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("真,真的嗎? #b(嗚咽)#k", 9390384, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("(果斷地)是真的啦!\r\n...\r\n…啊,不是,就算這樣,也不是說真的很弱的意…算了!趕快找把手把門關起來吧!", 9390384, [true, true], 1);
    } else if (status === i++) {
        ms.wait(1000);
    } else if (status === i++) {
        ms.sendOthersTalk("開關在那裡?", 9390384, [false, true], 3);
    } else if (status === i++) {
        ms.wait(1000);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.wait(500);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.sendOthersTalk("好, 現在把這個開關!", 9390384, [false, true], 3);
    } else if (status === i++) {
        ms.wait(900);
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [26, 540]);
        ms.wait(1100);
    } else if (status === i++) {
        ms.sendOthersTalk("這樣可以了!", 9390383, [false, true], 3);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/23", [7450, 15, -5, 1, 0, 0]);
        ms.wait(1500);
    } else if (status === i++) {
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/22", [1, 0]);
        ms.getDirectionEffect(5, "", [0, 1000, 1303, -50]);
    } else if (status === i++) {
        ms.wait(1500);
    } else if (status === i++) {
        ms.wait(500);
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/0", [1, 0]);
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/1", [1, 1, 0, 1442, 20]);
    } else if (status === i++) {
        ms.playSound("advStory/disappear");
        ms.wait(950);
    } else if (status === i++) {
        ms.wait(3000);
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/1", [1, 0]);
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/2", [1, 1, 0, 1442, 20]);
    } else if (status === i++) {
        ms.lockUI(false);
        ms.disableOthers(false);
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/2", [1, 0]);
        ms.resetMap(ms.getMapId());
        ms.dispose();
        ms.warp(866133000, 0);
    } else {
        ms.dispose();
    }
}