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
        ms.spawnNPCRequestController(2159356, 1500, 50, 0, 5438940);
        ms.spawnNPCRequestController(2159360, 1350, 50, 1, 5438941);
        ms.spawnNPCRequestController(2159361, 1300, 50, 1, 5438942);
        ms.getDirectionEffect(2, "Effect/OnUserEff.img/normalEffect/demonSlayer/chatBalloon0", [1000, 0, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.setNPCSpecialAction(5438940, "attack", 0, true);
        ms.showEnvironment(5, "LuminousTuto/Use2", []);
        ms.getDirectionEffect(1, "", [450]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/BalloonMsg0/5", [0, 0, -120, 0, 0]);
        ms.setNPCSpecialAction(5438941, "hit", 0, true);
        ms.setNPCSpecialAction(5438942, "hit", 0, true);
        ms.getDirectionEffect(2, "Skill/2112.img/skill/21120005/hit/0", [0, -5, -50, 1, 1, 0, 5438941, 0]);
        ms.getDirectionEffect(2, "Skill/2112.img/skill/21120005/hit/0", [0, -5, -50, 1, 1, 0, 5438942, 0]);
        ms.showEnvironment(5, "LuminousTuto/Hit2", []);
        ms.getDirectionEffect(1, "", [270]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Skill/2112.img/skill/21120005/hit/0", [0, -5, -50, 1, 1, 0, 5438941, 0]);
        ms.getDirectionEffect(2, "Skill/2112.img/skill/21120005/hit/0", [0, -5, -50, 1, 1, 0, 5438942, 0]);
        ms.showEnvironment(5, "LuminousTuto/Hit2", []);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.showEnvironment(5, "LuminousTuto/Use3", []);
        ms.setNPCSpecialAction(5438941, "die", 0, true);
        ms.setNPCSpecialAction(5438942, "die", 0, true);
        ms.getDirectionEffect(2, "Skill/2112.img/skill/21120005/hit/0", [0, -5, -50, 1, 1, 0, 5438941, 0]);
        ms.getDirectionEffect(2, "Skill/2112.img/skill/21120005/hit/0", [0, -5, -50, 1, 1, 0, 5438942, 0]);
        ms.showEnvironment(5, "LuminousTuto/Hit3", []);
        ms.getDirectionEffect(1, "", [2200]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.removeNPCRequestController(5438941);
        ms.removeNPCRequestController(5438942);
    } else if (status === i++) {
        ms.getNPCTalk(["那邊也不簡單，比想像中早到。果然是光的守護者。"], [3, 0, 2159356, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["為什麼自己一個人在這個地方? 普力特或精靈遊俠呢?這個傷又是怎麼來的?"], [3, 0, 2159356, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["他們二個先出發。說不一定已經跟黑魔法師在戰鬥。我發生一些事情，有些晚了。"], [3, 0, 2159356, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["矛鬥士，有辦法一起去嗎?"], [3, 0, 2159356, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["現在是在擔心我嗎?這個還挺開心?但是沒關係。這種程度不算什麼 。比起這個趕快跟去看看吧。雖然普力特跟精靈遊俠配合的不錯，但是跟黑魔法師戰鬥還是無法負荷的。"], [3, 0, 2159356, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["…知道了。不要太逞強。"], [3, 0, 2159356, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/BalloonMsg0/7", [4000, 0, -100, 0, 0]);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/BalloonMsg0/6", [2500, 0, -100, 1, 1, 0, 5438940, 0]);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(5438940);
        ms.dispose();
        ms.warp(927020060, 0);
    } else {
        ms.dispose();
    }
}
