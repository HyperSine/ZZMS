var status = -1;

function start(mode, sttype, selection) {
    qm.forceStartQuest(24097, 1022006, "1");
	qm.gainItem(4033075, 1);
	qm.dispose();
}

function end(mode, sttype, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }
    
    var i = -1;
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.sendNext("這是變異菇菇的血…");
    } else if (status === i++) {
        qm.sendNext("…什麼？！ 溫斯頓說我有東西要給他嗎？ …我沒有東西要給他呀。 那就幫他提升名聲吧。");
    } else {
        qm.removeItem(4033075);
        qm.getPlayer().gainHonor(3, true);
        qm.forceCompleteQuest(24097);
        qm.dispose();
    }
}