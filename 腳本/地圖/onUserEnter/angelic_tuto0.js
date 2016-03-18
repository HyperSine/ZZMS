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
        ms.getDirectionEffect(9, "", [1]);
        ms.getNPCTalk(["欸! #h0#! 不要再哭了。"], [3, 0, 3000101, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["哼…但他們又欺負我。"], [3, 0, 3000100, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你的名字不值得#h0#知道嗎? 說你沒魔力就哭的話怎麼辦。"], [3, 0, 3000101, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getNPCTalk(["貝代洛斯。別太超過了。#h0#從一出生就沒有魔力。"], [3, 0, 3000106, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但是..我#h0#不想看到受到欺負跟哭。"], [3, 0, 3000101, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["貝代洛斯.. 夠了。他們走了。"], [3, 0, 3000102, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯..知道了。我不哭了。"], [3, 0, 3000100, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(可憐的#h0#...)"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["可是#h0#還有我們啊。"], [3, 0, 3000102, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那我們去村莊外面玩吧?轉換一下心情吧。"], [3, 0, 3000101, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯!"], [3, 0, 3000100, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getNPCTalk(["#h0#。你笑的話看起來比較好。"], [3, 0, 3000101, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我也這樣想。"], [3, 0, 3000102, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["他們是怎樣。老是這樣。"], [3, 0, 3000100, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(940011010, 0);
    } else {
        ms.dispose();
    }
}
