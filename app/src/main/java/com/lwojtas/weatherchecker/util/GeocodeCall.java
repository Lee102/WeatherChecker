package com.lwojtas.weatherchecker.util;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.GeocodeCity;
import com.lwojtas.weatherchecker.model.Settings;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class GeocodeCall implements Callable<List<GeocodeCity>> {

    private static final Logger LOG = LoggerFactory.getLogger(GeocodeCall.class);

    private final String CITY_NAME;

    public GeocodeCall(String cityName) {
        this.CITY_NAME = cityName;
    }

    @Override
    public List<GeocodeCity> call() throws Exception {
        LOG.trace("call");

        Settings settings = AppData.getSettings();

        URL url = new URL("https://nominatim.openstreetmap.org/search?" +
                "city=" + CITY_NAME +
                "&accept-language=" + settings.getLocale().toLanguageTag() +
                "&format=json");

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

        JSONArray arr = new JSONArray(textBuilder.toString());
        List<GeocodeCity> cities = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++)
            cities.add(new GeocodeCity(arr.getJSONObject(i)));

        con.disconnect();

        return cities;
    }

}
