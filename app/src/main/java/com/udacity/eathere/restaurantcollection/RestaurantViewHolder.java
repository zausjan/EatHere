package com.udacity.eathere.restaurantcollection;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.udacity.eathere.R;
import com.udacity.eathere.model.SimpleRestaurant;

public class RestaurantViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    ImageView thumbnail;
    ImageButton favorite;
    RatingBar ratingBar;

    public RestaurantViewHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.tv_title);
        thumbnail = itemView.findViewById(R.id.iv_thumbnail);
        favorite = itemView.findViewById(R.id.ib_favorite);
        ratingBar = itemView.findViewById(R.id.rating_bar);
    }

    public void bindData(SimpleRestaurant model) {
        title.setText(model.getName());
        if(!model.getThumb().equals("")){
            Glide.with(itemView).load(model.getThumb()).into(thumbnail);
        }else {
            thumbnail.setImageResource(R.drawable.restaurant);
        }
        favorite.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
        ratingBar.setStepSize(0.01f);
        ratingBar.setIsIndicator(true);
        ratingBar.setRating(Float.valueOf(model.getUserRating()));
        ratingBar.invalidate();
    }
}
