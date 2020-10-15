package com.lwojtas.weatherchecker.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.lwojtas.weatherchecker.R;
import com.lwojtas.weatherchecker.model.city.container.Weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

abstract class ViewInitializer {

    private static final Logger LOG = LoggerFactory.getLogger(ViewInitializer.class);

    protected TableLayout buildTableLayout(Context context) {
        LOG.trace("buildTableLayout");

        TableLayout tableLayout = new TableLayout(context);

        tableLayout.setShrinkAllColumns(true);
        tableLayout.setStretchAllColumns(true);

        return tableLayout;
    }

    protected TableRow buildTableRow(Context context, boolean isHeader, boolean isEven) {
        LOG.trace("buildTableRow");

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

    protected TextView buildTableHeaderTextView(Context context, String text) {
        LOG.trace("buildTableHeaderTextView");

        TextView textView = buildTextView(context, text);

        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setTypeface(null, Typeface.BOLD);

        return textView;
    }

    protected TextView buildTableRowTextView(Context context, String text) {
        LOG.trace("buildTableRowTextView");

        TextView textView = buildTextView(context, text);

        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        return textView;
    }

    protected LinearLayout buildShortHeaderLinearLayout(Context context, List<Weather> weather, String date) throws Exception {
        LOG.trace("buildShortHeaderLinearLayout");

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = buildTextView(context, date);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        linearLayout.addView(textView);

        LinearLayout linearLayout1 = new LinearLayout(context);
        linearLayout1.setOrientation(LinearLayout.HORIZONTAL);

        float weatherWeight = 1f;
        if (weather.size() > 1)
            weatherWeight = .9f / ((float) weather.size());
        float nextWeight = .1f / ((float) weather.size() - 1);

        Iterator<Weather> iterator = weather.iterator();
        linearLayout1.addView(buildWeatherLinearLayout(context, iterator.next(), weatherWeight));
        while (iterator.hasNext()) {
            linearLayout1.addView(buildNextImageView(context, nextWeight));
            linearLayout1.addView(buildWeatherLinearLayout(context, iterator.next(), weatherWeight));
        }

        linearLayout.addView(linearLayout1);

        return linearLayout;
    }

    protected LinearLayout buildLongHeaderLinearLayout(Context context, List<Weather> weather, String date, String temp, String rain, String snow) throws Exception {
        LOG.trace("buildLongHeaderLinearLayout");

        LinearLayout linearLayout = buildShortHeaderLinearLayout(context, weather, date);
        linearLayout.addView(buildLongHeaderLinearLayout(context, temp, rain, snow));
        return linearLayout;
    }

    private LinearLayout buildWeatherLinearLayout(Context context, Weather weather, float weight) throws Exception {
        LOG.trace("buildWeatherLinearLayout");

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = weight;
        linearLayout.setLayoutParams(layoutParams);

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(getImageResourceId(weather.getIcon()));
        linearLayout.addView(imageView);

        TextView textView = buildTextView(context, weather.getDescription());
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        linearLayout.addView(textView);

        return linearLayout;
    }

    private ImageView buildNextImageView(Context context, float weight) {
        LOG.trace("buildNextImageView");

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.ic_baseline_arrow_forward_24);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = weight;
        imageView.setLayoutParams(layoutParams);

        return imageView;
    }

    private LinearLayout buildLongHeaderLinearLayout(Context context, String temp, String rain, String snow) {
        LOG.trace("buildLongHeaderLinearLayout");

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = .33f;

        TextView textView;

        textView = buildTableRowTextView(context, temp);
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);

        if (rain == null)
            rain = "-";
        textView = buildTableRowTextView(context, rain);
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);

        if (snow == null)
            snow = "-";
        textView = buildTableRowTextView(context, snow);
        textView.setLayoutParams(layoutParams);
        linearLayout.addView(textView);

        return linearLayout;
    }

    private int getImageResourceId(String iconName) throws Exception {
        LOG.trace("getImageResourceId");

        String resourceName;

        switch (iconName) {
            case "03d":
            case "03n":
                resourceName = "w03";
                break;
            case "04d":
            case "04n":
                resourceName = "w04";
                break;
            case "09d":
            case "09n":
                resourceName = "w09";
                break;
            case "11d":
            case "11n":
                resourceName = "w11";
                break;
            case "13d":
            case "13n":
                resourceName = "w13";
                break;
            case "50d":
            case "50n":
                resourceName = "w50";
                break;
            default:
                resourceName = "w" + iconName;
                break;
        }

        Field field = (R.drawable.class).getDeclaredField(resourceName);
        return field.getInt(field);
    }

    private TextView buildTextView(Context context, String text) {
        LOG.trace("buildTextView");

        TextView textView = new TextView(context);

        textView.setText(text);

        return textView;
    }

}
