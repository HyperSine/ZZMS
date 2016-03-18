/* global ms */

var status = -1;

function action(mode, type, selection) {
    if (mode === 0 && status !== 0) {
        status--;
    } else {
        status++;
    }
    
    var i = -1;
    if (ms.isQuestFinished(24009) == false && ms.isQuestActive(24009) == false) {
        if (status <= i++) {
            ms.dispose();
        } else if (status === i++) {
            ms.lockUI(true);
            ms.disableOthers(true);
            //ms.getDirectionStatus(true);
            ms.getDirectionEffect(3, "", [1]);   //往左走
            ms.wait(2000);
        } else if (status === i++) {
            ms.getDirectionEffect(3, "", [0]);   //停下
            ms.sendNextS("長老們？", 2);
        } else if (status === i++) {
            ms.getDirectionEffect(3, "", [2]);    //往右走
            ms.wait(3000);
        } else if (status === i++) {
            ms.getDirectionEffect(3, "", [0]);   //停下
            ms.sendNextS("孩子們……", 2);
        } else if (status === i++) {
            ms.getDirectionEffect(3, "", [1]);   //往左走
            ms.wait(1000);
        } else if (status === i++) {
            ms.getDirectionEffect(3, "", [0]);   //停下
            ms.sendNextS("大家都還在冰里……", 2);
        } else {
            ms.forceStartQuest(24009, "1");    //似乎是个标记任务，以触发24040任务的完成
            ms.lockUI(false);
            ms.disableOthers(false);
            ms.dispose();
        }
    } else {
        ms.dispose();
    }
}