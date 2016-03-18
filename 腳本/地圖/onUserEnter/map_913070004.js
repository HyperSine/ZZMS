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
        ms.disableOthers(true);
        ms.getTopMsg("林伯特的雜貨商店");
        ms.getTopMsg("楓之谷曆XXXX年 3月11日");
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [1000]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction7.img/effect/tuto/step0/5", [2000, 0, -100, 1, 0, -100]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(2, "Effect/Direction7.img/effect/tuto/step0/6", [2000, 0, -100, 1, 0, -100]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction7.img/effect/tuto/step0/4", [2000, 0, -100, 1, 0, -100]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction7.img/effect/tuto/step0/7", [2000, 0, -100, 1, 0, -100]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction7.img/effect/tuto/step0/8", [2000, 0, -100, 1, 0, -100]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getTopMsg("後院好像有人。 去後院看一看。");
        ms.lockUI(0);
        ms.disableOthers(false);
        ms.forceCompleteQuest(20034);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
