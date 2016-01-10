package client;

import handling.channel.ChannelServer;
import server.Timer;

public class ZZMSEvent {

    public static void start() {
        Timer.WorldTimer.getInstance().register(new Runnable() {
            @Override
            public void run() {
                for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                    for (MapleCharacter players : cserv.getPlayerStorage().getAllCharacters()) {
                        if (players.getLastCheckProcess() <= 0) {
                            players.iNeedSystemProcess();
                            //System.out.println("請求進程");
                            continue;
                        }
                        if (System.currentTimeMillis() - players.getLastCheckProcess() >= 100 * 1000) { //距上次發送進程包經過 2 分鐘
                            players.iNeedSystemProcess();
                        }
                    }
                }
            }
        }, 30 * 1000);
    }
}
