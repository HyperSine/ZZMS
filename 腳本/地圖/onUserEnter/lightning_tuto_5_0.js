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
        ms.lockUI(1, 1);
        ms.getDirectionEffect(1, "", [9000]);
        ms.getDirectionStatus(true);
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial/Scene1");
    } else if (status === i++) {
        ms.dispose();
        ms.warp(927020070, 0);
    } else {
        ms.dispose();
    }
}
