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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private static final Logger LOG = LoggerFactory.getLogger(WeatherActivity.class);

    private ViewFlipper viewFlipper;
    private City city;
    private DailyView dailyView;
    private HourlyView hourlyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LOG.trace("onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        viewFlipper = findViewById(R.id.cityViewFlipper);


        try {
            city = AppData.getCities().get(getIntent().getIntExtra("index", -1));
            LOG.debug("weather onCreate - city name: " + city.getName());

            if (city != null) {
                setTitle(city.getName() + " - " + getResources().getString(R.string.weather_current_title));

                initializeCurrentView(city.getCurrent());
                initializeDailyView(city.getDaily());
                initializeHourlyView(city.getHourly());
                initializeMinutelyView(city.getMinutely());
            }
        } catch (Exception e) {
            LOG.error("weather onCreate error" + e.getMessage());
            Toast.makeText(this, this.getResources().getString(R.string.weather_on_create_error_message), Toast.LENGTH_SHORT).show();
        }

        if (savedInstanceState != null) {
            LOG.trace("weather onCreate - savedInstanceState");

            int displayedChild = savedInstanceState.getInt("viewFlipperIndex");
            viewFlipper.setDisplayedChild(displayedChild);

            if (displayedChild == 2) {
                List<Integer> indexes = savedInstanceState.getIntegerArrayList("expandedRowsIndexes");
                if (dailyView != null && indexes != null)
                    dailyView.callOnClick(indexes);
            } else if (displayedChild == 3) {
                List<Integer> indexes = savedInstanceState.getIntegerArrayList("expandedRowsIndexes");
                if (hourlyView != null && indexes != null)
                    hourlyView.callOnClick(indexes);
            }
        } else
            viewFlipper.setDisplayedChild(1);
    }

    @Override
    protected void attachBaseContext(Context base) {
        LOG.trace("attachBaseContext");

        super.attachBaseContext(LocaleTool.setApplicationLocale(base, false));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        LOG.trace("onSaveInstanceState");

        int displayedChild = viewFlipper.getDisplayedChild();
        outState.putInt("viewFlipperIndex", displayedChild);
        LOG.debug("onSaveInstanceState - displayedChild: " + displayedChild);

        if (displayedChild == 2)
            outState.putIntegerArrayList("expandedRowsIndexes", dailyView.getExpandedIndexes());
        else if (displayedChild == 3)
            outState.putIntegerArrayList("expandedRowsIndexes", hourlyView.getExpandedIndexes());

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        LOG.trace("onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.weather_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        LOG.trace("onPrepareOptionsMenu");

        if (city != null) {
            if (city.getCurrent() == null)
                menu.getItem(0).setEnabled(false);
            if (city.getDaily() == null)
                menu.getItem(1).setEnabled(false);
            if (city.getHourly() == null)
                menu.getItem(2).setEnabled(false);
            if (city.getMinutely() == null)
                menu.getItem(3).setEnabled(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        LOG.trace("onOptionsItemSelected - id: " + item.getItemId());

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

            int displayedItemIndex = viewFlipper.getDisplayedChild();
            if (newDisplayItemIndex != displayedItemIndex) {
                if (newDisplayItemIndex > displayedItemIndex) {
                    viewFlipper.setInAnimation(this, R.anim.right_in);
                    viewFlipper.setOutAnimation(this, R.anim.left_out);
                } else {
                    viewFlipper.setInAnimation(this, R.anim.left_in);
                    viewFlipper.setOutAnimation(this, R.anim.right_out);
                }

                viewFlipper.setDisplayedChild(newDisplayItemIndex);
                setTitle(newTitle);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeCurrentView(Current current) throws Exception {
        LOG.trace("initializeCurrentView");

        if (current != null) {
            ViewStub viewStub = findViewById(R.id.weatherViewCurrentViewStub);
            CurrentView currentView = new CurrentView(current);
            currentView.initialize(this, viewStub);
        }
    }

    private void initializeDailyView(Daily daily) throws Exception {
        LOG.trace("initializeDailyView");

        if (daily != null) {
            ViewStub viewStub = findViewById(R.id.weatherViewDailyViewStub);
            dailyView = new DailyView(daily);
            dailyView.initialize(this, viewStub);
        }
    }

    private void initializeHourlyView(Hourly hourly) throws Exception {
        LOG.trace("initializeHourlyView");

        if (hourly != null) {
            ViewStub viewStub = findViewById(R.id.weatherViewHourlyViewStub);
            hourlyView = new HourlyView(hourly);
            hourlyView.initialize(this, viewStub);
        }
    }

    private void initializeMinutelyView(Minutely minutely) {
        LOG.trace("initializeMinutelyView");

        if (minutely != null) {
            ViewStub viewStub = findViewById(R.id.weatherViewMinutelyViewStub);
            MinutelyView minutelyView = new MinutelyView(minutely);
            minutelyView.initialize(this, viewStub);
        }
    }

}