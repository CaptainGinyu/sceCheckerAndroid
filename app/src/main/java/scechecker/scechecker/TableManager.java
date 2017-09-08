package scechecker.scechecker;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Kevin on 7/11/2017.
 */

public class TableManager {

    private TableLayout tableContent;
    private Context context;
    private ProgressBar progressBar;

    private ArrayList<String[]> tableInfo;

    private int columnBeingSortedBy;

    public TableManager(TableLayout tableContent, Context context) {
        this.tableContent = tableContent;
        this.context = context;
        progressBar = new ProgressBar(context);
        tableInfo = new ArrayList<String[]>();
        columnBeingSortedBy = -1;
    }

    public void addRow(String[] info) {
        int infoLength = info.length;
        String[] tableInfoRow = new String[infoLength];
        for (int i = 0; i < infoLength; i++) {
            String item = info[i];
            tableInfoRow[i] = item;
        }
        tableInfo.add(tableInfoRow);
    }

    public void displayRows() {
        for (String[] tableInfoRow : tableInfo) {
            TableRow row = new TableRow(context);

            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(layoutParams);

            TextView textView;

            layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
            int horizMargin = (int) context.getResources()
                    .getDimension(R.dimen.activity_horizontal_margin);
            int vertMargin = (int) context.getResources()
                    .getDimension(R.dimen.activity_vertical_margin);
            layoutParams.setMargins(horizMargin, vertMargin, horizMargin, vertMargin);


            for (String item : tableInfoRow) {
                textView = new TextView(context);
                textView.setLayoutParams(layoutParams);
                textView.setText(item);
                row.addView(textView);
            }

            View line = new View(context);

            TableLayout.LayoutParams layoutParams2 = new TableLayout.LayoutParams();
            layoutParams2.width =  TableLayout.LayoutParams.MATCH_PARENT;
            layoutParams2.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                    context.getResources().getDisplayMetrics());
            line.setLayoutParams(layoutParams2);
            layoutParams2.setMargins(horizMargin, 0, horizMargin, 0);
            line.setBackgroundColor(Color.parseColor("#A9A9A9"));

            tableContent.addView(row);
            tableContent.addView(line);
        }
    }

    public void sortByColumn(int columnIndex) {
        if (columnBeingSortedBy == columnIndex) {
            Collections.reverse(tableInfo);
        } else {
            Collections.sort(tableInfo, new TableInfoComparator(columnIndex));
        }

        tableContent.removeAllViews();
        displayRows();
        columnBeingSortedBy = columnIndex;
    }

    public void showProgressBar() {
        tableContent.removeAllViews();
        tableContent.addView(progressBar);
        progressBar.bringToFront();
    }

    public void removeProgressBar() {
        tableContent.removeView(progressBar);
    }
}
