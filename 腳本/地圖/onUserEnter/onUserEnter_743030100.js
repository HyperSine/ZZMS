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
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.getDirectionEffect(3, "", [0]);
        ms.spawnNPCRequestController(9330205, 69, 3, 1, 8512017);
        ms.spawnNPCRequestController(9330202, 389, 3, 0, 8512018);
        ms.getDirectionEffect(1, "", [500]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/balloonMsg10/0", [2000, 550, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/balloonMsg10/1", [2000, 250, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/balloonMsg10/2", [2000, 250, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/balloonMsg10/3", [2000, 550, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/balloonMsg10/4", [2000, 550, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/balloonMsg10/9", [2000, 550, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/balloonMsg10/5", [2000, 250, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/balloonMsg10/10", [2000, 550, -150, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8512018, "attack0", 0, true);
        ms.getDirectionEffect(1, "", [720]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/DaddysSkill0/0", [1000, 530, 0, 0, 0]);
        ms.showEnvironment(5, "chivalrousFighter/arkAttack0", []);
        ms.getDirectionEffect(1, "", [240]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossHit/0", [600, 250, 0, 0, 0]);
        ms.setNPCSpecialAction(8512017, "hit1", 0, true);
        ms.setNPCSpecialAction(8512018, "hit0", 0, true);
        ms.getDirectionEffect(1, "", [100]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossHit/0", [600, 235, -10, 0, 0]);
        ms.getDirectionEffect(1, "", [100]);
    } else if (status === i++) {
        ms.showEnvironment(5, "demonSlayer/arkAttack0", []);
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossHit/0", [600, 259, -20, 0, 0]);
        ms.getDirectionEffect(1, "", [120]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8512017, "hit1", 0, true);
        ms.showEnvironment(5, "demonSlayer/arkAttack1", []);
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossHit/0", [600, 235, -10, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossHit/0", [600, 259, -20, 0, 0]);
        ms.getDirectionEffect(1, "", [120]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8512017, "attack1", 0, true);
        ms.setNPCSpecialAction(8512018, "attack0", 0, true);
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/DaddysSkill0/1", [1000, 550, 0, 0, 0]);
        ms.showEnvironment(5, "chivalrousFighter/arkAttack1", []);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossSkill10-1/3", [320, 250, 30, 0, 0]);
        ms.setNPCSpecialAction(8512018, "hit0", 0, true);
        ms.getDirectionEffect(1, "", [290]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossHit/0", [600, 530, -30, 0, 0]);
        ms.showEnvironment(5, "demonSlayer/arkAttack1", []);
        ms.getDirectionEffect(1, "", [100]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossHit/0", [600, 510, -20, 0, 0]);
        ms.showEnvironment(5, "demonSlayer/arkAttack0", []);
        ms.getDirectionEffect(1, "", [120]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossHit/0", [600, 570, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [50]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossHit/0", [600, 540, 15, 0, 0]);
        ms.getDirectionEffect(1, "", [120]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8512017, "move", 0, true);
        ms.updateNPCSpecialAction(8512017, 1, 40, 100);
        ms.getDirectionEffect(1, "", [800]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8512017, "attack1", 0, true);
        ms.setNPCSpecialAction(8512018, "teleportation", 0, true);
        ms.getDirectionEffect(1, "", [720]);
    } else if (status === i++) {
        ms.removeNPCRequestController(8512018);
        ms.showEnvironment(5, "chivalrousFighter/dragonSkillUse", []);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/DaddysSkill0/2", [2000, 380, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossSkill10-1/0", [3000, 510, 0, 0, 0]);
        ms.showEnvironment(5, "chivalrousFighter/dragonSkillBlast", []);
        ms.getDirectionEffect(1, "", [1300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossHit/0", [600, 290, -50, 0, 0]);
        ms.setNPCSpecialAction(8512017, "hit1", 0, true);
        ms.spawnNPCRequestController(9330202, 350, 3, 0, 8513690);
        ms.setNPCSpecialAction(8513690, "hit0", 0, true);
        ms.getDirectionEffect(1, "", [1300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossHit/0", [600, 550, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [720]);
    } else if (status === i++) {
        ms.getNPCTalk(["比想像中更強　!!!"], [3, 0, 9330205, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["真的是皇帝的話，不會使用出這種邪惡的力量，你這假貨!!!!"], [3, 0, 9330202, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/balloonMsg10/6", [2000, 0, -100, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/balloonMsg10/7", [2000, 450, -130, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/balloonMsg10/8", [2000, 200, -150, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8512017, "move", 0, true);
        ms.updateNPCSpecialAction(8512017, -1, 10, 100);
        ms.getDirectionEffect(1, "", [100]);
    } else if (status === i++) {
        ms.getNPCTalk(["不行!!!!!!!!!!!"], [3, 0, 9330202, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8512017, "attack1", 0, true);
        ms.getDirectionEffect(1, "", [720]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction100.img/effect/tuto/BossSkill10-1/4", [1600, 250, 30, 0, 0]);
        ms.showEnvironment(5, "chivalrousFighter/dragonSkillUse", []);
        ms.getDirectionEffect(1, "", [180]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8513690, "teleportation", 0, true);
        ms.getDirectionEffect(1, "", [720]);
    } else if (status === i++) {
        ms.removeNPCRequestController(8513690);
        ms.getDirectionEffect(1, "", [50]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(8512017);
        ms.dispose();
        ms.warp(743020300, 0);
    } else {
        ms.dispose();
    }
}
