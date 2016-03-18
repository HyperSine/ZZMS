/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.teachSkill(20041222, 1, 1);
        ms.setSkillMap(20041222, 42);
        ms.spawnMob(9300535, 600, -298);
        ms.spawnMob(9300535, 150, -508);
        ms.spawnMob(9300535, -150, -508);
        ms.spawnMob(9300535, -600, -298);
        ms.spawnNPCRequestController(2159363, 600, -310, 0, 5442797);
        ms.spawnNPCRequestController(2159364, 150, -520, 0, 5442798);
        ms.spawnNPCRequestController(2159365, -150, -520, 0, 5442799);
        ms.spawnNPCRequestController(2159366, -600, -310, 0, 5442800);
        ms.showEnvironment(13, "lightning/screenMsg/4", [0]);
        ms.getNPCTalk(["時間停止了，被黑魔法師發現之前要把所有封印要啟動。"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["首先過去右邊可以看到發光的踏板地方，過去時點選 #r[SHIFT]#k後使用 #b<星光閃爍>#k 技能。"], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.showEnvironment(13, "lightning/screenMsg/5", [0]);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
