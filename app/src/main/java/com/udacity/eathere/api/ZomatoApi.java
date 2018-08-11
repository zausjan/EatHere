package com.udacity.eathere.api;


import com.udacity.eathere.BuildConfig;
import com.udacity.eathere.model.DailyMenuResponse;
import com.udacity.eathere.model.LocationSuggestionResponse;
import com.udacity.eathere.model.Restaurant;
import com.udacity.eathere.model.RestaurantResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ZomatoApi {
    String BASE_URL = "https://developers.zomato.com/";

    @GET("api/v2.1/search")
    Call<RestaurantResponse> getNearestRestaurants(
            @Header("user-key") String user_key,
            @Query("lat") String lat,
            @Query("lon") String lon,
            @Query("radius") String radius,
            @Query("sort") String sort);

    @GET("api/v2.1/restaurant")
    Call<Restaurant> getRestaurantDetail(@Header("user-key") String user_key,
                                         @Query("res_id") String id);

    @GET("api/v2.1/dailymenu")
    Call<DailyMenuResponse> getDailyMenu(@Header("user-key") String user_key,
                                         @Query("res_id") String id);

    @GET("api/v2.1/locations")
    Call<LocationSuggestionResponse> getSuggestedLocation(@Header("user-key") String user_key,
                                                          @Query("query") String city);

}
