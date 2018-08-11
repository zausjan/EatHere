
package com.udacity.eathere.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dishes {

    @SerializedName("dish")
    @Expose
    private Dish dish;

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

}
