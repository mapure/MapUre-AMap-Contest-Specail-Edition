package com.mapure.amap.contest.ui.fagment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.mapure.amap.contest.R;
import com.viewpagerindicator.IconPagerAdapter;
import com.viewpagerindicator.LinePageIndicator;

public class AlumnusPagerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_impression, null);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.history_pager);
        viewPager.setAdapter(new MyViewPagerAdapter(getActivity()));
        LinePageIndicator mIndicator = (LinePageIndicator) view
                .findViewById(R.id.history_indicator);
        mIndicator.setViewPager(viewPager);

        return view;
    }

    public class MyViewPagerAdapter extends PagerAdapter implements
            IconPagerAdapter {

        Context mContext;

        private int[] images = {
                R.drawable.ailuming,
                R.drawable.chili,
                R.drawable.leijun,
                R.drawable.xiongzhaozheng,
                R.drawable.yizhongtian,
                R.drawable.yushan,
        };

        public MyViewPagerAdapter(Context context) {
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            imageView.setImageResource(images[position]);

            container.addView(imageView, 0);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((ImageView) object);
        }

        @Override
        public int getIconResId(int index) {
            return images[index % images.length];
        }

    }
}
