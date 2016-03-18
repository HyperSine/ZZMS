/* global qm */

var status = -1;

function start(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.sendYesNo("(啟動音樂盒讓音樂輕瀉而出。)");
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.sendOk("(在和激動的心情非常不搭調的平靜音樂撫慰下。希望孩子們能做個美夢…)");
        qm.playSoundEffect("Game/QueenOfElf");
        qm.getDirectionStatus(true);
        qm.dispose();
    } else {
        qm.dispose();
    }
}
