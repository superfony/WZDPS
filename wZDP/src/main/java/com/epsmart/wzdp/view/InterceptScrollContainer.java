package com.epsmart.wzdp.view;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/*
 * 
 * 阻止 拦截 ontouch事件传�?给其子控�?
 * */
public class InterceptScrollContainer extends LinearLayout {

	public InterceptScrollContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public InterceptScrollContainer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		//return super.onInterceptTouchEvent(ev);
		Log.i("pdwy","ScrollContainer onInterceptTouchEvent");
		return true;
	}
	
}