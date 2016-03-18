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
        ms.getDirectionEffect(1, "", [500]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["...就這樣,時間的女神優伊娜被企圖滅絕世界的黑魔法師與其軍團長封印.雖然能透視世界的未來,卻無法改變自己的命運...這就是時間的超越者擁有的極限."], [3, 0, 2400000, 0, 33, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Pieng/0");
    } else if (status === i++) {
        ms.getNPCTalk(["但是女神也不是沒有做任何的準備.代替女神成為時間的超越者,而且被認為該產生擁有與祂#b不同未來的繼承人#k, #b神之子#k就這樣誕生了."], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Pieng/1");
    } else if (status === i++) {
        ms.getNPCTalk(["狡猾的黑魔法師並沒有放過神之子的誕生.在剛出生的神之子擁有自我之前,透過軍團長禁錮神之子讓他無法覺醒."], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Pieng/2");
    } else if (status === i++) {
        ms.getNPCTalk(["所以兩位到目前為止對使兩位誕生的女神,還有為兩位建立的這神殿,以及對自己都毫無所知.嗯,這樣明白了嗎?"], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Pieng/3");
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.showWZEffectNew("Effect/OnUserEff.img/questEffect/phantom/tutorial");
    } else if (status === i++) {
        ms.getNPCTalk(["OK.到這裡都能理解.所以說,我就是時間女神優伊娜的繼承人是嗎?而女神呢,被黑魔法師還是什麼的敵人以及他的部下所封印,而我因為軍團長就這樣一無所知的活著."], [3, 0, 2400005, 0, 41, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/100");
        ms.getNPCTalk(["可是我想知道的不止是這個.當然那也想知道,但是現在重點不是這個.就算我是神之子也好.可是...#face1#為什麼我突然會變成兩個呢?!這傢伙也是我?!"], [3, 0, 2400005, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/101");
    } else if (status === i++) {
        ms.getNPCTalk(["是,哇哈哈.剛剛也說過,女神優伊娜剛開始讓 #r神之子誕生時,明明是只有一個#k.所以我們也都準備好要侍奉一位.但是因為軍團長的影響,原來的一個神之子變成兩個."], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
        ms.showWZEffect("Effect/Direction13.img/zeroPrologue/Emotion5");
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Pieng/4");
        ms.getNPCTalk(["黑魔法師的軍團長.雖然他擁有強大的力量,但是他也無力抵擋要成為超越者的兩位.所以他選擇的方法.就是把#r神之子分成兩個#k."], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Pieng/5");
    } else if (status === i++) {
        ms.getNPCTalk(["一分為二的神之子,各自擁有一半的時間之力.將能力減弱的兩者之一關在神殿內與神殿一起幽禁,讓她無法醒來...另外一個就直接洗腦,阻擋他的覺醒."], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Pieng/6");
    } else if (status === i++) {
        ms.getNPCTalk(["可是怎麼會連兩位的性別都不同,這我也不太清楚.也許是在分開的過程中,有無法預測的力量介入?當然,兩位無論是男是女,我都喜歡.哈哈."], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Pieng/7");
        ms.getNPCTalk(["...所以...結論是,她是我,我也是我是吧...因為不好區別,所以她是 #p2400005#,我就是 #p2400006#..."], [3, 0, 2400006, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Beta/0");
    } else if (status === i++) {
        ms.getNPCTalk(["知道了.那現在開始要做什麼呢?"], [3, 0, 2400006, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Beta/1");
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/base/0", [1, 1, 1, 0, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#face0#別這麼酷的帶過好嗎!我給自己取名叫神之子了!#b將重複9次的過去化為烏有重頭開始的#k意思就是神之子!可是為什麼我要被叫做 #p2400005#呢?!"], [3, 0, 2400005, 0, 41, 0, 0, 1, 0]);
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/displeased/0", [1, 1, 1, 0, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/102");
        ms.getNPCTalk(["那你做 #p2400006#.我做 #p2400005#好了."], [3, 0, 2400006, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Beta/2");
    } else if (status === i++) {
        ms.getNPCTalk(["#face0#我不是那個意思嘛!"], [3, 0, 2400005, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/103");
        ms.getDirectionEffect(1, "", [500]);
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/displeased/0", [1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["哈啊,哈啊...我白白叫了呀.你對這種狀況沒有不滿嗎?一副若無其事的表情?"], [3, 0, 2400005, 0, 41, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.showWZEffect3("Effect/Direction13.img/effect/zero/base/0", [1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/104");
        ms.getNPCTalk(["這個...我不明白這情況有什麼不好.你何必生這麼大的氣呢?"], [3, 0, 2400006, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Beta/3");
    } else if (status === i++) {
        ms.getNPCTalk(["#face1#這當然是...!...喂,她真的是我嗎?個性完全不同?我從來都不會這麼傻傻的!"], [3, 0, 2400005, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/105");
        ms.getDirectionEffect(2, "Effect/Direction6.img/effect/tuto/balloonMsg0/10", [207, -339, 185, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction6.img/effect/tuto/balloonMsg0/10", [372, -367, 185, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction6.img/effect/tuto/balloonMsg0/10", [489, -333, 185, 1, 1, 0, 0, 0]);
        ms.getNPCTalk(["個性是會受到成長環境影響的.兩位在不同環境下長大,個性當然會不同.除此之外,其他部分都相同!"], [3, 0, 2400007, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Benedict/0");
    } else if (status === i++) {
        ms.getNPCTalk(["感受到的時間之力,也完全相同.兩位確定是完全一樣.其實除了性別不同,長相也幾乎一樣."], [3, 0, 2400008, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Casillas/0");
    } else if (status === i++) {
        ms.getNPCTalk(["很抱歉,兩位重新變成一個的方法目前還不得知.超越者一分為二從未發生過,我是第一次經歷.關於時間神殿的眾多紀錄當中也沒有這種情況."], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Pieng/8");
    } else if (status === i++) {
        ms.getNPCTalk(["但是老實說,我有點開心...因為神之子有兩位.一個已經很好了,可是有兩位...這要叫什麼是 #r雙手捧花#k嗎?從來沒想到會有這麼美妙的情況來臨..."], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Pieng/9");
        ms.showEnvironment(5, "Kite/Storm", []);
        ms.getNPCTalk(["#face7#嗯,這...好吧...所以關於你的興趣,可以不用再說了!接下來,談下一個話題吧!所以說,這裡不是真實的世界嗎?"], [3, 0, 2400005, 0, 41, 0, 1, 1, 0]);
        ms.showWZEffect("Effect/Direction13.img/zeroPrologue/Emotion7");
        ms.playVoiceEffect("Voice.img/Alpha/106");
    } else if (status === i++) {
        ms.getNPCTalk(["沒錯.不是說過黑魔法師與時間女神,軍團長...都是楓之谷的事情,而現在這裡不是楓之谷了嘛....那麼,這裡是哪裡呢?"], [3, 0, 2400006, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Beta/4");
        ms.getNPCTalk(["啊啊,是.就是這樣沒錯.這裡是原來我們居住的世界...不是楓之谷.這裡是黑魔法師與軍團長為了禁錮我們製作的監獄的世界...稱為#b鏡子世界#k."], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Pieng/10");
    } else if (status === i++) {
        ms.getNPCTalk(["神之子能夠真正發揮超越者能力的地方,只有超越者所屬的世界,就是楓之谷.知道這個事實後,敵人為了阻擋兩位的力量,創造了這鏡子世界."], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Pieng/11");
        ms.getNPCTalk(["雖然是創造的,但不是很完整.就像是把部分楓之谷映在鏡子裡複製的一樣,是個又小又不穩定的假象世界而已."], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Pieng/12");
    } else if (status === i++) {
        ms.getNPCTalk(["還好兩位身為神之子,不僅自我覺醒,而且因 #b鏡子世界的部分碎裂後已與楓之谷有了#k連接.右邊看到的這個門就是那個通路."], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Pieng/13");
    } else if (status === i++) {
        ms.getNPCTalk(["可是很可惜的是利用這個門去楓之谷也是徒然.因為目前兩位是被束縛在這鏡子世界裡.喏...百聞不如一見.讓我來打開這個門吧."], [3, 0, 2400000, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Pieng/14");
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.forceStartQuest(40901);
        ms.lockUI(0);
        ms.dispose();
        ms.warp(320000000, 4);
    } else {
        ms.dispose();
    }
}
