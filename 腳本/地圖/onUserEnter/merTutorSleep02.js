/* global ms */

var status = -1;

function action(mode, type, selection) {
    status++;

    if (ms.isQuestFinished(24005)) {
        ms.dispose();
        return;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.lockUI(false);
        ms.lockKey(false);
        ms.disableOthers(false);
        ms.teachSkill(20021181, -1, 0);  //关闭剧情临时技能
        ms.teachSkill(20021166, -1, 0);  //关闭剧情临时技能
        ms.teachSkill(20020109, 1, 1);   //开启精灵的回复，否则精灵游侠回魔太艰难了。呵呵呵
        ms.teachSkill(20021110, 1, 3);   //开启精灵的祝福，增加10%经验，呵呵呵
        ms.teachSkill(20020112, 1, 1);   //开启王的资格，魅力直接30级，呵呵呵
        ms.teachSkill();
        ms.forceStartQuest(24008, "1");    //似乎是个标记任务，以触发24005任务的完成
        ms.dispose();
    } else {
        ms.dispose();
    }
}