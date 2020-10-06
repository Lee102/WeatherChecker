package com.lwojtas.weatherchecker;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.model.Settings;

import java.util.Locale;

import static com.lwojtas.weatherchecker.util.LocaleTool.changeLanguage;

public class SettingsActivity extends AppCompatActivity {

    private Spinner unitsSpinner;
    private EditText decimalsEditText;
    private EditText preciseDecimalsEditText;
    private Spinner languageSpinner;
    private Spinner updateModeSpinner;
    private EditText timeoutEditText;
    private EditText threadPoolEditText;
    private EditText appIdEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(getResources().getString(R.string.settings_title));

        findViews();

        Settings settings = AppData.getSettings();
        unitsSpinner.setSelection(settings.getUnits().getNUM());
        decimalsEditText.setText(settings.getDecimalsAsString());
        preciseDecimalsEditText.setText(settings.getPreciseDecimalsAsString());
        fillLanguageSpinner();
        updateModeSpinner.setSelection(settings.getUpdateMode().getNUM());
        timeoutEditText.setText(settings.getTimeoutAsString());
        threadPoolEditText.setText(settings.getThreadPoolAsString());
        appIdEditText.setText(settings.getAppId());
    }

    public void onSaveButtonClick(View view) {
        Settings settings = AppData.getSettings();
        Locale language = getLanguageSpinnerValue();
        boolean languageChanged = settings.getLocale() != language;

        settings.setUnits(Settings.Units.fromNum(unitsSpinner.getSelectedItemPosition()));
        settings.setDecimals(Integer.parseInt(decimalsEditText.getText().toString()));
        settings.setPreciseDecimals(Integer.parseInt(preciseDecimalsEditText.getText().toString()));
        settings.setLocale(language);
        settings.setUpdateMode(Settings.UpdateMode.fromNum(updateModeSpinner.getSelectedItemPosition()));
        settings.setTimeout(Integer.parseInt(timeoutEditText.getText().toString()));
        settings.setThreadPool(Integer.parseInt(threadPoolEditText.getText().toString()));
        settings.setAppId(appIdEditText.getText().toString());

        if (languageChanged) {
            changeLanguage(this, language);
        }

        finish();
    }

    private void findViews() {
        unitsSpinner = findViewById(R.id.settingsUnitsSpinner);
        decimalsEditText = findViewById(R.id.settingsDecimalsEditText);
        preciseDecimalsEditText = findViewById(R.id.settingsPreciseDecimalsEditText);
        languageSpinner = findViewById(R.id.settingsLanguageSpinner);
        updateModeSpinner = findViewById(R.id.settingsUpdateModeSpinner);
        timeoutEditText = findViewById(R.id.settingsTimeoutEditText);
        threadPoolEditText = findViewById(R.id.settingsThreadPoolEditText);
        appIdEditText = findViewById(R.id.settingsAppIdEditText);
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