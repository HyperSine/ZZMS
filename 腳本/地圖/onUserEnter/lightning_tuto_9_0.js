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
        ms.getDirectionEffect(3, "", [4]);
        ms.getDirectionEffect(2, "Effect/OnUserEff.img/guideEffect/evanTutorial/evanBalloon40", [0, 20, 0, 0, 0]);
        ms.spawnNPCRequestController(1032200, 800, -40, 0, 5448808);
        ms.getDirectionEffect(1, "", [1000]);
        ms.getDirectionStatus(true);
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial2/Scene0");
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 400, 540, -230]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(5448808, -1, 200, 100);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.getNPCTalk(["那邊好像有人昏倒。沒事嗎?"], [3, 0, 1032200, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(5448808, -1, 600, 100);
        ms.getDirectionEffect(5, "", [1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [0]);
    } else if (status === i++) {
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial2/Scene0");
        ms.getDirectionEffect(1, "", [0]);
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial2/Scene0");
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [0]);
    } else if (status === i++) {
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial2/Scene0");
        ms.getDirectionEffect(1, "", [0]);
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial2/Scene0");
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [0]);
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial2/Scene0");
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [200]);
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial2/Scene0");
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [3000]);
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial2/Scene0");
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(0, "", [4, 2000]);
        ms.getDirectionEffect(2, "Effect/Direction5.img/effect/mercedesInIce/merBalloon/1", [2000, 20, -100, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial2/Scene2");
    } else if (status === i++) {
        ms.getNPCTalk(["太好了。醒來了。這裡是精靈住的都市#b魔法森林#k附近的小森林，我叫拉尼亞。"], [3, 0, 1032200, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["魔法森林?第一次聽到。那你有沒有聽過#b櫻花處#k?"], [3, 0, 1032200, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["櫻花處? 嗯…好像有聽說書的歌曲中聽過的樣子…記不太起來。"], [3, 0, 1032200, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(分明我將黑魔法師關在封印裡，而我也被困在那個裡面。那之後到底過了多久啊?)你知道黑魔法師嗎?"], [3, 0, 1032200, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["啊，如果是那個的話，我確時有印象。數百年更早之前，有一個叫黑魔法師的壞人，有可以分開大陸的強大力量。然後有5個英雄出現，將黑魔法師擊退，替世界帶來和平。"], [3, 0, 1032200, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這樣子…數百年只是一瞬間。內心悶悶的有點噁心也是因為這樣嗎?"], [3, 0, 1032200, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯?現在在說什麼?"], [3, 0, 1032200, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["沒事。你的父母在哪裡?一個人來這種地方很危險。"], [3, 0, 1032200, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["…不知道。我知道的是在森林某處遇見佩尼。啊，佩尼是跟我一起住的貓。"], [3, 0, 1032200, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["對不起。我太不關心了。"], [3, 0, 1032200, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["沒關係。因為佩尼的關係，沒有感受到孤單呢?但是還挺想跟人說話的 。佩尼只會嘮叨。哼。"], [3, 0, 1032200, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我的口才不好。"], [3, 0, 1032200, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["沒關係。我很久沒在這邊遇到人。我介紹佩尼給你。要不要到我家玩?"], [3, 0, 1032200, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(所知道的東西都已經過去了，我能這樣安靜的活下去嗎?)如果不會麻煩的話，我可以去拉尼亞的家嗎?"], [3, 0, 1032200, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(5448808, 1, 600, 100);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.removeNPCRequestController(5448808);
        ms.dispose();
        ms.warp(910141050, 0);
    } else {
        ms.dispose();
    }
}
