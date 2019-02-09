package com.example.pocketsoccer.views.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.utils.FontManager;
import com.example.pocketsoccer.utils.SettingsManager;

import org.jetbrains.annotations.NotNull;

public class SpeedFragment extends Fragment {
     private ChangeFragmentListener listener;

    private TextView value;

    private SeekBar slider;

    private int speed, defaultSpeed;

    public SpeedFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speed, container, false);

        this.listener = (SettingsActivity) getActivity();

        TextView speedTitle = view.findViewById(R.id.speed_title);
        speedTitle.setTypeface(FontManager.getTitleFont());

        value = view.findViewById(R.id.speed_value);
        value.setTypeface(FontManager.getMenuFont());

        slider = view.findViewById(R.id.speed_slider);
        slider.setMin(1);
        slider.setMax(7);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setSpeed(progress, false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ImageView back = view.findViewById(R.id.speed_settings_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeToSettingsFragment();
            }
        });

        TextView reset = view.findViewById(R.id.reset_speed);
        reset.setTypeface(FontManager.getMenuFont());
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSpeed(defaultSpeed, true);
            }
        });

        return view;
    }

    private void setSpeed(int s, boolean andSlider) {
        speed = s;
        String text = "" + speed;
        value.setText(text);
        if (andSlider) {
            slider.setProgress(speed);
        }
    }

    private void loadSettings() {
        defaultSpeed = SettingsManager.getDefaultSpeed();
        setSpeed(SettingsManager.getSpeed(defaultSpeed), true);
    }

    private void saveSettings() {
        SettingsManager.setSpeed(speed);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSettings();
    }

    @Override
    public void onPause() {
        super.onPause();
        saveSettings();
    }
}