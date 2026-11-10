package factory;

import model.DocumentElement;
import model.Table;

import java.util.List;

public class TableCreator extends ElementCreator {

    private final List<String> headers;
    private final List<List<String>> rows;

    public TableCreator(List<String> headers, List<List<String>> rows) {
        this.headers = headers;
        this.rows = rows;
    }

    @Override
    protected void validate() {
        if (headers == null || headers.isEmpty()) {
            throw new IllegalArgumentException("Table must have at least one column");
        }
        if (rows == null) {
            throw new IllegalArgumentException("Table rows must not be null");
        }
        for (int i = 0; i < rows.size(); i++) {
            if (rows.get(i).size() != headers.size()) {
                throw new IllegalArgumentException("Row " + (i + 1) + " has " + rows.get(i).size()
                        + " cells, expected " + headers.size());
            }
        }
    }

    @Override
    protected DocumentElement createElement() {
        return new Table(headers, rows);
    }
}