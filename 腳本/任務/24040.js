var status = -1;

function end(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (status <= i++) {
        cm.dispose();
    } else if (status === i++) {
        qm.sendNextS("這就是對我的詛咒嗎？這不是真的，這一定是場夢……", 2);
        qm.forceCompleteQuest();
    } else {
        qm.forceCompleteQuest(29952);
        qm.gainItem(1142336,1);
        qm.dispose();
	}
}
