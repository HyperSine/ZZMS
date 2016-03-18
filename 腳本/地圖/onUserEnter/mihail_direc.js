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
        ms.disableOthers(true);
        ms.showWZEffect("Effect/Direction7.img/mikhail/1st_Job");
        ms.dispose();
    } else {
        ms.dispose();
    }
}
