package com.lwojtas.weatherchecker.model.city;

import com.lwojtas.weatherchecker.model.city.container.DailyValue;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

public class Daily {

    private final List<DailyValue> VALUES;

    public Daily(JSONArray arr, TimeZone timeZone) throws JSONException {
        VALUES = new LinkedList<>();

        for (int i = 0; i < arr.length(); i++)
            VALUES.add(new DailyValue(arr.getJSONObject(i), timeZone));
    }

    public JSONArray toJSON() throws JSONException {
        JSONArray arr = new JSONArray();

        for (DailyValue val : VALUES)
            arr.put(val.toJSON());

        return arr;
    }

    public List<DailyValue> getVALUES() {
        return VALUES;
    }

}
