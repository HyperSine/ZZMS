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
        ms.getDirectionEffect(1, "", [30]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.spawnNPCRequestController(3000103, -1400, 0, 0, 8381030);
        ms.spawnNPCRequestController(3000104, -1650, 0, 1, 8381031);
        ms.getNPCTalk(["呀 天氣真好！"], [3, 0, 3000103, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你還在說風涼話。我跟 #h0#都成了騎士團員，你要什麼時候能成為騎士團員啊？"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["呵呵。反正我沒有魔力。但總有一天我應該也會有魔力吧？"], [3, 0, 3000103, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那件事好像都講超過百萬次了吧？"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["反正恭喜你們兩個！騎士團員啊∼嗚嗚∼我也想當！"], [3, 0, 3000103, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我覺得提爾不需要成為騎士團員啊？"], [3, 0, 3000103, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這什麼意思啊！我們成立赫力席母攻擊隊的時候不是說好我們三個成為騎士團員嗎！"], [3, 0, 3000103, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那時不知道你沒有魔力啊。"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗚嗚。你一直要這樣嗎？"], [3, 0, 3000103, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["知道了。知道了拉。現在不是該返回了嗎？"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗚嗚。真。羨慕。"], [3, 0, 3000103, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg1/0", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getNPCTalk(["這是這麼？好像有不好的感覺。"], [3, 0, 3000103, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["再說什麼啊？趕快回去吧。在這樣下去會遲到。"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["不不…有什麼東西。東邊聖所好想發生什麼事情。"], [3, 0, 3000104, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["去看看吧！ 是什麼事啊？？"], [3, 0, 3000103, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["喂…提爾。你要想信那種荒唐的話嗎？"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#h0# 感覺不錯啊。還有我沒什麼事做。"], [3, 0, 3000103, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["無論如何都沒有老大的權威樣子。好。走吧。"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(8381030, 1, 400, 100);
        ms.updateNPCSpecialAction(8381031, 1, 400, 100);
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/tuto/BalloonMsg0/3", [1200, 0, -120, 1, 1, 0, 8381030, 0]);
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(8381030);
        ms.removeNPCRequestController(8381031);
        ms.dispose();
        ms.warp(940001210, 0);
    } else {
        ms.dispose();
    }
}
