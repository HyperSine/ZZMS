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
        qm.sendNext("那個那個,可愛的那位,救救我吧!");
    } else if (status === i++) {
        qm.sendNextPrevS("嗚哇! 貓咪會說話也!!!", 2);
    } else if (status === i++) {
        qm.sendNextPrev("喵嗚!! 是會說話的貓咪阿樂喵嗚!!!\r\n啊,現在那不是重點啦.");
    } else if (status === i++) {
        if (mode !== 1) {
            qm.dispose();
            return;
        }
        qm.sendNextPrev("現在被補貓器卡住了出不來.\r\n再加上想尿尿又坐立不安喵嗚.\r\n救我出來就太好了.");
    } else if (status === i++) {
        qm.sendYesNo("把這個補貓器打破喵嗚. #e#b[Ctrl]#k#n鍵,集氣後使出連擊看看.應該多打幾下補貓器就會壞了.");
    } else if (status === i++) {
        qm.sendNextS("使用 #e#b[Ctrl]#k#n鍵連續打捕貓器前面那個對嗎?", 3);
    } else if (status === i++) {
        qm.sendNextPrev("喵嗚! " + (qm.getPlayerStat("GENDER") === 0 ? "哥哥" : "姐姐") + "也像我一樣聰明.");
    } else if (status === i++) {
        qm.forceStartQuest();
        qm.showWZEffectNew("Effect/Direction14.img/effect/ShamanBT/ChapterA/29");
        qm.dispose();
    } else {
        qm.dispose();
    }
}