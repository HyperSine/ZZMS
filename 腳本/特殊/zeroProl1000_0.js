/* global cm */
var status = -1;

function action(mode, type, selection) {
    if (mode === 0) {
        status--;
    } else {
        status++;
    }

    var i = -1;
    if (status <= i++) {
        cm.dispose();
    } else if (status === i++) {
        cm.getDirectionStatus(true);
        cm.lockUI(1, 1);
        cm.getNPCTalk(["...你要去哪裡呢?你不是沒有參與這次的作戰嗎?你的任務不是和受傷的我一起待在村裡?難道你忘記你的任務嗎?"], [3, 0, 2410004, 0, 33, 0, 0, 1, 0]);
        cm.playVoiceEffect("Voice.img/Ken/7");
    } else if (status === i++) {
        cm.getNPCTalk(["你絕對不能去 #m321000600#.就像 #p2410000# 醫務隊長所說的,請你閉嘴安靜的待在村裡,等所有的事情結束."], [3, 0, 2410004, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        cm.playVoiceEffect("Voice.img/Ken/8");
        cm.getDirectionEffect(3, "", [1]);
        cm.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        cm.getDirectionEffect(3, "", [0]);
        cm.getDirectionEffect(1, "", [100]);
    } else if (status === i++) {
        cm.getDirectionEffect(0, "", [5, 0]);
        cm.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        cm.setNPCSpecialAction(11601142, "punch", 0, true);
        cm.showEnvironment(5, "demonSlayer/punch", []);
        cm.getDirectionEffect(1, "", [500]);
    } else if (status === i++) {
        cm.getNPCTalk(["...!你在做什麼?竟然攻擊影子騎士同伴!你不知道叛徒是不被原諒的嗎?"], [3, 0, 2410004, 0, 33, 0, 0, 1, 0]);
    } else if (status === i++) {
        cm.playVoiceEffect("Voice.img/Ken/10");
        cm.getNPCTalk(["#face3#叛徒?一開始就不是站在同一邊的話...就不是叛徒吧?是不是?"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
    } else if (status === i++) {
        cm.playVoiceEffect("Voice.img/Alpha/87");
        cm.getNPCTalk(["你...發現了嗎?!"], [3, 0, 2410004, 0, 33, 0, 1, 1, 0]);
        cm.playVoiceEffect("Voice.img/Ken/11");
    } else if (status === i++) {
        cm.getNPCTalk(["#face2#你認為我會永遠被你們的謊言矇騙嗎?你相信這齣戲會永遠演下去嗎?"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        cm.playVoiceEffect("Voice.img/Alpha/88");
    } else if (status === i++) {
        cm.getNPCTalk(["哼!現在發現已經來不及了!反正大家都去了 #m321000600#!你死定了!"], [3, 0, 2410004, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        cm.playVoiceEffect("Voice.img/Ken/12");
        cm.getNPCTalk(["#face3#還是擔心自己的性命吧...你以為我會把你放著離開嗎?"], [3, 0, 2410008, 0, 41, 0, 1, 1, 0]);
        cm.playVoiceEffect("Voice.img/Alpha/89");
    } else if (status === i++) {
        cm.getNPCTalk(["你要打一架嗎!哈!笨蛋!"], [3, 0, 2410004, 0, 33, 0, 1, 1, 0]);
    } else if (status === i++) {
        cm.playVoiceEffect("Voice.img/Ken/13");
        cm.forceStartQuest(40011, "1");
        cm.removeNPCRequestController(11601142);
        cm.spawnMob(9300746, 1170, -6);
        cm.lockUI(0);
        cm.forceStartQuest(40007, "1");
        cm.dispose();
    } else {
        cm.dispose();
    }
}
