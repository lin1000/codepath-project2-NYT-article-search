package com.codepath.week2assignment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lin1000 on 2017/2/24.
 */

public class ArticleWebViewActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar tlToolbar;
    @BindView(R.id.wvArticle) WebView wvArticle;

    private ShareActionProvider miShareAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_webview);
        ButterKnife.bind(this);

        setSupportActionBar(tlToolbar);
        getSupportActionBar().setLogo(R.drawable.ic_cloud);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_webview, menu);

        // Together with android.support.v7.widget.ShareActionProvider on menu_webview.xml to implement the share feture
        MenuItem item = menu.findItem(R.id.menu_item_share);
        miShareAction = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());
        miShareAction.setShareIntent(shareIntent);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Log.d(this.getClass().getName(), "item.getItemId()="+ item.getItemId());

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_item_share) {
            Log.d(this.getClass().getName()," R.id.menu_item_share clicked");
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, wvArticle.getUrl());
            startActivity(Intent.createChooser(shareIntent, "Share link using"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}