package com.example.pocketsoccer.views.statistics;

import android.arch.lifecycle.Observer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.database.entities.Pair;
import com.example.pocketsoccer.utils.FontManager;

import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {
    private ShowScoresListener listener;

    private List<Pair> pairs;

    public StatisticsAdapter(StatisticsActivity listener) {
        this.listener = listener;
    }

    public void setPairs(List<Pair> pairs) {
        this.pairs = pairs;
        notifyDataSetChanged();
    }

    public List<Pair> getPairs() {
        return pairs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.statistics_item, viewGroup, false));
        holder.setShowScoresListener(listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (pairs != null) {
            viewHolder.setPair(pairs.get(i));
        }
    }

    @Override
    public int getItemCount() {
        if (pairs != null) {
            return pairs.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView view;

        private TextView player1, wins1, semicolon, wins2, player2;

        private ShowScoresListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = (CardView) itemView;
            player1 = itemView.findViewById(R.id.player1);
            player1.setTypeface(FontManager.getMenuFont());
            wins1 = itemView.findViewById(R.id.wins1);
            wins1.setTypeface(FontManager.getMenuFont());
            semicolon = itemView.findViewById(R.id.semicolon);
            semicolon.setTypeface(FontManager.getMenuFont());
            wins2 = itemView.findViewById(R.id.wins2);
            wins2.setTypeface(FontManager.getMenuFont());
            player2 = itemView.findViewById(R.id.player2);
            player2.setTypeface(FontManager.getMenuFont());
        }

        public void setShowScoresListener(ShowScoresListener listener) {
            this.listener = listener;
        }

        public void setPair(final Pair pair) {
            player1.setText(pair.player1);
            listener.getWinsForPlayer1(pair).observe((StatisticsActivity) listener, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
                    wins1.setText(integer.toString());
                }
            });
            listener.getWinsForPlayer2(pair).observe((StatisticsActivity) listener, new Observer<Integer>() {
                @Override
                public void onChanged(@Nullable Integer integer) {
                    wins2.setText(integer.toString());
                }
            });
            player2.setText(pair.player2);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.showScores(pair);
                }
            });
        }
    }
}