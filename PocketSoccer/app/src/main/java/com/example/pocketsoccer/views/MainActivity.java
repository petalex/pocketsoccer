package com.example.pocketsoccer.views;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.utils.FontManager;
import com.example.pocketsoccer.utils.GameInfoManager;
import com.example.pocketsoccer.utils.ImageManager;
import com.example.pocketsoccer.utils.SettingsManager;
import com.example.pocketsoccer.utils.SoundManager;
import com.example.pocketsoccer.views.game.GameActivity;
import com.example.pocketsoccer.views.settings.SettingsActivity;
import com.example.pocketsoccer.views.statistics.StatisticsActivity;

public class MainActivity extends AppCompatActivity {
    private TextView resumeGame;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        initAppUtils();

        ConstraintLayout mainLayout = findViewById(R.id.main_layout);
        mainLayout.setBackground(ImageManager.getBackground());

        TextView title = findViewById(R.id.title);
        title.setTypeface(FontManager.getTitleFont());

        TextView newGame = findViewById(R.id.new_game);
        newGame.setTypeface(FontManager.getMenuFont());
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent playersIntent = new Intent(v.getContext(), PlayersActivity.class);
                startActivity(playersIntent);
            }
        });

        resumeGame = findViewById(R.id.resume_game);
        resumeGame.setTypeface(FontManager.getMenuFont());

        TextView statistics = findViewById(R.id.statistics);
        statistics.setTypeface(FontManager.getMenuFont());
        statistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statisticsIntent = new Intent(v.getContext(), StatisticsActivity.class);
                startActivity(statisticsIntent);
            }
        });

        TextView settings = findViewById(R.id.settings);
        settings.setTypeface(FontManager.getMenuFont());
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingsIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivity(settingsIntent);
            }
        });

        /*final ImageView soundIcon = findViewById(R.id.sound);
        soundIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sound) {
                    // Stop playing main track
                    player.stop();
                    soundIcon.setImageDrawable(getDrawable(R.drawable.ic_volume_off_white_24dp));
                } else {
                    // Start playing main track
                    player.start();
                    soundIcon.setImageDrawable(getDrawable(R.drawable.ic_volume_up_white_24dp));
                }
                sound = !sound;
            }
        });*/
    }

    private void initAppUtils() {
        FontManager.init(getAssets());
        ImageManager.init(getAssets());
        SoundManager.init(getAssets());
        GameInfoManager.init(this);
        SettingsManager.init(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (GameInfoManager.isSaved()) {
            // Enable Resume button
            resumeGame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gameIntent = new Intent(v.getContext(), GameActivity.class);
                    gameIntent.putExtra("new", false);
                    startActivity(gameIntent);
                }
            });
            resumeGame.setEnabled(true);
            resumeGame.setTextColor(getResources().getColor(android.R.color.white));
        } else {
            // Disable Resume button
            resumeGame.setEnabled(false);
            resumeGame.setTextColor(getResources().getColor(android.R.color.darker_gray));
        }
    }
}