/* global qm */

function start(mode, type, selection) {
    if (qm.getLevel() >= 30 && qm.getJob() >= 2202 && qm.getJob() <= 2218) {
        qm.forceCompleteQuest();
        qm.gainSp(2, 2);
    }
    qm.dispose();
}