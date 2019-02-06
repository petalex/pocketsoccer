package com.example.pocketsoccer.views.settings;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.views.SettingsPreferences;

public class MatchFragment extends Fragment implements ChangingFragment {
    private SettingsPreferences preferences;

    private ChangeFragmentListener listener;

    private TextView time1, time3, time5, goals1, goals3, goals5, goals10;

    private TextView selectedOption, defaultOption;

    public MatchFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        setChangeFragmentListener((SettingsActivity) getActivity());

        Typeface titleFont = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Chunkfive.otf");
        Typeface menuFont = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Sanson.otf");

        TextView matchTitle = view.findViewById(R.id.match_title);
        matchTitle.setTypeface(titleFont);

        TextView timeTitle = view.findViewById(R.id.time_title);
        timeTitle.setTypeface(menuFont);

        TextView orTitle = view.findViewById(R.id.or_title);
        orTitle.setTypeface(menuFont);

        TextView goalsTitle = view.findViewById(R.id.goals_title);
        goalsTitle.setTypeface(menuFont);

        time1 = view.findViewById(R.id.time_1);
        time1.setTypeface(menuFont);
        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(time1);
            }
        });

        time3 = view.findViewById(R.id.time_3);
        time3.setTypeface(menuFont);
        time3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(time3);
            }
        });

        time5 = view.findViewById(R.id.time_5);
        time5.setTypeface(menuFont);
        time5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(time5);
            }
        });

        goals1 = view.findViewById(R.id.goals_1);
        goals1.setTypeface(menuFont);
        goals1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(goals1);
            }
        });

        goals3 = view.findViewById(R.id.goals_3);
        goals3.setTypeface(menuFont);
        goals3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(goals3);
            }
        });

        goals5 = view.findViewById(R.id.goals_5);
        goals5.setTypeface(menuFont);
        goals5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(goals5);
            }
        });

        goals10 = view.findViewById(R.id.goals_10);
        goals10.setTypeface(menuFont);
        goals10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(goals10);
            }
        });

        ImageView back = view.findViewById(R.id.match_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeToSettingsFragment();
            }
        });

        TextView reset = view.findViewById(R.id.reset_match);
        reset.setTypeface(menuFont);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(defaultOption);
            }
        });

        preferences = SettingsPreferences.getInstance(getContext());

        return view;
    }

    private void selectOptionView(TextView option) {
        if (selectedOption != null) {
            selectedOption.setTypeface(selectedOption.getTypeface(), Typeface.NORMAL);
            selectedOption.setTextSize(TypedValue.COMPLEX_UNIT_PX, option.getTextSize());
            selectedOption.setPadding(0, 0, 0, 0);
        }

        option.setTypeface(option.getTypeface(), Typeface.BOLD);
        option.setTextSize(TypedValue.COMPLEX_UNIT_PX, option.getTextSize() * 1.2f);
        option.setPadding(0, -12, 0, 0);

        selectedOption = option;
    }

    private void loadSettings() {
        int defaultMatch = preferences.getIntSetting("defaultMatch", 0);
        if (defaultMatch == 0) { // Only first time
            preferences.setIntSetting("defaultMatch", 3);
            defaultMatch = preferences.getIntSetting("defaultMatch", 0);
        }
        defaultOption = optionToView("defaultMatch", defaultMatch);

        String matchText = preferences.getStringSetting("match", "defaultMatch");
        int matchValue = preferences.getIntSetting(matchText, 0);
        selectOptionView(optionToView(matchText, matchValue));
    }

    private TextView optionToView(String option, int value) {
        switch (option) {
            case "time": {
                switch (value) {
                    case 1:
                        return time1;
                    case 3:
                        return time3;
                    case 5:
                        return time5;
                }
            }
            case "goals":
            case "defaultMatch": {
                switch (value) {
                    case 1:
                        return goals1;
                    case 3:
                        return goals3;
                    case 5:
                        return goals5;
                    case 10:
                        return goals10;
                }
            }
        }
        return null;
    }

    private String optionToText() {
        switch (selectedOption.getId()) {
            case R.id.time_1:
            case R.id.time_3:
            case R.id.time_5:
                return "time";
            case R.id.goals_1:
            case R.id.goals_3:
            case R.id.goals_5:
            case R.id.goals_10:
                return "goals";
        }
        return null;
    }

    private int optionToValue() {
        switch (selectedOption.getId()) {
            case R.id.time_1:
            case R.id.goals_1:
                return 1;
            case R.id.time_3:
            case R.id.goals_3:
                return 3;
            case R.id.time_5:
            case R.id.goals_5:
                return 5;
            case R.id.goals_10:
                return 10;
        }
        return 0;
    }

    private void saveSettings() {
        preferences.setStringSetting("match", optionToText());
        preferences.setIntSetting(optionToText(), optionToValue());
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
