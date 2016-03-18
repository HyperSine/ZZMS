/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (ms.getQuestCustomData(38900) === "2") {
        action2(mode, type, selection);
    } else {
        action1(mode, type, selection);
    }
}

function action1(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.getDirectionEffect(9, "", [1]);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [3000]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["......"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["死了...嗎?"], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["......"], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["果然沒有所謂的死後世界啊...看來應該很難在黃泉之下見面,普力特那傢伙會很失望吧."], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["......"], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction15.img/effect/story/BalloonMsg1/0", [3200, 300, -210, 0, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction15.img/effect/story/BalloonMsg1/1", [2900, -300, -70, 0, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction15.img/effect/story/BalloonMsg1/2", [2600, 300, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction15.img/effect/story/BalloonMsg1/3", [2300, -300, 70, 0, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction15.img/effect/story/BalloonMsg1/4", [2000, 300, 210, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getNPCTalk(["這是什麼聲音啊?"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction15.img/effect/story/BalloonMsg1/5", [3200, -300, -70, 0, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction15.img/effect/story/BalloonMsg1/6", [2900, 300, -210, 0, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction15.img/effect/story/BalloonMsg1/7", [2600, -300, 70, 0, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction15.img/effect/story/BalloonMsg1/8", [2300, 300, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction15.img/effect/story/BalloonMsg1/9", [2000, 300, 210, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getNPCTalk(["好吵喔.是那些傢伙嗎?不過我看他們應該不像那麼短命的樣子啊."], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.showEnvironment(15, "", [1, 250, 240, 240, 240, 3000]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.showEnvironment(15, "", [0, 0, 0, 0, 0, 1000]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.showEnvironment(13, "Map/Effect2.img/eunwol/meetfox", [0]);
        ms.getDirectionEffect(1, "", [3200]);
    } else if (status === i++) {
        ms.getDirectionEffect(9, "", [0]);
        ms.lockUI(0);
        ms.dispose();
        ms.warp(940200010, 0);
    } else {
        ms.dispose();
    }
}

function action2(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.lockUI(1, 0);
        ms.getDirectionEffect(9, "", [1]);
        ms.getDirectionEffect(3, "", [0]);
        ms.showEnvironment(13, "Map/Effect2.img/eunwol/gofoxvillage", [0]);
        ms.getDirectionEffect(1, "", [3000]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(9, "", [0]);
        ms.lockUI(0);
        ms.dispose();
        ms.warp(940200010, 0);
    } else {
        ms.dispose();
    }
}
