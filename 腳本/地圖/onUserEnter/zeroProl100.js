/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    if (ms.getQuestStatus(40005) !== 0) {
        ms.dispose();
        return;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.showEnvironment(13, "zero/before16hour", [0]);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getNPCTalk(["緊急任務!"], [3, 0, 2410002, 0, 33, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Layla/3");
        ms.getNPCTalk(["收到#o9300744#突然出沒的消息!到目前為止,確認的數量是9隻!數量不少!請準備好立即投入戰鬥!"], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Layla/4");
    } else if (status === i++) {
        ms.getNPCTalk(["若臨時改變其它戰鬥配置可能會誤事,所以這次戰鬥編制仍維持和之前相同!攻擊主力就交給騎士#p2410008#!#p2410008#,沒問題吧?"], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Layla/5");
    } else if (status === i++) {
        ms.getNPCTalk(["那麼就請#p2410008#來我這裡聽取詳細內容!"], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Layla/6");
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.showWZEffectNew("Effect/OnUserEff.img/questEffect/phantom/tutorial");
    } else if (status === i++) {
        ms.getNPCTalk(["#face11# #b(果然是這樣...我就知道會是這樣.什麼#o9300744#突然出沒?若是以前,我也許會上當.)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/13");
        ms.getNPCTalk(["#face11# #b(他們只有在需要時才會擊退#o9300744#.就像現在.想把我的心思轉移到其它地方的時候.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/14");
    } else if (status === i++) {
        ms.getNPCTalk(["#face11# #b(毫無意義的命令與毫無意義的作戰...可是我只能服從.在找出他們的目的之前...)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/15");
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.updateInfoQuest(40900, "");
        ms.forceStartQuest(40900);
        ms.forceStartQuest(40005, "1");
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
