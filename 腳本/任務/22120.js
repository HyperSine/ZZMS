/* global qm */

function start(mode, type, selection) {
    if (qm.getLevel() >= 60 && qm.getJob() >= 2205 && qm.getJob() <= 2218) {
        qm.forceCompleteQuest();
    }
    qm.dispose();
}