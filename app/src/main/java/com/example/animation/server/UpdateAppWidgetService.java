package com.example.animation.server;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.example.animation.R;
import com.example.animation.activity.DownloadActivity;
import com.example.animation.db.AnimationItem;
import com.example.animation.view.HomeAnimationList;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateAppWidgetService extends Service {

    private Timer timer;
    private TimerTask timerTask;
    private int list = 1;
    public static List<AnimationItem> itemList = new ArrayList<>();
    public UpdateAppWidgetService() {
    }

    @Override
    public void onCreate() {
        //Log.d("creat","creat");
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                updateView();
            }
        };
        Date date = new Date();
        long updateTime = 60*60*1000;
        timer.schedule(timerTask,date.getTime(),updateTime);
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("onstartComand","onstartComand");
        if(intent.getStringExtra("type")!=null) {
            switch (intent.getStringExtra("type")) {
                case "refresh":
                    list = 1;
                    getNormalList();
                    updateView();
                    break;
                case "changelist":
                    if (list == 1) {
                        getFavoriteList();
                        updateList();
                        list = 0;
                        break;
                    } else {
                        getNormalList();
                        updateList();
                        list = 1;
                        break;
                    }
            }
        }else {
            list = 1;
            getNormalList();
            updateView();
        }
        return START_REDELIVER_INTENT;
    }

        private void updateView(){
            RemoteViews views = new RemoteViews(getPackageName(),R.layout.home_animation_list);
            int week = HomeAnimationList.getCalendarWeek();
            setWeekDay(week,views);
            setListData(views,getApplicationContext());
            Intent intent = new Intent(HomeAnimationList.CHANGE_LIST);
            intent.putExtra("type","refresh");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(UpdateAppWidgetService.this,0,intent,0);
            views.setOnClickPendingIntent(R.id.home_refresh,pendingIntent);
            intent.putExtra("type","changelist");
            PendingIntent changelist = PendingIntent.getBroadcast(UpdateAppWidgetService.this,1,intent,0);
            views.setOnClickPendingIntent(R.id.home_heart,changelist);
            AppWidgetManager manager = AppWidgetManager.getInstance(UpdateAppWidgetService.this);
            ComponentName componentName = new ComponentName(UpdateAppWidgetService.this,HomeAnimationList.class);
            manager.updateAppWidget(componentName,views);
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(componentName),R.id.home_list);
    }

    private void updateList(){
        AppWidgetManager manager = AppWidgetManager.getInstance(UpdateAppWidgetService.this);
        ComponentName componentName = new ComponentName(UpdateAppWidgetService.this,HomeAnimationList.class);
        manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(componentName),R.id.home_list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer = null;
    }

    private void setListData(RemoteViews views, Context context){
        Intent intent = new Intent(context, ListViewService.class);
        views.setRemoteAdapter(R.id.home_list,intent);
        views.setEmptyView(R.id.home_list,R.layout.home_empty_view);
        Intent intent1 = new Intent(context, DownloadActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,10,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.home_list,pendingIntent);
    }

    private void setWeekDay(int week,RemoteViews views){
        switch (week){
            case 1: views.setTextViewText(R.id.home_text,"月曜日");
                break;
            case 2: views.setTextViewText(R.id.home_text,"火曜日");
                break;
            case 3: views.setTextViewText(R.id.home_text,"水曜日");
                break;
            case 4: views.setTextViewText(R.id.home_text,"木曜日");
                break;
            case 5: views.setTextViewText(R.id.home_text,"金曜日");
                break;
            case 6: views.setTextViewText(R.id.home_text,"土曜日");
                break;
            case 7: views.setTextViewText(R.id.home_text,"日曜日");
                break;
            default:
                break;
        }
    }

    private void getNormalList(){
        int week = HomeAnimationList.getCalendarWeek();
        //Log.d("22","22");
        //Log.d("week11",week+"");
        List<AnimationItem> list = DataSupport.where("week=?","week"+week).find(AnimationItem.class);
        itemList.clear();
        for(AnimationItem item:list){
            itemList.add(item);
        }
    }

    private void getFavoriteList(){
        int week = HomeAnimationList.getCalendarWeek();
        //Log.d("22","22");
        //Log.d("week11",week+"");
        List<AnimationItem> list = DataSupport.where("week=?","week"+week).find(AnimationItem.class);
        itemList.clear();
        for(AnimationItem item:list){
            if(item.isFavortiy())
            itemList.add(item);
        }
    }

}
