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
        qm.sendNext("等等,你剛才說要成為英雄?");
    } else if (status === i++) {
        qm.sendNextPrevS("嗯!我要成為和楓之谷英雄一樣帥氣的英雄!", 2);
    } else if (status === i++) {
        qm.sendNextPrev("太可笑了,你憑什麼?");
    } else if (status === i++) {
        if (mode !== 1) {
            qm.dispose();
            return;
        }
        qm.sendNextPrevS("切!!我真的要當英雄啦!\r\n要怎麼證明你才相信?", 2);
    } else if (status === i++) {
        qm.sendYesNo("那如果有勇氣去村莊東邊的#b野狼森林#k我就相信你.\r\n知道吧? 那裡是連大人去都很危險又忌諱的地方!");
    } else if (status === i++) {
        qm.sendNextS("好啊, 我就去!", 2);
    } else if (status === i++) {
        qm.sendNextPrev("哦哦, 真的嗎? 不要勉強啊.");
    } else if (status === i++) {
        qm.sendNextPrevS("嗯, 要能輕鬆應付這種程度也是英雄的條件!", 2);
    } else if (status === i++) {
        qm.forceStartQuest();
        qm.dispose();
    } else {
        qm.dispose();
    }
}