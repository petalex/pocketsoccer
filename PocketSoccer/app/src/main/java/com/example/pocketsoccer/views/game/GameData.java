package com.example.pocketsoccer.views.game;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import com.example.pocketsoccer.views.game.figures.Ball;
import com.example.pocketsoccer.views.game.figures.Player;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    public static final int N = 3;

    private List<Player> team1 = new ArrayList<>();

    private List<Player> team2 = new ArrayList<>();

    private Ball ball;

    public void reset(float width, float height) {
        PointF team1Up = new PointF(0.12f * width, 0.2f * height);
        team1.add(new Player(team1Up));
        PointF team1Middle = new PointF(0.23f * width, 0.5f * height);
        team1.add(new Player(team1Middle));
        PointF team1Down = new PointF(0.12f * width, 0.8f * height);
        team1.add(new Player(team1Down));

        PointF team2Up = new PointF(0.88f * width, 0.2f * height);
        team2.add(new Player(team2Up));
        PointF team2Middle = new PointF(0.77f * width, 0.5f * height);
        team2.add(new Player(team2Middle));
        PointF team2Down = new PointF(0.88f * width, 0.8f * height);
        team2.add(new Player(team2Down));

        PointF ballCenter = new PointF(0.5f * width, 0.5f * height);
        ball = new Ball(ballCenter);
    }

    public List<Player> getTeam1() {
        return team1;
    }

    public List<Player> getTeam2() {
        return team2;
    }

    public Ball getBall() {
        return ball;
    }
}
