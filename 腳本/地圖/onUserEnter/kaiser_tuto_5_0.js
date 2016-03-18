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
        ms.spawnNPCRequestController(3000131, -390, 170, 0, 8370699);
        ms.spawnNPCRequestController(3000122, -740, 170, 0, 8370700);
        ms.spawnNPCRequestController(3000125, -640, 170, 0, 8370701);
        ms.getDirectionEffect(1, "", [1500]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [460, 0]);
        ms.getDirectionEffect(2, "Skill/6112.img/skill/61121100/effect", [0, 0, 0, 0, 0]);
        ms.showEnvironment(5, "Kaiser/61121100", []);
        ms.getDirectionEffect(1, "", [150]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8370700, "die1", 0, false);
        ms.setNPCSpecialAction(8370701, "die1", 0, false);
        ms.getDirectionEffect(2, "Skill/6112.img/skill/61121100/hit", [0, 0, 0, 1, 1, 0, 8370700, 0]);
        ms.getDirectionEffect(2, "Skill/6112.img/skill/61121100/hit", [0, 0, 0, 1, 1, 0, 8370701, 0]);
        ms.getDirectionEffect(1, "", [1400]);
        ms.playVoiceEffect("Skill.img/61121100/Hit");
    } else if (status === i++) {
        ms.removeNPCRequestController(8370700);
        ms.removeNPCRequestController(8370701);
        ms.getDirectionEffect(5, "", [0, 300, -600, 178]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1002]);
    } else if (status === i++) {
        ms.getNPCTalk(["真棒。不錯的實力。凱撒。打敗了多少啊？再努力一點應該就足夠了吧？"], [3, 0, 3000131, 0, 9, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["現在輪到你了，梅格耐斯。"], [3, 0, 3000131, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [0]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000128, -750, 170, 0, 8371357);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000128, -650, 170, 0, 8371358);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000128, -550, 170, 0, 8371360);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000128, -1150, 170, 1, 8371440);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000128, -1250, 170, 1, 8371441);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000128, -1350, 170, 1, 8371442);
        ms.getDirectionEffect(1, "", [210]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 450, -600, 178]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [666]);
    } else if (status === i++) {
        ms.getNPCTalk(["幽靈還有很多。不要太急。啊，你是時間不足吧？"], [3, 0, 3000131, 0, 9, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["躲在達爾默面前獲得的手下後面的樣子，看來你應該也不算一個堂堂的戰士啦。"], [3, 0, 3000131, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這個嘛，就當作是我的興趣吧。我想繼續看看你這個傢伙掙扎的樣子。"], [3, 0, 3000131, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但不要擔心。你的最後一程我會親自用我有雙手送你。雖然凱撒是投胎的但能證明他不是無敵的。哈哈哈。"], [3, 0, 3000131, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2000]);
        ms.showWZEffectNew("Effect/OnUserEff.img/normalEffect/demonSlayer/chatBalloon0");
        ms.showWZEffect("Effect/Direction9.img/kaiserTutorial/Scene2");
    } else if (status === i++) {
        ms.getNPCTalk(["(毒好像散的不少。要速戰速決嗎？情況不太好。但在這裡倒下的話浪費了凱撒的名聲。)"], [3, 0, 3000131, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你這個傢伙死了再投胎比現在的你還弱吧。投胎後恢復到現在這個水準的時候事情已經結束了吧。"], [3, 0, 3000131, 0, 9, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["真不像你話真多，梅格耐斯。決定勝負吧。"], [3, 0, 3000131, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/tuto/Effect/0", [0, 0, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(9, "", [1]);
        ms.spawnNPCRequestController(3000142, -900, 170, 1, 8372775);
        ms.getNPCTalk(["對。就是這樣。毒已經散到無法變身哈哈。現在要贏過你我要成為超新星的最強戰士！"], [3, 0, 3000131, 0, 9, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.showEnvironment(13, "demonSlayer/whiteOut", [0]);
        ms.getDirectionEffect(1, "", [5000]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(8370699);
        ms.removeNPCRequestController(8371357);
        ms.removeNPCRequestController(8371358);
        ms.removeNPCRequestController(8371360);
        ms.removeNPCRequestController(8371440);
        ms.removeNPCRequestController(8371441);
        ms.removeNPCRequestController(8371442);
        ms.removeNPCRequestController(8372775);
        ms.dispose();
        ms.warp(940002030, 0);
    } else {
        ms.dispose();
    }
}
