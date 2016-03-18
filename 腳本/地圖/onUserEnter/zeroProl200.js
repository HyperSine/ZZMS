/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    if (ms.getQuestStatus(40001) === 2) {
        ms.dispose();
        return;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.spawnNPCRequestController(2410003, -200, -20, 0, 11585230);
        ms.dispose();
    } else {
        ms.dispose();
    }
}