package com.udacity.eathere.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.udacity.eathere.model.Restaurant;
import com.udacity.eathere.model.SimpleRestaurant;

import java.util.List;

@Dao
public interface RestaurantDao {

    @Query("SELECT * FROM restaurants")
    LiveData<List<SimpleRestaurant>> getAllRestaurants();

    @Query("SELECT * FROM restaurants WHERE id = :id")
    SimpleRestaurant getRestaurantById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SimpleRestaurant... restaurants);

    @Delete
    void delete(SimpleRestaurant... restaurants);


}
