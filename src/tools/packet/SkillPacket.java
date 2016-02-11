/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools.packet;

import client.MapleCharacter;
import client.MonsterStatus;
import constants.ServerConfig;
import handling.SendPacketOpcode;
import java.awt.Point;
import java.util.List;
import server.Randomizer;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MechDoor;
import tools.DateUtil;
import tools.HexTool;
import tools.data.MaplePacketLittleEndianWriter;

/**
 *
 * @author Pungin
 */
public class SkillPacket {

    public static byte[] sendEngagementRequest(String name, int chrId) {
        if (ServerConfig.LOG_PACKETS) {
            System.out.println(new StringBuilder().append("調用位置: ").append(new java.lang.Throwable().getStackTrace()[0]).toString());
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ENGAGE_REQUEST.getValue());
        mplew.write(0);
        mplew.writeMapleAsciiString(name);
        mplew.writeInt(chrId);

        return mplew.getPacket();
    }

    public static byte[] sendEngagement(byte msg, int item, MapleCharacter male, MapleCharacter female) {
        if (ServerConfig.LOG_PACKETS) {
            System.out.println(new StringBuilder().append("調用位置: ").append(new java.lang.Throwable().getStackTrace()[0]).toString());
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ENGAGE_RESULT.getValue());
        mplew.write(msg);
        if (msg == 9 || msg == 17 || msg >= 11 && msg <= 14) {
            mplew.writeInt(0);
            mplew.writeInt(male.getId());
            mplew.writeInt(female.getId());
            mplew.writeShort(1);
            mplew.writeInt(item);
            mplew.writeInt(item);
            mplew.writeAsciiString(male.getName(), 13);
            mplew.writeAsciiString(female.getName(), 13);
        } else if (msg == 10 || msg >= 15 && msg <= 16) {
            mplew.writeAsciiString("Male", 13);
            mplew.writeAsciiString("Female", 13);
            mplew.writeShort(0);
        }

        return mplew.getPacket();
    }

    public static byte[] updateJaguar(MapleCharacter from) {
        if (ServerConfig.LOG_PACKETS) {
            System.out.println(new StringBuilder().append("調用位置: ").append(new java.lang.Throwable().getStackTrace()[0]).toString());
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_JAGUAR.getValue());
        PacketHelper.addJaguarInfo(mplew, from);

        return mplew.getPacket();
    }

    public static byte[] teslaTriangle(int chrId, int sum1, int sum2, int sum3) {
        if (ServerConfig.LOG_PACKETS) {
            System.out.println(new StringBuilder().append("調用位置: ").append(new java.lang.Throwable().getStackTrace()[0]).toString());
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TESLA_TRIANGLE.getValue());
        mplew.writeInt(chrId);
        mplew.writeInt(sum1);
        mplew.writeInt(sum2);
        mplew.writeInt(sum3);

        return mplew.getPacket();
    }

    public static byte[] mechPortal(Point pos) {
        if (ServerConfig.LOG_PACKETS) {
            System.out.println(new StringBuilder().append("調用位置: ").append(new java.lang.Throwable().getStackTrace()[0]).toString());
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MECH_PORTAL.getValue());
        mplew.writePos(pos);

        return mplew.getPacket();
    }

    public static byte[] spawnMechDoor(MechDoor md, boolean animated) {
        if (ServerConfig.LOG_PACKETS) {
            System.out.println(new StringBuilder().append("調用位置: ").append(new java.lang.Throwable().getStackTrace()[0]).toString());
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MECH_DOOR_SPAWN.getValue());
        mplew.write(animated ? 0 : 1);
        mplew.writeInt(md.getOwnerId());
        mplew.writePos(md.getTruePosition());
        mplew.write(md.getId());
        mplew.writeInt(md.getPartyId());

        return mplew.getPacket();
    }

    public static byte[] removeMechDoor(MechDoor md, boolean animated) {
        if (ServerConfig.LOG_PACKETS) {
            System.out.println(new StringBuilder().append("調用位置: ").append(new java.lang.Throwable().getStackTrace()[0]).toString());
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MECH_DOOR_REMOVE.getValue());
        mplew.write(animated ? 0 : 1);
        mplew.writeInt(md.getOwnerId());
        mplew.write(md.getId());

        return mplew.getPacket();
    }

    public static byte[] getMesosBomb(MapleCharacter chr, int skillId, List<MapleMapObject> mesos, List<MapleMapObject> mons, int delay) { //楓幣炸彈封包
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        MapleMapItem mapmeso;
        mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
        mplew.write(0);
        mplew.writeInt(chr.getId());
        mplew.writeInt(0x0C);//mode 
        mplew.write(1);
        int count = 0;
        mplew.writeInt(mons.size() <= 10 ? mons.size() : 10);//怪物數量
        for (MapleMapObject obj : mons) {
            count++;
            mplew.writeInt(obj.getObjectId());//怪物的OID
            if (count == 10) {
                break;
            }
        }
        mplew.writeInt(4210014);
        mplew.write(1);
        int i = 0;
        for (MapleMapObject meso : mesos) {
            i++;
            mapmeso = (MapleMapItem) meso;
            mplew.writeInt(i + 1);//未解
            mplew.writeInt((int) mapmeso.getMeso());//楓幣
            //2C 2A 2B 29 
            mplew.writeInt(Randomizer.rand(0x05, 0x2C));//楓幣向上飛的距離
            mplew.writeInt(Randomizer.rand(3, 4));//速度
            mplew.write(Randomizer.rand(6, 64));//可能是坐標
            mplew.write(Randomizer.rand(0, 1));//type
            mplew.writeShort(0);
            mplew.writeInt(delay);//延遲
            mplew.writeInt(mapmeso.getTruePosition().x - chr.getPosition().x);//pos X
            mplew.writeInt(mapmeso.getTruePosition().y - chr.getPosition().y);//pose Y
            mplew.writeLong(0);
            mplew.write(i == 25 ? 0 : i == mesos.size() ? 0 : 1);//最後一個 是00
            if (i == 25) {
                break;
            }
        }

        return mplew.getPacket();
    }

    public static byte[] gainAssassinStack(int chrId, int oid, int forceCount, boolean isAssassin, List<Integer> moboids, int visProjectile, Point posFrom) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
        mplew.write(1);
        mplew.writeInt(chrId);
        mplew.writeInt(oid);
        mplew.writeInt(11);
        mplew.write(1);
        mplew.writeInt(moboids.size());

        for (Integer moboid : moboids) {
            mplew.writeInt(moboid);
        }
        mplew.writeInt(isAssassin ? 4100012 : 4120019);
        for (int i = 0; i < moboids.size(); i++) {
            mplew.write(1);
            mplew.writeInt(forceCount + i);
            mplew.writeInt(isAssassin ? 1 : 2);
            mplew.writeInt(Randomizer.rand(32, 48));
            mplew.writeInt(Randomizer.rand(3, 4));
            mplew.writeInt(Randomizer.rand(100, 200));
            mplew.writeInt(200);
            mplew.writeZeroBytes(8);
            mplew.writeLong(8);
        }
        mplew.write(0);
        mplew.writeInt(posFrom.x - 120);
        mplew.writeInt(posFrom.y - 100);
        mplew.writeInt(posFrom.x + 120);
        mplew.writeInt(posFrom.y + 100);
        mplew.writeInt(visProjectile);

        return mplew.getPacket();
    }

    public static byte[] 隱月小狐仙(int cid, int skillid, int ftt, boolean frommob) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
        mplew.write(frommob);
        mplew.writeInt(cid);
        if (frommob) {
            mplew.writeInt(ftt);//mode 
            mplew.writeInt(4);
            mplew.write(1);
        } else {
            mplew.writeInt(0x0D);//mode 
            mplew.write(1);
            mplew.writeInt(1);//mode 
        }
        mplew.writeInt(ftt);
        mplew.writeInt(skillid);
        mplew.write(1);
        mplew.writeInt(Randomizer.rand(1, 10));
        mplew.writeInt(skillid == 25100010 ? 1 : 2);
        mplew.writeInt(Randomizer.rand(10, 20)); //speed
        mplew.writeInt(Randomizer.rand(20, 40));
        mplew.writeInt(Randomizer.rand(40, 50));
        mplew.writeInt(630);
        mplew.writeLong(0);
        mplew.writeInt(DateUtil.getTime(System.currentTimeMillis()));
        mplew.writeInt(0);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] showQuiverKartrigeEffect(int chrid, int mode, int totle, boolean other) {
        if (ServerConfig.LOG_PACKETS) {
            System.out.println(new StringBuilder().append("調用位置: ").append(new java.lang.Throwable().getStackTrace()[0]).toString());
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (other) {
            mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
            mplew.writeInt(chrid);
        } else {
            mplew.writeShort(SendPacketOpcode.SHOW_SPECIAL_EFFECT.getValue());
        }
        mplew.write(58);
        mplew.writeInt(3101009);
        mplew.writeInt(mode - 1);
        mplew.writeInt(totle);
        return mplew.getPacket();
    }

    public static byte[] showQuiverKartrigeEffect2(int chrid) { //魔幻箭筒 發送給其他玩家的效果包
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
        mplew.writeInt(chrid);
        mplew.write(1);
        mplew.writeInt(3101009);
        mplew.write(0xA0);
        mplew.write(1);
        return mplew.getPacket();
    }

    public static byte[] showQuiverKartrigeAction(int chrid, int skillID, int oid) { //魔幻箭筒 發動攻擊包
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
        mplew.write(0);
        mplew.writeInt(chrid);
        mplew.writeInt(10);//mode 
        mplew.write(1);
        mplew.writeInt(oid);
        mplew.writeInt(skillID);
        mplew.write(1);
        mplew.writeInt(0xA); //遞增
        mplew.writeInt(0);
        mplew.writeInt(Randomizer.rand(10, 14));  //10- 14 隨機
        mplew.writeInt(Randomizer.rand(8, 9));
        mplew.writeInt(Randomizer.rand(0xF8, 0xFA));
        mplew.writeInt(Randomizer.rand(0x36, 0x40));
        mplew.writeLong(0);
        mplew.writeInt(DateUtil.getTime(System.currentTimeMillis()));//時間
        mplew.writeInt(0);
        mplew.write(0);
        return mplew.getPacket();
    }

    public static byte[] showArmorPenetrat() { //壓制 發送的效果包
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_SPECIAL_EFFECT.getValue());
        mplew.write(1);
        mplew.writeInt(3120018);
        mplew.write(0xA0);
        mplew.write(1);
        mplew.write(1);
        return mplew.getPacket();
    }

    public static byte[] showVoidPressure(SendPacketOpcode op, int cid, String len) {//黑暗之眼效果包 
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(op.getValue());
        mplew.writeInt(cid);
        mplew.write(HexTool.getByteArrayFromHexString(len));
        return mplew.getPacket();
    }

    public static byte[] AccurateRocket(MapleCharacter chr, int skillId, List<Integer> mons, int monsid, int BulletCount, int times, boolean isAegis) { //傑諾追縱火箭 和 神盾系統
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
        mplew.write(0);
        mplew.writeInt(chr.getId());
        mplew.writeInt(isAegis ? 5 : 6);//mode 
        mplew.write(1);
        if (isAegis) {
            mplew.writeInt(monsid);
        } else {
            mplew.writeInt(mons.size());//怪物數量
            for (Integer mon : mons) {
                mplew.writeInt(mon); //怪物的OID
            }
        }
        //36001005 傑諾追縱火箭
        //36110004 神盾系統
        mplew.writeInt(skillId);
        mplew.write(1);

        for (int i = 0; i < times; i++) {
            mplew.writeInt(BulletCount + i);
            mplew.writeInt(0);
            mplew.writeInt(isAegis ? Randomizer.rand(0x20, 0x30) : Randomizer.rand(0x10, 0x16));//2上坐標
            mplew.writeInt(isAegis ? 5 : Randomizer.rand(0x10, 0x20)); //速度 延遲
            mplew.writeInt(Randomizer.rand(0x55, 0x99));//大於0x50 右下 
            mplew.write(Randomizer.rand(0xBC, 0xF0));
            mplew.write(1);
            mplew.writeShort(0);
            mplew.writeLong(0);
            mplew.writeInt(DateUtil.getTime(System.currentTimeMillis()));
            mplew.writeInt(0);
            mplew.write(i == times - 1 ? 0 : 1);//最後一個 是00
        }
        return mplew.getPacket();
    }

    //TODO 全息力场
    public static byte[] ForceFieldAttack(int id, int mapid, int level, Point cpoint, int side, Point spoint, boolean sp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        /*mplew.writeShort(SendPacketOpcode.ARROWS_TURRET_ATTACK.getValue());
         mplew.writeShort(0);
         mplew.writeInt(id);//Object_id  第几个
         mplew.writeInt(mapid);
         mplew.write(1);
         mplew.writeInt(1);
         mplew.writeShort(5);
         mplew.writeInt(cpoint.x);
         mplew.writeInt(cpoint.y);
         mplew.writeShort(14);
         mplew.writeInt(sp ? 0 : 655370);
         mplew.writeShort(14);
         mplew.writeInt(skillid);
         mplew.write(side);//方向
         mplew.writeShort(0);*/
        return mplew.getPacket();
    }

    public static byte[] use意志之劍(MapleCharacter chr, int skillId, List<Integer> mons, int BulletCount, int times) { //剑刃之壁
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
        mplew.write(0);
        mplew.writeInt(chr.getId());
        mplew.writeInt(2);//mode 
        mplew.write(1);

        mplew.writeInt(mons.size());//怪物數量
        for (Integer mon : mons) {
            mplew.writeInt(mon); //怪物的OID
        }
        mplew.writeInt(skillId);
        mplew.write(1);

        for (int i = 0; i < times; i++) {
            mplew.writeInt(BulletCount + i);
            mplew.writeInt(2);
            mplew.writeInt(i % 2 == 0 ? 0x13 : 0x0F);//2上坐标
            mplew.writeInt(Randomizer.rand(0x10, 0x20)); //速度  延遲
            mplew.writeInt(0);
            mplew.write(Randomizer.rand(0x60, 0xF0));
            mplew.write(Randomizer.rand(0x4, 0x5));
            mplew.writeShort(0);
            mplew.writeLong(0);
            mplew.writeInt(DateUtil.getTime(System.currentTimeMillis()));
            mplew.writeInt(0);
            mplew.write(i == times - 1 ? 0 : 1);//最后一个 是00
        }
        return mplew.getPacket();
    }

    public static byte[] use風妖精之箭(MapleCharacter chr, int skillId, List<MapleMapObject> mons, int BulletCount, int zz) { //狂风肆虐
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
        mplew.write(0);
        mplew.writeInt(chr.getId());
        mplew.writeInt(7);//mode 
        mplew.write(1);
        int time = mons.size() > 4 ? 4 : mons.size();
        mplew.writeInt(time);//怪物的数量
        int zzz = 0;
        for (MapleMapObject mon : mons) {
            mplew.writeInt(mon.getObjectId()); //怪物的OID
            if (zzz == 3) {
                break;
            }
            zzz++;
        }
        mplew.writeInt(skillId);
        mplew.write(1);
        for (int i = 0; i < time; i++) {
            int rang = Randomizer.rand(0x20, 0x35);
            mplew.writeInt(BulletCount + i);
            mplew.writeInt(zz == 1 ? 2 : 1);
            mplew.writeInt(rang);  //距离
            mplew.writeInt(zz == 1 ? 5 : i % 2 == 0 ? 0x11 : 0x16);  //速度
            mplew.writeInt(chr.isFacingLeft() ? Randomizer.rand(240, 300) : Randomizer.rand(60, 120));//方向角度
            mplew.writeInt(Randomizer.rand(0x20, 0x30));//停留延时
            mplew.writeLong(0);
            mplew.writeInt(DateUtil.getTime(System.currentTimeMillis()));
            mplew.writeInt(0);
            mplew.write(i == time - 1 ? 0 : 1);//最后一个 是00
        }
        return mplew.getPacket();
    }

    public static byte[] use風暴使者(MapleCharacter chr, int skillId, int mod) { //暴风灭世 坐标错误
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
        mplew.write(0);
        mplew.writeInt(chr.getId());
        mplew.writeInt(8);//mode 
        mplew.write(1);
        mplew.writeInt(mod); //怪物的OID
        mplew.writeInt(skillId);
        mplew.write(1);
        mplew.writeInt(5);
        mplew.writeInt(1);
        mplew.writeInt(1);//2上坐标
        mplew.writeInt(7); //速度  延时
        mplew.writeInt(chr.isFacingLeft() ? 270 : 90);//角度
        mplew.writeInt(58);
        mplew.writeInt(0);//做坐标
        mplew.writeInt(0);//下坐标
        mplew.writeInt(DateUtil.getTime(System.currentTimeMillis()));
        mplew.writeInt(0);
        mplew.write(0);//最后一个 是00

        return mplew.getPacket();
    }

    public static byte[] cancel标飞标记(int monOID, MapleCharacter chr) { //标飞标记
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.CANCEL_MONSTER_STATUS.getValue());
        mplew.writeInt(monOID);
        PacketHelper.writeSingleMobMask(mplew, MonsterStatus.BLEED);
        mplew.writeInt(1);
        mplew.writeInt(1);
        mplew.writeInt(chr.getId());
        mplew.write(HexTool.getByteArrayFromHexString("20 DD ED 03"));
        mplew.write(3);
        return mplew.getPacket();
    }

    public static byte[] use标飞标记(MapleCharacter chr, int skillId, int monOID) { //标飞标记
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.APPLY_MONSTER_STATUS.getValue());
        mplew.writeInt(monOID);
        PacketHelper.writeSingleMobMask(mplew, MonsterStatus.BLEED);
        mplew.write(1);
        mplew.writeInt(chr.getId());
        mplew.writeInt(skillId);
        mplew.writeInt(0x25);
        mplew.writeInt(1000);
        mplew.write(HexTool.getByteArrayFromHexString("C7 54 24 56"));
        mplew.writeInt(21000);
        mplew.writeShort(0x14);
        mplew.writeLong(0);
        mplew.write(2);
        return mplew.getPacket();
    }

    public static byte[] getTrialFlame(int chrid, int oid) { //藍焰斬
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
        mplew.write(0);
        mplew.writeInt(chrid);
        mplew.writeInt(3);//mode 
        mplew.write(1);
        mplew.writeInt(1);
        mplew.writeInt(oid);
        mplew.writeInt(2121055);
        mplew.write(1);
        mplew.writeInt(0xA); //递增
        mplew.writeInt(2);
        mplew.writeInt(Randomizer.rand(10, 14));  //10- 14 随机
        mplew.writeInt(Randomizer.rand(15, 18));
        mplew.writeInt(Randomizer.rand(0x20, 0x24));
        mplew.writeInt(Randomizer.rand(0x36, 0x40));
        mplew.writeLong(0);
        mplew.writeInt(DateUtil.getTime(System.currentTimeMillis()));//时间
        mplew.writeInt(0);
        mplew.write(0);
        return mplew.getPacket();
    }

//    public static byte[] canLuckyMoney(boolean x) { //翻轉硬幣是否可以使用
//        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
//
//        mplew.writeShort(SendPacketOpcode.LUCKY_MONEY.getValue());
//        mplew.write(x);
//        return mplew.getPacket();
//    }
//
//    public static byte[] getAdvancedAttack(int srcskill, int paskill, int WeaponType, int value, int monoid) { //被动触发进阶攻击或被动攻击
//        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
//
//        mplew.writeShort(SendPacketOpcode.ADVANCED_ATTACK.getValue());
//        mplew.writeInt(srcskill);//当前使用的技能
//        mplew.writeInt(paskill);//被动终极技能
//        mplew.writeInt(WeaponType);//武器类型
//        mplew.writeInt(value);//技能 特定值？
//        mplew.writeInt(monoid);//怪物的OID
//        return mplew.getPacket();
//    }
//
//    public static byte[] AngelReborn(int srcskill, boolean done) { //被动触发进阶攻击或被动攻击
//        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
//        if (done) {
//            mplew.writeShort(SendPacketOpcode.ANGEL_REBORN_DONE.getValue());
//        } else {
//            mplew.writeShort(SendPacketOpcode.ANGEL_REBORN.getValue());
//            mplew.writeInt(srcskill);
//        }
//        return mplew.getPacket();
//    }

    public static byte[] DrainSoul(MapleCharacter chr, int oid, List<Integer> mon, int forceCount, int count, int skillid, int dell, boolean frommon) {//灵魂吸取
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
        mplew.write(frommon);
        mplew.writeInt(chr.getId());
        if (frommon) {
            mplew.writeInt(oid);
        }
        mplew.writeInt(frommon ? 4 : 3);//type

        mplew.write(1);
        if (frommon) {
            mplew.writeInt(oid);
        } else {
            mplew.writeInt(mon.size());
            for (int i : mon) {
                mplew.writeInt(i);
            }
        }
        mplew.writeInt(skillid);
        mplew.write(1);
        for (int i = 0; i < (frommon ? 1 : count); i++) {
            mplew.writeInt(forceCount + i);
            mplew.writeInt(skillid == 31221014 ? 3 : 1);
            mplew.writeInt(frommon ? Randomizer.rand(0x20, 0x30) : Randomizer.rand(0x0F, 0x20));
            mplew.writeInt(frommon ? Randomizer.rand(3, 4) : Randomizer.rand(0x15, 0x30));
            mplew.writeInt(frommon ? Randomizer.rand(0x0, 0xFF) : Randomizer.rand(0x30, 0x50));
            mplew.writeInt(dell);
            mplew.writeZeroBytes(8);
            mplew.writeInt(DateUtil.getTime(System.currentTimeMillis()));
            mplew.writeInt(0);
            mplew.write(frommon ? 0 : (i == count - 1 ? 0 : 1));
        }
        return mplew.getPacket();
    }

    public static byte[] FlamesTrack(int chrid, int skillid, int side, int k) { //FlamesTrack  轨道烈焰
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());
        mplew.write(0);
        mplew.writeInt(chrid);
        mplew.writeInt(0x11);//mode 
        mplew.write(1);
        mplew.writeInt(0);
        mplew.writeInt(skillid);
        mplew.write(1);
        mplew.writeInt(0x2); //递增
        mplew.writeInt(k);
        mplew.writeInt(0x14);  //10- 14 随机
        mplew.writeInt(0x14);
        mplew.writeInt(0x5A);
        mplew.writeInt(0);
        mplew.writeLong(0);
        mplew.writeInt(DateUtil.getTime(System.currentTimeMillis()));//时间
        mplew.writeInt(8);
        mplew.write(0);
        mplew.writeInt(side);
        mplew.writeInt(500);
        return mplew.getPacket();
    }

    public static byte[] FireStep() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FIRE_STEP.getValue());
        mplew.writeInt(6850036);
        return mplew.getPacket();
    }

    public static byte[] ConveyTo() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CONVEY_TO.getValue());
        mplew.write(1);
        return mplew.getPacket();
    }
}
