
package com.udacity.eathere.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantResponse {

    @SerializedName("results_found")
    @Expose
    private Integer resultsFound;
    @SerializedName("results_start")
    @Expose
    private Integer resultsStart;
    @SerializedName("results_shown")
    @Expose
    private Integer resultsShown;
    @SerializedName("restaurants")
    @Expose
    private List<RestaurantObject> restaurantObjects = null;

    public Integer getResultsFound() {
        return resultsFound;
    }

    public void setResultsFound(Integer resultsFound) {
        this.resultsFound = resultsFound;
    }

    public Integer getResultsStart() {
        return resultsStart;
    }

    public void setResultsStart(Integer resultsStart) {
        this.resultsStart = resultsStart;
    }

    public Integer getResultsShown() {
        return resultsShown;
    }

    public void setResultsShown(Integer resultsShown) {
        this.resultsShown = resultsShown;
    }

    public List<RestaurantObject> getRestaurantObjects() {
        return restaurantObjects;
    }

    public void setRestaurantObjects(List<RestaurantObject> restaurantObjects) {
        this.restaurantObjects = restaurantObjects;
    }

}
