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
        qm.sendNext("精靈遊俠…！(很熟悉的聲音…)");
    } else if (status === i++) {
        qm.sendNext("精靈遊俠…！(這聲音是…)…？！");
    } else if (status === i++) {
        qm.sendNext("王！ 原來你平安無事… 還好臉色看起來不錯。 哈… 現在稍微安心了…\r\n你看起來很疲憊。 還好嗎？");
    } else if (status === i++) {
        qm.sendPlayerToNpc("嗯，沒事…能看到你醒來真是太好了。");
    } else {
        qm.forceCompleteQuest(24063);
        qm.gainExp(2000);
        qm.dispose();
    }
}
function end(mode, type, selection) {
	qm.dispose();
}
