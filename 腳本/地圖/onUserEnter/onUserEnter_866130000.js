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
        ms.resetMap(ms.getMapId());
        ms.getDirectionStatus(true);
        ms.lockUI(true);
        ms.getDirectionEffect(3, "", [0]);
        ms.disableOthers(true);
        ms.spawnNPCRequestController(9390383, -622, -529, 1);
        ms.spawnNPCRequestController(9390382, -652, -529, 1);
        ms.spawnNPCRequestController(9390306, -661, -526, 1);
        ms.wait(300);
        ms.showDarkEffect(false);
    } else if (status === i++) {
        ms.sendOthersTalk("現在怎麼辦?\r\n不能一直這樣下去啊.", 9390383, [false, true], 3);
    } else if (status === i++) {
        ms.wait(300);
    } else if (status === i++) {
        ms.sendOthersTalk("現在無論怎麼擊退下面那些傢伙們,說不定又會像剛才那些怪物一樣,蜂擁而至, BOSS.", 9390383, [false, true], 1);
    } else if (status === i++) {
        ms.wait(300);
    } else if (status === i++) {
        ms.sendOthersTalk("有吧, 有吧. 不能封住前往村莊的門嗎?", 9390382, [false, true], 1);
    } else if (status === i++) {
        ms.wait(300);
    } else if (status === i++) {
        ms.sendOthersTalk("但要把門關上的話,必需要爬到瞭望台上轉動控制桿. \r\n 下面的怪物實在太多了,要去那邊很危險啊.", 9390382, [false, true], 3);
    } else if (status === i++) {
        ms.wait(300);
    } else if (status === i++) {
        ms.sendOthersTalk("無論怎麼爬上去瞭望台,梯子都會粉碎,也是進退兩難啊.", 9390383, [false, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("這種時候要是能飛到天上就好了...", 9390383, [true, true], 3);
    } else if (status === i++) {
        ms.wait(1000);
    } else if (status === i++) {
        ms.sendOthersTalk("布蘭登, 這可不能開玩笑,很危險的,會掉下去啊!", 9390305, [false, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("沒關係,這樹是每天都爬上來的地方.哦... ", 9390306, [true, true], 1);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/21", [800, -470, -50, 1, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/23", [800, -320, -50, 1, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg1/5", [800, 0, -100, 1, 0, 0]);
        ms.wait(900);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390306, "falling", 0, true, 300);
    } else if (status === i++) {
        ms.removeNPCRequestController(9390306);
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/18", [2600, -189, 0, 1, 0, 0]);
        ms.getDirectionEffect(5, "", [0, 1000, -604, -250]);
    } else if (status === i++) {
        ms.wait(800);
    } else if (status === i++) {
        ms.wait(400);
    } else if (status === i++) {
        ms.wait(400);
    } else if (status === i++) {
        ms.showBlackBGEffect(0, 2000, 500, -1);
        ms.wait(500);
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/19", [1, 1, 1, 0, 0]);
    } else if (status === i++) {
        ms.changeMusic("BgmBT.img/FightingPinkBeenHawk");
        ms.spawnNPCRequestController(9390306, -690, -526, 1);
        ms.wait(1500);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 1000]);
    } else if (status === i++) {
        ms.showWZEffect3("Effect/Direction14.img/effect/ShamanBT/ChapterA/19", [1, 0]);
        ms.wait(500);
    } else if (status === i++) {
        ms.spawnNPCRequestController(9390384, -330, -529);
        ms.wait(1300);
    } else if (status === i++) {
        ms.sendOthersTalk("哇啊! 艾卡來了!", 9390382, [false, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("哼,在這裡做什麼啊? 傻小子.", 9390384, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("來了啊,姐姐! \r\n 我們都在等你. 傻小子.", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.wait(500);
    } else if (status === i++) {
        ms.sendOthersTalk("咦? 萊伊的態度改變了.", 9390383, [false, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("萊伊, 波波. 阿樂說的那孩子在哪?", 9390384, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("就正在眼前啊,姐姐.", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.wait(500);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390384, "attack1", 0, true, 800);
    } else if (status === i++) {
        ms.sendOthersTalk("(呃啊... 嚴厲的眼神...)", 9390383, [false, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("所以說是在那裡?", 9390384, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("感覺馬上被否定了!", 9390384, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("有吧, 有吧. 那孩子就是阿樂說的對吧. \r\n剛剛和波波簽訂守護條約,一起戰鬥,看來技巧不錯的樣子? 嘿嘿嘿.", 9390382, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("什麼?你說已經簽訂合約了? \r\n萊伊你也是嗎", 9390384, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("呃!那..那個姐姐... 他看起來相當勇敢...", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("吵死了! 傻小子1號! \r\n 我們沒有主人已經超過100年了! \r\n 但現在是要突然和那種小鬼簽約嗎?", 9390384, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("美麗的雀鷹. 請不要罵萊伊和波波.雖然不清楚是什麼事,但若是因我而起,我代他們道歉. \r\n 不過現在拯救我們的村莊非常急迫. \r\n 請借給我們妳的力量.", 9390384, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("看,看啊, 姐姐! \r\n 是心地善良,判斷情況又很迅速的最棒的BOSS啊.", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("吵死了!!! \r\n 反正照那孩子說的,當務之急是救出村莊的人們. \r\n 萊伊, 所以這樣做就可以了嗎?", 9390384, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("姐姐和BOSS簽訂合約,借助力量的話...\r\n 就可以飛到那個瞭望台上面把村莊的門關起來了.", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.wait(500);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390384, "attack1", 0, true, 800);
        ms.playSound("ShamanBTTuto/Eka1");
    } else if (status === i++) {
        ms.sendOthersTalk("#b(呃啊... 這次是萊伊在虎視耽耽...)#k", 9390383, [false, true], 3);
    } else if (status === i++) {
        ms.wait(500);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/balloonMsg/22", [800, -80, -150, 1, 0, 0]);
        ms.wait(1000);
    } else if (status === i++) {
        ms.sendOthersTalk("名字是 #h0#對吧?  \r\n首先, 你已經和那些笨蛋簽訂合約了,而且現在也沒時間猶豫. \r\n但不要誤會,並不代表我完全接受你了?\r\n知道方法吧?", 9390384, [false, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("是的, 擊掌!!!", 9390384, [true, true], 3);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9390384, "special2", 0, true, 100);
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [1002, 1000]);
        ms.getDirectionEffect(2, "Effect/Direction14.img/effect/ShamanBT/ChapterA/13", [700, 60, -529, 1, 0, 0]);
        ms.wait(1000);
    } else if (status === i++) {
        ms.playSound("ShamanBTTuto/Contract");
        ms.sendOthersTalk("拍, 拍, 拍!!!", 9390384, [false, true], 3);
    } else if (status === i++) {
        ms.wait(1000);
    } else if (status === i++) {
        ms.sendOthersTalk("艾卡. 現在和我簽約了...", 9390384, [false, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("啊哈哈哈哈... 姐姐. 我們BOSS有點沒禮貌.等一下...", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("嘿BOSS. 大姐是很有個性的啊.先從名稱開始,有必要尊重一下.", 9390383, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("如何? 艾卡大人... 還是?", 9390383, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("哼, 笨蛋. \r\n 不用改稱呼了,在我改變心意前快點行動吧. 想去那個瞭望台上面吧?", 9390384, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("啊, 嗯... 是!", 9390384, [true, true], 3);
    } else if (status === i++) {
        ms.sendOthersTalk("知道了. 雖然很煩但這是簽約後的第一個請求.", 9390384, [true, true], 1);
    } else if (status === i++) {
        ms.sendOthersTalk("要從這裡飛出去還是,應該要使用技能飛出去吧.", 9390384, [true, true], 1);
    } else if (status === i++) {
        ms.removeNPCRequestController(9390383);
        ms.removeNPCRequestController(9390382);
        ms.removeNPCRequestController(9390306);
        ms.removeNPCRequestController(9390384);
        ms.lockUI(false);
        ms.disableOthers(false);
        ms.resetMap(ms.getMapId());
        ms.dispose();
        ms.warp(866131000, 0);
    } else {
        ms.dispose();
    }
}