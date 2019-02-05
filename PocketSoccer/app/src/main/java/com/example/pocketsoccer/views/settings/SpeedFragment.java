package com.example.pocketsoccer.views.settings;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.views.SettingsPreferences;

public class SpeedFragment extends Fragment implements ChangingFragment {
    private SettingsPreferences preferences;

    private ChangeFragmentListener listener;

    private TextView value;

    private SeekBar slider;

    private int speed, defaultSpeed;

    public SpeedFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speed, container, false);

        setChangeFragmentListener((SettingsActivity) getActivity());

        Typeface titleFont = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Chunkfive.otf");
        Typeface menuFont = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Sanson.otf");

        TextView speedTitle = view.findViewById(R.id.speed_title);
        speedTitle.setTypeface(titleFont);

        value = view.findViewById(R.id.speed_value);
        value.setTypeface(menuFont);

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
        reset.setTypeface(menuFont);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSpeed(defaultSpeed, true);
            }
        });

        preferences = SettingsPreferences.getInstance(getContext());

        return view;
    }

    private void setSpeed(int s, boolean andSlider) {
        speed = s;
        value.setText("" + speed);
        if (andSlider) {
            slider.setProgress(speed);
        }
    }

    private void loadSettings() {
        defaultSpeed = preferences.getIntSetting("defaultSpeed", 0);
        if (defaultSpeed == 0) { // Only first time
            preferences.setIntSetting("defaultSpeed", 4);
            defaultSpeed = preferences.getIntSetting("defaultSpeed", 0);
        }
        setSpeed(preferences.getIntSetting("speed", defaultSpeed), true);
    }

    private void saveSettings() {
        preferences.setIntSetting("speed", speed);
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

    @Override
    public void setChangeFragmentListener(ChangeFragmentListener listener) {
        this.listener = listener;
    }
}
