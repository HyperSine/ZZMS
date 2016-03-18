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
        ms.spawnNPCRequestController(9330204, 807, -100, 0, 8519856);
        ms.getNPCTalk(["你到了!!!"], [3, 0, 9330204, 0, 1, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["父親，父親還沒有到達嗎？"], [3, 0, 9330204, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["好，先坐上船做好出發準備。我觀察一下四週"], [3, 0, 9330204, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["好，知道了!"], [3, 0, 9330204, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["然，然後，請領取這個。"], [3, 0, 9330204, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(8519856);
        ms.dispose();
        ms.warp(743020401, 0);
    } else {
        ms.dispose();
    }
}
