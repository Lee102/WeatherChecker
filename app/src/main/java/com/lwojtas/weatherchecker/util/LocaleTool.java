package com.lwojtas.weatherchecker.util;

import android.content.Intent;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatActivity;

import com.lwojtas.weatherchecker.MainActivity;

import java.util.Locale;

public class LocaleTool {

    //todo try createConfigurationContext
    public static void changeLanguage(AppCompatActivity context, Locale locale) {
        Configuration conf = context.getResources().getConfiguration();
        if (!conf.locale.equals(locale)) {
            conf.setLocale(locale);
            context.getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }

}
