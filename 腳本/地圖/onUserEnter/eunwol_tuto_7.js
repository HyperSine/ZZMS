/* global ms */
var status = -1;

function action(mode, type, selection) {
    if (ms.getQuestCustomData(38900) === "2") {
        action2(mode, type, selection);
    } else {
        action1(mode, type, selection);
    }
}

function action1(mode, type, selection) {
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
        ms.spawnNPCRequestController(3002100, 200, -145, 0, 11627962);
        ms.spawnNPCRequestController(3002004, 40, -145, 1, 11627963);
        ms.spawnNPCRequestController(3002001, -30, -145, 1, 11627964);
        ms.spawnNPCRequestController(3002002, -100, -145, 1, 11627965);
        ms.spawnNPCRequestController(3002003, 260, -145, 0, 11627966);
        ms.getDirectionEffect(3, "", [4]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction5.img/effect/mercedesInIce/merBalloon/0", [0, 20, -80, 0, 0]);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [3]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["這, 這裡……?"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["喔, 起來了, 起來了. "], [3, 0, 0, 0, 5, 0, 1, 1, 0, 3002003]);
    } else if (status === i++) {
        ms.getNPCTalk(["超強, 太扯了. 好像也會說話!"], [3, 0, 3002002, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["看到了嗎? 看到了嗎? 眼珠圓滾滾的!"], [3, 0, 3002004, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["超大! 應該不是要把我們抓來吃吧?"], [3, 0, 3002001, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["厄…… "], [3, 0, 3002001, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["喂, 好像哪裡不舒服的樣子耶. 你去問問看是不是不舒服吧. "], [3, 0, 3002004, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["為何只對我這樣. 我很害怕."], [3, 0, 3002001, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這群膽小鬼! 全部讓開. 你! 哪裡不舒服嗎?"], [3, 0, 3002001, 0, 5, 0, 1, 1, 0, 3002100]);
    } else if (status === i++) {
        ms.getNPCTalk(["沒有, 現在沒事. 只是頭有點暈. 但這裡是哪裡? 還有你們是……"], [3, 0, 3002001, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我們是尖耳狐狸. 這裡是我們生活的地方. 你是誰? 怎麼會來這裡? 這裡是我們的領地."], [3, 0, 3002001, 0, 5, 0, 1, 1, 0, 3002100]);
    } else if (status === i++) {
        ms.getNPCTalk(["(尖耳狐狸? 是不為人知的異種族嗎. 有耳朵跟尾巴, 真的跟狐狸很像.)"], [3, 0, 3002001, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我是人類. 從時間神殿附近來的, 這個地方大概是哪裡?"], [3, 0, 3002001, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["人類? 時間神殿? 都是第一次聽到?"], [3, 0, 3002001, 0, 5, 0, 1, 1, 0, 3002100]);
    } else if (status === i++) {
        ms.getNPCTalk(["(不知道時間神殿? 到底是從哪裡來的? 到底這裡是哪裡……)"], [3, 0, 3002001, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [600]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction2.img/effect/chat/nugu/0", [2000, 20, -80, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [2500]);
    } else if (status === i++) {
        ms.getNPCTalk(["腿, 腿有兩隻?!"], [3, 0, 3002001, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["……? 腿本來就是兩隻啊. 人類, 很不舒服嗎?"], [3, 0, 3002001, 0, 5, 0, 1, 1, 0, 3002100]);
    } else if (status === i++) {
        ms.getNPCTalk(["你說腿本來就是兩隻? 不可能有那種事! 還有我的等級…… #r#e10等級#k#n?! 現在到底是什麼事…!"], [3, 0, 3002001, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["從沒有耳朵開始就覺得奇怪了. 大概是有很多不足吧."], [3, 0, 3002001, 0, 5, 0, 1, 1, 0, 3002003]);
    } else if (status === i++) {
        ms.getNPCTalk(["所以被遺棄了嗎? 哎呀, 好可憐……"], [3, 0, 3002002, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["不行. 先帶去村莊吧."], [3, 0, 3002000, 0, 5, 0, 1, 1, 0, 3002100]);
    } else if (status === i++) {
        ms.getNPCTalk(["但是如果隨便帶外人到村莊的話會被大人罵耶."], [3, 0, 3002001, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但是也不能把他丟在這裡就走啊. 全部我來負責. 走吧!"], [3, 0, 3002000, 0, 5, 0, 1, 1, 0, 3002100]);
    } else if (status === i++) {
        ms.forceStartQuest(38900, "2");
        ms.removeNPCRequestController(11627963);
        ms.removeNPCRequestController(11627965);
        ms.removeNPCRequestController(11627966);
        ms.removeNPCRequestController(11627962);
        ms.removeNPCRequestController(11627964);
        ms.lockUI(0);
        ms.dispose();
        ms.warp(940200000, 0);
    } else {
        ms.dispose();
    }
}

function action2(mode, type, selection) {
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
        ms.spawnNPCRequestController(3002100, 200, -145, 0, 11628668);
        ms.spawnNPCRequestController(3002004, 40, -145, 1, 11628669);
        ms.spawnNPCRequestController(3002001, -30, -145, 1, 11628670);
        ms.spawnNPCRequestController(3002002, -100, -145, 1, 11628671);
        ms.spawnNPCRequestController(3002003, 260, -145, 0, 11628672);
        ms.getNPCTalk(["現在這是在做什麼?"], [3, 0, 0, 0, 17, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["喂, 真的連尾巴都沒有!"], [3, 0, 3002004, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你真的太壞了. 媽媽說過不能拿別人的弱點來開玩笑."], [3, 0, 3002002, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["哈啊…… 這情況到底……"], [3, 0, 3002002, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["蘭, 我們先去村莊. 你帶著那個來."], [3, 0, 3002002, 0, 5, 0, 1, 1, 0, 3002003]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.removeNPCRequestController(11628669);
        ms.removeNPCRequestController(11628671);
        ms.removeNPCRequestController(11628672);
        ms.removeNPCRequestController(11628670);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["那麼我們也走吧, 人類."], [3, 0, 3002002, 0, 5, 0, 0, 1, 0, 3002100]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.removeNPCRequestController(11628668);
        ms.lockUI(0);
        ms.dispose();
        ms.warp(940200060, 0);
    } else {
        ms.dispose();
    }
}
