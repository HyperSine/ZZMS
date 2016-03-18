var status = -1;

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (cm.isQuestActive(24058) && !cm.haveItem(4032963)) {
        if (status <= i++) {
            cm.dispose();
        } else if (status === i++) {
            cm.sendPlayerToNpc("怪物圖鑒……我找找……");
        } else if (status === i++) {
            cm.sendPlayerToNpc("啊，找到了。不過上面滿是厚厚的灰了。");
        } else if (status === i++) {
            cm.sendPlayerToNpc("事不宜遲，趕緊拿過去吧。");
        } else {
            cm.gainItem(4032963, 1);
            cm.dispose();
        }
    } else {
        cm.dispose();
    }
}