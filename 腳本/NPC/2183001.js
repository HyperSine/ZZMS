/*
 *	阿斯旺 - 阿斯旺解放戰
 */

var status = -1;
var minLevel = 40;
var maxCount = 5;
var minPartySize = 1;
var maxPartySize = 4;

function start() {
    cm.sendSimple("#e<阿斯旺解放戰>#n\r\n要掃蕩依然在阿斯旺地區徘徊的希拉的殘黨嗎?#b\r\n\r\n\r\n#L1# 掃蕩希拉的殘黨.#l\r\n#L0# 直接對抗希拉. (等級 120以上)#l");
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        status--;
    }
    if (status == 0) {
        if (selection == 0) {
            if (cm.getLevel() >= 120) {
                cm.sendNext("送你到希拉之塔，請一定要擊退希拉。");
            } else {
                cm.sendOk("以你現在的實例，對戰希拉有些勉強。必須達到120級以上才能進行挑戰。");
                cm.dispose();
            }
        } else if (selection == 1) {
            cm.sendOk("活動尚未開放，敬請期待。");
            cm.dispose();
        } else {
            cm.dispose();
        }
    } else if (status == 1) {
        cm.warp(262030000, 0); //希拉之塔 - 希拉之塔入口
        cm.dispose();
    } else {
        cm.dispose();
    }
}