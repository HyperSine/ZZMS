package handling.cashshop;

import constants.ServerConfig;
import handling.MapleServerHandler;
import handling.channel.PlayerStorage;
import handling.mina.MapleCodecFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class CashShopServer {

    private static String ip;
    public final static int PORT = 8790;
    private static IoAcceptor acceptor;
    private static PlayerStorage players;
    private static boolean finishedShutdown = false;

    public static void run_startup_configurations() {
        System.out.print("正在加載商城...");
        ip = ServerConfig.IP + ":" + PORT;

        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());

        acceptor = new NioSocketAcceptor();
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MapleCodecFactory()));
        acceptor.getFilterChain().addLast("exceutor", new ExecutorFilter());

        players = new PlayerStorage(MapleServerHandler.CASH_SHOP_SERVER);
        try {
            acceptor.setHandler(new MapleServerHandler(MapleServerHandler.CASH_SHOP_SERVER));
            acceptor.bind(new InetSocketAddress(PORT));
            ((SocketSessionConfig) acceptor.getSessionConfig()).setTcpNoDelay(true);

            System.out.println("完成!");
            System.out.println("商城伺服器正在監聽" + PORT + "端口\r\n");
        } catch (final IOException e) {
            System.out.println("失敗!");
            System.err.println("無法綁定" + PORT + "端口");
            throw new RuntimeException("Binding failed.", e);
        }
    }

    public static String getIP() {
        return ip;
    }

    public static PlayerStorage getPlayerStorage() {
        return players;
    }

    public static void shutdown() {
        if (finishedShutdown) {
            return;
        }
        System.out.println("儲存所有連接的用戶端(商城)...");
        players.disconnectAll();
        System.out.println("正在關閉商城...");
        acceptor.unbind();
        finishedShutdown = true;
    }

    public static boolean isShutdown() {
        return finishedShutdown;
    }
}
