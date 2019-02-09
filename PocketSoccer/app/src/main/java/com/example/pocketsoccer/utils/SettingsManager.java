package com.example.pocketsoccer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsManager {
    private static SharedPreferences preferences;

    public static void init(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static int getDefaultField() {
        int defaultField = getIntSetting("defaultField", 0);
        if (defaultField == 0) { // Only first time
            setIntSetting("defaultField", 1);
        }
        return getIntSetting("defaultField", 0);
    }

    public static int getField(int defaultField) {
        return getIntSetting("field", defaultField);
    }

    public static void setField(int field) {
        setIntSetting("field", field);
    }

    public static String getDefaultMatchType() {
        String defaultMatchType = getStringSetting("defaultMatchType", "");
        if (defaultMatchType == "") { // Only first time
            setStringSetting("defaultMatchType", "goals");
        }
        return getStringSetting("defaultMatchType", "");
    }

    public static String getMatchType(String defaultMatchType) {
        return getStringSetting("matchType", defaultMatchType);
    }

    public static void setMatchType(String matchType) {
        setStringSetting("matchType", matchType);
    }

    public static int getDefaultMatch() {
        int defaultMatch = getIntSetting("defaultMatch", 0);
        if (defaultMatch == 0) { // Only first time
            setIntSetting("defaultMatch", 3);
        }
        return getIntSetting("defaultMatch", 0);
    }

    public static int getMatch(int defaultMatch) {
        return getIntSetting("match", defaultMatch);
    }

    public static void setMatch(int match) {
        setIntSetting("match", match);
    }

    public static int getDefaultSpeed() {
        int defaultSpeed = getIntSetting("defaultSpeed", 0);
        if (defaultSpeed == 0) { // Only first time
            setIntSetting("defaultSpeed", 4);
        }
        return getIntSetting("defaultSpeed", 0);
    }

    public static int getSpeed(int defaultSpeed) {
        return getIntSetting("speed", defaultSpeed);
    }

    public static void setSpeed(int speed) {
        setIntSetting("speed", speed);
    }

    public static void resetAll() {
        setField(getDefaultField());
        setMatchType(getDefaultMatchType());
        setMatch(getDefaultMatch());
        setSpeed(getDefaultSpeed());
    }

    private static int getIntSetting(String setting, int defaultValue) {
        return preferences.getInt(setting, defaultValue);
    }

    private static void setIntSetting(String setting, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(setting, value);
        editor.commit();
    }

    private static String getStringSetting(String setting, String defaultValue) {
        return preferences.getString(setting, defaultValue);
    }

    private static void setStringSetting(String setting, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(setting, value);
        editor.commit();
    }
}
