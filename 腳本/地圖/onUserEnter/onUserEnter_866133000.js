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
        ms.disableOthers(true);
        ms.spawnNPCRequestController(9390382, -707, -600, 0, 1972972);
        ms.spawnNPCRequestController(9390383, -641, -550, 0, 1972973);
        ms.spawnNPCRequestController(9390384, -936, -550, 1, 1972974);
        ms.spawnNPCRequestController(9390307, -788, -547, 1, 1972975);
        ms.spawnNPCRequestController(9390308, -856, -569, 1, 1972976);
        ms.spawnNPCRequestController(9390305, -908, -544, 1, 1972977);
        ms.spawnNPCRequestController(9390304, -984, -573, 0, 1972978);
        ms.getNPCTalk(["門關起來了,現在不會再有怪物跑進來了. \r\n 快下去把那些討厭的怪物趕走吧!"], [3, 0, 9390307, 0, 3, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
        ms.showDarkEffect(false);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect//BalloonMsg1/39", [1500, -210, -30, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getNPCTalk(["我也一起去!!!"], [3, 0, 9390307, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["好啊, 對. 我們也下去和那些傢伙們戰鬥吧!"], [3, 0, 9390308, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg1/18", [800, -280, -30, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["不行,太危險了!"], [3, 0, 9390308, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["別擔心, #h0#, 從以前看來這也是戰士夢寐以求的戰場! \r\n好久沒有發揮實力了,就讓我去!"], [3, 0, 9390308, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.removeNPCRequestController(1972975);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.removeNPCRequestController(1972976);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.removeNPCRequestController(1972977);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.removeNPCRequestController(1972978);
        ms.getDirectionEffect(5, "", [0, 1000, -578, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1042]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390307, -781, 1, 1, 1973280);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390308, -900, -1, 1, 1973283);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390305, -1024, -1, 1, 1973284);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390304, -1100, -1, 0, 1973408);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.removeNPCRequestController(1972973);
        ms.removeNPCRequestController(1972972);
        ms.removeNPCRequestController(1972974);
        ms.removeNPCRequestController(1973280);
        ms.removeNPCRequestController(1973283);
        ms.removeNPCRequestController(1973284);
        ms.removeNPCRequestController(1973408);
        ms.lockUI(0);
        ms.disableOthers(false);
        ms.dispose();
        ms.warp(866134000, 0);
    } else {
        ms.dispose();
    }
}
