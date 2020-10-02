package com.lwojtas.weatherchecker.model.city.container;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MinutelyValue {

    private final String dtJSON = "dt";
    private Date dt;
    private final String precipitationJSON = "precipitation";
    private Double precipitation;

    public MinutelyValue(JSONObject obj, Long timezoneOffset) throws JSONException {
        dt = new Date((obj.getLong(dtJSON) + timezoneOffset) * 1000);
        precipitation = obj.getDouble(precipitationJSON);
    }

    public JSONObject toJSON(Long timezoneOffset) throws JSONException {
        JSONObject obj = new JSONObject();

        long dt = this.dt.getTime() / 1000 - timezoneOffset;
        obj.put(dtJSON, dt);
        obj.put(precipitationJSON, precipitation);

        return obj;
    }

    public Date getDt() {
        return dt;
    }

    public String getDtAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", new Locale("en"));

        return sdf.format(dt);
    }

    public Double getPrecipitation() {
        return precipitation;
    }

    public String getPrecipitationAsString() {
        return String.format("%.2f", precipitation);
    }
}
