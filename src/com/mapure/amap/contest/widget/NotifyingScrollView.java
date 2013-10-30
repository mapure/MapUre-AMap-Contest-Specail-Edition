package com.mapure.amap.contest.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 *  
 * @author Izzy
 *
 * 重寫ScrollView，增加滾動狀態監聽器
 * 以實現根據滾動狀態設置ActionBar透明度的功能
 */
public class NotifyingScrollView extends ScrollView {
	private boolean mIsOverScrollEnabled = true;
	private GestureDetector mGestureDetector;

    //滾動監聽器
	public interface OnScrollChangedListener {
		void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
	}

	private OnScrollChangedListener mOnScrollChangedListener;

	public NotifyingScrollView(Context context) {
		super(context);
	}

	public NotifyingScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(context, new YScrollDetector());
        setFadingEdgeLength(0);
	}

	public NotifyingScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (mOnScrollChangedListener != null) {
			mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}

	public void setOnScrollChangedListener(OnScrollChangedListener listener) {
		mOnScrollChangedListener = listener;
	}

	public void setOverScrollEnabled(boolean enabled) {
		mIsOverScrollEnabled = enabled;
	}

	public boolean isOverScrollEnabled() {
		return mIsOverScrollEnabled;
	}

	//判斷是否允許過度滾動
	//若否，禁止過度滾動效果
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY,
				mIsOverScrollEnabled ? maxOverScrollX : 0,
				mIsOverScrollEnabled ? maxOverScrollY : 0, isTouchEvent);
	}

	//判斷觸摸事件
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	//判斷是否在Y軸方向的移動比在X軸方向上的大
	class YScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
            return Math.abs(distanceY) > Math.abs(distanceX);
        }
	}
}
