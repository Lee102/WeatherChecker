package com.lwojtas.weatherchecker.util;

import android.content.Context;
import android.widget.Toast;

import com.lwojtas.weatherchecker.R;
import com.lwojtas.weatherchecker.model.AppData;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FileTool {

    private static final Logger LOG = LoggerFactory.getLogger(FileTool.class);

    private final String FILE_NAME = "WeatherChecker.json";

    public void save(Context context) {
        LOG.trace("save");

        if (context != null) {
            try {
                JSONObject obj = AppData.toJSON();

                FileOutputStream outputStream = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                outputStream.write(obj.toString().getBytes());
                outputStream.close();

                LOG.info("file saved");
            } catch (Exception e) {
                LOG.error("save error" + e.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.file_save_error_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void load(Context context) {
        LOG.trace("load");

        if (context != null) {
            try {
                FileInputStream inputStream = context.openFileInput(FILE_NAME);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String str = bufferedReader.readLine();
                AppData.fromJSON(new JSONObject(str));

                LOG.info("file loaded");
            } catch (Exception e) {
                LOG.error("load error" + e.getMessage());
                Toast.makeText(context, context.getResources().getString(R.string.file_load_error_message), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
