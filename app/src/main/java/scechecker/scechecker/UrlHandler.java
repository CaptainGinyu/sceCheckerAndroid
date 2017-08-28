package scechecker.scechecker;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by Kevin on 7/12/2017.
 */

public class UrlHandler extends AsyncTask<String, Void, JSONObject> {

    protected Context context;
    protected TableManager tableManager;
    protected boolean connectionException;
    protected String currTask;


    public UrlHandler(Context context, TableManager tableManager) {
        this.context = context;
        this.tableManager = tableManager;
        connectionException = false;
        currTask = "";
    }

    @Override
    protected JSONObject doInBackground(String... strings) {

        if (strings[0].equals("steamlvlupToSce")) {
            currTask = "steamlvlupToSce";
            return steamlvlupToSce();
        }

        currTask = null;
        return null;
    }

    protected String processUrl(String string) {

        connectionException = false;
        String result = "";

        URL url;
        HttpURLConnection urlConnection = null;
        InputStream inputStream;
        BufferedReader bufferedReader = null;

        try {
            Log.i("Website url", string);
            url = new URL(string);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setRequestMethod("GET");

            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();
            Log.i("status", Integer.toString(urlConnection.getResponseCode()));

            inputStream = urlConnection.getInputStream();

            if (inputStream != null) {
                bufferedReader = new BufferedReader(
                        new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                result += stringBuilder.toString();
            }
        } catch (Exception e) {
            connectionException = true;
            Log.i("Exception", e.toString());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (Exception e) {
                    Log.i("Exception", e.toString());
                }
            }
        }

        return result;
    }

    protected JSONObject getSceInventory() {
        JSONObject inventory = new JSONObject();
        String sce = processUrl("http://www.steamcardexchange.net/api/request.php?GetInventory&_=1496541700233");
        if (connectionException) {
            return inventory;
        }

        try {
            JSONObject sceJson = new JSONObject(sce);
            JSONArray data = sceJson.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONArray game = (JSONArray) data.get(i);
                JSONArray generalInfo = (JSONArray) game.get(0);

                int cardPrice = (int) game.get(1);
                int numCardsInSet = (int) ((JSONArray) game.get(3)).get(0);

                JSONArray entry = new JSONArray();
                entry.put(generalInfo.get(1));
                entry.put(cardPrice);
                entry.put(numCardsInSet);
                entry.put(cardPrice * numCardsInSet);
                inventory.put(Integer.toString((int) generalInfo.get(0)), entry);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("getSceInventory", inventory.toString());
        return inventory;
    }

    protected JSONObject steamlvlupToSce() {
        JSONObject result = new JSONObject();

        JSONObject sceInventory = getSceInventory();

        if (sceInventory.length() == 0) {
            return result;
        }

        int page = 0;
        String steamlvlupUrl = "https://steamlvlup.com/shop/items?hide_exist=false&page_size=999&page="
                + Integer.toString(page) + "&sort_by=price&sort_type=asc";
        String steamlvlupPage = processUrl(steamlvlupUrl);

        if (connectionException) {
            return result;
        }

        try {
            JSONObject steamlvlupJson = new JSONObject(steamlvlupPage);
            int steamlvlupCount = (int) steamlvlupJson.get("count");
            JSONArray steamlvlupItems = (JSONArray) steamlvlupJson.get("items");

            while (steamlvlupCount > 0) {
                page++;
                steamlvlupUrl = "https://steamlvlup.com/shop/items?hide_exist=false&page_size=999&page="
                        + Integer.toString(page) + "&sort_by=price&sort_type=asc";
                steamlvlupPage = processUrl(steamlvlupUrl);
                if (connectionException) {
                    return result;
                }

                steamlvlupJson = new JSONObject(steamlvlupPage);
                steamlvlupCount = (int) steamlvlupJson.get("count");
                JSONArray itemsToAppend = (JSONArray) steamlvlupJson.get("items");
                for (int i = 0; i < itemsToAppend.length(); i++) {
                    steamlvlupItems.put(itemsToAppend.get(i));
                }
            }

            JSONObject sceCardSetPrices = new JSONObject();
            JSONObject steamlvlupPrices = new JSONObject();

            for (int i = 0; i < steamlvlupItems.length(); i++) {
                JSONObject item = (JSONObject) steamlvlupItems.get(i);
                String appId = Integer.toString((int) item.get("appid"));
                steamlvlupPrices.put(appId, item.get("set_price"));

                JSONArray gameNameAndSetPrice = new JSONArray();

                if (sceInventory.has(appId)) {
                    JSONArray currGame = (JSONArray) sceInventory.get(appId);
                    gameNameAndSetPrice.put(0, currGame.get(0));
                    gameNameAndSetPrice.put(1, currGame.get(3));
                    sceCardSetPrices.put(appId, gameNameAndSetPrice);
                } else {
                    gameNameAndSetPrice.put(0, "Unknown game " + appId);
                    gameNameAndSetPrice.put(1, "?");
                    sceCardSetPrices.put(appId, gameNameAndSetPrice);
                }
            }

            result.put("sce_card_set_prices", sceCardSetPrices);
            result.put("steamlvlup_prices", steamlvlupPrices);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    protected void getSteamlvlupTable(JSONObject response) {
        JSONObject tableContent = response;

        try {
            JSONObject steamlvlupPrices = (JSONObject) tableContent.get("steamlvlup_prices");

            Iterator<String> keys = ((JSONObject) tableContent.get("sce_card_set_prices")).keys();
            String[] tableRowInfo;

            while (keys.hasNext()) {
                String game = keys.next();
                JSONArray sceInfo = (JSONArray) ((JSONObject) tableContent
                        .get("sce_card_set_prices")).get(game);
                tableRowInfo = new String[3];
                tableRowInfo[0] = (String) sceInfo.get(0);
                tableRowInfo[1] = Integer.toString((int) steamlvlupPrices.get(game));
                tableRowInfo[2] = Integer.toString((int) sceInfo.get(1));
                tableManager.addRow(tableRowInfo);
            }
            tableManager.displayRows();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        if (connectionException) {
            Toast.makeText(context, "Problem reading from website", Toast.LENGTH_LONG).show();
            return;
        }
        if (response != null) {
            if (currTask.equals("steamlvlupToSce")) {
                Log.i("steamlvlulpToSce", response.toString());
                getSteamlvlupTable(response);
            }

        } else {
            Log.i("Error", "Invalid task executed");
        }
        currTask = "";
    }
}
