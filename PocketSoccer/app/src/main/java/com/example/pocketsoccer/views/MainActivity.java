package com.example.pocketsoccer.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.views.settings.SettingsActivity;
import com.example.pocketsoccer.views.statistics.StatisticsActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private boolean sound = true;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        setContentView(R.layout.activity_main);

        try {
            Drawable background = Drawable.createFromStream(getAssets().open("backgrounds/main.jpg"), null);
            ConstraintLayout mainLayout = findViewById(R.id.main_layout);
            mainLayout.setBackground(background);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/Chunkfive.otf");
        Typeface menuFont = Typeface.createFromAsset(getAssets(), "fonts/Sanson.otf");

        TextView title = findViewById(R.id.title);
        title.setTypeface(titleFont);

        TextView newGame = findViewById(R.id.new_game);
        newGame.setTypeface(menuFont);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playersIntent = new Intent(v.getContext(), PlayersActivity.class);
                startActivity(playersIntent);
            }
        });

        TextView resumeGame = findViewById(R.id.resume_game);
        resumeGame.setTypeface(menuFont);
        resumeGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Shit", Toast.LENGTH_SHORT).show();
            }
        });
        if (true) {
            resumeGame.setEnabled(false);
            resumeGame.setTextColor(getResources().getColor(R.color.gray));
        }

        TextView statistics = findViewById(R.id.statistics);
        statistics.setTypeface(menuFont);
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statisticsIntent = new Intent(v.getContext(), StatisticsActivity.class);
                startActivity(statisticsIntent);
            }
        });

        TextView settings = findViewById(R.id.settings);
        settings.setTypeface(menuFont);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        final ImageView soundIcon = findViewById(R.id.sound);
        soundIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound) {
                    soundIcon.setImageDrawable(getDrawable(R.drawable.ic_volume_off_white_24dp));
                } else {
                    soundIcon.setImageDrawable(getDrawable(R.drawable.ic_volume_up_white_24dp));
                }
                sound = !sound;
            }
        });
    }
}
