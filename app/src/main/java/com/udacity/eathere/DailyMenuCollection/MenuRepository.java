package com.udacity.eathere.DailyMenuCollection;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.udacity.eathere.BuildConfig;
import com.udacity.eathere.api.ZomatoApi;
import com.udacity.eathere.model.DailyMenu;
import com.udacity.eathere.model.DailyMenuResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MenuRepository {
    private ZomatoApi api;

    public MenuRepository(Application application) {
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(ZomatoApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        api = retrofit.create(ZomatoApi.class);
    }

    public LiveData<DailyMenu> getDailyMenu(String id) {
        final MutableLiveData<DailyMenu> data = new MutableLiveData<>();


        api.getDailyMenu(BuildConfig.ZOMATO_API_KEY, id)
                .enqueue(new Callback<DailyMenuResponse>() {
                             @Override
                             public void onResponse(Call<DailyMenuResponse> call, Response<DailyMenuResponse> response) {
                                 if (response.body() != null && response.body().getDailyMenus() != null
                                         && response.body().getDailyMenus().size() > 0) {
                                     data.setValue(response.body().getDailyMenus().get(0).getDailyMenu());
                                 }

                             }

                             @Override
                             public void onFailure(Call<DailyMenuResponse> call, Throwable t) {
                                 t.printStackTrace();
                             }
                         }
                );
        return data;
    }
}
