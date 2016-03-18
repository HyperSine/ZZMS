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
        ms.getDirectionEffect(0, "", [373, 20500]);
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/illustration/ChivalrousLogo/0", [10000, 0, -50, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [10000]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(743020103, 0);
    } else {
        ms.dispose();
    }
}
