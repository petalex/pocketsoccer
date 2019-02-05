package com.example.pocketsoccer.database.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.pocketsoccer.database.entities.Pair;

import java.util.List;

@Dao
public interface PairDao {
    @Query("SELECT * FROM pair")
    LiveData<List<Pair>> getAll();

    @Insert
    void insert(Pair pair);

    @Delete
    void delete(Pair pair);

    @Delete
    void deleteAll(List<Pair> pairs);
}
