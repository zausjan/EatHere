package com.udacity.eathere.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.udacity.eathere.DailyMenuCollection.MenuFragment;
import com.udacity.eathere.viewmodel.MenuViewModel;
import com.udacity.eathere.viewmodel.MenuViewModelFactory;
import com.udacity.eathere.R;
import com.udacity.eathere.viewmodel.RestaurantDetailViewModel;
import com.udacity.eathere.viewmodel.RestaurantDetailViewModelFactory;
import com.udacity.eathere.Widget.UpdateWidgetsService;
import com.udacity.eathere.model.DailyMenu;
import com.udacity.eathere.model.Restaurant;

import static android.content.Context.MODE_PRIVATE;
import static com.udacity.eathere.ui.LocationFragment.EXTRA_LAT;
import static com.udacity.eathere.ui.LocationFragment.EXTRA_LON;
import static com.udacity.eathere.ui.RestaurantDetailActivity.EXTRA_ID;
import static com.udacity.eathere.Widget.MenuWidgetProvider.WIDGET_MENU;


public class RestaurantDetailFragment extends Fragment {

    private static final String TAG = "RestaurantDetailFrag";
    private RestaurantDetailViewModel viewModel;
    private String restaurantId = "";
    private Restaurant restaurant;

    private TextView tv_title;
    private TextView tv_cuisines;
    private TextView tv_location;
    private ImageView iv_banner;
    private RatingBar rating;
    private RatingBar priceRange;
    private FloatingActionButton addtoWidget;

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RestaurantDetailPagerAdapter pagerAdapter;
    private MenuViewModel menuViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        restaurantId = bundle.getString(EXTRA_ID);

        viewModel = ViewModelProviders.of(this, new RestaurantDetailViewModelFactory(getActivity().getApplication(), restaurantId))
                .get(RestaurantDetailViewModel.class);

        menuViewModel = ViewModelProviders.of(this, new MenuViewModelFactory(getActivity().getApplication(), restaurantId)).get(MenuViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);

        tv_title = rootView.findViewById(R.id.tv_title);
        tv_cuisines = rootView.findViewById(R.id.tv_cuisines);
        tv_location = rootView.findViewById(R.id.tv_address);
        iv_banner = rootView.findViewById(R.id.iv_banner);
        rating = rootView.findViewById(R.id.rating_bar);
        priceRange = rootView.findViewById(R.id.price_range);
        addtoWidget = rootView.findViewById(R.id.add_to_widget);
        addtoWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DailyMenu menu = menuViewModel.getDailyMenu().getValue();
                if (menu != null) {
                    setSharedPreferences(menu);
                    UpdateWidgetsService.updateWidgets(getContext());
                    Toast.makeText(getContext(), getString(R.string.widget_updated), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.widget_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewPager = rootView.findViewById(R.id.pager);
        tabLayout = rootView.findViewById(R.id.tabs);


        observeViewModel(viewModel);

        return rootView;

    }


    private void observeViewModel(RestaurantDetailViewModel viewModel) {
        viewModel.getRestaurantDetail().observe(this, new Observer<Restaurant>() {
            @Override
            public void onChanged(@Nullable Restaurant restaurant) {
                if (restaurant != null) {
                    updateUi(restaurant);
                    setupViewPager();
                }
            }
        });
    }

    private void setupViewPager() {
        pagerAdapter = new RestaurantDetailPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setNestedScrollingEnabled(false);
        viewPager.setNestedScrollingEnabled(false);

    }

    private void updateUi(Restaurant restaurant) {
        tv_title.setText(restaurant.getName());
        tv_cuisines.setText(restaurant.getCuisines());
        tv_location.setText(restaurant.getLocation().getAddress());
        priceRange.setNumStars(restaurant.getPriceRange());
        rating.setRating(Float.valueOf(restaurant.getUserRating().getAggregateRating()));

        if (!restaurant.getThumb().equals("")) {
            Glide.with(getContext()).load(restaurant.getThumb()).into(iv_banner);
        }
        this.restaurant = restaurant;
    }

    public class RestaurantDetailPagerAdapter extends FragmentStatePagerAdapter {

        final int PAGE_COUNT = 2;
        final String tabTitles[] = new String[]{getString(R.string.menu), getString(R.string.map)};

        public RestaurantDetailPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            Bundle bundle = new Bundle();
            switch (position) {
                default:
                case 0:
                    fragment = new MenuFragment();
                    bundle.putString(EXTRA_ID, restaurantId);
                    break;
                case 1:
                    fragment = new LocationFragment();
                    bundle.putDouble(EXTRA_LAT, Double.parseDouble(restaurant.getLocation().getLatitude()));
                    bundle.putDouble(EXTRA_LON, Double.parseDouble(restaurant.getLocation().getLongitude()));
                    break;

            }

            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

    void setSharedPreferences(DailyMenu menu) {
        Gson gson = new Gson();
        String widget_recipe = gson.toJson(menu);

        SharedPreferences.Editor editor =
                getActivity().getSharedPreferences(getString(R.string.preference_file_key), MODE_PRIVATE)
                        .edit();
        editor.putString(WIDGET_MENU, widget_recipe);
        editor.apply();
    }
}
