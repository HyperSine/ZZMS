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
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [30]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.spawnNPCRequestController(2159380, 450, 180, 0, 9240591);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
    } else if (status === i++) {
        ms.getNPCTalk(["#h0#， #h0#，有什麼事嗎?"], [3, 0, 2159380, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["露緹，我想問這個俘虜一些事。可以對博士保密嗎?"], [3, 0, 2159380, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["剛跟那個人見面時，做了什麼吧。#h0#，難道那記憶是我嗎?我的過去……."], [3, 0, 2159380, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["記憶? 過去?什麼意思? 露緹，你怎會知道?"], [3, 0, 2159380, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["先收下這個。"], [3, 0, 2159380, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.showEnvironment(13, "xenon/knife", [0]);
        ms.getDirectionEffect(1, "", [4200]);
    } else if (status === i++) {
        ms.getNPCTalk(["這個是那個人的東西。在我看來，那個人是找回你的記憶的重要線索。你們聊一下吧。我幫你看傑利麥勒回來了沒。"], [3, 0, 2159380, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9240591, 1, 700, 100);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getNPCTalk(["露緹謝謝。"], [3, 0, 2159380, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2100]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.removeNPCRequestController(9240591);
        ms.getNPCTalk(["那個……。我想問你。"], [3, 0, 2159384, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/2", [900, 700, 50, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [810]);
    } else if (status === i++) {
        ms.getNPCTalk(["你! 黑色翅膀的……!"], [3, 0, 2159384, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(果然……。看這個人的話，總有一股神秘的感覺。明明是今天第一次看到的人，我知道這個人的樣子。)"], [3, 0, 2159384, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(而且這個人的短刀，總覺得不像別人的東西的樣子。)"], [3, 0, 2159384, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那，這個"], [3, 0, 2159384, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.showEnvironment(13, "xenon/knife", [0]);
        ms.getDirectionEffect(1, "", [4200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/1", [900, 700, 50, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [810]);
    } else if (status === i++) {
        ms.getNPCTalk(["這我的短刀!"], [3, 0, 2159384, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這是你的吧?這有特別的力量吧?你怎會有這個?"], [3, 0, 2159384, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["黑色翅膀為什麼問這個呢?"], [3, 0, 2159384, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["看到這個的瞬間，好像想起什麼似的，我也搞不清楚。露緹說這個跟我的記憶或跟我的過去有關聯……。如果你知道我的過去告訴我吧。"], [3, 0, 2159384, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(這個人到底再說什麼? 記憶?)"], [3, 0, 2159384, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/1", [1200, 700, 50, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getNPCTalk(["等一下，你說你對這短刀不陌生?難道你……!"], [3, 0, 2159384, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(2159381, 1200, 180, 1, 9243911);
        ms.updateNPCSpecialAction(9243911, -1, 350, 100);
        ms.getNPCTalk(["#h0#，#h0#!"], [3, 0, 2159381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["完蛋了，傑利麥勒回來了!有從那個人那邊得知什麼嗎?"], [3, 0, 2159381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["什麼都沒有。剛那場面再也想不起來。"], [3, 0, 2159381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["真是的…果然記憶不容易恢復。"], [3, 0, 2159381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["記憶? 剛到底再說什麼，露緹?"], [3, 0, 2159381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["不是再這樣下去的時候#h0#，在我看來這是最後的機會。你跟這個人一起逃吧。不這樣的話，可能連找回的記憶都被傑利麥勒給刪除。"], [3, 0, 2159381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["記憶，刪除?"], [3, 0, 2159381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["是啊。你聽不懂我剛說的話吧……。事實我之前跟你約定好了。我會幫你離開這邊。"], [3, 0, 2159381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["約定?跟誰?"], [3, 0, 2159381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["……記憶喪失之前，跟你。"], [3, 0, 2159381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/1", [1200, 700, 50, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/1", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getNPCTalk(["你一定記不得了。這個全是傑利麥勒刪掉你的記憶的關係。但是刪除記憶之前，你想要離開這邊。我跟那時的你做約定。一定讓你離開這邊。"], [3, 0, 2159381, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["所以到現在一直裝做是傑利麥勒的好部下……。\r\n今天就是機會。"], [3, 0, 2159381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這個人一定跟你的過去有關。所以你們一見面瞬間，你就想起什麼……。"], [3, 0, 2159381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["一起逃吧。不這樣的話，你會連找回的記憶都會消失的。"], [3, 0, 2159381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["記憶……。剛出現的場面是我過去的記憶嗎?"], [3, 0, 2159381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["是的。\r\n時間不多。傑利麥勒會立刻回來。一定會刪除你的記憶，連這個人都會有危險。你要怎做?"], [3, 0, 2159381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我想找回記憶。"], [3, 0, 2159381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["事實上我也不知道那是什麼。但是就是想找回來的感覺。而且不能讓那個人受傷的樣子。"], [3, 0, 2159381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg0/1", [1200, 700, 50, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.getNPCTalk(["好，既然決定的話， 傑利麥勒到達前，趕快逃跑。剩下的我來解決……."], [3, 0, 2159381, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["露緹，你也一起走。我逃跑的事，傑利麥勒知道的話，也不會放過你的。"], [3, 0, 2159381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但我不是戰鬥用機器人。帶著我只會成為你的包袱。"], [3, 0, 2159381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["也因為這樣，更不能把你留在這邊。一起逃吧。"], [3, 0, 2159381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["…好吧，現在說這些，太浪費時間。我也一起走。那麼趕快吧!"], [3, 0, 2159381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionStatus(true);
        ms.removeNPCRequestController(9243911);
        ms.dispose();
        ms.warp(931050970, 0);
    } else {
        ms.dispose();
    }
}
