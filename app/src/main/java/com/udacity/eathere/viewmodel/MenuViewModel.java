package com.udacity.eathere.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.udacity.eathere.dailymenucollection.MenuRepository;
import com.udacity.eathere.model.DailyMenu;


public class MenuViewModel extends AndroidViewModel {

    private MenuRepository repository;
    private LiveData<DailyMenu> dailyMenuLiveData;

    public MenuViewModel(@NonNull Application application, String id) {
        super(application);
        repository = new MenuRepository(application);
        dailyMenuLiveData = repository.getDailyMenu(id);
    }

    public LiveData<DailyMenu> getDailyMenu() {
        return dailyMenuLiveData;
    }
}
