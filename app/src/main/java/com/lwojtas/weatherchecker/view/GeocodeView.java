package com.lwojtas.weatherchecker.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lwojtas.weatherchecker.R;
import com.lwojtas.weatherchecker.model.GeocodeCity;

import java.util.List;

public class GeocodeView extends ViewInitializer {

    private final List<GeocodeCity> CITIES;

    public GeocodeView(List<GeocodeCity> cities) {
        this.CITIES = cities;
    }

    public void initialize(Context context, TableLayout tableLayout, EditText latEditText, EditText lonEditText) {
        tableLayout.removeAllViews();

        tableLayout.addView(buildTableHeader(context));

        boolean even = false;
        for (GeocodeCity city : CITIES) {
            tableLayout.addView(buildTableRow(context, city, even, latEditText, lonEditText));
            even = !even;
        }
    }

    private TableRow buildTableHeader(Context context) {
        TableRow row = buildTableRow(context, true, false);

        row.addView(buildTableHeaderTextView(context, context.getResources().getString(R.string.city_name)));
        row.addView(buildTableHeaderTextView(context, context.getResources().getString(R.string.city_lat)));
        row.addView(buildTableHeaderTextView(context, context.getResources().getString(R.string.city_lon)));

        return row;
    }

    private TableRow buildTableRow(Context context, final GeocodeCity city, boolean even, final EditText latEditText, final EditText lonEditText) {
        TableRow row = buildTableRow(context, false, even);

        row.addView(buildNameLinearLayout(context, city.getDisplayNameParts()));
        row.addView(buildTableRowTextView(context, city.getLatAsString()));
        row.addView(buildTableRowTextView(context, city.getLonAsString()));

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latEditText.setText(city.getLatAsString());
                lonEditText.setText(city.getLonAsString());
            }
        });

        return row;
    }

    private LinearLayout buildNameLinearLayout(Context context, List<String> parts) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        for (String part : parts) {
            TextView textView = buildTableRowTextView(context, part);
            linearLayout.addView(textView);
        }

        return linearLayout;
    }

}
