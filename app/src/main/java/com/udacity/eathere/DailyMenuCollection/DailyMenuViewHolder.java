package com.udacity.eathere.DailyMenuCollection;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.udacity.eathere.R;
import com.udacity.eathere.model.Dish;

public class DailyMenuViewHolder extends RecyclerView.ViewHolder {
    TextView name;
    TextView price;

    public DailyMenuViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.tv_name);
        price = itemView.findViewById(R.id.tv_price);
    }

    public void bindData(Dish model) {
        name.setText(model.getName());
        price.setText(model.getPrice());
    }
}
