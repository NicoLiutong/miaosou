package com.example.animation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.animation.R;
import com.example.animation.db.PictureSaveManager;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘通 on 2018/3/13.
 */

public class DownloadManagerPicture extends Fragment {

    private int type = 1;
    public List<PictureSaveManager> managerList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView textView;
    private ManagerPictureAdapter adapter;

    public static final DownloadManagerPicture downloadManagerPictureInstance(int type){
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        DownloadManagerPicture picture = new DownloadManagerPicture();
        picture.setArguments(bundle);
        return picture;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        type = bundle.getInt("type");
        if(type == 1){
            //DataSupport.deleteAll(PictureSaveManager.class);
            List<PictureSaveManager> list = DataSupport.where("type=?","1").find(PictureSaveManager.class);
            for(PictureSaveManager item:list){
                File file = new File(item.getFilePath());
                if(file.exists())
                managerList.add(item);
            }
        }else if(type == 2){
            List<PictureSaveManager> list = DataSupport.where("type=?","2").find(PictureSaveManager.class);
            for(PictureSaveManager item:list){
                File file = new File(item.getFilePath());
                if(file.exists())
                managerList.add(item);
            }
        }
        adapter = new ManagerPictureAdapter();
    }

    private void onClick(int position){
        String showPicturePath = managerList.get(position).getFilePath();
        Bundle bundle = new Bundle();
        bundle.putString("showPath",showPicturePath);
        bundle.putInt("type",type);
        DownloadManagerDialogFragment fragment = new DownloadManagerDialogFragment();
        fragment.setArguments(bundle);
        fragment.show(getChildFragmentManager(),"downloadShowPicture");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.animation_favourity,container,false);
        recyclerView =(RecyclerView) view.findViewById(R.id.animation_favourite_rv);
        textView = (TextView) view.findViewById(R.id.animation_favourite_tv);
        textView.setVisibility(View.INVISIBLE);
        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        if(managerList.isEmpty()) {
            textView.setText("主人还没有下载图片呦！！");
            textView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    class ManagerPictureAdapter extends RecyclerView.Adapter<ManagerPictureAdapter.managerPictureHolder>{

        @Override
        public managerPictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final managerPictureHolder holder = new managerPictureHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.animation_picture_item,parent,false));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadManagerPicture.this.onClick(holder.getAdapterPosition());
                }
            });
            return holder;
        }

        @Override
        public void onBindViewHolder(managerPictureHolder holder, int position) {
            //Log.d("filepath",managerList.get(position).getFilePath());
            Glide.with(DownloadManagerPicture.this).load(managerList.get(position).getFilePath()).into(holder.imageView);
        }

        @Override
        public int getItemCount() {
            return managerList.size();
        }

        class managerPictureHolder extends RecyclerView.ViewHolder{

            private ImageView imageView;
            public managerPictureHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.iv_animation_picture);
            }
        }
    }
}
