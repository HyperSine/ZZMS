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
        ms.lockUI(true);
        ms.getDirectionStatus(true);
        ms.showWZEffect("Effect/Direction5.img/mersedesTutorial/Scene0");
        ms.dispose();
    } else {
        ms.dispose();
    }
}