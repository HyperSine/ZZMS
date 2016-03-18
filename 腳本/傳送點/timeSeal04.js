/* global pi */

function enter(pi) {
    if (pi.isQuestActive(25670) && pi.isQuestActive(25671) && pi.isQuestActive(25672) && pi.isQuestActive(25673) && !pi.isQuestActive(25674)) {
        pi.showEnvironment(13, "lightning/screenMsg/6", [0]);
        pi.forceStartQuest(25674, "1");
    }
    pi.openNpc("timeSeal");
}
