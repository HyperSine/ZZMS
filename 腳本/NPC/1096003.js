/* global cm */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        cm.dispose();
    } else if (status === i++) {
        cm.getDirectionStatus(true);
        cm.lockUI(0);
        cm.getDirectionStatus(true);
        cm.lockUI(1, 1);
        cm.getDirectionEffect(3, "", [4]);
        cm.getNPCTalk(["咭咭~~咭咭~~？？ "], [3, 0, 1096003, 0, 1, 0, 0, 1, 0]);
        cm.showWZEffect("Effect/Direction4.img/cannonshooter/face00");
    } else if (status === i++) {
        cm.lockUI(0);
        cm.disableOthers(false);
        cm.dispose();
    } else {
        cm.dispose();
    }
}