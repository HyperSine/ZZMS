/* global qm */
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
    } else if (status === i++) {
        qm.getNPCTalk(["咭咭∼咭，咭！咭咭咭！咭噫~！咭！咭？！"], [4, 1096003, 0, 0, 0, 0, 0, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["我是為了成為冒險家而來到楓之島…怎麼會變成這樣？"], [4, 1096003, 0, 0, 2, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["咭咭∼咭，咭！咭噫~！咭咭咭咭！"], [4, 1096003, 0, 0, 0, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["和船長聊天後並在週邊看看…對了！巴洛古！巴洛古出現了！還有我從船那邊掉了下來？但是怎麼會平安無事呢？雖然會#b游泳但是#k不可能在這樣的過程中還會打起精神游泳吧…難道我是個游泳神童？"], [4, 1096003, 0, 0, 2, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["咭咭咭咭咭！咭！咭！(小小隻的猴子似乎有很多不滿，而蹬著腿，看起來還很小，但是仔細一看，像是眼睛張開後看到的人一樣...)"], [4, 1096003, 0, 0, 0, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["耶…？這樣一看你從剛剛開始就有很多想說的話？在你手裡的東西... (猴子從懷裡拿出蘋果，看起來非常好吃的蘋果，但是…這蘋果是？)\r\n\r\n#i2010000#"], [4, 1096003, 0, 0, 2, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["咭咭咭咭∼骨碌！(猴子用鬱悶的面拿出蘋果並做出吃的動作，難道…知道我體力下降而要我吃！真是個好傢伙！)"], [4, 1096003, 0, 15, 0, 0]);
    } else if (status === i++) {
        qm.forceStartQuest(2561);
        qm.getNPCTalk(["(拿到看起來很好吃的水果，一口喀擦~吃掉，按下#bI#k鍵的話，就可以打開背包~？)"], [4, 1096003, 0, 0, 2, 0, 0, 1, 0]);
        qm.gainItem(2010000, 1);
    } else if (status === i++) {
        qm.showWZEffectNew("UI/tutorial.img/cannon/2");
	qm.forceCompleteQuest();
        qm.dispose();
    } else {
        qm.dispose();
    }
}