/* global ms */

function action(mode, type, selection) {
    ms.resetMap(ms.getMapId());
    var box = Math.floor(Math.random() * 5);
    var i = 0;
    ms.spawnMob(box === i++ ? 9300521 : 9300522, -578, 152);
    ms.spawnMob(box === i++ ? 9300521 : 9300522, -428, 152);
    ms.spawnMob(box === i++ ? 9300521 : 9300522, -278, 152);
    ms.spawnMob(box === i++ ? 9300521 : 9300522, -128, 152);
    ms.spawnMob(box === i++ ? 9300521 : 9300522, 22, 152);
    ms.spawnMob(box === i++ ? 9300521 : 9300522, 172, 152);
    ms.dispose();
}