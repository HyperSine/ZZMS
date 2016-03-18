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
        ms.spawnNPCRequestController(1032201, 340, 0, 0, 5453445);
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["天氣真好。這種天氣應該要去野餐。"], [3, 0, 1032201, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那麼要不要買一些準備便當的材料呢?"], [3, 0, 1032201, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["好。一些容易煮的菜跟麵包就可以了。"], [3, 0, 1032201, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(趕快回去，吃拉尼亞所做的便當，然後好好休息。這樣不明的悶氣應該也會消失吧?)"], [3, 0, 1032201, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(5453445, -1, 400, 100);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["那麼別太晚。等你喔。"], [3, 0, 1032201, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯，馬上回來。等會見。 "], [3, 0, 1032201, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(如果能跟拉尼亞一直這樣幸福的活下去…嗯?痛症怎麼突然變嚴重。為什麼會這樣?!)"], [3, 0, 1032201, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["…夜光？"], [3, 0, 1032201, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [4, 6000]);
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/floodEffect/0", [5400, 0, 0, 1, 0, -5]);
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/BalloonMsg1/1", [1400, 0, -120, 0, 0]);
        ms.updateNPCSpecialAction(5453445, -1, 50, 100);
        ms.getDirectionEffect(1, "", [500]);
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial2/Scene2");
        ms.playSoundEffect("Bgm26.img/Flood");
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/BalloonMsg1/2", [0, 0, -120, 1, 1, 0, 5453445, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/BalloonMsg1/3", [0, 0, -180, 0, 0]);
        ms.getDirectionEffect(1, "", [2300]);
    } else if (status === i++) {
        ms.getDirectionEffect(10, "", [21066]);
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/floodEffect/1", [0, 0, 0, 1, 0, -5]);
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/floodEffect/2", [0, 0, 0, 1, 0, -6]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.getDirectionStatus(true);
        ms.removeNPCRequestController(5453445);
        ms.dispose();
        ms.warp(910141060, 0);
    } else {
        ms.dispose();
    }
}
