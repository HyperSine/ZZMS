/* global qm */

function start(mode, type, selection) {
    if (qm.getLevel() >= 55 && qm.getJob() >= 2204 && qm.getJob() <= 2218) {
        qm.forceCompleteQuest();
        qm.gainSp(3, 4);
    }
    qm.dispose();
}