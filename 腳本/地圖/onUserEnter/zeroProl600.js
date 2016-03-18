/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    if (ms.getOneInfo(40006, "clear") === "clear") {
        ms.dispose();
        return;
    }

    var i = -1;
    if (status <= i++) {
        ms.dispose();
    } else if (status === i++) {
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.showEnvironment(5, "Dojang/start", []);
        ms.getDirectionEffect(1, "", [5000]);
        ms.getDirectionStatus(true);
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/base/0", [1, 1, 1, 0, 0]);
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/boymeetgirl/0", [1, 1, 1, 0, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/boymeetgirl/0", [1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/base/0", [1, 0]);
        ms.showWZEffectNew("Effect/OnUserEff.img/questEffect/phantom/tutorial");
    } else if (status === i++) {
        ms.getNPCTalk(["#b(我能進入影子神殿完全是偶然.因為#o9300744#好像有話要說的樣子,一時的鬆懈就開始了這一切.)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/42");
    } else if (status === i++) {
        ms.getNPCTalk(["#b(與#o9300744#接觸後,我覺得我死定了...但是代替死亡出現的,卻是纖細的女人聲音.那個聲音告訴我要來影子神殿.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/43");
    } else if (status === i++) {
        ms.getNPCTalk(["#face11# #b(既然看到了奇異的現象,沒有向隊長報告就來到影子神殿的原因...也許是我在下意識裡已經對這個村莊存著懷疑.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/44");
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.showWZEffectNew("Effect/OnUserEff.img/questEffect/phantom/tutorial");
    } else if (status === i++) {
        ms.getNPCTalk(["#b(影子神殿內並沒有怪物.只有和失去光澤的石像一起被禁錮的閉上眼睛的少女...她的臉龐和我如此相像,難道只是偶然嗎?)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/45");
        ms.getNPCTalk(["#b(那天以後我對 #m321000100#小心翼翼的進行調查.影子神殿到底是什麼,#o9300744#的真相是什麼,經常吃的影子中和劑又是什麼...)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/46");
    } else if (status === i++) {
        ms.getNPCTalk(["#b(越是調查,增加的只有疑惑和問號.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/47");
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [100]);
    } else if (status === i++) {
        ms.getNPCTalk(["#face11# #b(確定的只有一件事.少女和我一定有關聯,證據很明顯.當我用力氣的時候,像這樣...就和少女產生共鳴.)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/48");
    } else if (status === i++) {
        ms.getTopMsg("請連按控制鍵,用時間之力喚醒不知名少女");
        ms.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/power/0", [3600, 451, -167, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/power/0", [3600, 358, -372, 1, 1, 0, 0, 0]);
        ms.showEnvironment(5, "Kite/HolyAuraE", []);
        ms.getDirectionEffect(4, "17#17#", [2, 2, 3000]);
    } else if (status === i++) {
        ms.getTopMsg("請連按控制鍵,用時間之力喚醒不知名少女");
        ms.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/power/0", [3600, 451, -167, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/power/0", [3600, 358, -372, 1, 1, 0, 0, 0]);
        ms.showEnvironment(5, "Kite/HolyAuraE", []);
        ms.getDirectionEffect(4, "17#17#", [2, 2, 3000]);
    } else if (status === i++) {
        ms.getTopMsg("請連按控制鍵,用時間之力喚醒不知名少女");
        ms.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/power/1", [3600, 451, -167, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/power/1", [3600, 358, -372, 1, 1, 0, 0, 0]);
        ms.showEnvironment(5, "Kite/HolyAuraE", []);
        ms.getDirectionEffect(4, "17#17#", [2, 2, 3000]);
    } else if (status === i++) {
        ms.getTopMsg("請連按控制鍵,用時間之力喚醒不知名少女");
        ms.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/power/2", [3600, 451, -167, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/power/2", [3600, 358, -372, 1, 1, 0, 0, 0]);
        ms.showEnvironment(5, "Kite/HolyAuraE", []);
        ms.getDirectionEffect(4, "17#17#", [2, 2, 3000]);
    } else if (status === i++) {
        ms.getTopMsg("請連按控制鍵,用時間之力喚醒不知名少女");
        ms.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/power/3", [0, 451, -167, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/power/3", [0, 358, -372, 1, 1, 0, 0, 0]);
        ms.showEnvironment(5, "Kite/HolyAuraE", []);
        ms.getDirectionEffect(4, "17#17#", [2, 2, 3000]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getNPCTalk(["#face11# #b(利用這力量應該就能喚醒少女...預感?不,這是確信.可是力量還不夠嗎,少女還是沒有醒來...)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/49");
    } else if (status === i++) {
        ms.getNPCTalk(["#face11# #b(是因為這力量嗎?所以影子村的人欺瞞我?影子村...只是為了欺瞞我才存在的村莊嗎?)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/50");
    } else if (status === i++) {
        ms.getNPCTalk(["#face11# #b(依附在他人注入的錯誤知識中生存的生命...這豈不就像懸吊在繩子上的玩偶嗎?)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/51");
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.showWZEffectNew("Effect/OnUserEff.img/questEffect/phantom/tutorial");
    } else if (status === i++) {
        ms.getNPCTalk(["#face3# #b(但是...我不能永遠停留在這狀態中.把我變成笑話的人,讓我變得如此不堪的人...我豈能放任他們?)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/52");
    } else if (status === i++) {
        ms.getNPCTalk(["#face3# #b(當我挖掘到所有隱藏的真相之後,將我所受的恥辱與憤怒加倍奉還給他們.總有一天...一定會!)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/53");
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/sparkle/0", [0, 723, -202, 1, 1, 0, 0, 0]);
        ms.getNPCTalk(["#face11# #b(咦?後面有東西...好像看到了閃爍的東西.)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/54");
    } else if (status === i++) {
        ms.updateInfoQuest(40006, "clear=clear");
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
