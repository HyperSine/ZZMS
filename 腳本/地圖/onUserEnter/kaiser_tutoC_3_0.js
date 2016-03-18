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
        ms.getDirectionEffect(9, "", [1]);
        ms.spawnNPCRequestController(3000142, -100, 220, 1, 8383900);
        ms.spawnNPCRequestController(3000139, -150, 220, 1, 8383901);
        ms.spawnNPCRequestController(3000114, 70, 220, 0, 8383902);
        ms.spawnNPCRequestController(3000111, 130, 220, 0, 8383903);
        ms.spawnNPCRequestController(3000115, 250, 220, 0, 8383904);
        ms.getNPCTalk(["這…這是什麼…"], [3, 0, 3000114, 0, 1, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["那個小孩變成這個樣子？"], [3, 0, 3000111, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["看起來好危險…"], [3, 0, 3000114, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/tuto/BalloonMsg0/2", [1200, 0, -200, 1, 1, 0, 8383900, 0]);
        ms.setNPCSpecialAction(8383900, "DKgigaSlasher", 0, true);
        ms.getDirectionEffect(2, "Skill/6112.img/skill/61121201/effect", [0, 0, 0, 0, 0]);
        ms.showEnvironment(5, "Kaiser/61121100", []);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8383902, "die1", 0, true);
        ms.setNPCSpecialAction(8383903, "die1", 0, true);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.removeNPCRequestController(8383902);
        ms.removeNPCRequestController(8383903);
        ms.getNPCTalk(["怎…怎麼可可能…"], [3, 0, 3000115, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.showEnvironment(13, "demonSlayer/whiteOut", [0]);
        ms.getDirectionEffect(1, "", [1950]);
    } else if (status === i++) {
        ms.getDirectionEffect(9, "", [0]);
        ms.lockUI(0);
        ms.removeNPCRequestController(8383900);
        ms.removeNPCRequestController(8383901);
        ms.removeNPCRequestController(8383904);
        ms.dispose();
        ms.warp(940001240, 0);
    } else {
        ms.dispose();
    }
}
