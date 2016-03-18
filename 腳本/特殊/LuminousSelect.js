/* global cm */
var status = -1;

function action(mode, type, selection) {
    status++;
    if (status === 0) {
        cm.getNPCTalk([], [3, 0, 0, 23, 1, 0]);
    } else {
        if (selection === 0) {
            cm.teachSkill(27001100, 1, 20);//星星閃光
            cm.teachSkill(27000106, 1, 5);//光魔法強化
        } else {
            cm.teachSkill(27001201, 1, 20);//黑暗球體
            cm.teachSkill(27000207, 1, 5);//黑暗魔法強化
        }
        cm.dispose();
    }
}