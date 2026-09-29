package legacy;

import storage.StorageBackend;
import storage.StorageException;

public class LegacyArchiveAdapter implements StorageBackend {

    private final OldArchiveSystem archiveSystem;

    public LegacyArchiveAdapter(OldArchiveSystem archiveSystem) {
        this.archiveSystem = archiveSystem;
    }

    @Override
    public void save(int id, String data) {
        int result = archiveSystem.archive(data, id);

        if (result == -1) {
            throw new StorageException("Invalid artifact id");
        }

        if (result == -2) {
            throw new StorageException("Legacy archive is unavailable");
        }

        if (result != 0) {
            throw new StorageException("Unknown legacy archive error");
        }
    }
}