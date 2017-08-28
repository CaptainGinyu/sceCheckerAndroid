package scechecker.scechecker;

import java.util.Comparator;

/**
 * Created by Kevin on 8/1/2017.
 */

public class TableInfoComparator implements Comparator<String[]> {

    private int columnIndex;

    public TableInfoComparator(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    @Override
    public int compare(String[] strings, String[] t1) {
        if (columnIndex > 0) {
            return Double.compare(Double.parseDouble(strings[columnIndex]),
                    Double.parseDouble(t1[columnIndex]));
        }
        return strings[columnIndex].compareTo(t1[columnIndex]);
    }
}
