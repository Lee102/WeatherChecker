package com.lwojtas.weatherchecker.model.city.container;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyValue extends Common {

    private final String SUNRISE_JSON = "sunrise";
    private Date sunrise;
    private final String SUNSET_JSON = "sunset";
    private Date sunset;
    private final String TEMP_JSON = "temp";
    private Temp temp;
    private final String FEELS_LIKE_JSON = "feels_like";
    private FeelsLike feelsLike;
    private final String UVI_JSON = "uvi";
    private Double uvi;
    private final String POP_JSON = "pop";
    private Double pop;
    private final String RAIN_JSON = "rain";
    private Double rain;
    private final String SNOW_JSON = "snow";
    private Double snow;

    public DailyValue(JSONObject obj, Long timezoneOffset) throws JSONException {
        super(obj, timezoneOffset);
        sunrise = new Date((obj.getLong(SUNRISE_JSON) + timezoneOffset) * 1000);
        sunset = new Date((obj.getLong(SUNSET_JSON) + timezoneOffset) * 1000);
        temp = new Temp(obj.getJSONObject(TEMP_JSON));
        feelsLike = new FeelsLike(obj.getJSONObject(FEELS_LIKE_JSON));
        uvi = obj.getDouble(UVI_JSON);
        pop = obj.getDouble(POP_JSON);
        if (obj.has(RAIN_JSON))
            rain = obj.getDouble(RAIN_JSON);
        if (obj.has(SNOW_JSON))
            snow = obj.getDouble(SNOW_JSON);
    }

    public JSONObject toJSON(Long timezoneOffset) throws JSONException {
        JSONObject obj = super.toJSON(timezoneOffset);

        long sunrise = this.sunrise.getTime() / 1000 - timezoneOffset;
        obj.put(SUNRISE_JSON, sunrise);
        long sunset = this.sunset.getTime() / 1000 - timezoneOffset;
        obj.put(SUNSET_JSON, sunset);
        obj.put(TEMP_JSON, temp.toJSON());
        obj.put(FEELS_LIKE_JSON, feelsLike.toJSON());
        obj.put(UVI_JSON, uvi);
        obj.put(POP_JSON, pop);
        if (rain != null)
            obj.put(RAIN_JSON, rain);
        if (snow != null)
            obj.put(SNOW_JSON, snow);

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

    public Temp getTemp() {
        return temp;
    }

    public FeelsLike getFeelsLike() {
        return feelsLike;
    }

    public String getUviAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(uvi, settings.getPreciseDecimals(), settings.getLocale(), "", false);
    }

    public String getPopAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(pop * 100, settings.getDecimals(), settings.getLocale(), "%", false);
    }

    public boolean rainExists() {
        return rain != null;
    }

    public String getRainAsString() {
        if (rain != null) {
            Settings settings = AppData.getSettings();

            return getAsString(rain, settings.getPreciseDecimals(), settings.getLocale(), "mm", true);
        } else
            return null;
    }

    public boolean snowExists() {
        return snow != null;
    }

    public String getSnowAsString() {
        if (snow != null) {
            Settings settings = AppData.getSettings();

            return getAsString(snow, settings.getPreciseDecimals(), settings.getLocale(), "mm", true);
        } else
            return null;
    }

}
