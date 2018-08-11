package com.udacity.eathere.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "app_state")
public class AppState {

    @PrimaryKey()
    @ColumnInfo(name = "id")
    Long id;
    @ColumnInfo(name = "latitude")
    Double latitude;
    @ColumnInfo(name = "gps")
    Boolean isGPSOn;
    @ColumnInfo(name = "longtitude")
    Double longitude;
    @ColumnInfo(name = "radius")
    Long radius;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getRadius() {
        return radius;
    }

    public void setRadius(Long radius) {
        this.radius = radius;
    }

    public Boolean getGPSOn() {
        return isGPSOn;
    }

    public void setGPSOn(Boolean GPSOn) {
        isGPSOn = GPSOn;
    }

    public AppState(Long id, Double latitude,  Double longitude, Long radius, Boolean isGPSOn) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.id = id;
        this.isGPSOn = isGPSOn;
    }


}
