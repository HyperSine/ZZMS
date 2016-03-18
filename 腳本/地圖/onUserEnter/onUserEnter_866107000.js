/* global ms */

var status = -1;

function action(mode, type, selection) {
    if (ms.getQuestStatus(59005) !== 2 ) {
        ms.resetMap(ms.getMapId());
        ms.spawnMob(9390931, 661, 246);
        ms.spawnMob(9390931, 761, 246);
        ms.spawnMob(9390931, 861, 246);
        ms.spawnMob(9390931, 961, 246);
        ms.spawnMob(9390931, 1061, 246);
    }
    ms.dispose();
}