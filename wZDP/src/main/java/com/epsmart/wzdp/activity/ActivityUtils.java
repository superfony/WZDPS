package com.epsmart.wzdp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

public class ActivityUtils {

	/**
	 * 去除标题栏
	 * 
	 * @param mActivity
	 */
	public static void requestNotTitleBar(final Activity mActivity) {
		mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 全屏
	 * 
	 * @param mActivity
	 */
	public static void requestFullscreen(final Activity mActivity) {
		final Window window = mActivity.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.requestFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 屏幕亮度
	 * 
	 * @param mActivity
	 * @param pScreenBrightness
	 */
	public static void setScreenBrightness(final Activity mActivity,
			final float pScreenBrightness) {
		final Window window = mActivity.getWindow();
		final WindowManager.LayoutParams windowLayoutParams = window
				.getAttributes();
		windowLayoutParams.screenBrightness = pScreenBrightness;
		window.setAttributes(windowLayoutParams);
	}

	public static void keepScreenOn(final Activity mActivity) {
		mActivity.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}



	public static int dipToPixels(Context context, int dip) {
		Resources r = context.getResources();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip,
				r.getDisplayMetrics());
		return (int) px;
	}
}