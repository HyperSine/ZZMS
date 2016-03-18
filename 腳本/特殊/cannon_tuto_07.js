/* global cm */
var status = -1;

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (status <= i++) {
        cm.dispose();
    } else if (status === i++) {
        cm.getDirectionStatus(true);
        cm.lockUI(1, 1);
        cm.disableOthers(true);
        cm.spawnNPCRequestController(1096012, -51, -97, 0, 9742420);
        cm.getDirectionEffect(3, "", [0]);
        cm.getDirectionEffect(3, "", [2]);
        cm.getNPCTalk(["來現在要出發了。"], [3, 0, 1096005, 0, 1, 0, 0, 1, 0]);
        cm.dispose();
    } else if (status === i++) {
        cm.forceStartQuest(2572);
        cm.removeNPCRequestController(9742420);
        cm.showEnvironment(5, "cannonshooter/fire", []);
        cm.getDirectionEffect(2, "Effect/Direction4.img/effect/cannonshooter/flying/0", [7000, 0, 0, 0, 0]);
        cm.getDirectionEffect(2, "Effect/Direction4.img/effect/cannonshooter/flying1/0", [7000, 0, 0, 0, 0]);
        cm.getDirectionEffect(1, "", [800]);
        cm.dispose();
    } else {
        cm.dispose();
    }
}