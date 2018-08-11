package com.udacity.eathere.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.udacity.eathere.model.AppState;


@Dao
public interface AppStateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void setAppState(AppState... appState);

    @Query("SELECT * FROM app_state LIMIT 1")
    public AppState getAppState();

    @Query("SELECT * FROM app_state LIMIT 1")
    public LiveData<AppState> getAppStateLiveData();


    @Query("UPDATE app_state SET latitude=:latitude, longtitude=:longitude")
    public void updateLocation(Double latitude, Double longitude);

    @Query("UPDATE app_state SET radius=:radius")
    public void updateRadius(Long radius);

    @Query("UPDATE app_state SET gps=:isGpsOn")
    public void updateGps(Boolean isGpsOn);
}
