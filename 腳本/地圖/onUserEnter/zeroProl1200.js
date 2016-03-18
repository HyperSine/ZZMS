/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0 && status !== 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.teachSkill(100001273, -1, 0); // 月之降臨
        ms.teachSkill(100000275, -1, 0); // 猛烈刺擊
        ms.teachSkill(100000278, -1, 0); // 暗影降臨
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.playMovie("zero2.avi", true);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(321190000, 0);
    } else {
        ms.dispose();
    }
}
