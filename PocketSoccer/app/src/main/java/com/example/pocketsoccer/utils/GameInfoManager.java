package com.example.pocketsoccer.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.pocketsoccer.views.game.GameData;
import com.example.pocketsoccer.views.game.figures.Ball;
import com.example.pocketsoccer.views.game.figures.Figure;
import com.example.pocketsoccer.views.game.figures.Player;

import java.util.ArrayList;
import java.util.List;

public class GameInfoManager {
    private static SharedPreferences preferences;

    private static String player1;

    private static String player2;

    private static boolean computer1;

    private static boolean computer2;

    private static int team1;

    private static int team2;

    private static int teamOnMove;

    private static int score1;

    private static int score2;

    private static int time;

    private static int field;

    private static String matchType;

    private static int match;

    private static int speed;

    public static void init(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void loadFromBundle(Bundle bundle) {
        player1 = bundle.getString("player1");
        player2 = bundle.getString("player2");
        computer1 = bundle.getBoolean("computer1");
        computer2 = bundle.getBoolean("computer2");
        team1 = bundle.getInt("team1");
        team2 = bundle.getInt("team2");
        teamOnMove = 1;
        loadSettings();
        score1 = 0;
        score2 = 0;
        time = 0;
        clearSavedGame();
    }

    public static void loadFromPreferences(GameData data) {
        player1 = preferences.getString("player1", "");
        player2 = preferences.getString("player2", "");
        computer1 = preferences.getBoolean("computer1", false);
        computer2 = preferences.getBoolean("computer2", false);
        team1 = preferences.getInt("team1", 0);
        team2 = preferences.getInt("team2", 0);
        teamOnMove = preferences.getInt("teamOnMove", 0);
        score1 = preferences.getInt("score1", 0);
        score2 = preferences.getInt("score2", 0);
        time = preferences.getInt("time", 0);
        loadFiguresFromPreferences(data);
        loadSettings();
        clearSavedGame();
    }

    private static void loadFiguresFromPreferences(GameData data) {
        List<Figure> team1 = new ArrayList<>();
        PointF team1Up = new PointF(preferences.getFloat("team1Player0X", 0), preferences.getFloat("team1Player0Y", 0));
        team1.add(new Player(team1Up));
        PointF team1Middle = new PointF(preferences.getFloat("team1Player1X", 0), preferences.getFloat("team1Player1Y", 0));
        team1.add(new Player(team1Middle));
        PointF team1Down = new PointF(preferences.getFloat("team1Player2X", 0), preferences.getFloat("team1Player2Y", 0));
        team1.add(new Player(team1Down));

        List<Figure> team2 = new ArrayList<>();
        PointF team2Up = new PointF(preferences.getFloat("team2Player0X", 0), preferences.getFloat("team2Player0Y", 0));
        team2.add(new Player(team2Up));
        PointF team2Middle = new PointF(preferences.getFloat("team2Player1X", 0), preferences.getFloat("team2Player1Y", 0));
        team2.add(new Player(team2Middle));
        PointF team2Down = new PointF(preferences.getFloat("team2Player2X", 0), preferences.getFloat("team2Player2Y", 0));
        team2.add(new Player(team2Down));

        PointF ballCenter = new PointF(preferences.getFloat("ballX", 0), preferences.getFloat("ballY", 0));
        Figure ball = new Ball(ballCenter);

        data.setTeam1(team1);
        data.setTeam2(team2);
        data.setBall(ball);
        data.setSelectedPlayer(null);
    }

    private static void loadSettings() {
        field = SettingsManager.getField(SettingsManager.getDefaultField());
        matchType = SettingsManager.getMatchType(SettingsManager.getDefaultMatchType());
        match = SettingsManager.getMatch(SettingsManager.getDefaultMatch());
        speed = SettingsManager.getSpeed(SettingsManager.getDefaultSpeed());
    }

    private static void clearSavedGame() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("saved", false);
        editor.commit();
    }

    public static void saveToPreferences(GameData data) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("saved", true);
        editor.putString("player1", player1);
        editor.putString("player2", player2);
        editor.putBoolean("computer1", computer1);
        editor.putBoolean("computer2", computer2);
        editor.putInt("team1", team1);
        editor.putInt("team2", team2);
        editor.putInt("teamOnMove", teamOnMove);
        editor.putInt("score1", score1);
        editor.putInt("score2", score2);
        editor.putInt("time", time);
        saveFiguresToPreferences(editor, data);
        editor.commit();
    }

    private static void saveFiguresToPreferences(SharedPreferences.Editor editor, GameData data) {
        List<Figure> figures;
        figures = data.getTeam1();
        for (int i = 0; i < GameData.N; ++i) {
            editor.putFloat("team1Player" + i + "X", figures.get(i).getX());
            editor.putFloat("team1Player" + i + "Y", figures.get(i).getY());
        }
        figures = data.getTeam2();
        for (int i = 0; i < GameData.N; ++i) {
            editor.putFloat("team2Player" + i + "X", figures.get(i).getX());
            editor.putFloat("team2Player" + i + "Y", figures.get(i).getY());
        }
        editor.putFloat("ballX", data.getBall().getX());
        editor.putFloat("ballY", data.getBall().getY());
    }

    public static boolean isSaved() {
        if (preferences != null) {
            return preferences.getBoolean("saved", false);
        }
        return false;
    }

    public static String getPlayer1() {
        return player1;
    }

    public static void setPlayer1(String player1) {
        GameInfoManager.player1 = player1;
    }

    public static String getPlayer2() {
        return player2;
    }

    public static void setPlayer2(String player2) {
        GameInfoManager.player2 = player2;
    }

    public static boolean isComputer1() {
        return computer1;
    }

    public static void setComputer1(boolean computer1) {
        GameInfoManager.computer1 = computer1;
    }

    public static boolean isComputer2() {
        return computer2;
    }

    public static void setComputer2(boolean computer2) {
        GameInfoManager.computer2 = computer2;
    }

    public static int getTeam1() {
        return team1;
    }

    public static void setTeam1(int team1) {
        GameInfoManager.team1 = team1;
    }

    public static int getTeam2() {
        return team2;
    }

    public static void setTeam2(int team2) {
        GameInfoManager.team2 = team2;
    }

    public static int getTeamOnMove() {
        return teamOnMove;
    }

    public static void setTeamOnMove(int teamOnMove) {
        GameInfoManager.teamOnMove = teamOnMove;
    }

    public static int getScore1() {
        return score1;
    }

    public static void addToScore1() {
        score1++;
    }

    public static int getScore2() {
        return score2;
    }

    public static void addToScore2() {
        score2++;
    }

    public static int getTime() {
        return time;
    }

    public static void addToTime() {
        time++;
    }

    public static void resetTime() {
        time = 0;
    }

    public static int getField() {
        return field;
    }

    public static void setField(int field) {
        GameInfoManager.field = field;
    }

    public static String getMatchType() {
        return matchType;
    }

    public static void setMatchType(String matchType) {
        GameInfoManager.matchType = matchType;
    }

    public static int getMatch() {
        return match;
    }

    public static void setMatch(int match) {
        GameInfoManager.match = match;
    }

    public static int getSpeed() {
        return speed;
    }

    public static void setSpeed(int speed) {
        GameInfoManager.speed = speed;
    }
}