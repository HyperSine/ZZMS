/* global im */

function start() {
    if (im.haveItem(2432253)) {
        im.gainItem(2432253, -1);
        im.addHP(500000);
    }
    im.dispose();
}