var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        qm.forceStartQuest(24041, "1");
        qm.sendPlayerToNpc("不，冷靜！現在好好想想到底發生了什麼。");
    } else if (status == 1) {
        qm.sendPlayerToNpc("其他的精靈還在冰里，所以黑魔法師的詛咒仍然還在起作用！");
    } else if (status == 2) {
        qm.sendPlayerToNpc("我是唯一一個醒來的，我不知道為什麼，但我感覺黑魔法師的詛咒在減弱。");
    } else if (status == 3) {
        qm.sendPlayerToNpc("我現在必須出去，我要到外面去看看世界成什麼樣了。可是我現在只有10等級。真不敢相信……詛咒的力量究竟有多大？");
    } else {
        qm.dispose();
    }
}

function end(mode, type, selection) {
	qm.completeQuest(24041);
	qm.dispose();
}
