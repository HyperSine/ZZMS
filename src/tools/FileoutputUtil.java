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
package tools;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class FileoutputUtil {

    // Logging output file
    public static final String Acc_Stuck = "日誌/日誌_AccountStuck.rtf",
            Login_Error = "日誌/登入_錯誤.rtf",
            // IP_Log = "日誌/Log_AccountIP.rtf",
            //GMCommand_Log = "日誌/Log_GMCommand.rtf",
            // Zakum_Log = "日誌/Log_Zakum.rtf",
            //Horntail_Log = "日誌/Log_Horntail.rtf",

            Pinkbean_Log = "日誌/Pinkbean.rtf",
            ScriptEx_Log = "日誌/腳本_異常.txt",
            PacketEx_Log = "日誌/數據包_異常.txt", // I cba looking for every error, adding this back in.
            UnknownPacket_Log = "日誌/數據包_未知.txt",
            Packet_Log = "日誌/數據包_收發.txt",
            Packet_Record = "日誌/數據包_編寫.txt",
            Hacker_Log = "日誌/Hacker.rtf",
            Movement_Log = "日誌/移動_錯誤.txt",
            Client_Feedback = "日誌/用戶端_反饋.txt",
            Client_Error = "日誌/用戶端_報錯.txt",
            Create_Character = "日誌/創建角色.txt",
            CommandEx_Log = "日誌/指令_異常.txt", //PQ_Log = "Log_PQ.rtf"
            Process_Error = "日誌/進程獲取_錯誤.log",
            Monitor_Dir = "日誌/監控日誌/",
            Shark_Dir = "日誌/楓鯊檔案/",
            BanLog_Process_Dir = "日誌/封號進程/";
    // End
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdfGMT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdf_ = new SimpleDateFormat("yyyy-MM-dd");
    private static final String FILE_PATH = "日誌/";
    private static final String ERROR = "錯誤/";

    static {
        sdfGMT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public static void print(final String name, final String s) {
        print(name, s, true);
    }

    public static void print(final String name, final String s, boolean line) {
        logToFile(FILE_PATH + name, s + (line ? "\r\n---------------------------------\r\n" : null));
    }

    public static void printError(final String name, final Throwable t, final String info) {
        printError(name, info + "\r\n" + getString(t));
    }

    public static void printError(final String name, final String s) {
        logToFile(FILE_PATH + ERROR + sdf_.format(Calendar.getInstance().getTime()) + "/" + name, s + "\r\n---------------------------------\r\n");
    }

    public static void outputFileError(final String file, final Throwable t) {
        log(file, getString(t));
    }

    public static void log(final String file, final String msg) {
        logToFile(file, "\r\n------------------------ " + CurrentReadable_Time() + " ------------------------\r\n" + msg);
    }

    public static void logToFile(final String file, final String[] msgs) {
        for (int i = 0; i < msgs.length; i++) {
            logToFile(file, msgs[i], false);
            if (i < msgs.length - 1) {
                logToFile(file, "\r\n", false);
            }
        }
    }

    public static void logToFile(final String file, final String msg) {
        logToFile(file, msg, false);
    }

    public static void logToFileIfNotExists(final String file, final String msg) {
        logToFile(file, msg, true);
    }

    public static void logToFile(final String file, final String msg, boolean notExists) {
        FileOutputStream out = null;
        try {
            File outputFile = new File(file);
            if (outputFile.getParentFile() != null) {
                outputFile.getParentFile().mkdirs();
            }
            out = new FileOutputStream(file, true);
            if (!out.toString().contains(msg) || !notExists) {
                OutputStreamWriter osw = new OutputStreamWriter(out, "UTF-8");
                osw.write(msg);
                osw.flush();
            }
        } catch (IOException ess) {
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ignore) {
            }
        }
    }

    public static String CurrentReadable_Date() {
        return sdf_.format(Calendar.getInstance().getTime());
    }

    public static String CurrentReadable_Time() {
        return sdf.format(Calendar.getInstance().getTime());
    }

    public static String CurrentReadable_TimeGMT() {
        return sdfGMT.format(new Date());
    }

    public static String getString(final Throwable e) {
        String retValue = null;
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            retValue = sw.toString();
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (sw != null) {
                    sw.close();
                }
            } catch (IOException ignore) {
            }
        }
        return retValue;
    }

    public static void packetLog(String file, String msg) {
        logToFile(file, msg + "\r\n\r\n");
    }

}
