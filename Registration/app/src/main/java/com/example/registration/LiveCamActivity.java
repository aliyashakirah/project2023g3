package com.example.registration;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class LiveCamActivity extends AppCompatActivity {
    WebView superWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livecam);

        superWebView=findViewById(R.id.myWebView);

        superWebView.loadUrl("http://192.168.0.100:8000");
        superWebView.getSettings().setJavaScriptEnabled(true);

        superWebView.getSettings().setLoadWithOverviewMode(true);
        superWebView.getSettings().setUseWideViewPort(true);
        superWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        superWebView.setScrollbarFadingEnabled(true);

        superWebView.setWebViewClient(new WebViewClient());
        superWebView.setWebChromeClient(new WebChromeClient());
    }

    @Override
    public void onBackPressed()
    {
        if(superWebView.canGoBack())
        {
            superWebView.goBack();
        }
        else        {
            finish();
        }
    }
}