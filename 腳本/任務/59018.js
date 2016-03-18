/* global qm */
var status = -1;

function start(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.getNPCTalk(["嗯,醒過來需要花3天時間."], [4, 9390300, 0, 0, 4, 0, 0, 1, 0, 9390303]);
    } else if (status === i++) {
        qm.getNPCTalk(["艾卡很不坦誠.\r\n#b#h0##k 昏倒的時候最坐立不安的."], [4, 9390300, 0, 0, 4, 0, 1, 1, 0, 9390300]);
    } else if (status === i++) {
        qm.getNPCTalk(["吵, 吵死了!!!"], [4, 9390300, 0, 0, 4, 0, 1, 1, 0, 9390303]);
    } else if (status === i++) {
        qm.getNPCTalk(["但是我要再次和艾卡打聲招呼.\r\n我們使用了他的力量,所以#h0#的魔力有點不夠."], [4, 9390300, 0, 0, 4, 0, 1, 1, 0, 9390300]);
    } else if (status === i++) {
        qm.getNPCTalk(["和#h0#簽訂了守護條約,所以現在我們是精靈狀態."], [4, 9390300, 0, 0, 4, 0, 1, 1, 0, 9390300]);
    } else if (status === i++) {
        qm.getNPCTalk(["都在這個狀態下使用魔法的話,只能用#h0#的魔力了.\r\n上次#h0#昏倒也是因為使用了全部的力量,魔力全都下降了."], [4, 9390300, 0, 0, 4, 0, 1, 1, 0, 9390300]);
    } else if (status === i++) {
        qm.getNPCTalk(["萊伊和波波平時也必需回到精靈界. 一直這樣不回去的話 #h0#的魔力會漸漸消耗掉的."], [4, 9390300, 0, 0, 4, 0, 1, 1, 0, 9390300]);
    } else if (status === i++) {
        qm.getNPCTalk(["但沒關係的啦, BOSS!"], [4, 9390300, 0, 0, 4, 0, 1, 1, 0, 9390302]);
    } else if (status === i++) {
        qm.getNPCTalk(["只有平時需要回到精靈界. 一下下的話不需要太多魔力的,無論何時都可以召喚我."], [4, 9390300, 0, 0, 4, 0, 1, 1, 0, 9390302]);
    } else if (status === i++) {
        qm.getNPCTalk(["就向我們學習一樣,施展動物的守護模式就可以了, BOSS!"], [4, 9390300, 0, 0, 4, 0, 1, 1, 0, 9390302]);
    } else if (status === i++) {
        qm.getNPCTalk(["波波也是一樣的心情~"], [4, 9390300, 0, 0, 4, 0, 1, 1, 0, 9390301]);
    } else if (status === i++) {
        qm.getNPCTalk(["但是不要用你和艾卡的力量比較好."], [4, 9390300, 0, 0, 4, 0, 1, 1, 0, 9390300]);
    } else if (status === i++) {
        qm.getNPCTalk(["使用我們的力量需要很大量的魔力,像上次一樣又昏倒的話可不行.\r\n持續升級養成力量的話,無論何時都可再次召喚我們."], [4, 9390300, 0, 0, 4, 0, 1, 1, 0, 9390300]);
    } else if (status === i++) {
        qm.getNPCTalk(["那短時間就見不到面了吧?"], [4, 9390300, 0, 0, 2, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["只是稍微離開一下下的程度而已. \r\n不要擔心喵嗚. 那麼現在我們就要消失了. 但會一直留在你的心裡,你的力量裡的"], [4, 9390300, 0, 0, 4, 0, 1, 1, 0, 9390300]);
    } else if (status === i++) {
        qm.getNPCTalk(["那要好好的哦"], [4, 9390300, 0, 0, 5, 0, 1, 1, 0, 9390300]);
    } else if (status === i++) {
        qm.getNPCTalk(["好了, BOSS! 應該會常常見面的啦!"], [4, 9390300, 0, 0, 5, 0, 1, 1, 0, 9390302]);
    } else if (status === i++) {
        qm.getNPCTalk(["可愛的波波也是."], [4, 9390300, 0, 0, 5, 0, 1, 1, 0, 9390301]);
    } else if (status === i++) {
        qm.getNPCTalk(["嗯. 小小的... 時候叫我.我會過來."], [4, 9390300, 0, 0, 5, 0, 1, 1, 0, 9390303]);
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.dispose();
        qm.warp(866138000, 0);
    } else {
        qm.dispose();
    }
}
