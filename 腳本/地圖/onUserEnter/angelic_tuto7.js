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
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [60]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["我…我讓聖物消失??我沒有這樣的想法!!"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我沒有魔力，沒有知道的東西…怎會發生這樣的事 ..."], [3, 0, 0, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [120]);
        ms.showWZEffect("Effect/Direction10.img/angelicTuto/Scene1");
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg0/5", [1200, 0, -120, 0, 0]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000152, 300, 10, 0, 11563912);
        ms.getNPCTalk(["#h0#!!等一下!"], [3, 0, 3000152, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [60]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["#h0#!別太傷心。原本聖物就不會對誰有反應。如果對任何人都有反應的話，其他祭司早就有反應了。"], [3, 0, 3000152, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但是聖物消失是不變的!"], [3, 0, 3000152, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg0/6", [1200, 0, -120, 1, 1, 0, 11563912, 0]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(11563912);
        ms.dispose();
        ms.warp(940011080, 0);
    } else {
        ms.dispose();
    }
}
