/*
 Author: Pungin
 */

function start() {
    ms.lockUI(true);
    ms.disableOthers(true);
    ms.getDirectionEffect(1, "", [3000]);
    ms.getDirectionStatus(true);
    ms.dispose();
    ms.openNpc(2159314);
}