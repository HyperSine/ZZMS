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
        ms.spawnNPCRequestController(9330204, 339, -7, 0, 8516447);
        ms.getNPCTalk(["現在有提起精神了嗎？"], [3, 0, 9330204, 0, 1, 0, 0, 1, 0]);
        ms.getDirectionStatus(true);
    } else if (status === i++) {
        ms.getNPCTalk(["咻。耶願。這裡是哪裡呢？父親呢？"], [3, 0, 9330204, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["幸好我的傷口沒有哪麼深，所以還算可以幫忙掩護你。"], [3, 0, 9330204, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["已經說好將會在渡口見面，所以，趕緊去吧。"], [3, 0, 9330204, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["是喔? 真的嗎? "], [3, 0, 9330204, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [720]);
    } else if (status === i++) {
        ms.getNPCTalk(["噓，稍等一下。"], [3, 0, 9330204, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.getNPCTalk(["好像還有跟蹤我們的傢伙的樣子。"], [3, 0, 9330204, 0, 1, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我來除掉這些傢伙。得在此地分手了。請記住。要在渡口見面！"], [3, 0, 9330204, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["我會除掉追逐你們的傢伙，可是前往的路上仍然不太順遂。我會在路上放上告知的標誌板，請多小心。"], [3, 0, 9330204, 0, 1, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["恩，知道了!"], [3, 0, 9330204, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.setNPCSpecialAction(8516447, "teleportation", 0, true);
        ms.getDirectionEffect(1, "", [720]);
    } else if (status === i++) {
        ms.removeNPCRequestController(8516447);
        ms.getNPCTalk(["父親真的會沒事嗎？"], [3, 0, 9330204, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getNPCTalk(["如果回頭的話？不是的。渡口！先去渡口！耶願不可能說謊！在渡口見父親。"], [3, 0, 9330204, 0, 3, 0, 1, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1500]);
    } else if (status === i++) {
        ms.getNPCTalk(["好吧，就照耶願說的去見面吧。"], [3, 0, 9330204, 0, 3, 0, 0, 1, 0]);
    } else if (status === i++) {
        ms.getDirectionEffect(1, "", [1000]);
    } else if (status === i++) {
        ms.lockUI(0);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
