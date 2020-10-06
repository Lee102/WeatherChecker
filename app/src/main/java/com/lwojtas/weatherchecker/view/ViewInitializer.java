package com.lwojtas.weatherchecker.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lwojtas.weatherchecker.R;

abstract class ViewInitializer {

    protected TableLayout buildTableLayout(Context context) {
        TableLayout tableLayout = new TableLayout(context);

        tableLayout.setShrinkAllColumns(true);
        tableLayout.setStretchAllColumns(true);

        return tableLayout;
    }

    protected TableRow buildTableRow(Context context, boolean isHeader, boolean isEven) {
        TableRow tableRow = new TableRow(context);

        tableRow.setPadding(0, 0, 0, 4);
        if (isHeader)
            tableRow.setBackgroundColor(context.getColor(R.color.headerRowColor));
        else {
            if (isEven)
                tableRow.setBackgroundColor(context.getColor(R.color.evenRowColor));
            else
                tableRow.setBackgroundColor(context.getColor(R.color.oddRowColor));
        }

        return tableRow;
    }

    private TextView buildTextView(Context context, String text) {
        TextView textView = new TextView(context);

        textView.setText(text);

        return textView;
    }

    protected TextView buildTableHeaderTextView(Context context, String text) {
        TextView textView = buildTextView(context, text);

        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTypeface(null, Typeface.BOLD);

        return textView;
    }

    protected TextView buildTableRowTextView(Context context, String text) {
        TextView textView = buildTextView(context, text);

        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        return textView;
    }

}
