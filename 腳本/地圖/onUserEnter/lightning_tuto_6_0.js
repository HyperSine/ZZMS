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
        ms.lockUI(0);
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.spawnNPCRequestController(2159357, 300, -80, 0, 5440951);
        ms.getDirectionEffect(2, "Effect/Direction6.img/effect/tuto/balloonMsg1/6", [0, 0, -160, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 300, 0, -500]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2322]);
    } else if (status === i++) {
        ms.showEnvironment(13, "lightning/screenMsg/2", [0]);
        ms.getDirectionEffect(1, "", [4000]);
        ms.playVoiceEffect("Voice.img/DarkMage/0");
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 300, 300, -100]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1667]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction5.img/effect/mercedesInIce/merBalloon/0", [0, 0, -90, 1, 1, 0, 5440951, 0]);
        ms.getDirectionEffect(1, "", [2100]);
    } else if (status === i++) {
        ms.getNPCTalk(["普力特!... 精靈遊俠!"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(5, "", [1, 180]);
    } else if (status === i++) {
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["來了啊。已經盡力了但還是不夠的樣子。"], [3, 0, 2159357, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["精靈遊俠怎樣了?"], [3, 0, 2159357, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["暫時昏過去了。問題是黑魔法師比我們想像中還要更強大。現在只剩下一個。"], [3, 0, 2159357, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction6.img/effect/tuto/balloonMsg0/10", [0, 0, -90, 1, 1, 0, 5440951, 0]);
        ms.getDirectionEffect(1, "", [1800]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Skill/2218.img/skill/22181003/affected", [0, 0, 0, 1, 1, 0, 5440951, 0]);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getNPCTalk(["(#b聽好了。現在我說的話，不要給你之外的人知道。以前我曾說過的封印，還記得嗎?#k)"], [3, 0, 2159357, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(#b是的。你跟阿普立恩研究好一陣子。#k)"], [3, 0, 2159357, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(#b這封印是設定為黑魔法師從優伊娜開始搶走時間的力量的逆使用。也因此即使黑魔法師在強大，也無法逃離。但是要發動封印的話，要從黑魔法師開始牽動時間的力量。#k)"], [3, 0, 2159357, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(#b我在戰鬥中發現後，黑魔法師沒辦法發現，在這個房間設置了封印。但是我跟精靈遊俠，無法沒有辦法讓黑魔法師使用時間的力量。你所擁有的光的力量的話，說不一定可行。#k)"], [3, 0, 2159357, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#b(我做什麼就可以呢？)#k"], [3, 0, 2159357, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#b（首先要把設置的封印啟動，我會使用剩餘的力量躲過黑魔法師把時間短暫停止，在這期間你就親自去把五個封印啟動。）#k"], [3, 0, 2159357, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#b（首先右邊第一個封印有留下痕跡，靠近過去封印就會顯露出它的樣子，這樣把所有封印啟動就可以了。）#k"], [3, 0, 2159357, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#b（知道了，交給我吧。）#k"], [3, 0, 2159357, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["（#b停止的時間在所有封印都啟動時自動會解除，就交給你了。時間的女神啊，請賜給我力量… ！！#k）"], [3, 0, 2159357, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(5440951);
        ms.dispose();
        ms.warp(927020071, 0);
    } else {
        ms.dispose();
    }
}
