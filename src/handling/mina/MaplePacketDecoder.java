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
import handling.RecvPacketOpcode;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import server.shark.SharkPacket;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.MapleAESOFB;
import tools.MapleCustomEncryption;
import tools.data.input.ByteArrayByteStream;
import tools.data.input.GenericLittleEndianAccessor;

public class MaplePacketDecoder extends CumulativeProtocolDecoder {

    public static final String DECODER_STATE_KEY = MaplePacketDecoder.class.getName() + ".STATE";

    public static class DecoderState {

        public int packetlength = -1;
    }

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        final DecoderState decoderState = (DecoderState) session.getAttribute(DECODER_STATE_KEY);

        /*	if (decoderState == null) {
         decoderState = new DecoderState();
         session.setAttribute(DECODER_STATE_KEY, decoderState);
         }*/
        final MapleClient client = (MapleClient) session.getAttribute(MapleClient.CLIENT_KEY);

        if (decoderState.packetlength == -1) {
            if (in.remaining() >= 4) {
                final int packetHeader = in.getInt();
                if (!client.getReceiveCrypto().checkPacket(packetHeader)) {
                    session.close(true);
                    return false;
                }
                decoderState.packetlength = MapleAESOFB.getPacketLength(packetHeader);
            } else {
                return false;
            }
        }
        if (in.remaining() >= decoderState.packetlength) {
            final byte decryptedPacket[] = new byte[decoderState.packetlength];
            in.get(decryptedPacket, 0, decoderState.packetlength);
            decoderState.packetlength = -1;

            client.getReceiveCrypto().crypt(decryptedPacket);

            if (ServerConstants.MAPLE_TYPE == ServerConstants.MapleType.GLOBAL) {
                MapleCustomEncryption.decryptData(decryptedPacket);
            }

            out.write(decryptedPacket);

            final SharkPacket sp = new SharkPacket(decryptedPacket, true);
            client.sl.log(sp);

            //封包輸出
            int packetLen = decryptedPacket.length;
            short pHeader = new GenericLittleEndianAccessor(new ByteArrayByteStream(decryptedPacket)).readShort();
            String op = RecvPacketOpcode.nameOf(pHeader);
            if (ServerConfig.LOG_PACKETS && !RecvPacketOpcode.isSpamHeader(RecvPacketOpcode.valueOf(op))) {
                String tab = "";
                for (int i = 4; i > op.length() / 8; i--) {
                    tab += "\t";
                }
                String t = packetLen >= 10 ? packetLen >= 100 ? packetLen >= 1000 ? "" : " " : "  " : "   ";
                final StringBuilder sb = new StringBuilder("[接收]\t" + op + tab + "\t包頭:" + HexTool.getOpcodeToString(pHeader) + t + "[" + packetLen + "字元]");
                ZZMS.packet.println(sb.toString());
                sb.append("\r\n\r\n").append(HexTool.toString(decryptedPacket)).append("\r\n").append(HexTool.toStringFromAscii(decryptedPacket));
                FileoutputUtil.log(FileoutputUtil.Packet_Log, "\r\n\r\n" + sb.toString() + "\r\n\r\n");
            }

            return true;
        }
        return false;
    }
}
