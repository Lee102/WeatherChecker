package com.lwojtas.weatherchecker.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppData {

    private static final String CITIES_JSON = "cities";
    private static List<City> cities;
    private static final String SETTINGS_JSON = "settings";
    private static Settings settings;

    static {
        cities = new ArrayList<>();
        settings = new Settings();
    }

    public static void fromJSON(JSONObject obj) throws JSONException {
        JSONArray arr = obj.getJSONArray(CITIES_JSON);
        cities = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++)
            cities.add(new City(arr.getJSONObject(i)));

        settings = new Settings(obj.getJSONObject(SETTINGS_JSON));
    }

    public static JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        JSONArray arr = new JSONArray();
        for (City city : cities)
            arr.put(city.toJSON());
        obj.put(CITIES_JSON, arr);

        obj.put(SETTINGS_JSON, settings.toJSON());

        return obj;
    }

    public static List<City> getCities() {
        return cities;
    }

    public static Settings getSettings() {
        return settings;
    }

}
