package com.udacity.eathere.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacity.eathere.model.Location;

import java.lang.reflect.Type;

public class Converters {

    @TypeConverter
    public static Location stringToLocation(String data) {
        if (data == null) {
            return null;
        }

        Type type = new TypeToken<Location>() {
        }.getType();

        return new Gson().fromJson(data, type);
    }


    @TypeConverter
    public static String locationToString(Location location) {
        return new Gson().toJson(location);
    }
}
