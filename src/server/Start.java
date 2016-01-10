package server;

import client.SkillFactory;
import client.ZZMSEvent;
import client.inventory.MapleAndroid;
import client.inventory.MaplePet;
import constants.GameConstants;
import constants.ServerConfig;
import constants.ServerConstants;
import constants.WorldConstants;
import database.DatabaseConnection;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.MapleDojoRanking;
import handling.channel.MapleGuildRanking;
import handling.farm.FarmServer;
import handling.login.LoginServer;
import handling.world.World;
import handling.world.guild.MapleGuild;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import server.Timer.BuffTimer;
import server.Timer.CloneTimer;
import server.Timer.EtcTimer;
import server.Timer.EventTimer;
import server.Timer.MapTimer;
import server.Timer.PingTimer;
import server.Timer.WorldTimer;
import server.life.MapleLifeFactory;
import server.life.MapleMonsterInformationProvider;
import server.life.PlayerNPC;
import server.maps.MapleMapFactory;
import server.quest.MapleQuest;
import tools.MapleAESOFB;

public class Start {

    public static long startTime = System.currentTimeMillis();
    public static final Start instance = new Start();
    public static AtomicInteger CompletedLoadingThreads = new AtomicInteger(0);

    public void run() throws InterruptedException, IOException {
        long start = System.currentTimeMillis();

        if (ServerConfig.ADMIN_ONLY || ServerConstants.USE_LOCALHOST) {               //只允许管理员登录或本地模式时开启管理员模式
            System.out.println("Admin Only mode is active.");
        }
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET loggedin = 0")) {   //将accounts表中的loggedin字段清零
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("運行時錯誤: 無法連接到MySQL數據庫服務器 - " + ex);
        }

        System.out.println("正在加載" + ServerConfig.SERVER_NAME + "伺服器");
        World.init();
        System.out.println("\r\n主機: " + ServerConfig.IP + ":" + LoginServer.PORT);
        System.out.println("支援遊戲版本: " + ServerConstants.MAPLE_TYPE + "的" + ServerConstants.MAPLE_VERSION + "." + ServerConstants.MAPLE_PATCH + "版本" + (ServerConstants.TESPIA ? "測試機" : "") + "用戶端");
        System.out.println("主伺服器名稱: " + WorldConstants.getMainWorld().name());
        System.out.println("");

        if (ServerConstants.MAPLE_TYPE == ServerConstants.MapleType.GLOBAL) {
            boolean encryptionfound = false;
            for (MapleAESOFB.EncryptionKey encryptkey : MapleAESOFB.EncryptionKey.values()) {
                if (("V" + ServerConstants.MAPLE_VERSION).equals(encryptkey.name())) {
                    System.out.println("Packet Encryption: Up-To-Date!");
                    encryptionfound = true;
                    break;
                }
            }
            if (!encryptionfound) {
                System.out.println("Cannot find the packet encryption for the Maple Version you entered. Using the previous packet encryption instead.");
            }
        }
        runThread();
        loadData(false);
        LoginServer.run_startup_configurations();
        ChannelServer.startChannel_Main();
        CashShopServer.run_startup_configurations();
        if (ServerConstants.MAPLE_TYPE == ServerConstants.MapleType.GLOBAL) {
            FarmServer.run_startup_configurations();
        }
        World.registerRespawn();
        Runtime.getRuntime().addShutdownHook(new Thread(new Shutdown()));
        System.out.println("加載地圖元素");
        //加載自訂地圖元素
        MapleMapFactory.loadCustomLife(false);
        //加載玩家NPC
        PlayerNPC.loadAll();
        LoginServer.setOn();
        //System.out.println("Event Script List: " + ServerConfig.getEventList());
        if (ServerConfig.LOG_PACKETS) {
            System.out.println("數據包日誌模式已啟用");
        }
        if (ServerConfig.USE_FIXED_IV) {
            System.out.println("反抓包功能已啟用");
        }
        long now = System.currentTimeMillis() - start;
        long seconds = now / 1000;
        long ms = now % 1000;
        System.out.println("\r\n加載完成, 耗時: " + seconds + "秒" + ms + "毫秒");
        System.gc();
        PingTimer.getInstance().register(System::gc, 1800000); // 每30分鐘釋放一次記憶體        
    }

    public static void runThread() {
        System.out.print("正在加載線程");
        WorldTimer.getInstance().start();
        EtcTimer.getInstance().start();
        MapTimer.getInstance().start();
        CloneTimer.getInstance().start();
        System.out.print(/*"\u25CF"*/".");
        EventTimer.getInstance().start();
        BuffTimer.getInstance().start();
        PingTimer.getInstance().start();
        ZZMSEvent.start();
        System.out.println("完成!\r\n");
    }

    public static void loadData(boolean reload) {
        System.out.println("載入數據(因為數據量大可能比較久而且記憶體消耗會飆升)");
        //加載等級經驗
        System.out.println("加載等級經驗數據");
        GameConstants.LoadEXP();

        System.out.println("加載排名訊息數據");
        //加載道場拍排名
        MapleDojoRanking.getInstance().load(reload);
        //加載公會排名
        MapleGuildRanking.getInstance().load(reload);
        //加載排名
        RankingWorker.run();

        System.out.println("加載公會數據并清理不存在公會/寵物/機器人");
        //清理已經刪除的寵物
        MaplePet.clearPet();
        //清理已經刪除的機器人
        MapleAndroid.clearAndroid();
        //加載公會並且清理無人公會
        MapleGuild.loadAll();
        //加載家族(家族功能已去掉)
//        MapleFamily.loadAll();
        
        System.out.println("加載任務數據");
        //加載任務訊息
        MapleLifeFactory.loadQuestCounts(reload);
        //加載轉存到數據庫的任務訊息
        MapleQuest.initQuests(reload);
        
        System.out.println("加載掉寶數據");
        //加載掉寶訊息
        MapleMonsterInformationProvider.getInstance().addExtra();
        //加載全域掉寶數據
        MapleMonsterInformationProvider.getInstance().load();
        
        System.out.println("加載道具數據");
        //加載道具訊息(從WZ)
        MapleItemInformationProvider.getInstance().runEtc(reload);
        //加載道具訊息(從SQL)
        MapleItemInformationProvider.getInstance().runItems(reload);
        //加載髮型臉型
        MapleItemInformationProvider.getInstance().loadStyles(reload);
        
        System.out.println("加載技能數據");
        //加載技能
        SkillFactory.load(reload);
        
        System.out.println("加載角色卡數據");
        //加載角色卡訊息
        CharacterCardFactory.getInstance().initialize(reload);

        System.out.println("loadSpeedRuns");
        //?
        SpeedRunner.loadSpeedRuns(reload);

        System.out.println("加載商城道具數據");
        //加載商城道具訊息
        CashItemFactory.getInstance().initialize(reload);
        System.out.println("數據載入完成!\r\n");
    }

    public static class Shutdown implements Runnable {

        @Override
        public void run() {
            ShutdownServer.getInstance().run();
        }
    }

    public static void main(final String args[]) throws InterruptedException, IOException {
        instance.run();
    }
}
