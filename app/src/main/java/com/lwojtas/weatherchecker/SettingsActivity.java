package com.lwojtas.weatherchecker;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.Settings;
import com.lwojtas.weatherchecker.util.LocaleTool;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    private Spinner unitsSpinner;
    private EditText decimalsEditText;
    private EditText preciseDecimalsEditText;
    private TextView weatherActualThresholdTextView;
    private SeekBar weatherActualThresholdSeekBar;
    private Spinner languageSpinner;
    private ToggleButton updateModeToggleButton;
    private EditText timeoutEditText;
    private EditText threadPoolEditText;
    private EditText appIdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getResources().getString(R.string.settings_title));

        findViews();
        fillViews();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleTool.setApplicationLocale(base, false));
    }

    public void onSaveButtonClick(View view) {
        Settings settings = AppData.getSettings();
        Locale language = getLanguageSpinnerValue();
        boolean languageChanged = settings.getLocale() != language;

        settings.setUnits(Settings.Units.fromNum(unitsSpinner.getSelectedItemPosition()));
        settings.setDecimals(Integer.parseInt(decimalsEditText.getText().toString()));
        settings.setPreciseDecimals(Integer.parseInt(preciseDecimalsEditText.getText().toString()));
        settings.setWeatherActualThreshold(weatherActualThresholdSeekBar.getProgress());
        settings.setLocale(language);

        if (updateModeToggleButton.isChecked())
            settings.setUpdateMode(Settings.UpdateMode.ON_STARTUP);
        else
            settings.setUpdateMode(Settings.UpdateMode.MANUAL);

        settings.setTimeout(Integer.parseInt(timeoutEditText.getText().toString()));
        settings.setThreadPool(Integer.parseInt(threadPoolEditText.getText().toString()));
        settings.setAppId(appIdEditText.getText().toString());

        if (languageChanged) {
            LocaleTool.setApplicationLocale(this, true);
            setResult(1);
        }

        finish();
    }

    private void findViews() {
        unitsSpinner = findViewById(R.id.settingsUnitsSpinner);
        decimalsEditText = findViewById(R.id.settingsDecimalsEditText);
        preciseDecimalsEditText = findViewById(R.id.settingsPreciseDecimalsEditText);
        weatherActualThresholdTextView = findViewById(R.id.settingsWeatherActualThresholdTextView);
        weatherActualThresholdSeekBar = findViewById(R.id.settingsWeatherActualThresholdSeekBar);
        languageSpinner = findViewById(R.id.settingsLanguageSpinner);
        updateModeToggleButton = findViewById(R.id.settingsUpdateModeToggleButton);
        timeoutEditText = findViewById(R.id.settingsTimeoutEditText);
        threadPoolEditText = findViewById(R.id.settingsThreadPoolEditText);
        appIdEditText = findViewById(R.id.settingsAppIdEditText);
    }

    private void fillViews() {
        Settings settings = AppData.getSettings();
        unitsSpinner.setSelection(settings.getUnits().getNUM());
        decimalsEditText.setText(settings.getDecimalsAsString());
        preciseDecimalsEditText.setText(settings.getPreciseDecimalsAsString());

        fillWeatherActualThresholdTextView(settings.getWeatherActualThreshold());
        weatherActualThresholdSeekBar.setMin(1);
        weatherActualThresholdSeekBar.setMax(168);
        weatherActualThresholdSeekBar.setProgress(settings.getWeatherActualThreshold());
        weatherActualThresholdSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                fillWeatherActualThresholdTextView(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        fillLanguageSpinner();

        updateModeToggleButton.setChecked(settings.getUpdateMode() == Settings.UpdateMode.ON_STARTUP);

        timeoutEditText.setText(settings.getTimeoutAsString());
        threadPoolEditText.setText(settings.getThreadPoolAsString());
        appIdEditText.setText(settings.getAppId());
    }

    private void fillLanguageSpinner() {
        int selection = 0;

        switch (AppData.getSettings().getLocale().toLanguageTag()) {
            case "en":
                break;
            case "pl":
            default:
                selection = 1;
                break;
        }

        languageSpinner.setSelection(selection);
    }

    private void fillWeatherActualThresholdTextView(int progress) {
        String text = getResources().getString(R.string.settings_weather_actual_threshold)
                + " "
                + progress
                + " "
                + getResources().getString(R.string.settings_weather_actual_threshold_end);

        weatherActualThresholdTextView.setText(text);
    }

    private Locale getLanguageSpinnerValue() {
        Locale locale = null;

        switch (languageSpinner.getSelectedItemPosition()) {
            case 0:
                locale = Locale.ENGLISH;
                break;
            case 1:
                locale = Locale.forLanguageTag("pl");
                break;
        }

        return locale;
    }

}