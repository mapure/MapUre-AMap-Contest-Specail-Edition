package com.mapure.amap.contest.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import com.mapure.amap.contest.R;

public class SplashActivity extends Activity {

    private static final int MSG_STOP_SPLASH = 0;
    private static final int SPLASH_TIME = 2000;

    private final Handler splashHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Intent localIntent = new Intent(SplashActivity.this,
                        MainActivity.class);
                SplashActivity.this.startActivity(localIntent);
                SplashActivity.this.overridePendingTransition(
                        R.anim.splash_fade_in, R.anim.splash_fade);
                SplashActivity.this.finish();
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);

        Message msg = new Message();
        msg.what = MSG_STOP_SPLASH;
        this.splashHandler.sendMessageDelayed(msg, SPLASH_TIME);
    }

    @Override
    public void onBackPressed() {

    }
}
