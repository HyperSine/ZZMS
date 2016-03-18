/* global qm */
var status = -1;

function end(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.getDirectionStatus(true);
        qm.lockUI(1, 1);
        qm.getNPCTalk(["天啊，已經結束了嗎？"], [3, 0, 9330203, 0, 9, 0, 0, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["不，不要這樣看我？不會花很...很久的時間。"], [3, 0, 9330203, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["我不是已經說過了嗎？少爺"], [3, 0, 9330203, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["真...真的嗎？"], [3, 0, 9330203, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["哼哼。"], [3, 0, 9330203, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getDirectionFacialExpression(6, 8000);
        qm.getNPCTalk(["為，為什麼這樣笑？今天耶願有點奇怪。突然生氣，然後又這樣笑咪咪的…？這樣…？"], [3, 0, 9330203, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["少爺剛才對你兇的是...開玩笑的啦"], [3, 0, 9330203, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["實際上你父親拜託我，如果今天認真的修煉，就會給你好東西。"], [3, 0, 9330203, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["好，好了？那是什麼？ (是類似生日禮物嗎？)"], [3, 0, 9330203, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["你猜猜看這是什麼東西呢？"], [3, 0, 9330203, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["耶願。不要鬧了。真的很想知道。"], [3, 0, 9330203, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["好，這裡，好了嗎!"], [3, 0, 9330203, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.lockUI(0);
        qm.dispose();
        qm.warp(743020200, 0);
    } else {
        qm.dispose();
    }
}
