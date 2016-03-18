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
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(3, "", [4]);
        ms.spawnNPCRequestController(3000141, -150, 220, 1, 11561722);
        ms.spawnNPCRequestController(3000115, 200, 220, 0, 11561723);
        ms.spawnNPCRequestController(3000111, 300, 220, 0, 11561724);
        ms.getDirectionEffect(1, "", [1200]);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/BasicEff.img/Kaiser_Transform4_S", [0, 0, 0, 1, 1, 0, 11561722, 0]);
        ms.getNPCTalk(["怎…怎麼可能..."], [3, 0, 3000115, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這..這種小孩有什麼力量.."], [3, 0, 3000111, 0, 1, 0, 1, 1, 0]);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
    } else if (status === i++) {
        ms.getNPCTalk(["還好昏過去的樣子。幸好。"], [3, 0, 3000115, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
        ms.getNPCTalk(["嚇一跳。既然看到我們的臉要除掉他們!"], [3, 0, 3000111, 0, 1, 0, 1, 1, 0]);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg0/1", [1200, 0, -100, 0, 0]);
        ms.getDirectionEffect(1, "", [900]);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["那....那個!爬起來了!"], [3, 0, 3000115, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [150]);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [482, 0]);
        ms.getDirectionEffect(2, "Skill/6512.img/skill/65121002/effect", [0, 0, 0, 0, 0]);
        ms.showEnvironment(5, "Angelicburster/65121002", []);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.showEnvironment(13, "demonSlayer/whiteOut", [0]);
        ms.setNPCSpecialAction(11561723, "die1", 0, true);
        ms.setNPCSpecialAction(11561724, "die1", 0, true);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.removeNPCRequestController(11561723);
        ms.removeNPCRequestController(11561724);
        ms.getDirectionEffect(1, "", [720]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(11561722);
        ms.dispose();
        ms.warp(940011050, 0);
    } else {
        ms.dispose();
    }
}
