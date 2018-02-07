package com.example.animation.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.animation.R;
import com.example.animation.Util.ACache;
import com.example.animation.Util.ImageLoader;
import com.example.animation.db.CosplayImageMessage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.bmob.v3.okhttp3.Call;
import cn.bmob.v3.okhttp3.OkHttpClient;
import cn.bmob.v3.okhttp3.Request;
import cn.bmob.v3.okhttp3.Response;

/**
 * Created by 刘通 on 2017/12/29.
 */

public class MyScorllView extends ScrollView implements View.OnTouchListener {

    /**
     * 图片地址
     */
    private List<CosplayImageMessage> cosplayImageMessages = new ArrayList<>();
    /**
     * 每一列的宽度
     */
    private int columnWidth;

    /**
     * 当前第一列的高度
     */
    private int firstColumnHeight;

    /**
     * 当前第二列的高度
     */
    private int secondColumnHeight;

    /**
     * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
     */
    private boolean loadOnce;

    /**
     * 对图片进行管理的工具类
     */
    private ImageLoader imageLoader;

    /**
     * 第一列的布局
     */
    private LinearLayout firstColumn;

    /**
     * 第二列的布局
     */
    private LinearLayout secondColumn;

    /**
     * 记录上垂直方向的滚动距离。
     */
    private static int lastScrollY = -1;

    /**
     * 记录所有正在下载或等待下载的任务。
     */
    private static Set<LoadImageTask> taskCollection;

    /**
     * MyScrollView下的直接子布局。
     */
    private static View scrollLayout;

    /**
     * MyScrollView布局的高度。
     */
    private static int scrollViewHeight;

    private AlertDialog dialog = null;
    private TextView tvLoadingNumber = null;
    private int number = 1;

    /**
     * 记录所有界面上的图片，用以可以随时控制对图片的释放。
     */
    private List<ImageView> imageViewList = new ArrayList<ImageView>();

    private ACache mCache;

    private OnClick click;
    /**
     * 在Handler中进行图片可见性检查的判断，以及加载更多图片的操作。
     */
    private static Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            MyScorllView myScrollView = (MyScorllView) msg.obj;
            int scrollY = myScrollView.getScrollY();
            // 如果当前的滚动位置和上次相同，表示已停止滚动
            if (scrollY == lastScrollY) {
                myScrollView.checkVisibility();
            } else {
                lastScrollY = scrollY;
                Message message = new Message();
                message.obj = myScrollView;
                // 5毫秒后再次对滚动位置进行判断
                handler.sendMessageDelayed(message, 5);
            }
        };

    };

    /**
     * MyScrollView的构造函数。
     *
     * @param context
     * @param attrs
     */
    public MyScorllView(Context context, AttributeSet attrs) {
        super(context, attrs);
        imageLoader = ImageLoader.getInstance();
        taskCollection = new HashSet<LoadImageTask>();
        setOnTouchListener(this);
    }

    public void setView(AlertDialog dialog, TextView tvLoadingNumber){
        this.dialog = dialog;
        this.tvLoadingNumber = tvLoadingNumber;
    }

    public void setmAcache(ACache acache){
        mCache = acache;
    }

    public void closeAsyncTask(){
        if(taskCollection != null){
            if(taskCollection.size() > 0){
                for (LoadImageTask tast:taskCollection){
                    tast.cancel(true);
                }
            }
        }
    }

    public void addImageUrl(List<CosplayImageMessage> cosplayImageMessages){
        this.cosplayImageMessages = cosplayImageMessages;
        loadMoreImages();
    }

    public void setOnClickListener(OnClick clickListener){
        click = clickListener;
    }


    /**
     * 进行一些关键性的初始化操作，获取MyScrollView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的图片。
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && !loadOnce) {
            scrollViewHeight = getHeight();
            scrollLayout = getChildAt(0);
            firstColumn = (LinearLayout) findViewById(R.id.first_column);
            secondColumn = (LinearLayout) findViewById(R.id.second_column);
            columnWidth = firstColumn.getWidth();
            loadOnce = true;
        }
    }

    /**
     * 监听用户的触屏事件，如果用户手指离开屏幕则开始进行滚动检测。
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Message message = new Message();
            message.obj = this;
            handler.sendMessageDelayed(message, 5);
        }
        return false;
    }

    /**
     * 开始加载下一页的图片，每张图片都会开启一个异步线程去下载。
     */
    public void loadMoreImages() {
        for (int i = 0; i < cosplayImageMessages.size(); i++) {
            LoadImageTask task = new LoadImageTask(cosplayImageMessages.get(i).getImageId());
            taskCollection.add(task);
            task.execute(cosplayImageMessages.get(i).getImageUrl());
        }
    }

    /**
     * 遍历imageViewList中的每张图片，对图片的可见性进行检查，如果图片已经离开屏幕可见范围，则将图片替换成一张空图。
     */
    public void checkVisibility() {
        for (int i = 0; i < imageViewList.size(); i++) {
            ImageView imageView = imageViewList.get(i);
            int borderTop = (Integer) imageView.getTag(R.string.border_top);
            int borderBottom = (Integer) imageView.getTag(R.string.border_bottom);
            if (borderBottom > getScrollY()
                    && borderTop < getScrollY() + scrollViewHeight) {
                String imageId = (String) imageView.getTag(R.string.image_id);
                String imageUrl = (String) imageView.getTag(R.string.image_url);
                Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(mCache,imageId);
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    LoadImageTask task = new LoadImageTask(imageView,imageId);
                    task.execute(imageUrl);
                }
            } else {
                imageView.setImageResource(R.drawable.ic_girl);
            }
        }
    }

    /**
     * 异步下载图片的任务。
     *
     * @author guolin
     */
    class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        /**
         * 图片的URL地址
         */
        private String mImageUrl;

        /**
         * 图片id
         */
        private String mImageId;
        /**
         * 可重复使用的ImageView
         */
        private ImageView mImageView;

        public LoadImageTask(String imageId) {
            mImageId = imageId;
        }

        /**
         * 将可重复使用的ImageView传入
         *
         * @param imageView
         */
        public LoadImageTask(ImageView imageView,String imageId) {
            mImageView = imageView;
            mImageId = imageId;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            mImageUrl = params[0];
            Bitmap imageBitmap = imageLoader.getBitmapFromMemoryCache(mCache,mImageId);
            Bitmap bagImageBitmap = imageLoader.getBitmapFromMemoryCache(mCache,"bagBitmap" + mImageId);
            if (imageBitmap == null || bagImageBitmap == null) {
                imageBitmap = loadImage(mImageUrl,mImageId,isCancelled());
            }
            if(bagImageBitmap != null)
            bagImageBitmap.recycle();
            return imageBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                double ratio = bitmap.getWidth() / (columnWidth * 1.0);
                int scaledHeight = (int) (bitmap.getHeight() / ratio);
                addImage(bitmap, columnWidth, scaledHeight);
            }
            taskCollection.remove(this);
        }

        /**
         * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
         *
         * @param imageUrl
         *            图片的URL地址
         * @return 加载到内存的图片。
         */
        private Bitmap loadImage(String imageUrl,String imageId,boolean isCancellect) {
            return downloadImage(imageUrl,imageId,isCancellect);
        }

        /**
         * 向ImageView中添加一张图片
         *
         * @param bitmap
         *            待添加的图片
         * @param imageWidth
         *            图片的宽度
         * @param imageHeight
         *            图片的高度
         */
        private void addImage(Bitmap bitmap, int imageWidth, int imageHeight) {
            if(dialog != null && mImageId.equals(cosplayImageMessages.get(cosplayImageMessages.size()-1).getImageId()))
                dialog.dismiss();
            if (tvLoadingNumber != null)
                tvLoadingNumber.setText(number + "/" + cosplayImageMessages.size());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageWidth, imageHeight);
            if (mImageView != null) {
                mImageView.setImageBitmap(bitmap);
            } else {
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(params);
                imageView.setImageBitmap(bitmap);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setPadding(5, 5, 5, 5);
                imageView.setTag(R.string.image_id, mImageId);
                imageView.setTag(R.string.image_url, mImageUrl);
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(click != null){
                            click.OnclickListener(mImageId);
                        }
                    }
                });
                findColumnToAdd(imageView, imageHeight).addView(imageView);
                imageViewList.add(imageView);
            }
            number++;


        }

        /**
         * 找到此时应该添加图片的一列。原则就是对三列的高度进行判断，当前高度最小的一列就是应该添加的一列。
         *
         * @param imageView
         * @param imageHeight
         * @return 应该添加图片的一列
         */
        private LinearLayout findColumnToAdd(ImageView imageView,int imageHeight) {
            if (firstColumnHeight <= secondColumnHeight) {
                    imageView.setTag(R.string.border_top, firstColumnHeight);
                    firstColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, firstColumnHeight);
                    return firstColumn;
                } else {
                    imageView.setTag(R.string.border_top, secondColumnHeight);
                    secondColumnHeight += imageHeight;
                    imageView.setTag(R.string.border_bottom, secondColumnHeight);
                    return secondColumn;
                }
            }
        }

        /**
         * 将图片下载到SD卡缓存起来。
         *
         * @param imageUrl
         *            图片的URL地址。
         */
        private Bitmap downloadImage(String imageUrl,String imageId,boolean isCancellet) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(imageUrl).build();
            Call call = null;
            Response response;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            InputStream is = null;
            int length;
            try {
                call = client.newCall(request);
                response = call.execute();
                is = response.body().byteStream();
                byte[] bs = new byte[50];
                while ((length = is.read(bs)) != -1){
                    if(isCancellet){
                        return null;
                    }
                    baos.write(bs,0,length);
                }
                baos.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    if ( is != null) {
                        is.close();
                    }
                    if (baos != null) {
                        baos.close();
                    }
                    if (call != null) {
                        call.cancel();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Bitmap smallBitmap = ImageLoader.decodeSampledBitmapFromResource(baos.toByteArray(),columnWidth);
            Bitmap bagBitmap = ImageLoader.decodeSampledBitmapFromResource(baos.toByteArray(),columnWidth * 2);
                    //BitmapFactory.decodeByteArray(baos.toByteArray(),0,baos.size());
            if(bagBitmap != null){
                imageLoader.addBitmapToMemoryCache(mCache,"bagBitmap" + imageId,bagBitmap);
            }
            if(smallBitmap != null){
            imageLoader.addBitmapToMemoryCache(mCache,imageId,smallBitmap);
                return smallBitmap;
            }
            return null;
        }

    public interface OnClick{
        void OnclickListener(String imageId);
    }
}
