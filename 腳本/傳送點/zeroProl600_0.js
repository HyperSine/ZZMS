/* global pi */

function enter(pi) {
    if (pi.getQuestStatus(40003) !== 0) {
        pi.warp(321000500, 2);
    }
}
