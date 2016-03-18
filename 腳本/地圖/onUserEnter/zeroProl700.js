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
        ms.getDirectionEffect(3, "", [1]);
        ms.getDirectionEffect(1, "", [3000]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        ms.getNPCTalk(["喂,#p2410008#!你回來晚了!說留下你一個人我自己回來,我被罵慘了!他們說我不該丟下王牌你呀!可是...老實說."], [3, 0, 2410003, 0, 33, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Milro/10");
    } else if (status === i++) {
        ms.getNPCTalk(["像你王牌都被攻擊的話,我還能怎麼對付呢!那麼我留下,還沒幫上忙可能就沒命了,為什麼我還一定要留下呢!你說是不是?"], [3, 0, 2410003, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Milro/11");
    } else if (status === i++) {
        ms.getNPCTalk(["#face10#抱歉,#p2410003# 前輩...我應該早點消滅它,可是...沒想到 #o9300744#跑那麼遠,所以有點吃力.很抱歉."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/58");
        ms.getNPCTalk(["你該不會是消滅 #o9300744# 之後去做別的才回來晚了吧?"], [3, 0, 2410004, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Ken/0");
    } else if (status === i++) {
        ms.getNPCTalk(["#face10#什麼?啊... 難道...是被發現了嗎?"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/59");
        ms.getDirectionEffect(2, "Effect/Direction5.img/effect/mercedesInIce/merBalloon/5", [1500, 652, -131, 1, 1, 0, 0, 0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getNPCTalk(["你真的...!"], [3, 0, 2410004, 0, 33, 0, 0, 1, 0]);
        ms.playVoiceEffect("Voice.img/Ken/1");
    } else if (status === i++) {
        ms.getNPCTalk(["其實消滅怪物之後,我在空地裡練習了一下劍術.我知道該早點回來,可是最近都在戰鬥所以沒時間就...抱歉了!"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/60");
    } else if (status === i++) {
        ms.getNPCTalk(["啊...是這樣嗎?"], [3, 0, 2410004, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Ken/2");
        ms.getNPCTalk(["說真的.怎麼可能被發現...哼.果然是影子騎士團的模範生啊.你還真喜歡練習沒用的劍術啊."], [3, 0, 2410004, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Ken/3");
    } else if (status === i++) {
        ms.getNPCTalk(["嘿.你跩什麼呀.學弟做得好,你身為學長就該像學長一樣溫暖的對待嘛."], [3, 0, 2410003, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Milro/12");
    } else if (status === i++) {
        ms.getNPCTalk(["什麼學弟...哼."], [3, 0, 2410004, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Ken/4");
        ms.getNPCTalk(["啊...啊,#p2410008#.別理他.#p2410004#本來就很狂傲嘛.發什麼脾氣呀.不管怎樣沒有受傷嗎?"], [3, 0, 2410003, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Milro/13");
    } else if (status === i++) {
        ms.getNPCTalk(["#face5#嗯.還好沒有什麼受傷.謝謝你的關心."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Alpha/61");
        ms.getNPCTalk(["沒事,沒受傷就好.受傷了就要到治療所.其實 #p2410000# 醫務隊長總是笑笑的,又不是什麼可怕的人...但是不想整天和他在一起...是不是,#p2410004#?"], [3, 0, 2410003, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Milro/14");
    } else if (status === i++) {
        ms.getNPCTalk(["你手肘受傷住院時,不是說很無聊什麼的嗎."], [3, 0, 2410003, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Milro/15");
        ms.getNPCTalk(["你不要亂說!想到他我就不開心...放在治療所的那個 #r拼圖#k...真是最爛的嗜好.要是我才不會把它放在那裡."], [3, 0, 2410004, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Ken/5");
    } else if (status === i++) {
        ms.getNPCTalk(["說的也是.拿出來也看不懂,應該也沒什麼關係.哼."], [3, 0, 2410004, 0, 33, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Ken/6");
    } else if (status === i++) {
        ms.getNPCTalk(["啊!現在才想到 #p2410008# 你應該很疲累嗎?快回宿舍去吧.報告讓我來就好了."], [3, 0, 2410003, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.playVoiceEffect("Voice.img/Milro/16");
        ms.getNPCTalk(["#face5#啊...謝謝.那就交給你.前輩.我先回去了."], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        ms.playVoiceEffect("Voice.img/Alpha/62");
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
        ms.warp(321000800, 1);
    } else {
        ms.dispose();
    }
}
