package com.example.pocketsoccer.views.scores;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.database.entities.Game;
import com.example.pocketsoccer.utils.FontManager;

import java.util.List;

public class ScoresAdapter extends RecyclerView.Adapter<ScoresAdapter.ViewHolder> {
    private List<Game> games;

    public void setGames(List<Game> games) {
        this.games = games;
        notifyDataSetChanged();
    }

    public List<Game> getGames() {
        return games;
    }

    @NonNull
    @Override
    public ScoresAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.scores_item, viewGroup, false));
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
            timeMinutes.setTypeface(FontManager.getMenuFont());
            timeSemicolon = itemView.findViewById(R.id.time_semicolon);
            timeSemicolon.setTypeface(FontManager.getMenuFont());
            timeSeconds = itemView.findViewById(R.id.time_seconds);
            timeSeconds.setTypeface(FontManager.getMenuFont());
            score1 = itemView.findViewById(R.id.score1);
            score1.setTypeface(FontManager.getMenuFont());
            scoreSemicolon = itemView.findViewById(R.id.score_semicolon);
            scoreSemicolon.setTypeface(FontManager.getMenuFont());
            score2 = itemView.findViewById(R.id.score2);
            score2.setTypeface(FontManager.getMenuFont());
        }

        @SuppressLint("SetTextI18n")
        public void setGame(Game game) {
            timeMinutes.setText(timeToString(game.time / 60));
            timeSeconds.setText(timeToString(game.time % 60));
            score1.setText("" + game.score1);
            score2.setText("" + game.score2);
        }

        private String timeToString(int time) {
            if (time < 10) {
                return "0" + time;
            }
            return "" + time;
        }
    }
}