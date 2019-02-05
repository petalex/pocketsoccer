package com.example.pocketsoccer.views.settings;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.views.SettingsPreferences;

public class SettingsFragment extends Fragment implements ChangingFragment {
    private SettingsPreferences preferences;

    private ChangeFragmentListener listener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        setChangeFragmentListener((SettingsActivity) getActivity());

        Typeface titleFont = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Chunkfive.otf");
        Typeface menuFont = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Sanson.otf");

        TextView settingsTitle = view.findViewById(R.id.settings_title);
        settingsTitle.setTypeface(titleFont);

        TextView field = view.findViewById(R.id.field);
        field.setTypeface(menuFont);
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeToFieldFragment();
            }
        });

        TextView match = view.findViewById(R.id.match);
        match.setTypeface(menuFont);
        match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeToMatchFragment();
            }
        });

        TextView speed = view.findViewById(R.id.speed);
        speed.setTypeface(menuFont);
        speed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeToSpeedFragment();
            }
        });

        ImageView back = view.findViewById(R.id.settings_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        TextView reset = view.findViewById(R.id.reset_settings);
        reset.setTypeface(menuFont);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAll();
                Toast.makeText(getContext(), "Settings have been reset!", Toast.LENGTH_SHORT).show();
            }
        });

        preferences = SettingsPreferences.getInstance(getContext());

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void resetAll() {
        // Reset field
        int defaultField = preferences.getIntSetting("defaultField", 0);
        if (defaultField == 0) { // Only first time
            preferences.setIntSetting("defaultField", 1);
            defaultField = preferences.getIntSetting("defaultField", 0);
        }
        preferences.setIntSetting("field", defaultField);
        // Reset match
        int defaultMatch = preferences.getIntSetting("defaultMatch", 0);
        if (defaultMatch == 0) { // Only first time
            preferences.setIntSetting("defaultMatch", 3);
            defaultMatch = preferences.getIntSetting("defaultMatch", 0);
        }

        preferences.setStringSetting("match", "goals");
        preferences.setIntSetting("goals", defaultMatch);
        // Reset speed
        int defaultSpeed = preferences.getIntSetting("defaultSpeed", 0);
        if (defaultSpeed == 0) { // Only first time
            preferences.setIntSetting("defaultSpeed", 4);
            defaultSpeed = preferences.getIntSetting("defaultSpeed", 0);
        }
        preferences.setIntSetting("speed", defaultSpeed);
    }

    @Override
    public void setChangeFragmentListener(ChangeFragmentListener listener) {
        this.listener = listener;
    }
}
