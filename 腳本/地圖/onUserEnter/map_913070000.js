/* global ms */
var status = -1;
var select = -1;

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
        ms.disableOthers(true);
        ms.sendSimpleS("你好, 請問你的性別是?\r\n#L0#我是一名#b男生#k, 你看不到嗎?!#l\r\n#L1#好吧, 我當然是一名#r女生#k啦!#l", 1, 9010000);
    } else if (status === i++) {
        if (select === -1) {
            select = selection;
            ms.unequip(-6, true); // 褲子
            if (select === 1) {
                ms.setGender(1);
                ms.setFace(21158); // 青色乖女孩臉型
                ms.setHair(34773); // 黃色艾麗亞造型
                ms.equip(1051355, -5, true); // 黃色點點整套
            } else {
                ms.setGender(0);
                ms.setFace(20169); // 青色米哈逸臉型
                ms.setHair(36033); // 黃色米哈逸造型
                ms.equip(1050288, -5, true); // 紅色圍巾套服
            }
            ms.equip(1072833, -7, true); // 黃色冒險鞋
        }
        ms.sendNextS("啊, 原來是一名可愛的" + (select === 0 ? "男生" : "女生") + "啊, 好的, 祝你遊戲愉快!", 1);
    } else if (status === i++) {
        ms.getTopMsg("林伯特的雜貨商店");
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getTopMsg("楓之谷曆 XXXX年 3月4日… ");
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction7.img/effect/tuto/step0/0", [2000, 0, -100, 1, 0, -100]);
        ms.getDirectionFacialExpression(6, 10000);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction7.img/effect/tuto/step0/1", [2000, 0, -100, 1, 0, -100]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(2, "Effect/Direction7.img/effect/tuto/step0/2", [3000, 0, -100, 1, 0, -100]);
        ms.getDirectionFacialExpression(4, 8000);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(2, "Effect/Direction7.img/effect/tuto/step0/3", [3000, 0, -100, 1, 0, -100]);
        ms.getDirectionFacialExpression(6, 2000);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(3, "", [0]);
        ms.getNPCTalk(["有…有話跟我說嗎？"], [4, 1106000, 0, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你叫什麼名字？"], [4, 1106000, 0, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我…沒有名字。叫我 #b「小毛頭」#k。林伯特大叔也這樣叫。你找什麼物品呢？"], [4, 1106000, 0, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["家人…沒有家人嗎？"], [4, 1106000, 0, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我沒有家人…#b\r\n（這個人是什麼，為什麼問我這些？ ）#k\r\n如果沒有需要的物品…我就先…"], [4, 1106000, 0, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["你知道聖殿騎士雷神之錘嗎？"], [4, 1106000, 0, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["雷神之錘嗎？ 這個嘛...我不太...#b\r\n（雷神之錘是誰？ 這個人為什麼問我這些？）"], [4, 1106000, 0, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["#e小毛頭！\r\n叫你把箱子清一清，你還在跟誰聊天？"], [4, 1106000, 0, 0, 5, 0, 1, 1, 0, 1106002]);
    } else if (status === i++) {
        ms.getNPCTalk(["是…是！ 林伯特大叔！ 我要整理了！\r\n那個，那麼…我…先告辭…"], [4, 1106000, 0, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.forceCompleteQuest(20030);
        ms.getDirectionEffect(2, "Effect/Direction7.img/effect/tuto/step0/4", [2000, 0, -100, 1, 0, -100]);
        ms.getDirectionFacialExpression(6, 2000);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getNPCTalk(["去…哪裡呢？ 這個人？\r\n真是的！ 在林伯特大叔教訓我之前要快點把箱子清乾淨！"], [4, 1106000, 0, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [800]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.disableOthers(false);
        ms.dispose();
        ms.warp(913070001, 0);
    } else {
        ms.dispose();
    }
}
