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
        ms.spawnNPCRequestController(9330205, 200, 3, 0, 8515039);
        ms.spawnNPCRequestController(9330202, 80, 3, 1, 8515040);
        ms.getDirectionEffect(1, "", [1500]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8515040, "dead", 0, true);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getNPCTalk(["你跟其他人一樣都是笨蛋。為了救那種人奮不顧身。"], [3, 0, 9330205, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionFacialExpression(4, 10000);
        ms.getNPCTalk(["父親!!! 父親!!! 父親!!!!!!"], [3, 0, 9330205, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["現在全部都結束了。 呵呵"], [3, 0, 9330205, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9330204, -130, 3, 1, 8515209);
        ms.getNPCTalk(["洪武團長!還有 少爺!"], [3, 0, 9330204, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["咻，耶願….父親，父親!!!!"], [3, 0, 9330204, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["逃，必須要逃脫。這，這裡的話，我會處理的。"], [3, 0, 9330202, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["所有東西都在我的計畫裡面。"], [3, 0, 9330205, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["為了不害你這樣的傢伙，我有必要站出來嗎？呵呵"], [3, 0, 9330205, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8515039, "attack0", 0, true);
        ms.showEnvironment(5, "demonSlayer/arkAttack0", []);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9330212, 300, -31, 0, 8515472);
        ms.spawnNPCRequestController(9330212, 430, -31, 0, 8515473);
        ms.spawnNPCRequestController(9330212, 370, -31, 0, 8515474);
        ms.spawnNPCRequestController(9330212, 400, -31, 0, 8515475);
        ms.spawnNPCRequestController(9330213, 380, -31, 0, 8515476);
        ms.spawnNPCRequestController(9330212, 490, -31, 0, 8515477);
        ms.spawnNPCRequestController(9330212, 545, -31, 0, 8515478);
        ms.spawnNPCRequestController(9330212, 600, -31, 0, 8515479);
        ms.spawnNPCRequestController(9330212, 612, -31, 0, 8515480);
        ms.spawnNPCRequestController(9330213, 678, -31, 0, 8515481);
        ms.spawnNPCRequestController(9330212, 701, -31, 0, 8515482);
        ms.spawnNPCRequestController(9330212, 731, -31, 0, 8515483);
        ms.spawnNPCRequestController(9330212, 800, -31, 0, 8515484);
        ms.getNPCTalk(["呼呼，那麼現在就結束吧。各位！一個都不要救！"], [3, 0, 9330205, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["還....還沒有結束。"], [3, 0, 9330202, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["跟我一起來！"], [3, 0, 9330204, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getNPCTalk(["不，我不會去的…."], [3, 0, 9330204, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8515209, "attack0", 0, true);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionFacialExpression(1, 1000);
        ms.getNPCTalk(["啊啊，耶願… 你會打….打我….."], [3, 0, 9330204, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionFacialExpression(4, 30000);
        ms.getDirectionEffect(0, "", [29, 2000]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [25, 30000]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getNPCTalk(["耶願!趕緊走吧!!!"], [3, 0, 9330202, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/DaddysSkill0/4", [1920, 150, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/DaddysSkill0/5", [3840, 600, 200, 0, 0]);
        ms.showEnvironment(5, "chivalrousFighter/dragonSkillUse", []);
        ms.getDirectionEffect(1, "", [2640]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossHit/2", [4000, 600, 20, 0, 0]);
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossHit/2", [4000, 400, -10, 0, 0]);
        ms.removeNPCRequestController(8515209);
        ms.lockUI(0);
        ms.removeNPCRequestController(8515039);
        ms.removeNPCRequestController(8515040);
        ms.removeNPCRequestController(8515472);
        ms.removeNPCRequestController(8515473);
        ms.removeNPCRequestController(8515474);
        ms.removeNPCRequestController(8515475);
        ms.removeNPCRequestController(8515476);
        ms.removeNPCRequestController(8515477);
        ms.removeNPCRequestController(8515478);
        ms.removeNPCRequestController(8515479);
        ms.removeNPCRequestController(8515480);
        ms.removeNPCRequestController(8515481);
        ms.removeNPCRequestController(8515482);
        ms.removeNPCRequestController(8515483);
        ms.removeNPCRequestController(8515484);
        ms.dispose();
        ms.warp(743020102, 0);
    } else {
        ms.dispose();
    }
}
