package com.lwojtas.weatherchecker.view;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lwojtas.weatherchecker.R;
import com.lwojtas.weatherchecker.model.city.Daily;
import com.lwojtas.weatherchecker.model.city.container.DailyValue;

import java.lang.reflect.Field;

public class DailyView extends ViewInitializer {

    private final Daily daily;

    public DailyView(Daily daily) {
        this.daily = daily;
    }

    public void initialize(Context context, ViewStub stub) throws Exception {
        stub.setLayoutResource(R.layout.city_common);
        View view = stub.inflate();

        LinearLayout linearLayout = view.findViewById(R.id.cityCommonLinearLayout);
        linearLayout.addView(buildRecordsTable(context));
    }

    private TableLayout buildRecordsTable(final Context context) throws Exception {
        TableLayout tableLayout = buildTableLayout(context);

        boolean even = false;
        for (DailyValue val : daily.getValues()) {
            TableRow row = buildTableRow(context, false, even);
            row.setTag(val);

            row.addView(buildCollapsedView(context, val));

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TableRow row = (TableRow) v;
                    DailyValue val = (DailyValue) v.getTag();

                    try {
                        LinearLayout linearLayout;

                        if ((Boolean) row.getChildAt(0).getTag())
                            linearLayout = buildCollapsedView(context, val);
                        else
                            linearLayout = buildExpandedView(context, val);

                        row.removeAllViews();
                        row.addView(linearLayout);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            });

            tableLayout.addView(row);

            even = !even;
        }

        return tableLayout;
    }

    private LinearLayout buildCollapsedView(Context context, DailyValue val) throws Exception {
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
        row.addView(buildTableRowTextView(context, val.getDtAsString("E")));
        String rain = "R: ";
        if (val.getRain() != null)
            rain += val.getRainAsString();
        else
            rain += "-";
        row.addView(buildTableRowTextView(context, rain));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, val.getTemp().getDayAsString()));
        String snow = "S: ";
        if (val.getSnow() != null)
            snow += val.getSnowAsString();
        else
            snow += "-";
        row.addView(buildTableRowTextView(context, snow));
        tableLayout.addView(row);

        linearLayout.addView(tableLayout);
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
        row.addView(buildTableRowTextView(context, "Date:"));
        row.addView(buildTableRowTextView(context, val.getDtAsString("d-MM-yyyy")));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, ""));
        row.addView(buildTableRowTextView(context, val.getWeather().get(0).getDescription()));
        tableLayout.addView(row);

        linearLayout.addView(tableLayout);
        return linearLayout;
    }

    private LinearLayout buildExpandedViewTemp(Context context, DailyValue val) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView header = buildTableHeaderTextView(context, "Temp");
        header.setBackgroundColor(context.getColor(R.color.subHeaderRowColor));
        linearLayout.addView(header);

        TableLayout tableLayout = buildTableLayout(context);
        TableRow row;

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Day"));
        row.addView(buildTableRowTextView(context, "Min"));
        row.addView(buildTableRowTextView(context, "Max"));
        row.addView(buildTableRowTextView(context, "Night"));
        row.addView(buildTableRowTextView(context, "Eve"));
        row.addView(buildTableRowTextView(context, "Morn"));
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

        TextView header = buildTableHeaderTextView(context, "Feels Like");
        header.setBackgroundColor(context.getColor(R.color.subHeaderRowColor));
        linearLayout.addView(header);

        TableLayout tableLayout = buildTableLayout(context);
        TableRow row;

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Morn"));
        row.addView(buildTableRowTextView(context, "Day"));
        row.addView(buildTableRowTextView(context, "Eve"));
        row.addView(buildTableRowTextView(context, "Night"));
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
        row.addView(buildTableRowTextView(context, "Sunrise:"));
        row.addView(buildTableRowTextView(context, val.getSunriseAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, "Sunset:"));
        row.addView(buildTableRowTextView(context, val.getSunsetAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "uvi"));
        row.addView(buildTableRowTextView(context, val.getUviAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, "Pressure"));
        row.addView(buildTableRowTextView(context, val.getPressureAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Humidity"));
        row.addView(buildTableRowTextView(context, val.getHumidityAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, "Dew Point"));
        row.addView(buildTableRowTextView(context, val.getDewPointAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Clouds"));
        row.addView(buildTableRowTextView(context, val.getCloudsAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, true);
        row.addView(buildTableRowTextView(context, "pop"));
        row.addView(buildTableRowTextView(context, val.getPopAsString()));
        tableLayout.addView(row);

        row = buildTableRow(context, false, false);
        row.addView(buildTableRowTextView(context, "Wind Speed"));
        row.addView(buildTableRowTextView(context, val.getWindSpeedAsString()));
        tableLayout.addView(row);

        boolean even = true;
        if (val.getWindGust() != null) {
            row = buildTableRow(context, false, true);
            row.addView(buildTableRowTextView(context, "Wind Gust"));
            row.addView(buildTableRowTextView(context, val.getWindGustAsString()));
            tableLayout.addView(row);
            even = false;
        }

        row = buildTableRow(context, false, even);
        row.addView(buildTableRowTextView(context, "Wind Deg"));
        row.addView(buildTableRowTextView(context, val.getWindDegAsString()));
        tableLayout.addView(row);
        even = !even;

        if (val.getRain() != null) {
            row = buildTableRow(context, false, even);
            row.addView(buildTableRowTextView(context, "Rain"));
            row.addView(buildTableRowTextView(context, val.getRainAsString()));
            tableLayout.addView(row);
            even = !even;
        }

        if (val.getSnow() != null) {
            row = buildTableRow(context, false, even);
            row.addView(buildTableRowTextView(context, "Snow"));
            row.addView(buildTableRowTextView(context, val.getSnowAsString()));
            tableLayout.addView(row);
        }

        return tableLayout;
    }

}
