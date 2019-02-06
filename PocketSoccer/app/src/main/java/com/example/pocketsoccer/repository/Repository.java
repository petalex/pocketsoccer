package com.example.pocketsoccer.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.example.pocketsoccer.database.AppDatabase;
import com.example.pocketsoccer.database.daos.GameDao;
import com.example.pocketsoccer.database.daos.PairDao;
import com.example.pocketsoccer.database.entities.Game;
import com.example.pocketsoccer.database.entities.Pair;

import java.util.List;

public class Repository {
    private PairDao pairDao;

    private GameDao gameDao;

    public Repository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        pairDao = database.pairDao();
        gameDao = database.gameDao();
    }

    public LiveData<List<Pair>> getAllPairs() {
        return pairDao.getAllPairs();
    }

    public LiveData<Pair> getPairById(int id) {
        return pairDao.getPairById(id);
    }

    public void insertPair(final Pair pair) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pairDao.insertPair(pair);
            }
        }).start();
    }

    public void deleteAllPairs(final List<Pair> pairs) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pairDao.deleteAllPairs(pairs);
            }
        }).start();
    }

    public LiveData<List<Game>> getAllGamesOfPair(int pairId) {
        return gameDao.getAllGamesOfPair(pairId);
    }

    public LiveData<Integer> getPlayer1WinsOfPair(int pairId) {
        return gameDao.getPlayer1WinsOfPair(pairId);
    }

    public LiveData<Integer> getPlayer2WinsOfPair(int pairId) {
        return gameDao.getPlayer2WinsOfPair(pairId);
    }

    public void insertGame(final Game game) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                gameDao.insertGame(game);
            }
        }).start();
    }

    public void deleteGames(final List<Game> games) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                gameDao.deleteGames(games);
            }
        }).start();
    }
}
