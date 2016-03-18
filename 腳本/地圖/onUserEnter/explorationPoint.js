/*
 Author: DoubleLabyrinth
 */
var status = -1;

function start() {
    if (ms.getMapId() == 104000000) {
        ms.environmentChange("maplemap/enter/104000000", 13);
        ms.lockUI(false);
    }
///////////////精灵游侠进入魔法密林时，开启24094，以触发24046任务的完成
    if (ms.isQuestActive(24046) && (ms.getJob() == 2002 ||
                                    ms.getJob() == 2300 ||
                                    ms.getJob() == 2310 ||
                                    ms.getJob() == 2311 ||
                                    ms.getJob() == 2312)) {
        ms.forceStartQuest(24094, "1");
    }
/////////////////////////////////////////////////////////////
    ms.checkMedalQuest();
    ms.dispose();
}