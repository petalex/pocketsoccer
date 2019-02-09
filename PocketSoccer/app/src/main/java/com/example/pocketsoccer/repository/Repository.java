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

    private LiveData<List<Pair>> pairs;

    public Repository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        pairDao = database.pairDao();
        gameDao = database.gameDao();
        pairs =  pairDao.getAllPairs();
    }

    public LiveData<List<Pair>> getAllPairs() {
        return pairs;
    }

    public LiveData<Pair> getPairById(int id) {
        return pairDao.getPairById(id);
    }

    public LiveData<Pair> getPairByPlayers(String player1, String player2) {
        return pairDao.getPairByPlayers(player1, player2);
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
