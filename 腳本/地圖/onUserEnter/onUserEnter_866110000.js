/* global ms */

var status = -1;

function action(mode, type, selection) {
    ms.dispose();
    if (ms.getQuestStatus(59009) < 2) {
        ms.resetMap(ms.getMapId());
	ms.spawnMob(9390927, 461, 28);
	ms.spawnMob(9390927, 561, 28);
	ms.spawnMob(9390927, 661, 28);
	ms.spawnMob(9390927, 761, 28);
	ms.spawnMob(9390927, 861, 28);
        if (ms.getQuestStatus(59009) < 1) {
            ms.startQuest(59009, 9390302);
        }
    }
}