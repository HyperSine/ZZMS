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
        ms.getDirectionEffect(9, "", [1]);
        ms.getDirectionEffect(1, "", [900]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.showEnvironment(13, "xenon/text1", [0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.showEnvironment(13, "xenon/text2", [0]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.showEnvironment(13, "xenon/text3", [0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.showEnvironment(13, "xenon/text4", [0]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.showEnvironment(13, "xenon/text5", [0]);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.showEnvironment(13, "xenon/text6", [0]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.showEnvironment(13, "xenon/text7", [0]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.showEnvironment(13, "xenon/text8", [0]);
        ms.getDirectionEffect(1, "", [4000]);
    } else if (status === i++) {
        ms.dispose();
        ms.warp(931050930, 0);
    } else {
        ms.dispose();
    }
}
