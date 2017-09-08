package scechecker.scechecker;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

/**
 * Created by Kevin on 8/27/2017.
 */

public class SteamlvlupToSce extends Fragment {

    private TableManager tableManager;
    private View inflatedView;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.steamlvlup_to_sce_fragment,
                container, false);
        activity = getActivity();

        final Button steamlvlupGetPricesButton = (Button) inflatedView.findViewById(
                R.id.steamlvlup_getprices);
        steamlvlupGetPricesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableManager = new TableManager(
                        (TableLayout) inflatedView.findViewById(R.id.steamlvlup), activity);

                UrlHandler urlHandler = new UrlHandler(activity, tableManager, steamlvlupGetPricesButton);
                urlHandler.execute("steamlvlupToSce");

                TextView gameLabel = (TextView) inflatedView.findViewById(R.id.steamlvlup_0);
                gameLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tableManager.sortByColumn(0);
                    }
                });

                TextView steamlvlupPriceLabel = (TextView) inflatedView.findViewById(R.id.steamlvlup_1);
                steamlvlupPriceLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tableManager.sortByColumn(1);
                    }
                });

                TextView scePriceLabel = (TextView) inflatedView.findViewById(R.id.steamlvlup_2);
                scePriceLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tableManager.sortByColumn(2);
                    }
                });

            }
        });

        return inflatedView;
    }
}
