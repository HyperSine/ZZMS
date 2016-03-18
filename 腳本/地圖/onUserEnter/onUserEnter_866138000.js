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
        ms.playMovie("BeastTamer.avi", true);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.dispose();
        ms.warp(866191000, 0);
    } else {
        ms.dispose();
    }
}
