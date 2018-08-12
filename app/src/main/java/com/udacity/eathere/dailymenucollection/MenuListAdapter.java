package com.udacity.eathere.dailymenucollection;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.eathere.R;
import com.udacity.eathere.model.DailyMenu;
import com.udacity.eathere.model.Dish;


public class MenuListAdapter extends RecyclerView.Adapter<DailyMenuViewHolder> {

    private DailyMenu dailyMenu;

    public MenuListAdapter() {
    }

    @NonNull
    @Override
    public DailyMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_menu_item, parent, false);
        return new DailyMenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DailyMenuViewHolder holder, int position) {
        final Dish dish = dailyMenu.getDishes().get(position).getDish();
        holder.bindData(dish);

    }

    @Override
    public int getItemCount() {
        if (dailyMenu != null) {
            return dailyMenu.getDishes().size();
        } else {
            return 0;
        }
    }

    public void setData(DailyMenu data) {
        dailyMenu = data;
        notifyDataSetChanged();
    }
}
