package com.lwojtas.weatherchecker.model.city.container;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import static com.lwojtas.weatherchecker.model.city.container.Common.getAsString;

public class Temp {

    private final String DAY_JSON = "day";
    private Double day;
    private final String MIN_JSON = "min";
    private Double min;
    private final String MAX_JSON = "max";
    private Double max;
    private final String NIGHT_JSON = "night";
    private Double night;
    private final String EVE_JSON = "eve";
    private Double eve;
    private final String MORN_JSON = "morn";
    private Double morn;

    public Temp(JSONObject obj) throws JSONException {
        day = obj.getDouble(DAY_JSON);
        min = obj.getDouble(MIN_JSON);
        max = obj.getDouble(MAX_JSON);
        night = obj.getDouble(NIGHT_JSON);
        eve = obj.getDouble(EVE_JSON);
        morn = obj.getDouble(MORN_JSON);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put(DAY_JSON, day);
        obj.put(MIN_JSON, min);
        obj.put(MAX_JSON, max);
        obj.put(NIGHT_JSON, night);
        obj.put(EVE_JSON, eve);
        obj.put(MORN_JSON, morn);

        return obj;
    }

    public String getDayAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(day, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

    public String getMinAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(min, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

    public String getMaxAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(max, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

    public String getNightAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(night, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

    public String getEveAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(eve, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

    public String getMornAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(morn, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

}
