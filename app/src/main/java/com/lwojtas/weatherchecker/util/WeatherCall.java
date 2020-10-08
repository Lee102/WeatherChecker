package com.lwojtas.weatherchecker.util;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.City;
import com.lwojtas.weatherchecker.model.Settings;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

public class WeatherCall implements Callable<Void> {

    private final City CITY;

    public WeatherCall(City city) {
        this.CITY = city;
    }

    @Override
    public Void call() throws Exception {
        Settings settings = AppData.getSettings();

        URL url = new URL("https://api.openweathermap.org/data/2.5/onecall?" +
                "lat=" + CITY.getLat() +
                "&lon=" + CITY.getLon() +
                "&units=" + settings.getUnits().getTag() +
                "&lang=" + settings.getLocale().toLanguageTag() +
                "&appid=" + settings.getAppId());

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setConnectTimeout(settings.getTimeout());
        InputStream in = new BufferedInputStream(con.getInputStream());

        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader(in, Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }

        CITY.fromJSON(new JSONObject(textBuilder.toString()));

        con.disconnect();

        return null;
    }

}
