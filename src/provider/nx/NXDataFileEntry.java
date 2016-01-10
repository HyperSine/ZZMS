package provider.nx;

import provider.MapleDataFileEntry;
import provider.pkgnx.NXNode;

/**
 * @author Aaron
 * @version 1.0
 * @since 6/8/13
 */
public class NXDataFileEntry extends NXDataEntry implements MapleDataFileEntry {

    private int offset;

    public NXDataFileEntry(NXNode node, NXData parent) {
        super(node, parent);
    }

    @Override
    public int getOffset() {
        return offset;
    }

    @Override
    public void setOffset(int offset) {
        this.offset = offset;
    }
}
