package com.udacity.eathere.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.ui.IconGenerator;
import com.udacity.eathere.model.AppState;
import com.udacity.eathere.database.AppStateRepository;
import com.udacity.eathere.R;
import com.udacity.eathere.viewmodel.RestaurantViewModel;
import com.udacity.eathere.viewmodel.RestaurantViewModelFactory;
import com.udacity.eathere.model.SimpleRestaurant;

import java.util.List;

import static com.udacity.eathere.ui.RestaurantDetailActivity.EXTRA_ID;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String TAG = "MapFragment";


    private static final int DEFAULT_ZOOM = 13;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private MapView mapView;
    private GoogleMap map;

    private Location lastKnownLocation;
    private CameraPosition cameraPosition;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private RestaurantViewModel viewModel;
    private AppStateRepository appStateRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getString(R.string.map));

        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);

        }
        viewModel = ViewModelProviders.of(this, new RestaurantViewModelFactory(getActivity().getApplication())).get(RestaurantViewModel.class);
        appStateRepository = new AppStateRepository(getActivity().getApplication());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = rootView.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        map.setOnMarkerClickListener(this);

        if (cameraPosition != null) {
            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        appStateRepository.getAppStateLiveData().observe(this, new Observer<AppState>() {
            @Override
            public void onChanged(@Nullable AppState appState) {
                if (appState == null)
                    return;
                updateLocationUI(appState.getGPSOn());

                if (appState.getGPSOn()) {
                    getDeviceLocation();
                }
                if(cameraPosition == null){
                    zoomToLocation(appState);
                }
                viewModel.getNearestRestaurantsObservable(appState.getLatitude(), appState.getLongitude()).observe(getActivity(), new Observer<List<SimpleRestaurant>>() {
                    @Override
                    public void onChanged(@Nullable List<SimpleRestaurant> restaurants) {
                        if (restaurants != null) {
                            markRestaurants(restaurants);
                        }
                    }
                });
            }
        });
    }

    private void getDeviceLocation() {
        try {
            Task locationResult = fusedLocationProviderClient.getLastLocation();
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
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void zoomToLocation(AppState appState) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(appState.getLatitude(), appState.getLongitude()), DEFAULT_ZOOM));
    }

    private void updateLocationUI(Boolean permissionGranted) {
        if (map == null) {
            return;
        }
        try {
            if (permissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        marker.hideInfoWindow();
        Intent intent = new Intent(getContext(), RestaurantDetailActivity.class);
        intent.putExtra(EXTRA_ID, (String) marker.getTag());
        startActivity(intent);
        return false;
    }


    private void markRestaurants(List<SimpleRestaurant> restaurants) {
        if (restaurants == null) return;
        for (SimpleRestaurant r : restaurants) {
            LatLng position = new LatLng(Double.valueOf(r.getLocation().getLatitude()),
                    Double.valueOf(r.getLocation().getLongitude()));
            IconGenerator iconGen = new IconGenerator(getContext());
            MarkerOptions markerOptions = new MarkerOptions().
                    icon(BitmapDescriptorFactory.fromBitmap(iconGen.makeIcon(r.getName()))).
                    position(position).
                    anchor(iconGen.getAnchorU(), iconGen.getAnchorV());
            Marker marker = map.addMarker(markerOptions);
            marker.setTag(r.getId());

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


}
