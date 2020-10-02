package com.lwojtas.weatherchecker.model;

import com.lwojtas.weatherchecker.model.city.Current;
import com.lwojtas.weatherchecker.model.city.Daily;
import com.lwojtas.weatherchecker.model.city.Hourly;
import com.lwojtas.weatherchecker.model.city.Minutely;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimeZone;

public class City {

    private final String nameJSON = "name";
    private String name;
    private final String latJSON = "lat";
    private Double lat;
    private final String lonJSON = "lon";
    private Double lon;
    private final String timezoneJSON = "timezone";
    private TimeZone timezone;
    private final String timezoneOffsetJSON = "timezone_offset";
    private Long timezoneOffset;
    private final String currentJSON = "current";
    private Current current;
    private final String minutelyJSON = "minutely";
    private Minutely minutely;
    private final String hourlyJSON = "hourly";
    private Hourly hourly;
    private final String dailyJSON = "daily";
    private Daily daily;

    public City(String name, Double lat, Double lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public City(JSONObject obj) throws JSONException {
        name = obj.getString(nameJSON);
        fromJSON(obj);
    }

    public void fromJSON(JSONObject obj) throws JSONException {
        lat = obj.getDouble(latJSON);
        lon = obj.getDouble(lonJSON);
        timezone = TimeZone.getTimeZone(obj.getString(timezoneJSON));
        timezoneOffset = obj.getLong(timezoneOffsetJSON);
        if (obj.has(currentJSON))
            current = new Current(obj.getJSONObject(currentJSON), timezoneOffset);
        if (obj.has(minutelyJSON))
            minutely = new Minutely(obj.getJSONArray(minutelyJSON), timezoneOffset);
        if (obj.has(hourlyJSON))
            hourly = new Hourly(obj.getJSONArray(hourlyJSON), timezoneOffset);
        if (obj.has(dailyJSON))
            daily = new Daily(obj.getJSONArray(dailyJSON), timezoneOffset);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(nameJSON, name);
        obj.put(latJSON, lat);
        obj.put(lonJSON, lon);
        obj.put(timezoneJSON, timezone);
        obj.put(timezoneOffsetJSON, timezoneOffset);
        if (current != null)
            obj.put(currentJSON, current.toJSON(timezoneOffset));
        if (minutely != null)
            obj.put(minutelyJSON, minutely.toJSON(timezoneOffset));
        if (hourly != null)
            obj.put(hourlyJSON, hourly.toJSON(timezoneOffset));
        if (daily != null)
            obj.put(dailyJSON, daily.toJSON(timezoneOffset));
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
