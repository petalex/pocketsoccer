package com.example.pocketsoccer.views.settings;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.pocketsoccer.R;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity implements ChangeFragmentListener {
    private static FragmentManager manager;

    private Fragment currentFragment;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        setContentView(R.layout.activity_settings);

        try {
            Drawable background = Drawable.createFromStream(getAssets().open("backgrounds/main.jpg"), null);
            ConstraintLayout settingsLayout = findViewById(R.id.settings_layout);
            settingsLayout.setBackground(background);
        } catch (IOException e) {
            e.printStackTrace();
        }

        manager = getSupportFragmentManager();
        changeToSettingsFragment();
    }

    public void changeToSettingsFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        currentFragment = new SettingsFragment();
        transaction.replace(R.id.settings_fragment_layout, currentFragment);
        transaction.commit();
    }

    public void changeToFieldFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        currentFragment = new FieldFragment();
        transaction.replace(R.id.settings_fragment_layout, currentFragment);
        transaction.commit();
    }

    public void changeToMatchFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        currentFragment = new MatchFragment();
        transaction.replace(R.id.settings_fragment_layout, currentFragment);
        transaction.commit();
    }

    public void changeToSpeedFragment() {
        FragmentTransaction transaction = manager.beginTransaction();
        currentFragment = new SpeedFragment();
        transaction.replace(R.id.settings_fragment_layout, currentFragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof FieldFragment || currentFragment instanceof MatchFragment || currentFragment instanceof SpeedFragment) {
            changeToSettingsFragment();
        } else {
            super.onBackPressed();
        }
    }
}
