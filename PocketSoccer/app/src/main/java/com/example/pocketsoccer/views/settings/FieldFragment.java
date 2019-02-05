package com.example.pocketsoccer.views.settings;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.views.SettingsPreferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FieldFragment extends Fragment implements ChangingFragment {
    private SettingsPreferences preferences;

    private ChangeFragmentListener listener;

    private ViewPager fieldPager;

    private ViewPagerAdapter adapter;

    private int field, defaultField;

    public FieldFragment() {
        // Required empty public constructor
    }

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_field, container, false);

        setChangeFragmentListener((SettingsActivity) getActivity());

        Typeface titleFont = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Chunkfive.otf");
        Typeface menuFont = Typeface.createFromAsset(view.getContext().getAssets(), "fonts/Sanson.otf");

        TextView fieldTitle = view.findViewById(R.id.field_title);
        fieldTitle.setTypeface(titleFont);

        fieldPager = view.findViewById(R.id.field_pager);
        adapter = new ViewPagerAdapter();
        fieldPager.setAdapter(adapter);
        fieldPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                int count = adapter.getCount();
                if (i == 0) {
                    setField(count - 2);
                } else if (i == count - 1) {
                    setField(1);
                } else {
                    setField(i);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        ImageView back = view.findViewById(R.id.field_settings_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.changeToSettingsFragment();
            }
        });

        ImageView prev = view.findViewById(R.id.field_prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = fieldPager.getCurrentItem();
                int count = adapter.getCount();
                if (position == 0) {
                    setField(count - 2);
                } else {
                    setField(position - 1);
                }
            }
        });

        ImageView next = view.findViewById(R.id.field_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = fieldPager.getCurrentItem();
                int count = adapter.getCount();
                if (position == count - 1) {
                    setField(0);
                } else {
                    setField(position + 1);
                }
            }
        });

        TextView reset = view.findViewById(R.id.reset_field);
        reset.setTypeface(menuFont);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setField(defaultField);
            }
        });

        preferences = SettingsPreferences.getInstance(getContext());

        return view;
    }

    private void setField(int f) {
        field = f;
        fieldPager.setCurrentItem(f);
    }

    private void loadSettings() {
        defaultField = preferences.getIntSetting("defaultField", 0);
        if (defaultField == 0) { // Only first time
            preferences.setIntSetting("defaultField", 1);
            defaultField = preferences.getIntSetting("defaultField", 0);
        }
        setField(preferences.getIntSetting("field", defaultField));
    }

    private void saveSettings() {
        preferences.setIntSetting("field", field);
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

    private class ViewPagerAdapter extends PagerAdapter {

        private List<Drawable> fields;
        private static final int FIELD_COUNT = 4;

        public ViewPagerAdapter() {
            fields = new ArrayList<>();
            fields.add(null);
            for (int i = 1; i <= FIELD_COUNT; ++i) {
                try {
                    fields.add(Drawable.createFromStream(getActivity().getAssets().open("fields/field" + i + ".png"), null));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            fields.add(null);
        }

        @Override
        public int getCount() {
            return fields.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.view_pager_item, null);
            ImageView image = view.findViewById(R.id.field_image);
            image.setImageDrawable(fields.get(position));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.changeToSettingsFragment();
                }
            });
            ((ViewPager) container).addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ((ViewPager) container).removeView((View) object);
        }
    }
}
