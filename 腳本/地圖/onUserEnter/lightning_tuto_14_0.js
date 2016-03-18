/* global ms */
var status = -1;
var select = -1;

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
        ms.spawnNPCRequestController(1032203, 0, 0, 0, 5455262);
        ms.getDirectionEffect(1, "", [1500]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["…我，做了什麼事項呢？"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Luminous_M/0");
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 300, 0, 27]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1671]);
    } else if (status === i++) {
        ms.getNPCTalk(["拉尼亞也..."], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 300, 500, 27]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1667]);
    } else if (status === i++) {
        ms.getNPCTalk(["家也..."], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [0]);
    } else if (status === i++) {
        ms.getNPCTalk(["而且森林也…全部是我造成的。"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這個就是黑魔法師留在我體內的意思。那時黑暗的力量一直沉睡在我體內。"], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["為什麼是現在?難道黑魔法師解開封印了嗎?不是這樣的話，黑暗的力量不會突然暴走。"], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["該怎麼辦?失去光的力量，現在的我，我..."], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk([], [3, 0, 0, 23, 1, 0]);
    } else if (status === i++) {
        if (select === -1) {
            select = selection;
        }
        ms.getNPCTalk(["…不能被卷入這力量。救拉尼亞，再次阻擋黑暗的力量不會破壞這世界。即使永遠被困在封印裡。"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.playVoiceEffect("Voice.img/Luminous_M/2");
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.teachSkill(20040216, 1, 1);//光蝕
        ms.teachSkill(20040217, 1, 1);//暗蝕
        ms.teachSkill(20040221, 1, 1);//光明力量
        ms.teachSkill(20041222, 1, 1);//星光順移
        ms.unequip(1212000, true); //行星
        ms.unequip(1052495, true); //歐洛拉長袍
        ms.unequip(1072700, true); //歐洛拉鞋
        ms.unequip(1102442, true); //歐洛拉披肩
        if (select === 0) {
            ms.teachSkill(27001100, 1, 20);//星星閃光
            ms.teachSkill(27000106, 1, 5);//光魔法強化
            ms.gainItem(1142478, 1); //找回光者
            ms.equip(1052496, -5); //歐洛拉長袍
            ms.equip(1072701, -7); //歐洛拉鞋
            ms.equip(1102443, -9); //歐洛拉披肩
        } else {
            ms.teachSkill(27001201, 1, 20);//黑暗球體
            ms.teachSkill(27000207, 1, 5);//黑暗魔法強化
            ms.gainItem(1142479, 1); //陷入黑暗者
            ms.equip(1052497, -5); //暗黑歐洛拉長袍
            ms.equip(1072702, -7); //暗黑歐洛拉鞋
            ms.equip(1102444, -9); //暗黑歐洛拉披肩
        }
        ms.equip(1212001, -11); //行星
        ms.equip(1352400, -10); //星光彈
        var level = 10 - ms.getLevel();
        for (var i = 0 ; i < level ; i++) {
            ms.levelUp();
        }
        ms.lockUI(0);
        ms.removeNPCRequestController(5455262);
        ms.dispose();
        ms.warp(101000100, 0);
    } else {
        ms.dispose();
    }
}
