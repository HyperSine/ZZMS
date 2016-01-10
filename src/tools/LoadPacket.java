/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import tools.HexTool;
import tools.data.MaplePacketLittleEndianWriter;

/**
 *
 * @author Itzik
 */
public class LoadPacket {

    public static byte[] getPacket() {
        Properties packetProps = new Properties();
        InputStreamReader is;
        try {
            is = new FileReader("其他/測試數據包.txt");
            packetProps.load(is);
            is.close();
        } catch (IOException ex) {
            System.err.println("讀取 測試數據包.txt 失敗");
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.write(HexTool.getByteArrayFromHexString(packetProps.getProperty("packet")));
        return mplew.getPacket();
    }
}
