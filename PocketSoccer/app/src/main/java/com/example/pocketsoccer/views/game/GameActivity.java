package com.example.pocketsoccer.views.game;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.views.GameInfo;

import java.io.IOException;

public class GameActivity extends AppCompatActivity {
    private GameInfo gameInfo;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
