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
        ms.getTopMsg("林伯特的雜貨商店");
        ms.getTopMsg("楓之谷曆XXXX年 3月8日");
        ms.dispose();
    } else {
        ms.dispose();
    }
}
