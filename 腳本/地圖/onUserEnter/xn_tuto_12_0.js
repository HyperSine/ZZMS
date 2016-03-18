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
        ms.getDirectionEffect(1, "", [30]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.spawnNPCRequestController(2159381, -1040, 20, 1, 8464666);
        ms.spawnNPCRequestController(2159384, -990, 20, 1, 8464667);
        ms.spawnNPCRequestController(2159379, -780, 20, 0, 8464668);
        ms.spawnNPCRequestController(2159385, -470, 20, 0, 8464669);
        ms.spawnNPCRequestController(2159386, -550, 20, 0, 8464670);
        ms.spawnNPCRequestController(2159387, -370, 20, 0, 8464671);
        ms.spawnNPCRequestController(2159388, -620, 20, 0, 8464672);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/17", [2400, -500, -130, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [2400]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/18", [1500, 0, -145, 1, 1, 0, 8464669, 0]);
        ms.getDirectionEffect(1, "", [1380]);
    } else if (status === i++) {
        ms.getNPCTalk(["各位!"], [3, 0, 2159384, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["吉可穆德，救回來了。之後再打招呼。唉呀!"], [3, 0, 2159387, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8464671, "shoot", 0, true);
        ms.getDirectionEffect(1, "", [720]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/smogStart", [0, -370, 20, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [1050]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/smog", [3900, -370, 20, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.removeNPCRequestController(8464666);
        ms.removeNPCRequestController(8464667);
        ms.removeNPCRequestController(8464669);
        ms.removeNPCRequestController(8464670);
        ms.removeNPCRequestController(8464671);
        ms.removeNPCRequestController(8464672);
        ms.getDirectionEffect(9, "", [1]);
        ms.getDirectionEffect(1, "", [2220]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/smogEnd", [0, -370, 20, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [420]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg0/1", [1200, 0, -120, 1, 1, 0, 8464668, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.removeNPCRequestController(8464668);
        ms.dispose();
        ms.warp(931060070, 0);
    } else {
        ms.dispose();
    }
}
