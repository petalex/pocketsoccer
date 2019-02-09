package com.example.pocketsoccer.utils;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;

import java.io.IOException;

public class ImageManager {
    private static AssetManager manager;

    private static Drawable background, ball, goals, selectedPlayer;

    public static void init(AssetManager manager) {
        ImageManager.manager = manager;
        try {
            background = Drawable.createFromStream(manager.open("backgrounds/main.jpg"), null);
            ball = Drawable.createFromStream(manager.open("balls/ball.png"), null);
            goals = Drawable.createFromStream(manager.open("fields/fieldgoals.png"), null);
            selectedPlayer = Drawable.createFromStream(manager.open("selectedplayer.png"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Drawable getBackground() {
        return background;
    }

    public static Drawable getBall() {
        return ball;
    }

    public static Drawable getField(int i) {
        Drawable field = null;
        try {
            field = Drawable.createFromStream(manager.open("fields/field" + i + ".png"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return field;
    }

    public static Drawable getGoals() {
        return goals;
    }

    public static Drawable getTeam(int i) {
        Drawable team = null;
        try {
            team = Drawable.createFromStream(manager.open("teams/t" + i + ".png"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return team;
    }

    public static Drawable getSelectedPlayer() {
        return selectedPlayer;
    }}
