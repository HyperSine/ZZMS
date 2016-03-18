var status = -1;

function start(mode, type, selection) {
	if (mode === 1) {
        status++;
    } else {
        status--;
    }
    
    var i = -1;
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.sendNext("精靈遊俠！(很熟悉的聲音…)");
    } else if (status === i++) {
        qm.sendPlayerToNpc("啊！亞斯提那也醒過來了。");
    } else if (status === i++) {
        qm.sendPlayerToNpc("太好了…大家都醒過來了…");
    } else {
        qm.forceCompleteQuest(24064);
        qm.gainExp(2000);
        qm.dispose();
    }
}
function end(mode, type, selection) {
	qm.dispose();
}
