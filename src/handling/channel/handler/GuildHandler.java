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
package handling.channel.handler;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

import client.MapleCharacter;
import client.MapleClient;

import client.Skill;
import client.SkillFactory;
import handling.channel.MapleGuildRanking;
import handling.world.World;
import handling.world.guild.*;
import server.MapleStatEffect;
import tools.packet.CField;
import tools.data.LittleEndianAccessor;
import tools.Pair;
import tools.StringUtil;
import tools.packet.CWvsContext.GuildPacket;

public class GuildHandler {

    public static final void DenyGuildRequest(final String from, final MapleClient c) {
        final MapleCharacter cfrom = c.getChannelServer().getPlayerStorage().getCharacterByName(from);
        if (cfrom != null && invited.remove(c.getPlayer().getName().toLowerCase()) != null) {
            cfrom.dropMessage(5, "'" + c.getPlayer().getName() + "'拒絕了公會邀請.");
//            cfrom.getClient().getSession().write(GuildPacket.denyGuildInvitation(c.getPlayer().getName()));
        }
    }

    public static final void JoinGuildRequest(final int guildId, final MapleClient c) {
        c.getPlayer().setGuildId(guildId);
        int addJoinList = World.Guild.addGuildJoinMember(c.getPlayer().getMGC());
        if (addJoinList == 0) {
            c.getPlayer().dropMessage(1, "公會成員已經達到最高限制。");
            return;
        }
        c.getPlayer().setGuildId(0);
    }

    public static final void JoinGuildCancel(final MapleClient c) {
        c.getPlayer().setGuildId(MapleGuild.getJoinGuildId(c.getPlayer().getId()));
        World.Guild.removeGuildJoinMember(c.getPlayer().getMGC());
        c.getSession().write(GuildPacket.removeGuildJoin(c.getPlayer().getId()));
        c.getPlayer().setGuildId(0);
    }

    public static final void AddGuildMember(final LittleEndianAccessor slea, final MapleClient c) {
        int size = slea.readByte();
        for (int i = 0; i < size; i++) {
            MapleCharacter chr = MapleCharacter.getCharacterById(slea.readInt());
            chr.setGuildId(c.getPlayer().getGuildId());
            chr.setGuildRank((byte) 5);
            int s = World.Guild.addGuildMember(chr.getMGC());
            if (s == 0) {
                c.getPlayer().dropMessage(1, "公會成員已經達到最高限制。");
                chr.setGuildId(0);
                return;
            }
            chr.getClient().getSession().write(GuildPacket.showGuildInfo(chr));
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            for (byte[] pack : World.Alliance.getAllianceInfo(gs.getAllianceId(), true)) {
                if (pack != null) {
                    chr.getClient().getSession().write(pack);
                }
            }
            chr.saveGuildStatus();
            respawnPlayer(chr);
        }
    }

    public static final void DenyGuildJoin(final LittleEndianAccessor slea, final MapleClient c) {
        for (int i = 0; i < (slea.readByte() & 0xFF); i++) {
            int cid = slea.readInt();
            MapleGuild guild = World.Guild.getGuild(MapleGuild.getJoinGuildId(cid));
            guild.setGuildQuest(false, MapleCharacter.getCharacterById(cid));
            byte[] packet = GuildPacket.removeGuildJoin(cid);
            MapleCharacter chr = c.getChannelServer().getPlayerStorage().getCharacterById(cid);
            if (chr != null) {
                chr.getClient().getSession().write(packet);
            }
            guild.broadcast(packet);
        }
    }

    private static boolean isGuildNameAcceptable(final String name) {
        if (name.getBytes().length < 3 || name.getBytes().length > 12) {
            return false;
        }
        return World.Guild.getGuildByName(name) == null;
    }

    private static void respawnPlayer(final MapleCharacter mc) {
        if (mc.getMap() == null) {
            return;
        }
        mc.getMap().broadcastMessage(CField.loadGuildName(mc));
        mc.getMap().broadcastMessage(CField.loadGuildIcon(mc));
    }
    private static final Map<String, Pair<Integer, Long>> invited = new HashMap<>();
    private static long nextPruneTime = System.currentTimeMillis() + 5 * 60 * 1000;

    private static String GuildName;

    public static final void Guild(final LittleEndianAccessor slea, final MapleClient c) {
        final long currentTime = System.currentTimeMillis();
        if (currentTime >= nextPruneTime) {
            Iterator<Entry<String, Pair<Integer, Long>>> itr = invited.entrySet().iterator();
            Entry<String, Pair<Integer, Long>> inv;
            while (itr.hasNext()) {
                inv = itr.next();
                if (currentTime >= inv.getValue().right) {
                    itr.remove();
                }
            }
            nextPruneTime += 5 * 60 * 1000;
        }
        byte mode = slea.readByte();
        int guildId;
        String name;
        int cid;
        switch (mode) { //AFTERSHOCK: most are +1
            case 0:
                c.getSession().write(GuildPacket.showGuildInfo(c.getPlayer()));
                break;
            case 0x01: // 接受邀請
                if (c.getPlayer().getGuildId() > 0) {
                    return;
                }
                invited.remove(c.getPlayer().getName().toLowerCase());
            case 0x02: // 顯示公會
                guildId = slea.readInt();
                c.getSession().write(GuildPacket.getGuildReceipt(guildId));
                break;
            case 0x04: // 檢查公會名稱
                GuildName = slea.readMapleAsciiString();
                if (!isGuildNameAcceptable(GuildName)) {
                    c.getSession().write(GuildPacket.genericGuildMessage((byte) 0x34)); // 公會名稱重複 Version.176 [完成]
                    return;
                }
                c.getSession().write(GuildPacket.createGuildNotice(GuildName));
                break;
            case 0x07: // 邀請
                if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 2) { // 1 == guild master, 2 == jr
                    return;
                }
                name = slea.readMapleAsciiString().toLowerCase();
                MapleCharacter cfrom = c.getChannelServer().getPlayerStorage().getCharacterByName(name);
                if (cfrom.getGuildId() > 0) {
                    c.getPlayer().dropMessage(1, "玩家已經加入其他公會。");
                    return;
                }/*
                 if (invited.containsKey(name)) {
                 c.getPlayer().dropMessage(5, "玩家正在處理邀請。");
                 return;
                 }*/

                final MapleGuildResponse mgr = MapleGuild.sendInvite(c, name);

                if (mgr != null) {
                    c.getSession().write(mgr.getPacket());
                } else {
                    c.getPlayer().dropMessage(5, "已邀請'" + name + "'加入公會。");
                    invited.put(name, new Pair<>(c.getPlayer().getGuildId(), currentTime + (20 * 60000))); //20 mins expire
                }
                break;
            case 0x0B: // 離開
                cid = slea.readInt();
                name = slea.readMapleAsciiString();

                if (cid != c.getPlayer().getId() || !name.equals(c.getPlayer().getName()) || c.getPlayer().getGuildId() <= 0) {
                    return;
                }
                World.Guild.leaveGuild(c.getPlayer().getMGC());
                c.getSession().write(GuildPacket.showGuildInfo(null));
                break;
            case 0x0C: // 驅逐
                cid = slea.readInt();
                name = slea.readMapleAsciiString();

                if (c.getPlayer().getGuildRank() > 2 || c.getPlayer().getGuildId() <= 0) {
                    return;
                }
                World.Guild.expelMember(c.getPlayer().getMGC(), name, cid);
                break;
            case 0x12: // 更變職位名稱
                if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() != 1) {
                    return;
                }
                String ranks[] = new String[5];
                for (int i = 0; i < 5; i++) {
                    ranks[i] = slea.readMapleAsciiString();
                }

                World.Guild.changeRankTitle(c.getPlayer().getGuildId(), ranks);
                break;
            case 0x13: // 更變職位
                cid = slea.readInt();
                byte newRank = slea.readByte();

                if ((newRank <= 1 || newRank > 5) || c.getPlayer().getGuildRank() > 2 || (newRank <= 2 && c.getPlayer().getGuildRank() != 1) || c.getPlayer().getGuildId() <= 0) {
                    return;
                }

                World.Guild.changeRank(c.getPlayer().getGuildId(), cid, newRank);
                break;
            case 0x14: // 更變圖標
                if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() != 1 || c.getPlayer().getMapId() != 200000301) {
                    return;
                }

                final short bg = slea.readShort();
                final byte bgcolor = slea.readByte();
                final short logo = slea.readShort();
                final byte logocolor = slea.readByte();

                World.Guild.setGuildEmblem(c.getPlayer().getGuildId(), bg, bgcolor, logo, logocolor);

                respawnPlayer(c.getPlayer());
                break;
            case 0x11: // 更變公告
                final String notice = slea.readMapleAsciiString();
                if (notice.length() > 100 || c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 2) {
                    return;
                }
                World.Guild.setGuildNotice(c.getPlayer().getGuildId(), notice);
                break;
            case 0x1d: //guild skill purchase
                Skill skilli = SkillFactory.getSkill(slea.readInt());
                if (c.getPlayer().getGuildId() <= 0 || skilli == null || skilli.getId() < 91000000) {
                    return;
                }
                int eff = World.Guild.getSkillLevel(c.getPlayer().getGuildId(), skilli.getId()) + 1;
                if (eff > skilli.getMaxLevel()) {
                    return;
                }
                final MapleStatEffect skillid = skilli.getEffect(eff);
                if (skillid.getReqGuildLevel() <= 0 || c.getPlayer().getMeso() < skillid.getPrice()) {
                    return;
                }
                if (World.Guild.purchaseSkill(c.getPlayer().getGuildId(), skillid.getSourceId(), c.getPlayer().getName(), c.getPlayer().getId())) {
                    c.getPlayer().gainMeso(-skillid.getPrice(), true);
                }
                break;
            case 0x1e: //guild skill activation
                skilli = SkillFactory.getSkill(slea.readInt());
                if (c.getPlayer().getGuildId() <= 0 || skilli == null) {
                    return;
                }
                eff = World.Guild.getSkillLevel(c.getPlayer().getGuildId(), skilli.getId());
                if (eff <= 0) {
                    return;
                }
                final MapleStatEffect skillii = skilli.getEffect(eff);
                if (skillii.getReqGuildLevel() < 0 || c.getPlayer().getMeso() < skillii.getExtendPrice()) {
                    return;
                }
                if (World.Guild.activateSkill(c.getPlayer().getGuildId(), skillii.getSourceId(), c.getPlayer().getName())) {
                    c.getPlayer().gainMeso(-skillii.getExtendPrice(), true);
                }
                break;
            case 0x1f: //guild leader change
                cid = slea.readInt();
                if (c.getPlayer().getGuildId() <= 0 || c.getPlayer().getGuildRank() > 1) {
                    return;
                }
                World.Guild.setGuildLeader(c.getPlayer().getGuildId(), cid);
                break;
            case 0x2D: // 公會搜尋
                switch (slea.readByte()) {
                    case 0:// 名字搜尋
                        c.getSession().write(GuildPacket.showSearchGuilds(MapleGuild.searchGuild(slea.readShort(), slea.readByte() == 1, slea.readMapleAsciiString())));
                        break;
                    case 1:// 條件搜尋
                        int[] keyWords = new int[6];
                        for (int i = 0; i < 6; i++) {
                            keyWords[i] = slea.readByte() & 0xFF;
                        }
                        c.getSession().write(GuildPacket.showSearchGuilds(MapleGuild.searchGuild(keyWords)));
                        break;
                }
                break;
            case 0x36: // 建立公會提示 Version.176 [完成]
                int create = slea.readByte();
                if (create == 1) { // 建立公會成功
                    int cost = 5000000;
                    if ((c.getPlayer().getGuildId() > 0) || (c.getPlayer().getMapId() != 200000301)) {
                        c.getPlayer().dropMessage(1, "無法建立公會\r\n已經有公會或不在英雄之殿");
                        return;
                    }
                    if (c.getPlayer().getMeso() < cost) {
                        c.getPlayer().dropMessage(1, "你沒有足夠的楓幣建立公會。目前建立公會需要: " + cost + " 的楓幣。");
                        return;
                    }
                    guildId = World.Guild.createGuild(c.getPlayer().getId(), GuildName);
                    if (guildId == 0) {
                        c.getSession().write(GuildPacket.genericGuildMessage((byte) 0x3E)); // 建立公會異常 Version.176 [完成]
                        return;
                    }
                    c.getPlayer().gainMeso(-cost, true, true);
                    c.getPlayer().setGuildId(guildId);
                    c.getPlayer().setGuildRank((byte) 1);
                    c.getPlayer().saveGuildStatus();
                    World.Guild.setGuildMemberOnline(c.getPlayer().getMGC(), true, c.getChannel());
                    //c.getSession().write(GuildPacket.showGuildInfo(c.getPlayer()));
                    c.getSession().write(GuildPacket.newGuildInfo(c.getPlayer()));
                    World.Guild.gainGP(c.getPlayer().getGuildId(), 500, c.getPlayer().getId());
                    MapleGuildRanking.getInstance().load(true);
                    //c.getPlayer().dropMessage(1, "恭喜你成功创建家族.");
                    respawnPlayer(c.getPlayer());
                } else if (create == 0) { // 建立公會失敗
                    c.getSession().write(GuildPacket.genericGuildMessage((byte) 0x3D)); // Version.176 [完成]
                }
                break;
            default:
                System.out.println("未知公會操作類型: ( 0x" + StringUtil.getLeftPaddedStr(Integer.toHexString(mode).toUpperCase(), '0', 2) + " )" + slea.toString());
        }
    }
}
