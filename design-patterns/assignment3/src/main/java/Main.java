import legacy.OldArchiveSystem;
import model.Artifact;
import record.ArtifactRecord;
import record.FieldRecord;
import record.MuseumRecord;
import storage.StorageBackend;
import storage.StorageSelector;

public class Main {

    public static void main(String[] args) {

        OldArchiveSystem oldArchive = new OldArchiveSystem();
        StorageSelector selector = new StorageSelector(oldArchive);

        Artifact coin = new Artifact(
                1,
                "Ancient Coin",
                "Taraz",
                2025,
                "field"
        );

        Artifact vase = new Artifact(
                2,
                "Ceramic Vase",
                "Turkistan",
                2024,
                "museum"
        );

        Artifact tablet = new Artifact(
                3,
                "Stone Tablet",
                "Otyrar",
                2018,
                "legacy"
        );

        saveFieldRecord(coin, selector);
        saveMuseumRecord(vase, selector);
        saveMuseumRecord(tablet, selector);
    }

    private static void saveFieldRecord(
            Artifact artifact,
            StorageSelector selector
    ) {
        StorageBackend storage = selector.select(artifact);
        ArtifactRecord record = new FieldRecord(storage);
        record.save(artifact);
    }

    private static void saveMuseumRecord(
            Artifact artifact,
            StorageSelector selector
    ) {
        StorageBackend storage = selector.select(artifact);
        ArtifactRecord record = new MuseumRecord(storage);
        record.save(artifact);
    }
}