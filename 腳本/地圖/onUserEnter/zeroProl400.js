/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    if (ms.getOneInfo(40008, "act1") === "clear") {
        ms.dispose();
        return;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.getDirectionStatus(true);
        ms.showWZEffectNew("Effect/OnUserEff.img/questEffect/phantom/tutorial");
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getNPCTalk(["#b(這裡就是影子神殿.是 #o9300744#誕生的地方.不知是供奉了誰,現在已成廢墟的神殿.)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/32");
    } else if (status === i++) {
        ms.getNPCTalk(["#b(沒有人知道影子神殿內有什麼.因為進去的人只有一死...現在即使是影子騎士也不被允許進入神殿內部.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/33");
    } else if (status === i++) {
        ms.getNPCTalk(["#b(人們都說,影子神殿內有怪物.這怪物目前雖然在沉睡...但是醒來時會讓世界滅亡.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/34");
        ms.getNPCTalk(["#face11# #b(明明就是謊言.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/35");
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getNPCTalk(["#face11# #b(我已經知道這神殿裡有什麼...應該是說知道是誰在這裡.)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/36");
        ms.getNPCTalk(["#face11# #b(快進入神殿內部吧.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/37");
    } else if (status === i++) {
        ms.updateOneInfo(40008, "act1", "clear");
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
