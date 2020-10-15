package com.lwojtas.weatherchecker.model;

import com.lwojtas.weatherchecker.util.exception.IllegalNegativeNumberException;
import com.lwojtas.weatherchecker.util.exception.IllegalNegativeOrZeroNumberException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import ch.qos.logback.classic.Level;

public class Settings {

    public enum Units {
        STANDARD(0),
        METRIC(1),
        IMPERIAL(2);

        private final int NUM;

        Units(int num) {
            this.NUM = num;
        }

        public int getNUM() {
            return NUM;
        }

        public String getTag() {
            String tag = "metric";
            switch (NUM) {
                case 0:
                    tag = "standard";
                    break;
                case 2:
                    tag = "imperial";
                    break;
            }

            return tag;
        }

        public static Units fromNum(int num) {
            for (Units e : values())
                if (num == e.NUM)
                    return e;
            return null;
        }
    }

    public enum UnitType {
        TEMP,
        WIND_SPEED
    }

    public enum UpdateMode {
        MANUAL(0),
        ON_STARTUP(1);

        private final int NUM;

        UpdateMode(int num) {
            this.NUM = num;
        }

        public int getNUM() {
            return NUM;
        }

        public static UpdateMode fromNum(int num) {
            for (UpdateMode e : values())
                if (num == e.NUM)
                    return e;
            return null;
        }
    }

    private final String UNITS_JSON = "units";
    private Units units;
    private final String DECIMALS_JSON = "decimals";
    private Integer decimals;
    private final String PRECISE_DECIMALS_JSON = "preciseDecimals";
    private Integer preciseDecimals;
    private final String WEATHER_ACTUAL_THRESHOLD = "weatherActualThreshold";
    private Integer weatherActualThreshold;
    private final String LOCALE_JSON = "locale";
    private Locale locale;
    private final String UPDATE_MODE_JSON = "updateMode";
    private UpdateMode updateMode;
    private final String TIMEOUT_JSON = "timeout";
    private Integer timeout;
    private final String THREAD_POOL_JSON = "threadPool";
    private Integer threadPool;
    private final String APP_ID_JSON = "appID";
    private String appId;
    private final String LOG_MODE_JSON = "logMode";
    private Level logMode;

    public Settings() {
        units = Units.METRIC;
        decimals = 0;
        preciseDecimals = 2;
        weatherActualThreshold = 24;
        locale = Locale.ENGLISH;
        updateMode = UpdateMode.MANUAL;
        timeout = 10000;
        threadPool = 10;
        appId = "";
        logMode = Level.OFF;
    }

    public Settings(JSONObject obj) throws JSONException {
        this();

        if (obj != null) {
            if (obj.has(UNITS_JSON))
                units = Units.fromNum(obj.getInt(UNITS_JSON));

            if (obj.has(DECIMALS_JSON))
                decimals = obj.getInt(DECIMALS_JSON);

            if (obj.has(PRECISE_DECIMALS_JSON))
                preciseDecimals = obj.getInt(PRECISE_DECIMALS_JSON);

            if (obj.has(WEATHER_ACTUAL_THRESHOLD))
                weatherActualThreshold = obj.getInt(WEATHER_ACTUAL_THRESHOLD);

            if (obj.has(LOCALE_JSON))
                locale = Locale.forLanguageTag(obj.getString(LOCALE_JSON));

            if (obj.has(UPDATE_MODE_JSON))
                updateMode = UpdateMode.fromNum(obj.getInt(UPDATE_MODE_JSON));

            if (obj.has(TIMEOUT_JSON))
                timeout = obj.getInt(TIMEOUT_JSON);

            if (obj.has(THREAD_POOL_JSON))
                threadPool = obj.getInt(THREAD_POOL_JSON);

            if (obj.has(APP_ID_JSON))
                appId = obj.getString(APP_ID_JSON);

            if (obj.has(LOG_MODE_JSON))
                logMode = Level.toLevel(obj.getInt(LOG_MODE_JSON));
        }
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject obj = new JSONObject();

        obj.put(UNITS_JSON, units.getNUM());
        obj.put(DECIMALS_JSON, decimals);
        obj.put(PRECISE_DECIMALS_JSON, preciseDecimals);
        obj.put(WEATHER_ACTUAL_THRESHOLD, weatherActualThreshold);
        obj.put(LOCALE_JSON, locale.toLanguageTag());
        obj.put(UPDATE_MODE_JSON, updateMode.getNUM());
        obj.put(TIMEOUT_JSON, timeout);
        obj.put(THREAD_POOL_JSON, threadPool);
        obj.put(APP_ID_JSON, appId);
        obj.put(LOG_MODE_JSON, logMode.toInt());

        return obj;
    }

    public Units getUnits() {
        return units;
    }

    public String getUnitString(UnitType unitType) {
        String unit = "";

        switch (unitType) {
            case TEMP:
                switch (units) {
                    case STANDARD:
                        unit = "K";
                        break;
                    case METRIC:
                        unit = "°C";
                        break;
                    case IMPERIAL:
                        unit = "°F";
                        break;
                }
                break;
            case WIND_SPEED:
                switch (units) {
                    case STANDARD:
                    case METRIC:
                        unit = "m/s";
                        break;
                    case IMPERIAL:
                        unit = "mph";
                        break;
                }
                break;
        }

        return unit;
    }

    public void setUnits(Units units) {
        this.units = units;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public String getDecimalsAsString() {
        return String.valueOf(decimals);
    }

    public void setDecimals(Integer decimals) throws IllegalNegativeNumberException {
        if (decimals < 0)
            throw new IllegalNegativeNumberException();

        this.decimals = decimals;
    }

    public Integer getPreciseDecimals() {
        return preciseDecimals;
    }

    public String getPreciseDecimalsAsString() {
        return String.valueOf(preciseDecimals);
    }

    public void setPreciseDecimals(Integer preciseDecimals) throws IllegalNegativeNumberException {
        if (preciseDecimals < 0)
            throw new IllegalNegativeNumberException();

        this.preciseDecimals = preciseDecimals;
    }

    public Integer getWeatherActualThreshold() {
        return weatherActualThreshold;
    }

    public void setWeatherActualThreshold(Integer weatherActualThreshold) throws IllegalNegativeNumberException {
        if (weatherActualThreshold < 0)
            throw new IllegalNegativeNumberException();

        this.weatherActualThreshold = weatherActualThreshold;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public UpdateMode getUpdateMode() {
        return updateMode;
    }

    public void setUpdateMode(UpdateMode updateMode) {
        this.updateMode = updateMode;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public String getTimeoutAsString() {
        return String.valueOf(timeout);
    }

    public void setTimeout(Integer timeout) throws IllegalNegativeOrZeroNumberException {
        if (timeout < 1)
            throw new IllegalNegativeOrZeroNumberException();

        this.timeout = timeout;
    }

    public Integer getThreadPool() {
        return threadPool;
    }

    public String getThreadPoolAsString() {
        return String.valueOf(threadPool);
    }

    public void setThreadPool(Integer threadPool) throws IllegalNegativeOrZeroNumberException {
        if (threadPool < 1)
            throw new IllegalNegativeOrZeroNumberException();

        this.threadPool = threadPool;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Level getLogMode() {
        return logMode;
    }

    public Integer getLogModeNum() {
        if (this.logMode == null)
            return null;
        else
            switch (this.logMode.toInteger()) {
                case Level.ALL_INT:
                    return 6;
                case Level.TRACE_INT:
                    return 5;
                case Level.DEBUG_INT:
                    return 4;
                case Level.INFO_INT:
                    return 3;
                case Level.WARN_INT:
                    return 2;
                case Level.ERROR_INT:
                    return 1;
                case Level.OFF_INT:
                default:
                    return 0;
            }
    }

    public void setLogMode(Level logMode) {
        this.logMode = logMode;
    }

    public Level formatLogMode(int logMode) {
        switch (logMode) {
            case 6:
                return Level.ALL;
            case 5:
                return Level.TRACE;
            case 4:
                return Level.DEBUG;
            case 3:
                return Level.INFO;
            case 2:
                return Level.WARN;
            case 1:
                return Level.ERROR;
            case 0:
            default:
                return Level.OFF;
        }
    }

}
