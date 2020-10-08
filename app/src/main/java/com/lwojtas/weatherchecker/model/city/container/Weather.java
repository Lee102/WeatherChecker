package com.lwojtas.weatherchecker.model.city.container;

import org.json.JSONException;
import org.json.JSONObject;

public class Weather {

    private final String MAIN_JSON = "main";
    private String main;
    private final String DESCRIPTION_JSON = "description";
    private String description;
    private final String ICON_JSON = "icon";
    private String icon;

    public Weather(JSONObject obj) throws JSONException {
        main = obj.getString(MAIN_JSON);
        description = formatDescription(obj.getString(DESCRIPTION_JSON));
        icon = obj.getString(ICON_JSON);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put(MAIN_JSON, main);
        obj.put(DESCRIPTION_JSON, description);
        obj.put(ICON_JSON, icon);

        return obj;
    }

    private String formatDescription(String description) {
        StringBuilder sb = new StringBuilder();
        boolean space = true;

        for (char c : description.toCharArray()) {
            if (c == ' ' && !space) {
                sb.append(c);
                space = true;
            } else {
                if (!space)
                    sb.append(c);
                else
                    sb.append(Character.toUpperCase(c));
                space = false;
            }
        }

        return sb.toString();
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

}
