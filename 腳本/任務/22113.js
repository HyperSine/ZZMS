/* global qm */

function start(mode, type, selection) {
    if (qm.getLevel() >= 25 && qm.getJob() >= 2201 && qm.getJob() <= 2218) {
        qm.forceCompleteQuest();
        qm.gainSp(3, 1);
    }
    qm.dispose();
}