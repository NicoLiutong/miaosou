package com.example.animation.view;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.example.animation.R;
import com.example.animation.server.UpdateAppWidgetService;

import java.util.Calendar;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Implementation of App Widget functionality.
 */
public class HomeAnimationList extends AppWidgetProvider {

    public static final String CHANGE_LIST = "com.example.animation.list.CHANGE_LIST";
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_animation_list);
        Intent intent = new Intent(HomeAnimationList.CHANGE_LIST);
        intent.putExtra("type","refresh");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
        views.setOnClickPendingIntent(R.id.home_refresh,pendingIntent);
        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName componentName = new ComponentName(getApplicationContext(),HomeAnimationList.class);
        manager.updateAppWidget(componentName,views);
    }

    @Override
    public void onEnabled(final Context context) {
        // Enter relevant functionality for when the first widget is created
        Intent intent = new Intent(context, UpdateAppWidgetService.class);
        intent.putExtra("type","refresh");
        context.startService(intent);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Intent intent = new Intent(context, UpdateAppWidgetService.class);
        //intent.putExtra("type","refresh");
        context.stopService(intent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(TextUtils.equals(intent.getAction(),CHANGE_LIST)){
            Intent intent2 = new Intent(context, UpdateAppWidgetService.class);
            intent2.putExtra("type",intent.getStringExtra("type"));
            context.startService(intent2);
        }else if(TextUtils.equals(intent.getAction(),"android.intent.action.BOOT_COMPLETED")||
                TextUtils.equals(intent.getAction(),"android.intent.action.USER_PRESENT")){
            Intent intent2 = new Intent(context, UpdateAppWidgetService.class);
            intent2.putExtra("type","refresh");
            context.startService(intent2);
        }else {
            super.onReceive(context, intent);
            Intent intent2 = new Intent(context, UpdateAppWidgetService.class);
            intent2.putExtra("type","refresh");
            context.startService(intent2);
        }

    }



    public static int getCalendarWeek(){
        Calendar calendar = Calendar.getInstance();
        int i;
        i = calendar.get(Calendar.DAY_OF_WEEK);
        switch (i){
            case 1:return 7;
            case 2:return 1;
            case 3:return 2;
            case 4:return 3;
            case 5:return 4;
            case 6:return 5;
            case 7:return 6;
            default:return -1;
        }
    }
}

