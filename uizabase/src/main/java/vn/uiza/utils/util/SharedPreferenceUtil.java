package vn.uiza.utils.util;

import android.content.SharedPreferences;

public class SharedPreferenceUtil {

    public static <T> void put(SharedPreferences preferences, String key, T data) {
        SharedPreferences.Editor editor = preferences.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        } else {
            throw new IllegalArgumentException("This data is not support to save to shared preference");
        }
        editor.apply();
    }

    public static <T> T get(SharedPreferences preferences, String key, T defaultValue) {
        try {
            if (defaultValue instanceof String) {
                return (T) preferences.getString(key, (String) defaultValue);
            } else if (defaultValue instanceof Integer) {
                return (T) Integer.valueOf(preferences.getInt(key, (Integer) defaultValue));
            } else if (defaultValue instanceof Boolean) {
                return (T) Boolean.valueOf(preferences.getBoolean(key, (Boolean) defaultValue));
            } else if (defaultValue instanceof Long) {
                return (T) Long.valueOf(preferences.getLong(key, (Long) defaultValue));
            } else if (defaultValue instanceof Float) {
                return (T) Float.valueOf(preferences.getFloat(key, (Float) defaultValue));
            } else if (defaultValue instanceof Double) {
                return (T) Double.valueOf(preferences.getFloat(key, (Float) defaultValue));
            } else {
                throw new IllegalArgumentException("This data is not support to save to shared preference");
            }
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static boolean hasKey(SharedPreferences preferences, String key) {
        return preferences.contains(key);
    }

    public static void removeKey(SharedPreferences preferences, String key) {
        preferences.edit().remove(key).apply();
    }

    public static void clear(SharedPreferences preferences) {
        preferences.edit().clear().apply();
    }
}
