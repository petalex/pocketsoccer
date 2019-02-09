package com.example.pocketsoccer.views.game;

import android.annotation.SuppressLint;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.database.entities.Game;
import com.example.pocketsoccer.database.entities.Pair;
import com.example.pocketsoccer.utils.GameInfoManager;
import com.example.pocketsoccer.utils.ImageManager;
import com.example.pocketsoccer.viewmodels.GameViewModel;
import com.example.pocketsoccer.viewmodels.PairViewModel;
import com.example.pocketsoccer.viewmodels.ViewModelFactory;
import com.example.pocketsoccer.views.scores.ScoresActivity;

import java.util.List;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener {
    private GameImageView game;

    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_game);

        game = findViewById(R.id.game);

        Bundle bundle = getIntent().getExtras();
        boolean newGame = bundle != null && bundle.getBoolean("new", false);
        if (newGame) {
            GameInfoManager.loadFromBundle(bundle);
        } else {
            GameInfoManager.loadFromPreferences(game.getData());
        }

        game.initDrawables();
        game.setBackground(ImageManager.getField(GameInfoManager.getField()));
        game.setOnGameOverListener(this);
        game.setOnTouchListener(this);
        game.start(newGame);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        game.release();
    }

    @Override
    public void onBackPressed() {
        game.freeze();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save game").
                setMessage("Do you want to save current game before quiting?").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        GameInfoManager.saveToPreferences(game.getData());
                        finish();
                    }
                }).
                setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nothing to save
                        finish();
                    }
                }).
                setNeutralButton("Back to game", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nothing to do
                        game.unfreeze();
                    }
                }).
                setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // Nothing to do
                        game.unfreeze();
                    }
                }).
                create().show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                game.getDetector().down(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                game.getDetector().up(event.getX(), event.getY());
                break;
        }
        return true;
    }

    public void gameOver() {
        ViewModelFactory<AndroidViewModel> factory = new ViewModelFactory<>(ViewModelProviders.of(this));
        final PairViewModel pairViewModel = factory.create(PairViewModel.class);
        final GameViewModel gameViewModel = factory.create(GameViewModel.class);

        pairViewModel.getPairByPlayers(GameInfoManager.getPlayer1(), GameInfoManager.getPlayer2()).observe(this, new Observer<Pair>() {
            @Override
            public void onChanged(@Nullable Pair pair) {
                if (pair != null) {
                    Game game = new Game();
                    game.pairId = pair.id;
                    game.score1 = GameInfoManager.getScore1();
                    game.score2 = GameInfoManager.getScore2();
                    game.time = GameInfoManager.getTime();
                    gameViewModel.insertGame(game);

                    changeToScores(pair);
                } else {
                    Pair p = new Pair();
                    p.player1 = GameInfoManager.getPlayer1();
                    p.player2 = GameInfoManager.getPlayer2();
                    pairViewModel.insertPair(p);
                }
            }
        });

    }

    private void changeToScores(Pair pair) {
        Intent scoresIntent = new Intent(this, ScoresActivity.class);
        scoresIntent.putExtra("id", pair.id);
        scoresIntent.putExtra("fromGame", true);
        startActivity(scoresIntent);
        finish();
    }
}