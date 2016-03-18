var status = -1;

function start(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.sendNext("連長老的純白淨化都行不通的原因怎麼想也都只有一個。 請鎮定的聽我說，精靈遊俠。 #r黑魔法師的封印變弱或者已經從封印中解開#k 了。");
    } else {
        qm.forceCompleteQuest(24066);
        qm.gainExp(2000);
        qm.dispose();
    }
}
function end(mode, type, selection) {
	qm.dispose();
}
