package com.lwojtas.weatherchecker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.lwojtas.weatherchecker.model.AppData;
import com.lwojtas.weatherchecker.util.FileTool;
import com.lwojtas.weatherchecker.util.LocaleTool;
import com.lwojtas.weatherchecker.view.MainView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout citiesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        citiesView = findViewById(R.id.mainCitiesView);

        MainView mainView = new MainView(AppData.getCities());
        mainView.initialize(this, citiesView);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleTool.setApplicationLocale(base, false));
    }

    @Override
    protected void onPause() {
        FileTool fileTool = new FileTool();
        fileTool.save(this);

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mainMenuSettings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivityForResult(intent, 0);
                break;
            case R.id.mainMenuAbout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(this.getResources().getString(R.string.about_title));
                CharSequence[] text = {this.getResources().getString(R.string.about)};
                builder.setItems(text, null);
                AlertDialog alert = builder.create();
                alert.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            MainView mainView = new MainView(AppData.getCities());
            mainView.initialize(this, citiesView);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onAddButtonClick(View view) {
        Intent intent = new Intent(this, CityActivity.class);
        startActivityForResult(intent, 0);

        MainView mainView = new MainView(AppData.getCities());
        mainView.initialize(this, citiesView);
    }

}