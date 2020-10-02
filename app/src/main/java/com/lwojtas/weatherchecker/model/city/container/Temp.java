package com.lwojtas.weatherchecker.model.city.container;

import org.json.JSONException;
import org.json.JSONObject;

public class Temp {

    private final String dayJSON = "day";
    private Double day;
    private final String minJSON = "min";
    private Double min;
    private final String maxJSON = "max";
    private Double max;
    private final String nightJSON = "night";
    private Double night;
    private final String eveJSON = "eve";
    private Double eve;
    private final String mornJSON = "morn";
    private Double morn;

    public Temp(JSONObject obj) throws JSONException {
        day = obj.getDouble(dayJSON);
        min = obj.getDouble(minJSON);
        max = obj.getDouble(maxJSON);
        night = obj.getDouble(nightJSON);
        eve = obj.getDouble(eveJSON);
        morn = obj.getDouble(mornJSON);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put(dayJSON, day);
        obj.put(minJSON, min);
        obj.put(maxJSON, max);
        obj.put(nightJSON, night);
        obj.put(eveJSON, eve);
        obj.put(mornJSON, morn);

        return obj;
    }

    public Double getDay() {
        return day;
    }

    public String getDayAsString() {
        return String.format("%.2f", day) + "°C";
    }

    public Double getMin() {
        return min;
    }

    public String getMinAsString() {
        return String.format("%.2f", min) + "°C";
    }

    public Double getMax() {
        return max;
    }

    public String getMaxAsString() {
        return String.format("%.2f", max) + "°C";
    }

    public Double getNight() {
        return night;
    }

    public String getNightAsString() {
        return String.format("%.2f", night) + "°C";
    }

    public Double getEve() {
        return eve;
    }

    public String getEveAsString() {
        return String.format("%.2f", eve) + "°C";
    }

    public Double getMorn() {
        return morn;
    }

    public String getMornAsString() {
        return String.format("%.2f", morn) + "°C";
    }
}
