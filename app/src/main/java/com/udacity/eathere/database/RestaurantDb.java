package com.udacity.eathere.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.udacity.eathere.model.AppState;
import com.udacity.eathere.model.SimpleRestaurant;

@Database(entities = {SimpleRestaurant.class, AppState.class}, version = 1, exportSchema = false)

@TypeConverters({Converters.class})
public abstract class RestaurantDb extends RoomDatabase {

    public abstract RestaurantDao restaurantDao();

    public abstract AppStateDao appStateDao();

    private static RestaurantDb INSTANCE;

    public static RestaurantDb getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RestaurantDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RestaurantDb.class, "restaurant_db")
                            .build();

                }
            }
        }
        return INSTANCE;
    }

}