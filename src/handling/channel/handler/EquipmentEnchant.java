package handling.channel.handler;

import client.MapleClient;
import client.inventory.EchantEquipStat;
import client.inventory.EchantScroll;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import constants.ItemConstants;
import constants.ServerConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataTool;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.Randomizer;
import tools.data.LittleEndianAccessor;
import tools.packet.CWvsContext;

public class EquipmentEnchant {

    //星力強化
    private static final int[] sfSuccessProp = {950, 900, 850, 850, 800, 750, 700, 650, 600, 550, 450, 350, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 30, 20, 10};
    private static final int[] sfDestroyProp = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 10, 10, 20, 20, 20, 30, 30, 70, 70, 190, 290, 400};
    private static final int[] sfFallProp = {0, 0, 0, 0, 0, 0, 30, 35, 40, 45, 0, 65, 69, 69, 69, 0, 68, 68, 67, 67, 0, 63, 78, 69, 59};

    private static enum StarForceResult {

        降級(0),
        成功(1),
        損壞(2),
        失敗(3),
        默認(-1);

        private byte value;

        StarForceResult(int value) {
            this.value = (byte) value;
        }

        public byte getValue() {
            return value;
        }
    }

    public static void handlePacket(final LittleEndianAccessor slea, final MapleClient c) {
        byte code = slea.readByte();
        switch (code) {
            case 0: { //咒文的痕跡 強化
                slea.skip(4);
                short position = slea.readShort();
                short sposition = slea.readShort();
                short success = slea.readShort();
                success = 0;
                Equip item = (Equip) c.getPlayer().getInventory(position < 0 ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP).getItem(position);
                if (item == null) {
                    c.getSession().write(CWvsContext.Enchant.getEnchantResult(item, 0, sposition, c));
                    return;
                }
                ArrayList<EchantScroll> scrolls = getEchantScrolls(item);
                if (scrolls.isEmpty() || scrolls.size() < sposition - 1 || item.getUpgradeSlots() < 1 || !c.getPlayer().haveItem(4001832, scrolls.get(sposition).getCost())) {
                    c.getSession().write(CWvsContext.Enchant.getEnchantResult(item, 0, sposition, c));
                    if (c.getPlayer().isShowErr()) {
                        c.getPlayer().showInfo("咒文強化", true, "咒文強化檢測 找不到捲軸：" + (scrolls.isEmpty()) + " 收到的捲軸不在範圍：" + (scrolls.size() < sposition - 1) + " 沒有剩餘升級次數：" + (item.getUpgradeSlots() < 1) + " 咒文不足：" + (!c.getPlayer().haveItem(4001832, scrolls.get(sposition).getCost())));
                    }
                    return;
                }
                EchantScroll scroll = scrolls.get(sposition);
                if (Randomizer.isSuccess(scroll.getSuccessRate())) {
                    success = 1;
                }
                if (c.getPlayer().isShowInfo()) {
                    c.getPlayer().showMessage(30, "咒文強化 - 強化道具：" + item + " 選中捲軸:" + scroll.getName() + " 消耗咒文：" + scroll.getCost() + " 成功率：" + scroll.getSuccessRate() + "%  強化結果：" + (success == 1));
                }
                int mask = scroll.getMask();
                
                if (c.getPlayer().isAdmin()) {
                    success = 1;
                    c.getPlayer().dropMessage(-6, "伺服器管理員咒文強化成功率提升到100%");
                }
                
                if (success == 1) {
                    for (int i : scroll.getValues()) {
                        if (EchantEquipStat.WATK.check(mask)) {
                            item.setWatk((short) (item.getWatk() + i));
                            mask = mask ^ EchantEquipStat.WATK.getValue();
                        } else if (EchantEquipStat.MATK.check(mask)) {
                            item.setMatk((short) (item.getMatk() + i));
                        } else if (EchantEquipStat.STR.check(mask)) {
                            item.setStr((short) (item.getStr() + i));
                        } else if (EchantEquipStat.DEX.check(mask)) {
                            item.setDex((short) (item.getDex() + i));
                        } else if (EchantEquipStat.INT.check(mask)) {
                            item.setInt((short) (item.getInt() + i));
                        } else if (EchantEquipStat.LUK.check(mask)) {
                            item.setLuk((short) (item.getLuk() + i));
                        } else if (EchantEquipStat.MHP.check(mask)) {
                            item.setHp((short) (item.getHp() + i));
                        }
                    }
                    item.setLevel((byte) (item.getLevel() + 1));
                }

                MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(4001832), 4001832, (short) scroll.getCost(), true, false);
                item.setUpgradeSlots((byte) (item.getUpgradeSlots() - 1));
                c.getSession().write(CWvsContext.Enchant.getEnchantResult(item, success, sposition, c));
                c.getPlayer().forceReAddItem(item, item.getPosition() >= 0 ? MapleInventoryType.EQUIP : MapleInventoryType.EQUIPPED);
                break;
            }
            case 1: { //星力強化
                slea.skip(4);
                short position = slea.readShort();
                boolean maplePoint = slea.readByte() == 1;
                boolean mgsuccess = slea.readByte() == 1;
                boolean safe = false;
                if (mgsuccess) {
                    slea.skip(4);
                }
                int targetstar = slea.readInt();
                final Equip item = (Equip) c.getPlayer().getInventory(position < 0 ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP).getItem(position);
                if (item == null) {
                    c.getSession().write(CWvsContext.Enchant.getStarForceResult(c.getPlayer(), item, item, StarForceResult.失敗.getValue()));
                    return;
                }
                if (maplePoint) {
                    c.getSession().write(CWvsContext.Enchant.getStarForceResult(c.getPlayer(), item, item, StarForceResult.失敗.getValue()));
                    c.getPlayer().dropMessage(1, "暫時無法使用楓點強化。");
                    return;
                }
                final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                int reqLevel = ii.getReqLevel(item.getItemId());
                long meso = maplePoint ? safe ? 50 : 9 : getStarForceMeso(reqLevel, item.getEnhance());
                if (item.getUpgradeSlots() > 0 || item.getEnhance() >= ItemConstants.卷軸.getEnhanceTimes(item.getItemId()) || ii.isCash(item.getItemId()) || maplePoint ? c.getPlayer().getCSPoints(2) < meso : c.getPlayer().getMeso() < meso) {
                    c.getSession().write(CWvsContext.Enchant.getStarForceResult(c.getPlayer(), item, item, StarForceResult.失敗.getValue()));
                    if (c.getPlayer().isShowErr()) {
                        c.getPlayer().showInfo("星力強化", true, "星力強化檢測 裝備是否有剩餘升級次數：" + (item.getUpgradeSlots() >= 1) + " 裝備星級是否大於可升級的星數：" + (item.getEnhance() >= ItemConstants.卷軸.getEnhanceTimes(item.getItemId())) + " 裝備是否是為點裝：" + ii.isCash(item.getItemId()) + (maplePoint ? " 楓點" : " 楓幣") + "不足：" + (maplePoint ? c.getPlayer().getCSPoints(2) < meso : c.getPlayer().getMeso() < meso));
                    }
                    return;
                }
                int successprop = sfSuccessProp[item.getEnhance()] / 10;
                int destroyprop = sfDestroyProp[item.getEnhance()] / 10;
                int fallprop = sfFallProp[item.getEnhance()];
                StarForceResult result;
                if (Randomizer.isSuccess(successprop - (mgsuccess ? 0 : 10)) || item.getFailCount() >= 2) { //成功
                    result = StarForceResult.成功;
                } else { //失敗
                    if (Randomizer.isSuccess(fallprop)) {
                        result = StarForceResult.降級;
                    } else if (Randomizer.isSuccess(destroyprop) && !safe) { //損壞概率
                        //道具損壞
                        result = StarForceResult.損壞;
                    } else {
                        result = StarForceResult.失敗;
                    }
                }
                if (c.getPlayer().isShowInfo()) {
                    c.getPlayer().showMessage(30, "星力強化 - 強化道具：" + item + " 當前星級：" + item.getEnhance() + "星 消耗幣種：" + (maplePoint ? "楓點" : "楓幣") + " 消耗量：" + meso + " 成功率：" + (successprop - (mgsuccess ? 0 : 10)) + "% 小遊戲成功率加成：" + (mgsuccess ? 10 : 0) + "% 損壞率：" + destroyprop + "% 降級率：" + fallprop + "% 強化結果：" + result.name());
                }
                if (c.getPlayer().isAdmin()) {
                    result = StarForceResult.成功;
                    c.getPlayer().dropMessage(-6, "伺服器管理員升星成功率提升到100%");
                }
                //道具處理
                switch (result) {
                    case 降級:
                        StarForceEnhanceItem(item, true);
                        item.setFailCount(item.getFailCount() + 1);
                        break;
                    case 成功:
                        StarForceEnhanceItem(item, false);
                        item.setFailCount(0);
                    case 損壞:
                    case 失敗:
                        item.getFailCount();
                        item.setFailCount(0);
                        break;
                }
                if (maplePoint) {
                    c.getPlayer().modifyCSPoints(2, (int) -meso);
                } else {
                    c.getPlayer().gainMeso(-meso, false, false);
                }
                c.getPlayer().forceReAddItem(item, item.getPosition() >= 0 ? MapleInventoryType.EQUIP : MapleInventoryType.EQUIPPED);
                final Equip source = (Equip) item.copy();
                c.getSession().write(CWvsContext.Enchant.getStarForceResult(c.getPlayer(), source, item, result.getValue()));
                break;
            }
            case 50: { // 咒文的痕跡 放置道具
                short position = slea.readShort();
                slea.skip(2);
                Equip item = (Equip) c.getPlayer().getInventory(position < 0 ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP).getItem(position);
                c.getSession().write(CWvsContext.Enchant.getEnchantList(c, item, getEchantScrolls(item), ServerConstants.FEVER_TIME));
                break;
            }
            case 52: { // 星力強化 放置道具
                short position = slea.readShort();
                Equip item = (Equip) c.getPlayer().getInventory(position < 0 ? MapleInventoryType.EQUIPPED : MapleInventoryType.EQUIP).getItem(position);
                int reqLevel = MapleItemInformationProvider.getInstance().getReqLevel(item.getItemId());
                long meso = getStarForceMeso(reqLevel, item.getEnhance());
                int successprop = sfSuccessProp[item.getEnhance()];
                int destroyprop = sfDestroyProp[item.getEnhance()];
                Map<EchantEquipStat, Integer> stats = getStarForceStatus(item);
                c.getSession().write(CWvsContext.Enchant.getStarForcePreview(c, stats, meso, successprop, destroyprop, destroyprop != 0, item.getFailCount() >= 2));
                break;
            }
            case 53: { //星力強化成功率加成遊戲
                c.getSession().write(CWvsContext.Enchant.getStarForceMiniGame());
                break;
            }
            default: {
                System.out.println("裝備強化系統,未知操作碼:" + code);
            }
        }
    }

    public static ArrayList<EchantScroll> getEchantScrolls(Equip eq) {
        ArrayList<EchantScroll> scrolls = new ArrayList();
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        MapleData data = ii.getItemData(eq.getItemId());
        MapleData info = data.getChildByPath("info");
        int reqJob = MapleDataTool.getInt("reqJob", info, 0);
        Map<EchantEquipStat, Integer> stats = new HashMap();
        if (ItemConstants.類型.武器(eq.getItemId()) || ItemConstants.類型.雙刀(eq.getItemId())) {
            switch (reqJob) {
                case 0: {
                    scrolls.add(EchantScroll.攻擊力_力量_100);
                    scrolls.add(EchantScroll.攻擊力_力量_70);
                    scrolls.add(EchantScroll.攻擊力_力量_30);
                    scrolls.add(EchantScroll.攻擊力_力量_15);
                    scrolls.add(EchantScroll.魔力_智力_100);
                    scrolls.add(EchantScroll.魔力_智力_70);
                    scrolls.add(EchantScroll.魔力_智力_30);
                    scrolls.add(EchantScroll.魔力_智力_15);
                    scrolls.add(EchantScroll.攻擊力_敏捷_100);
                    scrolls.add(EchantScroll.攻擊力_敏捷_70);
                    scrolls.add(EchantScroll.攻擊力_敏捷_30);
                    scrolls.add(EchantScroll.攻擊力_敏捷_15);
                    scrolls.add(EchantScroll.攻擊力_幸運_100);
                    scrolls.add(EchantScroll.攻擊力_幸運_70);
                    scrolls.add(EchantScroll.攻擊力_幸運_30);
                    scrolls.add(EchantScroll.攻擊力_幸運_15);
                    scrolls.add(EchantScroll.攻擊力_體力_100);
                    scrolls.add(EchantScroll.攻擊力_體力_70);
                    scrolls.add(EchantScroll.攻擊力_體力_30);
                    scrolls.add(EchantScroll.攻擊力_體力_15);
                    break;
                }
                case 1: {
                    scrolls.add(EchantScroll.攻擊力_力量_100);
                    scrolls.add(EchantScroll.攻擊力_力量_70);
                    scrolls.add(EchantScroll.攻擊力_力量_30);
                    scrolls.add(EchantScroll.攻擊力_力量_15);
                    scrolls.add(EchantScroll.攻擊力_體力_100);
                    scrolls.add(EchantScroll.攻擊力_體力_70);
                    scrolls.add(EchantScroll.攻擊力_體力_30);
                    scrolls.add(EchantScroll.攻擊力_體力_15);
                    break;
                }
                case 2:
                    scrolls.add(EchantScroll.魔力_智力_100);
                    scrolls.add(EchantScroll.魔力_智力_70);
                    scrolls.add(EchantScroll.魔力_智力_30);
                    scrolls.add(EchantScroll.魔力_智力_15);
                    break;
                case 4:
                    scrolls.add(EchantScroll.攻擊力_敏捷_100);
                    scrolls.add(EchantScroll.攻擊力_敏捷_70);
                    scrolls.add(EchantScroll.攻擊力_敏捷_30);
                    scrolls.add(EchantScroll.攻擊力_敏捷_15);
                    scrolls.add(EchantScroll.攻擊力_體力_100);
                    scrolls.add(EchantScroll.攻擊力_體力_70);
                    scrolls.add(EchantScroll.攻擊力_體力_30);
                    scrolls.add(EchantScroll.攻擊力_體力_15);
                    break;
                case 8:
                    scrolls.add(EchantScroll.攻擊力_力量_100);
                    scrolls.add(EchantScroll.攻擊力_力量_70);
                    scrolls.add(EchantScroll.攻擊力_力量_30);
                    scrolls.add(EchantScroll.攻擊力_力量_15);
                    scrolls.add(EchantScroll.攻擊力_敏捷_100);
                    scrolls.add(EchantScroll.攻擊力_敏捷_70);
                    scrolls.add(EchantScroll.攻擊力_敏捷_30);
                    scrolls.add(EchantScroll.攻擊力_敏捷_15);
                    scrolls.add(EchantScroll.攻擊力_幸運_100);
                    scrolls.add(EchantScroll.攻擊力_幸運_70);
                    scrolls.add(EchantScroll.攻擊力_幸運_30);
                    scrolls.add(EchantScroll.攻擊力_幸運_15);
                    break;
                case 16:
                    scrolls.add(EchantScroll.攻擊力_力量_100);
                    scrolls.add(EchantScroll.攻擊力_力量_70);
                    scrolls.add(EchantScroll.攻擊力_力量_30);
                    scrolls.add(EchantScroll.攻擊力_力量_15);
                    scrolls.add(EchantScroll.攻擊力_敏捷_100);
                    scrolls.add(EchantScroll.攻擊力_敏捷_70);
                    scrolls.add(EchantScroll.攻擊力_敏捷_30);
                    scrolls.add(EchantScroll.攻擊力_敏捷_15);
                    scrolls.add(EchantScroll.攻擊力_體力_100);
                    scrolls.add(EchantScroll.攻擊力_體力_70);
                    scrolls.add(EchantScroll.攻擊力_體力_30);
                    scrolls.add(EchantScroll.攻擊力_體力_15);
                case 3:
                case 5:
                case 6:
                case 7:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
            }
        } else if (eq.getItemId() / 10000 == 108) {
            switch (reqJob) {
                case 0:
                    scrolls.add(EchantScroll.攻擊力_100);
                    scrolls.add(EchantScroll.攻擊力_70);
                    scrolls.add(EchantScroll.攻擊力_30);
                    scrolls.add(EchantScroll.魔力_100);
                    scrolls.add(EchantScroll.魔力_70);
                    scrolls.add(EchantScroll.魔力_30);
                    break;
                case 1:
                    scrolls.add(EchantScroll.攻擊力_100);
                    scrolls.add(EchantScroll.攻擊力_70);
                    scrolls.add(EchantScroll.攻擊力_30);
                    break;
                case 2:
                    scrolls.add(EchantScroll.魔力_100);
                    scrolls.add(EchantScroll.魔力_70);
                    scrolls.add(EchantScroll.魔力_30);
                    break;
                case 4:
                    scrolls.add(EchantScroll.攻擊力_100);
                    scrolls.add(EchantScroll.攻擊力_70);
                    scrolls.add(EchantScroll.攻擊力_30);
                    break;
                case 8:
                    scrolls.add(EchantScroll.攻擊力_100);
                    scrolls.add(EchantScroll.攻擊力_70);
                    scrolls.add(EchantScroll.攻擊力_30);
                    break;
                case 16:
                    scrolls.add(EchantScroll.攻擊力_100);
                    scrolls.add(EchantScroll.攻擊力_70);
                    scrolls.add(EchantScroll.攻擊力_30);
                case 3:
                case 5:
                case 6:
                case 7:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
            }
        } else {
            switch (reqJob) {
                case 0:
                    scrolls.add(EchantScroll.力量_100);
                    scrolls.add(EchantScroll.力量_70);
                    scrolls.add(EchantScroll.力量_30);
                    scrolls.add(EchantScroll.智力_100);
                    scrolls.add(EchantScroll.智力_70);
                    scrolls.add(EchantScroll.智力_30);
                    scrolls.add(EchantScroll.敏捷_100);
                    scrolls.add(EchantScroll.敏捷_70);
                    scrolls.add(EchantScroll.敏捷_30);
                    scrolls.add(EchantScroll.幸運_100);
                    scrolls.add(EchantScroll.幸運_70);
                    scrolls.add(EchantScroll.幸運_30);
                    scrolls.add(EchantScroll.體力_100);
                    scrolls.add(EchantScroll.體力_70);
                    scrolls.add(EchantScroll.體力_30);
                    break;
                case 1:
                    scrolls.add(EchantScroll.力量_100);
                    scrolls.add(EchantScroll.力量_70);
                    scrolls.add(EchantScroll.力量_30);
                    scrolls.add(EchantScroll.體力_100);
                    scrolls.add(EchantScroll.體力_70);
                    scrolls.add(EchantScroll.體力_30);
                    break;
                case 2:
                    scrolls.add(EchantScroll.智力_100);
                    scrolls.add(EchantScroll.智力_70);
                    scrolls.add(EchantScroll.智力_30);
                    scrolls.add(EchantScroll.體力_100);
                    scrolls.add(EchantScroll.體力_70);
                    scrolls.add(EchantScroll.體力_30);
                    break;
                case 4:
                    scrolls.add(EchantScroll.敏捷_100);
                    scrolls.add(EchantScroll.敏捷_70);
                    scrolls.add(EchantScroll.敏捷_30);
                    scrolls.add(EchantScroll.體力_100);
                    scrolls.add(EchantScroll.體力_70);
                    scrolls.add(EchantScroll.體力_30);
                    break;
                case 8:
                    scrolls.add(EchantScroll.力量_100);
                    scrolls.add(EchantScroll.力量_70);
                    scrolls.add(EchantScroll.力量_30);
                    scrolls.add(EchantScroll.敏捷_100);
                    scrolls.add(EchantScroll.敏捷_70);
                    scrolls.add(EchantScroll.敏捷_30);
                    scrolls.add(EchantScroll.幸運_100);
                    scrolls.add(EchantScroll.幸運_70);
                    scrolls.add(EchantScroll.幸運_30);
                    break;
                case 16:
                    scrolls.add(EchantScroll.力量_100);
                    scrolls.add(EchantScroll.力量_70);
                    scrolls.add(EchantScroll.力量_30);
                    scrolls.add(EchantScroll.敏捷_100);
                    scrolls.add(EchantScroll.敏捷_70);
                    scrolls.add(EchantScroll.敏捷_30);
                    scrolls.add(EchantScroll.體力_100);
                    scrolls.add(EchantScroll.體力_70);
                    scrolls.add(EchantScroll.體力_30);
                case 3:
                case 5:
                case 6:
                case 7:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
            }
        }
        return scrolls;
    }

    public static long getStarForceMeso(int equiplevel, int enhanced) {
        final long[] sfMeso100lv = {41000, 81000, 121000, 161000, 201000, 241000, 281000, 321000};
        final long[] sfMeso110lv = {54200, 107500, 160700, 214000, 267200, 320400, 373700, 426900, 480200, 533400};
        final long[] sfMeso120lv = {70100, 139200, 208400, 277500, 346600, 415700, 484800, 554000, 623100, 692200, 5602100, 7085400, 8794500, 10742400, 12941800};
        final long[] sfMeso130lv = {88900, 176800, 264600, 352500, 440400, 528300, 616200, 704000, 791900, 879800, 7122300, 9008200, 11181100, 13657700, 16454100, 19586000, 23069100, 26918600, 31149300, 35776100};
        final long[] sfMeso140lv = {110800, 220500, 330300, 440000, 549800, 659600, 769300, 879100, 988800, 1098600, 8895400, 11250800, 13964700, 17057900, 20550500, 24462200, 28812500, 33620400, 38904500, 44683300, 50974700, 57796700, 65166700, 73102200, 81620200};
        final long[] sfMeso150lv = {136000, 271000, 406000, 541000, 676000, 811000, 946000, 1081000, 1216000, 1351000, 10940700, 13837700, 17175800, 20980200, 25275900, 30087200, 35437900, 41351400, 47850600, 54985200, 62696400, 71087200, 80152000, 89912300, 100389000};
        if (equiplevel >= 0 && equiplevel <= 109) {
            return sfMeso100lv[enhanced];
        } else if (equiplevel >= 110 && equiplevel <= 119) {
            return sfMeso110lv[enhanced];
        } else if (equiplevel >= 120 && equiplevel <= 129) {
            return sfMeso120lv[enhanced];
        } else if (equiplevel >= 130 && equiplevel <= 139) {
            return sfMeso130lv[enhanced];
        } else if (equiplevel >= 140 && equiplevel <= 149) {
            return sfMeso140lv[enhanced];
        } else {
            return sfMeso150lv[enhanced];
        }
    }

    public static Map<EchantEquipStat, Integer> getStarForceStatus(Item equip) {
        final Map<EchantEquipStat, Integer> stats = new HashMap<>();
        Equip nEquip = (Equip) equip;
        int enhance = nEquip.getEnhance();
        if (enhance >= 15) {
            switch (enhance) {
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                    stats.put(EchantEquipStat.WATK, enhance - 6);
                    stats.put(EchantEquipStat.MATK, enhance - 6);
                    break;
                case 20:
                    stats.put(EchantEquipStat.WATK, 15);
                    stats.put(EchantEquipStat.MATK, 15);
                    break;
                case 21:
                    stats.put(EchantEquipStat.WATK, 17);
                    stats.put(EchantEquipStat.MATK, 17);
                    break;
                case 22:
                    stats.put(EchantEquipStat.WATK, 19);
                    stats.put(EchantEquipStat.MATK, 19);
                    break;
                case 23:
                    stats.put(EchantEquipStat.WATK, 21);
                    stats.put(EchantEquipStat.MATK, 21);
                    break;
                case 24:
                    stats.put(EchantEquipStat.WATK, 23);
                    stats.put(EchantEquipStat.MATK, 23);
                    break;
            }
            stats.put(EchantEquipStat.STR, 11);
            stats.put(EchantEquipStat.DEX, 11);
            stats.put(EchantEquipStat.INT, 11);
            stats.put(EchantEquipStat.LUK, 11);
        } else if (MapleItemInformationProvider.getInstance().isSuperiorEquip(nEquip.getItemId())) {
            switch (enhance) {
                case 0:
                    if (nEquip.getStr() > 0) {
                        stats.put(EchantEquipStat.STR, 19);
                    }
                    if (nEquip.getDex() > 0) {
                        stats.put(EchantEquipStat.DEX, 19);
                    }
                    if (nEquip.getInt() > 0) {
                        stats.put(EchantEquipStat.INT, 19);
                    }
                    if (nEquip.getLuk() > 0) {
                        stats.put(EchantEquipStat.LUK, 19);
                    }
                    break;
                case 1:
                    if (nEquip.getStr() > 0) {
                        stats.put(EchantEquipStat.STR, 20);
                    }
                    if (nEquip.getDex() > 0) {
                        stats.put(EchantEquipStat.DEX, 20);
                    }
                    if (nEquip.getInt() > 0) {
                        stats.put(EchantEquipStat.INT, 20);
                    }
                    if (nEquip.getLuk() > 0) {
                        stats.put(EchantEquipStat.LUK, 20);
                    }
                    break;
                case 2:
                    if (nEquip.getStr() > 0) {
                        stats.put(EchantEquipStat.STR, 22);
                    }
                    if (nEquip.getDex() > 0) {
                        stats.put(EchantEquipStat.DEX, 22);
                    }
                    if (nEquip.getInt() > 0) {
                        stats.put(EchantEquipStat.INT, 22);
                    }
                    if (nEquip.getLuk() > 0) {
                        stats.put(EchantEquipStat.LUK, 22);
                    }
                    break;
                case 3:
                    if (nEquip.getStr() > 0) {
                        stats.put(EchantEquipStat.STR, 25);
                    }
                    if (nEquip.getDex() > 0) {
                        stats.put(EchantEquipStat.DEX, 25);
                    }
                    if (nEquip.getInt() > 0) {
                        stats.put(EchantEquipStat.INT, 25);
                    }
                    if (nEquip.getLuk() > 0) {
                        stats.put(EchantEquipStat.LUK, 25);
                    }
                    break;
                case 4:
                    if (nEquip.getStr() > 0) {
                        stats.put(EchantEquipStat.STR, 29);
                    }
                    if (nEquip.getDex() > 0) {
                        stats.put(EchantEquipStat.DEX, 29);
                    }
                    if (nEquip.getInt() > 0) {
                        stats.put(EchantEquipStat.INT, 29);
                    }
                    if (nEquip.getLuk() > 0) {
                        stats.put(EchantEquipStat.LUK, 29);
                    }
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                    if (nEquip.getWatk() > 0) {
                        stats.put(EchantEquipStat.WATK, enhance + 4);
                    }
                    if (nEquip.getMatk() > 0) {
                        stats.put(EchantEquipStat.MATK, enhance + 4);
                    }
                    break;
                case 10:
                    if (nEquip.getWatk() > 0) {
                        stats.put(EchantEquipStat.WATK, 15);
                    }
                    if (nEquip.getMatk() > 0) {
                        stats.put(EchantEquipStat.MATK, 15);
                    }
                    break;
                case 11:
                    if (nEquip.getWatk() > 0) {
                        stats.put(EchantEquipStat.WATK, 17);
                    }
                    if (nEquip.getMatk() > 0) {
                        stats.put(EchantEquipStat.MATK, 17);
                    }
                    break;
                case 12:
                    if (nEquip.getWatk() > 0) {
                        stats.put(EchantEquipStat.WATK, 19);
                    }
                    if (nEquip.getMatk() > 0) {
                        stats.put(EchantEquipStat.MATK, 19);
                    }
                    break;
                case 13:
                    if (nEquip.getWatk() > 0) {
                        stats.put(EchantEquipStat.WATK, 21);
                    }
                    if (nEquip.getMatk() > 0) {
                        stats.put(EchantEquipStat.MATK, 21);
                    }
                    break;
                case 14:
                    if (nEquip.getWatk() > 0) {
                        stats.put(EchantEquipStat.WATK, 23);
                    }
                    if (nEquip.getMatk() > 0) {
                        stats.put(EchantEquipStat.MATK, 23);
                    }
                    break;
            }
        } else {
            int value;
            if (enhance >= 0 && enhance < 5) {
                value = 2;
            } else {
                value = 3;
            }
            if (nEquip.getStr() > 0) {
                stats.put(EchantEquipStat.STR, value);
            }
            if (nEquip.getDex() > 0) {
                stats.put(EchantEquipStat.DEX, value);
            }
            if (nEquip.getInt() > 0) {
                stats.put(EchantEquipStat.INT, value);
            }
            if (nEquip.getLuk() > 0) {
                stats.put(EchantEquipStat.LUK, value);
            }
            if (!ItemConstants.類型.武器(nEquip.getItemId()) && !ItemConstants.類型.雙刀(nEquip.getItemId())) {
                return stats;
            }

            value = nEquip.getWatk() > nEquip.getMatk() ? nEquip.getWatk() : nEquip.getMatk();
            if (value > 0 && value < 50) {
                value = 1;
            } else if (value >= 50 && value < 100) {
                value = 2;
            } else if (value >= 100 && value < 150) {
                value = 3;
            } else if (value >= 150 && value < 200) {
                value = 4;
            } else if (value >= 200 && value < 250) {
                value = 5;
            } else if (value >= 250 && value < 300) {
                value = 6;
            } else if (value >= 300 && value < 350) {
                value = 7;
            } else if (value >= 350 && value < 400) {
                value = 8;
            } else if (value >= 400) {
                value = 9;
            } else {
                value = 0;
            }
            if (nEquip.getWatk() > 0) {
                stats.put(EchantEquipStat.WATK, value);
            }
            if (nEquip.getMatk() > 0) {
                stats.put(EchantEquipStat.MATK, value);
            }
        }
        return stats;
    }

    public static void StarForceEnhanceItem(Item equip, boolean fall) {
        Map<EchantEquipStat, Integer> stats = getStarForceStatus(equip);
        Equip nEquip = (Equip) equip;
        if (stats.containsKey(EchantEquipStat.WATK)) {
            nEquip.setWatk((short) (nEquip.getWatk() + (fall ? -stats.get(EchantEquipStat.WATK) : stats.get(EchantEquipStat.WATK))));
        }
        if (stats.containsKey(EchantEquipStat.MATK)) {
            nEquip.setMatk((short) (nEquip.getMatk() + (fall ? -stats.get(EchantEquipStat.MATK) : stats.get(EchantEquipStat.MATK))));
        }
        if (stats.containsKey(EchantEquipStat.STR)) {
            nEquip.setStr((short) (nEquip.getStr() + (fall ? -stats.get(EchantEquipStat.STR) : stats.get(EchantEquipStat.STR))));
        }
        if (stats.containsKey(EchantEquipStat.DEX)) {
            nEquip.setDex((short) (nEquip.getDex() + (fall ? -stats.get(EchantEquipStat.DEX) : stats.get(EchantEquipStat.DEX))));
        }
        if (stats.containsKey(EchantEquipStat.INT)) {
            nEquip.setInt((short) (nEquip.getInt() + (fall ? -stats.get(EchantEquipStat.INT) : stats.get(EchantEquipStat.INT))));
        }
        if (stats.containsKey(EchantEquipStat.LUK)) {
            nEquip.setLuk((short) (nEquip.getLuk() + (fall ? -stats.get(EchantEquipStat.LUK) : stats.get(EchantEquipStat.LUK))));
        }
        if (stats.containsKey(EchantEquipStat.WDEF)) {
            nEquip.setWdef((short) (nEquip.getWdef() + (fall ? -stats.get(EchantEquipStat.WDEF) : stats.get(EchantEquipStat.WDEF))));
        }
        if (stats.containsKey(EchantEquipStat.MDEF)) {
            nEquip.setMdef((short) (nEquip.getMdef() + (fall ? -stats.get(EchantEquipStat.MDEF) : stats.get(EchantEquipStat.MDEF))));
        }
        if (stats.containsKey(EchantEquipStat.MHP)) {
            nEquip.setHp((short) (nEquip.getHp() + (fall ? -stats.get(EchantEquipStat.MHP) : stats.get(EchantEquipStat.MHP))));
        }
        if (stats.containsKey(EchantEquipStat.MMP)) {
            nEquip.setHp((short) (nEquip.getHp() + (fall ? -stats.get(EchantEquipStat.MMP) : stats.get(EchantEquipStat.MMP))));
        }
        if (stats.containsKey(EchantEquipStat.ACC)) {
            nEquip.setAcc((short) (nEquip.getAcc() + (fall ? -stats.get(EchantEquipStat.ACC) : stats.get(EchantEquipStat.ACC))));
        }
        if (stats.containsKey(EchantEquipStat.AVOID)) {
            nEquip.setAvoid((short) (nEquip.getAvoid() + (fall ? -stats.get(EchantEquipStat.AVOID) : stats.get(EchantEquipStat.AVOID))));
        }
        if (stats.containsKey(EchantEquipStat.JUMP)) {
            nEquip.setJump((short) (nEquip.getJump() + (fall ? -stats.get(EchantEquipStat.JUMP) : stats.get(EchantEquipStat.JUMP))));
        }
        if (stats.containsKey(EchantEquipStat.SPEED)) {
            nEquip.setSpeed((short) (nEquip.getSpeed() + (fall ? -stats.get(EchantEquipStat.SPEED) : stats.get(EchantEquipStat.SPEED))));
        }
        if (fall) {
            nEquip.setEnhance((byte) (Math.max(0, nEquip.getEnhance() - 1)));
        } else {
            nEquip.setEnhance((byte) (nEquip.getEnhance() + 1));
        }
        nEquip.setStarForce((byte) 17);
    }
}
