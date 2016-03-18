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
        ms.showEnvironment(13, "lightning/screenMsg/1", [0]);
        ms.getDirectionEffect(3, "", [0]);
        ms.spawnNPCRequestController(1032201, 230, -130, 0, 5452421);
        ms.spawnNPCRequestController(1032202, 340, -400, 0, 5452422);
        ms.getDirectionEffect(1, "", [2000]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["我剛說的話還記著吧?"], [3, 0, 1032201, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["新鮮的牛奶1桶，烤肉用豬肉3人份，一點釣魚用的魚餌，還有..."], [3, 0, 1032201, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["又忘記了!編織用的線。變冷之前要先把衣服跟圍巾織好。"], [3, 0, 1032201, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["對了。 編織用的線。有紅線跟白線吧?"], [3, 0, 1032201, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(跟拉尼亞見面之後，痛症一直出現。是因為封印之後，光的力量消失的關係嗎?)"], [3, 0, 1032201, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["夜光真笨。所以養他真沒保障。"], [3, 0, 1032202, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["佩尼，你跟夜光這樣說對吧?這次別在忘了。忘了就揍你。"], [3, 0, 1032201, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["知道了，拉尼亞。 我一定會買回來的。哈哈。那麼我出門了。"], [3, 0, 1032201, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["陪你到前面。佩尼，好好看家。 "], [3, 0, 1032201, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["哼，拉尼亞最近都偏愛夜光。這是動物差別待遇。."], [3, 0, 1032202, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["回來要抓一些佩尼喜歡的鮮魚。"], [3, 0, 1032202, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯，嗯! 我不是一隻只會吃貓。但是一定要抓的話，又得拜託那油膩的傢伙。又不是一定想吃!"], [3, 0, 1032202, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["當然會。呵呵。走吧，夜光。"], [3, 0, 1032201, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯，嗯。走吧。(跟之前不一樣，胸口悶悶的感覺不見了。為什麼?)"], [3, 0, 1032201, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.updateNPCSpecialAction(5452421, -1, 400, 100);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.lockUI(0);
        ms.removeNPCRequestController(5452421);
        ms.removeNPCRequestController(5452422);
        ms.dispose();
        ms.warp(910141020, 0);
    } else {
        ms.dispose();
    }
}
