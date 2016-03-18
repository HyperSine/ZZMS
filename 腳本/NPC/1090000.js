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
        if (cm.getMapId() === 743000600) {
            cm.getNPCTalk(["歡迎來到鯨魚號。你沒打算前去航海，這裡沒什麼好參觀的。到甲板上去看看如何？"], [4, 1090000, 0, 0, 0, 0, 0, 0, 0]);
        }
        cm.dispose();
    } else {
        cm.dispose();
    }
}
