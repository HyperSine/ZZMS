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
        ms.getDirectionEffect(9, "", [1]);
        ms.spawnNPCRequestController(2159439, 1210, 10, 0, 11619627);
        ms.spawnNPCRequestController(2159467, 830, 10, 1, 11619628);
        ms.getDirectionEffect(1, "", [1000]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["...所以我一開始就不喜歡你."], [3, 0, 2159467, 0, 5, 0, 0, 1, 0, 2159439]);
    } else if (status === i++) {
        ms.getNPCTalk(["我實在很不欣賞你那狡猾的個性."], [3, 0, 2159467, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["也對啦,我們之間唯一的共同點就是不喜歡彼此.儘管如此,普力特還是同時派遣我們兩個過來,他到底在想什麼啊?唉..他該不會對我們有什麼意見吧?是嗎?"], [3, 0, 2159467, 0, 5, 0, 1, 1, 0, 2159439]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(9, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
        ms.showWZEffectNew("Effect/BasicEff.img/Summoned");
    } else if (status === i++) {
        ms.getNPCTalk(["這個嘛,雖然不確定,但要是這樣繼續在這裡拖時間,普力特可能就會對我們大發雷霆了."], [3, 0, 2159467, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["普力特生氣的樣子?那應該很有趣."], [3, 0, 2159467, 0, 5, 0, 1, 1, 0, 2159439]);
    } else if (status === i++) {
        ms.getNPCTalk(["不過我覺得永遠都看不到的可能性更高.我先進去囉.從裡面傳出來的氣息不太尋常."], [3, 0, 2159467, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionEffect(9, "", [1]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 100, 1000, 18]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [191]);
    } else if (status === i++) {
        ms.getNPCTalk(["...那麼廢話就不多說了,下次再見面時直接問普力特吧.所以..可別在這種地方白白的犧牲."], [3, 0, 2159439, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(11619627, "teleportation", 0, true);
        ms.getDirectionEffect(1, "", [840]);
    } else if (status === i++) {
        ms.removeNPCRequestController(11619627);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getDirectionStatus(true);
        ms.removeNPCRequestController(11619628);
        ms.lockUI(0);
        ms.dispose();
        ms.warp(927030010, 0);
    } else {
        ms.dispose();
    }
}
