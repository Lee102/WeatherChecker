package com.lwojtas.weatherchecker.model.city.container;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DailyValue extends Common {

    private final String sunriseJSON = "sunrise";
    private Date sunrise;
    private final String sunsetJSON = "sunset";
    private Date sunset;
    private final String tempJSON = "temp";
    private Temp temp;
    private final String feelsLikeJSON = "feels_like";
    private FeelsLike feelsLike;
    private final String uviJSON = "uvi";
    private Double uvi;
    private final String popJSON = "pop";
    private Double pop;
    private final String rainJSON = "rain";
    private Double rain;
    private final String snowJSON = "snow";
    private Double snow;

    public DailyValue(JSONObject obj, Long timezoneOffset) throws JSONException {
        super(obj, timezoneOffset);
        sunrise = new Date((obj.getLong(sunriseJSON) + timezoneOffset) * 1000);
        sunset = new Date((obj.getLong(sunsetJSON) + timezoneOffset) * 1000);
        temp = new Temp(obj.getJSONObject(tempJSON));
        feelsLike = new FeelsLike(obj.getJSONObject(feelsLikeJSON));
        uvi = obj.getDouble(uviJSON);
        pop = obj.getDouble(popJSON);
        if (obj.has(rainJSON))
            rain = obj.getDouble(rainJSON);
        if (obj.has(snowJSON))
            snow = obj.getDouble(snowJSON);
    }

    public JSONObject toJSON(Long timezoneOffset) throws JSONException {
        JSONObject obj = commonToJSON(timezoneOffset);

        long sunrise = this.sunrise.getTime() / 1000 - timezoneOffset;
        obj.put(sunriseJSON, sunrise);
        long sunset = this.sunset.getTime() / 1000 - timezoneOffset;
        obj.put(sunsetJSON, sunset);
        obj.put(tempJSON, temp.toJSON());
        obj.put(feelsLikeJSON, feelsLike.toJSON());
        obj.put(uviJSON, uvi);
        obj.put(popJSON, pop);
        if (rain != null)
            obj.put(rainJSON, rain);
        if (snow != null)
            obj.put(snowJSON, snow);

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

    public Temp getTemp() {
        return temp;
    }

    public FeelsLike getFeelsLike() {
        return feelsLike;
    }

    public Double getUvi() {
        return uvi;
    }

    public String getUviAsString() {
        return String.format("%.2f", uvi);
    }

    public Double getPop() {
        return pop;
    }

    public String getPopAsString() {
        return String.format("%.0f", pop * 100) + " %";
    }

    public Double getRain() {
        return rain;
    }

    public String getRainAsString() {
        if (rain != null)
            return String.format("%.2f", rain) + " mm";
        else
            return null;
    }

    public Double getSnow() {
        return snow;
    }

    public String getSnowAsString() {
        if (snow != null)
            return String.format("%.2f", snow) + " mm";
        else
            return null;
    }
}
