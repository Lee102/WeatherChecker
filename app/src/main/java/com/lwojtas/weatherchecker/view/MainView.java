package com.lwojtas.weatherchecker.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lwojtas.weatherchecker.CityActivity;
import com.lwojtas.weatherchecker.R;
import com.lwojtas.weatherchecker.WeatherActivity;
import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.City;
import com.lwojtas.weatherchecker.util.WeatherCall;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainView {

    private final List<City> CITIES;

    public MainView(List<City> cities) {
        this.CITIES = cities;
    }

    public void initialize(Context context, LinearLayout linearLayout) {
        linearLayout.removeAllViews();

        for (City city : CITIES)
            linearLayout.addView(buildCityLinearLayout(context, city));
    }

    private LinearLayout buildCityLinearLayout(final Context context, City city) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.bottomMargin = 4;

        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setTag(city);

        Boolean weatherActual = city.isActual();
        if (weatherActual != null) {
            if (weatherActual)
                linearLayout.setBackgroundColor(context.getColor(R.color.cityWeatherActual));
            else
                linearLayout.setBackgroundColor(context.getColor(R.color.cityWeatherOld));

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), WeatherActivity.class);
                    intent.putExtra("index", AppData.getCities().indexOf((City) v.getTag()));
                    context.startActivity(intent);
                }
            });
        } else
            linearLayout.setBackgroundColor(context.getColor(R.color.cityWeatherNull));

        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData clipData = ClipData.newPlainText("index", String.valueOf(AppData.getCities().indexOf((City) v.getTag())));
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(clipData, dragShadowBuilder, v, 0);
                v.setVisibility(View.INVISIBLE);
                return true;
            }
        });

        linearLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (event.getAction() == DragEvent.ACTION_DROP) {
                    int sourceInd = Integer.parseInt(event.getClipData().getItemAt(0).getText().toString());
                    City source = AppData.getCities().get(sourceInd);
                    City dest = (City) v.getTag();
                    int destInd = AppData.getCities().indexOf(dest);

                    if (source != dest) {
                        AppData.getCities().set(destInd, source);
                        AppData.getCities().set(sourceInd, dest);
                        initialize(context, (LinearLayout) linearLayout.getParent());
                    }
                }
                v.setVisibility(View.VISIBLE);
                return true;
            }
        });

        float textWidth = .9f;
        float buttonWidth = .1f;

        linearLayout.addView(buildCityNameTextView(context, city.getName(), textWidth));
        linearLayout.addView(buildCityMoreImageButton(context, buttonWidth));

        return linearLayout;
    }

    private TextView buildCityNameTextView(Context context, String name, float weight) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = weight;

        TextView textView = new TextView(context);
        textView.setTextSize(30);
        textView.setLayoutParams(layoutParams);
        textView.setText(name);

        return textView;
    }

    private ImageButton buildCityMoreImageButton(final Context context, float weight) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = weight;
        layoutParams.gravity = Gravity.CENTER;

        ImageButton imageButton = new ImageButton(context);
        imageButton.setLayoutParams(layoutParams);
        imageButton.setImageResource(R.drawable.ic_baseline_more_horiz_24);
        imageButton.setBackgroundColor(Color.TRANSPARENT);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View V) {
                final CharSequence[] OPTIONS = context.getResources().getStringArray(R.array.main_city_button_array);
                final AlertDialog.Builder BUILDER = new AlertDialog.Builder(context);
                BUILDER.setTitle(context.getResources().getString(R.string.main_choose_action_dialog));
                BUILDER.setItems(OPTIONS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                City city = (City) ((View) V.getParent()).getTag();

                                ExecutorService executor = Executors.newSingleThreadExecutor();
                                Callable<Void> call = new WeatherCall(city);
                                Future<Void> result = executor.submit(call);
                                try {
                                    result.get();
                                    ((LinearLayout) V.getParent()).setBackgroundColor(context.getColor(R.color.cityWeatherActual));
                                } catch (Exception e) {
                                    Toast.makeText(V.getContext(), V.getResources().getString(R.string.main_update_city_error_message), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                if (context instanceof Activity) {
                                    Intent intent = new Intent(V.getContext(), CityActivity.class);
                                    intent.putExtra("index", AppData.getCities().indexOf((City) ((View) V.getParent()).getTag()));
                                    ((Activity) context).startActivityForResult(intent, 0);
                                }
                                break;
                            case 2:
                                final CharSequence[] DELETE_OPTIONS = context.getResources().getStringArray(R.array.alert_options);
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setTitle(context.getResources().getString(R.string.main_confirmation_dialog));
                                builder1.setItems(DELETE_OPTIONS, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            City city = (City) ((View) V.getParent()).getTag();
                                            AppData.getCities().remove(city);
                                            ((LinearLayout) V.getParent().getParent()).removeView((View) V.getParent());
                                        }
                                    }
                                });
                                AlertDialog alert1 = builder1.create();
                                alert1.show();
                                break;
                        }
                    }
                });
                AlertDialog alert = BUILDER.create();
                alert.show();
            }
        });

        return imageButton;
    }

}
