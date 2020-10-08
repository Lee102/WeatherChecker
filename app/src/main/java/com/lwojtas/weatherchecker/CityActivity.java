package com.lwojtas.weatherchecker;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.City;
import com.lwojtas.weatherchecker.model.GeocodeCity;
import com.lwojtas.weatherchecker.util.GeocodeCall;
import com.lwojtas.weatherchecker.util.LocaleTool;
import com.lwojtas.weatherchecker.view.GeocodeView;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CityActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText latEditText;
    private EditText lonEditText;

    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        findViews();

        int cityIndex = getIntent().getIntExtra("index", -1);
        if (cityIndex < 0)
            setTitle(getResources().getString(R.string.city_title_new));
        else {
            setTitle(getResources().getString(R.string.city_title_modify));

            city = AppData.getCities().get(cityIndex);
            nameEditText.setText(city.getName());
            latEditText.setText(city.getLatAsString());
            lonEditText.setText(city.getLonAsString());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleTool.setApplicationLocale(base, false));
    }

    public void onSaveButtonClick(View view) {
        String name = nameEditText.getText().toString();
        String latStr = latEditText.getText().toString();
        String lonStr = lonEditText.getText().toString();

        if (!name.isEmpty() && !latStr.isEmpty() && !lonStr.isEmpty()) {
            Double lat = Double.parseDouble(latEditText.getText().toString());
            Double lon = Double.parseDouble(lonEditText.getText().toString());

            if (city == null) {
                city = new City(name, lat, lon);
                AppData.getCities().add(city);
            } else {
                city.setName(name);
                city.setLat(lat);
                city.setLon(lon);
            }

            setResult(1);
            finish();
        } else
            Toast.makeText(this, this.getResources().getString(R.string.city_save_error_message), Toast.LENGTH_SHORT).show();
    }

    public void onSearchButtonClick(View view) {
        String cityName = nameEditText.getText().toString();

        if (!cityName.isEmpty()) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Callable<List<GeocodeCity>> call = new GeocodeCall(cityName);
            Future<List<GeocodeCity>> result = executor.submit(call);
            try {
                TableLayout tableLayout = findViewById(R.id.citySearchResultTableLayout);
                GeocodeView geocodeView = new GeocodeView(result.get());
                geocodeView.initialize(this, tableLayout, latEditText, lonEditText);
            } catch (Exception e) {
                Toast.makeText(this, this.getResources().getString(R.string.city_search_error_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void findViews() {
        nameEditText = findViewById(R.id.cityNameEditText);
        latEditText = findViewById(R.id.cityLatEditText);
        lonEditText = findViewById(R.id.cityLonEditText);
    }

}