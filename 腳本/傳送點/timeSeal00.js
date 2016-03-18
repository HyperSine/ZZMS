/* global pi */

function enter(pi) {
    if (!pi.isQuestActive(25670)) {
        pi.showEnvironment(13, "lightning/screenMsg/6", [0]);
        pi.forceStartQuest(25670, "1");
    }
    pi.openNpc("timeSeal");
}