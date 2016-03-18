/* global qm */

var status = -1;

function start(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        qm.dispose();
        return;
    }

    var i = -1;
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.sendNext("你好，你不覺得真是適合旅行的天氣嗎？看起來很陌生，應該是進行新旅行的冒險家吧，我是要搭乘前往楓之島的船長，很高興認識你。");
    } else if (status === i++) {
        qm.sendAcceptDecline("現在還不是出發的時間，因為在進行出發的準備，所以一邊看船一邊等候吧。");
    } else if (status === i++) {
        qm.sendNext("喔∼！出發準備已經完成的樣子，準備算快嗎？不管怎樣都是好消息∼可以有趟美好出航，那麼就出發吧。");
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.dispose();
        qm.warp(3000000, 0);
    } else {
        qm.dispose();
    }
}