package com.example.animation.server;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.animation.R;
import com.example.animation.activity.DownloadActivity;
import com.example.animation.db.AnimationItem;
import com.example.animation.fragments.AnimationFragment;

import java.util.List;

/**
 * Created by 刘通 on 2018/2/28.
 */

public class ListViewService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewFactory(this.getApplicationContext(),intent);
    }

    public static class ListRemoteViewFactory implements RemoteViewsFactory{

        private Context context;
        private List<AnimationItem> itemList = null;
        public ListRemoteViewFactory(Context context,Intent intent){
            this.context = context;
            //Log.d("11","11");
        }
        @Override
        public void onCreate() {
        }

        @Override
        public void onDataSetChanged() {
            if(UpdateAppWidgetService.itemList!=null)
            itemList = UpdateAppWidgetService.itemList;
        }

        @Override
        public void onDestroy() {
            itemList.clear();
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.home_list_item);
            views.setTextViewText(R.id.home_list_item,itemList.get(position).getAnimationItem());
            Intent intent = new Intent(context, DownloadActivity.class);
            intent.putExtra(AnimationFragment.ANIMATION_NAME,itemList.get(position).getAnimationItem());
            intent.putExtra(AnimationFragment.ANIMATION_URL,itemList.get(position).getDownloadUrl());
            views.setOnClickFillInIntent(R.id.home_list_item,intent);
            return views;
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
            return false;
        }
    }

}
