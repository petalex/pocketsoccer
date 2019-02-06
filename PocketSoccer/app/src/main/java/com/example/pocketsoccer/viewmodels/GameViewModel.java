package com.example.pocketsoccer.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.pocketsoccer.database.entities.Game;
import com.example.pocketsoccer.repository.Repository;

import java.util.List;

public class GameViewModel extends AndroidViewModel {
    private Repository repository;

    public GameViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<List<Game>> getAllGamesOfPair(int pairId) {
        return repository.getAllGamesOfPair(pairId);
    }

    public LiveData<Integer> getPlayer1WinsOfPair(int pairId) {
        return repository.getPlayer1WinsOfPair(pairId);
    }

    public LiveData<Integer> getPlayer2WinsOfPair(int pairId) {
        return repository.getPlayer2WinsOfPair(pairId);
    }

    public void insertGame(final Game game) {
        repository.insertGame(game);
    }

    public void deleteGames(final List<Game> games) {
        repository.deleteGames(games);
    }
}
