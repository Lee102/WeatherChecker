package com.lwojtas.weatherchecker.model.city.container;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import static com.lwojtas.weatherchecker.model.city.container.Common.getAsString;

public class FeelsLike {

    private final String MORN_JSON = "morn";
    private Double morn;
    private final String DAY_JSON = "day";
    private Double day;
    private final String EVE_JSON = "eve";
    private Double eve;
    private final String NIGHT_JSON = "night";
    private Double night;

    public FeelsLike(JSONObject obj) throws JSONException {
        morn = obj.getDouble(MORN_JSON);
        day = obj.getDouble(DAY_JSON);
        eve = obj.getDouble(EVE_JSON);
        night = obj.getDouble(NIGHT_JSON);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put(MORN_JSON, morn);
        obj.put(DAY_JSON, day);
        obj.put(EVE_JSON, eve);
        obj.put(NIGHT_JSON, night);

        return obj;
    }

    public String getMornAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(morn, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

    public String getDayAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(day, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

    public String getEveAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(eve, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

    public String getNightAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(night, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

}
