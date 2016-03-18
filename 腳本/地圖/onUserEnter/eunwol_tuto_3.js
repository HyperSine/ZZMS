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
        ms.lockUI(1, 0);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(9, "", [1]);
        ms.spawnNPCRequestController(2159449 + ms.getPlayerStat("GENDER"), 215, -85, 1, 11622178);
        ms.getDirectionEffect(5, "", [0, 400, 215, -85]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1310]);
    } else if (status === i++) {
        ms.showEnvironment(15, "", [1, 200, 0, 0, 0, 1000]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(11, "\r\n\r\n狀況不好.", [0]);
    } else if (status === i++) {
        ms.getDirectionEffect(11, "\r\n黑暗魔法師的力量比想像中的更強.", [0]);
    } else if (status === i++) {
        ms.getDirectionEffect(11, "\r\n只靠他們的力量看起來是無法消滅黑暗魔法師的.", [1]);
    } else if (status === i++) {
        ms.getDirectionEffect(11, "\r\n\r\n\r\n\r\n剩下的辦法只有一個, \r\n\r\n\r\n只有使用時間的封印將黑暗魔法師 #fs35#封印#fs20#.", [1]);
    } else if (status === i++) {
        ms.showEnvironment(15, "", [0, 0, 0, 0, 0, 1000]);
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["(已經啟動全部五個封印. 現在該怎麼做呢?)"], [3, 0, 2159445, 0, 5, 0, 0, 1, 0, 2159467]);
    } else if (status === i++) {
        ms.getNPCTalk(["(現在剩下的只有從黑暗魔法師那引出時間的力量了. 當然必須要先滿足一個小條件.)"], [3, 0, 2159445, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(條件? 什麼條件?)"], [3, 0, 2159445, 0, 5, 0, 1, 1, 0, 2159467]);
    } else if (status === i++) {
        ms.getNPCTalk(["(為了發動時間的封印，需要一個人的全部時間, 就是 '#r#e存在#k#n`. 就是祭物或代價之類的. 啊, 不用露出那樣的表情. 這個我來做.)"], [3, 0, 2159445, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(不要亂說! 那個為什麼是你來做?)"], [3, 0, 2159445, 0, 5, 0, 1, 1, 0, 2159467]);
    } else if (status === i++) {
        ms.getNPCTalk(["(製作的人是我當然應該是我來做. 而且也讓我當一次主角看看吧. 我也厭倦每次都幫你們收尾了.)"], [3, 0, 2159445, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(現在這個情況還開玩笑? 門都沒有. 乾脆我來. 因為我沒有什麼好失去的.)"], [3, 0, 2159445, 0, 5, 0, 1, 1, 0, 2159467]);
    } else if (status === i++) {
        ms.getNPCTalk(["(夜光, 我知道你的心意. 但這個……)"], [3, 0, 2159445, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(我來吧.)"], [3, 0, 2159445, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(哈啊, 連你都這樣怎麼辦? 這是攸關楓之谷世界命運的事啊. 不可以感情用事.)"], [3, 0, 2159445, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(所以才說我來啊. 現在佛列德你的身體狀態要承受封印太勉強了. 這點你應該更清楚的.)"], [3, 0, 2159445, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(……)"], [3, 0, 2159445, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(所以讓我…)"], [3, 0, 2159445, 0, 5, 0, 1, 1, 0, 2159467]);
    } else if (status === i++) {
        ms.getNPCTalk(["(夜光, 對你來說有更重要的事. 就是發動封印. 為了封印黑暗魔法師需要你擁有的光的力量. 這是只有你才可以做到的事. 知道嗎?)"], [3, 0, 2159445, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(但是要賭上存在不是嗎! 說是祭物! 那樣跟死有什麼不同?!)"], [3, 0, 2159445, 0, 5, 0, 1, 1, 0, 2159467]);
    } else if (status === i++) {
        ms.getNPCTalk(["(反正一定要有人來做的話，讓我來才對. 因為對我來說沒有必須守護的族人, 也沒有親愛的家人. 雖然祭物這個用法讓人心情有些不悅.)"], [3, 0, 2159445, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["(要繼續感性對話的話這場所好像不太適合. 這就留到日後黃泉相見時再繼續談吧.)"], [3, 0, 2159445, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(11622178, -1, 180, 100);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction15.img/effect/story/BalloonMsg0/0", [0, 410, -210, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [2500]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction15.img/effect/story/BalloonMsg0/1", [0, 410, -180, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction15.img/effect/story/BalloonMsg0/2", [0, 0, -100, 1, 1, 0, 11622178, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/BasicEff.img/Teleport", [0, 0, 0, 1, 1, 0, 11622178, 0]);
        ms.removeNPCRequestController(11622178);
        ms.getDirectionEffect(1, "", [300]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [0]);
    } else if (status === i++) {
        ms.getDirectionEffect(9, "", [0]);
        ms.getDirectionEffect(1, "", [1000]);
        ms.showWZEffectNew("Effect/BasicEff.img/Teleport");
    } else if (status === i++) {
        ms.getNPCTalk(["那麼開始吧?"], [3, 0, 2159445, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.forceStartQuest(38907, "2");
        ms.lockUI(0);
        ms.dispose();
        ms.warp(927030050, 0);
    } else {
        ms.dispose();
    }
}
