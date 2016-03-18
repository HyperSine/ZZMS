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
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.getDirectionEffect(3, "", [0]);
        ms.disableOthers(true);
        ms.getDirectionEffect(1, "", [3000]);
        ms.getDirectionStatus(true);
        ms.showDarkEffect(false);
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/25", [1, 1, 1, 0, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["怎麼回事?"], [3, 0, 9390381, 0, 3, 0, 0, 1, 0]);
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/25", [1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["足足睡了3天. 看來想使用我的力量還是太勉強了"], [3, 0, 9390381, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        if (!ms.isQuestFinished(59017)) {
            ms.forceStartQuest(59017);
        }
        ms.lockUI(0);
        ms.disableOthers(false);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
