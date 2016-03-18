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
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [60]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg1/4", [900, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [150]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [60]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [60]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [90]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [60]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [510]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg1/9", [900, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.getNPCTalk(["呼呼呼。如何我的力量?"], [3, 0, 3000132, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["挺不錯的?終於我也有可以做些什麼的力量嗎?"], [3, 0, 3000132, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["託這偉大的超新星勇士的福.."], [3, 0, 3000132, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["真開心~嘿嘿嘿"], [3, 0, 3000132, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(都沒有在聽我說話..) 喂。你要熟悉這力量的話，需要練習。"], [3, 0, 3000132, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["練習? 什麼練習?"], [3, 0, 3000132, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["為了好好使用新力量的練習。要練習看看嗎?"], [3, 0, 3000132, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯。很好!何種練習都願意做!"], [3, 0, 3000132, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(很好!)那麼先從變身練習開始!‘變裝!’"], [3, 0, 3000132, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["變裝?"], [3, 0, 3000132, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(940012020, 0);
    } else {
        ms.dispose();
    }
}
