package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataSet {

    private final List<String> headers;
    private final List<List<String>> rows;

    public DataSet(List<String> headers, List<List<String>> rows) {
        this.headers = Collections.unmodifiableList(new ArrayList<>(headers));
        this.rows = Collections.unmodifiableList(new ArrayList<>(rows));
    }

    public List<String> getHeaders() {
        return headers;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public int columnIndex(String header) {
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).equalsIgnoreCase(header)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Column '" + header + "' not found. Available: " + headers);
    }

    public DataSet withRows(List<List<String>> newRows) {
        return new DataSet(headers, newRows);
    }
}