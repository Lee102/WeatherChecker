package com.lwojtas.weatherchecker.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;

import com.lwojtas.weatherchecker.MainActivity;
import com.lwojtas.weatherchecker.model.AppData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public class LocaleTool {

    private static final Logger LOG = LoggerFactory.getLogger(LocaleTool.class);

    public static Context setApplicationLocale(Context context, boolean settingsChangeLocale) {
        LOG.trace("setApplicationLocale");

        if (context != null) {
            Locale locale = AppData.getSettings().getLocale();
            Configuration config = new Configuration(context.getResources().getConfiguration());

            Locale.setDefault(locale);

            config.setLocale(locale);
            context = context.createConfigurationContext(config);

            if (settingsChangeLocale) {
                LOG.info("setApplicationLocale - new locale: " + locale.toLanguageTag());

                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        }

        return context;
    }

    public static String getDoubleAsString(double value, int decimals, Locale locale, String unit, boolean unitWithSpace) {
        LOG.trace("getDoubleAsString");

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(decimals, RoundingMode.HALF_UP);

        String pattern = "%." + decimals + "f";

        if (unitWithSpace)
            unit = " " + unit;

        return String.format(locale, pattern, bd.doubleValue()) + unit;
    }

}
