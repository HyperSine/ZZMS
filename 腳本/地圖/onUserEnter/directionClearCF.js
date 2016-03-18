/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        if (!ms.isQuestActive(52400) && !ms.isQuestFinished(52400)) {
            ms.forceStartQuest(52400);
        }
        if (!ms.isQuestFinished(52401) && ms.isQuestFinished(52400)) {
            ms.forceCompleteQuest(52401);
        }
        if (!ms.isQuestActive(52402) && !ms.isQuestFinished(52402) && ms.isQuestFinished(52400)) {
            ms.forceStartQuest(52402);
        }
        if (!ms.isQuestActive(52403) && !ms.isQuestFinished(52403) && ms.isQuestFinished(52402)) {
            ms.forceStartQuest(52403);
        }
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
