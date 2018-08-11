package com.udacity.eathere.ui;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.udacity.eathere.R;
import com.udacity.eathere.ui.RestaurantDetailFragment;


public class RestaurantDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "extra_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        ImageButton upButton = findViewById(R.id.action_up);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        String id = getIntent().getStringExtra(EXTRA_ID);

        if(savedInstanceState == null){
            Bundle bundle = new Bundle();
            bundle.putString(EXTRA_ID, id);
            RestaurantDetailFragment fragment = new RestaurantDetailFragment();
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.restaurant_detail_container, fragment);
            transaction.commit();
        }

    }

}
