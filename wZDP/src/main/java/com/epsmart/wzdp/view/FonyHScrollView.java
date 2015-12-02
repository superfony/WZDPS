package com.epsmart.wzdp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
/**
 * ʵϰ����HorizontalScrollView ͬʱ����
 * @author fony
 */

public class FonyHScrollView extends HorizontalScrollView {
	private View mView; 
	public FonyHScrollView(Context context) {
		super(context);
	}

	public FonyHScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public FonyHScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	   @Override  
	    protected void onScrollChanged(int l, int t, int oldl, int oldt) {  
	        super.onScrollChanged(l, t, oldl, oldt);  
	        if(mView!=null){  
	            mView.scrollTo(l, t);  
	        }  
	    }  
	
	  public void setAnotherView(View v){  
	        this.mView=v;  
	    }  

}

/** �÷�
 * scrollView0=(SyncHorizontalScrollView)findViewById(R.id.scrollView0); 
scrollView1=(SyncHorizontalScrollView)findViewById(R.id.scrollView1); 
scrollView0.setAnotherView(scrollView1); 
scrollView1.setAnotherView(scrollView0); 
 */
