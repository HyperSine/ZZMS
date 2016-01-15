package tools.packet;

import client.MonsterStatus;
import client.MonsterStatusEffect;
import constants.GameConstants;
import handling.SendPacketOpcode;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import server.life.MapleMonster;
import server.maps.MapleMap;
import server.maps.MapleNodes;
import server.movement.LifeMovementFragment;
import tools.Pair;
import tools.data.MaplePacketLittleEndianWriter;

public class MobPacket {

    public static byte[] damageMonster(int oid, long damage) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DAMAGE_MONSTER.getValue());
        mplew.writeInt(oid);
        mplew.write(0);
        mplew.writeLong(damage);

        return mplew.getPacket();
    }

    public static byte[] damageFriendlyMob(MapleMonster mob, long damage, boolean display) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DAMAGE_MONSTER.getValue());
        mplew.writeInt(mob.getObjectId());
        mplew.write(display ? 1 : 2);
        mplew.writeInt(damage > 2147483647L ? 2147483647 : (int) damage);
        mplew.writeInt(mob.getHp() > 2147483647L ? (int) (mob.getHp() / mob.getMobMaxHp() * 2147483647.0D) : (int) mob.getHp());
        mplew.writeInt(mob.getMobMaxHp() > 2147483647L ? 2147483647 : (int) mob.getMobMaxHp());

        return mplew.getPacket();
    }

    public static byte[] killMonster(int oid, int animation, boolean azwan) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (azwan) {
            mplew.writeShort(SendPacketOpcode.AZWAN_KILL_MONSTER.getValue());
        } else {
            mplew.writeShort(SendPacketOpcode.KILL_MONSTER.getValue());
        }
        boolean a = false; //idk
        boolean b = false; //idk
        if (azwan) {
            mplew.write(a ? 1 : 0);
            mplew.write(b ? 1 : 0);
        }
        mplew.writeInt(oid);
        if (azwan) {
            if (a) {
                mplew.write(0);
                if (b) {
                    //set mob temporary stat
                } else {
                    //set mob temporary stat
                }
            } else {
                if (b) {
                    //idk
                } else {
                    //idk
                }
            }
            return mplew.getPacket();
        }
        mplew.write(animation);
        if (animation == 4) {
            mplew.writeInt(-1);
        }

        return mplew.getPacket();
    }

    public static byte[] suckMonster(int oid, int chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.KILL_MONSTER.getValue());
        mplew.writeInt(oid);
        mplew.write(4);
        mplew.writeInt(chr);

        return mplew.getPacket();
    }

    public static byte[] healMonster(int oid, int heal) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DAMAGE_MONSTER.getValue());
        mplew.writeInt(oid);
        mplew.write(0);
        mplew.writeInt(-heal);

        return mplew.getPacket();
    }

    public static byte[] MobToMobDamage(int oid, int dmg, int mobid, boolean azwan) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (azwan) {
            mplew.writeShort(SendPacketOpcode.AZWAN_MOB_TO_MOB_DAMAGE.getValue());
        } else {
            mplew.writeShort(SendPacketOpcode.MOB_TO_MOB_DAMAGE.getValue());
        }
        mplew.writeInt(oid);
        mplew.write(0);
        mplew.writeInt(dmg);
        mplew.writeInt(mobid);
        mplew.write(1);

        return mplew.getPacket();
    }

    public static byte[] getMobSkillEffect(int oid, int skillid, int cid, int skilllevel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SKILL_EFFECT_MOB.getValue());
        mplew.writeInt(oid);
        mplew.writeInt(skillid);
        mplew.writeInt(cid);
        mplew.writeShort(skilllevel);

        return mplew.getPacket();
    }

    public static byte[] getMobCoolEffect(int oid, int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ITEM_EFFECT_MOB.getValue());
        mplew.writeInt(oid);
        mplew.writeInt(itemid);

        return mplew.getPacket();
    }

    public static byte[] showMonsterHP(int oid, int remhppercentage) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_MONSTER_HP.getValue());
        mplew.writeInt(oid);
        mplew.write(remhppercentage);

        return mplew.getPacket();
    }

    public static byte[] showCygnusAttack(int oid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CYGNUS_ATTACK.getValue());
        mplew.writeInt(oid);

        return mplew.getPacket();
    }

    public static byte[] showMonsterResist(int oid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MONSTER_RESIST.getValue());
        mplew.writeInt(oid);
        mplew.writeInt(0);
        mplew.writeShort(1);
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] showBossHP(MapleMonster mob) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(6);
        mplew.writeInt(mob.getId() == 9400589 ? 9300184 : mob.getId());
        mplew.writeInt(mob.getHp() > 2147483647L ? (int) (mob.getHp() / mob.getMobMaxHp() * 2147483647.0D) : (int) mob.getHp());
        mplew.writeInt(mob.getMobMaxHp() > 2147483647L ? 2147483647 : (int) mob.getMobMaxHp());
        mplew.write(mob.getStats().getTagColor());
        mplew.write(mob.getStats().getTagBgColor());

        return mplew.getPacket();
    }

    public static byte[] showBossHP(int monsterId, long currentHp, long maxHp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(6);
        mplew.writeInt(monsterId);
        mplew.writeInt(currentHp > 2147483647L ? (int) (currentHp / maxHp * 2147483647.0D) : (int) (currentHp <= 0L ? -1L : currentHp));
        mplew.writeInt(maxHp > 2147483647L ? 2147483647 : (int) maxHp);
        mplew.write(6);
        mplew.write(5);

        return mplew.getPacket();
    }

    public static byte[] moveMonster(boolean useskill, int skill, int unk, int oid, Point startPos, List<LifeMovementFragment> moves) {
        return moveMonster(useskill, skill, unk, oid, startPos, moves, null, null);
    }

    public static byte[] moveMonster(boolean useskill, int skill, int unk, int oid, Point startPos, List<LifeMovementFragment> moves, List<Integer> unk2, List<Pair<Integer, Integer>> unk3) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MOVE_MONSTER.getValue());
        mplew.writeInt(oid);
        mplew.write(useskill ? 1 : 0);
        mplew.write(skill);
        mplew.writeInt(unk);
        mplew.write(unk3 == null ? 0 : unk3.size());
        if (unk3 != null) {
            for (Pair i : unk3) {
                mplew.writeShort(((Integer) i.left));
                mplew.writeShort(((Integer) i.right));
            }
        }
        mplew.write(unk2 == null ? 0 : unk2.size());
        if (unk2 != null) {
            for (Integer i : unk2) {
                mplew.writeShort(i);
            }
        }
        mplew.writeInt(0);
        mplew.writePos(startPos);
        mplew.writeInt(0);
        PacketHelper.serializeMovementList(mplew, moves);
        mplew.writeShort(0);

        return mplew.getPacket();
    }

    public static byte[] spawnMonster(MapleMonster life, int spawnType, int link, boolean azwan) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_MONSTER.getValue());
        mplew.write(0);//new143
        mplew.writeInt(life.getObjectId());
        mplew.write(1);
        mplew.writeInt(life.getId());
        addMonsterStatus(mplew, life);
        Collection<MonsterStatusEffect> buffs = life.getStati().values();
        EncodeTemporary(mplew, buffs);
        addMonsterInformation(mplew, life, true, false, (byte) spawnType, link);

        return mplew.getPacket();
    }

    public static byte[] controlMonster(MapleMonster life, boolean newSpawn, boolean aggro, boolean azwan) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_MONSTER_CONTROL.getValue());
        mplew.write(aggro ? 2 : 1);
        mplew.writeInt(life.getObjectId());
        mplew.write(1);// 1 = Control normal, 5 = Control none?
        mplew.writeInt(life.getId());//idk?
        addMonsterStatus(mplew, life);
        Collection<MonsterStatusEffect> buffs = life.getStati().values();
        EncodeTemporary(mplew, buffs);
        addMonsterInformation(mplew, life, newSpawn, false, (byte) (life.isFake() ? 1 : 0), 0);

        return mplew.getPacket();
    }

    public static byte[] stopControllingMonster(MapleMonster life, boolean azwan) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_MONSTER_CONTROL.getValue());
        mplew.write(0);
        mplew.writeInt(life.getObjectId());
//        if (azwan) {
//            mplew.write(0);
//            mplew.writeInt(0);
//            mplew.write(0);
//            addMonsterStatus(mplew, life);
//
//            mplew.writePos(life.getTruePosition());
//            mplew.write(life.getStance());
//            mplew.writeShort(0);
//            mplew.writeShort(life.getFh());
//            mplew.write(life.isFake() ? -4 : -1);
//            mplew.write(life.getCarnivalTeam());
//            mplew.writeInt(63000);
//            mplew.writeInt(0);
//            mplew.writeInt(0);
//            mplew.write(-1);
//        }

        return mplew.getPacket();
    }

    public static byte[] makeMonsterReal(MapleMonster life, boolean azwan) {
        return spawnMonster(life, -1, 0, azwan);
    }

    public static byte[] makeMonsterFake(MapleMonster life, boolean azwan) {
        return spawnMonster(life, -4, 0, azwan);
    }

    public static byte[] makeMonsterEffect(MapleMonster life, int effect, boolean azwan) {
        return spawnMonster(life, effect, 0, azwan);
    }

    public static byte[] moveMonsterResponse(int objectid, short moveid, int currentMp, boolean useSkills, int skillId, int skillLevel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MOVE_MONSTER_RESPONSE.getValue());
        mplew.writeInt(objectid); // 4
        mplew.writeShort(moveid); // 6
        mplew.write(useSkills ? 1 : 0); // 7
        mplew.writeShort(currentMp); // 9
        mplew.write(skillId); // 10
        mplew.write(skillLevel); // 11 
        mplew.writeInt(0); // 15
        mplew.writeShort(0);//new143 // 17

        return mplew.getPacket();
    }

//    public static byte[] getMonsterSkill(int objectid) {
//        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
//
//        mplew.writeShort(SendPacketOpcode.MONSTER_SKILL.getValue());
//        mplew.writeInt(objectid);
//        mplew.writeLong(0);
//
//        return mplew.getPacket();
//    }
    public static byte[] getMonsterTeleport(int objectid, int x, int y) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TELE_MONSTER.getValue());
        mplew.writeInt(objectid);
        mplew.writeInt(x);
        mplew.writeInt(y);

        return mplew.getPacket();
    }

    public static byte[] applyMonsterStatus(MapleMonster mons, MonsterStatusEffect ms) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.APPLY_MONSTER_STATUS.getValue());
        mplew.writeInt(mons.getObjectId());
        SingleProcessStatSet(mplew, ms);
//        System.out.println("applyMonsterStatus 1");

        return mplew.getPacket();
    }

    public static byte[] applyMonsterStatus(MapleMonster mons, List<MonsterStatusEffect> mse) {
        if ((mse.size() <= 0) || (mse.get(0) == null)) {
            return CWvsContext.enableActions();
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.APPLY_MONSTER_STATUS.getValue());
        mplew.writeInt(mons.getObjectId());
        ProcessStatSet(mplew, mse);
//        System.out.println("applyMonsterStatus 2");

        return mplew.getPacket();
    }

    public static byte[] cancelMonsterStatus(MapleMonster mons, MonsterStatusEffect ms) {
        List<MonsterStatusEffect> mse = new ArrayList<>();
        mse.add(ms);
        return cancelMonsterStatus(mons, mse);
    }

    public static byte[] cancelMonsterStatus(MapleMonster mons, List<MonsterStatusEffect> mse) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CANCEL_MONSTER_STATUS.getValue());
        mplew.writeInt(mons.getObjectId());
        writeMaskFromList(mplew, mse);
        for (MonsterStatusEffect buff : mse) {
            if (buff.getStati() == MonsterStatus.BLEED) {
                mplew.writeInt(0);
                int v6 = 0;
                mplew.writeInt(v6);
                if (v6 > 0) {
                    do {
                        mplew.writeInt(0);
                        mplew.writeInt(0);
                        --v6;
                    } while (v6 == 0);
                }
            }
        }
        mplew.write(2);
        // if (MobStat::IsMovementAffectingStat)
        mplew.write(1);
//        System.out.println("cancelMonsterStatus");

        return mplew.getPacket();
    }

    public static byte[] talkMonster(int oid, int itemId, String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TALK_MONSTER.getValue());
        mplew.writeInt(oid);
        mplew.writeInt(500);
        mplew.writeInt(itemId);
        mplew.write(itemId <= 0 ? 0 : 1);
        mplew.write((msg == null) || (msg.length() <= 0) ? 0 : 1);
        if ((msg != null) && (msg.length() > 0)) {
            mplew.writeMapleAsciiString(msg);
        }
        mplew.writeInt(1);

        return mplew.getPacket();
    }

    public static byte[] removeTalkMonster(int oid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REMOVE_TALK_MONSTER.getValue());
        mplew.writeInt(oid);

        return mplew.getPacket();
    }

    public static final byte[] getNodeProperties(MapleMonster objectid, MapleMap map) {
        if (objectid.getNodePacket() != null) {
            return objectid.getNodePacket();
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MONSTER_PROPERTIES.getValue());
        mplew.writeInt(objectid.getObjectId());
        mplew.writeInt(map.getNodes().size());
        mplew.writeInt(objectid.getPosition().x);
        mplew.writeInt(objectid.getPosition().y);
        for (MapleNodes.MapleNodeInfo mni : map.getNodes()) {
            mplew.writeInt(mni.x);
            mplew.writeInt(mni.y);
            mplew.writeInt(mni.attr);
            if (mni.attr == 2) {
                mplew.writeInt(500);
            }
        }
        mplew.writeInt(0);
        mplew.write(0);
        mplew.write(0);

        objectid.setNodePacket(mplew.getPacket());
        return objectid.getNodePacket();
    }

    public static byte[] showMagnet(int mobid, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_MAGNET.getValue());
        mplew.writeInt(mobid);
        mplew.write(success ? 1 : 0);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] catchMonster(int mobid, int itemid, byte success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CATCH_MONSTER.getValue());
        mplew.writeInt(mobid);
        mplew.writeInt(itemid);
        mplew.write(success);

        return mplew.getPacket();
    }

    public static void addMonsterStatus(MaplePacketLittleEndianWriter mplew, MapleMonster life) {
        if (life.getStati().size() <= 1) {
            life.addEmpty();
        }
        mplew.write(life.getChangedStats() != null);
        if (life.getChangedStats() != null) {
            mplew.writeInt(life.getChangedStats().hp > 2147483647L ? 2147483647 : (int) life.getChangedStats().hp);
            mplew.writeInt(life.getChangedStats().mp);
            mplew.writeInt(life.getChangedStats().exp);
            mplew.writeInt(life.getChangedStats().watk);
            mplew.writeInt(life.getChangedStats().matk);
            mplew.writeInt(life.getChangedStats().PDRate);
            mplew.writeInt(life.getChangedStats().MDRate);
            mplew.writeInt(life.getChangedStats().acc);
            mplew.writeInt(life.getChangedStats().eva);
            mplew.writeInt(life.getChangedStats().pushed);
            mplew.writeInt(life.getChangedStats().speed);//new 141?
            mplew.writeInt(life.getChangedStats().level);
        }
    }

    public static void EncodeTemporary(MaplePacketLittleEndianWriter mplew, Collection<MonsterStatusEffect> buffs) {
        Set<MonsterStatus> mobstat = new HashSet();
        writeMaskFromList(mplew, buffs);
        for (MonsterStatusEffect buff : buffs) {
            mobstat.add(buff.getStati());
            if (buff.getStati().getBitNumber() < MonsterStatus.BLEED.getBitNumber()) {
                mplew.writeInt(buff.getX());
                if (buff.getMobSkill() != null) {
                    mplew.writeShort(buff.getMobSkill().getSkillId());
                    mplew.writeShort(buff.getMobSkill().getSkillLevel());
                } else {
                    mplew.writeInt(buff.getSkill() > 0 ? buff.getSkill() : 0);
                }
                mplew.writeShort((short) ((buff.getCancelTask() - System.currentTimeMillis()) / 1000));
            }
        }
        if (mobstat.contains(MonsterStatus.WDEF)) {
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.MDEF)) {
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.BLIND)) {
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.SEAL_SKILL)) {
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.BLIND) || mobstat.contains(MonsterStatus.SEAL_SKILL)) {
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.HEAL_DAMAGE)) {
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.MBS61)) {
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.MBS65)) {
            int result = 0;
            mplew.write(result);
            if (result != 0) {
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
        }
        if (mobstat.contains(MonsterStatus.MBS39)) {
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.MBS42)) {
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.SPEED)) {
            mplew.write(0);
        }
        if (mobstat.contains(MonsterStatus.MBS51)) {
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.MBS54)) {
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.MBS56)) {
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.BLEED)) {
            int v4 = 0;
            int v23 = 0;
            mplew.write(v4);
            if (v4 > 0) {
                do {
                    mplew.writeInt(8695624);
                    mplew.writeInt(80001431); // 技能ID
                    mplew.writeInt(7100);
                    mplew.writeInt(1000); // 延遲毫秒 : dotInterval * 1000
                    mplew.writeInt(187277775);
                    mplew.writeInt(16450);
                    mplew.writeInt(15); // dotTime
                    mplew.writeInt(0);
                    mplew.writeInt(1);
                    mplew.writeInt(7100);
                    ++v23;
                } while (v23 < v4);
            }
        }
        if (mobstat.contains(MonsterStatus.MBS63)) {
            mplew.write(0);
            mplew.write(0);
        }
        if (mobstat.contains(MonsterStatus.MBS64)) {
            mplew.write(0);
        }
        if (mobstat.contains(MonsterStatus.MONSTER_BOMB)) {
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.MBS66)) {
            mplew.writeMapleAsciiString("");
        }
        if (mobstat.contains(MonsterStatus.MBS67)) {
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.MBS68)) {
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeShort(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.SUMMON)) {
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeShort(0);
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.MBS70)) {
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.MBS71)) {
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        if (mobstat.contains(MonsterStatus.MBS72)) {
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
    }

    public static void SingleProcessStatSet(MaplePacketLittleEndianWriter mplew, MonsterStatusEffect buff) {
        Set<MonsterStatusEffect> ss = new HashSet<>();
        ss.add(buff);
        ProcessStatSet(mplew, ss);
    }

    public static void ProcessStatSet(MaplePacketLittleEndianWriter mplew, Collection<MonsterStatusEffect> buffs) {
        EncodeTemporary(mplew, buffs);
        mplew.writeShort(2);
        mplew.write(1);
        // if (MobStat::IsMovementAffectingStat)
        mplew.write(1);
    }

    private static void writeMaskFromList(MaplePacketLittleEndianWriter mplew, Collection<MonsterStatusEffect> ss) {
        int[] mask = new int[GameConstants.MAX_MOBSTAT];
        for (MonsterStatusEffect statup : ss) {
            mask[(statup.getStati().getPosition())] |= statup.getStati().getValue();
        }
        for (int i = 0; i < mask.length; i++) {
            mplew.writeInt(mask[(i)]);
        }
    }

    public static void addMonsterInformation(MaplePacketLittleEndianWriter mplew, MapleMonster life, boolean newSpawn, boolean summon, int spawnType, int link) {
        mplew.writePos(life.getTruePosition());
        mplew.write(life.getStance());
        mplew.writeShort(life.getFh());
        mplew.writeShort(life.getFh());
        mplew.writeShort(newSpawn ? spawnType : life.isFake() ? -4 : -1);
        if ((spawnType == -3) || (spawnType >= 0)) {
            mplew.writeInt(link);
        }
        mplew.write(life.getCarnivalTeam());
        mplew.writeInt(life.getHp() > 2147483647 ? 2147483647 : (int) life.getHp());
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(-1);
        mplew.writeInt(-1);
        mplew.write(0);
        mplew.writeLong(0);
        mplew.writeInt(-1);
        mplew.write(0);
        mplew.write(-1);
        mplew.writeInt(0);
    }
}
