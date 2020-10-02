package com.lwojtas.weatherchecker.model.city.container;

import org.json.JSONException;
import org.json.JSONObject;

public class FeelsLike {

    private final String mornJSON = "morn";
    private Double morn;
    private final String dayJSON = "day";
    private Double day;
    private final String eveJSON = "eve";
    private Double eve;
    private final String nightJSON = "night";
    private Double night;

    public FeelsLike(JSONObject obj) throws JSONException {
        morn = obj.getDouble(mornJSON);
        day = obj.getDouble(dayJSON);
        eve = obj.getDouble(eveJSON);
        night = obj.getDouble(nightJSON);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put(mornJSON, morn);
        obj.put(dayJSON, day);
        obj.put(eveJSON, eve);
        obj.put(nightJSON, night);

        return obj;
    }

    public Double getMorn() {
        return morn;
    }

    public String getMornAsString() {
        return String.format("%.2f", morn) + "°C";
    }

    public Double getDay() {
        return day;
    }

    public String getDayAsString() {
        return String.format("%.2f", day) + "°C";
    }

    public Double getEve() {
        return eve;
    }

    public String getEveAsString() {
        return String.format("%.2f", eve) + "°C";
    }

    public Double getNight() {
        return night;
    }

    public String getNightAsString() {
        return String.format("%.2f", night) + "°C";
    }
}
