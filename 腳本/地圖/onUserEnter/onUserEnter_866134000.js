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
        ms.disableOthers(true);
        ms.spawnNPCRequestController(9390382, -707, -650, 0, 1973518);
        ms.spawnNPCRequestController(9390383, -641, -550, 0, 1973519);
        ms.spawnNPCRequestController(9390384, -936, -550, 1, 1973520);
        ms.getNPCTalk(["啊, 怎麼辦? 我們也快點下去幫忙吧!"], [3, 0, 9390381, 0, 3, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
        ms.showDarkEffect(false);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390381, -776, -484, 0, 1974239);
        ms.setNPCSpecialAction(84433, "attack1", 0, false);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["不需要啦!"], [3, 0, 9390381, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["啊, 阿樂!!!什麼時候來的啊?"], [3, 0, 9390381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["現在來的啊喵.\r\n比起那個, 沒時間了啦!快點和我簽約."], [3, 0, 9390381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(1974239, "special1", 0, true);
        ms.getDirectionEffect(0, "", [1005, 540]);
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/14", [2000, 0, 0, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [750]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(1974239, "special4", 0, true);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["拍, 拍, 拍!!"], [3, 0, 9390381, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["好,那集合村莊裡的人們去除掉怪物吧!"], [3, 0, 9390381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["貓咪的小聰明和力量湧現吧喵!"], [3, 0, 9390381, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/3", [3850, 0, 0, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [50]);
    } else if (status === i++) {
        ms.removeNPCRequestController(1974239);
        ms.getDirectionEffect(1, "", [450]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390381, -776, -484, 0, 1975435);
        ms.getDirectionEffect(1, "", [2200]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 1000, -676, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1032]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/4", [1190, 380, 480, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [400]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/4", [1190, 300, 480, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/4", [1190, 220, 480, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/4", [1190, 120, 480, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getNPCTalk(["再一次!!!!!"], [3, 0, 9390381, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/5", [1540, 0, 0, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [40]);
    } else if (status === i++) {
        ms.removeNPCRequestController(1975435);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 1000, -676, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1004]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/6", [2200, 260, 480, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [2500]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390381, -776, -484, 0, 1976235);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["這怎會變成這樣?"], [3, 0, 9390307, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["好像有三十年沒有感覺到這種無法承受的力量了!"], [3, 0, 9390308, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那就來打一場吧?!!!"], [3, 0, 9390304, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.showBlackBGEffect(1000, 3500, 1000, -1);
        ms.getDirectionEffect(1, "", [1000]);
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/16", [1, 1, 1, 0, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(9, "", [1]);
        ms.getDirectionEffect(1, "", [2500]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 1000]);
    } else if (status === i++) {
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/16", [1, 0]);
        ms.getDirectionEffect(9, "", [0]);
        ms.getNPCTalk(["嗚哇! 村莊的人們變的超強的!!! \r\n 啊, 那個羅莎那奶奶抓著怪物一圈一圈甩來甩去!!"], [3, 0, 9390304, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["喵嗚! 這就是我的力量喵嗚!!"], [3, 0, 9390381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["呃嗯, 了不..起... 但是... 為..什麼這麼... 睏?"], [3, 0, 9390381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionFacialExpression(3, 10000);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [25, 3000]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/10", [2000, 169, -260, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/21", [2000, 100, -120, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [100]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/10", [2000, 235, -220, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/21", [2000, -60, -220, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.removeNPCRequestController(1973518);
        ms.removeNPCRequestController(1973519);
        ms.removeNPCRequestController(1973520);
        ms.removeNPCRequestController(1976235);
        ms.lockUI(0);
        ms.disableOthers(false);
        ms.dispose();
        ms.warp(866135000, 0);
    } else {
        ms.dispose();
    }
}
