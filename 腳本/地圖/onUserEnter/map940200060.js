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
        ms.getDirectionEffect(9, "", [0]);
        ms.spawnNPCRequestController(3002100, 10, 20, 1, 11628981);
        ms.getDirectionEffect(1, "", [100]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(11628981, 1, 250, 100);
        ms.getDirectionEffect(3, "", [2]);
        ms.getDirectionEffect(1, "", [4500]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(11628981, -1, 70, 100);
        ms.getDirectionEffect(3, "", [0]);
        ms.getDirectionEffect(1, "", [2000]);
    } else if (status === i++) {
        ms.getNPCTalk(["人類原本就走得這麼慢嗎?是因為沒有尾巴嗎?"], [3, 0, 3002100, 0, 5, 0, 0, 1, 0, 3002100]);
    } else if (status === i++) {
        ms.getNPCTalk(["呃呃,真受不了!我要先走了.已經快到村莊了,而且現在也沒什麼危險,自己跟上吧.沿著這條路來就行了.別走到別條路去喔.那我先走啦!"], [3, 0, 3002100, 0, 5, 0, 1, 1, 0, 3002100]);
    } else if (status === i++) {
        ms.updateNPCSpecialAction(11628981, 1, 510, 100);
        ms.getDirectionEffect(1, "", [3000]);
    } else if (status === i++) {
        ms.getNPCTalk(["...真是任性.一下拉著我一起到村裡,一下又要我自己過去.呼,我看還是先跟上比較好.總不能在這裡走丟吧."], [3, 0, 3002100, 0, 17, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getTopMsg("按下鍵盤的方向鍵[←],[→]鍵就能移動.");
        ms.removeNPCRequestController(11628981);
        ms.forceStartQuest(38000);
        var level = 10 - ms.getLevel();
        for (var i = 0 ; i < level ; i++) {
            ms.levelUp();
        }
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
