 package tools;
 
 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.SimpleTimeZone;
 import tools.packet.PacketHelper;
 
 public class DateUtil
 {
   private static final int ITEM_YEAR2000 = -1085019342;
   private static final long REAL_YEAR2000 = 946681229830L;
   private static final int QUEST_UNIXAGE = 27111908;
   private static final long FT_UT_OFFSET = 116444736000000000L;
 
   public static long getTempBanTimestamp(long realTimestamp)
   {
     return realTimestamp * 10000L + 116444736000000000L;
   }
 
   public static int getItemTimestamp(long realTimestamp) {
     int time = (int)((realTimestamp - 946681229830L) / 1000L / 60L);
     return (int)(time * 35.762787000000003D) + 1085019342;
   }
 
   public static int getQuestTimestamp(long realTimestamp) {
     int time = (int)(realTimestamp / 1000L / 60L);
     return (int)(time * 0.1396987D) + 27111908;
   }
 
   public static boolean isDST() {
     return SimpleTimeZone.getDefault().inDaylightTime(new Date());
   }
 
   public static long getFileTimestamp(long timeStampinMillis) {
     return getFileTimestamp(timeStampinMillis, false);
   }
 
   public static long getFileTimestamp(long timeStampinMillis, boolean roundToMinutes) {
     if (isDST())
       timeStampinMillis -= 3600000L;
     long time;
     //long time;
     if (roundToMinutes)
       time = timeStampinMillis / 1000L / 60L * 600000000L;
     else {
       time = timeStampinMillis * 10000L;
     }
     return time + PacketHelper.FT_UT_OFFSET;
   }
 
   public static int getTime() {
     String time = new SimpleDateFormat("yyyy-MM-dd-HH").format(new Date()).replace("-", "");
     return Integer.valueOf(time);
   }
 }

