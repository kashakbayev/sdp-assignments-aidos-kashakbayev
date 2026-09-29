package storage;

import legacy.LegacyArchiveAdapter;
import legacy.OldArchiveSystem;
import model.Artifact;

public class StorageSelector {

    private final OldArchiveSystem archiveSystem;

    public StorageSelector(OldArchiveSystem archiveSystem) {
        this.archiveSystem = archiveSystem;
    }

    public StorageBackend select(Artifact artifact) {
        String source = artifact.getSource().toLowerCase();

        if (source.equals("legacy")) {
            return new LegacyArchiveAdapter(archiveSystem);
        }

        if (source.equals("museum")) {
            return new DatabaseStorage();
        }

        return new JsonStorage();
    }
}