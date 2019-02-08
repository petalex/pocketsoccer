package com.example.pocketsoccer.views.game;

import android.os.AsyncTask;

import com.example.pocketsoccer.views.game.figures.Figure;
import com.example.pocketsoccer.views.game.figures.Player;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MoveDetector {
    public static final int REFRESH_RATE = 100;

    private GameImageView game;

    private Timer moveTimer;

    private Figure touchedPlayer;

    private float downX, downY;

    private float speed;

    public MoveDetector(GameImageView game) {
        this.game = game;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void down(float x, float y) {
        touchedPlayer = findTouchedPlayer(x, y);
    }

    public void up(float x, float y) {
        if (touchedPlayer != null) {
            touchedPlayer.initMove(x - downX, y - downY, speed);
            startMovement();
            game.swapTeamOnMove();
        }
    }

    private void refreshGame() {
        game.invalidate();
    }

    private Figure findTouchedPlayer(float x, float y) {
        List<Figure> team = game.getData().getTeamOnMove();
        for (int i = 0; i < GameData.N; ++i) {
            Figure player = team.get(i);
            if (isTouched(player, x, y)) {
                downX = x;
                downY = y;
                return player;
            }
        }

        return null;
    }

    private boolean isTouched(Figure player, float x, float y) {
        return getDistance(player.getX(), player.getY(), x, y) <= player.getR();
    }

    private float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }


    public void startMovement() {
        if (moveTimer == null) {
            moveTimer = new Timer();
            moveTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    refreshGame();
                }
            }, 0, 1000 / REFRESH_RATE);
        }
    }

    public void endMovement() {
        if (moveTimer != null) {
            moveTimer.cancel();
            moveTimer = null;
        }

    }
}
