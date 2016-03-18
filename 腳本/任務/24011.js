var status = -1;
function start(mode, type, selection) {
	if (mode === 1) {
		status++;
	} else {
		status--;
	}

	var i = -1;
	if (status <= i++) {
		qm.dispose();
	} else if (status == i++) {
        qm.sendNext("…真奇妙。 從你身上感受不到王的力量… 那不是經過數百年的時間就會消失的力量… 這是怎麼回事？ 從你感受到的是幾乎異常而不詳的黑暗氣息…\r\n#L0##b若要說明就說來話長了。#l\n#k");
    } else if (status == i++) {
        qm.sendNext("肉體已經像灰塵一樣消失剩下的只是將數百數千年的時間像水一樣度過的精神而已，我樂意洗耳恭聽。 請告訴我這段時間你的世界都發生了什麼事。\r\n#L0##b(慢慢說明黑魔法師與消滅他的戰鬥，雖然封印結果使精靈全部陷入危險的詛咒… 一個都沒有漏掉。 任何丟人現眼的事也不隱藏。)#l\n#k");
    } else if (status == i++) {
        qm.sendPlayerToNpc("也許我在精靈的歷史上被記載是愚蠢的王。 如果不是我的選擇… 如果我沒有和黑魔法師戰鬥我們精靈就不會落入這悲慘的地步。 …#p1033210#啊。 我願意接受懲罰。");
    } else if (status == i++) {
        qm.sendNext("原來發生了很多事… 讓我很驚訝。 原來存在著甚至將新楓之谷整體覆蓋的黑暗力量… 然後這影響甚至延到我們精靈。 太悲哀了… 但是這不是你該負責的事。");
    } else if (status == i++) {
        qm.sendNext("王的選擇是精靈的選擇。 你做的任何選擇就等於是精靈整體的選擇。 戰鬥還沒有結束，沒有任何事有定論。 只要不是愚蠢的選擇而是最高的選擇就可以了…");
    } else if (status == i++) {
        qm.sendNext("我現在知道你怎麼這個樣子來找我了。 擁有之事卻失去王的力量者呀… 應該不需要再給你 <王的試煉>了吧。 你已經具備了資格，容許給你王的力量。");
    } else if (status == i++) {
		if (qm.getJob() == 2300) qm.changeJob(2310);
		qm.forceStartQuest(24011, "1");
	}
}

function end(mode, type, selection) {
	if (mode === 1) {
		status++;
	} else {
		status--;
	}

	var i = -1;
	if (status <= i++) {
		qm.dispose();
	} else if (status == i++) {
        qm.sendNext("好了，你現在已經是雙弩精靈(2轉)了。現在去向世人展示我們的力量吧。");
    } else if (status == i++) {
        if(qm.isQuestActive(24011)) qm.forceCompleteQuest(24011);
    } else {
        qm.dispose();
	}
}
