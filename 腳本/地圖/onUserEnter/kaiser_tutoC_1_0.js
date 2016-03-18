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
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.spawnNPCRequestController(3000103, -300, 220, 1, 8381532);
        ms.spawnNPCRequestController(3000104, -450, 220, 1, 8381533);
        ms.spawnNPCRequestController(3000110, -120, 220, 1, 8381534);
        ms.spawnNPCRequestController(3000114, -100, 220, 1, 8381535);
        ms.spawnNPCRequestController(3000111, 130, 220, 0, 8381536);
        ms.spawnNPCRequestController(3000115, 250, 220, 0, 8381537);
        ms.getNPCTalk(["到底有什麼東西啊…"], [3, 0, 3000104, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg1/3", [1200, 0, -120, 1, 1, 0, 8381533, 0]);
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg1/3", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg1/4", [1200, 0, -120, 1, 1, 0, 8381532, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getNPCTalk(["師弟們在做什麼事情啊？這些好像是沒有見過的人吧？"], [3, 0, 3000103, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["噓！感覺不對勁。 貝代洛斯！"], [3, 0, 3000103, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["知道。好像有可疑的味道吧？我先到村莊去找人幫忙，你們盯著那些人在做什麼。如果危險的話逃跑。"], [3, 0, 3000104, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg0/0", [1200, 0, -120, 1, 1, 0, 8381532, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.getNPCTalk(["在說什麼啊？"], [3, 0, 3000103, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.removeNPCRequestController(8381533);
        ms.getNPCTalk(["(東邊聖所是在受到攻擊嗎？那些人像要對聖物做什麼啊？)"], [3, 0, 3000103, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["除掉聖物的話保護罩應該也會弱化吧？"], [3, 0, 3000110, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但碰聖物的話應該會被詛咒吧？"], [3, 0, 3000114, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你相信那種迷信嗎？這是不會再有第二次的機會！現在不能再回去了！"], [3, 0, 3000110, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["要把聖物帶走嗎？"], [3, 0, 3000110, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["要抵擋！！"], [3, 0, 3000103, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(8381532, 1, 300, 100);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg1/1", [1200, 0, -120, 1, 1, 0, 8381534, 0]);
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg1/1", [1200, 0, -120, 1, 1, 0, 8381535, 0]);
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg1/1", [1200, 0, -120, 1, 1, 0, 8381536, 0]);
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg1/1", [1200, 0, -120, 1, 1, 0, 8381537, 0]);
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/story/BalloonMsg1/7", [900, 0, -120, 1, 1, 0, 8381532, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.showEnvironment(13, "kaiser/tear_rush", [0]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.removeNPCRequestController(8381532);
        ms.lockUI(0);
        ms.removeNPCRequestController(8381534);
        ms.removeNPCRequestController(8381535);
        ms.removeNPCRequestController(8381536);
        ms.removeNPCRequestController(8381537);
        ms.dispose();
        ms.warp(940001220, 0);
    } else {
        ms.dispose();
    }
}
