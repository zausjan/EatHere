package com.udacity.eathere.restaurantcollection;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.widget.Toast;

import com.udacity.eathere.BuildConfig;
import com.udacity.eathere.R;
import com.udacity.eathere.model.AppState;
import com.udacity.eathere.database.AppStateDao;
import com.udacity.eathere.database.RestaurantDao;
import com.udacity.eathere.database.RestaurantDb;
import com.udacity.eathere.api.ZomatoApi;
import com.udacity.eathere.model.LocationSuggestion;
import com.udacity.eathere.model.LocationSuggestionResponse;
import com.udacity.eathere.model.Restaurant;
import com.udacity.eathere.model.RestaurantObject;
import com.udacity.eathere.model.RestaurantResponse;
import com.udacity.eathere.model.SimpleRestaurant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestaurantRepository {

    private ZomatoApi zomatoApi;
    private RestaurantDao restaurantDao;
    private AppStateDao appStateDao;
    private LiveData<List<SimpleRestaurant>> favoriteRestaurants;
    private Application application;

    public RestaurantRepository(Application application) {
        this.application = application;
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(ZomatoApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        zomatoApi = retrofit.create(ZomatoApi.class);

        RestaurantDb db = RestaurantDb.getDatabase(application);
        restaurantDao = db.restaurantDao();
        appStateDao = db.appStateDao();
        favoriteRestaurants = restaurantDao.getAllRestaurants();
    }

    public void insert(SimpleRestaurant restaurant) {
        new insertAsyncTask(restaurantDao).execute(restaurant);
    }

    public void delete(SimpleRestaurant restaurant) {
        new deleteAsyncTask(restaurantDao).execute(restaurant);
    }

    public LiveData<List<SimpleRestaurant>> getFavoriteRestaurants() {
        return favoriteRestaurants;
    }

    public SimpleRestaurant getRestaurantById(final String id) {
        return restaurantDao.getRestaurantById(id);
    }


    public LiveData<List<SimpleRestaurant>> getNearestRestaurants(Double lat, Double lon) {
        final MutableLiveData<List<SimpleRestaurant>> data = new MutableLiveData<>();

        String radius = "10000";
        AppState appState = appStateDao.getAppStateLiveData().getValue();
        if(appState != null){
            radius = appState.getRadius().toString();
        }
        zomatoApi.getNearestRestaurants(BuildConfig.ZOMATO_API_KEY, lat.toString(), lon.toString(), radius, "real_distance")
                .enqueue(new Callback<RestaurantResponse>() {
                    @Override
                    public void onResponse(Call<RestaurantResponse> call, Response<RestaurantResponse> response) {
                        List<Restaurant> restaurants = new ArrayList<>();
                        for (RestaurantObject o : response.body().getRestaurantObjects()) {
                            restaurants.add(o.getRestaurant());
                        }

                        List<SimpleRestaurant> simpleRestaurants = new ArrayList<>();
                        for (Restaurant r : restaurants) {
                            simpleRestaurants.add(new SimpleRestaurant(r.getId(), r.getName(), r.getUrl(), r.getCuisines(), r.getThumb(), r.getUserRating().getAggregateRating(), r.getLocation()));
                        }
                        if (simpleRestaurants.size() > 0) {
                            data.setValue(simpleRestaurants);
                        }
                    }

                    @Override
                    public void onFailure(Call<RestaurantResponse> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(application.getApplicationContext(), R.string.network_error,Toast.LENGTH_LONG).show();

                    }
                });

        return data;
    }


    public LiveData<Restaurant> getRestaurantDetail(String id) {
        final MutableLiveData<Restaurant> data = new MutableLiveData<>();
        zomatoApi.getRestaurantDetail(BuildConfig.ZOMATO_API_KEY, id).enqueue(
                new Callback<Restaurant>() {
                    @Override
                    public void onResponse(Call<Restaurant> call, Response<Restaurant> response) {
                        data.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<Restaurant> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(application.getApplicationContext(), R.string.network_error,Toast.LENGTH_LONG).show();
                    }
                }
        );
        return data;
    }

    public LiveData<LocationSuggestion> getLocationSuggestion(String city) {
        final MutableLiveData<LocationSuggestion> data = new MutableLiveData<>();
        zomatoApi.getSuggestedLocation(BuildConfig.ZOMATO_API_KEY, city).enqueue(
                new Callback<LocationSuggestionResponse>() {
                    @Override
                    public void onResponse(Call<LocationSuggestionResponse> call, Response<LocationSuggestionResponse> response) {
                        try {
                            data.setValue(response.body().getLocationSuggestions().get(0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<LocationSuggestionResponse> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(application.getApplicationContext(), R.string.network_error,Toast.LENGTH_LONG).show();
                    }
                }
        );
        return data;
    }

    public LiveData<AppState> getAppStateLiveData() {
        return appStateDao.getAppStateLiveData();
    }

    public void setAppState(AppState appState) {
        new setAppStateAsyncTask(appStateDao).execute(appState);
    }


    private static class insertAsyncTask extends AsyncTask<SimpleRestaurant, Void, Void> {
        private RestaurantDao asyncDao;
        insertAsyncTask(RestaurantDao dao) {
            asyncDao = dao;
        }

        @Override
        protected Void doInBackground(SimpleRestaurant... restaurants) {
            asyncDao.insert(restaurants[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<SimpleRestaurant, Void, Void> {
        private RestaurantDao asyncDao;

        deleteAsyncTask(RestaurantDao dao) {
            asyncDao = dao;
        }

        @Override
        protected Void doInBackground(SimpleRestaurant... simpleRestaurants) {
            asyncDao.delete(simpleRestaurants[0]);
            return null;
        }
    }

    private static class setAppStateAsyncTask extends AsyncTask<AppState, Void, Void> {
        private AppStateDao asyncDao;

        setAppStateAsyncTask(AppStateDao dao) {
            asyncDao = dao;
        }

        @Override
        protected Void doInBackground(AppState... appStates) {
            asyncDao.setAppState(appStates[0]);
            return null;
        }
    }

}
