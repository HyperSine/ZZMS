/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (ms.getQuestCustomData(38907) === "3") {
        action4(mode, type, selection);
    } else if (ms.getQuestCustomData(38907) === "2") {
        action3(mode, type, selection);
    } else if (ms.getQuestCustomData(38900) === "1") {
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
        ms.lockUI(1, 0);
        ms.getDirectionEffect(9, "", [1]);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(11, "\r\n\r\n楓之谷世界中流傳著一股濃厚的黑暗.", [0]);
    } else if (status === i++) {
        ms.getDirectionEffect(11, "\r\n突然出現的黑暗魔法師說要用強力的黑暗力量支配楓之谷世界.", [0]);
    } else if (status === i++) {
        ms.getDirectionEffect(11, "\r\n所有的人因為害怕黑暗魔法師，不敢有想要對抗他的想法.", [1]);
    } else if (status === i++) {
        ms.getDirectionEffect(11, "\r\n楓之谷世界中只充滿了絕望.", [0]);
    } else if (status === i++) {
        ms.getDirectionEffect(11, "\r\n\r\n\r\n還有......", [0]);
    } else if (status === i++) {
        ms.getDirectionEffect(11, "\r\n出現了要從黑暗魔法師的掌心中救出楓之谷的他們.", [1]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [3300]);
        ms.showWZEffect("Effect/Direction8.img/lightningTutorial/Scene0");
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(927030000, 0);
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
        ms.showEnvironment(13, "Map/Effect2.img/eunwol/enter", [0]);
        ms.getDirectionEffect(1, "", [3500]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(927030020, 0);
    } else {
        ms.dispose();
    }
}

function action3(mode, type, selection) {
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
        ms.getDirectionEffect(1, "", [4000]);
        ms.getDirectionStatus(true);
        ms.showWZEffect("Effect/Direction15.img/eunwolTutorial/Scene0");
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(927030030, 0);
    } else {
        ms.dispose();
    }
}

function action4(mode, type, selection) {
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
        ms.getDirectionEffect(1, "", [16000]);
        ms.getDirectionStatus(true);
        ms.showWZEffect("Effect/Direction15.img/eunwolTutorial/Scene1");
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(927030040, 0);
    } else {
        ms.dispose();
    }
}
