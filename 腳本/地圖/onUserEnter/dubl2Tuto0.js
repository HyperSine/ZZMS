/* global ms */

var status = -1;

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.topMsg("秘密花園深處");
        ms.topMsg("下雨的某一天");
        ms.getDirectionStatus(true);
        ms.disableOthers(true);
        ms.lockUI(true);
        ms.getDirectionEffect(3, "", [0]);
        ms.wait(3000);
        ms.showDarkEffect(false);
        ms.showDarkEffect(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
    } else if (status === i++) {
        ms.disableOthers(false);
        ms.lockUI(false);
        ms.showDarkEffect(false);
        ms.dispose();
    } else {
        ms.dispose();
    }
}