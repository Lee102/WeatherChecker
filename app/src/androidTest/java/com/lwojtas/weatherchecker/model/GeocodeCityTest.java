package com.lwojtas.weatherchecker.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GeocodeCityTest {

    private GeocodeCity geocodeCity;

    private final String APP_DATA = "{\"settings\":{\"units\":0,\"decimals\":0,\"preciseDecimals\":0,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_1 = "{\"settings\":{\"units\":1,\"decimals\":1,\"preciseDecimals\":4,\"locale\":\"en\"},\"cities\":[]}";
    private final String APP_DATA_2 = "{\"settings\":{\"units\":2,\"decimals\":2,\"preciseDecimals\":2,\"locale\":\"pl\"},\"cities\":[]}";

    @Before
    public void setUp() throws JSONException {
        String geocodeCityJSON = "{\"display_name\":\"Warsaw, Masovian Voivodeship, Poland\",\"lat\":52.22,\"lon\":21.01}";

        geocodeCity = new GeocodeCity(new JSONObject(geocodeCityJSON));
    }

    @Test
    public void getDisplayNameParts() {
        assertEquals(3, geocodeCity.getDisplayNameParts().size());
        assertEquals("Warsaw", geocodeCity.getDisplayNameParts().get(0));
        assertEquals("Masovian Voivodeship", geocodeCity.getDisplayNameParts().get(1));
        assertEquals("Poland", geocodeCity.getDisplayNameParts().get(2));
    }

    @Test
    public void getLatAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("52", geocodeCity.getLatAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("52.2200", geocodeCity.getLatAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("52,22", geocodeCity.getLatAsString());
    }

    @Test
    public void getLonAsString() throws JSONException {
        AppData.fromJSON(new JSONObject(APP_DATA));
        assertEquals("21", geocodeCity.getLonAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_1));
        assertEquals("21.0100", geocodeCity.getLonAsString());
        AppData.fromJSON(new JSONObject(APP_DATA_2));
        assertEquals("21,01", geocodeCity.getLonAsString());
    }

}