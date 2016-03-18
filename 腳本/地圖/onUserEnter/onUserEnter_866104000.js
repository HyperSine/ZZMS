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
        ms.showBlackBGEffect(300, 3300, 3000, -1);
        ms.wait(300);
    } else if (status === i++) {
        ms.wait(3000);
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/20", [1, 1, 1, 0, 0]);
    } else if (status === i++) {
        ms.sendOthersTalk("還好嗎? 喵嗚?", 9390300, [false, true], 3);
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/20", [1, 0]);
    } else if (status === i++) {
        ms.sendOthersTalk("謝謝你哦! 可是好... 等一下... 喵嗚... 你轉過去後面啦喵嗚, 人家很害羞.", 9390300, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("好了沒啊?", 9390300, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("喵嗚!!! 不好意思喵嗚!!! 再等一下下喵嗚哦!!!!", 9390300, [true, true], 1);
    } else if (status === i++) {
        ms.forceCompleteQuest(59003);
        ms.lockUI(false);
        ms.disableOthers(false);
        ms.dispose();
        ms.warp(866105000, 0);
    } else {
        ms.dispose();
    }
}