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
        ms.getDirectionEffect(1, "", [60]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg1/2", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getNPCTalk(["嗯。看起來很好為什麼這樣呢。有特別力量的正義勇士需要制服。"], [3, 0, 3000132, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這..好像不太對..."], [3, 0, 3000132, 0, 17, 0, 1, 1, 0]);
        ms.playSoundEffect("Voice.img/Angelic_F/1");
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000119, 150, 220, 0, 11567015);
        ms.getDirectionEffect(1, "", [90]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000115, 250, 220, 0, 11567017);
        ms.getDirectionEffect(1, "", [90]);
    } else if (status === i++) {
        ms.spawnNPCRequestController(3000111, 350, 220, 0, 11567018);
        ms.getDirectionEffect(1, "", [90]);
    } else if (status === i++) {
        ms.getNPCTalk(["在那裡!那個小孩手上好像是聖物的樣子。"], [3, 0, 3000119, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["可是一定要搶那個嗎? 聖物已經消失在聖所。"], [3, 0, 3000115, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["說什麼啊!搶了那個清除聖物我們才有功勞!"], [3, 0, 3000119, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["很抱歉。你手上的聖物可以給我們嗎?"], [3, 0, 3000111, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你們是那時候的壞祭司!"], [3, 0, 3000111, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["是的。我們是壞人，手上的聖物快交出來。這樣就不會傷害你。"], [3, 0, 3000111, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["又不是我想拔就拔。 克里安說沒有拔下的方法?"], [3, 0, 3000111, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["沒辦法。先把那小孩帶走!"], [3, 0, 3000119, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#h0#!使用我給的力量，擊退他們!"], [3, 0, 3000132, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我可以嗎?"], [3, 0, 3000132, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction10.img/effect/tuto/BalloonMsg1/3", [900, 30, -70, 0, 0]);
        ms.getDirectionEffect(1, "", [900]);
    } else if (status === i++) {
        ms.getNPCTalk(["幹嗎一個人碎碎念!小子!走吧!"], [3, 0, 3000119, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.removeNPCRequestController(11567015);
        ms.removeNPCRequestController(11567017);
        ms.removeNPCRequestController(11567018);
        ms.spawnMob(9300560, 150, 239);
        ms.spawnMob(9300561, 250, 239);
        ms.spawnMob(9300562, 350, 239);
        ms.getTopMsg("一直按著CTRL鍵就可以攻擊。");
        ms.showEnvironment(13, "lightning/screenMsg/0", [0]);
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
