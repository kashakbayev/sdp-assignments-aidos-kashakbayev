package strategy;

import data.DataSet;

/** Keeps rows exactly as they are in the file. */
public class OriginalOrder implements SortStrategy {

    @Override
    public DataSet sort(DataSet data) {
        return data;
    }

    @Override
    public String describe() {
        return "in original order";
    }
}