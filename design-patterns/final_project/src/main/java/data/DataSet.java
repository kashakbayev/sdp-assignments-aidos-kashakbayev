package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Tabular data loaded from a file: column names and rows of cells. */
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

    /** Finds a column by name, ignoring case. */
    public int columnIndex(String header) {
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i).equalsIgnoreCase(header)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Column '" + header + "' not found. Available: " + headers);
    }

    /** Returns a copy of this data set with the same columns and other rows. */
    public DataSet withRows(List<List<String>> newRows) {
        return new DataSet(headers, newRows);
    }
}