package com.claudiawu.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.claudiawu.nytimessearch.R;
import com.claudiawu.nytimessearch.models.Article;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleActivity extends AppCompatActivity {

    @BindView(R.id.wvArticle) WebView webView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Article article = Parcels.unwrap(getIntent().getParcelableExtra("article"));
        WebView webView = (WebView) findViewById(R.id.wvArticle);

        pb = (ProgressBar) findViewById(R.id.pbLoading);
        pb.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorProgress),
                android.graphics.PorterDuff.Mode.SRC_IN);
        pb.setVisibility(ProgressBar.VISIBLE);

        if (webView != null) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    pb.setVisibility(ProgressBar.INVISIBLE);
                    return true;
                }
                //pb.setVisibility(ProgressBar.INVISIBLE);
            });

            String url = article.getWebUrl();
            //pb.setVisibility(ProgressBar.INVISIBLE);
            webView.loadUrl(url);
            //pb.setVisibility(ProgressBar.INVISIBLE);
        }
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);
        ShareActionProvider miShare = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");

        // get reference to WebView
        WebView webView = (WebView) findViewById(R.id.wvArticle);
        // pass in the URL currently being used by the WebView
        if (webView != null) {
            shareIntent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
        }

        miShare.setShareIntent(shareIntent);
        return super.onCreateOptionsMenu(menu);
    }

}
