package com.lwojtas.weatherchecker.model.city.container;

import com.lwojtas.weatherchecker.model.AppData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class MinutelyValueTest {

    private MinutelyValue minutelyValue;

    private final String APP_DATA = "{\"settings\":{\"preciseDecimals\":2,\"locale\":\"en\"},\"cities\":[]}";

    @Before
    public void setUp() throws JSONException {
        String minutelyValueJSON = "{\"dt\":1602400920,\"precipitation\":0.4438}";

        minutelyValue = new MinutelyValue(new JSONObject(minutelyValueJSON), TimeZone.getTimeZone("Europe/Warsaw"));
    }

    @Test
    public void toJSON() throws JSONException {
        String dtJSON = "dt";
        String precipitationJSON = "precipitation";

        JSONObject obj = minutelyValue.toJSON();
        assertEquals(1602400920L, obj.getLong(dtJSON));
        assertEquals(0.4438, obj.getDouble(precipitationJSON), 0d);
    }

    @Test
    public void getDtAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("09:22", minutelyValue.getDtAsString());
    }

    @Test
    public void getPrecipitationAsString() throws JSONException {
        String appData1 = "{\"settings\":{\"preciseDecimals\":3,\"locale\":\"en\"},\"cities\":[]}";
        String appData2 = "{\"settings\":{\"preciseDecimals\":4,\"locale\":\"pl\"},\"cities\":[]}";

        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("0.44", minutelyValue.getPrecipitationAsString());
        AppData.fromJSON(new JSONObject(appData1));
        assertEquals("0.444", minutelyValue.getPrecipitationAsString());
        AppData.fromJSON(new JSONObject(appData2));
        assertEquals("0,4438", minutelyValue.getPrecipitationAsString());
    }

}