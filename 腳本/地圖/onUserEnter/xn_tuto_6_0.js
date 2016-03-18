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
        ms.lockUI(1, 1);
        ms.getDirectionEffect(9, "", [1]);
        ms.spawnNPCRequestController(2159386, 350, 30, 0, 9235548);
        ms.spawnNPCRequestController(2159382, 250, 30, 0, 9235549);
        ms.spawnNPCRequestController(2159385, 480, 30, 0, 9235550);
        ms.spawnNPCRequestController(2159427, 50, 30, 1, 9235551);
        ms.spawnNPCRequestController(2159427, 0, 30, 1, 9235552);
        ms.getDirectionEffect(5, "", [0, 150, 200, 22]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [2100]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9235551, "die", 0, true);
        ms.getDirectionEffect(1, "", [120]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(9235552, "die", 0, true);
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.removeNPCRequestController(9235551);
        ms.getDirectionEffect(1, "", [120]);
    } else if (status === i++) {
        ms.removeNPCRequestController(9235552);
        ms.getNPCTalk(["這些傢伙，真是源源不絕出現。"], [3, 0, 2159386, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["聽到發現隱藏研究所的報告，然後潛入，碰到比想像中還大的巨物的樣子。"], [3, 0, 2159382, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但是增強防禦的話，反而好像是要隱藏什麼? 一定要找出來。"], [3, 0, 2159385, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["鈴，你這種時候還能悠閒地將聲音…"], [3, 0, 2159382, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/2", [900, 0, -120, 1, 1, 0, 9235549, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.removeNPCRequestController(9235549);
        ms.spawnNPCRequestController(2159382, 250, 30, 1, 9236118);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/12", [1200, 0, -120, 1, 1, 0, 9236118, 0]);
        ms.setNPCSpecialAction(9236118, "catched", 0, true);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/1", [900, 0, -120, 1, 1, 0, 9235548, 0]);
        ms.updateNPCSpecialAction(9235548, 1, 30, 100);
        ms.getDirectionEffect(1, "", [2160]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9235548, -1, 2, 100);
        ms.removeNPCRequestController(9236118);
        ms.spawnNPCRequestController(2159383, 270, 30, 0, 9236629);
        ms.getDirectionEffect(9, "", [0]);
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/13", [900, 0, -120, 1, 1, 0, 9235550, 0]);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/13", [900, 0, -120, 1, 1, 0, 9235550, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
    } else if (status === i++) {
        ms.spawnNPCRequestController(2159377, -700, 30, 1, 9237131);
        ms.spawnNPCRequestController(2159378, -800, 30, 1, 9237132);
        ms.getNPCTalk(["根據命令攻擊目標。"], [3, 0, 2159383, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/2", [900, 0, -120, 1, 1, 0, 9236629, 0]);
        ms.getDirectionEffect(1, "", [810]);
    } else if (status === i++) {
        ms.getNPCTalk(["離吉可穆德遠一點!"], [3, 0, 2159385, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(0, "", [4, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/1", [900, 0, -120, 0, 0]);
        ms.showEnvironment(7, "Bgm30.img/thePhoto", [0]);
        ms.getDirectionEffect(1, "", [810]);
    } else if (status === i++) {
        ms.getNPCTalk(["啊，頭…頭好痛。"], [3, 0, 2159385, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/memory/0", [3900, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [3900]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg0/1", [900, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [810]);
    } else if (status === i++) {
        ms.getNPCTalk(["剛那個是什麼? 出現從來沒有看過的場面。而且胸口悶悶的樣子，這感覺……。"], [3, 0, 2159385, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg0/2", [900, 0, -120, 1, 1, 0, 9236629, 0]);
        ms.getDirectionEffect(1, "", [810]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9237131, 1, 650, 100);
        ms.getDirectionEffect(1, "", [150]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9237132, 1, 650, 100);
        ms.getDirectionEffect(5, "", [0, 200, -450, 43]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [3251]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 80]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [6823]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯?在幹嗎!趕快這傢伙…… 不對，先把剩下的傢伙全部抓來!"], [3, 0, 2159377, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/2", [900, 0, -120, 1, 1, 0, 9235548, 0]);
        ms.getDirectionEffect(1, "", [810]);
    } else if (status === i++) {
        ms.getNPCTalk(["…鈴!現在要先逃。"], [3, 0, 2159386, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["吉可穆德怎麼辦?"], [3, 0, 2159385, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["單靠我們的力量沒有辦法!募集同伴再來!啊啊!"], [3, 0, 2159386, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/smog", [3300, 550, 0, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg2/14", [1200, 120, -260, 1, 1, 0, 0, 1]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.removeNPCRequestController(9235548);
        ms.removeNPCRequestController(9235550);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg1/1", [1500, 0, -120, 1, 1, 0, 9237131, 0]);
        ms.getDirectionEffect(1, "", [840]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/smogEnd", [0, 550, 0, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [1020]);
    } else if (status === i++) {
        ms.getNPCTalk(["真是的，不能放過那些傢伙!"], [3, 0, 2159377, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["傑諾! 不要讓末日反抗軍逃了!鈴 !你跟我去追那些傢伙 !"], [3, 0, 2159377, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9237131, 1, 600, 100);
        ms.updateNPCSpecialAction(9237132, 1, 600, 100);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/tuto/BalloonMsg0/0", [900, 0, -120, 0, 0]);
        ms.getNPCTalk(["是什麼，剛那個……."], [3, 0, 2159377, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionStatus(true);
        ms.removeNPCRequestController(9236629);
        ms.removeNPCRequestController(9237131);
        ms.removeNPCRequestController(9237132);
        ms.dispose();
        ms.warp(931050950, 0);
    } else {
        ms.dispose();
    }
}
