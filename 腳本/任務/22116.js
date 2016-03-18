/* global qm */

function start(mode, type, selection) {
    if (qm.getLevel() >= 40 && qm.getJob() >= 2203 && qm.getJob() <= 2218) {
        qm.forceCompleteQuest();
        qm.gainSp(2, 3);
    }
    qm.dispose();
}