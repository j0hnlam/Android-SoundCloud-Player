
package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;

public class DBScrollView extends ScrollView{
	
	public static final String TAG = DBScrollView.class.getSimpleName();
	
	private OnLastItemVisibleListener mOnLastItemVisibleListener;
	
	public DBScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DBScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DBScrollView(Context context) {
		super(context);
	}
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		if(getChildCount()>0){
			View view = (View) getChildAt(getChildCount()-1);
			 int diff = (view.getBottom()-(getHeight()+getScrollY()));
			 Log.d(TAG, "================>diff="+diff);
			 if(diff<=0){
				 if(mOnLastItemVisibleListener!=null){
					 mOnLastItemVisibleListener.onLastItemVisible();
				 }
			 }
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}
	public void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
		mOnLastItemVisibleListener = listener;
	}
	
}
