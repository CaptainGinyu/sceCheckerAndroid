package scechecker.scechecker;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by Kevin on 8/27/2017.
 */

public class SteamlvlupToSce extends Fragment {

    private TableManager tableManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();

        tableManager = new TableManager(
                (TableLayout) activity.findViewById(R.id.steamlvlup), activity);

        UrlHandler urlHandler = new UrlHandler(activity, tableManager);
        urlHandler.execute("steamlvlupToSce");

        TextView gameLabel = (TextView) activity.findViewById(R.id.steamlvlup_0);
        gameLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableManager.sortByColumn(0);
            }
        });

        TextView steamlvlupPriceLabel = (TextView) activity.findViewById(R.id.steamlvlup_1);
        steamlvlupPriceLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableManager.sortByColumn(1);
            }
        });

        TextView scePriceLabel = (TextView) activity.findViewById(R.id.steamlvlup_2);
        scePriceLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableManager.sortByColumn(2);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null; //TODO: make the xml for this thing
    }
}
