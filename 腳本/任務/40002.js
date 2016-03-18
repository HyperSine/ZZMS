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
        qm.getNPCTalk(["真的有漏網之魚嗎?沒有往這邊來的...那是往另一邊去了?很多嗎?"], [3, 0, 2410003, 0, 33, 0, 0, 1, 0]);
        qm.playVoiceEffect("Voice.img/Milro/5");
    } else if (status === i++) {
        qm.getNPCTalk(["#face10#不是的.9隻當中擊倒8隻,逃走了一隻.戰鬥的時候稍為疏忽了一下...抱歉."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/26");
        qm.getNPCTalk(["不需要抱歉.那我們就去追漏網之魚吧?應該還沒有跑太遠?"], [3, 0, 2410003, 0, 33, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Milro/6");
    } else if (status === i++) {
        qm.getNPCTalk(["啊...這樣太不好意思了,讓我自己去消滅它吧.剛看到它逃亡的方向應該很快就能解決了."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Alpha/27");
    } else if (status === i++) {
        qm.getNPCTalk(["是嗎?那就快去消滅它.我在這裡等你.說實在,就一隻而已 #p2410008#應該不會追丟的."], [3, 0, 2410003, 0, 33, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Milro/7");
    } else if (status === i++) {
        qm.getNPCTalk(["#face5#就一隻而已了.你不必等我.雖然它好像跑遠了一點,但是應該不會有危險,而且我也不會追丟...還是先返回吧."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/28");
        qm.getNPCTalk(["嗯?沒問題嗎?老實說這麼簡單的任務應該沒問題,可是支援後方的任務也是個任務..."], [3, 0, 2410003, 0, 33, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Milro/8");
    } else if (status === i++) {
        qm.getNPCTalk(["#face4#當然.我不想這點小事還麻煩你."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.playVoiceEffect("Voice.img/Alpha/29");
        qm.getNPCTalk(["嗯...這樣不好意思,我就先走一步了#p2410008# 你也盡快解決隨後就到吧~"], [3, 0, 2410003, 0, 33, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Milro/9");
    } else if (status === i++) {
        qm.updateNPCSpecialAction(11587956, -1, 770, 100);
        qm.getDirectionEffect(1, "", [3000]);
        qm.showWZEffect("Effect/Direction13.img/zeroPrologue/Scene0");
    } else if (status === i++) {
        qm.getNPCTalk(["終於甩掉了嗎?跟我說什麼任務呀,一點都不配?什麼時候開始這麼認真起來了...無聊."], [3, 0, 2410008, 0, 41, 0, 0, 1, 0]);
        qm.playVoiceEffect("Voice.img/Alpha/30");
    } else if (status === i++) {
        qm.getNPCTalk(["剩下時間縮短了呀?可惡,我得快一點.快去 #b#m321000600##k看看吧."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        qm.playVoiceEffect("Voice.img/Alpha/31");
    } else if (status === i++) {
        qm.updateInfoQuest(40006, "");
        qm.forceStartQuest();
        qm.lockUI(0);
        qm.removeNPCRequestController(11587956);
        qm.dispose();
        qm.warp(321000300, 1);
    } else {
        qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        status--;
    }

    var i = -1;
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.getNPCTalk(["#b(是閉著眼睛的女人石像.就像這神殿的其它石像穿著長袍.)#k"], [4, 2410006, 0, 0, 41, 0, 0, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["#b(表情感覺很悲傷.是因為我的情緒嗎?)#k"], [4, 2410006, 0, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.updateInfoQuest(40002, "");
        qm.forceCompleteQuest();
        qm.updateInfoQuest(40006, "");
        qm.dispose();
    } else {
        qm.dispose();
    }
}
