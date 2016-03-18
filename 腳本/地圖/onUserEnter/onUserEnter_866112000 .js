/* global ms */

var status = -1;

function action(mode, type, selection) {
    ms.dispose();
    if (ms.getQuestStatus(59013) < 2) {
        ms.resetMap(ms.getMapId());
	ms.spawnMob(9390928, 461, 28);
	ms.spawnMob(9390928, 561, 28);
	ms.spawnMob(9390928, 661, 28);
	ms.spawnMob(9390928, 761, 28);
	ms.spawnMob(9390928, 861, 28);
        if (ms.getQuestStatus(59013) < 1) {
            ms.startQuest(59013, 9390303);
        }
    }
}