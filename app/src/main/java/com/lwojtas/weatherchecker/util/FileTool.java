package com.lwojtas.weatherchecker.util;

import android.content.Context;
import android.widget.Toast;

import com.lwojtas.weatherchecker.R;
import com.lwojtas.weatherchecker.model.AppData;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FileTool {

    private final String FILE_NAME = "WeatherChecker.dat";

    public void save(Context context) {
        if (context != null) {
            try {
                JSONObject obj = AppData.toJSON();

                FileOutputStream outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                outputStream.write(obj.toString().getBytes());
                outputStream.close();
            } catch (Exception e) {
                Toast.makeText(context, context.getResources().getString(R.string.file_save_error_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void readSettings(Context context) {
        if (context != null) {
            try {
                FileInputStream inputStream = context.openFileInput(FILE_NAME);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String str = bufferedReader.readLine();
                AppData.fromJSON(new JSONObject(str));
            } catch (Exception e) {
                Toast.makeText(context, context.getResources().getString(R.string.file_load_error_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
