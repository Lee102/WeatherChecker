package com.lwojtas.weatherchecker;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.City;
import com.lwojtas.weatherchecker.model.Settings;
import com.lwojtas.weatherchecker.util.FileTool;
import com.lwojtas.weatherchecker.util.LocaleTool;
import com.lwojtas.weatherchecker.util.NetworkConnectionTool;
import com.lwojtas.weatherchecker.util.WeatherCall;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CustomApplication extends Application {

    @Override
    public void onCreate() {
        FileTool fileTool = new FileTool();
        fileTool.readSettings(this);

        if (new NetworkConnectionTool().checkNetworkState(this)) {
            if (AppData.getSettings().getUpdateMode() == Settings.UpdateMode.ON_STARTUP) {
                List<Future<Void>> results = new ArrayList<>();
                ExecutorService executor = Executors.newFixedThreadPool(AppData.getSettings().getThreadPool());

                for (City city : AppData.getCities()) {
                    Callable<Void> call = new WeatherCall(city);
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
        } else
            Toast.makeText(this, this.getResources().getString(R.string.network_connection_error_message), Toast.LENGTH_SHORT).show();

        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleTool.setApplicationLocale(base, false));
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleTool.setApplicationLocale(this, false);
    }

}
