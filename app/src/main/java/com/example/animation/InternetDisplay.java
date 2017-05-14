package com.example.animation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.animation.Fragment.AnimationFragment;

public class InternetDisplay extends AppCompatActivity {

    private String animationName;

    private String animationUrl;

    private TextView internetTitle;

    private Button internrtClose;

    private Button internetGotoInternet;

    private ProgressBar webBar;

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_display);
        final Intent intent = getIntent();
        animationName = intent.getStringExtra(AnimationFragment.ANIMATION_NAME);
        animationUrl = intent.getStringExtra(AnimationFragment.ANIMATION_URL);
        internetTitle = (TextView) findViewById(R.id.internet_title);
        internrtClose = (Button) findViewById(R.id.internet_close);
        internetGotoInternet = (Button) findViewById(R.id.internet_open);

        webView = (WebView) findViewById(R.id.web_view);
        webBar = (ProgressBar) findViewById(R.id.web_bar);
        webBar.getProgressDrawable().setColorFilter(Color.RED,
                android.graphics.PorterDuff.Mode.SRC_IN);

        WebSettings settings = webView.getSettings();

        //设置自适应屏幕，两者合用
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        settings.setJavaScriptEnabled(true);  //支持js

        //缩放操作
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        settings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        settings.setAllowFileAccess(true); //设置可以访问文件
        settings.setDefaultTextEncodingName("utf-8");//设置编码格式
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);


        WebViewClient webViewClient = new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("url",url);
                if(url.split("\\:")[0].equals("magnet")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                /*String uri = request.getUrl().getScheme().trim();*/
                //Log.d("uri","1111");
                /*if(uri.split("\\:")[0].equals("magnet")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(webView.getUrl()));
                    Log.d("uri",uri.split("\\:")[0]);
                    startActivity(intent);
                    return false;
                }else {*/
                    return super.shouldOverrideUrlLoading(view, request);
                //}

            }


        };

        WebChromeClient webChromeClient = new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                webBar.setProgress(newProgress);

                if (newProgress == 100) {
                    webBar.setVisibility(View.GONE);
                } else {
                    webBar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("标题在这里");
                internetTitle.setText(title);
            }
        };


        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        webView.loadUrl(animationUrl);

        internrtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        internetGotoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(webView.getUrl()));
                Log.d("url",webView.getUrl());
                startActivity(intent);
            }
        });

        }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}

