/* global qm */

function start(mode, type, selection) {
    if (qm.getLevel() >= 50 && qm.getJob() >= 2204 && qm.getJob() <= 2218) {
        qm.forceCompleteQuest();
        qm.gainSp(2, 4);
    }
    qm.dispose();
}