var status = -1;

function start(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (status <= i++) {
        cm.dispose();
    } else if (status === i++) {
        qm.sendNextS("雖然感覺不到他的一點誠意但是他的回答也許是對的。", 2);
    } else if (status === i++) {
        qm.sendNextS("對我來說是昨天和黑魔法師發生的熾熱戰鬥但是對其他人已經是數百年前，或是在歷史書中看到的事實…", 2);
    } else if (status === i++) {
        qm.sendNextS("為了守護新楓之谷使用的力量也都消失了。和 綠水靈 戰鬥都會氣喘吁吁的我，現在也許比村民還弱", 2);
    } else if (status === i++) {
        qm.sendNextS("和從前的情況不同了。和過去可靠的同伴在一起不同的是現在我身邊沒有一個人…誰也沒有…", 2);
    } else if (status === i++) {
        qm.sendNextS("可是…但是！即使這樣我還是要站起來。因為我是王。我的百姓都被黑魔法師的詛咒關進寒冰裡的精靈之王…", 2);
    } else if (status === i++) {
        qm.sendNextS("對王來說不容許放棄兩個字。", 2);
    } else if (status === i++) {
        qm.sendNextS("想要放棄的心…必須要丟開。", 2);
    } else {
        qm.forceCompleteQuest(24051);
        qm.gainExp(2000);
        qm.dispose();
    }
}

function end(mode, type, selection) {
	qm.dispose();
}
