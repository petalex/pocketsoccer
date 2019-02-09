package com.example.pocketsoccer.utils;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class SoundManager {
    private static MediaPlayer player;

    private static AssetFileDescriptor intro, bounce, crowd, whistle;

    public static void init(AssetManager manager) {
        try {
            intro = manager.openFd("sounds/intro.mp3");
            bounce = manager.openFd("sounds/bounce.mp3");
            crowd = manager.openFd("sounds/crowd.mp3");
            whistle = manager.openFd("sounds/whistle.mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void bounceSound() {
        if (bounce != null) {
            setSound(bounce);
            playSound();
        }
    }

    public static void crowdSound() {
        if (crowd != null) {
            setSound(crowd);
            playSound();
        }
    }

    public static void whistleSound() {
        if (whistle != null) {
            setSound(whistle);
            playSound();
        }
    }

    private static void setSound(AssetFileDescriptor sound) {
        try {
            player = new MediaPlayer();
            player.setDataSource(sound.getFileDescriptor(), sound.getStartOffset(), sound.getLength());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void playSound() {
        try {
            player.prepare();
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
