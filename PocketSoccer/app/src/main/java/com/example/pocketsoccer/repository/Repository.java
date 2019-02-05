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

    private LiveData<List<Game>> games;

    public Repository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        pairDao = database.pairDao();
        gameDao = database.gameDao();
        pairs = pairDao.getAll();
        games = gameDao.getAll();
    }

    public LiveData<List<Pair>> getAllPairs() {
        return pairs;
    }

    public void deleteAllPairs() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pairDao.deleteAll(pairs.getValue());
            }
        }).start();
    }

    public LiveData<List<Game>> getAllGames() {
        return games;
    }
}
