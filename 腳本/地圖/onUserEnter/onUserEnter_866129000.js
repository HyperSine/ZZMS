/* global ms */

var status = -1;

function action(mode, type, selection) {
    ms.dispose();
    if (ms.getQuestStatus(59016) < 2) {
        ms.resetMap(ms.getMapId());
	ms.spawnMob(9390927, 1096, 28);
	ms.spawnMob(9390927, 1028, 28);
	ms.spawnMob(9390928, 1299, 28);
	ms.spawnMob(9390928, 1201, 28);
	ms.spawnMob(9390929, 981, 28);
	ms.spawnMob(9390929, 1099, 28);
	ms.spawnMob(9390930, 900, 28);
        if (ms.getQuestStatus(59016) < 1) {
            ms.startQuest(59016, 9390302);
        }
    }
}