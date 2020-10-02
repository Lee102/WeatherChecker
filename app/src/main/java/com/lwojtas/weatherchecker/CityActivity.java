package com.lwojtas.weatherchecker;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;

import com.lwojtas.weatherchecker.model.City;
import com.lwojtas.weatherchecker.model.city.Current;
import com.lwojtas.weatherchecker.model.city.Daily;
import com.lwojtas.weatherchecker.model.city.Hourly;
import com.lwojtas.weatherchecker.model.city.Minutely;
import com.lwojtas.weatherchecker.view.CurrentView;
import com.lwojtas.weatherchecker.view.DailyView;
import com.lwojtas.weatherchecker.view.HourlyView;
import com.lwojtas.weatherchecker.view.MinutelyView;

import org.json.JSONObject;

import java.util.Objects;

public class CityActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        viewFlipper = findViewById(R.id.cityViewFlipper);

        try {
            city = new City(new JSONObject(Objects.requireNonNull(getIntent().getStringExtra("city"))));

            initializeCurrentView(city.getCurrent());
            initializeDailyView(city.getDaily());
            initializeHourlyView(city.getHourly());
            initializeMinutelyView(city.getMinutely());
        } catch (Exception e) {
            System.err.println(e);
        }

        setTitle(city.getName() + " Current Weather");
        viewFlipper.setDisplayedChild(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.city_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int newDisplayItemIndex;
        String newTitle = getTitle().toString();

        switch (item.getItemId()) {
            case R.id.cityMenuCurrent:
                newDisplayItemIndex = 1;
                newTitle = city.getName() + " Current Weather";
                break;
            case R.id.cityMenuDaily:
                newDisplayItemIndex = 2;
                newTitle = city.getName() + " Daily Weather";
                break;
            case R.id.cityMenuHourly:
                newDisplayItemIndex = 3;
                newTitle = city.getName() + " Hourly Weather";
                break;
            case R.id.cityMenuMinutely:
                newDisplayItemIndex = 4;
                newTitle = city.getName() + " Minutely Weather";
                break;
            default:
                newDisplayItemIndex = viewFlipper.getDisplayedChild();
        }

        if (newDisplayItemIndex != viewFlipper.getDisplayedChild()) {
            viewFlipper.setDisplayedChild(newDisplayItemIndex);
            setTitle(newTitle);
        }

        return true;
    }

    private void initializeCurrentView(Current current) throws Exception {
        ViewStub viewStub = findViewById(R.id.cityViewCurrentViewStub);
        CurrentView currentView = new CurrentView(current);
        currentView.initialize(getApplicationContext(), viewStub);
    }

    private void initializeDailyView(Daily daily) throws Exception {
        ViewStub viewStub = findViewById(R.id.cityViewDailyViewStub);
        DailyView dailyView = new DailyView(daily);
        dailyView.initialize(getApplicationContext(), viewStub);
    }

    private void initializeHourlyView(Hourly hourly) throws Exception {
        ViewStub viewStub = findViewById(R.id.cityViewHourlyViewStub);
        HourlyView hourlyView = new HourlyView(hourly);
        hourlyView.initialize(getApplicationContext(), viewStub);
    }

    private void initializeMinutelyView(Minutely minutely) {
        ViewStub viewStub = findViewById(R.id.cityViewMinutelyViewStub);
        MinutelyView minutelyView = new MinutelyView(minutely);
        minutelyView.initialize(getApplicationContext(), viewStub);
    }
}