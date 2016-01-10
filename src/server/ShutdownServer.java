package server;

import constants.ServerConstants;
import database.DatabaseConnection;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.farm.FarmServer;
import handling.login.LoginServer;
import handling.world.World;
import java.sql.SQLException;
import server.Timer.BuffTimer;
import server.Timer.CloneTimer;
import server.Timer.EtcTimer;
import server.Timer.EventTimer;
import server.Timer.MapTimer;
import server.Timer.PingTimer;
import server.Timer.WorldTimer;
import tools.packet.CWvsContext;

public class ShutdownServer implements ShutdownServerMBean {

    public static ShutdownServer instance = new ShutdownServer();

    public static ShutdownServer getInstance() {
        return instance;
    }

    @Override
    public void shutdown() {//can execute twice
        run();
    }

    @Override
    public void run() {
        int ret = 0;
        World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(0, "伺服器將進行停機維護, 請安全的下線, 以免造成不必要的損失。"));
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            cs.setShutdown();
            cs.setServerMessage("伺服器將進行停機維護, 請安全的下線, 以免造成不必要的損失。");
            ret += cs.closeAllMerchant();
        }
        /*AtomicInteger FinishedThreads = new AtomicInteger(0);
         HiredMerchantSave.Execute(this);
         synchronized (this) {
         try {
         wait();
         } catch (InterruptedException ex) {
         Logger.getLogger(ShutdownServer.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         while (FinishedThreads.incrementAndGet() != HiredMerchantSave.NumSavingThreads) {
         synchronized (this) {
         try {
         wait();
         } catch (InterruptedException ex) {
         Logger.getLogger(ShutdownServer.class.getName()).log(Level.SEVERE, null, ex);
         }
         }
         }*/
        World.Guild.save();
        World.Alliance.save();
        World.Family.save();
        System.out.println("數據存檔完成，僱傭商人儲存個數: " + ret);
        try {
            World.Broadcast.broadcastMessage(CWvsContext.broadcastMsg(0, "伺服器將進行停機維護, 請安全的下線, 以免造成不必要的損失。"));
            Integer[] chs = ChannelServer.getAllInstance().toArray(new Integer[0]);

            for (int i : chs) {
                try {
                    ChannelServer cs = ChannelServer.getInstance(i);
                    synchronized (this) {
                        cs.shutdown();
                    }
                } catch (Exception e) {
                }
            }
            CashShopServer.shutdown();
            DatabaseConnection.closeAll();
        } catch (SQLException e) {
            System.err.println("THROW" + e);
        }
        WorldTimer.getInstance().stop();
        EtcTimer.getInstance().stop();
        MapTimer.getInstance().stop();
        CloneTimer.getInstance().stop();
        EventTimer.getInstance().stop();
        BuffTimer.getInstance().stop();
        PingTimer.getInstance().stop();

        for (ChannelServer ch : ChannelServer.getAllInstances()) {
            ch.shutdown();
        }
        CashShopServer.shutdown();
        if (ServerConstants.MAPLE_TYPE == ServerConstants.MapleType.GLOBAL) {
            FarmServer.shutdown();
        }
        LoginServer.shutdown();
        System.out.println("關閉伺服器完成\r\n");
    }
}
