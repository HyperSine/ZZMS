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
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [1500]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["這次是那邊嗎？"], [3, 0, 9330204, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
