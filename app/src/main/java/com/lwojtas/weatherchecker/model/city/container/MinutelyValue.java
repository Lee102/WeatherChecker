package com.lwojtas.weatherchecker.model.city.container;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.lwojtas.weatherchecker.model.city.container.Common.getAsString;

public class MinutelyValue {

    private final String DT_JSON = "dt";
    private Date dt;
    private final String PRECIPITATION_JSON = "precipitation";
    private Double precipitation;

    private TimeZone timeZone;

    public MinutelyValue(JSONObject obj, TimeZone timeZone) throws JSONException {
        dt = new Date(obj.getLong(DT_JSON) * 1000);
        precipitation = obj.getDouble(PRECIPITATION_JSON);

        this.timeZone = timeZone;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        long dt = this.dt.getTime() / 1000;
        obj.put(DT_JSON, dt);
        obj.put(PRECIPITATION_JSON, precipitation);

        return obj;
    }

    public String getDtAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", AppData.getSettings().getLocale());
        sdf.setTimeZone(timeZone);

        return sdf.format(dt);
    }

    public String getPrecipitationAsString() {
        Settings settings = AppData.getSettings();

        return getAsString(precipitation, settings.getPreciseDecimals(), settings.getLocale(), "", false);
    }

}
