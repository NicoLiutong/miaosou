package com.example.animation.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.animation.BuildConfig;
import com.example.animation.R;

/**
 * Created by 刘通 on 2017/10/15.
 */

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Button button = (Button) findViewById(R.id.about_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getFragmentManager().beginTransaction().replace(R.id.ll_fragment_container,new AboutFragment()).commit();
    }

    public static class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener{

        private Preference mVersion;
        private Preference mShare;
        private Preference mGithub;
        private Preference mWeiXinHao;
        private Preference mQQQun;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_about);
            mVersion = findPreference("version");
            mShare = findPreference("share");
            mGithub = findPreference("github");
            mWeiXinHao = findPreference("weixingongzhonghao");
            mQQQun = findPreference("qqqun");
            mVersion.setSummary("v " + BuildConfig.VERSION_NAME);
            setListener();
        }

        private void setListener(){
            mShare.setOnPreferenceClickListener(this);
            mGithub.setOnPreferenceClickListener(this);
            mWeiXinHao.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if(preference == mShare){
                share();
                return true;
            }else if(preference == mGithub){
                openUrl(preference.getSummary().toString());
                return true;
            }else if (preference == mWeiXinHao){
                Intent intent = new Intent(AboutFragment.this.getActivity(),WeiXinHao.class);
                intent.putExtra(WeiXinHao.TYPE,WeiXinHao.WEIXIN);
                AboutFragment.this.getActivity().startActivity(intent);
            }else if (preference == mQQQun){
                Intent intent = new Intent(AboutFragment.this.getActivity(),WeiXinHao.class);
                intent.putExtra(WeiXinHao.TYPE,WeiXinHao.QQQUN);
                AboutFragment.this.getActivity().startActivity(intent);
            }
            return false;
        }

        private void share(){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,getString(R.string.share_app,"喵搜"));
            startActivity(Intent.createChooser(intent,getString(R.string.share)));
        }

        private void openUrl(String url){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
    }

}
