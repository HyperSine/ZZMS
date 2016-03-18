/* global qm */

function start(mode, type, selection) {
    if (qm.getLevel() >= 10 && qm.getJob() >= 2200 && qm.getJob() <= 2218) {
        qm.forceCompleteQuest();
        qm.gainSp(2, 0);
    }
    qm.dispose();
}