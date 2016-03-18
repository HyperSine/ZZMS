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
        ms.spawnNPCRequestController(3000107, -2950, 20, 1, 8364295);
        ms.getNPCTalk(["凱撒，還是不行了。跟我一起到萬神殿吧，那裡除了我以外還有很多超新星的人。"], [3, 0, 3000107, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 300, -2150, 20]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [356]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(8364295, 1, 200, 100);
        ms.getDirectionEffect(1, "", [416]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/tuto/BalloonMsg1/1", [0, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 600, -1200, 29]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [1553]);
    } else if (status === i++) {
        ms.spawnMob(9300545, -1600, 29);
        ms.getDirectionEffect(1, "", [150]);
    } else if (status === i++) {
        ms.spawnMob(9300546, -1500, 29);
        ms.getDirectionEffect(1, "", [150]);
    } else if (status === i++) {
        ms.spawnMob(9300545, -1400, 29);
        ms.getDirectionEffect(1, "", [150]);
    } else if (status === i++) {
        ms.spawnMob(9300546, -1300, 29);
        ms.getDirectionEffect(1, "", [150]);
    } else if (status === i++) {
        ms.spawnMob(9300545, -1200, 29);
        ms.getDirectionEffect(1, "", [150]);
    } else if (status === i++) {
        ms.spawnMob(9300546, -1100, 29);
        ms.getDirectionEffect(1, "", [150]);
    } else if (status === i++) {
        ms.spawnMob(9300545, -1000, 29);
        ms.getDirectionEffect(1, "", [150]);
    } else if (status === i++) {
        ms.spawnMob(9300546, -900, 29);
        ms.getDirectionEffect(1, "", [150]);
    } else if (status === i++) {
        ms.spawnMob(9300546, -800, 29);
        ms.getDirectionEffect(5, "", [1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [0]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.showEnvironment(13, "lightning/screenMsg/0", [0]);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
