/* global cm */

var status = -1;

function action(mode, type, selection) {
    mode === 1 ? status++ : status--;

    if (cm.itemQuantity(4033194) || cm.itemQuantity(4033195) >= 1 || !cm.isQuestActive(20031)) {
        cm.dispose();
        return;
    }

    var i = -1;
    if (status <= i++) {
        cm.dispose();
    } else if (status === i++) {
        cm.getNPCTalk(["裝滿了藥水的蒙塵的藥水箱。令人懷疑可不可以賣。\r\n要取出藥水箱嗎？"], [4, 1106004, 0, 2, 0, 0]);
    } else if (status === i++) {
        cm.getNPCTalk(["咦？ 這封信是什麼？寫給林伯特大叔的信？看起來好老舊了？\r\n「雷神之錘」寫來的… 雷神之錘是誰？ 好像在哪聽過？想不起來了？沒寫收件人…先拿去給林伯特大叔吧！"], [4, 1106004, 0, 0, 2, 0, 0, 1, 0]);
        cm.gainItem(4033194, 1);
        cm.gainItem(4033195, 1);
        cm.dispose();
    } else {
        cm.dispose();
    }
}