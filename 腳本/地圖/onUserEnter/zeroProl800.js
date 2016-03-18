/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    if (ms.getOneInfo(40010, "clear") === "clear") {
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
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getNPCTalk(["#face3#...這兩個麻煩的傢伙沒事拖延人家.浪費時間."], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/63");
    } else if (status === i++) {
        ms.getNPCTalk(["這卷軸...雖然有點老舊,但是明明有字跡在上面...快看看吧."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/64");
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/base/0", [1, 1, 1, 0, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["在某個稱為楓之谷的世界,有三位超越者."], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/transcendence/0", [1, 1, 1, 0, 0]);
        ms.getNPCTalk(["他們各自以光的超越者、生命的超越者、時間的超越者的名稱,在各自的領域裡構成世界並全力維持."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["楓之谷有一個任何人都不能知道的祕密,其中兩位個性寂靜的超越者雖然沒有興趣知道,但是另一位光的超越者卻想要挖掘這個祕密."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/transcendence/0", [1, 0]);
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/blackmagician/0", [1, 1, 1, 0, 0]);
        ms.getNPCTalk(["可是挖掘祕密的代價卻很殘酷,當他得知世界的祕密之後陷入墮落,以至於想要滅絕楓之谷..."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["還好經過多人的犧牲與努力,終於成功的封印光的超越者,停止了滅亡,但這只是權宜之計.滅亡卻仍在進行."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/blackmagician/0", [1, 0]);
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/hero/0", [1, 1, 1, 0, 0]);
        ms.getNPCTalk(["利用透視未來的力量得知這件事的時間的超越者,決定讓不同的未來誕生."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/hero/0", [1, 0]);
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/lune/0", [1, 1, 1, 0, 0]);
        ms.getNPCTalk(["這就是神使誕生的契機."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/lune/0", [1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但是黑魔法師覬覦神之子,他的部下蜘蛛王..."], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/base/0", [1, 0]);
        ms.getNPCTalk(["#b(紀錄在此中斷...)#k"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.updateInfoQuest(40010, "clear=clear");
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
