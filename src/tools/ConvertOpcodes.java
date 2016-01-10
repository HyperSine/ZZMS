/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import handling.RecvPacketOpcode;
import handling.SendPacketOpcode;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Itzik
 */
public class ConvertOpcodes {

    public static void main(String[] args) {
        boolean decimal;
        boolean positive;
        String recvopsName;
        String sendopsName;
        Scanner input = new Scanner(System.in);
        if (args != null) {
            try {
                decimal = Boolean.parseBoolean(args[0]);
            } catch (Exception e) {
                decimal = false;
            }
            try {
                positive = Boolean.parseBoolean(args[1]);
            } catch (Exception e) {
                positive = false;
            }
            try {
                recvopsName = args[2] + ".properties";
            } catch (Exception e) {
                recvopsName = "recvops.properties";
            }
            try {
                sendopsName = args[3] + ".properties";
            } catch (Exception e) {
                sendopsName = "sendops.properties";
            }
        } else {
            System.out.println("歡迎使用包頭轉換器 \r\n你可以選擇十六進制或者十進制的包頭值, \r\n然後它們會被保存到新的檔案中");
            //RecvPacketOpcode.reloadValues();
            //SendPacketOpcode.reloadValues();
            System.out.println("你想轉換成多少進制? 16 還是 10?");
            decimal = "10".equals(input.next().toLowerCase());
            System.out.println("輸出檔案為正序請出入1,其他則為倒序");
            positive = "1".equals(input.next().toLowerCase());
            System.out.println("\r\n輸入你要儲存的用戶端包頭值檔案名字(輸入1為recvops): \r\n");
            recvopsName = input.next();
            if (recvopsName.equals("1")) {
                recvopsName = "recvops.properties";
            } else {
                recvopsName += ".properties";
            }
            System.out.println("\r\n輸入你要儲存的伺服端包頭值檔案名字(輸入1為sendops): \r\n");
            sendopsName = input.next();
            if (sendopsName.equals("1")) {
                sendopsName = "sendops.properties";
            } else {
                sendopsName += ".properties";
            }
        }
        StringBuilder sb = new StringBuilder();
        FileOutputStream out;
        try {
            RecvPacketOpcode.loadValues();
            out = new FileOutputStream(recvopsName, false);
            for (RecvPacketOpcode recv : RecvPacketOpcode.values()) {
                if (positive) {
                    sb.append(recv.name()).append(" = ").append(decimal ? recv.getValue() : HexTool.getOpcodeToString(recv.getValue())).append("\r\n");
                } else {
                    sb.insert(0, "\r\n").insert(0, decimal ? recv.getValue() : HexTool.getOpcodeToString(recv.getValue())).insert(0, " = ").insert(0, recv.name());
                }
            }
            out.write(sb.toString().getBytes());
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConvertOpcodes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConvertOpcodes.class.getName()).log(Level.SEVERE, null, ex);
        }
        sb = new StringBuilder();
        try {
            SendPacketOpcode.loadValues();
            out = new FileOutputStream(sendopsName, false);
            for (SendPacketOpcode send : SendPacketOpcode.values()) {
                if (positive) {
                    sb.append(send.name()).append(" = ").append(decimal ? send.getValue(false) : HexTool.getOpcodeToString(send.getValue(false))).append("\r\n");
                } else {
                    sb.insert(0, "\r\n").insert(0, decimal ? send.getValue(false) : HexTool.getOpcodeToString(send.getValue(false))).insert(0, " = ").insert(0, send.name());
                }
            }
            out.write(sb.toString().getBytes());
            out.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConvertOpcodes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConvertOpcodes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
