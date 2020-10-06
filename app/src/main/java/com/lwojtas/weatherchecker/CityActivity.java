package com.lwojtas.weatherchecker;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.City;
import com.lwojtas.weatherchecker.model.city.Current;
import com.lwojtas.weatherchecker.model.city.Daily;
import com.lwojtas.weatherchecker.model.city.Hourly;
import com.lwojtas.weatherchecker.model.city.Minutely;
import com.lwojtas.weatherchecker.view.CurrentView;
import com.lwojtas.weatherchecker.view.DailyView;
import com.lwojtas.weatherchecker.view.HourlyView;
import com.lwojtas.weatherchecker.view.MinutelyView;

public class CityActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        viewFlipper = findViewById(R.id.cityViewFlipper);

        try {
            city = AppData.getCities().get(getIntent().getIntExtra("index", -1));

            if (city != null) {
                setTitle(city.getName() + " - " + getResources().getString(R.string.city_current_title));

                initializeCurrentView(city.getCurrent());
                initializeDailyView(city.getDaily());
                initializeHourlyView(city.getHourly());
                initializeMinutelyView(city.getMinutely());
            }
        } catch (Exception e) {
            Toast.makeText(this, this.getResources().getString(R.string.city_on_create_error_message), Toast.LENGTH_SHORT).show();
        }

        viewFlipper.setDisplayedChild(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.city_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (city != null) {
            int newDisplayItemIndex;
            String newTitle = city.getName() + " - ";

            switch (item.getItemId()) {
                case R.id.cityMenuCurrent:
                    newDisplayItemIndex = 1;
                    newTitle += getResources().getString(R.string.city_current_title);
                    break;
                case R.id.cityMenuDaily:
                    newDisplayItemIndex = 2;
                    newTitle += getResources().getString(R.string.city_daily_title);
                    break;
                case R.id.cityMenuHourly:
                    newDisplayItemIndex = 3;
                    newTitle += getResources().getString(R.string.city_hourly_title);
                    break;
                case R.id.cityMenuMinutely:
                    newDisplayItemIndex = 4;
                    newTitle += getResources().getString(R.string.city_minutely_title);
                    break;
                default:
                    newDisplayItemIndex = viewFlipper.getDisplayedChild();
            }

            if (newDisplayItemIndex != viewFlipper.getDisplayedChild()) {
                viewFlipper.setDisplayedChild(newDisplayItemIndex);
                setTitle(newTitle);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeCurrentView(Current current) throws Exception {
        ViewStub viewStub = findViewById(R.id.cityViewCurrentViewStub);
        CurrentView currentView = new CurrentView(current);
        currentView.initialize(this, viewStub);
    }

    private void initializeDailyView(Daily daily) throws Exception {
        ViewStub viewStub = findViewById(R.id.cityViewDailyViewStub);
        DailyView dailyView = new DailyView(daily);
        dailyView.initialize(this, viewStub);
    }

    private void initializeHourlyView(Hourly hourly) throws Exception {
        ViewStub viewStub = findViewById(R.id.cityViewHourlyViewStub);
        HourlyView hourlyView = new HourlyView(hourly);
        hourlyView.initialize(this, viewStub);
    }

    private void initializeMinutelyView(Minutely minutely) {
        ViewStub viewStub = findViewById(R.id.cityViewMinutelyViewStub);
        MinutelyView minutelyView = new MinutelyView(minutely);
        minutelyView.initialize(this, viewStub);
    }

}