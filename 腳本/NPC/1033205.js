var status = -1;

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (cm.isQuestActive(24040)) {
        if (status <= i++) {
            cm.dispose();
        } else if (status === i++) {
            cm.lockUI(true);
            cm.disableOthers(true);
            cm.sendPlayerToNpc("等等！結界解開了？");
        } else if (status === i++) {
            cm.sendPlayerToNpc("看樣子已經過去100多年了。外面變成了什麼樣子了呢？在出去之前先檢查檢查吧……");
        } else if (status === i++) {
            cm.wait(3000);
        } else if (status === i++) {
            cm.showWZEffect("Effect/Direction5.img/mersedesQuest/Scene2");
            cm.showWZEffectNew("Effect/OnUserEff.img/questEffect/mercedes/q24040");
            cm.wait(6000);
        } else if (status === i++) {
            cm.sendPlayerToNpc("我的等級……只有10級了！");
        } else {
            cm.lockUI(false);
            cm.disableOthers(false);
            cm.forceStartQuest(24093, "1");  //开启24093任务，以触发24040任务的完成
            cm.dispose();
        }
    } else {
        cm.dispose();
    }
}