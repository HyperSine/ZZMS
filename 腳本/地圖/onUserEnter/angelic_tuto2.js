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
        ms.spawnNPCRequestController(3000140, -1400, 0, 0, 11560192);
        ms.spawnNPCRequestController(3000104, -1650, 0, 1, 11560193);
        ms.getNPCTalk(["天氣真好!"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你也真是好命。我和凱爾已成為騎士團員，你何時要成為騎士團員呢?"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嘿嘿。我沒有魔力。 可是總有一天我也會有魔力嗎?"], [3, 0, 3000104, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這已經說好幾百次的樣子?"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["反正恭喜你們2個!騎士團員~嗚嗚~我也想當!"], [3, 0, 3000104, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#h0#好像沒有必要一定要成為騎士團員的樣子嗎?"], [3, 0, 3000140, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["到底在說什麼啊! 我們結成赫力席母攻擊隊時，三個人一起發誓成為騎士團員!"], [3, 0, 3000140, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那時你一直不知道沒有魔力。"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯嗯。還要這樣嘛?"], [3, 0, 3000104, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["知道了。我說我知道了。那麼要慢慢回去的時候了嗎?"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗚嗚。真羨慕。真的。"], [3, 0, 3000104, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/story/BalloonMsg1/0", [1200, 0, -120, 1, 1, 0, 11560192, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getNPCTalk(["什麼?有一種不好的感覺。"], [3, 0, 3000140, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["什麼啊?趕快回去吧。要晚了。"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["不是…好像有什麼。東部聖所好像發生什麼事的樣子。"], [3, 0, 3000140, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["去看看!有什麼事呢??"], [3, 0, 3000140, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["啊... #h0#。你相信這樣荒唐的話啊?"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["凱爾的直覺很好啊。而且我也沒事。"], [3, 0, 3000104, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["反正沒有身為隊長的權威。很好。走吧。"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(11560192, 1, 400, 100);
        ms.updateNPCSpecialAction(11560193, 1, 400, 100);
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg0/0", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(11560192);
        ms.removeNPCRequestController(11560193);
        ms.dispose();
        ms.warp(940011030, 0);
    } else {
        ms.dispose();
    }
}
