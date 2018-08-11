package com.udacity.eathere.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.udacity.eathere.RestaurantCollection.RestaurantRepository;
import com.udacity.eathere.model.Restaurant;

public class RestaurantDetailViewModel extends AndroidViewModel{
    private RestaurantRepository repository;
    private LiveData<Restaurant> restaurant;

    public RestaurantDetailViewModel(@NonNull Application application, String id){
        super(application);
        repository = new RestaurantRepository(application);
        restaurant = repository.getRestaurantDetail(id);
    }
    public LiveData<Restaurant> getRestaurantDetail(){
        return restaurant;
    }

}
