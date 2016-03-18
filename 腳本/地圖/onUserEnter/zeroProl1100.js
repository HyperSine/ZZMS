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
        ms.showEnvironment(13, "zero/now", [0]);
        ms.getDirectionStatus(true);
        ms.lockUI(1, 1);
        ms.spawnNPCRequestController(2410001, 1344, -31, 0, 11603140);
        ms.spawnNPCRequestController(2410002, 1461, -31, 0, 11603141);
        ms.spawnNPCRequestController(2410010, 1254, -31, 0, 11603142);
        ms.spawnNPCRequestController(2410010, 1532, -31, 0, 11603143);
        ms.spawnNPCRequestController(2410010, 1625, -31, 0, 11603144);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["住手,#p2410008#,到此為止."], [3, 0, 2410002, 0, 33, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Layla/15");
    } else if (status === i++) {
        ms.getNPCTalk(["竟然違反後方支援命令來到這裡...出了什麼事嗎?"], [3, 0, 2410001, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Kaison/5");
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.showWZEffectNew("Effect/OnUserEff.img/questEffect/phantom/tutorial");
    } else if (status === i++) {
        ms.getNPCTalk(["...呼.這次就當作是你血氣方剛,原諒你一次.回去吧,這裡不是你該待的地方."], [3, 0, 2410001, 0, 33, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Kaison/6");
    } else if (status === i++) {
        ms.getNPCTalk(["#face3#那麼哪裡才是我該待的地方呢?是欺騙我的...你們所在的#m321000100#嗎?"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/94");
        ms.getNPCTalk(["你在說什麼,你!沒有任何人欺騙你!"], [3, 0, 2410001, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Kaison/7");
    } else if (status === i++) {
        ms.getNPCTalk(["#face3#哼.現在就露出真面目吧.反正你也不會讓我活著.原諒?現在是在後面準備捅一刀再原諒嗎?"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/95");
        ms.spawnNPCRequestController(2410005, 980, -335, 1, 11603492);
        ms.getNPCTalk(["...被發現 ...了嗎?"], [3, 0, 2410005, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Simus/0");
    } else if (status === i++) {
        ms.getNPCTalk(["嘻,#p2410005#只是以防萬一的準備而已,請不要誤會.趕快放下武器和我們回 #m321000100#去吧."], [3, 0, 2410001, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Kaison/8");
        ms.getNPCTalk(["#face2#廢話少說!你以為我笨到還會相信你那些話嗎?你們的藥我已經很久沒吃了!"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/96");
    } else if (status === i++) {
        ms.getNPCTalk(["呵...如果繼續反抗只好用武力壓制後帶回去了!"], [3, 0, 2410001, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Kaison/9");
        ms.getNPCTalk(["#face3#用武力?誰怕誰呀.但是我先警告你們,我的實力不只是你們想像的如此而已.為了此刻我一直隱藏我的實力."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/97");
    } else if (status === i++) {
        ms.getNPCTalk(["先由我來抵擋,拉伊拉隊長!趕快去請求支援吧!"], [3, 0, 2410001, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Kaison/10");
    } else if (status === i++) {
        ms.getNPCTalk(["千萬不要放鬆!他不是普通的人!"], [3, 0, 2410002, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Layla/17");
        ms.removeNPCRequestController(11603140);
        ms.spawnMob(9300747, 1344, -25);
        ms.removeNPCRequestController(11603141);
        ms.removeNPCRequestController(11603142);
        ms.spawnMob(9300745, 1254, -25);
        ms.removeNPCRequestController(11603143);
        ms.spawnMob(9300745, 1532, -25);
        ms.removeNPCRequestController(11603144);
        ms.spawnMob(9300745, 1625, -25);
        ms.removeNPCRequestController(11603492);
        ms.spawnMob(9300748, 980, -25);
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
