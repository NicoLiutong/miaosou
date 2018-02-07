package com.example.animation.Util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

import com.example.animation.db.CosplayImageMessage;

import java.util.List;

/**
 * Created by 刘通 on 2017/12/29.
 */

public class ImageLoader {
    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private static LruCache<String, Bitmap> mMemoryCache;

    private static List<CosplayImageMessage> cosplayImageMessages;
    /**
     * ImageLoader的实例。
     */
    private static ImageLoader mImageLoader;

    private ImageLoader() {

    }

    /**
     * 获取ImageLoader的实例。
     *
     * @return ImageLoader的实例。
     */
    public static ImageLoader getInstance() {
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader();
        }
        return mImageLoader;
    }

    public static void setCosplayImageMessages(List<CosplayImageMessage> messages){
        cosplayImageMessages = messages;
    }

    public static List<CosplayImageMessage> getCosplayImageMessages(){
        return cosplayImageMessages;
    }

    public static void clearCosplayImageMessages(){
        cosplayImageMessages = null;
    }

    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @param bitmap
     *            LruCache的键，这里传入从网络上下载的Bitmap对象。
     */
    public void addBitmapToMemoryCache(ACache aCache, String key, Bitmap bitmap) {
        if (aCache.getAsBitmap(key) == null) {
            aCache.put(key, bitmap);
        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的Bitmap对象，或者null。
     */
    public Bitmap getBitmapFromMemoryCache(ACache aCache,String key) {
        return aCache.getAsBitmap(key);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth) {
        // 源图片的宽度
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (width > reqWidth) {
            // 计算出实际宽度和目标宽度的比率
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(byte[] byteArray, int reqWidth) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(byteArray,0,byteArray.length,options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(byteArray,0,byteArray.length,options);
    }
}
