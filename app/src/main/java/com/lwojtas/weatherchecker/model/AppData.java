package com.lwojtas.weatherchecker.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppData {

    private static final String SETTINGS_JSON = "settings";
    private static Settings settings;
    private static final String CITIES_JSON = "cities";
    private static List<City> cities;

    static {
        settings = new Settings();
        cities = new ArrayList<>();
    }

    public static void fromJSON(JSONObject obj) throws JSONException {
        settings = new Settings(obj.getJSONObject(SETTINGS_JSON));

        JSONArray arr = obj.getJSONArray(CITIES_JSON);
        cities = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++)
            cities.add(new City(arr.getJSONObject(i)));
    }

    public static JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put(SETTINGS_JSON, settings.toJSON());

        JSONArray arr = new JSONArray();
        for (City city : cities)
            arr.put(city.toJSON());
        obj.put(CITIES_JSON, arr);

        return obj;
    }

    public static Settings getSettings() {
        return settings;
    }

    public static List<City> getCities() {
        return cities;
    }

}
