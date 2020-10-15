package com.lwojtas.weatherchecker.model.city.container;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WeatherTest {

    private Weather weather;

    @Before
    public void setUp() throws JSONException {
        String weatherJSON = "{\"description\":\"few clouds\",\"icon\":\"02d\"}";

        weather = new Weather(new JSONObject(weatherJSON));
    }

    @Test
    public void toJSON() throws JSONException {
        String descriptionJSON = "description";
        String iconJSON = "icon";

        JSONObject obj = weather.toJSON();
        assertEquals("Few Clouds", obj.getString(descriptionJSON));
        assertEquals("02d", obj.getString(iconJSON));
    }

    @Test
    public void getDescription() {
        assertEquals("Few Clouds", weather.getDescription());
    }

    @Test
    public void getIcon() {
        assertEquals("02d", weather.getIcon());
    }

}