package com.lwojtas.weatherchecker.model.city;

import com.lwojtas.weatherchecker.model.AppData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CurrentTest {

    private Current current;
    private Current current1;

    private final String APP_DATA = "{\"settings\":{\"units\":0,\"decimals\":0,\"preciseDecimals\":0,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_1 = "{\"settings\":{\"units\":1,\"decimals\":1,\"preciseDecimals\":4,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_2 = "{\"settings\":{\"units\":2,\"decimals\":2,\"preciseDecimals\":2,\"locale\":\"pl\"},\"cities\":[]}";

    @Before
    public void setUp() throws JSONException {
        String currentJSON = "{\"dt\":1602399836,\"sunrise\":1602392057,\"sunset\":1602431438,\"temp\":10.06,\"feels_like\":7.32,\"pressure\":1018,\"humidity\":93,\"dew_point\":8.98,\"uvi\":1.76,\"clouds\":20,\"visibility\":10000,\"wind_speed\":3.6,\"wind_deg\":310,\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}]}";
        String current1JSON = "{\"dt\":1602399836,\"sunrise\":1602392057,\"sunset\":1602431438,\"temp\":10.06,\"feels_like\":7.32,\"pressure\":1018,\"humidity\":93,\"dew_point\":8.98,\"uvi\":1.76,\"clouds\":20,\"visibility\":10000,\"wind_speed\":3.6,\"wind_deg\":310,\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"rain\":{\"1h\":2.46},\"snow\":{\"1h\":2.46}}";

        current = new Current(new JSONObject(currentJSON), TimeZone.getTimeZone("Europe/Warsaw"));
        current1 = new Current(new JSONObject(current1JSON), TimeZone.getTimeZone("Europe/Warsaw"));
    }

    @Test
    public void toJSON() throws JSONException {
        String sunriseJSON = "sunrise";
        String sunsetJSON = "sunset";
        String tempJSON = "temp";
        String feelsLikeJSON = "feels_like";
        String uviJSON = "uvi";
        String visibilityJSON = "visibility";
        String rain1hJSON = "rain";
        String snow1hJSON = "snow";
        String h1JSON = "1h";

        JSONObject obj;

        obj = current.toJSON();
        assertEquals(1602392057L, obj.getLong(sunriseJSON));
        assertEquals(1602431438L, obj.getLong(sunsetJSON));
        assertEquals(10.06d, obj.getDouble(tempJSON), 0d);
        assertEquals(7.32d, obj.getDouble(feelsLikeJSON), 0d);
        assertEquals(1.76d, obj.getDouble(uviJSON), 0d);
        assertEquals(10000d, obj.getDouble(visibilityJSON), 0d);
        assertFalse(obj.has(rain1hJSON));
        assertFalse(obj.has(snow1hJSON));

        obj = current1.toJSON();
        assertTrue(obj.has(rain1hJSON));
        assertTrue(obj.getJSONObject(rain1hJSON).has(h1JSON));
        assertEquals(2.46d, obj.getJSONObject(rain1hJSON).getDouble(h1JSON), 0d);
        assertTrue(obj.has(snow1hJSON));
        assertTrue(obj.getJSONObject(snow1hJSON).has(h1JSON));
        assertEquals(2.46d, obj.getJSONObject(snow1hJSON).getDouble(h1JSON), 0d);
    }

    @Test
    public void getSunriseAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("06:54", current.getSunriseAsString());
    }

    @Test
    public void getSunsetAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("17:50", current.getSunsetAsString());
    }

    @Test
    public void getTempAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("10K", current.getTempAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("10.0600°C", current.getTempAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("10,06°F", current.getTempAsString());
    }

    @Test
    public void getFeelsLikeAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("7K", current.getFeelsLikeAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("7.3200°C", current.getFeelsLikeAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("7,32°F", current.getFeelsLikeAsString());
    }

    @Test
    public void getUviAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("2", current.getUviAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("1.7600", current.getUviAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("1,76", current.getUviAsString());
    }

    @Test
    public void getVisibilityAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("10000 m", current.getVisibilityAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("10000.0 m", current.getVisibilityAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("10000,00 m", current.getVisibilityAsString());
    }

    @Test
    public void rain1hExists() {
        assertFalse(current.rain1hExists());
        assertTrue(current1.rain1hExists());
    }

    @Test
    public void getRain1hAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("2 mm", current1.getRain1hAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("2.4600 mm", current1.getRain1hAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("2,46 mm", current1.getRain1hAsString());
    }

    @Test
    public void snow1hExists() {
        assertFalse(current.snow1hExists());
        assertTrue(current1.snow1hExists());
    }

    @Test
    public void getSnow1hAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("2 mm", current1.getSnow1hAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("2.4600 mm", current1.getSnow1hAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("2,46 mm", current1.getSnow1hAsString());
    }

}