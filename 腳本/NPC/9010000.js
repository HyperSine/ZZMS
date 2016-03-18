var status, ObjName, ObjIDArray;

function start() {
    status = 0;
    cm.sendSimple("Hello！在楓之谷世界玩得愉快嗎?\r\n\r\n#b#L0# 結束對話。#l\r\n#b#L1#換取裝備。#l");
}

function action(mode, type, selection) {
    if (mode != 1) {
        cm.dispose();
        return;
    }

    if (status == 0) {

        switch (selection) {
            case 0:
                cm.dispose();
                return;
            case 1:
		cm.sendGetText("你想獲得什麼物品？輸入物品名稱或者ID：");
		status = 1;
        }
    } else if (status == 1) {
	ObjName = cm.getText();
	var ObjID = cm.findMapleObjectByName(ObjName);
	ObjIDArray = cm.MapleObjectIDMapToIntArray(ObjID);
	cm.sendSimple(cm.MapleObjectIDMapToString(ObjID));
	status = 2;
    } else if (status == 2) {
	cm.getPlayer().gainItem(ObjIDArray[selection], 1);
	cm.dispose();
	return;
    }

}