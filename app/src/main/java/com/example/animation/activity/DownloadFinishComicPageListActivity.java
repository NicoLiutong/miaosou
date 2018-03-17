package com.example.animation.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.example.animation.fragments.DownloadManagerDialogFragment;
import com.example.animation.view.HorizontalProgressBarWithNumber;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadFinishComicPageListActivity extends AppCompatActivity implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {

    private Toolbar toolbarDownloadFinish;
    private List<DownloadComic> downloadComics = new ArrayList<>();
    private String toolbarName = "";
    private RecyclerView rvDownloadFinish;
    private LinearLayout llDownloadFinish;
    private CheckBox cbDownloadSelectAll;
    private TextView tvDownloadDelet;
    private TextView tvDownloadNegative;
    private DownloadFinishComicPageListAdapter adapter;
    private boolean canSeeCheckBox = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_finish_comic_page_list);
        toolbarDownloadFinish = (Toolbar) findViewById(R.id.download_finish_toolbar);
        rvDownloadFinish = (RecyclerView) findViewById(R.id.rv_download_finish);
        llDownloadFinish = (LinearLayout) findViewById(R.id.ll_download_finish_check_is_delet);
        llDownloadFinish.setVisibility(View.INVISIBLE);
        cbDownloadSelectAll = (CheckBox) findViewById(R.id.cb_download_choose_all);
        cbDownloadSelectAll.setOnCheckedChangeListener(this);
        tvDownloadDelet = (TextView) findViewById(R.id.tv_download_finish_delet);
        tvDownloadDelet.setOnClickListener(this);
        tvDownloadNegative = (TextView) findViewById(R.id.tv_download_finish_negative);
        tvDownloadNegative.setOnClickListener(this);
        initData(getIntent().getStringExtra("comicId"));
        toolbarSet(toolbarName);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDownloadFinish.setLayoutManager(manager);
        adapter = new DownloadFinishComicPageListAdapter();
        rvDownloadFinish.setAdapter(adapter);


    }

    private void initData(String comicId){
        downloadComics.clear();
        List<DownloadComic> list = DataSupport.where("comicId=?",comicId).order("comicPages asc").find(DownloadComic.class);
        for(DownloadComic item:list){
            if(item.isDownloadFinish()){
                downloadComics.add(item);
                toolbarName = item.getComicName();
            }
        }
    }

    private void toolbarSet(String name){
        toolbarDownloadFinish.setTitle(name);
        toolbarDownloadFinish.setTitleTextColor(ContextCompat.getColor(this,R.color.white));
        setSupportActionBar(toolbarDownloadFinish);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_download_finish_delet:
                canSeeCheckBox = false;
                List<Integer> index = new ArrayList<>();
                index.clear();
                for(int i = 0;i<downloadComics.size();i++){
                    if(downloadComics.get(i).isSelect()){
                        deletItem(downloadComics.get(i));
                        index.add(i);
                    }
                }
                for (int i:index){
                    downloadComics.remove(i);
                }
                llDownloadFinish.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
                break;
            case R.id.tv_download_finish_negative:
                canSeeCheckBox = false;
                for (DownloadComic item : downloadComics) {
                    item.setSelect(false);
                }
                llDownloadFinish.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            for (DownloadComic item : downloadComics) {
                item.setSelect(true);
            }
        }else {
            for (DownloadComic item : downloadComics) {
                item.setSelect(false);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void onItemClick(DownloadComic downloadComic){
        DownloadManagerDialogFragment fragment = new DownloadManagerDialogFragment();
        Bundle bundle = new Bundle();
        String filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "miaosou" + File.separator + "comic" + File.separator + downloadComic.getComicId() + File.separator + downloadComic.getComicPagesId() + File.separator;
        bundle.putString("showPath",filePath);
        bundle.putInt("type",3);
        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(),"downloadFinish");
    }

    private void onItemLongClick(int position){
        llDownloadFinish.setVisibility(View.VISIBLE);
        canSeeCheckBox = true;
        downloadComics.get(position).setSelect(true);
        adapter.notifyDataSetChanged();
    }

    private void onItemCheckedChanged(int position,boolean isChecked){
      downloadComics.get(position).setSelect(isChecked);
    }

    private void deletItem(DownloadComic item){
        String filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "miaosou" + File.separator + "comic" + File.separator + item.getComicId() + File.separator + item.getComicPagesId() + File.separator;
        File file = new File(filePath);
        if(file.exists()){
            file.delete();
        }
        DataSupport.deleteAll(DownloadComic.class,"comicPagesId=?",item.getComicPagesId());
    }

    class DownloadFinishComicPageListAdapter extends RecyclerView.Adapter<DownloadFinishComicPageListAdapter.DownloadComicPageListHolder>{

        @Override
        public DownloadComicPageListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final DownloadComicPageListHolder holder = new DownloadComicPageListHolder(LayoutInflater.from(DownloadFinishComicPageListActivity.this).inflate(R.layout.download_finish_item,parent,false));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(downloadComics.get(holder.getAdapterPosition()));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemLongClick(holder.getAdapterPosition());
                    return true;
                }
            });

            holder.cbDownloadItemDelet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    onItemCheckedChanged(holder.getAdapterPosition(),isChecked);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(DownloadComicPageListHolder holder, int position) {
            Glide.with(DownloadFinishComicPageListActivity.this).load(downloadComics.get(position).getComicImageUrl()).into(holder.ivDownloadItemPicture);
            holder.tvDownloadName.setText(downloadComics.get(position).getComicName());
            holder.tvDownloadPage.setText(downloadComics.get(position).getComicPages());
            holder.tvDownloadAllPages.setText(downloadComics.get(position).getAllPages()+"P");
            holder.ivDownloadItemStartPause.setVisibility(View.GONE);
            holder.progressBarDownloadItem.setVisibility(View.GONE);
            if(downloadComics.get(position).isSelect()){
                holder.cbDownloadItemDelet.setChecked(true);
            }else {
                holder.cbDownloadItemDelet.setChecked(false);
            }
            if(canSeeCheckBox){
                holder.cbDownloadItemDelet.setVisibility(View.VISIBLE);
            }else {
                holder.cbDownloadItemDelet.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return downloadComics.size();
        }

        class DownloadComicPageListHolder extends RecyclerView.ViewHolder{

            private CheckBox cbDownloadItemDelet;
            private ImageView ivDownloadItemPicture;
            private TextView tvDownloadName;
            private TextView tvDownloadPage;
            private TextView tvDownloadAllPages;
            private View itemView;
            private ImageView ivDownloadItemStartPause;
            private HorizontalProgressBarWithNumber progressBarDownloadItem;

            public DownloadComicPageListHolder(View itemView) {
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
}
