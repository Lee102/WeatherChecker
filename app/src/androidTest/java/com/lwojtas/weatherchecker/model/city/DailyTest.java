package com.lwojtas.weatherchecker.model.city;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Test;

import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DailyTest {

    @Test
    public void toJSON() throws JSONException {
        String dailyJSON = "[{\"dt\":1602410400,\"sunrise\":1602392057,\"sunset\":1602431438,\"temp\":{\"day\":10.86,\"min\":10.06,\"max\":12.84,\"night\":10.27,\"eve\":12.84,\"morn\":11.4},\"feels_like\":{\"day\":8.26,\"night\":7.36,\"eve\":9.66,\"morn\":8.52},\"pressure\":1018,\"humidity\":78,\"dew_point\":7.18,\"wind_speed\":2.77,\"wind_deg\":343,\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":59,\"pop\":0.5,\"rain\":1.12,\"uvi\":1.76},{\"dt\":1602496800,\"sunrise\":1602478561,\"sunset\":1602517703,\"temp\":{\"day\":10.05,\"min\":7.76,\"max\":10.05,\"night\":7.76,\"eve\":8.4,\"morn\":9.06},\"feels_like\":{\"day\":6.49,\"night\":2.71,\"eve\":4.16,\"morn\":5.95},\"pressure\":1017,\"humidity\":58,\"dew_point\":2.17,\"wind_speed\":2.74,\"wind_deg\":32,\"weather\":[{\"id\":502,\"main\":\"Rain\",\"description\":\"heavy intensity rain\",\"icon\":\"10d\"}],\"clouds\":100,\"pop\":1,\"rain\":16.32,\"uvi\":1.82}]";
        Daily daily = new Daily(new JSONArray(dailyJSON), TimeZone.getTimeZone("Europe/Warsaw"));

        JSONArray arr = daily.toJSON();
        assertEquals(daily.getVALUES().size(), arr.length());
    }

}