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
        ms.getNPCTalk(["哇哇!!終於完成了!!"], [3, 0, 3000100, 0, 1, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["呼呼呼。真棒。"], [3, 0, 3000101, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["要叫它什麼名字?"], [3, 0, 3000102, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯..要用什麼呢?我討厭幼稚的東西.."], [3, 0, 3000101, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.getNPCTalk(["正義的勇士基地。如何?"], [3, 0, 3000100, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["哈哈哈。又說正義啊?你真喜歡正義。"], [3, 0, 3000102, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["幼稚。"], [3, 0, 3000101, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那麼要用什麼?"], [3, 0, 3000102, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯。用赫力席母攻擊隊。"], [3, 0, 3000101, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["赫力席母攻擊隊?"], [3, 0, 3000102, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我們的目標是奪回有梅格耐斯暴君的城堡，找回赫力席母!"], [3, 0, 3000101, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["很好!帥氣。貝代洛斯!"], [3, 0, 3000100, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["凱爾如何?"], [3, 0, 3000101, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我啊。沒有關係。"], [3, 0, 3000102, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那麼這裡就是赫力席母攻擊隊基地!!"], [3, 0, 3000100, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["很好!就這樣決定!"], [3, 0, 3000101, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那麼隊長是貝代洛斯?"], [3, 0, 3000100, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我是隊長?"], [3, 0, 3000101, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我也贊成。貝代洛斯我們之中最出色?"], [3, 0, 3000102, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["沒關係嗎?"], [3, 0, 3000101, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["是的!隊長!"], [3, 0, 3000100, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["哈哈哈。那麼赫力席母攻擊隊!開始!!"], [3, 0, 3000101, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(940012010, 0);
    } else {
        ms.dispose();
    }
}
