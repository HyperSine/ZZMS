/*
 Made by Pungin
 */

var status = -1;

function action(mode, type, selection) {
    status++;

    if (status == 0) {
        ms.getDirectionStatus(true);
        ms.lockUI(true);
        ms.playMovie("adventurer.avi");
    } else if (status == 1) {
        ms.getDirectionStatus(true);
        ms.lockUI(false);
        ms.dispose();
        ms.warp(4000005, 0);
    } else {
        ms.dispose();
    }
}
	