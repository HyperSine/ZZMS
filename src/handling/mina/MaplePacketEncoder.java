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
package handling.mina;

import client.MapleClient;
import constants.ServerConfig;
import constants.ServerConstants;
import gui.ZZMS;
import handling.SendPacketOpcode;
import java.util.concurrent.locks.Lock;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import server.shark.SharkPacket;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.MapleAESOFB;
import tools.MapleCustomEncryption;
import tools.StringUtil;

public class MaplePacketEncoder implements ProtocolEncoder {

    @Override
    public void encode(final IoSession session, final Object message, final ProtocolEncoderOutput out) throws Exception {
        final MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);

        if (client != null) {
            final MapleAESOFB send_crypto = client.getSendCrypto();

            final byte[] inputInitialPacket = ((byte[]) message);
            final SharkPacket sp = new SharkPacket((byte[]) message, false);
            client.sl.log(sp);

            //封包輸出
            byte[] input = inputInitialPacket;
            int pHeader = ((input[0]) & 0xFF) + (((input[1]) & 0xFF) << 8);
            String op = SendPacketOpcode.nameOf(pHeader);
            if (ServerConfig.LOG_PACKETS && !SendPacketOpcode.isSpamHeader(SendPacketOpcode.valueOf(op))) {
                int packetLen = input.length;
                String pHeaderStr = Integer.toHexString(pHeader).toUpperCase();
                pHeaderStr = "0x" + StringUtil.getLeftPaddedStr(pHeaderStr, '0', 4);
                String tab = "";
                for (int i = 4; i > op.length() / 8; i--) {
                    tab += "\t";
                }
                String t = packetLen >= 10 ? packetLen >= 100 ? packetLen >= 1000 ? "" : " " : "  " : "   ";
                final StringBuilder sb = new StringBuilder("[發送]\t" + op + tab + "\t包頭:" + pHeaderStr + t + "[" + packetLen/* + "\r\nCaller: " + Thread.currentThread().getStackTrace()[2] */ + "字元]");
                ZZMS.packet.println(sb.toString());
                sb.append("\r\n\r\n").append(HexTool.toString((byte[]) message)).append("\r\n").append(HexTool.toStringFromAscii((byte[]) message));
                FileoutputUtil.log(FileoutputUtil.Packet_Log, "\r\n\r\n" + sb.toString() + "\r\n\r\n");
            }

            final byte[] unencrypted = new byte[inputInitialPacket.length];
            System.arraycopy(inputInitialPacket, 0, unencrypted, 0, inputInitialPacket.length); // Copy the input > "unencrypted"
            final byte[] ret = new byte[unencrypted.length + 4]; // Create new bytes with length = "unencrypted" + 4

            final Lock mutex = client.getLock();
            mutex.lock();
            try {
                final byte[] header = send_crypto.getPacketHeader(unencrypted.length);

                if (ServerConstants.MAPLE_TYPE == ServerConstants.MapleType.GLOBAL) {
                    MapleCustomEncryption.encryptData(unencrypted); // Encrypting Data
                }

               send_crypto.crypt(unencrypted); // Crypt it with IV
                System.arraycopy(header, 0, ret, 0, 4); // Copy the header > "Ret", first 4 bytes
            } finally {
                mutex.unlock();
            }
            System.arraycopy(unencrypted, 0, ret, 4, unencrypted.length); // Copy the unencrypted > "ret"
            out.write(IoBuffer.wrap(ret));
        } else { // no client object created yet, send unencrypted (hello)
            out.write(IoBuffer.wrap((byte[]) message));
        }
    }

    @Override
    public void dispose(IoSession session) throws Exception {
        // nothing to do
    }
}
