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
        ms.lockUI(1, 0);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/sig", [0, 0, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/luti", [0, 0, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [2100]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 1000, -374, 43]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/0", [900, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/15", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/laser", [0, 0, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [1800]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/16", [1200, -110, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.dispose();
        ms.warp(931050990, 0);
    } else {
        ms.dispose();
    }
}
