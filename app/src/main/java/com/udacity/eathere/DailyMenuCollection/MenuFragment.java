package com.udacity.eathere.DailyMenuCollection;


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
import com.udacity.eathere.model.DailyMenu;
import com.udacity.eathere.viewmodel.MenuViewModel;
import com.udacity.eathere.viewmodel.MenuViewModelFactory;

import static com.udacity.eathere.ui.RestaurantDetailActivity.EXTRA_ID;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    private MenuViewModel viewModel;
    private RecyclerView recyclerView;
    private MenuListAdapter adapter;

    private DailyMenu dailyMenu;

    private String RESTAURANT_ID;

    private TextView noMenu;

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RESTAURANT_ID = getArguments().getString(EXTRA_ID);
        viewModel = ViewModelProviders.of(this, new MenuViewModelFactory(getActivity().getApplication(), RESTAURANT_ID)).get(MenuViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
        noMenu = rootView.findViewById(R.id.tv_empty);
        recyclerView = rootView.findViewById(R.id.rv_menu);
        adapter = new MenuListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        observeViewModel(viewModel);

        return rootView;
    }

    private void observeViewModel(MenuViewModel viewModel) {
        viewModel.getDailyMenu().observe(this, new Observer<DailyMenu>() {
            @Override
            public void onChanged(@Nullable DailyMenu dailyMenu) {
                if (dailyMenu == null) {
                    noMenu.setVisibility(View.VISIBLE);
                } else {
                    noMenu.setVisibility(View.GONE);
                    adapter.setData(dailyMenu);
                }
            }
        });
    }

}
