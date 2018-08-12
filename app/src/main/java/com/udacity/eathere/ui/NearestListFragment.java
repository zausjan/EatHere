package com.udacity.eathere.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.udacity.eathere.restaurantcollection.RestaurantListAdapter;
import com.udacity.eathere.model.AppState;
import com.udacity.eathere.database.AppStateRepository;
import com.udacity.eathere.R;
import com.udacity.eathere.model.SimpleRestaurant;
import com.udacity.eathere.viewmodel.RestaurantViewModel;
import com.udacity.eathere.viewmodel.RestaurantViewModelFactory;

import java.util.List;


public class NearestListFragment extends Fragment {

    private static final String TAG = "NearestListFragment";
    static final String RECYCLER_SCROLL_STATE = "scroll_state";
    private TextView empty;
    private RecyclerView rv;
    private RestaurantListAdapter adapter;

    private RestaurantViewModel viewModel;
    private AppStateRepository appStateRepository;

    private Location lastKnownLocation;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getString(R.string.nerest));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        viewModel = ViewModelProviders.of(this, new RestaurantViewModelFactory(getActivity().getApplication())).get(RestaurantViewModel.class);
        appStateRepository = new AppStateRepository(getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        rv = rootView.findViewById(R.id.rv_nearest_restaurants);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        empty = rootView.findViewById(R.id.tv_empty);
        adapter = new RestaurantListAdapter(getContext(), viewModel);
        rv.setAdapter(adapter);

        appStateRepository.getAppStateLiveData().observe(this, new Observer<AppState>() {
            @Override
            public void onChanged(@Nullable AppState appState) {
                if (appState == null)
                    return;
                if (appState.getGPSOn()) {
                    getDeviceLocation();
                }
                viewModel.getNearestRestaurantsObservable(appState.getLatitude(), appState.getLongitude()).observe(getActivity(), new Observer<List<SimpleRestaurant>>() {
                    @Override
                    public void onChanged(@Nullable List<SimpleRestaurant> restaurants) {
                        if (restaurants != null && restaurants.size() > 0) {
                            adapter.setData(restaurants);
                            if (savedInstanceState != null)
                                rv.getLayoutManager().onRestoreInstanceState(savedInstanceState.getParcelable(RECYCLER_SCROLL_STATE));
                            empty.setVisibility(View.GONE);
                        } else {
                            empty.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.drawer_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLER_SCROLL_STATE, rv.getLayoutManager().onSaveInstanceState());
    }


    private void getDeviceLocation() {
        try {
            Task locationResult = mFusedLocationClient.getLastLocation();
            locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Location temp = (Location) task.getResult();
                        if (lastKnownLocation == null || Math.abs(temp.getLatitude() - lastKnownLocation.getLatitude()) > 0.000001) {
                            lastKnownLocation = (Location) task.getResult();
                            appStateRepository.updateLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                        }

                    } else {
                        Log.d(TAG, "Current location is null. ");
                    }
                }
            });

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

    }


}
