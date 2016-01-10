
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
package server.maps;

import client.MapleClient;
import tools.packet.CField;

public class MapleRune extends MapleMapObject {
    private final int type, posX, posY;
    private MapleMap map;

    public MapleRune(final int type, final int posX, final int posY, final MapleMap map) {
        this.type = type;
        this.posX = posX;
        this.posY = posY;
        this.map = map;
    }
    
    public final void setMap(final MapleMap map) {
	this.map = map;
    }

    public final MapleMap getMap() {
	return map;
    }
    
    public final int getRuneType() {
        return type;
    }
    
    public final int getPositionX() {
        return posX;
    }
    
    public final int getPositionY() {
        return posY;
    }

    @Override
    public final void sendSpawnData(final MapleClient client) {
        client.getSession().write(CField.RunePacket.spawnRune(this, false));
    }

    @Override
    public final void sendDestroyData(final MapleClient client) {
        client.getSession().write(CField.RunePacket.removeRune(this, client.getPlayer()));
    }

    @Override
    public final MapleMapObjectType getType() {
        return MapleMapObjectType.RUNE;
    }
}
