package com.udacity.eathere.Widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class UpdateWidgetsService extends IntentService {

    private static final String ACTION_UPDATE_MENU = "com.udacity.eathere.action.update_widget";

    public UpdateWidgetsService() {
        super("UpdateWidgetsService");
    }

    public static void updateWidgets(Context context) {
        Intent intent = new Intent(context, UpdateWidgetsService.class);
        intent.setAction(ACTION_UPDATE_MENU);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            final String action = intent.getAction();
            if(action.equals(ACTION_UPDATE_MENU)){
                handleActionUpdateMenu();
            }
        }
    }

    private void handleActionUpdateMenu() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, MenuWidgetProvider.class));

        for(int appWidgetId: appWidgetIds){
            MenuWidgetProvider.updateAppWidget(this, appWidgetManager, appWidgetId);
        }

    }


}
