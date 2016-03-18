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
        if (mode !== 1) {
            qm.dispose();
            return;
        }
        qm.sendNext("謝謝你哦!可是我的鈴噹不見了.\r\n\r\n好像掉在那個樹上面的樣子... 使用 [Ctrl]鍵攻擊樹木,鈴噹掉下來的話,使用[z]鍵撿起來給我嗎?");
    } else if (status === i++) {
        qm.sendYesNo("請快點幫我找到.沒有那個就像沒穿衣服一樣!");
    } else if (status === i++) {
        qm.sendNextS("就像剛才一樣在樹前面,使用 #e#b[Ctrl]#k#n鍵攻擊, 再使用 #e#b[Z]#k#n鍵把鈴噹撿起來就可以了吧?", 2);
        qm.forceStartQuest();
        qm.dispose();
    } else {
        qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    if (!qm.haveItem(4034005)) {
        qm.dispose();
        return;
    }

    var i = -1;
    if (status === i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.sendNext("鈴噹在這裡!還好嗎, 喵嗚?");
    } else if (status === i++) {
        qm.sendNextPrev("好... 等一下... 喵嗚... 你轉過去後面啦喵嗚, 人家很害羞.");
    } else if (status === i++) {
        qm.sendNextPrevS("好了沒啊?", 2);
    } else if (status === i++) {
        qm.sendNextPrev("喵嗚!!! 不好意思喵嗚!!! 再等一下下喵嗚哦!!!!");
        qm.gainItem(4034005, -1);
        qm.dispose();
        qm.warp(866106000, 0);
    } else {
        qm.dispose();
    }
}