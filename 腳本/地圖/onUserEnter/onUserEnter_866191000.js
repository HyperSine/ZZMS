/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.lockUI(0);
        if (ms.isQuestFinished(59021)) {
            ms.dispose();
            return;
        }
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.getDirectionEffect(3, "", [0]);
        ms.disableOthers(true);
        ms.lockUI(0);
        ms.disableOthers(false);
        ms.teachSkill(110001506, 1, 1);
        ms.teachSkill(110001514, 1, 1);
        ms.getNPCTalk(["可以使用可愛的波波力量~!"], [3, 0, 0, 0, 5, 0, 0, 1, 0, 9390301]);
        ms.showDarkEffect(false);
    } else if (status === i++) {
        ms.getNPCTalk(["萊伊的力量也是一樣! BOSS."], [3, 0, 0, 0, 5, 0, 1, 1, 0, 9390302]);
    } else if (status === i++) {
        ms.getNPCTalk(["艾卡和阿樂走的時候給了你新的力量. 確認技能視窗裡我們倆的資訊吧.Bo~~SS!"], [3, 0, 0, 0, 5, 0, 1, 1, 0, 9390302]);
    } else if (status === i++) {
        ms.openUIOption(532);
        ms.topMsg("透過朋友們的力量,開放了守護者的身手和重回樹木村莊.");
        ms.forceCompleteQuest(59021);
        var level = ms.getLevel();
        for (var i = 0 ; i < 10 - level ; i++) {
            ms.levelUp();
        }
        ms.dispose();
    } else {
        ms.dispose();
    }
}
