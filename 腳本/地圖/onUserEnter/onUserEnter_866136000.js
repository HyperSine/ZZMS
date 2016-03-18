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
        ms.getDirectionEffect(3, "", [0]);
        ms.disableOthers(true);
        ms.getNPCTalk(["現在該出發了. \r\n晚了的話艾卡會生氣的."], [3, 0, 9390381, 0, 1, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
        ms.showDarkEffect(false);
    } else if (status === i++) {
        ms.getNPCTalk(["知道了. 阿樂."], [3, 0, 9390381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["奶奶, 那我走了哦."], [3, 0, 9390381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["好. 小心身體啊.不要做太危險的行為啊!"], [3, 0, 9390304, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["是! 奶奶!不要擔心!"], [3, 0, 9390304, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#b#h0##k, 那段期間我誤會了! \r\n #h0#真的像英雄一樣!"], [3, 0, 9390305, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#b#h0##k啊, 你已經是我們村莊的英雄了. \r\n 我們非常以你為榮."], [3, 0, 9390308, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["對了! #b#h0##k已經是英雄了!"], [3, 0, 9390306, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["什麼啊, 為什麼還不出來? "], [3, 0, 9390384, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["啊, 艾卡生氣了喵."], [3, 0, 9390381, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionFacialExpression(1, 5000);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["對奶奶來說,#b#h0##k永遠都是小可愛.\r\n好,我相信#b#h0##k,就去看看外面的世界吧.\r\n不管何時,只要感到辛苦就回來!\r\n還有,帶著這個走.\r\n#b(羅莎那奶奶掏出某個東西,掛在我脖子上.)#k"], [3, 0, 9390304, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#b#h0##k, 那就快去吧. 朋友們在等著."], [3, 0, 9390304, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["這是什麼?奶奶"], [3, 0, 9390304, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你沉睡的期間,我就感覺到會有這一天,所以先做好了這個.帶著這個口哨跟你的朋友們一起旅行吧."], [3, 0, 9390304, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionFacialExpression(1, 10000);
        ms.getDirectionEffect(1, "", [200]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [50]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(0, "", [109, 540]);
        ms.getDirectionEffect(1, "", [540]);
    } else if (status === i++) {
        ms.getNPCTalk(["是! 那大家都要好好的哦. \r\n 我很快就會回來的~ 成為真正的英雄之後! \r\n 走吧, 阿樂!"], [3, 0, 9390304, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.disableOthers(false);
        ms.dispose();
        ms.warp(866137000, 0);
    } else {
        ms.dispose();
    }
}
