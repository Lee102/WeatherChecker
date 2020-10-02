package com.lwojtas.weatherchecker.model.city.container;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Common {

    private final String dtJSON = "dt";
    private Date dt;
    private final String pressureJSON = "pressure";
    private Double pressure;
    private final String humidityJSON = "humidity";
    private Double humidity;
    private final String dewPointJSON = "dew_point";
    private Double dewPoint;
    private final String cloudsJSON = "clouds";
    private Double clouds;
    private final String windSpeedJSON = "wind_speed";
    private Double windSpeed;
    private final String windGustJSON = "wind_gust";
    private Double windGust;
    private final String windDegJSON = "wind_deg";
    private Double windDeg;
    private final String weatherJSON = "weather";
    private List<Weather> weather;

    public Common(JSONObject obj, Long timezoneOffset) throws JSONException {
        dt = new Date((obj.getLong(dtJSON) + timezoneOffset) * 1000);
        pressure = obj.getDouble(pressureJSON);
        humidity = obj.getDouble(humidityJSON);
        dewPoint = obj.getDouble(dewPointJSON);
        clouds = obj.getDouble(cloudsJSON);
        windSpeed = obj.getDouble(windSpeedJSON);
        if (obj.has(windGustJSON))
            windGust = obj.getDouble(windGustJSON);
        windDeg = obj.getDouble(windDegJSON);

        weather = new LinkedList<>();
        JSONArray arr = obj.getJSONArray(weatherJSON);
        for (int i = 0; i < arr.length(); i++)
            weather.add(new Weather(arr.getJSONObject(i)));
    }

    protected JSONObject commonToJSON(Long timezoneOffset) throws JSONException {
        JSONObject obj = new JSONObject();

        long dt = this.dt.getTime() / 1000 - timezoneOffset;
        obj.put(dtJSON, dt);
        obj.put(pressureJSON, pressure);
        obj.put(humidityJSON, humidity);
        obj.put(dewPointJSON, dewPoint);
        obj.put(cloudsJSON, clouds);
        obj.put(windSpeedJSON, windSpeed);
        if (windGust != null)
            obj.put(windGustJSON, windGust);
        obj.put(windDegJSON, windDeg);

        JSONArray arr = new JSONArray();
        for (Weather weather : this.weather)
            arr.put(weather.toJSON());
        obj.put(weatherJSON, arr);

        return obj;
    }

    public Date getDt() {
        return dt;
    }

    public String getDtAsString(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, new Locale("en"));

        return sdf.format(dt);
    }

    public Double getPressure() {
        return pressure;
    }

    public String getPressureAsString() {
        return String.format("%.0f", pressure) + " hPa";
    }

    public Double getHumidity() {
        return humidity;
    }

    public String getHumidityAsString() {
        return String.format("%.0f", humidity) + "%";
    }

    public Double getDewPoint() {
        return dewPoint;
    }

    public String getDewPointAsString() {
        return String.format("%.2f", dewPoint) + "°C";
    }

    public Double getClouds() {
        return clouds;
    }

    public String getCloudsAsString() {
        return String.format("%.0f", clouds) + "%";
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public String getWindSpeedAsString() {
        return String.format("%.2f", windSpeed) + " m/s";
    }

    public Double getWindGust() {
        return windGust;
    }

    public String getWindGustAsString() {
        if (windGust != null)
            return String.format("%.2f", windGust) + " m/s";
        else
            return null;
    }

    public Double getWindDeg() {
        return windDeg;
    }

    public String getWindDegAsString() {
        return String.format("%.0f", windDeg) + "°";
    }

    public List<Weather> getWeather() {
        return weather;
    }
}
