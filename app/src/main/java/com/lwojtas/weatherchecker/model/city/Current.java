package com.lwojtas.weatherchecker.model.city;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.Settings;
import com.lwojtas.weatherchecker.model.city.container.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Current extends Common {

    private final String SUNRISE_JSON = "sunrise";
    private Date sunrise;
    private final String SUNSET_JSON = "sunset";
    private Date sunset;
    private final String TEMP_JSON = "temp";
    private Double temp;
    private final String FEELS_LIKE_JSON = "feels_like";
    private Double feelsLike;
    private final String UVI_JSON = "uvi";
    private Double uvi;
    private final String VISIBILITY_JSON = "visibility";
    private Double visibility;
    private final String RAIN_1H_JSON = "rain";
    private Double rain1h;
    private final String SNOW_1H_JSON = "snow";
    private Double snow1h;
    private final String H1_JSON = "1h";

    public Current(JSONObject obj, Long timezoneOffset) throws JSONException {
        super(obj, timezoneOffset);
        sunrise = new Date((obj.getLong(SUNRISE_JSON) + timezoneOffset) * 1000);
        sunset = new Date((obj.getLong(SUNSET_JSON) + timezoneOffset) * 1000);
        temp = obj.getDouble(TEMP_JSON);
        feelsLike = obj.getDouble(FEELS_LIKE_JSON);
        uvi = obj.getDouble(UVI_JSON);
        visibility = obj.getDouble(VISIBILITY_JSON);
        if (obj.has(RAIN_1H_JSON))
            rain1h = obj.getJSONObject(RAIN_1H_JSON).getDouble(H1_JSON);
        if (obj.has(SNOW_1H_JSON))
            snow1h = obj.getJSONObject(SNOW_1H_JSON).getDouble(H1_JSON);
    }

    public JSONObject toJSON(Long timezoneOffset) throws JSONException {
        JSONObject obj = super.toJSON(timezoneOffset);

        long sunrise = this.sunrise.getTime() / 1000 - timezoneOffset;
        obj.put(SUNRISE_JSON, sunrise);
        long sunset = this.sunset.getTime() / 1000 - timezoneOffset;
        obj.put(SUNSET_JSON, sunset);
        obj.put(TEMP_JSON, temp);
        obj.put(FEELS_LIKE_JSON, feelsLike);
        obj.put(UVI_JSON, uvi);
        obj.put(VISIBILITY_JSON, visibility);

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

    public String getSunriseAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", new Locale("en"));

        return sdf.format(sunrise);
    }

    public String getSunsetAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", new Locale("en"));

        return sdf.format(sunset);
    }

    public String getTempAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(temp, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

    public String getFeelsLikeAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(feelsLike, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP));
    }

    public String getUviAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(uvi, settings.getPreciseDecimals(), settings.getLocale(), "");
    }

    public String getVisibilityAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(visibility, settings.getDecimals(), settings.getLocale(), "m");
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
