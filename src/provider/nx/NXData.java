package provider.nx;

import provider.MapleData;
import provider.MapleDataEntity;
import provider.wz.MapleDataType;
import provider.pkgnx.NXNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import provider.pkgnx.nodes.NXAudioNode;
import provider.pkgnx.nodes.NXBitmapNode;
import provider.pkgnx.nodes.NXDoubleNode;
import provider.pkgnx.nodes.NXLongNode;
import provider.pkgnx.nodes.NXNullNode;
import provider.pkgnx.nodes.NXPointNode;
import provider.pkgnx.nodes.NXStringNode;

/**
 * @author Aaron
 * @version 1.0
 * @since 6/8/13
 */
public class NXData implements MapleData {

    private final NXNode node;
    private final NXData parent;

    public NXData(NXNode node, NXData parent) {
        this.node = node;
        this.parent = parent;
    }

    public NXNode getNode() {
        return node;
    }

    public NXData getParentAsNX() {
        return new NXData(parent.getNode(), parent.getParentAsNX());
    }

    @Override
    public String getName() {
        return node.getName();
    }

    @Override
    public MapleDataEntity getParent() {
        return new NXDataEntity(node, parent);
    }

    @Override
    public MapleDataType getType() {
        if (node instanceof NXBitmapNode) {
            return MapleDataType.CANVAS;
        } else if (node instanceof NXPointNode) {
            return MapleDataType.VECTOR;
        } else if (node instanceof NXNullNode) {
            return MapleDataType.NONE;
        } else if (node instanceof NXAudioNode) {
            return MapleDataType.SOUND;
        } else if (node instanceof NXLongNode) {
            return MapleDataType.INT;
        } else if (node instanceof NXDoubleNode) {
            return MapleDataType.DOUBLE;
        } else if (node instanceof NXStringNode) {
            return MapleDataType.STRING;
        } else {
            return MapleDataType.UNKNOWN_TYPE;
        }
    }

    @Override
    public List<MapleData> getChildren() {
        List<MapleData> md = new ArrayList<>();
        for (NXNode child : node) {
            md.add(new NXData(child, new NXData(node, parent)));
        }
        return md;
    }

    @Override
    public MapleData getChildByPath(String path) {
        NXNode cursor = node;
        NXData parent = null;
        for (String child : path.split("/")) {
            if (cursor == null) {
                return null;
            }
            try {
                cursor.getChild(child).get();
            } catch (Exception e) {
                return null;
            }
            parent = new NXData(cursor, parent);
            cursor = cursor.getChild(child);
        }
        return new NXData(cursor, parent);
    }

    @Override
    public Object getData() {
        try {
            Object tmp = node.get();
            if (node instanceof NXNullNode) {
                return tmp;
            } else if (node instanceof NXLongNode) {
                return Integer.valueOf(tmp.toString());
            } else if (node instanceof NXDoubleNode) {
                return Double.valueOf(tmp.toString());
            } else if (node instanceof NXStringNode) {
                return tmp.toString();
            } else {
                return tmp.toString();
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Iterator<MapleData> iterator() {
        return new Iterator<MapleData>() {
            @Override
            public boolean hasNext() {
                return node.iterator().hasNext();
            }

            @Override
            public MapleData next() {
                return new NXData(node.iterator().next(), new NXData(node, parent));
            }

            @Override
            public void remove() {
                node.iterator().remove();
            }
        };
    }
}
