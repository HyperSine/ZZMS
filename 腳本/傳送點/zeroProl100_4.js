/* global pi */

function enter(pi) {
    if (pi.getQuestStatus(40000) === 2) {
        pi.warp(321000200, 2);
    }
}
