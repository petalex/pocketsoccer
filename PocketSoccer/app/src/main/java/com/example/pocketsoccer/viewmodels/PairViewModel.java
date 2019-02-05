package com.example.pocketsoccer.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.pocketsoccer.database.entities.Game;
import com.example.pocketsoccer.database.entities.Pair;
import com.example.pocketsoccer.repository.Repository;

import java.util.List;

public class PairViewModel extends AndroidViewModel {
    private Repository repository;

    public PairViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public LiveData<List<Pair>> getAllPairs() {
        return repository.getAllPairs();
    }

    public void deleteAllPairs() {
        repository.deleteAllPairs();
    }
}
