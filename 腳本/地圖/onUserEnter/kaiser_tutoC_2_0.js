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
        ms.spawnNPCRequestController(3000139, -150, 220, 1, 8382572);
        ms.spawnNPCRequestController(3000110, -20, 220, 0, 8382573);
        ms.spawnNPCRequestController(3000114, 70, 220, 0, 8382574);
        ms.spawnNPCRequestController(3000111, 130, 220, 0, 8382575);
        ms.spawnNPCRequestController(3000115, 250, 220, 0, 8382576);
        ms.getNPCTalk(["小傢伙竟然妨礙我！"], [3, 0, 3000110, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["提爾！！！ 醒醒啊！！"], [3, 0, 3000110, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這些小傢伙真不知天高地厚。看到這個就沒辦法了。除掉吧！"], [3, 0, 3000110, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你覺得我會那麼容易被欺負嗎！！"], [3, 0, 3000110, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你這個傢伙自己一個人能幹嘛！！"], [3, 0, 3000114, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg1/0", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(3, "", [2]);
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [443, 0]);
        ms.showEnvironment(5, "Kaiser/61001101", []);
        ms.getDirectionEffect(2, "Skill/6100.img/skill/61001101/effect", [0, 0, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg1/5", [1200, 0, -120, 1, 1, 0, 8382573, 0]);
        ms.getDirectionEffect(1, "", [150]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8382573, "die1", 0, true);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.removeNPCRequestController(8382573);
        ms.getNPCTalk(["真不自量力。攻擊！"], [3, 0, 3000114, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8382574, "attack1", 0, true);
        ms.setNPCSpecialAction(8382576, "attack1", 0, true);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Npc/3000114.img/hit", [0, 0, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg1/6", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [900]);
        ms.showWZEffect("Effect/Direction9.img/KaiserTutorial/Scene2");
    } else if (status === i++) {
        ms.getNPCTalk(["什麼嘛…還有這樣不知死活的傢伙。"], [3, 0, 3000111, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["一次就被擊倒。這種傢伙不能繞了他。除掉吧！"], [3, 0, 3000114, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/tuto/Effect/0", [0, 0, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.showEnvironment(13, "demonSlayer/whiteOut", [0]);
        ms.getDirectionEffect(1, "", [1950]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(8382572);
        ms.removeNPCRequestController(8382574);
        ms.removeNPCRequestController(8382575);
        ms.removeNPCRequestController(8382576);
        ms.dispose();
        ms.warp(940002020, 0);
    } else {
        ms.dispose();
    }
}
