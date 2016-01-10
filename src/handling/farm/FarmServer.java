/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package handling.farm;

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

/**
 *
 * @author Itzik
 */
public class FarmServer {

    private static String ip;
    public static final int PORT = 8601;
    private static IoAcceptor acceptor;
    private static PlayerStorage players;
    private static boolean finishedShutdown = false;

    public static void run_startup_configurations() {
        ip = ServerConfig.IP + ":" + PORT;

        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());

        acceptor = new NioSocketAcceptor();
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MapleCodecFactory()));
        acceptor.getFilterChain().addLast("exceutor", new ExecutorFilter());

        players = new PlayerStorage(MapleServerHandler.FARM_SERVER);
        try {
            acceptor.setHandler(new MapleServerHandler(MapleServerHandler.FARM_SERVER));
            acceptor.bind(new InetSocketAddress(PORT));
            ((SocketSessionConfig) acceptor.getSessionConfig()).setTcpNoDelay(true);
            System.out.println("Farm Server is listening on port 8601.");
        } catch (IOException e) {
            System.err.println("Binding to port 8601 failed");
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
        System.out.println("Saving all connected clients (Farm)...");
        players.disconnectAll();
        System.out.println("Shutting down Farm...");

        finishedShutdown = true;
    }

    public static boolean isShutdown() {
        return finishedShutdown;
    }
}
