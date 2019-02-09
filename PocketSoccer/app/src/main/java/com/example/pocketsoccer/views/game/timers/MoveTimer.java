package com.example.pocketsoccer.views.game.timers;

import com.example.pocketsoccer.views.game.GameImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MoveTimer {
    /**
     * Time for move
     */
    public static int MOVE_TIME = 5;

    private static GameImageView listener;

    private static boolean started, paused;

    private static Timer timer;

    private static int time;

    public static void init(GameImageView listener) {
        MoveTimer.listener = listener;
    }

    public static void setTimer() {
        release();
        timer = new Timer();
        time = 0;
        started = false;
        paused = false;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!started) {
                    started = true;
                    return;
                }
                if (!paused) {
                    time++;
                    listener.invalidate(); // To show updated time
                    if (time == MOVE_TIME) {
                        listener.swapTeamOnMove();
                        time = 0;
                    }
                }
            }
        }, 0,  1000);
    }

    public static int getMoveTime() {
        return MOVE_TIME - time;
    }

    public static void unpauseTimer() {
        paused = false;
    }

    public static void pauseTimer() {
        paused = true;
    }

    public static void release() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
