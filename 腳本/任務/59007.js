/* global qm */

var status = -1;

function start(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (status === i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.sendNext("喵嗚! 再徹底的介紹一次! 我是楓之谷動物英雄團的成員!");
    } else if (status === i++) {
        qm.sendNextPrevS("我的名字是#h0#. 那楓之谷動物英雄團是?", 2);
    } else if (status === i++) {
        qm.sendNextPrev("喵嗚! 楓之谷動物英雄團就是,為了將來的楓之谷英雄,在旅程上所準備的4隻帥氣動物們.");
    } else if (status === i++) {
        qm.sendNextPrevS("喂~,我早晚也會成為楓之谷的英雄. 就像楓之谷5英雄一樣!!", 2);
    } else if (status === i++) {
        qm.sendNextPrev("喵嗚! 我們也很喜歡楓之谷5英雄. 特別我是幻影俠盜的粉絲. 太棒了. 英雄的話一定就是楓之谷5英雄喵嗚. 所以才在找最後的同伴,你就是最後的那一位.");
    } else if (status === i++) {
        qm.sendNextPrevS("啊?我嗎?", 1);
    } else if (status === i++) {
        qm.sendNextPrev("雖然有人的臉和身體,但我還是動物不是嗎喵嗚?");
    } else if (status === i++) {
        if (mode !== 1) {
            qm.dispose();
            return;
        }
        qm.sendNextPrevS("錯!!!雖然有動物的耳朵和尾巴但也是人!!!", 1);
    } else if (status === i++) {
        qm.sendYesNo("不要管那些了啦. 反正想成為我們的同伴嗎喵嗚?");
    } else if (status === i++) {
        qm.sendNextS("嗯. 我想成為你們的同伴!", 1);
    } else if (status === i++) {
        qm.sendNextPrev("知道了喵嗚. 那麼心情準備好的話,再和我對話喵嗚.");
    } else if (status === i++) {
        qm.forceStartQuest();
        qm.dispose();
    } else {
        qm.dispose();
    }
}