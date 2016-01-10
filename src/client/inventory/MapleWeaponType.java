package client.inventory;

import client.MapleJob;

public enum MapleWeaponType {

    沒有武器(1.43f, 20),
    閃亮克魯(1.2f, 25),
    靈魂射手(1.7f, 15),
    魔劍(1.3f, 20),
    能量劍(1.3125f, 20), 
    幻獸棍棒(1.34f, 20),
    單手劍(1.2f, 20),
    單手斧(1.2f, 20),
    單手棍(1.2f, 20),
    短劍(1.3f, 20),
    雙刀(1.3f, 20),
    手杖(1.3f, 20),
    短杖(1.0f, 25),//冒險家係數為1.2
    長杖(1.0f, 25),//冒險家係數為1.2
    雙手劍(1.34f, 20),
    雙手斧(1.34f, 20),
    雙手棍(1.34f, 20),
    槍(1.49f, 20),
    矛(1.49f, 20),
    弓(1.3f, 15),
    弩(1.35f, 15),
    拳套(1.75f, 15),
    指虎(1.7f, 20),
    火槍(1.5f, 15),
    雙弩槍(1.3f, 15), //beyond op
    加農炮(1.5f, 15),
    太刀(1.25f, 20),
    扇子(1.35f, 25),
    琉(1.49f, 20),
    璃(1.34f, 20),
    ESP限幅器(1.0f, 20),
    未知(1.0f, 15),
    ;
    private final float damageMultiplier; // 武器係數
    private final int baseMastery; // 初始武器熟練度

    private MapleWeaponType(final float maxDamageMultiplier, int baseMastery) {
        this.damageMultiplier = maxDamageMultiplier;
        this.baseMastery = baseMastery;
    }

    public final float getMaxDamageMultiplier(int jobId) {
        if (MapleJob.is法師(jobId) && MapleJob.is冒險家(jobId) && (this == MapleWeaponType.短杖 || this == MapleWeaponType.長杖)) {
            return damageMultiplier + 0.2f;
        } else {
            return damageMultiplier;
        }
    }

    public final int getBaseMastery() {
        return baseMastery;
    }
};
