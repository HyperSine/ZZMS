package handling.channel.handler;

import client.MapleCharacter;
import client.MapleJob;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import server.maps.AnimatedMapleMapObject;
import server.movement.*;
import tools.FileoutputUtil;
import tools.HexTool;
import tools.data.LittleEndianAccessor;

public class MovementParse {

    // 1 = 玩家, 2 = 怪物, 3 = 寵物, 4 = 召喚獸, 5 = 龍神
    public static List<LifeMovementFragment> parseMovement(final LittleEndianAccessor lea, final int kind) {
        return parseMovement(lea, kind, null);
    }

    public static List<LifeMovementFragment> parseMovement(final LittleEndianAccessor lea, final int kind, MapleCharacter chr) {
        final List<LifeMovementFragment> res = new ArrayList<>();
        final byte numCommands = lea.readByte();

        for (byte i = 0; i < numCommands; i++) {
            final byte command = lea.readByte();
            switch (command) {
                case 0:
                case 8:
                case 15:
                case 16:
                case 18:
                case 65:
                case 66:
                case 67: {
                    final short xpos = (short) lea.readShort();
                    final short ypos = lea.readShort();
                    final short xwobble = lea.readShort();
                    final short ywobble = lea.readShort();
                    final short unk = lea.readShort();
                    short fh = 0;
                    if (command == 15 || command == 16) {
                        fh = lea.readShort();
                    }
                    short xoffset = lea.readShort();
                    short yoffset = lea.readShort();


                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final byte wui = lea.readByte();
                    final StaticLifeMovement mov = new StaticLifeMovement(command, new Point(xpos, ypos), duration, newstate, unk);
                    mov.setUnk(unk);
                    mov.setFh(fh);
                    mov.setPixelsPerSecond(new Point(xwobble, ywobble));
                    mov.setOffset(new Point(xoffset, yoffset));
                    mov.setWui(wui);
                    res.add(mov);
                    break;
                }
                case 54:
                case 64:
                case 79: {
                    final short xpos = (short) lea.readShort();
                    final short ypos = lea.readShort();
                    final short xwobble = lea.readShort();
                    final short ywobble = lea.readShort();
                    final short unk = lea.readShort();


                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final byte wui = lea.readByte();
                    final StaticLifeMovement mov = new StaticLifeMovement(command, new Point(xpos, ypos), duration, newstate, unk);
                    mov.setUnk(unk);
                    mov.setPixelsPerSecond(new Point(xwobble, ywobble));
                    mov.setWui(wui);
                    res.add(mov);
                    break;
                }
                case 1:
                case 2:
                case 17:
                case 20:
                case 21:
                case 23:
                case 60:
                case 61:
                case 62:
                case 63: {
                    final short xwobble = lea.readShort();
                    final short ywobble = lea.readShort();
                    short fh = 0;
                    if (command == 20 || command == 21) {
                        fh = lea.readShort();
                    }


                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final byte wui = lea.readByte();
                    final StaticLifeMovement mov = new StaticLifeMovement(command, new Point(0, 0), duration, newstate, fh);
                    mov.setPixelsPerSecond(new Point(xwobble, ywobble));
                    mov.setFh(fh);
                    mov.setWui(wui);
                    res.add(mov);
                    break;
                }
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                case 55:
                case 56:
                case 58:
                case 59:
                case 68:
                case 69:
                case 70:
                case 72:
                case 77: {
                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final byte wui = lea.readByte();
                    final StaticLifeMovement mov = new StaticLifeMovement(command, new Point(0, 0), duration, newstate, 0);
                    mov.setWui(wui);
                    res.add(mov);
                    break;
                }
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 9:
                case 10:
                case 11:
                case 13:
                case 25:
                case 26:
                case 50:
                case 51:
                case 52:
                case 57:
                case 74:
                case 75:
                case 76: {
                    final short xpos = (short) lea.readShort();
                    final short ypos = lea.readShort();
                    final short unk = lea.readShort();


                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final byte wui = lea.readByte();
                    final StaticLifeMovement mov = new StaticLifeMovement(command, new Point(xpos, ypos), duration, newstate, unk);
                    mov.setUnk(unk);
                    mov.setWui(wui);
                    res.add(mov);
                    break;
                }
                case 14: {
                    final short xpos = 0;
                    final short ypos = 0;
                    final short xwobble = lea.readShort();
                    final short ywobble = lea.readShort();
                    final short fh = lea.readShort();


                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final byte wui = lea.readByte();
                    final StaticLifeMovement mov = new StaticLifeMovement(command, new Point(xpos, ypos), duration, newstate, fh);
                    mov.setPixelsPerSecond(new Point(xwobble, ywobble));
                    mov.setFh(fh);
                    mov.setWui(wui);
                    res.add(mov);
                    break;
                }
                case 22: {
                    final short xpos = (short) lea.readShort();
                    final short ypos = lea.readShort();
                    final short xwobble = lea.readShort();
                    final short ywobble = lea.readShort();


                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final byte wui = lea.readByte();
                    final StaticLifeMovement mov = new StaticLifeMovement(command, new Point(xpos, ypos), duration, newstate, 0);
                    mov.setPixelsPerSecond(new Point(xwobble, ywobble));
                    mov.setWui(wui);
                    res.add(mov);
                    break;
                }
                case 12: {
                    final byte newstate = 0;
                    final short duration = 0;
                    final int wui = lea.readByte();
                    final StaticLifeMovement mov = new StaticLifeMovement(command, new Point(0, 0), duration, newstate, 0);
                    mov.setWui(wui);
                    res.add(mov);
                    break;
                }
                default:
                    final byte newstate = lea.readByte();
                    final short duration = lea.readShort();
                    final int wui = lea.readByte();
                    final StaticLifeMovement mov = new StaticLifeMovement(command, new Point(0, 0), duration, newstate, 0);
                    mov.setWui(wui);
                    res.add(mov);

                    System.out.println("未知的移動類型: 0x" + HexTool.toString(command) + " - ( " + command + " )");
                    if (chr.isShowErr()) {
                        chr.showInfo("移動", true, "未知的移動類型: 0x" + HexTool.toString(command) + " - ( " + command + " )");
                    }
                    String moveMsg = "";
                    if (kind == 1) {
                        moveMsg = "玩家";
                    } else if (kind == 2) {
                        moveMsg = "怪物";
                    } else if (kind == 3) {
                        moveMsg = "寵物";
                    } else if (kind == 4) {
                        moveMsg = "召喚獸";
                    } else if (kind == 5) {
                        moveMsg = "龍";
                    } else if (kind == 6) {
                        moveMsg = "攻擊怪物";
                    }
                    FileoutputUtil.log(FileoutputUtil.Movement_Log, moveMsg + "(" + chr.getName() + ") 職業：" + MapleJob.getName(MapleJob.getById(chr.getJob())) + "(" + chr.getJob() + ")  未知移動封包 剩餘次數: " + (numCommands - res.size()) + " 移動類型: 0x" + HexTool.toString(command) + ", 封包: " + lea.toString(true));
                    return null;
            }
            if (kind == 6) {
                if (chr.isShowErr()) {
                    chr.showInfo("移動", true, "攻擊怪物" + "(" + chr.getName() + ") 職業：" + MapleJob.getName(MapleJob.getById(chr.getJob())) + "移動類型: 0x" + HexTool.toString(command));
                }
                FileoutputUtil.log(FileoutputUtil.Movement_Log, "攻擊怪物" + "(" + chr.getName() + ") 職業：" + MapleJob.getName(MapleJob.getById(chr.getJob())) + "移動類型: 0x" + HexTool.toString(command) + ", 封包: " + lea.toString(true));
            }
        }

        double skip = lea.readByte();
        skip = Math.ceil(skip / 2.0D);
        lea.skip((int) skip);
        if (numCommands != res.size()) {
            System.out.println("循環次數[" + numCommands + "]和實際上獲取的循環次數[" + res.size() + "]不符");
            if (chr.isShowErr()) {
                chr.showInfo("移動", true, "循環次數[" + numCommands + "]和實際上獲取的循環次數[" + res.size() + "]不符");
            }
            FileoutputUtil.log(FileoutputUtil.Movement_Log, "循環次數[" + numCommands + "]和實際上獲取的循環次數[" + res.size() + "]不符 " + "(" + chr.getName() + ") 職業：" + MapleJob.getName(MapleJob.getById(chr.getJob())) + "(" + chr.getJob() + "移動封包 剩餘次數: " + (numCommands - res.size()) + "  封包: " + lea.toString(true));
            return null; // Probably hack
        }
        return res;
    }

    public static void updatePosition(final List<LifeMovementFragment> movement, final AnimatedMapleMapObject target, final int yoffset) {
        if (movement == null) {
            return;
        }
        for (final LifeMovementFragment move : movement) {
            if (move instanceof LifeMovement) {
                if (move instanceof StaticLifeMovement) {
                    final Point position = ((LifeMovement) move).getPosition();
                    if (position != null) {
                        position.y += yoffset;
                        target.setPosition(position);
                    }
                }
                target.setStance(((LifeMovement) move).getNewstate());
                target.setNewFh(((LifeMovement) move).getNewFh());
            }
        }
    }
}
