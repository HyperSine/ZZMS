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
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [30]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["吉可穆德! 沒事就好。你知道我有多擔心嘛。"], [3, 0, 2159385, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["謝謝。鈴。"], [3, 0, 2159384, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["倒是……。他是什麼人 ?看剛回來的狀況，好像不是敵人的樣子。"], [3, 0, 2159388, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["傑利麥勒的部下。第一次見面時，攻擊吉可穆德。 ……這之間有什麼事吧，看剛的狀況雖然有點搞不清楚。 "], [3, 0, 2159386, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我也不知道，但應該不是壞人。是我的恩人。而且失去記憶，待在傑利麥勒研究所的樣子。"], [3, 0, 2159384, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但不久之前你有看見吧?在我看來，他受到傑利麥勒的操控的樣子。這個人進去秘密廣場也沒關係嗎?如果再被操控怎麼辦啊?"], [3, 0, 2159387, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/story/BalloonMsg0/1", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getNPCTalk(["應該可以解除這裝置……。"], [3, 0, 2159380, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/story/BalloonMsg1/1", [1200, -250, -60, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/story/BalloonMsg1/1", [1200, 50, -90, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/story/BalloonMsg1/1", [1200, -60, -50, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/story/BalloonMsg1/1", [1200, 130, -50, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/story/BalloonMsg1/1", [1200, -340, -100, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(2, "Effect/Direction12.img/effect/story/BalloonMsg1/1", [1200, 0, -120, 0, 0]);
        ms.getDirectionEffect(1, "", [1200]);
    } else if (status === i++) {
        ms.getNPCTalk(["那麼立刻拆吧。那麼危險的東西無法一直帶著吧。"], [3, 0, 2159388, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但是這不單單是操控的裝置.......連戰鬥系統全部包含的裝置。拆掉的話， #h0#系統 本身會壞掉，比現在弱更多。傑利麥勒不知何時會派人追來，這個太危險了。"], [3, 0, 2159380, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["沒關係，露緹。如果可以刪除的話，就這樣做吧。"], [3, 0, 2159380, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["真的? 不後悔嗎?"], [3, 0, 2159380, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["系統慢慢恢復就好。 雖然會花些時間……。但比起被操控的話，有這樣的裝置更危險。不想再被操控了。"], [3, 0, 2159380, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["都這樣說的話，我知道了。幫你解除。"], [3, 0, 2159380, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["很好。解除了。剛開始應該不適應，說不定會暈暈的。"], [3, 0, 2159380, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.teachSkill(30021238, -1, 0); // 刀舞
        ms.lockUI(0);
        var level = 10 - ms.getLevel();
        for (var i = 0 ; i < level ; i++) {
            ms.levelUp();
        }
        ms.gainItem(2000018, 15);
        ms.gainItem(1142575, 1);
        ms.gainItem(3010585, 1);
        ms.dispose();
        ms.warp(310010000, 0);
    } else {
        ms.dispose();
    }
}
