/* global ms */

var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    if (ms.isQuestActive(24005) == false) {   //防止某些做死的玩家未完成“请沉睡”任务就进了国王休息室然后触发剧情。
        ms.dispose();
        return;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.lockUI(true);
        ms.getDirectionEffect(2, "Effect/Direction5.img/effect/mercedesInIce/merBalloon/9", [2000, 0, -100, 1, 0, 0]);
        ms.wait(2000);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
    } else if (status === i++) {
        ms.lockKey(true);
        ms.dispose();
        ms.warp(910150005, 0);
    } else {
        ms.dispose();
    }
}