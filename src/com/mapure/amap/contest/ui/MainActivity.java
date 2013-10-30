package com.mapure.amap.contest.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.mapure.amap.contest.R;
import com.mapure.amap.contest.ui.fagment.*;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private WHUMapFragment mWHUMapFragment;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    private boolean doubleBackToExitPressedOnce = false;

    private String[] mFragmentTitles;

    public PullToRefreshAttacher getPullToRefreshAttacher() {
        return pullToRefreshAttacher;
    }

    private PullToRefreshAttacher pullToRefreshAttacher;

    private final int SEARCH_POI_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pullToRefreshAttacher = PullToRefreshAttacher.get(this);

        mTitle = mDrawerTitle = getTitle();
        mFragmentTitles = getResources()
                .getStringArray(R.array.fragments_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        HashMap<String, String> map1 = new HashMap<>();
        HashMap<String, String> map2 = new HashMap<>();
        HashMap<String, String> map3 = new HashMap<>();
        HashMap<String, String> map4 = new HashMap<>();
        HashMap<String, String> map5 = new HashMap<>();
        HashMap<String, String> map6 = new HashMap<>();
        HashMap<String, String> map7 = new HashMap<>();

        map1.put("title", mFragmentTitles[0]);
        map2.put("title", mFragmentTitles[1]);
        map3.put("title", mFragmentTitles[2]);
        map4.put("title", mFragmentTitles[3]);
        map5.put("title", mFragmentTitles[4]);
        map6.put("title", mFragmentTitles[5]);
        map7.put("title", mFragmentTitles[6]);

        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);
        list.add(map6);
        list.add(map7);

        SimpleAdapter listAdapter = new SimpleAdapter(this, list,
                R.layout.drawer_list_item, new String[]{"title"},
                new int[]{R.id.title});

        mDrawerList.setAdapter(listAdapter);

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(1);
        }

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

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments

        Fragment newFragment = null;
        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();

        switch (position) {
            case 0:
                mWHUMapFragment = new WHUMapFragment();
                newFragment = mWHUMapFragment;
                break;
            case 1:
                newFragment = new MainPageFragment();
                break;
            case 2:
                newFragment = new ImpressionFragment();
                break;
            case 3:
                newFragment = new AlumnusFragment();
                break;
            case 4:
                newFragment = new WishFragment();
                break;
            case 5:
                newFragment = new CountDownFragment();
                break;
            case 6:
                newFragment = new AboutFragment();
                break;
        }

        transaction.replace(R.id.content_frame, newFragment).commitAllowingStateLoss();

        mDrawerList.setItemChecked(position, true);
        setTitle(mFragmentTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
                return true;

            case R.id.action_search:
                Intent intent = new Intent();
                intent.setClass(this, SearchActivity.class);
                startActivityForResult(intent, SEARCH_POI_REQUEST_CODE);
                return true;
            case R.id.action_share:
                FragmentManager fm = getSupportFragmentManager();
                SharingFragment sf = SharingFragment.getInstance();
                sf.show(fm, "分享到？");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast exitToast = Toast.makeText(this,
                    "再按一次以退出软件", Toast.LENGTH_SHORT);
            exitToast.setDuration(2000);
            exitToast.show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        if (requestCode == SEARCH_POI_REQUEST_CODE && resultCode == RESULT_OK) {
            selectItem(0);

            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {

                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                    mWHUMapFragment.onActivityResult(requestCode, resultCode, data);
                }
            }.execute();
        }

    }
}
