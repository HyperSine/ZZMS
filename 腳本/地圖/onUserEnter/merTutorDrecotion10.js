/* global ms */

var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    if (ms.isQuestActive(24007) || ms.isQuestFinished(24007)) {
        ms.dispose();
        return;
    }
    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.lockUI(true);
        ms.getDirectionEffect(2, "Effect/Direction5.img/effect/mercedesInIce/merBalloon/6", [2000, 0, -100, 1, 0, 0]);
        //ms.wait(2000);
        //ms.getDirectionEffect(2, "Effect/Direction5.img/effect/mercedesInIce/merBalloon/7", [2000, 100, -100, 1, 0, 0]);   这儿不知为什么在游戏里是反着显示的
        ms.wait(2000);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);   // [1]为向左走，[2]为向右走，[4]为趴下，[3]估计为跳起
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction5.img/effect/mercedesInIce/merBalloon/8", [2000, 0, -100, 1, 0, 0]);
        ms.wait(2000);
    } else if (status === i++) {
        ms.lockUI(false);
        ms.dispose();
    } else {
        ms.dispose();
    }
}