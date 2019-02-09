package com.example.pocketsoccer.views.scores;

import android.annotation.SuppressLint;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import com.example.pocketsoccer.database.entities.Game;
import com.example.pocketsoccer.database.entities.Pair;
import com.example.pocketsoccer.utils.FontManager;
import com.example.pocketsoccer.utils.ImageManager;
import com.example.pocketsoccer.viewmodels.GameViewModel;
import com.example.pocketsoccer.viewmodels.PairViewModel;
import com.example.pocketsoccer.viewmodels.ViewModelFactory;
import com.example.pocketsoccer.views.MainActivity;

import java.util.List;

public class ScoresActivity extends AppCompatActivity {
    private TextView scoresTitle;

    private ScoresAdapter scoresAdapter;

    private GameViewModel gameViewModel;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_scores);

        ConstraintLayout scoresLayout = findViewById(R.id.scores_layout);
        scoresLayout.setBackground(ImageManager.getBackground());

        scoresTitle = findViewById(R.id.scores_title);
        scoresTitle.setTypeface(FontManager.getTitleFont());

        TextView time_title = findViewById(R.id.time_title);
        time_title.setTypeface(FontManager.getMenuFont());

        TextView scoreTitle = findViewById(R.id.score_title);
        scoreTitle.setTypeface(FontManager.getMenuFont());

        ImageView back = findViewById(R.id.scores_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView clear = findViewById(R.id.clear_scores);
        clear.setTypeface(FontManager.getMenuFont());
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameViewModel.deleteGames(scoresAdapter.getGames());
                Toast.makeText(v.getContext(), "Scores have been cleared!", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView scoresList = findViewById(R.id.scores_list);
        scoresList.setLayoutManager(new LinearLayoutManager(this));
        scoresAdapter = new ScoresAdapter();
        scoresList.setAdapter(scoresAdapter);

        ViewModelFactory<AndroidViewModel> factory = new ViewModelFactory<>(ViewModelProviders.of(this));
        PairViewModel pairViewModel = factory.create(PairViewModel.class);
        int pairId = getIntent().getIntExtra("id", 0);
        pairViewModel.getPairById(pairId).observe(this, new Observer<Pair>() {
            @Override
            public void onChanged(@Nullable Pair pair) {
                String title = "";
                if (pair != null) {
                    title = pair.player1 + " - " + pair.player2;
                }
                scoresTitle.setText(title);

            }
        });
        gameViewModel = factory.create(GameViewModel.class);
        gameViewModel.getAllGamesOfPair(pairId).observe(this, new Observer<List<Game>>() {
            @Override
            public void onChanged(@Nullable List<Game> games) {
                scoresAdapter.setGames(games);
            }
        });
    }
}