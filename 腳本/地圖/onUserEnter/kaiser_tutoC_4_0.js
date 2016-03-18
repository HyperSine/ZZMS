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
        ms.spawnNPCRequestController(3000106, 150, 50, 1, 8384662);
        ms.spawnNPCRequestController(3000107, 70, 50, 1, 8384663);
        ms.getNPCTalk(["清醒了嗎？"], [3, 0, 3000106, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["培霖？嗚嗚…這裡是哪裡啊？我是活著嗎？"], [3, 0, 3000106, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["現在好多了。"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["卡塔利溫？提爾怎麼樣了？"], [3, 0, 3000107, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那孩子很安全。只是有點小麻煩… "], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["為什麼對我使用敬語…呢？"], [3, 0, 3000107, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你叫凱撒。凱撒是超新星們要尊重的對象。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["凱撒？我？我不知道你在說什麼。"], [3, 0, 3000106, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["受到貝代洛斯請求，我與培霖前往東邊的聖所的時候， 發現你在那裡覺醒成凱撒。"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["到達的時候你已經打敗可疑的師弟們，昏迷的狀態。"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["提爾也沒事所以不用擔心。聖物的詛咒好像下給提爾所以有點擔心。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["聖物的詛咒？不 我是不能相信我是凱撒這句話？"], [3, 0, 3000106, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["到達的時候你身邊有極光圍繞著而且還有變身的徵兆所以是凱撒沒錯。雖然不太能接受，你是個命運的孩子。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["那我…我會怎麼樣？"], [3, 0, 3000106, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["接受自己的命運，做出相符的行為就可以了。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["年紀輕輕的就讓你走苦路，有了偉大的力量也有相對的責任。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我擁有那種力量的話我會做。但我目前還感覺不到那種力量。"], [3, 0, 3000106, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["凱撒是投胎的，投胎之後就不會記得前世的能力與記憶。也就是說凱撒你擁有無窮盡的潛在力的原石。"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["趕快修練成為像前一代凱撒般的凱撒吧。帶領我們的超新星吧。"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["不要一直催。卡塔利溫。應該還體會不到沉重的命運。先休息一下吧。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["現在的凱撒是弱的狀態，所以不要到處說是凱撒。那是讓自己變得危險的行為。"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我比較擔心提爾。提爾在哪裡？"], [3, 0, 3000107, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["比凱撒提早醒來確認自己的狀態後往萬神殿跑過去了。克里安跟隨在後面應該沒事的。"], [3, 0, 3000107, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["頭腦很混亂。身體好一點了我需要慢慢整理一下思考。"], [3, 0, 3000107, 0, 17, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["不要太勉強。"], [3, 0, 3000106, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.removeNPCRequestController(8384662);
        ms.removeNPCRequestController(8384663);
        ms.dispose();
        ms.warp(400000000, 0);
    } else {
        ms.dispose();
    }
}
