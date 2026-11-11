package strategy;

import data.DataSet;

/** Strategy interface: one interchangeable way of ordering the rows. */
public interface SortStrategy {

    /** Returns a new data set with sorted rows; the input is not changed. */
    DataSet sort(DataSet data);

    /** Short text for the report, for example "sorted by Product (A to Z)". */
    String describe();
}