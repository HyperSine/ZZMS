/* global cm */
var status = -1;

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (status <= i++) {
        cm.dispose();
    } else if (status === i++) {
        cm.getDirectionStatus(true);
        cm.lockUI(1, 1);
        cm.showEnvironment(7, "Bgm33.img/ShadowKnight", [0]);
        cm.getDirectionEffect(3, "", [0]);
        cm.getDirectionEffect(1, "", [2000]);
        cm.showWZEffectNew("Effect/OnUserEff.img/questEffect/phantom/tutorial");
    } else if (status === i++) {
        cm.getNPCTalk(["#face11# #b(這次沒辦法離開村莊了嗎...)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        cm.playVoiceEffect("Voice.img/Alpha/9");
    } else if (status === i++) {
        cm.getNPCTalk(["#face11# #b(#o9300744#?要守護世界?是因為我有能力,所以不讓我離開村莊?為什麼每個人都被允許的事情只有我不被允許?)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        cm.playVoiceEffect("Voice.img/Alpha/10");
    } else if (status === i++) {
        cm.getNPCTalk(["#face11# #b(他們是在騙我.他們不希望我離開這裡,不擇手段要把我永遠留在這裡!)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        cm.playVoiceEffect("Voice.img/Alpha/11");
    } else if (status === i++) {
        cm.getNPCTalk(["#face11# #b(也許待會他們就會交代我新的任務,讓我忙得沒有心思去想別的.他們就是這樣一直把我關在這村子裡.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        cm.playVoiceEffect("Voice.img/Alpha/12");
        cm.lockUI(0);
        cm.dispose();
        cm.warp(321000100, 0);
    } else {
        cm.dispose();
    }
}
