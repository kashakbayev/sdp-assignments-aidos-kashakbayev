package data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    public DataSet read(String path) throws IOException {
        if (!Files.isRegularFile(Paths.get(path))) {
            throw new IOException("File not found: " + path);
        }
        List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);

        List<String> headers = null;
        List<List<String>> rows = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            List<String> cells = split(line);
            if (headers == null) {
                headers = cells;
            } else if (cells.size() != headers.size()) {
                throw new IOException("Line " + (i + 1) + " of " + path + " has " + cells.size()
                        + " values, expected " + headers.size());
            } else {
                rows.add(cells);
            }
        }

        if (headers == null) {
            throw new IOException("File " + path + " is empty");
        }
        return new DataSet(headers, rows);
    }

    private List<String> split(String line) {
        List<String> cells = new ArrayList<>();
        for (String cell : line.split(",", -1)) {
            cells.add(cell.trim());
        }
        return cells;
    }
}