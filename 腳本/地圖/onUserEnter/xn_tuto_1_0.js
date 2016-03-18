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
        ms.getDirectionEffect(9, "", [1]);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.spawnNPCRequestController(2159368, -1050, -30, 0, 9224348);
        ms.spawnNPCRequestController(2159376, -1800, -30, 1, 9224349);
        ms.updateNPCSpecialAction(9224348, -1, 300, 100);
        ms.getDirectionEffect(5, "", [0, 80, -1600, -34]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [6870]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg0/2", [900, 0, -120, 1, 1, 0, 9224348, 0]);
        ms.getDirectionEffect(1, "", [810]);
    } else if (status === i++) {
        ms.getNPCTalk(["那個爺爺是誰?好像不是村莊的人的樣子。"], [3, 0, 2159368, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/6", [900, 0, -120, 1, 1, 0, 9224349, 0]);
        ms.getDirectionEffect(1, "", [810]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(2159422, -1450, -30, 1, 9228546);
        ms.spawnNPCRequestController(2159422, -1350, -30, 0, 9228547);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/0", [1200, 0, -120, 1, 1, 0, 9224348, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(2159370, -1400, -30, 0, 9228730);
        ms.removeNPCRequestController(9224348);
        ms.removeNPCRequestController(9228546);
        ms.removeNPCRequestController(9228547);
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getNPCTalk(["呵呵…結果發現這樣地方，到各村莊找果然有成果。"], [3, 0, 2159376, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/8", [1200, 0, -120, 1, 1, 0, 9224349, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9224349, -1, 300, 100);
        ms.updateNPCSpecialAction(9228730, -1, 300, 100);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(2159372, -500, -30, 0, 9229274);
        ms.updateNPCSpecialAction(9229274, -1, 200, 100);
        ms.getDirectionEffect(5, "", [0, 150, -950, -34]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [4335]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9229274, 1, 1, 100);
        ms.getDirectionEffect(1, "", [90]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9229274, -1, 1, 100);
        ms.getDirectionEffect(1, "", [90]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9229274, 1, 1, 100);
        ms.getDirectionEffect(1, "", [90]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9229274, -1, 1, 100);
        ms.getDirectionEffect(1, "", [90]);
    } else if (status === i++) {
        ms.getNPCTalk(["#h0#那麼快回家了啊?說好今天要還借走的短劍……."], [3, 0, 2159372, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["明天見面的話，要還他。"], [3, 0, 2159372, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9229274, 1, 100, 100);
        ms.getDirectionEffect(1, "", [150]);
    } else if (status === i++) {
        ms.removeNPCRequestController(9224349);
        ms.removeNPCRequestController(9228730);
        ms.removeNPCRequestController(9229274);
        ms.dispose();
        ms.warp(931060080, 0);
    } else {
        ms.dispose();
    }
}
