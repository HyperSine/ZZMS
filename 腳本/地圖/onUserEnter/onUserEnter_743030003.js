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
        ms.spawnNPCRequestController(9330204, 650, 3, 0, 8508813);
        ms.getDirectionEffect(2, "Effect/Direction5.img/mercedesInIce/merBalloon/5", [500, 0, -100, 0, 0]);
        ms.getDirectionEffect(1, "", [500]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getNPCTalk(["耶願!!!!"], [3, 0, 9330204, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["少爺"], [3, 0, 9330204, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.setNPCSpecialAction(8508813, "move", 0, true);
        ms.updateNPCSpecialAction(8508813, -1, 250, 100);
    } else if (status === i++) {
        ms.getNPCTalk(["耶願!為什麼這麼晚了呢?父親在哪裡?"], [3, 0, 9330204, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["吼吼，少爺.你必須要快點閃避。"], [3, 0, 9330204, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["有什麼事呢！我做了惡夢心煩意亂的。這麼急迫的表情。汗…"], [3, 0, 9330204, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getNPCTalk(["難道是血？哪裡受傷了？父親，父親在哪裡？"], [3, 0, 9330204, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["從現在起好好聽我說。快點在這裡躲好。"], [3, 0, 9330204, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["不，父親。我要找父親。雖然不曉得有什麼問題，"], [3, 0, 9330204, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["可是不是這樣的情況。現在不能從正門走。快點從後門…"], [3, 0, 9330204, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那麼父親應該會在大門!"], [3, 0, 9330204, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionFacialExpression(5, 10000);
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8508813, "move", 0, true);
        ms.updateNPCSpecialAction(8508813, 1, 150, 100);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["不行的!!"], [3, 0, 9330204, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["住手!我要先去找父親!!"], [3, 0, 9330204, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(8508813);
        ms.dispose();
        ms.warp(743000100, 0);
    } else {
        ms.dispose();
    }
}
