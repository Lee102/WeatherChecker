package com.lwojtas.weatherchecker.view;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.lwojtas.weatherchecker.R;
import com.lwojtas.weatherchecker.model.city.Minutely;
import com.lwojtas.weatherchecker.model.city.container.MinutelyValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinutelyView extends ViewInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(MinutelyView.class);

    private final Minutely MINUTELY;

    public MinutelyView(Minutely minutely) {
        this.MINUTELY = minutely;
    }

    public void initialize(Context context, ViewStub stub) {
        LOG.trace("initialize");

        if (context != null && stub != null && MINUTELY != null) {
            stub.setLayoutResource(R.layout.weather_common);
            View view = stub.inflate();

            LinearLayout linearLayout = view.findViewById(R.id.weatherCommonLinearLayout);
            linearLayout.addView(buildRecordsTable(context));
        }
    }

    private TableLayout buildRecordsTable(Context context) {
        LOG.trace("buildRecordsTable");

        TableLayout tableLayout = buildTableLayout(context);

        tableLayout.addView(buildTableHeader(context));

        boolean even = false;
        for (MinutelyValue val : MINUTELY.getVALUES()) {
            TableRow row = buildTableRow(context, false, even);

            row.addView(buildTableRowTextView(context, val.getDtAsString()));
            row.addView(buildTableRowTextView(context, val.getPrecipitationAsString()));

            tableLayout.addView(row);

            even = !even;
        }

        return tableLayout;
    }

    private TableRow buildTableHeader(Context context) {
        LOG.trace("buildTableHeader");

        TableRow row = buildTableRow(context, true, false);

        row.addView(buildTableHeaderTextView(context, context.getResources().getString(R.string.weather_date)));
        row.addView(buildTableHeaderTextView(context, context.getResources().getString(R.string.weather_precipitation)));

        return row;
    }

}
