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
        ms.resetMap(ms.getMapId());
        ms.spawnMob(9390937, 515, 77);
        ms.spawnMob(9390935, 278, 70);
        ms.getDirectionStatus(true);
        ms.lockUI(true);
        ms.getDirectionEffect(3, "", [0]);
        ms.disableOthers(true);
        ms.sendOthersTalk("喂!將來的楓之谷英雄!!, 到了大人們也害怕的野狼森林!!", 9390300, [false, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("嗯?那個是什麼??? 好像有什麼東西...去看看吧.!!", 9390300, [true, true], 3);
    } else if (status === i++) {
        ms.lockUI(false);
        ms.disableOthers(false);
        ms.forceCompleteQuest(59002);
        ms.dispose();
    } else {
        ms.dispose();
    }
}