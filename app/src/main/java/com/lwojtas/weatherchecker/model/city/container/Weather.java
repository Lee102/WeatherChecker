package com.lwojtas.weatherchecker.model.city.container;

import org.json.JSONException;
import org.json.JSONObject;

public class Weather {

    private final String idJSON = "id";
    private Integer id;
    private final String mainJSON = "main";
    private String main;
    private final String descriptionJSON = "description";
    private String description;
    private final String iconJSON = "icon";
    private String icon;

    public Weather(JSONObject obj) throws JSONException {
        id = obj.getInt(idJSON);
        main = obj.getString(mainJSON);
        description = formatDescription(obj.getString(descriptionJSON));
        icon = obj.getString(iconJSON);
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put(idJSON, id);
        obj.put(mainJSON, main);
        obj.put(descriptionJSON, description);
        obj.put(iconJSON, icon);

        return obj;
    }

    private String formatDescription(String description) {
        StringBuilder sb = new StringBuilder();
        boolean space = true;

        for (char c : description.toCharArray()) {
            if (c == ' ' && !space) {
                sb.append(c);
                space = true;
            }
            else {
                if (!space)
                    sb.append(c);
                else
                    sb.append(Character.toUpperCase(c));
                space = false;
            }
        }

        return sb.toString();
    }

    public Integer getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}
