package tools.packet;

import client.*;
import client.MapleStat.Temp;
import client.inventory.*;
import constants.GameConstants;
import constants.ServerConfig;
import handling.SendPacketOpcode;
import handling.channel.DojoRankingsData;
import handling.channel.MapleGeneralRanking.CandyRankingInfo;
import handling.channel.MapleGuildRanking;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.World;
import handling.world.exped.MapleExpedition;
import handling.world.exped.PartySearch;
import handling.world.exped.PartySearchType;
import handling.world.family.MapleFamily;
import handling.world.family.MapleFamilyBuff;
import handling.world.family.MapleFamilyCharacter;
import handling.world.guild.*;
import java.awt.Point;
import java.util.*;
import java.util.Map.Entry;
import server.MapleItemInformationProvider;
import server.MapleStatEffect;
import server.StructFamiliar;
import server.life.PlayerNPC;
import server.stores.HiredMerchant;
import server.stores.MaplePlayerShopItem;
import tools.DateUtil;
import tools.HexTool;
import tools.Pair;
import tools.StringUtil;
import tools.data.MaplePacketLittleEndianWriter;

public class CWvsContext {

    public static byte[] enableActions() {
        return updatePlayerStats(new EnumMap<MapleStat, Long>(MapleStat.class), true, null);
    }

    public static byte[] updatePlayerStats(Map<MapleStat, Long> stats, MapleCharacter chr) {
        return updatePlayerStats(stats, false, chr);
    }

    public static byte[] updatePlayerStats(Map<MapleStat, Long> mystats, boolean itemReaction, MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_STATS.getValue());
        mplew.write(itemReaction ? 1 : 0);
        long updateMask = 0L;
        for (MapleStat statupdate : mystats.keySet()) {
            updateMask |= statupdate.getValue();
        }
        mplew.writeLong(updateMask);
        for (final Entry<MapleStat, Long> statupdate : mystats.entrySet()) {
            switch (statupdate.getKey()) {
                case SKIN:
                case LEVEL:
                case FATIGUE:
                case BATTLE_RANK:
                case ICE_GAGE:
                    mplew.write((statupdate.getValue()).byteValue());
                    break;
                case JOB:
                    mplew.writeShort((statupdate.getValue()).shortValue());
                    mplew.writeShort(chr.getSubcategory());
                    break;
                case STR:
                case DEX:
                case INT:
                case LUK:
                case AVAILABLEAP:
                    mplew.writeShort((statupdate.getValue()).shortValue());
                    break;
                case AVAILABLESP:
                    if (GameConstants.isSeparatedSp(chr.getJob())) {
                        mplew.write(chr.getRemainingSpSize());
                        for (int i = 0; i < chr.getRemainingSps().length; i++) {
                            if (chr.getRemainingSp(i) > 0) {
                                mplew.write(i + 1);
                                mplew.writeInt(chr.getRemainingSp(i));
                            }
                        }
                    } else {
                        mplew.writeShort(chr.getRemainingSp());
                    }
                    break;
                case TRAIT_LIMIT:
                    mplew.writeInt((statupdate.getValue()).intValue());
                    mplew.writeInt((statupdate.getValue()).intValue());
                    mplew.writeInt((statupdate.getValue()).intValue());
                    break;
                case EXP:
                case GACHAPONEXP:
                case MESO:
                    mplew.writeLong((statupdate.getValue()));
                    break;
                case PET:
                    mplew.writeLong((statupdate.getValue()).intValue());
                    mplew.writeLong((statupdate.getValue()).intValue());
                    mplew.writeLong((statupdate.getValue()).intValue());
                    break;
                default:
                    mplew.writeInt((statupdate.getValue()).intValue());
            }
        }

        mplew.write(-1);//176+
        mplew.writeShort(0);//176+

        if ((updateMask == 0L) && (!itemReaction)) {
            mplew.write(1);
        }

        mplew.write(0);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] setTemporaryStats(short str, short dex, short _int, short luk, short watk, short matk, short acc, short avoid, short speed, short jump) {
        Map<Temp, Integer> stats = new EnumMap<>(MapleStat.Temp.class);

        stats.put(MapleStat.Temp.STR, Integer.valueOf(str));
        stats.put(MapleStat.Temp.DEX, Integer.valueOf(dex));
        stats.put(MapleStat.Temp.INT, Integer.valueOf(_int));
        stats.put(MapleStat.Temp.LUK, Integer.valueOf(luk));
        stats.put(MapleStat.Temp.WATK, Integer.valueOf(watk));
        stats.put(MapleStat.Temp.MATK, Integer.valueOf(matk));
        stats.put(MapleStat.Temp.ACC, Integer.valueOf(acc));
        stats.put(MapleStat.Temp.AVOID, Integer.valueOf(avoid));
        stats.put(MapleStat.Temp.SPEED, Integer.valueOf(speed));
        stats.put(MapleStat.Temp.JUMP, Integer.valueOf(jump));

        return temporaryStats(stats);
    }

    public static byte[] temporaryStats_Aran() {
        Map<Temp, Integer> stats = new EnumMap<>(MapleStat.Temp.class);

        stats.put(MapleStat.Temp.STR, 999);
        stats.put(MapleStat.Temp.DEX, 999);
        stats.put(MapleStat.Temp.INT, 999);
        stats.put(MapleStat.Temp.LUK, 999);
        stats.put(MapleStat.Temp.WATK, 255);
        stats.put(MapleStat.Temp.ACC, 999);
        stats.put(MapleStat.Temp.AVOID, 999);
        stats.put(MapleStat.Temp.SPEED, 140);
        stats.put(MapleStat.Temp.JUMP, 120);

        return temporaryStats(stats);
    }

    public static byte[] temporaryStats_Balrog(MapleCharacter chr) {
        Map<Temp, Integer> stats = new EnumMap<>(MapleStat.Temp.class);

        int offset = 1 + (chr.getLevel() - 90) / 20;
        stats.put(MapleStat.Temp.STR, chr.getStat().getTotalStr() / offset);
        stats.put(MapleStat.Temp.DEX, chr.getStat().getTotalDex() / offset);
        stats.put(MapleStat.Temp.INT, chr.getStat().getTotalInt() / offset);
        stats.put(MapleStat.Temp.LUK, chr.getStat().getTotalLuk() / offset);
        stats.put(MapleStat.Temp.WATK, chr.getStat().getTotalWatk() / offset);
        stats.put(MapleStat.Temp.MATK, chr.getStat().getTotalMagic() / offset);

        return temporaryStats(stats);
    }

    public static byte[] temporaryStats(Map<MapleStat.Temp, Integer> mystats) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TEMP_STATS.getValue());
        int updateMask = 0;
        for (MapleStat.Temp statupdate : mystats.keySet()) {
            updateMask |= statupdate.getValue();
        }
        mplew.writeInt(updateMask);
        for (final Entry<MapleStat.Temp, Integer> statupdate : mystats.entrySet()) {
            switch (statupdate.getKey()) {
                case SPEED:
                case JUMP:
                case UNKNOWN:
                    mplew.write((statupdate.getValue()).byteValue());
                    break;
                default:
                    mplew.writeShort((statupdate.getValue()).shortValue());
            }
        }

        return mplew.getPacket();
    }

    public static byte[] temporaryStats_Reset() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TEMP_STATS_RESET.getValue());

        return mplew.getPacket();
    }

    public static byte[] updateSkills(Map<Skill, SkillEntry> update, boolean hyper) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_SKILLS.getValue());
        boolean unk = false;
        mplew.writeBoolean(true); // get_update_time
        mplew.write(0);
        mplew.writeBoolean(unk); // masterlevel?
        mplew.writeShort(update.size());
        for (Map.Entry z : update.entrySet()) {
            mplew.writeInt(((Skill) z.getKey()).getId());
            mplew.writeInt(((SkillEntry) z.getValue()).skillLevel);
            mplew.writeInt(((SkillEntry) z.getValue()).masterlevel);
            PacketHelper.addExpirationTime(mplew, ((SkillEntry) z.getValue()).expiration);
        }
        mplew.write(/*hyper ? 0x0C : */4);

        return mplew.getPacket();
    }

    public static byte[] giveFameErrorResponse(int op) {
        return OnFameResult(op, null, true, 0);
    }

    public static byte[] OnFameResult(int op, String charname, boolean raise, int newFame) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FAME_RESPONSE.getValue());
        mplew.write(op);
        if ((op == 0) || (op == 5)) {
            mplew.writeMapleAsciiString(charname == null ? "" : charname);
            mplew.write(raise ? 1 : 0);
            if (op == 0) {
                mplew.writeInt(newFame);
            }
        }

        return mplew.getPacket();
    }

    public static byte[] fullClientDownload() {
        //Opens "http://maplestory.nexon.net/support/game-download"
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FULL_CLIENT_DOWNLOAD.getValue());

        return mplew.getPacket();
    }

    public static byte[] bombLieDetector(boolean error, int mapid, int channel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LIE_DETECTOR.getValue());
        mplew.write(error ? 2 : 1);
        mplew.writeInt(mapid);
        mplew.writeInt(channel);

        return mplew.getPacket();
    }

    public static byte[] sendLieDetector(final byte[] image) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LIE_DETECTOR.getValue());
        mplew.write(6); // 1 = not attacking, 2 = tested, 3 = going through 

        mplew.write(4); // 2 give invalid pointer (suppose to be admin macro) 
        mplew.write(1); // the time >0 is always 1 minute 
        if (image == null) {
            mplew.writeInt(0);
            return mplew.getPacket();
        }
        mplew.writeInt(image.length);
        mplew.write(image);

        return mplew.getPacket();
    }

    public static byte[] LieDetectorResponse(final byte msg) {
        return LieDetectorResponse(msg, (byte) 0);
    }

    public static byte[] LieDetectorResponse(final byte msg, final byte msg2) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LIE_DETECTOR.getValue());
        mplew.write(msg); // 1 = not attacking, 2 = tested, 3 = going through 
        mplew.write(msg2);

        return mplew.getPacket();
    }

    public static byte[] getLieDetector(byte type, String tester) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LIE_DETECTOR.getValue()); // 2A 00 01 00 00 00  
        mplew.write(type); // 1 = not attacking, 2 = tested, 3 = going through, 4 save screenshot 
        switch (type) {
            case 4: //save screen shot 
                mplew.write(0);
                mplew.writeMapleAsciiString(""); // file name 
                break;
            case 5:
                mplew.write(1); // 2 = save screen shot 
                mplew.writeMapleAsciiString(tester); // me or file name 
                break;
            case 6:
                mplew.write(4); // 2 or anything else, 2 = with maple admin picture, basicaly manager's skill? 
                mplew.write(1); // if > 0, then time = 60,000..maybe try < 0? 
                //mplew.writeInt(size);
                //mplew.write(byte); // bytes 
                break;
            case 7://send this if failed 
                // 2 = You have been appointed as a auto BOT program user and will be restrained. 
                mplew.write(4); // default 
                break;
            case 9:
                // 0 = passed lie detector test 
                // 1 = reward 5000 mesos for not botting. 
                // 2 = thank you for your cooperation with administrator. 
                mplew.write(0);
                break;
            case 8: // save screen shot.. it appears that you may be using a macro-assisted program
                mplew.write(0); // 2 or anything else , 2 = show msg, 0 = none 
                mplew.writeMapleAsciiString(""); // file name 
                break;
            case 10: // no save 
                mplew.write(0); // 2 or anything else, 2 = show msg 
                mplew.writeMapleAsciiString(""); // ?? // hi_You have passed the lie detector test 
                break;
            default:
                mplew.write(0);
                break;
        }
        return mplew.getPacket();
    }

    public static byte[] lieDetector(byte mode, byte action, byte[] image, String str1, String str2, String str3) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LIE_DETECTOR.getValue());
        mplew.write(mode);
        mplew.write(action); //2 = show msg/save screenshot/maple admin picture(mode 6)
        if (mode == 6) {
            mplew.write(1); //if true time is 60:00
            PacketHelper.addImageInfo(mplew, image);
        }
        if (mode == 7 || mode == 9) {
        }
        if (mode == 4) { //save screenshot
            mplew.writeMapleAsciiString(str1); //file name
        }
        if (mode != 5) {
            if (mode == 10) {
                mplew.writeMapleAsciiString(str2); //passed lie detector message
            } else {
                if (mode != 8) {
                }
                mplew.writeMapleAsciiString(str2); //failed lie detector, file name (for screenshot)
            }
        }
        mplew.writeMapleAsciiString(str3); //file name for screenshot

        return mplew.getPacket();
    }

    public static byte[] report(int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.REPORT_RESPONSE.getValue());
        mplew.write(mode);
        if (mode == 2) {
            mplew.write(0);
            mplew.writeInt(1); //times left to report
        }

        return mplew.getPacket();
    }

    public static byte[] OnSetClaimSvrAvailableTime(int from, int to) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(4);

        mplew.writeShort(SendPacketOpcode.REPORT_TIME.getValue());
        mplew.write(from);
        mplew.write(to);

        return mplew.getPacket();
    }

    public static byte[] OnClaimSvrStatusChanged(boolean enable) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);

        mplew.writeShort(SendPacketOpcode.REPORT_STATUS.getValue());
        mplew.write(enable ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] updateMount(MapleCharacter chr, boolean levelup) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_MOUNT.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(chr.getMount().getLevel());
        mplew.writeInt(chr.getMount().getExp());
        mplew.writeInt(chr.getMount().getFatigue());
        mplew.write(levelup ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] showQuestCompletion(int id) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOW_QUEST_COMPLETION.getValue());
        mplew.writeShort(id);

        return mplew.getPacket();
    }

    public static byte[] useSkillBook(MapleCharacter chr, int skillid, int maxlevel, boolean canuse, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.USE_SKILL_BOOK.getValue());
        mplew.write(0);
        mplew.writeInt(chr.getId());
        mplew.write(1);
        mplew.writeInt(skillid);
        mplew.writeInt(maxlevel);
        mplew.write(canuse ? 1 : 0);
        mplew.write(success ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] useAPSPReset(boolean spReset, int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(spReset ? SendPacketOpcode.SP_RESET.getValue() : SendPacketOpcode.AP_RESET.getValue());
        mplew.write(1);
        mplew.writeInt(cid);
        mplew.write(1);

        return mplew.getPacket();
    }

    public static byte[] expandCharacterSlots(int mode) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.EXPAND_CHARACTER_SLOTS.getValue());
        mplew.writeInt(mode);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] finishedGather(int type) {
        return gatherSortItem(true, type);
    }

    public static byte[] finishedSort(int type) {
        return gatherSortItem(false, type);
    }

    public static byte[] gatherSortItem(boolean gather, int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(gather ? SendPacketOpcode.FINISH_GATHER.getValue() : SendPacketOpcode.FINISH_SORT.getValue());
        mplew.write(1);
        mplew.write(type);

        return mplew.getPacket();
    }

    public static byte[] updateExpPotion(int mode, int id, int itemId, boolean firstTime, int level, int potionDstLevel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.EXP_POTION.getValue());
        mplew.write(mode);
        mplew.write(1); //bool for get_update_time
        mplew.writeInt(id);
        if (id != 0) {
            mplew.write(1); //not even being read how rude of nexon
            if (mode == 1) {
                mplew.writeInt(0);
            }
            if (mode == 2) {
                mplew.write(firstTime ? 1 : 0); //1 on first time then it turns 0
                mplew.writeInt(itemId);
                if (itemId != 0) {
                    mplew.writeInt(level); //level, confirmed
                    mplew.writeInt(potionDstLevel); //max level with potion
                    mplew.writeLong(384); //random, more like potion id
                }
            }
        }

        return mplew.getPacket();
    }

    public static byte[] updateGender(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_GENDER.getValue());
        mplew.write(chr.getGender());

        return mplew.getPacket();
    }

    public static byte[] charInfo(MapleCharacter chr, boolean isSelf) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CHAR_INFO.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(0); // 未知
        mplew.write(chr.getLevel());
        mplew.writeShort(chr.getJob());
        mplew.writeShort(chr.getSubcategory());
        mplew.write(chr.getStat().pvpRank);
        mplew.writeInt(chr.getFame());
        MapleMarriage marriage = chr.getMarriage();
        mplew.write(marriage != null && marriage.getId() != 0);
        if (marriage != null && marriage.getId() != 0) {
            mplew.writeInt(marriage.getId()); //marriage id
            mplew.writeInt(marriage.getHusbandId()); //husband char id
            mplew.writeInt(marriage.getWifeId()); //wife char id
            mplew.writeShort(3); //msg type
            mplew.writeInt(chr.getMarriageItemId()); //ring id husband
            mplew.writeInt(chr.getMarriageItemId()); //ring id wife
            mplew.writeAsciiString(marriage.getHusbandName(), 15); //husband name
            mplew.writeAsciiString(marriage.getWifeName(), 15); //wife name
        }
        List prof = chr.getProfessions();
        mplew.write(prof.size());
        for (Iterator ii = prof.iterator(); ii.hasNext();) {
            int i = ((Integer) ii.next());
            mplew.writeShort(i);
        }
        if (chr.getGuildId() <= 0) {
            mplew.writeMapleAsciiString("-");
            mplew.writeMapleAsciiString("");
        } else {
            MapleGuild gs = World.Guild.getGuild(chr.getGuildId());
            if (gs != null) {
                mplew.writeMapleAsciiString(gs.getName());
                if (gs.getAllianceId() > 0) {
                    MapleGuildAlliance allianceName = World.Alliance.getAlliance(gs.getAllianceId());
                    if (allianceName != null) {
                        mplew.writeMapleAsciiString(allianceName.getName());
                    } else {
                        mplew.writeMapleAsciiString("");
                    }
                } else {
                    mplew.writeMapleAsciiString("");
                }
            } else {
                mplew.writeMapleAsciiString("-");
                mplew.writeMapleAsciiString("");
            }
        }
        mplew.write(isSelf ? -1 : 0);
        mplew.write(0);
        mplew.writeMapleAsciiString("");
        mplew.write(0);
        mplew.write(0);
        mplew.write(0);
        mplew.write(0);
        mplew.write(0);
        mplew.write(chr.getPets().isEmpty() ? 0 : 1);
        byte index = 1;
        for (MaplePet pet : chr.getSummonedPets()) {
            mplew.write(index);
            mplew.writeInt(chr.getPetIndex(pet));
            mplew.writeInt(pet.getPetItemId());
            mplew.writeMapleAsciiString(pet.getName());
            mplew.write(pet.getLevel());
            mplew.writeShort(pet.getCloseness());
            mplew.write(pet.getFullness());
            mplew.writeShort(pet.getFlags());
            Item inv = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) (index == 2 ? -130 : index == 1 ? -114 : -138));
            mplew.writeInt(inv == null ? 0 : inv.getItemId());
            mplew.writeInt(-1);
        }
        mplew.write(0);
        int wishlistSize = chr.getWishlistSize();
        mplew.write(wishlistSize);
        if (wishlistSize > 0) {
            int[] wishlist = chr.getWishlist();
            for (int x = 0; x < wishlistSize; x++) {
                mplew.writeInt(wishlist[x]);
            }
        }
        Item medal = chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -46);
        mplew.writeInt(medal == null ? 0 : medal.getItemId());
        List<Pair<Integer, Long>> medalQuests = chr.getCompletedMedals();
        mplew.writeShort(medalQuests.size());
        for (Pair x : medalQuests) {
            mplew.writeShort(((Integer) x.left));
            mplew.writeLong(((Long) x.right));
        }
        for (MapleTrait.MapleTraitType t : MapleTrait.MapleTraitType.values()) {
            mplew.write(chr.getTrait(t).getLevel());
        }
        List chairs = new ArrayList();
        for (Item i : chr.getInventory(MapleInventoryType.SETUP).newList()) {
            if ((i.getItemId() / 10000 == 301) && (!chairs.contains(i.getItemId()))) {
                chairs.add(i.getItemId());
            }
        }
        mplew.writeInt(chairs.size());
        for (Iterator ii = chairs.iterator(); ii.hasNext();) {
            int i = ((Integer) ii.next());
            mplew.writeInt(i);
        }
        List<Integer> medals = new ArrayList<>();
        for (Item i : chr.getInventory(MapleInventoryType.EQUIP).newList()) {
            if (i.getItemId() >= 1142000 && i.getItemId() < 1152000) {
                medals.add(i.getItemId());
            }
        }
        mplew.writeInt(medals.size());
        for (int i : medals) {
            mplew.writeInt(i);
        }

        return mplew.getPacket();
    }

    public static byte[] getMonsterBookInfo(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BOOK_INFO.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(chr.getLevel());
        chr.getMonsterBook().writeCharInfoPacket(mplew);

        return mplew.getPacket();
    }

    public static byte[] spawnPortal(int townId, int targetId, int skillId, Point pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_PORTAL.getValue());
        mplew.writeInt(townId);
        mplew.writeInt(targetId);
        if ((townId != 999999999) && (targetId != 999999999)) {
            mplew.writeInt(skillId);
            mplew.writePos(pos);
        }

        return mplew.getPacket();
    }

    public static byte[] mechPortal(Point pos) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MECH_PORTAL.getValue());
        mplew.writePos(pos);

        return mplew.getPacket();
    }

    public static byte[] echoMegaphone(String name, String message) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ECHO_MESSAGE.getValue());
        mplew.write(0);
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.writeMapleAsciiString(name);
        mplew.writeMapleAsciiString(message);

        return mplew.getPacket();
    }

    public static byte[] showQuestMsg(String msg) {
        return broadcastMsg(5, msg);
    }

    public static byte[] Mulung_Pts(int recv, int total) {
        return showQuestMsg(new StringBuilder().append("You have received ").append(recv).append(" training points, for the accumulated total of ").append(total).append(" training points.").toString());
    }

    public static byte[] broadcastMsg(String message) {
        return broadcastMessage(4, 0, message, false);
    }

    public static byte[] broadcastMsg(int type, String message) {
        return broadcastMessage(type, 0, message, false);
    }

    public static byte[] broadcastMsg(int type, int channel, String message) {
        return broadcastMessage(type, channel, message, false);
    }

    public static byte[] broadcastMsg(int type, int channel, String message, boolean smegaEar) {
        return broadcastMessage(type, channel, message, smegaEar);
    }

    private static byte[] broadcastMessage(int type, int channel, String message, boolean megaEar) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(type);
        if (type == 4) {
            mplew.write(1);
        }
        if ((type != 23) && (type != 24)) {
            mplew.writeMapleAsciiString(message);
        }
        switch (type) {
            case 3:
            case 22:
            case 25:
            case 26:
                mplew.write(channel - 1);
                mplew.write(megaEar ? 1 : 0);
                break;
            case 9:
                mplew.write(channel - 1);
                break;
            case 12:
                mplew.writeInt(channel);
                break;
            case 6:
            case 11:
            case 20:
                mplew.writeInt((channel >= 1000000) && (channel < 6000000) ? channel : 0);
                break;
            case 24:
                mplew.writeShort(0);
            case 4:
            case 5:
            case 7:
            case 8:
            case 10:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 21:
            case 23:
        }
        return mplew.getPacket();
    }

    public static byte[] getGachaponMega(String name, String message, Item item, byte rareness, String gacha) {
        return getGachaponMega(name, message, item, rareness, false, gacha);
    }

    public static byte[] getGachaponMega(String name, String message, Item item, byte rareness, boolean dragon, String gacha) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(0x20);//173ok
        mplew.writeMapleAsciiString(new StringBuilder().append(name).append(message).toString());
        if (!dragon) {
            mplew.writeInt(0);
            mplew.writeInt(item.getItemId());
        }
        mplew.writeMapleAsciiString(gacha);
        PacketHelper.addItemInfo(mplew, item);

        return mplew.getPacket();
    }

    public static byte[] getEventEnvelope(int questID, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(0x17);
        mplew.writeShort(questID);
        mplew.writeInt(time);

        return mplew.getPacket();
    }

    public static byte[] tripleSmega(List<String> message, boolean ear, int channel) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(0xA);
        if (message.get(0) != null) {
            mplew.writeMapleAsciiString(message.get(0));
        }
        mplew.write(message.size());
        for (int i = 1; i < message.size(); i++) {
            if (message.get(i) != null) {
                mplew.writeMapleAsciiString(message.get(i));
            }
        }
        mplew.write(channel - 1);
        mplew.write(ear ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] cubeMega(String msg, Item item) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(0xB);
        mplew.writeMapleAsciiString(msg);
        mplew.writeInt(item == null ? 0 : item.getItemId());
        PacketHelper.addItemPosition(mplew, item, true, false);
        if (item != null) {
            PacketHelper.addItemInfo(mplew, item);
        }

        return mplew.getPacket();
    }

    public static byte[] itemMegaphone(String msg, boolean whisper, int channel, Item item) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SERVERMESSAGE.getValue());
        mplew.write(8);
        mplew.writeMapleAsciiString(msg);
        mplew.write(channel - 1);
        mplew.write(whisper ? 1 : 0);
        PacketHelper.addItemPosition(mplew, item, true, false);
        if (item != null) {
            PacketHelper.addItemInfo(mplew, item);
        }

        return mplew.getPacket();
    }

    public static byte[] getPeanutResult(int itemId, short quantity, int itemId2, short quantity2, int ourItem) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PIGMI_REWARD.getValue());
        mplew.writeInt(itemId);
        mplew.writeShort(quantity);
        mplew.writeInt(ourItem);
        mplew.writeInt(itemId2);
        mplew.writeInt(quantity2);
        mplew.write(0);
        mplew.write(0); // Boolean

        return mplew.getPacket();
    }

    public static byte[] getOwlOpen() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.OWL_OF_MINERVA.getValue());
        mplew.write(0xA);
        mplew.write(GameConstants.owlItems.length);
        for (int i : GameConstants.owlItems) {
            mplew.writeInt(i);
        }

        return mplew.getPacket();
    }

    public static byte[] getOwlSearched(int itemSearch, List<HiredMerchant> hms) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.OWL_OF_MINERVA.getValue());
        mplew.write(0x9);
        mplew.writeInt(0);
        mplew.writeInt(itemSearch);
        int size = 0;

        for (HiredMerchant hm : hms) {
            size += hm.searchItem(itemSearch).size();
        }

        mplew.writeInt(size);
        for (HiredMerchant hm : hms) {
            for (Iterator<HiredMerchant> i = hms.iterator(); i.hasNext();) {
                hm = i.next();
                final List<MaplePlayerShopItem> items = hm.searchItem(itemSearch);
                for (MaplePlayerShopItem item : items) {
                    mplew.writeMapleAsciiString(hm.getOwnerName());
                    mplew.writeInt(hm.getMap().getId());
                    mplew.writeMapleAsciiString(hm.getDescription());
                    mplew.writeInt(item.item.getQuantity());
                    mplew.writeInt(item.bundles);
                    mplew.writeInt(item.price);
                    switch (2) {
                        case 0:
                            mplew.writeInt(hm.getOwnerId());
                            break;
                        case 1:
                            mplew.writeInt(hm.getStoreId());
                            break;
                        default:
                            mplew.writeInt(hm.getObjectId());
                    }

                    mplew.write(hm.getFreeSlot() == -1 ? 1 : 0);
                    mplew.write(GameConstants.getInventoryType(itemSearch).getType());
                    if (GameConstants.getInventoryType(itemSearch) == MapleInventoryType.EQUIP) {
                        PacketHelper.addItemInfo(mplew, item.item);
                    }
                }
            }
        }
        return mplew.getPacket();
    }

    public static byte[] getOwlMessage(int msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);

        mplew.writeShort(SendPacketOpcode.OWL_RESULT.getValue());
        mplew.write(msg);

        return mplew.getPacket();
    }

    public static byte[] sendEngagementRequest(String name, int cid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ENGAGE_REQUEST.getValue());

        mplew.write(0);
        mplew.writeMapleAsciiString(name);
        mplew.writeInt(cid);

        return mplew.getPacket();
    }

    public static byte[] sendEngagement(byte msg, int item, MapleCharacter male, MapleCharacter female) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ENGAGE_RESULT.getValue());
        mplew.write(msg);
        if (msg == 9 || msg >= 11 && msg <= 14) {
            mplew.writeInt(0);
            mplew.writeInt(male.getId());
            mplew.writeInt(female.getId());
            mplew.writeShort(1);
            mplew.writeInt(item);
            mplew.writeInt(item);
            mplew.writeAsciiString(male.getName(), 13);
            mplew.writeAsciiString(female.getName(), 13);
        } else if (msg == 10 || msg >= 15 && msg <= 16) {
            mplew.writeAsciiString("Male", 13);
            mplew.writeAsciiString("Female", 13);
            mplew.writeShort(0);
        }

        return mplew.getPacket();
    }

    public static byte[] sendWeddingGive() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(9);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] sendWeddingReceive() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(10);
        mplew.writeLong(-1L);
        mplew.writeInt(0);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] giveWeddingItem() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(11);
        mplew.write(0);
        mplew.writeLong(0L);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] receiveWeddingItem() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WEDDING_GIFT.getValue());
        mplew.write(15);
        mplew.writeLong(0L);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] sendCashPetFood(boolean success, byte index) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3 + (success ? 1 : 0));

        mplew.writeShort(SendPacketOpcode.USE_CASH_PET_FOOD.getValue());
        mplew.write(success ? 0 : 1);
        if (success) {
            mplew.write(index);
        }

        return mplew.getPacket();
    }

    public static byte[] getFusionAnvil(boolean success, int index, int itemID) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3 + (success ? 1 : 0));

        mplew.writeShort(SendPacketOpcode.FUSION_ANVIL.getValue());
        mplew.writeBoolean(success);
        mplew.writeInt(index);
        mplew.writeInt(itemID);

        return mplew.getPacket();
    }

    public static byte[] yellowChat(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

//        mplew.writeShort(SendPacketOpcode.YELLOW_CHAT.getValue());
        mplew.writeShort(SendPacketOpcode.GAME_MESSAGE.getValue());
        mplew.writeShort(7);
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] shopDiscount(int percent) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SHOP_DISCOUNT.getValue());
        mplew.write(percent);

        return mplew.getPacket();
    }

    public static byte[] catchMob(int mobid, int itemid, byte success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CATCH_MOB.getValue());
        mplew.write(success);
        mplew.writeInt(itemid);
        mplew.writeInt(mobid);

        return mplew.getPacket();
    }

    public static byte[] spawnPlayerNPC(PlayerNPC npc) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PLAYER_NPC.getValue());
        mplew.write(1);
        mplew.writeInt(npc.getId());
        mplew.writeMapleAsciiString(npc.getName());
        PacketHelper.addCharLook(mplew, npc, true, false);

        return mplew.getPacket();
    }

    public static byte[] disabledNPC(List<Integer> ids) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3 + ids.size() * 4);

        mplew.writeShort(SendPacketOpcode.DISABLE_NPC.getValue());
        mplew.write(ids.size());
        for (Integer i : ids) {
            mplew.writeInt(i);
        }

        return mplew.getPacket();
    }

    public static byte[] getCard(int itemid, int level) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GET_CARD.getValue());
        mplew.write(itemid > 0 ? 1 : 0);
        if (itemid > 0) {
            mplew.writeInt(itemid);
            mplew.writeInt(level);
        }
        return mplew.getPacket();
    }

    public static byte[] changeCardSet(int set) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CARD_SET.getValue());
        mplew.writeInt(set);

        return mplew.getPacket();
    }

    public static byte[] upgradeBook(Item book, MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.BOOK_STATS.getValue());
        mplew.writeInt(book.getPosition());
        PacketHelper.addItemInfo(mplew, book, chr);

        return mplew.getPacket();
    }

    public static byte[] getCardDrops(int cardid, List<Integer> drops) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CARD_DROPS.getValue());
        mplew.writeInt(cardid);
        mplew.writeShort(drops == null ? 0 : drops.size());
        if (drops != null) {
            for (Integer de : drops) {
                mplew.writeInt(de);
            }
        }

        return mplew.getPacket();
    }

    public static byte[] getFamiliarInfo(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FAMILIAR_INFO.getValue());
        mplew.writeInt(chr.getFamiliars().size());
        for (MonsterFamiliar mf : chr.getFamiliars().values()) {
            mf.writeRegisterPacket(mplew, true);
        }
        List<Pair<Integer, Long>> size = new ArrayList<>();
        for (Item i : chr.getInventory(MapleInventoryType.USE).list()) {
            if (i.getItemId() / 10000 == 287) {
                StructFamiliar f = MapleItemInformationProvider.getInstance().getFamiliarByItem(i.getItemId());
                if (f != null) {
                    size.add(new Pair<>(f.familiar, i.getInventoryId()));
                }
            }
        }
        mplew.writeInt(size.size());
        for (Pair<?, ?> s : size) {
            mplew.writeInt(chr.getId());
            mplew.writeInt(((Integer) s.left));
            mplew.writeLong(((Long) s.right));
            mplew.write(0);
        }
        size.clear();

        return mplew.getPacket();
    }

    public static byte[] updateWebBoard(boolean result) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.WEB_BOARD_UPDATE.getValue());
        mplew.writeBoolean(result);

        return mplew.getPacket();
    }

    public static byte[] MulungEnergy(int energy) {
        return sendPyramidEnergy("energy", String.valueOf(energy));
    }

    public static byte[] sendPyramidEnergy(String type, String amount) {
        return sendString(1, type, amount);
    }

    public static byte[] sendGhostPoint(String type, String amount) {
        return sendString(2, type, amount);
    }

    public static byte[] sendGhostStatus(String type, String amount) {
        return sendString(3, type, amount);
    }

    public static byte[] sendString(int type, String object, String amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        switch (type) {
            case 1:
                mplew.writeShort(SendPacketOpcode.SESSION_VALUE.getValue());
                break;
            case 2:
                mplew.writeShort(SendPacketOpcode.PARTY_VALUE.getValue());
                break;
            case 3:
                mplew.writeShort(SendPacketOpcode.MAP_VALUE.getValue());
        }

        mplew.writeMapleAsciiString(object);
        mplew.writeMapleAsciiString(amount);

        return mplew.getPacket();
    }

    public static byte[] fairyPendantMessage(int termStart, int incExpR) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(14);

        mplew.writeShort(SendPacketOpcode.EXP_BONUS.getValue());
        mplew.writeInt(17);
        mplew.writeInt(0);

        mplew.writeInt(incExpR);

        return mplew.getPacket();
    }

    public static byte[] potionDiscountMessage(int type, int potionDiscR) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(10);

        mplew.writeShort(SendPacketOpcode.POTION_BONUS.getValue());
        mplew.writeInt(type);
        mplew.writeInt(potionDiscR);

        return mplew.getPacket();
    }

    public static byte[] sendLevelup(boolean family, int level, String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.LEVEL_UPDATE.getValue());
        mplew.write(family ? 1 : 2);
        mplew.writeInt(level);
        mplew.writeMapleAsciiString(name);

        return mplew.getPacket();
    }

    public static byte[] sendMarriage(boolean family, String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MARRIAGE_UPDATE.getValue());
        mplew.write(family ? 1 : 0);
        mplew.writeMapleAsciiString(name);

        return mplew.getPacket();
    }

    public static byte[] sendJobup(boolean family, int jobid, String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.JOB_UPDATE.getValue());
        mplew.write(family ? 1 : 0);
        mplew.writeInt(jobid);
        mplew.writeMapleAsciiString(new StringBuilder().append(!family ? "> " : "").append(name).toString());

        return mplew.getPacket();
    }

    public static byte[] getAvatarMega(MapleCharacter chr, int channel, int itemId, List<String> text, boolean ear) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.AVATAR_MEGA.getValue());
        mplew.writeInt(itemId);
        mplew.writeMapleAsciiString(chr.getName());
        for (String i : text) {
            mplew.writeMapleAsciiString(i);
        }
        mplew.writeInt(channel - 1);
        mplew.write(ear ? 1 : 0);
        PacketHelper.addCharLook(mplew, chr, true, false);

        return mplew.getPacket();
    }

    public static byte[] GMPoliceMessage(boolean dc) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(3);

        mplew.writeShort(SendPacketOpcode.GM_POLICE.getValue());
        mplew.write(dc ? 10 : 0);

        return mplew.getPacket();
    }

    public static byte[] GMPoliceMessage(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MAPLE_ADMIN_MSG.getValue());
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] pendantSlot(boolean p) { //slot -59
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SLOT_UPDATE.getValue());
        mplew.write(p ? 1 : 0);
        return mplew.getPacket();
    }

    public static byte[] followRequest(int chrid) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FOLLOW_REQUEST.getValue());
        mplew.writeInt(chrid);

        return mplew.getPacket();
    }

    public static byte[] getTopMsg(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TOP_MSG.getValue());
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] getTopMsg2(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.TOP_MSG2.getValue());
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] getMapleTipMsg(String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MAPLE_TIP_MSG.getValue());
        mplew.writeInt(3);
        mplew.writeInt(0x11);
        mplew.writeInt(0);
        mplew.writeInt(0);
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] getNewTopMsg() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.NEW_TOP_MSG.getValue());
        mplew.writeInt(0);
        mplew.writeAsciiString("Welcome to Maple World. We're one big family!");

        return mplew.getPacket();
    }

    public static byte[] showMidMsg(String s, int l) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MID_MSG.getValue());
        mplew.write(l);
        mplew.writeMapleAsciiString(s);
        mplew.write(s.length() > 0 ? 0 : 1);

        return mplew.getPacket();
    }

    public static byte[] getMidMsg(String msg, boolean keep, int index) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MID_MSG.getValue());
        mplew.write(index);
        mplew.writeMapleAsciiString(msg);
        mplew.write(keep ? 0 : 1);

        return mplew.getPacket();
    }

    public static byte[] clearMidMsg() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CLEAR_MID_MSG.getValue());

        return mplew.getPacket();
    }

    public static byte[] getSpecialMsg(String msg, int type, boolean show) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPECIAL_MSG.getValue());
        mplew.writeMapleAsciiString(msg);
        mplew.writeInt(type);
        mplew.writeInt(show ? 0 : 1);

        return mplew.getPacket();
    }

    public static byte[] CakePieMsg() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.CAKE_VS_PIE_MSG.getValue());

        return mplew.getPacket();
    }

    public static byte[] gmBoard(int increnement, String url) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GM_STORY_BOARD.getValue());
        mplew.writeInt(increnement); //Increnement number
        mplew.writeMapleAsciiString(url);

        return mplew.getPacket();
    }

    public static byte[] updateJaguar(MapleCharacter from) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_JAGUAR.getValue());
        PacketHelper.addJaguarInfo(mplew, from);

        return mplew.getPacket();
    }

    public static byte[] loadInformation(byte mode, int location, int birthday, int favoriteAction, int favoriteLocation, boolean success) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.YOUR_INFORMATION.getValue());
        mplew.write(mode);
        if (mode == 2) {
            mplew.writeInt(location);
            mplew.writeInt(birthday);
            mplew.writeInt(favoriteAction);
            mplew.writeInt(favoriteLocation);
        } else if (mode == 4) {
            mplew.write(success ? 1 : 0);
        }

        return mplew.getPacket();
    }

    public static byte[] saveInformation(boolean fail) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.YOUR_INFORMATION.getValue());
        mplew.write(4);
        mplew.write(fail ? 0 : 1);

        return mplew.getPacket();
    }

    public static byte[] myInfoResult() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FIND_FRIEND.getValue());
        mplew.write(6);
        mplew.writeInt(0);
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] findFriendResult(byte mode, List<MapleCharacter> friends, int error, MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.FIND_FRIEND.getValue());
        mplew.write(mode);
        switch (mode) {
            case 6:
                mplew.writeInt(0);
                mplew.writeInt(0);
                break;
            case 8:
                mplew.writeShort(friends.size());
                for (MapleCharacter mc : friends) {
                    mplew.writeInt(mc.getId());
                    mplew.writeMapleAsciiString(mc.getName());
                    //mplew.write(mc.getLevel());
                    //mplew.writeShort(mc.getJob());
                    //mplew.writeInt(0);
                    //mplew.writeInt(0);
                }
                break;
            case 9:
                mplew.write(error);
                break;
            case 11:
                mplew.writeInt(chr.getId());
                PacketHelper.addCharLook(mplew, chr, true, false);
                break;
        }

        return mplew.getPacket();
    }

    public static byte[] showBackgroundEffect(String eff, int value) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.VISITOR.getValue());
        mplew.writeMapleAsciiString(eff);
        mplew.write(value);

        return mplew.getPacket();
    }

    public static byte[] sendPinkBeanChoco() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PINKBEAN_CHOCO.getValue());
        mplew.writeInt(0);
        mplew.write(1);
        mplew.write(0);
        mplew.write(0);
        mplew.writeInt(0);

        return mplew.getPacket();
    }

    public static byte[] changeChannelMsg(int channel, String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(9 + msg.length());

        mplew.writeShort(SendPacketOpcode.AUTO_CC_MSG.getValue());
        mplew.writeInt(channel);
        mplew.writeMapleAsciiString(msg);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] pamSongUI() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        mplew.writeShort(SendPacketOpcode.PAM_SONG.getValue());
        return mplew.getPacket();
    }

    public static byte[] ultimateExplorer() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ULTIMATE_EXPLORER.getValue());

        return mplew.getPacket();
    }

    public static byte[] professionInfo(String skil, int level1, int level2, int chance) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPECIAL_STAT.getValue());
        mplew.writeMapleAsciiString(skil);
        mplew.writeInt(level1);
        mplew.writeInt(level2);
        mplew.write(1);
        mplew.writeInt((skil.startsWith("9200")) || (skil.startsWith("9201")) ? 100 : chance);

        return mplew.getPacket();
    }

    public static byte[] updateAzwanFame(int level, int fame, boolean levelup) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_HONOUR.getValue());
        //mplew.writeInt(level);
        mplew.writeInt(fame);
        //mplew.write(levelup ? 1 : 0);

        return mplew.getPacket();
    }

    public static byte[] showAzwanKilled() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.AZWAN_KILLED.getValue());

        return mplew.getPacket();
    }

    public static byte[] showSilentCrusadeMsg(byte type, short chapter) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        /* 類型(type)：
         * [0] 十字獵人介面
         * [2] 道具欄空間不足。
         * [3] 未知的原因失敗。
         */
        mplew.writeShort(SendPacketOpcode.SILENT_CRUSADE_MSG.getValue());
        mplew.write(type);
        mplew.writeShort(chapter - 1);

        return mplew.getPacket();
    }

    public static byte[] getSilentCrusadeMsg(byte type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        /* 類型(type)：
         * [0] 申請的道具購買已完全。
         * [1] 道具不足。
         * [2] 道具欄空間不足。
         * [3] 無法再擁有的道具。
         * [4] 現在無法購買道具。
         */
        mplew.writeShort(SendPacketOpcode.SILENT_CRUSADE_SHOP.getValue());
        mplew.write(type);

        return mplew.getPacket();
    }

    public static byte[] showSCShopMsg(byte type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SILENT_CRUSADE_SHOP.getValue());
        mplew.write(type);

        return mplew.getPacket();
    }

    public static byte[] updateImpTime() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_IMP_TIME.getValue());
        mplew.writeInt(0);
        mplew.writeLong(0L);

        return mplew.getPacket();
    }

    public static byte[] updateImp(MapleImp imp, int mask, int index, boolean login) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ITEM_POT.getValue());
        mplew.write(login ? 0 : 1);
        mplew.writeInt(index + 1);
        mplew.writeInt(mask);
        if ((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) {
            Pair<?, ?> i = MapleItemInformationProvider.getInstance().getPot(imp.getItemId());
            if (i == null) {
                return enableActions();
            }
            mplew.writeInt(((Integer) i.left));
            mplew.write(imp.getLevel());
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.STATE.getValue()) != 0)) {
            mplew.write(imp.getState());
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.FULLNESS.getValue()) != 0)) {
            mplew.writeInt(imp.getFullness());
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.CLOSENESS.getValue()) != 0)) {
            mplew.writeInt(imp.getCloseness());
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.CLOSENESS_LEFT.getValue()) != 0)) {
            mplew.writeInt(1);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MINUTES_LEFT.getValue()) != 0)) {
            mplew.writeInt(0);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.LEVEL.getValue()) != 0)) {
            mplew.write(1);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.FULLNESS_2.getValue()) != 0)) {
            mplew.writeInt(imp.getFullness());
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.UPDATE_TIME.getValue()) != 0)) {
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.CREATE_TIME.getValue()) != 0)) {
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.AWAKE_TIME.getValue()) != 0)) {
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.SLEEP_TIME.getValue()) != 0)) {
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_CLOSENESS.getValue()) != 0)) {
            mplew.writeInt(100);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_DELAY.getValue()) != 0)) {
            mplew.writeInt(1000);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_FULLNESS.getValue()) != 0)) {
            mplew.writeInt(1000);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_ALIVE.getValue()) != 0)) {
            mplew.writeInt(1);
        }
        if (((mask & MapleImp.ImpFlag.SUMMONED.getValue()) != 0) || ((mask & MapleImp.ImpFlag.MAX_MINUTES.getValue()) != 0)) {
            mplew.writeInt(10);
        }
        mplew.write(0);

        return mplew.getPacket();
    }

//    public static byte[] getMulungRanking(MapleClient c, List<DojoRankingInfo> all) {
//        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
//        mplew.writeShort(SendPacketOpcode.MULUNG_DOJO_RANKING.getValue());
//        MapleDojoRanking data = MapleDojoRanking.getInstance();
//        mplew.writeInt(all.size()); // size
//        for (DojoRankingInfo info : all) {
//            mplew.writeShort(info.getRank());
//            mplew.writeMapleAsciiString(info.getName());
//            mplew.writeLong(info.getTime());
//        }
//        return mplew.getPacket();
//    }
    public static byte[] getMulungRanking() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MULUNG_DOJO_RANKING.getValue());
        DojoRankingsData data = DojoRankingsData.loadLeaderboard();
        mplew.writeInt(data.totalCharacters); // size
        for (int i = 0; i < data.totalCharacters; i++) {
            mplew.writeShort(data.ranks[i]); // rank
            mplew.writeMapleAsciiString(data.names[i]); // Character name
            mplew.writeLong(data.times[i]); // time in seconds
        }
        return mplew.getPacket();
    }

    public static byte[] getMulungMessage(boolean dc, String msg) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MULUNG_MESSAGE.getValue());
        mplew.write(dc ? 1 : 0);
        mplew.writeMapleAsciiString(msg);

        return mplew.getPacket();
    }

    public static byte[] getCandyRanking(MapleClient c, List<CandyRankingInfo> all) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(10);

        mplew.writeShort(SendPacketOpcode.CANDY_RANKING.getValue());
        mplew.writeInt(all.size());
        for (CandyRankingInfo info : all) {
            mplew.writeShort(info.getRank());
            mplew.writeMapleAsciiString(info.getName());
        }
        return mplew.getPacket();
    }

    public static byte[] showChronosphere(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.DAY_OF_CHRONOSPHERE.getValue());

        mplew.writeInt(chr.getFreeChronosphere());
        mplew.writeInt(chr.getChronosphere());

        return mplew.getPacket();
    }

    public static byte[] errorChronosphere() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.ERROR_CHRONOSPHERE.getValue());
        mplew.write(0);

        return mplew.getPacket();
    }

    public static byte[] SystemProcess() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SYSTEM_PROCESS_LIST.getValue());
        mplew.write(1);

        return mplew.getPacket();
    }

    public static class AlliancePacket {

        public static byte[] getAllianceInfo(MapleGuildAlliance alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(12);
            mplew.write(alliance == null ? 0 : 1);
            if (alliance != null) {
                addAllianceInfo(mplew, alliance);
            }

            return mplew.getPacket();
        }

        private static void addAllianceInfo(MaplePacketLittleEndianWriter mplew, MapleGuildAlliance alliance) {
            mplew.writeInt(alliance.getId());
            mplew.writeMapleAsciiString(alliance.getName());
            for (int i = 1; i <= 5; i++) {
                mplew.writeMapleAsciiString(alliance.getRank(i));
            }
            mplew.write(alliance.getNoGuilds());
            for (int i = 0; i < alliance.getNoGuilds(); i++) {
                mplew.writeInt(alliance.getGuildId(i));
            }
            mplew.writeInt(alliance.getCapacity());
            mplew.writeMapleAsciiString(alliance.getNotice());
        }

        public static byte[] getGuildAlliance(MapleGuildAlliance alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(13);
            if (alliance == null) {
                mplew.writeInt(0);
                return mplew.getPacket();
            }
            int noGuilds = alliance.getNoGuilds();
            MapleGuild[] g = new MapleGuild[noGuilds];
            for (int i = 0; i < alliance.getNoGuilds(); i++) {
                g[i] = World.Guild.getGuild(alliance.getGuildId(i));
                if (g[i] == null) {
                    return CWvsContext.enableActions();
                }
            }
            mplew.writeInt(noGuilds);
            for (MapleGuild gg : g) {
                CWvsContext.GuildPacket.getGuildInfo(mplew, gg);
            }
            return mplew.getPacket();
        }

        public static byte[] allianceMemberOnline(int alliance, int gid, int id, boolean online) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(14);
            mplew.writeInt(alliance);
            mplew.writeInt(gid);
            mplew.writeInt(id);
            mplew.write(online ? 1 : 0);

            return mplew.getPacket();
        }

        public static byte[] removeGuildFromAlliance(MapleGuildAlliance alliance, MapleGuild expelledGuild, boolean expelled) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(16);
            addAllianceInfo(mplew, alliance);
            CWvsContext.GuildPacket.getGuildInfo(mplew, expelledGuild);
            mplew.write(expelled ? 1 : 0);

            return mplew.getPacket();
        }

        public static byte[] addGuildToAlliance(MapleGuildAlliance alliance, MapleGuild newGuild) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(18);
            addAllianceInfo(mplew, alliance);
            mplew.writeInt(newGuild.getId());
            CWvsContext.GuildPacket.getGuildInfo(mplew, newGuild);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] sendAllianceInvite(String allianceName, MapleCharacter inviter) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(3);
            mplew.writeInt(inviter.getGuildId());
            mplew.writeMapleAsciiString(inviter.getName());
            mplew.writeMapleAsciiString(allianceName);

            return mplew.getPacket();
        }

        public static byte[] getAllianceUpdate(MapleGuildAlliance alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(23);
            addAllianceInfo(mplew, alliance);

            return mplew.getPacket();
        }

        public static byte[] createGuildAlliance(MapleGuildAlliance alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(15);
            addAllianceInfo(mplew, alliance);
            int noGuilds = alliance.getNoGuilds();
            MapleGuild[] g = new MapleGuild[noGuilds];
            for (int i = 0; i < alliance.getNoGuilds(); i++) {
                g[i] = World.Guild.getGuild(alliance.getGuildId(i));
                if (g[i] == null) {
                    return CWvsContext.enableActions();
                }
            }
            for (MapleGuild gg : g) {
                CWvsContext.GuildPacket.getGuildInfo(mplew, gg);
            }
            return mplew.getPacket();
        }

        public static byte[] updateAlliance(MapleGuildCharacter mgc, int allianceid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(24);
            mplew.writeInt(allianceid);
            mplew.writeInt(mgc.getGuildId());
            mplew.writeInt(mgc.getId());
            mplew.writeInt(mgc.getLevel());
            mplew.writeInt(mgc.getJobId());

            return mplew.getPacket();
        }

        public static byte[] updateAllianceLeader(int allianceid, int newLeader, int oldLeader) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(25);
            mplew.writeInt(allianceid);
            mplew.writeInt(oldLeader);
            mplew.writeInt(newLeader);

            return mplew.getPacket();
        }

        public static byte[] allianceRankChange(int aid, String[] ranks) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x1a);
            mplew.writeInt(aid);
            for (String r : ranks) {
                mplew.writeMapleAsciiString(r);
            }

            return mplew.getPacket();
        }

        public static byte[] updateAllianceRank(MapleGuildCharacter mgc) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(27);
            mplew.writeInt(mgc.getId());
            mplew.write(mgc.getAllianceRank());

            return mplew.getPacket();
        }

        public static byte[] changeAllianceNotice(int allianceid, String notice) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(28);
            mplew.writeInt(allianceid);
            mplew.writeMapleAsciiString(notice);

            return mplew.getPacket();
        }

        public static byte[] disbandAlliance(int alliance) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(29);
            mplew.writeInt(alliance);

            return mplew.getPacket();
        }

        public static byte[] changeAlliance(MapleGuildAlliance alliance, boolean in) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(1);
            mplew.write(in ? 1 : 0);
            mplew.writeInt(in ? alliance.getId() : 0);
            int noGuilds = alliance.getNoGuilds();
            MapleGuild[] g = new MapleGuild[noGuilds];
            for (int i = 0; i < noGuilds; i++) {
                g[i] = World.Guild.getGuild(alliance.getGuildId(i));
                if (g[i] == null) {
                    return CWvsContext.enableActions();
                }
            }
            mplew.write(noGuilds);
            for (int i = 0; i < noGuilds; i++) {
                mplew.writeInt(g[i].getId());

                Collection<MapleGuildCharacter> members = g[i].getMembers();
                mplew.writeInt(members.size());
                for (MapleGuildCharacter mgc : members) {
                    mplew.writeInt(mgc.getId());
                    mplew.write(in ? mgc.getAllianceRank() : 0);
                }
            }

            return mplew.getPacket();
        }

        public static byte[] changeAllianceLeader(int allianceid, int newLeader, int oldLeader) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(2);
            mplew.writeInt(allianceid);
            mplew.writeInt(oldLeader);
            mplew.writeInt(newLeader);

            return mplew.getPacket();
        }

        public static byte[] changeGuildInAlliance(MapleGuildAlliance alliance, MapleGuild guild, boolean add) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(4);
            mplew.writeInt(add ? alliance.getId() : 0);
            mplew.writeInt(guild.getId());
            Collection<MapleGuildCharacter> members = guild.getMembers();
            mplew.writeInt(members.size());
            for (MapleGuildCharacter mgc : members) {
                mplew.writeInt(mgc.getId());
                mplew.write(add ? mgc.getAllianceRank() : 0);
            }

            return mplew.getPacket();
        }

        public static byte[] changeAllianceRank(int allianceid, MapleGuildCharacter player) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.ALLIANCE_OPERATION.getValue());
            mplew.write(5);
            mplew.writeInt(allianceid);
            mplew.writeInt(player.getId());
            mplew.writeInt(player.getAllianceRank());

            return mplew.getPacket();
        }
    }

    public static class FamilyPacket {

        public static byte[] getFamilyData() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.FAMILY.getValue());
            MapleFamilyBuff[] entries = MapleFamilyBuff.values();
            mplew.writeInt(entries.length);

            for (MapleFamilyBuff entry : entries) {
                mplew.write(entry.type);
                mplew.writeInt(entry.rep);
                mplew.writeInt(1);
                mplew.writeMapleAsciiString(entry.name);
                mplew.writeMapleAsciiString(entry.desc);
            }
            return mplew.getPacket();
        }

        public static byte[] getFamilyInfo(MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.OPEN_FAMILY.getValue());
            mplew.writeInt(chr.getCurrentRep());
            mplew.writeInt(chr.getTotalRep());
            mplew.writeInt(chr.getTotalRep());
            mplew.writeShort(chr.getNoJuniors());
            mplew.writeShort(2);
            mplew.writeShort(chr.getNoJuniors());
            MapleFamily family = World.Family.getFamily(chr.getFamilyId());
            if (family != null) {
                mplew.writeInt(family.getLeaderId());
                mplew.writeMapleAsciiString(family.getLeaderName());
                mplew.writeMapleAsciiString(family.getNotice());
            } else {
                mplew.writeLong(0L);
            }
            List<?> b = chr.usedBuffs();
            mplew.writeInt(b.size());
            for (Iterator<?> i$ = b.iterator(); i$.hasNext();) {
                int ii = ((Integer) i$.next());
                mplew.writeInt(ii);
                mplew.writeInt(1);
            }

            return mplew.getPacket();
        }

        public static void addFamilyCharInfo(MapleFamilyCharacter ldr, MaplePacketLittleEndianWriter mplew) {
            mplew.writeInt(ldr.getId());
            mplew.writeInt(ldr.getSeniorId());
            mplew.writeShort(ldr.getJobId());
            mplew.writeShort(0);
            mplew.write(ldr.getLevel());
            mplew.write(ldr.isOnline() ? 1 : 0);
            mplew.writeInt(ldr.getCurrentRep());
            mplew.writeInt(ldr.getTotalRep());
            mplew.writeInt(ldr.getTotalRep());
            mplew.writeInt(ldr.getTotalRep());
            mplew.writeInt(Math.max(ldr.getChannel(), 0));
            mplew.writeInt(0);
            mplew.writeMapleAsciiString(ldr.getName());
        }

        public static byte[] getFamilyPedigree(MapleCharacter chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SEND_PEDIGREE.getValue());
            mplew.writeInt(chr.getId());
            MapleFamily family = World.Family.getFamily(chr.getFamilyId());

            int descendants = 2;
            int gens = 0;
            int generations = 0;
            if (family == null) {
                mplew.writeInt(2);
                addFamilyCharInfo(new MapleFamilyCharacter(chr, 0, 0, 0, 0), mplew);
            } else {
                mplew.writeInt(family.getMFC(chr.getId()).getPedigree().size() + 1);
                addFamilyCharInfo(family.getMFC(family.getLeaderId()), mplew);

                if (chr.getSeniorId() > 0) {
                    MapleFamilyCharacter senior = family.getMFC(chr.getSeniorId());
                    if (senior != null) {
                        if (senior.getSeniorId() > 0) {
                            addFamilyCharInfo(family.getMFC(senior.getSeniorId()), mplew);
                        }
                        addFamilyCharInfo(senior, mplew);
                    }
                }
            }
            addFamilyCharInfo(chr.getMFC() == null ? new MapleFamilyCharacter(chr, 0, 0, 0, 0) : chr.getMFC(), mplew);
            if (family != null) {
                if (chr.getSeniorId() > 0) {
                    MapleFamilyCharacter senior = family.getMFC(chr.getSeniorId());
                    if (senior != null) {
                        if ((senior.getJunior1() > 0) && (senior.getJunior1() != chr.getId())) {
                            addFamilyCharInfo(family.getMFC(senior.getJunior1()), mplew);
                        } else if ((senior.getJunior2() > 0) && (senior.getJunior2() != chr.getId())) {
                            addFamilyCharInfo(family.getMFC(senior.getJunior2()), mplew);
                        }

                    }

                }

                if (chr.getJunior1() > 0) {
                    MapleFamilyCharacter junior = family.getMFC(chr.getJunior1());
                    if (junior != null) {
                        addFamilyCharInfo(junior, mplew);
                    }
                }
                if (chr.getJunior2() > 0) {
                    MapleFamilyCharacter junior = family.getMFC(chr.getJunior2());
                    if (junior != null) {
                        addFamilyCharInfo(junior, mplew);
                    }
                }
                if (chr.getJunior1() > 0) {
                    MapleFamilyCharacter junior = family.getMFC(chr.getJunior1());
                    if (junior != null) {
                        if ((junior.getJunior1() > 0) && (family.getMFC(junior.getJunior1()) != null)) {
                            gens++;
                            addFamilyCharInfo(family.getMFC(junior.getJunior1()), mplew);
                        }
                        if ((junior.getJunior2() > 0) && (family.getMFC(junior.getJunior2()) != null)) {
                            gens++;
                            addFamilyCharInfo(family.getMFC(junior.getJunior2()), mplew);
                        }
                    }
                }
                if (chr.getJunior2() > 0) {
                    MapleFamilyCharacter junior = family.getMFC(chr.getJunior2());
                    if (junior != null) {
                        if ((junior.getJunior1() > 0) && (family.getMFC(junior.getJunior1()) != null)) {
                            gens++;
                            addFamilyCharInfo(family.getMFC(junior.getJunior1()), mplew);
                        }
                        if ((junior.getJunior2() > 0) && (family.getMFC(junior.getJunior2()) != null)) {
                            gens++;
                            addFamilyCharInfo(family.getMFC(junior.getJunior2()), mplew);
                        }
                    }
                }
                generations = family.getMemberSize();
            }
            mplew.writeLong(gens);
            mplew.writeInt(0);
            mplew.writeInt(-1);
            mplew.writeInt(generations);

            if (family != null) {
                if (chr.getJunior1() > 0) {
                    MapleFamilyCharacter junior = family.getMFC(chr.getJunior1());
                    if (junior != null) {
                        if ((junior.getJunior1() > 0) && (family.getMFC(junior.getJunior1()) != null)) {
                            mplew.writeInt(junior.getJunior1());
                            mplew.writeInt(family.getMFC(junior.getJunior1()).getDescendants());
                        } else {
                            mplew.writeInt(0);
                        }
                        if ((junior.getJunior2() > 0) && (family.getMFC(junior.getJunior2()) != null)) {
                            mplew.writeInt(junior.getJunior2());
                            mplew.writeInt(family.getMFC(junior.getJunior2()).getDescendants());
                        } else {
                            mplew.writeInt(0);
                        }
                    }
                }
                if (chr.getJunior2() > 0) {
                    MapleFamilyCharacter junior = family.getMFC(chr.getJunior2());
                    if (junior != null) {
                        if ((junior.getJunior1() > 0) && (family.getMFC(junior.getJunior1()) != null)) {
                            mplew.writeInt(junior.getJunior1());
                            mplew.writeInt(family.getMFC(junior.getJunior1()).getDescendants());
                        } else {
                            mplew.writeInt(0);
                        }
                        if ((junior.getJunior2() > 0) && (family.getMFC(junior.getJunior2()) != null)) {
                            mplew.writeInt(junior.getJunior2());
                            mplew.writeInt(family.getMFC(junior.getJunior2()).getDescendants());
                        } else {
                            mplew.writeInt(0);
                        }
                    }
                }
            }

            List<?> b = chr.usedBuffs();
            mplew.writeInt(b.size());
            for (Iterator<?> i$ = b.iterator(); i$.hasNext();) {
                int ii = ((Integer) i$.next());
                mplew.writeInt(ii);
                mplew.writeInt(1);
            }
            mplew.writeShort(2);

            return mplew.getPacket();
        }

        public static byte[] getFamilyMsg(byte type, int meso) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.FAMILY_MESSAGE.getValue());
            mplew.writeInt(type);
            mplew.writeInt(meso);

            return mplew.getPacket();
        }

        public static byte[] sendFamilyInvite(int cid, int otherLevel, int otherJob, String inviter) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.FAMILY_INVITE.getValue());
            mplew.writeInt(cid);
            mplew.writeInt(otherLevel);
            mplew.writeInt(otherJob);
            mplew.writeInt(0);
            mplew.writeMapleAsciiString(inviter);
            return mplew.getPacket();
        }

        public static byte[] sendFamilyJoinResponse(boolean accepted, String added) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.FAMILY_JUNIOR.getValue());
            mplew.write(accepted ? 1 : 0);
            mplew.writeMapleAsciiString(added);

            return mplew.getPacket();
        }

        public static byte[] getSeniorMessage(String name) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SENIOR_MESSAGE.getValue());
            mplew.writeMapleAsciiString(name);

            return mplew.getPacket();
        }

        public static byte[] changeRep(int r, String name) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.REP_INCREASE.getValue());
            mplew.writeInt(r);
            mplew.writeMapleAsciiString(name);

            return mplew.getPacket();
        }

        public static byte[] familyLoggedIn(boolean online, String name) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.FAMILY_LOGGEDIN.getValue());
            mplew.write(online ? 1 : 0);
            mplew.writeMapleAsciiString(name);

            return mplew.getPacket();
        }

        public static byte[] familyBuff(int type, int buffnr, int amount, int time) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.FAMILY_BUFF.getValue());
            mplew.write(type);
            if ((type >= 2) && (type <= 4)) {
                mplew.writeInt(buffnr);

                mplew.writeInt(type == 3 ? 0 : amount);
                mplew.writeInt(type == 2 ? 0 : amount);
                mplew.write(0);
                mplew.writeInt(time);
            }
            return mplew.getPacket();
        }

        public static byte[] cancelFamilyBuff() {
            return familyBuff(0, 0, 0, 0);
        }

        public static byte[] familySummonRequest(String name, String mapname) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.FAMILY_USE_REQUEST.getValue());
            mplew.writeMapleAsciiString(name);
            mplew.writeMapleAsciiString(mapname);

            return mplew.getPacket();
        }
    }

    public static class BuddylistPacket {

        public static byte[] updateBuddylist(Collection<BuddylistEntry> buddylist) {
            return updateBuddylist(buddylist, 7);
        }

        public static byte[] updateBuddylist(Collection<BuddylistEntry> buddylist, int deleted) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            mplew.write(deleted);
            mplew.writeInt(buddylist.size());
            for (BuddylistEntry buddy : buddylist) {
                mplew.writeInt(buddy.getCharacterId());
                mplew.writeAsciiString(buddy.getName(), 15);
                mplew.write(buddy.isVisible() ? 0 : 1);//if adding = 2
                mplew.writeInt(buddy.getChannel() == -1 ? -1 : buddy.getChannel());
                mplew.writeAsciiString(buddy.getGroup(), 17);
            }
            for (int x = 0; x < buddylist.size(); x++) {
                mplew.writeInt(0);
            }

            return mplew.getPacket();
        }

        public static byte[] updateBuddyChannel(int characterid, int channel) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            mplew.write(20);
            mplew.writeInt(characterid);
            mplew.write(0);
            mplew.writeInt(channel);

            return mplew.getPacket();
        }

        public static byte[] requestBuddylistAdd(int cidFrom, String nameFrom, int levelFrom, int jobFrom) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            mplew.write(9);
            mplew.writeInt(cidFrom);
            mplew.writeMapleAsciiString(nameFrom);
            mplew.writeInt(levelFrom);
            mplew.writeInt(jobFrom);
            mplew.writeInt(0);//v115
            mplew.writeInt(cidFrom);
            mplew.writeAsciiString(nameFrom, 15);
            mplew.write(1);
            mplew.writeInt(0);
            mplew.writeAsciiString("群組未指定", 16);
            mplew.writeShort(0);//was1

            return mplew.getPacket();
        }

        public static byte[] updateBuddyCapacity(int capacity) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            mplew.write(21);
            mplew.write(capacity);

            return mplew.getPacket();
        }

        public static byte[] buddylistMessage(byte message) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BUDDYLIST.getValue());
            mplew.write(message);

            return mplew.getPacket();
        }
    }

    public static byte[] giveKilling(int x) {
        if (ServerConfig.LOG_PACKETS) {
            System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
        PacketHelper.writeSingleMask(mplew, MapleBuffStat.USING_SKILL_MOVE);
//        mplew.writeInt(0);
//        mplew.write(0);
//        mplew.writeInt(x);
//        mplew.writeZeroBytes(6);
        mplew.writeShort(0);
        mplew.write(0);
        mplew.writeInt(x);
        return mplew.getPacket();
    }

    public static class ExpeditionPacket {

        public static byte[] expeditionStatus(MapleExpedition me, boolean created, boolean silent) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(created ? 86 : silent ? 72 : 76);//74
            mplew.writeInt(me.getType().exped);
            mplew.writeInt(0);
            for (int i = 0; i < 6; i++) {
                if (i < me.getParties().size()) {
                    MapleParty party = World.Party.getParty((me.getParties().get(i)).intValue());

                    CWvsContext.PartyPacket.addPartyStatus(-1, party, mplew, false, true);
                } else {
                    CWvsContext.PartyPacket.addPartyStatus(-1, null, mplew, false, true);
                }

            }

            return mplew.getPacket();
        }

        public static byte[] expeditionError(int errcode, String name) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(100);//88
            mplew.writeInt(errcode);
            mplew.writeMapleAsciiString(name);

            return mplew.getPacket();
        }

        public static byte[] expeditionMessage(int code) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(code);

            return mplew.getPacket();
        }

        public static byte[] expeditionJoined(String name) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(87);//75
            mplew.writeMapleAsciiString(name);

            return mplew.getPacket();
        }

        public static byte[] expeditionLeft(String name) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(91);//79
            mplew.writeMapleAsciiString(name);

            return mplew.getPacket();
        }

        public static byte[] expeditionLeaderChanged(int newLeader) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(96);//84
            mplew.writeInt(newLeader);

            return mplew.getPacket();
        }

        public static byte[] expeditionUpdate(int partyIndex, MapleParty party) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(97);//85
            mplew.writeInt(0);
            mplew.writeInt(partyIndex);

            CWvsContext.PartyPacket.addPartyStatus(-1, party, mplew, false, true);

            return mplew.getPacket();
        }

        public static byte[] expeditionInvite(MapleCharacter from, int exped) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EXPEDITION_OPERATION.getValue());
            mplew.write(99);//87
            mplew.writeInt(from.getLevel());
            mplew.writeInt(from.getJob());
            mplew.writeInt(0);
            mplew.writeMapleAsciiString(from.getName());
            mplew.writeInt(exped);

            return mplew.getPacket();
        }
    }

    public static class PartyPacket {

        public static byte[] partyCreated(MapleParty party) { // 建立隊伍
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(0x10); // Version.176 [完成]
            mplew.writeInt(party.getId());
            mplew.writeInt(999999999);
            mplew.writeInt(999999999);
            mplew.writeInt(0);
            mplew.writeShort(0);
            mplew.writeShort(0);
            mplew.write(party.isPrivate() ? 0 : 1);
            mplew.writeMapleAsciiString(party.getName());

            return mplew.getPacket();
        }

        public static byte[] partyInvite(MapleCharacter from) { // 隊伍邀請
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(0x04); // Version.176 [完成]
            mplew.writeInt(from.getParty() == null ? 0 : from.getParty().getId());
            mplew.writeMapleAsciiString(from.getName());
            mplew.writeInt(from.getLevel());
            mplew.writeInt(from.getJob());
            mplew.write(0);
            mplew.writeInt(0);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] partyRequestInvite(MapleCharacter from) { // 隊伍邀請回覆
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(0x08); // Version.176 [完成]
            mplew.writeInt(from.getId());
            mplew.writeMapleAsciiString(from.getName());
            mplew.writeInt(from.getLevel());
            mplew.writeInt(from.getJob());
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] partyStatusMessage(int message) {
            return partyStatusMessage(message, null);
        }

        public static byte[] partyStatusMessage(int message, String charname) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(message);
            if ((message == 0x21) || (message == 0x43)) { // Version.176 [完成]
                mplew.writeMapleAsciiString(charname);
            } else if (message == 0x30) { // Version.176 [推測]
                mplew.write(0);
            }

            return mplew.getPacket();
        }

        public static void addPartyStatus(int forchannel, MapleParty party, MaplePacketLittleEndianWriter lew, boolean leaving) {
            addPartyStatus(forchannel, party, lew, leaving, false);
        }

        public static void addPartyStatus(int forchannel, MapleParty party, MaplePacketLittleEndianWriter lew, boolean leaving, boolean exped) {
            List<MaplePartyCharacter> partymembers;
            if (party == null) {
                partymembers = new ArrayList();
            } else {
                partymembers = new ArrayList(party.getMembers());
            }
            while (partymembers.size() < 6) {
                partymembers.add(new MaplePartyCharacter());
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.getId());
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeAsciiString(partychar.getName(), 15);
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.getJobId());
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(0);
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.getLevel());
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.isOnline() ? partychar.getChannel() - 1 : -2);
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(0);
            }
            lew.writeInt(party == null ? 0 : party.getLeader().getId());
            if (exped) {
                return;
            }
            for (MaplePartyCharacter partychar : partymembers) {
                lew.writeInt(partychar.getChannel() == forchannel ? partychar.getMapid() : 0);
            }
            for (MaplePartyCharacter partychar : partymembers) {
                if ((partychar.getChannel() == forchannel) && (!leaving)) {
                    lew.writeInt(partychar.getDoorTown());
                    lew.writeInt(partychar.getDoorTarget());
                    lew.writeInt(partychar.getDoorSkill());
                    lew.writeInt(partychar.getDoorPosition().x);
                    lew.writeInt(partychar.getDoorPosition().y);
                } else {
                    lew.writeInt(leaving ? 999999999 : 0);
                    lew.writeInt(leaving ? 999999999 : 0);
                    lew.writeInt(0);
                    lew.writeInt(leaving ? -1 : 0);
                    lew.writeInt(leaving ? -1 : 0);
                }
            }
            lew.write(party == null || party.isPrivate() ? 0 : 1);
            lew.writeMapleAsciiString(party.getName());
        }

        public static byte[] updateParty(int forChannel, MapleParty party, PartyOperation op, MaplePartyCharacter target) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            switch (op) {
                case LEAVE: // 離開隊伍
                case EXPEL: // 驅逐隊伍
                case DISBAND: // 解散隊伍
                    mplew.write(0x15); // Version.176 [完成]
                    mplew.writeInt(party.getId());
                    mplew.writeInt(target.getId());
                    mplew.write(op == PartyOperation.DISBAND ? 0 : 1);
                    if (op == PartyOperation.DISBAND) {
                        mplew.writeInt(target.getId());
                    } else {
                        mplew.write(op == PartyOperation.EXPEL ? 1 : 0);
                        mplew.writeMapleAsciiString(target.getName());
                        addPartyStatus(forChannel, party, mplew, op == PartyOperation.LEAVE);
                    }
                    break;
                case JOIN: // 加入隊伍
                    mplew.write(0x18); // Version.176 [完成]
                    mplew.writeInt(party.getId());
                    mplew.writeMapleAsciiString(target.getName());
                    addPartyStatus(forChannel, party, mplew, false);
                    break;
                case SILENT_UPDATE: // 更新隊伍
                case LOG_ONOFF: // 更新隊員線上狀態
                    mplew.write(0x0F); // Version.176 [完成]
                    mplew.writeInt(party.getId());
                    addPartyStatus(forChannel, party, mplew, op == PartyOperation.LOG_ONOFF);
                    break;
                case INFO_UPDATE: // 變更隊伍設置
                    mplew.write(0x4D); // Version.176 [完成]
                    mplew.write(party.isPrivate() ? 0 : 1);
                    mplew.writeMapleAsciiString(party.getName());
                    break;
                case CHANGE_LEADER: // 委任隊長
                case CHANGE_LEADER_DC: // 自動委任隊長
                    mplew.write(0x30); // Version.176 [完成]
                    mplew.writeInt(target.getId());
                    mplew.write(op == PartyOperation.CHANGE_LEADER_DC ? 1 : 0);
            }
            return mplew.getPacket();
        }

        public static byte[] partyPortal(int townId, int targetId, int skillId, Point position, boolean animation) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(0x50); // Version.176 [完成]
            mplew.write(animation ? 0 : 1);
            mplew.writeInt(townId);
            mplew.writeInt(targetId);
            mplew.writeInt(skillId);
            mplew.writePos(position);

            return mplew.getPacket();
        }

        public static byte[] getPartyListing(PartySearchType pst) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(0x60); // Version.176 [推測]
            mplew.writeInt(pst.id);
            final List<PartySearch> parties = World.Party.searchParty(pst);
            mplew.writeInt(parties.size());
            for (PartySearch party : parties) {
                mplew.writeInt(0);
                mplew.writeInt(2);
                if (pst.exped) {
                    MapleExpedition me = World.Party.getExped(party.getId());
                    mplew.writeInt(me.getType().maxMembers);
                    mplew.writeInt(party.getId());
                    mplew.writeAsciiString(party.getName(), 48);
                    for (int i = 0; i < 5; i++) {
                        if (i < me.getParties().size()) {
                            MapleParty part = World.Party.getParty((me.getParties().get(i)).intValue());
                            if (part != null) {
                                addPartyStatus(-1, part, mplew, false, true);
                            } else {
                                mplew.writeZeroBytes(202);
                            }
                        } else {
                            mplew.writeZeroBytes(202);
                        }
                    }
                } else {
                    mplew.writeInt(0);
                    mplew.writeInt(party.getId());
                    mplew.writeAsciiString(party.getName(), 48);
                    addPartyStatus(-1, World.Party.getParty(party.getId()), mplew, false, true);
                }

                mplew.writeShort(0);
            }

            return mplew.getPacket();
        }

        public static byte[] partyListingAdded(PartySearch ps) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PARTY_OPERATION.getValue());
            mplew.write(0x70); // Version.176 [推測]
            mplew.writeInt(ps.getType().id);
            mplew.writeInt(0);
            mplew.writeInt(1);
            if (ps.getType().exped) {
                MapleExpedition me = World.Party.getExped(ps.getId());
                mplew.writeInt(me.getType().maxMembers);
                mplew.writeInt(ps.getId());
                mplew.writeAsciiString(ps.getName(), 48);
                for (int i = 0; i < 5; i++) {
                    if (i < me.getParties().size()) {
                        MapleParty party = World.Party.getParty((me.getParties().get(i)).intValue());
                        if (party != null) {
                            addPartyStatus(-1, party, mplew, false, true);
                        } else {
                            mplew.writeZeroBytes(202);
                        }
                    } else {
                        mplew.writeZeroBytes(202);
                    }
                }
            } else {
                mplew.writeInt(0);
                mplew.writeInt(ps.getId());
                mplew.writeAsciiString(ps.getName(), 48);
                addPartyStatus(-1, World.Party.getParty(ps.getId()), mplew, false, true);
            }
            mplew.writeShort(0);

            return mplew.getPacket();
        }

        public static byte[] showMemberSearch(List<MapleCharacter> chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.MEMBER_SEARCH.getValue());
            mplew.write(chr.size());
            for (MapleCharacter c : chr) {
                mplew.writeInt(c.getId());
                mplew.writeMapleAsciiString(c.getName());
                mplew.writeShort(c.getJob());
                mplew.write(c.getLevel());
            }
            return mplew.getPacket();
        }

        public static byte[] showPartySearch(List<MapleParty> chr) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.PARTY_SEARCH.getValue());
            mplew.write(chr.size());
            for (MapleParty c : chr) {
                mplew.writeInt(c.getId());
                mplew.writeMapleAsciiString(c.getLeader().getName());
                mplew.write(c.getLeader().getLevel());
                mplew.write(c.getLeader().isOnline() ? 1 : 0);
                mplew.write(c.getMembers().size());
                for (MaplePartyCharacter ch : c.getMembers()) {
                    mplew.writeInt(ch.getId());
                    mplew.writeMapleAsciiString(ch.getName());
                    mplew.writeShort(ch.getJobId());
                    mplew.write(ch.getLevel());
                    mplew.write(ch.isOnline() ? 1 : 0);
                }
            }
            return mplew.getPacket();
        }
    }

    public static class GuildPacket {

        public static byte[] genericGuildMessage(byte code) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(code);//30 = cant find in ch
            if (code == 0x70) {//0x57+19
                mplew.writeInt(0);
            }
            switch (code) {
                case 0x05://0x03+2
                case 0x47://0x30+17
                case 0x53://0x3B+18
                case 0x54://0x3C+18
                case 0x55://0x3D+18
                case 0x6D://0x54+19
                case 0x70://0x57+19
                    mplew.writeMapleAsciiString("");
            }

            return mplew.getPacket();
        }

        public static byte[] createGuildNotice(String Name) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x05); // Version.176 [完成]
            mplew.writeMapleAsciiString(Name);

            return mplew.getPacket();
        }

        public static byte[] guildInvite(int gid, String charName, int levelFrom, int jobFrom) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x7); // Version.176 [完成]
            mplew.writeInt(gid);
            mplew.writeMapleAsciiString(charName);
            mplew.writeInt(levelFrom);
            mplew.writeInt(jobFrom);
            mplew.writeInt(0);
            return mplew.getPacket();
        }

        public static byte[] showGuildInfo(MapleCharacter c) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x31); // Version.176 [完成]
            mplew.write(0);
            if ((c == null) || (c.getMGC() == null)) {
                mplew.write(0);
                return mplew.getPacket();
            }
            MapleGuild g = World.Guild.getGuild(c.getGuildId());
            if (g == null) {
                mplew.write(0);
                return mplew.getPacket();
            }
            mplew.write(1);
            getGuildInfo(mplew, g);
            // 公會經驗值
            mplew.writeInt(25); // 大小
            mplew.writeInt(0);
            mplew.writeInt(15000);
            mplew.writeInt(60000);
            mplew.writeInt(135000);
            mplew.writeInt(240000);
            mplew.writeInt(375000);
            mplew.writeInt(540000);
            mplew.writeInt(735000);
            mplew.writeInt(960000);
            mplew.writeInt(1215000);
            mplew.writeInt(1500000);
            mplew.writeInt(1815000);
            mplew.writeInt(2160000);
            mplew.writeInt(2535000);
            mplew.writeInt(2940000);
            mplew.writeInt(3375000);
            mplew.writeInt(3840000);
            mplew.writeInt(4335000);
            mplew.writeInt(4860000);
            mplew.writeInt(5415000);
            mplew.writeInt(6000000);
            mplew.writeInt(6615000);
            mplew.writeInt(7260000);
            mplew.writeInt(7935000);
            mplew.writeInt(8640000);

            return mplew.getPacket();
        }

        public static Object getGuildReceipt(int guildId) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x32); // Version.176 [推測]
            mplew.writeInt(guildId);
            MapleGuild guild = World.Guild.getGuild(guildId);
            getGuildInfo(mplew, guild);
            return mplew.getPacket();
        }

        public static byte[] newGuildInfo(MapleCharacter c) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x38); // Version.176 [推測]
            if ((c == null) || (c.getMGC() == null)) {
                return genericGuildMessage((byte) 0x26);
            }
            MapleGuild g = World.Guild.getGuild(c.getGuildId());
            if (g == null) {
                return genericGuildMessage((byte) 0x26);
            }
            getGuildInfo(mplew, g);

            return mplew.getPacket();
        }

        public static byte[] newGuildMember(MapleGuildCharacter mgc) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x3F); // Version.176 [推測]
            mplew.writeInt(mgc.getGuildId());
            guildMemberInfo(mplew, mgc);

            return mplew.getPacket();
        }

        public static byte[] newGuildJoinMember(MapleGuildCharacter mgc) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x45); // Version.176 [推測]
            mplew.writeInt(mgc.getGuildId());
            guildMemberInfo(mplew, mgc);

            return mplew.getPacket();
        }

        public static byte[] removeGuildJoin(int cid) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x4A); // Version.176 [推測]
            mplew.writeInt(cid);

            return mplew.getPacket();
        }

        public static byte[] memberLeft(MapleGuildCharacter mgc, boolean bExpelled) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(bExpelled ? 0x4E : 0x4B); // Version.176 [完成]
            mplew.writeInt(mgc.getGuildId());
            mplew.writeInt(mgc.getId());
            mplew.writeMapleAsciiString(mgc.getName());

            return mplew.getPacket();
        }

        public static byte[] guildDisband(int gid) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x51); // Version.176 [推測]
            mplew.writeInt(gid);

            return mplew.getPacket();
        }

        public static byte[] guildCapacityChange(int gid, int capacity) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x59); // Version.176 [推測]
            mplew.writeInt(gid);
            mplew.write(capacity);

            return mplew.getPacket();
        }

        public static byte[] guildMemberLevelJobUpdate(MapleGuildCharacter mgc) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x5C); // Version.176 [完成]
            mplew.writeInt(mgc.getGuildId());
            mplew.writeInt(mgc.getId());
            mplew.writeInt(mgc.getLevel());
            mplew.writeInt(mgc.getJobId());

            return mplew.getPacket();
        }

        public static byte[] guildMemberOnline(int gid, int cid, boolean bOnline) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x5D); // Version.176 [推測]
            mplew.writeInt(gid);
            mplew.writeInt(cid);
            mplew.write(bOnline ? 1 : 0);

            return mplew.getPacket();
        }

        public static byte[] rankTitleChange(int gid, String[] ranks) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x5E); // Version.176 [推測]
            mplew.writeInt(gid);
            for (String r : ranks) {
                mplew.writeMapleAsciiString(r);
            }

            return mplew.getPacket();
        }

        public static byte[] changeRank(MapleGuildCharacter mgc) { // 變更公會職位
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x60); // Version.176 [完成]
            mplew.writeInt(mgc.getGuildId());
            mplew.writeInt(mgc.getId());
            mplew.write(mgc.getGuildRank());

            return mplew.getPacket();
        }

        public static byte[] guildContribution(int gid, int cid, int c) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x62); // Version.176 [完成]
            mplew.writeInt(gid);
            mplew.writeInt(cid);
            mplew.writeInt(c);
            mplew.writeInt(500); //117
            mplew.writeInt(350); //117
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis())); //117

            return mplew.getPacket();
        }

        public static byte[] guildEmblemChange(int gid, short bg, byte bgcolor, short logo, byte logocolor) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x63); // Version.176 [完成]
            mplew.writeInt(gid);
            mplew.writeShort(bg);
            mplew.write(bgcolor);
            mplew.writeShort(logo);
            mplew.write(logocolor);

            return mplew.getPacket();
        }

        public static byte[] guildNotice(int gid, String notice) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x65); // Version.176 [完成]
            mplew.writeInt(gid);
            mplew.writeMapleAsciiString(notice);

            return mplew.getPacket();
        }

        public static byte[] updateGP(int gid, int GP, int glevel) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x69); // Version.176 [完成]
            mplew.writeInt(gid);
            mplew.writeInt(GP);
            mplew.writeInt(glevel);
            mplew.writeInt(GP);

            return mplew.getPacket();
        }

        public static byte[] showGuildRanks(int npcid, List<MapleGuildRanking.GuildRankingInfo> all) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x6A); // Version.176 [完成]
            mplew.writeInt(npcid);
            mplew.writeInt(all.size());
            for (MapleGuildRanking.GuildRankingInfo info : all) {
                mplew.writeShort(0);
                mplew.writeMapleAsciiString(info.getName());
                mplew.writeInt(info.getGP());
                mplew.writeInt(info.getLogo());
                mplew.writeInt(info.getLogoColor());
                mplew.writeInt(info.getLogoBg());
                mplew.writeInt(info.getLogoBgColor());
            }

            return mplew.getPacket();
        }

        public static byte[] guildSkillPurchased(int gid, int sid, int level, long expiration, String purchase, String activate) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x6F); // Version.176 [推測]
            mplew.writeInt(gid);
            mplew.writeInt(sid);
            mplew.writeShort(level);
            mplew.writeLong(PacketHelper.getTime(expiration));
            mplew.writeMapleAsciiString(purchase);
            mplew.writeMapleAsciiString(activate);

            return mplew.getPacket();
        }

        public static byte[] guildLeaderChanged(int gid, int oldLeader, int newLeader, int allianceId) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x79); // Version.176 [完成]
            mplew.writeInt(gid);
            mplew.writeInt(oldLeader);
            mplew.writeInt(newLeader);
            mplew.write(1);
            mplew.writeInt(allianceId);

            return mplew.getPacket();
        }

        private static void guildMemberInfo(MaplePacketLittleEndianWriter mplew, MapleGuildCharacter mgc) {
            mplew.writeInt(mgc.getId());
            mplew.writeAsciiString(mgc.getName(), 15);
            mplew.writeInt(mgc.getJobId());
            mplew.writeInt(mgc.getLevel());
            mplew.writeInt(mgc.getGuildRank());
            mplew.writeInt(mgc.isOnline() ? 1 : 0);
            mplew.writeInt(mgc.getAllianceRank());
            mplew.writeInt(mgc.getGuildContribution());
            mplew.writeInt(0);//可能是GP+IGP
            mplew.writeInt(0);//IGP
            mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        }

        public static byte[] denyGuildInvitation(String charname) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_OPERATION.getValue());
            mplew.write(0x57); // Version.176 [推測]
            mplew.writeMapleAsciiString(charname);

            return mplew.getPacket();
        }

        public static void getGuildInfo(MaplePacketLittleEndianWriter mplew, MapleGuild guild) {
            mplew.writeInt(guild.getId());
            mplew.writeMapleAsciiString(guild.getName());
            for (int i = 1; i <= 5; i++) {
                mplew.writeMapleAsciiString(guild.getRankTitle(i));
            }
            guild.addMemberData(mplew);
            guild.addMemberForm(mplew);
            mplew.writeInt(guild.getCapacity());
            mplew.writeShort(guild.getLogoBG());
            mplew.write(guild.getLogoBGColor());
            mplew.writeShort(guild.getLogo());
            mplew.write(guild.getLogoColor());
            mplew.writeMapleAsciiString(guild.getNotice());
            mplew.writeInt(guild.getGP());
            mplew.writeInt(guild.getGP());
            mplew.writeInt(guild.getAllianceId() > 0 ? guild.getAllianceId() : 0);
            mplew.write(guild.getLevel());
            mplew.writeShort(0);
            mplew.writeInt(0); // GP
            mplew.writeShort(guild.getSkills().size());
            for (MapleGuildSkill i : guild.getSkills()) {
                mplew.writeInt(i.skillID);
                mplew.writeShort(i.level);
                mplew.writeLong(PacketHelper.getTime(i.timestamp));
                mplew.writeMapleAsciiString(i.purchaser);
                mplew.writeMapleAsciiString(i.activator);
            }
            mplew.write(0);
            // 升級經驗值
            mplew.writeInt(25); // 大小
            mplew.writeInt(0);
            mplew.writeInt(15000);
            mplew.writeInt(60000);
            mplew.writeInt(135000);
            mplew.writeInt(240000);
            mplew.writeInt(375000);
            mplew.writeInt(540000);
            mplew.writeInt(735000);
            mplew.writeInt(960000);
            mplew.writeInt(1215000);
            mplew.writeInt(1500000);
            mplew.writeInt(1815000);
            mplew.writeInt(2160000);
            mplew.writeInt(2535000);
            mplew.writeInt(2940000);
            mplew.writeInt(3375000);
            mplew.writeInt(3840000);
            mplew.writeInt(4335000);
            mplew.writeInt(4860000);
            mplew.writeInt(5415000);
            mplew.writeInt(6000000);
            mplew.writeInt(6615000);
            mplew.writeInt(7260000);
            mplew.writeInt(7935000);
            mplew.writeInt(8640000);
        }

        public static byte[] showSearchGuilds(List<MapleGuild> guilds) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GUILD_SEARCH.getValue());
            mplew.writeInt(guilds.size());
            for (MapleGuild guild : guilds) {
                mplew.writeInt(guild.getId());
                mplew.writeInt(guild.getLevel());
                mplew.writeMapleAsciiString(guild.getName());
                mplew.writeMapleAsciiString(guild.getMGC(guild.getLeaderId()).getName());
                mplew.writeInt(guild.getMembers().size());
                mplew.writeInt(guild.getAverageLevel());
            }

            return mplew.getPacket();
        }

        public static byte[] BBSThreadList(List<MapleBBSThread> bbs, int start) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BBS_OPERATION.getValue());
            mplew.write(0x6);
            if (bbs == null) {
                mplew.write(0);
                mplew.writeLong(0L);
                return mplew.getPacket();
            }
            int threadCount = bbs.size();
            MapleBBSThread notice = null;
            for (MapleBBSThread b : bbs) {
                if (b.isNotice()) {
                    notice = b;
                    break;
                }
            }
            mplew.write(notice == null ? 0 : 1);
            if (notice != null) {
                addThread(mplew, notice);
            }
            if (threadCount < start) {
                start = 0;
            }
            mplew.writeInt(threadCount);
            int pages = Math.min(10, threadCount - start);
            mplew.writeInt(pages);
            for (int i = 0; i < pages; i++) {
                addThread(mplew, bbs.get(start + i));
            }

            return mplew.getPacket();
        }

        private static void addThread(MaplePacketLittleEndianWriter mplew, MapleBBSThread rs) {
            mplew.writeInt(rs.localthreadID);
            mplew.writeInt(rs.ownerID);
            mplew.writeMapleAsciiString(rs.name);
            mplew.writeLong(PacketHelper.getKoreanTimestamp(rs.timestamp));
            mplew.writeInt(rs.icon);
            mplew.writeInt(rs.getReplyCount());
        }

        public static byte[] showThread(MapleBBSThread thread) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.BBS_OPERATION.getValue());
            mplew.write(7);
            mplew.writeInt(thread.localthreadID);
            mplew.writeInt(thread.ownerID);
            mplew.writeLong(PacketHelper.getKoreanTimestamp(thread.timestamp));
            mplew.writeMapleAsciiString(thread.name);
            mplew.writeMapleAsciiString(thread.text);
            mplew.writeInt(thread.icon);
            mplew.writeInt(thread.getReplyCount());
            for (MapleBBSThread.MapleBBSReply reply : thread.replies.values()) {
                mplew.writeInt(reply.replyid);
                mplew.writeInt(reply.ownerID);
                mplew.writeLong(PacketHelper.getKoreanTimestamp(reply.timestamp));
                mplew.writeMapleAsciiString(reply.content);
            }

            return mplew.getPacket();
        }
    }

    public static class InfoPacket {

        public static byte[] showMesoGain(long gain, boolean inChat) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            if (!inChat) {
                mplew.write(0);
                mplew.write(1);
                mplew.write(0);
                mplew.writeLong(gain);
                mplew.writeShort(0);
            } else {
                mplew.write(6);
                mplew.writeLong(gain);
                mplew.writeInt(-1);
            }

            return mplew.getPacket();
        }

        public static byte[] getShowInventoryStatus(int mode) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(0);
            mplew.write(mode);
            mplew.writeInt(0);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] getShowItemGain(int itemId, short quantity) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.writeShort(0);
            mplew.writeInt(itemId);
            mplew.writeInt(quantity);

            return mplew.getPacket();
        }

        public static byte[] getShowItemGain(int itemId, short quantity, boolean inChat) {
            if (inChat) {
                Map<Integer, Integer> items = new HashMap();
                items.put(itemId, (int) quantity);
                return CField.EffectPacket.getShowItemGain(items);
            } else {
                return getShowItemGain(itemId, quantity);
            }
        }

        public static byte[] updateQuest(MapleQuestStatus quest) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(1);
            mplew.writeShort(quest.getQuest().getId());
            mplew.write(quest.getStatus());
            switch (quest.getStatus()) {
                case 0:
                    mplew.write(0);
                    break;
                case 1:
                    mplew.writeMapleAsciiString(quest.getCustomData() != null ? quest.getCustomData() : "");
                    break;
                case 2:
                    mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
            }

            return mplew.getPacket();
        }

        public static byte[] updateQuestMobKills(MapleQuestStatus status) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(1);
            mplew.writeShort(status.getQuest().getId());
            mplew.write(1);
            StringBuilder sb = new StringBuilder();
            for (Iterator<?> i$ = status.getMobKills().values().iterator(); i$.hasNext();) {
                int kills = ((Integer) i$.next());
                sb.append(StringUtil.getLeftPaddedStr(String.valueOf(kills), '0', 3));
            }
            mplew.writeMapleAsciiString(sb.toString());
            //mplew.writeLong(0L);//removed v146

            return mplew.getPacket();
        }

        public static byte[] itemExpired(int itemid) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(2);
            mplew.writeInt(itemid);

            return mplew.getPacket();
        }

        public static byte[] GainEXP_Monster(int gain, boolean inChat, boolean white, List<Pair<ExpMasks, Object[]>> masks) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(3);
            mplew.write(white ? 1 : 0);
            mplew.writeInt(gain);
            mplew.write(inChat ? 1 : 0);
            int expMask = 0;
            for (final Pair<ExpMasks, Object[]> maskupdate : masks) {
                Object i = maskupdate.getRight()[0];
                if ((i instanceof Byte && (Byte) i > 0) || (i instanceof Short && (Short) i > 0) || (i instanceof Integer && (Integer) i > 0) || (i instanceof Long && (Long) i > 0)) {
                    expMask |= maskupdate.getLeft().getValue();
                }
            }
            List<Pair<ExpMasks, Object[]>> newmasks = masks;
            if (newmasks.size() > 1) {
                Collections.sort(newmasks, (final Pair<ExpMasks, Object[]> o1, final Pair<ExpMasks, Object[]> o2) -> {
                    long val1 = o1.getLeft().getValue();
                    long val2 = o2.getLeft().getValue();
                    return (val1 < val2 ? -1 : (val1 == val2 ? 0 : 1));
                });
            }
            mplew.writeLong(expMask);
            mplew.writeInt(0);
            if (inChat) {
                mplew.write(0);
            }
            for (Pair<ExpMasks, Object[]> mask : newmasks) {
                if (mask.getRight() == null) {
                    continue;
                }
                for (Object i : mask.getRight()) {
                    if (i instanceof Byte) {
                        if ((byte) i > 0) {
                            mplew.write((Byte) i);
                        }
                    } else if (i instanceof Short) {
                        if ((Short) i > 0) {
                            mplew.writeShort((Short) i);
                        }
                    } else if (i instanceof Integer) {
                        if ((Integer) i > 0) {
                            mplew.writeInt((Integer) i);
                        }
                    } else if (i instanceof Long) {
                        if ((Long) i > 0) {
                            mplew.writeLong((Long) i);
                        }
                    }
                }
            }
            mplew.writeInt(0); // 道具裝備紅利經驗值(+%d)
            mplew.writeInt(0); // 蛋糕 vs 派餅經驗值(+%d)
            mplew.writeInt(0); // PvP 獎勵經驗值(+%d)

            return mplew.getPacket();
        }

        public static byte[] GainEXP_Others(long gain, boolean inChat, boolean white) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(3);
            mplew.write(white ? 1 : 0);
            mplew.writeInt((int) gain);
            mplew.write(inChat ? 1 : 0);
            mplew.writeLong(0); // expMask
            mplew.writeInt(0);
            if (inChat) {
                mplew.write(0);
            }
            mplew.writeInt(0);
            mplew.writeInt(0);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] GainEXP_MonsterCombo(long exp, int Combo, int moid) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(0x22);
            mplew.write(1);
            mplew.writeLong(exp);
            mplew.writeInt(Combo);
            mplew.writeInt(moid);

            return mplew.getPacket();
        }

        public static byte[] GainEXP_MultiKill(long exp, int num) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(0x22);
            mplew.write(0);
            mplew.writeLong(exp);
            mplew.writeInt(num);

            return mplew.getPacket();
        }

        public static byte[] getSPMsg(byte sp, short job) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(4);
            mplew.writeShort(job);
            mplew.write(sp);

            return mplew.getPacket();
        }

        public static byte[] getShowFameGain(int gain) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(5);
            mplew.writeInt(gain);

            return mplew.getPacket();
        }

        public static byte[] getGPMsg(int itemid) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(7);
            mplew.writeInt(itemid);

            return mplew.getPacket();
        }

        public static byte[] getGPContribution(int itemid) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(8);
            mplew.writeInt(itemid);

            return mplew.getPacket();
        }

        public static byte[] getStatusMsg(int itemid) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(9);
            mplew.writeInt(itemid);

            return mplew.getPacket();
        }

        public static byte[] showInfo(String info) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(0xB);
            mplew.writeMapleAsciiString(info);

            return mplew.getPacket();
        }

        public static byte[] updateInfoQuest(int quest, String data) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(12);
            mplew.writeShort(quest);
            mplew.writeMapleAsciiString(data);

//            System.err.println("infoquest " + mplew.toString());
            return mplew.getPacket();
        }

        public static byte[] showItemReplaceMessage(List<String> message) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(14);
            mplew.write(message.size());
            for (String x : message) {
                mplew.writeMapleAsciiString(x);
            }

            return mplew.getPacket();
        }

        public static byte[] showTraitGain(MapleTrait.MapleTraitType trait, int amount) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(0x11);
            mplew.writeLong(trait.getStat().getValue());
            mplew.writeInt(amount);

            return mplew.getPacket();
        }

        public static byte[] showTraitMaxed(MapleTrait.MapleTraitType trait) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(17);
            mplew.writeLong(trait.getStat().getValue());

            return mplew.getPacket();
        }

        public static byte[] getBPMsg(int amount) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(21);
            mplew.writeInt(amount);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] showExpireMessage(byte type, List<Integer> item) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter(4 + item.size() * 4);

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(type);
            mplew.write(item.size());
            for (Integer it : item) {
                mplew.writeInt(it);
            }

            return mplew.getPacket();
        }

        public static byte[] showStatusMessage(int mode, String info, String data) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(mode);
            if (mode == 22) {
                mplew.writeMapleAsciiString(info);
                mplew.writeMapleAsciiString(data);
            }

            return mplew.getPacket();
        }

        public static byte[] showReturnStone(int act) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(23);
            mplew.write(act);

            return mplew.getPacket();
        }

        public static byte[] showStatus(int IDK) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());

            mplew.write(3);
            mplew.write(1);
            mplew.writeInt(IDK);
            mplew.writeLong(0);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] showItemBox() {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(1);
            mplew.write(0x7A);
            mplew.write(0x1C);
            mplew.write(1);
            mplew.write(1);
            mplew.write(0);
            mplew.write(0x30);
            return mplew.getPacket();
        }

        public static byte[] getShowCoreGain(int core, int quantity) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.SHOW_STATUS_INFO.getValue());
            mplew.write(30);
            mplew.write(22);
            mplew.writeInt(core);
            mplew.writeInt(quantity);

            return mplew.getPacket();
        }

        public static byte[] getEventList(String notice, List<String> eventMessage) { // By 寒霜天地 [尚未完成]
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EVENT_LIST.getValue());
            mplew.writeMapleAsciiString(notice);
            mplew.write(0); // Boolean
            mplew.writeInt(eventMessage.size());
            if (eventMessage.size() > 0) {
                for (int i = 0; i < eventMessage.size(); i++) {
                    mplew.write(i);
                    mplew.writeMapleAsciiString(eventMessage.get(i));
                }
            }
            mplew.writeInt(5); // 活動數量
            // 迴圈開始(活動1)
            mplew.write(HexTool.getByteArrayFromHexString("1C 01 00 00")); // 未知
            mplew.writeMapleAsciiString("[點名] 在新星世界裡點名吧!"); // 活動名稱
            mplew.write(HexTool.getByteArrayFromHexString("00 00 00 00 00 00 00 00 00 00")); // 未知
            mplew.writeInt(20150128); // 開始時間
            mplew.writeInt(20150210); // 結束時間
            mplew.write(HexTool.getByteArrayFromHexString("00 00 00 00 06 00 00 00 00 00 00 00 01")); // 未知
            mplew.writeInt(4); // 活動道具數量
            mplew.writeInt(2433062); // 活動道具
            mplew.writeInt(1102755); // 活動道具
            mplew.writeInt(5121048); // 活動道具
            mplew.writeInt(1702382); // 活動道具
            mplew.write(HexTool.getByteArrayFromHexString("00 00 00 00 00 00")); // 未知
            // 迴圈(活動2、活動3、活動4、活動5)
            mplew.write(HexTool.getByteArrayFromHexString("1D 01 00 00 16 00 5B A5 56 A9 75 AD AD A9 77 5D 20 49 63 65 62 6F 78 20 AC A1 B0 CA 00 00 00 00 00 00 00 00 00 00 69 77 33 01 C2 77 33 01 00 00 00 00 05 00 00 00 00 00 00 00 01 05 00 00 00 28 10 3D 00 71 C4 41 00 C0 72 0F 00 F3 B8 21 00 69 8A 1E 00 00 00 00 00 00 00 1E 01 00 00 1A 00 5B A5 56 A9 75 AD AD A9 77 5D 20 A8 43 A4 D1 AC 44 BE D4 C2 49 A6 57 21 0D 0A 00 00 00 00 00 00 00 00 00 00 69 77 33 01 BB 77 33 01 00 00 00 00 05 00 00 00 00 00 00 00 01 05 00 00 00 93 84 1E 00 28 10 3D 00 71 C4 41 00 43 46 1F 00 AE 46 1F 00 00 00 00 00 00 01 21 00 00 00 FA 00 00 00 FF FF FF FF 1F 01 00 00 18 00 5B A5 56 A9 75 AD AD A9 77 5D 20 B5 77 B9 F4 B0 D3 A9 B1 B6 7D B1 D2 21 00 00 00 00 00 00 00 00 00 00 69 77 33 01 C2 77 33 01 00 00 00 00 04 00 00 00 00 00 00 00 01 05 00 00 00 70 FD 2D 00 71 FD 2D 00 46 C4 41 00 A2 25 26 00 89 29 26 00 00 00 00 00 00 00 20 01 00 00 0D 00 32 30 31 35 20 B7 73 A6 7E AC A1 B0 CA 00 00 00 00 00 00 00 00 00 00 C3 77 33 01 D0 77 33 01 00 00 00 00 05 00 00 00 00 00 00 00 01 03 00 00 00 35 22 25 00 42 10 20 00 6D 22 25 00"));
            // 未知
            mplew.write(HexTool.getByteArrayFromHexString("0A 00 00 00 73 D7 00 00 74 D7 00 00 75 D7 00 00 76 D7 00 00 77 D7 00 00 78 D7 00 00 79 D7 00 00 7A D7 00 00 7B D7 00 00 7C D7 00 00 00 01 21 00 00 00 FA 00 00 00 FF FF FF FF"));

            return mplew.getPacket();
        }
    }

    public static class BuffPacket {
        
        public static byte[] getQuiverKartrigecount(int count, int mode) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            switch (mode) {
                case 1:
                    count = 10000 * count;
                    break;
                case 2:
                    count = 130040 + count * 100;
                    break;
                case 3:
                    count = 160000 + count;
                    break;
            }
            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.QUIVER_KARTRIGE);
            mplew.writeInt(count);
            mplew.writeInt(3101009);
            mplew.writeInt(DateUtil.getTime(System.currentTimeMillis()));
            mplew.writeZeroBytes(5);
            mplew.writeInt(mode);
            mplew.writeLong(0);
            mplew.writeInt(0);
            mplew.writeInt(0);
            return mplew.getPacket();
        }
        public static byte[] giveSoulGauge(int count, int skillid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.SOUL_WEAPON);
            mplew.writeShort(count);
            mplew.writeInt(skillid);//skill
//            mplew.write(HexTool.getByteArrayFromHexString("A1 47 FD ED"));
            mplew.writeInt(0);
            mplew.writeInt(1000);
            mplew.writeInt(skillid);//soulskill
            mplew.writeInt(0);
            mplew.writeShort(0);
            mplew.writeLong(0);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] cancelSoulGauge() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_BUFF.getValue());
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.SOUL_WEAPON);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] giveSoulEffect(int skillid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.SOUL_WEAPON_HEAD);
            mplew.writeShort(0);
            mplew.writeInt(skillid);
            mplew.writeInt(640000);
            mplew.writeLong(0);
            mplew.writeShort(8);
            mplew.writeLong(0);
            mplew.writeInt(0);

            return mplew.getPacket();
        }

        public static byte[] giveForeignSoulEffect(int cid, int skillid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
            mplew.writeInt(cid);
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.SOUL_WEAPON_HEAD);
            mplew.writeInt(skillid);
            mplew.writeLong(0x60000000000L);
            mplew.writeLong(0);
            mplew.writeLong(0);
            mplew.writeInt(0);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] cancelForeignSoulEffect(int cid) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_FOREIGN_BUFF.getValue());
            mplew.writeInt(cid);
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.SOUL_WEAPON_HEAD);
            mplew.write(1);

            return mplew.getPacket();
        }

        public static byte[] giveDice(int buffid, int skillid, int duration, Map<MapleBuffStat, Integer> statups) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeBuffMask(mplew, statups);

            mplew.writeShort(Math.max(buffid / 100, Math.max(buffid / 10, buffid % 10))); // 1-6

            mplew.writeInt(skillid); // skillid
            mplew.writeInt(duration);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.writeInt(GameConstants.getDiceStat(buffid, 3));
            mplew.writeInt(GameConstants.getDiceStat(buffid, 3));
            mplew.writeInt(GameConstants.getDiceStat(buffid, 4));
            mplew.writeZeroBytes(20); //idk
            mplew.writeInt(GameConstants.getDiceStat(buffid, 2));
            mplew.writeZeroBytes(12); //idk
            mplew.writeInt(GameConstants.getDiceStat(buffid, 5));
            mplew.writeZeroBytes(16); //idk
            mplew.writeInt(GameConstants.getDiceStat(buffid, 6));
            mplew.writeZeroBytes(16);
            mplew.writeZeroBytes(6);//new 143
            mplew.writeInt(1000);//new 143
            mplew.write(1);
            mplew.writeInt(0);//new143
//            mplew.write(4); // Total buffed times
//            mplew.write(0);//v112
            return mplew.getPacket();
        }

        public static byte[] giveHoming(int skillid, int mobid, int x) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.HOMING_BEACON);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.writeInt(1);
            mplew.writeLong(skillid);
            mplew.write(0);
            mplew.writeLong(mobid);
            mplew.writeShort(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(0);//v112
            return mplew.getPacket();
        }

        public static byte[] giveMount(int buffid, int skillid, Map<MapleBuffStat, Integer> statups) {
            return showMonsterRiding(-1, statups, buffid, skillid);
        }

        public static byte[] showMonsterRiding(int cid, Map<MapleBuffStat, Integer> statups, int buffid, int skillId) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            if (cid == -1) {
                mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            } else {
                mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
                mplew.writeInt(cid);
            }
            PacketHelper.writeBuffMask(mplew, statups);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.writeInt(buffid);
            mplew.writeInt(skillId);
            mplew.writeInt(0);
            mplew.writeShort(0);
            mplew.write(1);
            mplew.write(4);
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] givePirate(Map<MapleBuffStat, Integer> statups, int duration, int skillid) {
            return giveForeignPirate(statups, duration, -1, skillid);
        }

        public static byte[] giveForeignPirate(Map<MapleBuffStat, Integer> statups, int duration, int cid, int skillid) {
            final boolean infusion = skillid == 5121009 || skillid == 15111005;
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
            mplew.writeInt(cid);
            PacketHelper.writeBuffMask(mplew, statups);
            mplew.writeShort(0);
            mplew.write(0);
            for (Integer stat : statups.values()) {
                mplew.writeInt(stat);
                mplew.writeLong(skillid);
                mplew.writeZeroBytes(infusion ? 6 : 1);
                mplew.writeShort(duration);//duration... seconds
            }
            mplew.writeShort(0);
            mplew.writeShort(0);
            mplew.write(1);
            mplew.write(1);
            return mplew.getPacket();
        }

        public static byte[] giveArcane(int skillid, Map<Integer, Integer> statups) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.MANY_USES);
            mplew.writeShort(statups.size());
            mplew.writeInt(skillid);
            mplew.writeInt(5000);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.writeShort(0);
            mplew.writeShort(0);
            mplew.write(0);
            mplew.write(0);
            mplew.writeZeroBytes(9);
            return mplew.getPacket();
        }

        public static byte[] giveEnergyChargeTest(int bar, int bufflength) {
            return giveEnergyChargeTest(-1, bar, bufflength);
        }

        public static byte[] giveEnergyChargeTest(int cid, int bar, int bufflength) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            if (cid == -1) {
                mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            } else {
                mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
                mplew.writeInt(cid);
            }
            PacketHelper.writeSingleMask(mplew, MapleBuffStat.ENERGY_CHARGE);
            mplew.writeZeroBytes(5);
            mplew.writeInt(0);  // 技能ID
            mplew.writeInt(Math.min(bar, 10000));
            mplew.writeInt(0);
            mplew.write(0);
            mplew.write(0);
            //mplew.writeInt(bar >= 10000 ? bufflength : 0);
            mplew.writeLong(0);
            mplew.writeShort(6);
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] giveBuff(int buffid, int bufflength, Map<MapleBuffStat, Integer> statups, MapleStatEffect effect, MapleCharacter chr) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeBuffMask(mplew, statups);
            boolean specialBuff = false;
            for (Map.Entry<MapleBuffStat, Integer> stat : statups.entrySet()) {
                if (GameConstants.isIDA_SpecialBuff(stat.getKey())) {
                    specialBuff = true;
                }
            }
            for (Map.Entry<MapleBuffStat, Integer> stat : statups.entrySet()) {
                if (!stat.getKey().canStack()) {
                    System.out.println("一般Buff::" + stat.getKey().name()); // 查看排列順序用                    
                    if (specialBuff) {
                        mplew.writeInt(stat.getValue());
                    } else {
                        mplew.writeShort(stat.getValue());
                    }
                    mplew.writeInt(buffid);
                    mplew.writeInt(bufflength);
                }
            }

            // IDA 確定的部分...太多寫到另一個Function
            IsSpecialBuff(mplew, statups, effect, chr, buffid, bufflength);

            //尚未找到位置的Buff
            for (Map.Entry<MapleBuffStat, Integer> stat : statups.entrySet()) {                
                if (stat.getKey().canStack() && GameConstants.isSpecialStackBuff(stat.getKey())) {
                    System.out.println("特殊Buff::" + stat.getKey().name()); // 查看排列順序用
                    mplew.writeInt(stat.getValue());
                    mplew.writeInt(buffid);
                    mplew.writeZeroBytes(5);
                    if (stat.getKey() == MapleBuffStat.SPEED_INFUSION) {
                        mplew.writeZeroBytes(5);
                    }
                }
            }

            // 這裡是堆疊Buff
            for (Map.Entry<MapleBuffStat, Integer> stat : statups.entrySet()) {                
                if (stat.getKey().canStack() && !GameConstants.isSpecialStackBuff(stat.getKey())) {
                    System.out.println("堆疊Buff::" + stat.getKey().name()); // 查看排列順序用
                    List<SpecialBuffInfo> buffs = chr.getStackBuffInfo(stat.getKey());
                    long currentTime = 0;
                    for (SpecialBuffInfo ss : buffs) {
                        if (ss.getSkillId() == buffid) {
                            currentTime = ss.getBuffStatValueHolder().startTime;
                        }
                    }
                    mplew.writeInt(buffs.size());
                    for (SpecialBuffInfo ss : buffs) {
                        int ctime = ((int)(currentTime - ss.getBuffStatValueHolder().startTime));
                        mplew.writeInt(ss.getSkillId());
                        mplew.writeInt(ss.getBuffStatValueHolder().value);
                        mplew.writeInt(((int)currentTime) - ctime);
                        mplew.writeInt(ctime);
                        mplew.writeInt(ss.getBuffStatValueHolder().localDuration);
                    }
                }
            }
            //最後的SpecialBuff
            if (statups.containsKey(MapleBuffStat.XENON_FLY)) {
                mplew.writeInt(0);
            }

            // IDA 確認的格式
            mplew.writeShort(0); //MoopleDEV 寫 delay ??
            mplew.write(0);
            mplew.write(0);
            mplew.write(1);

            for (Map.Entry<MapleBuffStat, Integer> stat : statups.entrySet()) {
                if (GameConstants.isMovementAffectingStat(stat.getKey())) {
                    mplew.write(0);
                }
            }

            mplew.writeInt(0);

//            mplew.writeZeroBytes(69); //防止長度不夠Error 38 用的，對填錯值無效...
            System.err.println("\u001B[34m buff " + mplew.toString());
            return mplew.getPacket();
        }

        public static void IsSpecialBuff(MaplePacketLittleEndianWriter mplew, Map<MapleBuffStat, Integer> statups, MapleStatEffect effect, MapleCharacter chr, int buffid, int bufflength) {
            if (statups.containsKey(MapleBuffStat.SOUL_WEAPON)) {
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.SOUL_WEAPON_HEAD)) {
                mplew.writeInt(0);
            }
            int Unk_Size = 0;
            mplew.writeShort(Unk_Size);
            for (int i = 0; i < Unk_Size; ++i) {
                mplew.writeInt(0);
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.HAYATO)) {
                mplew.writeInt(statups.containsKey(MapleBuffStat.BATTOUJUTSU_SOUL) ? 41110008 : 0);
            }
            mplew.write(0); //DefenseAtt
            mplew.write(0); //DefenseState
            mplew.write(statups.containsKey(MapleBuffStat.SOLUNA_EFFECT) ? 7 : 0);
            if (statups.containsKey(MapleBuffStat.DICE_ROLL)) {
                for (int j = 0; j < 22; ++j) {
                    mplew.writeInt(0);
                }
            }
//            if (statups.containsKey(MapleBuffStat.DARK_AURA_OLD)) {
//                mplew.write(1);
//            }
//            if (statups.containsKey(MapleBuffStat.YELLOW_AURA_OLD)) {
//                mplew.write(1);
//            }
//            if (statups.containsKey(MapleBuffStat.BLUE_AURA_OLD)) {
//                mplew.write(1);
//            }
            if (statups.containsKey(MapleBuffStat.MOUNT_MORPH)) {
                mplew.write(1);
            }
//            if (statups.containsKey(MapleBuffStat.BODY_BOOST)) {
//                mplew.writeInt(0);
//            }
            if (statups.containsKey(MapleBuffStat.JUDGMENT_DRAW)) {
                switch (statups.get(MapleBuffStat.JUDGMENT_DRAW)) {
                    case 1:
                    case 2:
                    case 4:
                        mplew.writeInt(effect.getSourceId() == 20031209 ? 10 : 20);
                        break;
                    case 3:
                        mplew.writeInt(2020); // 2020 <== 抗性20%, 屬性20%
                        break;
                    default:
                        mplew.writeInt(0);
                        break;
                }
            }
            if (statups.containsKey(MapleBuffStat.DARK_CRESCENDO)) {
                mplew.write(1);
            }
            if (statups.containsKey(MapleBuffStat.IDA_UNK_BUFF3)) {
                mplew.write(1);
            }
            if (statups.containsKey(MapleBuffStat.IDA_UNK_BUFF4)) {
                mplew.write(0);
                mplew.writeShort(0);
                mplew.write(0);
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.LUNAR_TIDE)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.ABNORMAL_BUFF_RESISTANCES)) {
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.LUMINOUS_GAUGE)) {
                int ct = (int)System.currentTimeMillis();
                int[] val;
                switch (buffid) {
                    case 20040216:
                        val = new int[] {20040216, 0};
                        break;
                    case 20040217:
                        val = new int[] {20040217, 0};
                        break;
                    case 20040219:
                        val = new int[] {20040216, 20040217};
                        break;
                    case 20040220:
                        val = new int[] {20040217, 20040216};
                        break;
                    default:
                        val = new int[] {0, 0};
                        break;
                }
                for (int k = 0; k < 2; ++k) {
                    mplew.writeInt(val[k]);
                    mplew.writeInt(ct);
                }
                mplew.writeInt(chr.getDarkGauge()); // 暗值
                mplew.writeInt(chr.getLightGauge()); // 光值
            }
            if (statups.containsKey(MapleBuffStat.IGNORE_DEF)) {
                if (effect.getSourceId() == 15001022) {
                    mplew.writeInt(statups.get(MapleBuffStat.IGNORE_DEF) / 5);
                }
            }
            if (statups.containsKey(MapleBuffStat.TEMPEST_BLADES)) {
                List<Integer> ss = new ArrayList<>();
                int type = 1;
                switch (effect.getSourceId()) {
                    case 61101002:
                        type = 1;
                        break;
                    case 61120007:
                        type = 2;
                        break;
                    case 61111008:
                        type = 3;
                        break;
                    case 61120008:
                        type = 4;
                        break;
                }
                int count = effect.getSourceId() == 61101002 ? 3 : 5;
                for (int j = 0; j < count; j++) {
                    ss.add(0);
                }

                mplew.writeInt(type);
                mplew.writeInt(count);
                mplew.writeInt(effect.getWeapon());
                mplew.writeInt(ss.size()); // Size
                for (int i = 0; i < ss.size(); i++) {
                    mplew.writeInt(ss.get(i));
                }
            }
            if (statups.containsKey(MapleBuffStat.KAISER_COMBO)) {
                int comboType = 0;
                if (effect.getCombo() >= 100) {
                    comboType = 1;
                } else if (effect.getCombo() >= 300) {
                    comboType = 2;
                }
                mplew.writeInt(comboType);
            }
            if (statups.containsKey(MapleBuffStat.IDA_UNK_BUFF5)) {
                List<Integer> ss = new ArrayList<>();
                ss.add(1);
                ss.add(1);
                ss.add(0); // 如果 value <= 0 結束

                for (int s : ss) {
                    mplew.writeInt(s);
                }
            }
            if (statups.containsKey(MapleBuffStat.SLOW)) {
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.ABSOLUTE_ZERO_AURA)) {
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.CHILLING_STEP)) {
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.IDA_UNK_BUFF8)) {
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.OOPARTS_CODE)) {
                mplew.write(1);
            }
            if (statups.containsKey(MapleBuffStat.IDA_UNK_BUFF10)) {
                mplew.writeInt(0);
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.SOLUNA_EFFECT)) {
                mplew.write(1);
            }
            if (statups.containsKey(MapleBuffStat.IDA_UNK_BUFF11)) {
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.CROSS_SURGE)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.IDA_UNK_BUFF12)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.IDA_UNK_BUFF13)) {
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.IDA_SPECIAL_BUFF_6)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.IDA_SPECIAL_BUFF_7)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.DIVINE_FORCE_AURA)) {
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.DIVINE_SPEED_AURA)) {
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.IDA_UNK_BUFF14)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.SHARP_EYES)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.HOLY_SHIELD)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.IGNORE_MOB_DAM_R)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.SPIRIT_WARD)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.IDA_UNK_BUFF16)) {
                mplew.writeInt(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.PROP)) {
                mplew.writeInt(0xA);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.IDA_UNK_BUFF2)) {
                mplew.writeInt(1);
            }
            if (statups.containsKey(MapleBuffStat.IDA_UNK_BUFF1)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.NEW_AURA)) {
                mplew.writeInt(bufflength);
                mplew.write(1);
            }
            mplew.writeInt(0);
        }

        public static byte[] giveDebuff(MapleDisease statups, int x, int skillid, int level, int duration) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_BUFF.getValue());
            PacketHelper.writeSingleMask(mplew, statups);
            mplew.writeShort(x);
            mplew.writeShort(skillid);
            mplew.writeShort(level);
            mplew.writeInt(duration);
            mplew.writeShort(0);
            mplew.writeShort(0);
            //mplew.write(1);
            mplew.write(0);
            //mplew.write(1);
            mplew.writeZeroBytes(30);
            //System.out.println(HexTool.toString(mplew.getPacket()));
            return mplew.getPacket();
        }

        public static byte[] cancelBuff(List<MapleBuffStat> statups, MapleCharacter chr) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_BUFF.getValue());

            PacketHelper.writeMask(mplew, statups);
            
            for (MapleBuffStat stat : statups) {                
                if (stat.canStack() && !GameConstants.isSpecialStackBuff(stat)) {
                    System.out.println("堆疊Buff::" + stat.name()); // 查看排列順序用
                    List<SpecialBuffInfo> buffs = chr.getStackBuffInfo(stat);
                    long currentTime = System.currentTimeMillis();                    
                    mplew.writeInt(buffs.size());
                    for (SpecialBuffInfo ss : buffs) {
                        int ctime = ((int)(currentTime - ss.getBuffStatValueHolder().startTime));
                        mplew.writeInt(ss.getSkillId());
                        mplew.writeInt(ss.getBuffStatValueHolder().value);
                        mplew.writeInt(((int)currentTime) - ctime);
                        mplew.writeInt(ctime);
                        mplew.writeInt(ss.getBuffStatValueHolder().localDuration);
                    }
                }
            }
            
            for (MapleBuffStat z : statups) {
                if (GameConstants.isMovementAffectingStat(z)) {
                    mplew.write(0);
                }
                if (z == MapleBuffStat.SOLUNA_EFFECT) {
                    mplew.write(0);
                }
                if (z == MapleBuffStat.MONSTER_RIDING) {
                    mplew.write(0); // boolean
                }
            }

            mplew.writeZeroBytes(20); //防止長度不夠Error 38 用的

            return mplew.getPacket();
        }

        public static byte[] cancelDebuff(MapleDisease mask) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_BUFF.getValue());

            PacketHelper.writeSingleMask(mplew, mask);
            mplew.write(3);
            mplew.write(1);
            mplew.writeLong(0);
            mplew.write(0);//v112
            return mplew.getPacket();
        }

        public static byte[] cancelHoming() {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_BUFF.getValue());

            PacketHelper.writeSingleMask(mplew, MapleBuffStat.HOMING_BEACON);
            mplew.write(0);//v112

            return mplew.getPacket();
        }

        public static byte[] giveForeignBuff(int cid, Map<MapleBuffStat, Integer> statups, MapleStatEffect effect) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();//有問題

            mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
            mplew.writeInt(cid);

            PacketHelper.writeBuffMask(mplew, statups);            
            for (Entry<MapleBuffStat, Integer> statup : statups.entrySet()) {
                if (statup.getKey() == MapleBuffStat.SHADOWPARTNER || statup.getKey() == MapleBuffStat.MECH_CHANGE || statup.getKey() == MapleBuffStat.DARK_AURA_OLD || statup.getKey() == MapleBuffStat.YELLOW_AURA_OLD || statup.getKey() == MapleBuffStat.BLUE_AURA_OLD || statup.getKey() == MapleBuffStat.GIANT_POTION || statup.getKey() == MapleBuffStat.SPIRIT_LINK || statup.getKey() == MapleBuffStat.PYRAMID_PQ || statup.getKey() == MapleBuffStat.WK_CHARGE || statup.getKey() == MapleBuffStat.DAMAGE_R || statup.getKey() == MapleBuffStat.MORPH || statup.getKey() == MapleBuffStat.WATER_SHIELD || statup.getKey() == MapleBuffStat.DARK_METAMORPHOSIS) {
                    mplew.writeShort(statup.getValue().shortValue());
                    mplew.writeInt(effect.isSkill() ? effect.getSourceId() : -effect.getSourceId());
                } else if (statup.getKey() == MapleBuffStat.FAMILIAR_SHADOW) {
                    mplew.writeInt(statup.getValue());
                    mplew.writeInt(effect.getCharColor());
                } else {
                    mplew.writeShort(statup.getValue().shortValue());
                }
            }
            mplew.writeShort(1);
            mplew.write(0);
            mplew.writeInt(2);
            mplew.writeZeroBytes(13);
            mplew.writeShort(600);

            mplew.writeZeroBytes(20);
            /*
             mplew.writeShort(0);
             mplew.writeShort(0);
             mplew.write(1);
             mplew.write(1);
             mplew.write(0);*///v140
            return mplew.getPacket();
        }
        
        public void getForeignBuffInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, MapleStatEffect effect, Map<MapleBuffStat, Integer> statups) {
            if (statups.containsKey(MapleBuffStat.SPEED)) {
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.SUMMON)) {
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.WK_CHARGE)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.IDA_UNK_BUFF4)) {
                mplew.writeShort(0);
            }
            if (statups.containsKey(MapleBuffStat.STUN)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.FROZEN)) {
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.DARKNESS)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.SEAL)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.WEAKEN)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF1)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.CURSE)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.SLOW)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF2)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF3)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF4)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF5)) {
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF6)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF7)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.POISON)) {
                mplew.writeShort(0);
            }
            if (statups.containsKey(MapleBuffStat.POISON)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.SHADOWPARTNER)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.DARKSIGHT)) {
            }
            if (statups.containsKey(MapleBuffStat.SOULARROW)) {
            }
            if (statups.containsKey(MapleBuffStat.MORPH)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.GHOST_MORPH)) {
                mplew.writeShort(0);
            }
            if (statups.containsKey(MapleBuffStat.SEDUCE)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.IDA_MOVE_BUFF2)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.DARK_METAMORPHOSIS)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.SPIRIT_CLAW)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.ZOMBIFY)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.REVERSE_DIRECTION)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.SPARK)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.DROP_RATE)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.ACASH_RATE)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.INDIE_ALL_STATE_R)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF8)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF9)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.BERSERK_FURY)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.DIVINE_BODY)) {
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF10)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF11)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.SHADOW)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.BLINDNESS)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.MAGIC_SHIELD)) {
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.SOARING)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.FREEZE)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF12)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.TORNADO_CURSE)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.BACKSTEP)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.FINAL_CUT)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.SATELLITESAFE_ABSORB)) {
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF13)) {
                mplew.write(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF13)) {
            }
            if (statups.containsKey(MapleBuffStat.INFILTRATE)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.MECH_CHANGE)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.DIVINE_SHIELD)) {
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF14)) {
            }
            if (statups.containsKey(MapleBuffStat.GIANT_POTION)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.PLAYERS_BUFF15)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.IDA_MOVE_BUFF7)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.HIDDEN_POTENTIAL)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
            if (statups.containsKey(MapleBuffStat.WIND_WALK)) {
                mplew.writeShort(0);
                mplew.writeInt(0);
            }
        }

        public static byte[] giveForeignDebuff(int cid, final MapleDisease statups, int skillid, int level, int x) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.GIVE_FOREIGN_BUFF.getValue());
            mplew.writeInt(cid);

            PacketHelper.writeSingleMask(mplew, statups);
            if (skillid == 125) {
                mplew.writeShort(0);
                mplew.write(0);
            }
            mplew.writeShort(x);
            mplew.writeShort(skillid);
            mplew.writeShort(level);
            mplew.writeShort(0); // same as give_buff
            mplew.writeShort(0); //Delay
            mplew.write(1);
            mplew.write(1);
            mplew.write(0);//v112
            mplew.writeZeroBytes(20);
            return mplew.getPacket();
        }

        public static byte[] cancelForeignBuff(int cid, List<MapleBuffStat> statups) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_FOREIGN_BUFF.getValue());
            mplew.writeInt(cid);
            PacketHelper.writeMask(mplew, statups);//36 bytes
//            mplew.write(3);
            mplew.write(1);
//            mplew.write(0);//v112

            return mplew.getPacket();
        }

        public static byte[] cancelForeignDebuff(int cid, MapleDisease mask) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.CANCEL_FOREIGN_BUFF.getValue());
            mplew.writeInt(cid);

            PacketHelper.writeSingleMask(mplew, mask);//48 bytes
            //mplew.write(3);
            mplew.write(1);
            //mplew.write(0);//v112
            return mplew.getPacket();
        }

    }

    public static class InventoryPacket {

        public static byte[] addInventorySlot(MapleInventoryType type, Item item) {
            return addInventorySlot(type, item, false);
        }

        public static byte[] updateInventorySlot() {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);
            return mplew.getPacket();
        }

        public static byte[] addInventorySlot(MapleInventoryType type, Item item, boolean fromDrop) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(fromDrop ? 1 : 0);
            mplew.write(1);
            mplew.write(0);

            mplew.write(GameConstants.isInBag(item.getPosition(), type.getType()) ? 9 : 0);
            mplew.write(type.getType());
            mplew.writeShort(item.getPosition());
            PacketHelper.addItemInfo(mplew, item);
            return mplew.getPacket();
        }

        public static byte[] updateInventorySlot(MapleInventoryType type, Item item, boolean fromDrop) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(fromDrop ? 1 : 0);
            mplew.write(1);
            mplew.write(0);

            mplew.write(GameConstants.isInBag(item.getPosition(), type.getType()) ? 6 : 1);
            mplew.write(type.getType());
            mplew.writeShort(item.getPosition());
            mplew.writeShort(item.getQuantity());

            return mplew.getPacket();
        }

        public static byte[] moveInventoryItem(MapleInventoryType type, short src, short dst, boolean bag, boolean bothBag) {
            return moveInventoryItem(type, src, dst, (byte) -1, bag, bothBag);
        }

        public static byte[] moveInventoryItem(MapleInventoryType type, short src, short dst, short equipIndicator, boolean bag, boolean bothBag) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(1);
            mplew.write(0);

            mplew.write(bag ? 5 : bothBag ? 8 : 2);
            mplew.write(type.getType());
            mplew.writeShort(src);
            mplew.writeShort(dst);
            if (bag) {
                mplew.writeShort(0);
            }
            if (equipIndicator != -1) {
                mplew.write(equipIndicator);
            }

            return mplew.getPacket();
        }

        public static byte[] moveAndMergeInventoryItem(MapleInventoryType type, short src, short dst, short total, boolean bag, boolean switchSrcDst, boolean bothBag) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(2);
            mplew.write(0);

            mplew.write((bag) && ((switchSrcDst) || (bothBag)) ? 7 : 3);
            mplew.write(type.getType());
            mplew.writeShort(src);

            mplew.write((bag) && ((!switchSrcDst) || (bothBag)) ? 6 : 1);
            mplew.write(type.getType());
            mplew.writeShort(dst);
            mplew.writeShort(total);

            return mplew.getPacket();
        }

        public static byte[] moveAndMergeWithRestInventoryItem(MapleInventoryType type, short src, short dst, short srcQ, short dstQ, boolean bag, boolean switchSrcDst, boolean bothBag) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(2);
            mplew.write(0);

            mplew.write((bag) && ((switchSrcDst) || (bothBag)) ? 6 : 1);
            mplew.write(type.getType());
            mplew.writeShort(src);
            mplew.writeShort(srcQ);

            mplew.write((bag) && ((!switchSrcDst) || (bothBag)) ? 6 : 1);
            mplew.write(type.getType());
            mplew.writeShort(dst);
            mplew.writeShort(dstQ);

            return mplew.getPacket();
        }

        public static byte[] clearInventoryItem(MapleInventoryType type, short slot, boolean fromDrop) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(fromDrop ? 1 : 0);
            mplew.write(1);
            mplew.write(0);

            mplew.write((slot > 100) && (type == MapleInventoryType.ETC) ? 7 : 3);
            mplew.write(type.getType());
            mplew.writeShort(slot);

            return mplew.getPacket();
        }

        public static byte[] updateSpecialItemUse(Item item, byte invType, MapleCharacter chr) {
            return updateSpecialItemUse(item, invType, item.getPosition(), false, chr);
        }

        public static byte[] updateSpecialItemUse(Item item, byte invType, short pos, boolean theShort, MapleCharacter chr) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(0);
            mplew.write(2);
            mplew.write(0);

            mplew.write(GameConstants.isInBag(pos, invType) ? 7 : 3);
            mplew.write(invType);
            mplew.writeShort(pos);

            mplew.write(0);
            mplew.write(invType);
            if ((item.getType() == 1) || (theShort)) {
                mplew.writeShort(pos);
            } else {
                mplew.write(pos);
            }
            PacketHelper.addItemInfo(mplew, item, chr);
            if (pos < 0) {
                mplew.write(2);
            }

            return mplew.getPacket();
        }

        public static byte[] updateSpecialItemUse_(Item item, byte invType, MapleCharacter chr) {
            return updateSpecialItemUse_(item, invType, item.getPosition(), chr);
        }

        public static byte[] updateSpecialItemUse_(Item item, byte invType, short pos, MapleCharacter chr) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(0);
            mplew.write(1);
            mplew.write(0);

            mplew.write(0);
            mplew.write(invType);
            if (item.getType() == 1) {
                mplew.writeShort(pos);
            } else {
                mplew.write(pos);
            }
            PacketHelper.addItemInfo(mplew, item, chr);
            if (pos < 0) {
                mplew.write(1);
            }

            return mplew.getPacket();
        }

        public static byte[] updateEquippedItem(MapleCharacter chr, Equip eq, short pos) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(0);
            mplew.write(1);
            mplew.write(0);

            mplew.write(0);
            mplew.write(1);
            mplew.writeShort(pos);
            PacketHelper.addItemInfo(mplew, eq, chr);

            return mplew.getPacket();
        }

        public static byte[] scrolledItem(Item scroll, MapleInventoryType inv, Item item, boolean destroyed, boolean potential, boolean equipped) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(destroyed ? 2 : 3);
            mplew.write(0);

            mplew.write(scroll.getQuantity() > 0 ? 1 : 3);
            mplew.write(GameConstants.getInventoryType(scroll.getItemId()).getType());
            mplew.writeShort(scroll.getPosition());
            if (scroll.getQuantity() > 0) {
                mplew.writeShort(scroll.getQuantity());
            }

            mplew.write(3);
            mplew.write(inv.getType());
            mplew.writeShort(item.getPosition());
            if (!destroyed) {
                mplew.write(0);
                mplew.write(inv.getType());
                mplew.writeShort(item.getPosition());
                PacketHelper.addItemInfo(mplew, item);
            }
            if (!potential) {
                mplew.write(1);
            }
            if (equipped) {
                mplew.write(8);
            }

            return mplew.getPacket();
        }

        public static byte[] moveAndUpgradeItem(MapleInventoryType type, Item item, short oldpos, short newpos, MapleCharacter chr) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(3);
            mplew.write(0);

            mplew.write(GameConstants.isInBag(newpos, type.getType()) ? 7 : 3);
            mplew.write(type.getType());
            mplew.writeShort(oldpos);

            mplew.write(0);
            mplew.write(1);
            mplew.writeShort(oldpos);
            PacketHelper.addItemInfo(mplew, item, chr);

            mplew.write(2);
            mplew.write(type.getType());
            mplew.writeShort(oldpos);
            mplew.writeShort(newpos);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] dropInventoryItem(MapleInventoryType type, short src) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(1);
            mplew.write(0);

            mplew.write(3);
            mplew.write(type.getType());
            mplew.writeShort(src);
            if (src < 0) {
                mplew.write(1);
            }

            return mplew.getPacket();
        }

        public static byte[] dropInventoryItemUpdate(MapleInventoryType type, Item item) {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(1);
            mplew.write(0);

            mplew.write(1);
            mplew.write(type.getType());
            mplew.writeShort(item.getPosition());
            mplew.writeShort(item.getQuantity());

            return mplew.getPacket();
        }

        public static byte[] getInventoryFull() {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(1);
            mplew.write(0);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] getInventoryStatus() {
            if (ServerConfig.LOG_PACKETS) {
                System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
            }
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
            mplew.write(0);
            mplew.write(0);
            mplew.write(0);

            return mplew.getPacket();
        }

        public static byte[] getSlotUpdate(byte invType, byte newSlots) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.INVENTORY_GROW.getValue());
            mplew.write(invType);
            mplew.write(newSlots);

            return mplew.getPacket();
        }

        public static byte[] getShowInventoryFull() {
            return CWvsContext.InfoPacket.getShowInventoryStatus(255);
        }

        public static byte[] showItemUnavailable() {
            return CWvsContext.InfoPacket.getShowInventoryStatus(254);
        }
    }

    public static byte[] updateHyperSp(int mode, int remainSp) {
        return updateSpecialStat("hyper", 0x1C, mode, remainSp);
    }

    public static byte[] updateSpecialStat(String stat, int array, int mode, int amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPECIAL_STAT.getValue());
        mplew.writeMapleAsciiString(stat);
        mplew.writeInt(array);
        mplew.writeInt(mode);
        mplew.write(1);
        mplew.writeInt(amount);

        return mplew.getPacket();
    }

    public static byte[] updateSpecialStat(String stat, int array, int mode, boolean is, int amount) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPECIAL_STAT.getValue());
        mplew.writeMapleAsciiString(stat);
        mplew.writeInt(array);
        mplew.writeInt(mode);
        mplew.write(is ? 1 : 0);
        mplew.writeInt(amount);

        return mplew.getPacket();
    }

    public static byte[] updateMaplePoint(int maplepoints) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MAPLE_POINT.getValue());
        mplew.writeInt(maplepoints);

        return mplew.getPacket();
    }

    public static byte[] updateCrowns(int[] titles) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.EVENT_CROWN.getValue());
        for (int i = 0; i < 5; i++) {
            mplew.writeMapleAsciiString("");
            if (titles.length < i + 1) {
                mplew.write(-1);
            } else {
                mplew.write(titles[i]);
            }
        }

        return mplew.getPacket();
    }

    public static byte[] magicWheel(int type, List<Integer> items, String data, int endSlot) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MAGIC_WHEEL.getValue());
        mplew.write(type);
        switch (type) {
            case 3:
                mplew.write(items.size());
                for (int item : items) {
                    mplew.writeInt(item);
                }
                mplew.writeMapleAsciiString(data); // nexon encrypt the item and then send the string
                mplew.write(endSlot);
                break;
            case 5:
                //<Character Name> got <Item Name>.
                break;
            case 6:
                //You don't have a Magic Gachapon Wheel in your Inventory.
                break;
            case 7:
                //You don't have any Inventory Space.\r\n You must have 2 or more slots available\r\n in each of your tabs.
                break;
            case 8:
                //Please try this again later.
                break;
            case 9:
                //Failed to delete Magic Gachapon Wheel item.
                break;
            case 0xA:
                //Failed to receive Magic Gachapon Wheel item.
                break;
            case 0xB:
                //You cannot move while Magic Wheel window is open.
                break;
        }

        return mplew.getPacket();
    }

    public static class Reward {

        public static byte[] receiveReward(int id, byte mode, int quantity) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.REWARD.getValue());
            mplew.write(mode); // mode
            switch (mode) { // mode
                case 0x0A:
                    mplew.writeInt(0);
                    break;
                case 0x0B:
                    mplew.writeInt(id);
                    mplew.writeInt(quantity); //quantity
                    //Popup: You have received the Maple Points.\r\n( %d maple point )
                    break;
                case 0x0C:
                    mplew.writeInt(id);
                    //Popup You have received the Game item.
                    break;
                case 0x0E:
                    mplew.writeInt(id);
                    mplew.writeInt(quantity); //quantity
                    //Popup: You have received the Mesos.\r\n( %d meso )
                    break;
                case 0x0F:
                    mplew.writeInt(id);
                    mplew.writeInt(quantity); //quantity
                    //Popup: You have received the Exp.\r\n( %d exp )
                    break;
                case 0x14:
                    //Popup: Failed to receive the Maple Points.
                    break;
                case 0x15:
                    mplew.write(0);
                    //Popup: Failed to receive the Game Item.
                    break;
                case 0x16:
                    mplew.write(0);
                    //Popup: Failed to receive the Game Item.
                    break;
                case 0x17:
                    //Popup: Failed to receive the Mesos.
                    break;
                case 0x18:
                    //Popup: Failed to receive the Exp.
                    break;
                case 0x21:
                    mplew.write(0); //66
                    //No inventory space
                    break;
            }

            return mplew.getPacket();
        }

        public static byte[] updateReward(int id, byte mode, List<MapleReward> rewards, int option) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.REWARD.getValue());
            mplew.write(mode);
            switch (mode) {
                case 0x09:
                    mplew.writeInt(rewards.size());
                    if (rewards.size() > 0) {
                        for (MapleReward reward : rewards) {
                            boolean empty = reward.getId() < 1;
                            mplew.writeInt(empty ? 0 : reward.getId()); // 0 = blank 1+ = gift
                            if (!empty) {
                                if ((option & 1) != 0) {
                                    mplew.writeLong(reward.getReceiveDate());
                                    mplew.writeLong(reward.getExpireDate());
                                }
                                if ((option & 2) != 0) { //nexon do here a3 & 2 when a3 is 9
                                    mplew.writeInt(0);
                                    mplew.writeInt(0);
                                    mplew.writeInt(0);
                                    mplew.writeInt(0);
                                    mplew.writeInt(0);
                                    mplew.writeInt(0);
                                    mplew.writeMapleAsciiString("");
                                    mplew.writeMapleAsciiString("");
                                    mplew.writeMapleAsciiString("");
                                }
                                mplew.writeInt(reward.getType()); // 3 = 楓葉點數 4 = 楓幣 5 = 經驗值
                                mplew.writeInt(reward.getItem());
                                mplew.writeInt(/*itemQ*/reward.getItem() > 0 ? 1 : 0); // item quantity (?)
                                mplew.writeInt(0);
                                mplew.writeLong(0L);
                                mplew.writeInt(0);
                                mplew.writeInt(reward.getMaplePoints());
                                mplew.writeInt(reward.getMeso());
                                mplew.writeInt(reward.getExp());
                                mplew.writeInt(0);
                                mplew.writeInt(0);
                                mplew.writeMapleAsciiString("");
                                mplew.writeMapleAsciiString("");
                                mplew.writeMapleAsciiString("");
                                if ((option & 4) != 0) {
                                    mplew.writeMapleAsciiString("");
                                }
                                if ((option & 8) != 0) {
                                    mplew.writeMapleAsciiString(reward.getDesc());
                                }
                                mplew.writeInt(0);
                                mplew.writeInt(0);
                                mplew.writeInt(0);
                            }
                        }
                    }
                    break;
                case 0x0B: // 獲得楓點。\r\n(%d 楓點)
                    mplew.writeInt(id);
                    mplew.writeInt(0); // 楓葉點數數量
                    break;
                case 0x0C: // 獲得此道具。
                    mplew.writeInt(id);
                    break;
                case 0x0E: // 獲得楓幣。\r\n(%d 楓幣)
                    mplew.writeInt(id);
                    mplew.writeInt(0); // 楓幣數量
                    break;
                case 0x0F: // 獲得經驗值。\r\n(%d 經驗值)
                    mplew.writeInt(id);
                    mplew.writeInt(0); // 經驗值數量
                    break;
                case 0x14: // 楓點領取失敗。
                    break;
                case 0x15: // 道具領取失敗。
                    mplew.write(0);
                    break;
                case 0x16: // 現金道具領取失敗。
                    mplew.write(0);
                    break;
                case 0x17: // 楓幣領取失敗。
                    break;
                case 0x18: // 經驗值領取失敗。
                    break;
                case 0x21: // 獎勵領取失敗。請再試一次。
                    mplew.write(0);
                    break;
            }

            return mplew.getPacket();
        }
    }

    public static class Enchant {

        public static byte[] getEnchantList(MapleClient c, Equip item, ArrayList<EchantScroll> scrolls, boolean feverTime) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EQUIPMENT_ENCHANT.getValue());
            mplew.write(0x32);
            mplew.write(feverTime ? 1 : 0);
            mplew.write(scrolls.size());
            for (EchantScroll scroll : scrolls) {
                mplew.writeInt(scroll.getViewType());
                mplew.writeMapleAsciiString(scroll.getName());
                mplew.writeInt(scroll.getMask());
                if (scroll.getMask() > 0) {
                    for (int i : scroll.getValues()) {
                        mplew.writeInt(i);
                    }
                }
                mplew.writeInt(scroll.getCost());
            }

            return mplew.getPacket();
        }

        public static byte[] getStarForcePreview(MapleClient c, final Map<EchantEquipStat, Integer> stats, long meso, int successprop, int destroyprop, boolean canfall, boolean chancetime) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EQUIPMENT_ENCHANT.getValue());
            mplew.write(0x34);
            mplew.write(canfall ? 1 : 0); //失敗是否降級 ? 1 : 0
            mplew.writeLong(meso); //消耗量
            mplew.writeLong(9);
            mplew.writeLong(50);
            mplew.writeInt(successprop); //成功概率
            mplew.writeInt(destroyprop);//損壞概率
            mplew.write(chancetime);//chancetime ? 1 : 0
            int equipStats = 0;
            for (EchantEquipStat stat : stats.keySet()) {
                if (!stats.containsKey(stat) || stats.get(stat) <= 0) {
                    continue;
                }
                equipStats |= stat.getValue();
            }
            mplew.writeInt(equipStats);
            if (equipStats != 0) {
                for (EchantEquipStat stat : EchantEquipStat.values()) {
                    if (!stats.containsKey(stat) || stats.get(stat) <= 0) {
                        continue;
                    }
                    mplew.writeInt(stats.get(stat));
                }
            }

            return mplew.getPacket();
        }

        public static byte[] getStarForceMiniGame() {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EQUIPMENT_ENCHANT.getValue());
            mplew.write(0x35);
            mplew.write(0);
            mplew.write(HexTool.getByteArrayFromHexString("00 C4 2F 63"));

            return mplew.getPacket();
        }

        public static byte[] getEnchantResult(Equip item, int result, int scrollnumber, MapleClient c) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EQUIPMENT_ENCHANT.getValue());
            mplew.write(0x64);
            mplew.write(0);
            mplew.writeInt(result); //0 - 失敗, 1 - 成功, 2 - 損壞
            PacketHelper.addItemInfo(mplew, item, null);
            if (result == 2) {
                mplew.writeShort(0);
            } else {
                PacketHelper.addItemInfo(mplew, item, null);
            }

            return mplew.getPacket();
        }

        public static byte[] getStarForceResult(MapleCharacter chr, Item oldItem, Item newItem, byte result) {
            MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

            mplew.writeShort(SendPacketOpcode.EQUIPMENT_ENCHANT.getValue());
            mplew.write(0x65);
            mplew.write(result); //0 - 下降, 1 - 成功, 2 - 損壞, 3 - 失敗
            mplew.writeInt(0);
            PacketHelper.addItemInfo(mplew, oldItem, null);
            if (result == 2) {
                mplew.writeShort(0);
            } else {
                PacketHelper.addItemInfo(mplew, newItem, null);
            }

            return mplew.getPacket();
        }
    }
}
