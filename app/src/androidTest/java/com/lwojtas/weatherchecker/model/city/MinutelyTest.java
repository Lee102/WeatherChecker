package com.lwojtas.weatherchecker.model.city;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class MinutelyTest {

    @Test
    public void toJSON() throws JSONException {
        String minutelyJSON = "[{\"dt\":1602399840,\"precipitation\":0},{\"dt\":1602399900,\"precipitation\":0},{\"dt\":1602399960,\"precipitation\":0},{\"dt\":1602400020,\"precipitation\":0},{\"dt\":1602400080,\"precipitation\":0},{\"dt\":1602400140,\"precipitation\":0},{\"dt\":1602400200,\"precipitation\":0},{\"dt\":1602400260,\"precipitation\":0},{\"dt\":1602400320,\"precipitation\":0}]";
        Minutely minutely = new Minutely(new JSONArray(minutelyJSON), TimeZone.getTimeZone("Europe/Warsaw"));

        JSONArray arr = minutely.toJSON();
        assertEquals(minutely.getVALUES().size(), arr.length());
    }

}