package com.lwojtas.weatherchecker.model.city;

import com.lwojtas.weatherchecker.model.city.container.HourlyValue;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

public class Hourly {

    private final List<HourlyValue> VALUES;

    public Hourly(JSONArray arr, Long timezoneOffset) throws JSONException {
        VALUES = new LinkedList<>();

        for (int i = 0; i < arr.length(); i++)
            VALUES.add(new HourlyValue(arr.getJSONObject(i), timezoneOffset));
    }

    public JSONArray toJSON(Long timezoneOffset) throws JSONException {
        JSONArray arr = new JSONArray();

        for (HourlyValue val : VALUES)
            arr.put(val.toJSON(timezoneOffset));

        return arr;
    }

    public List<HourlyValue> getVALUES() {
        return VALUES;
    }

}
