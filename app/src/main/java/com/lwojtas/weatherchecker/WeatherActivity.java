package com.lwojtas.weatherchecker;

import android.content.Context;
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
import com.lwojtas.weatherchecker.util.LocaleTool;
import com.lwojtas.weatherchecker.view.CurrentView;
import com.lwojtas.weatherchecker.view.DailyView;
import com.lwojtas.weatherchecker.view.HourlyView;
import com.lwojtas.weatherchecker.view.MinutelyView;

import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private ViewFlipper viewFlipper;
    private City city;
    private DailyView dailyView;
    private HourlyView hourlyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        viewFlipper = findViewById(R.id.cityViewFlipper);


        try {
            city = AppData.getCities().get(getIntent().getIntExtra("index", -1));

            if (city != null) {
                setTitle(city.getName() + " - " + getResources().getString(R.string.weather_current_title));

                initializeCurrentView(city.getCurrent());
                dailyView = initializeDailyView(city.getDaily());
                hourlyView = initializeHourlyView(city.getHourly());
                initializeMinutelyView(city.getMinutely());
            }
        } catch (Exception e) {
            Toast.makeText(this, this.getResources().getString(R.string.weather_on_create_error_message), Toast.LENGTH_SHORT).show();
        }

        if (savedInstanceState != null) {
            int displayedChild = savedInstanceState.getInt("viewFlipperIndex");
            viewFlipper.setDisplayedChild(displayedChild);

            if (displayedChild == 2) {
                List<Integer> indexes = savedInstanceState.getIntegerArrayList("expandedRowsIndexes");
                if (indexes != null)
                    dailyView.callOnClick(indexes);
            } else if (displayedChild == 3) {
                List<Integer> indexes = savedInstanceState.getIntegerArrayList("expandedRowsIndexes");
                if (indexes != null)
                    hourlyView.callOnClick(indexes);
            }
        } else
            viewFlipper.setDisplayedChild(1);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleTool.setApplicationLocale(base, false));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int displayedChild = viewFlipper.getDisplayedChild();
        outState.putInt("viewFlipperIndex", displayedChild);

        if (displayedChild == 2)
            outState.putIntegerArrayList("expandedRowsIndexes", dailyView.getExpandedIndexes());
        else if (displayedChild == 3)
            outState.putIntegerArrayList("expandedRowsIndexes", hourlyView.getExpandedIndexes());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (city != null) {
            int newDisplayItemIndex;
            String newTitle = city.getName() + " - ";

            switch (item.getItemId()) {
                case R.id.weatherMenuCurrent:
                    newDisplayItemIndex = 1;
                    newTitle += getResources().getString(R.string.weather_current_title);
                    break;
                case R.id.weatherMenuDaily:
                    newDisplayItemIndex = 2;
                    newTitle += getResources().getString(R.string.weather_daily_title);
                    break;
                case R.id.weatherMenuHourly:
                    newDisplayItemIndex = 3;
                    newTitle += getResources().getString(R.string.weather_hourly_title);
                    break;
                case R.id.weatherMenuMinutely:
                    newDisplayItemIndex = 4;
                    newTitle += getResources().getString(R.string.weather_minutely_title);
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
        ViewStub viewStub = findViewById(R.id.weatherViewCurrentViewStub);
        CurrentView currentView = new CurrentView(current);
        currentView.initialize(this, viewStub);
    }

    private DailyView initializeDailyView(Daily daily) throws Exception {
        ViewStub viewStub = findViewById(R.id.weatherViewDailyViewStub);
        DailyView dailyView = new DailyView(daily);
        dailyView.initialize(this, viewStub);

        return dailyView;
    }

    private HourlyView initializeHourlyView(Hourly hourly) throws Exception {
        ViewStub viewStub = findViewById(R.id.weatherViewHourlyViewStub);
        HourlyView hourlyView = new HourlyView(hourly);
        hourlyView.initialize(this, viewStub);

        return hourlyView;
    }

    private void initializeMinutelyView(Minutely minutely) {
        ViewStub viewStub = findViewById(R.id.weatherViewMinutelyViewStub);
        MinutelyView minutelyView = new MinutelyView(minutely);
        minutelyView.initialize(this, viewStub);
    }

}