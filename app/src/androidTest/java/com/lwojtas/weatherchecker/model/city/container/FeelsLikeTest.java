package com.lwojtas.weatherchecker.model.city.container;

import com.lwojtas.weatherchecker.model.AppData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FeelsLikeTest {

    private FeelsLike feelsLike;
    private final String APP_DATA_1 = "{\"settings\":{\"units\":0,\"preciseDecimals\":2,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_2 = "{\"settings\":{\"units\":1,\"preciseDecimals\":4,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_3 = "{\"settings\":{\"units\":2,\"preciseDecimals\":2,\"locale\":\"pl\"},\"cities\":[]}";

    @Before
    public void setUp() throws JSONException {
        String feelsLikeJSON = "{\"morn\":8.52,\"day\":8.26,\"eve\":9.66,\"night\":7.36}";

        feelsLike = new FeelsLike(new JSONObject(feelsLikeJSON));
    }

    @Test
    public void toJSON() throws JSONException {
        String mornJSON = "morn";
        String dayJSON = "day";
        String eveJSON = "eve";
        String nightJSON = "night";

        JSONObject obj = feelsLike.toJSON();
        assertEquals(8.52d, obj.getDouble(mornJSON), 0d);
        assertEquals(8.26d, obj.getDouble(dayJSON), 0d);
        assertEquals(9.66d, obj.getDouble(eveJSON), 0d);
        assertEquals(7.36d, obj.getDouble(nightJSON), 0d);
    }

    @Test
    public void getMornAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("8.52K", feelsLike.getMornAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("8.5200°C", feelsLike.getMornAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_3));
        assertEquals("8,52°F", feelsLike.getMornAsString());
    }

    @Test
    public void getDayAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("8.26K", feelsLike.getDayAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("8.2600°C", feelsLike.getDayAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_3));
        assertEquals("8,26°F", feelsLike.getDayAsString());
    }

    @Test
    public void getEveAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("9.66K", feelsLike.getEveAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("9.6600°C", feelsLike.getEveAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_3));
        assertEquals("9,66°F", feelsLike.getEveAsString());
    }

    @Test
    public void getNightAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("7.36K", feelsLike.getNightAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("7.3600°C", feelsLike.getNightAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_3));
        assertEquals("7,36°F", feelsLike.getNightAsString());
    }

}