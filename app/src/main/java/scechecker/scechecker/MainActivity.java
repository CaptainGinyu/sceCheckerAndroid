package scechecker.scechecker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TableManager tableManager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_steamlvlup:
                    //TODO: replace this with stuff
                    return true;
                case R.id.navigation_inventory:
                    //TODO: replace this with stuff
                    return true;
                case R.id.navigation_notifications:
                    //TODO: replace this with stuff
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableManager = new TableManager((TableLayout) findViewById(R.id.steamlvlup), this);

        UrlHandler urlHandler = new UrlHandler(this, tableManager);
        urlHandler.execute("steamlvlupToSce");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        TextView gameLabel = (TextView) findViewById(R.id.steamlvlup_0);
        gameLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableManager.sortByColumn(0);
            }
        });

        TextView steamlvlupPriceLabel = (TextView) findViewById(R.id.steamlvlup_1);
        steamlvlupPriceLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableManager.sortByColumn(1);
            }
        });

        TextView scePriceLabel = (TextView) findViewById(R.id.steamlvlup_2);
        scePriceLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableManager.sortByColumn(2);
            }
        });
    }
}