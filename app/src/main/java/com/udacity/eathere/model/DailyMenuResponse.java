
package com.udacity.eathere.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyMenuResponse {

    @SerializedName("daily_menus")
    @Expose
    private List<DailyMenuObject> dailyMenus = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<DailyMenuObject> getDailyMenus() {
        return dailyMenus;
    }

    public void setDailyMenus(List<DailyMenuObject> dailyMenus) {
        this.dailyMenus = dailyMenus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
