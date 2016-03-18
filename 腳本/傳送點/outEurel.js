function enter(pi) {
    if (pi.isQuestFinished(24002) && !pi.isQuestActive(24093)) {
        pi.openNpc(1033205);
    } else if ((!pi.isQuestFinished(24040) || 
                !pi.isQuestFinished(24041) ||
                !pi.isQuestFinished(24042) ||
                !pi.isQuestFinished(24043) ||
                !pi.isQuestFinished(24044)) &&
                !pi.isQuestActive(24045)) {
        pi.getPlayer().dropMessage(5, "現在還不能出去。");
    } else {
        pi.warp(101050100,0);
    }
}