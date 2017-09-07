package scechecker.scechecker;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewToInflate = inflater.inflate(R.layout.steamlvlup_to_sce_fragment,
                container, false);
        Activity activity = getActivity();

        tableManager = new TableManager(
                (TableLayout) viewToInflate.findViewById(R.id.steamlvlup), activity);

        UrlHandler urlHandler = new UrlHandler(activity, tableManager);
        urlHandler.execute("steamlvlupToSce");

        TextView gameLabel = (TextView) viewToInflate.findViewById(R.id.steamlvlup_0);
        Log.i("thing", gameLabel.toString());
        gameLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableManager.sortByColumn(0);
            }
        });

        TextView steamlvlupPriceLabel = (TextView) viewToInflate.findViewById(R.id.steamlvlup_1);
        steamlvlupPriceLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableManager.sortByColumn(1);
            }
        });

        TextView scePriceLabel = (TextView) viewToInflate.findViewById(R.id.steamlvlup_2);
        scePriceLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableManager.sortByColumn(2);
            }
        });
        return viewToInflate;
    }
}
