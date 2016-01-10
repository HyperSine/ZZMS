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

import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.MapleClient;
import handling.channel.ChannelServer;
import handling.world.MapleMessenger;
import handling.world.MapleMessengerCharacter;
import handling.world.World;
import server.commands.CommandProcessor;
import server.commands.CommandType;
import tools.StringUtil;
import tools.data.LittleEndianAccessor;
import tools.packet.CField;
import tools.packet.CWvsContext;

public class ChatHandler {

    public static void GeneralChat(final String text, final byte unk, final MapleClient c, final MapleCharacter chr) {
        if (text.length() > 0 && chr != null && chr.getMap() != null && !CommandProcessor.processCommand(c, text, CommandType.NORMAL)) {
            if (!chr.isIntern() && text.length() >= 80) {
                return;
            }
            if (chr.getCanTalk() || chr.isStaff()) {
                //Note: This patch is needed to prevent chat packet from being broadcast to people who might be packet sniffing.
                byte[] colorChatPacket;
                colorChatPacket = ColourChat(chr, text, unk, chr.getChatType(), chr.isHidden());
                if (chr.isHidden()) {
                    if (colorChatPacket == null || !chr.isIntern()) {
                        chr.getMap().broadcastGMMessage(chr, CField.getChatText(chr.getId(), text, c.getPlayer().isSuperGM(), unk), true);
                    } else {
                        if (unk == 0) {
                            if (chr.isHiddenChatCanSee()) {
                                chr.getMap().broadcastMessage(colorChatPacket);
                            } else {
                                chr.getMap().broadcastGMMessage(chr, colorChatPacket, true);
                            }
                        }
                        chr.getMap().broadcastGMMessage(chr, CField.getChatText(chr.getId(), text, c.getPlayer().isSuperGM(), 1), true);
                    }
                } else {
                    chr.getCheatTracker().checkMsg();
                    if (colorChatPacket == null) {
                        chr.getMap().broadcastMessage(CField.getChatText(chr.getId(), text, c.getPlayer().isSuperGM(), unk));
                    } else {
                        if (unk == 0) {
                            chr.getMap().broadcastMessage(colorChatPacket);
                        }
                        chr.getMap().broadcastMessage(CField.getChatText(chr.getId(), text, c.getPlayer().isSuperGM(), 1));
                    }
                }
            } else {
                c.getSession().write(CWvsContext.broadcastMsg(6, "已被禁言, 無法說話"));
            }
        }
    }

    public static void AdminChat(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (!c.getPlayer().isGM()) {//if ( (signed int)CWvsContext::GetAdminLevel((void *)v294) > 2 )
            return;
        }
        byte mode = slea.readByte();
        //not saving slides...
        byte[] packet = CWvsContext.broadcastMsg(slea.readByte(), slea.readMapleAsciiString());//maybe I should make a check for the slea.readByte()... but I just hope gm's don't fuck things up :)
        switch (mode) {
            case 0:// /alertall, /noticeall, /slideall
                World.Broadcast.broadcastMessage(packet);
                break;
            case 1:// /alertch, /noticech, /slidech
                c.getChannelServer().broadcastMessage(packet);
                break;
            case 2:// /alertm /alertmap, /noticem /noticemap, /slidem /slidemap
                c.getPlayer().getMap().broadcastMessage(packet);
                break;

        }
    }

    public static byte[] ColourChat(final MapleCharacter chr, String text, final byte unk, short colour) {
        return ColourChat(chr, text, unk, colour, false);
    }

    public static byte[] ColourChat(final MapleCharacter chr, String text, final byte unk, short colour, boolean hidden) {
        String rank = "";
        if (chr.isAdmin()) {
            if ((hidden && chr.isHiddenChatCanSee()) || !hidden) {
                rank = "[伺服器管理員] ";
            }
            if (hidden) {
                colour = 10;
            }
        } else if (chr.isSuperGM()) {
            if ((hidden && chr.isHiddenChatCanSee()) || !hidden) {
                rank = "[高級管理員] ";
            }
            if (hidden) {
                colour = 10;
            }
        } else if (chr.isGM()) {
            if ((hidden && chr.isHiddenChatCanSee()) || !hidden) {
                rank = "[遊戲管理員] ";
            }
            if (hidden) {
                colour = 12;
            }
        } else if (chr.isIntern()) {
            if ((hidden && chr.isHiddenChatCanSee()) || !hidden) {
                rank = "[實習管理員] ";
            }
            if (hidden) {
                colour = 12;
            }
        } else if (chr.isDonator()) {
            rank = "[捐贈者] ";
        }

        if (rank.isEmpty() && colour == 0) {
            return null;
        } else {
            return CField.getGameMessage(rank + chr.getName() + " : " + text, colour);
        }
    }

    public static void Others(final LittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final int type = slea.readByte();
        final byte numRecipients = slea.readByte();
        if (numRecipients <= 0) {
            return;
        }
        int recipients[] = new int[numRecipients];

        for (byte i = 0; i < numRecipients; i++) {
            recipients[i] = slea.readInt();
        }
        final String chattext = slea.readMapleAsciiString();
        if (chr == null || !chr.getCanTalk()) {
            c.getSession().write(CWvsContext.broadcastMsg(6, "You have been muted and are therefore unable to talk."));
            return;
        }

        System.out.println(new StringBuilder().append("[聊天訊息] ").append(chr.getName()).append(" : ").append(chattext).toString());
        if (c.isMonitored()) {
            String chattype = "未知";
            switch (type) {
                case 0:
                    chattype = "好友";
                    break;
                case 1:
                    chattype = "隊伍";
                    break;
                case 2:
                    chattype = "公會";
                    break;
                case 3:
                    chattype = "聯盟";
                    break;
                case 4:
                    chattype = "遠征隊";
                    break;
            }
            World.Broadcast.broadcastGMMessage(
                    CWvsContext.broadcastMsg(6, "[GM 訊息] " + MapleCharacterUtil.makeMapleReadable(chr.getName())
                            + " 在 (" + chattype + ") 中說: " + chattext));

        }
        if (chattext.length() <= 0 || CommandProcessor.processCommand(c, chattext, CommandType.NORMAL)) {
            return;
        }
        chr.getCheatTracker().checkMsg();
        switch (type) {
            case 0:
                World.Buddy.buddyChat(recipients, chr.getId(), chr.getName(), chattext);
                break;
            case 1:
                if (chr.getParty() == null) {
                    break;
                }
                World.Party.partyChat(chr.getParty().getId(), chattext, chr.getName());
                break;
            case 2:
                if (chr.getGuildId() <= 0) {
                    break;
                }
                World.Guild.guildChat(chr.getGuildId(), chr.getName(), chr.getId(), chattext);
                break;
            case 3:
                if (chr.getGuildId() <= 0) {
                    break;
                }
                World.Alliance.allianceChat(chr.getGuildId(), chr.getName(), chr.getId(), chattext);
                break;
            case 4:
                if (chr.getParty().getExpeditionId() <= 0) {
                    break;
                }
                World.Party.expedChat(chr.getParty().getExpeditionId(), chattext, chr.getName());
                break;
            default:
                System.err.println("未知操作類型: ( 0x" + StringUtil.getLeftPaddedStr(Integer.toHexString(type).toUpperCase(), '0', 2) + " )" + slea.toString());
        }
    }

    public static void Messenger(final LittleEndianAccessor slea, final MapleClient c) {
        String input;
        MapleMessenger messenger = c.getPlayer().getMessenger();

        switch (slea.readByte()) {
            case 0x00: // open
                System.out.println("0");
                if (messenger == null) {
                    System.out.println("1");
                    slea.readByte();
                    byte mode = slea.readByte();
                    int messengerid = slea.readInt();
                    if (messengerid == 0) { // create
                        System.out.println("2");
                        c.getPlayer().setMessenger(World.Messenger.createMessenger(new MapleMessengerCharacter(c.getPlayer())));
                    } else { // join
                        System.out.println("3");
                        messenger = World.Messenger.getMessenger(messengerid);
                        if (messenger != null) {
                            System.out.println("4");
                            final int position = messenger.getLowestPosition();
                            if (position > -1 && position < 7) {
                                System.out.println("5");
                                c.getPlayer().setMessenger(messenger);
                                World.Messenger.joinMessenger(messenger.getId(), new MapleMessengerCharacter(c.getPlayer()), c.getPlayer().getName(), c.getChannel());
                            }
                        }
                    }
                }
                break;
            case 0x02: // exit
                if (messenger != null) {
                    final MapleMessengerCharacter messengerplayer = new MapleMessengerCharacter(c.getPlayer());
                    World.Messenger.leaveMessenger(messenger.getId(), messengerplayer);
                    c.getPlayer().setMessenger(null);
                }
                break;
            case 0x03: // invite
                if (messenger != null) {
                    final int position = messenger.getLowestPosition();
                    if (position <= -1 || position >= 7) {
                        return;
                    }
                    input = slea.readMapleAsciiString();
                    final MapleCharacter target = c.getChannelServer().getPlayerStorage().getCharacterByName(input);

                    if (target != null) {
                        if (target.getMessenger() == null) {
                            if (!target.isIntern() || c.getPlayer().isIntern()) {
                                c.getSession().write(CField.messengerNote(input, 4, 1));
                                target.getClient().getSession().write(CField.messengerInvite(c.getPlayer().getName(), messenger.getId()));
                            } else {
                                c.getSession().write(CField.messengerNote(input, 4, 0));
                            }
                        } else {
                            c.getSession().write(CField.messengerChat(c.getPlayer().getName(), " : " + target.getName() + " is already using Maple Messenger."));
                        }
                    } else {
                        if (World.isConnected(input)) {
                            World.Messenger.messengerInvite(c.getPlayer().getName(), messenger.getId(), input, c.getChannel(), c.getPlayer().isIntern());
                        } else {
                            c.getSession().write(CField.messengerNote(input, 4, 0));
                        }
                    }
                }
                break;
            case 0x05: // decline
                final String targeted = slea.readMapleAsciiString();
                final MapleCharacter target = c.getChannelServer().getPlayerStorage().getCharacterByName(targeted);
                if (target != null) { // This channel
                    if (target.getMessenger() != null) {
                        target.getClient().getSession().write(CField.messengerNote(c.getPlayer().getName(), 5, 0));
                    }
                } else { // Other channel
                    if (!c.getPlayer().isIntern()) {
                        World.Messenger.declineChat(targeted, c.getPlayer().getName());
                    }
                }
                break;
            case 0x06: // message
                if (messenger != null) {
                    final String charname = slea.readMapleAsciiString();
                    final String text = slea.readMapleAsciiString();
                    if (!c.getPlayer().isIntern() && text.length() >= 1000) {
                        return;
                    }
                    final String chattext = charname + "" + text;
                    World.Messenger.messengerChat(messenger.getId(), charname, text, c.getPlayer().getName());
                    if (messenger.isMonitored() && chattext.length() > c.getPlayer().getName().length() + 3) { //name : NOT name0 or name1
                        World.Broadcast.broadcastGMMessage(
                                CWvsContext.broadcastMsg(
                                        6, "[GM Message] " + MapleCharacterUtil.makeMapleReadable(c.getPlayer().getName()) + "(Messenger: "
                                        + messenger.getMemberNamesDEBUG() + ") said: " + chattext));
                    }
                }
                break;
            case 0x09: //like
                if (messenger != null) {
                    String charname = slea.readMapleAsciiString();
                }
                break;
            case 0x0A: //guidance
                if (messenger != null) {
                    slea.readByte();
                    String charname = slea.readMapleAsciiString();
                    String targetname = slea.readMapleAsciiString();
                }
                break;
            case 0x0B: //char info
                if (messenger != null) {
                    String charname = slea.readMapleAsciiString();
                    MapleCharacter character = c.getChannelServer().getPlayerStorage().getCharacterByName(charname);
                    c.getSession().write(CField.messengerCharInfo(character));
                }
                break;
            case 0x0E: //whisper
                if (messenger != null) {
                    String charname = slea.readMapleAsciiString();
                }
                break;
        }
    }

    public static void Command(final LittleEndianAccessor slea, final MapleClient c) {
        final byte mode = slea.readByte();
        slea.readInt(); //ticks
        switch (mode) {
            case 68: //好友buddy
            case 5: { //搜尋Find
                final String recipient = slea.readMapleAsciiString();
                MapleCharacter player = c.getChannelServer().getPlayerStorage().getCharacterByName(recipient);
                if (player != null) {
                    if (!player.isIntern() || c.getPlayer().isIntern() && player.isIntern()) {

                        c.getSession().write(CField.getFindReplyWithMap(player.getName(), player.getMap().getId(), mode == 72));//68
                    } else {
                        c.getSession().write(CField.getWhisperReply(recipient, (byte) 0));
                    }
                } else { //沒找到Not found
                    int ch = World.Find.findChannel(recipient);
                    if (ch > 0) {
                        player = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(recipient);
                        if (player == null) {
                            break;
                        }
                        if (player != null) {
                            if (!player.isIntern() || (c.getPlayer().isIntern() && player.isIntern())) {
                                c.getSession().write(CField.getFindReply(recipient, (byte) ch, mode == 68));
                            } else {
                                c.getSession().write(CField.getWhisperReply(recipient, (byte) 0));
                            }
                            return;
                        }
                    }
                    if (ch == -10) {
                        c.getSession().write(CField.getFindReplyWithCS(recipient, mode == 68));
                    } else {
                        c.getSession().write(CField.getWhisperReply(recipient, (byte) 0));
                    }
                }
                break;
            }
            case 6: { //悄悄話Whisper
                if (c.getPlayer() == null || c.getPlayer().getMap() == null) {
                    return;
                }
                if (!c.getPlayer().getCanTalk()) {
                    c.getSession().write(CWvsContext.broadcastMsg(6, "You have been muted and are therefore unable to talk."));
                    return;
                }
                c.getPlayer().getCheatTracker().checkMsg();
                final String recipient = slea.readMapleAsciiString();
                final String text = slea.readMapleAsciiString();
                final int ch = World.Find.findChannel(recipient);
                if (!c.getPlayer().isIntern() && text.length() >= 80) {
                    return;
                }
                if (ch > 0) {
                    MapleCharacter player = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(recipient);
                    if (player == null) {
                        break;
                    }
                    player.getClient().getSession().write(CField.getWhisper(c.getPlayer().getName(), c.getChannel(), text));
                    if (!c.getPlayer().isIntern() && player.isIntern()) {
                        c.getSession().write(CField.getWhisperReply(recipient, (byte) 0));
                    } else {
                        c.getSession().write(CField.getWhisperReply(recipient, (byte) 1));
                    }
                    if (c.isMonitored()) {
                        World.Broadcast.broadcastGMMessage(CWvsContext.broadcastMsg(6, c.getPlayer().getName() + " whispered " + recipient + " : " + text));
                    } else if (player.getClient().isMonitored()) {
                        World.Broadcast.broadcastGMMessage(CWvsContext.broadcastMsg(6, c.getPlayer().getName() + " whispered " + recipient + " : " + text));
                    }
                } else {
                    c.getSession().write(CField.getWhisperReply(recipient, (byte) 0));
                }
            }
            break;
            default:{
                System.err.println("未知操作類型: ( 0x" + StringUtil.getLeftPaddedStr(Integer.toHexString(mode).toUpperCase(), '0', 2) + " )" + slea.toString());
            }
        }
    }
}
