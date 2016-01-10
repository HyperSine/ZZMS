package provider.nx;

import provider.MapleDataEntity;
import provider.MapleDataEntry;
import provider.pkgnx.NXNode;

/**
 * @author Aaron
 * @version 1.0
 * @since 6/8/13
 */
public class NXDataEntry implements MapleDataEntry {

    private final NXNode node;
    private final NXData parent;

    private final String name;
    private final int size;
    private final int checksum;
    private int offset;

    public NXDataEntry(NXNode node, NXData parent) {
        super();
        this.name = node.getName();
        this.size = 0;
        this.checksum = 0;
        this.node = node;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return node.getName();
    }

    @Override
    public MapleDataEntity getParent() {
        return new NXDataEntity(parent.getNode(), parent.getParentAsNX());
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getChecksum() {
        return checksum; 
    }

    @Override
    public int getOffset() {
        return offset;
    }
}
