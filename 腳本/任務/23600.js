/* global qm */
var status = -1;

function end(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        qm.dispose();
    } else if (status === i++) {
        qm.getDirectionStatus(true);
        qm.lockUI(1, 1);
        qm.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        qm.getDirectionEffect(5, "", [0, 100, 90, -41]);
    } else if (status === i++) {
        qm.getDirectionEffect(1, "", [2604]);
    } else if (status === i++) {
        qm.getNPCTalk(["呵呵呵非常好!算是不錯的結果。只剩細微的調整結束的話……."], [3, 0, 2159377, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        qm.forceStartQuest(23724);
        qm.showEnvironment(7, "Bgm30.img/fromUnderToUpper", [0]);
        qm.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/0", [1200, 0, -120, 1, 1, 0, 9232782, 0]);
        qm.updateNPCSpecialAction(9232782, -1, 1, 100);
        qm.getDirectionEffect(1, "", [90]);
    } else if (status === i++) {
        qm.getNPCTalk(["入侵者?難道是殺人鯨?顯示器打開看看"], [3, 0, 2159377, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        qm.forceStartQuest(23725);
        qm.getDirectionEffect(1, "", [2100]);
    } else if (status === i++) {
        qm.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        qm.getNPCTalk(["是末日反抗軍嗎……。雖然比被殺人鯨發現還好，怎麼剛好是這個時候潛入，這些自大的傢伙。"], [3, 0, 2159377, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        qm.getNPCTalk(["什麼，說不定是一件好事。再做最後一次測試，對付那些傢伙看看。呵呵呵……。"], [3, 0, 2159377, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        qm.removeNPCRequestController(9232782);
        qm.forceCompleteQuest();
        qm.dispose();
        qm.warp(931050940, 0);
    } else {
        qm.dispose();
    }
}
