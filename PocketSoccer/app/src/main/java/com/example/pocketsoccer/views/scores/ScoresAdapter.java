package com.example.pocketsoccer.views.scores;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.database.entities.Game;

import java.util.List;

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> {
    private List<Game> games;

    private static Typeface viewHolderFont;

    public ScoresAdapter(ScoresActivity activity) {
        this.viewHolderFont = Typeface.createFromAsset(activity.getAssets(), "fonts/Sanson.otf");
    }

    public void setGames(List<Game> games) {
        this.games = games;
        notifyDataSetChanged();
    }

    public List<Game> getGames() {
        return games;
    }

    public Game getGameAtPosition(int position) {
        if (games != null) {
            return games.get(position);
        }
        return null;
    }

    @NonNull
    @Override
    public ScoresAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ScoresAdapter.ViewHolder holder = new ScoresAdapter.ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scores_item, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoresAdapter.ViewHolder viewHolder, int i) {
        if (games != null) {
            viewHolder.setGame(games.get(i));
        }
    }

    @Override
    public int getItemCount() {
        if (games != null) {
            return games.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView timeMinutes, timeSemicolon, timeSeconds, score1, scoreSemicolon, score2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeMinutes = itemView.findViewById(R.id.time_minutes);
            timeMinutes.setTypeface(viewHolderFont);
            timeSemicolon = itemView.findViewById(R.id.time_semicolon);
            timeSemicolon.setTypeface(viewHolderFont);
            timeSeconds = itemView.findViewById(R.id.time_seconds);
            timeSeconds.setTypeface(viewHolderFont);
            score1 = itemView.findViewById(R.id.score1);
            score1.setTypeface(viewHolderFont);
            scoreSemicolon = itemView.findViewById(R.id.score_semicolon);
            scoreSemicolon.setTypeface(viewHolderFont);
            score2 = itemView.findViewById(R.id.score2);
            score2.setTypeface(viewHolderFont);
        }

        @SuppressLint("SetTextI18n")
        public void setGame(Game game) {
            timeMinutes.setText("" + game.time / 60);
            timeSeconds.setText("" + game.time % 60);
            score1.setText("" + game.score1);
            score2.setText("" + game.score2);
        }
    }
}
