package com.example.pocketsoccer.views.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.utils.FontManager;
import com.example.pocketsoccer.utils.SettingsManager;

import org.jetbrains.annotations.NotNull;

public class SettingsFragment extends Fragment {
    private ChangeFragmentListener listener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        this.listener = (SettingsActivity) getActivity();

        TextView settingsTitle = view.findViewById(R.id.settings_title);
        settingsTitle.setTypeface(FontManager.getTitleFont());

        TextView field = view.findViewById(R.id.field);
        field.setTypeface(FontManager.getMenuFont());
        field.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeToFieldFragment();
            }
        });

        TextView match = view.findViewById(R.id.match);
        match.setTypeface(FontManager.getMenuFont());
        match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeToMatchFragment();
            }
        });

        TextView speed = view.findViewById(R.id.speed);
        speed.setTypeface(FontManager.getMenuFont());
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
        reset.setTypeface(FontManager.getMenuFont());
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsManager.resetAll();
                Toast.makeText(getContext(), "Settings have been reset!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}