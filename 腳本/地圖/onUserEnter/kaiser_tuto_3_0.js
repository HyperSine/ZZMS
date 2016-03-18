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
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg0/0", [0, 0, -105, 0, 0]);
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["氣氛好像怪怪的。我來晚了嗎？"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但是赫力席母的防護罩即使是達爾默本人來也不容易穿破這到底怎麼一回事？"], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["標識赫力席母境界停止啟動。"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 600, -600, 178]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2638]);
    } else if (status === i++) {
        ms.getNPCTalk(["一次都沒有閉上眼睛的守護石也失去了光亮。"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["赫力席母已經被佔領了嗎？怎麼防護罩不會啟動呢？"], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000131, -390, 170, 0, 8367711);
        ms.getNPCTalk(["凱撒，太晚了。"], [3, 0, 3000131, 0, 9, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 450, -600, 178]);
    } else if (status === i++) {
        ms.getNPCTalk(["梅格耐斯！你怎麼會在這？赫力席母有發生什麼事嗎？"], [3, 0, 3000131, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["現在重要的不是那個。達爾默的軍隊剛剛戰領了赫力席母"], [3, 0, 3000131, 0, 9, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8367711, "fake", 0, true);
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg1/1", [0, 0, -110, 0, 0]);
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8367711, "fake", 0, true);
        ms.getNPCTalk(["梅格耐斯! 受傷了嗎？是不是與達爾默的軍隊對抗嗎？"], [3, 0, 3000131, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["真不像樣。真不想給你看這種樣子。唉，不小心變成這個樣子了。因為只追求力量的理由被剔除我無法擔當得起啊。"], [3, 0, 3000131, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["達爾默的軍隊到底怎麼侵入到赫力席母？ 防護罩是用什麼穿破的？"], [3, 0, 3000131, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["內部有背叛者。他們一時除掉防護罩時赫力席母到處都有幽靈出現。"], [3, 0, 3000131, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["到底是誰做了那些事… "], [3, 0, 3000131, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["就是… 我！！！"], [3, 0, 3000131, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(8367711);
        ms.dispose();
        ms.warp(940002010, 0);
    } else {
        ms.dispose();
    }
}
