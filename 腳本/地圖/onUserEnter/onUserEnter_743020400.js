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
        ms.showWZEffect("Effect/Direction100.img/BackGroundDirection/intro3");
        ms.dispose();
    } else {
        ms.dispose();
    }
}
