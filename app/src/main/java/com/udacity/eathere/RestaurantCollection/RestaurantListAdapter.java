package com.udacity.eathere.RestaurantCollection;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.eathere.database.AppExecutors;
import com.udacity.eathere.R;
import com.udacity.eathere.ui.RestaurantDetailActivity;
import com.udacity.eathere.model.SimpleRestaurant;
import com.udacity.eathere.viewmodel.RestaurantViewModel;

import java.util.List;

import static com.udacity.eathere.ui.RestaurantDetailActivity.EXTRA_ID;


public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantViewHolder> {

    private List<SimpleRestaurant> restaurants;
    private RestaurantViewModel viewModel;
    private Boolean isFavorite = false;
    private Context context;

    public RestaurantListAdapter(Context context, RestaurantViewModel viewModel) {
        this.viewModel = viewModel;
        this.context = context;
    }


    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_restaurant, parent, false);
        return new RestaurantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RestaurantViewHolder holder, int position) {

        final SimpleRestaurant restaurant = restaurants.get(position);
        holder.bindData(restaurant);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            public void run() {
                SimpleRestaurant temp = viewModel.getRestaurantById(restaurant.getId());
                isFavorite = temp != null;

                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (isFavorite) {
                            holder.favorite.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                        } else {
                            holder.favorite.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                        }
                    }
                });

            }
        });

        holder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    holder.favorite.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
                    isFavorite = false;
                    viewModel.delete(restaurant);
                } else {
                    holder.favorite.setBackgroundResource(R.drawable.ic_favorite_black_24dp);
                    isFavorite = true;
                    viewModel.insert(restaurant);
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RestaurantDetailActivity.class);
                intent.putExtra(EXTRA_ID, restaurant.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (restaurants != null) {
            return restaurants.size();
        } else {
            return 0;
        }
    }

    public void setData(List<SimpleRestaurant> data) {
        restaurants = data;
        notifyDataSetChanged();
    }
}
