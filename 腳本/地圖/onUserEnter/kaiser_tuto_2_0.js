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
        ms.spawnNPCRequestController(3000107, -2000, 20, 1, 8365969);
        ms.getNPCTalk(["這裡還有幽靈出現…"], [3, 0, 3000107, 0, 17, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["該不會是比想像中還要嚴重吧？"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["感覺不太好。趕快回去打開防護罩，來做好準備。"], [3, 0, 3000107, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這種情況下要更是要我跟去。凱撒一個人實在是… "], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["卡塔利溫，你是超新星的勇士。第一個義務是要把超新星的安全當做必生命還要重要你忘了嗎？還有我是凱撒。我是可以擔心別人但我無法讓人擔心。"], [3, 0, 3000107, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你竟然這麼說…那祝你好運。"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(8365969);
        ms.dispose();
        ms.warp(940001100, 0);
    } else {
        ms.dispose();
    }
}
