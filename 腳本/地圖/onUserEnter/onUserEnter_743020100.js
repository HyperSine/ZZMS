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
        ms.lockUI(1, 1);
        ms.teachSkill(5081023, -1, 0);
        ms.teachSkill(5080022, -1, 0);
        ms.teachSkill(5081021, -1, 0);
        ms.teachSkill(5081003, -1, 0);
        ms.teachSkill(5081002, -1, 0);
        ms.teachSkill(5081020, -1, 0);
        ms.showWZEffect("Effect/Direction100.img/TimeLine/timeLine0");
        ms.dispose();
    } else {
        ms.dispose();
    }
}
