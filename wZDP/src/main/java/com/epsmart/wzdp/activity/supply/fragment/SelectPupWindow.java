package com.epsmart.wzdp.activity.supply.fragment;

import com.epsmart.wzdp.R;
import com.epsmart.wzdp.util.DateTimePickerDialogUtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

public class SelectPupWindow extends PopupWindow {
	private View mview;
	private EditText purchaseorder;
	private Button select;

	public SelectPupWindow(Activity activity, OnClickListener itemsOnClick) {
		super(activity);
		LayoutInflater inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mview = inflater.inflate(R.layout.select_dialog, null);
		purchaseorder = (EditText) mview.findViewById(R.id.purchaseorder);
		select = (Button) mview.findViewById(R.id.select);
		
		
		
		// 设置按钮监听
		select.setOnClickListener(new OnClickListener() {

					public void onClick(View v) {
									//销毁弹出框
									dismiss();
								}
							});
				
		// 设置SelectPicPopupWindow的View
				this.setContentView(mview);
				// 设置SelectPicPopupWindow弹出窗体的宽
				this.setWidth(LayoutParams.FILL_PARENT);
				// 设置SelectPicPopupWindow弹出窗体的高
				this.setHeight(LayoutParams.WRAP_CONTENT);
				// 设置SelectPicPopupWindow弹出窗体可点击
				this.setFocusable(true);
				// 设置SelectPicPopupWindow弹出窗体动画效果
				this.setAnimationStyle(R.style.AnimationFade);
				// 实例化一个ColorDrawable颜色为半透明
				ColorDrawable dw = new ColorDrawable(0xb0000000);
				// 设置SelectPicPopupWindow弹出窗体的背景
				this.setBackgroundDrawable(dw);
				// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
				mview.setOnTouchListener(new OnTouchListener() {

					public boolean onTouch(View v, MotionEvent event) {

						int height = mview.findViewById(R.id.pup_pop).getTop();
						int y = (int) event.getY();
						if (event.getAction() == MotionEvent.ACTION_UP) {
							if (y < height) {
								dismiss();
							}
						}
						return true;
					}
				});
			}

			//年月日时分秒
			protected void selectDateAndTime(Activity activity,View v) {
				EditText et = (EditText) v;
				DateTimePickerDialogUtil dialog = new DateTimePickerDialogUtil(
						activity, null);
				dialog.dateTimePicKDialog(et);
			}
		}

