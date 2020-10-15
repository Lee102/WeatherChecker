package com.lwojtas.weatherchecker.model;

import com.lwojtas.weatherchecker.model.city.Current;
import com.lwojtas.weatherchecker.model.city.Daily;
import com.lwojtas.weatherchecker.model.city.Hourly;
import com.lwojtas.weatherchecker.model.city.Minutely;
import com.lwojtas.weatherchecker.util.exception.CoordinateOutOfBoundsException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.lwojtas.weatherchecker.util.LocaleTool.getDoubleAsString;

public class City {

    private final String NAME_JSON = "name";
    private String name;
    private final String LAT_JSON = "lat";
    private Double lat;
    private final String LON_JSON = "lon";
    private Double lon;
    private final String TIMEZONE_JSON = "timezone";
    private TimeZone timezone;
    private final String CURRENT_JSON = "current";
    private Current current;
    private final String MINUTELY_JSON = "minutely";
    private Minutely minutely;
    private final String HOURLY_JSON = "hourly";
    private Hourly hourly;
    private final String DAILY_JSON = "daily";
    private Daily daily;

    public City(String name, Double lat, Double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public City(JSONObject obj) throws JSONException {
        name = obj.getString(NAME_JSON);
        fromJSON(obj);
    }

    public void fromJSON(JSONObject obj) throws JSONException {
        lat = obj.getDouble(LAT_JSON);
        lon = obj.getDouble(LON_JSON);

        if (obj.has(TIMEZONE_JSON))
            timezone = TimeZone.getTimeZone(obj.getString(TIMEZONE_JSON));
        if (obj.has(CURRENT_JSON))
            current = new Current(obj.getJSONObject(CURRENT_JSON), timezone);
        if (obj.has(MINUTELY_JSON))
            minutely = new Minutely(obj.getJSONArray(MINUTELY_JSON), timezone);
        if (obj.has(HOURLY_JSON))
            hourly = new Hourly(obj.getJSONArray(HOURLY_JSON), timezone);
        if (obj.has(DAILY_JSON))
            daily = new Daily(obj.getJSONArray(DAILY_JSON), timezone);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(NAME_JSON, name);
        obj.put(LAT_JSON, lat);
        obj.put(LON_JSON, lon);

        if (timezone != null)
            obj.put(TIMEZONE_JSON, timezone.getID());
        if (current != null)
            obj.put(CURRENT_JSON, current.toJSON());
        if (minutely != null)
            obj.put(MINUTELY_JSON, minutely.toJSON());
        if (hourly != null)
            obj.put(HOURLY_JSON, hourly.toJSON());
        if (daily != null)
            obj.put(DAILY_JSON, daily.toJSON());
        return obj;
    }

    public boolean isInitialized() {
        return timezone != null && current != null;
    }

    public Boolean isActual() {
        if (current != null) {
            Date currentDate = new Date();
            long diff = currentDate.getTime() - current.getDt().getTime();
            return TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS) < AppData.getSettings().getWeatherActualThreshold();
        } else
            return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLat() {
        return lat;
    }

    public String getLatAsString() {
        Settings settings = AppData.getSettings();

        return getDoubleAsString(lat, settings.getPreciseDecimals(), settings.getLocale(), "", false);
    }

    public void setLat(Double lat) throws CoordinateOutOfBoundsException {
        if (lat < -180d || lat > 180d)
            throw new CoordinateOutOfBoundsException();

        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getLonAsString() {
        Settings settings = AppData.getSettings();

        return getDoubleAsString(lon, settings.getPreciseDecimals(), settings.getLocale(), "", false);
    }

    public void setLon(Double lon) throws CoordinateOutOfBoundsException {
        if (lon < -180d || lon > 180d)
            throw new CoordinateOutOfBoundsException();

        this.lon = lon;
    }

    public Current getCurrent() {
        return current;
    }

    public Minutely getMinutely() {
        return minutely;
    }

    public Hourly getHourly() {
        return hourly;
    }

    public Daily getDaily() {
        return daily;
    }

}
