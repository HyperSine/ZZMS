/* global pi */

function enter(pi) {
    if (pi.getQuestStatus(40002) !== 0) {
        pi.warp(321000400, 0);
    }
}
