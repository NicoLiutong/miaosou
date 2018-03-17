package com.example.animation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.animation.R;

import java.io.File;
import java.util.List;

/**
 * Created by 刘通 on 2018/3/12.
 */

public class SelectFileAdapter extends RecyclerView.Adapter<SelectFileAdapter.SelectFileHolder> {

    private List<File> mList;
    private SelectFileOnClick mSelectFileOnClick;
    private Context mContext;

    public SelectFileAdapter(List<File> list,SelectFileOnClick onClick){
        mList = list;
        mSelectFileOnClick = onClick;
    }

    @Override
    public SelectFileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        SelectFileHolder holder = new SelectFileHolder(LayoutInflater.from(mContext).inflate(R.layout.select_file_item,parent,false));
        return holder;
    }

    @Override
    public void onBindViewHolder(SelectFileHolder holder, final int position) {
        holder.tvSelectFileItem.setText(mList.get(position).getName());
        holder.tvSelectFileItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectFileOnClick.onClickListener(position,mList.get(position).getAbsolutePath());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SelectFileHolder extends RecyclerView.ViewHolder{

        private TextView tvSelectFileItem;
        public SelectFileHolder(View itemView) {
            super(itemView);
            tvSelectFileItem =(TextView) itemView.findViewById(R.id.tv_select_file_item);
        }
    }

    public interface SelectFileOnClick{
        void onClickListener(int position,String absolutePath);
    }
}
