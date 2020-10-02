package com.lwojtas.weatherchecker;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.City;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private AppData appData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appData = new AppData();

        fillCitiesView();
    }

    private void fillCitiesView() {
        LinearLayout citiesView = findViewById(R.id.CitiesView);
        citiesView.removeAllViews();
        //boolean first = true;
        for (City city : appData.getCities()) {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setClickable(true);
            linearLayout.setFocusable(true);
            linearLayout.setTag(city);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String city = ((City) v.getTag()).toJSON().toString();
                        new City(new JSONObject(city));

                        Intent intent = new Intent(getApplicationContext(), CityActivity.class);
                        intent.putExtra("city", city);
                        startActivity(intent);
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            });
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipData clipData = ClipData.newPlainText("index", String.valueOf(appData.getCities().indexOf(v.getTag())));
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
                        City source = appData.getCities().get(sourceInd);
                        City dest = (City) v.getTag();
                        int destInd = appData.getCities().indexOf(dest);

                        if (source != dest) {
                            appData.getCities().set(destInd, source);
                            appData.getCities().set(sourceInd, dest);
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
                public void onClick(final View v) {
                    final CharSequence[] options = {"Edit", "Delete"};
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Choose an action");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    //todo
                                    break;
                                case 1:
                                    final CharSequence[] options1 = {"Yes", "No"};
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                                    builder1.setTitle("Are you sure?");
                                    builder1.setItems(options1, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == 0) {
                                                City city = (City) v.getTag();
                                                //todo fix
                                                appData.getCities().remove(city);
                                                fillCitiesView();
                                            }
                                        }
                                    });
                                    AlertDialog alert1 = builder1.create();
                                    alert1.show();
                                    break;
                            }
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });

            linearLayout.addView(imageButton);

            citiesView.addView(linearLayout);
        }
    }
}