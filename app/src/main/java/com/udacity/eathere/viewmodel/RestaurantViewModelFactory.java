package com.udacity.eathere.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.udacity.eathere.viewmodel.RestaurantViewModel;

public class RestaurantViewModelFactory implements ViewModelProvider.Factory {
    private Application application;


    public RestaurantViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RestaurantViewModel(application);
    }
}
