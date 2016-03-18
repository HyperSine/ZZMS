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
        ms.getDirectionEffect(9, "", [1]);
        ms.teachSkill(30021238, 1, 1);
        ms.getDirectionEffect(1, "", [100]);
        ms.forceStartQuest(7292, "0");
        ms.forceStartQuest(14431);
        ms.forceStartQuest(-32347);
        ms.forceStartQuest(-32346);
        ms.forceStartQuest(-32293);
        ms.forceStartQuest(-32271);
        ms.forceStartQuest(-32269);
        ms.forceStartQuest(-26190);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [6300]);
        ms.showWZEffect("Effect/Direction12.img/XenonTutorial/SceneLogo");
    } else if (status === i++) {
        ms.dispose();
        ms.warp(931050900, 0);
    } else {
        ms.dispose();
    }
}
