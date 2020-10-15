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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainView {

    private static final Logger LOG = LoggerFactory.getLogger(MainView.class);

    private final List<City> CITIES;

    public MainView(List<City> cities) {
        this.CITIES = cities;
    }

    public void initialize(Context context, LinearLayout linearLayout) {
        LOG.trace("initialize");

        if (context != null && linearLayout != null && CITIES != null) {
            linearLayout.removeAllViews();

            for (City city : CITIES)
                linearLayout.addView(buildCityLinearLayout(context, city));
        }
    }

    private LinearLayout buildCityLinearLayout(final Context context, City city) {
        LOG.trace("buildCityLinearLayout");

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.bottomMargin = 4;

        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setTag(city);
        linearLayout.setBackgroundColor(initCityLinearLayoutBackgroundColor(context, city));

        if (city.isInitialized())
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LOG.trace("buildCityLinearLayout onClick");
                    City city = (City) v.getTag();
                    LOG.debug("buildCityLinearLayout onClick - city name: " + city.getName());

                    Intent intent = new Intent(v.getContext(), WeatherActivity.class);
                    intent.putExtra("index", AppData.getCities().indexOf(city));
                    context.startActivity(intent);
                }
            });

        linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                LOG.trace("buildCityLinearLayout onLongClick");
                City city = (City) v.getTag();
                LOG.debug("buildCityLinearLayout onLongClick - city name: " + city.getName());

                ClipData clipData = ClipData.newPlainText("index", String.valueOf(AppData.getCities().indexOf(city)));
                View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
                v.startDragAndDrop(clipData, dragShadowBuilder, v, 0);
                return true;
            }
        });

        linearLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                LOG.trace("buildCityLinearLayout onDrag");
                City city = (City) v.getTag();
                LOG.debug("buildCityLinearLayout onDrag - city name: " + city.getName());

                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        v.setBackgroundColor(v.getContext().getColor(R.color.dragEntered));
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                    case DragEvent.ACTION_DRAG_ENDED:
                        v.setBackgroundColor(initCityLinearLayoutBackgroundColor(v.getContext(), (City) v.getTag()));
                        break;
                    case DragEvent.ACTION_DROP:
                        int sourceInd = Integer.parseInt(event.getClipData().getItemAt(0).getText().toString());
                        City source = AppData.getCities().get(sourceInd);
                        City dest = (City) v.getTag();
                        int destInd = AppData.getCities().indexOf(dest);

                        if (source != dest) {
                            AppData.getCities().set(destInd, source);
                            AppData.getCities().set(sourceInd, dest);
                            initialize(context, (LinearLayout) linearLayout.getParent());
                        }
                        break;
                }

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
        LOG.trace("buildCityNameTextView");

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = weight;

        TextView textView = new TextView(context);
        textView.setTextSize(30);
        textView.setLayoutParams(layoutParams);
        textView.setText(name);

        return textView;
    }

    private ImageButton buildCityMoreImageButton(final Context context, float weight) {
        LOG.trace("buildCityMoreImageButton");

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
                LOG.trace("buildCityMoreImageButton onClick");

                final CharSequence[] OPTIONS = context.getResources().getStringArray(R.array.main_city_button_array);
                final AlertDialog.Builder BUILDER = new AlertDialog.Builder(context);
                BUILDER.setTitle(context.getResources().getString(R.string.main_choose_action_dialog));
                BUILDER.setItems(OPTIONS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LOG.trace("buildCityMoreImageButton onClick onClick - which: " + which);

                        final City city = (City) ((View) V.getParent()).getTag();

                        switch (which) {
                            case 0:
                                LOG.trace("buildCityMoreImageButton onClick onClick update");
                                LOG.debug("buildCityMoreImageButton onClick onClick update - city name: " + city.getName());

                                ExecutorService executor = Executors.newSingleThreadExecutor();
                                Callable<Void> call = new WeatherCall(city);
                                Future<Void> result = executor.submit(call);
                                try {
                                    result.get();
                                    initialize(context, (LinearLayout) V.getParent().getParent());
                                    LOG.info("buildCityMoreImageButton onClick onClick update success");
                                } catch (Exception e) {
                                    LOG.error("buildCityMoreImageButton onClick onClick update error" + e.getMessage());

                                    Toast.makeText(V.getContext(), V.getResources().getString(R.string.main_update_city_error_message), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                LOG.trace("buildCityMoreImageButton onClick onClick modify");
                                LOG.debug("buildCityMoreImageButton onClick onClick modify - city name: " + city.getName());

                                if (context instanceof Activity) {
                                    Intent intent = new Intent(V.getContext(), CityActivity.class);
                                    intent.putExtra("index", AppData.getCities().indexOf(city));
                                    ((Activity) context).startActivityForResult(intent, 0);

                                    LOG.info("buildCityMoreImageButton onClick onClick modify success");
                                }
                                break;
                            case 2:
                                LOG.trace("buildCityMoreImageButton onClick onClick delete");
                                LOG.debug("buildCityMoreImageButton onClick onClick delete - city name: " + city.getName());

                                final CharSequence[] DELETE_OPTIONS = context.getResources().getStringArray(R.array.alert_options);
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setTitle(context.getResources().getString(R.string.main_confirmation_dialog));
                                builder1.setItems(DELETE_OPTIONS, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            AppData.getCities().remove(city);
                                            ((LinearLayout) V.getParent().getParent()).removeView((View) V.getParent());

                                            LOG.info("buildCityMoreImageButton onClick onClick delete success");
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

    private Integer initCityLinearLayoutBackgroundColor(Context context, City city) {
        LOG.trace("initCityLinearLayoutBackgroundColor");

        Boolean weatherActual = city.isActual();
        if (weatherActual != null) {
            if (weatherActual)
                return context.getColor(R.color.cityWeatherActual);
            else
                return context.getColor(R.color.cityWeatherOld);
        } else
            return context.getColor(R.color.cityWeatherNull);
    }

}
