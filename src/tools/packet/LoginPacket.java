package tools.packet;

import client.MapleCharacter;
import client.MapleClient;
import client.MapleJob;
import client.PartTimeJob;
import constants.GameConstants;
import constants.JobConstants;
import constants.JobConstants.LoginJob;
import constants.ServerConfig;
import constants.ServerConstants;
import constants.WorldConstants;
import handling.SendPacketOpcode;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import java.io.File;
import java.util.List;
import java.util.Set;
import server.Randomizer;
import tools.DateUtil;
import tools.HexTool;
import tools.Triple;
import tools.data.MaplePacketLittleEndianWriter;

public class LoginPacket {

    public static byte[] getHello(byte[] sendIv, byte[] recvIv) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(0x0E);
        mplew.writeShort(ServerConstants.MAPLE_VERSION);
        mplew.writeMapleAsciiString(ServerConstants.MAPLE_PATCH);
        if (ServerConfig.USE_FIXED_IV) {
            mplew.write(HexTool.getByteArrayFromHexString("73 6F 72 72 69 20 77 65 20 68 61 73 20 61 6E 74 69 73 6E 69 66 66 3D 5D"));
        } else {
            mplew.write(recvIv);
            mplew.write(sendIv);
        }
        mplew.write(ServerConstants.MAPLE_TYPE.getType());

        return mplew.getPacket();
    }

    public static final byte[] showMapleStory() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(6);

        mplew.writeShort(SendPacketOpcode.SHOW_MAPLESTORY.getValue());
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static final byte[] getPing() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(2);

        mplew.writeShort(SendPacketOpcode.PING.getValue());

        return mplew.getPacket();
    }

    public static byte[] getLoginBackground() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LOGIN_AUTH.getValue());
        String[] bg = {"MapLogin", "MapLogin0", "MapLogin1", "MapLogin2"};
        mplew.writeMapleAsciiString(bg[(int) (Math.random() * bg.length)]);
        mplew.writeInt(GameConstants.getCurrentDate());
        mplew.write(1);

        return mplew.getPacket();
    }

    public static byte[] getAuthSuccessRequest(MapleClient client) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        getAuthSuccess(mplew, client, false);

        return mplew.getPacket();
    }

    public static final byte[] getSecondAuthSuccess(MapleClient client) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ACCOUNT_INFO.getValue());
        getAuthSuccess(mplew, client, true);

        return mplew.getPacket();
    }

    public static final void getAuthSuccess(MaplePacketLittleEndianWriter mplew, MapleClient client, boolean second) {
        mplew.write(0);
        mplew.writeInt(client.getAccID());
        mplew.write(client.getGender());
        mplew.write(client.isGM() ? 1 : 0);
        mplew.writeInt(client.isGM() ? 0x10 : 0);//gm stuff　0x10 = 8588, 0x20 = 8596, 0x2000 = 8600
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeInt(0x21);
        mplew.write(0);
        mplew.write(0); // 1 = 帳號禁止說話
        mplew.writeLong(0); // 禁止說話期限
        mplew.write(0);
        mplew.writeLong(0);
        if (second) {
            mplew.writeMapleAsciiString(client.getAccountName());
        } else {
            mplew.write(0); // Boolean
        }
        mplew.writeMapleAsciiString(client.getAccountName());
        mplew.writeMapleAsciiString("");
        mplew.write(JobConstants.enableJobs);
        if (JobConstants.enableJobs) {
            mplew.write(JobConstants.jobOrder);
            for (LoginJob j : LoginJob.values()) {
                mplew.write(j.enableCreate());
                mplew.writeShort(1);
            }
        }
        mplew.write(0);//176+
        mplew.writeInt(-1);//176+
        mplew.write(0);
    }

    public static final byte[] getLoginFailed(int reason) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);

        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.write(reason);

        return mplew.getPacket();
    }
    /*
     * location: UI.wz/Login.img/Notice/text
     * reasons:
     * useful:
     * 32 - server under maintenance check site for updates
     * 35 - your computer is running thirdy part programs close them and play again
     * 36 - due to high population char creation has been disabled
     * 43 - revision needed your ip is temporary blocked
     * 75-78 are cool for auto register
     
     */

    public static byte[] getPermBan(byte reason) {//TO DO
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(16);

        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.write(2);
        mplew.write(0);
        mplew.writeInt(0);
        mplew.writeShort(reason);
        mplew.write(HexTool.getByteArrayFromHexString("01 01 01 01 00"));

        return mplew.getPacket();
    }

    public static byte[] getTempBan(long timestampTill, byte reason) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(17);

        mplew.writeShort(SendPacketOpcode.LOGIN_STATUS.getValue());
        mplew.write(2);
        mplew.write(reason);
        mplew.writeLong(timestampTill);

        return mplew.getPacket();
    }

    public static byte[] getWorldSelected(MapleClient c) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        byte lastWorld = c.getAccountWorld(c.getAccountName());
        mplew.writeShort(SendPacketOpcode.ENABLE_RECOMMENDED.getValue());
        mplew.writeInt(lastWorld == 0 ? -3 : lastWorld);

        return mplew.getPacket();
    }

    public static final byte[] deleteCharResponse(int cid, int state) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DELETE_CHAR_RESPONSE.getValue());
        mplew.writeInt(cid);
        mplew.write(state);

        return mplew.getPacket();
    }

    public static final byte[] createCharResponse(int state) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CREATE_CHAR_RESPONSE.getValue());
        mplew.write(state);
        switch (state) {
            case 0x0:
                mplew.write(JobConstants.enableJobs);
                if (JobConstants.enableJobs) {
                    mplew.write(JobConstants.jobOrder);
                    for (LoginJob j : LoginJob.values()) {
                        mplew.write(j.enableCreate());
                        mplew.writeShort(1);
                    }
                }
                break;
        }

        return mplew.getPacket();
    }

    public static final byte[] createCharCheckCode(File file, byte value1, byte value2, byte value3, byte value4) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CREATE_CHAR_RESPONSE.getValue());
        mplew.write(0x45);
        mplew.write(value1);
        mplew.write(value2);
        mplew.write(value3);
        mplew.write(value4);
        mplew.writeFile(file);

        return mplew.getPacket();
    }

    public static byte[] secondPwError(byte mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);

        mplew.writeShort(SendPacketOpcode.SECONDPW_ERROR.getValue());
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] sendAuthResponse(int response) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.AUTH_RESPONSE.getValue());
        mplew.writeInt(response);

        return mplew.getPacket();
    }

    public static byte[] enableRecommended(int world) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ENABLE_RECOMMENDED.getValue());
        mplew.writeInt(world);

        return mplew.getPacket();
    }

    public static byte[] sendRecommended(int world, String message) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SEND_RECOMMENDED.getValue());
        mplew.write(message != null ? 1 : 0);
        if (message != null) {
            mplew.writeInt(world);
            mplew.writeMapleAsciiString(message);
        }
        return mplew.getPacket();
    }

    public static byte[] ResetScreen() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.RESET_SCREEN.getValue());
        mplew.write(HexTool.getByteArrayFromHexString("02 08 00 32 30 31 32 30 38 30 38 00 08 00 32 30 31 32 30 38 31 35 00"));

        return mplew.getPacket();
    }

    public static byte[] getServerList(WorldConstants.Option world) {

        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERLIST.getValue());
        mplew.write(world.getWorld());
        mplew.writeMapleAsciiString(LoginServer.getServerName());
        mplew.write(world.getFlag());
        mplew.write(0);
        mplew.writeMapleAsciiString(world.getWorldTip());
        mplew.writeShort(100);
        mplew.writeShort(100);
        int lastChannel = 1;
        Set<Integer> channels = ChannelServer.getAllInstance();
        for (int i = world.getChannelCount(); i > 0; i--) {
            if (channels.contains(i)) {
                lastChannel = i;
                break;
            }
        }
        mplew.write(lastChannel);
        for (int i = 1; i <= lastChannel; i++) {
            int load;
            if (channels.contains(i)) {
                load = ChannelServer.getInstance(i).getPlayerStorage().pendingCharacterSize(world.getWorld());
            } else {
                load = 1200;
            }
            mplew.writeMapleAsciiString(world.name() + "-" + i);
            mplew.writeInt(Math.max(load * 55 / ServerConfig.CHANNEL_MAX_CHAR_VIEW, 1));
            mplew.write(world.getWorld());
            mplew.write(i - 1);
            mplew.write(0);
        }
        mplew.writeShort(0);
        mplew.writeInt(0);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] getEndOfServerList() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERLIST.getValue());;
        mplew.write(-1);
        mplew.write(0);
        mplew.write(0); // Boolean [0:-、1:遊戲帳號保護政策]
        mplew.write(0);

        return mplew.getPacket();
    }

    /*
     public static final byte[] getLoginWelcome() {
     List flags = new LinkedList();

     return CField.spawnFlags(flags);
     }
     */
    public static byte[] getServerStatus(int status) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        /* 類型(status)：
         * [1] 您所選擇的伺服器人數較多，建議您選擇其他伺服器來創建角色或進行遊戲！
         * [2] 您所選擇的伺服器人數已滿，請您選擇其他伺服器來創建角色或進行遊戲！
         */
        mplew.writeShort(SendPacketOpcode.SERVERSTATUS.getValue());
        mplew.write(status);

        return mplew.getPacket();
    }

    public static byte[] changeBackground(List<Triple<String, Integer, Boolean>> backgrounds) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHANGE_BACKGROUND.getValue());
        mplew.write(backgrounds.size()); //number of bgs
        for (Triple<String, Integer, Boolean> background : backgrounds) {
            mplew.writeMapleAsciiString(background.getLeft());
            mplew.write(background.getRight() ? Randomizer.nextInt(2) : background.getMid());
        }
        /* 
         Map.wz/Obj/login.img/WorldSelect/background/background number
         Backgrounds ids sometime have more than one background anumation
         Background are like layers, backgrounds in the packets are
         removed, so the background which was hiden by the last one
         is shown.
         */
        return mplew.getPacket();
    }

    public static byte[] getChannelSelected() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHANNEL_SELECTED.getValue());
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] getCharList(String secondpw, List<Integer> charPos, List<MapleCharacter> chars, int charslots) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHARLIST.getValue());
        mplew.write(0);
        mplew.writeInt(0); // 176+閃耀之星數量
        mplew.writeLong(DateUtil.getFileTimestamp(System.currentTimeMillis()));//176+
        for (int i = 0; i < 0; i++) { // 閃耀之星數量循環
            mplew.writeInt(0);//176+
            mplew.writeLong(0);//176+
        }
        mplew.write(0);
        mplew.writeInt(charPos.size());
        charPos.forEach((chrId) -> mplew.writeInt(chrId));// 角色順序
        mplew.write(chars.size());
        chars.stream().forEach((chr) -> addCharEntry(mplew, chr, !chr.isGM() && chr.getLevel() >= 30, false));
        mplew.write(3);
        mplew.write((secondpw != null) && (secondpw.length() > 0) ? 1 : 0);// 第二組密碼
        mplew.writeInt(charslots);
        mplew.writeInt(0); // 50級角色卡角色數量
        mplew.writeInt(-1);
        mplew.writeReversedLong(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.write(0); // 變更角色名稱開關[0:關、1:開]
        mplew.write(0); // 協議書開關[-1:開、0:關]
        mplew.writeInt(0);
        mplew.writeReversedLong(PacketHelper.getTime(System.currentTimeMillis()));

        return mplew.getPacket();
    }

    public static byte[] addNewCharEntry(MapleCharacter chr, boolean worked) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ADD_NEW_CHAR_ENTRY.getValue());
        mplew.write(worked ? 0 : 1);
        addCharEntry(mplew, chr, false, false);

        return mplew.getPacket();
    }

    public static byte[] charNameResponse(String charname, boolean nameUsed) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHAR_NAME_RESPONSE.getValue());
        mplew.writeMapleAsciiString(charname);
        mplew.write(nameUsed ? 1 : 0);

        return mplew.getPacket();
    }

    private static void addCharEntry(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, boolean ranking, boolean viewAll) {
        PacketHelper.addCharCreateStats(mplew, chr);
        PacketHelper.addCharLook(mplew, chr, true, false);
        if (MapleJob.is神之子(chr.getJob())) {
            PacketHelper.addCharLook(mplew, chr, true, true);
        }
        if (!viewAll) {
            mplew.write(0);
        }
        mplew.write(ranking ? 1 : 0);
        if (ranking) {
            mplew.writeInt(chr.getRank());
            mplew.writeInt(chr.getRankMove());
            mplew.writeInt(chr.getJobRank());
            mplew.writeInt(chr.getJobRankMove());
        }
    }

    public static byte[] enableSpecialCreation(int accid, boolean enable) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPECIAL_CREATION.getValue());
        mplew.writeInt(accid);
        mplew.write(enable ? 0 : 1);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] partTimeJob(int cid, short type, long time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PART_TIME.getValue());
        mplew.writeInt(cid);
        mplew.write(0);
        mplew.write(type);
        //1) 0A D2 CD 01 70 59 9F EA
        //2) 0B D2 CD 01 B0 6B 9C 18
        mplew.writeReversedLong(PacketHelper.getTime(time));
        mplew.writeInt(0);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] updatePartTimeJob(PartTimeJob partTime) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(21);

        mplew.writeShort(SendPacketOpcode.PART_TIME.getValue());
        mplew.writeInt(partTime.getCharacterId());
        mplew.write(0);
        PacketHelper.addPartTimeJob(mplew, partTime);

        return mplew.getPacket();
    }

    public static byte[] sendLink() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SEND_LINK.getValue());
        mplew.write(1);
        mplew.write(ServerConstants.getGateway_IP());
        mplew.writeShort(0x2057);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] genderNeeded() {

        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);

        mplew.writeShort(SendPacketOpcode.CHOOSE_GENDER.getValue());
        mplew.write(1);

        return mplew.getPacket();
    }

    public static byte[] genderChanged(boolean success) {

        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);

        mplew.writeShort(SendPacketOpcode.GENDER_SET.getValue());
        mplew.write(success);

        return mplew.getPacket();
    }
}
