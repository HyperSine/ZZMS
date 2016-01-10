package constants;

import client.MapleCharacter;
import handling.channel.ChannelServer;
import server.ServerProperties;
import tools.packet.CWvsContext;

public class WorldConstants {

    public static int EXP_RATE = 1;
    public static int MESO_RATE = 1;
    public static int DROP_RATE = 1;
    public static byte FLAG = 3;
    public static int CHANNEL_COUNT = 5;
    public static String WORLD_TIP = "ZZMS Enjoy it!";
    public static String SCROLL_MESSAGE = "";
    public static final int gmserver = -1; // -1 = no gm server
    public static final byte recommended = (byte) -1; //-1 = no recommended
    public static final String recommendedmsg = recommended < 0 ? "" : "        Join " + getById(recommended).name() + ",       the newest world! (If youhave friends who play, consider joining their worldinstead. Characters can`t move between worlds.)";

    private static int channels = 0;

    public static interface Option {

        public int getWorld();

        public int getExp();

        public int getMeso();

        public int getDrop();

        public byte getFlag();

        public boolean show();

        public boolean isAvailable();

        public int getChannelCount();

        public String getWorldTip();

        public String getScrollMessage();

        public void setExp(int info);

        public void setMeso(int info);

        public void setDrop(int info);

        public void setFlag(byte info);

        public void setShow(boolean info);

        public void setAvailable(boolean info);

        public void setChannelCount(int info);

        public void setWorldTip(String info);

        public void setScrollMessage(String info);

        public String name();
    }

    /**
     *
     * @Warning: World will be duplicated if it's the same as the gm server
     */
    public static enum WorldOption implements Option {

        레드(43),
        판테온(40),
        템페스트(39),
        레이븐(38),
        저스터스(37),
        엘프(36),
        레전드(35),
        터탄(34),
        카오스(33),
        안드로아(32),
        코스모(31),
        노바(30),
        時間女神(22),
        西格諾斯(21),
        喵怪仙人(20),
        葛雷金剛(19),
        九尾妖狐(18),
        寒霜冰龍(17),
        泰勒熊(16),
        神獸(15),
        皮卡啾(14),
        鯨魚號(13),
        電擊象(12),
        海努斯(11),
        巴洛古(10),
        蝴蝶精(9),
        火獨眼獸(8),
        木妖(7),
        三眼章魚(6),
        綠水靈(5),
        藍寶(4),
        緞帶肥肥(3),
        星光精靈(2),
        菇菇寶貝(1),
        雪吉拉(0, true);
        private final int world, exp, meso, drop, channels;
        private final byte flag;
        private final boolean show, available;
        private final String worldtip, scrollMessage;

        WorldOption(int world) {
            this.world = world;
            this.exp = 0;
            this.meso = 0;
            this.drop = 0;
            this.flag = -1;
            this.show = false;
            this.available = false;
            this.channels = 0;
            this.worldtip = null;
            this.scrollMessage = null;
        }

        WorldOption(int world, boolean show) {
            this.world = world;
            this.exp = 0;
            this.meso = 0;
            this.drop = 0;
            this.flag = -1;
            this.show = show;
            this.available = show;
            this.channels = 0;
            this.worldtip = null;
            this.scrollMessage = null;
        }

        @Override
        public int getWorld() {
            return world;
        }

        @Override
        public int getExp() {
            int info = ServerProperties.getProperty("expWorld" + world, exp);
            return info > 0 ? info : EXP_RATE;
        }

        @Override
        public int getMeso() {
            int info = ServerProperties.getProperty("mesoWorld" + world, meso);
            return info > 0 ? info : MESO_RATE;
        }

        @Override
        public int getDrop() {
            int info = ServerProperties.getProperty("dropWorld" + world, drop);
            return info > 0 ? info : DROP_RATE;
        }

        @Override
        public byte getFlag() {
            byte info = ServerProperties.getProperty("flagWorld" + world, flag);
            return info >= 0 ? info : FLAG;
        }

        @Override
        public boolean show() {
            return ServerProperties.getProperty("showWorld" + world, show);
        }

        @Override
        public boolean isAvailable() {
            return ServerProperties.getProperty("availableWorld" + world, available);
        }

        @Override
        public int getChannelCount() {
            int info = ServerProperties.getProperty("channelWorld" + world, channels);
            return info > 0 ? info : CHANNEL_COUNT;
        }

        @Override
        public String getWorldTip() {
            String info = ServerProperties.getProperty("tipWorld" + world, worldtip);
            return info != null ? info : WORLD_TIP;
        }

        @Override
        public String getScrollMessage() {
            String info = ServerProperties.getProperty("scrollMsgWorld" + world, scrollMessage);
            return info != null ? info : SCROLL_MESSAGE;
        }

        @Override
        public void setExp(int info) {
            if (info == exp) {
                ServerProperties.removeProperty("expWorld" + world);
                return;
            }
            ServerProperties.setProperty("expWorld" + world, info);
        }

        @Override
        public void setMeso(int info) {
            if (info == meso) {
                ServerProperties.removeProperty("mesoWorld" + world);
                return;
            }
            ServerProperties.setProperty("mesoWorld" + world, info);
        }

        @Override
        public void setDrop(int info) {
            if (info == drop) {
                ServerProperties.removeProperty("dropWorld" + world);
                return;
            }
            ServerProperties.setProperty("dropWorld" + world, info);
        }

        @Override
        public void setFlag(byte info) {
            if (info == flag) {
                ServerProperties.removeProperty("flagWorld" + world);
                return;
            }
            ServerProperties.setProperty("flagWorld" + world, info);
        }

        @Override
        public void setShow(boolean info) {
            if (info == show) {
                ServerProperties.removeProperty("expWorld" + world);
                return;
            }
            ServerProperties.setProperty("showWorld" + world, info);
        }

        @Override
        public void setAvailable(boolean info) {
            if (info == available) {
                ServerProperties.removeProperty("availableWorld" + world);
                return;
            }
            ServerProperties.setProperty("availableWorld" + world, info);
        }

        @Override
        public void setChannelCount(int info) {
            if (info == channels) {
                ServerProperties.removeProperty("channelWorld" + world);
                return;
            }
            ServerProperties.setProperty("channelWorld" + world, info);
        }

        @Override
        public void setWorldTip(String info) {
            if (info == worldtip) {
                ServerProperties.removeProperty("tipWorld" + world);
                return;
            }
            ServerProperties.setProperty("tipWorld" + world, info);
        }

        @Override
        public void setScrollMessage(String info) {
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                    if (mch.getWorld() == getWorld()) {
                        mch.getClient().getSession().write(CWvsContext.broadcastMsg(info == null ? "" : info));
                    }
                }
            }
            if (info == scrollMessage) {
                ServerProperties.removeProperty("scrollMsgWorld" + world);
                return;
            }
            ServerProperties.setProperty("scrollMsgWorld" + world, info);
        }
    }

    public static enum TespiaWorldOption implements Option {

        레드("t43"),
        판테온("t40"),
        템페스트("t39"),
        브로아("t3"),
        測試機("t0", true);
        private final int world, exp, meso, drop, channels;
        private final byte flag;
        private final boolean show, available;
        private final String worldName, worldtip, scrollMessage;

        TespiaWorldOption(String world) {
            this.world = Integer.parseInt(world.replaceAll("t", ""));
            this.worldName = world;
            this.exp = 0;
            this.meso = 0;
            this.drop = 0;
            this.flag = -1;
            this.show = false;
            this.available = false;
            this.channels = 0;
            this.worldtip = null;
            this.scrollMessage = null;
        }

        TespiaWorldOption(String world, boolean show) {
            this.world = Integer.parseInt(world.replaceAll("t", ""));
            this.worldName = world;
            this.exp = 0;
            this.meso = 0;
            this.drop = 0;
            this.flag = -1;
            this.show = show;
            this.available = show;
            this.channels = 0;
            this.worldtip = null;
            this.scrollMessage = null;
        }

        @Override
        public int getWorld() {
            return world;
        }

        @Override
        public int getExp() {
            int info = ServerProperties.getProperty("expWorld" + worldName, exp);
            return info > 0 ? info : EXP_RATE;
        }

        @Override
        public int getMeso() {
            int info = ServerProperties.getProperty("mesoWorld" + worldName, meso);
            return info > 0 ? info : MESO_RATE;
        }

        @Override
        public int getDrop() {
            int info = ServerProperties.getProperty("dropWorld" + worldName, drop);
            return info > 0 ? info : DROP_RATE;
        }

        @Override
        public byte getFlag() {
            byte info = ServerProperties.getProperty("flagWorld" + worldName, flag);
            return info >= 0 ? info : FLAG;
        }

        @Override
        public boolean show() {
            return ServerProperties.getProperty("showWorld" + worldName, show);
        }

        @Override
        public boolean isAvailable() {
            return ServerProperties.getProperty("availableWorld" + worldName, available);
        }

        @Override
        public int getChannelCount() {
            int info = ServerProperties.getProperty("channelWorld" + worldName, channels);
            return info > 0 ? info : CHANNEL_COUNT;
        }

        @Override
        public String getWorldTip() {
            String info = ServerProperties.getProperty("tipWorld" + worldName, worldtip);
            return info != null ? info : WORLD_TIP;
        }

        @Override
        public String getScrollMessage() {
            String info = ServerProperties.getProperty("scrollMsgWorld" + worldName, scrollMessage);
            return info != null ? info : SCROLL_MESSAGE;
        }

        @Override
        public void setExp(int info) {
            if (info == exp) {
                ServerProperties.removeProperty("expWorld" + worldName);
                return;
            }
            ServerProperties.setProperty("expWorld" + worldName, info);
        }

        @Override
        public void setMeso(int info) {
            if (info == meso) {
                ServerProperties.removeProperty("mesoWorld" + worldName);
                return;
            }
            ServerProperties.setProperty("mesoWorld" + worldName, info);
        }

        @Override
        public void setDrop(int info) {
            if (info == drop) {
                ServerProperties.removeProperty("dropWorld" + worldName);
                return;
            }
            ServerProperties.setProperty("dropWorld" + worldName, info);
        }

        @Override
        public void setFlag(byte info) {
            if (info == flag) {
                ServerProperties.removeProperty("flagWorld" + worldName);
                return;
            }
            ServerProperties.setProperty("flagWorld" + worldName, info);
        }

        @Override
        public void setShow(boolean info) {
            if (info == show) {
                ServerProperties.removeProperty("expWorld" + worldName);
                return;
            }
            ServerProperties.setProperty("showWorld" + worldName, info);
        }

        @Override
        public void setAvailable(boolean info) {
            if (info == available) {
                ServerProperties.removeProperty("availableWorld" + worldName);
                return;
            }
            ServerProperties.setProperty("availableWorld" + worldName, info);
        }

        @Override
        public void setChannelCount(int info) {
            if (info == channels) {
                ServerProperties.removeProperty("channelWorld" + worldName);
                return;
            }
            ServerProperties.setProperty("channelWorld" + worldName, info);
        }

        @Override
        public void setWorldTip(String info) {
            if (info == worldtip) {
                ServerProperties.removeProperty("tipWorld" + worldName);
                return;
            }
            ServerProperties.setProperty("tipWorld" + worldName, info);
        }

        @Override
        public void setScrollMessage(String info) {
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter mch : cserv.getPlayerStorage().getAllCharacters()) {
                    if (mch.getWorld() == getWorld()) {
                        mch.getClient().getSession().write(CWvsContext.broadcastMsg(info == null ? "" : info));
                    }
                }
            }
            if (info == scrollMessage) {
                ServerProperties.removeProperty("scrollMsgWorld" + worldName);
                return;
            }
        }
    }

    public static Option[] values() {
        return ServerConstants.TESPIA ? TespiaWorldOption.values() : WorldOption.values();
    }

    public static Option valueOf(String name) {
        return ServerConstants.TESPIA ? TespiaWorldOption.valueOf(name) : WorldOption.valueOf(name);
    }

    public static Option getById(int g) {
        for (Option e : values()) {
            if (e.getWorld() == g) {
                return e;
            }
        }
        return null;
    }

    public static Option getMainWorld() {
        for (Option e : values()) {
            if (e.show() == true) {
                return e;
            }
        }
        return null;
    }

    public static boolean isExists(int id) {
        return getById(id) != null;
    }

    public static String getNameById(int serverid) {
        if (getById(serverid) == null) {
            System.err.println("World doesn't exists exception. ID: " + serverid);
            return "";
        }
        return getById(serverid).name();
    }

    public static int getChannelCount() {
        if (channels <= 0) {
            for (Option e : values()) {
                if (e.getChannelCount() > channels) {
                    channels = e.getChannelCount();
                }
            }
        }
        return channels;
    }

    public static void loadSetting() {
        FLAG = ServerProperties.getProperty("FLAG", FLAG);
        EXP_RATE = ServerProperties.getProperty("EXP_RATE", EXP_RATE);
        MESO_RATE = ServerProperties.getProperty("MESO_RATE", MESO_RATE);
        DROP_RATE = ServerProperties.getProperty("DROP_RATE", DROP_RATE);
        WORLD_TIP = ServerProperties.getProperty("WORLD_TIP", WORLD_TIP);
        SCROLL_MESSAGE = ServerProperties.getProperty("SCROLL_MESSAGE", SCROLL_MESSAGE);
        CHANNEL_COUNT = ServerProperties.getProperty("CHANNEL_COUNT", CHANNEL_COUNT);
    }

    static {
        loadSetting();
    }
}
