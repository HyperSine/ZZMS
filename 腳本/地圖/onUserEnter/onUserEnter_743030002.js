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
        ms.getDirectionEffect(1, "", [500]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/balloonMsg9/0", [2000, 0, -100, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getNPCTalk(["呵。時間已經到了…，父親還沒回來。也沒看到耶願，耶願。"], [3, 0, 9330204, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["到這麼晚時間… 到底怎麼回事呢?"], [3, 0, 9330204, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(743020201, 0);
    } else {
        ms.dispose();
    }
}
