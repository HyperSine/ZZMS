/* global pi */

function enter(pi) {
    if (!pi.isQuestActive(25673)) {
        pi.showEnvironment(13, "lightning/screenMsg/6", [0]);
        pi.forceStartQuest(25673, "1");
    }
    pi.openNpc("timeSeal");
}