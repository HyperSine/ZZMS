/* global pi */

function enter(pi) {
    if (pi.getQuestStatus(40004) === 0) {
        pi.openNpc("zeroProl1000_0");
    } else {
        pi.warp(321001100, 0);
    }
}