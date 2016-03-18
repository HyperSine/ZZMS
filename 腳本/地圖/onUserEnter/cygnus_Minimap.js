/* global ms */

function action(mode, type, selection) {
    ms.ShowWZEffect("Effect/OnUserEff.img/guideEffect/cygnusTutorial/0");
    ms.teachSkill(10000252, 1, 1);
    ms.teachSkill(10001244, 1, 1);
    ms.teachSkill(10001254, 1, 1);
    ms.dispose();
}