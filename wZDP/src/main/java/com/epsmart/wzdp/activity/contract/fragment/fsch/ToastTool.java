package com.epsmart.wzdp.activity.contract.fragment.fsch;


import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 弹出Toast的一个工具类,这里主要是增加了对系统Toast背景的修改
 * 
 * @author zhangdq
 * 
 */
public class ToastTool {

	public static void toast(Context context, String msg) {
		toast(context, msg, Toast.LENGTH_SHORT);
	}

	public static void toast(Context context, String msg, int duration) {
		Toast toast = Toast.makeText(context, null, duration);
		LinearLayout layout = (LinearLayout) toast.getView();
		/*
		 * layout.setLayoutParams(new WindowManager.LayoutParams(10000,
		 * android.view.WindowManager.LayoutParams.WRAP_CONTENT,
		 * WindowManager.LayoutParams.TYPE_TOAST,
		 * WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
		 * WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
		 * WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
		 * PixelFormat.TRANSLUCENT));
		 */
		// layout.setBackgroundResource(R.drawable.bg_msg_toast);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setGravity(Gravity.CENTER_VERTICAL);
		TextView tv = new TextView(context);
		tv.setBackgroundColor(context.getResources().getColor(
				android.R.color.transparent));
		tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		tv.setGravity(Gravity.CENTER_VERTICAL);
		tv.setTextColor(context.getResources().getColor(android.R.color.white));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		tv.setPadding(0, 0, 0, 0);
		tv.setText(msg);
		layout.addView(tv);
		toast.show();
	}
}
