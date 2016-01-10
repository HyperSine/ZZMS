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
package handling.world;

public enum PartyOperation {

    JOIN, //加入隊伍
    LEAVE, //離開隊伍
    EXPEL, //強制退出
    DISBAND, //解散隊伍
    SILENT_UPDATE, //更新隊伍
    INFO_UPDATE, //更新訊息
    LOG_ONOFF, //
    CHANGE_LEADER, //委任隊長
    CHANGE_LEADER_DC;//委任隊長(斷線)
}
