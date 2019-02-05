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

    public LiveData<List<Game>> getAllGames() {
        return repository.getAllGames();
    }
}
