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
        ms.getDirectionEffect(3, "", [4]);
        ms.spawnNPCRequestController(3000141, -150, 220, 1, 11562270);
        ms.getDirectionEffect(1, "", [1200]);
        ms.getDirectionStatus(true);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000104, -380, 220, 1, 11562632);
        ms.updateNPCSpecialAction(11562632, 1, 100, 100);
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg1/5", [900, 0, -120, 1, 1, 0, 11562632, 0]);
        ms.getDirectionEffect(1, "", [900]);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/0", [1200, 0, -120, 1, 1, 0, 11562632, 0]);
        ms.spawnNPCRequestController(3000106, -330, 220, 1, 11562658);
        ms.spawnNPCRequestController(3000107, -420, 220, 1, 11562659);
        ms.getDirectionEffect(1, "", [900]);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/0", [1200, 0, -120, 1, 1, 0, 11562658, 0]);
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/0", [1200, 0, -120, 1, 1, 0, 11562659, 0]);
        ms.getDirectionEffect(1, "", [900]);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
    } else if (status === i++) {
        ms.getNPCTalk(["這到底是.."], [3, 0, 3000107, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["凱爾!!! #h0#!"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/BasicEff.img/Kaiser_Transform4_S", [0, 0, 0, 1, 1, 0, 11562270, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg1/6", [1200, 0, -120, 1, 1, 0, 11562658, 0]);
        ms.getDirectionEffect(1, "", [300]);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg1/7", [1200, 0, -120, 1, 1, 0, 11562659, 0]);
        ms.getDirectionEffect(1, "", [600]);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg0/0", [1200, 0, -120, 1, 1, 0, 11562632, 0]);
        ms.getNPCTalk(["凱撒終於出現的樣子。 卡塔利溫。立刻帶他們2個去治療所。"], [3, 0, 3000106, 0, 1, 0, 0, 1, 0]);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
    } else if (status === i++) {
        ms.lockUI(0);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene3");
        ms.removeNPCRequestController(11562270);
        ms.removeNPCRequestController(11562632);
        ms.removeNPCRequestController(11562658);
        ms.removeNPCRequestController(11562659);
        ms.dispose();
        ms.warp(940011060, 0);
    } else {
        ms.dispose();
    }
}
