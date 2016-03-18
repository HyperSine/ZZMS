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
        ms.getDirectionEffect(9, "", [1]);
        ms.spawnNPCRequestController(3000106, 250, 20, 0, 8379543);
        ms.spawnNPCRequestController(3000107, -800, 20, 1, 8379544);
        ms.spawnNPCRequestController(3000108, -600, 20, 1, 8379545);
        ms.getNPCTalk(["凱撒會不會發生什麼事情？感覺不太好… "], [3, 0, 3000106, 0, 1, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["大師弟！"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(5, "", [0, 300, -400, 27]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1334]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(8379544, 1, 450, 100);
        ms.updateNPCSpecialAction(8379545, 1, 450, 100);
        ms.getDirectionEffect(5, "", [1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["是什麼事呢？這麼趕…貝德你怎麼會這來…"], [3, 0, 3000106, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["赫力席母已經被攻破了。我和赫力席母的住戶躲到這裡來。"], [3, 0, 3000108, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["赫力席母… 但凱撒不久前往赫力席母去了。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["包含我在內赫力席母的居民在赫力席母被攻破前逃走。在避難的時候看到赫力席母那裡有巨大的爆發，那個爆發有可能是因為凱撒吧。"], [3, 0, 3000108, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["培霖！我帶領這裡的守衛兵到赫力席母看看。要幫助凱撒擊退入侵者！"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["不要做傻事！ 赫力席母已經被攻破了！我們要在這裡使用防護罩撐下去。那才是我們超新星能夠生存的唯一的方法。"], [3, 0, 3000108, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那只能看著凱撒死去嗎？"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那裡有數萬個幽靈！即使是凱撒也是一個人抵擋不住的！ 凱撒如果活著的話已經往這裡逃，不然已經不在人世。"], [3, 0, 3000108, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/tuto/BalloonMsg0/2", [0, 0, -100, 1, 1, 0, 8379543, 0]);
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/tuto/BalloonMsg0/2", [0, 0, -120, 1, 1, 0, 8379544, 0]);
        ms.getDirectionEffect(2, "Effect/Direction9.img/effect/tuto/BalloonMsg0/2", [0, 0, -120, 1, 1, 0, 8379545, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getNPCTalk(["但赫力席母被攻破的狀態下事實上沒有在這裡使用防護罩的力量。與赫力席母的聖物聯繫的方法消失了。"], [3, 0, 3000106, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["所以我逃亡的時候讓師弟們把聖物帶到這裡來。有聖物的話可以使用在赫力席母同樣的水準的防護罩。"], [3, 0, 3000108, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你逃跑的時候先把…"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["要怪我沒關係。因為逃跑的事實就不能讓我自由。但我是為了我們超新星所做的決定。決定一起死很容易。但我選擇未來的希望。"], [3, 0, 3000108, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但赫力席母的防護罩不是也被攻破嗎？使用防護罩也不一定會安全吧？"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["對了，有了防護罩的赫力席母怎麼那麼容易被攻破啊？"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["還記得在評議會被剔除的梅格耐斯嗎？"], [3, 0, 3000108, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["聽說實力雖然跟凱撒一樣好但他是只追求力的兇猛傢伙所以被剔除。"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["…他親自解除防護罩。不久前疲憊的進到赫力席母來當時看起來以為悔改。看到那樣的梅格耐斯安心的時候他把達爾默的軍隊整頓在赫力席母周圍先把防護罩除掉與幽靈一起以瞬間站領了赫力席母。"], [3, 0, 3000108, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["達爾默佔領阿寶立斯後到這裡來需要很多時間，以為有防護罩能撐下去的。達爾默幾乎是同時攻下阿寶立斯與赫力席母，把梅格耐斯做為前鋒。我們連那種事都不知道就讓他進到城裡也都沒有做任何打算。"], [3, 0, 3000108, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["…話說國王與王族都在哪裡呢？"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["不知道。現在生死不明的不只一兩個。如果活著的話應該會到這裡來吧。話先不說要先構成防護罩。"], [3, 0, 3000108, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["這次不要把聖物放在一起，分散保管。又不知道什麼時候會發生像赫力席母的情況。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但是分開保管能分散危險，相對來說比較難保護聖物。"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["首先在聖物施上被允許的固有師弟能使用的保護魔法。但也不是這樣就完美了。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["防護罩就交給你了。假如凱撒死了什麼時候會投胎？"], [3, 0, 3000108, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["…只知道從今以後出生的孩子其中一個，其他的什麼都不知道。只能等待。有特別的機會的話凱撒會自己覺醒。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["但要把孩子其中一個能夠成為凱撒的事情先保密。想知道投胎的人站在敵方，這個事實被他們知道我們的孩子們也會有危險的。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(8379543);
        ms.removeNPCRequestController(8379544);
        ms.removeNPCRequestController(8379545);
        ms.dispose();
        ms.warp(940002040, 0);
    } else {
        ms.dispose();
    }
}
