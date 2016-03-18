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
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/BalloonMsg0/2", [0, 0, -120, 0, 0]);
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [700]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/BalloonMsg0/3", [0, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction8.img/effect/tuto/BalloonMsg0/4", [0, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.teachSkill(20041226, 1, 1);
        ms.spawnMob(9300529, 800, 69);
        ms.spawnMob(9300529, 700, 69);
        ms.spawnMob(9300529, 600, 69);
        ms.spawnMob(9300529, 500, 69);
        ms.spawnMob(9300529, 400, 69);
        ms.spawnMob(9300529, 300, 69);
        ms.spawnMob(9300529, 200, 69);
        ms.spawnMob(9300529, 100, 69);
        ms.spawnMob(9300530, 750, 69);
        ms.spawnMob(9300530, 650, 69);
        ms.spawnMob(9300530, 550, 69);
        ms.spawnMob(9300530, 450, 69);
        ms.spawnMob(9300530, 350, 69);
        ms.spawnMob(9300530, 250, 69);
        ms.spawnMob(9300530, 150, 69);
        ms.lockUI(0);
        ms.showEnvironment(13, "lightning/screenMsg/0", [0]);
        ms.spawnMob(9300529, 1304, 69);
        ms.spawnMob(9300529, 1272, 69);
        ms.spawnMob(9300529, 1226, 69);
        ms.spawnMob(9300529, 1159, 69);
        ms.spawnMob(9300529, 1097, 69);
        ms.spawnMob(9300529, 1196, 69);
        ms.spawnMob(9300529, 802, 69);
        ms.spawnMob(9300529, 1083, 69);
        ms.spawnMob(9300529, 963, 69);
        ms.spawnMob(9300529, 839, 69);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
