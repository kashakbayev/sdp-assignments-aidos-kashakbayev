package strategy;

import data.DataSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Sorts rows by the text of one column, A to Z. */
public class AlphabeticalSort implements SortStrategy {

    private final String column;

    public AlphabeticalSort(String column) {
        this.column = column;
    }

    @Override
    public DataSet sort(DataSet data) {
        final int index = data.columnIndex(column);
        List<List<String>> sorted = new ArrayList<>(data.getRows());
        sorted.sort(new Comparator<List<String>>() {
            @Override
            public int compare(List<String> a, List<String> b) {
                return a.get(index).compareToIgnoreCase(b.get(index));
            }
        });
        return data.withRows(sorted);
    }

    @Override
    public String describe() {
        return "sorted by " + column + " (A to Z)";
    }
}