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
        ms.getDirectionEffect(1, "", [60]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["是的。我不能只顧著哭。"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["在去有聖物所在的東部聖所看看。那個地方應該有解除詛咒的方法。"], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg0/1", [1200, 30, -70, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg0/0", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg0/2", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [60]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [120]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [60]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [60]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg0/7", [1200, 30, -70, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg0/3", [1200, 30, -70, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg1/0", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [1800]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg0/4", [1200, 30, -70, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg0/8", [1200, 30, -70, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getNPCTalk(["可以看見我的樣子嗎??"], [3, 0, 3000132, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["喔.. 你..什麼? 愛斯卡達?"], [3, 0, 3000132, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["是的我是愛斯卡達。寄居在你的手環。為了要給你力量所以找你說話。"], [3, 0, 3000132, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["力量?到底再說什麼。."], [3, 0, 3000132, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["先前往你對聖物有反應的場所。"], [3, 0, 3000132, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["喔..我也正在途中。"], [3, 0, 3000132, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["好。那邊可以發現實現我的力量的東西。"], [3, 0, 3000132, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我..我如何相信你說的話!"], [3, 0, 3000132, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["不管你信或不信，但是相信我說的話不會有壞處?"], [3, 0, 3000132, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯。也是。反正我也想去。"], [3, 0, 3000132, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(940011100, 0);
    } else {
        ms.dispose();
    }
}
