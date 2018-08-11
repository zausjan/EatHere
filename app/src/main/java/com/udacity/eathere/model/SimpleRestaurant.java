package com.udacity.eathere.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "restaurants")
public class SimpleRestaurant {
    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String url;
    private String cuisines;
    private String thumb;
    private String userRating;
    private Location location;

    public SimpleRestaurant(String id, String name, String url, String cuisines, String thumb, String userRating, Location location) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.cuisines = cuisines;
        this.thumb = thumb;
        this.userRating = userRating;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
