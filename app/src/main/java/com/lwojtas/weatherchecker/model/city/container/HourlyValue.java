package com.lwojtas.weatherchecker.model.city.container;

import org.json.JSONException;
import org.json.JSONObject;

public class HourlyValue extends Common {

    private final String tempJSON = "temp";
    private Double temp;
    private final String feelsLikeJSON = "feels_like";
    private Double feelsLike;
    private final String visibilityJSON = "visibility";
    private Double visibility;
    private final String popJSON = "pop";
    private Double pop;
    private final String rain1hJSON = "rain";
    private Double rain1h;
    private final String snow1hJSON = "snow";
    private Double snow1h;
    private final String h1JSON = "1h";

    public HourlyValue(JSONObject obj, Long timezoneOffset) throws JSONException {
        super(obj, timezoneOffset);
        temp = obj.getDouble(tempJSON);
        feelsLike = obj.getDouble(feelsLikeJSON);
        visibility = obj.getDouble(visibilityJSON);
        pop = obj.getDouble(popJSON);
        if (obj.has(rain1hJSON))
            rain1h = obj.getJSONObject(rain1hJSON).getDouble(h1JSON);
        if (obj.has(snow1hJSON))
            snow1h = obj.getJSONObject(snow1hJSON).getDouble(h1JSON);
    }

    public JSONObject toJSON(Long timezoneOffset) throws JSONException {
        JSONObject obj = commonToJSON(timezoneOffset);

        obj.put(tempJSON, temp);
        obj.put(feelsLikeJSON, feelsLike);
        obj.put(visibilityJSON, visibility);
        obj.put(popJSON, pop);

        JSONObject obj1;
        if (rain1h != null) {
            obj1 = new JSONObject();
            obj1.put(h1JSON, rain1h);
            obj.put(rain1hJSON, obj1);
        }
        if (snow1h != null) {
            obj1 = new JSONObject();
            obj1.put(h1JSON, snow1h);
            obj.put(snow1hJSON, obj1);
        }

        return obj;
    }

    public Double getTemp() {
        return temp;
    }

    public String getTempAsString() {
        return String.format("%.2f", temp) + "°C";
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public String getFeelsLikeAsString() {
        return String.format("%.2f", feelsLike) + "°C";
    }

    public Double getVisibility() {
        return visibility;
    }

    public String getVisibilityAsString() {
        return String.format("%.0f", visibility) + " m";
    }

    public Double getPop() {
        return pop;
    }

    public String getPopAsString() {
        return String.format("%.0f", pop * 100) + " %";
    }

    public Double getRain1h() {
        return rain1h;
    }

    public String getRain1hAsString() {
        if (rain1h != null)
            return String.format("%.2f", rain1h) + " mm";
        else
            return null;
    }

    public Double getSnow1h() {
        return snow1h;
    }

    public String getSnow1hAsString() {
        if (snow1h != null)
            return String.format("%.2f", snow1h) + " mm";
        else
            return null;
    }
}
