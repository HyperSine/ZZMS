/* global qm */

function start(mode, type, selection) {
    if (qm.getLevel() >= 35 && qm.getJob() >= 2202 && qm.getJob() <= 2218) {
        qm.forceCompleteQuest();
        qm.gainSp(3, 2);
    }
    qm.dispose();
}