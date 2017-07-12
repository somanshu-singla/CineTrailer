package com.example.somanshu.cinetrailer;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class YoutubeWeb extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_web);
        Movie movie=getIntent().getExtras().getParcelable("movie");
        WebView webView=(WebView)findViewById(R.id.web);
        String url=movie.getTrailer_path();
        webView.getSettings().setJavaScriptEnabled(true);
        setTitle(movie.getOriginal_title());
        webView.setWebViewClient(new WebViewClient(){});
        webView.loadUrl(url);
    }
}

