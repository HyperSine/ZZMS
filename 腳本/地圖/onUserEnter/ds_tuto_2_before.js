/*
 Author: Pungin
 */
var status = -1;

function action(mode, type, selection) {
    status++;
    if (status == 0) {
        ms.lockUI(true);
        ms.disableOthers(true);
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [30]);
        ms.getDirectionStatus(true);
    } else if (status == 0) {
        ms.getDirectionEffect(3, "", [0]);
        ms.showEffect(false, "demonSlayer/text13");
        ms.getDirectionEffect(1, "", [500]);
    } else if (status == 1) {
        ms.showEffect(false, "demonSlayer/text14");
        ms.getDirectionEffect(1, "", [4000]);
    } else {
        ms.lockUI(false);
        ms.dispose();
        ms.warp(927000020, 0);
    }
}


