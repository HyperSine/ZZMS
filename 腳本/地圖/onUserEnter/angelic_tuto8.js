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
        ms.getNPCTalk(["呼... 來了基地，我再來該怎麼辦?"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗚嗚..我為什麼沒有一件好事啊?"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene1");
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000140, -400, 0, 1, 11564645);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(11564645, 1, 170, 100);
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/0", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [690]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["喔!凱爾。成為凱撒?真棒。"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#h0#。我在找你。你還好嗎?"], [3, 0, 3000140, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我?為什麼找我? 難道是這個的關係? 手有奇怪的東西附著但是沒事。"], [3, 0, 3000140, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你也知道。我習慣這種事了。哈哈哈"], [3, 0, 3000140, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#h0#..."], [3, 0, 3000140, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["以為這種東西附在手上，會有魔力，但是沒有運氣吧。說要守住聖物反而讓他消失了…連我都對自己失望。"], [3, 0, 3000140, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["貝代洛斯也擔心我。"], [3, 0, 3000140, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#p3000007#也擔心我? 我有讓你們擔心了。對不起。但是我沒事。不用擔心我也沒關係。"], [3, 0, 3000140, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那麼我有事先走了。"], [3, 0, 3000140, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(11564645);
        ms.dispose();
        ms.warp(940011090, 0);
    } else {
        ms.dispose();
    }
}
