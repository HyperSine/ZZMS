/* global qm */

function start(mode, type, selection) {
    if (qm.getLevel() >= 20 && qm.getJob() >= 2201 && qm.getJob() <= 2218) {
        qm.forceCompleteQuest();
        qm.gainSp(2, 1);
    }
    qm.dispose();
}