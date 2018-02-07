package com.example.animation.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import cn.bmob.v3.okhttp3.Call;
import cn.bmob.v3.okhttp3.OkHttpClient;
import cn.bmob.v3.okhttp3.Request;
import cn.bmob.v3.okhttp3.Response;

/**
 * Created by 刘通 on 2017/11/7.
 */

public class LoadImageAsync extends AsyncTask<String, Integer, Bitmap> {

    private OnLoadImageListener loadImageListener;

    public LoadImageAsync(OnLoadImageListener loadImageListener){
        this.loadImageListener = loadImageListener;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loadImageListener.onStartLoading();
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Request request = new Request.Builder().url(params[0]).build();
        Call call = null;
        Response response;
        try {
            OkHttpClient client = new OkHttpClient();
            call = client.newCall(request);
            response = call.execute();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is;
            int length;
            int progress = 0;
            is = response.body().byteStream();
            long count = response.body().contentLength();
            byte[] bs = new byte[50];
            while ((length = is.read(bs)) != -1){
                progress += length;
                if(count == -1){
                    publishProgress(-1);
                }else {
                    publishProgress((int) ((float) progress/count * 100));
                }
                if (isCancelled()){//如果取消了任务 就不执行
                    return null;
                }
                baos.write(bs,0,length);
            }
            is.close();
            baos.close();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(baos.toByteArray(),0,baos.size(),options);
            if(params[0].substring(8,17).equals("cdn.anime")){
                options.inSampleSize = 1;
            }else {
                options.inSampleSize = 2;
            }
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(baos.toByteArray(),0,baos.size());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            call.cancel();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap != null){
            loadImageListener.onSuccess(bitmap);
        }else {
            loadImageListener.onFailed();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        loadImageListener.onProgressUpdate(values[0]);
    }

    public interface OnLoadImageListener{
        void onStartLoading();
        void onProgressUpdate(Integer progress);
        void onSuccess(Bitmap bitmap);
        void onFailed();
    }
}
