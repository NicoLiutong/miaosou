package com.example.animation.Util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by 刘通 on 2017/12/20.
 */

public class SavePicture {
    public static boolean savePicture(String filePath, Bitmap bitmap, String fileName, Activity activity){
        boolean flag = false;
        File dirFile = new File(filePath);
        if(!dirFile.exists()){
            dirFile.mkdirs();
        }
        File file = new File(dirFile,fileName + ".jpg");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            flag = true;
            outputStream.flush();
            outputStream.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        saveToContentProvider(file,activity);
        return flag;
    }

    private static void saveToContentProvider(File file,Activity activity){
        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(activity.getContentResolver(), file.getAbsolutePath(),file.getName(), null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
    }
}
