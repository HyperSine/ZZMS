var status = -1;

function start(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.lockUI(true);
        qm.disableOthers(true);
        qm.sendNext("王，我想我們可以發動純白淨化來解除精靈的詛咒，我們不妨試一試吧。");
    } else if (status === i++) {
        qm.sendPlayerToNpc("好啊，何樂不為呢。現在就開始吧");
    } else if (status === i++) {
        qm.wait(1000);
    } else if (status === i++) {
        qm.showWZEffect("Effect/Direction5.img/mersedesQuest/Scene1");    //这儿有点小问题
        qm.wait(4000);
    } else {
        qm.lockUI(false);
        qm.disableOthers(false);
        qm.forceStartQuest(24065, "1");
        qm.dispose();
    }
}
