package com.lwojtas.weatherchecker.view;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.lwojtas.weatherchecker.R;
import com.lwojtas.weatherchecker.model.city.Current;

import java.lang.reflect.Field;

public class CurrentView extends ViewInitializer {

    private final Current current;

    public CurrentView(Current current) {
        this.current = current;
    }

    public void initialize(Context context, ViewStub stub) throws Exception {
        stub.setLayoutResource(R.layout.city_common);
        View view = stub.inflate();

        LinearLayout linearLayout = view.findViewById(R.id.cityCommonLinearLayout);
        linearLayout.addView(buildHeaderView(context));
        linearLayout.addView(buildRestView(context));
    }

    private LinearLayout buildHeaderView(Context context) throws Exception {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setTag(Boolean.FALSE);

        ImageView imageView = new ImageView(context);
        Field field = (R.drawable.class).getDeclaredField("w" + current.getWeather().get(0).getIcon());
        imageView.setImageResource(field.getInt(field));
        linearLayout.addView(imageView);

        TableLayout tableLayout = buildTableLayout(context);
        TableRow row;

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Temp"));
        row.addView(buildTableRowTextView(context, current.getTempAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Feels Like"));
        row.addView(buildTableRowTextView(context, current.getFeelsLikeAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, ""));
        row.addView(buildTableRowTextView(context, current.getWeather().get(0).getDescription()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Rain"));
        String rain = "";
        if (current.getRain1h() != null)
            rain += current.getRain1hAsString();
        else
            rain += "-";
        row.addView(buildTableRowTextView(context, rain));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Snow"));
        String snow = "";
        if (current.getSnow1h() != null)
            snow += current.getSnow1hAsString();
        else
            snow += "-";
        row.addView(buildTableRowTextView(context, snow));
        tableLayout.addView(row);

        linearLayout.addView(tableLayout);
        return linearLayout;
    }

    private TableLayout buildRestView(Context context) {
        TableLayout tableLayout = buildTableLayout(context);
        TableRow row;

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Date"));
        row.addView(buildTableRowTextView(context, current.getDtAsString("d-MM-yyyy")));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, "Sunrise"));
        row.addView(buildTableRowTextView(context, current.getSunriseAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Sunset"));
        row.addView(buildTableRowTextView(context, current.getSunsetAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, "Uvi"));
        row.addView(buildTableRowTextView(context, current.getUviAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Visibility"));
        row.addView(buildTableRowTextView(context, current.getVisibilityAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, "Pressure"));
        row.addView(buildTableRowTextView(context, current.getPressureAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Humidity"));
        row.addView(buildTableRowTextView(context, current.getHumidityAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, "Dew Point"));
        row.addView(buildTableRowTextView(context, current.getDewPointAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Clouds"));
        row.addView(buildTableRowTextView(context, current.getCloudsAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, "Wind Speed"));
        row.addView(buildTableRowTextView(context, current.getWindSpeedAsString()));
        tableLayout.addView(row);

        boolean even = false;
        if (current.getWindGust() != null) {
            row = buildTableRow(context, false, false);
            row.addView(buildTableRowTextView(context, "Wind Gust"));
            row.addView(buildTableRowTextView(context, current.getWindGustAsString()));
            tableLayout.addView(row);
            even = true;
        }

        row = buildTableRow(context, false, even);
        row.addView(buildTableRowTextView(context, "Wind Deg"));
        row.addView(buildTableRowTextView(context, current.getWindDegAsString()));
        tableLayout.addView(row);

        return tableLayout;
    }

}
