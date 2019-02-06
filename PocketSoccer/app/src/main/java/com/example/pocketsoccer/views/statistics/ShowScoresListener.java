package com.example.pocketsoccer.views.statistics;

import android.arch.lifecycle.LiveData;

import com.example.pocketsoccer.database.entities.Pair;

public interface ShowScoresListener {
    void showScores(Pair pair);
    LiveData<Integer> getWinsForPlayer1(Pair pair);
    LiveData<Integer> getWinsForPlayer2(Pair pair);
}
