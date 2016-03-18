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
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.playMovie("angelic.avi", true);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.gainItem(1142495, 1); // 古代超新星的契約者
        ms.dispose();
        ms.warp(400000000, 0);
    } else {
        ms.dispose();
    }
}
