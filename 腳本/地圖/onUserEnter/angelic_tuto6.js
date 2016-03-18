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
        ms.spawnNPCRequestController(3000106, 160, 50, 1, 11563196);
        ms.spawnNPCRequestController(3000107, 10, 50, 1, 11563197);
        ms.spawnNPCRequestController(3000152, 90, 50, 1, 11563198);
        ms.getNPCTalk(["#h0#醒了啊。"], [3, 0, 3000152, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯嗯.. 這裡是?"], [3, 0, 3000152, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["萬神殿。覺得如何?"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["除了頭有點痛，其他還好。"], [3, 0, 3000106, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/0", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getNPCTalk(["喔? 這是什麼??"], [3, 0, 3000106, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["呼..老實說吧。#h0#。你受到東部聖所聖物的詛咒，聖物附在你身體裡。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯?? 詛咒? 附在身體?"], [3, 0, 3000106, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["聖物附著所變化的樣子就是那個手環。可以說是聖物消失了一個。"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["卡塔利溫。別太責怪。不是凱撒 #h0#的話，也是被搶走的聖物。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["好..知道了。"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["哈…哈哈哈..不懂在說什麼。我只記得我碰了聖物..."], [3, 0, 3000107, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["聖物附在我身上變成手環又是什麼意思? 到底是..."], [3, 0, 3000107, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["鎮定一點。 附著的聖物不知會怎樣。但是聖物附著好像沒有傷害的樣子。"], [3, 0, 3000152, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我..我沒有要讓聖物消失的想法.."], [3, 0, 3000152, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#h0#。沒有人會說你什麼的。萬神殿還有聖物3個沒有大問題的。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但..但是..我.."], [3, 0, 3000106, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#h0#。又要哭了啊?"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯.."], [3, 0, 3000107, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["卡塔利溫!!"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["對不起。我也不知道.."], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(11563196);
        ms.removeNPCRequestController(11563197);
        ms.removeNPCRequestController(11563198);
        ms.dispose();
        ms.warp(940011070, 0);
    } else {
        ms.dispose();
    }
}
