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
        ms.getNPCTalk(["看來普力特和精靈遊俠已經回去的樣子。希望不會太遲。"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [750]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
        ms.showWZEffectNew("Effect/OnUserEff.img/normalEffect/demonSlayer/chatBalloon0");
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial2/Scene2");
    } else if (status === i++) {
        ms.getNPCTalk(["我感受到門裡有一股令人窒息的黑暗氣息。然道黑魔法師的力量是這樣...?!要趕快進去幫他們2個。"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionStatus(true);
        ms.dispose();
        ms.warp(927020090, 0);
    } else {
        ms.dispose();
    }
}
