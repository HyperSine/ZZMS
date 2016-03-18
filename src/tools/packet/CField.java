package tools.packet;

import client.*;
import client.inventory.*;
import constants.GameConstants;
import constants.QuickMove.QuickMoveNPC;
import constants.ServerConfig;
import constants.ServerConstants;
import handling.SendPacketOpcode;
import handling.channel.handler.PlayerInteractionHandler;
import handling.world.World;
import handling.world.guild.MapleGuild;
import handling.world.guild.MapleGuildAlliance;
import java.awt.Point;
import java.util.*;
import server.MaplePackageActions;
import server.MapleTrade;
import server.Randomizer;
import server.events.MapleSnowball;
import server.life.MapleNPC;
import server.maps.*;
import server.movement.LifeMovementFragment;
import server.quest.MapleQuest;
import server.shops.MapleShop;
import tools.AttackPair;
import tools.HexTool;
import tools.Pair;
import tools.Triple;
import tools.data.MaplePacketLittleEndianWriter;
import tools.packet.provider.SpecialEffectType;

public class CField {

    public static byte[] getPacketFromHexString(String hex) {
        return HexTool.getByteArrayFromHexString(hex);
    }

    public static byte[] getServerIP(MapleClient c, int port, int clientId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVER_IP.getValue());
        mplew.write(0);
        mplew.write(0);
        if (c.getTempIP().length() > 0) {
            for (String s : c.getTempIP().split(",")) {
                mplew.write(Integer.parseInt(s));
            }
        } else {
            mplew.write(ServerConstants.getGateway_IP());
        }
        mplew.writeShort(port);
        mplew.writeInt(0); // [IP位置] 176+ IDB無調用?
        mplew.writeShort(0); // [Port] 176+ IDB無調用?
        mplew.writeInt(clientId);
        mplew.write(0);
        mplew.writeInt(0);
//      System.err.println(mplew.toString());

        return mplew.getPacket();
    }

    public static byte[] updatePendantSlot(long time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_PENDANT_SLOT.getValue());
        mplew.writeLong(time);

        return mplew.getPacket();
    }

    public static byte[] autoLogin(String code) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.AUTO_LOGIN.getValue());
        mplew.writeMapleAsciiString(code);

        return mplew.getPacket();
    }

    public static byte[] exitGame() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.EXIT_GAME.getValue());

        return mplew.getPacket();
    }

    public static byte[] getChannelChange(MapleClient c, int port) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHANGE_CHANNEL.getValue());
        mplew.write(1);
        mplew.write(ServerConstants.getGateway_IP());
        mplew.writeShort(port);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] getPVPType(int type, List<Pair<Integer, String>> players1, int team, boolean enabled, int lvl) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_TYPE.getValue());
        mplew.write(type);
        mplew.write(lvl);
        mplew.write(enabled ? 1 : 0);
        mplew.write(0);
        if (type > 0) {
            mplew.write(team);
            mplew.writeInt(players1.size());
            for (Pair pl : players1) {
                mplew.writeInt(((Integer) pl.left));
                mplew.writeMapleAsciiString((String) pl.right);
                mplew.writeShort(2660);
            }
        }

        return mplew.getPacket();
    }

    public static byte[] getPVPTransform(int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_TRANSFORM.getValue());
        mplew.write(type);

        return mplew.getPacket();
    }

    public static byte[] getPVPDetails(List<Pair<Integer, Integer>> players) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_DETAILS.getValue());
        mplew.write(1);
        mplew.write(0);
        mplew.writeInt(players.size());
        for (Pair pl : players) {
            mplew.writeInt(((Integer) pl.left));
            mplew.write(((Integer) pl.right));
        }

        return mplew.getPacket();
    }

    public static byte[] enablePVP(boolean enabled) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_ENABLED.getValue());
        mplew.write(enabled ? 1 : 2);

        return mplew.getPacket();
    }

    public static byte[] getPVPScore(int score, boolean kill) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_SCORE.getValue());
        mplew.writeInt(score);
        mplew.write(kill ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] getPVPResult(List<Pair<Integer, MapleCharacter>> flags, int exp, int winningTeam, int playerTeam) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_RESULT.getValue());
        mplew.writeInt(flags.size());
        for (Pair f : flags) {
            mplew.writeInt(((MapleCharacter) f.right).getId());
            mplew.writeMapleAsciiString(((MapleCharacter) f.right).getName());
            mplew.writeInt(((Integer) f.left));
            mplew.write(((MapleCharacter) f.right).getTeam() + 1);
            mplew.write(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(exp);
        mplew.write(0);
        mplew.writeShort(100);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.write(0);
        mplew.write(winningTeam);
        mplew.write(playerTeam);

        return mplew.getPacket();
    }

    public static byte[] getPVPTeam(List<Pair<Integer, String>> players) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_TEAM.getValue());
        mplew.writeInt(players.size());
        for (Pair pl : players) {
            mplew.writeInt(((Integer) pl.left));
            mplew.writeMapleAsciiString((String) pl.right);
            mplew.write(0x0A);
            mplew.write(0x64);
        }

        return mplew.getPacket();
    }

    public static byte[] getPVPScoreboard(List<Pair<Integer, MapleCharacter>> flags, int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_SCOREBOARD.getValue());
        mplew.writeShort(flags.size());
        for (Pair f : flags) {
            mplew.writeInt(((MapleCharacter) f.right).getId());
            mplew.writeMapleAsciiString(((MapleCharacter) f.right).getName());
            mplew.writeInt(((Integer) f.left));
            mplew.write(type == 0 ? 0 : ((MapleCharacter) f.right).getTeam() + 1);
            mplew.writeInt(0);
        }
        mplew.writeShort(flags.size());
        for (Pair f : flags) {
            mplew.writeInt(((MapleCharacter) f.right).getId());
            mplew.writeMapleAsciiString(((MapleCharacter) f.right).getName());
            mplew.writeInt(((Integer) f.left));
            mplew.write(type == 0 ? 0 : ((MapleCharacter) f.right).getTeam() + 1);
            mplew.writeInt(0);
        }

        return mplew.getPacket();
    }

    public static byte[] getPVPPoints(int p1, int p2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_POINTS.getValue());
        mplew.writeInt(p1);
        mplew.writeInt(p2);

        return mplew.getPacket();
    }

    public static byte[] getPVPKilled(String lastWords) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_KILLED.getValue());
        mplew.writeMapleAsciiString(lastWords);

        return mplew.getPacket();
    }

    public static byte[] getPVPMode(int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_MODE.getValue());
        mplew.write(mode);

        return mplew.getPacket();
    }

    public static byte[] getPVPIceHPBar(int hp, int maxHp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_ICEKNIGHT.getValue());
        mplew.writeInt(hp);
        mplew.writeInt(maxHp);

        return mplew.getPacket();
    }

    public static byte[] getCaptureFlags(MapleMap map) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CAPTURE_FLAGS.getValue());
        mplew.writeRect(map.getArea(0));
        mplew.writeInt(((Point) ((Pair) map.getGuardians().get(0)).left).x);
        mplew.writeInt(((Point) ((Pair) map.getGuardians().get(0)).left).y);
        mplew.writeRect(map.getArea(1));
        mplew.writeInt(((Point) ((Pair) map.getGuardians().get(1)).left).x);
        mplew.writeInt(((Point) ((Pair) map.getGuardians().get(1)).left).y);

        return mplew.getPacket();
    }

    public static byte[] getCapturePosition(MapleMap map) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        Point p1 = map.getPointOfItem(2910000);
        Point p2 = map.getPointOfItem(2910001);
        mplew.writeShort(SendPacketOpcode.CAPTURE_POSITION.getValue());
        mplew.write(p1 == null ? 0 : 1);
        if (p1 != null) {
            mplew.writeInt(p1.x);
            mplew.writeInt(p1.y);
        }
        mplew.write(p2 == null ? 0 : 1);
        if (p2 != null) {
            mplew.writeInt(p2.x);
            mplew.writeInt(p2.y);
        }

        return mplew.getPacket();
    }

    public static byte[] resetCapture() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CAPTURE_RESET.getValue());

        return mplew.getPacket();
    }

    public static byte[] getMacros(SkillMacro[] macros) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SKILL_MACRO.getValue());
        int count = 0;
        for (int i = 0; i < 5; i++) {
            if (macros[i] != null) {
                count++;
            }
        }
        mplew.write(count);
        for (int i = 0; i < 5; i++) {
            SkillMacro macro = macros[i];
            if (macro != null) {
                mplew.writeMapleAsciiString(macro.getName());
                mplew.write(macro.getShout());
                mplew.writeInt(macro.getSkill1());
                mplew.writeInt(macro.getSkill2());
                mplew.writeInt(macro.getSkill3());
            }
        }

        return mplew.getPacket();
    }

    public static byte[] gameMsg(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GAME_MSG.getValue());
        mplew.writeAsciiString(msg);
        mplew.write(1);

        return mplew.getPacket();
    }

    public static byte[] innerPotentialMsg(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.INNER_ABILITY_MSG.getValue());
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] updateInnerPotential(byte position, int skillid, int level, int rank) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ENABLE_INNER_ABILITY.getValue());
        mplew.write(1); //unlock
        mplew.write(1); //0 = no update
        mplew.writeShort(position); //1-3
        mplew.writeInt(skillid); //skill id (7000000+)
        mplew.writeShort(level); //level, 0 = blank inner ability
        mplew.writeShort(rank); //rank
        mplew.write(1); //0 = no update

        return mplew.getPacket();
    }

    public static byte[] innerPotentialResetMessage() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.INNER_ABILITY_RESET_MSG.getValue());
        mplew.write(HexTool.getByteArrayFromHexString("26 00 49 6E 6E 65 72 20 50 6F 74 65 6E 74 69 61 6C 20 68 61 73 20 62 65 65 6E 20 72 65 63 6F 6E 66 69 67 75 72 65 64 2E 01"));

        return mplew.getPacket();
    }

    public static byte[] updateHonour(int honourLevel, int honourExp, boolean levelup) {
        /*
         * data:
         * 03 00 00 00
         * 69 00 00 00
         * 01
         */
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_HONOUR.getValue());
        mplew.writeInt(honourExp);
        mplew.writeInt(honourLevel);
        //mplew.writeInt(honourExp);
        mplew.write(levelup ? 1 : 0); //shows level up effect

        return mplew.getPacket();
    }

    public static byte[] getCharInfo(MapleCharacter chr) {
        return setField(chr, true, null, 0);
    }

    public static byte[] getWarpToMap(MapleMap to, int spawnPoint, MapleCharacter chr) {
        return setField(chr, false, to, spawnPoint);
    }

    public static byte[] setField(MapleCharacter chr, boolean CharInfo, MapleMap to, int spawnPoint) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WARP_TO_MAP.getValue());
        mplew.writeInt(chr.getClient().getChannel() - 1);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.write(CharInfo ? 1 : 2);
        mplew.writeInt(0x63);
        mplew.writeInt(0x794);
        mplew.writeInt(0x14A);
        mplew.write(CharInfo);

        int v104 = 0;
        mplew.writeShort(v104); // size :: v104
        if (v104 != 0) {
            mplew.writeMapleAsciiString("");
            for (int i = 0; i < v104; i++) {
                mplew.writeMapleAsciiString("");
            }
        }

        if (CharInfo) {
            chr.CRand().connectData(mplew); // [Int][Int][Int] 
            PacketHelper.addCharacterInfo(mplew, chr);
            UnkFunction(mplew);
        } else {
            mplew.writeBoolean(false);
            mplew.writeInt(to.getId());
            mplew.write(spawnPoint);
            mplew.writeInt(chr.getStat().getHp());
            boolean v12 = false;
            mplew.writeBoolean(v12);
            if (v12) {
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
        }
        mplew.write(0);
        mplew.write(0);
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.writeInt(100);
        boolean v87 = false;
        mplew.writeBoolean(v87);
        if (v87) {
            mplew.writeInt(0);
            mplew.writeMapleAsciiString("");
            mplew.writeInt(0);
        }
        mplew.writeBoolean(false);
        mplew.write(0);
        mplew.write(GameConstants.isSeparatedSp(chr.getJob()) ? 1 : 0); // v109
        boolean v88 = false;
        mplew.writeBoolean(v88);
        if (v88) {
            mplew.writeInt(0);
        }
      //  if (false) {
    //        int v18 = 0;
    //        mplew.write(v18);
    //        for (int i = 0; i < v18; i++) {
     //           mplew.writeMapleAsciiString("");
      //      }

      //      if (chr.getMapId() == 910196000) {
        //        mplew.writeInt(0);
      //      }
     //   }

        UnkFunction2(mplew, 48140);

        boolean v116 = false;
        mplew.writeBoolean(v116);
        if (v116) { // Function
            mplew.writeInt(0);
            mplew.write(0);
            mplew.writeLong(0);
        }
        UnkFunction3(mplew);
        UnkFunction4(mplew);
        UnkFunction5(mplew);
        mplew.write(0x96);
        mplew.writeInt(0);
        int v5 = 0;
        mplew.writeInt(v5);
        for (int i = 0; i < v5; i++) {
            mplew.writeInt(0);
        }

        return mplew.getPacket();
    }

    public static void UnkFunction(final MaplePacketLittleEndianWriter mplew) {
        mplew.writeInt(0);
        for (int i = 0; i < 3; i++) {
            mplew.writeInt(0);
        }
    }

    public static void UnkFunction2(final MaplePacketLittleEndianWriter mplew, int v) {
        boolean result = false;
        mplew.writeBoolean(result);
        if (result) {
            mplew.writeInt(0);
            int v5 = 0;
            mplew.write(v5);
            if (v5 >= 9) {
                for (int i = 0; i <= 9; i++) {
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                }
            } else {
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            mplew.writeLong(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
    }

    public static void UnkFunction3(final MaplePacketLittleEndianWriter mplew) {
        int v12 = 0;
        mplew.writeInt(v12);
        for (int i = 0; i < v12; i++) {
            mplew.writeInt(0);
            mplew.writeMapleAsciiString("");
        }
    }

    public static void UnkFunction4(final MaplePacketLittleEndianWriter mplew) {
        mplew.write(0);
        mplew.writeInt(0);
    }

    public static void UnkFunction5(final MaplePacketLittleEndianWriter mplew) {
        mplew.writeInt(0);
    }

    public static byte[] removeBGLayer(boolean remove, int map, byte layer, int duration) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REMOVE_BG_LAYER.getValue());
        mplew.write(remove ? 1 : 0); //Boolean show or remove
        mplew.writeInt(map);
        mplew.write(layer); //Layer to show/remove
        mplew.writeInt(duration);

        return mplew.getPacket();
    }

    public static byte[] setMapObjectVisible(List<Pair<String, Byte>> objects) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SET_MAP_OBJECT_VISIBLE.getValue());
        mplew.write(objects.size());
        for (Pair<String, Byte> object : objects) {
            mplew.writeMapleAsciiString(object.getLeft());
            mplew.write(object.getRight());
        }

        return mplew.getPacket();
    }

    public static byte[] spawnFlags(List<Pair<String, Integer>> flags) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHANGE_BACKGROUND.getValue());
        mplew.write(flags == null ? 0 : flags.size());
        if (flags != null) {
            for (Pair f : flags) {
                mplew.writeMapleAsciiString((String) f.left);
                mplew.write(((Integer) f.right));
            }
        }

        return mplew.getPacket();
    }

    public static byte[] serverBlocked(int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVER_BLOCKED.getValue());
        mplew.write(type);

        return mplew.getPacket();
    }

    public static byte[] pvpBlocked(int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PARTY_BLOCKED.getValue());
        mplew.write(type);

        return mplew.getPacket();
    }

    public static byte[] showEquipEffect() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_EQUIP_EFFECT.getValue());

        return mplew.getPacket();
    }

    public static byte[] showEquipEffect(int team) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_EQUIP_EFFECT.getValue());
        mplew.writeShort(team);

        return mplew.getPacket();
    }

    public static byte[] multiChat(String name, String chattext, int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MULTICHAT.getValue());
        mplew.write(mode);
        mplew.writeMapleAsciiString(name);
        mplew.writeMapleAsciiString(chattext);

        return mplew.getPacket();
    }

    public static byte[] getFindReplyWithCS(String target, boolean buddy) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
        mplew.write(buddy ? 72 : 9);
        mplew.writeMapleAsciiString(target);
        mplew.write(2);
        mplew.writeInt(-1);

        return mplew.getPacket();
    }

    public static byte[] getWhisper(String sender, int channel, String text) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
        mplew.write(18);
        mplew.writeMapleAsciiString(sender);
        mplew.writeShort(channel - 1);
        mplew.writeMapleAsciiString(text);

        return mplew.getPacket();
    }

    public static byte[] getWhisperReply(String target, byte reply) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
        mplew.write(10);
        mplew.writeMapleAsciiString(target);
        mplew.write(reply);

        return mplew.getPacket();
    }

    public static byte[] getFindReplyWithMap(String target, int mapid, boolean buddy) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
        mplew.write(buddy ? 72 : 9);
        mplew.writeMapleAsciiString(target);
        mplew.write(3);//was1
        mplew.writeInt(0);//mapid);
//        mplew.writeZeroBytes(8);

        return mplew.getPacket();
    }

    public static byte[] getFindReply(String target, int channel, boolean buddy) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WHISPER.getValue());
        mplew.write(buddy ? 72 : 9);
        mplew.writeMapleAsciiString(target);
        mplew.write(3);
        mplew.writeInt(channel - 1);

        return mplew.getPacket();
    }

    public static byte[] showForeignDamageSkin(MapleCharacter chr, int skinid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_DAMAGE_SKIN.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(skinid);

        return mplew.getPacket();
    }

    public static byte[] MapEff(String path) {
        return environmentChange(path, 4);//was 3
    }

    public static byte[] MapNameDisplay(int mapid) {
        return environmentChange("maplemap/enter/" + mapid, 4);
    }

    public static byte[] Aran_Start() {
        return environmentChange("Aran/balloon", 4);
    }

public static byte[] musicChange(String song) {
        return environmentChange(song, 7);//was 6
    }

    public static byte[] showEffect(String effect) {
        return environmentChange(effect, 0x0D);//was 3
    }

    public static byte[] playSound(String sound) {
        return environmentChange(sound, 5);//was 4
    }

    public static byte[] showEnvironment(int mode, String env, int[] values) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(mode);
        switch (mode) {
            case 0:
                mplew.write(values[0]);
                mplew.writeInt(values[1]);
                mplew.writeInt(values[2]);
                break;
            case 1:
                mplew.write(values[0]);
                mplew.writeInt(values[1]);
                mplew.writeShort(values[2]);
                break;
            case 2:
            case 4:
            case 5: //播放音樂
            case 10:
            case 21:
                mplew.writeMapleAsciiString(env);
                break;
            case 6:
                mplew.writeInt(values[0]);
                mplew.writeInt(values[1]);
                mplew.writeInt(values[2]);
                mplew.write(values[3]);
                mplew.write(values[4]);
                break;
            case 8:
                mplew.writeInt(values[0]);
                mplew.writeInt(values[1]);
                mplew.writeInt(values[2]);
                break;
            case 3:
            case 24:
                mplew.writeMapleAsciiString(env);
                mplew.write(values[0]);
                break;
            case 7: // 更變背景音樂
            case 9:
            case 11:
            case 12:
            case 13:
                mplew.writeMapleAsciiString(env);
                mplew.writeInt(values[0]);
                break;
            case 14:
                mplew.writeMapleAsciiString(env);
                mplew.write(values[0]);
                mplew.write(values[1]);
                break;
            case 15://背景變暗
                mplew.write(values[0]);
                mplew.writeShort(values[1]);
                mplew.writeShort(values[2]);
                mplew.writeShort(values[3]);
                mplew.writeShort(values[4]);
                mplew.writeInt(values[5]);
                break;
            case 16:
                mplew.writeShort(values[0]);
                mplew.write(values[1]);
                break;
            case 17:
                mplew.write(values[0]);
                mplew.writeInt(values[1]);
                mplew.writeMapleAsciiString(env);
                break;
            case 18:
            case 20:
            case 23:
                mplew.writeInt(values[0]);
                break;
            case 19:
                mplew.writeInt(values[0]);
                mplew.writeInt(values[1]);
                mplew.writeInt(values[2]);
                mplew.write(values[3]);
                break;
            case 22:
                mplew.writeShort(values[0]);
                mplew.writeShort(values[1]);
                mplew.writeShort(values[2]);
                mplew.writeShort(values[3]);
                mplew.writeInt(values[4]);
                break;
        }

        return mplew.getPacket();
    }
    
    public static byte[] environmentChange(String env, int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(mode);
        mplew.writeMapleAsciiString(env);
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] trembleEffect(int type, int delay) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BOSS_ENV.getValue());
        mplew.write(1);
        mplew.write(type);
        mplew.writeInt(delay);
        mplew.writeShort(30);
        // mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] environmentMove(String env, int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MOVE_ENV.getValue());
        mplew.writeMapleAsciiString(env);
        mplew.writeInt(mode);

        return mplew.getPacket();
    }

    public static byte[] getUpdateEnvironment(MapleMap map) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_ENV.getValue());
        mplew.writeInt(map.getEnvironment().size());
        for (Map.Entry mp : map.getEnvironment().entrySet()) {
            mplew.writeMapleAsciiString((String) mp.getKey());
            mplew.writeInt(((Integer) mp.getValue()));
        }

        return mplew.getPacket();
    }

    public static byte[] startMapEffect(String msg, int itemid, boolean active) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MAP_EFFECT.getValue());
        mplew.write(active ? 0 : 1);
        mplew.writeInt(itemid);
        if (active) {
            mplew.writeMapleAsciiString(msg);
            mplew.write(0); // Boolean
        }
        return mplew.getPacket();
    }

    public static byte[] removeMapEffect() {
        return startMapEffect(null, 0, false);
    }

    public static byte[] getGMEffect(int value, int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GM_EFFECT.getValue());
        mplew.write(value);
        mplew.writeZeroBytes(17);

        return mplew.getPacket();
    }

    public static byte[] showOXQuiz(int questionSet, int questionId, boolean askQuestion) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.OX_QUIZ.getValue());
        mplew.write(askQuestion ? 1 : 0);
        mplew.write(questionSet);
        mplew.writeShort(questionId);

        return mplew.getPacket();
    }

    public static byte[] showEventInstructions() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GMEVENT_INSTRUCTIONS.getValue());
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] getPVPClock(int type, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
        mplew.write(3);
        mplew.write(type);
        mplew.writeInt(time);

        return mplew.getPacket();
    }

    public static byte[] getClock(int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
        mplew.write(2);
        mplew.writeInt(time);

        return mplew.getPacket();
    }

    public static byte[] getClockTime(int hour, int min, int sec) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CLOCK.getValue());
        mplew.write(1);
        mplew.write(hour);
        mplew.write(min);
        mplew.write(sec);

        return mplew.getPacket();
    }

    public static byte[] boatPacket(int effect, int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BOAT_MOVE.getValue());
        mplew.write(effect);
        mplew.write(mode);

        return mplew.getPacket();
    }

    public static byte[] setBoatState(int effect) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BOAT_STATE.getValue());
        mplew.write(effect);
        mplew.write(1);

        return mplew.getPacket();
    }

    public static byte[] stopClock() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.STOP_CLOCK.getValue());
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] showAriantScoreBoard() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ARIANT_SCOREBOARD.getValue());

        return mplew.getPacket();
    }

    public static byte[] sendPyramidUpdate(int amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PYRAMID_UPDATE.getValue());
        mplew.writeInt(amount);

        return mplew.getPacket();
    }

    public static byte[] sendPyramidResult(byte rank, int amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PYRAMID_RESULT.getValue());
        mplew.write(rank);
        mplew.writeInt(amount);

        return mplew.getPacket();
    }

    public static byte[] quickSlot(String skil) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.QUICK_SLOT.getValue());
        mplew.write(skil == null ? 0 : 1);
        if (skil != null) {
            String[] slots = skil.split(",");
            for (int i = 0; i < 28; i++) {
                mplew.writeInt(Integer.parseInt(slots[i]));
            }
        }

        return mplew.getPacket();
    }

    public static byte[] getMovingPlatforms(MapleMap map) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MOVE_PLATFORM.getValue());
        mplew.writeInt(map.getPlatforms().size());
        for (MapleNodes.MaplePlatform mp : map.getPlatforms()) {
            mplew.writeMapleAsciiString(mp.name);
            mplew.writeInt(mp.start);
            mplew.writeInt(mp.SN.size());
            for (Integer SN : mp.SN) {
                mplew.writeInt(SN);
            }
            mplew.writeInt(mp.speed);
            mplew.writeInt(mp.x1);
            mplew.writeInt(mp.x2);
            mplew.writeInt(mp.y1);
            mplew.writeInt(mp.y2);
            mplew.writeInt(mp.x1);
            mplew.writeInt(mp.y1);
            mplew.write(mp.r);
            mplew.write(0);
        }

        return mplew.getPacket();
    }

    public static byte[] sendPyramidKills(int amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PYRAMID_KILL_COUNT.getValue());
        mplew.writeInt(amount);

        return mplew.getPacket();
    }

    public static byte[] sendPVPMaps() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_INFO.getValue());
        mplew.write(3); //max amount of players
        for (int i = 0; i < 3; i++) {
            mplew.writeInt(10); //how many peoples in each map
            mplew.writeZeroBytes(120);
        }
        mplew.writeShort(150); // 經驗值加倍活動(1.5倍)
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] gainForce(MapleCharacter chr, int oid, int count, int color) {
        List<Integer> mobid = new ArrayList<>();
        mobid.add(oid);

        List<Pair<Integer, Integer>> forceinfo = new ArrayList<>();
        forceinfo.add(new Pair<>(count, color));
        forceinfo.add(new Pair<>(count, color));
        forceinfo.add(new Pair<>(count, color));
        forceinfo.add(new Pair<>(count, color));

        return gainForce(true, chr, mobid, 0, 0, forceinfo);
    }

    public static byte[] gainForce(boolean isRemote, MapleCharacter chr, List<Integer> oid, int type, int skillid, List<Pair<Integer, Integer>> forceInfo) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GAIN_FORCE.getValue());

        mplew.write(isRemote);
        if (isRemote) {
            mplew.writeInt(chr.getId());
        }
        mplew.writeInt(isRemote ? oid.get(0) : chr.getId());
        mplew.writeInt(type); //unk

        if (!(type == 0 || type == 9 || type == 14)) {
            mplew.write(1);
            if (GameConstants.isSpecialForce(type)) {
                mplew.writeInt(oid.size()); // size
                for (int i = 0; i < oid.size(); i++) {
                    mplew.writeInt(oid.get(i));
                }
            } else {
                mplew.writeInt(oid.get(0));
            }
            mplew.writeInt(skillid); //skillid
        }

        for (Pair<Integer, Integer> info : forceInfo) {
            mplew.write(1); // while on/off
            mplew.writeInt(info.left); // count
            mplew.writeInt(info.right); // color
            mplew.writeInt(Randomizer.rand(15, 29));
            mplew.writeInt(Randomizer.rand(5, 6));
            mplew.writeInt(Randomizer.rand(35, 50));
            mplew.writeInt((skillid / 1000000) == 14 ? 1 : 0); // 0
            mplew.writeInt(0); // 0
            mplew.writeInt(0); // 0
            mplew.writeInt((skillid / 1000000) == 14 ? 51399013 : 0); // 0
            mplew.writeInt(0); // 0
        }

        mplew.write(0); // where read??

        if (type == 11) {
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        if (type == 9 || type == 15) {
            mplew.writeInt(0x1E3);
            mplew.writeInt(-106);
            mplew.writeInt(0x1F7);
            mplew.writeInt(-86);
        }
        if (type == 16) {
            mplew.writeInt(543);
            mplew.writeInt(-325);
        }
        if (type == 17) {
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        if (type == 18) {
            mplew.writeInt(0);
            mplew.writeInt(0);
        }

        return mplew.getPacket();
    }

    public static byte[] achievementRatio(int amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ACHIEVEMENT_RATIO.getValue());
        mplew.writeInt(amount);

        return mplew.getPacket();
    }

    public static byte[] getQuickMoveInfo(boolean show, List<QuickMoveNPC> qm) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.QUICK_MOVE.getValue());
        mplew.write(qm.size() <= 0 ? 0 : show ? qm.size() : 0);
        if (show && qm.size() > 0) {
            for (QuickMoveNPC qmn : qm) {
                mplew.writeInt(0);
                mplew.writeInt(qmn.getId());
                mplew.writeInt(qmn.getType());
                mplew.writeInt(qmn.getLevel());
                mplew.writeMapleAsciiString(qmn.getDescription());
                mplew.writeLong(PacketHelper.getTime(-2));
                mplew.writeLong(PacketHelper.getTime(-1));
            }
        }

        return mplew.getPacket();
    }

    public static byte[] spawnPlayerMapobject(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_PLAYER.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(chr.getLevel());
        mplew.writeMapleAsciiString(chr.getName());
        MapleQuestStatus ultExplorer = chr.getQuestNoAdd(MapleQuest.getInstance(111111));
        //if ((ultExplorer != null) && (ultExplorer.getCustomData() != null)) {
        //    mplew.writeMapleAsciiString(ultExplorer.getCustomData());
        //} else {
        //    mplew.writeMapleAsciiString("");
        //}
        mplew.writeMapleAsciiString((ultExplorer != null) && (ultExplorer.getCustomData() != null) ? ultExplorer.getCustomData() : "");
        if (chr.getGuildId() <= 0) {
            mplew.writeZeroBytes(8);
        } else {
            MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
            if (gs != null) {
                mplew.writeMapleAsciiString(gs.getName());
                mplew.writeShort(gs.getLogoBG());
                mplew.write(gs.getLogoBGColor());
                mplew.writeShort(gs.getLogo());
                mplew.write(gs.getLogoColor());
            } else {
                mplew.writeZeroBytes(8);
            }
        }
        mplew.write(0);  
        mplew.writeInt(0);
        mplew.writeInt(0);//179Change
        /////////////////////// for BuffStat

        PacketHelper.addSpawnPlayerBuffStat(mplew, chr);

        int CHAR_MAGIC_SPAWN = Randomizer.nextInt();

        mplew.writeZeroBytes(8);   
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//1

        mplew.writeZeroBytes(8);
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//2

        mplew.writeZeroBytes(10);
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//3

        mplew.writeShort(0);
        int buffSrc = chr.getBuffSource(MapleBuffStat.MONSTER_RIDING);
        if (buffSrc > 0) {
            Item c_mount = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -118);
            Item mount = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -18);
            if ((GameConstants.getMountItem(buffSrc, chr) == 0) && (c_mount != null) && (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -119) != null)) {
                mplew.writeInt(c_mount.getItemId());
            } else if ((GameConstants.getMountItem(buffSrc, chr) == 0) && (mount != null) && (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -19) != null)) {
                mplew.writeInt(mount.getItemId());
            } else {
                mplew.writeInt(GameConstants.getMountItem(buffSrc, chr));
            }
            mplew.writeInt(buffSrc);
        } else {
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//4
        mplew.writeLong(0L);
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//5
        mplew.write(0);//177Change
        mplew.writeInt(Randomizer.nextInt());
        mplew.writeZeroBytes(10);
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//6
        mplew.writeZeroBytes(16);
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//7
        mplew.writeZeroBytes(10);
        mplew.write(1);
        mplew.writeInt(CHAR_MAGIC_SPAWN);//8
        
        ///////////////////////// End for BuffStat
        mplew.writeShort(chr.getJob());
        mplew.writeShort(chr.getSubcategory());
        mplew.writeInt(0);//[33 01 00 00]
        PacketHelper.addCharLook(mplew, chr, true, false);
        if (MapleJob.is神之子(chr.getJob())) {
            PacketHelper.addCharLook(mplew, chr, true, true);   //第四个参数应该和上一个反过来
        }

        PacketHelper.UnkFunctin6(mplew);  

        mplew.writeInt(0);
        mplew.writeInt(0);
        
        if ((chr.getBuffedValue(MapleBuffStat.SOARING) != null) && (buffSrc > 0)) {//妮娜的魔法阵 1C 7B 1D 00 //5C 58 8A 00
            addMountId(mplew, chr, buffSrc);
            mplew.writeInt(chr.getId());
            mplew.writeInt(0);   
        } else {
            mplew.writeInt(0);
            mplew.writeInt(0);
            int size = 0;
            mplew.writeInt(size);
            for (int i = 0; i < size; i++) {  //可能多余
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
        }

        mplew.writeInt(Math.min(250, chr.getInventory(MapleInventoryType.CASH).countById(5110000))); //Valentine Effect
        mplew.writeInt(chr.getItemEffect());//[76 72 4C 00] - 撥水柱特效
        mplew.writeInt(chr.getTitleEffect());//[51 75 38 00] - 與風暴同在
        mplew.writeInt(chr.getDamageSkin());//數據1：[0C 00 00 00] 數據2：[09 04 00 00]傷害字型
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeShort(-1);
        mplew.writeMapleAsciiString("");
        mplew.writeMapleAsciiString("");
        mplew.writeShort(-1);
        mplew.writeShort(-1);
        mplew.write(0);
        mplew.writeInt(GameConstants.getInventoryType(chr.getChair()) == MapleInventoryType.SETUP ? chr.getChair() : 0);
        String text = "";
        mplew.writeInt(text.length()); // 椅子文字長度
        if (text.length() > 0) {
            mplew.writeMapleAsciiString(text); // 椅子文字
        }
        mplew.writeInt(0); //new v143
        mplew.writePos(chr.getTruePosition());   //?
        mplew.write(chr.getStance());    //?
        mplew.writeShort(chr.getFH());   //?

        mplew.write(chr.getPets().size() > 0); // 寵物數量
        for (MaplePet pet : chr.getPets()) {
            if (pet.getSummoned()) {
                mplew.writeInt(chr.getPetIndex(pet));
                PetPacket.addPetInfo(mplew, chr, pet, false);
            }
        }
        
        mplew.write(chr.getHaku() != null && MapleJob.is陰陽師(chr.getJob()));
        if (chr.getHaku() != null && MapleJob.is陰陽師(chr.getJob())) {
            MapleHaku haku = chr.getHaku();
            mplew.writeInt(haku.getObjectId());
            mplew.writeInt(40020109);
            mplew.write(1);
            mplew.writePos(haku.getPosition());
            mplew.write(0);
            mplew.writeShort(haku.getStance());
        }
        mplew.writeInt(chr.getMount() != null ? chr.getMount().getLevel() : 1); // 骑宠等级 默认是1级
        mplew.writeInt(chr.getMount() != null ? chr.getMount().getExp() : 0);
        mplew.writeInt(chr.getMount() != null ? chr.getMount().getFatigue() : 0);
        //mplew.writeInt(chr.getMount().getLevel());
        //mplew.writeInt(chr.getMount().getExp());
        //mplew.writeInt(chr.getMount().getFatigue());

        PacketHelper.addAnnounceBox(mplew, chr);
        mplew.write((chr.getChalkboard() != null) && (chr.getChalkboard().length() > 0) ? 1 : 0);
        if ((chr.getChalkboard() != null) && (chr.getChalkboard().length() > 0)) {
            mplew.writeMapleAsciiString(chr.getChalkboard());
        }

        Triple rings = chr.getRings(false);
        addRingInfo(mplew, (List) rings.getLeft());
        addRingInfo(mplew, (List) rings.getMid());
        addMRingInfo(mplew, (List) rings.getRight(), chr);
        int v65 = 0;
        mplew.write(v65);
        for (int o = 0; o < v65; o++) {
            mplew.writeInt(0);
        }
        int unk_mask = 0;
        mplew.writeShort(chr.getStat().Berserk ? 1 : 0); //unk_mask   write改为writeShort
        if ((unk_mask & 1) != 0) {
        }
        if ((unk_mask & 2) != 0) {
        }
        if ((unk_mask & 8) != 0) {
            mplew.writeInt(0);
        }
        if ((unk_mask & 10) != 0) {
            mplew.writeInt(0);
        }
        if ((unk_mask & 20) != 0) {
            mplew.writeInt(0);
        }
        //mplew.writeInt(chr.getMount().getItemId());//骑宠id
        
        if (MapleJob.is凱撒(chr.getJob())) {
            String x = chr.getOneInfo(12860, "extern");
            mplew.writeInt(x == null ? 0 : Integer.parseInt(x));
            x = chr.getOneInfo(12860, "inner");
            mplew.writeInt(x == null ? 0 : Integer.parseInt(x));
            x = chr.getOneInfo(12860, "premium");
            mplew.write(x == null ? 0 : Integer.parseInt(x));
        }

        mplew.write(0);    
        mplew.writeInt(0);

        //PacketHelper.addFarmInfo(mplew, chr.getClient(), 0);
        for (int i = 0; i < 5; i++) {
            mplew.write(-1);
        }
        int v84 = 0;
        mplew.writeInt(v84);
        if (v84 > 0) {
            mplew.writeMapleAsciiString("");
        }
        mplew.write(1);
        if (false) {    //这儿完全不会被执行，我很奇怪为什么要这么写
            int v87 = 0;
            mplew.writeInt(v87);
            for (int i = 0; i < v87; i++) {
                mplew.writeInt(0);
            }
        }
        boolean v90 = false;
        mplew.write(v90);
        if (v90) {          //这儿完全不会被执行，因为v90值为false，我很奇怪为什么要这么写
            boolean v91 = false;
            mplew.write(v91);
            if (v91) {
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeShort(0);
                mplew.writeShort(0);
            }
        }

        mplew.write(0); // for      // 34 "00"
        mplew.writeInt(0); // for
        mplew.writeInt(0); // for

        mplew.write(0);
        mplew.writeInt(0);
        mplew.writeInt(0);

        mplew.writeInt(0);

        mplew.writeInt(0);
        mplew.writeInt(0);

        mplew.writeInt(0); 
        
        return mplew.getPacket();
    }

    public static byte[] removePlayerFromMap(int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REMOVE_PLAYER_FROM_MAP.getValue());
        mplew.writeInt(cid);

        return mplew.getPacket();
    }

    public static byte[] getChatText(int cidfrom, String text, boolean whiteBG, int show) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHATTEXT.getValue());
        mplew.writeInt(cidfrom);
        mplew.write(whiteBG ? 1 : 0);
        mplew.writeMapleAsciiString(text);
        mplew.write(show);
        mplew.write(0);
        mplew.write(-1);

        return mplew.getPacket();
    }

    public static byte[] getEffectSwitch(int cid, List<Integer> items) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.EFFECT_SWITCH.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(items.size());
        for (int i : items) {
            mplew.writeInt(i);
        }
        mplew.write(1);

        return mplew.getPacket();
    }

    public static byte[] getScrollEffect(int chr, int scroll, int toScroll) {
        return getScrollEffect(chr, Equip.ScrollResult.SUCCESS, false, false, scroll, toScroll);
    }

    public static byte[] getScrollEffect(int chr, Equip.ScrollResult scrollSuccess, boolean legendarySpirit, boolean whiteScroll, int scroll, int toScroll) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_SCROLL_EFFECT.getValue());
        mplew.writeInt(chr);
        mplew.write(scrollSuccess == Equip.ScrollResult.SUCCESS ? 1 : scrollSuccess == Equip.ScrollResult.CURSE ? 2 : 0);
        mplew.write(legendarySpirit ? 1 : 0);
        mplew.writeInt(scroll);
        mplew.writeInt(toScroll);
        mplew.write(whiteScroll ? 1 : 0);
        mplew.write(0);//?

        return mplew.getPacket();
    }

    public static byte[] showEnchanterEffect(int cid, byte result) {
        tools.data.MaplePacketLittleEndianWriter mplew = new tools.data.MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_ENCHANTER_EFFECT.getValue());
        mplew.writeInt(cid);
        mplew.write(result);

        return mplew.getPacket();
    }

    public static byte[] showSoulScrollEffect(int cid, byte result, boolean destroyed) {
        tools.data.MaplePacketLittleEndianWriter mplew = new tools.data.MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_SOULSCROLL_EFFECT.getValue());
        mplew.writeInt(cid);
        mplew.write(result);
        mplew.write(destroyed ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] showMagnifyingEffect(int chr, short pos, boolean bonusPot) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_MAGNIFYING_EFFECT.getValue());
        mplew.writeInt(chr);
        mplew.writeShort(pos);
        mplew.write(bonusPot ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] showPotentialReset(int chrId, boolean success, int itemid, boolean bonus) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(bonus ? SendPacketOpcode.SHOW_BONUS_POTENTIAL_RESET.getValue() : SendPacketOpcode.SHOW_POTENTIAL_RESET.getValue());
        mplew.writeInt(chrId);
        mplew.write(success ? 1 : 0);
        mplew.writeInt(itemid);

        return mplew.getPacket();
    }

    public static byte[] showPotentialEx(int chrId, boolean success, int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_POTENTIAL_EXPANSION.getValue());
        mplew.writeInt(chrId);
        mplew.write(success);
        mplew.writeInt(itemid);

        return mplew.getPacket();
    }

    public static byte[] showBonusPotentialEx(int chrId, boolean success, int itemid, boolean broken) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_FIREWORKS_EFFECT.getValue());
        mplew.writeInt(chrId);
        mplew.write(success);
        mplew.writeInt(itemid);
        mplew.write(broken);

        return mplew.getPacket();
    }

    public static byte[] getTaiwanCube(int cubeId, byte op, int[] value, long[] value2, Item item, List<Integer> selects) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (cubeId == 5062020) { // 閃炫方塊
            mplew.writeShort(SendPacketOpcode.SHIMMER_CUBE_RESPONSE.getValue());
        } else {
            mplew.writeShort(SendPacketOpcode.FLASH_CUBE_RESPONSE.getValue());
        }

        int pointer;
        switch (cubeId) {
            case 3994895: // 楓方塊 [完成-182]
                pointer = -1043023352;
                break;
            case 5062017: // 閃耀方塊 [完成-182]
                pointer = -803375447;
                break;
            case 5062019: // 閃耀鏡射方塊 [完成-182]
                pointer = 1805734203;
                break;
            case 5062020: // 閃炫方塊 [完成-182]
                if (op == 7) { // 顯示洗出的潛能
                    pointer = 1166928371;
                } else { // 選擇潛能
                    pointer = -437621832;
                }
                break;
            case 5062021: // 新對等方塊
            default:
                pointer = 0;
                System.err.println("未知指針值的台方塊：" + cubeId);
        }

        mplew.writeInt(pointer);
        mplew.write(op);
        mplew.writeInt(value[0]);//楓方塊：「0 - 更新價格; 1 - 詢問是否洗道具; 2 - 重新加載道具潛能 ; 大於3 - 取下道具」
        switch (op) {
            case 3:
                if (item != null) { // 閃耀顯示洗後結果
                    PacketHelper.addItemInfo(mplew, item);
                } else if (value2 != null) { // 楓方塊顯示消耗楓幣
                    mplew.writeLong(value2[0]);
                }
                break;
            case 7: // 閃耀顯示洗後結果
                mplew.writeInt(value[1]);
                mplew.writeInt(selects.size());
                selects.forEach((i) -> mplew.writeInt(i));
                break;
        }

        return mplew.getPacket();
    }

    public static byte[] showMapleCubeCost(int value, long cost) {
        return getTaiwanCube(3994895, (byte) 3, new int[]{value}, new long[]{cost}, null, null);
    }

    public static byte[] showFlashCubeEquip(Item item) {
        return getTaiwanCube(5062017, (byte) 3, new int[]{0}, null, item, null);
    }

    public static byte[] getFlashCubeRespons(int itemId, int value) {
        return getTaiwanCube(itemId, (byte) 1, new int[]{value}, null, null, null);
    }

    public static byte[] getShimmerCubeRespons() {
        return getTaiwanCube(5062020, (byte) 1, new int[]{0}, null, null, null);
    }

    public static byte[] getShimmerCubeRespons(int line, List<Integer> selects) {
        return getTaiwanCube(5062020, (byte) 7, new int[]{0, line}, null, null, selects);
    }

    public static byte[] showNebuliteEffect(int chr, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_NEBULITE_EFFECT.getValue());
        mplew.writeInt(chr);
        mplew.write(success ? 1 : 0);
        mplew.writeMapleAsciiString(success ? "Successfully mounted Nebulite." : "Failed to mount Nebulite.");

        return mplew.getPacket();
    }

    public static byte[] useNebuliteFusion(int cid, int itemId, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_FUSION_EFFECT.getValue());
        mplew.writeInt(cid);
        mplew.write(success ? 1 : 0);
        mplew.writeInt(itemId);

        return mplew.getPacket();
    }

    public static byte[] sendKaiserQuickKey(int[] skills) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.KAISER_QUICK_KEY.getValue());
        for (int i = 0; i < 3; i++) {
            if (skills[i] != 0) {
                mplew.write(true);
                mplew.write(i);
                mplew.writeInt(skills[i]);
                int x = 0;
                mplew.write(x);
                if (x != 0) {
                    mplew.write(0);
                    mplew.writeInt(0);
                }
            }
        }

        return mplew.getPacket();
    }

    public static byte[] pvpAttack(int cid, int playerLevel, int skill, int skillLevel, int speed, int mastery, int projectile, int attackCount, int chargeTime, int stance, int direction, int range, int linkSkill, int linkSkillLevel, boolean movementSkill, boolean pushTarget, boolean pullTarget, List<AttackPair> attack) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_ATTACK.getValue());
        mplew.writeInt(cid);
        mplew.write(playerLevel);
        mplew.writeInt(skill);
        mplew.write(skillLevel);
        mplew.writeInt(linkSkill != skill ? linkSkill : 0);
        mplew.write(linkSkillLevel != skillLevel ? linkSkillLevel : 0);
        mplew.write(direction);
        mplew.write(movementSkill ? 1 : 0);
        mplew.write(pushTarget ? 1 : 0);
        mplew.write(pullTarget ? 1 : 0);
        mplew.write(0);
        mplew.writeShort(stance);
        mplew.write(speed);
        mplew.write(mastery);
        mplew.writeInt(projectile);
        mplew.writeInt(chargeTime);
        mplew.writeInt(range);
        mplew.write(attack.size());
        mplew.write(0);
        mplew.writeInt(0);
        mplew.write(attackCount);
        mplew.write(0);
        mplew.write(0);
        for (AttackPair p : attack) {
            mplew.writeInt(p.objectid);
            mplew.writeInt(0);
            mplew.writePos(p.point);
            mplew.write(0);
            mplew.writeInt(0);
            for (Pair atk : p.attack) {
                mplew.writeInt(((Integer) atk.left));
                mplew.writeInt(0);
                mplew.write(((Boolean) atk.right) ? 1 : 0);
                mplew.writeShort(0);
            }
        }

        return mplew.getPacket();
    }

    public static byte[] getPVPMist(int cid, int mistSkill, int mistLevel, int damage) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_MIST.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(mistSkill);
        mplew.write(mistLevel);
        mplew.writeInt(damage);
        mplew.write(8);
        mplew.writeInt(1000);

        return mplew.getPacket();
    }

    public static byte[] pvpCool(int cid, List<Integer> attack) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_COOL.getValue());
        mplew.writeInt(cid);
        mplew.write(attack.size());
        for (Iterator i$ = attack.iterator(); i$.hasNext();) {
            int b = ((Integer) i$.next());
            mplew.writeInt(b);
        }

        return mplew.getPacket();
    }

    public static byte[] teslaTriangle(int cid, int sum1, int sum2, int sum3) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TESLA_TRIANGLE.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(sum1);
        mplew.writeInt(sum2);
        mplew.writeInt(sum3);

        return mplew.getPacket();
    }

    public static byte[] followEffect(int initiator, int replier, Point toMap) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FOLLOW_EFFECT.getValue());
        mplew.writeInt(initiator);
        mplew.writeInt(replier);
        mplew.writeLong(0);
        if (replier == 0) {
            mplew.write(toMap == null ? 0 : 1);
            if (toMap != null) {
                mplew.writeInt(toMap.x);
                mplew.writeInt(toMap.y);
            }
        }

        return mplew.getPacket();
    }

    public static byte[] showPQReward(int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_PQ_REWARD.getValue());
        mplew.writeInt(cid);
        for (int i = 0; i < 6; i++) {
            mplew.write(0);
        }

        return mplew.getPacket();
    }

    public static byte[] craftMake(int cid, int something, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CRAFT_EFFECT.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(something);
        mplew.writeInt(time);

        return mplew.getPacket();
    }

    public static byte[] craftFinished(int cid, int craftID, int ranking, int itemId, int quantity, int exp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CRAFT_COMPLETE.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(craftID);
        mplew.writeInt(ranking);
        mplew.writeInt(itemId);
        mplew.writeInt(quantity);
        mplew.writeInt(exp);

        return mplew.getPacket();
    }

    public static byte[] harvestResult(int cid, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.HARVESTED.getValue());
        mplew.writeInt(cid);
        mplew.write(success ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] playerDamaged(int cid, int dmg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PLAYER_DAMAGED.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(dmg);

        return mplew.getPacket();
    }

    public static byte[] showPyramidEffect(int chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.NETT_PYRAMID.getValue());
        mplew.writeInt(chr);
        mplew.write(1);
        mplew.writeInt(0);
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] pamsSongEffect(int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PAMS_SONG.getValue());
        mplew.writeInt(cid);
        return mplew.getPacket();
    }

    public static byte[] spawnHaku_change0(int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.HAKU_CHANGE_0.getValue());
        mplew.writeInt(cid);

        return mplew.getPacket();
    }

    public static byte[] spawnHaku_change1(MapleHaku d) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.HAKU_CHANGE_1.getValue());
        mplew.writeInt(d.getOwner());
        mplew.writePos(d.getPosition());
        mplew.write(d.getStance());
        mplew.writeShort(0);
        mplew.write(0);
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] spawnHaku_bianshen(int cid, int oid, boolean change) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.HAKU_CHANGE.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(oid);
        mplew.write(change ? 2 : 1);

        return mplew.getPacket();
    }

    public static byte[] hakuUnk(int cid, int oid, boolean change) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.HAKU_CHANGE.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(oid);
        mplew.write(0);
        mplew.write(0);
        mplew.writeMapleAsciiString("lol");

        return mplew.getPacket();
    }

    public static byte[] spawnHaku(MapleHaku d) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_HAKU.getValue());
        mplew.writeInt(d.getOwner());
        mplew.writeInt(d.getObjectId());
        mplew.writeInt(40020109);
        mplew.write(1);
        mplew.writePos(d.getPosition());
        mplew.write(0);
        mplew.writeShort(d.getStance());

        return mplew.getPacket();
    }

    public static byte[] moveHaku(int cid, int oid, Point pos, List<LifeMovementFragment> res) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.HAKU_MOVE.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(oid);
        mplew.writeInt(0);
        mplew.writePos(pos);
        mplew.writeInt(0);
        PacketHelper.serializeMovementList(mplew, res);
        return mplew.getPacket();
    }

    public static byte[] spawnDragon(MapleDragon d) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DRAGON_SPAWN.getValue());
        mplew.writeInt(d.getOwner());
        mplew.writeInt(d.getPosition().x);
        mplew.writeInt(d.getPosition().y);
        mplew.write(d.getStance());
        mplew.writeShort(0);
        mplew.writeShort(d.getJobId());

        return mplew.getPacket();
    }

    public static byte[] removeDragon(int chrid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DRAGON_REMOVE.getValue());
        mplew.writeInt(chrid);

        return mplew.getPacket();
    }

    public static byte[] moveDragon(MapleDragon d, Point startPos, List<LifeMovementFragment> moves) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DRAGON_MOVE.getValue());
        mplew.writeInt(d.getOwner());
        mplew.writeInt(0);
        mplew.writePos(startPos);
        mplew.writeInt(0);
        PacketHelper.serializeMovementList(mplew, moves);

        return mplew.getPacket();
    }

    public static byte[] spawnAndroid(MapleCharacter cid, MapleAndroid android) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ANDROID_SPAWN.getValue());
        mplew.writeInt(cid.getId());
        mplew.write(android.getType());
        mplew.writePos(android.getPos());
        mplew.write(android.getStance());
        mplew.writeShort(0);
        mplew.writeShort(android.getSkin() >= 2000 ? android.getSkin() - 2000 : android.getSkin());
        mplew.writeShort(android.getHair() - 30000);
        mplew.writeShort(android.getFace() - 20000);
        mplew.writeMapleAsciiString(android.getName());
        for (short i = -1200; i > -1208; i--) {
            Equip eq = (Equip) cid.getInventory(MapleInventoryType.EQUIPPED).getItem(i);
            mplew.writeInt(eq != null ? eq.getItemId() : 0);
            mplew.writeInt(eq != null ? eq.getFusionAnvil() : 0);
        }

        return mplew.getPacket();
    }

    public static byte[] moveAndroid(int cid, Point pos, List<LifeMovementFragment> res) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.ANDROID_MOVE.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(0);
        mplew.writePos(pos);
        mplew.writeInt(0);
        PacketHelper.serializeMovementList(mplew, res);

        return mplew.getPacket();
    }

    public static byte[] showAndroidEmotion(int cid, byte emo1, byte emo2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        // Packet: 97 DB 00 00 04 E7 FD C4 FF 05 00 00 03 00 3A 1C 52 04 07 00 41 6E 64 72 6F 69 64 85 4D 0F 00 00 00 00 00 00 00 00 00 BF 09 10
        // and more 63 zero bytes
        mplew.writeShort(SendPacketOpcode.ANDROID_EMOTION.getValue());
        mplew.writeInt(cid);
        mplew.write(emo1);
        mplew.write(emo2);

        return mplew.getPacket();
    }

    public static byte[] updateAndroidEquip(MapleCharacter chr, short position) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ANDROID_UPDATE.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(position + 1201);
        Equip eq = (Equip) chr.getInventory(MapleInventoryType.EQUIPPED).getItem(position);
        mplew.writeInt(eq != null ? eq.getItemId() : 0);
        mplew.writeInt(eq != null ? eq.getFusionAnvil() : 0);
        mplew.write(0);//178+ 作用未知

        return mplew.getPacket();
    }

    public static byte[] updateAndroidLook(MapleCharacter cid, MapleAndroid android) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ANDROID_UPDATE.getValue());
        mplew.writeInt(cid.getId());
        mplew.write(0);
        mplew.writeShort(android.getSkin() >= 2000 ? android.getSkin() - 2000 : android.getSkin());
        mplew.writeShort(android.getHair() - 30000);
        mplew.writeShort(android.getFace() - 20000);
        mplew.writeMapleAsciiString(android.getName());

        return mplew.getPacket();
    }

    public static byte[] deactivateAndroid(int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ANDROID_DEACTIVATED.getValue());
        mplew.writeInt(cid);

        return mplew.getPacket();
    }

    public static byte[] removeAndroidHeart() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
        mplew.write(0x14);

        return mplew.getPacket();
    }

    public static byte[] removeFamiliar(int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_FAMILIAR.getValue());
        mplew.writeInt(cid);
        mplew.writeShort(0);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] spawnFamiliar(MonsterFamiliar mf, boolean spawn, boolean respawn) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(respawn ? SendPacketOpcode.SPAWN_FAMILIAR_2.getValue() : SendPacketOpcode.SPAWN_FAMILIAR.getValue());
        mplew.writeInt(mf.getCharacterId());
        mplew.write(spawn ? 1 : 0);
        mplew.write(respawn ? 1 : 0);
        mplew.write(0);
        if (spawn) {
            mplew.writeInt(mf.getFamiliar());
            mplew.writeInt(mf.getFatigue());
            mplew.writeInt(mf.getVitality() * 300); //max fatigue
            mplew.writeMapleAsciiString(mf.getName());
            mplew.writePos(mf.getTruePosition());
            mplew.write(mf.getStance());
            mplew.writeShort(mf.getFh());
        }

        return mplew.getPacket();
    }

    public static byte[] moveFamiliar(int cid, Point startPos, List<LifeMovementFragment> moves) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MOVE_FAMILIAR.getValue());
        mplew.writeInt(cid);
        mplew.write(0);
        mplew.writePos(startPos);
        mplew.writeInt(0);
        PacketHelper.serializeMovementList(mplew, moves);

        return mplew.getPacket();
    }

    public static byte[] touchFamiliar(int cid, byte unk, int objectid, int type, int delay, int damage) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TOUCH_FAMILIAR.getValue());
        mplew.writeInt(cid);
        mplew.write(0);
        mplew.write(unk);
        mplew.writeInt(objectid);
        mplew.writeInt(type);
        mplew.writeInt(delay);
        mplew.writeInt(damage);

        return mplew.getPacket();
    }

    public static byte[] familiarAttack(int cid, byte unk, List<Triple<Integer, Integer, List<Integer>>> attackPair) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ATTACK_FAMILIAR.getValue());
        mplew.writeInt(cid);
        mplew.write(0);// familiar id?
        mplew.write(unk);
        mplew.write(attackPair.size());
        for (Triple<Integer, Integer, List<Integer>> s : attackPair) {
            mplew.writeInt(s.left);
            mplew.write(s.mid);
            mplew.write(s.right.size());
            for (int damage : s.right) {
                mplew.writeInt(damage);
            }
        }

        return mplew.getPacket();
    }

    public static byte[] renameFamiliar(MonsterFamiliar mf) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.RENAME_FAMILIAR.getValue());
        mplew.writeInt(mf.getCharacterId());
        mplew.write(0);
        mplew.writeInt(mf.getFamiliar());
        mplew.writeMapleAsciiString(mf.getName());

        return mplew.getPacket();
    }

    public static byte[] updateFamiliar(MonsterFamiliar mf) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_FAMILIAR.getValue());
        mplew.writeInt(mf.getCharacterId());
        mplew.writeInt(mf.getFamiliar());
        mplew.writeInt(mf.getFatigue());
        mplew.writeLong(PacketHelper.getTime(mf.getVitality() >= 3 ? System.currentTimeMillis() : -2L));

        return mplew.getPacket();
    }

    public static byte[] movePlayer(int cid, List<LifeMovementFragment> moves, Point startPos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MOVE_PLAYER.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(0);
        mplew.writePos(startPos);
        mplew.writeShort(0);
        mplew.writeShort(0);
        PacketHelper.serializeMovementList(mplew, moves);

        return mplew.getPacket();
    }

    public static byte[] closeRangeAttack(MapleCharacter cid, int tbyte, int skill, int level, int display, byte speed, List<AttackPair> damage, boolean energy, int lvl, byte mastery, byte unk, int charge) {
        return addAttackInfo(energy ? 4 : 0, cid, tbyte, skill, level, display, speed, damage, lvl, mastery, unk, 0, null, 0, false);
    }

    public static byte[] rangedAttack(MapleCharacter cid, byte tbyte, int skill, int level, int display, byte speed, int itemid, List<AttackPair> damage, Point pos, int lvl, byte mastery, byte unk) {
        return addAttackInfo(1, cid, tbyte, skill, level, display, speed, damage, lvl, mastery, unk, itemid, pos, 0, true);
    }

    public static byte[] strafeAttack(MapleCharacter cid, byte tbyte, int skill, int level, int display, byte speed, int itemid, List<AttackPair> damage, Point pos, int lvl, byte mastery, byte unk, int ultLevel) {
        return addAttackInfo(2, cid, tbyte, skill, level, display, speed, damage, lvl, mastery, unk, itemid, pos, ultLevel, true);
    }

    public static byte[] magicAttack(MapleCharacter cid, int tbyte, int skill, int level, int display, byte speed, List<AttackPair> damage, int charge, int lvl, byte unk) {
        return addAttackInfo(3, cid, tbyte, skill, level, display, speed, damage, lvl, (byte) 0, unk, charge, null, 0, false);
    }

    public static byte[] addAttackInfo(int type, MapleCharacter cid, int tbyte, int skill, int level, int display, byte speed, List<AttackPair> damage, int lvl, byte mastery, byte unk, int charge, Point pos, int ultLevel, boolean RangedAttack) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (type == 0) {
            mplew.writeShort(SendPacketOpcode.CLOSE_RANGE_ATTACK.getValue());
        } else if (type == 1 || type == 2) {
            mplew.writeShort(SendPacketOpcode.RANGED_ATTACK.getValue());
        } else if (type == 3) {
            mplew.writeShort(SendPacketOpcode.MAGIC_ATTACK.getValue());
        } else {
            mplew.writeShort(SendPacketOpcode.ENERGY_ATTACK.getValue());
        }

        addAttackBody(mplew, type, cid, tbyte, skill, level, display, speed, damage, lvl, mastery, unk, charge, pos, ultLevel, RangedAttack);
        /* mplew.writeInt(cid);
         mplew.write(RangedAttack ? 1 : 0);
         mplew.write(tbyte);
         //        System.out.println(tbyte + " - tbyte");
         mplew.write(lvl);
         if ((skill > 0) || (type == 3)) {
         mplew.write(level);
         if (level > 0) {
         mplew.writeInt(skill);
         }
         } else if (type != 2 && type != 3) {
         mplew.write(0);
         }

         if (GameConstants.isZero(skill / 10000) && skill != 100001283) {
         short zero1 = 0;
         short zero2 = 0;
         mplew.write(zero1 > 0 || zero2 > 0); //boolean
         if (zero1 > 0 || zero2 > 0) {
         mplew.writeShort(zero1);
         mplew.writeShort(zero2);
         //there is a full handler so better not write zero
         }
         }

         if (type == 2) {
         mplew.write(ultLevel);
         if (ultLevel > 0) {
         mplew.writeInt(3220010);
         }
         }
         if (skill == 40021185 || skill == 42001006) {
         mplew.write(0); //boolean if true then int
         }
         if (type == 0 || type == 1) {
         mplew.write(0);
         }
         mplew.write(unk);//always 0?
         if ((unk & 2) != 0) {
         mplew.writeInt(0);
         mplew.writeInt(0);
         }
         mplew.writeShort(display);
         mplew.write(speed);
         mplew.write(mastery);
         mplew.writeInt(charge);
         for (AttackPair oned : damage) {
         if (oned.attack != null) {
         mplew.writeInt(oned.objectid);
         mplew.write(7);
         mplew.write(0);
         mplew.write(0);
         if (skill == 42111002) {
         mplew.write(oned.attack.size());
         for (Pair eachd : oned.attack) {
         mplew.writeInt(((Integer) eachd.left));
         }
         } else {
         for (Pair eachd : oned.attack) {
         mplew.write(((Boolean) eachd.right) ? 1 : 0);
         mplew.writeInt(((Integer) eachd.left));
         }
         }
         }
         }
         if (skill == 2321001 || skill == 2221052 || skill == 11121052) {
         mplew.writeInt(0);
         } else if (skill == 65121052 || skill == 101000202 || skill == 101000102) {
         mplew.writeInt(0);
         mplew.writeInt(0);
         }
         if (skill == 42100007) {
         mplew.writeShort(0);
         mplew.write(0);
         }
         if (type == 1 || type == 2) {
         mplew.writePos(pos);
         } else if (type == 3 && charge > 0) {
         mplew.writeInt(charge);
         }
         if (skill == 5321000
         || skill == 5311001
         || skill == 5321001
         || skill == 5011002
         || skill == 5311002
         || skill == 5221013
         || skill == 5221017
         || skill == 3120019
         || skill == 3121015
         || skill == 4121017) {
         mplew.writePos(pos);
         }
         mplew.writeZeroBytes(30);//test
         */
        return mplew.getPacket();
    }

    //int type, int cid, int tbyte, int skill, int level, int display, byte speed, List<AttackPair> damage, int lvl, byte mastery, byte unk, int charge, Point pos, int ultLevel, boolean RangedAttack
    public static void addAttackBody(MaplePacketLittleEndianWriter mplew, int type, MapleCharacter cid, int tbyte, int skill, int level, int display, byte speed, List<AttackPair> damage, int lvl, byte mastery, byte unk, int charge, Point pos, int ultLevel, boolean RangedAttack) {
        mplew.writeInt(cid.getId());
        mplew.write(RangedAttack ? 1 : 0);
        mplew.write(tbyte);
        mplew.write(lvl);
        if (skill > 0) {
            mplew.write(level);
            mplew.writeInt(skill);
        } else {
            mplew.write(0);
        }

        if (MapleJob.is神之子(skill / 10000) && skill != 100001283) {
            short zero1 = 0;
            short zero2 = 0;
            mplew.write(zero1 > 0 || zero2 > 0); //boolean
            if (zero1 > 0 || zero2 > 0) {
                mplew.writeShort(zero1);
                mplew.writeShort(zero2);
                //there is a full handler so better not write zero
            }
        }

        if (RangedAttack) {
            if (skill != 13121052) {
                mplew.write(0);
            }
            mplew.writeInt(8);
        } else {
            mplew.writeInt(GameConstants.Attacktype(skill));
        }

        mplew.write(0);
        mplew.write(unk);//always 0?
        mplew.writeInt(0);
        if ((unk & 2) != 0) {
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        if ((unk & 8) != 0) {
            mplew.write(0);
        }
        mplew.writeShort(display);
        mplew.write(speed);
        mplew.write(mastery);
        mplew.writeInt(charge);
        for (AttackPair oned : damage) {
            if (oned.attack != null) {
                mplew.writeInt(oned.objectid);
                mplew.write(7);
                mplew.write(0);
                mplew.write(0);
                mplew.writeShort(0);
                if (skill == 42111002) {
                    mplew.write(oned.attack.size());
                    for (Pair eachd : oned.attack) {
                        mplew.writeInt(((Integer) eachd.left));
                    }
                } else {
                    for (Pair eachd : oned.attack) {
                        if (((Boolean) eachd.right)) {
                            mplew.writeInt(((Integer) eachd.left) + -2147483648);
                        } else {
                            mplew.writeInt(((Integer) eachd.left));
                        }
                    }
                }
            }
        }
        if (skill == 2321001 || skill == 2221052 || skill == 11121052 || skill == 12121054) {
            mplew.writeInt(0);
        } else if (skill == 4221052
                || skill == 65121052
                || skill == 13121052
                || (skill - 13121052) == 1000000
                || (skill - 13121052) == 2000000
                || (skill - 13121052) == 66880377
                || (skill - 13121052) == 66880379
                || (skill - 13121052 - 66880379) == 36
                || skill == 101000202
                || skill == 101000102) {
            mplew.writeInt(0);
            mplew.writeInt(0);
        }
        if (skill == 13111020 || skill == 112111016) {
            mplew.writePos(cid.getPosition()); // Position
        }
        if (skill == 112110003) {
            mplew.writeInt(0);
        }
        if (skill == 42100007) {
            mplew.writeShort(0);
            int size = 0;
            mplew.write(size);
            for (int i = 0; i < size; i++) {
                mplew.writeShort(0);
                mplew.writeShort(0);
            }
        }
    }

    public static byte[] skillEffect(MapleCharacter from, int skillId, byte level, short display, byte unk) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SKILL_EFFECT.getValue());
        mplew.writeInt(from.getId());
        mplew.writeInt(skillId);
        mplew.write(level);
        mplew.writeShort(display);
        mplew.write(unk);
        if (skillId == 13111020 || skillId == 112111016) {
            mplew.writePos(from.getPosition()); // Position
        }

        return mplew.getPacket();
    }

    public static byte[] skillMoveAttack(MapleCharacter from, int skillId, byte level, short display, byte unk) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MOVE_ATTACK.getValue());
        mplew.writeInt(from.getId());
        mplew.write(0);
        mplew.write(1);
        mplew.writeInt(skillId);
        mplew.writeShort(display);
        mplew.write(unk);
        if (skillId == 33121009 || skillId == 33121214) {
            mplew.writePos(from.getPosition()); // Position
        }

        return mplew.getPacket();
    }

    public static byte[] skillCancel(MapleCharacter from, int skillId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CANCEL_SKILL_EFFECT.getValue());
        mplew.writeInt(from.getId());
        mplew.writeInt(skillId);

        return mplew.getPacket();
    }

    public static byte[] damagePlayer(int cid, int type, int damage, int monsteridfrom, byte direction, int skillid, int pDMG, boolean pPhysical, int pID, byte pType, Point pPos, byte offset, int offset_d, int fake) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DAMAGE_PLAYER.getValue());
        mplew.writeInt(cid);
        mplew.write(type);
        mplew.writeInt(damage);
        mplew.write(0);
        if (type >= -1) {
            mplew.writeInt(monsteridfrom);
            mplew.write(direction);
            mplew.writeInt(skillid);
        } else {
            if (type == -8) {
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
        }
        mplew.writeInt(0);
        mplew.writeInt(pDMG);
        mplew.write(0);
        if (pDMG > 0) {
            mplew.write(pPhysical ? 1 : 0);
            mplew.writeInt(pID);
            mplew.write(pType);
            mplew.writePos(pPos);
        }
        mplew.write(offset);
        if ((offset & 1) != 0) {
            mplew.writeInt(offset_d);
        }
        mplew.writeInt(damage);
        if (damage == -1) {
            mplew.writeInt(fake);
        }

        return mplew.getPacket();
    }

    public static byte[] facialExpression(MapleCharacter from, int expression) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FACIAL_EXPRESSION.getValue());
        mplew.writeInt(from.getId());
        mplew.writeInt(expression);
        mplew.writeInt(-1);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] itemEffect(int characterid, int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        MapleCharacter chr;
        mplew.writeShort(SendPacketOpcode.SHOW_EFFECT.getValue());
        mplew.writeInt(characterid);
        mplew.writeInt(itemid);
        mplew.writeInt(-1); // not sure, added in v146.1
        mplew.write(0);
        System.out.println("Item Effect:\r\nCharacter ID: " + characterid + "\r\nItem ID: " + itemid);
        return mplew.getPacket();
    }

    public static byte[] showTitle(int characterid, int itemid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_TITLE.getValue());
        mplew.writeInt(characterid);
        mplew.writeInt(itemid);

        return mplew.getPacket();
    }

    public static byte[] showAngelicBuster(int characterid, int tempid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ANGELIC_CHANGE.getValue());
        mplew.writeInt(characterid);
        mplew.writeInt(tempid);

        return mplew.getPacket();
    }

    public static byte[] showChair(int characterid, int itemid, String text) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_CHAIR.getValue());
        mplew.writeInt(characterid);
        mplew.writeInt(itemid);
        mplew.writeInt(text.length() > 0 ? 1 : 0);
        if (text.length() != 0 && (itemid / 1000 == 3014)) {
            mplew.writeMapleAsciiString(text);
        }
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] updateCharLook(MapleCharacter chr, boolean second) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_CHAR_LOOK.getValue());
        mplew.writeInt(chr.getId());
        int unk = 1;
        mplew.write(unk);
        if ((unk & 1) != 0) {
            PacketHelper.addCharLook(mplew, chr, false, second);
        }
        if ((unk & 8) != 0) {
            PacketHelper.addCharLook(mplew, chr, false, second);
        }
        PacketHelper.UnkFunctin6(mplew);
        if ((unk & 2) != 0) {
            mplew.write(0);
        }
        if ((unk & 4) != 0) {
            mplew.write(0);
        }
        Triple<List<MapleRing>, List<MapleRing>, List<MapleRing>> rings = chr.getRings(false);
        addRingInfo(mplew, rings.getLeft());
        addRingInfo(mplew, rings.getMid());
        addMRingInfo(mplew, rings.getRight(), chr);
        mplew.writeInt(0); // -> charid to follow (4)
        mplew.writeInt(0x0F);

        return mplew.getPacket();
    }

    public static byte[] updatePartyMemberHP(int cid, int curhp, int maxhp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_PARTYMEMBER_HP.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(curhp);
        mplew.writeInt(maxhp);

        return mplew.getPacket();
    }

    public static byte[] loadGuildName(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LOAD_GUILD_NAME.getValue());
        mplew.writeInt(chr.getId());
        if (chr.getGuildId() <= 0) {
            mplew.writeShort(0);
        } else {
            MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
            if (gs != null) {
                mplew.writeMapleAsciiString(gs.getName());
            } else {
                mplew.writeShort(0);
            }
        }

        return mplew.getPacket();
    }

    public static byte[] loadGuildIcon(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LOAD_GUILD_ICON.getValue());
        mplew.writeInt(chr.getId());
        if (chr.getGuildId() <= 0) {
            mplew.writeZeroBytes(6);
        } else {
            MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
            if (gs != null) {
                mplew.writeShort(gs.getLogoBG());
                mplew.write(gs.getLogoBGColor());
                mplew.writeShort(gs.getLogo());
                mplew.write(gs.getLogoColor());
            } else {
                mplew.writeZeroBytes(6);
            }
        }

        return mplew.getPacket();
    }

    public static byte[] changeTeam(int cid, int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LOAD_TEAM.getValue());
        mplew.writeInt(cid);
        mplew.write(type);

        return mplew.getPacket();
    }

    public static byte[] showHarvesting(int cid, int tool) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_HARVEST.getValue());
        mplew.writeInt(cid);
        if (tool > 0) {
            mplew.write(1);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.writeInt(tool);
            mplew.writeZeroBytes(30);
        } else {
            mplew.write(0);
            mplew.writeZeroBytes(33);
        }

        return mplew.getPacket();
    }

    public static byte[] getPVPHPBar(int cid, int hp, int maxHp) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_HP.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(hp);
        mplew.writeInt(maxHp);

        return mplew.getPacket();
    }

    public static byte[] cancelChair(int id, int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CANCEL_CHAIR.getValue());
        if (id == -1) {
            mplew.writeInt(cid);
            mplew.write(0);
        } else {
            mplew.writeInt(cid);
            mplew.write(1);
            mplew.writeShort(id);
        }

        return mplew.getPacket();
    }

    public static byte[] instantMapWarp(byte portal) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CURRENT_MAP_WARP.getValue());
        mplew.write(0);
        mplew.write(portal);

        return mplew.getPacket();
    }

    public static byte[] updateQuestInfo(MapleCharacter c, int quest, int npc, byte progress) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_QUEST_INFO.getValue());
        mplew.write(11);
        mplew.writeShort(quest);
        mplew.writeInt(npc);
        mplew.writeShort(0);
        mplew.write(2);

        return mplew.getPacket();
    }

    public static byte[] updateQuestFinish(int quest, int npc, int nextquest) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_QUEST_INFO.getValue());
        mplew.write(11);
        mplew.writeShort(quest);
        mplew.writeInt(npc);
        mplew.writeShort(0);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] sendHint(String hint, int width, int height) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PLAYER_HINT.getValue());
        mplew.writeMapleAsciiString(hint);
        mplew.writeShort(width < 1 ? Math.max(hint.length() * 10, 40) : width);
        mplew.writeShort(Math.max(height, 5));
        mplew.write(1);

        return mplew.getPacket();
    }

    public static byte[] updateCombo(int value) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ARAN_COMBO.getValue());
        mplew.writeInt(value);

        return mplew.getPacket();
    }

    public static byte[] rechargeCombo(int value) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ARAN_COMBO_RECHARGE.getValue());
        mplew.writeInt(value);

        return mplew.getPacket();
    }

    public static byte[] getFollowMessage(String msg) {
        return getGameMessage(msg, (short) 11);
    }

    public static byte[] getGameMessage(String msg, short colour) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GAME_MESSAGE.getValue());
        mplew.writeShort(colour);
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] getBuffZoneEffect(int itemId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BUFF_ZONE_EFFECT.getValue());
        mplew.writeInt(itemId);

        return mplew.getPacket();
    }

    public static byte[] getTimeBombAttack() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TIME_BOMB_ATTACK.getValue());
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(10);
        mplew.writeInt(6);

        return mplew.getPacket();
    }

    public static byte[] moveFollow(Point otherStart, Point myStart, Point otherEnd, List<LifeMovementFragment> moves) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FOLLOW_MOVE.getValue());
        mplew.writeInt(0);
        mplew.writePos(otherStart);
        mplew.writePos(myStart);
        PacketHelper.serializeMovementList(mplew, moves);
        mplew.write(17);
        for (int i = 0; i < 8; i++) {
            mplew.write(0);
        }
        mplew.write(0);
        mplew.writePos(otherEnd);
        mplew.writePos(otherStart);
        mplew.writeZeroBytes(100);

        return mplew.getPacket();
    }

    public static byte[] getFollowMsg(int opcode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FOLLOW_MSG.getValue());
        mplew.writeLong(opcode);

        return mplew.getPacket();
    }

    public static byte[] registerFamiliar(MonsterFamiliar mf) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REGISTER_FAMILIAR.getValue());
        mplew.writeLong(mf.getId());
        mf.writeRegisterPacket(mplew, false);
        mplew.writeShort(mf.getVitality() >= 3 ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] createUltimate(int amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CREATE_ULTIMATE.getValue());
        mplew.writeInt(amount);

        return mplew.getPacket();
    }

    public static byte[] harvestMessage(int oid, int msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.HARVEST_MESSAGE.getValue());
        mplew.writeInt(oid);
        mplew.writeInt(msg);

        return mplew.getPacket();
    }

    public static byte[] openBag(int index, int itemId, boolean firstTime) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.OPEN_BAG.getValue());
        mplew.writeInt(index);
        mplew.writeInt(itemId);
        mplew.writeShort(firstTime ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] dragonBlink(int portalId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DRAGON_BLINK.getValue());
        mplew.write(portalId);

        return mplew.getPacket();
    }

    public static byte[] getPVPIceGage(int score) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PVP_ICEGAGE.getValue());
        mplew.writeInt(score);

        return mplew.getPacket();
    }

    public static byte[] skillCooldown(int sid, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.COOLDOWN.getValue());
        mplew.writeInt(1);
        mplew.writeInt(sid);
        mplew.writeInt(time);

        return mplew.getPacket();
    }

    public static byte[] showFusionAnvil(int itemId, int giveItemId, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FUSION_ANVIL.getValue());
        mplew.write(success ? 1 : 0);
        mplew.writeInt(itemId);
        mplew.writeInt(giveItemId);

        return mplew.getPacket();
    }

    public static byte[] dropItemFromMapObject(MapleMapItem drop, Point dropfrom, Point dropto, byte mod) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DROP_ITEM_FROM_MAPOBJECT.getValue());
        mplew.write(0);
        mplew.write(mod);
        mplew.writeInt(drop.getObjectId());
        mplew.write(drop.getMeso() > 0 ? 1 : 0);
        mplew.writeInt(0);
        mplew.writeLong(0/*Randomizer.nextInt(255)*/);
        mplew.writeInt(drop.getItemId());
        mplew.writeInt(drop.getOwner());
        mplew.write(drop.getDropType());
        mplew.writePos(dropto);
        mplew.writeInt(0);
        mplew.writeInt(0);//v175+
        if (mod != 2) {
            mplew.writePos(dropfrom);
            mplew.writeShort(0);
            mplew.writeShort(0);
        }
        mplew.write(0);
        mplew.write(0);
        if (drop.getMeso() == 0) {
            PacketHelper.addExpirationTime(mplew, drop.getItem().getExpiration());
        }
        mplew.writeShort(drop.isPlayerDrop() ? 0 : 1);
        mplew.writeInt(0);
        mplew.writeInt(0);//new v148
        mplew.write(drop.getState());//潛能等級特效

        return mplew.getPacket();
    }

    public static byte[] explodeDrop(int oid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REMOVE_ITEM_FROM_MAP.getValue());
        mplew.write(4);
        mplew.writeInt(oid);
        mplew.writeShort(655);

        return mplew.getPacket();
    }

    public static byte[] removeItemFromMap(int oid, int animation, int cid) {
        return removeItemFromMap(oid, animation, cid, 0);
    }

    public static byte[] removeItemFromMap(int oid, int animation, int cid, int slot) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REMOVE_ITEM_FROM_MAP.getValue());
        mplew.write(animation);
        mplew.writeInt(oid);
        if (animation >= 2) {
            mplew.writeInt(cid);
            if (animation == 5) {
                mplew.writeInt(slot);
            }
        }
        return mplew.getPacket();
    }

    public static byte[] spawnMist(MapleMist mist) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_MIST.getValue());
        mplew.writeInt(mist.getObjectId());

        mplew.write(mist.isMobMist() ? 0 : mist.isPoisonMist());
        //mplew.write(0);
        mplew.writeInt(mist.getOwnerId());
        if (mist.getMobSkill() == null) {
            mplew.writeInt(mist.getSourceSkill().getId());
        } else {
            mplew.writeInt(mist.getMobSkill().getSkillId());
        }
        mplew.write(mist.getSkillLevel());
        mplew.writeShort(mist.getSkillDelay());
        mplew.writeRect(mist.getBox());
        mplew.writeInt(mist.isShelter() ? 1 : 0);
        //mplew.writeInt(0);
        mplew.writePos(mist.getPosition());
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.write(0);
        mplew.writeInt(0);
        if(mist.getSourceSkill() != null) {
            if (mist.getSourceSkill().getId() == 33111013 || mist.getSourceSkill().getId() == 33121012 || mist.getSourceSkill().getId() == 33121016 || mist.getSourceSkill().getId() == 35121052) {
                mplew.write(0);
            } else {
                mplew.writeInt(0);
            }
            
        } else {
            mplew.writeInt(0);
        }
        return mplew.getPacket();
    }

    public static byte[] unkMist(int oid, List<Integer> unk) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MIST_UNK.getValue());
        mplew.writeInt(oid);
        mplew.writeInt(0);
        mplew.writeInt(unk.size());
        for (int mm : unk) {
            mplew.writeInt(mm);
        }

        return mplew.getPacket();
    }

    public static byte[] removeMist(int oid, boolean eruption) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REMOVE_MIST.getValue());
        mplew.writeInt(oid);
        mplew.write(eruption ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] spawnDoor(int oid, int skillId, Point pos, boolean animation) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_DOOR.getValue());
        mplew.write(animation ? 0 : 1);
        mplew.writeInt(oid);
        mplew.writeInt(skillId);
        mplew.writePos(pos);

        return mplew.getPacket();
    }

    public static byte[] removeDoor(int oid, boolean animation) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.REMOVE_DOOR.getValue());
        mplew.write(animation ? 0 : 1);
        mplew.writeInt(oid);

        return mplew.getPacket();
    }

    public static byte[] spawnKiteError() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_KITE_ERROR.getValue());

        return mplew.getPacket();
    }

    public static byte[] spawnKite(int oid, int id, Point pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_KITE.getValue());
        mplew.writeInt(oid);
        mplew.writeInt(0);
        mplew.writeMapleAsciiString("");
        mplew.writeMapleAsciiString("");
        mplew.writePos(pos);

        return mplew.getPacket();
    }

    public static byte[] destroyKite(int oid, int id, boolean animation) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.DESTROY_KITE.getValue());
        mplew.write(animation ? 0 : 1);
        mplew.writeInt(oid);

        return mplew.getPacket();
    }

    public static byte[] spawnMechDoor(MechDoor md, boolean animated) {
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
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MECH_DOOR_REMOVE.getValue());
        mplew.write(animated ? 0 : 1);
        mplew.writeInt(md.getOwnerId());
        mplew.write(md.getId());

        return mplew.getPacket();
    }

    //[8A 16 25 00] [03] [9D 07 7F 01] [06 01 00 06]
    public static byte[] triggerReactor(final MapleCharacter chr, MapleReactor reactor, int stance) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REACTOR_HIT.getValue());
        mplew.writeInt(reactor.getObjectId());
        mplew.write(reactor.getState());
        mplew.writePos(reactor.getTruePosition());
        mplew.writeShort(stance);
        mplew.write(0);
        mplew.write(0);
        mplew.writeInt(chr.getId());

        return mplew.getPacket();
    }

    public static byte[] spawnReactor(MapleReactor reactor) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REACTOR_SPAWN.getValue());
        mplew.writeInt(reactor.getObjectId());
        mplew.writeInt(reactor.getReactorId());
        mplew.write(reactor.getState());
        mplew.writePos(reactor.getTruePosition());
        mplew.write(reactor.getFacingDirection());
        mplew.writeMapleAsciiString(reactor.getName());

        return mplew.getPacket();
    }

    public static byte[] destroyReactor(MapleReactor reactor) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REACTOR_DESTROY.getValue());
        mplew.writeInt(reactor.getObjectId());
        mplew.write(reactor.getState());
        mplew.writePos(reactor.getPosition());

        return mplew.getPacket();
    }

    public static byte[] makeExtractor(int cid, String cname, Point pos, int timeLeft, int itemId, int fee) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_EXTRACTOR.getValue());
        mplew.writeInt(cid);
        mplew.writeMapleAsciiString(cname);
        mplew.writeInt(pos.x);
        mplew.writeInt(pos.y);
        mplew.writeShort(timeLeft);
        mplew.writeInt(itemId);
        mplew.writeInt(fee);

        return mplew.getPacket();
    }

    public static byte[] removeExtractor(int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REMOVE_EXTRACTOR.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(1);

        return mplew.getPacket();
    }

    public static byte[] rollSnowball(int type, MapleSnowball.MapleSnowballs ball1, MapleSnowball.MapleSnowballs ball2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ROLL_SNOWBALL.getValue());
        mplew.write(type);
        mplew.writeInt(ball1 == null ? 0 : ball1.getSnowmanHP() / 75);
        mplew.writeInt(ball2 == null ? 0 : ball2.getSnowmanHP() / 75);
        mplew.writeShort(ball1 == null ? 0 : ball1.getPosition());
        mplew.write(0);
        mplew.writeShort(ball2 == null ? 0 : ball2.getPosition());
        mplew.writeZeroBytes(11);

        return mplew.getPacket();
    }

    public static byte[] enterSnowBall() {
        return rollSnowball(0, null, null);
    }

    public static byte[] hitSnowBall(int team, int damage, int distance, int delay) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.HIT_SNOWBALL.getValue());
        mplew.write(team);
        mplew.writeShort(damage);
        mplew.write(distance);
        mplew.write(delay);

        return mplew.getPacket();
    }

    public static byte[] snowballMessage(int team, int message) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SNOWBALL_MESSAGE.getValue());
        mplew.write(team);
        mplew.writeInt(message);

        return mplew.getPacket();
    }

    public static byte[] leftKnockBack() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LEFT_KNOCK_BACK.getValue());

        return mplew.getPacket();
    }

    public static byte[] hitCoconut(boolean spawn, int id, int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.HIT_COCONUT.getValue());
        mplew.writeShort(spawn ? 0x8000 : id);
        mplew.writeShort(0); // 延遲時間
        mplew.write(spawn ? 0 : type);

        return mplew.getPacket();
    }

    public static byte[] coconutScore(int[] coconutscore) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.COCONUT_SCORE.getValue());
        mplew.writeShort(coconutscore[0]);
        mplew.writeShort(coconutscore[1]);

        return mplew.getPacket();
    }

    public static byte[] updateAriantScore(List<MapleCharacter> players) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ARIANT_SCORE_UPDATE.getValue());
        mplew.write(players.size());
        for (MapleCharacter i : players) {
            mplew.writeMapleAsciiString(i.getName());
            mplew.writeInt(0);
        }

        return mplew.getPacket();
    }

    public static byte[] sheepRanchInfo(byte wolf, byte sheep) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHEEP_RANCH_INFO.getValue());
        mplew.write(wolf);
        mplew.write(sheep);

        return mplew.getPacket();
    }

    public static byte[] sheepRanchClothes(int cid, byte clothes) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHEEP_RANCH_CLOTHES.getValue());
        mplew.writeInt(cid);
        mplew.write(clothes);

        return mplew.getPacket();
    }

    public static byte[] updateWitchTowerKeys(int keys) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WITCH_TOWER.getValue());
        mplew.write(keys);

        return mplew.getPacket();
    }

    public static byte[] showChaosZakumShrine(boolean spawned, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHAOS_ZAKUM_SHRINE.getValue());
        mplew.write(spawned ? 1 : 0);
        mplew.writeInt(time);

        return mplew.getPacket();
    }
    
    public static byte[] showZakumShrine(boolean spawned, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ZAKUM_SHRINE.getValue());
        mplew.write(spawned ? 1 : 0);
        mplew.writeInt(time);

        return mplew.getPacket();
    }

    public static byte[] showChaosHorntailShrine(boolean spawned, int time) {
        return showHorntailShrine(spawned, time);
    }

    public static byte[] showHorntailShrine(boolean spawned, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.HORNTAIL_SHRINE.getValue());
        mplew.write(spawned ? 1 : 0);
        mplew.writeInt(time);

        return mplew.getPacket();
    }

    public static byte[] getRPSMode(byte mode, int mesos, int selection, int answer) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.RPS_GAME.getValue());
        mplew.write(mode);
        switch (mode) {
            case 6:
                if (mesos == -1) {
                    break;
                }
                mplew.writeInt(mesos);
                break;
            case 8:
                mplew.writeInt(9000019);
                break;
            case 11:
                mplew.write(selection);
                mplew.write(answer);
        }

        return mplew.getPacket();
    }

    public static byte[] messengerInvite(String from, int messengerid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(3);
        mplew.writeMapleAsciiString(from);
        mplew.write(1);//channel?
        mplew.writeInt(messengerid);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] addMessengerPlayer(String from, MapleCharacter chr, int position, int channel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(0);
        mplew.write(position);
        PacketHelper.addCharLook(mplew, chr, true, false);
        mplew.writeMapleAsciiString(from);
        mplew.write(channel);
        mplew.write(1); // v140
        mplew.writeInt(chr.getJob());

        return mplew.getPacket();
    }

    public static byte[] removeMessengerPlayer(int position) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(2);
        mplew.write(position);

        return mplew.getPacket();
    }

    public static byte[] updateMessengerPlayer(String from, MapleCharacter chr, int position, int channel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(0); // v140.
        mplew.write(position);
        PacketHelper.addCharLook(mplew, chr, true, false);
        mplew.writeMapleAsciiString(from);
        mplew.write(channel);
        mplew.write(0); // v140.
        mplew.writeInt(chr.getJob()); // doubt it's the job, lol. v140.

        return mplew.getPacket();
    }

    public static byte[] joinMessenger(int position) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(1);
        mplew.write(position);

        return mplew.getPacket();
    }

    public static byte[] messengerChat(String charname, String text) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(6);
        mplew.writeMapleAsciiString(charname);
        mplew.writeMapleAsciiString(text);

        return mplew.getPacket();
    }

    public static byte[] messengerNote(String text, int mode, int mode2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(mode);
        mplew.writeMapleAsciiString(text);
        mplew.write(mode2);

        return mplew.getPacket();
    }

    public static byte[] messengerOpen(byte type, List<MapleCharacter> chars) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MESSENGER_OPEN.getValue());
        mplew.write(type); //7 in messenger open ui 8 new ui
        if (chars.isEmpty()) {
            mplew.writeShort(0);
        }
        for (MapleCharacter chr : chars) {
            mplew.write(1);
            mplew.writeInt(chr.getId());
            mplew.writeInt(0); //likes
            mplew.writeLong(0); //some time
            mplew.writeMapleAsciiString(chr.getName());
            PacketHelper.addCharLook(mplew, chr, true, false);
        }

        return mplew.getPacket();
    }

    public static byte[] messengerCharInfo(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MESSENGER.getValue());
        mplew.write(0x0B);
        mplew.writeMapleAsciiString(chr.getName());
        mplew.writeInt(chr.getJob());
        mplew.writeInt(chr.getFame());
        mplew.writeInt(0); //likes
        MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
        mplew.writeMapleAsciiString(gs != null ? gs.getName() : "-");
        MapleGuildAlliance alliance = World.Alliance.getAlliance(gs.getAllianceId());
        mplew.writeMapleAsciiString(alliance != null ? alliance.getName() : "");
        mplew.write(2);

        return mplew.getPacket();
    }

    public static byte[] removeFromPackageList(boolean remove, int Package) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PACKAGE_OPERATION.getValue());
        mplew.write(24);
        mplew.writeInt(Package);
        mplew.write(remove ? 3 : 4);

        return mplew.getPacket();
    }

    public static byte[] sendPackageMSG(byte operation, List<MaplePackageActions> packages) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PACKAGE_OPERATION.getValue());
        mplew.write(operation);

        switch (operation) {
            case 9:
                mplew.write(1);
                break;
            case 10:
                mplew.write(0);
                mplew.write(packages.size());

                for (MaplePackageActions dp : packages) {
                    mplew.writeInt(dp.getPackageId());
                    mplew.writeAsciiString(dp.getSender(), 13);
                    mplew.writeInt(dp.getMesos());
                    mplew.writeLong(PacketHelper.getTime(dp.getSentTime()));
                    mplew.writeZeroBytes(205);

                    if (dp.getItem() != null) {
                        mplew.write(1);
                        PacketHelper.addItemInfo(mplew, dp.getItem());
                    } else {
                        mplew.write(0);
                    }
                }
                mplew.write(0);
        }

        return mplew.getPacket();
    }

    public static byte[] getKeymap(MapleKeyLayout layout, MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.KEYMAP.getValue());
        layout.writeData(mplew, chr);

        return mplew.getPacket();
    }

    public static byte[] petAutoHP(int itemId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PET_AUTO_HP.getValue());
        mplew.writeInt(itemId);

        return mplew.getPacket();
    }

    public static byte[] petAutoMP(int itemId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PET_AUTO_MP.getValue());
        mplew.writeInt(itemId);

        return mplew.getPacket();
    }

    public static byte[] petAutoCure(int itemId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PET_AUTO_CURE.getValue());
        mplew.writeInt(itemId);

        return mplew.getPacket();
    }

    public static byte[] petAutoBuff(int skillId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        //mplew.writeShort(SendPacketOpcode.PET_AUTO_BUFF.getValue());
        mplew.writeInt(skillId);

        return mplew.getPacket();
    }

    public static void addRingInfo(MaplePacketLittleEndianWriter mplew, List<MapleRing> rings) {
        mplew.write(rings.size());
        for (MapleRing ring : rings) {
            mplew.writeInt(1);
            mplew.writeLong(ring.getRingId());
            mplew.writeLong(ring.getPartnerRingId());
            mplew.writeInt(ring.getItemId());
        }
    }

    public static void addMRingInfo(MaplePacketLittleEndianWriter mplew, List<MapleRing> rings, MapleCharacter chr) {
        mplew.write(rings.size());
        for (MapleRing ring : rings) {
            mplew.writeInt(1);
            mplew.writeInt(chr.getId());
            mplew.writeInt(ring.getPartnerChrId());
            mplew.writeInt(ring.getItemId());
        }
    }

    public static byte[] getBuffBar(long millis) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BUFF_BAR.getValue());
        mplew.writeLong(millis);

        return mplew.getPacket();
    }

    public static byte[] getBoosterFamiliar(int cid, int familiar, int id) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BOOSTER_FAMILIAR.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(familiar);
        mplew.writeLong(id);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] viewSkills(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TARGET_SKILL.getValue());
        List skillz = new ArrayList();
        for (Skill sk : chr.getSkills().keySet()) {
            if ((sk.canBeLearnedBy(chr.getJob())) && (GameConstants.canSteal(sk)) && (!skillz.contains(sk.getId()))) {
                skillz.add(sk.getId());
            }
        }
        mplew.write(1);
        mplew.writeInt(chr.getId());
        mplew.writeInt(skillz.isEmpty() ? 2 : 4);
        mplew.writeInt(chr.getJob());
        mplew.writeInt(skillz.size());
        for (Iterator i$ = skillz.iterator(); i$.hasNext();) {
            int i = ((Integer) i$.next());
            mplew.writeInt(i);
        }

        return mplew.getPacket();
    }

    public static byte[] spawnArrowBlaster(MapleCharacter chr, int x, int y, int a) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_ARROW_BLASTER.getValue());
        mplew.writeInt(1);
        mplew.writeInt(1);
        mplew.writeInt(chr.getId());
        mplew.writeInt(0);
        mplew.writeInt((int) chr.getPosition().getX());
        mplew.writeInt((int) chr.getPosition().getY());
        mplew.write(1);

        return mplew.getPacket();
    }

    public static byte[] controlArrowBlaster(int a) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ARROW_BLASTER_CONTROL.getValue());
        mplew.writeInt(a);
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] cancelArrowBlaster(int b) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CANCEL_ARROW_BLASTER.getValue());
        mplew.writeInt(1);
        mplew.writeInt(b);

        return mplew.getPacket();
    }

    public static byte[] getDeathTip(int op) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DEATH_TIP.getValue());

        mplew.writeInt(op);
        boolean v41 = false;
        mplew.write(v41);
        int v15 = 0;
        if ((op & 1) == 1) {
            mplew.writeInt(v15);
        }
        if (((op >> 1) & 1) == 1) {
            if (v15 != 9) {
                mplew.writeInt(0);
            }
        }

        return mplew.getPacket();
    }

    public static class InteractionPacket {

        public static byte[] getTradeInvite(MapleCharacter c) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.INVITE_TRADE.action);
            mplew.write(4);//was 3
            mplew.writeMapleAsciiString(c.getName());
//            mplew.writeInt(c.getLevel());
            mplew.writeInt(c.getJob());
            return mplew.getPacket();
        }

        public static byte[] getTradeMesoSet(byte number, long meso) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.UPDATE_MESO.action);
            mplew.write(number);
            mplew.writeLong(meso);
            return mplew.getPacket();
        }

        public static byte[] getTradeItemAdd(byte number, Item item) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.SET_ITEMS.action);
            mplew.write(number);
            mplew.write(item.getPosition());
            PacketHelper.addItemInfo(mplew, item);

            return mplew.getPacket();
        }

        public static byte[] getTradeStart(MapleClient c, MapleTrade trade, byte number) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
//            mplew.write(PlayerInteractionHandler.Interaction.START_TRADE.action);
//            if (number != 0){//13 a0
////                mplew.write(HexTool.getByteArrayFromHexString("13 01 01 03 FE 53 00 00 40 08 00 00 00 E2 7B 00 00 01 E9 50 0F 00 03 62 98 0F 00 04 56 BF 0F 00 05 2A E7 0F 00 07 B7 5B 10 00 08 3D 83 10 00 09 D3 D1 10 00 0B 13 01 16 00 11 8C 1F 11 00 12 BF 05 1D 00 13 CB 2C 1D 00 31 40 6F 11 00 32 6B 46 11 00 35 32 5C 19 00 37 20 E2 11 00 FF 03 B6 98 0F 00 05 AE 0A 10 00 09 CC D0 10 00 FF FF 00 00 00 00 13 01 16 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0B 00 4D 6F 6D 6F 6C 6F 76 65 73 4B 48 40 08"));
//                mplew.write(19);
//                mplew.write(1);
//                PacketHelper.addCharLook(mplew, trade.getPartner().getChr(), false);
//                mplew.writeMapleAsciiString(trade.getPartner().getChr().getName());
//                mplew.writeShort(trade.getPartner().getChr().getJob());
//            }else{
            mplew.write(20);
            mplew.write(4);
            mplew.write(2);
            mplew.write(number);

            if (number == 1) {
                mplew.write(0);
                PacketHelper.addCharLook(mplew, trade.getPartner().getChr(), false, false);
                mplew.writeMapleAsciiString(trade.getPartner().getChr().getName());
                mplew.writeShort(trade.getPartner().getChr().getJob());
            }
            mplew.write(number);
            PacketHelper.addCharLook(mplew, c.getPlayer(), false, false);
            mplew.writeMapleAsciiString(c.getPlayer().getName());
            mplew.writeShort(c.getPlayer().getJob());
            mplew.write(255);
//            }
            return mplew.getPacket();
        }

        public static byte[] getTradeConfirmation() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.CONFIRM_TRADE.action);

            return mplew.getPacket();
        }

        public static byte[] TradeMessage(byte UserSlot, byte message) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.EXIT.action);
//            mplew.write(25);//new v141
            mplew.write(UserSlot);
            mplew.write(message);

            return mplew.getPacket();
        }

        public static byte[] getTradeCancel(byte UserSlot, int unsuccessful) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
            mplew.write(PlayerInteractionHandler.Interaction.EXIT.action);
            mplew.write(UserSlot);
            mplew.write(7);//was2

            return mplew.getPacket();
        }
    }

    public static class NPCPacket {
        
        public static byte[] resetNPC(int objectid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.RESET_NPC.getValue());
            mplew.writeInt(objectid);

            return mplew.getPacket();
        }
        
        public static byte[] getOthersTalk(int npc, byte msgType, int npcid, String talk, String endBytes, byte type) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(3);
            mplew.writeInt(npc);
            mplew.write(npcid > 0); // Boolean
            if (npcid > 0) {
                mplew.writeInt(npcid);
            }
            mplew.write(0);
            mplew.write(type);
            mplew.write(0);
            mplew.writeMapleAsciiString(talk);
            mplew.write(HexTool.getByteArrayFromHexString(endBytes));
            mplew.writeInt(0); // 178+

            return mplew.getPacket();
        }
        
        public static byte[] spawnNPC(MapleNPC life, boolean show) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SPAWN_NPC.getValue());
            mplew.writeInt(life.getObjectId());
            mplew.writeInt(life.getId());
            mplew.writeShort(life.getPosition().x);
            mplew.writeShort(life.getCy());
            mplew.write(life.getF() == 1 ? 0 : 1);
            mplew.writeShort(life.getFh());
            mplew.writeShort(life.getRx0());
            mplew.writeShort(life.getRx1());
            mplew.write(show ? 1 : 0);
            mplew.writeInt(0);
            mplew.write(0);
            mplew.writeInt(-1);
            mplew.writeLong(0);
            mplew.writeZeroBytes(3);//176+

            return mplew.getPacket();
        }

        public static byte[] removeNPC(int objectid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.REMOVE_NPC.getValue());
            mplew.writeInt(objectid);

            return mplew.getPacket();
        }

        public static byte[] removeNPCController(int objectid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SPAWN_NPC_REQUEST_CONTROLLER.getValue());
            mplew.write(0);
            mplew.writeInt(objectid);

            return mplew.getPacket();
        }

        public static byte[] spawnNPCRequestController(int npc) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SPAWN_NPC_REQUEST_CONTROLLER.getValue());
            mplew.write(0);
            mplew.writeInt(npc);

            return mplew.getPacket();
        }

        public static byte[] spawnNPCRequestController(MapleNPC life, boolean MiniMap) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SPAWN_NPC_REQUEST_CONTROLLER.getValue());
            mplew.write(1);
            mplew.writeInt(life.getObjectId());
            mplew.writeInt(life.getId());
            mplew.writeShort(life.getPosition().x);
            mplew.writeShort(life.getCy());
            mplew.write(life.getF() == 1 ? 0 : 1);
            mplew.writeShort(life.getFh());
            mplew.writeShort(life.getRx0());
            mplew.writeShort(life.getRx1());
            mplew.write(MiniMap ? 1 : 0);
            mplew.writeInt(0);//new 143
            mplew.write(0);
            mplew.writeInt(-1);
            mplew.writeLong(0);
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] toggleNPCShow(int oid, boolean hide) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.NPC_TOGGLE_VISIBLE.getValue());
            mplew.writeInt(oid);
            mplew.write(hide ? 0 : 1);
            return mplew.getPacket();
        }

        public static byte[] setNPCSpecialAction(int oid, String action, int time, boolean unk) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.NPC_SET_SPECIAL_ACTION.getValue());
            mplew.writeInt(oid);
            mplew.writeMapleAsciiString(action);
            mplew.writeInt(time);
            mplew.write(unk); //unknown yet
            return mplew.getPacket();
        }

        public static byte[] NPCSpecialAction(int oid, int value, int x, int y) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.NPC_UPDATE_LIMITED_INFO.getValue());
            mplew.writeInt(oid);
            mplew.writeInt(value);
            mplew.writeInt(x);
            mplew.writeInt(y);

            return mplew.getPacket();
        }

        public static byte[] setNPCScriptable() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.NPC_SCRIPTABLE.getValue());
            List<Pair<Integer, String>> npcs = new LinkedList();
            npcs.add(new Pair<>(9070006, "Why...why has this happened to me? My knightly honor... My knightly pride..."));
            npcs.add(new Pair<>(9000021, "Are you enjoying the event?"));
            mplew.write(npcs.size());
            for (Pair<Integer, String> s : npcs) {
                mplew.writeInt(s.getLeft());
                mplew.writeMapleAsciiString(s.getRight());
                mplew.writeInt(0);
                mplew.writeInt(Integer.MAX_VALUE);
            }
            return mplew.getPacket();
        }

        public static byte[] getNPCTalk(int npc, byte msgType, String talk, String endBytes, byte type) {
            return getNPCTalk(npc, msgType, talk, endBytes, type, npc);
        }

        public static byte[] getNPCTalk(int npc, byte msgType, String talk, String endBytes, byte type, int diffNPC) {
            return getNPCTalk(npc, msgType, talk, endBytes, type, (byte) 0, diffNPC);
        }

        public static byte[] getNPCTalk(int npc, byte msgType, String talk, String endBytes, byte type, byte type2, int diffNPC) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(4);
            mplew.writeInt(npc);
            mplew.write(0); // Boolean
            mplew.write(msgType);
            mplew.write(type);
            mplew.write(type2);
            if ((type & 0x4) != 0) {
                mplew.writeInt(diffNPC);
            }
            mplew.writeMapleAsciiString(talk);
            mplew.write(HexTool.getByteArrayFromHexString(endBytes));
            mplew.writeInt(0); // 178+

            return mplew.getPacket();
        }

        public static byte[] getNPCTalk_IDA(int npc, byte msgType, String talk, String endBytes, byte type, byte type2, int diffNPC) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(4); // v4
            mplew.writeInt(npc); // v13
            boolean read = false;
            mplew.write(read); // Boolean
            if (read) {
                mplew.writeInt(0);
            }
            mplew.write(msgType); // v5
            mplew.write(type); // v12
            mplew.write(type2); // result
            switch (msgType) {
                case 0:
                    if ((type & 0x4) != 0) {
                        mplew.writeInt(diffNPC);
                    }
                    mplew.writeMapleAsciiString(talk);
                    mplew.write(HexTool.getByteArrayFromHexString(endBytes));
                    mplew.writeInt(0);
                    break;
                case 1:
                    int t_size = 0;
                    mplew.write(t_size);
                    for (int i = 0; i < t_size; i++) {
                        mplew.writeMapleAsciiString(talk);
                    }
                    break;
                case 2:
                    if ((type & 0x4) != 0) {
                        mplew.writeInt(diffNPC);
                    }
                    mplew.writeMapleAsciiString(talk);
                    break;
                case 15:
                    if ((type & 0x4) != 0) {
                        mplew.writeInt(diffNPC);
                    }
                    mplew.writeMapleAsciiString(talk);
                    break;
                case 3:
                    mplew.writeMapleAsciiString(talk);
                    mplew.writeMapleAsciiString(talk);
                    mplew.writeShort(0);
                    mplew.writeShort(0);
                    break;
                case 16:
                    mplew.writeMapleAsciiString(talk);
                    mplew.writeMapleAsciiString(talk);
                    mplew.writeShort(0);
                    mplew.writeShort(0);
                    break;
                case 36:
                    mplew.writeShort(0);
                    mplew.writeMapleAsciiString(talk);
                    mplew.writeMapleAsciiString(talk);
                    mplew.writeShort(0);
                    mplew.writeShort(0);
                    mplew.writeShort(0);
                    mplew.writeShort(0);
                    break;
                case 4:
                    mplew.writeMapleAsciiString(talk);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case 5:
                    if ((type & 0x4) != 0) {
                        mplew.writeInt(diffNPC);
                    }
                    mplew.writeMapleAsciiString(talk);
                    break;
                case 9:

                    break;
                case 33:
                    break;
                case 39:
                    break;
                case 40:
                    break;
                case 41:
                    break;
                case 42:
                    break;
                case 10:
                    break;
                case 11:
                    break;
                case 12:
                    break;
                case 13:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                case 8:
                    break;
                case 17:
                    break;
                case 23:
                    break;
                case 24:
                    break;
                case 25:
                    break;
                case 26:
                    break;
                case 27:
                    break;
                case 28:
                    break;
                case 29:
                    break;
                case 30:
                    break;
                case 31:
                    break;
                case 32:
                    break;
                case 35:
                    break;
                case 37:
                    break;
                case 44:
                    break;
                case 46:
                    break;
                case 21:
                    break;
                case 22:
                    break;
                default:
                    break;
            }

            return mplew.getPacket();
        }

        public static byte[] getZeroNPCTalk(int npc, byte msgType, String talk, String endBytes, byte type, int diffNPC) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(3);
            mplew.writeInt(0);
            mplew.write(1); // Boolean
            mplew.writeInt(npc);
            mplew.write(msgType);
            mplew.write(type);
            mplew.write(0);
            if ((type & 0x4) != 0) {
                mplew.writeInt(diffNPC);
            }
            mplew.writeMapleAsciiString(talk);
            mplew.write(HexTool.getByteArrayFromHexString(endBytes));
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] getSengokuNPCTalk(boolean unknown, int npc, byte msgType, byte type, int diffNPC, String talk, boolean next, boolean prev, boolean pic) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(unknown ? 4 : 3);
            if (!unknown) {
                mplew.writeInt(0);
            }
            mplew.write(unknown); // Boolean
            if (unknown) {
                mplew.writeInt(npc);
            }
            mplew.write(msgType);
            mplew.write(type);
            mplew.write(0);//?
            if ((type & 0x4) != 0) {
                mplew.writeInt(diffNPC);
            }
            mplew.writeMapleAsciiString(talk);
            mplew.write(next);
            mplew.write(prev);
            mplew.writeInt(diffNPC);
            mplew.write(pic);
            mplew.writeInt(0);
            return mplew.getPacket();
        }

        public static byte[] getEnglishQuiz(int npc, byte type, int diffNPC, String talk, String endBytes) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(4);
            mplew.writeInt(npc);
            mplew.write(0); // Boolean
            mplew.write(10); //not sure
            mplew.write(type);
            mplew.write(0);
            if ((type & 0x4) != 0) {
                mplew.writeInt(diffNPC);
            }
            mplew.writeMapleAsciiString(talk);
            mplew.write(HexTool.getByteArrayFromHexString(endBytes));
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] getAdviceTalk(String[] wzinfo) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(8);
            mplew.writeInt(0);
            mplew.write(0); // Boolean
            mplew.write(1);
            mplew.write(1);
            mplew.write(0);
            mplew.write(wzinfo.length);
            for (String data : wzinfo) {
                mplew.writeMapleAsciiString(data);
            }
            return mplew.getPacket();
        }

        public static byte[] getSlideMenu(int npcid, int type, int lasticon, String sel) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            //Types: 0 - map selection 1 - neo city map selection 2 - korean map selection 3 - tele rock map selection 4 - dojo buff selection
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(4); // slide menu
            mplew.writeInt(npcid);
            mplew.write(0); // Boolean
            mplew.write(0x11);
            mplew.write(0);
            mplew.write(0); // 175+
            mplew.writeInt(type); // 選單類型
            mplew.writeInt(type == 0 ? lasticon : 0); // last icon on menu
            mplew.writeMapleAsciiString(sel);

            return mplew.getPacket();
        }

        public static byte[] getNPCTalkStyle(int npc, String talk, int[] args, boolean second) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(4);
            mplew.writeInt(npc);
            mplew.write(0); // Boolean
            mplew.write(9);
            mplew.write(0);
            mplew.write(0);
            mplew.write(second ? 1 : 0);
            mplew.write(0);
            mplew.writeMapleAsciiString(talk);
            mplew.write(args.length);
            for (int i = 0; i < args.length; i++) {
                mplew.writeInt(args[i]);
            }
            return mplew.getPacket();
        }

        public static byte[] getNPCTalkNum(int npc, String talk, int def, int min, int max) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(4);
            mplew.writeInt(npc);
            mplew.write(0); // Boolean
            mplew.write(4);
            mplew.write(0);
            mplew.write(0);
            mplew.writeMapleAsciiString(talk);
            mplew.writeInt(def);
            mplew.writeInt(min);
            mplew.writeInt(max);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] getNPCTalkText(int npc, String talk) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(4);
            mplew.writeInt(npc);
            mplew.write(0); // Boolean
            mplew.write(3); // 3 regular 6 quiz
            mplew.write(0);
            mplew.write(0);
            mplew.writeMapleAsciiString(talk);
            mplew.writeMapleAsciiString("");
            mplew.writeShort(0);
            mplew.writeShort(0);

            return mplew.getPacket();
        }

        public static byte[] getNPCTalkQuiz(int npc, String caption, String talk, int time) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(4);
            mplew.writeInt(npc);
            mplew.write(0); // Boolean
            mplew.write(6);
            mplew.write(0);
            mplew.write(0);
            mplew.write(0); // Boolean
            mplew.writeMapleAsciiString(caption);
            mplew.writeMapleAsciiString(talk);
            mplew.writeMapleAsciiString("");
            mplew.writeInt(0);
            mplew.writeInt(0xF); //no idea
            mplew.writeInt(time); //seconds

            return mplew.getPacket();
        }

        public static byte[] getSelfTalkText(String text) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(3);
            mplew.writeInt(0);
            mplew.write(1); // Boolean
            mplew.writeInt(0);
            mplew.write(0);
            mplew.write(0x11);
            mplew.write(0); // 173+
            mplew.writeMapleAsciiString(text);
            mplew.write(0);
            mplew.write(1);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] getNPCTutoEffect(String effect) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(3);
            mplew.writeInt(0);
            mplew.write(0); // Boolean
            mplew.write(1);
            mplew.write(1);
            mplew.write(0);
            mplew.write(1);
            mplew.writeMapleAsciiString(effect);

            return mplew.getPacket();
        }

        public static byte[] getDemonSelection() {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(3);
            mplew.writeInt(0);
            mplew.write(1); // Boolean
            mplew.writeInt(2159311); // npcID
            mplew.write(0x17);
            mplew.write(1);
            mplew.write(0);
            mplew.writeShort(1);
            mplew.writeZeroBytes(8);

            return mplew.getPacket();
        }

        public static byte[] getLuminousSelection() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(3);
            mplew.writeInt(0);
            mplew.write(1);
            mplew.writeInt(2159311); //npc
            mplew.write(0x17);
            mplew.write(1);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.writeZeroBytes(8);
            return mplew.getPacket();
        }

        public static byte[] getAngelicBusterAvatarSelect(int npc) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(4);
            mplew.writeInt(npc);
            mplew.write(0); // Boolean
            mplew.write(0x18);
            mplew.write(0);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] getEvanTutorial(String data) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(8);
            mplew.writeInt(0);
            mplew.write(0); // Boolean
            mplew.write(1);
            mplew.write(1);
            mplew.write(0);
            mplew.write(1);
            mplew.writeMapleAsciiString(data);

            return mplew.getPacket();
        }

        public static byte[] getAndroidTalkStyle(int npc, String talk, int... args) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(4);
            mplew.writeInt(npc);
            mplew.write(0); // Boolean
            mplew.write(10);
            mplew.write(0);
            mplew.write(0);
            mplew.writeMapleAsciiString(talk);
            mplew.write(args.length);
            for (int i = 0; i < args.length; i++) {
                mplew.writeInt(args[i]);
            }
            return mplew.getPacket();
        }

        public static byte[] getArisanNPCTalk(int npc, boolean read, byte msgType, byte type, byte result, String talk) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(8);
            mplew.writeInt(npc);
            mplew.write(read); // Boolean
            if (read) {
                mplew.writeInt(0);
            }
            mplew.write(msgType);
            mplew.write(type);
            mplew.write(result);
            if ((type & 0x4) != 0) {
                mplew.writeInt(0);
            }
            mplew.writeMapleAsciiString(talk);

            return mplew.getPacket();
        }

        public static byte[] getDreamWorldNPCTalk(int npc, boolean read, byte msgType, byte type, byte result, int npcId, String talk) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(8);
            mplew.writeInt(npc);
            mplew.write(read); // Boolean
            if (read) {
                mplew.writeInt(0);
            }
            mplew.write(msgType);
            mplew.write(type);
            mplew.write(result);
            if ((type & 0x4) != 0) {
                mplew.writeInt(npcId);
            }
            mplew.writeMapleAsciiString(talk);

            return mplew.getPacket();
        }

        public static byte[] getNPCShop(int sid, MapleShop shop, MapleClient c) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.OPEN_NPC_SHOP.getValue());
            mplew.write(0); // Boolean [true => + [Int]]
            mplew.writeInt(0);
            mplew.writeInt(sid);
            PacketHelper.addShopInfo(mplew, shop, c);

            return mplew.getPacket();
        }

        public static byte[] confirmShopTransaction(byte code, MapleShop shop, MapleClient c, int indexBought) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CONFIRM_SHOP_TRANSACTION.getValue());
            mplew.write(code);
            if (code == 8) {
                mplew.writeInt(0);
                mplew.writeInt(shop.getNpcId());
                PacketHelper.addShopInfo(mplew, shop, c);
            } else {
                mplew.write(indexBought >= 0 ? 1 : 0);
                if (indexBought >= 0) {
                    mplew.writeInt(indexBought);
                } else {
                    mplew.write(0);
                }
                mplew.write(0);
                mplew.write(0);
            }

            return mplew.getPacket();
        }

        public static byte[] getStorage(int npcId, byte slots, Collection<Item> items, long meso) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
            mplew.write(22);
            mplew.writeInt(npcId);
            mplew.write(slots);
            mplew.writeShort(126);
            mplew.writeShort(0);
            mplew.writeInt(0);
            mplew.writeLong(meso);
            mplew.writeShort(0);
            mplew.write((byte) items.size());
            for (Item item : items) {
                PacketHelper.addItemInfo(mplew, item);
            }
            mplew.writeZeroBytes(2);//4

            return mplew.getPacket();
        }

        public static byte[] getStorageFull() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
            mplew.write(17);

            return mplew.getPacket();
        }

        public static byte[] mesoStorage(byte slots, long meso) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
            mplew.write(19);
            mplew.write(slots);
            mplew.writeShort(2);
            mplew.writeShort(0);
            mplew.writeInt(0);
            mplew.writeLong(meso);

            return mplew.getPacket();
        }

        public static byte[] arrangeStorage(byte slots, Collection<Item> items, boolean changed) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
            mplew.write(15);
            mplew.write(slots);
            mplew.write(124);
            mplew.writeZeroBytes(10);
            mplew.write(items.size());
            for (Item item : items) {
                PacketHelper.addItemInfo(mplew, item);
            }
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] storeStorage(byte slots, MapleInventoryType type, Collection<Item> items) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
            mplew.write(13);
            mplew.write(slots);
            mplew.writeShort(type.getBitfieldEncoding());
            mplew.writeShort(0);
            mplew.writeInt(0);
            mplew.write(items.size());
            for (Item item : items) {
                PacketHelper.addItemInfo(mplew, item);
            }
            return mplew.getPacket();
        }

        public static byte[] takeOutStorage(byte slots, MapleInventoryType type, Collection<Item> items) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.OPEN_STORAGE.getValue());
            mplew.write(9);
            mplew.write(slots);
            mplew.writeShort(type.getBitfieldEncoding());
            mplew.writeShort(0);
            mplew.writeInt(0);
            mplew.write(items.size());
            for (Item item : items) {
                PacketHelper.addItemInfo(mplew, item);
            }
            return mplew.getPacket();
        }
    }

    public static class SummonPacket {

        public static byte[] spawnSummon(MapleSummon summon, boolean animated) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SPAWN_SUMMON.getValue());
            mplew.writeInt(summon.getOwnerId());
            mplew.writeInt(summon.getObjectId());
            mplew.writeInt(summon.getSkill());
            mplew.write(summon.getOwnerLevel() - 1);
            mplew.write(summon.getSkillLevel());
            mplew.writePos(summon.getPosition());
            mplew.write((summon.getSkill() == 32111006) || (summon.getSkill() == 33101005) ? 5 : 4);// Summon Reaper Buff - Call of the Wild
            if ((summon.getSkill() == 35121003) && (summon.getOwner().getMap() != null)) {//Giant Robot SG-88
                mplew.writeShort(summon.getOwner().getMap().getFootholds().findBelow(summon.getPosition(), true).getId());
            } else {
                mplew.writeShort(0);
            }
            mplew.write(summon.getMovementType().getValue());
            mplew.write(summon.getSummonType());
            mplew.write(animated ? 1 : 0);
            mplew.writeInt(0);
            mplew.write(0);
            mplew.write(1);
            mplew.writeInt(0);
            mplew.writeInt(0);

            MapleCharacter chr = summon.getOwner();
            boolean sendAddCharLook = ((summon.getSkill() == 4341006) && (chr != null));
            mplew.write(sendAddCharLook ? 1 : 0); // Mirrored Target
            if (sendAddCharLook) { // Mirrored Target
                PacketHelper.addCharLook(mplew, chr, true, false);
            }
            if (summon.getSkill() == 35111002) {// Rock 'n Shock
                boolean v8 = false;
                mplew.write(v8);
                if (v8) {
                    int v33 = 0;
                    do {
                        mplew.writeShort(0);
                        mplew.writeShort(0);
                        v33++;
                    } while (v33 < 3);
                }
            }
            if (summon.getSkill() == 42111003) {
                mplew.writeShort(0);
                mplew.writeShort(0);
                mplew.writeShort(0);
                mplew.writeShort(0);
            }
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] removeSummon(int ownerId, int objId) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.REMOVE_SUMMON.getValue());
            mplew.writeInt(ownerId);
            mplew.writeInt(objId);
            mplew.write(10);

            return mplew.getPacket();
        }

        public static byte[] removeSummon(MapleSummon summon, boolean animated) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.REMOVE_SUMMON.getValue());
            mplew.writeInt(summon.getOwnerId());
            mplew.writeInt(summon.getObjectId());
            if (animated) {
                switch (summon.getSkill()) {
                    case 35121003:
                        mplew.write(10);
                        break;
                    case 33101008:
                    case 35111001:
                    case 35111002:
                    case 35111005:
                    case 35111009:
                    case 35111010:
                    case 35111011:
                    case 35121009:
                    case 35121010:
                    case 35121011:
                        mplew.write(5);
                        break;
                    default:
                        mplew.write(4);
                        break;
                }
            } else {
                mplew.write(1);
            }

            return mplew.getPacket();
        }

        public static byte[] moveSummon(int cid, int oid, Point startPos, List<LifeMovementFragment> moves) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.MOVE_SUMMON.getValue());
            mplew.writeInt(cid);
            mplew.writeInt(oid);
            mplew.writeInt(0);
            mplew.writePos(startPos);
            mplew.writeInt(0);
            PacketHelper.serializeMovementList(mplew, moves);

            return mplew.getPacket();
        }

        public static byte[] summonAttack(int cid, int summonSkillId, byte animation, List<Pair<Integer, Integer>> allDamage, int level, boolean darkFlare) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SUMMON_ATTACK.getValue());
            mplew.writeInt(cid);
            mplew.writeInt(summonSkillId);
            mplew.write(level - 1);
            mplew.write(animation);
            mplew.write(allDamage.size());
            for (Pair attackEntry : allDamage) {
                mplew.writeInt(((Integer) attackEntry.left));
                mplew.write(7);
                mplew.writeInt(((Integer) attackEntry.right));
            }
            mplew.write(darkFlare ? 1 : 0);

            return mplew.getPacket();
        }

        public static byte[] pvpSummonAttack(int cid, int playerLevel, int oid, int animation, Point pos, List<AttackPair> attack) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PVP_SUMMON.getValue());
            mplew.writeInt(cid);
            mplew.writeInt(oid);
            mplew.write(playerLevel);
            mplew.write(animation);
            mplew.writePos(pos);
            mplew.writeInt(0);
            mplew.write(attack.size());
            for (AttackPair p : attack) {
                mplew.writeInt(p.objectid);
                mplew.writePos(p.point);
                mplew.write(p.attack.size());
                mplew.write(0);
                for (Pair atk : p.attack) {
                    mplew.writeInt(((Integer) atk.left));
                }
            }

            return mplew.getPacket();
        }

        public static byte[] summonSkill(int cid, int summonSkillId, int newStance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SUMMON_SKILL.getValue());
            mplew.writeInt(cid);
            mplew.writeInt(summonSkillId);
            mplew.write(newStance);

            return mplew.getPacket();
        }

        public static byte[] damageSummon(int cid, int summonSkillId, int damage, int unkByte, int monsterIdFrom) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.DAMAGE_SUMMON.getValue());
            mplew.writeInt(cid);
            mplew.writeInt(summonSkillId);
            mplew.write(unkByte);
            mplew.writeInt(damage);
            mplew.writeInt(monsterIdFrom);
            mplew.write(0);

            return mplew.getPacket();
        }
    }

    public static class UIPacket {

        public static byte[] getDirectionStatus(boolean enable) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.DIRECTION_STATUS.getValue());
            mplew.write(enable ? 1 : 0);

            return mplew.getPacket();
        }

        public static byte[] openUI(int type) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(6);

            mplew.writeShort(SendPacketOpcode.OPEN_UI.getValue());
            mplew.writeInt(type);

            return mplew.getPacket();
        }

        public static byte[] openUIOption(int type, int npc) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(14);

            mplew.writeShort(SendPacketOpcode.OPEN_UI_OPTION.getValue());
            mplew.writeInt(type);
            mplew.writeInt(npc);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] sendRedLeaf(int points, boolean viewonly) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(10);

            mplew.writeShort(SendPacketOpcode.OPEN_UI_OPTION.getValue());
            mplew.writeInt(0x73);
            mplew.writeInt(points);
            mplew.write(viewonly ? 1 : 0); //if view only, then complete button is disabled

            return mplew.getPacket();
        }

        public static byte[] DublStartAutoMove() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.MOVE_SCREEN.getValue());
            mplew.write(3);
            mplew.writeInt(2);

            return mplew.getPacket();
        }

        public static byte[] lockKey(boolean enable) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.LOCK_KEY.getValue());
            mplew.write(enable ? 1 : 0);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        // 1 Enable 0: Disable 
        public static byte[] lockUI(boolean enable) {
            return lockUI(enable ? 1 : 0, enable ? 1 : 0);
        }

        public static byte[] lockUI(int enable, int enable2) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.LOCK_UI.getValue());
            mplew.write(enable > 0 ? 1 : 0);
            if (enable > 0) {
                mplew.writeShort(enable2);
                //mplew.write(0);
            } 
            mplew.write(enable < 0 ? 1 : 0);
            
            return mplew.getPacket();
        }

        // 1 Enable 0: Disable 
        public static byte[] disableOthers(boolean enable) {
            return disableOthers(enable, enable ? 1 : 0);
        }

        public static byte[] disableOthers(boolean enable, int enable2) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.DISABLE_OTHERS.getValue());
            mplew.write(enable ? 1 : 0);
            if (enable) {
                mplew.writeShort(enable2);
                mplew.write(0);
            } else {
                mplew.write(!enable ? 1 : 0);
            }

            return mplew.getPacket();
        }

        public static byte[] summonHelper(boolean summon) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SUMMON_HINT.getValue());
            mplew.write(summon ? 1 : 0);

            return mplew.getPacket();
        }

        public static byte[] summonMessage(boolean isUI, int type, String message) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SUMMON_HINT_MSG.getValue());
            mplew.write(isUI ? 1 : 0);
            if (isUI) {
                mplew.writeInt(type);
                mplew.writeInt(7000);
            } else {
                mplew.writeMapleAsciiString(message);
                mplew.writeInt(200);
                mplew.writeInt(10000);
            }

            return mplew.getPacket();
        }

        public static byte[] getDirectionInfo(int type, int value) {
            return getDirectionInfo(type, value, 0);
        }

        public static byte[] getDirectionInfo(int type, int value, int value2) {
            return getDirectionEffect(type, "", new int[]{value, value2, 0, 0, 0, 0, 0, 0});
        }

        public static byte[] getDirectionInfo(String data, int value, int x, int y, int a, int b) {
            return getDirectionEffect(2, data, new int[]{value, x, y, a, b, 0, 0, 0});
        }

        public static byte[] getDirectionEffect(String data, int value, int x, int y) {
            return getDirectionEffect(data, value, x, y, 0);
        }

        public static byte[] getDirectionEffect(String data, int value, int x, int y, int npc) {
            //[02]  [02 00 31 31] [84 03 00 00] [00 00 00 00] [88 FF FF FF] [01] [00 00 00 00] [01] [29 C2 1D 00] [00] [00]
            //[mod] [data       ] [value      ] [value2     ] [value3     ] [a1] [a3         ] [a2] [npc        ] [  ] [a4]
            return getDirectionEffect(2, data, new int[]{value, x, y, 1, 1, 0, 0, npc});
        }

        public static byte[] getDirectionInfoNew(byte x, int value) {
            return getDirectionInfoNew(x, value, 0, 0);
        }

        public static byte[] getDirectionInfoNew(byte x, int value, int a, int b) {
            //[mod] [data] [value] [value2] [value3] [a1] ....
            return getDirectionEffect(5, "", new int[]{x, value, a, b, 0, 0, 0, 0});
        }
        //int value, int value2, int value3, int a1, int a2, int a3, int a4, int npc
        public static byte[] getDirectionEffect(int mod, String data, int[] value) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.DIRECTION_INFO.getValue());
            mplew.write(mod);
            switch (mod) {
                case 0:
                    mplew.writeInt(value[0]);
                    if (value[0] <= 0x4AB) {
                        mplew.writeInt(value[1]);
                    }
                    break;
                case 1:
                    mplew.writeInt(value[0]);
                    break;
                case 2:
                    mplew.writeMapleAsciiString(data);
                    mplew.writeInt(value[0]);   //tell client the delay
                    mplew.writeInt(value[1]);   //tell client the x of effect occuring
                    mplew.writeInt(value[2]);   //tell client the y of effect occuring
                    mplew.write(value[3]);
                    if (value[3] > 0) {
                        mplew.writeInt(value[5]);
                    }
                    mplew.write(value[4]);
                    if (value[4] > 0) {
                        mplew.writeInt(value[7]);
                        mplew.write(value[7] > 0 ? 0 : 1); // 暫時解決
                        mplew.write(value[6]);  
                    }
                    break;
                case 3:
                    mplew.writeInt(value[0]);
                    break;
                case 4:
                    mplew.writeMapleAsciiString(data);
                    mplew.writeInt(value[0]);
                    mplew.writeInt(value[1]);
                    mplew.writeInt(value[2]);
                    break;
                case 5:
                    mplew.write(value[0]);
                    mplew.writeInt(value[1]);
                    if (value[1] > 0) {
                        if (value[0] == 0) {
                            mplew.writeInt(value[2]);
                            mplew.writeInt(value[3]);
                        }
                    }
                    break;
                case 6:
                    mplew.write(value[0]);
                    break;
                case 7:
                    mplew.writeInt(value[0]);
                    mplew.writeInt(value[1]);
                    mplew.writeInt(value[2]);
                    mplew.writeInt(value[3]);
                    mplew.writeInt(value[4]);
                    break;
                case 8:
                    // CCameraWork::ReleaseCameraFromUserPoint
                    break;
                case 9://是否隱藏角色[1 - 隱藏, 0 - 顯示]
                    mplew.write(value[0]);
                    break;
                case 10:
                    mplew.writeInt(value[0]);
                    break;
                case 11:
                    mplew.writeMapleAsciiString(data);
                    mplew.write(value[0]);
                    break;
                case 12:
                    mplew.writeMapleAsciiString(data);
                    mplew.write(value[0]);
                    mplew.writeShort(value[1]);
                    mplew.writeInt(value[2]);
                    mplew.writeInt(value[3]);
                    break;
                case 13:
                    mplew.write(value[0]);
                    for (int i = 0; i >= value[0]; i++) {
                        mplew.writeInt(value[1]); // 要重寫
                    }
                    break;
                case 14:
                    break;
                case 15:
                    mplew.writeInt(value[0]);
                    mplew.writeInt(value[1]);
                    break;
                case 16:
                    mplew.write(value[0]);
                    break;
                case 17:
                    mplew.write(value[0]);
                    break;
                default:
                    System.out.println("CField.getDirectionInfo() is Unknow mod :: [" + mod + "]");
                    break;
            }

            return mplew.getPacket();
        }

        public static byte[] getDirectionFacialExpression(int expression, int duration) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.DIRECTION_FACIAL_EXPRESSION.getValue());
            mplew.writeInt(expression);
            mplew.writeInt(duration);
            mplew.write(0);

            /* Facial Expressions:
             * 0 - Normal 
             * 1 - F1
             * 2 - F2
             * 3 - F3
             * 4 - F4
             * 5 - F5
             * 6 - F6
             * 7 - F7
             * 8 - Vomit
             * 9 - Panic
             * 10 - Sweetness
             * 11 - Kiss
             * 12 - Wink
             * 13 - Ouch!
             * 14 - Goo goo eyes
             * 15 - Blaze
             * 16 - Star
             * 17 - Love
             * 18 - Ghost
             * 19 - Constant Sigh
             * 20 - Sleepy
             * 21 - Flaming hot
             * 22 - Bleh
             * 23 - No Face
             */
            return mplew.getPacket();
        }

        public static byte[] moveScreen(int x) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.MOVE_SCREEN_X.getValue());
            mplew.writeInt(x);
            mplew.writeInt(0);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] screenDown() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.MOVE_SCREEN_DOWN.getValue());

            return mplew.getPacket();
        }

        public static byte[] resetScreen() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.RESET_SCREEN.getValue());

            return mplew.getPacket();
        }

        public static byte[] reissueMedal(int itemId, int type) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.REISSUE_MEDAL.getValue());
            mplew.write(type);
            mplew.writeInt(itemId);

            return mplew.getPacket();
        }

        public static byte[] playMovie(String data, boolean show) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PLAY_MOVIE.getValue());
            mplew.writeMapleAsciiString(data);
            mplew.write(show ? 1 : 0);

            return mplew.getPacket();
        }

        public static byte[] setRedLeafStatus(int joejoe, int hermoninny, int littledragon, int ika) {
            //packet made to set status
            //should remove it and make a handler for it, it's a recv opcode
            /*
             * slea:
             * E2 9F 72 00
             * 5D 0A 73 01
             * E2 9F 72 00
             * 04 00 00 00
             * 00 00 00 00
             * 75 96 8F 00
             * 55 01 00 00
             * 76 96 8F 00
             * 00 00 00 00
             * 77 96 8F 00
             * 00 00 00 00
             * 78 96 8F 00
             * 00 00 00 00
             */
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            //mplew.writeShort();
            mplew.writeInt(7512034); //no idea
            mplew.writeInt(24316509); //no idea
            mplew.writeInt(7512034); //no idea
            mplew.writeInt(4); //no idea
            mplew.writeInt(0); //no idea
            mplew.writeInt(9410165); //joe joe
            mplew.writeInt(joejoe); //amount points added
            mplew.writeInt(9410166); //hermoninny
            mplew.writeInt(hermoninny); //amount points added
            mplew.writeInt(9410167); //little dragon
            mplew.writeInt(littledragon); //amount points added
            mplew.writeInt(9410168); //ika
            mplew.writeInt(ika); //amount points added

            return mplew.getPacket();
        }
    }

    public static class RunePacket {

        public static byte[] spawnRune(MapleRune rune, boolean respawn) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(respawn ? SendPacketOpcode.RESPAWN_RUNE.getValue() : SendPacketOpcode.SPAWN_RUNE.getValue());
            mplew.writeInt(1);
            mplew.writeInt(rune.getRuneType());
            mplew.writeInt(rune.getPositionX());
            mplew.writeInt(rune.getPositionY());
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] removeRune(MapleRune rune, MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.REMOVE_RUNE.getValue());
            mplew.writeInt(0);
            mplew.writeInt(chr.getId());

            return mplew.getPacket();
        }

        public static byte[] RuneAction(int type, int time) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.RUNE_ACTION.getValue());
            mplew.writeInt(type);
            mplew.writeInt(time);

            return mplew.getPacket();
        }

        public static byte[] showRuneEffect(int type) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.RUNE_EFFECT.getValue());
            mplew.writeInt(type);

            return mplew.getPacket();
        }
    }

    public static class ZeroPacket {

        public static byte[] gainWeaponPoint(int gain) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(0x20);
            mplew.writeInt(gain);

            return mplew.getPacket();
        }

        public static byte[] updateWeaponPoint(int wp) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ZERO_UPDATE.getValue());
            mplew.writeInt(wp);

            return mplew.getPacket();
        }

        public static byte[] UseWeaponScroll(int Success) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ZERO_SCROLL.getValue());
            mplew.writeShort(1);
            mplew.write(0);
            mplew.writeInt(Success);

            return mplew.getPacket();
        }

        public static byte[] UseWeaponScrollStart() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ZERO_SCROLL_START.getValue());

            return mplew.getPacket();
        }

        public static byte[] OpenWeaponUI(int type) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ZERO_RESULT.getValue());
            mplew.writeInt(type);
            mplew.writeInt((type == 1) ? 100000 : 50000);
            mplew.writeInt((type == 1) ? 600 : 500);
            mplew.write(0);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] OpenZeroUpgrade(int type, int level, int action, int weapon) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ZERO_UPGRADE.getValue());
            mplew.write(0);
            mplew.write(action);
            mplew.writeInt(type);
            mplew.writeInt(level);
            mplew.writeInt(weapon + 10001);
            mplew.writeInt(weapon + 1);

            return mplew.getPacket();
        }

        public static byte[] NPCTalk() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.NPC_TALK.getValue());
            mplew.write(3);
            mplew.writeInt(0);
            mplew.writeShort(0);
            mplew.writeShort(0x24);
            mplew.writeInt(2400010); // 2400009 남자, 2400010 여자
            mplew.writeMapleAsciiString("#face1#滾開！");
            mplew.write(HexTool.getByteArrayFromHexString("01 01"));
            mplew.writeInt(0);

            return mplew.getPacket();
        }
    }

    public static class EffectPacket {
        
        /*public static byte[] showBuffEffect(boolean self, MapleCharacter chr, int skillid, SpecialEffectType effect, int playerLevel, int skillLevel, byte direction) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            if (!self && chr != null) {
                mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
                mplew.writeInt(chr.getId());
            } else {
                mplew.writeShort(SendPacketOpcode.SHOW_SPECIAL_EFFECT.getValue());
            }
            mplew.write(effect.getValue());
            mplew.writeShort(0);
            mplew.writeInt(skillid);
            mplew.write(0);
            mplew.write(playerLevel - 1);
            mplew.write(skillLevel);
            if (direction != 3) {
                mplew.write(direction);
                if (!self && chr != null) {
                    switch (skillid) {
                        case 65121052:
                            mplew.writeInt(chr.getTruePosition().x);
                            mplew.writeInt(chr.getTruePosition().y);
                            mplew.write(1);
                    }
                }
            }

            return mplew.getPacket();
        }*/
        
        public static byte[] showEffect(boolean self, MapleCharacter chr, SpecialEffectType effect, int[] value, String[] str, Map<Integer, Integer> itemMap, Item[] items) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            /*if (chr == null) {
                mplew.writeShort(SendPacketOpcode.SHOW_SPECIAL_EFFECT.getValue());
            } else {
                mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
                mplew.writeInt(chr.getId());
            }*/
            if (!self && chr != null) {
                mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
                mplew.writeInt(chr.getId());
            } else {
                mplew.writeShort(SendPacketOpcode.SHOW_SPECIAL_EFFECT.getValue());
            }
            mplew.write((int) effect.getValue());

            switch (effect) { // 排序根據IDA
                // IF開始
                case WZ3:
                    mplew.writeMapleAsciiString(str[0]);
                    mplew.write(value[0]);
                    if (value[0] == 0) {
                        mplew.writeInt(value[1]);
                        mplew.writeInt(value[2]);
                        mplew.writeInt(value[3]);
                        break;
                    } else {
                        mplew.write(value[1]);
                        if (value[1] == 0) {
                            break;
                        } else {
                            mplew.write(value[2]);
                            mplew.writeInt(value[3]);
                            mplew.writeInt(value[4]);
                        }
                    }                    
                    break;
                case UNK_44:
                    mplew.writeMapleAsciiString(str[0]);
                    mplew.writeInt(value[0]);
                    mplew.writeInt(value[1]);
                    mplew.writeInt(value[2]);
                    mplew.writeInt(value[2]);
                    mplew.writeInt(value[0]);
                    mplew.writeInt(value[1]);
                    mplew.writeInt(value[2]);
                    mplew.writeInt(value[2]);
                    break;
                case NPC_BUBBLE:
                    mplew.write(value[0]);
                    mplew.writeInt(value[1]);
                    mplew.writeInt(value[2]);
                    mplew.writeMapleAsciiString(str[0]);
                    mplew.writeInt(value[3]);
                    mplew.writeInt(value[4]);
                    mplew.writeInt(value[5]);
                    mplew.writeInt(value[6]);
                    mplew.writeInt(value[7]);
                    mplew.writeInt(value[8]);
                    mplew.writeInt(value[9]);//NPCID
                    break;
                case UNK_49:
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case ITEM_TOP_MSG:
                    PacketHelper.addItemInfo(mplew, items[0], null);
                    break;
                case DARK:
                    mplew.write(value[0]);
                    break;
                case WZ:
                    mplew.write(value[0]);
                    mplew.writeInt(value[1]);
                    mplew.writeInt(value[2]);
                    mplew.writeMapleAsciiString(str[0]);
                    break;
                case WZ2:
                    mplew.writeMapleAsciiString(str[0]);
                    break;
                case UNK_1D:
                    mplew.write(value[0]);
                    if (value[0] != 0) {
                        mplew.writeMapleAsciiString(str[0]);
                        mplew.writeInt(value[1]);
                        mplew.writeInt(value[2]);
                    }
                    break;
                case SOUND:
                    mplew.writeMapleAsciiString(str[0]);
                    break;
                case UNK_20:
                    mplew.writeMapleAsciiString(str[0]);
                    break;
                case BLACK_BACKGROUND:
                    mplew.writeInt(value[0]);
                    mplew.writeInt(value[1]);
                    mplew.writeInt(value[2]);
                    mplew.write(value[3]);
                    break;
                // IF結束 SWITCH 開始
                case UNK_32:
                case LEVEL_UP:
                    // Nothing...
                    break;
                case LOCAL_SKILL:
                case REMOTE_SKILL:
                    // This is a long code
                    break;
                case ZERO:
                    int skill = 0;
                    mplew.writeInt(value[0]);
                    mplew.write(value[1]);
                    if (skill == 25121006 || skill == 31111003) {
                        mplew.writeInt(value[2]);
                    }
                    break;
                case MECHANIC:
                    mplew.writeInt(value[0]);
                    mplew.write(value[1]);
                    mplew.writeInt(value[2]);
                    mplew.writeInt(value[3]);
                    break;
                case UNK_6:
                    mplew.writeInt(value[0]);
                    mplew.write(value[1]);
                    break;
                case SHOW_DICE:
                    mplew.writeInt(value[0]);
                    mplew.writeInt(value[1]);
                    mplew.writeInt(value[2]);
                    mplew.write(value[3]);
                    mplew.write(value[4]);
                    break;
                case SKILL_FLYING_OBJECT:
                    mplew.writeInt(value[0]);
                    if (value[0] == 11121013 || (int) value[0] == 12100029 || (int) value[0] == 13121009 || value[0] == 36110005 || value[0] == 65101006) {
                        mplew.writeInt(value[1]);
                        mplew.writeInt(value[2]);
                        mplew.writeInt(value[3]);
                    }
                    if (value[0] == 32111016) {
                        mplew.write(value[1]);
                        mplew.write(value[2]);
                        mplew.writeInt(value[3]);
                        mplew.writeInt(value[4]);
                        mplew.writeInt(value[5]);
                        mplew.writeInt(value[6]);
                    }
                    break;
                case FIRE:
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case RESIST:
                    // Nothing...
                    break;
                case ITEM_OPERATION:
                    mplew.write(itemMap.size());
                    int v3 = 0;
                    if (itemMap.size() <= v3) {
                        mplew.writeMapleAsciiString("");
                        mplew.writeInt(0);
                    }
                    itemMap.entrySet().forEach((item) -> {
                        mplew.writeInt(item.getKey());
                        mplew.writeInt(item.getValue());
                    });
                    break;
                case UNK_3C:
                    mplew.write(0);
                    mplew.write(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case PET_LEVEL_UP:
                    mplew.write(value[0]);
                    mplew.writeInt(value[1]);
                    if (chr == null) {
                        mplew.writeInt(value[2]);
                    }
                    break;
                case USE_AMULET:
                    mplew.writeInt(value[0]); // 1 = 護身符, 2 = 紡織輪, 4 = 戰鬥機器人
                    mplew.write(value[1]); // 剩餘次數
                    mplew.write(value[2]);
                    if (value[0] != 1 || value[0] != 2 || value[0] != 4) {
                        mplew.writeInt(value[3]); // Unk
                    }
                    break;
                case UNK_C:
                    mplew.writeInt(0);
                    break;
                case UNK_26:
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.write(0);
                    break;
                case MULUNG_DOJO_UP:
                case CHANGE_JOB:
                case QUET_COMPLETE:
                    // Nothing...
                    break;
                case UNK_41:
                    // if (fun??)
                    mplew.writeShort(0);
                    break;
                case HEALED_BYTE:
                    mplew.write(value[0]);
                    break;
                case CASH_ITEM:
                    mplew.writeInt(value[0]);
                    break;
                case UNK_4B:
                    mplew.writeInt(0);
                    mplew.writeMapleAsciiString("");
                    break;
                case HEALED_INT:
                    mplew.writeInt(value[0]);
                    break;
                case UNK_23:
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case UNK_11:
                    mplew.writeInt(0);
                    break;
                case UNK_12:
                    mplew.writeMapleAsciiString("");
                    break;
                case REWARD_ITEM:
                    mplew.writeInt(0);
                    mplew.write(str != null && str.length > 0 && str[0].length() > 0);
                    if (str != null && str.length > 0 && str[0].length() > 0) {
                        mplew.writeMapleAsciiString(str[0]);
                    }
                    break;
                case UNK_13:
                    // Nothing...
                    break;
                case ITEM_LEVEL_UP:
                    // Nothing...
                    break;
                case ITEM_MAKER_SUCCESS:
                    mplew.writeInt(0);
                    break;
                case DODGE_CHANCE:
                    // Nothing...
                    break;
                case UNK_18:
                    mplew.writeInt(value[0]);
                    break;
                case UNK_21:
                    break;
                /*case WZ:
                    mplew.write(value[0]);
                    mplew.writeInt(value[1]);
                    mplew.write(value[2]);
                    mplew.writeZeroBytes(3);
                    mplew.writeMapleAsciiString(str[0]);
                    break;*/
                case RESURRECTION_INFO:
                    mplew.write(value[0]);
                    break;
                case UNK_1B:
                    //if?
                    mplew.writeMapleAsciiString(str[0]);
                    break;
                case UNK_1E:
                    mplew.writeInt(value[0]);
                    mplew.writeMapleAsciiString(str[0]);
                    break;
                case CHAMPION:
                    mplew.writeInt(value[0]);
                    break;
                case UNK_27:
                case UNK_28:
                case UNK_29:
                case UNK_2A:
                case UNK_2B:
                case UNK_2C:
                    // Nothing...
                    break;
                case CRAFTING:
                    mplew.writeMapleAsciiString(str[0]);
                    mplew.write((byte) value[0]);
                    mplew.writeInt(value[1]);
                    mplew.writeInt(value[2]);
                    if (value[2] == 2) {
                        mplew.writeInt(value[3]);
                    }
                    break;
                case UNK_2E:
                    mplew.writeInt(0);
                    break;
                case UNK_2F:
                    mplew.writeInt(0);
                    break;
                case UNK_30:
                    // Nothing...
                    break;
                case UNK_31:
                    // Nothing...
                    break;
                case UNSEAL_BOX:
                    mplew.writeInt(value[0]);
                    mplew.writeInt(value[1]);
                    break;
                case UNK_35:
                    mplew.write(value[0]);
                    break;
                case UNK_37:
                    mplew.writeInt(value[0]);
                    mplew.writeInt(value[1]);
                    break;
                case ANGELIC_RECHARGE:
                    // Nothing...
                    break;
                case UNK_39:
                    // This is a long code...
                    break;
                case UNK_3A:
                    mplew.writeInt(value[0]);
                    break;
                case UNK_3B:
                    // Nothing...
                    break;
                case UNK_3D:
                    mplew.writeInt(0);
                    break;
                case UNK_3F:
                    boolean res3f = false;
                    mplew.writeBoolean(res3f);
                    if (!res3f) {
                        mplew.writeInt(0);
                        mplew.write(0);
                    }
                    break;
                case UNK_40:
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case UNK_42:
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case UNK_46:
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case UNK_47:
                    mplew.writeInt(0);
                    mplew.writeInt(0);
                    break;
                case UNK_48:
                    mplew.writeShort(0);
                    mplew.writeInt(0);
                    mplew.write(0);
                    mplew.write(0);
                    mplew.write(0);
                    break;
            }
            return mplew.getPacket();
        }

        public static byte[] showLevelupEffect(MapleCharacter chr) {
            return showEffect(chr == null, chr, SpecialEffectType.LEVEL_UP, null, null, null, null);
        }

        public static byte[] showDiceEffect(int skillid, int effectid, int effectid2, int level) {
            return showDiceEffect(null, skillid, effectid, effectid2, level);
        }

        public static byte[] showDiceEffect(MapleCharacter chr, int skillid, int effectid, int effectid2, int level) {
            return showEffect(chr == null, chr, SpecialEffectType.SHOW_DICE, new int[]{effectid, effectid2, skillid, level, 0}, null, null, null);
        }

        public static byte[] getShowItemGain(Map<Integer, Integer> items) {
            return showEffect(true, null, SpecialEffectType.ITEM_OPERATION, null, null, items, null);
        }

        public static byte[] showPetLevelUp(byte index) {
            return showPetLevelUp(null, index);
        }

        public static final byte[] showPetLevelUp(MapleCharacter chr, byte index) {
            return showEffect(chr == null, chr, SpecialEffectType.PET_LEVEL_UP, new int[]{0, index, 0}, null, null, null);
        }

        public static byte[] showBlackBlessingEffect(MapleCharacter chr, int value) {
            return showEffect(chr == null, chr, SpecialEffectType.SKILL_FLYING_OBJECT, new int[]{value, 0, 0, 0, 0, 0, 0, 0}, null, null, null);
        }

        public static byte[] useAmulet(int amuletType, byte timesleft, byte daysleft) {
            return showEffect(true, null, SpecialEffectType.USE_AMULET, new int[]{amuletType, timesleft, daysleft, 0}, null, null, null);
        }

        public static byte[] Mulung_DojoUp() {
            return showEffect(true, null, SpecialEffectType.MULUNG_DOJO_UP, null, null, null, null);
        }

        public static byte[] showJobChangeEffect(MapleCharacter chr) {
            return showEffect(chr == null, chr, SpecialEffectType.CHANGE_JOB, null, null, null, null);
        }

        public static byte[] showQuetCompleteEffect() {
            return showQuetCompleteEffect(null);
        }

        public static byte[] showQuetCompleteEffect(MapleCharacter chr) {
            return showEffect(chr == null, chr, SpecialEffectType.QUET_COMPLETE, null, null, null, null);
        }

        public static byte[] showHealed(int amount) {
            return EffectPacket.showHealed(null, amount);
        }

        public static byte[] showHealed(MapleCharacter chr, int amount) {
            return showEffect(chr == null, chr, SpecialEffectType.HEALED_INT, new int[]{amount}, null, null, null);
        }

        public static byte[] showMonsterBookEffect() {
            return showEffect(true, null, SpecialEffectType.UNK_13, null, null, null, null);
        }

        public static byte[] showRewardItemAnimation(int itemId, String effect) {
            return showRewardItemAnimation(itemId, effect, null);
        }

        public static byte[] showRewardItemAnimation(int itemId, String effect, MapleCharacter chr) {
            return showEffect(chr == null, chr, SpecialEffectType.REWARD_ITEM, new int[]{itemId}, new String[]{effect}, null, null);
        }

        public static byte[] showItemLevelupEffect() {
            return showItemLevelupEffect(null);
        }

        public static byte[] showItemLevelupEffect(MapleCharacter chr) {
            return showEffect(chr == null, chr, SpecialEffectType.ITEM_LEVEL_UP, null, null, null, null);
        }

        public static byte[] ItemMaker_Success() {
            return ItemMaker_Success(null);
        }

        public static byte[] ItemMaker_Success(MapleCharacter chr) {
            return showEffect(chr == null, chr, SpecialEffectType.ITEM_MAKER_SUCCESS, null, null, null, null);
        }

        public static byte[] showDodgeChanceEffect() {
            return showEffect(true, null, SpecialEffectType.DODGE_CHANCE, null, null, null, null);
        }

        public static byte[] showWZEffect(String data) {
            return showEffect(true, null, SpecialEffectType.WZ, new int[]{0, 0, 0}, new String[]{data}, null, null);
        }

        public static byte[] showWZEffectNew(String data) {
            return showEffect(true, null, SpecialEffectType.WZ2, null, new String[]{data}, null, null);
        }

        public static byte[] TutInstructionalBalloon(String data) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_SPECIAL_EFFECT.getValue());
            mplew.write(SpecialEffectType.WZ2.getValue());//?
            mplew.writeMapleAsciiString(data);
            mplew.writeInt(1);

            return mplew.getPacket();
        }

        public static byte[] playSoundEffect(String data) {
            return showEffect(true, null, SpecialEffectType.SOUND, null, new String[]{data}, null, null);
        }
        
        public static byte[] showCashItemEffect(int itemId) {
            return showEffect(true, null, SpecialEffectType.CASH_ITEM, new int[]{itemId}, null, null, null);
        }

        public static byte[] showChampionEffect() {
            return EffectPacket.showChampionEffect(null);
        }

        public static byte[] showChampionEffect(MapleCharacter chr) {
            return showEffect(true, null, SpecialEffectType.CHAMPION, new int[]{0x7530}, null, null, null);
        }

        public static byte[] showCraftingEffect(String effect, byte direction, int time, int mode) {
            return EffectPacket.showCraftingEffect(null, effect, direction, time, mode);
        }

        public static byte[] showCraftingEffect(MapleCharacter chr, String effect, byte direction, int time, int mode) {
            return showEffect(true, null, SpecialEffectType.CRAFTING, new int[]{direction, time, mode, 0}, new String[]{effect}, null, null);
        }

        public static byte[] showWeirdEffect(String effect, int itemId) {
            return showWeirdEffect(null, effect, itemId);
        }

        public static byte[] showWeirdEffect(MapleCharacter chr, String effect, int itemId) {
            return showEffect(true, null, SpecialEffectType.CRAFTING, new int[]{1, 0, 2, itemId}, new String[]{effect}, null, null);
        }
        
        public static byte[] showBlackBGEffect(int value, int value2, int value3, byte value4) {
            return showEffect(true, null, SpecialEffectType.BLACK_BACKGROUND, new int[]{value, value2, value3, value4}, null, null, null);
        }
        
        public static byte[] unsealBox(int reward) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_SPECIAL_EFFECT.getValue());
            mplew.write(SpecialEffectType.UNSEAL_BOX.getValue());
            mplew.write(1);
            mplew.writeInt(reward);
            mplew.writeInt(1);

            return mplew.getPacket();
        }

        public static byte[] showDarkEffect(boolean dark) {
            return showEffect(true, null, SpecialEffectType.DARK, new int[]{dark ? 1 : 0}, null, null, null);
        }

        public static byte[] showRechargeEffect() {
            return showEffect(true, null, SpecialEffectType.ANGELIC_RECHARGE, null, null, null, null);
        }

        public static byte[] showWZEffect3(String data, int[] value) {
            return showEffect(true, null, SpecialEffectType.WZ3, value, new String[]{data}, null, null);
        }
        
        public static byte[] showFireEffect(MapleCharacter chr) {
            return showEffect(chr == null, chr, SpecialEffectType.FIRE, null, null, null, null);
        }

        public static byte[] showItemTopMsgEffect(Item item) {
            return showEffect(true, null, SpecialEffectType.ITEM_TOP_MSG, null, null, null, new Item[]{item});
        }
        
        /*public static byte[] showBuffEffect(int skillid, SpecialEffectType effect, int playerLevel, int skillLevel) {
            return showBuffeffect(-1, skillid, effect, playerLevel, skillLevel, (byte) 3);
        }

        public static byte[] showBuffEffect(int skillid, SpecialEffectType effect, int playerLevel, int skillLevel, byte direction) {
            return showBuffeffect(-1, skillid, effect, playerLevel, skillLevel, direction);
        }

        public static byte[] showBuffeffect(int cid, int skillid, SpecialEffectType effect, int playerLevel, int skillLevel) {
            return showBuffeffect(cid, skillid, effect, playerLevel, skillLevel, (byte) 3);
        }*/
        
        public static byte[] showBuffEffect(boolean self, MapleCharacter chr, int skillid, SpecialEffectType effect, int playerLevel, int skillLevel) {
            return showBuffEffect(self, chr, skillid, effect, playerLevel, skillLevel, (byte) 3);
        }
        
        /*public static byte[] showBuffeffect(int cid, int skillid, SpecialEffectType effect, int playerLevel, int skillLevel, byte direction) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            if (cid == -1) {
                mplew.writeShort(SendPacketOpcode.SHOW_SPECIAL_EFFECT.getValue());
            } else {
                mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
                mplew.writeInt(cid);
            }
            mplew.write(effect.getValue());
            mplew.writeShort(0);
            mplew.writeInt(skillid);
            mplew.write(0);
            mplew.write(playerLevel);
            mplew.write(skillLevel);
            if (direction != 3) {
                mplew.write(direction);
            }
            //if ((effectid == 2) && (skillid == 31111003)) {
             mplew.writeInt(0);
             }
             mplew.write(skillLevel);
             if ((direction != 3) || (skillid == 1320006) || (skillid == 30001062) || (skillid == 30001061)) {
             mplew.write(direction);
             }

             if (skillid == 30001062) {
             mplew.writeInt(0);
             }
             if (cid == -1) {
             mplew.writeZeroBytes(10);
             }
             mplew.writeZeroBytes(20);
             //
            return mplew.getPacket();
        }
    }*/
        
        public static byte[] showBuffEffect(boolean self, MapleCharacter chr, int skillid, SpecialEffectType effect, int playerLevel, int skillLevel, byte direction) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            if (!self && chr != null) {
                mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
                mplew.writeInt(chr.getId());
            } else {
                mplew.writeShort(SendPacketOpcode.SHOW_SPECIAL_EFFECT.getValue());
            }
            mplew.writeInt(skillid);
            mplew.write(playerLevel - 1);
            mplew.write(skillLevel);
            mplew.write(direction);
            if (skillid == 4331006) {
                mplew.writeInt(0);
            }
            if ( skillid == 3211010 || skillid == 3111010 || skillid == 1100012 )
            {
                mplew.write(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (skillid == 30001062) {
                mplew.write(0);
                mplew.writeShort(0);
                mplew.writeShort(0);
            }
            if (skillid == 30001061 || skillid == 80001132) {
                mplew.write(0);
            }
            if ( skillid == 60001218 || skillid == 60011218 ){
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if ( skillid == 11121013 || skillid == 12100029 || skillid == 13121009 || skillid == 36110005 || skillid == 65101006){
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (skillid == 32111016) {
                mplew.write(0);
                mplew.write(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (skillid == 4221052 || skillid == 65121052) {
                if (!self && chr != null) {
                    mplew.writeInt(chr.getTruePosition().x);
                    mplew.writeInt(chr.getTruePosition().y);
                    mplew.write(1);
                }                
            }
            
            mplew.writeZeroBytes(20); // skip error 38 

            return mplew.getPacket();
        }
    }
    
    public static byte[] updateDeathCount(int deathCount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DEATH_COUNT.getValue());
        mplew.writeInt(deathCount);

        return mplew.getPacket();
    }

    public static byte[] enchantResult(int result, int itemId) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.STRENGTHEN_UI.getValue());
        mplew.writeInt(result);//0=fail/1=sucess/2=idk/3=shows stats
        mplew.writeInt(itemId);
        return mplew.getPacket();
    }

    public static byte[] sendSealedBox(short slot, int itemId, List<Integer> items) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SEALED_BOX.getValue());
        mplew.writeShort(slot);
        mplew.writeInt(itemId);
        mplew.writeInt(items.size());
        for (int item : items) {
            mplew.writeInt(item);
        }

        return mplew.getPacket();
    }

    public static byte[] getRandomResponse(MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.RANDOM_RESPONSE.getValue());
        mplew.write(12);
        mplew.writeShort(1);
        mplew.writeLong(1);
        mplew.writeInt(100);
        mplew.writeInt(GameConstants.getCurrentDate());

        return mplew.getPacket();
    }

    public static byte[] getCassandrasCollection() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CASSANDRAS_COLLECTION.getValue());
        mplew.write(6);

        return mplew.getPacket();
    }

    public static byte[] getLuckyLuckyMonstory() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LUCKY_LUCKY_MONSTORY.getValue());
        mplew.writeShort(1);
        mplew.write(30);

        return mplew.getPacket();
    }

    public static void addMountId(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, int buffSrc) {
        Item c_mount = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -123);
        Item mount = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -18);
        int mountId = GameConstants.getMountItem(buffSrc, chr);
        if ((mountId == 0) && (c_mount != null) && (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -124) != null)) {
            mplew.writeInt(c_mount.getItemId());
        } else if ((mountId == 0) && (mount != null) && (chr.getInventory(MapleInventoryType.EQUIPPED).getItem((short) -19) != null)) {
            mplew.writeInt(mount.getItemId());
        } else {
            mplew.writeInt(mountId);
        }
    }

    public static byte[] harvestResultEffect(int cid, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.HARVESTED_EFFECT.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(0);
        mplew.write(0);

        return mplew.getPacket();
    }
    
    public static byte[] CurentMapWarp(Point pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CURRENT_MAP_WARP.getValue());
        mplew.write(0);
        mplew.write(2);
        mplew.writeInt(6850036);
        mplew.writePos(pos);

        return mplew.getPacket();
    }
}
