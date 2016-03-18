function start() {
    ms.lockUI(true);
    ms.disableOthers(true);
    ms.teachSkill(20000014, -1, 0);
    ms.teachSkill(20000015, -1, 0);
    ms.teachSkill(20000016, -1, 0);
    ms.teachSkill(20000017, -1, 0);
    ms.teachSkill(20000018, -1, 0);
    ms.lockUI(false);
    ms.showWZEffect("Effect/Direction1.img/aranTutorial/ClickLilin");
    ms.dispose();
}