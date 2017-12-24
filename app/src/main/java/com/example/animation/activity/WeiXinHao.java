package com.example.animation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.animation.R;

public class WeiXinHao extends AppCompatActivity {
    public static final String WEIXIN = "weixin";
    public static final String QQQUN = "qqqun";
    public static final String TYPE = "type";
    private String type;
    private TextView title;
    private TextView text;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei_xin_hao);
        title = (TextView) findViewById(R.id.weixin_title);
        image = (ImageView) findViewById(R.id.weixin_picture);
        text = (TextView) findViewById(R.id.weixin_text);
        Intent intent = getIntent();
        type = intent.getStringExtra(TYPE);

        if(type.equals(WEIXIN)){
            title.setText("公众号二维码");
            image.setImageResource(R.drawable.ic_person);
            text.setText("请截图并打开微信扫一扫\n选择相册加载二维码关注公众号\n每天都会有最新的动漫资讯和美图呦");
        }else if(type.equals(QQQUN)){
            title.setText("QQ群");
            image.setImageResource(R.drawable.ic_qun);
            text.setText("请截图并打开QQ扫一扫\n选择相册加载二维码\n或者直接加群556415474，让我们一起来闲聊吧");
        }


    }
}
