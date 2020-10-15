package com.lwojtas.weatherchecker.model.city.container;

import com.lwojtas.weatherchecker.model.AppData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DailyValueTest {

    private DailyValue dailyValue;
    private DailyValue dailyValue1;

    private final String APP_DATA = "{\"settings\":{\"units\":0,\"decimals\":0,\"preciseDecimals\":0,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_1 = "{\"settings\":{\"units\":1,\"decimals\":1,\"preciseDecimals\":4,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_2 = "{\"settings\":{\"units\":2,\"decimals\":2,\"preciseDecimals\":2,\"locale\":\"pl\"},\"cities\":[]}";

    @Before
    public void setUp() throws JSONException {
        String dailyValueJSON = "{\"dt\":1602410400,\"sunrise\":1602392057,\"sunset\":1602431438,\"temp\":{\"day\":10.86,\"min\":10.06,\"max\":12.84,\"night\":10.27,\"eve\":12.84,\"morn\":11.4},\"feels_like\":{\"day\":8.26,\"night\":7.36,\"eve\":9.66,\"morn\":8.52},\"pressure\":1018,\"humidity\":78,\"dew_point\":7.18,\"wind_speed\":2.77,\"wind_deg\":343,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":59,\"pop\":0.5,\"uvi\":1.76}";
        String dailyValue1JSON = "{\"dt\":1602410400,\"sunrise\":1602392057,\"sunset\":1602431438,\"temp\":{\"day\":10.86,\"min\":10.06,\"max\":12.84,\"night\":10.27,\"eve\":12.84,\"morn\":11.4},\"feels_like\":{\"day\":8.26,\"night\":7.36,\"eve\":9.66,\"morn\":8.52},\"pressure\":1018,\"humidity\":78,\"dew_point\":7.18,\"wind_speed\":2.77,\"wind_deg\":343,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":59,\"pop\":0.5,\"uvi\":1.76,\"rain\":2.54,\"snow\":2.54}";

        dailyValue = new DailyValue(new JSONObject(dailyValueJSON), TimeZone.getTimeZone("Europe/Warsaw"));
        dailyValue1 = new DailyValue(new JSONObject(dailyValue1JSON), TimeZone.getTimeZone("Europe/Warsaw"));
    }

    @Test
    public void toJSON() throws JSONException {
        String sunriseJSON = "sunrise";
        String sunsetJSON = "sunset";
        String tempJSON = "temp";
        String feelsLikeJSON = "feels_like";
        String uviJSON = "uvi";
        String popJSON = "pop";
        String rainJSON = "rain";
        String snowJSON = "snow";

        JSONObject obj;

        obj = dailyValue.toJSON();
        assertEquals(1602392057L, obj.getLong(sunriseJSON));
        assertEquals(1602431438L, obj.getLong(sunsetJSON));
        assertTrue(obj.has(tempJSON));
        assertTrue(obj.has(feelsLikeJSON));
        assertEquals(1.76d, obj.getDouble(uviJSON), 0d);
        assertEquals(0.5d, obj.getDouble(popJSON), 0d);
        assertFalse(obj.has(rainJSON));
        assertFalse(obj.has(snowJSON));

        obj = dailyValue1.toJSON();
        assertTrue(obj.has(rainJSON));
        assertEquals(2.54d, obj.getDouble(rainJSON), 0d);
        assertTrue(obj.has(snowJSON));
        assertEquals(2.54d, obj.getDouble(snowJSON), 0d);
    }

    @Test
    public void getSunriseAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("06:54", dailyValue.getSunriseAsString());
    }

    @Test
    public void getSunsetAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("17:50", dailyValue.getSunsetAsString());
    }

    @Test
    public void getTemp() {
        assertNotNull(dailyValue.getTemp());
    }

    @Test
    public void getFeelsLike() {
        assertNotNull(dailyValue.getFeelsLike());
    }

    @Test
    public void getUviAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("2", dailyValue.getUviAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("1.7600", dailyValue.getUviAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("1,76", dailyValue.getUviAsString());
    }

    @Test
    public void getPopAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("50%", dailyValue.getPopAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("50.0%", dailyValue.getPopAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("50,00%", dailyValue.getPopAsString());
    }

    @Test
    public void rainExists() {
        assertFalse(dailyValue.rainExists());
        assertTrue(dailyValue1.rainExists());
    }

    @Test
    public void getRainAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("3 mm", dailyValue1.getRainAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("2.5400 mm", dailyValue1.getRainAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("2,54 mm", dailyValue1.getRainAsString());
    }

    @Test
    public void snowExists() {
        assertFalse(dailyValue.snowExists());
        assertTrue(dailyValue1.snowExists());
    }

    @Test
    public void getSnowAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("3 mm", dailyValue1.getSnowAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("2.5400 mm", dailyValue1.getSnowAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("2,54 mm", dailyValue1.getSnowAsString());
    }

}