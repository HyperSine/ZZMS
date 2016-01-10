package server.shops;

import client.MapleClient;
import client.SkillFactory;
import client.inventory.Item;
import client.inventory.MapleInventoryIdentifier;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import constants.GameConstants;
import constants.ItemConstants;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import tools.FileoutputUtil;
import tools.Pair;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class MapleShop {

    private static final Set<Integer> rechargeableItems = new LinkedHashSet();
    private final int id;
    private final int npcId;
    private final List<MapleShopItem> items = new LinkedList();
    private final List<Pair<Integer, String>> ranks = new ArrayList();

    static {
        //鏢
        rechargeableItems.add(2070000);//海星鏢
        rechargeableItems.add(2070001);//迴旋鏢
        rechargeableItems.add(2070002);//黑色利刃
        rechargeableItems.add(2070003);//雪花鏢
        rechargeableItems.add(2070004);//梅之鏢
        rechargeableItems.add(2070005);//雷之鏢
        rechargeableItems.add(2070006);//日之鏢
        rechargeableItems.add(2070007);//月牙鏢
        rechargeableItems.add(2070008);//雪球
        rechargeableItems.add(2070009);//木製陀螺
        rechargeableItems.add(2070010);//冰柱
        rechargeableItems.add(2070011);//楓葉飛鏢
        rechargeableItems.add(2070012);//紙飛機
        rechargeableItems.add(2070013);//橘子
        rechargeableItems.add(2070015);//新手盜賊的飛鏢
        rechargeableItems.add(2070019);//手裡劍-魔
        rechargeableItems.add(2070020);//鞭炮
        rechargeableItems.add(2070021);//手裡劍-魔
        rechargeableItems.add(2070022);//閃亮的紙條
        rechargeableItems.add(2070023);//火牢術飛鏢
        rechargeableItems.add(2070024);//無限的增加鏢
        rechargeableItems.add(2070026);//白金飛鏢
        //弹
        rechargeableItems.add(2330000);//彈丸
        rechargeableItems.add(2330001);//子彈
        rechargeableItems.add(2330002);//強力子彈
        rechargeableItems.add(2330003);//高等子彈
        rechargeableItems.add(2330004);//高爆彈
        rechargeableItems.add(2330005);//穿甲彈
        rechargeableItems.add(2330006);//新手海盜的子彈
        rechargeableItems.add(2330007);//貫穿裝甲的特製子彈
        rechargeableItems.add(2330008);//巨人彈丸
        rechargeableItems.add(2330016);//白金彈丸
        rechargeableItems.add(2331000);//火炎膠囊
        rechargeableItems.add(2332000);//寒冰膠囊

    }

    public MapleShop(int id, int npcId) {
        this.id = id;
        this.npcId = npcId;
    }

    public void addItem(MapleShopItem item) {
        this.items.add(item);
    }

    public List<MapleShopItem> getItems() {
        return this.items;
    }

    public void sendShop(MapleClient c) {
        c.getPlayer().setShop(this);
        c.getSession().write(CField.NPCPacket.getNPCShop(getNpcId(), this, c));
    }

    public void sendShop(MapleClient c, int customNpc) {
        c.getPlayer().setShop(this);
        c.getSession().write(CField.NPCPacket.getNPCShop(customNpc, this, c));
    }

    public void buy(MapleClient c, short slot, int itemId, short quantity) {
        if ((itemId / 10000 == 190) && (!GameConstants.isMountItemAvailable(itemId, c.getPlayer().getJob()))) {
            c.getPlayer().dropMessage(1, "無法購買此道具.");
            c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
            return;
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        int x = 0;
        int index = -1;
        for (Item i : c.getPlayer().getRebuy()) {
            if (i.getItemId() == itemId) {
                index = x;
                break;
            }
            x++;
        }
        if (index >= 0) {
            Item i = c.getPlayer().getRebuy().get(index);
            int price = (int) Math.max(Math.ceil(ii.getPrice(itemId) * (ItemConstants.類型.可充值道具(itemId) ? 1 : i.getQuantity())), 0.0D);
            if ((price >= 0) && (c.getPlayer().getMeso() >= price)) {
                if (MapleInventoryManipulator.checkSpace(c, itemId, i.getQuantity(), i.getOwner())) {
                    c.getPlayer().gainMeso(-price, false);
                    MapleInventoryManipulator.addbyItem(c, i);
                    c.getPlayer().getRebuy().remove(index);
                    c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, x));
                } else {
                    c.getPlayer().dropMessage(1, "請確認道具欄有足夠的空間.");
                    c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
                }
            } else {
                c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
            }
            return;
        }

        MapleShopItem item = findBySlotAndId(itemId, slot);
        if (item == null) {
            c.getPlayer().dropMessage(1, "無法購買此道具.");
            c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
            return;
        }

        if (c.getPlayer().getLevel() < item.getMinLevel()) {
            c.getPlayer().dropMessage(1, item.getMinLevel() + "等級以上才可購入。");
            c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
            return;
        }

        int slotMax = MapleItemInformationProvider.getInstance().getSlotMax(item.getItemId());
        int shopItemQ = item.getQuantity() == 0 ? slotMax : item.getQuantity();
        int buyable = shopItemQ > 1 ? 1 : item.getBuyable() == 0 ? slotMax : item.getBuyable();
        if ((shopItemQ > 1 && quantity > 1) || (shopItemQ == 1 && quantity > buyable) || shopItemQ < 1) {
            c.getPlayer().dropMessage(1, "購買失敗.");
            c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
            return;
        }
        quantity = (short) (shopItemQ > 1 ? shopItemQ : quantity);

        if (item.getPrice() > 0 && item.getReqItem() == 0) {
            if (item.getRank() >= 0) {
                boolean passed = true;
                int y = 0;
                for (Pair i : getRanks()) {
                    if ((c.getPlayer().haveItem(((Integer) i.left), 1, true, true)) && (item.getRank() >= y)) {
                        passed = true;
                        break;
                    }
                    y++;
                }
                if (!passed) {
                    c.getPlayer().dropMessage(1, "你需要更高的級別才能購買此道具.");
                    c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
                    return;
                }
            }
            int price = ItemConstants.類型.可充值道具(itemId) || shopItemQ > 1 ? item.getPrice() : item.getPrice() * quantity;
            if (price >= 0 && c.getPlayer().getMeso() >= price) {
                if (MapleInventoryManipulator.checkSpace(c, itemId, quantity, "")) {
                    c.getPlayer().gainMeso(-price, false);
                    if (ItemConstants.類型.寵物(itemId)) {
                        MapleInventoryManipulator.addById(c, itemId, (short) quantity, "", MaplePet.createPet(itemId, MapleInventoryIdentifier.getInstance()), item.getExpiration() == 0 ? -1 : item.getExpiration(), MapleInventoryManipulator.MINUTE, "從商店ID:" + this.id + ", NPCID:" + this.npcId + " 處購買, 時間:" + FileoutputUtil.CurrentReadable_Date());
                    } else {
                        if (ItemConstants.類型.可充值道具(itemId)) {
                            quantity = ii.getSlotMax(item.getItemId());
                        }

                        MapleInventoryManipulator.addById(c, itemId, quantity, null, null, item.getExpiration(), MapleInventoryManipulator.MINUTE, "從商店ID:" + this.id + ", NPCID:" + this.npcId + " 處購買, 時間:" + FileoutputUtil.CurrentReadable_Date());
                    }
                } else {
                    c.getPlayer().dropMessage(1, "請確認道具欄有足夠的空間.");
                }
                c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
            }
        } else if (item.getReqItem() > 0 && quantity == 1 && c.getPlayer().haveItem(item.getReqItem(), item.getReqItemQ(), false, true)) {
            if (MapleInventoryManipulator.checkSpace(c, itemId, quantity, "")) {
                MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(item.getReqItem()), item.getReqItem(), item.getReqItemQ(), false, false);
                if (ItemConstants.類型.寵物(itemId)) {
                    MapleInventoryManipulator.addById(c, itemId, (short) quantity, "", MaplePet.createPet(itemId, MapleInventoryIdentifier.getInstance()), item.getExpiration() == 0 ? -1 : item.getExpiration(), MapleInventoryManipulator.MINUTE, "從商店ID:" + this.id + ", NPCID:" + this.npcId + " 處購買, 時間:" + FileoutputUtil.CurrentReadable_Date());
                } else {
                    if (ItemConstants.類型.可充值道具(itemId)) {
                        quantity = ii.getSlotMax(item.getItemId());
                    }
                    MapleInventoryManipulator.addById(c, itemId, quantity, null, null, item.getExpiration(), MapleInventoryManipulator.MINUTE, "從商店ID:" + this.id + ", NPCID:" + this.npcId + " 處購買, 時間:" + FileoutputUtil.CurrentReadable_Date());
                }
            } else {
                c.getPlayer().dropMessage(1, "請確認道具欄有足夠的空間.");
            }
            c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
        }
    }

    public void sell(MapleClient c, MapleInventoryType type, byte slot, short quantity) {
        if ((quantity == 65535) || (quantity == 0)) {
            quantity = 1;
        }
        Item item = c.getPlayer().getInventory(type).getItem(slot);
        if (item == null) {
            return;
        }

        if (ItemConstants.類型.可充值道具(item.getItemId())) {
            quantity = item.getQuantity();
        }

        short iQuant = item.getQuantity();
        if (iQuant == 65535) {
            iQuant = 1;
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if ((ii.cantSell(item.getItemId())) || (ItemConstants.類型.寵物(item.getItemId()))) {
            return;
        }

        List<Item> listRebuy = new ArrayList<>();
        if ((quantity <= iQuant) && (iQuant > 0)) {
            if (item.getQuantity() == quantity) {
                if (c.getPlayer().getRebuy().size() < 10) {
                    c.getPlayer().getRebuy().add(item.copy());
                } else if (c.getPlayer().getRebuy().size() == 10) {
                    for (int i = 1; i < 10; i++) {
                        listRebuy.add(c.getPlayer().getRebuy().get(i));
                    }
                    listRebuy.add(item.copy());
                    c.getPlayer().setRebuy(listRebuy);
                } else {
                    int x = c.getPlayer().getRebuy().size();
                    for (int i = x - 10; i < x; i++) {
                        listRebuy.add(c.getPlayer().getRebuy().get(i));
                    }
                    c.getPlayer().setRebuy(listRebuy);
                }
            } else {
                c.getPlayer().getRebuy().add(item.copyWithQuantity(quantity));
            }
            MapleInventoryManipulator.removeFromSlot(c, type, slot, quantity, false);
            double price;
            if (ItemConstants.類型.可充值道具(item.getItemId())) {
                price = ii.getWholePrice(item.getItemId()) / ii.getSlotMax(item.getItemId());
            } else {
                price = ii.getPrice(item.getItemId());
            }

            int recvMesos = (int) Math.max(Math.ceil(price * quantity), 0.0D);
            if ((price != -1.0D) && (recvMesos > 0)) {
                c.getPlayer().gainMeso(recvMesos, false);
            }
            c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 8, this, c, -1));
        }
    }

    public void recharge(MapleClient c, byte slot) {
        Item item = c.getPlayer().getInventory(MapleInventoryType.USE).getItem(slot);

        if ((item == null) || (!ItemConstants.類型.可充值道具(item.getItemId()))) {
            return;
        }
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        short slotMax = ii.getSlotMax(item.getItemId());
        int skill = GameConstants.getMasterySkill(c.getPlayer().getJob());

        if (skill != 0) {
            slotMax = (short) (slotMax + c.getPlayer().getTotalSkillLevel(SkillFactory.getSkill(skill)) * 10);
        }
        if (item.getQuantity() < slotMax) {
            int price = (int) Math.round(ii.getPrice(item.getItemId()) * (slotMax - item.getQuantity()));
            if (c.getPlayer().getMeso() >= price) {
                item.setQuantity(slotMax);
                c.getSession().write(CWvsContext.InventoryPacket.updateInventorySlot(MapleInventoryType.USE, item, false));
                c.getPlayer().gainMeso(-price, false, false);
                c.getSession().write(CField.NPCPacket.confirmShopTransaction((byte) 0, this, c, -1));
            }
        }
    }

    protected MapleShopItem findById(int itemId) {
        for (MapleShopItem item : this.items) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }
        return null;
    }

    protected MapleShopItem findBySlot(short slot) {
        for (MapleShopItem item : this.items) {
            if (item.getSlot() == slot) {
                return item;
            }
        }
        return null;
    }

    protected MapleShopItem findBySlotAndId(int itemId, int slot) {
        MapleShopItem shopItem = (MapleShopItem) items.get(slot);

        if ((shopItem != null) && (shopItem.getItemId() == itemId)) {
            return shopItem;
        }
        return null;
    }

    public static MapleShop createFromDB(int id, boolean isShopId) {
        MapleShop ret = null;

        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(isShopId ? "SELECT * FROM shops WHERE shopid = ?" : "SELECT * FROM shops WHERE npcid = ?");
            int shopId;
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                shopId = rs.getInt("shopid");
                ret = new MapleShop(shopId, rs.getInt("npcid"));
                rs.close();
                ps.close();
            } else {
                rs.close();
                ps.close();
                return null;
            }
            ps = con.prepareStatement("SELECT * FROM shopitems WHERE shopid = ? ORDER BY position ASC");
            ps.setInt(1, shopId);
            rs = ps.executeQuery();
            List<Integer> recharges = new ArrayList(rechargeableItems);
            while (rs.next()) {
                if (ii.itemExists(rs.getInt("itemid"))) {
                    if (ItemConstants.類型.可充值道具(rs.getInt("itemid"))) {
                        MapleShopItem starItem = new MapleShopItem(rs.getShort("buyable"), ii.getSlotMax(rs.getInt("itemid")), rs.getInt("itemid"), rs.getInt("price"), (short) rs.getInt("position"), rs.getInt("reqitem"), rs.getInt("reqitemq"), rs.getByte("rank"), rs.getInt("category"), rs.getInt("minLevel"), rs.getInt("expiration"), false);
                        ret.addItem(starItem);
                        if (rechargeableItems.contains(starItem.getItemId())) {
                            recharges.remove(Integer.valueOf(starItem.getItemId()));
                        }
                    } else {
                        ret.addItem(new MapleShopItem(rs.getShort("buyable"), rs.getShort("quantity"), rs.getInt("itemid"), rs.getInt("price"), (short) rs.getInt("position"), rs.getInt("reqitem"), rs.getInt("reqitemq"), rs.getByte("rank"), rs.getInt("category"), rs.getInt("minLevel"), rs.getInt("expiration"), false)); //todo potential
                    }
                }
            }
            for (Integer recharge : recharges) {
                ret.addItem(new MapleShopItem((short) 1, ii.getSlotMax(recharge), recharge, 0, (short) 0, 0, 0, (byte) 0, 0, 0, 0, false));
            }
            rs.close();
            ps.close();

            ps = con.prepareStatement("SELECT * FROM shopranks WHERE shopid = ? ORDER BY rank ASC");
            ps.setInt(1, shopId);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (ii.itemExists(rs.getInt("itemid"))) {
                    ret.ranks.add(new Pair(rs.getInt("itemid"), rs.getString("name")));
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.err.println("Could not load shop");
        }
        return ret;
    }

    public int getNpcId() {
        return this.npcId;
    }

    public int getId() {
        return this.id;
    }

    public List<Pair<Integer, String>> getRanks() {
        return this.ranks;
    }
}
