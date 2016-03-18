/* global qm */
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
        qm.getNPCTalk(["來啦？在你來報告的時候，在大砲上已經安裝點火裝置了，來，不需要帶等待！現在馬上就出發！"], [4, 1096005, 0, 15, 0, 0]);
    } else if (status === i++) {
	qm.forceCompleteQuest();
        qm.dispose();
        qm.warp(912060200, 0);
    } else {
        qm.dispose();
    }
}
