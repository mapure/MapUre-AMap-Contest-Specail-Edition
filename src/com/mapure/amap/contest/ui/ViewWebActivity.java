package com.mapure.amap.contest.ui;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.webkit.WebView;
import com.mapure.amap.contest.R;

public class ViewWebActivity extends Activity {

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_web);

        url = getIntent().getStringExtra("url");

        WebView webView = (WebView) findViewById(R.id.view_web_browser);

        webView.loadUrl(url);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Drawable mActionBarBackgroundDrawable = getResources().getDrawable(
                R.drawable.ab_solid_mapure);
        mActionBarBackgroundDrawable.setAlpha(255);
        getActionBar().setBackgroundDrawable(
                mActionBarBackgroundDrawable);
    }
}
