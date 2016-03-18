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
        ms.spawnNPCRequestController(2410004, 1170, -16, 1, 11601142);
        ms.forceStartQuest(40011, "2");
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.getDirectionStatus(true);
        ms.showWZEffectNew("Effect/OnUserEff.img/questEffect/phantom/tutorial");
    } else if (status === i++) {
        ms.getNPCTalk(["#b(除了留在村裡的幾個人之外,都出發去了影子神殿...)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/81");
    } else if (status === i++) {
        ms.getNPCTalk(["#face11# #b(可惡...叫誰乖乖待著呀!為什麼突然要襲擊神殿呢?難道...發現了我的行動嗎?)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/82");
    } else if (status === i++) {
        ms.getNPCTalk(["#face11# #b(...那麼這是陷阱...他們的目標不是神殿裡的女人而是我...但是...)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/83");
    } else if (status === i++) {
        ms.getNPCTalk(["#face11# #b(如果我不出現的話就會除掉那女人,或者是強化警戒讓我無法再進入神殿.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/84");
        ms.getNPCTalk(["#face11# #b(放下真相...然後放棄復仇傻傻的待在這村莊...還是明知這是陷阱也要進去先救出那女人...)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/85");
    } else if (status === i++) {
        ms.getNPCTalk(["#b(是該選擇的時候嗎?...如何是好呢?)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/86");
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
