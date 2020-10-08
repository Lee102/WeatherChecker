package com.lwojtas.weatherchecker.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.lwojtas.weatherchecker.util.LocaleTool.getDoubleAsString;

public class GeocodeCity {

    private List<String> displayNameParts;
    private Double lat;
    private Double lon;

    public GeocodeCity(JSONObject obj) throws JSONException {
        String DISPLAY_NAME_JSON = "display_name";
        String DISPLAY_NAME_REGEX = ", ";
        String LAT_JSON = "lat";
        String LON_JSON = "lon";

        displayNameParts = Arrays.asList(obj.getString(DISPLAY_NAME_JSON).split(DISPLAY_NAME_REGEX));
        lat = obj.getDouble(LAT_JSON);
        lon = obj.getDouble(LON_JSON);
    }

    public List<String> getDisplayNameParts() {
        return displayNameParts;
    }

    public String getLatAsString() {
        Settings settings = AppData.getSettings();

        return getDoubleAsString(lat, settings.getPreciseDecimals(), settings.getLocale(), "", false);
    }

    public String getLonAsString() {
        Settings settings = AppData.getSettings();

        return getDoubleAsString(lon, settings.getPreciseDecimals(), settings.getLocale(), "", false);
    }

}
