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
        qm.sendNext("偉大的精靈遊俠啊…");
    } else if (status === i++) {
        qm.sendNext("之後的道路將會非常艱辛。");
    } else if (status === i++) {
        qm.sendNext("但不要害怕，我將永遠在你身邊…");
    } else {
        qm.forceCompleteQuest(29953);
        qm.gainItem(1142337,1);
        qm.dispose();
    }
    
}

function end(mode, type, selection) {
    
}