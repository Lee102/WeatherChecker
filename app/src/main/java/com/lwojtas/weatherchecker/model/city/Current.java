package com.lwojtas.weatherchecker.model.city;

import com.lwojtas.weatherchecker.model.city.container.Common;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Current extends Common {

    private final String sunriseJSON = "sunrise";
    private Date sunrise;
    private final String sunsetJSON = "sunset";
    private Date sunset;
    private final String tempJSON = "temp";
    private Double temp;
    private final String feelsLikeJSON = "feels_like";
    private Double feelsLike;
    private final String uviJSON = "uvi";
    private Double uvi;
    private final String visibilityJSON = "visibility";
    private Double visibility;
    private final String rain1hJSON = "rain";
    private Double rain1h;
    private final String snow1hJSON = "snow";
    private Double snow1h;
    private final String h1JSON = "1h";

    public Current(JSONObject obj, Long timezoneOffset) throws JSONException {
        super(obj, timezoneOffset);
        sunrise = new Date((obj.getLong(sunriseJSON) + timezoneOffset) * 1000);
        sunset = new Date((obj.getLong(sunsetJSON) + timezoneOffset) * 1000);
        temp = obj.getDouble(tempJSON);
        feelsLike = obj.getDouble(feelsLikeJSON);
        uvi = obj.getDouble(uviJSON);
        visibility = obj.getDouble(visibilityJSON);
        if (obj.has(rain1hJSON))
            rain1h = obj.getJSONObject(rain1hJSON).getDouble(h1JSON);
        if (obj.has(snow1hJSON))
            snow1h = obj.getJSONObject(snow1hJSON).getDouble(h1JSON);
    }

    public JSONObject toJSON(Long timezoneOffset) throws JSONException {
        JSONObject obj = commonToJSON(timezoneOffset);

        long sunrise = this.sunrise.getTime() / 1000 - timezoneOffset;
        obj.put(sunriseJSON, sunrise);
        long sunset = this.sunset.getTime() / 1000 - timezoneOffset;
        obj.put(sunsetJSON, sunset);
        obj.put(tempJSON, temp);
        obj.put(feelsLikeJSON, feelsLike);
        obj.put(uviJSON, uvi);
        obj.put(visibilityJSON, visibility);

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

    public Date getSunrise() {
        return sunrise;
    }

    public String getSunriseAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", new Locale("en"));

        return sdf.format(sunrise);
    }

    public Date getSunset() {
        return sunset;
    }

    public String getSunsetAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", new Locale("en"));

        return sdf.format(sunset);
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

    public Double getUvi() {
        return uvi;
    }

    public String getUviAsString() {
        return String.format("%.2f", uvi);
    }

    public Double getVisibility() {
        return visibility;
    }

    public String getVisibilityAsString() {
        return String.format("%.0f", visibility) + " m";
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
