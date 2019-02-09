package com.example.pocketsoccer.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class ViewModelFactory<T extends AndroidViewModel> implements ViewModelProvider.Factory {
    private ViewModelProvider provider;

    public ViewModelFactory(ViewModelProvider provider) {
        this.provider = provider;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) provider.get(modelClass);
    }
}