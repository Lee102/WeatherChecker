package com.lwojtas.weatherchecker.model;

import com.lwojtas.weatherchecker.model.city.Current;
import com.lwojtas.weatherchecker.model.city.Daily;
import com.lwojtas.weatherchecker.model.city.Hourly;
import com.lwojtas.weatherchecker.model.city.Minutely;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimeZone;

public class City {

    private final String NAME_JSON = "name";
    private String name;
    private final String LAT_JSON = "lat";
    private Double lat;
    private final String LON_JSON = "lon";
    private Double lon;
    private final String TIMEZONE_JSON = "timezone";
    private TimeZone timezone;
    private final String TIMEZONE_OFFSET_JSON = "timezone_offset";
    private Long timezoneOffset;
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
        timezone = TimeZone.getTimeZone(obj.getString(TIMEZONE_JSON));
        timezoneOffset = obj.getLong(TIMEZONE_OFFSET_JSON);
        if (obj.has(CURRENT_JSON))
            current = new Current(obj.getJSONObject(CURRENT_JSON), timezoneOffset);
        if (obj.has(MINUTELY_JSON))
            minutely = new Minutely(obj.getJSONArray(MINUTELY_JSON), timezoneOffset);
        if (obj.has(HOURLY_JSON))
            hourly = new Hourly(obj.getJSONArray(HOURLY_JSON), timezoneOffset);
        if (obj.has(DAILY_JSON))
            daily = new Daily(obj.getJSONArray(DAILY_JSON), timezoneOffset);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(NAME_JSON, name);
        obj.put(LAT_JSON, lat);
        obj.put(LON_JSON, lon);
        obj.put(TIMEZONE_JSON, timezone);
        obj.put(TIMEZONE_OFFSET_JSON, timezoneOffset);
        if (current != null)
            obj.put(CURRENT_JSON, current.toJSON(timezoneOffset));
        if (minutely != null)
            obj.put(MINUTELY_JSON, minutely.toJSON(timezoneOffset));
        if (hourly != null)
            obj.put(HOURLY_JSON, hourly.toJSON(timezoneOffset));
        if (daily != null)
            obj.put(DAILY_JSON, daily.toJSON(timezoneOffset));
        return obj;
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

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public TimeZone getTimezone() {
        return timezone;
    }

    public Long getTimezoneOffset() {
        return timezoneOffset;
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
