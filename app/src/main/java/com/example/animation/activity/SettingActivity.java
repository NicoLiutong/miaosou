package com.example.animation.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import com.example.animation.R;

import java.io.File;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getFragmentManager().beginTransaction().replace(R.id.ll_fragment_setting,new SettingFragment()).commit();
    }

    public static class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

        private Preference backgroundFilepath;
        private Preference backgroundSaveFilepath;
        private Preference animationPictureFilepath;
        private Preference cosplayPictureFilepath;
        private PreferenceManager manager;
        private SharedPreferences preferences;
        private String backgroundsavepath = Environment.getExternalStorageDirectory().toString() + File.separator;
        private String animationpicturepath = Environment.getExternalStorageDirectory().toString() + File.separator;
        private String cosplaypicturepath = Environment.getExternalStorageDirectory().toString() + File.separator;



        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_setting);
            manager = getPreferenceManager();
            preferences = manager.getSharedPreferences();
            backgroundFilepath = findPreference("backgroundFilepath");
            backgroundFilepath.setOnPreferenceClickListener(this);
            backgroundSaveFilepath = findPreference("backgroundSaveFilepath");
            backgroundSaveFilepath.setOnPreferenceClickListener(this);
            animationPictureFilepath = findPreference("animationPictureFilepath");
            animationPictureFilepath.setOnPreferenceClickListener(this);
            cosplayPictureFilepath = findPreference("cosplayPictureFilepath");
            cosplayPictureFilepath.setOnPreferenceClickListener(this);
            initSummary();
        }

        private void initSummary(){

            SharedPreferences.Editor editor = preferences.edit();
            backgroundFilepath.setSummary(preferences.getString("backgroundFilepath",""));
            backgroundsavepath = Environment.getExternalStorageDirectory().toString() + File.separator + "miaosou" + File.separator + "backgroundpicture" + File.separator;
            animationpicturepath = Environment.getExternalStorageDirectory().toString() + File.separator + "miaosou" + File.separator + "picture" + File.separator;
            cosplaypicturepath = Environment.getExternalStorageDirectory().toString() + File.separator + "miaosou" + File.separator + "cosplay" + File.separator;
            backgroundSaveFilepath.setSummary(preferences.getString("backgroundSaveFilepath",backgroundsavepath));
            animationPictureFilepath.setSummary(preferences.getString("animationPictureFilepath",animationpicturepath));
            cosplayPictureFilepath.setSummary(preferences.getString("cosplayPictureFilepath",cosplaypicturepath));
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if(preference == backgroundFilepath){
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                //intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,0);
                return true;
            }else if(preference == backgroundSaveFilepath){
                SharedPreferences preferences = backgroundSaveFilepath.getSharedPreferences();
                Intent intent = new Intent(getActivity(),FilePathSelectActivity.class);
                intent.putExtra("currentFilePath",preferences.getString("backgroundSaveFilepath",backgroundsavepath));
                this.startActivityForResult(intent,100);
                return true;
            }else if(preference == animationPictureFilepath){
                SharedPreferences preferences = backgroundSaveFilepath.getSharedPreferences();
                Intent intent = new Intent(getActivity(),FilePathSelectActivity.class);
                intent.putExtra("currentFilePath",preferences.getString("animationPictureFilepath",animationpicturepath));
                this.startActivityForResult(intent,101);
                return true;
            }else if(preference == cosplayPictureFilepath){
                SharedPreferences preferences = backgroundSaveFilepath.getSharedPreferences();
                Intent intent = new Intent(getActivity(),FilePathSelectActivity.class);
                intent.putExtra("currentFilePath",preferences.getString("cosplayPictureFilepath",cosplaypicturepath));
                this.startActivityForResult(intent,102);
                return true;
            }
            return false;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode){
                case 0:
                    if(resultCode == RESULT_OK){
                        Uri imageUri = data.getData();

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getActivity().getContentResolver().query(imageUri,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String s = cursor.getString(columnIndex);
                        cursor.close();

                        backgroundFilepath.setSummary(s);
                        SharedPreferences.Editor editor = backgroundFilepath.getEditor();
                        editor.putString("backgroundFilepath",s);
                        editor.commit();
                    }
                    break;
                case 100:
                    if(resultCode == RESULT_OK) {
                        backgroundSaveFilepath.setSummary(data.getStringExtra("filePath"));
                        //Log.d("filepath1",data.getStringExtra("filePath"));
                        SharedPreferences.Editor editor = backgroundSaveFilepath.getEditor();
                        editor.putString("backgroundSaveFilepath", data.getStringExtra("filePath"));
                        editor.commit();
                    }
                    break;
                case 101:
                    if(resultCode == RESULT_OK) {
                        animationPictureFilepath.setSummary(data.getStringExtra("filePath"));
                        //Log.d("filepath2",data.getStringExtra("filePath"));
                        SharedPreferences.Editor editor = animationPictureFilepath.getEditor();
                        editor.putString("animationPictureFilepath", data.getStringExtra("filePath"));
                        editor.commit();
                    }
                    break;
                case 102:
                    if(resultCode == RESULT_OK) {
                        cosplayPictureFilepath.setSummary(data.getStringExtra("filePath"));
                        //Log.d("filepath3",data.getStringExtra("filePath"));
                        SharedPreferences.Editor editor = cosplayPictureFilepath.getEditor();
                        editor.putString("cosplayPictureFilepath", data.getStringExtra("filePath"));
                        editor.commit();
                    }
                    break;
            }
        }
    }

}
