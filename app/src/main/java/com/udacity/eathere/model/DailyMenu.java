
package com.udacity.eathere.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyMenu {

    @SerializedName("daily_menu_id")
    @Expose
    private String dailyMenuId;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("dishes")
    @Expose
    private List<Dishes> dishes = null;

    public String getDailyMenuId() {
        return dailyMenuId;
    }

    public void setDailyMenuId(String dailyMenuId) {
        this.dailyMenuId = dailyMenuId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Dishes> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dishes> dishes) {
        this.dishes = dishes;
    }

}
