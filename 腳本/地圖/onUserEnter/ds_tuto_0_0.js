/*
 Author: Pungin
 */
var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }

    if (status == 0) {
        ms.getDirectionStatus(true);
        ms.lockUI(true);
        ms.disableOthers(true);
        // UPDATE_STATS HP 250
        // UPDATE_STATS MP 120
        ms.teachSkill(30011109, 1, 1);
        ms.teachSkill(30010110, 1, 1);
        ms.teachSkill(30010185, 1, 1);
        ms.getDirectionEffect(3, "", [0]);
        ms.spawnNPCRequestController(2159307, 1430, 50);
        ms.showEffect(false, "demonSlayer/back");
        ms.showEffect(false, "demonSlayer/text0");
        ms.getDirectionEffect(1, "", [500]);
    } else if (status == 0) {
        ms.showEffect(false, "demonSlayer/text1");
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status == 1) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status == 2) {
        ms.showEffect(false, "demonSlayer/text2");
        ms.getDirectionEffect(1, "", [500]);
    } else if (status == 3) {
        ms.showEffect(false, "demonSlayer/text3");
        ms.getDirectionEffect(1, "", [4000]);
    } else if (status == 4) {
        ms.showEffect(false, "demonSlayer/text4");
        ms.getDirectionEffect(1, "", [500]);
    } else if (status == 5) {
        ms.showEffect(false, "demonSlayer/text5");
        ms.getDirectionEffect(1, "", [4000]);
    } else if (status == 6) {
        ms.showEffect(false, "demonSlayer/text6");
        ms.getDirectionEffect(1, "", [500]);
    } else if (status == 7) {
        ms.getDirectionEffect(3, "", [0]);
        ms.showEffect(false, "demonSlayer/text7");
        ms.getDirectionEffect(1, "", [100]);
    } else if (status == 8) {
        ms.sendNextS("軍團長！ 這段期間你跑去哪了，為何會音訊全無呢？ 你比任何人都清楚 #p2159309#為了找碴，一直處心積慮地在等待機會…", 5, 2159307);
    } else if (status == 9) {
        ms.sendNextPrevS("整個氣氛真的很不尋常。 大概是因為軍團長捕捉到時間神殿的女神，所以才會招惹他人忌妒的。 哼！ #p2159309#充其量只是稍微掩蔽女神的雙眼而已。 而且還是靠利用本來的地位來進行的！", 5, 2159307);
    } else if (status == 10) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionStatus(true);
    } else if (status == 11) {
        ms.sendNextS("嗯… 如果是平常的話，你早就責罵我說不准說這種毫無意義的話了… 你該不會是發生什麼事了吧？ 仔細看看，你的臉色非常難看… 是哪裡不舒服嗎？ 還是先前在戰鬥中受傷了呢？", 5, 2159307);
    } else if (status == 12) {
        ms.sendNextPrevS("…#p2151009#。 你是… 黑魔法師和那兩個人當中誰的部下呢？", 3);
    } else if (status == 13) {
        ms.sendNextPrevS("嗯？ 為何突然這樣問呢？", 5, 2159307);
    } else if (status == 14) {
        ms.sendNextPrevS("請快點回答！", 3);
    } else if (status == 15) {
        ms.sendNextPrevS("這… 我當然是效忠於偉大的那個人。 但是，自從你救了我之後，我便下定決心要將這條性命奉獻給你了！ 你不是知道嗎？ 但是為何卻…？", 5, 2159307);
    } else if (status == 16) {
        ms.sendNextPrevS("…我想拜託你一件事情。", 3);
    } else if (status == 17) {
        ms.sendNextPrevS("將這封信… 交給那些被稱為 #r英雄#k的人們。", 3);
    } else if (status == 18) {
        ms.sendNextPrevS("嗯？ 為何要交給他們呢… 光是離開崗位，就一定會招來閒言閒語了。 若是和他們接觸的事情曝光的話，一定會被冠上違抗黑魔法師的污名的！ 一定會是這樣的！ 你到底在想些什麼呢，軍團長？", 5, 2159307);
    } else if (status == 19) {
        ms.sendNextPrevS("…我已經不是軍團長了。", 3);
    } else if (status == 20) {
        ms.sendNextPrevS("難道… 你要背叛黑魔法師嗎？ 你不是一向對他最忠誠的嗎？ 佔領時間神殿也還只是不久前的事情而已！ 現在只要等著領取報酬就好了… 為什麼卻… 這是為什麼呢？", 5, 2159307);
    } else if (status == 21) {
        ms.sendNextPrevS("…已經沒時間了。 若是這件事對你來說太困難，那我就收回吧。 …我不想要和你交手。", 3);
    } else if (status == 22) {
        ms.sendNextPrevS("問題不在於困不困難！ 我只是擔心你而已…！", 5, 2159307);
    } else if (status == 23) {
        ms.sendNextPrevS("......", 3);
    } else if (status == 24) {
        ms.sendNextPrevS("你的家人該怎麼辦呢？ 這樣說不定會危害到你的家人的…！", 5, 2159307);
    } else if (status == 25) {
        ms.sendNextPrevS("別再說了！ 到此為止吧！", 3);
    } else if (status == 26) {
        ms.sendNextPrevS("…為什麼呢？ 難道是… 難道他們發生什麼事情了？", 5, 2159307);
    } else if (status == 27) {
        ms.sendNextPrevS("......", 3);
    } else if (status == 28) {
        ms.sendNextPrevS("所以才會又… 別再說了… 好吧。 你本來不是這麼多話的人吧？", 5, 2159307);
    } else if (status == 29) {
        ms.sendNextPrevS("很好。 就算賭上性命，我也會將這封信交給他們的。", 5, 2159307);
    } else if (status == 30) {
        ms.sendNextPrevS("真是抱歉。 #p2151009#…", 3);
    } else if (status == 31) {
        ms.sendNextPrevS("請不要道歉。 我的性命是為你而存在的， 你能夠交給我這種任務，我反而感到很高興。", 5, 2159307);
    } else if (status == 32) {
        ms.sendNextPrevS("那我就遵從命令上路了。 希望你能夠成功…", 5, 2159307);
    } else if (status == 33) {
        ms.setNPCSpecialAction(2159307, "teleportation");
        ms.getDirectionEffect(1, "", [720]);
    } else if (status == 34) {
        ms.removeNPCRequestController(2159307);
        ms.sendNextS("(這段期間謝謝你了。 #p2151009#.)", 3);
    } else if (status == 35) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionStatus(true);
    } else {
        ms.getDirectionStatus(true);
        ms.dispose();
        ms.warp(927000080, 0);
    }
}


