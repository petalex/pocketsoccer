package com.example.pocketsoccer.views.statistics;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.database.entities.Pair;
import com.example.pocketsoccer.viewmodels.PairViewModel;
import com.example.pocketsoccer.viewmodels.ViewModelFactory;
import com.example.pocketsoccer.views.ScoresActivity;

import java.io.IOException;
import java.util.List;

public class StatisticsActivity extends AppCompatActivity {
    private RecyclerView statisticsList;

    private StatisticsAdapter statisticsAdapter;

    private PairViewModel model;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        setContentView(R.layout.activity_statistics);

        try {
            Drawable background = Drawable.createFromStream(getAssets().open("backgrounds/main.jpg"), null);
            ConstraintLayout statisticsLayout = findViewById(R.id.statistics_layout);
            statisticsLayout.setBackground(background);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/Chunkfive.otf");
        Typeface menuFont = Typeface.createFromAsset(getAssets(), "fonts/Sanson.otf");

        TextView statisticsTitle = findViewById(R.id.statistics_title);
        statisticsTitle.setTypeface(titleFont);

        ImageView back = findViewById(R.id.statistics_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView clear = findViewById(R.id.clear_statistics);
        clear.setTypeface(menuFont);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.deleteAllPairs();
                Toast.makeText(v.getContext(), "Statistics have been cleared!", Toast.LENGTH_SHORT).show();
            }
        });

        statisticsList = findViewById(R.id.statistics_list);
        statisticsList.setLayoutManager(new LinearLayoutManager(this));
        statisticsAdapter = new StatisticsAdapter(menuFont);
        statisticsList.setAdapter(statisticsAdapter);

        ViewModelFactory<PairViewModel> factory = new ViewModelFactory<>(ViewModelProviders.of(this));
        model = factory.create(PairViewModel.class);
        model.getAllPairs().observe(this, new Observer<List<Pair>>() {
            @Override
            public void onChanged(@Nullable List<Pair> pairs) {
                statisticsAdapter.setPairs(pairs);
            }
        });
    }
}
