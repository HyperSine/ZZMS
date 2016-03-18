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
        qm.getDirectionStatus(true);
        qm.lockUI(1, 1);
        qm.getNPCTalk(["同樣的戰鬥配置已經進行過很多次,應該都很熟悉,但是再說一次.這次任務的攻擊隊成員只有#p2410008#一人,其它騎士全都是防禦編制."], [3, 0, 2410002, 0, 33, 0, 0, 1, 0]);
        qm.playVoiceEffect("Voice.img/Layla/7");
    } else if (status === i++) {
        qm.getNPCTalk(["防禦編制的騎士為防止意外狀況,只在指定地區等待,基本上這任務的成功與否,由構成軍隊主力的攻擊手決定."], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Layla/8");
    } else if (status === i++) {
        qm.getNPCTalk(["任務的最終目標是完全殲滅新發現的#r9隻#k#r#o9300744##k之後平安歸還.發現位置在#b#m321000300##k區域."], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Layla/9");
        qm.getNPCTalk(["很抱歉無法讓你好好的休息.那就直接參與戰鬥吧.你也知道,沒有影子中和劑是不被允許加入戰鬥的."], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Layla/10");
    } else if (status === i++) {
        qm.getDirectionEffect(3, "", [0]);
        qm.getDirectionEffect(1, "", [2000]);
        qm.showWZEffectNew("Effect/OnUserEff.img/questEffect/phantom/tutorial");
    } else if (status === i++) {
        qm.getNPCTalk(["#face11# #b(影子中和劑.就是影子騎士和#o9300744#戰鬥之前都會服用的藥物.效用很單純.就是服下該藥物後即使與#o9300744#接觸也暫時不會被消滅.)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        qm.playVoiceEffect("Voice.img/Alpha/16");
    } else if (status === i++) {
        qm.getNPCTalk(["#face11# #b(影子騎士團把這藥物看得相當重要.未服用影子中和劑就進行戰鬥會被視為1級罪犯而遭受處罰.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/17");
        qm.getNPCTalk(["#face11# #b(就是因為這樣.到目前為止我只能在這充滿矛盾的村莊毫無選擇活下來的原因...)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Alpha/18");
    } else if (status === i++) {
        qm.getNPCTalk(["#face11# #b(總之先收下藥吧.如果不接受的話,連戰鬥都不能參加.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/19");
        qm.forceStartQuest();
        qm.lockUI(0);
        qm.dispose();
    } else {
        qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.getDirectionStatus(true);
        qm.lockUI(1, 1);
        qm.getNPCTalk(["啊,原來是#p2410008#.這次已經是第九次個別任務了吧?小小的失誤可能導致大危機,所以請不要鬆懈.其實以#p2410008#的實力來看也沒有危機可言了,哈哈..."], [3, 0, 2410000, 0, 33, 0, 0, 1, 0]);
        qm.playVoiceEffect("Voice.img/Will/12");
    } else if (status === i++) {
        qm.getNPCTalk(["喏,這是影子中和劑.你知道持續時間吧?只有 3小時...所以在那之前務必要返回村莊.你現在就要服下中和劑嗎?"], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Will/13");
        qm.getNPCTalk(["#face5#不...這次的怪物數量稍微多一點,所以我想在戰鬥之前服下.謝謝您的提醒."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Alpha/20");
    } else if (status === i++) {
        qm.getNPCTalk(["#face0#不客氣,#p2410008#.不要忘記服下藥劑.絕對不能忘了."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Will/14");
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.getDirectionEffect(3, "", [2]);
        qm.getDirectionEffect(1, "", [8000]);
    } else if (status === i++) {
        qm.getDirectionEffect(3, "", [0]);
        qm.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        qm.getNPCTalk(["#face11# #b(絕對不要忘記...是吧.這當然不能忘記.只有服下這藥物才會被控制.)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        qm.playVoiceEffect("Voice.img/Alpha/21");
    } else if (status === i++) {
        qm.getNPCTalk(["#face11# #b(不久前才知道一輩子不曾懷疑過就服下的這藥物成份,居然是使人類的精神渙散,容易被洗腦的藥物.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/22");
        qm.getNPCTalk(["#face11# #b(從那以後,我...再也沒有吃過這藥.經過幾次戰鬥之後,我仍然沒有被消滅.這不是因為我的實力不凡...)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Alpha/23");
    } else if (status === i++) {
        qm.getNPCTalk(["#face11# #b(#o9300744#一開始就沒有消滅人類的能力.影子騎士團的工作從一開始就毫無意義.)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/24");
        qm.getTopMsg("連按控制鍵,用時間之力稀釋洗腦藥");
        qm.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/break/0", [3600, 824, -6, 1, 1, 1, 0, 0]);
        qm.getDirectionEffect(4, "17#17#17#", [2, 2, 2000]);
    } else if (status === i++) {
        qm.getTopMsg("連按控制鍵,用時間之力稀釋洗腦藥");
        qm.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/break/0", [3600, 824, -6, 1, 1, 1, 0, 0]);
        qm.getDirectionEffect(4, "17#17#17#", [2, 2, 2000]);
    } else if (status === i++) {
        qm.showEnvironment(5, "Cokeplay/Broken", []);
        qm.getTopMsg("連按控制鍵,用時間之力稀釋洗腦藥");
        qm.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/break/1", [3600, 824, -6, 1, 1, 1, 0, 0]);
        qm.getDirectionEffect(4, "17#17#17#", [2, 2, 2000]);
    } else if (status === i++) {
        qm.showEnvironment(5, "Cokeplay/Broken", []);
        qm.getTopMsg("連按控制鍵,用時間之力稀釋洗腦藥");
        qm.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/break/2", [3600, 824, -6, 1, 1, 1, 0, 0]);
        qm.getDirectionEffect(4, "17#17#17#", [2, 2, 2000]);
    } else if (status === i++) {
        qm.showEnvironment(5, "Cokeplay/Broken", []);
        qm.getTopMsg("連按控制鍵,用時間之力稀釋洗腦藥");
        qm.getDirectionEffect(2, "Effect/Direction13.img/effect/zero/break/3", [3600, 824, -6, 1, 1, 1, 0, 0]);
        qm.getDirectionEffect(4, "17#17#17#", [2, 2, 2000]);
    } else if (status === i++) {
        qm.showEnvironment(5, "Cokeplay/Broken", []);
        qm.getNPCTalk(["#face11# #b(...現在就去執行任務吧.)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        qm.playVoiceEffect("Voice.img/Alpha/25");
    } else if (status === i++) {
        qm.lockUI(0);
        qm.dispose();
    } else {
        qm.dispose();
    }
}
