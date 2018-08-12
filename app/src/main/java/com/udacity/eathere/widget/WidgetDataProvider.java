package com.udacity.eathere.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.udacity.eathere.R;
import com.udacity.eathere.model.DailyMenu;
import com.udacity.eathere.model.Dish;
import com.udacity.eathere.model.Dishes;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.udacity.eathere.widget.MenuWidgetProvider.WIDGET_MENU;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {


    private List<Dishes> dishes;
    private Context mContext;

    public WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(dishes == null){
            return 0;
        }
        return dishes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews view = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_item);
        Dish dish = dishes.get(position).getDish();
        view.setTextViewText(R.id.tv_name, dish.getName());
        view.setTextViewText(R.id.tv_price, dish.getPrice());
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void initData() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(mContext.getString(R.string.preference_file_key), MODE_PRIVATE);
        String result = sharedPreferences.getString(WIDGET_MENU, null);
        if(result == null){
            return;
        }
        Gson gson = new Gson();
        DailyMenu dailyMenu = gson.fromJson(result, DailyMenu.class);
        dishes = dailyMenu.getDishes();
    }

}