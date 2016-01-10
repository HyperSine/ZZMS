package provider.nx;

import provider.MapleData;
import provider.MapleDataDirectoryEntry;
import provider.MapleDataProvider;
import provider.pkgnx.NXFile;
import provider.pkgnx.EagerNXFile;
import java.io.IOException;
import java.util.Iterator;
import provider.MapleDataFileEntry;
import provider.pkgnx.NXNode;

/**
 * @author 黯淡
 * @version 3.1
 * @since 6/5/15
 */
public class NXDataProvider implements MapleDataProvider {

    private final NXFile root;
    private final NXDataDirectoryEntry rootForNavigation;

    public NXDataProvider(String path) throws IOException {
        root = new EagerNXFile(path);
        rootForNavigation = new NXDataDirectoryEntry(root.getRoot(), null);
        fillMapleDataEntitysRoot(root, rootForNavigation);
    }

    private void fillMapleDataEntitysRoot(NXFile lroot, NXDataDirectoryEntry nxdir) {
        for (NXNode child : lroot.getRoot()) {
            String fileName = child.getName();
            if (child.getChildCount() > 0 && !fileName.endsWith(".img")) {
                NXDataDirectoryEntry mDDE = new NXDataDirectoryEntry(child, new NXData(child, null));
                nxdir.addDirectory(mDDE);
                fillMapleDataEntitys(child, mDDE);
            } else {
                nxdir.addFile(new NXDataFileEntry(child, new NXData(child, null)));
            }
        }
    }
    
    private void fillMapleDataEntitys(NXNode lroot, NXDataDirectoryEntry nxdir) {
        for (NXNode child : lroot) {
            String fileName = child.getName();
            if (child.getChildCount() > 0 && !fileName.endsWith(".img")) {
                NXDataDirectoryEntry mDDE = new NXDataDirectoryEntry(child, new NXData(child, null));
                nxdir.addDirectory(mDDE);
                fillMapleDataEntitys(child, mDDE);
            } else {
                nxdir.addFile(new NXDataFileEntry(child, new NXData(child, null)));
            }
        }
    }

    @Override
    public MapleData getData(String path) {
        try {
            NXData nxData = new NXData(root.resolve(path), null);
            return nxData;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public MapleDataDirectoryEntry getRoot() {
        return rootForNavigation;
    }
}
