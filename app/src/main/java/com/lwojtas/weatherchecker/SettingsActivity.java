package com.lwojtas.weatherchecker;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.Settings;
import com.lwojtas.weatherchecker.util.LocaleTool;
import com.lwojtas.weatherchecker.util.exception.IllegalNegativeNumberException;
import com.lwojtas.weatherchecker.util.exception.IllegalNegativeOrZeroNumberException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class SettingsActivity extends AppCompatActivity {

    private static final Logger LOG = LoggerFactory.getLogger(SettingsActivity.class);

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
    private Spinner logModeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LOG.trace("onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getResources().getString(R.string.settings_title));

        findViews();
        fillViews();
    }

    @Override
    protected void attachBaseContext(Context base) {
        LOG.trace("attachBaseContext");

        super.attachBaseContext(LocaleTool.setApplicationLocale(base, false));
    }

    public void onSaveButtonClick(View view) {
        LOG.trace("onSaveButtonClick");

        try {
            Settings settings = AppData.getSettings();

            settings.setUnits(Settings.Units.fromNum(unitsSpinner.getSelectedItemPosition()));
            settings.setDecimals(Integer.parseInt(decimalsEditText.getText().toString()));
            settings.setPreciseDecimals(Integer.parseInt(preciseDecimalsEditText.getText().toString()));
            settings.setWeatherActualThreshold(weatherActualThresholdSeekBar.getProgress());

            Locale language = getLanguageSpinnerValue();
            boolean languageChanged = settings.getLocale() != language;

            if (updateModeToggleButton.isChecked())
                settings.setUpdateMode(Settings.UpdateMode.ON_STARTUP);
            else
                settings.setUpdateMode(Settings.UpdateMode.MANUAL);

            settings.setTimeout(Integer.parseInt(timeoutEditText.getText().toString()));
            settings.setThreadPool(Integer.parseInt(threadPoolEditText.getText().toString()));
            settings.setAppId(appIdEditText.getText().toString());

            Level logMode = settings.formatLogMode(logModeSpinner.getSelectedItemPosition());
            boolean logModeChanged = settings.getLogMode() != logMode;

            if (logModeChanged) {
                settings.setLogMode(logMode);

                ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("ROOT").setLevel(logMode);
            }

            if (languageChanged) {
                settings.setLocale(language);
                LocaleTool.setApplicationLocale(this, true);
            }

            LOG.info("settings saved");
            LOG.debug("settings: units: " + settings.getUnits().getTag() + ", decimals: " + settings.getDecimals() +
                    ", preciseDecimals: " + settings.getPreciseDecimals() + ", weatherActualThreshold: " + settings.getWeatherActualThreshold() +
                    ", locale: " + settings.getLocale().toLanguageTag() + ", updateMode: " + settings.getUpdateMode().getNUM() +
                    ", timeout: " + settings.getTimeout() + ", threadPool: " + settings.getThreadPool() +
                    ", logMode: " + settings.getLogModeNum());

            setResult(1);
            finish();
        } catch (IllegalNegativeNumberException e) {
            LOG.warn("settings negative number error");
            Toast.makeText(this, this.getResources().getString(R.string.settings_negative_number_error_message), Toast.LENGTH_SHORT).show();
        } catch (IllegalNegativeOrZeroNumberException e) {
            LOG.warn("settings negative or zero number error");
            Toast.makeText(this, this.getResources().getString(R.string.settings_negative_or_zero_number_error_message), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            LOG.warn("settings save error");
            Toast.makeText(this, this.getResources().getString(R.string.settings_save_error_message), Toast.LENGTH_SHORT).show();
        }
    }

    private void findViews() {
        LOG.trace("findViews");

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
        logModeSpinner = findViewById(R.id.settingsLogModeSpinner);
    }

    private void fillViews() {
        LOG.trace("fillViews");

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
        logModeSpinner.setSelection(settings.getLogModeNum());
    }

    private void fillWeatherActualThresholdTextView(int progress) {
        LOG.trace("fillWeatherActualThresholdTextView");

        String end;
        if (progress == 1)
            end = getResources().getString(R.string.settings_weather_actual_threshold_end);
        else
            end = getResources().getString(R.string.settings_weather_actual_threshold_end_plural);

        String text = getResources().getString(R.string.settings_weather_actual_threshold)
                + " "
                + progress
                + " "
                + end;

        weatherActualThresholdTextView.setText(text);
    }

    public enum LocaleMapper {
        CHINESE(0, "zh-CN"),
        DUTCH(1, "nl-NL"),
        ENGLISH(2, "en"),
        FRENCH(3, "fr-FR"),
        GERMAN(4, "de-DE"),
        ITALIAN(5, "it-IT"),
        JAPANESE(6, "ja-JP"),
        POLISH(7, "pl-PL"),
        PORTUGUESE(8, "pt-PT"),
        RUSSIAN(9, "ru-RU"),
        SPANISH(10, "es-ES");

        private final int NUM;
        private final String TAG;

        LocaleMapper(int num, String tag) {
            this.NUM = num;
            this.TAG = tag;
        }

        public int getNUM() {
            return NUM;
        }

        public String getTAG() {
            return TAG;
        }

        public static LocaleMapper forNum(int num) {
            switch (num) {
                case 0:
                    return CHINESE;
                case 1:
                    return DUTCH;
                case 3:
                    return FRENCH;
                case 4:
                    return GERMAN;
                case 5:
                    return ITALIAN;
                case 6:
                    return JAPANESE;
                case 7:
                    return POLISH;
                case 8:
                    return PORTUGUESE;
                case 9:
                    return RUSSIAN;
                case 10:
                    return SPANISH;
                case 2:
                default:
                    return ENGLISH;
            }
        }

        public static LocaleMapper forTag(String tag) {
            switch (tag) {
                case "zh-CN":
                    return CHINESE;
                case "nl-NL":
                    return DUTCH;
                case "fr-FR":
                    return FRENCH;
                case "de-DE":
                    return GERMAN;
                case "it-IT":
                    return ITALIAN;
                case "ja-JP":
                    return JAPANESE;
                case "pl-PL":
                    return POLISH;
                case "pt-PT":
                    return PORTUGUESE;
                case "ru-RU":
                    return RUSSIAN;
                case "es-ES":
                    return SPANISH;
                case "en":
                default:
                    return ENGLISH;
            }
        }
    }

    private void fillLanguageSpinner() {
        LOG.trace("fillLanguageSpinner");

        languageSpinner.setSelection(LocaleMapper.forTag(AppData.getSettings().getLocale().toLanguageTag()).getNUM());
    }

    private Locale getLanguageSpinnerValue() {
        LOG.trace("getLanguageSpinnerValue");

        return Locale.forLanguageTag(LocaleMapper.forNum(languageSpinner.getSelectedItemPosition()).getTAG());
    }

}