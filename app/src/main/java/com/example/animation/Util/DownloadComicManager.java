package com.example.animation.Util;

import com.example.animation.db.DownloadComic;

/**
 * Created by 刘通 on 2018/3/17.
 */

public interface DownloadComicManager {

    void onAnalysic(DownloadComic downloadComic);
    void onStart(DownloadComic downloadComic);
    void onPause(DownloadComic downloadComic);
    void onFinish(DownloadComic downloadComic);


}
