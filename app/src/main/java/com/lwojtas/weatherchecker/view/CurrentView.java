package com.lwojtas.weatherchecker.view;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.lwojtas.weatherchecker.R;
import com.lwojtas.weatherchecker.model.city.Current;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CurrentView extends ViewInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(CurrentView.class);

    private final Current CURRENT;

    public CurrentView(Current current) {
        this.CURRENT = current;
    }

    public void initialize(Context context, ViewStub stub) throws Exception {
        LOG.trace("initialize");

        if (context != null && stub != null && CURRENT != null) {
            stub.setLayoutResource(R.layout.weather_common);
            View view = stub.inflate();

            LinearLayout linearLayout = view.findViewById(R.id.weatherCommonLinearLayout);
            linearLayout.addView(buildHeaderLinearLayout(context));
            linearLayout.addView(buildRecordsTableLayout(context));
        }
    }

    private LinearLayout buildHeaderLinearLayout(Context context) throws Exception {
        LOG.trace("buildHeaderLinearLayout");

        LinearLayout linearLayout = buildShortHeaderLinearLayout(context, CURRENT.getWeather(), CURRENT.getDtAsString("d-MM-yyyy"));
        linearLayout.setBackgroundColor(context.getColor(R.color.headerRowColor));

        return linearLayout;
    }

    private TableLayout buildRecordsTableLayout(Context context) {
        LOG.trace("buildRecordsTableLayout");

        TableLayout tableLayout = buildTableLayout(context);
        TableRow row;

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_temp)));
        row.addView(buildTableRowTextView(context, CURRENT.getTempAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_feels_like)));
        row.addView(buildTableRowTextView(context, CURRENT.getFeelsLikeAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_sunrise)));
        row.addView(buildTableRowTextView(context, CURRENT.getSunriseAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_sunset)));
        row.addView(buildTableRowTextView(context, CURRENT.getSunsetAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_uvi)));
        row.addView(buildTableRowTextView(context, CURRENT.getUviAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_visibility)));
        row.addView(buildTableRowTextView(context, CURRENT.getVisibilityAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_pressure)));
        row.addView(buildTableRowTextView(context, CURRENT.getPressureAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_humidity)));
        row.addView(buildTableRowTextView(context, CURRENT.getHumidityAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_dew_point)));
        row.addView(buildTableRowTextView(context, CURRENT.getDewPointAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_clouds)));
        row.addView(buildTableRowTextView(context, CURRENT.getCloudsAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_wind_speed)));
        row.addView(buildTableRowTextView(context, CURRENT.getWindSpeedAsString()));
        tableLayout.addView(row);

        boolean even = true;
        if (CURRENT.windGustExists()) {
            row = buildTableRow(context, false, true);
            row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_wind_gust)));
            row.addView(buildTableRowTextView(context, CURRENT.getWindGustAsString()));
            tableLayout.addView(row);
            even = false;
        }

        row = buildTableRow(context, false, even);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_wind_deg)));
        row.addView(buildTableRowTextView(context, CURRENT.getWindDegAsString()));
        tableLayout.addView(row);
        even = !even;

        row = buildTableRow(context, false, even);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_rain)));
        String rain = "";
        if (CURRENT.rain1hExists())
            rain += CURRENT.getRain1hAsString();
        else
            rain += "-";
        row.addView(buildTableRowTextView(context, rain));
        tableLayout.addView(row);
        even = !even;

        row = buildTableRow(context, false, even);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_snow)));
        String snow = "";
        if (CURRENT.snow1hExists())
            snow += CURRENT.getSnow1hAsString();
        else
            snow += "-";
        row.addView(buildTableRowTextView(context, snow));
        tableLayout.addView(row);

        return tableLayout;
    }

}
