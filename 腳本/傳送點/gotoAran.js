/* global pi */

function enter(pi) {
    if (pi.getMap().getAllMonstersThreadsafe().size() === 0) {
        pi.teachSkill(20041226, -1, 0);
        pi.warp(927020050, 0);
    }
}
