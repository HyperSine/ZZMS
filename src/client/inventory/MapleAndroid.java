package client.inventory;

import database.DatabaseConnection;
import java.awt.Point;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.StructAndroid;
import server.movement.LifeMovement;
import server.movement.LifeMovementFragment;
import server.movement.StaticLifeMovement;

public class MapleAndroid
        implements Serializable {

    private static final long serialVersionUID = 9179541993413738569L;
    private int stance = 0;
    private final int uniqueid;
    private final int itemid;
    private int skin;
    private int hair;
    private int face;
    private int gender;
    private int type;
    private String name;
    private Point pos = new Point(0, 0);
    private boolean changed = false;

    private MapleAndroid(int itemid, int uniqueid) {
        this.itemid = itemid;
        this.uniqueid = uniqueid;
    }

    public static final MapleAndroid loadFromDb(int itemid, int uid) {
        try {
            MapleAndroid ret = new MapleAndroid(itemid, uid);

            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("SELECT * FROM androids WHERE uniqueid = ?")) {
                ps.setInt(1, uid);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        rs.close();
                        ps.close();
                        return null;
                    }

                    int type = rs.getInt("type");
                    int gender = rs.getInt("gender");
                    boolean fix = false;
                    if (type < 1) {
                        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                        type = ii.getAndroidType(itemid);
                        StructAndroid aInfo = ii.getAndroidInfo(type);
                        if (aInfo == null) {
                            return null;
                        }
                        gender = aInfo.gender;
                        fix = true;
                    }
                    ret.setType(type);
                    ret.setGender(gender);
                    ret.setSkin(rs.getInt("skin"));
                    ret.setHair(rs.getInt("hair"));
                    ret.setFace(rs.getInt("face"));
                    ret.setName(rs.getString("name"));
                    ret.changed = false;
                }
            }

            return ret;
        } catch (SQLException ex) {
            System.err.println("加載機器人出錯:" + ex);
        }
        return null;
    }

    public final void saveToDb() {
        if (!this.changed) {
            return;
        }
        try {
            try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE androids SET type = ?, gender = ?, skin = ?, hair = ?, face = ?, name = ? WHERE uniqueid = ?")) {
                ps.setInt(1, this.type);
                ps.setInt(2, this.gender);
                ps.setInt(3, this.skin);
                ps.setInt(4, this.hair);
                ps.setInt(5, this.face);
                ps.setString(6, this.name);
                ps.setInt(7, this.uniqueid);
                ps.executeUpdate();
                ps.close();
            }
            this.changed = false;
        } catch (SQLException ex) {
            System.err.println("保存機器人出錯:" + ex);
        }
    }

    public static final MapleAndroid create(int itemid, int uniqueid) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        StructAndroid aInfo = ii.getAndroidInfo(ii.getAndroidType(itemid));
        if (aInfo == null) {
            return null;
        }
        return create(itemid, uniqueid, aInfo);
    }

    public static final MapleAndroid create(int itemid, int uniqueid, StructAndroid aInfo) {
        if (uniqueid <= -1) {
            uniqueid = MapleInventoryIdentifier.getInstance();
        }
        int type = aInfo.type;
        int gender = aInfo.gender;
        int skin = (aInfo.skin.get(Randomizer.nextInt(aInfo.skin.size())));
        int hair = (aInfo.hair.get(Randomizer.nextInt(aInfo.hair.size())));
        int face = (aInfo.face.get(Randomizer.nextInt(aInfo.face.size())));
        try {
            try (PreparedStatement pse = DatabaseConnection.getConnection().prepareStatement("INSERT INTO androids (uniqueid, type, gender, skin, hair, face, name) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                pse.setInt(1, uniqueid);
                pse.setInt(2, type);
                pse.setInt(3, gender);
                pse.setInt(4, skin);
                pse.setInt(5, hair);
                pse.setInt(6, face);
                pse.setString(7, "機器人");
                pse.executeUpdate();
            }
        } catch (SQLException ex) {
            return null;
        }
        MapleAndroid android = new MapleAndroid(itemid, uniqueid);
        android.setType(type);
        android.setGender(gender);
        android.setSkin(skin);
        android.setHair(hair);
        android.setFace(face);
        android.setName("機器人");

        return android;
    }
//

    public void delete() {

    }

    public int getUniqueId() {
        return this.uniqueid;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int t) {
        this.type = t;
        this.changed = true;
    }

    public int getGender() {
        return this.gender;
    }

    public void setGender(int g) {
        this.gender = g;
        this.changed = true;
    }

    public int getSkin() {
        return this.skin;
    }

    public void setSkin(int s) {
        this.skin = s;
        this.changed = true;
    }

    public final void setHair(int closeness) {
        this.hair = closeness;
        this.changed = true;
    }

    public final int getHair() {
        return this.hair;
    }

    public final void setFace(int closeness) {
        this.face = closeness;
        this.changed = true;
    }

    public final int getFace() {
        return this.face;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String n) {
        this.name = n;
        this.changed = true;
    }

    public final Point getPos() {
        return this.pos;
    }

    public final void setPos(Point pos) {
        this.pos = pos;
    }

    public final int getStance() {
        return this.stance;
    }

    public final void setStance(int stance) {
        this.stance = stance;
    }

    public final int getItemId() {
        return this.itemid;
    }

    public final void updatePosition(List<LifeMovementFragment> movement) {
        for (LifeMovementFragment move : movement) {
            if ((move instanceof LifeMovement)) {
                if ((move instanceof StaticLifeMovement)) {
                    setPos(((LifeMovement) move).getPosition());
                }
                setStance(((LifeMovement) move).getNewstate());
            }
        }
    }

    public static void clearAndroid() {
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM androids");
            ResultSet rs = ps.executeQuery();
            ArrayList<Integer> uids = new ArrayList();
            while (rs.next()) {
                int uid = rs.getInt("uniqueid");
                if (!ItemLoader.isExistsByUniqueid(uid)) {
                    System.err.println("機器人:" + rs.getString("name") + " uniqueid: " + uid + " 不存在, 清理。");
                    uids.add(uid);
                }
            }
            ps.close();
            rs.close();
            for (int id : uids) {
                ps = con.prepareStatement("DELETE FROM androids WHERE uniqueid = ?");
                ps.setInt(1, id);
                ps.executeUpdate();
            }
        } catch (SQLException se) {
            System.err.println("[MapleAndroid] 從數據庫中加載機器人訊息出錯");
        }
    }
}
