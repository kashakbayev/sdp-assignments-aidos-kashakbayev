import legacy.LegacyArchiveAdapter;
import legacy.OldArchiveSystem;
import model.Artifact;
import storage.DatabaseStorage;
import storage.JsonStorage;
import storage.StorageBackend;
import storage.StorageSelector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class StorageSelectorTest {

    @Test
    void selectsJsonStorageForFieldArtifact() {

        StorageSelector selector =
                new StorageSelector(new OldArchiveSystem());

        Artifact artifact = new Artifact(
                1,
                "Coin",
                "Taraz",
                2025,
                "field"
        );

        StorageBackend storage = selector.select(artifact);

        assertInstanceOf(JsonStorage.class, storage);
    }

    @Test
    void selectsDatabaseForMuseumArtifact() {

        StorageSelector selector =
                new StorageSelector(new OldArchiveSystem());

        Artifact artifact = new Artifact(
                2,
                "Vase",
                "Turkistan",
                2025,
                "museum"
        );

        StorageBackend storage = selector.select(artifact);

        assertInstanceOf(DatabaseStorage.class, storage);
    }

    @Test
    void selectsLegacyAdapterForLegacyArtifact() {

        StorageSelector selector =
                new StorageSelector(new OldArchiveSystem());

        Artifact artifact = new Artifact(
                3,
                "Tablet",
                "Otyrar",
                2020,
                "legacy"
        );

        StorageBackend storage = selector.select(artifact);

        assertInstanceOf(LegacyArchiveAdapter.class, storage);
    }
}