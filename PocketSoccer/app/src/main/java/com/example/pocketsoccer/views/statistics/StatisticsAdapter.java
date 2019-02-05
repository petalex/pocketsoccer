package com.example.pocketsoccer.views.statistics;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pocketsoccer.R;
import com.example.pocketsoccer.database.entities.Pair;
import com.example.pocketsoccer.views.ScoresActivity;

import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.ViewHolder> {
    private List<Pair> pairs;

    private static Typeface viewHolderFont;

    public StatisticsAdapter(Typeface viewHolderFont) {
        this.viewHolderFont = viewHolderFont;
    }

    public void setPairs(List<Pair> pairs) {
        this.pairs = pairs;
        notifyDataSetChanged();
    }

    public Pair getPairAtPosition(int position) {
        if (pairs != null) {
            return pairs.get(position);
        }
        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.statistics_item, viewGroup, false));
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
        private TextView player1, wins1, semicolon, wins2, player2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            player1 = itemView.findViewById(R.id.player1);
            player1.setTypeface(viewHolderFont);
            wins1 = itemView.findViewById(R.id.wins1);
            wins1.setTypeface(viewHolderFont);
            semicolon = itemView.findViewById(R.id.semicolon);
            semicolon.setTypeface(viewHolderFont);
            wins2 = itemView.findViewById(R.id.wins2);
            wins2.setTypeface(viewHolderFont);
            player2 = itemView.findViewById(R.id.player2);
            player2.setTypeface(viewHolderFont);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //StatisticsActivity.openScores();
                }
            });
        }

        public void setPair(Pair pair) {
            player1.setText(pair.player1);
            // TO-DO wins1
            // TO-DO wins2
            player2.setText(pair.player2);
        }
    }
}
