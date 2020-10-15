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
import com.lwojtas.weatherchecker.util.exception.CoordinateOutOfBoundsException;
import com.lwojtas.weatherchecker.view.GeocodeView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CityActivity extends AppCompatActivity {

    private static final Logger LOG = LoggerFactory.getLogger(CityActivity.class);

    private EditText nameEditText;
    private EditText latEditText;
    private EditText lonEditText;

    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LOG.trace("onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        findViews();

        int cityIndex = getIntent().getIntExtra("index", -1);
        if (cityIndex < 0) {
            LOG.info("new city");

            setTitle(getResources().getString(R.string.city_title_new));
        } else {
            LOG.info("modify city");

            setTitle(getResources().getString(R.string.city_title_modify));

            city = AppData.getCities().get(cityIndex);
            LOG.debug("modify city - name: " + city.getName());

            nameEditText.setText(city.getName());
            latEditText.setText(city.getLatAsString());
            lonEditText.setText(city.getLonAsString());
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        LOG.trace("attachBaseContext");

        super.attachBaseContext(LocaleTool.setApplicationLocale(base, false));
    }

    public void onSaveButtonClick(View view) {
        LOG.trace("onSaveButtonClick");

        String name = nameEditText.getText().toString();
        String latStr = latEditText.getText().toString();
        String lonStr = lonEditText.getText().toString();

        if (!name.isEmpty() && !latStr.isEmpty() && !lonStr.isEmpty()) {
            NumberFormat format = NumberFormat.getInstance(AppData.getSettings().getLocale());
            try {
                Number number;
                Double lat = null;
                Double lon = null;

                number = format.parse(latStr);
                if (number != null)
                    lat = number.doubleValue();

                number = format.parse(lonStr);
                if (number != null)
                    lon = number.doubleValue();

                if (lat != null && lon != null)
                    if (city == null) {
                        city = new City(name, lat, lon);
                        AppData.getCities().add(city);
                    } else {
                        city.setName(name);
                        city.setLat(lat);
                        city.setLon(lon);
                    }

                LOG.info("city saved");
                LOG.debug("city saved - name: " + name);

                setResult(1);
                finish();
            } catch (CoordinateOutOfBoundsException e) {
                LOG.warn("city save lat/lon out of bounds error");
                Toast.makeText(this, this.getResources().getString(R.string.city_lat_lon_out_of_bounds_error_message), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                LOG.warn("city save double parse error");
                Toast.makeText(this, this.getResources().getString(R.string.city_double_parse_error_message), Toast.LENGTH_SHORT).show();
            }
        } else {
            LOG.warn("city save error");
            Toast.makeText(this, this.getResources().getString(R.string.city_save_error_message), Toast.LENGTH_SHORT).show();
        }
    }

    public void onSearchButtonClick(View view) {
        LOG.trace("onSearchButtonClick");

        String cityName = nameEditText.getText().toString();
        LOG.debug("search city - name: " + cityName);

        if (!cityName.isEmpty()) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Callable<List<GeocodeCity>> call = new GeocodeCall(cityName);
            Future<List<GeocodeCity>> result = executor.submit(call);
            try {
                TableLayout tableLayout = findViewById(R.id.citySearchResultTableLayout);
                GeocodeView geocodeView = new GeocodeView(result.get());
                geocodeView.initialize(this, tableLayout, latEditText, lonEditText);
                LOG.info("city searched");
            } catch (Exception e) {
                LOG.error("city search error " + e.getMessage());
                Toast.makeText(this, this.getResources().getString(R.string.city_search_error_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void findViews() {
        LOG.trace("findViews");

        nameEditText = findViewById(R.id.cityNameEditText);
        latEditText = findViewById(R.id.cityLatEditText);
        lonEditText = findViewById(R.id.cityLonEditText);
    }

}