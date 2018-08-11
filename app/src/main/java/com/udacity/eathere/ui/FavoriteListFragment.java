package com.udacity.eathere.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.eathere.R;
import com.udacity.eathere.RestaurantCollection.RestaurantListAdapter;
import com.udacity.eathere.model.SimpleRestaurant;
import com.udacity.eathere.viewmodel.RestaurantViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteListFragment extends Fragment {

    private RecyclerView rv;
    private RestaurantListAdapter adapter;

    private RestaurantViewModel viewModel;
    private TextView empty;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(getString(R.string.favorite));
        viewModel = ViewModelProviders.of(this).get(RestaurantViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        rv = rootView.findViewById(R.id.rv_nearest_restaurants);
        empty = rootView.findViewById(R.id.tv_empty);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RestaurantListAdapter(getContext(), viewModel);
        rv.setAdapter(adapter);


        viewModel.getFavoriteRestaurants().observe(this, new Observer<List<SimpleRestaurant>>() {
            @Override
            public void onChanged(@Nullable List<SimpleRestaurant> restaurants) {
                if(restaurants != null && restaurants.size() > 0) {
                    adapter.setData(restaurants);
                    empty.setVisibility(View.GONE);
                }
                else {
                    empty.setVisibility(View.VISIBLE);
                }
            }
        });

        return rootView;
    }


}
