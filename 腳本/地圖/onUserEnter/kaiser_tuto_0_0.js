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
        ms.teachSkill(60001229, 1, 1);
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.spawnNPCRequestController(3000107, -600, 20, 1, 8615395);
        ms.spawnNPCRequestController(3000106, 500, 20, 0, 8615396);
        ms.getNPCTalk(["完蛋了！！"], [3, 0, 3000107, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 300, -400, 27]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2501]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/tuto/BalloonMsg1/0", [7000, 0, -150, 1, 1, 0, 8615395, 0]);
        ms.updateNPCSpecialAction(8615395, 1, 600, 100);
        ms.getDirectionEffect(5, "", [1, 100]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [8047]);
    } else if (status === i++) {
        ms.getNPCTalk(["木雷普族的首都被攻破了！"], [3, 0, 3000107, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(達爾默終於闖禍了…)"], [3, 0, 3000107, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["現在在格蘭蒂斯裡沒有多少抵抗達爾默的勢力了。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["雖然還有阿尼瑪…"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["阿尼瑪跟我們一樣不是個很大的勢力，自然親和的他們沒有受到攻擊的話應該不會想強抵抗達爾默。"], [3, 0, 3000107, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那麼現在…"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["是。達爾默即將會攻擊我們的首都赫力席母。我現在到赫力席母去準備防禦達爾默的侵略。"], [3, 0, 3000107, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我們有贏過達爾默的方法嗎？達爾默現在擁有了像神一般的力量。也把時間超越者克羅尼卡的力量奪去。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getNPCTalk(["赫力席母的防護罩達爾默也不容易進來所以抵擋應該沒問題。力量的平衡雖然傾斜了但我們也不能失去希望。"], [3, 0, 3000106, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getNPCTalk(["卡塔利溫，你留在這裡守護此地方。"], [3, 0, 3000106, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["留在這裡不如我也去戰鬥。我也是 超新星的戰士們。 "], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["以防萬一萬神殿也要用防護罩。還有使用防護罩後在內部防護罩有可能變成無力化，為了阻擋此部分需要一個負責人。你應該也知道目前你是最適任的人選吧？"], [3, 0, 3000107, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["..."], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["凱撒。不要過去。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getNPCTalk(["不要擔心，培霖。我是凱撒。"], [3, 0, 3000106, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(凱撒…)"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(8615395);
        ms.removeNPCRequestController(8615396);
        ms.dispose();
        ms.warp(940001010);
    } else {
        ms.dispose();
    }
}
