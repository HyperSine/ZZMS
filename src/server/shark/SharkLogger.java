/*
 * This file was designed for Titanium.
 * Do not redistribute without explicit permission from the
 * developer(s).
 */
package server.shark;

import client.MapleClient;
import constants.ServerConfig;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import constants.ServerConstants;
import handling.cashshop.CashShopServer;
import handling.farm.FarmServer;
import handling.login.LoginServer;
import tools.FileoutputUtil;
import tools.data.MaplePacketLittleEndianWriter;

public class SharkLogger {

    private List<SharkPacket> stored = new ArrayList<>();
    private final static int SEVENBITS = 0x0000007f;
    private final static int SIGNBIT = 0x00000080;
    private final int MAPLE_SHARK_VERSION;
    private final MapleClient client;

    public SharkLogger(int ver, MapleClient client) {
        this.MAPLE_SHARK_VERSION = ver;
        this.client = client;
    }

    /**
     * Writes a 7-bit integer. As if I actually know what that is. Credits to
     * Arnold Lamkamp
     *
     * @ CWI Eclipse
     * @param value
     * @param mplew
     */
    private void write7BitInt(int value, MaplePacketLittleEndianWriter mplew) {
        int intValue = value;

        if ((intValue & 0xffffff80) == 0) {
            mplew.write((byte) (intValue & SEVENBITS));
            return;
        }
        mplew.write((byte) ((intValue & SEVENBITS) | SIGNBIT));

        if ((intValue & 0xffffc000) == 0) {
            mplew.write((byte) ((intValue >>> 7) & SEVENBITS));
            return;
        }
        mplew.write((byte) (((intValue >>> 7) & SEVENBITS) | SIGNBIT));

        if ((intValue & 0xffe00000) == 0) {
            mplew.write((byte) ((intValue >>> 14) & SEVENBITS));
            return;
        }
        mplew.write((byte) (((intValue >>> 14) & SEVENBITS) | SIGNBIT));

        if ((intValue & 0xf0000000) == 0) {
            mplew.write((byte) ((intValue >>> 21) & SEVENBITS));
            return;
        }
        mplew.write((byte) (((intValue >>> 21) & SEVENBITS) | SIGNBIT));

        mplew.write((byte) ((intValue >>> 28) & SEVENBITS));
    }

    public void dump() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(); // because fuck you
        String localend = client.getSession().getRemoteAddress().toString().replace("/", "");
        int mRemotePort;
        switch (client.getChannel()) {
            case -2:
                mRemotePort = FarmServer.PORT;
                break;
            case -1:
                mRemotePort = CashShopServer.PORT;
                break;
            case 0:
                mRemotePort = LoginServer.PORT;
                break;
            default:
                mRemotePort = client.getChannelServer().getPort();
                break;
        }
        mplew.writeShort(MAPLE_SHARK_VERSION);//MapleSharkVersion
        if (MAPLE_SHARK_VERSION == 0x2012) {
            mplew.writeShort(ServerConstants.MAPLE_TYPE.getType()); // mLocale
            mplew.writeShort(ServerConstants.MAPLE_VERSION); // mBuild
            mplew.writeShort(Integer.parseInt(localend.split(":")[1])); // mLocalPort
        } else if (MAPLE_SHARK_VERSION == 0x2014) {
            write7BitInt(localend.length(), mplew);
            mplew.writeAsciiString(localend); // mLocalEndpoint
            mplew.writeShort(Integer.parseInt(localend.split(":")[1])); // mLocalPort
            write7BitInt((ServerConfig.IP + ":" + mRemotePort).length(), mplew);
            mplew.writeAsciiString(ServerConfig.IP + ":" + mRemotePort); // mRemoteEndpoint
            mplew.writeShort(mRemotePort); // mRemotePort
            mplew.writeShort(ServerConstants.MAPLE_TYPE.getType()); // mLocale
            mplew.writeShort(ServerConstants.MAPLE_VERSION); // mBuild
        } else if (MAPLE_SHARK_VERSION == 0x2015 || MAPLE_SHARK_VERSION >= 0x2020) {
            write7BitInt(localend.length(), mplew);
            mplew.writeAsciiString(localend); // mLocalEndpoint
            mplew.writeShort(Integer.parseInt(localend.split(":")[1])); // mLocalPort
            write7BitInt((ServerConfig.IP + ":" + mRemotePort).length(), mplew);
            mplew.writeAsciiString(ServerConfig.IP + ":" + mRemotePort); // mRemoteEndpoint
            mplew.writeShort(mRemotePort); // mRemotePort
            mplew.write(ServerConstants.MAPLE_TYPE.getType()); // mLocale
            mplew.writeShort(ServerConstants.MAPLE_VERSION); // mBuild
            if (MAPLE_SHARK_VERSION >= 0x2021) {
                write7BitInt(ServerConstants.MAPLE_PATCH.length(), mplew);
                mplew.writeAsciiString(ServerConstants.MAPLE_PATCH); // mPatchLocation
            }
        }

        for (SharkPacket b : stored) {
            b.dump(mplew, MAPLE_SHARK_VERSION);
        }

        File toWrite = new File(FileoutputUtil.Shark_Dir + (client.getPlayer() != null ? client.getPlayer().getName() + "_" : "") + client.getSessionId() + ".msb");
        if (toWrite.getParentFile() != null) {
            toWrite.getParentFile().mkdirs();
        }
        try {
            toWrite.createNewFile();
            try (FileOutputStream fos = new FileOutputStream(toWrite)) {
                fos.write(mplew.getPacket());
                fos.flush();
            }
        } catch (IOException ex) {
            System.out.println("Ex" + ex);
        }

        stored.clear();
    }

    public void log(SharkPacket sp) {
        this.stored.add(sp);
    }
}
