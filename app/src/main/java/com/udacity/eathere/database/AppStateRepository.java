package com.udacity.eathere.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;

import com.udacity.eathere.model.AppState;

import java.util.concurrent.Executors;


public class AppStateRepository {

    private AppStateDao appStateDao;
    public AppStateRepository(Application application){
        RestaurantDb db = RestaurantDb.getDatabase(application);
        appStateDao = db.appStateDao();
    }
    public LiveData<AppState> getAppStateLiveData() {
        return appStateDao.getAppStateLiveData();
    }

    public AppState getAppState() {
        return appStateDao.getAppState();
    }
    public void setAppState(final AppState appState) {
        //todo
      //  AppExecutors.getInstance().diskIO().execute(
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                appStateDao.setAppState(appState);
            }
        });
    }

    public void updateLocation(final Double lat, final Double lng){
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                appStateDao.updateLocation(lat, lng);
            }
        });
    }

    public void updateGps(final Boolean isGpsOn){
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                appStateDao.updateGps(isGpsOn);
            }
        });
    }

    public void updateRadius(final Long radius) {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                appStateDao.updateRadius(radius);
            }
        });
    }
}
