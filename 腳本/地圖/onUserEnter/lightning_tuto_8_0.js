/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.lockUI(1, 1);
        ms.getDirectionEffect(1, "", [7200]);
        ms.getDirectionStatus(true);
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial/Scene2");
        ms.playVoiceEffect("Voice.img/DarkMage/5");
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(910141000, 0);
    } else {
        ms.dispose();
    }
}
