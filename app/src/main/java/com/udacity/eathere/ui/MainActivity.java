package com.udacity.eathere.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.udacity.eathere.model.AppState;
import com.udacity.eathere.database.AppStateRepository;
import com.udacity.eathere.R;

import java.util.concurrent.Executors;

import static com.udacity.eathere.ui.MapFragment.LOCATION_PERMISSION_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private AppStateRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repository = new AppStateRepository(getApplication());

        setupAppState();

        drawerLayout = findViewById(R.id.drawer_layout);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        setupDrawerContent(navigationView);

        if (savedInstanceState == null) {
            NearestListFragment fragment = new NearestListFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.commit();
        }


    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Fragment fragment;
                        switch (menuItem.getItemId()) {
                            case R.id.nav_nearest:
                                fragment = new NearestListFragment();
                                break;
                            case R.id.nav_favorite:
                                fragment = new FavoriteListFragment();
                                break;
                            case R.id.nav_map:
                                fragment = new MapFragment();
                                break;
                            case R.id.nav_settings:
                                fragment = new PreferenceFragment();
                                break;
                            default:
                                return false;
                        }

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupAppState() {
        Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                AppState appState = repository.getAppState();
                if (appState == null) {
                    appState = new AppState(1L, 49.195060, 16.606837, 10000L, false);
                    repository.setAppState(appState);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION_REQUEST_CODE);
                    }
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    repository.updateGps(true);
                }
            }

        }
    }

}
