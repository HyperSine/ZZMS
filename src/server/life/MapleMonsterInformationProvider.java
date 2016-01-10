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
package server.life;

import client.inventory.MapleInventoryType;
import constants.GameConstants;
import database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.MapleItemInformationProvider;
import server.StructFamiliar;
import tools.Pair;

public class MapleMonsterInformationProvider {

    private static final MapleMonsterInformationProvider instance = new MapleMonsterInformationProvider();
    private final Map<Integer, String> mobCache = new TreeMap<>((v1, v2) -> v1.compareTo(v2));
    private final Map<Integer, ArrayList<MonsterDropEntry>> drops = new HashMap<>();
    private final List<MonsterGlobalDropEntry> globaldrops = new ArrayList<>();
    private static final MapleDataProvider stringDataWZ = MapleDataProviderFactory.getDataProvider("String.wz");
    private static final MapleData mobStringData = stringDataWZ.getData("MonsterBook.img");

    public static MapleMonsterInformationProvider getInstance() {
        return instance;
    }

    public List<MonsterGlobalDropEntry> getGlobalDrop() {
        return globaldrops;
    }

    public void load() {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            final Connection con = DatabaseConnection.getConnection();
            ps = con.prepareStatement("SELECT * FROM drop_data_global WHERE chance > 0");
            rs = ps.executeQuery();

            while (rs.next()) {
                globaldrops.add(
                        new MonsterGlobalDropEntry(
                                rs.getInt("itemid"),
                                rs.getInt("chance"),
                                rs.getInt("continent"),
                                rs.getByte("dropType"),
                                rs.getInt("minimum_quantity"),
                                rs.getInt("maximum_quantity"),
                                rs.getInt("questid"),
                                "數據庫"));
            }
            rs.close();
            ps.close();

            ps = con.prepareStatement("SELECT dropperid FROM drop_data");
            List<Integer> mobIds = new ArrayList<>();
            rs = ps.executeQuery();
            while (rs.next()) {
                if (!mobIds.contains(rs.getInt("dropperid"))) {
                    loadDrop(rs.getInt("dropperid"));
                    mobIds.add(rs.getInt("dropperid"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving drop" + e);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ignore) {
            }
        }
        loadCustom();
    }

    public void loadCustom() {//讀取自定義全域掉寶
//        globaldrops.add(new MonsterGlobalDropEntry(4001126, 5 * 10000, -1, (byte) 0, 1, 3, 0, "源碼自定義")); //楓葉
//        globaldrops.add(new MonsterGlobalDropEntry(4310050, (int) (1 * 10000), -1, (byte) 0, 1, 1, 0, "源碼自定義")); //Old Maple Coin
//        globaldrops.add(new MonsterGlobalDropEntry(2290285, (int) (0.5 * 10000), -1, (byte) 0, 1, 1, 0, "源碼自定義")); //[百科書]神秘百科書
//        globaldrops.add(new MonsterGlobalDropEntry(4000524, 5 * 10000, -1, (byte) 0, 1, 1, 0, "源碼自定義"));
    }

    public ArrayList<MonsterDropEntry> retrieveDrop(final int monsterId) {
        if (drops.containsKey(monsterId)) {
            return drops.get(monsterId);
        } else {
            return new ArrayList<>();
        }
    }

    private void loadDrop(final int monsterId) {
        final ArrayList<MonsterDropEntry> ret = new ArrayList<>();

        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            final MapleMonsterStats mons = MapleLifeFactory.getMonsterStats(monsterId);
            if (mons == null) {
                return;
            }
            ps = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM drop_data WHERE dropperid = ?");
            ps.setInt(1, monsterId);
            rs = ps.executeQuery();
            int itemid;
            int chance;
            boolean doneMesos = false;
            while (rs.next()) {
                itemid = rs.getInt("itemid");
                chance = rs.getInt("chance");
                if (itemid / 10000 == 238) {
                    continue;
                }
                ret.add(new MonsterDropEntry(
                        itemid,
                        chance,
                        rs.getInt("minimum_quantity"),
                        rs.getInt("maximum_quantity"),
                        rs.getInt("questid"),
                        "數據庫"));
                if (itemid == 0) {
                    doneMesos = true;
                }
            }
            if (!doneMesos) {
                addMeso(mons, ret);
            }

        } catch (SQLException e) {
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                return;
            }
        }
        drops.put(monsterId, ret);
    }

    public void addExtra() {
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        for (Entry<Integer, ArrayList<MonsterDropEntry>> e : drops.entrySet()) {
            for (int i = 0; i < e.getValue().size(); i++) {//處理已有掉寶數據
                if (e.getValue().get(i).itemId != 0 && !ii.itemExists(e.getValue().get(i).itemId)) {
                    e.getValue().remove(i);
                }
            }
            final MapleMonsterStats mons = MapleLifeFactory.getMonsterStats(e.getKey());
            Integer item = ii.getItemIdByMob(e.getKey());
            if (item != null && item > 0) {
                if (item / 10000 == 238) {//過濾怪物卡掉寶數據
                    continue;
                }
                e.getValue().add(new MonsterDropEntry(item, mons.isBoss() ? 1000000 : 10000, 1, 1, 0, "道具數據庫"));
            }
            StructFamiliar f = ii.getFamiliarByMob(e.getKey().intValue());
            if (f != null) {
                if (f.itemid / 10000 == 238) {//過濾怪物卡掉寶數據
                    continue;
                }
                e.getValue().add(new MonsterDropEntry(f.itemid, mons.isBoss() ? 10000 : 100, 1, 1, 0, "FamiliarInfo"));
            }
        }
        for (Entry<Integer, Integer> i : ii.getMonsterBook().entrySet()) {
            if (!drops.containsKey(i.getKey())) {
                final MapleMonsterStats mons = MapleLifeFactory.getMonsterStats(i.getKey());
                ArrayList<MonsterDropEntry> e = new ArrayList<>();
                if ((i.getValue()) / 10000 == 238) {//過濾怪物卡掉寶數據
                    continue;
                }
                e.add(new MonsterDropEntry(i.getValue(), mons.isBoss() ? 1000000 : 10000, 1, 1, 0, "道具數據庫2"));
                StructFamiliar f = ii.getFamiliarByMob(i.getKey().intValue());
                if (f != null) {
                    if (f.itemid / 10000 == 238) {//過濾怪物卡掉寶數據
                        continue;
                    }
                    e.add(new MonsterDropEntry(f.itemid, mons.isBoss() ? 10000 : 100, 1, 1, 0, "FamiliarInfo2"));
                }
                addMeso(mons, e);
                drops.put(i.getKey(), e);
            }
        }
        for (StructFamiliar f : ii.getFamiliars().values()) {
            if (!drops.containsKey(f.mob)) {
                MapleMonsterStats mons = MapleLifeFactory.getMonsterStats(f.mob);
                ArrayList<MonsterDropEntry> e = new ArrayList<>();
                if (f.itemid / 10000 == 238) {//過濾怪物卡掉寶數據
                    continue;
                }
                e.add(new MonsterDropEntry(f.itemid, mons.isBoss() ? 10000 : 100, 1, 1, 0, "FamiliarInfo3"));
                addMeso(mons, e);
                drops.put(f.mob, e);
            }
        }
        for (Entry<Integer, ArrayList<MonsterDropEntry>> e : drops.entrySet()) {//怪物書掉寶數據
            if (e.getKey() != 9400408 && mobStringData.getChildByPath(String.valueOf(e.getKey())) != null) {
                for (MapleData d : mobStringData.getChildByPath(e.getKey() + "/reward")) {
                    final int toAdd = MapleDataTool.getInt(d, 0);
                    if (toAdd > 0 && !contains(e.getValue(), toAdd) && ii.itemExists(toAdd)) {
                        if (toAdd / 10000 != 238 && toAdd / 10000 != 243 && toAdd / 10000 != 399 && toAdd != 4001126 && toAdd != 4001128 && toAdd != 4001246 && toAdd != 4001473 && toAdd != 2022450 && toAdd != 2022451 && toAdd != 2022452 && toAdd != 4032302 && toAdd != 4032303 && toAdd != 4032304) {
                            e.getValue().add(new MonsterDropEntry(toAdd, chanceLogic(toAdd), 1, 1, 0, "怪物書"));
                        }
                    }
                }
            }
        }
    }

    public void addMeso(MapleMonsterStats mons, ArrayList<MonsterDropEntry> ret) {
        final double divided = (mons.getLevel() < 100 ? (mons.getLevel() < 10 ? (double) mons.getLevel() : 10.0) : (mons.getLevel() / 10.0));
        final int max = mons.isBoss() && !mons.isPartyBonus() ? (mons.getLevel() * mons.getLevel()) : (mons.getLevel() * (int) Math.ceil(mons.getLevel() / divided));
        for (int i = 0; i < mons.dropsMeso(); i++) {
            ret.add(new MonsterDropEntry(0, mons.isBoss() && !mons.isPartyBonus() ? 1000000 : (mons.isPartyBonus() ? 100000 : 200000), (int) Math.floor(0.66 * max), max, 0, "楓幣添加"));
        }
    }

    public void clearDrops() {
        drops.clear();
        globaldrops.clear();
        load();
        addExtra();
    }

    public boolean contains(ArrayList<MonsterDropEntry> e, int toAdd) {
        for (MonsterDropEntry f : e) {
            if (f.itemId == toAdd) {
                return true;
            }
        }
        return false;
    }

    public String getDrops(String item) {
        List<Integer> dropsfound = new LinkedList<>();
        for (Pair<Integer, String> a : MapleItemInformationProvider.getInstance().getAllItems2()) {
            if (a.getRight().toLowerCase().contains(item.toLowerCase())) {
                int itemId = a.getLeft();
                for (int mobId : getAllMonsters().keySet()) {
                    for (MonsterDropEntry c : retrieveDrop(mobId)) {
                        if (c.itemId == itemId) {
                            if (!dropsfound.contains(mobId)) {
                                dropsfound.add(mobId);
                            }
                        }
                    }
                }
            }
        }
        String droplist = "";
        for (int d : dropsfound) {
            droplist += "#o" + d + "#(" + d + ")\r\n";
        }
        return droplist;
    }

    public String getDrops(int item) {
        List<Integer> dropsfound = new LinkedList<>();
        for (int mobId : getAllMonsters().keySet()) {
            for (MonsterDropEntry b : retrieveDrop(mobId)) {
                if (b.itemId == item) {
                    if (!dropsfound.contains(mobId)) {
                        dropsfound.add(mobId);
                    }
                }
            }
        }
        String droplist = "";
        for (int c : dropsfound) {
            droplist += "#o" + c + "#(" + c + ")\r\n";
        }
        return droplist;
    }

    public Map<Integer, String> getAllMonsters() {
        if (mobCache.isEmpty()) {
            final MapleDataProvider stringData = MapleDataProviderFactory.getDataProvider("String.wz");
            MapleData mobsData = stringData.getData("Mob.img");
            for (MapleData itemFolder : mobsData.getChildren()) {
                mobCache.put(Integer.parseInt(itemFolder.getName()), MapleDataTool.getString("name", itemFolder, "NO-NAME"));
            }
        }
        return mobCache;
    }

    public int chanceLogic(int itemId) { //not much logic in here. most of the drops should already be there anyway.
        if (GameConstants.getInventoryType(itemId) == MapleInventoryType.EQUIP) {
            return 50000; //with *10
        } else if (GameConstants.getInventoryType(itemId) == MapleInventoryType.SETUP || GameConstants.getInventoryType(itemId) == MapleInventoryType.CASH) {
            return 500;
        } else {
            switch (itemId / 10000) {
                case 204:
                case 207:
                case 233:
                case 229:
                    return 500;
                case 401:
                case 402:
                    return 5000;
                case 403:
                    return 5000; //lol
            }
            return 20000;
        }
    }
    //MESO DROP: level * (level / 10) = max, min = 0.66 * max
    //explosive Reward = 7 meso drops
    //boss, ffaloot = 2 meso drops
    //boss = level * level = max
    //no mesos if: mobid / 100000 == 97 or 95 or 93 or 91 or 90 or removeAfter > 0 or invincible or onlyNormalAttack or friendly or dropitemperiod > 0 or cp > 0 or point > 0 or fixeddamage > 0 or selfd > 0 or mobType != null and mobType.charat(0) == 7 or PDRate <= 0
}
