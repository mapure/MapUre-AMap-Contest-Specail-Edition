package com.mapure.amap.contest.ui.fagment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.astuetz.viewpager.extensions.PagerSlidingTabStrip;
import com.mapure.amap.contest.R;

public class MainPageFragment extends Fragment {

    private ViewPager mainPagePager;
    private ArticlePagerAdapter articlePagerAdapter;
    private PagerSlidingTabStrip mainPageTabs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page, null);

        mainPageTabs = (PagerSlidingTabStrip) view.findViewById(R.id.main_page_tabs);
        mainPagePager = (ViewPager) view.findViewById(R.id.main_page_pager);

        articlePagerAdapter = new ArticlePagerAdapter(
                getChildFragmentManager());

        new SetAdapterTask().execute();

        return view;
    }

    private class ArticlePagerAdapter extends FragmentPagerAdapter {

        private String[] TITLES = {"要闻关注", "媒体聚焦", "通知公告", "“校庆日”前后重点活动一览表"};

        public ArticlePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public Fragment getItem(int position) {

            Bundle b;
            NewsListFragment newsListFragment;

            switch (position) {

                case 0:
                    return new ImageNewsListFragment();

                case 1:
                    b = new Bundle();
                    b.putString("url", "http://wdxq.cnhubei.com/html/huodong/mtjj/");
                    Fragment newFragment = new SmallNewsListFragment();
                    newFragment.setArguments(b);
                    return newFragment;

                case 2:
                    b = new Bundle();
                    b.putString("url", "http://wdxq.cnhubei.com/html/huodong/TZGG/");
                    newsListFragment = new NewsListFragment();
                    newsListFragment.setArguments(b);
                    return newsListFragment;

                case 3:
                    return new TimeTableFragment();
            }
            return null;
        }

    }

    private class SetAdapterTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mainPagePager.setAdapter(articlePagerAdapter);

            final int pageMargin = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                    .getDisplayMetrics());
            mainPagePager.setPageMargin(pageMargin);
            mainPagePager.setOffscreenPageLimit(0);
            mainPageTabs.setViewPager(mainPagePager);
            mainPageTabs.setIndicatorColor(Color.parseColor("#FFADB5"));
        }
    }
}
