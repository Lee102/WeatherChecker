package com.lwojtas.weatherchecker.model.city;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class HourlyTest {

    @Test
    public void toJSON() throws JSONException {
        String hourlyJSON = "[{\"dt\":1602399600,\"temp\":10.06,\"feels_like\":7.26,\"pressure\":1018,\"humidity\":93,\"dew_point\":8.98,\"clouds\":20,\"visibility\":10000,\"wind_speed\":3.68,\"wind_deg\":328,\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"pop\":0.05},{\"dt\":1602403200,\"temp\":10.66,\"feels_like\":7.8,\"pressure\":1018,\"humidity\":80,\"dew_point\":7.36,\"clouds\":59,\"visibility\":10000,\"wind_speed\":3.2,\"wind_deg\":335,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"pop\":0.09},{\"dt\":1602406800,\"temp\":11.34,\"feels_like\":8.5,\"pressure\":1018,\"humidity\":70,\"dew_point\":6.07,\"clouds\":83,\"visibility\":10000,\"wind_speed\":2.77,\"wind_deg\":343,\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"pop\":0.09},{\"dt\":1602410400,\"temp\":12.53,\"feels_like\":9.36,\"pressure\":1018,\"humidity\":60,\"dew_point\":4.98,\"clouds\":95,\"visibility\":10000,\"wind_speed\":2.92,\"wind_deg\":335,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"pop\":0.05},{\"dt\":1602414000,\"temp\":13.23,\"feels_like\":9.84,\"pressure\":1018,\"humidity\":53,\"dew_point\":3.87,\"clouds\":99,\"visibility\":10000,\"wind_speed\":2.92,\"wind_deg\":332,\"weather\":[{\"id\":804,\"main\":\"Clouds\",\"description\":\"overcast clouds\",\"icon\":\"04d\"}],\"pop\":0.05}]";
        Hourly hourly = new Hourly(new JSONArray(hourlyJSON), TimeZone.getTimeZone("Europe/Warsaw"));

        JSONArray arr = hourly.toJSON();
        assertEquals(hourly.getVALUES().size(), arr.length());
    }

}