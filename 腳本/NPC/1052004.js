var status = -1;
var facetype;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0) {
		cm.dispose();
		return;
    } else {
		status++;
    }

    if (status == 0) {
		var face = cm.getPlayerStat("FACE");

		if (cm.getPlayerStat("GENDER") == 0) {
			facetype = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20012, 20014];
		} else {
			facetype = [21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21012, 21014];
		}
		for (var i = 0; i < facetype.length; i++) {
			facetype[i] = facetype[i] + face % 1000 - (face % 100);
		}
		facetype = cm.getCanFace(facetype);
		cm.askAvatar("我能把你現在的臉變成全新的臉…你不想換張新的臉嗎？只要有#b#t5152057##k的話，我就給你做整容手術。慢慢挑選你喜歡的臉吧~", facetype,5152057);
    } else if (status == 1){
		if (facetype.length == 0) {
			cm.sendOk("出現未知錯誤。");
		} else if (cm.setAvatar(5152057, facetype[selection]) == 1) {
			cm.sendOk("好了，你的朋友們一定認不出來是你了！");
		} else {
			cm.sendOk("嗯……看樣子你沒有整容券……很抱歉，沒有整容券的話，我不能給你做整形手術。");
		}
		cm.dispose();
    }
}
