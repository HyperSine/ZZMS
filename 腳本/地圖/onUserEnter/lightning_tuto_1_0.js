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
        ms.getNPCTalk(["連天氣都配合氣氛。符合最後的天氣啊。"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [120]);
    } else if (status === i++) {
        ms.getNPCTalk(["下雨天淒慘的等待的習慣不太好。"], [3, 0, 2159353, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(2159353, 1210, 10, 0, 5432519);
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["你遲了。世界最高的怪盜也無法偷走時間。"], [3, 0, 2159353, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["哼，反正要打的人不來，你算是早來的。變長的打鬥剛好今日也結束，一定要這樣急的理由是什麼呢?如何。有自信嗎?"], [3, 0, 2159353, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["對自己的勝利沒有自信。"], [3, 0, 2159353, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這種狀況也心直口快。所以一開始我對你不滿意。"], [3, 0, 2159353, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這種狡猾的性格果然不甘心。"], [3, 0, 2159353, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["說真的，我們之間唯一的共通點就是不喜歡彼此。不管這個，將我們送做堆的普力特到底在想什麼。那個人，是不是有什麼事啊?"], [3, 0, 2159353, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那麼對於分配5個人最有效率，考慮後的結果。跟全部的人都很親，所以覺得跟其他人也會好好相處。"], [3, 0, 2159353, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["普力特跟其他人不同。但是並不是你想像中單純的傢伙。"], [3, 0, 2159353, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["說不一定是這樣呢。閒話到此為止。空氣安靜下來了。"], [3, 0, 2159353, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["無趣的傢伙。那麼不多說。再次見面的話，直接問普力特。別在這地區瞎談。"], [3, 0, 2159353, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(5432519, "teleportation", 0, true);
        ms.getDirectionEffect(1, "", [840]);
    } else if (status === i++) {
        ms.removeNPCRequestController(5432519);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["現在只差一步。"], [3, 0, 2159353, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.spawnNPCRequestController(2159354, 128, 10, 1, 5433566);
        ms.getNPCTalk(["停住。這個地方是我跟你的戰場。"], [3, 0, 2159354, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 450, -200, 18]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1938]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["你所持有光的力量跟精靈一樣是溫暖的陽光。在這個地方打倒你我的內心覺得不太舒服。"], [3, 0, 2159354, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["成為黑魔法師的走狗，破壞世界，不應該是率領精靈的口中說出來的話。"], [3, 0, 2159354, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["如果朝著自己的夢想盡最大的力是瘋狂的事的話，我就同意我瘋了。但是這樣的話，你跟全世界應該全都瘋了吧?"], [3, 0, 2159354, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["別在絞辯。#p2159354#."], [3, 0, 2159354, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你真是一個不懂美食當前，一個美食家的心情。那麼我來配合你。"], [3, 0, 2159354, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(5433566, "special", 0, true);
        ms.showEnvironment(5, "LuminousTuto/Special1", []);
        ms.getDirectionEffect(1, "", [1600]);
        ms.showWZEffectNew("Effect/OnUserEff.img/normalEffect/demonSlayer/chatBalloon0");
    } else if (status === i++) {
        ms.showEnvironment(5, "LuminousTuto/Special2", []);
        ms.getDirectionEffect(1, "", [2280]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(2159355, 0, 10, 1, 5436114);
        ms.getDirectionEffect(1, "", [700]);
    } else if (status === i++) {
        ms.removeNPCRequestController(5433566);
        ms.getNPCTalk(["嗯哼，事情變容易了。感謝你。"], [3, 0, 2159355, 0, 9, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#p2159355#!難道連同伴都要傷害嗎!"], [3, 0, 2159355, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [436, 540]);
        ms.getDirectionEffect(2, "Skill/2711.img/skill/27111100/prepare", [540, -40, -25, 0, 0]);
        ms.showEnvironment(5, "LuminousTuto/Use", []);
        ms.getDirectionEffect(1, "", [90]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(5436114, "barrier", 0, false);
        ms.getDirectionEffect(1, "", [450]);
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [437, 3000]);
        ms.getDirectionEffect(2, "Skill/2711.img/skill/27111100/keydown", [3000, -40, -25, 0, 0]);
        ms.showEnvironment(5, "LuminousTuto/Loop", []);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/OnUserEff.img/normalEffect/lightning/guard", [0, 0, 0, 1, 1, 0, 5436114, 0]);
        ms.showEnvironment(5, "LuminousTuto/Hit", []);
        ms.getDirectionEffect(1, "", [270]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/OnUserEff.img/normalEffect/lightning/guard", [0, 0, 0, 1, 1, 0, 5436114, 0]);
        ms.showEnvironment(5, "LuminousTuto/Hit", []);
        ms.getDirectionEffect(1, "", [270]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/OnUserEff.img/normalEffect/lightning/guard", [0, 0, 0, 1, 1, 0, 5436114, 0]);
        ms.showEnvironment(5, "LuminousTuto/Hit", []);
        ms.getDirectionEffect(1, "", [270]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/OnUserEff.img/normalEffect/lightning/guard", [0, 0, 0, 1, 1, 0, 5436114, 0]);
        ms.showEnvironment(5, "LuminousTuto/Loop", []);
        ms.showEnvironment(5, "LuminousTuto/Hit", []);
        ms.getDirectionEffect(1, "", [270]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/OnUserEff.img/normalEffect/lightning/guard", [0, 0, 0, 1, 1, 0, 5436114, 0]);
        ms.showEnvironment(5, "LuminousTuto/Hit", []);
        ms.getDirectionEffect(1, "", [270]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/OnUserEff.img/normalEffect/lightning/guard", [0, 0, 0, 1, 1, 0, 5436114, 0]);
        ms.showEnvironment(5, "LuminousTuto/Hit", []);
        ms.getDirectionEffect(1, "", [270]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/OnUserEff.img/normalEffect/lightning/guard", [0, 0, 0, 1, 1, 0, 5436114, 0]);
        ms.showEnvironment(5, "LuminousTuto/Hit", []);
        ms.getDirectionEffect(1, "", [270]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/OnUserEff.img/normalEffect/lightning/guard", [0, 0, 0, 1, 1, 0, 5436114, 0]);
        ms.showEnvironment(5, "LuminousTuto/Loop", []);
        ms.showEnvironment(5, "LuminousTuto/Hit", []);
        ms.getDirectionEffect(1, "", [270]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/OnUserEff.img/normalEffect/lightning/guard", [0, 0, 0, 1, 1, 0, 5436114, 0]);
        ms.showEnvironment(5, "LuminousTuto/Hit", []);
        ms.getDirectionEffect(1, "", [270]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/OnUserEff.img/normalEffect/lightning/guard", [0, 0, 0, 1, 1, 0, 5436114, 0]);
        ms.showEnvironment(5, "LuminousTuto/Hit", []);
        ms.getDirectionEffect(1, "", [540]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Skill/2711.img/skill/27111101/keyedownend", [0, -40, -25, 0, 0]);
        ms.showEnvironment(5, "LuminousTuto/End", []);
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getNPCTalk(["這種程度還好。但是時候到了。雖然可惜，要除掉你延到下次。"], [3, 0, 2159355, 0, 9, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["什麼意思?"], [3, 0, 2159355, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["沒有空跟你玩下去，在這世界已經沒事了。哈哈哈。"], [3, 0, 2159355, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(5436114, "teleportation", 0, false);
        ms.getDirectionEffect(1, "", [450]);
    } else if (status === i++) {
        ms.removeNPCRequestController(5436114);
        ms.getNPCTalk(["吸收庫瓦勒的力量，從戰鬥中逃走…? 並且說 #b'這世界'#k，應該有其他的詭計。但是託福少了一些事。現在集中在黑魔法師吧。先去的同伴現在應該到達神殿裡。應該要趕快跟他們會合。"], [3, 0, 2159355, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(927020010, 0);
    } else {
        ms.dispose();
    }
}
