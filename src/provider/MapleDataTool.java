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
package provider;

import java.awt.Point;
import java.awt.image.BufferedImage;
import provider.wz.MapleDataType;

public class MapleDataTool {

    public static String getString(MapleData data) {
        if (data.getData() instanceof String) {
            return ((String) data.getData());
        }
        return String.valueOf(getLong(data));
    }

    public static String getString(String path, MapleData data) {
        return getString(data.getChildByPath(path));
    }

    public static String getString(MapleData data, String def) {
        try {
            return getString(data);
        } catch (Exception ex) {
            return def;
        }
    }

    public static String getString(String path, MapleData data, String def) {
        try {
            return getString(data.getChildByPath(path));
        } catch (Exception ex) {
            return def;
        }
    }

    public static double getDouble(MapleData data) {
        double value;
        if (data.getData() instanceof Float) {
            value = (Float) data.getData();
        } else {
            value = (Double) data.getData();
        }
        return value;
    }

    public static float getFloat(MapleData data) {
        return (float) getDouble(data);
    }

    public static float getFloat(MapleData data, float def) {
        try {
            return getFloat(data);
        } catch (Exception ex) {
            return def;
        }
    }

    public static short getShort(MapleData data) {
        return (Short) data.getData();
    }

    public static int getInt(MapleData data) {
        int value;
        if (data.getData() instanceof Integer) {
            value = (Integer) data.getData();
        } else {
            value = getShort(data);
        }
        return value;
    }

    public static int getInt(String path, MapleData data) {
        return getInt(data.getChildByPath(path));
    }

    public static int getInt(MapleData data, int def) {
        try {
            return getInt(data);
        } catch (Exception ex) {
            return def;
        }
    }

    public static int getInt(String path, MapleData data, int def) {
        try {
            return getInt(data.getChildByPath(path));
        } catch (Exception ex) {
            return def;
        }
    }

    public static int getIntConvert(MapleData data) {
        if (data.getType() == MapleDataType.STRING) {
            String dd = getString(data);
            if (dd.endsWith("%")) {
                dd = dd.substring(0, dd.length() - 1);
            }
            return Integer.parseInt(dd);
        } else {
            return (int) getLong(data);
        }
    }

    public static int getIntConvert(String path, MapleData data) {
        return getIntConvert(data.getChildByPath(path));
    }

    public static int getIntConvert(MapleData d, int def) {
        try {
            return getIntConvert(d);
        } catch (Exception ex) {
            return def;
        }
    }

    public static int getIntConvert(String path, MapleData data, int def) {
        try {
            return getIntConvert(data.getChildByPath(path));
        } catch (Exception ex) {
            return def;
        }
    }

    public static long getLong(MapleData data) {
        long value;
        if (data.getData() instanceof Long) {
            value = (Long) data.getData();
        } else {
            value = getInt(data);
        }
        return value;
    }

    public static long getLong(String path, MapleData data) {
        return getLong(data.getChildByPath(path));
    }

    public static long getLong(MapleData data, long def) {
        try {
            return getLong(data);
        } catch (Exception ex) {
            return def;
        }
    }

    public static long getLong(String path, MapleData data, long def) {
        try {
            return getLong(data.getChildByPath(path));
        } catch (Exception ex) {
            return def;
        }
    }

    public static long getLongConvert(MapleData data) {
        if (data.getType() == MapleDataType.STRING) {
            String dd = getString(data);
            if (dd.endsWith("%")) {
                dd = dd.substring(0, dd.length() - 1);
            }
            return Long.parseLong(dd);
        } else {
            return getLong(data);
        }
    }

    public static long getLongConvert(String path, MapleData data) {
        return getLongConvert(data.getChildByPath(path));
    }

    public static long getLongConvert(MapleData d, long def) {
        try {
            return getLongConvert(d);
        } catch (Exception ex) {
            return def;
        }
    }

    public static long getLongConvert(String path, MapleData data, long def) {
        try {
            return getLongConvert(data.getChildByPath(path));
        } catch (Exception ex) {
            return def;
        }
    }

    public static BufferedImage getImage(MapleData data) {
        return ((MapleCanvas) data.getData()).getImage();
    }

    public static Point getPoint(MapleData data) {
        return ((Point) data.getData());
    }

    public static Point getPoint(String path, MapleData data) {
        return getPoint(data.getChildByPath(path));
    }

    public static Point getPoint(String path, MapleData data, Point def) {
        try {
            return getPoint(path, data);
        } catch (Exception ex) {
            return def;
        }
    }

    public static String getFullDataPath(MapleData data) {
        String path = "";
        MapleDataEntity myData = data;
        while (myData != null) {
            path = myData.getName() + "/" + path;
            myData = myData.getParent();
        }
        return path.substring(0, path.length() - 1);
    }
}
