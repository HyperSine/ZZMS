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
package handling.login;

import constants.ServerConfig;
import handling.MapleServerHandler;
import handling.mina.MapleCodecFactory;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import tools.Triple;

public class LoginServer {

    public static final int PORT = 8484;
    private static IoAcceptor acceptor;
    private static Map<Integer, Integer> load = new HashMap<>();
    private static String serverName;
    private static int userLimit, usersOn = 0;
    private static boolean finishedShutdown = true, adminOnly = false;
    private static final HashMap<Integer, Triple<String, String, Integer>> loginAuth = new HashMap<>();
    private static final HashSet<String> loginIPAuth = new HashSet<>();

    public static void putLoginAuth(int chrid, String ip, String tempIP, int channel) {
        Triple<String, String, Integer> put = loginAuth.put(chrid, new Triple<>(ip, tempIP, channel));
        loginIPAuth.add(ip);
    }

    public static Triple<String, String, Integer> getLoginAuth(int chrid) {
        return loginAuth.remove(chrid);
    }

    public static boolean containsIPAuth(String ip) {
        return loginIPAuth.contains(ip);
    }

    public static void removeIPAuth(String ip) {
        loginIPAuth.remove(ip);
    }

    public static void addIPAuth(String ip) {
        loginIPAuth.add(ip);
    }

    public static final void addChannel(final int channel) {
        load.put(channel, 0);
    }

    public static final void removeChannel(final int channel) {
        load.remove(channel);
    }

    public static final void run_startup_configurations() {
        System.out.print("加載\"登入\"伺服器...");
        userLimit = ServerConfig.USER_LIMIT;
        serverName = ServerConfig.SERVER_NAME;
        adminOnly = ServerConfig.ADMIN_ONLY;

        IoBuffer.setUseDirectBuffer(false);
        IoBuffer.setAllocator(new SimpleBufferAllocator());

        acceptor = new NioSocketAcceptor();
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 30);
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new MapleCodecFactory()));
        acceptor.getFilterChain().addLast("exceutor", new ExecutorFilter());

        try {
            acceptor.setHandler(new MapleServerHandler(MapleServerHandler.LOGIN_SERVER));
            acceptor.bind(new InetSocketAddress(PORT));
            ((SocketSessionConfig) acceptor.getSessionConfig()).setTcpNoDelay(true);
            System.out.println("完成!");
            System.out.println("\"登入\"伺服器正在監聽" + PORT + "端口\r\n");
        } catch (IOException e) {
            System.out.println("失敗!");
            System.err.println("無法綁定" + PORT + "端口: " + e);
        }
    }

    public static final void shutdown() {
        if (finishedShutdown) {
            return;
        }
        System.out.println("正在關閉\"登入\"伺服器...");
        acceptor.unbind();
        finishedShutdown = true; //nothing. lol
    }

    public static final String getServerName() {
        return serverName;
    }

    public static Map<Integer, Integer> getLoad() {
        return load;
    }

    public static void setLoad(final Map<Integer, Integer> load_, final int usersOn_) {
        load = load_;
        usersOn = usersOn_;
    }

    public static final int getUserLimit() {
        return userLimit;
    }

    public static final int getUsersOn() {
        return usersOn;
    }

    public static final void setUserLimit(final int newLimit) {
        userLimit = newLimit;
    }

    public static final int getNumberOfSessions() {
        return acceptor.getManagedSessionCount();
    }

    public static final boolean isAdminOnly() {
        return adminOnly;
    }

    public static final boolean isShutdown() {
        return finishedShutdown;
    }

    public static final void setOn() {
        finishedShutdown = false;
    }
}
