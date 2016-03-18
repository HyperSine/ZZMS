/* global ms */
var status = -1;

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
        ms.disableOthers(true);
        ms.getTopMsg("雜貨商店後院");
        ms.getDirectionEffect(2, "Effect/Direction7.img/effect/tuto/step0/4", [2000, 0, -100, 1, 0, -100]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionFacialExpression(6, 10000);
        ms.getNPCTalk(["咦！ 有人耶！ 那個少女是誰啊？"], [4, 1106005, 0, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["你是誰？迷路了嗎？ "], [4, 1106005, 0, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["找你好久了。好不容易才找到擁有光之命運的人。"], [4, 1106005, 0, 0, 5, 0, 1, 1, 0, 1106001]);
    } else if (status === i++) {
        ms.getNPCTalk(["你在說什麼？ 光之命運？"], [4, 1106005, 0, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["有禮貌的少年！ 這個人是個高貴的人！"], [4, 1106005, 0, 0, 5, 0, 1, 1, 0, 1106003]);
    } else if (status === i++) {
        ms.getNPCTalk(["啊！ 你是不久前來商店的…對了！想你來了。你說過吧？ 問我知不知道雷神之錘。不久前我在商店的閣樓看到他寄來的信。不曉得是不是那個人…不過林伯特或許知道關於那個人的事。如果去找林伯特…"], [4, 1106005, 0, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["雷神之錘…那個人和林伯特無關，是和你有關的人。正是你的父親…"], [4, 1106005, 0, 0, 5, 0, 1, 1, 0, 1106001]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯？你說什麼？ 不…你說什麼？可是我對父親沒有任何記憶。他在我小時候就離開此地了…"], [4, 1106005, 0, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["他並沒有遺棄你。你的父親-聖殿騎士雷神之錘失去心愛的妻子，因為太過悲傷了被困在黑暗之中。光和黑暗只有很微小的差異。在黑暗把你帶領走向悲劇之前，將你託付在此地。結果他無法逃離黑暗喪命了…"], [4, 1106005, 0, 0, 5, 0, 1, 1, 0, 1106001]);
    } else if (status === i++) {
        ms.getNPCTalk(["你來救我嗎？錯了。我的生活也很黑暗。至今為止我沒有名字，被關在小商店內，內心等待著沒回來的父親度日。"], [4, 1106005, 0, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["光是從黑暗之中誕生就像雙面刃…他在黑暗裡面，因此你就能成為光。跟我一起走吧！有個你要待的地方。"], [4, 1106005, 0, 0, 5, 0, 1, 1, 0, 1106001]);
    } else if (status === i++) {
        ms.getNPCTalk(["等一下！女皇。我對這名少年還不能百分之百確信，要確認他是否具有成為真正聖殿騎士的資格。"], [4, 1106005, 0, 0, 5, 0, 1, 1, 0, 1106003]);
    } else if (status === i++) {
        ms.getNPCTalk(["那因哈特還抱持懷疑...很好。我允許你的方式，可是不能讓他受傷。"], [4, 1106005, 0, 0, 5, 0, 1, 1, 0, 1106001]);
    } else if (status === i++) {
        ms.getNPCTalk(["等等！你們現在想做什麼？"], [4, 1106005, 0, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.disableOthers(false);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
