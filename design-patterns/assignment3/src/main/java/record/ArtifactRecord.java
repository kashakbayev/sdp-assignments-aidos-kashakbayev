package record;

import model.Artifact;
import storage.StorageBackend;

public abstract class ArtifactRecord {

    protected final StorageBackend storage;

    public ArtifactRecord(StorageBackend storage) {
        this.storage = storage;
    }

    public abstract void save(Artifact artifact);

    protected String basicData(Artifact artifact) {
        return "id=" + artifact.getId()
                + ", name=" + artifact.getName()
                + ", location=" + artifact.getLocation()
                + ", year=" + artifact.getYearFound();
    }
}