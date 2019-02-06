package com.example.pocketsoccer.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class GameInfo {
    private SharedPreferences preferences;

    private String player1;

    private String player2;

    private boolean computer1;

    private boolean computer2;

    private int team1;

    private int team2;

    private int field;

    private String matchType;

    private int matchValue;

    private int speed;

    public GameInfo(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void loadFromBundle(Bundle bundle) {
        this.player1 = bundle.getString("player1");
        this.player2 = bundle.getString("player2");
        this.computer1 = bundle.getBoolean("computer1");
        this.computer2 = bundle.getBoolean("computer2");
        this.team1 = bundle.getInt("team1");
        this.team2 = bundle.getInt("team2");
        this.loadSettings();
    }

    public void loadFromPreferences() {
        this.player1 = preferences.getString("player1", "");
        this.player2 = preferences.getString("player2", "");
        this.computer1 = preferences.getBoolean("computer1", false);
        this.computer2 = preferences.getBoolean("computer2", false);
        this.team1 = preferences.getInt("team1", 0);
        this.team2 = preferences.getInt("team2", 0);
        this.loadSettings();
    }

    private void loadSettings() {
        this.field = preferences.getInt("field", 1);
        this.matchType = preferences.getString("match", "goals");
        this.matchValue = preferences.getInt(this.matchType, 3);
        this.speed = preferences.getInt("speed", 4);
    }

    public void saveToPreferences() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("player1", player1);
        editor.putString("player2", player2);
        editor.putBoolean("computer1", computer1);
        editor.putBoolean("computer2", computer2);
        editor.putInt("team1", team1);
        editor.putInt("team2", team2);
        editor.commit();
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public boolean isComputer1() {
        return computer1;
    }

    public void setComputer1(boolean computer1) {
        this.computer1 = computer1;
    }

    public boolean isComputer2() {
        return computer2;
    }

    public void setComputer2(boolean computer2) {
        this.computer2 = computer2;
    }

    public int getTeam1() {
        return team1;
    }

    public void setTeam1(int team1) {
        this.team1 = team1;
    }

    public int getTeam2() {
        return team2;
    }

    public void setTeam2(int team2) {
        this.team2 = team2;
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    public String getMatchType() {
        return matchType;
    }

    public void setMatchType(String matchType) {
        this.matchType = matchType;
    }

    public int getMatchValue() {
        return matchValue;
    }

    public void setMatchValue(int matchValue) {
        this.matchValue = matchValue;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
