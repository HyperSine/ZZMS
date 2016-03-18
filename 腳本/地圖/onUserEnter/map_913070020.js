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
        ms.getTopMsg("雜貨商店後院");
        ms.spawnMob(9001051, 229, 65);
        ms.dispose();
    } else {
        ms.dispose();
    }
}
