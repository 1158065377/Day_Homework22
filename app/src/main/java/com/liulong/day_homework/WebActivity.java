package com.liulong.day_homework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Administrator on 2016/10/15.
 */

public class WebActivity extends Activity{

    private WebView wb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        wb = (WebView) findViewById(R.id.webview);
        Intent i = getIntent();
        String web = i.getStringExtra("web");
        WebSettings se = wb.getSettings();
        se.setJavaScriptEnabled(true);
        se.setDomStorageEnabled(true);
        wb.loadUrl(web);
        wb.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK&&wb.canGoBack()){
            wb.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
