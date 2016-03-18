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
        ms.showEnvironment(7, "Bgm33.img/NastyLiar", [0]);
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.teachSkill(100001273, 1, 0); // 月之降臨
        ms.teachSkill(100000275, 1, 0); // 猛烈刺擊
        ms.teachSkill(100000278, 1, 0); // 暗影降臨
        ms.teachSkill(100001262, 1, 0); // 神殿回歸
        ms.teachSkill(100001263, 1, 0); // 時之威能
        ms.teachSkill(100001264, 1, 0); // 聖靈神速
        ms.teachSkill(100001265, 1, 0); // 爆裂跳躍
        ms.teachSkill(100001266, 1, 0); // 爆裂衝刺
        ms.teachSkill(100000267, 1, 0); // 時間凝結
        ms.teachSkill(100001268, 1, 0); // 時之庇護
        ms.teachSkill(100000282, 1, 0); // 雙重打擊
        ms.teachSkill(101000103, 7, 10); // 璃之力
        ms.teachSkill(101000203, 7, 10); // 琉之力
        ms.getDirectionEffect(1, "", [1000]);
        ms.getDirectionStatus(true);
        ms.gainItem(1003840, 1);
        ms.gainItem(1142634, 1);
    } else if (status === i++) {
        ms.showEnvironment(13, "zero/before1day", [0]);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getNPCTalk(["...因此,最後確認此次戰鬥中殲滅的 #o9300744#總共是13隻.到戰鬥結束一共花費 1小時 30分,我軍沒有損失."], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/0");
    } else if (status === i++) {
        ms.getNPCTalk(["比預估時間好像還要久一點,往後會盡力縮短時間."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/1");
    } else if (status === i++) {
        ms.getNPCTalk(["預計會花2小時以上,沒想到這麼快,真令人驚訝.不需要再縮短時間了.很好."], [3, 0, 2410001, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Kaison/0");
    } else if (status === i++) {
        ms.getNPCTalk(["這樣應該算是通過考驗了,你認為呢,#p2410002#攻擊隊長?#p2410008#既然隸屬攻擊隊,就請攻擊隊長決定吧."], [3, 0, 2410001, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Kaison/1");
        ms.getNPCTalk(["一共經過 9次的戰鬥能力評價結果,認為給騎士#p2410008#的戰鬥等級提升一階是正確的."], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Layla/0");
    } else if (status === i++) {
        ms.getNPCTalk(["一個人竟然能發揮一個部隊的實力,若還不讓他升級那就是我們的損失了.我贊成."], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Layla/1");
        ms.getNPCTalk(["繼最年輕的騎士之後誕生最年輕的S級騎士嗎?哈哈...我們影子騎士團所有的紀錄都被#p2410008#更新了.恭喜,#p2410008#."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Will/0");
    } else if (status === i++) {
        ms.getNPCTalk(["#face5#不,不是的.只是我的運氣比較好.謝謝大家的評價,往後還要更加努力,才不會辜負大家對我的期待."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/2");
        ms.getNPCTalk(["謙遜的璃也是影子騎士該具備的美德.希望你不要忘記這個心態.那麼交待下一個任務之前回去休息吧,#p2410008#."], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Layla/2");
        ms.getNPCTalk(["#face5#我...想跟各位隊長報告一件事情.我可以針對下一個任務提出看法嗎?"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/3");
    } else if (status === i++) {
        ms.getNPCTalk(["我聽說隸屬防禦隊的騎士#p2410004#在2週前的戰鬥中傷到了手肘,目前尚未復原."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/4");
        ms.getNPCTalk(["如果還沒有下一個任務交待給我的話,我想代替#p2410004#執行任務.剛好經過這次的戰鬥之後,短時間內將不會有#o9300744#的出沒,請允許我這個要求."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/5");
    } else if (status === i++) {
        ms.getNPCTalk(["你說防禦隊的#p2410004#...不就是負責運送糧食的騎士 #p2410004#嗎?#p2410008#,你想去運送糧食嗎?"], [3, 0, 2410001, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Kaison/2");
    } else if (status === i++) {
        ms.getNPCTalk(["是的,沒錯."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/6");
        ms.getDirectionEffect(2, "Effect/Direction6.img/effect/tuto/balloonMsg0/10", [1500, -14, 185, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction6.img/effect/tuto/balloonMsg0/10", [1500, 99, 180, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction6.img/effect/tuto/balloonMsg0/10", [1500, 322, 178, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getNPCTalk(["...運送糧食的任務是幾乎不會有戰鬥發生的,單純運送工作而已.只要從其它城鎮購買糧食回來就可以了."], [3, 0, 2410000, 0, 33, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Will/1");
    } else if (status === i++) {
        ms.getNPCTalk(["雖然是簡單的工作,但是影子森林本來就大,而且距離其它村莊又遠,所以需要花很長的時間...你有特別想要從事這任務的理由嗎,#p2410008#?"], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Will/2");
        ms.getNPCTalk(["#face10#嗯....大家都知道,因為我一直待在#m321000100#,從來都沒去過其它地方.想利用這個機會增長我的見聞."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/7");
    } else if (status === i++) {
        ms.getNPCTalk(["原來是這樣.說真的,#p2410008#也該是有這種想法的年紀了.但是..."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Will/3");
        ms.getNPCTalk(["#face0##p2410008#,你也知道我們影子騎士團身負的最大責任就是要從影子神殿誕生的#o9300744#手中守護這個世界."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Will/4");
    } else if (status === i++) {
        ms.getNPCTalk(["#face0##o9300744#是只要接觸就能消滅人類的危險怪物.若有一隻溜出影子森林,就能使整個世界陷入混亂."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Will/5");
        ms.getNPCTalk(["#face0#想想我們身負的任務重要性,將#p2410008# 這樣有才能的騎士投入微不足道的任務,豈不是耗損人力?"], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Will/6");
    } else if (status === i++) {
        ms.getNPCTalk(["還有...哈哈,也許#p2410008#有很多的期待,可是其它地區並沒有什麼特別.而且你是去負責運送糧食的話,忙著趕路更沒時間觀賞."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Will/7");
    } else if (status === i++) {
        ms.getNPCTalk(["不如請你更專注的擊退#o9300744#吧?當我們擊退所有的 #o9300744#永遠關閉影子神殿時,到時候你想去哪裡都可以."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Will/8");
    } else if (status === i++) {
        ms.getNPCTalk(["永遠都那麼成熟的#p2410008#沒想到也會說這麼可愛的事?哈哈...好像看到了#p2410008#的另外一面,所以感覺還不錯啊."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Will/9");
        ms.getNPCTalk(["因為目前事態比較危急,雖然不能把#p2410008#希望的任務交給你...但是我想#p2410008#應該能體諒."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Will/10");
    } else if (status === i++) {
        ms.getNPCTalk(["#face5#是的!我知道身為影子騎士的責任是第一要務.很抱歉,我的要求讓您擔憂了."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/8");
        ms.getNPCTalk(["你太客氣了.不好意思,在你很疲累的時候還留你這麼久.現在就回去休息吧."], [3, 0, 2410000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Will/11");
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/guide/0", [1, 1, 0, -410, 150]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
