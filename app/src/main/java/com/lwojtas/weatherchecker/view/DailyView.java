package com.lwojtas.weatherchecker.view;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.lwojtas.weatherchecker.R;
import com.lwojtas.weatherchecker.model.city.Daily;
import com.lwojtas.weatherchecker.model.city.container.DailyValue;

import java.util.ArrayList;
import java.util.List;

public class DailyView extends ViewInitializer {

    private final Daily DAILY;
    private final List<TableRow> tableRows;

    public DailyView(Daily daily) {
        this.DAILY = daily;
        tableRows = new ArrayList<>();
    }

    public void callOnClick(List<Integer> indexes) {
        for (Integer i : indexes)
            tableRows.get(i).callOnClick();
    }

    public ArrayList<Integer> getExpandedIndexes() {
        ArrayList<Integer> indexes = new ArrayList<>();

        for (int i = 0; i < tableRows.size(); i++) {
            TableRow row = tableRows.get(i);
            if ((Boolean) row.getChildAt(0).getTag())
                indexes.add(i);
        }

        return indexes;
    }

    public void initialize(Context context, ViewStub stub) throws Exception {
        stub.setLayoutResource(R.layout.weather_common);
        View view = stub.inflate();

        LinearLayout linearLayout = view.findViewById(R.id.weatherCommonLinearLayout);
        linearLayout.addView(buildRecordsTable(context));
    }

    private TableLayout buildRecordsTable(final Context CONTEXT) throws Exception {
        TableLayout tableLayout = buildTableLayout(CONTEXT);

        boolean even = false;
        for (DailyValue val : DAILY.getVALUES()) {
            TableRow row = buildTableRow(CONTEXT, false, even);
            row.setTag(val);

            row.addView(buildCollapsedView(CONTEXT, val));

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableRow row = (TableRow) v;
                    DailyValue val = (DailyValue) v.getTag();

                    try {
                        LinearLayout linearLayout;

                        if ((Boolean) row.getChildAt(0).getTag())
                            linearLayout = buildCollapsedView(CONTEXT, val);
                        else
                            linearLayout = buildExpandedView(CONTEXT, val);

                        row.removeAllViews();
                        row.addView(linearLayout);
                    } catch (Exception e) {
                        Toast.makeText(v.getContext(), v.getResources().getString(R.string.weather_daily_on_click_error_message), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            tableLayout.addView(row);
            tableRows.add(row);

            even = !even;
        }

        return tableLayout;
    }

    private LinearLayout buildCollapsedView(Context context, DailyValue val) throws Exception {
        LinearLayout linearLayout = buildLongHeaderLinearLayout(context, val.getWeather(),
                val.getDtAsString("E"), val.getTemp().getDayAsString(), val.getRainAsString(), val.getSnowAsString());
        linearLayout.setTag(Boolean.FALSE);

        return linearLayout;
    }

    private LinearLayout buildExpandedView(Context context, DailyValue val) throws Exception {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setTag(Boolean.TRUE);

        linearLayout.addView(buildExpandedViewHeader(context, val));
        linearLayout.addView(buildExpandedViewTemp(context, val));
        linearLayout.addView(buildExpandedViewFeelsLike(context, val));
        linearLayout.addView(buildExpandedViewRest(context, val));

        return linearLayout;
    }

    private LinearLayout buildExpandedViewHeader(Context context, DailyValue val) throws Exception {
        LinearLayout linearLayout = buildShortHeaderLinearLayout(context, val.getWeather(), val.getDtAsString("d-MM-yyyy"));
        linearLayout.setBackgroundColor(context.getColor(R.color.headerRowColor));

        return linearLayout;
    }

    private LinearLayout buildExpandedViewTemp(Context context, DailyValue val) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView header = buildTableHeaderTextView(context, context.getResources().getString(R.string.weather_temp));
        header.setBackgroundColor(context.getColor(R.color.subHeaderRowColor));
        linearLayout.addView(header);

        TableLayout tableLayout = buildTableLayout(context);
        TableRow row;

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_temp_day)));
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_temp_min)));
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_temp_max)));
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_temp_night)));
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_temp_eve)));
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_temp_morn)));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, val.getTemp().getDayAsString()));
        row.addView(buildTableRowTextView(context, val.getTemp().getMinAsString()));
        row.addView(buildTableRowTextView(context, val.getTemp().getMaxAsString()));
        row.addView(buildTableRowTextView(context, val.getTemp().getNightAsString()));
        row.addView(buildTableRowTextView(context, val.getTemp().getEveAsString()));
        row.addView(buildTableRowTextView(context, val.getTemp().getMornAsString()));
        tableLayout.addView(row);

        linearLayout.addView(tableLayout);
        return linearLayout;
    }

    private LinearLayout buildExpandedViewFeelsLike(Context context, DailyValue val) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView header = buildTableHeaderTextView(context, context.getResources().getString(R.string.weather_feels_like));
        header.setBackgroundColor(context.getColor(R.color.subHeaderRowColor));
        linearLayout.addView(header);

        TableLayout tableLayout = buildTableLayout(context);
        TableRow row;

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_temp_morn)));
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_temp_day)));
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_temp_eve)));
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_temp_night)));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, val.getFeelsLike().getMornAsString()));
        row.addView(buildTableRowTextView(context, val.getFeelsLike().getDayAsString()));
        row.addView(buildTableRowTextView(context, val.getFeelsLike().getEveAsString()));
        row.addView(buildTableRowTextView(context, val.getFeelsLike().getNightAsString()));
        tableLayout.addView(row);

        linearLayout.addView(tableLayout);
        return linearLayout;
    }

    private TableLayout buildExpandedViewRest(Context context, DailyValue val) {
        TableLayout tableLayout = buildTableLayout(context);
        TableRow row;

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_sunrise)));
        row.addView(buildTableRowTextView(context, val.getSunriseAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_sunset)));
        row.addView(buildTableRowTextView(context, val.getSunsetAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_uvi)));
        row.addView(buildTableRowTextView(context, val.getUviAsString()));
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

        if (val.rainExists()) {
            row = buildTableRow(context, false, even);
            row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_rain)));
            row.addView(buildTableRowTextView(context, val.getRainAsString()));
            tableLayout.addView(row);
            even = !even;
        }

        if (val.snowExists()) {
            row = buildTableRow(context, false, even);
            row.addView(buildTableRowTextView(context, context.getResources().getString(R.string.weather_snow)));
            row.addView(buildTableRowTextView(context, val.getSnowAsString()));
            tableLayout.addView(row);
        }

        return tableLayout;
    }

}
