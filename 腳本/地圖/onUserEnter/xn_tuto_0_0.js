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
        ms.spawnNPCRequestController(2159368, 665, -20, 0, 9222538);
        ms.spawnNPCRequestController(2159374, 380, -20, 1, 9222539);
        ms.spawnNPCRequestController(2159372, 450, -20, 1, 9222540);
        ms.spawnNPCRequestController(2159373, 520, -20, 1, 9222541);
        ms.spawnNPCRequestController(2159375, 590, -20, 0, 9222542);
        ms.showEnvironment(13, "xenon/text0", [0]);
        ms.getDirectionEffect(1, "", [1900]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯，那麼走吧?"], [3, 0, 2159373, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/0", [900, 0, -120, 1, 1, 0, 9222541, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/1", [900, 0, -120, 1, 1, 0, 9222542, 0]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["基督山 紅色!"], [3, 0, 2159375, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["基督山 黃色!"], [3, 0, 2159373, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["基督山 藍色!"], [3, 0, 2159372, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["基督山 綠色!"], [3, 0, 2159374, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["基督山，黑色!"], [3, 0, 2159368, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["全部合在一起…"], [3, 0, 2159373, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/2", [900, 0, -120, 1, 1, 0, 9222541, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9222541, "happy", 0, true);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.getNPCTalk(["哇!真好看!"], [3, 0, 2159373, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你真的喜歡基督山遊戲。"], [3, 0, 2159372, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯!真棒。維持埃德爾斯坦的正義英雄!"], [3, 0, 2159373, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["沒有打敗惡黨雖然是問題。"], [3, 0, 2159374, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["所以每次都只是呼喊一下口號。這遊戲。"], [3, 0, 2159375, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["由我來做壞人的角色的話就可以……."], [3, 0, 2159368, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#h0#! 又這樣說?那可不行。要一起當正義的一方!要不然會遺憾。"], [3, 0, 2159373, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯嗯……."], [3, 0, 2159368, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["好，即使沒有壞人，還是很有趣就好。那麼今天該玩的遊戲都玩了，今天要解散了嗎?"], [3, 0, 2159374, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那麼我先走了。回家做事。"], [3, 0, 2159368, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["再見。 #h0#，明天見!"], [3, 0, 2159373, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9222538, -1, 550, 100);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/3", [1200, 0, -120, 1, 1, 0, 9222542, 0]);
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/4", [1200, 0, -120, 1, 1, 0, 9222539, 0]);
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/5", [1200, 0, -120, 1, 1, 0, 9222538, 0]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.removeNPCRequestController(9222538);
        ms.removeNPCRequestController(9222539);
        ms.removeNPCRequestController(9222540);
        ms.removeNPCRequestController(9222541);
        ms.removeNPCRequestController(9222542);
        ms.dispose();
        ms.warp(931050910, 0);
    } else {
        ms.dispose();
    }
}
