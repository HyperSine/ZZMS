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
        var level = 10 - ms.getLevel();
        for (var i = 0 ; i < level ; i++) {
            ms.levelUp();
        }
        ms.teachSkill(60001229, -1, 0); // 氣焰隱忍
        ms.getDirectionEffect(1, "", [900]);
        ms.getDirectionStatus(true);
        ms.gainItem(1142484, 1);
    } else if (status === i++) {
        ms.showEnvironment(13, "kaiser/text0", [0]);
        ms.getDirectionEffect(1, "", [4200]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(940001200, 0);
    } else {
        ms.dispose();
    }
}
