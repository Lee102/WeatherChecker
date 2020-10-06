package com.lwojtas.weatherchecker.model.city;

import com.lwojtas.weatherchecker.model.city.container.MinutelyValue;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

public class Minutely {

    private final List<MinutelyValue> VALUES;

    public Minutely(JSONArray arr, Long timezoneOffset) throws JSONException {
        VALUES = new LinkedList<>();

        for (int i = 0; i < arr.length(); i++)
            VALUES.add(new MinutelyValue(arr.getJSONObject(i), timezoneOffset));
    }

    public JSONArray toJSON(Long timezoneOffset) throws JSONException {
        JSONArray arr = new JSONArray();

        for (MinutelyValue val : VALUES)
            arr.put(val.toJSON(timezoneOffset));

        return arr;
    }

    public List<MinutelyValue> getVALUES() {
        return VALUES;
    }

}
