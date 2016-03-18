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
        ms.getDirectionEffect(1, "", [300]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.showEnvironment(13, "kaiser/text0", [0]);
        ms.getDirectionEffect(1, "", [4200]);
    } else if (status === i++) {
        var level = 10 - ms.getLevel();
        for (var i = 0 ; i < level ; i++) {
            ms.levelUp();
        }
        ms.lockUI(0);
        ms.dispose();
        ms.warp(940011020, 0);
    } else {
        ms.dispose();
    }
}
