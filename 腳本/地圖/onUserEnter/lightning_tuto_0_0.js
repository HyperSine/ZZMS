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
        ms.lockUI(1, 1);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [3300]);
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial/Scene0");
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(927020000, 0);
    } else {
        ms.dispose();
    }
}
