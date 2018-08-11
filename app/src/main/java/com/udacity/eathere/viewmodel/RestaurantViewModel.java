package com.udacity.eathere.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.udacity.eathere.model.AppState;
import com.udacity.eathere.RestaurantCollection.RestaurantRepository;
import com.udacity.eathere.model.LocationSuggestion;
import com.udacity.eathere.model.SimpleRestaurant;

import java.util.List;

public class RestaurantViewModel extends AndroidViewModel {

    private Double latitude;
    private Double longtitude;

    private RestaurantRepository repository;
    private LiveData<List<SimpleRestaurant>> favoriteRestaurants;
    private LiveData<List<SimpleRestaurant>> nearestRestaurantsObservable;


    public RestaurantViewModel(@NonNull Application application) {
        super(application);
        repository = new RestaurantRepository(application);
        favoriteRestaurants = repository.getFavoriteRestaurants();
    }

    public void insert(SimpleRestaurant restaurant){
        repository.insert(restaurant);
    }
    public void delete(SimpleRestaurant restaurant){
        repository.delete(restaurant);
    }
    public SimpleRestaurant getRestaurantById(String id){
        return repository.getRestaurantById(id);
    }


    public LiveData<List<SimpleRestaurant>> getFavoriteRestaurants() {
        return favoriteRestaurants;
    }

    public LiveData<List<SimpleRestaurant>> getNearestRestaurantsObservable(Double lat, Double lng) {
        if(nearestRestaurantsObservable == null || !lat.equals(latitude) || !lng.equals(longtitude)) {
            latitude = lat;
            longtitude = lng;
            nearestRestaurantsObservable = repository.getNearestRestaurants(lat, lng);
        }
        return nearestRestaurantsObservable;
    }

    public LiveData<LocationSuggestion> getLocationSuggestion(String city){
        return repository.getLocationSuggestion(city);
    }

    public LiveData<AppState> getAppStateLive(){
        return repository.getAppStateLiveData();
    }

}
