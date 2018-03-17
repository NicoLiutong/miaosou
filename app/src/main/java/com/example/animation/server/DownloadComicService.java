package com.example.animation.server;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.animation.Util.DownloadComicManager;
import com.example.animation.db.DownloadComic;
import com.example.animation.fragments.DownloadComicFragment;

import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DownloadComicService extends Service implements DownloadComicManager {

    public static final String TAG = "downloadComic";
    private Map<String,DownloadComicAsyncTask> taskMap = new HashMap<>();
    private DownloadBinder binder = new DownloadBinder();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Iterator iterator = taskMap.entrySet().iterator();
        while (iterator.hasNext()){
            DownloadComicAsyncTask task =(DownloadComicAsyncTask) iterator.next();
            task.cancel(true);
        }
        taskMap.clear();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    @Override
    public void onAnalysic(DownloadComic downloadComic) {
        ContentValues values = new ContentValues();
        values.put("currentPages",downloadComic.getCurrentPages());
        values.put("allPages",downloadComic.getAllPages());
        values.put("statue",downloadComic.getStatue());
        DataSupport.updateAll(DownloadComic.class,values,"comicPagesId = ?",downloadComic.getComicPagesId());

        Intent intent = new Intent(this, DownloadComicFragment.DownloadBroadCast.class);
        intent.putExtra("comicPagesId",downloadComic.getComicPagesId());
        intent.putExtra("currentPages",downloadComic.getCurrentPages());
        intent.putExtra("allPages",downloadComic.getAllPages());
        intent.putExtra("statue",downloadComic.getStatue());
        intent.putExtra("downloadFinish",false);
        this.sendBroadcast(intent);
    }

    @Override
    public void onStart(DownloadComic downloadComic) {
        ContentValues values = new ContentValues();
        values.put("currentPages",downloadComic.getCurrentPages());
        values.put("allPages",downloadComic.getAllPages());
        values.put("statue",downloadComic.getStatue());
        DataSupport.updateAll(DownloadComic.class,values,"comicPagesId = ?",downloadComic.getComicPagesId());

        Intent intent = new Intent(this, DownloadComicFragment.DownloadBroadCast.class);
        intent.putExtra("comicPagesId",downloadComic.getComicPagesId());
        intent.putExtra("currentPages",downloadComic.getCurrentPages());
        intent.putExtra("allPages",downloadComic.getAllPages());
        intent.putExtra("statue",downloadComic.getStatue());
        intent.putExtra("downloadFinish",false);
        this.sendBroadcast(intent);
    }

    @Override
    public void onPause(DownloadComic downloadComic) {
        ContentValues values = new ContentValues();
        values.put("statue",downloadComic.getStatue());
        values.put("currentPages",downloadComic.getCurrentPages());
        values.put("allPages",downloadComic.getAllPages());
        DataSupport.updateAll(DownloadComic.class,values,"comicPagesId = ?",downloadComic.getComicPagesId());

        Intent intent = new Intent(this, DownloadComicFragment.DownloadBroadCast.class);
        intent.putExtra("comicPagesId",downloadComic.getComicPagesId());
        intent.putExtra("currentPages",downloadComic.getCurrentPages());
        intent.putExtra("allPages",downloadComic.getAllPages());
        intent.putExtra("statue",downloadComic.getStatue());
        intent.putExtra("downloadFinish",false);
        this.sendBroadcast(intent);

    }

    @Override
    public void onFinish(DownloadComic downloadComic) {
        ContentValues values = new ContentValues();
        values.put("currentPages",downloadComic.getCurrentPages());
        values.put("allPages",downloadComic.getAllPages());
        values.put("statue",downloadComic.getStatue());
        values.put("downloadFinish",true);
        DataSupport.updateAll(DownloadComic.class,values,"comicPagesId = ?",downloadComic.getComicPagesId());

        Intent intent = new Intent(this, DownloadComicFragment.DownloadBroadCast.class);
        intent.putExtra("comicPagesId",downloadComic.getComicPagesId());
        intent.putExtra("currentPages",downloadComic.getCurrentPages());
        intent.putExtra("allPages",downloadComic.getAllPages());
        intent.putExtra("statue",downloadComic.getStatue());
        intent.putExtra("downloadFinish",true);
        this.sendBroadcast(intent);

    }

    public void start(DownloadComic item){
        DownloadComicAsyncTask task = new DownloadComicAsyncTask(this);
        if(!taskMap.containsKey(item.getComicPagesId())) {
            taskMap.put(item.getComicPagesId(), task);
            Log.d("onServiceStart", item.getComicPagesId());
            //task.execute(item);
        }
    }

    public void pause(DownloadComic item){
        if(taskMap.containsKey(item.getComicPagesId())) {
            DownloadComicAsyncTask task = taskMap.get(item.getComicPagesId());
            task.cancel(true);
            taskMap.remove(item.getComicPagesId());
            Log.d("onServicePause",item.getComicPagesId());
        }
    }


    public class DownloadBinder extends Binder{
        public DownloadComicService getService(){
            return DownloadComicService.this;
        }
    }

    class DownloadComicAsyncTask extends AsyncTask<DownloadComic,DownloadComic,DownloadComic>{

        private DownloadComicManager manager;
        public DownloadComicAsyncTask(DownloadComicManager manager){
            this.manager = manager;
        }
        @Override
        protected DownloadComic doInBackground(DownloadComic... params) {
            return null;
        }

        @Override
        protected void onProgressUpdate(DownloadComic... values) {
            if(values[0].getStatue()== DownloadComicFragment.DOWNLOADCOMICANALYSIC){
                manager.onAnalysic(values[0]);
            }else if (values[0].getStatue() == DownloadComicFragment.DOWNLOADCOMICSTART){
                manager.onStart(values[0]);
            }else {
                manager.onPause(values[0]);
            }
        }

        @Override
        protected void onPostExecute(DownloadComic downloadComic) {
            super.onPostExecute(downloadComic);
            manager.onFinish(downloadComic);
        }

    }
}
