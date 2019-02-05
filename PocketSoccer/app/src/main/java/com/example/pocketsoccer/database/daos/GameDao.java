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
    @Query("SELECT * FROM game")
    LiveData<List<Game>> getAll();

    @Insert
    void insert(Game game);

    @Delete
    void delete(Game game);
}
