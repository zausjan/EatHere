package com.udacity.eathere.viewmodel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.udacity.eathere.viewmodel.MenuViewModel;


public class MenuViewModelFactory implements ViewModelProvider.Factory{
    private Application application;
    private String id;

    public MenuViewModelFactory(Application application, String id){
        this.application = application;
        this.id = id;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MenuViewModel(application, id);
    }
}
