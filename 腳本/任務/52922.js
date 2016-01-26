var status = -1;
var complete = false;

function start(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        qm.sendNext("為了對抗逐漸變強的黑暗力量,楓之谷的所有勇士必須一起變強.");
    } else if (status == 1) {
        qm.sendNextPrev("勇士阿...給你注入了我的力量的楓方塊");
    } else if (status == 2) {
        qm.sendNextPrev("這個方塊是古代煉金術師製作的,雖然根據使用次數需要的楓幣會逐漸增加,但這個方塊是不會遜於以前支配楓之谷的各種方塊的.");
    } else if (status == 3) {
        qm.sendNextPrev("現在要重新注入我的力量,請收下..");
    } else if (status == 4) {
        if (!complete) {
            qm.gainItemPeriod(3994895, 1, 12, true, "");
            qm.forceCompleteQuest();
            complete = true;
        }
        qm.sendNextPrevS("因為在瞬間移動中因此無法注入太多力量,但請不要擔心.", 1);
    } else if (status == 5) {
        qm.sendNextPrevS("若親自來村莊找我,我會製作擁有更多力量的方塊給你.", 1);
    } else if (status == 6) {
        qm.dispose();
    }
}