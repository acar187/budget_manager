package com.budget_manager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class CurrencyConverter {
    public static double getEuroToLiraRate() {
    try {
        URL url = new URL("https://open.er-api.com/v6/latest/EUR");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        System.out.println("API Response: " + content.toString());
        JSONObject obj = new JSONObject(content.toString());
        if (obj.has("rates")) {
            return obj.getJSONObject("rates").getDouble("TRY");
        } else {
            System.out.println("API-Response ohne 'rates': " + content.toString());
            return 0.0;
        }
    } catch (Exception e) {
        e.printStackTrace();
        return 0.0;
        }
    }

    public static double getEuroToBTC() {
    try {
        //Für Ethereum: https://api.coingecko.com/api/v3/simple/price?ids=ethereum&vs_currencies=eur
        URL url = new URL("https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=eur");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        JSONObject obj = new JSONObject(content.toString());
        // 1 BTC in EUR
        return obj.getJSONObject("bitcoin").getDouble("eur");
    } catch (Exception e) {
        e.printStackTrace();
        return 0.0;
    }
}

    public static double getEuroToETH() {
    try {
        URL url = new URL("https://api.coingecko.com/api/v3/simple/price?ids=ethereum&vs_currencies=eur");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        JSONObject obj = new JSONObject(content.toString());
        return obj.getJSONObject("ethereum").getDouble("eur");
    } catch (Exception e) {
        e.printStackTrace();
        return 0.0;
        }
    }   

}