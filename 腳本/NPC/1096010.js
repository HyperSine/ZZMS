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
	if (cm.isQuestActive(2566) && !cm.haveItem(4032985,1)) {
            cm.getTopMsg("發現點火裝置，帶給斯托納。");
            cm.gainItem(4032985, 1);
	}
        cm.dispose();
    } else {
        cm.dispose();
    }
}