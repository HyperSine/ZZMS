/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package client.inventory;

public enum ItemFlag {

    // 封印
    LOCK(0x01),
    // 防滑
    SPIKES(0x02),
    // 防寒
    COLD(0x04),
    // 不可交易
    UNTRADABLE(0x08),
    // 裝備可以交換1次
    KARMA_EQ(0x10),
    // 消耗?可以交換1次
    KARMA_USE(0x02),
    // 裝備時獲得魅力
    CHARM_EQUIPPED(0x20),
    // 機器人激活
    ANDROID_ACTIVATED(0x40),
    CRAFTED(0x80),
    CRAFTED_USE(0x10),
    //裝備防爆
    SHIELD_WARD(0x100),
    //幸運捲軸
    LUCKY_DAY(0x200),
    KARMA_ACC_USE(0x400),
    KARMA_ACC(0x1000),
    // 保護升級次數
    SLOTS_PROTECT(0x2000),
    // 捲軸防護
    SCROLL_PROTECT(0x4000);
    private final int value;

    private ItemFlag(int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }

    public final boolean check(int flag) {
        return (flag & value) == value;
    }
}
