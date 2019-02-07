package com.example.pocketsoccer.views.game;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.views.GameInfo;

import java.io.IOException;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {
    private GameInfo gameInfo;

    private MoveDetector detector;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        setContentView(R.layout.activity_game);

        gameInfo = new GameInfo(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle.getBoolean("new", false)) {
            gameInfo.loadFromBundle(bundle);
        } else {
            gameInfo.loadFromPreferences();
        }

        GameImageView game = findViewById(R.id.game);
        try {
            Drawable background = Drawable.createFromStream(getAssets().open("fields/field" + gameInfo.getField() + ".png"), null);
            game.setBackground(background);
            Drawable team1 = Drawable.createFromStream(getAssets().open("teams/t" + gameInfo.getTeam1() + ".png"), null);
            game.setTeam1(team1);
            Drawable team2 = Drawable.createFromStream(getAssets().open("teams/t" + gameInfo.getTeam2() + ".png"), null);
            game.setTeam2(team2);
            Drawable ball = Drawable.createFromStream(getAssets().open("balls/ball.png"), null);
            game.setBall(ball);
        } catch (IOException e) {
            e.printStackTrace();
        }
        game.setOnTouchListener(this);
        game.setSpeed(gameInfo.getSpeed());
        detector = game.getDetector();
        game.start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                detector.down(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                detector.up(event.getX(), event.getY());
                break;
        }
        return true;
    }
}
