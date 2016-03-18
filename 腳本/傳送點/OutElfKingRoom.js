function enter(pi) {
    if (!pi.isQuestFinished(24005)) {
        pi.getPlayer().dropMessage(5, "現在還不能出去。");
    } else {
        pi.warp(101050000, 7);
    }
    
}