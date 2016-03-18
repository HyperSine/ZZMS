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
        ms.spawnNPCRequestController(2159377, -180, 50, 1, 9232782);
        ms.getDirectionEffect(5, "", [0, 100, -272, -63]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2701]);
    } else if (status === i++) {
        ms.getNPCTalk(["很久的測試和再調整是最後的階段了。那麼，準備好了嗎?"], [3, 0, 2159377, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/11", [1200, 0, -120, 1, 1, 0, 9232782, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 100]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2381]);
    } else if (status === i++) {
        ms.spawnMob(9300635, 150, -301);
        ms.forceStartQuest(23600);
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
