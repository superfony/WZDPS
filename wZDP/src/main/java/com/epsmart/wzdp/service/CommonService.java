package com.epsmart.wzdp.service;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.activity.CommonActivity;
import com.epsmart.wzdp.common.Common;
import com.epsmart.wzdp.util.AndroidUtil;

@SuppressLint("NewApi")
public class CommonService {

	protected CommonActivity activity;
	protected View view;

	public CommonService(CommonActivity activity, View view) {
		super();
		this.activity = activity;
		this.view = view;
	}

	/**
	 * 点击左侧导航栏时，改变点击的条目背景颜色
	 * 
	 * @param clickedView
	 */
	public void setLeftTextViewBackgroundColor(View clickedView, int tag) {

//		Constants.supplyTopTextView.setBackgroundDrawable(activity.getResources().getDrawable(
//				R.drawable.selector_sidebar_button));
//		Constants.supplyTopTextView.setFocusable(true);
//		Constants.supplyTopTextView.setClickable(true);
//
//		clickedView.setBackgroundDrawable(activity.getResources().getDrawable(
//				R.drawable.sidebar_button_hover));
//		clickedView.setFocusable(false);
//		clickedView.setClickable(false);
//		Constants.supplyTopTextView = clickedView;
	}

	/**
	 * 当Fragment改变时，改变左侧对应条目的背景颜色,并实现ScrollView自动滚动到焦点位置
	 * 
	 * @param clickedView
	 */
	public void setLeftTextViewBackgroundColorWhenFragmentChanged(
			String leftTextViewTag) {
		Fragment fragmentLeft = activity.getSupportFragmentManager().findFragmentById(
				R.id.fragment_left);
		View leftView = fragmentLeft.getView();
		View clickedLeftTextView = leftView.findViewWithTag(leftTextViewTag);
		ScrollView scrollView = (ScrollView) leftView
				.findViewById(R.id.scrollView_left);
		clickedLeftTextView.setFocusable(true);
		clickedLeftTextView.requestFocus();// view获取焦点
		if (leftTextViewTag.equals("0") || leftTextViewTag.equals("1")
				|| leftTextViewTag.equals("2"))
			scrollView.fullScroll(ScrollView.FOCUS_FORWARD);// 向前滚动
		else
			scrollView.fullScroll(ScrollView.FOCUS_DOWN);// 向后滚动
		Log.i(getClass().toString(), "clickedLeftTextView:"
				+ clickedLeftTextView);
		setLeftTextViewBackgroundColor(clickedLeftTextView,Integer.parseInt(leftTextViewTag));
	}

	/**
	 * 替换右侧Fragment
	 * 
	 * @param fragment
	 */
	public void replaceRightFragment(Fragment fragment) {
		replaceRightFragment(fragment, false);
	}

	public void replaceRightFragment(Fragment fragment, boolean popBackStack) {
		Common.replaceRightFragment(activity, fragment, popBackStack, 0);
	}

	/**
	 * 根据屏幕宽度计算得到view的宽度（用于适配GridLayout）
	 * 
	 * @param columnCount
	 *            要显示的GridView的列数
	 * @return view宽度（px）
	 */
	public int getViewWidthByWindowWidth(int columnCount) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int windth = (int) ((dm.widthPixels - getSidebarWidth() - (20 + 10 * columnCount)
				* density) / columnCount);
		Log.i(activity.getPackageName(), "===========计算得到的view-width:" + windth);
		return windth;
	}

	/**
	 * 获取左边导航栏的宽度
	 * 
	 * @return
	 */
	private int getSidebarWidth() {
		View view = activity.findViewById(R.id.fragment_left_container);
		return AndroidUtil.getViewWidth(view);
	}

}
