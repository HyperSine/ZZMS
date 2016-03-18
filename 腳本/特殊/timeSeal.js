/* global cm */
var status = -1;

function action(mode, type, selection) {
    status++;
    if (cm.isQuestActive(25670) && cm.isQuestActive(25671) && cm.isQuestActive(25672) && cm.isQuestActive(25673)) {
        if (!cm.isQuestActive(25674)) {
            cm.spawnMob(9300535, 0, -80);
            cm.spawnNPCRequestController(2159367, 0, -80, 0, 5443487);
            cm.getNPCTalk(["最後封印在中央階梯下方，過去後把封印完成。"], [8, 0, 0, 0, 16, 0, 0, 0, 0]);
        } else {
            if (status === 0) {
                cm.getNPCTalk(["如此就把所有的封印啟動完畢。"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
                cm.setSkillMap(20041222, 0);
                cm.removeNPCRequestController(5442797);
                cm.removeNPCRequestController(5442798);
                cm.removeNPCRequestController(5442799);
                cm.removeNPCRequestController(5442800);
                cm.removeNPCRequestController(5443487);
            } else {
                cm.dispose();
                cm.warp(927020072, 0);
            }
            return;
        }
    }
    cm.dispose();
}