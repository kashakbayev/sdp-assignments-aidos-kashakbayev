package record;

import model.Artifact;
import storage.StorageBackend;

public class FieldRecord extends ArtifactRecord {

    public FieldRecord(StorageBackend storage) {
        super(storage);
    }

    @Override
    public void save(Artifact artifact) {
        String data = "FIELD RECORD: " + basicData(artifact);
        storage.save(artifact.getId(), data);
    }
}