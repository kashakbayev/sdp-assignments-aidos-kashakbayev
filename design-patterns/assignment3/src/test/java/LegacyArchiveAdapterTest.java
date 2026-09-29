package org.example;

import legacy.LegacyArchiveAdapter;
import legacy.OldArchiveSystem;
import storage.StorageException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LegacyArchiveAdapterTest {

    @Test
    void savesToLegacyArchive() {

        OldArchiveSystem archive = new OldArchiveSystem();
        LegacyArchiveAdapter adapter = new LegacyArchiveAdapter(archive);

        assertDoesNotThrow(() ->
                adapter.save(5, "Test artifact")
        );
    }

    @Test
    void translatesInvalidIdError() {

        OldArchiveSystem archive = new OldArchiveSystem();
        LegacyArchiveAdapter adapter = new LegacyArchiveAdapter(archive);

        StorageException exception = assertThrows(
                StorageException.class,
                () -> adapter.save(-1, "Test artifact")
        );

        assertEquals(
                "Invalid artifact id",
                exception.getMessage()
        );
    }

    @Test
    void translatesUnavailableArchiveError() {

        OldArchiveSystem archive = new OldArchiveSystem();
        archive.setAvailable(false);

        LegacyArchiveAdapter adapter = new LegacyArchiveAdapter(archive);

        StorageException exception = assertThrows(
                StorageException.class,
                () -> adapter.save(5, "Test artifact")
        );

        assertEquals(
                "Legacy archive is unavailable",
                exception.getMessage()
        );
    }
}