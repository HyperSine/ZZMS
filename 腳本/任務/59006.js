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
        qm.sendNext("剛剛和野狼打鬥好像有提升等級的樣子.");
    } else if (status === i++) {
        qm.sendNextPrevS("#b等級#k?", 2);
    } else if (status === i++) {
        qm.sendNextPrev("就是啊, 消滅怪物可累積經驗值,到達某一個程度的話就會升級.");
    } else if (status === i++) {
        qm.sendNextPrevS("嗯嗯... 升級的話有什麼好處?", 2);
    } else if (status === i++) {
        if (mode !== 1) {
            qm.dispose();
            return;
        }
        qm.sendNextPrev("升級可以變的更強啊.\r\n當然體力也會提升, 升級的時候即可獲得叫做素質點數的東西,把它分配在我的屬性中可以變更強.\r\n\r\n但是在屬性欄位中,直接點OK會比點'自動分配'的按鍵還要好.");
    } else if (status === i++) {
        qm.sendYesNo("先打開屬性欄位,把剛剛升級得到的素質點數分配到屬性裡面.");
    } else if (status === i++) {
        qm.sendNextS("性欄位無論何時按[S]鍵即可開啟.\r\n#i03800628#", 1);
    } else if (status === i++) {
        qm.forceStartQuest();
        qm.updateInfoQuest(59006, "ap=1")
        qm.openUI(2);
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

    var i = -1;
    if (status === i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.sendNext("做的好,有感覺變強一點了喵嗚.");
    } else if (status === i++) {
        qm.sendNextPrev("就像現在這樣一直打怪,就可以升級變的更強了哦喵!!!");
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.gainExp(34);
        qm.dispose();
    } else {
        qm.dispose();
    }
}