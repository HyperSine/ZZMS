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
        ms.showEnvironment(13, "zero/before1hour", [0]);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["#b(突然間有什麼事呢?...既然昨天和前天連續戰鬥過,應該會緩和一段時間才正常.)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/71");
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getNPCTalk(["雖然很突然,這是重要發表!請大家注意!現在開始要進行從未有過的作戰計畫!命名為抹殺影子神殿計畫!"], [3, 0, 2410001, 0, 33, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Kaison/3");
    } else if (status === i++) {
        ms.getNPCTalk(["作戰計畫的核心很簡單!就是直接滲透到 #m321000600#裡面殲滅 #o9300744#!...請不要喧嘩!"], [3, 0, 2410001, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Kaison/4");
        ms.getDirectionEffect(2, "Effect/Direction5.img/effect/mercedesInIce/merBalloon/5", [2000, 0, -100, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getNPCTalk(["#face7# #b(可惡,說什麼?要攻擊 #m321000600#?!)"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/72");
        ms.getNPCTalk(["我能體諒大家的驚訝!但是最近在 #m321000600#發現了之前沒有的狀態,而且這狀態昨天達到最高峰!"], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Layla/11");
    } else if (status === i++) {
        ms.getNPCTalk(["因此我們的影子騎士團隊長認為不能坐視不管,決定要立即攻擊,將 #m321000600#回復到之前的狀態."], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Layla/12");
        ms.getNPCTalk(["當然可能會有危險!可是必須甘冒危險守護世界是我們的責任!有任何異議,請在作戰結束後提出!"], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Layla/13");
    } else if (status === i++) {
        ms.getNPCTalk(["如果這次作戰成功結束的話,也有可能完全的消滅掉#o9300744#!所以我們會盡全力完成!"], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Layla/14");
        ms.getNPCTalk(["#face7# #b(最近影子神殿發現的之前沒有的狀態... 難道是我進神殿的事被發現了嗎?)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/73");
    } else if (status === i++) {
        ms.getNPCTalk(["#face7# #b(不行...!圍繞著我的這一切真相才正要開始解開呢...)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/74");
        ms.getNPCTalk(["等等,#p2410008#.請你到這邊來好嗎?"], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Will/15");
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [4000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getNPCTalk(["#face0#你到昨天為止連續進行了兩場個別戰鬥.我們認為你不適合參加這次的任務."], [3, 0, 2410000, 0, 33, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Will/16");
        ms.getNPCTalk(["#face0#很可惜,但是希望你這次任務就留在後方支援部隊."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Will/17");
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction5.img/effect/mercedesInIce/merBalloon/5", [2000, 0, -100, 1, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getNPCTalk(["#face1#要我留守後方部隊?我隸屬攻擊隊而且到目前為止從來沒有留守過後方部隊...!"], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/75");
    } else if (status === i++) {
        ms.getNPCTalk(["這我們當然都知道...但是 #p2410008#在昨天的任務中狀態也是很不好,甚至很驚險的回來不是嗎?以目前狀態不適合戰鬥."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Will/18");
        ms.getNPCTalk(["#face1#昨天只是花太多時間追蹤,而我現在也沒有任何問題!請讓我參加作戰!"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/76");
    } else if (status === i++) {
        ms.getNPCTalk(["...你真的想要參加作戰嗎...說實在,#p2410008#從小就是影子騎士,且一向都看重影子騎士的責任.沒錯吧?"], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Will/19");
    } else if (status === i++) {
        ms.getNPCTalk(["那好吧...喏,這是影子中和劑.若要參加作戰就該服下吧?"], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Will/20");
        ms.getNPCTalk(["#face11# #b(...影子中和劑...!那只是洗腦藥而已.如果現在服下它也許過去的追蹤就化為烏有了...絕對不能服下...!)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/77");
    } else if (status === i++) {
        ms.getNPCTalk(["#face2#你在猶豫什麼,#p2410008#?難道是...你不想服下影子中和劑就戰鬥嗎?即使 #p2410008#的實力再卓越也絕不會允許這麼做."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Will/21");
        ms.getNPCTalk(["#face11# #b(那個眼神...不會是發現了我在懷疑吧?可惡...怎麼辦?)"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/78");
    } else if (status === i++) {
        ms.getNPCTalk(["#face10#...仔細思考...如你所說,我怕參加戰鬥後變成他人的累贅就麻煩了."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/79");
        ms.getNPCTalk(["#face5#抱歉打擾了,#p2410000# 醫務隊長.這次我就在後方支援部隊等待吧."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/80");
    } else if (status === i++) {
        ms.getNPCTalk(["很好,#p2410008#.那任務結束前就好好待著吧.直到我們回來為止."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Will/22");
        ms.lockUI(0);
        ms.dispose();
        ms.warp(321001000, 0);
    } else {
        ms.dispose();
    }
}
