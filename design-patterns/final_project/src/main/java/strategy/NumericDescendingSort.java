package strategy;

import data.DataSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Sorts rows by the number in one column, highest first. */
public class NumericDescendingSort implements SortStrategy {

    private final String column;

    public NumericDescendingSort(String column) {
        this.column = column;
    }

    @Override
    public DataSet sort(DataSet data) {
        final int index = data.columnIndex(column);
        List<List<String>> sorted = new ArrayList<>(data.getRows());
        sorted.sort(new Comparator<List<String>>() {
            @Override
            public int compare(List<String> a, List<String> b) {
                return Double.compare(toNumber(b.get(index)), toNumber(a.get(index)));
            }
        });
        return data.withRows(sorted);
    }

    /** Cells that are not numbers count as 0, so they go to the end. */
    private double toNumber(String cell) {
        try {
            return Double.parseDouble(cell);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    public String describe() {
        return "sorted by " + column + " (high to low)";
    }
}