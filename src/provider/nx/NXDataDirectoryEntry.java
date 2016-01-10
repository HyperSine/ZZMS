package provider.nx;

import provider.MapleDataDirectoryEntry;
import provider.MapleDataEntity;
import provider.MapleDataEntry;
import provider.MapleDataFileEntry;
import provider.pkgnx.NXNode;
import provider.pkgnx.nodes.NXNullNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黯淡
 * @version 3.1
 * @since 6/5/15
 */
public class NXDataDirectoryEntry implements MapleDataDirectoryEntry {

    private final NXNode node;
    private final NXData parent;

    private final List<MapleDataDirectoryEntry> subdirs = new ArrayList<>();
    private final List<MapleDataFileEntry> files = new ArrayList<>();
    private final Map<String, MapleDataEntry> entries = new HashMap<>();

    public NXDataDirectoryEntry(NXNode node, NXData parent) {
        this.node = node;
        this.parent = parent;
    }

    public void addDirectory(MapleDataDirectoryEntry dir) {
        subdirs.add(dir);
        entries.put(dir.getName(), dir);
    }

    public void addFile(MapleDataFileEntry fileEntry) {
        files.add(fileEntry);
        entries.put(fileEntry.getName(), fileEntry);
    }

    @Override
    public List<MapleDataDirectoryEntry> getSubdirectories() {
        return Collections.unmodifiableList(subdirs);
    }

    @Override
    public List<MapleDataFileEntry> getFiles() {
        return Collections.unmodifiableList(files);
    }

    @Override
    public MapleDataEntry getEntry(String name) {
        return entries.get(name);
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
        return node.getChildCount();
    }

    @Override
    public int getChecksum() {
        return -1; // NOT USED ANYWHERE IN THE SOURCE.
    }

    @Override
    public int getOffset() {
        int i = 0;
        for (NXNode child : node) {
            i++;
            if (child == node) {
                break;
            }
        }
        return (int) (node.getFile().getHeader().getNodeOffset() + (20 * i));
    }
}
