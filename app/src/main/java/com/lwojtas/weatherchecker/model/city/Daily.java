package com.lwojtas.weatherchecker.model.city;

import com.lwojtas.weatherchecker.model.city.container.DailyValue;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

public class Daily {

    private final List<DailyValue> values;

    public Daily(JSONArray arr, Long timezoneOffset) throws JSONException {
        values = new LinkedList<>();

        for (int i = 0; i < arr.length(); i++)
            values.add(new DailyValue(arr.getJSONObject(i), timezoneOffset));
    }

    public JSONArray toJSON(Long timezoneOffset) throws JSONException {
        JSONArray arr = new JSONArray();

        for (DailyValue val : values)
            arr.put(val.toJSON(timezoneOffset));

        return arr;
    }

    public List<DailyValue> getValues() {
        return values;
    }
}
