package com.lwojtas.weatherchecker.model.city.container;

import com.lwojtas.weatherchecker.model.AppData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CommonTest {

    private HourlyValue hourlyValue;
    private HourlyValue hourlyValue1;

    private final String APP_DATA = "{\"settings\":{\"units\":0,\"decimals\":0,\"preciseDecimals\":0,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_1 = "{\"settings\":{\"units\":1,\"decimals\":1,\"preciseDecimals\":4,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_2 = "{\"settings\":{\"units\":2,\"decimals\":2,\"preciseDecimals\":2,\"locale\":\"pl\"},\"cities\":[]}";

    @Before
    public void setUp() throws JSONException {
        String hourlyValueJSON = "{\"dt\":1602399600,\"temp\":10.06,\"feels_like\":7.26,\"pressure\":1018,\"humidity\":93,\"dew_point\":8.98,\"clouds\":20,\"visibility\":10000,\"wind_speed\":3.68,\"wind_deg\":328,\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"pop\":0.05}";
        String hourlyValue1JSON = "{\"dt\":1602054000,\"temp\":10.06,\"feels_like\":7.26,\"pressure\":1018,\"humidity\":93,\"dew_point\":8.98,\"clouds\":20,\"visibility\":10000,\"wind_speed\":3.68,\"wind_gust\":7.89,\"wind_deg\":328,\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"pop\":0.05}";

        hourlyValue = new HourlyValue(new JSONObject(hourlyValueJSON), TimeZone.getTimeZone("Europe/Warsaw"));
        hourlyValue1 = new HourlyValue(new JSONObject(hourlyValue1JSON), TimeZone.getTimeZone("Europe/Warsaw"));
    }

    @Test
    public void toJSON() throws JSONException {
        String dtJSON = "dt";
        String pressureJSON = "pressure";
        String humidityJSON = "humidity";
        String dewPointJSON = "dew_point";
        String cloudsJSON = "clouds";
        String windSpeedJSON = "wind_speed";
        String windGustJSON = "wind_gust";
        String windDegJSON = "wind_deg";
        String weatherJSON = "weather";

        JSONObject obj;

        obj = hourlyValue.toJSON();
        assertEquals(1602399600L, obj.getLong(dtJSON));
        assertEquals(1018d, obj.getDouble(pressureJSON), 0d);
        assertEquals(93d, obj.getDouble(humidityJSON), 0d);
        assertEquals(8.98d, obj.getDouble(dewPointJSON), 0d);
        assertEquals(20d, obj.getDouble(cloudsJSON), 0d);
        assertEquals(3.68d, obj.getDouble(windSpeedJSON), 0d);
        assertFalse(obj.has(windGustJSON));
        assertEquals(328d, obj.getDouble(windDegJSON), 0d);
        assertTrue(obj.has(weatherJSON));

        obj = hourlyValue1.toJSON();
        assertTrue(obj.has(windGustJSON));
        assertEquals(7.89d, obj.getDouble(windGustJSON), 0d);
    }

    @Test
    public void getAsString() {
        assertEquals("1.274qwe", Common.getAsString(1.2740123, 3, Locale.forLanguageTag("en"), "qwe", false));
        assertEquals("1,3 asd", Common.getAsString(1.2740123, 1, Locale.forLanguageTag("pl"), "asd", true));
    }

    @Test
    public void getDt() {
        assertEquals(new Date(1602399600000L), hourlyValue.getDt());
    }

    @Test
    public void getDtAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("11-10-2020", hourlyValue.getDtAsString("d-MM-yyyy"));
        assertEquals("7-10-2020", hourlyValue1.getDtAsString("d-MM-yyyy"));
        assertEquals("Sun", hourlyValue.getDtAsString("E"));
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("niedz.", hourlyValue.getDtAsString("E"));
    }

    @Test
    public void getPressureAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("1018 hPa", hourlyValue.getPressureAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("1018.0 hPa", hourlyValue.getPressureAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("1018,00 hPa", hourlyValue.getPressureAsString());
    }

    @Test
    public void getHumidityAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("93%", hourlyValue.getHumidityAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("93.0%", hourlyValue.getHumidityAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("93,00%", hourlyValue.getHumidityAsString());
    }

    @Test
    public void getDewPointAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("9K", hourlyValue.getDewPointAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("8.9800°C", hourlyValue.getDewPointAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("8,98°F", hourlyValue.getDewPointAsString());
    }

    @Test
    public void getCloudsAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("20%", hourlyValue.getCloudsAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("20.0%", hourlyValue.getCloudsAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("20,00%", hourlyValue.getCloudsAsString());
    }

    @Test
    public void getWindSpeedAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("4 m/s", hourlyValue.getWindSpeedAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("3.6800 m/s", hourlyValue.getWindSpeedAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("3,68 mph", hourlyValue.getWindSpeedAsString());
    }

    @Test
    public void windGustExists() {
        assertFalse(hourlyValue.windGustExists());
        assertTrue(hourlyValue1.windGustExists());
    }

    @Test
    public void getWindGustAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("8 m/s", hourlyValue1.getWindGustAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("7.8900 m/s", hourlyValue1.getWindGustAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("7,89 mph", hourlyValue1.getWindGustAsString());
    }

    @Test
    public void getWindDegAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("328°", hourlyValue.getWindDegAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("328.0°", hourlyValue.getWindDegAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("328,00°", hourlyValue.getWindDegAsString());
    }

    @Test
    public void getWeather() {
        assertNotNull(hourlyValue.getWeather());
    }

}