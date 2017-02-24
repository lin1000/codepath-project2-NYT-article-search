package com.codepath.week2assignment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;

/**
 * Created by lin1000 on 2017/2/24.
 */

public class ArticleWebViewActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar tlToolbar;
    @BindView(R.id.wvArticle) WebView wvArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_webview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wvArticle = (WebView) findViewById(R.id.wvArticle);
        Log.d(this.getClass().getName(),"wvArticle="+wvArticle);

        String url = getIntent().getStringExtra("url");

        wvArticle.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(view.getUrl());
                return true;
            }
        });
        wvArticle.loadUrl(url);
    }



}