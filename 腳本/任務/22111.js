/* global qm */

function start(mode, type, selection) {
    if (qm.getLevel() >= 15 && qm.getJob() >= 2200 && qm.getJob() <= 2218) {
        qm.forceCompleteQuest();
        qm.gainSp(3, 0);
    }
    qm.dispose();
}