/* global qm */
var status = -1;

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
        qm.getNPCTalk(["什麼, 去旅行了?"], [4, 9390304, 0, 0, 0, 0, 0, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["不行啊, 孩子啊. 離開這裡去冒險你還太小了."], [4, 9390304, 0, 0, 0, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["沒關係, 因為萊伊和波波, 阿樂,還有帥氣的艾卡都會一起去的."], [4, 9390304, 0, 0, 2, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["但孩子啊..."], [4, 9390304, 0, 0, 0, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["奶奶, 我說過早晚會成為楓之谷的英雄吧.\r\n我覺得現在就是為了成為英雄而離開,前往那條道路的時候."], [4, 9390304, 0, 0, 2, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["看來決心很堅定的樣子...\r\n知道了,孩子啊.那要好好平安的回來啊."], [4, 9390304, 0, 0, 0, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["但是, 要永遠走大路然後往明亮的地方前進, 不要跟看起來可怕的陌生人講話..."], [4, 9390304, 0, 0, 0, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["奶奶!我現在不是小孩子了!"], [4, 9390304, 0, 0, 2, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.forceCompleteQuest();
        qm.dispose();
        qm.warp(866136000, 0);
    } else {
        qm.dispose();
    }
}
