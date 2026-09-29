package record;

import model.Artifact;
import storage.StorageBackend;

public class MuseumRecord extends ArtifactRecord {

    public MuseumRecord(StorageBackend storage) {
        super(storage);
    }

    @Override
    public void save(Artifact artifact) {
        String data = "MUSEUM RECORD: " + basicData(artifact);
        storage.save(artifact.getId(), data);
    }
}