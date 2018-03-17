package com.example.animation.fragments;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.animation.R;
import com.example.animation.db.DownloadComic;
import com.example.animation.server.DownloadComicService;
import com.example.animation.view.HorizontalProgressBarWithNumber;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘通 on 2018/3/17.
 */

public class DownloadComicFragment extends Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    public static final int DOWNLOADCOMICPAUSE = 0;
    public static final int DOWNLOADCOMICANALYSIC = 1;
    public static final int DOWNLOADCOMICSTART = 2;
    public static final int DOWNLOADCOMICFINISH = 3;

    public static final String DownloadBroadcast = "com.miaosou.nico.download";

    private List<DownloadComic> downloadComicList = new ArrayList<>();
    private DownloadComicAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textView;
    private LinearLayout llDownloadComic;
    private CheckBox cbDownloadComicDeletAll;
    private TextView tvDownloadComicDelet;
    private TextView tvDownloadComicNeagtive;
    private ServiceConnection connection;
    private DownloadComicService mService;
    private DownloadBroadCast broadCast;
    private boolean canSee = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initList();
        adapter = new DownloadComicAdapter();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadBroadcast);
        broadCast = new DownloadBroadCast();
        getActivity().registerReceiver(broadCast,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadCast);
    }

    private void initList(){
        downloadComicList.clear();
        List<DownloadComic> list = DataSupport.findAll(DownloadComic.class);
        for(DownloadComic item:list){
            if(!item.isDownloadFinish()){
                downloadComicList.add(item);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comic_favourite,container,false);
        recyclerView =(RecyclerView) view.findViewById(R.id.my_favourityRecyclerview);
        llDownloadComic = (LinearLayout) view.findViewById(R.id.ll_download_comic_delet);
        cbDownloadComicDeletAll = (CheckBox) view.findViewById(R.id.cb_download_comic_choose_all);
        cbDownloadComicDeletAll.setOnCheckedChangeListener(this);
        tvDownloadComicDelet = (TextView) view.findViewById(R.id.tv_download_comic_delet);
        tvDownloadComicDelet.setOnClickListener(this);
        tvDownloadComicNeagtive = (TextView) view.findViewById(R.id.tv_download_comic_negative);
        tvDownloadComicNeagtive.setOnClickListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        textView = (TextView) view.findViewById(R.id.tv_myfavourity);
        if(downloadComicList.isEmpty()){
            textView.setText("还没有下载的漫画!!");
            textView.setVisibility(View.VISIBLE);
        }else {
            textView.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Intent intent = new Intent(getActivity(), DownloadComicService.class);
        getActivity().startService(intent);
        connection  = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadComicService.DownloadBinder binder = (DownloadComicService.DownloadBinder) service;
                mService = binder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }
        };
        getActivity().bindService(intent,connection, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();

        getActivity().unbindService(connection);
    }

    private void onItemLongClick(int position){
        downloadComicList.get(position).setSelect(true);
        canSee = true;
        llDownloadComic.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void onItemStart(int position){
        mService.start(downloadComicList.get(position));
    }

    private void onItemPause(int position){
        mService.pause(downloadComicList.get(position));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_download_comic_delet:
                canSee = false;
                List<Integer> index = new ArrayList<>();
                index.clear();
                for(int i = 0;i<downloadComicList.size();i++){
                    if(downloadComicList.get(i).isSelect()&&downloadComicList.get(i).getStatue()==DOWNLOADCOMICPAUSE){
                        index.add(i);
                        deletItem(downloadComicList.get(i));
                    }
                }

                for(int i:index){
                    downloadComicList.remove(i);
                }
                llDownloadComic.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_download_comic_negative:
                canSee = false;
                for(DownloadComic item:downloadComicList){
                    item.setSelect(false);
                }
                llDownloadComic.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    private void deletItem(DownloadComic item){
        String filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "miaosou" + File.separator + "comic" + File.separator + item.getComicId() + File.separator + item.getComicPagesId() + File.separator;
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
        DataSupport.deleteAll(DownloadComic.class,"comicPagesId=?",item.getComicPagesId());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for(DownloadComic item:downloadComicList){
            item.setSelect(isChecked);
        }
        adapter.notifyDataSetChanged();
    }

    class DownloadComicAdapter extends RecyclerView.Adapter<DownloadComicAdapter.DownloadComicHolder>{


        @Override
        public DownloadComicHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final DownloadComicHolder holder = new DownloadComicHolder(LayoutInflater.from(getActivity()).inflate(R.layout.download_finish_item,parent,false));
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClick(holder.getAdapterPosition());
                    return true;
                }
            });
            holder.ivDownloadItemStartPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.ivDownloadItemStartPause.isSelected()){
                        holder.ivDownloadItemStartPause.setSelected(false);
                        onItemPause(holder.getAdapterPosition());
                    }else {
                        holder.ivDownloadItemStartPause.setSelected(true);
                        onItemStart(holder.getAdapterPosition());
                    }
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(DownloadComicHolder holder, int position) {
            DownloadComic item = downloadComicList.get(position);
            if(item.isSelect()){
                holder.cbDownloadItemDelet.setChecked(true);
            }else {
                holder.cbDownloadItemDelet.setChecked(false);
            }

            if(canSee){
                holder.cbDownloadItemDelet.setVisibility(View.VISIBLE);
            }else {
                holder.cbDownloadItemDelet.setVisibility(View.GONE);
            }
            Glide.with(getActivity()).load(item.getComicImageUrl()).into(holder.ivDownloadItemPicture);
            holder.tvDownloadName.setText(item.getComicName());
            holder.tvDownloadPage.setText(item.getComicPages());

            switch (item.getStatue()){
                case DOWNLOADCOMICPAUSE:
                    holder.tvDownloadAllPages.setText(item.getCurrentPages()+"/"+item.getAllPages());
                    holder.progressBarDownloadItem.setVisibility(View.INVISIBLE);
                    holder.ivDownloadItemStartPause.setSelected(false);
                    break;
                case DOWNLOADCOMICSTART:
                    holder.tvDownloadAllPages.setText(item.getCurrentPages()+"/"+item.getAllPages());
                    holder.progressBarDownloadItem.setVisibility(View.VISIBLE);
                    holder.ivDownloadItemStartPause.setSelected(true);
                    break;
                case DOWNLOADCOMICANALYSIC:
                    holder.tvDownloadAllPages.setText("解析中...");
                    holder.progressBarDownloadItem.setVisibility(View.INVISIBLE);
                    holder.ivDownloadItemStartPause.setSelected(true);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return downloadComicList.size();
        }

        class DownloadComicHolder extends RecyclerView.ViewHolder{

            private CheckBox cbDownloadItemDelet;
            private ImageView ivDownloadItemPicture;
            private TextView tvDownloadName;
            private TextView tvDownloadPage;
            private TextView tvDownloadAllPages;
            private View itemView;
            private ImageView ivDownloadItemStartPause;
            private HorizontalProgressBarWithNumber progressBarDownloadItem;

            public DownloadComicHolder(View itemView) {
                super(itemView);
                cbDownloadItemDelet = (CheckBox) itemView.findViewById(R.id.cb_download_finish_item_delet);
                ivDownloadItemPicture = (ImageView) itemView.findViewById(R.id.iv_download_finish_item);
                tvDownloadName = (TextView) itemView.findViewById(R.id.tv_download_finish_item_name);
                tvDownloadPage = (TextView) itemView.findViewById(R.id.tv_download_finish_item_page);
                tvDownloadAllPages = (TextView) itemView.findViewById(R.id.tv_download_finish_item_allpages);
                ivDownloadItemStartPause = (ImageView) itemView.findViewById(R.id.iv_download_finish_item_start_pause);
                progressBarDownloadItem = (HorizontalProgressBarWithNumber) itemView.findViewById(R.id.progress_bar_download_finish_item);
                this.itemView = itemView;
            }
        }
    }

    public class DownloadBroadCast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(!intent.getBooleanExtra("downloadFinish",false)){
                for(DownloadComic item:downloadComicList){
                    if(item.getComicPagesId().equals(intent.getStringExtra("comicPagesId")));
                    item.setCurrentPages(intent.getIntExtra("currentPages",0));
                    item.setAllPages(intent.getIntExtra("allPages",0));
                    item.setStatue("statue",intent.getIntExtra("statue",1));
                    break;
                }
            }else {
                initList();
            }
            adapter.notifyDataSetChanged();
        }
    }
}
