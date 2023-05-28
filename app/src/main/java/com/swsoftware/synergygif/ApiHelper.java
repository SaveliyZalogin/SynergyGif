package com.swsoftware.synergygif;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class ApiHelper {
    private static final String RATES_API = "https://openexchangerates.org/api/";
    private static final String GIFS_API = "https://api.giphy.com/v1/gifs/search?";
    private static final String RATES_API_KEY = "467e302e16f14ec99b9bbc1612ae0201";
    private static final String GIFS_API_KEY = "OcRqZV62MIW44d4hSgT3SSZ24N4P9J52";

    public static String getGifUrl() {
        try {
            JSONObject todayData = new JSONObject(
                    getResponse(new URL(RATES_API + "latest.json?app_id=" + RATES_API_KEY)));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date yesterday = new Date(System.currentTimeMillis() - 86400000);
            JSONObject yesterdayData = new JSONObject(
                    getResponse(new URL(RATES_API + "historical/" + simpleDateFormat.format(yesterday) + ".json?app_id=" + RATES_API_KEY)));

            double yesterdayRate = Double.parseDouble(String.valueOf(((JSONObject) yesterdayData.get("rates")).get("RUB")));
            double todayRate = Double.parseDouble(String.valueOf(((JSONObject) todayData.get("rates")).get("RUB")));

            int gifOffset = new Random().nextInt(4999);

            String gifResponse;
            if (yesterdayRate < todayRate) {
                gifResponse = getResponse(new URL(GIFS_API + "q=rich&offset=" + gifOffset + "&limit=1&api_key=" + GIFS_API_KEY));
            } else {
                gifResponse = getResponse(new URL(GIFS_API + "q=broke&offset=" + gifOffset + "&limit=1&api_key=" + GIFS_API_KEY));
            }

            JSONObject gifJson = new JSONObject(gifResponse);

            return ((JSONObject) ((JSONObject) ((JSONObject) ((JSONArray) gifJson.get("data")).get(0)).get("images")).get("original")).get("url").toString();
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getResponse(URL url) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            return content.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
