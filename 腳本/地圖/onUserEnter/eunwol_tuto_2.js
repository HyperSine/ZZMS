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
        ms.lockUI(1, 1);
        ms.getDirectionEffect(9, "", [0]);
        ms.getDirectionEffect(1, "", [1000]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["...呵呵,我就知道.幻影和夜光只要一見面就會吵個不停.沒辦法,因為彼此的性格迥異."], [3, 0, 2159441, 0, 5, 0, 0, 1, 0, 2159441]);
    } else if (status === i++) {
        ms.getNPCTalk(["我倒是覺得,他們的個性似乎蠻合得來的.一個很吵鬧,另一個太無趣,讓他們混在一起就能達成平衡了."], [3, 0, 2159441, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["啊哈哈,那也是有可能的.不過他們本人會很討厭聽到這番話.哎呀,又來了."], [3, 0, 2159441, 0, 5, 0, 1, 1, 0, 2159441]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 200, 1100, 66]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1364]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(2159442, 770, 50, 1, 11620707);
        ms.getDirectionEffect(1, "", [100]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(2159443, 970, 50, 1, 11621034);
        ms.getDirectionEffect(1, "", [100]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(2159444, 1170, 50, 1, 11621035);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 200]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1364]);
    } else if (status === i++) {
        ms.getNPCTalk(["等等.這裡交給我來處理吧."], [3, 0, 2159441, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(5, "", [0, 180, 1100, 66]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [406, 900]);
        ms.getDirectionEffect(2, "Skill/512.img/skill/5121007/effect", [900, -40, -25, 0, 0]);
        ms.getDirectionEffect(2, "Skill/512.img/skill/5121007/effect0", [810, -40, -25, 0, 0]);
        ms.showEnvironment(5, "eunwolTuto/Use", []);
        ms.setNPCSpecialAction(11620707, "hit", 0, true);
        ms.setNPCSpecialAction(11621034, "hit", 0, true);
        ms.setNPCSpecialAction(11621035, "hit", 0, true);
        ms.getDirectionEffect(2, "Skill/512.img/skill/5121007/hit/0", [0, -5, -50, 1, 1, 0, 11620707, 0]);
        ms.getDirectionEffect(2, "Skill/512.img/skill/5121007/hit/0", [0, -5, -50, 1, 1, 0, 11621034, 0]);
        ms.getDirectionEffect(2, "Skill/512.img/skill/5121007/hit/0", [0, -5, -50, 1, 1, 0, 11621034, 0]);
        ms.showEnvironment(5, "eunwolTuto/Hit", []);
        ms.getDirectionEffect(2, "Skill/512.img/skill/5121020/effect", [900, -40, -25, 0, 0]);
        ms.getDirectionEffect(2, "Skill/512.img/skill/5121020/effect0", [810, -40, -25, 0, 0]);
        ms.showEnvironment(5, "eunwolTuto/Use", []);
        ms.setNPCSpecialAction(11620707, "hit", 0, true);
        ms.setNPCSpecialAction(11621034, "hit", 0, true);
        ms.setNPCSpecialAction(11621035, "hit", 0, true);
        ms.getDirectionEffect(2, "Skill/512.img/skill/5121020/hit/0", [0, -5, -50, 1, 1, 0, 11620707, 0]);
        ms.getDirectionEffect(2, "Skill/512.img/skill/5121020/hit/0", [0, -5, -50, 1, 1, 0, 11621034, 0]);
        ms.getDirectionEffect(2, "Skill/512.img/skill/5121020/hit/0", [0, -5, -50, 1, 1, 0, 11621034, 0]);
        ms.showEnvironment(5, "eunwolTuto/Hit", []);
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(11620707, "die", 0, true);
        ms.setNPCSpecialAction(11621034, "die", 0, true);
        ms.setNPCSpecialAction(11621035, "die", 0, true);
        ms.getDirectionEffect(1, "", [2100]);
    } else if (status === i++) {
        ms.removeNPCRequestController(11620707);
        ms.removeNPCRequestController(11621034);
        ms.removeNPCRequestController(11621035);
        ms.getDirectionEffect(1, "", [800]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(5, "", [1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getNPCTalk(["你要一個人待在這裡嗎?"], [3, 0, 2159441, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯.因為必須有人守住這路口.現在這樣子想直接對付黑魔法師太勉強了..."], [3, 0, 2159441, 0, 5, 0, 1, 1, 0, 2159441]);
    } else if (status === i++) {
        ms.getNPCTalk(["那我們先進去...待會再碰面吧!"], [3, 0, 2159441, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯!"], [3, 0, 2159441, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [100]);
    } else if (status === i++) {
        ms.getDirectionEffect(9, "", [1]);
        ms.showEnvironment(15, "", [1, 200, 0, 0, 0, 1000]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(11, "\r\n\r\n\r\n狂狼勇士從他身後的樣子就能感受到火焰.", [0]);
    } else if (status === i++) {
        ms.getDirectionEffect(11, "\r\n\r\n說不定再也無法見到面的樣子...... #fs35#預感 #fs20#或是 #fs35#直覺.", [1]);
    } else if (status === i++) {
        ms.forceStartQuest(38900, "1");
        ms.getDirectionStatus(true);
        ms.lockUI(0);
        ms.dispose();
        ms.warp(927030050, 0);
    } else {
        ms.dispose();
    }
}
