package com.lwojtas.weatherchecker.view;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.lwojtas.weatherchecker.R;
import com.lwojtas.weatherchecker.model.city.Hourly;
import com.lwojtas.weatherchecker.model.city.container.HourlyValue;

import java.lang.reflect.Field;

public class HourlyView extends ViewInitializer {

    private final Hourly HOURLY;

    public HourlyView(Hourly hourly) {
        this.HOURLY = hourly;
    }

    public void initialize(Context context, ViewStub stub) throws Exception {
        stub.setLayoutResource(R.layout.city_common);
        View view = stub.inflate();

        LinearLayout linearLayout = view.findViewById(R.id.cityCommonLinearLayout);
        linearLayout.addView(buildRecordsTable(context));
    }

    private TableLayout buildRecordsTable(final Context CONTEXT) throws Exception {
        TableLayout tableLayout = buildTableLayout(CONTEXT);

        boolean even = false;
        for (HourlyValue val : HOURLY.getVALUES()) {
            TableRow row = buildTableRow(CONTEXT, false, even);
            row.setTag(val);

            row.addView(buildCollapsedView(CONTEXT, val));

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableRow row = (TableRow) v;
                    HourlyValue val = (HourlyValue) v.getTag();

                    try {
                        LinearLayout linearLayout;

                        if ((Boolean) row.getChildAt(0).getTag())
                            linearLayout = buildCollapsedView(CONTEXT, val);
                        else
                            linearLayout = buildExpandedView(CONTEXT, val);

                        row.removeAllViews();
                        row.addView(linearLayout);
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), v.getResources().getString(R.string.city_hourly_on_click_error_message), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            tableLayout.addView(row);

            even = !even;
        }

        return tableLayout;
    }

    private LinearLayout buildCollapsedView(Context context, HourlyValue val) throws Exception {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setTag(Boolean.FALSE);

        ImageView imageView = new ImageView(context);
        Field field = (R.drawable.class).getDeclaredField("w" + val.getWeather().get(0).getIcon());
        imageView.setImageResource(field.getInt(field));
        linearLayout.addView(imageView);

        TableLayout tableLayout = buildTableLayout(context);
        TableRow row;

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, val.getDtAsString("HH:mm")));
        String rain = context.getResources().getString(R.string.weather_rain_r) + ": ";
        if (val.rain1hExists())
            rain += val.getRain1hAsString();
        else
            rain += "-";
        row.addView(buildTableRowTextView(context, rain));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, val.getTempAsString()));
        String snow = context.getResources().getString(R.string.weather_snow_s) + ": ";
        if (val.snow1hExists())
            snow += val.getSnow1hAsString();
        else
            snow += "-";
        row.addView(buildTableRowTextView(context, snow));
        tableLayout.addView(row);

        linearLayout.addView(tableLayout);
        return linearLayout;
    }

    private LinearLayout buildExpandedView(Context context, HourlyValue val) throws Exception {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setTag(Boolean.TRUE);

        linearLayout.addView(buildExpandedViewHeader(context, val));
        linearLayout.addView(buildExpandedViewRest(context, val));

        return linearLayout;
    }

    private LinearLayout buildExpandedViewHeader(Context context, HourlyValue val) throws Exception {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setBackgroundColor(context.getColor(R.color.headerRowColor));

        ImageView imageView = new ImageView(context);
        Field field = (R.drawable.class).getDeclaredField("w" + val.getWeather().get(0).getIcon());
        imageView.setImageResource(field.getInt(field));
        linearLayout.addView(imageView);

        TableLayout tableLayout = buildTableLayout(context);
        TableRow row;

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_date)));
        row.addView(buildTableRowTextView(context, val.getDtAsString("d-MM-yyyy")));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, ""));
        row.addView(buildTableRowTextView(context, val.getWeather().get(0).getDescription()));
        tableLayout.addView(row);

        linearLayout.addView(tableLayout);
        return linearLayout;
    }

    private TableLayout buildExpandedViewRest(Context context, HourlyValue val) {
        TableLayout tableLayout = buildTableLayout(context);
        TableRow row;

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_temp)));
        row.addView(buildTableRowTextView(context, val.getTempAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_feels_like)));
        row.addView(buildTableRowTextView(context, val.getFeelsLikeAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_visibility)));
        row.addView(buildTableRowTextView(context, val.getVisibilityAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_pressure)));
        row.addView(buildTableRowTextView(context, val.getPressureAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_humidity)));
        row.addView(buildTableRowTextView(context, val.getHumidityAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_dew_point)));
        row.addView(buildTableRowTextView(context, val.getDewPointAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_clouds)));
        row.addView(buildTableRowTextView(context, val.getCloudsAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_pop)));
        row.addView(buildTableRowTextView(context, val.getPopAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_wind_speed)));
        row.addView(buildTableRowTextView(context, val.getWindSpeedAsString()));
        tableLayout.addView(row);

        boolean even = true;
        if (val.windGustExists()) {
            row = buildTableRow(context, false, true);
            row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_wind_gust)));
            row.addView(buildTableRowTextView(context, val.getWindGustAsString()));
            tableLayout.addView(row);
            even = false;
        }

        row = buildTableRow(context, false, even);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_wind_deg)));
        row.addView(buildTableRowTextView(context, val.getWindDegAsString()));
        tableLayout.addView(row);
        even = !even;

        if (val.rain1hExists()) {
            row = buildTableRow(context, false, even);
            row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_rain)));
            row.addView(buildTableRowTextView(context, val.getRain1hAsString()));
            tableLayout.addView(row);
            even = !even;
        }

        if (val.snow1hExists()) {
            row = buildTableRow(context, false, even);
            row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_snow)));
            row.addView(buildTableRowTextView(context, val.getSnow1hAsString()));
            tableLayout.addView(row);
        }

        return tableLayout;
    }

}
