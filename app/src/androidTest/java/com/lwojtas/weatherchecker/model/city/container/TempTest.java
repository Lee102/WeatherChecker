package com.lwojtas.weatherchecker.model.city.container;

import com.lwojtas.weatherchecker.model.AppData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TempTest {

    private Temp temp;

    private final String APP_DATA = "{\"settings\":{\"units\":0,\"preciseDecimals\":2,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_1 = "{\"settings\":{\"units\":1,\"preciseDecimals\":4,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_2 = "{\"settings\":{\"units\":2,\"preciseDecimals\":2,\"locale\":\"pl\"},\"cities\":[]}";

    @Before
    public void setUp() throws JSONException {
        String tempJSON = "{\"day\":10.86,\"min\":10.06,\"max\":12.84,\"night\":10.27,\"eve\":12.84,\"morn\":11.4}";

        temp = new Temp(new JSONObject(tempJSON));
    }

    @Test
    public void toJSON() throws JSONException {
        String dayJSON = "day";
        String minJSON = "min";
        String maxJSON = "max";
        String nightJSON = "night";
        String eveJSON = "eve";
        String mornJSON = "morn";

        JSONObject obj = temp.toJSON();
        assertEquals(10.86d, obj.getDouble(dayJSON), 0d);
        assertEquals(10.06d, obj.getDouble(minJSON), 0d);
        assertEquals(12.84d, obj.getDouble(maxJSON), 0d);
        assertEquals(10.27d, obj.getDouble(nightJSON), 0d);
        assertEquals(12.84d, obj.getDouble(eveJSON), 0d);
        assertEquals(11.4d, obj.getDouble(mornJSON), 0d);
    }

    @Test
    public void getDayAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("10.86K", temp.getDayAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("10.8600°C", temp.getDayAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("10,86°F", temp.getDayAsString());
    }

    @Test
    public void getMinAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("10.06K", temp.getMinAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("10.0600°C", temp.getMinAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("10,06°F", temp.getMinAsString());
    }

    @Test
    public void getMaxAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("12.84K", temp.getMaxAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("12.8400°C", temp.getMaxAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("12,84°F", temp.getMaxAsString());
    }

    @Test
    public void getNightAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("10.27K", temp.getNightAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("10.2700°C", temp.getNightAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("10,27°F", temp.getNightAsString());
    }

    @Test
    public void getEveAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("12.84K", temp.getEveAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("12.8400°C", temp.getEveAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("12,84°F", temp.getEveAsString());
    }

    @Test
    public void getMornAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("11.40K", temp.getMornAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("11.4000°C", temp.getMornAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("11,40°F", temp.getMornAsString());
    }

}