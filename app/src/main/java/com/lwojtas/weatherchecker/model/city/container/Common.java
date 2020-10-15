package com.lwojtas.weatherchecker.model.city.container;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.Settings;
import com.lwojtas.weatherchecker.util.LocaleTool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public abstract class Common {

    private final String DT_JSON = "dt";
    private Date dt;
    private final String PRESSURE_JSON = "pressure";
    private Double pressure;
    private final String HUMIDITY_JSON = "humidity";
    private Double humidity;
    private final String DEW_POINT_JSON = "dew_point";
    private Double dewPoint;
    private final String CLOUDS_JSON = "clouds";
    private Double clouds;
    private final String WIND_SPEED_JSON = "wind_speed";
    private Double windSpeed;
    private final String WIND_GUST_JSON = "wind_gust";
    private Double windGust;
    private final String WIND_DEG_JSON = "wind_deg";
    private Double windDeg;
    private final String WEATHER_JSON = "weather";
    private List<Weather> weather;

    private TimeZone timeZone;

    public Common(JSONObject obj, TimeZone timeZone) throws JSONException {
        dt = new Date(obj.getLong(DT_JSON) * 1000);
        pressure = obj.getDouble(PRESSURE_JSON);
        humidity = obj.getDouble(HUMIDITY_JSON);
        dewPoint = obj.getDouble(DEW_POINT_JSON);
        clouds = obj.getDouble(CLOUDS_JSON);
        windSpeed = obj.getDouble(WIND_SPEED_JSON);
        if (obj.has(WIND_GUST_JSON))
            windGust = obj.getDouble(WIND_GUST_JSON);
        windDeg = obj.getDouble(WIND_DEG_JSON);

        weather = new LinkedList<>();
        JSONArray arr = obj.getJSONArray(WEATHER_JSON);
        for (int i = 0; i < arr.length(); i++)
            weather.add(new Weather(arr.getJSONObject(i)));

        this.timeZone = timeZone;
    }

    protected JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        long dt = this.dt.getTime() / 1000;
        obj.put(DT_JSON, dt);
        obj.put(PRESSURE_JSON, pressure);
        obj.put(HUMIDITY_JSON, humidity);
        obj.put(DEW_POINT_JSON, dewPoint);
        obj.put(CLOUDS_JSON, clouds);
        obj.put(WIND_SPEED_JSON, windSpeed);
        if (windGust != null)
            obj.put(WIND_GUST_JSON, windGust);
        obj.put(WIND_DEG_JSON, windDeg);

        JSONArray arr = new JSONArray();
        for (Weather weather : this.weather)
            arr.put(weather.toJSON());
        obj.put(WEATHER_JSON, arr);

        return obj;
    }

    protected static String getAsString(double value, int decimals, Locale locale, String unit, boolean unitWithSpace) {
        return LocaleTool.getDoubleAsString(value, decimals, locale, unit, unitWithSpace);
    }

    public Date getDt() {
        return dt;
    }

    public String getDtAsString(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, AppData.getSettings().getLocale());
        sdf.setTimeZone(timeZone);

        return sdf.format(dt);
    }

    public String getPressureAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(pressure, settings.getDecimals(), settings.getLocale(), "hPa", true);
    }

    public String getHumidityAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(humidity, settings.getDecimals(), settings.getLocale(), "%", false);
    }

    public String getDewPointAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(dewPoint, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.TEMP), false);
    }

    public String getCloudsAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(clouds, settings.getDecimals(), settings.getLocale(), "%", false);
    }

    public String getWindSpeedAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(windSpeed, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.WIND_SPEED), true);
    }

    public boolean windGustExists() {
        return windGust != null;
    }

    public String getWindGustAsString() {
        if (windGust != null) {
            Settings settings = AppData.getSettings();

            return getAsString(windGust, settings.getPreciseDecimals(), settings.getLocale(), settings.getUnitString(Settings.UnitType.WIND_SPEED), true);
        } else
            return null;
    }

    public String getWindDegAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(windDeg, settings.getDecimals(), settings.getLocale(), "°", false);
    }

    public List<Weather> getWeather() {
        return weather;
    }

}
