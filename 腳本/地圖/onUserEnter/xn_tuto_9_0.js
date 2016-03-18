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
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [30]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.spawnNPCRequestController(2159381, -1700, 20, 1, 9244531);
        ms.spawnNPCRequestController(2159384, -1600, 20, 1, 9244532);
        ms.getNPCTalk(["通過這條路的話就是機庫。就可以通到外面了。但是這條路有很多警衛機器人。"], [3, 0, 2159381, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我會處理。不要擔心。"], [3, 0, 2159381, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["雖然剛那位朋友說自己是包袱，但是我也因被陷阱抓住，手受傷無法幫忙。真的沒關係嗎?"], [3, 0, 2159384, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["沒關係。試試看。"], [3, 0, 2159384, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(9244531, 1, 2200, 100);
        ms.updateNPCSpecialAction(9244532, 1, 2200, 100);
        ms.getDirectionEffect(1, "", [2100]);
    } else if (status === i++) {
        ms.spawnMob(9300682, -1000, 32);
        ms.spawnMob(9300682, -700, 32);
        ms.spawnMob(9300682, -400, 16);
        ms.spawnMob(9300682, -100, 32);
        ms.spawnMob(9300682, 200, 32);
        ms.getTopMsg("CTRL鍵一直按著的話可攻擊。擊退擋路的怪物。");
        ms.lockUI(0);
        ms.spawnMob(9300682, 925, 32);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
