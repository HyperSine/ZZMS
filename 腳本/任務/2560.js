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
        qm.getNPCTalk(["咭咭咭咭咭！咭！咭！"], [4, 1096003, 0, 0, 0, 0, 0, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["找到船後精神還是很差…到底是什麼情況？一張開眼睛就看到猴子，也不知道這裡到底是那裡…船變成怎樣了？你知道到底是什麼情況嗎？"], [4, 1096003, 0, 0, 2, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["咭咭，咭咭！(猴子點點頭，這傢伙真的知道情況嗎？向猴子仔細問清楚！)"], [4, 1096003, 0, 15, 0, 0]);
    } else if (status === i++) {
	qm.forceCompleteQuest();
        qm.dispose();
    } else {
        qm.dispose();
    }
}