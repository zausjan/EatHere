
package com.udacity.eathere.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyMenuObject {

    @SerializedName("daily_menu")
    @Expose
    private DailyMenu dailyMenu;

    public DailyMenu getDailyMenu() {
        return dailyMenu;
    }

    public void setDailyMenu(DailyMenu dailyMenu) {
        this.dailyMenu = dailyMenu;
    }

}
