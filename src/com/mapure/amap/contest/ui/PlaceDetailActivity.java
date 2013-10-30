package com.mapure.amap.contest.ui;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.mapure.amap.contest.R;
import com.mapure.amap.contest.ui.fagment.SharingFragment;
import com.mapure.amap.contest.util.ConstantUtils;
import com.mapure.amap.contest.widget.NotifyingScrollView;

public class PlaceDetailActivity extends FragmentActivity {

    private Drawable mActionBarBackgroundDrawable;
    private Drawable.Callback mDrawableCallback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            getActionBar().setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
        }
    };

    private NotifyingScrollView.OnScrollChangedListener mOnScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        public void onScrollChanged(ScrollView who, int l, int t, int oldl,
                                    int oldt) {
            final int headerHeight = findViewById(R.id.place_detail_header_img).getHeight()
                    - getActionBar().getHeight();
            float alphaRatio = (float) Math.min(Math.max(t, 0), headerHeight)
                    / headerHeight;
            final int newAlpha = (int) (alphaRatio * 255);
            mActionBarBackgroundDrawable.setAlpha(newAlpha);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_place_detail);


        mActionBarBackgroundDrawable = getResources().getDrawable(
                R.drawable.ab_solid_mapure);
        mActionBarBackgroundDrawable.setAlpha(0);
        getActionBar().setBackgroundDrawable(
                mActionBarBackgroundDrawable);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        //若系統版本低於Jelly Bean MR1
        //將ActionBar註冊至Drawable的回調
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
        }

        NotifyingScrollView mNotifyingScrollView = ((NotifyingScrollView) findViewById(R.id.article_detail_scroll_view));
        mNotifyingScrollView
                .setOnScrollChangedListener(mOnScrollChangedListener);
        mNotifyingScrollView.setOverScrollEnabled(false);

        ImageView image = (ImageView) findViewById(R.id.place_detail_header_img);

        TextView title = (TextView) findViewById(R.id.place_detail_title);
        TextView content = (TextView) findViewById(R.id.content);

        int type = getIntent().getIntExtra("type", 0);

        switch (type) {
            case ConstantUtils.TYPE_WHU_DOOR:
                image.setImageDrawable(getResources().getDrawable(R.drawable.whu_door));
                title.setText(getResources().getString(R.string.whu_door));
                content.setText(getResources().getString(R.string.whu_door_intro));
                break;
            case ConstantUtils.TYPE_SAKURA_ROAD:
                image.setImageDrawable(getResources().getDrawable(R.drawable.sakura_road));
                title.setText(getResources().getString(R.string.sakura_road));
                content.setText(getResources().getString(R.string.sakura_road_intro));
                break;
            case ConstantUtils.TYPE_HISTORY_DEPT_CENTER:
                image.setImageDrawable(getResources().getDrawable(R.drawable.history_dept));
                title.setText(getResources().getString(R.string.history_dept));
                content.setText(getResources().getString(R.string.history_dept_intro));
                break;
            case ConstantUtils.TYPE_SQ_SPORT_CENTER:
                image.setImageDrawable(getResources().getDrawable(R.drawable.sq_sport_center));
                title.setText(getResources().getString(R.string.sq_sport_center));
                content.setText(getResources().getString(R.string.sq_sport_center_intro));
                break;
            case ConstantUtils.TYPE_SIX_ONE_PAVILION:
                image.setImageDrawable(getResources().getDrawable(R.drawable.six_one_pavilion));
                title.setText(getResources().getString(R.string.six_one_pavilion));
                content.setText(getResources().getString(R.string.six_one_pavilion_intro));
                break;
            case ConstantUtils.TYPE_NEW_LIBRARY:
                image.setImageDrawable(getResources().getDrawable(R.drawable.new_library));
                title.setText(getResources().getString(R.string.new_library));
                content.setText(getResources().getString(R.string.new_library_intro));
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.place_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_share_place:
                FragmentManager fm = getSupportFragmentManager();
                SharingFragment sf = SharingFragment.getInstance();
                sf.show(fm, "分享到？");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
