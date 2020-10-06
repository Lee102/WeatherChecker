package com.lwojtas.weatherchecker.model.city.container;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.Settings;

import org.json.JSONException;
import org.json.JSONObject;

public class HourlyValue extends Common {

    private final String TEMP_JSON = "temp";
    private Double temp;
    private final String FEELS_LIKE_JSON = "feels_like";
    private Double feelsLike;
    private final String VISIBILITY_JSON = "visibility";
    private Double visibility;
    private final String POP_JSON = "pop";
    private Double pop;
    private final String RAIN_1H_JSON = "rain";
    private Double rain1h;
    private final String SNOW_1H_JSON = "snow";
    private Double snow1h;
    private final String H1_JSON = "1h";

    public HourlyValue(JSONObject obj, Long timezoneOffset) throws JSONException {
        super(obj, timezoneOffset);
        temp = obj.getDouble(TEMP_JSON);
        feelsLike = obj.getDouble(FEELS_LIKE_JSON);
        visibility = obj.getDouble(VISIBILITY_JSON);
        pop = obj.getDouble(POP_JSON);
        if (obj.has(RAIN_1H_JSON))
            rain1h = obj.getJSONObject(RAIN_1H_JSON).getDouble(H1_JSON);
        if (obj.has(SNOW_1H_JSON))
            snow1h = obj.getJSONObject(SNOW_1H_JSON).getDouble(H1_JSON);
    }

    public JSONObject toJSON(Long timezoneOffset) throws JSONException {
        JSONObject obj = super.toJSON(timezoneOffset);

        obj.put(TEMP_JSON, temp);
        obj.put(FEELS_LIKE_JSON, feelsLike);
        obj.put(VISIBILITY_JSON, visibility);
        obj.put(POP_JSON, pop);

        JSONObject obj1;
        if (rain1h != null) {
            obj1 = new JSONObject();
            obj1.put(H1_JSON, rain1h);
            obj.put(RAIN_1H_JSON, obj1);
        }
        if (snow1h != null) {
            obj1 = new JSONObject();
            obj1.put(H1_JSON, snow1h);
            obj.put(SNOW_1H_JSON, obj1);
        }

        return obj;
    }

    public String getTempAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(temp, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

    public String getFeelsLikeAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(feelsLike, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

    public String getVisibilityAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(visibility, settings.getDecimals(), settings.getLocale(), "m");
    }

    public String getPopAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(pop * 100, settings.getDecimals(), settings.getLocale(), "%");
    }

    public boolean rain1hExists() {
        return rain1h != null;
    }

    public String getRain1hAsString() {
        if (rain1h != null) {
            Settings settings = AppData.getSettings();

            return getAsString(rain1h, settings.getPreciseDecimals(), settings.getLocale(), "mm");
        } else
            return null;
    }

    public boolean snow1hExists() {
        return snow1h != null;
    }

    public String getSnow1hAsString() {
        if (snow1h != null) {
            Settings settings = AppData.getSettings();

            return getAsString(snow1h, settings.getPreciseDecimals(), settings.getLocale(), "mm");
        } else
            return null;
    }

}
