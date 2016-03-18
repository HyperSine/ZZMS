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
        ms.spawnNPCRequestController(3000140, -300, 220, 1, 11560971);
        ms.spawnNPCRequestController(3000104, -450, 220, 1, 11560972);
        ms.spawnNPCRequestController(3000110, -120, 220, 1, 11560973);
        ms.spawnNPCRequestController(3000114, -50, 220, 1, 11560974);
        ms.spawnNPCRequestController(3000111, 130, 220, 0, 11560975);
        ms.spawnNPCRequestController(3000115, 250, 220, 0, 11560976);
        ms.getNPCTalk(["到底有些什麼啊..."], [3, 0, 3000104, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/0", [1200, 0, -120, 1, 1, 0, 11560972, 0]);
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/0", [1200, 0, -120, 1, 1, 0, 11560971, 0]);
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/3", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getNPCTalk(["祭司們到底在做什麼?可是是陌生的臉?"], [3, 0, 3000104, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["噓!不太尋常。貝代洛斯!"], [3, 0, 3000140, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["知道。有一股可疑的味道呢? 我先去村莊請求幫忙，你們看著他們在幹什麼。危險的話就逃跑。"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg0/0", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.getNPCTalk(["在說什麼啊?"], [3, 0, 3000104, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.removeNPCRequestController(11560972);
        ms.getNPCTalk(["解除聖物的話，防禦網就會變弱。"], [3, 0, 3000110, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但是碰聖物的話，不會受到詛咒嗎?"], [3, 0, 3000114, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你相信這種迷信? 這是很好的機會!現在無法回頭了 !"], [3, 0, 3000110, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(是要偷聖物嗎?)"], [3, 0, 3000110, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["是想拿走聖物嗎?"], [3, 0, 3000140, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["要阻止他們!!"], [3, 0, 3000140, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(11560971, 1, 300, 100);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/1", [1200, 0, -120, 1, 1, 0, 11560973, 0]);
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/1", [1200, 0, -120, 1, 1, 0, 11560974, 0]);
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/1", [1200, 0, -120, 1, 1, 0, 11560975, 0]);
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/1", [1200, 0, -120, 1, 1, 0, 11560976, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/6", [900, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.showEnvironment(13, "kaiser/tear_rush", [0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1800]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(11560971);
        ms.removeNPCRequestController(11560973);
        ms.removeNPCRequestController(11560974);
        ms.removeNPCRequestController(11560975);
        ms.removeNPCRequestController(11560976);
        ms.dispose();
        ms.warp(940011040, 0);
    } else {
        ms.dispose();
    }
}
