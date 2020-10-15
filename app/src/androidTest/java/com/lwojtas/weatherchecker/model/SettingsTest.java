package com.lwojtas.weatherchecker.model;

import com.lwojtas.weatherchecker.util.exception.IllegalNegativeNumberException;
import com.lwojtas.weatherchecker.util.exception.IllegalNegativeOrZeroNumberException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SettingsTest {

    private Settings settings;

    @Before
    public void setUp() throws Exception {
        String settingsJSON = "{\"units\":2,\"decimals\":1,\"preciseDecimals\":7,\"weatherActualThreshold\":12,\"locale\":\"pl\",\"updateMode\":1,\"timeout\":12345,\"threadPool\":7,\"appID\":\"test\"}";

        settings = new Settings(new JSONObject(settingsJSON));
    }

    @Test
    public void toJSON() throws JSONException {
        String unitsJSON = "units";
        String decimalsJSON = "decimals";
        String preciseDecimalsJSON = "preciseDecimals";
        String weatherActualThresholdJSON = "weatherActualThreshold";
        String localeJSON = "locale";
        String updateModeJSON = "updateMode";
        String timeoutJSON = "timeout";
        String threadPoolJSON = "threadPool";
        String appIdJSON = "appID";

        JSONObject obj = settings.toJSON();
        assertEquals(2, obj.getInt(unitsJSON));
        assertEquals(1, obj.getInt(decimalsJSON));
        assertEquals(7, obj.getInt(preciseDecimalsJSON));
        assertEquals(12, obj.getInt(weatherActualThresholdJSON));
        assertEquals("pl", obj.getString(localeJSON));
        assertEquals(1, obj.getInt(updateModeJSON));
        assertEquals(12345, obj.getInt(timeoutJSON));
        assertEquals(7, obj.getInt(threadPoolJSON));
        assertEquals("test", obj.getString(appIdJSON));
    }

    @Test
    public void getUnitString() {
        settings.setUnits(Settings.Units.STANDARD);
        assertEquals("K", settings.getUnitString(Settings.UnitType.TEMP));
        assertEquals("m/s", settings.getUnitString(Settings.UnitType.WIND_SPEED));
        settings.setUnits(Settings.Units.METRIC);
        assertEquals("°C", settings.getUnitString(Settings.UnitType.TEMP));
        assertEquals("m/s", settings.getUnitString(Settings.UnitType.WIND_SPEED));
        settings.setUnits(Settings.Units.IMPERIAL);
        assertEquals("°F", settings.getUnitString(Settings.UnitType.TEMP));
        assertEquals("mph", settings.getUnitString(Settings.UnitType.WIND_SPEED));
    }

    @Test(expected = IllegalNegativeNumberException.class)
    public void setDecimals() throws IllegalNegativeNumberException {
        settings.setDecimals(-1);
    }

    @Test(expected = IllegalNegativeNumberException.class)
    public void setPreciseDecimals() throws IllegalNegativeNumberException {
        settings.setPreciseDecimals(-1);
    }

    @Test(expected = IllegalNegativeNumberException.class)
    public void setWeatherActualThreshold() throws IllegalNegativeNumberException {
        settings.setWeatherActualThreshold(-1);
    }

    @Test(expected = IllegalNegativeOrZeroNumberException.class)
    public void setTimeout() throws IllegalNegativeOrZeroNumberException {
        settings.setTimeout(0);
    }

    @Test(expected = IllegalNegativeOrZeroNumberException.class)
    public void setThreadPool() throws IllegalNegativeOrZeroNumberException {
        settings.setThreadPool(0);
    }

}