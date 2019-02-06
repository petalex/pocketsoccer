package com.example.pocketsoccer.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsPreferences {
    private static Context context;

    private static SettingsPreferences instance;

    private SharedPreferences preferences;

    private SettingsPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SettingsPreferences getInstance(Context context) {
        if (SettingsPreferences.context == null || SettingsPreferences.context != context) {
            SettingsPreferences.context = context;
            instance = new SettingsPreferences();
        }
        return instance;
    }

    public int getIntSetting(String setting, int defaultValue) {
        return preferences.getInt(setting, defaultValue);
    }

    public void setIntSetting(String setting, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(setting, value);
        editor.commit();
    }

    public String getStringSetting(String setting, String defaultValue) {
        return preferences.getString(setting, defaultValue);
    }

    public void setStringSetting(String setting, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(setting, value);
        editor.commit();
    }
}
