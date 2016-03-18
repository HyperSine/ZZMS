/* global pi */

function enter(pi) {
    if (pi.getQuestStatus(40004) === 2) {
        pi.warp(321001200, 0);
    }
}
