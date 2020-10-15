package com.lwojtas.weatherchecker.model.city.container;

import com.lwojtas.weatherchecker.model.AppData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HourlyValueTest {

    private HourlyValue hourlyValue;
    private HourlyValue hourlyValue1;

    private final String APP_DATA = "{\"settings\":{\"units\":0,\"decimals\":0,\"preciseDecimals\":0,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_1 = "{\"settings\":{\"units\":1,\"decimals\":1,\"preciseDecimals\":4,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_2 = "{\"settings\":{\"units\":2,\"decimals\":2,\"preciseDecimals\":2,\"locale\":\"pl\"},\"cities\":[]}";

    @Before
    public void setUp() throws JSONException {
        String hourlyValueJSON = "{\"dt\":1602399600,\"temp\":10.06,\"feels_like\":7.26,\"pressure\":1018,\"humidity\":93,\"dew_point\":8.98,\"clouds\":20,\"visibility\":10000,\"wind_speed\":3.68,\"wind_deg\":328,\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"pop\":0.05}";
        String hourlyValue1JSON = "{\"dt\":1602399600,\"temp\":10.06,\"feels_like\":7.26,\"pressure\":1018,\"humidity\":93,\"dew_point\":8.98,\"clouds\":20,\"visibility\":10000,\"wind_speed\":3.68,\"wind_deg\":328,\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"pop\":0.05,\"rain\":{\"1h\":2.46},\"snow\":{\"1h\":2.46}}";

        hourlyValue = new HourlyValue(new JSONObject(hourlyValueJSON), TimeZone.getTimeZone("Europe/Warsaw"));
        hourlyValue1 = new HourlyValue(new JSONObject(hourlyValue1JSON), TimeZone.getTimeZone("Europe/Warsaw"));
    }

    @Test
    public void toJSON() throws JSONException {
        String tempJSON = "temp";
        String feelsLikeJSON = "feels_like";
        String visibilityJSON = "visibility";
        String popJSON = "pop";
        String rain1hJSON = "rain";
        String snow1hJSON = "snow";
        String h1JSON = "1h";

        JSONObject obj;

        obj = hourlyValue.toJSON();
        assertEquals(10.06d, obj.getDouble(tempJSON), 0d);
        assertEquals(7.26d, obj.getDouble(feelsLikeJSON), 0d);
        assertEquals(10000d, obj.getDouble(visibilityJSON), 0d);
        assertEquals(0.05d, obj.getDouble(popJSON), 0d);
        assertFalse(obj.has(rain1hJSON));
        assertFalse(obj.has(snow1hJSON));

        obj = hourlyValue1.toJSON();
        assertTrue(obj.has(rain1hJSON));
        assertTrue(obj.getJSONObject(rain1hJSON).has(h1JSON));
        assertEquals(2.46d, obj.getJSONObject(rain1hJSON).getDouble(h1JSON), 0d);
        assertTrue(obj.has(snow1hJSON));
        assertTrue(obj.getJSONObject(snow1hJSON).has(h1JSON));
        assertEquals(2.46d, obj.getJSONObject(snow1hJSON).getDouble(h1JSON), 0d);
    }

    @Test
    public void getTempAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("10K", hourlyValue.getTempAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("10.0600°C", hourlyValue.getTempAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("10,06°F", hourlyValue.getTempAsString());
    }

    @Test
    public void getFeelsLikeAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("7K", hourlyValue.getFeelsLikeAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("7.2600°C", hourlyValue.getFeelsLikeAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("7,26°F", hourlyValue.getFeelsLikeAsString());
    }

    @Test
    public void getVisibilityAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("10000 m", hourlyValue.getVisibilityAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("10000.0 m", hourlyValue.getVisibilityAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("10000,00 m", hourlyValue.getVisibilityAsString());
    }

    @Test
    public void getPopAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("5%", hourlyValue.getPopAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("5.0%", hourlyValue.getPopAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("5,00%", hourlyValue.getPopAsString());
    }

    @Test
    public void rain1hExists() {
        assertFalse(hourlyValue.rain1hExists());
        assertTrue(hourlyValue1.rain1hExists());
    }

    @Test
    public void getRain1hAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("2 mm", hourlyValue1.getRain1hAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("2.4600 mm", hourlyValue1.getRain1hAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("2,46 mm", hourlyValue1.getRain1hAsString());
    }

    @Test
    public void snow1hExists() {
        assertFalse(hourlyValue.snow1hExists());
        assertTrue(hourlyValue1.snow1hExists());
    }

    @Test
    public void getSnow1hAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("2 mm", hourlyValue1.getSnow1hAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("2.4600 mm", hourlyValue1.getSnow1hAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("2,46 mm", hourlyValue1.getSnow1hAsString());
    }

}