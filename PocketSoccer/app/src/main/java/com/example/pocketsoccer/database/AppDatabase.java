package com.example.pocketsoccer.database;


import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.pocketsoccer.database.daos.GameDao;
import com.example.pocketsoccer.database.daos.PairDao;
import com.example.pocketsoccer.database.entities.Game;
import com.example.pocketsoccer.database.entities.Pair;

@android.arch.persistence.room.Database(entities = {Pair.class, Game.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PairDao pairDao();

    public abstract GameDao gameDao();

    private static AppDatabase instance;

    private static AppDatabase.Callback callback = new AppDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            new PopulateDatabase(instance).execute();
        }
    };

    private static class PopulateDatabase extends AsyncTask<Void, Void, Void> {
        private PairDao pairDao;

        private GameDao gameDao;

        public PopulateDatabase(AppDatabase database) {
            pairDao = database.pairDao();
            gameDao = database.gameDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Pair pair = new Pair();
            pair.player1 = "petalex";
            pair.player2 = "djekatore";
            pairDao.insert(pair);
            Game game = new Game();
            game.pairId = 1;
            game.score1 = 1;
            game.score2 = 1;
            gameDao.insert(game);
            return null;
        }
    }

    public static AppDatabase getInstance(Application application) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                instance = Room.databaseBuilder(application, AppDatabase.class, "pocketsoccer").
                        fallbackToDestructiveMigration().
                        addCallback(callback).
                        build();
            }
        }
        return instance;
    }
}
