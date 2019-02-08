package com.example.pocketsoccer.views.game;

import android.graphics.PointF;
import android.graphics.RectF;

import com.example.pocketsoccer.views.game.figures.Ball;
import com.example.pocketsoccer.views.game.figures.Figure;
import com.example.pocketsoccer.views.game.figures.Player;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    public static final int N = 3;

    private List<Figure> team1;

    private List<Figure> team2;

    private List<Figure> teamOnMove;

    private Figure ball;

    private List<RectF> goals;

    public void reset(float width, float height, int team) {
        team1 = new ArrayList<>();
        PointF team1Up = new PointF(0.12f * width, 0.2f * height);
        team1.add(new Player(team1Up));
        PointF team1Middle = new PointF(0.23f * width, 0.5f * height);
        team1.add(new Player(team1Middle));
        PointF team1Down = new PointF(0.12f * width, 0.8f * height);
        team1.add(new Player(team1Down));

        team2 = new ArrayList<>();
        PointF team2Up = new PointF(0.88f * width, 0.2f * height);
        team2.add(new Player(team2Up));
        PointF team2Middle = new PointF(0.77f * width, 0.5f * height);
        team2.add(new Player(team2Middle));
        PointF team2Down = new PointF(0.88f * width, 0.8f * height);
        team2.add(new Player(team2Down));

        switch (team) {
            case 1:
                teamOnMove = team1;
                break;
            case 2:
                teamOnMove = team2;
                break;
        }

        PointF ballCenter = new PointF(0.5f * width, 0.5f * height);
        ball = new Ball(ballCenter);

        goals = new ArrayList<>();
        RectF goal1Top = new RectF(0.005f * width, 0.35f * height, 0.06f * width, 0.37f * height);
        goals.add(goal1Top);
        RectF goal1Bottom = new RectF(0.005f * width, 0.63f * height, 0.06f * width, 0.65f * height);
        goals.add(goal1Bottom);
        RectF goal2Top = new RectF(0.94f * width, 0.35f * height, 0.995f * width, 0.37f * height);
        goals.add(goal2Top);
        RectF goal2Bootom = new RectF(0.94f * width, 0.63f * height, 0.995f * width, 0.65f * height);
        goals.add(goal2Bootom);
    }

    public List<Figure> getTeam1() {
        return team1;
    }

    public List<Figure> getTeam2() {
        return team2;
    }

    public List<Figure> getTeamOnMove() {
        return teamOnMove;
    }

    public void setTeamOnMove(List<Figure> teamOnMove) {
        this.teamOnMove = teamOnMove;
    }

    public Figure getBall() {
        return ball;
    }

    public List<RectF> getGoals() {
        return goals;
    }
}
