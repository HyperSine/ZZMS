/* RED 1st impact
 The Explorer Book and A Maple Leaf
 Made by Daenerys
 */
var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 1) {
            qm.sendNextS("嗯?為什麼不收下呢?有了這個,就可以替你自己留下屬於自己的故事啊!", 4, 1012100);
            qm.dispose();
            return;
        }
        status--;
    }
    if (status == 0) {
        qm.sendNextS("今天我是來送你禮物的,這是一本讓你能夠在將來紀錄楓之谷世界旅行時,所經歷的一切的#b冒險之書#k,有了這個,將來你就能夠紀錄你自己的故事了!", 4, 1012100);
    } else if (status == 1) {
        qm.sendYesNoS("怎麼樣呢?要收下#b冒險之書#k嗎?", 15, 1012100);
    } else if (status == 2) {
        qm.sendNextS("讓我瞧一瞧...適合戰士你的冒險之書.....", 4, 1012100);
    } else if (status == 3) {
        qm.sendNextPrevS("那麼,希望將來你能夠有一段奇幻愉快的冒險.", 4, 1012100);
    } else if (status == 4) {
        qm.forceStartQuest();
        qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1)
        status++;
    else
        status--;

    if (status == 0) {
        qm.sendNextS("#b冒險之書#k嗎? 在這裡可以記錄我的冒險故事?", 16);
    } else if (status == 1) {
        qm.sendNextPrevS("剛好正是要開始冒險的時候，那麼從現在開始就行了. …咦?", 16);
    } else if (status == 2) {
        qm.lockUI(true);
        qm.showEffect(false, "adventureStory/mapleLeaf/0");
        qm.wait(1800);
    } else if (status == 3) {
        qm.lockUI(false);
        qm.sendNextS("這楓葉是什麼? 這麼看來在楓之島上有巨大的楓樹吧. 這楓葉是一路跟著我到這的嗎?", 17);
    } else if (status == 4) {
        qm.sendNextPrevS("這個也算是紀念，我會好好保存的. 夾在#b 冒險之書#k中間就不會變皺了.", 17);
    } else if (status == 5) {
        qm.sendNextPrevS("點選鍵盤設定鈕設定劇情書熱鍵後,打開書櫃確認#e#b'冒險之書'#k#n吧.", 17);
    } else if (status == 6) {
        qm.forceStartQuest();
        qm.forceCompleteQuest();
        qm.topMsg("獲得了冒險之書.");
        qm.openUI(191);
        qm.dispose();
    } else {
        qm.dispose();
    }
}