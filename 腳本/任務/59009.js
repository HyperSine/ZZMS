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
        qm.sendNext("喂~ BOSS!如何? 現在感受萊伊的力量看看!");
    } else if (status === i++) {
        qm.sendNextPrevS("#s110001510#\r\n#b[Shift]#k鍵即可儲存,使用 #b'召喚森林靈魂'#k , 按下#b'方向鍵右鍵'#k BOSS! 就可以切換成雪豹模式了! 超炫的!", 5);
    } else if (status === i++) {
        qm.sendNextPrevS("接著按[ctrl]鍵使用雪豹突擊技能", 5);
    } else if (status === i++) {
        qm.teachSkill(110001510, 1, 1);
        qm.teachSkill(110001500, 1, 1);
        qm.teachSkill(110001502, 1, 1);
        qm.teachSkill(112100000, 1, 20);
        qm.setSkillMap(110001510, 42);
	qm.spawnMob(9390927, 461, 28);
	qm.spawnMob(9390927, 561, 28);
	qm.spawnMob(9390927, 661, 28);
	qm.spawnMob(9390927, 761, 28);
	qm.spawnMob(9390927, 861, 28);
        qm.forceStartQuest();
        qm.showWZEffectNew("Effect/Direction14.img/effect/ShamanBT/ChapterA/27");
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
        qm.sendNext("哦嗚~ BOSS! 你, 還不賴嘛!. 但好像有點受傷了. 等一下~ BOSS!");
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.gainExp(135);
        qm.dispose();
    } else {
        qm.dispose();
    }
}