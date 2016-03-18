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
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [30]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.spawnNPCRequestController(2159377, -200, 50, 1, 9230273);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/doorOpen", [0, -15, 2, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/8", [1200, 0, -120, 1, 1, 0, 9230273, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/9", [1200, 0, -120, 1, 1, 0, 9230273, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/10", [1200, 30, -250, 1, 1, 0, 9230273, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/8", [1200, 0, -120, 1, 1, 0, 9230273, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/7", [1200, 0, -250, 1, 1, 0, 9230273, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.removeNPCRequestController(9230273);
        ms.dispose();
        ms.warp(931060081, 0);
    } else {
        ms.dispose();
    }
}
