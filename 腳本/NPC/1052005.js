var status = 0;
var beauty = 0;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0) {
		if (status == 1)
            cm.sendOk("好吧……等你考慮好之後再跟我說。");
        cm.dispose();
        return;
    }
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        cm.sendNext("你是想換臉型嗎？是想換個新樣子啊？好~我就幫你變成你想要的樣子。\r\n#L0##b整容（使用普通會員卡）#l");
    } else if (status == 1) {
        cm.sendYesNo("使使用普通整容券可以隨機更換臉型……你確定要使用#b#t5152056##k更換臉型嗎？");
    } else if (status == 2) {
        var face = cm.getPlayerStat("FACE");
        var facetype;
        if (cm.getPlayerStat("GENDER") == 0) {
            facetype = [20000, 20001, 20002, 20003, 20004, 20005, 20006, 20007, 20008, 20012, 20014];
        } else {
            facetype = [21000, 21001, 21002, 21003, 21004, 21005, 21006, 21007, 21008, 21012, 21014];
        }
        for (var i = 0; i < facetype.length; i++) {
            facetype[i] = facetype[i] + face % 1000 - (face % 100);
        }
		facetype = cm.getCanFace(facetype);
		if (facetype.length == 0) {
			cm.sendOk("出現未知錯誤。");
        } else if (cm.setRandomAvatar(5152056, facetype) == 1) {
            cm.sendOk("好了，你的朋友們一定認不出你了！");
        } else {
            cm.sendOk("嗯……看樣子你沒有整容券……很抱歉，沒有整容券的話，我不能給你做整形手術。");
        }
        cm.dispose();
    }
}