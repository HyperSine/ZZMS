/* global pi */

function enter(pi) {
    if (pi.getQuestStatus(40003) !== 0) {
        pi.warp(321000700, 3);
    } else {
        pi.warp(321000100);
    }
}
