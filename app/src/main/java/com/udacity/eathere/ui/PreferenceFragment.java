package com.udacity.eathere.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;

import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.udacity.eathere.model.AppState;
import com.udacity.eathere.database.AppStateRepository;
import com.udacity.eathere.R;
import com.udacity.eathere.viewmodel.RestaurantViewModel;
import com.udacity.eathere.viewmodel.RestaurantViewModelFactory;
import com.udacity.eathere.model.LocationSuggestion;

import static com.udacity.eathere.ui.MapFragment.LOCATION_PERMISSION_REQUEST_CODE;


public class PreferenceFragment extends PreferenceFragmentCompat {
    private AppStateRepository appStateRepository;
    private RestaurantViewModel viewModel;

    private EditTextPreference locationPref;
    private EditTextPreference radiusPref;
    private CheckBoxPreference gpsPref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_eathere);

        getActivity().setTitle(getString(R.string.settings));

        viewModel = ViewModelProviders.of(this, new RestaurantViewModelFactory(getActivity().getApplication())).get(RestaurantViewModel.class);
        appStateRepository = new AppStateRepository(getActivity().getApplication());

        locationPref = (EditTextPreference) findPreference(getString(R.string.key_location));
        radiusPref = (EditTextPreference) findPreference(getString(R.string.key_radius));
        gpsPref = (CheckBoxPreference) findPreference(getString(R.string.key_gps));


        locationPref.setSummary(locationPref.getText());
        radiusPref.setSummary(radiusPref.getText() + "m");


        appStateRepository.getAppStateLiveData().observe(this, new Observer<AppState>() {
            @Override
            public void onChanged(@Nullable AppState appState) {
                if (appState != null && appState.getGPSOn()) {
                    locationPref.setEnabled(false);
                    gpsPref.setChecked(true);

                } else {
                    locationPref.setEnabled(true);
                    gpsPref.setChecked(false);
                }
            }
        });

        setOnChangeListeners();

    }

    private void setLocation(final String city) {
        viewModel.getLocationSuggestion(city).observe(getActivity(),
                new Observer<LocationSuggestion>() {
                    @Override
                    public void onChanged(@Nullable final LocationSuggestion locationSuggestion) {
                        if (locationSuggestion != null) {
                            appStateRepository.updateLocation(locationSuggestion.getLatitude(), locationSuggestion.getLongitude());
                            locationPref.setText(locationSuggestion.getCityName());
                            locationPref.setSummary(locationSuggestion.getCityName());

                        }else {
                            Toast.makeText(getContext(), getString(R.string.location_error), Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void setOnChangeListeners() {
        locationPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                setLocation(String.valueOf(newValue));

                return false;
            }
        });

        radiusPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                try {
                    Long.valueOf((String) newValue);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    return false;
                }
                preference.setSummary(String.valueOf(newValue));
                appStateRepository.updateRadius(Long.valueOf((String) newValue));
                return true;
            }
        });

        gpsPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, final Object newValue) {
                appStateRepository.updateGps((Boolean) newValue);
                if ((Boolean) newValue) {
                    locationPref.setEnabled(false);
                    checkLocationPermission();
                } else {
                    locationPref.setEnabled(true);
                    setLocation(locationPref.getText());
                }
                return true;
            }
        });
    }
}