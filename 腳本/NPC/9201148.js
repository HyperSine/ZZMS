/* 
    皇家整形
 */
/* global cm */

var status = -1;
var royaList;
var faceCard = -1;

function start() {
    var info = "";
    royaList = cm.getFaceRoyaCoupon();
    for (var i = 0 ; i < royaList.length ; i++) {
        if (cm.haveItem(royaList[i])) {
            info += "#b#L" + royaList[i] + "#換臉(使用#t0" + royaList[i] + "#)#l\r\n";
        }
    }
    if (info === "") {
        cm.sendOk("哎呀，要給我皇家整容優惠劵才能幫你處理頭髮喔。"/*要現在移動到商城去看看現在賣哪些皇家整容優惠劵嗎？"*/);
        cm.dispose();
        return;
    } else {
        cm.sendSimple("你好，我是整形手術領域的藝術家#b#p2012036##k。如果你有#b#t05152053##k，我可以為你做臉部整形手術。怎麼樣？ 你想讓自己的臉變得完美無瑕嗎？\r\n" + info);
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
        if (faceCard === -1) {
            faceCard = selection;
        }
        cm.sendYesNo("使用皇家整容院優惠券，可以隨意更改臉型。但是請別擔心，我的手術已經達到了一樹的境界。你真的要使用#b#t0" + faceCard + "##k，變換臉型嗎？");
    } else if (status === i++) {
        if (cm.setRandomAvatar(faceCard) === 1) {
            cm.sendOk("好了,你的朋友們一定認不出你了!");
        } else {
            cm.sendOk("必須要有皇家整容優惠券，才能進行手術。");
        }
        cm.dispose();
    } else {
        cm.dispose();
    }
}