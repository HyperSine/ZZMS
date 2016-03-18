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
        ms.getDirectionEffect(1, "", [10000]);
        ms.getDirectionStatus(true);
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial2/Scene4");
    } else if (status === i++) {
        ms.lockUI(0);
        ms.playSoundEffect("MiniGame.img/Open");
        ms.dispose();
        ms.warp(910141030, 0);
    } else {
        ms.dispose();
    }
}
