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
        ms.getDirectionFacialExpression(1, 10000);
        ms.getNPCTalk(["這樣跑出來了，可是父親在哪裡？"], [3, 0, 9330201, 0, 3, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["好奇怪喔！那是什麼？怎麼會有這麼醜陋的東西出現？"], [3, 0, 9330201, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/balloonMsg9/1", [2000, 2000, -200, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getNPCTalk(["那個聲音是!父親，是父親的聲音!"], [3, 0, 9330201, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
