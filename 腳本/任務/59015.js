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
        qm.sendNext("嘿嘿,嘿嘿.\r\n可愛波波的力量在發動'守護技能'後,按下左邊的方向鍵就能使用囉~");
    } else if (status === i++) {
        qm.sendNextPrev("首先進入可愛波波的守護狀態後即可隨意使用我的力量了. 波波");
    } else if (status === i++) {
        qm.sendNextPrev("#s110001510#\r\n#b[Shift]#k鍵即可儲存,使用 #b'召喚森林靈魂'#k , 按下#b'方向鍵左鍵'#k BOSS! 就可以切換成熊模式了! 超炫的!");
    } else if (status === i++) {
        qm.sendNextPrev("接著按[ctrl]鍵使用熊技能");
    } else if (status === i++) {
        qm.sendNextPrev("清除這個地圖中的地精們吧.\r\n嗯... 特別是 #b#o9390930##k一定要借助我的力量,不然是無法擊退的.\r\n使用我的力量不難的!");
    } else if (status === i++) {
        qm.teachSkill(110001501, 1, 1);
        qm.teachSkill(112000000, 1, 20);
	qm.spawnMob(9390927, 1096, 28);
	qm.spawnMob(9390927, 1028, 28);
	qm.spawnMob(9390928, 1299, 28);
	qm.spawnMob(9390928, 1201, 28);
	qm.spawnMob(9390929, 981, 28);
	qm.spawnMob(9390929, 1099, 28);
	qm.spawnMob(9390930, 900, 28);
        qm.forceStartQuest();
        qm.showWZEffectNew("Effect/Direction14.img/effect/ShamanBT/ChapterA/26");
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
        qm.sendNext("哦, 已經!!! 嘿嘿.嘿嘿.\r\n有吧, 波波已經喜歡你了!");
    } else if (status === i++) {
        qm.sendNextPrev("真的了不起!嘿嘿嘿嘿!");
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.gainExp(560);
        qm.dispose();
        qm.warp(866128000, 0);
    } else {
        qm.dispose();
    }
}