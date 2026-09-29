import model.Artifact;
import record.MuseumRecord;
import storage.StorageBackend;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MuseumRecordTest {

    @Test
    void savesMuseumRecord() {

        TestStorage storage = new TestStorage();

        MuseumRecord record = new MuseumRecord(storage);

        Artifact artifact = new Artifact(
                20,
                "Clay Pot",
                "Otyrar",
                2023,
                "museum"
        );

        record.save(artifact);

        assertEquals(20, storage.id);
        assertTrue(storage.data.contains("MUSEUM RECORD"));
        assertTrue(storage.data.contains("Clay Pot"));
    }

    private static class TestStorage implements StorageBackend {

        private int id;
        private String data;

        @Override
        public void save(int id, String data) {
            this.id = id;
            this.data = data;
        }
    }
}