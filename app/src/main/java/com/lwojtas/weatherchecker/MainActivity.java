package com.lwojtas.weatherchecker;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.City;
import com.lwojtas.weatherchecker.model.Settings;
import com.lwojtas.weatherchecker.util.CityWeatherCall;
import com.lwojtas.weatherchecker.util.FileTool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.lwojtas.weatherchecker.util.LocaleTool.changeLanguage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!AppData.isInitialized()) {
            FileTool fileTool = new FileTool();
            fileTool.readSettings(getApplicationContext());

            if (AppData.getSettings().getUpdateMode() == Settings.UpdateMode.ON_STARTUP) {
                List<Future<Void>> results = new ArrayList<>();
                ExecutorService executor = Executors.newFixedThreadPool(AppData.getSettings().getThreadPool());

                for (City city : AppData.getCities()) {
                    Callable<Void> call = new CityWeatherCall(city);
                    Future<Void> result = executor.submit(call);
                    results.add(result);
                }

                for (Future<Void> result : results) {
                    try {
                        result.get();
                    } catch (Exception e) {
                        Toast.makeText(this, this.getResources().getString(R.string.main_update_city_error_message), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            changeLanguage(this, AppData.getSettings().getLocale());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillCitiesView();
    }

    @Override
    protected void onPause() {
        if (AppData.isInitialized()) {
            FileTool fileTool = new FileTool();
            fileTool.save(getApplicationContext());
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mainMenuSettings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void fillCitiesView() {
        LinearLayout citiesView = findViewById(R.id.CitiesView);
        citiesView.removeAllViews();

        for (City city : AppData.getCities()) {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setClickable(true);
            linearLayout.setFocusable(true);
            linearLayout.setTag(city);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), CityActivity.class);
                    intent.putExtra("index", AppData.getCities().indexOf((City) v.getTag()));
                    startActivity(intent);
                }
            });
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
                            fillCitiesView();
                        }
                    }
                    v.setVisibility(View.VISIBLE);
                    return true;
                }
            });

            TextView textView = new TextView(getApplicationContext());

            textView.setTextSize(30);
            textView.setText(city.getName());
            linearLayout.addView(textView);

            ImageButton imageButton = new ImageButton(getApplicationContext());
            imageButton.setImageResource(R.drawable.ic_baseline_more_horiz_24);
            imageButton.setBackgroundColor(Color.TRANSPARENT);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View V) {
                    final CharSequence[] OPTIONS = getResources().getStringArray(R.array.main_city_button_array);
                    final AlertDialog.Builder BUILDER = new AlertDialog.Builder(MainActivity.this);
                    BUILDER.setTitle(getResources().getString(R.string.main_choose_action_dialog));
                    BUILDER.setItems(OPTIONS, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    City city = (City) ((View) V.getParent()).getTag();

                                    ExecutorService executor = Executors.newSingleThreadExecutor();
                                    Callable<Void> call = new CityWeatherCall(city);
                                    Future<Void> result = executor.submit(call);
                                    try {
                                        result.get();
                                    } catch (Exception e) {
                                        Toast.makeText(V.getContext(), V.getResources().getString(R.string.main_update_city_error_message), Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 1:
                                    //todo
                                    break;
                                case 2:
                                    final CharSequence[] DELETE_OPTIONS = getResources().getStringArray(R.array.alert_options);
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                    builder1.setTitle(getResources().getString(R.string.main_confirmation_dialog));
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

            linearLayout.addView(imageButton);

            citiesView.addView(linearLayout);
        }
    }

}