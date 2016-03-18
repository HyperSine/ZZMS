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
        ms.lockUI(1, 1);
        ms.playMovie("xenon.avi", true);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.dispose();
        ms.warp(931060060, 0);
    } else {
        ms.dispose();
    }
}
