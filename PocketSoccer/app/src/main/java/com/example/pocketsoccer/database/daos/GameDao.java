package com.example.pocketsoccer.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.pocketsoccer.database.entities.Game;

import java.util.List;

@Dao
public interface GameDao {
    @Query("SELECT * FROM game WHERE pairId=:pairId")
    LiveData<List<Game>> getAllGamesOfPair(int pairId);

    @Query("SELECT COUNT(*) FROM game WHERE pairId=:pairId AND score1>score2")
    LiveData<Integer> getPlayer1WinsOfPair(int pairId);

    @Query("SELECT COUNT(*) FROM game WHERE pairId=:pairId AND score1<score2")
    LiveData<Integer> getPlayer2WinsOfPair(int pairId);

    @Insert
    void insertGame(Game game);

    @Delete
    void deleteGames(List<Game> games);
}
