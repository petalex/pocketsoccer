package com.example.pocketsoccer.views.game.timers;

import com.example.pocketsoccer.utils.GameInfoManager;
import com.example.pocketsoccer.views.game.GameActivity;
import com.example.pocketsoccer.views.game.GameImageView;

import java.util.Timer;
import java.util.TimerTask;

public class MatchTimer {
    private static GameImageView listener;

    private static boolean started, paused;

    private static Timer timer;

    public static void init(GameImageView listener) {
        MatchTimer.listener = listener;
    }

    public static void setTimer() {
        release();
        timer = new Timer();
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
                    GameInfoManager.addToTime();
                    if (GameInfoManager.getMatchType().equals("time") && GameInfoManager.getTime() == GameInfoManager.getMatch() * 60) {
                        listener.timeFinished();
                        timer.cancel();
                    }
                }
            }
        }, 0,  1000);
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
