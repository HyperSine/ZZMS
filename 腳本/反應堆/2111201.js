/*
Zakum Altar - Summons Zakum.
*/

function act() {
	rm.changeMusic("Bgm06/FinalFight");
	rm.getMap().spawnSimpleZakum(-10, -215);
	rm.mapMessage("因為火焰之眼的力量，炎魔被召喚了！！！");
	if (!rm.getPlayer().isGM()) {
		rm.getMap().startSpeedRun();
	}
}
