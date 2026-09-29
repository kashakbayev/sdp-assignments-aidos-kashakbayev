import model.Artifact;
import record.FieldRecord;
import storage.StorageBackend;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FieldRecordTest {

    @Test
    void savesFieldRecord() {

        TestStorage storage = new TestStorage();

        FieldRecord record = new FieldRecord(storage);

        Artifact artifact = new Artifact(
                10,
                "Bronze Coin",
                "Taraz",
                2026,
                "field"
        );

        record.save(artifact);

        assertEquals(10, storage.id);
        assertTrue(storage.data.contains("FIELD RECORD"));
        assertTrue(storage.data.contains("Bronze Coin"));
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