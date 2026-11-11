package facade;

import java.nio.file.Path;

public class ReportResult {

    private final Path file;
    private final int records;
    private final int words;

    public ReportResult(Path file, int records, int words) {
        this.file = file;
        this.records = records;
        this.words = words;
    }

    public Path getFile() {
        return file;
    }

    public int getRecords() {
        return records;
    }

    public int getWords() {
        return words;
    }
}