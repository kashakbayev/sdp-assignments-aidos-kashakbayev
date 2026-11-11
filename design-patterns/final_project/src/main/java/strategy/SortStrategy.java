package strategy;

import data.DataSet;

public interface SortStrategy {

    DataSet sort(DataSet data);

    String describe();
}