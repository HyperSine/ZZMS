/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    if (!ms.isQuestActive(38002) || ms.getQuestCustomData(38002) === "clear") {
        ms.dispose();
        return;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.getNPCTalk(["呼,這是怎麼一回事啊?家被改成新名字了...... 現在不是我吃驚的時候.大伙們都還活著嗎?從我還沒死的這點看來,封印黑魔法師一事失敗的機率很高.那麼大家現在......."], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["不對.他們不會那麼容易死.一定會堅強的活著.所以現在想想自己吧.先解決我遇到的情況吧.反正依現在的狀態,我只會成為累贅."], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["等級10......雖然比死來得好,但心情也愉快不起來.必須先停留在這裡找回力量才行.因為這是我現在唯一能做的事."], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.forceStartQuest(38002, "clear");
        ms.dispose();
    } else {
        ms.dispose();
    }
}
