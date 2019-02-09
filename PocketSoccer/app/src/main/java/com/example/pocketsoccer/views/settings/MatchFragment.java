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
import com.example.pocketsoccer.utils.FontManager;
import com.example.pocketsoccer.utils.SettingsManager;

public class MatchFragment extends Fragment {
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

        this.listener = (SettingsActivity) getActivity();

        TextView matchTitle = view.findViewById(R.id.match_title);
        matchTitle.setTypeface(FontManager.getTitleFont());

        TextView timeTitle = view.findViewById(R.id.time_title);
        timeTitle.setTypeface(FontManager.getMenuFont());

        TextView orTitle = view.findViewById(R.id.or_title);
        orTitle.setTypeface(FontManager.getMenuFont());

        TextView goalsTitle = view.findViewById(R.id.goals_title);
        goalsTitle.setTypeface(FontManager.getMenuFont());

        time1 = view.findViewById(R.id.time_1);
        time1.setTypeface(FontManager.getMenuFont());
        time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(time1);
            }
        });

        time3 = view.findViewById(R.id.time_3);
        time3.setTypeface(FontManager.getMenuFont());
        time3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(time3);
            }
        });

        time5 = view.findViewById(R.id.time_5);
        time5.setTypeface(FontManager.getMenuFont());
        time5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(time5);
            }
        });

        goals1 = view.findViewById(R.id.goals_1);
        goals1.setTypeface(FontManager.getMenuFont());
        goals1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(goals1);
            }
        });

        goals3 = view.findViewById(R.id.goals_3);
        goals3.setTypeface(FontManager.getMenuFont());
        goals3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(goals3);
            }
        });

        goals5 = view.findViewById(R.id.goals_5);
        goals5.setTypeface(FontManager.getMenuFont());
        goals5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(goals5);
            }
        });

        goals10 = view.findViewById(R.id.goals_10);
        goals10.setTypeface(FontManager.getMenuFont());
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
        reset.setTypeface(FontManager.getMenuFont());
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionView(defaultOption);
            }
        });

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
        String defaultMatchType = SettingsManager.getDefaultMatchType();
        int defaultMatch = SettingsManager.getDefaultMatch();
        defaultOption = matchToView(defaultMatchType, defaultMatch);
        String matchType = SettingsManager.getMatchType(defaultMatchType);
        int match = SettingsManager.getMatch(defaultMatch);
        selectOptionView(matchToView(matchType, match));
    }

    private TextView matchToView(String type, int value) {
        switch (type) {
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
            case "goals": {
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

    private String viewToMatchType() {
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

    private int viewToMatch() {
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
        SettingsManager.setMatchType(viewToMatchType());
        SettingsManager.setMatch(viewToMatch());
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