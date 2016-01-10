package tools.packet;

import client.MapleCharacter;
import handling.SendPacketOpcode;
import java.util.List;
import server.MapleCarnivalParty;
import tools.data.MaplePacketLittleEndianWriter;

public class MonsterCarnivalPacket {

    public static byte[] startMonsterCarnival(final MapleCharacter chr, final int enemyavailable, final int enemytotal) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MONSTER_CARNIVAL_START.getValue());
        final MapleCarnivalParty friendly = chr.getCarnivalParty();
        mplew.write(friendly.getTeam());
        mplew.writeInt(chr.getAvailableCP());
        mplew.writeInt(chr.getTotalCP());
        mplew.writeInt(friendly.getAvailableCP());
        mplew.writeInt(friendly.getTotalCP());

        return mplew.getPacket();
    }

    public static byte[] playerDiedMessage(String name, int lostCP, int team) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MONSTER_CARNIVAL_DIED.getValue());
        mplew.write(team);
        mplew.writeMapleAsciiString(name);
        mplew.write(lostCP);

        return mplew.getPacket();
    }

    public static byte[] playerLeaveMessage(boolean leader, String name, int team) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MONSTER_CARNIVAL_LEAVE.getValue());
        mplew.write(leader ? 7 : 0);
        mplew.write(team); // 0: Maple Red, 1: Maple Blue
        mplew.writeMapleAsciiString(name);

        return mplew.getPacket();
    }

    public static byte[] CPUpdate(boolean party, int curCP, int totalCP, int team) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MONSTER_CARNIVAL_OBTAINED_CP.getValue());
        mplew.writeInt(curCP);
        mplew.writeInt(totalCP);

        return mplew.getPacket();
    }

    public static byte[] showMCStats(int left, int right) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MONSTER_CARNIVAL_STATS.getValue());
        mplew.writeInt(left);
        mplew.writeInt(right);

        return mplew.getPacket();
    }

    public static byte[] playerSummoned(String name, int tab, int number) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MONSTER_CARNIVAL_SUMMON.getValue());
        mplew.write(tab);
        mplew.write(number);
        mplew.writeMapleAsciiString(name);

        return mplew.getPacket();
    }

    public static byte[] showMCResult(int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MONSTER_CARNIVAL_RESULT.getValue());
        mplew.write(mode);

        return mplew.getPacket();
    }

    public static byte[] showMCRanking(List<MapleCharacter> players) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MONSTER_CARNIVAL_RANKING.getValue());
        mplew.writeShort(players.size());
        for (MapleCharacter i : players) {
            mplew.writeInt(i.getId());
            mplew.writeMapleAsciiString(i.getName());
            mplew.writeInt(10); // points
            mplew.write(0); // team
        }

        return mplew.getPacket();
    }

    /**
     * 怪物擂臺賽訊息
     *
     * Possible values for <code>message</code>:<br>
     * 1: You don't have enough CP to continue. 
     * 2: You can no longer summon the Monster. 
     * 3: You can no longer summon the being. 
     * 4: This being is already summoned. 
     * 5: This request has failed due to an unknown error.
     *
     * @param message Displays a message inside Carnival PQ
     * @return
     *
     */
    public static byte[] CPQMessage(byte message) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MONSTER_CARNIVAL_MESSAGE.getValue());
        mplew.write(message);

        return mplew.getPacket();
    }
}
