package tools.packet;

import client.MapleCharacter;
import client.MapleStat;
import client.inventory.Item;
import client.inventory.MaplePet;
import constants.ServerConfig;
import handling.SendPacketOpcode;
import java.awt.Point;
import java.util.List;
import server.movement.LifeMovementFragment;
import tools.data.MaplePacketLittleEndianWriter;

public class PetPacket {

    public static final byte[] updatePet(MaplePet pet, Item item, boolean active) {
        if (ServerConfig.LOG_PACKETS) {
            System.out.println("調用位置: " + new java.lang.Throwable().getStackTrace()[0]);
        }
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.INVENTORY_OPERATION.getValue());
        mplew.write(0);
        mplew.write(2);
        mplew.write(0);
        mplew.write(3);
        mplew.write(5);
        mplew.writeShort(pet.getInventoryPosition());
        mplew.write(0);
        mplew.write(5);
        mplew.writeShort(pet.getInventoryPosition());
        mplew.write(3);
        mplew.writeInt(pet.getPetItemId());
        mplew.write(1);
        mplew.writeLong(pet.getUniqueId());
        PacketHelper.addPetItemInfo(mplew, item, pet, active);
        return mplew.getPacket();
    }

    public static final byte[] showPet(MapleCharacter chr, MaplePet pet, boolean remove, boolean hunger) {
        return showPet(chr, pet, remove, hunger, false);
    }

    public static final byte[] showPet(MapleCharacter chr, MaplePet pet, boolean remove, boolean hunger, boolean show) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(show ? SendPacketOpcode.SHOW_PET.getValue() : SendPacketOpcode.SPAWN_PET.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(chr.getPetIndex(pet));
        mplew.write(remove ? 0 : 1);
        mplew.write(hunger ? 1 : 0);
        if (!remove) {
            addPetInfo(mplew, chr, pet, false);
        }

        return mplew.getPacket();
    }

    public static final byte[] removePet(int cid, int index) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.SPAWN_PET.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(index);
        mplew.writeShort(0);

        return mplew.getPacket();
    }

    public static byte[] movePet(int cid, int pid, byte slot, Point pos, List<LifeMovementFragment> moves) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.MOVE_PET.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(slot);
        mplew.writeInt(0);
        mplew.writePos(pos);
        mplew.writeInt(pid);
        PacketHelper.serializeMovementList(mplew, moves);

        return mplew.getPacket();
    }

    public static byte[] petChat(int cid, int un, String text, byte slot) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PET_CHAT.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(slot);
        mplew.write(un);
        mplew.write(0);
        mplew.writeMapleAsciiString(text);

        return mplew.getPacket();
    }

    public static byte[] petColor(int cid, byte slot, int color) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PET_COLOR.getValue());
        mplew.writeInt(cid);
        mplew.write(slot);
        mplew.writeInt(color);

        return mplew.getPacket();
    }

    public static final byte[] commandResponse(int cid, byte command, byte slot, boolean success, boolean food) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PET_COMMAND.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(slot);
        mplew.write(command == 1 ? 2 : 1);
        if (command != 1) {
            mplew.write(command);
            mplew.write(success ? 1 : 0);
            mplew.write(0);
        } else {
            mplew.write(1);
            mplew.writeInt(0);
        }

        return mplew.getPacket();
    }

    public static final byte[] petSize(int cid, byte slot, short size) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PET_SIZE.getValue());
        mplew.writeInt(cid);
        mplew.writeInt(slot);
        mplew.writeShort(size);
        mplew.write(0);
        mplew.write(0);

        return mplew.getPacket();
    }

    public static final byte[] showPetUpdate(MapleCharacter chr, int uniqueId, byte index) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.PET_EXCEPTION_LIST.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeInt(index);
        mplew.writeLong(uniqueId);
        mplew.write(0); // > 0 => + [Int]
       
        return mplew.getPacket();
    }

    public static byte[] petStatUpdate(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.UPDATE_STATS.getValue());
        mplew.write(0);
        mplew.writeLong(MapleStat.PET.getValue());
        byte count = 0;
        for (MaplePet pet : chr.getPets()) {
            if (pet.getSummoned()) {
                mplew.writeLong(pet.getUniqueId());
                count = (byte) (count + 1);
            }
        }        
        while (count < 3) {
            mplew.writeZeroBytes(8);
            count = (byte) (count + 1);
        }
        mplew.write(0);
        mplew.writeShort(0);
        mplew.writeZeroBytes(100);

        return mplew.getPacket();
    }

    public static void addPetInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, MaplePet pet, boolean showpet) {
        if (showpet) {
            mplew.write(1);
            mplew.writeInt(chr.getPetIndex(pet));
        }
        mplew.writeInt(pet.getPetItemId());
        mplew.writeMapleAsciiString(pet.getName());
        mplew.writeLong(pet.getUniqueId());
        mplew.writePos(pet.getPos());
        mplew.write(pet.getStance());
        mplew.writeShort(pet.getFh());
        mplew.writeInt(-1);
        mplew.writeShort(100);
        mplew.write(0);
        mplew.write(0);
    }

    public static void addPet(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, MaplePet pet) {
        if (pet.getPetItemId() == 0) {
            mplew.writeInt(0);
        } else {
            mplew.writeInt(pet.getPetItemId());
        }
    }
    
    public static byte[] showPetPickUpMsg(boolean canPickup, int pets) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        mplew.writeShort(SendPacketOpcode.USE_CASH_PET_SKILL.getValue());
        mplew.write(canPickup ? 1 : 0);
        mplew.write(pets);

        return mplew.getPacket();
    }
}
