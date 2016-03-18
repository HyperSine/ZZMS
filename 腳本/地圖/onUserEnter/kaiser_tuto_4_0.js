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
        ms.spawnNPCRequestController(3000131, -390, 170, 0, 8369065);
        ms.getDirectionEffect(5, "", [0, 300, -600, 170]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [999]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
        ms.showWZEffect("Effect/Direction9.img/kaiserTutorial/Scene2");
    } else if (status === i++) {
        ms.getNPCTalk(["你這個傢伙竟然把赫力席母交給達爾默！！你這樣還算是超新星嗎？！"], [3, 0, 3000131, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["把你救活的評議會與超新星竟然會對他們恩將仇報… 絕對不能原諒！"], [3, 0, 3000131, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這個嘛，像你一樣的天才當然不能理解。我想要變得更強。還有剛好達爾默擁有那個方法。"], [3, 0, 3000131, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["不知道你得到了哪些力量你覺得你能贏過我嗎！這種傷現在對我來說不算什麼！"], [3, 0, 3000131, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["冷靜的凱撒現在搞不懂狀況。現在幫你整理一下你的狀況。"], [3, 0, 3000131, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["雖然我從達爾默那裡獲得新的力量但應該還贏不過超新星守護神的你。你覺得我就這樣對付你嗎？真單純。"], [3, 0, 3000131, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你剛剛受到的傷你覺得是一般的傷吧，但剛剛那個攻擊具有致命的毒。雖然不能打敗你但那個程度的話可以抑制你的力量。"], [3, 0, 3000131, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["…那麼到毒發作前把你除掉不就好了。"], [3, 0, 3000131, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["哈哈哈。你覺得那麼容易嗎？赫力席母已經落到達爾默的手裡。這裡很數萬個幽靈跟隨著我。即使是平常的你也贏不過我，你不知道嗎？"], [3, 0, 3000131, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["是長是短要量量看才知道的。"], [3, 0, 3000131, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000125, -750, 170, 0, 8369552);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000122, -650, 170, 0, 8369554);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000125, -550, 170, 0, 8369555);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 450, -1300, 170]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [1526]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000122, -1150, 170, 1, 8369679);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000125, -1250, 170, 1, 8369681);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000122, -1350, 170, 1, 8369682);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000125, -1450, 170, 1, 8369685);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000122, -1550, 170, 1, 8369687);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000125, -1650, 170, 1, 8369816);
        ms.getDirectionEffect(5, "", [1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [-30]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2000]);
        ms.showWZEffectNew("Effect/OnUserEff.img/normalEffect/demonSlayer/chatBalloon0");
        ms.showWZEffect("Effect/Direction9.img/kaiserTutorial/Scene2");
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/tuto/BalloonMsg2/0", [0, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 450, -600, 170]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [744]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8369065, "alert", 0, true);
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/tuto/BalloonMsg1/2", [0, 0, -130, 1, 1, 0, 8369065, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 0]);
    } else if (status === i++) {
        ms.removeNPCRequestController(8369552);
        ms.removeNPCRequestController(8369554);
        ms.removeNPCRequestController(8369555);
        ms.removeNPCRequestController(8369679);
        ms.removeNPCRequestController(8369681);
        ms.removeNPCRequestController(8369682);
        ms.removeNPCRequestController(8369685);
        ms.removeNPCRequestController(8369687);
        ms.removeNPCRequestController(8369816);
        ms.lockUI(0);
        ms.showEnvironment(13, "lightning/screenMsg/0", [0]);
        ms.removeNPCRequestController(8369065);
        ms.dispose();
        ms.warp(940001150, 0);
    } else {
        ms.dispose();
    }
}
