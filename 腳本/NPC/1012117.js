/* 
    皇家美髮
 */
/* global cm */

var status = -1;
var royaList;
var hairCard = -1;

function start() {
    var info = "";
    royaList = cm.getHairRoyaCoupon();
    for (var i = 0 ; i < royaList.length ; i++) {
        if (cm.haveItem(royaList[i])) {
            info += "#b#L" + royaList[i] + "#更換髮型(使用#t0" + royaList[i] + "#)#l\r\n";
        }
    }
    if (info === "") {
        cm.sendOk("哎呀，要給我皇家美髮卷才能幫你處理頭髮喔。"/*要現在移動到商城去看看現在賣哪些皇家美髮卷嗎？"*/);
        cm.dispose();
        return;
    } else {
        cm.sendSimple("你好。我是大頭國的#b#p1012117##k。如果你有#b#t05150040##k，我可以為你設計一個髮型。怎麼樣？\r\n" + info);
    }
}

function action(mode, type, selection) {
    if (mode === 1) {
        status++;
    } else {
        if (status === 0) {
            cm.sendOk("你還沒做好心理準備嗎？決定好了之後，請你再來和我說話。");
        }
        status--;
    }

    var i = -1;
    if (status === i++) {
        cm.dispose();
    } else if (status === i++) {
        if (hairCard === -1) {
            hairCard = selection;
        }
        cm.sendYesNo("怎麼樣？喜歡新換的髮型嗎?你看上去真帥。哈哈哈，我的手藝還能差到那裡去～需要更換髮型的話，可以隨時來找我，呵呵。");
    } else if (status === i++) {
        if (cm.setRandomAvatar(hairCard) === 1) {
            cm.sendOk("好了,讓朋友們讚歎你的新髮型吧!");
        } else {
            cm.sendOk("必須要有皇家理髮券，我才能為你理髮。");
        }
        cm.dispose();
    } else {
        cm.dispose();
    }
}